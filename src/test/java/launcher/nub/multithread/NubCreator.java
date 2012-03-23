/**
 * 
 */
package launcher.nub.multithread;

import java.util.LinkedList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.pool.PoolableObjectFactory;
import org.apache.commons.pool.impl.GenericObjectPool;
import org.gbif.portal.dao.DataResourceDAO;
import org.gbif.portal.harvest.taxonomy.TaxonomyUtils;
import org.gbif.portal.model.DataResource;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;


/**
 * Launcher for creating the nub taxonomy tree.
 * 
 * THIS IS A THROWN TOGETHER HACK!
 *  
 * @author tim
 */
public class NubCreator {
	/**
	 * The commons logger
	 */
	protected Log logger = LogFactory.getLog(this.getClass());
	
	/**
	 * The utils
	 */
	protected TaxonomyUtils taxonomyUtils;
	
	/**
	 * The DataResourceDAO
	 */
	protected DataResourceDAO dataResourceDAO;
	
	/**
	 * The spring context 
	 */
	protected ApplicationContext context;
	
	/**
	 * Does the merging of the taxonomy by paging over each resources'
	 */
	protected void launch(List<Long> resourceIds, int threads) {
		GenericObjectPool pool = new GenericObjectPool(new ObjectFactory());
		pool.setMaxActive(threads);
		// wait forever
		pool.setMaxWait(-1);
		
		for (long id : resourceIds) {
			logger.info("Queing the importing of resource[" + id +"]");
			Creator c = new Creator(pool, id);
			Thread t = new Thread(c);
			t.start();
		}
	}
	
	// factory
	class ObjectFactory implements PoolableObjectFactory {
		public Object makeObject() throws Exception {
			return new Object();
		}
		// required methods
		public void destroyObject(Object arg0) throws Exception {
		}
		public boolean validateObject(Object arg0) {
			return true;
		}
		public void activateObject(Object arg0) throws Exception {}
		public void passivateObject(Object arg0) throws Exception {}		
	}
	
	// runnable create
	class Creator implements Runnable {
		protected GenericObjectPool pool;
		protected Long id;
		public Creator(GenericObjectPool pool, Long id) {
			this.pool = pool;
			this.id = id;
		}		
		public void run() {
			Object lock;
			try {
				lock = pool.borrowObject();
				try {
					long time = System.currentTimeMillis();
					logger.info("Starting importing resource[" + id +"]");
					DataResource resource = dataResourceDAO.getById(id);
					// Only allow our highest taxonomic authorities to create kingdoms
					
					if (resource!=null) {					
						boolean canCreateKingdoms = (resource.getTaxonomicPriority() <= 10);
						taxonomyUtils.importTaxonomyFromDataResource(id, 1, 1, canCreateKingdoms, false, true);
						logger.info("Finished importing resource[" + id +"] in " + ((1+System.currentTimeMillis()-time)/1000) + " secs");
					} else {
						logger.info("Nothing to import from resource[" + id +"]");
					}
					
					
				} finally {
					pool.returnObject(lock);
				}
				
			} catch (Exception e) {
				logger.error("Id [" + id + "] is NOT imported", e);
			}
			
		}
	}
	
	/**
	 * Hidden constructor forcing the setting of the required values
	 * @param nubProviderId For the NUB taxonomy
	 * @param nubResourceId For the NUB taxonomy
	 * @param resources That are to be merged into the Nub taxonomy
	 */
	protected NubCreator() {
		String[] locations = {"classpath*:/**/applicationContext-*.xml",
				"classpath*:**/applicationContext-*.xml",
				"classpath*:org/gbif/portal/**/applicationContext-*.xml"
		};
		context = new ClassPathXmlApplicationContext(locations);
		taxonomyUtils = (TaxonomyUtils) context.getBean("taxonomyUtils");
		dataResourceDAO = (DataResourceDAO) context.getBean("dataResourceDAO");
	}
	
	/**
	 * @param args Usage: 
	 * 	1) resources <resource-id-to-merge-in>+
	 *     range <lower-inclusive - upper-exclusive>
	 *    E.g.
	 *     NubTaxonomyCreator resources 10 12 14 15
	 *     NubTaxonomyCreator range 0 2000 
	 */
	public static void main(String[] args) {
		try {
			String type = args[0];
			
			if ("RESOURCES".equalsIgnoreCase(type) && args.length>1) {
				List<Long> ids = new LinkedList<Long>();
				for (int i=1; i<args.length; i++) {
					ids.add(Long.parseLong(args[i]));
				}
				NubCreator launcher = new NubCreator();
				launcher.launch(ids, 10);
				
			} else if ("RANGE".equalsIgnoreCase(type) && args.length==3) {
				List<Long> ids = new LinkedList<Long>();
				
				long lower = Long.parseLong(args[1]);
				long upper = Long.parseLong(args[2]);
				
				if (upper - lower > 2000) {
					printUsage();
					return;
				}
								
				for (long i=lower; i<upper; i++) {
					// skip known resources
					if (i<41) {  // old COL and nub
						continue;
					}
					if (i>=1542 && i<=1590) { // COL
						continue;
					}
					if (i==1752) { // Index Fungorum
						continue;
					}
					ids.add(i);
				}
				NubCreator launcher = new NubCreator();
				launcher.launch(ids, 10);
				
			} else {
				printUsage();
			}
		} catch (Exception e) {
			e.printStackTrace();
			printUsage();
		}
	}
	
	protected static void printUsage() {
		System.out.println("Usage:");
		System.out.println("  resources <resource-id-to-merge-in>+");
		System.out.println("  range lower upper");
		System.out.println("e.g.");
		System.out.println("  resources 1 2 3 4 5");
		System.out.println("  range 1000 2000");
		System.out.println("Notes:");
		System.out.println("  1) Use ranges less no larger than 2000 in size (e.g. 'range 3000 5000' is fine, 'range 3000 6000' is NOT)");
		System.out.println("  2) Ranges have INCLUSIVE lower and EXCLUSIVE upper (so 0-1000 will do 0,1,2....999 and NOT 1000)");
	}
}
