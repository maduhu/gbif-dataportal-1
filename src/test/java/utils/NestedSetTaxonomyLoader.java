/***************************************************************************
 * Copyright (C) 2008 Global Biodiversity Information Facility Secretariat.  
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
package utils;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * Sets the left_concept_id and right_concept_id on taxon_concept
 * and then updates occurrence_record_ns.
 * 
 * ALTER TABLE taxon_concept ADD left_concept_id mediumint unsigned;
 * ALTER TABLE taxon_concept ADD right_concept_id mediumint unsigned;
 * 
 * @author dmartin
 */
public class NestedSetTaxonomyLoader {
	
	protected static Log logger = LogFactory.getLog(NestedSetTaxonomyLoader.class);

	private ApplicationContext context;
	private DataSource dataSource = null;
	private JdbcTemplate template;
	private int updates = 0;
	
	private void init() {
		String[] locations = {
				"classpath*:/**/applicationContext-*.xml",
				"classpath*:**/applicationContext-*.xml",
				"classpath*:org/gbif/portal/**/applicationContext-*.xml"
		};
		context = new ClassPathXmlApplicationContext(locations);
	}	
	
	public void run(){
		System.out.println(new Date());
		this.dataSource = (DataSource) context.getBean("dataSource");
		this.template = new JdbcTemplate(dataSource);
		logger.info("Retrieving root concepts");
		
		List<Map<String, Object>> kingdomIds = this.template.queryForList(
				"SELECT distinct id FROM taxon_concept " +
				"WHERE parent_concept_id IS NULL AND is_nub_concept=true " +
				"ORDER BY rank");
		
//		List<Map<String, Object>> kingdomIds = new ArrayList<Map<String, Object>>();
//		Map<String,Object> kingdom = new HashMap<String,Object>(); 
//		kingdom.put("id", new Long(13140803));
//		kingdomIds.add(kingdom);
		
		long currentId = 1l;
		for(Map<String, Object> result: kingdomIds){
			final long leftId = currentId;
			Long conceptId = (Long) result.get("id");
			logger.info("Setting kingdom id:"+conceptId);
			currentId = getRightId(conceptId, currentId);
			this.template.update(
					"UPDATE taxon_concept tc SET tc.left_concept_id=?, tc.right_concept_id=? WHERE tc.id=?", 
					new Object[]{leftId, currentId, conceptId});
			currentId++;
		}
		
		//need to set synonyms to have same left/right id as accepted concept
		logger.info("Updating synonyms - last run took 7 min 28.65 sec");
		this.template.update(
				"UPDATE taxon_concept tc " + 
				"inner join relationship_assertion ra ON ra.from_concept_id=tc.id " + 
				"inner join taxon_concept ac ON ra.to_concept_id=ac.id " + 
				"SET tc.left_concept_id=ac.left_concept_id, tc.right_concept_id=ac.right_concept_id");

		//update occurrence_record
		logger.info("Updating occurrence_record - last run took 50 min 56.93 sec");
		this.template.update(
				"UPDATE taxon_concept tc " + 
				"INNER JOIN occurrence_record_ns ocn ON tc.id=ocn.nub_concept_id " + 
				"SET ocn.left_concept_id=tc.left_concept_id");
		
		System.out.println(new Date());
		System.out.println("Updated: "+updates);
	}
	
	/**
	 * Retrieve the right concept id for this concept by recursively
	 * calling down to species/subspecies level.
	 * 
	 * @param conceptId
	 * @param currentId
	 * @return
	 */
	private long getRightId(final long conceptId, long currentId) {
		
		final long leftId = currentId;

		currentId++;
		
		List<Map<String, Object>> ids = this.template.queryForList(
				"SELECT id FROM taxon_concept WHERE parent_concept_id=?", 
				new Object[]{conceptId});
		
		Iterator<Map<String, Object>> iter = ids.iterator();
		
		while (iter.hasNext()){
			Map<String, Object> result = iter.next();
			Long childConceptId = (Long) result.get("id");
			currentId = getRightId(childConceptId, currentId);
			currentId++;
		}
		
		final long rightId = currentId;

		this.template.update(
			"UPDATE taxon_concept tc SET tc.left_concept_id=?, tc.right_concept_id=? WHERE tc.id=?", 
			new Object[]{leftId, rightId, conceptId}
		);
		++updates;
	  return currentId;
  }

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			NestedSetTaxonomyLoader launcher = new NestedSetTaxonomyLoader();
			launcher.init();
			launcher.run();
		} catch (Exception e){
			e.printStackTrace();
		}
		System.exit(1);
	}			
}