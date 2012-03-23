package org.gbif.portal.dao.tag.impl.jdbc.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.gbif.portal.dto.tag.BiRelationTagDTO;
import org.springframework.jdbc.core.RowMapper;

public class BiRelationTagRowMapper implements RowMapper {

	/**
	 * @see org.springframework.jdbc.core.RowMapper#mapRow(java.sql.ResultSet, int)
	 */
	public Object mapRow(ResultSet rs, int rowNumber) throws SQLException {
		
		BiRelationTagDTO biRelationTag = new BiRelationTagDTO(rs.getInt("tag_id"),
				rs.getLong("from_entity_id"),
				rs.getString("from_entity_name"),
				rs.getLong("to_entity_id"),
				rs.getString("to_entity_name"),
				rs.getInt("count"));

		//if additional columns are returned, add to properties map
		biRelationTag.setProperties(RowMapperUtils.addExtraColumnToProperties(rs, 6));
		return biRelationTag;
	}
}
