package org.gbif.portal.dao.tag.impl.jdbc.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.gbif.portal.dto.tag.QuadRelationTagDTO;
import org.springframework.jdbc.core.RowMapper;

public class QuadRelationTagRowMapper implements RowMapper {

	/**
	 * @see org.springframework.jdbc.core.RowMapper#mapRow(java.sql.ResultSet, int)
	 */
	public Object mapRow(ResultSet rs, int rowNumber) throws SQLException {

		QuadRelationTagDTO quadRelationTag = new QuadRelationTagDTO(rs.getInt("tag_id"), rs.getLong("entity1_id"), rs
		    .getString("entity1_name"), rs.getLong("entity2_id"), rs.getString("entity2_name"), rs.getLong("entity3_id"),
		    rs.getString("entity3_name"), rs.getLong("entity4_id"), rs.getString("entity4_name"), rs.getInt("count"));

		// if additional columns are returned, add to properties map
		quadRelationTag.setProperties(RowMapperUtils.addExtraColumnToProperties(rs, 10));
		return quadRelationTag;
	}
}
