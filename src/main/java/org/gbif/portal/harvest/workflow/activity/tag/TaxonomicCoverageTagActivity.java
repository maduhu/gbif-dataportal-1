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
package org.gbif.portal.harvest.workflow.activity.tag;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.gbif.portal.util.workflow.ContextCorruptException;
import org.gbif.portal.util.workflow.ProcessContext;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * Creates taxonomic scope tags
 * 
 * @author dmartin
 */
public class TaxonomicCoverageTagActivity extends BaseTagActivity  {
	
	protected static Log logger = LogFactory.getLog(TaxonomicCoverageTagActivity.class);

	public int maxConceptsToStore = 10;
	
	/**
	 * @see launcher.DataResourceTagLoader#process(javax.sql.DataSource, org.springframework.jdbc.core.JdbcTemplate, java.lang.Integer)
	 */
	@SuppressWarnings("unchecked")
	public ProcessContext execute(ProcessContext processContext) throws Exception {
		
		logger.info("Generating Taxonomic scope and associated kingdom tags");
		Long dataResourceId = (Long) processContext.get(contextKeyDataResourceId);
		if(dataResourceId==null){
			throw new ContextCorruptException("No data resource id in context");
		}
		JdbcTemplate template = new JdbcTemplate(dataSource);
		Connection conn = dataSource.getConnection();
		
		//clear old tags for this resource
		template.update("DELETE from string_tag where (tag_id=? or tag_id=?) and entity_id=?", 
				new Object[]{TagIds.DATA_RESOURCE_TAXONOMIC_SCOPE, TagIds.DATA_RESOURCE_ASSOCIATED_KINGDOM, dataResourceId});
		
		//determine root taxa
		List<Map> rootTaxaResults = template.queryForList(
				"SELECT tc.id id,tc.partner_concept_id as partner_concept_id,tc.rank, " +
				"nc.kingdom_concept_id as kingdom_concept_id " +
				"FROM taxon_concept tc " +
				"LEFT OUTER JOIN taxon_concept nc ON tc.partner_concept_id = nc.id "  +
				"WHERE tc.parent_concept_id IS NULL AND tc.data_resource_id=? ORDER BY tc.rank", 
				new Object[]{dataResourceId});
		
		boolean tagsInserted = false;
		
		int taxonomicScopeTagsAdded = 0;
		int MAX_TAGS = 10;
		
		PreparedStatement ps = conn.prepareStatement("INSERT INTO bi_relation_tag " +
				"(tag_id,from_entity_id,to_entity_id,is_system_generated) " +
				"VALUES (?, ?, ?,true)");		
		
		//for each kingdom, go down each root until there is more than one child
		for(Map rootTaxa: rootTaxaResults){
			
			Long taxonConceptId = (Long) rootTaxa.get("id");
			Long nubConceptId = (Long) rootTaxa.get("partner_concept_id");

			//determine root taxa
			List<Map> childResults = template.queryForList(
					"SELECT id,partner_concept_id, rank FROM taxon_concept tc " +
					"WHERE tc.parent_concept_id is null AND data_resource_id=?", 
					new Object[]{dataResourceId});
			
			//while there is only one child, iterate down
			while(childResults.size()==1){
				taxonConceptId = (Long) childResults.get(0).get("id");
				nubConceptId = (Long) childResults.get(0).get("partner_concept_id");
				childResults = template.queryForList(
						"SELECT tc.id, tc.partner_concept_id FROM taxon_concept tc " +
						"WHERE tc.parent_concept_id = ?", 
						new Object[]{taxonConceptId});
			}
			
			if(nubConceptId!=null){
				//we have found the root taxon for this branch - create root taxon tag
				ps.setLong(1, TagIds.DATA_RESOURCE_TAXONOMIC_SCOPE);
				ps.setLong(2, dataResourceId);
				ps.setLong(3, nubConceptId);
				ps.execute();
				tagsInserted = true;
				taxonomicScopeTagsAdded++;
				if(taxonomicScopeTagsAdded>MAX_TAGS)
					break;
			}
		}
		ps.close();
		
		//if nothing inserted, insert tags for the first few concepts in root list
		if(!tagsInserted){
			
			ps = conn.prepareStatement("INSERT INTO bi_relation_tag " +
					"(tag_id,from_entity_id,to_entity_id,is_system_generated) " +
					"VALUES (?,?,?,true)");

			int count = 0;
			for(Map rootTaxa: rootTaxaResults){
				if(count>=maxConceptsToStore)
					break;
				Long nubConceptId = (Long) rootTaxa.get("partner_concept_id");
				if(nubConceptId!=null){
					ps.setLong(1, TagIds.DATA_RESOURCE_TAXONOMIC_SCOPE);
					ps.setLong(2, dataResourceId);
					ps.setLong(3, nubConceptId);
					ps.execute();	
					count++;
				}
			}
			ps.close();
		}
		
		//set kingdom coverage
		ps = conn.prepareStatement("INSERT INTO bi_relation_tag " +
				"(tag_id,from_entity_id,to_entity_id,is_system_generated) " +
				"VALUES (?,?,?,true)");

		List<Long> addedKingdoms = new ArrayList<Long>();
		for(Map rootTaxa: rootTaxaResults){
			Long nubConceptId = (Long) rootTaxa.get("kingdom_concept_id");
			if(nubConceptId!=null && !addedKingdoms.contains(nubConceptId)){
				ps.setLong(1, TagIds.DATA_RESOURCE_ASSOCIATED_KINGDOM);
				ps.setLong(2, dataResourceId);
				ps.setLong(3, nubConceptId);
				ps.execute();
				addedKingdoms.add(nubConceptId);
			}
		}				
		ps.close();
		conn.close();
		return processContext;
	}

	/**
   * @param maxConceptsToStore the maxConceptsToStore to set
   */
  public void setMaxConceptsToStore(int maxConceptsToStore) {
  	this.maxConceptsToStore = maxConceptsToStore;
  }
}