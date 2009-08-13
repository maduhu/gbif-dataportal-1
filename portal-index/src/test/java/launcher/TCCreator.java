/**
 * 
 */
package launcher;

import java.util.Enumeration;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Appender;
import org.apache.log4j.Logger;
import org.gbif.portal.dao.TaxonConceptDAO;
import org.gbif.portal.dao.TaxonNameDAO;
import org.gbif.portal.model.TaxonName;
import org.gbif.portal.util.log.GbifDatabaseLogAppender;
import org.gbif.portal.util.log.GbifLogMessage;
import org.gbif.portal.util.log.GbifLogMessageDAO;
import org.gbif.portal.util.log.GbifLogUtils;
import org.gbif.portal.util.log.LogEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Launcher for indexing tests
 * 
 * @author tim
 */
public class TCCreator {
	protected static Log logger = LogFactory.getLog(TCCreator.class);
	private ApplicationContext context;
	
	private void init() {
		String[] locations = {
				"classpath*:/**/applicationContext-*.xml",
				"classpath*:**/applicationContext-*.xml",
				"classpath*:org/gbif/portal/**/applicationContext-*.xml"
		};
		context = new ClassPathXmlApplicationContext(locations);
	}
	
	private void index() {
		/*
		TaxonConceptDAO tcDao = (TaxonConceptDAO)context.getBean("taxonConceptDAO");
		TaxonNameDAO tnDao = (TaxonNameDAO)context.getBean("taxonNameDAO");
		logger.error("tcDao: " + tcDao);
		logger.error("tnDao: " + tnDao);
		
		TaxonName tn = tnDao.getUnique("Acanthocinus aedilis", "(Linnaeus, 1758)", 7000);
		logger.error("tn: " + tn);
		*/
		/*
		GbifLogUtils glu = new GbifLogUtils();
		GbifLogMessage message = glu.createGbifLogMessage(glu.startLogGroup(), LogEvent.EXTRACT_BEGIN);
		message.setPortalInstanceId(99);
		message.setMessage("Test message1");
		logger.info(message);
		
		GbifLogMessageDAO dao = (GbifLogMessageDAO)context.getBean("gbifLogMessageDAO");
		logger.info("DAO: " + context.getBean("gbifLogMessageDAO"));
		
		Logger log = Logger.getRootLogger();
		Enumeration<Appender> loggers = log.getAllAppenders();
		while (loggers.hasMoreElements()) {
			Appender a = loggers.nextElement();			
			System.out.println(a.getClass());
			
			if (a instanceof GbifDatabaseLogAppender) {
				((GbifDatabaseLogAppender)a).setGbifLogMessageDAO(dao);
			}
			
			
		}
		message = glu.createGbifLogMessage(glu.startLogGroup(), LogEvent.EXTRACT_BEGIN);
		message.setPortalInstanceId(99);
		message.setMessage("Test message2");
		
		logger.info(message);
		
		*/
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		TCCreator launcher = new TCCreator();
		launcher.init();
		launcher.index();
		System.exit(1);
	}
	
	public static void printUsage(){
		System.out.println(TCCreator.class.getName()+" <resource-access-point-id+>");
		System.out.println(" e.g. "+TCCreator.class.getName()+" 99 12 14");		
	}
}
