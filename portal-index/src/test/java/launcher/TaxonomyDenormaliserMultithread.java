/**
 * 
 */
package launcher;

import java.util.LinkedList;
import java.util.List;

import launcher.nub.multithread.NubCreator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.pool.PoolableObjectFactory;
import org.apache.commons.pool.impl.GenericObjectPool;
import org.gbif.portal.harvest.taxonomy.TaxonomyUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;


/**
 * Denormalises the taxonomy for a provider or resource
 * @author tim
 */
public class TaxonomyDenormaliserMultithread {
	protected static Log logger = LogFactory.getLog(TaxonomyDenormaliserMultithread.class);
	private ApplicationContext context;
	private TaxonomyUtils taxonomyUtils;
	
	private void init() {
		String[] locations = {"classpath*:/**/applicationContext-*.xml",
				"classpath*:**/applicationContext-*.xml",
				"classpath*:org/gbif/portal/**/applicationContext-*.xml"
		};
		context = new ClassPathXmlApplicationContext(locations);
		taxonomyUtils = (TaxonomyUtils) context.getBean("taxonomyUtils");
	}
	
	/**
	 * @param isProvider true if provider, false if resource
	 * @param id 
	 */
	private void launch(List<Long> ids, int threads) {
		GenericObjectPool pool = new GenericObjectPool(new ObjectFactory());
		pool.setMaxActive(threads);
		// wait forever
		pool.setMaxWait(-1);
		
		for (long id : ids) {
			logger.info("Queing the denormalising of resource[" + id +"]");
			Runner c = new Runner(pool, id, taxonomyUtils);
			Thread t = new Thread(c);
			t.start();
		}
	}

	
	// runner
	class Runner implements Runnable {
		long id;
		TaxonomyUtils taxonomyUtils;
		protected GenericObjectPool pool;
		public Runner(GenericObjectPool pool, long id, TaxonomyUtils taxonomyUtils) {
			this.pool = pool;
			this.id = id;
			this.taxonomyUtils = taxonomyUtils;
		}
		public void run() {
			Object lock;
			try {
				lock = pool.borrowObject();
				try {
					logger.info("Starting denormalising resource[" + id +"]");
					taxonomyUtils.denormalisedTaxonomyForResource(id);
					
				} finally {
					pool.returnObject(lock);
				}
				
			} catch (Exception e) {
				logger.error("Id [" + id + "] is NOT imported", e);
			}
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
	
	
	/**
	 * @param args Usage: 
	 * 	range <lower-inclusive - upper-exclusive>
	 *    E.g.
	 *     TaxonomyDenormaliserMultithread range 0 2000 
	 */
	public static void main(String[] args) {
		try {
			String type = args[0];
			
			if ("RANGE".equalsIgnoreCase(type) && args.length==3) {
				List<Long> ids = new LinkedList<Long>();
				
				long lower = Long.parseLong(args[1]);
				long upper = Long.parseLong(args[2]);
				
				if (upper - lower > 2000) {
					printUsage();
					return;
				}
				
				for (long i=lower; i<upper; i++) {
					ids.add(i);
				}
								
				TaxonomyDenormaliserMultithread me = new TaxonomyDenormaliserMultithread();
				me.init();
				me.launch(ids, 10);
				
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
		System.out.println("  range lower upper");
		System.out.println("e.g.");
		System.out.println("  range 1000 2000");
		System.out.println("Notes:");
		System.out.println("  1) Use ranges less no larger than 2000 in size (e.g. 'range 3000 5000' is fine, 'range 3000 6000' is NOT)");
		System.out.println("  2) Ranges have INCLUSIVE lower and EXCLUSIVE upper (so 0-1000 will do 0,1,2....999 and NOT 1000)");
	}
}