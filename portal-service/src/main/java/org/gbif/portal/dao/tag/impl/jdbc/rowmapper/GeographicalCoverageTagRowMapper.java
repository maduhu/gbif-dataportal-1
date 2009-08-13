package org.gbif.portal.dao.tag.impl.jdbc.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.gbif.portal.dto.tag.GeographicalCoverageTag;
import org.springframework.jdbc.core.RowMapper;

public class GeographicalCoverageTagRowMapper implements RowMapper {
	public Object mapRow(ResultSet rs, int rowNumber) throws SQLException {
		return new GeographicalCoverageTag(rs.getInt("tag_id"),
				rs.getLong("entity_id"),
				rs.getString("entity_name"),
				rs.getFloat("min_longitude"),
				rs.getFloat("min_latitude"),
				rs.getFloat("max_longitude"),
				rs.getFloat("max_latitude"));	
	}
}