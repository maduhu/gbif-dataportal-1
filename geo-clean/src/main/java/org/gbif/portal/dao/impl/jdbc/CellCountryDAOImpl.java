/**
 * 
 */
package org.gbif.portal.dao.impl.jdbc;

import org.gbif.portal.dao.CellCountryDAO;
import org.gbif.portal.model.CellCountry;

import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.RowMapperResultSetExtractor;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

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

	private static final String QUERY_BY_CELL_ID_AND_ISOCOUNTRYCODE = 
		"select cell_id, iso_country_code " +
		"from cell_country where cell_id=? and iso_country_code=?";

	protected static final String CREATE_SQL = 
		"insert into cell_country(" +
		"cell_id,"+
		"iso_country_code) "+
		"values (?,?)";

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
	
	/**
	 * @see org.gbif.portal.dao.CellCountryDAO#getByIsoCountryCode(String)
	 */
	@SuppressWarnings("unchecked")
	public CellCountry getByCellIdAndIsoCountryCode(int cellid, String isoCountryCode) {
		List<CellCountry> results = (List<CellCountry>) getJdbcTemplate()
		.query(CellCountryDAOImpl.QUERY_BY_CELL_ID_AND_ISOCOUNTRYCODE,
				new Object[] {cellid, isoCountryCode},
				new RowMapperResultSetExtractor(cellCountryRowMapper));
		if (results.size()==0) {
			logger.info("No cell_country record exists for cellId="+String.valueOf(cellid)+" and isoCountryCode="+String.valueOf(isoCountryCode));
			return null;
		} else if (results.size()>1) {
			logger.warn("Multiple cell_country record exists for cellId="+String.valueOf(cellid)+" and isoCountryCode="+String.valueOf(isoCountryCode));
		}
		return results.get(0);
	}

	public int create(final CellCountry cellCountry) {
		int rowsAffected = getJdbcTemplate().update(
			new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection conn) throws SQLException {
					PreparedStatement ps = conn.prepareStatement(CellCountryDAOImpl.CREATE_SQL);
					ps.setInt(1, cellCountry.getCellId());
					ps.setString(2, cellCountry.getIsoCountryCode());
					return ps;
				}					
			}
			);
		return rowsAffected;
	}

}