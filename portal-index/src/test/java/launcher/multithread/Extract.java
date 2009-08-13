/***************************************************************************
 * Copyright (C) 2005 Global Biodiversity Information Facility Secretariat.
 * All Rights Reserved.
 *
 * The contents of this file are subject to the Mozilla Public
 * License Version 1.1 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of
 * the License at http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS
 * IS" basis, WITHOUT WARRANTY OF ANY KIND, either express or
 * implied. See the License for the specific language governing
 * rights and limitations under the License.
 ***************************************************************************/
package launcher.multithread;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.pool.PoolableObjectFactory;
import org.apache.commons.pool.impl.GenericObjectPool;
import org.gbif.portal.util.workflow.SequenceProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * This is a quick hack to extract everything...
 * @author trobertson
 */
public class Extract {
	protected static Log logger = LogFactory.getLog(Extract.class);
	private ApplicationContext context;
	
	private void init() {
		String[] locations = {"classpath*:/**/applicationContext-*.xml",
				"classpath*:**/applicationContext-*.xml",
				"classpath*:org/gbif/portal/**/applicationContext-*.xml"};
		context = new ClassPathXmlApplicationContext(locations);
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Extract me = new Extract();
		me.init();
		me.go();
	}
	
	/**
	 * Starts
	 */
	protected void go() {
		GenericObjectPool pool = new GenericObjectPool(new ExtractorFactory());
		pool.setMaxActive(15);
		// wait forever
		pool.setMaxWait(-1);
		
		// hacktastic ;o)
		/*
		int[] rapIds = {218,67,92,26,497,374,1858,1039,429,716};		
		for(int i=0; i<rapIds.length; i++) {
			int rapId = rapIds[i];
		*/
		for(int rapId=0; rapId<2000; rapId++) {
			
			// these have been done... 
			if (rapId==218 || rapId==67|| rapId==92|| rapId==26|| rapId==497|| rapId==374|| rapId==1858|| rapId==1039|| rapId==429|| rapId==716) {
				continue;
			}
		
			logger.info("Queing up Id: " + rapId);
			SequenceProcessor workflow = (SequenceProcessor)context.getBean("GBIF:INDEX:1.0:extractOccurrence");
			Map<String, Object> seed = new HashMap<String, Object>();
			GregorianCalendar cal = new GregorianCalendar();
			cal.add(Calendar.MONTH, -6);
			seed.put("pageFrom", cal.getTime());
			seed.put("resourceAccessPointId", new Long(rapId));
			Thread t = new Thread(new Runner(seed, workflow, pool));
			t.start();
		}		
	}
	
	/**
	 * Simple factory
	 */
	class ExtractorFactory implements PoolableObjectFactory {
		public Object makeObject() throws Exception {
			return new Extractor();
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
	 * Launcher
	 */
	class Extractor {
		public void launch(Map<String, Object> seed, SequenceProcessor workflow) {
			try {
				long time = System.currentTimeMillis();
				logger.info("Starting workflow rapId[" + seed.get("resourceAccessPointId") + "]");
				workflow.doActivities(seed);
				logger.info("Finished workflow rapId[" + seed.get("resourceAccessPointId") + "] in " + ((1 + System.currentTimeMillis() - time) / 1000) + " secs");
			} catch (Exception e) {
				logger.error(e);
			}
		}
	}
	
	/**
	 * Just launches the extract 
	 */
	class Runner implements Runnable {
		protected GenericObjectPool pool;
		protected Map<String, Object> seed;
		protected SequenceProcessor workflow;
		public Runner(Map<String, Object> seed, SequenceProcessor workflow, GenericObjectPool pool) {
			this.pool=pool;
			this.seed = seed;
			this.workflow = workflow;
		}
		public void run() {
			Extractor extractor;
			try {
				extractor = (Extractor) pool.borrowObject();
				try {
					extractor.launch(seed, workflow);
				} finally {
					pool.returnObject(extractor);
				}
				
			} catch (Exception e) {
				logger.error(e);
			}
		}		
	}
}