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

import com.vividsolutions.jts.algorithm.ConvexHull;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;

/**
 * Generate a polygon for this resource.
 * Note: this should be ran with a generous helping of memory (-Xms2g -Xmx2g)
 * 
 * @author dmartin
 */
public class PolygonTagActivity extends BaseTagActivity {

	protected static Log logger = LogFactory.getLog(PolygonTagActivity.class);
	
	protected String WKT_POLYGON_PREFIX = "POLYGON";
	
	/**
	 * @see org.gbif.portal.harvest.workflow.activity.tag.BaseTagActivity#execute(org.gbif.portal.util.workflow.ProcessContext)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public ProcessContext execute(ProcessContext processContext) throws Exception {
		
		logger.info("Generating polygon tags");		
		Long dataResourceId = (Long) processContext.get(contextKeyDataResourceId);
		if(dataResourceId==null){
			throw new ContextCorruptException("No data resource id in context");
		}
		JdbcTemplate template = new JdbcTemplate(dataSource);
		Connection conn = dataSource.getConnection();
		
		//clear old tags for this resource
		template.update("DELETE from string_tag where tag_id=? and entity_id=?", 
				new Object[]{TagIds.DATA_RESOURCE_OCCURRENCES_WKT_POLYGON, dataResourceId});
		
		List<Map> results = template.queryForList(
				"select distinct(latitude), longitude from occurrence_record" +
				" where data_resource_id=? and latitude is not null", 
				new Object[]{dataResourceId});

		Coordinate[] coordinates = new Coordinate[results.size()];
		int i=0;
		for(Map result: results){
			coordinates[i] = new Coordinate((Float) result.get("longitude"), (Float) result.get("latitude"));
			i++;
		}
		
		GeometryFactory gf = new GeometryFactory();
		ConvexHull convexHull = new ConvexHull(coordinates, gf);
		Geometry g = convexHull.getConvexHull();
		Coordinate[] coords = g.getCoordinates();
		for(Coordinate coord: coords){
			coord.x = (Math.floor(coord.x * 100)) / 100;
			coord.y = (Math.floor(coord.y * 100)) / 100;
		}

		String polygon = g.toString();
		if(polygon.startsWith(WKT_POLYGON_PREFIX)){
			PreparedStatement ps = conn.prepareStatement(
					"INSERT INTO string_tag " +
					"(tag_id,entity_id,value,is_system_generated) " +
					"VALUES (?, ?, ?, true)");
			//escape single quotes
			ps.setLong(1, TagIds.DATA_RESOURCE_OCCURRENCES_WKT_POLYGON);
			ps.setLong(2, dataResourceId);
			ps.setString(3, polygon);
			ps.execute();
			ps.close();
		}
		conn.close();
		return processContext;
	}
}