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
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.gbif.portal.util.workflow.ContextCorruptException;
import org.gbif.portal.util.workflow.ProcessContext;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * Generates the common name tags for the resource.
 * 
 * @author dmartin
 */
public class CommonNameTagActivity extends BaseTagActivity{
	
	protected static Log logger = LogFactory.getLog(CommonNameTagActivity.class);
	
	/** Max common names to associate with the resource */
	protected int maxCommonNamesToStore = 10;
	
	@SuppressWarnings("unchecked")
	public ProcessContext execute(ProcessContext processContext) throws Exception {
		
		logger.info("Generating common name tags");
		Long dataResourceId = (Long) processContext.get(contextKeyDataResourceId);
		if(dataResourceId==null){
			throw new ContextCorruptException("No data resource id in context");
		}		
		
		JdbcTemplate template = new JdbcTemplate(dataSource);
		Connection conn = dataSource.getConnection();
		
		//clear old tags for this resource
		template.update("DELETE from bi_relation_tag where tag_id=? and from_entity_id=?", 
				new Object[]{TagIds.DATA_RESOURCE_COMMON_NAMES, dataResourceId});
		
		List<Map> commonNames = template.queryForList(
				"SELECT DISTINCT cn.id as id FROM taxon_concept tc " +
				"INNER JOIN taxon_concept nc ON tc.partner_concept_id=nc.id " +
				"INNER JOIN common_name cn ON cn.taxon_concept_id=nc.id " +
				"WHERE tc.data_resource_id=?", 
				new Object[]{dataResourceId});		

		int count=0;
		PreparedStatement ps = conn.prepareStatement(
				"INSERT INTO bi_relation_tag " +
				"(tag_id,from_entity_id,to_entity_id,is_system_generated) " +
				"VALUES (?,?,?,true)");
		
		for(Map commonName: commonNames){
			if(count>=maxCommonNamesToStore)
				break;
			Integer commonNameId = (Integer) commonName.get("id");
			ps.setLong(1, TagIds.DATA_RESOURCE_COMMON_NAMES);
			ps.setLong(2, dataResourceId);
			ps.setLong(3, commonNameId);
			ps.execute();	
			count++;
		}
		ps.close();
		conn.close();
		return processContext;
	}

	/**
   * @param maxCommonNamesToStore the maxCommonNamesToStore to set
   */
  public void setMaxCommonNamesToStore(int maxCommonNamesToStore) {
  	this.maxCommonNamesToStore = maxCommonNamesToStore;
  }
}