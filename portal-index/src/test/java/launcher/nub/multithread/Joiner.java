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
package launcher.nub.multithread;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.pool.PoolableObjectFactory;
import org.apache.commons.pool.impl.GenericObjectPool;
import org.gbif.portal.dao.TaxonConceptDAO;
import org.gbif.portal.harvest.taxonomy.TaxonomyUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * A multithreaded joiner
 * @author trobertson
 */
public class Joiner {
	/**
	 * The DAO for dealing with the taxon concepts
	 */
	protected TaxonConceptDAO taxonConceptDAO;
	
	/**
	 * The spring context 
	 */
	protected ApplicationContext context;
	
	/**
	 * The utilities for merging the classifications
	 */
	protected TaxonomyUtils taxonomyUtils;
	
	protected Log logger = LogFactory.getLog(this.getClass());
	
	/**
	 * Starts
	 */
	protected void go() {
		GenericObjectPool pool = new GenericObjectPool(new JoinerFactory());
		pool.setMaxActive(12);
		// wait forever
		pool.setMaxWait(-1);
		
		// hacktastic ;o)
		for (int i=2; i<1542; i++) {
			logger.info("Queing up Id: " + i);
			Thread t = new Thread(new Runner(taxonConceptDAO, taxonomyUtils, i,pool));
			t.start();
		}
	}
	
	

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Joiner me = new Joiner();
		me.go();
	}
	
	/**
	 */
	protected Joiner() {
		String[] locations = {"classpath*:/**/applicationContext-*.xml",
				"classpath*:**/applicationContext-*.xml",
				"classpath*:org/gbif/portal/**/applicationContext-*.xml"
		};
		context = new ClassPathXmlApplicationContext(locations);
		taxonConceptDAO = (TaxonConceptDAO) context.getBean("taxonConceptDAO");
		taxonomyUtils = (TaxonomyUtils)context.getBean("taxonomyUtils");
	}
	
	
	/**
	 * Simple factory
	 * @author trobertson
	 */
	class JoinerFactory implements PoolableObjectFactory {
		
		public Object makeObject() throws Exception {
			return new JoinResourceTaxonomyToNub();
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
	 * A thread that will get an Joiner from the pool and run
	 * @author trobertson
	 */
	class Runner implements Runnable {
		protected TaxonConceptDAO taxonConceptDAO;
		protected TaxonomyUtils taxonomyUtils;
		protected long id;
		protected GenericObjectPool pool;
		protected Log logger = LogFactory.getLog(this.getClass());
		public Runner(TaxonConceptDAO taxonConceptDAO, TaxonomyUtils taxonomyUtils, long id, GenericObjectPool pool) {
			this.id = id;
			this.taxonConceptDAO=taxonConceptDAO;
			this.taxonomyUtils=taxonomyUtils;
			this.pool=pool;
		}
		
		public void run() {
			JoinResourceTaxonomyToNub joiner;
			try {
				joiner = (JoinResourceTaxonomyToNub) pool.borrowObject();
				try {
					joiner.join(taxonConceptDAO, taxonomyUtils, id);
				} finally {
					pool.returnObject(joiner);
				}
				
			} catch (Exception e) {
				logger.error("Id [" + id + "] is NOT joined", e);
			}
		}		
	}
}
