/**
 * 
 */
package org.gbif.portal.dao.impl.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.gbif.portal.dao.CountryDAO;
import org.gbif.portal.model.Country;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.RowMapperResultSetExtractor;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

/**
 * Pure JDBC implementation
 * @author Donald Hobern
 */
public class CountryDAOImpl extends JdbcDaoSupport implements
		CountryDAO {

	/**
	 * The query by isoCountryCode sql
	 */
	protected static final String QUERY_BY_ISOCOUNTRYCODE = 
		"select iso_country_code, min_latitude, max_latitude, min_longitude, max_longitude " +
		"from country where iso_country_code=?";
	
	/**
	 * The query by country name sql
	 */
	protected static final String QUERY_BY_COUNTRYNAME = 
		"select c.iso_country_code, c.min_latitude, c.max_latitude, c.min_longitude, c.max_longitude " +
		"from country c inner join country_name cn on c.id=cn.country_id " +
		"where cn.name=?";	

	/**
	 * Reusable row mapper
	 */
	protected CountryRowMapper countryRowMapper = new CountryRowMapper();
	
	/**
	 * Utility to create a Country for a row 
	 * @author trobertson
	 */
	protected class CountryRowMapper implements RowMapper {
		/**
		 * The factory
		 */
		public Country mapRow(ResultSet rs, int rowNumber) throws SQLException {
			return new Country(rs.getString("iso_country_code"),
					rs.getFloat("min_latitude"),
					rs.getFloat("max_latitude"),
					rs.getFloat("min_longitude"),
					rs.getFloat("max_longitude"));			
		}
	}

	/**
	 * @see org.gbif.portal.dao.CountryDAO#getByIsoCountryCode(String)
	 */
	@SuppressWarnings("unchecked")
	public Country getByIsoCountryCode(String isoCountryCode) {
		List<Country> results = (List<Country>) getJdbcTemplate()
		.query(CountryDAOImpl.QUERY_BY_ISOCOUNTRYCODE,
				new Object[] {isoCountryCode},
				new RowMapperResultSetExtractor(countryRowMapper));
		
		Country country = null;
		
		// TODO handle errors
		if (results.size() == 1)
		{
			country = results.get(0);
		}
		
		return country;
	}
	
	/**
	 * @see org.gbif.portal.dao.CountryDAO#getByCountryName(String)
	 */
	@SuppressWarnings("unchecked")
	public Country getByCountryName(String countryName) {
		List<Country> results = (List<Country>) getJdbcTemplate()
		.query(CountryDAOImpl.QUERY_BY_COUNTRYNAME,
				new Object[] {countryName},
				new RowMapperResultSetExtractor(countryRowMapper));
		
		Country country = null;
		
		// TODO handle errors
		if (results.size() == 1)
		{
			country = results.get(0);
		}
		
		return country;
	}	

}