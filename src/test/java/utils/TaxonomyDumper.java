/**
 * 
 */
package utils;

import java.io.FileWriter;
import java.io.Writer;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.gbif.portal.dao.RelationshipAssertionDAO;
import org.gbif.portal.dao.TaxonConceptDAO;
import org.gbif.portal.model.RelationshipAssertion;
import org.gbif.portal.model.TaxonConcept;
import org.gbif.portal.model.TaxonConceptLite;
import org.gbif.portal.util.request.TemplateUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * Utility for dumping a taxonomy to a file.
 * 
 * @author davejmartin
 */
public class TaxonomyDumper {

	private ApplicationContext context;
	private VelocityContext velocityContext;
	private Writer writer;
	private String templateLocation = "utils/taxonDump.vm";
	private String headerTemplateLocation = "utils/taxonDumpHeader.vm";
	private String outputFileName = "/tmp/taxonomy_dump.txt";
	private String speciesRootUrl = "http://data.gbif.org/species/";
	private TemplateUtils templateUtils = new TemplateUtils();
	protected static Logger logger = Logger.getLogger(TaxonomyDumper.class);
	protected boolean includeHeader = false;
	protected static Map<Integer, String> ranks;

	/**
	 * Initialise classpath
	 */
	public void init() {
		String[] locations = { "classpath*:/**/applicationContext-*.xml", 
				"classpath*:**/applicationContext-*.xml",
				"classpath*:org/gbif/portal/**/applicationContext-*.xml" };
		this.context = new ClassPathXmlApplicationContext(locations);
	}

	/**
	 * Write out a complete classification for this concept including accepted names.
	 * 
	 * @param conceptId
	 * @throws Exception
	 */
	private void writeOutClassification(long conceptId) throws Exception {
		this.velocityContext = new VelocityContext();		
		try {
	    if(writer==null){
	    	writer = new FileWriter(outputFileName);
	    	if(includeHeader){
		    	//write out header
		    	Template template = templateUtils.getTemplate(headerTemplateLocation);
		    	templateUtils.merge(template, velocityContext, writer);
	    	}
	    }

	    TaxonConceptDAO tcDAO = (TaxonConceptDAO) this.context.getBean("taxonConceptDAO");
	    RelationshipAssertionDAO raDAO = (RelationshipAssertionDAO) this.context.getBean("relationshipAssertionDAO");
	    TaxonConceptLite fullTc = tcDAO.getTaxonConceptLite(conceptId);
	    
	    if(fullTc.getParentId()!=null){
	    	TaxonConceptLite parentConcept = tcDAO.getTaxonConceptLite(fullTc.getParentId());
	    	if(parentConcept!=null){
	    		velocityContext.put("parentConcept", parentConcept);
	    	}
	    }
	    List<TaxonConceptLite> concepts = tcDAO.getClassificationConcepts(conceptId);
	    
	    //FIXME This should be able to use the taxon_concept.accepted column to do an if !accepted
	    //but this is currently not being set correctly.
	    //if marked as unaccepted, look for accepted name
    	List<RelationshipAssertion> ras = raDAO.getRelationshipAssertionsForFromConcept(conceptId);
    	for(RelationshipAssertion ra: ras){
    		if(ra.getRelationshipType()>=RelationshipAssertionDAO.TYPE_AMBIGUOUS_SYNONYM 
    				&& ra.getRelationshipType()<=RelationshipAssertionDAO.TYPE_SYNONYM){
    			TaxonConceptLite acceptedConcept = tcDAO.getTaxonConceptLite(ra.getToConceptId());
    			velocityContext.put("acceptedConcept", acceptedConcept);
    			break;
    		}
    	}
	    
	    velocityContext.put("taxonConcept", fullTc);
	    for(TaxonConceptLite tc: concepts){
	    	if(tc.getRank()==1000)
	    		velocityContext.put("kingdomConcept", tc);
	    	else if(tc.getRank()==2000)
	    		velocityContext.put("phylumConcept", tc);
	    	else if(tc.getRank()==3000)
	    		velocityContext.put("orderConcept", tc);			
	    	else if(tc.getRank()==4000)
	    		velocityContext.put("classConcept", tc);
	    	else if(tc.getRank()==5000)
	    		velocityContext.put("familyConcept", tc);
	    	else if(tc.getRank()==6000)
	    		velocityContext.put("genusConcept", tc);
	    	else if(tc.getRank()==7000)
	    		velocityContext.put("speciesConcept", tc);
	    	else if(tc.getRank()==8000)
	    		velocityContext.put("subspeciesConcept", tc);
	    }
	    velocityContext.put("rootUrl", speciesRootUrl);
	    velocityContext.put("ff", new FieldFormatter());
	    
	    Template template = templateUtils.getTemplate(templateLocation);
	    templateUtils.merge(template, velocityContext, writer);
	    writer.flush();
    } catch (Exception e) {
    	System.err.println("Problem with concept: "+conceptId);
	    e.printStackTrace();
    	throw e;
    }
    velocityContext = null;
	}	
	
	/**
	 * Start the dump.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		logger.info("Start dump....");
		TaxonomyDumper td = new TaxonomyDumper();
		td.init();
		Long dataProviderId = null;
		Long dataResourceId = null;
		
		if(args.length>0)
			dataProviderId = Long.parseLong(args[0]);
		if(args.length>1)
			dataResourceId = Long.parseLong(args[1]);
	
	  dump(td, dataProviderId, dataResourceId);
	  logger.info("Finished dump.");
	}

	/**
	 * Dump the taxonomy.
	 * 
	 * @param td
	 * @param dataProviderId
	 * @param dataResourceId
	 */
	private static void dump(TaxonomyDumper td, Long dataProviderId,
			Long dataResourceId) {
		StringBuffer sb = new StringBuffer("select id from taxon_concept");
		if (dataProviderId != null || dataResourceId != null) {
			sb.append(" where ");
		}
		if (dataResourceId != null) {
			sb.append("data_resource_id=?");
		} else if (dataProviderId != null){
			sb.append("data_provider_id=? ");
		}

		DataSource dataSource = (DataSource) td.context.getBean("dataSource");
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		try {
			conn = dataSource.getConnection();
			conn.setReadOnly(true);
			
			JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
			List<Map<String, Object>> rankList = jdbcTemplate.queryForList("select id, name from rank");
			ranks = new HashMap<Integer, String>();
			for(Map<String,Object> kvs: rankList){
				Integer key = (Integer) kvs.get("id");
				String value = (String) kvs.get("name");
				ranks.put(key, value);
			}
			
			stmt = conn.prepareStatement(sb.toString(),ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
			stmt.setFetchSize(Integer.MIN_VALUE);
			if (dataResourceId != null){
				stmt.setLong(1, dataResourceId);
			} else if (dataProviderId != null) {
				stmt.setLong(1, dataProviderId);
			}
				
			rs = stmt.executeQuery();
			int conceptCount = 0;
			while (rs.next()) {
				td.writeOutClassification(rs.getLong(1));
				conceptCount++;
				if(conceptCount%1000==0){
					logger.info("Concepts processed: "+conceptCount);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				rs.close();
				stmt.close();
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		System.exit(1);
	}
	
	/**
	 * Formatter for velocity.
	 * 
	 * @author davejmartin
	 */
	public class FieldFormatter {
		
		public String format(Object value){
			if(value==null){
				return "\\N";
			} else {
				return value.toString();
			}
		}
		
		public String formatRank(Integer rank){
			if(ranks!=null){
				String rankAsString = ranks.get(rank);
				if(rankAsString!=null)
					return rankAsString;
			}
			return rank.toString();
		}
	}
}