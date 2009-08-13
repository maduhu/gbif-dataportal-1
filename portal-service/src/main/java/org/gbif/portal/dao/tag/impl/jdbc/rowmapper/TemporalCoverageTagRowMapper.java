package org.gbif.portal.dao.tag.impl.jdbc.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.gbif.portal.dto.tag.TemporalCoverageTag;
import org.springframework.jdbc.core.RowMapper;

public class TemporalCoverageTagRowMapper implements RowMapper {

	public Object mapRow(ResultSet rs, int rowNumber) throws SQLException {
		return new TemporalCoverageTag(rs.getInt("tag_id"),
				rs.getLong("entity_id"),
				rs.getString("entity_name"),
				rs.getTimestamp("start_date"),
				rs.getTimestamp("end_date"));
	}
}
