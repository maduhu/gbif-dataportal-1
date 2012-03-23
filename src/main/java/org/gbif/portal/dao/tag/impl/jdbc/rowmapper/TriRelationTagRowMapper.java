package org.gbif.portal.dao.tag.impl.jdbc.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.gbif.portal.dto.tag.TriRelationTagDTO;
import org.springframework.jdbc.core.RowMapper;

public class TriRelationTagRowMapper implements RowMapper {

	/**
	 * @see org.springframework.jdbc.core.RowMapper#mapRow(java.sql.ResultSet, int)
	 */
	public Object mapRow(ResultSet rs, int rowNumber) throws SQLException {
		
		TriRelationTagDTO triRelationTag = new TriRelationTagDTO(rs.getInt("tag_id"),
				rs.getLong("entity1_id"),
				rs.getString("entity1_name"),
				rs.getLong("entity2_id"),
				rs.getString("entity2_name"),
				rs.getLong("entity3_id"),
				rs.getString("entity3_name"),
				rs.getInt("count"));

		//if additional columns are returned, add to properties map
		triRelationTag.setProperties(RowMapperUtils.addExtraColumnToProperties(rs, 8));
		return triRelationTag;
	}
}
