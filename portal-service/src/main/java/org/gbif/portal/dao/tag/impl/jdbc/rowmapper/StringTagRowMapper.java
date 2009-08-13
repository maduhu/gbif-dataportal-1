/**
 * 
 */
package org.gbif.portal.dao.tag.impl.jdbc.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.gbif.portal.dto.tag.StringTag;
import org.springframework.jdbc.core.RowMapper;

/**
 * @author dave
 *
 */
public class StringTagRowMapper implements RowMapper {

	/**
	 * @see org.springframework.jdbc.core.RowMapper#mapRow(java.sql.ResultSet, int)
	 */
	public Object mapRow(ResultSet rs, int rowNumber) throws SQLException {
		return new StringTag(rs.getInt("tag_id"),
				rs.getLong("entity_id"),
				rs.getString("entity_name"),
				rs.getString("value"));
	}
}
