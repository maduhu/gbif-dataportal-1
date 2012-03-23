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
 * Generates the collector tags for the resource.
 * 
 * @author dmartin
 */
public class CollectorTagActivity extends BaseTagActivity {
	
	protected static Log logger = LogFactory.getLog(CollectorTagActivity.class);

	/** The maximum number of collectors to create tags for */
	protected int maxNumberOfCollectorsToRecord = 100;
	
	/**
	 * @see org.gbif.portal.util.workflow.Activity#execute(org.gbif.portal.util.workflow.ProcessContext)
	 */	
	@SuppressWarnings("unchecked")
  public ProcessContext execute(ProcessContext processContext) throws Exception {

		logger.info("Generating collect tags");
		Long dataResourceId = (Long) processContext.get(contextKeyDataResourceId);
		if(dataResourceId==null){
			throw new ContextCorruptException("No data resource id in context");
		}
		JdbcTemplate template = new JdbcTemplate(dataSource);
		Connection conn = dataSource.getConnection();
		
		//clear old tags for this resource
		template.update("DELETE FROM string_tag WHERE tag_id=? AND entity_id=?", 
				new Object[]{TagIds.DATA_RESOURCE_COLLECTOR, dataResourceId});
		
		PreparedStatement ps = conn.prepareStatement(
				"INSERT INTO string_tag " +
				"(tag_id,entity_id,value,is_system_generated) " +
				"VALUES (?,?,?,true)");
		
		List<Map<String, Object>> collectorNames = template.queryForList(
				"SELECT DISTINCT collector_name FROM raw_occurrence_record " +
				"WHERE data_resource_id=? AND collector_name IS NOT NULL LIMIT ?", 
				new Object[]{dataResourceId, maxNumberOfCollectorsToRecord});		
		
		int count=0;
		
		for(Map<String, Object> collectorNameMap: collectorNames){
			try {			
				String collectorName = (String) collectorNameMap.get("collector_name");
				//escape single quotes
				StringBuffer oldSe = new StringBuffer("'");
				StringBuffer newSe = new StringBuffer("\\'");
				collectorName = collectorName.replace(oldSe, newSe);
				ps.setInt(1, TagIds.DATA_RESOURCE_COLLECTOR);
				ps.setLong(2, dataResourceId);
				ps.setString(3, collectorName);
				ps.execute();
				count++;
			} catch (Exception e){
				logger.error(e.getMessage(), e);
			}
		}
		ps.close();
		conn.close();
		logger.info("Collector tags inserted: "+count);
		return processContext;
	}

	/**
   * @param maxNumberOfCollectorsToRecord the maxNumberOfCollectorsToRecord to set
   */
  public void setMaxNumberOfCollectorsToRecord(int maxNumberOfCollectorsToRecord) {
  	this.maxNumberOfCollectorsToRecord = maxNumberOfCollectorsToRecord;
  }
}