/**
 * 
 */
package org.gbif.portal.service.impl;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.sql.DataSource;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.gbif.portal.dao.resources.DataProviderDAO;
import org.gbif.portal.dto.DTOFactory;
import org.gbif.portal.dto.SearchResultsDTO;
import org.gbif.portal.service.SystemManager;

/**
 * System manager implementation.
 * 
 * @author dmartin
 */
public class SystemManagerImpl implements SystemManager {

	protected static Log logger = LogFactory.getLog(SystemManagerImpl.class);	
	
	protected DataProviderDAO dataProviderDAO;
	protected DTOFactory keyValueDTOFactory;
	protected CacheManager cacheManager;
	protected DataSource dataSource;
	protected boolean devMode=false;
	protected String retrieveProcessListSQL = "show full processlist";
	protected String killProcessSql = "kill ";

	/**
	 * Returns the rollover dates for this system.
	 */
	public List<Date> retrieveRolloverDates() {
	  return dataProviderDAO.getRolloverDates();
  }	
	
	/**
	 * @see org.gbif.portal.service.SystemManager#killLongRunningQueries(int)
	 */
	public void killLongRunningQueries(int maxProcessLengthInSecs){
		
		if(dataSource instanceof BasicDataSource){
			BasicDataSource bds = (BasicDataSource) dataSource;
		    Connection conn = null;
		    Statement stmt = null;
		    ResultSet rs = null;
		    try {
		      conn = bds.getConnection();
		      stmt = conn.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
		      stmt.setFetchSize(Integer.MIN_VALUE);	      
		      rs = stmt.executeQuery(retrieveProcessListSQL);
		      
		      List<String> processesToKill = new ArrayList<String>();
		      
		      // extract data from the ResultSet scroll from top
		      while (rs.next()) {
		    	  
		    	  //| Id | User | Host                     | db     | Command | Time | State    | Info
		    	  String processId = rs.getString(1);
		    	  String user = rs.getString(2);
		    	  Integer timeTaken = rs.getInt(6);
		    	  String state = rs.getString(5);
		    	  String info = rs.getString(7);
		    	  String description = rs.getString(8);
		    	  if(info==null)
		    		  info="";
		    	  if(logger.isDebugEnabled()){
		    		  logger.debug("Process id: "+processId+", User: "+user+", Time taken (secs): "+timeTaken+", State: "+state+", Information: "+info+", Description:"+description);
		    	  }
		    	  
				  if(timeTaken>maxProcessLengthInSecs 
						  && description!=null 
						  && description.startsWith("select")
						  && user!=null 
						  && user.equals(bds.getUsername())){
					  processesToKill.add(processId);
					  if(logger.isWarnEnabled()){
						  logger.warn("*** Killing process with Process id: "+processId+", User: "+user+", Time taken (secs): "+timeTaken+", State: "+state+", Information: "+info+", Description: "+description);
					  }
				  }
		      }
					rs.close();
					stmt.close();

	        for(String processId: processesToKill){
	        	Statement killStmt = conn.createStatement();
	        	killStmt.executeUpdate("kill "+processId);
	        	killStmt.close();
	        }
		      
		    } catch (Exception e) {
		      logger.error(e.getMessage(), e);
		    } finally {
		      // release database resources
		      try {
		        rs.close();
		        stmt.close();
		        conn.close();
		      } catch (SQLException e) {
		    	  logger.error(e.getMessage(), e);
		      }
		    }			
		}		
	}
	
	/**
	 * @see org.gbif.portal.service.SystemManager#clearCache()
	 */
	public void clearCache() {
		String[] cacheNames = cacheManager.getCacheNames();
		for(int i=0; i<cacheNames.length; i++){
			Cache cache = cacheManager.getCache(cacheNames[i]);
			try {
				cache.removeAll();
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			} 
		}
	}
	
	/**
	 * @see org.gbif.portal.service.SystemManager#getSystemDetails()
	 */
	public SearchResultsDTO getSystemDetails() {
		List<Object[]> kvps = dataProviderDAO.getSystemDetails();
		
		if(dataSource instanceof BasicDataSource){
			BasicDataSource bds = (BasicDataSource) dataSource;
			if(devMode)
				kvps.add(new Object[]{"Database Url", bds.getUrl()});			
			kvps.add(new Object[]{"Driver", bds.getDriverClassName()});
			kvps.add(new Object[]{"Num active connections", bds.getNumActive()});
			kvps.add(new Object[]{"Number idle connections", bds.getNumIdle()});
			kvps.add(new Object[]{"Initial size", bds.getInitialSize()});
			kvps.add(new Object[]{"Max active", bds.getMaxActive()});
			kvps.add(new Object[]{"Max idle", bds.getMaxIdle()});
			kvps.add(new Object[]{"Max wait", bds.getMaxWait()});
		}
		return keyValueDTOFactory.createResultsDTO(kvps, 1000);
	}

	/**
	 * @return the keyValueDTOFactory
	 */
	public DTOFactory getKeyValueDTOFactory() {
		return keyValueDTOFactory;
	}

	/**
	 * @param keyValueDTOFactory the keyValueDTOFactory to set
	 */
	public void setKeyValueDTOFactory(DTOFactory keyValueDTOFactory) {
		this.keyValueDTOFactory = keyValueDTOFactory;
	}

	/**
	 * @param cacheManager the cacheManager to set
	 */
	public void setCacheManager(CacheManager cacheManager) {
		this.cacheManager = cacheManager;
	}

	/**
	 * @param dataProviderDAO the dataProviderDAO to set
	 */
	public void setDataProviderDAO(DataProviderDAO dataProviderDAO) {
		this.dataProviderDAO = dataProviderDAO;
	}

	/**
	 * @param dataSource the dataSource to set
	 */
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	/**
	 * @param devMode the devMode to set
	 */
	public void setDevMode(boolean devMode) {
		this.devMode = devMode;
	}
}
