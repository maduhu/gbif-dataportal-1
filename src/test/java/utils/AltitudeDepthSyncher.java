package utils;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.gbif.portal.harvest.workflow.activity.occurrence.OccurrenceRecordSynchroniserActivity;
import org.gbif.portal.model.OccurrenceRecord;
import org.gbif.portal.model.RawOccurrenceRecord;
import org.gbif.portal.util.log.GbifLogMessage;
import org.gbif.portal.util.log.GbifLogUtils;
import org.gbif.portal.util.log.LogEvent;
import org.gbif.portal.util.log.LogGroup;
import org.gbif.portal.util.workflow.MapContext;
import org.gbif.portal.util.workflow.ProcessContext;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * Synchs up Altitude and Depth from ROR -> OR
 * 
 * For updates against the entire dataset:
 *
 * 

create table temp_or_altitude_depth (
       id int unsigned not null
     , data_resource_id smallint unsigned not null
     , min_altitude varchar(50)     
     , max_altitude varchar(50)
     , altitude_precision varchar(50)
     , min_depth varchar(50)
     , max_depth varchar(50)
     , depth_precision varchar(50)
     , primary key (id)
) engine=myisam;
create index ix_dr_alt_dep on temp_or_altitude_depth (data_resource_id);

insert into temp_or_altitude_depth
select id, data_resource_id, min_altitude, max_altitude, altitude_precision, min_depth, max_depth, depth_precision
from raw_occurrence_record 
where (max_altitude is not null or min_altitude is not null) 
or (min_depth is not null or max_depth is not null);

 * 
 * 
 * @author davejmartin
 */
public class AltitudeDepthSyncher {
	
	protected static Log logger = LogFactory.getLog(AltitudeDepthSyncher.class);

	private ApplicationContext context;
	private DataSource dataSource;
	private JdbcTemplate jdbcTemplate;
	private int increments = 10000;
	private OccurrenceRecordSynchroniserActivity occurrenceRecordSynchroniserActivity;
	protected GbifLogUtils gbifLogUtils;
	
	protected long startDataResourceId = -1l;
	
	private void init() {
		String[] locations = {
				"classpath*:/**/applicationContext-*.xml",
				"classpath*:**/applicationContext-*.xml",
				"classpath*:org/gbif/portal/**/applicationContext-*.xml"
		};
		this.context = new ClassPathXmlApplicationContext(locations);
		
		this.dataSource = (DataSource) context.getBean("dataSource");
		this.jdbcTemplate = new JdbcTemplate(dataSource);
		this.occurrenceRecordSynchroniserActivity = (OccurrenceRecordSynchroniserActivity) context.getBean("occurrenceRecordSynchroniserActivity");
		this.gbifLogUtils = (GbifLogUtils) context.getBean("gbifLogUtils");
	}

	/**
	 * Process all datasets.
	 */
	public void run(){
		List<Long> ids = this.jdbcTemplate.queryForList("select id from data_resource where deleted IS NULL", Long.class);
		int i=0;
		if(startDataResourceId>0){
			i = ids.indexOf(startDataResourceId);
		}
		for(; i<ids.size(); i++){
			processResource(ids.get(i));
		}
	}
	
	/**
	 * Process an entire dataset.
	 * 
	 * @param dataResourceId
	 * @param date
	 */
	private void processResource(Long dataResourceId) {

		logger.info("Processing resource id: "+dataResourceId);
		//start a log group
		LogGroup logGroup = this.gbifLogUtils.startLogGroup();
		ProcessContext processContext = new MapContext();
		processContext.put("dataResourceId", dataResourceId);
		processContext.put(this.occurrenceRecordSynchroniserActivity.getContextKeyLogGroup(), logGroup);

		GbifLogMessage message = gbifLogUtils.createGbifLogMessage(processContext, LogEvent.EXTRACT_BEGIN,"Updating altitude / depth parameters only");			
		logger.info(message);		

		int counter = 0;
		 
		List<Map<String, Object>> records = getRecords(dataResourceId, counter);
		
		do {
			final List<OccurrenceRecord> toUpdate = new ArrayList<OccurrenceRecord>(records.size());
			logger.info("Processing "+dataResourceId+", starting at "+counter);
			
			for(Map<String, Object> record: records){
				
				RawOccurrenceRecord ror = new RawOccurrenceRecord();
				
				Number idAsNumber = (Number) record.get("id");
				ror.setId(idAsNumber.longValue());
				ror.setMinAltitude((String)record.get("min_altitude"));
				ror.setMaxAltitude((String)record.get("max_altitude"));
				ror.setAltitudePrecision((String)record.get("altitude_precision"));
				ror.setMinDepth((String)record.get("min_depth"));
				ror.setMaxDepth((String)record.get("max_depth"));
				ror.setDepthPrecision((String)record.get("depth_precision"));
				
				OccurrenceRecord or = new OccurrenceRecord();
				or.setId(idAsNumber.longValue());
				or.setGeospatialIssue((Integer)record.get("geospatial_issue"));
				
				this.occurrenceRecordSynchroniserActivity.setAltitudeInMetres(processContext, ror, or);
				this.occurrenceRecordSynchroniserActivity.setDepthInCentimetres(processContext, ror, or);
				
				toUpdate.add(or);
			}

			//sync to database using a batch update
			if(!toUpdate.isEmpty()){
				this.jdbcTemplate.batchUpdate(
						"UPDATE occurrence_record SET altitude_metres=?, depth_centimetres=?, geospatial_issue=? WHERE id=?",  
						new BatchPreparedStatementSetter(){
					public int getBatchSize() {
			      return toUpdate.size();
		      }
					public void setValues(PreparedStatement stmt, int index) throws SQLException {
						OccurrenceRecord or = toUpdate.get(index);
						if(or.getAltitudeInMetres()!=null){
							stmt.setObject(1, or.getAltitudeInMetres());
						} else {
							stmt.setNull(1, Types.INTEGER);
						}
						if(or.getDepthInCentimetres()!=null){
							stmt.setObject(2, or.getDepthInCentimetres());
						} else {
							stmt.setNull(2, Types.INTEGER);
						}
						stmt.setObject(3, or.getGeospatialIssue());
						stmt.setLong(4, or.getId());
		      }
				});			
			}
			
			//get next batch
			counter+=increments;
			records = getRecords(dataResourceId, counter);
			
		} while (records!=null && !records.isEmpty());
		
		//end log group
		message = gbifLogUtils.createGbifLogMessage(processContext, LogEvent.EXTRACT_END,"Completed updating altitude / depth parameters");			
		logger.info(message);			
		
		this.gbifLogUtils.endLogGroup(logGroup);
  }

	private List<Map<String, Object>> getRecords(long dataResourceId, int counter) {
	  List<Map<String,Object>> records = this.jdbcTemplate.queryForList(
				"select ror.id, ror.min_altitude, ror.max_altitude, ror.altitude_precision, ror.min_depth, ror.max_depth, ror.depth_precision, oc.geospatial_issue " +
				"FROM temp_or_altitude_depth ror " +
				"INNER JOIN occurrence_record oc ON oc.id=ror.id " +
				"WHERE ror.data_resource_id=? " +
				"LIMIT ?,?", new Object[]{dataResourceId, counter, increments});
	  return records;
  }

	/**
	 * Run this launcher.
	 * 
	 * @param args supports one argument, the dataset to start at.
	 */
	public static void main(String[] args) {
		try {
			
			AltitudeDepthSyncher launcher = new AltitudeDepthSyncher();
			if(args.length==1){
				try{
					launcher.setStartDataResourceId(Long.parseLong(args[0]));
				} catch(Exception e){
					e.printStackTrace();
				}
			}
			launcher.init();
			launcher.run();
		} catch (Exception e){
			e.printStackTrace();
		}
		System.exit(1);
	}

	/**
   * @param context the context to set
   */
  public void setContext(ApplicationContext context) {
  	this.context = context;
  }

	/**
   * @param logger the logger to set
   */
  public static void setLogger(Log logger) {
  	AltitudeDepthSyncher.logger = logger;
  }

	/**
   * @param dataSource the dataSource to set
   */
  public void setDataSource(DataSource dataSource) {
  	this.dataSource = dataSource;
  }

	/**
   * @param jdbcTemplate the jdbcTemplate to set
   */
  public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
  	this.jdbcTemplate = jdbcTemplate;
  }

	/**
   * @param increments the increments to set
   */
  public void setIncrements(int increments) {
  	this.increments = increments;
  }

	/**
   * @param occurrenceRecordSynchroniserActivity the occurrenceRecordSynchroniserActivity to set
   */
  public void setOccurrenceRecordSynchroniserActivity(
      OccurrenceRecordSynchroniserActivity occurrenceRecordSynchroniserActivity) {
  	this.occurrenceRecordSynchroniserActivity = occurrenceRecordSynchroniserActivity;
  }

	/**
   * @param gbifLogUtils the gbifLogUtils to set
   */
  public void setGbifLogUtils(GbifLogUtils gbifLogUtils) {
  	this.gbifLogUtils = gbifLogUtils;
  }

	/**
   * @param startDataResourceId the startDataResourceId to set
   */
  public void setStartDataResourceId(long startDataResourceId) {
  	this.startDataResourceId = startDataResourceId;
  }		
}