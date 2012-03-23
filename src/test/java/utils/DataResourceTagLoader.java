package utils;

import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * @author davemartin
 */
public class DataResourceTagLoader   {
	
	protected static Log logger = LogFactory.getLog(DataResourceTagLoader.class);
	
	private ApplicationContext context;
	
	private void init() {
		String[] locations = {
				"classpath*:/**/applicationContext-*.xml",
				"classpath*:**/applicationContext-*.xml",
				"classpath*:org/gbif/portal/**/applicationContext-*.xml"
		};
		context = new ClassPathXmlApplicationContext(locations);
	}
	
	/**
	 * Run the loader.
	 * 
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	protected void run() throws Exception{
		init();
		DataSource dataSource = (DataSource) context.getBean("dataSource");
		clearOldTags(dataSource);
		JdbcTemplate template = new JdbcTemplate(dataSource);	
		List<Map> results = template.queryForList("SELECT id FROM data_resource");
		
		for(Map result: results){
			Integer dataResourceId = (Integer) result.get("id");
			logger.debug("Processing data resource: "+dataResourceId);
			process(dataSource, template, dataResourceId);	
		}
	}
	
	/**
	 * Method to override.
	 */
	protected void clearOldTags(DataSource dataSource)throws Exception{}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			DataResourceTagLoader launcher = new DataResourceTagLoader();
			launcher.run();
		} catch (Exception e){
			e.printStackTrace();
		}
		System.exit(1);
	}		
	

	protected void process(DataSource dataSource, JdbcTemplate template,
			Integer dataResourceId) throws Exception {}
}