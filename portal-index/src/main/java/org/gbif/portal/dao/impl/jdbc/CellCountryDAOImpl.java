/**
 * 
 */
package org.gbif.portal.dao.impl.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.gbif.portal.dao.CellCountryDAO;
import org.gbif.portal.model.CellCountry;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.RowMapperResultSetExtractor;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

/**
 * Pure JDBC implementation
 * @author Donald Hobern
 */
public class CellCountryDAOImpl extends JdbcDaoSupport implements
		CellCountryDAO {
	/**
	 * The query by cellId sql
	 */
	protected static final String QUERY_BY_CELLID = 
		"select cell_id, iso_country_code " +
		"from cell_country where cell_id=?";

	/**
	 * The query by isoCountryCode sql
	 */
	protected static final String QUERY_BY_ISOCOUNTRYCODE = 
		"select cell_id, iso_country_code " +
		"from cell_country where iso_country_code=?";

	/**
	 * Reusable row mapper
	 */
	protected CellCountryRowMapper cellCountryRowMapper = new CellCountryRowMapper();
	
	/**
	 * Utility to create a CellCountry for a row 
	 * @author trobertson
	 */
	protected class CellCountryRowMapper implements RowMapper {
		/**
		 * The factory
		 */
		public CellCountry mapRow(ResultSet rs, int rowNumber) throws SQLException {
			return new CellCountry(rs.getInt("cell_id"),
					rs.getString("iso_country_code"));			
		}
	}

	/**
	 * @see org.gbif.portal.dao.CellCountryDAO#getByCellId(int)
	 */
	@SuppressWarnings("unchecked")
	public List<CellCountry> getByCellId(int cellId) {
		List<CellCountry> results = (List<CellCountry>) getJdbcTemplate()
		.query(CellCountryDAOImpl.QUERY_BY_CELLID,
				new Object[] {cellId},
				new RowMapperResultSetExtractor(cellCountryRowMapper));
		return results;
	}

	/**
	 * @see org.gbif.portal.dao.CellCountryDAO#getByIsoCountryCode(String)
	 */
	@SuppressWarnings("unchecked")
	public List<CellCountry> getByIsoCountryCode(String isoCountryCode) {
		List<CellCountry> results = (List<CellCountry>) getJdbcTemplate()
		.query(CellCountryDAOImpl.QUERY_BY_ISOCOUNTRYCODE,
				new Object[] {isoCountryCode},
				new RowMapperResultSetExtractor(cellCountryRowMapper));
		return results;
	}

}