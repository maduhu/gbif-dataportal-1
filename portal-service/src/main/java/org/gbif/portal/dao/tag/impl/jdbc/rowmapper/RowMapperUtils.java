package org.gbif.portal.dao.tag.impl.jdbc.rowmapper;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * Row Mapper Utilities
 *  
 * @author davejmartin
 */
public class RowMapperUtils {

	/**
	 * Map extra columns of index greater than lastMappedCol into a Map.
	 * 
	 * @param rs
	 * @param lastMappedCol
	 * @return
	 * @throws SQLException
	 */
	public static Map<String,Object> addExtraColumnToProperties(ResultSet rs, int lastMappedCol) throws SQLException {
	  ResultSetMetaData metadata = rs.getMetaData();
		int columnCount = metadata.getColumnCount();
		if(columnCount> lastMappedCol){
			Map<String,Object> additionalProperties = new HashMap<String, Object>();
			for(int i=lastMappedCol+1; i<=columnCount;i++){
				String columnLabel = metadata.getColumnLabel(i);
				Object value = rs.getObject(i);
				additionalProperties.put(columnLabel, value);
			}
			return additionalProperties;	
		}
		return null;
  }
	
	
}
