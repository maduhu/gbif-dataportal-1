/***************************************************************************
 * Copyright (C) 2005 Global Biodiversity Information Facility Secretariat.  
 * All Rights Reserved.
 *
 * The contents of this file are subject to the Mozilla Public
 * License Version 1.1 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of
 * the License at http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS
 * IS" basis, WITHOUT WARRANTY OF ANY KIND, either express or
 * implied. See the License for the specific language governing
 * rights and limitations under the License.
 ***************************************************************************/
package org.gbif.portal.util.log;

import java.util.Map;

import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.Level;
import org.apache.log4j.spi.LoggingEvent;
import org.gbif.portal.util.log.impl.GbifLogMessageDAOImpl;

import com.mysql.jdbc.jdbc2.optional.MysqlConnectionPoolDataSource;

/**
 * Implementation of Log for use in GBIF portal.
 * @author Donald Hobern
 */
public class GbifDatabaseLogAppender extends AppenderSkeleton {

	/**
	 * Log severity values
	 */
	public static final int TRACE = 1;
	public static final int DEBUG = 2;
	public static final int INFO = 3;
	public static final int WARN = 4;
	public static final int ERROR = 5;
	public static final int FATAL = 6;
	
	// TODO get this from context
	public static final long PORTAL_INSTANCE_ID = 1;
	
	/**
	 * String parameters
	 */	
	protected String driverClassName;
	protected String userName;
	protected String password;
	protected String serverName;
	protected String port;
	protected String databaseName;
	
	/**
	 * DAO
	 */
	protected GbifLogMessageDAO gbifLogMessageDAO;
	
	/**
	 * The next identifier to return for a logGroup
	 */
	protected Long nextLogGroupId = null;
	
	/**
	 * @return the gbifLogMessageDAO
	 */
	public GbifLogMessageDAO getGbifLogMessageDAO() {
		return gbifLogMessageDAO;
	}

	/**
	 * @param gbifLogMessageDAO the gbifLogMessageDAO to set
	 */
	public void setGbifLogMessageDAO(GbifLogMessageDAO gbifLogMessageDAO) {
		this.gbifLogMessageDAO = gbifLogMessageDAO;
	}

	@Override
	protected void append(LoggingEvent event) {
		Level level = event.getLevel();
		Object o = event.getMessage();
		
		if (o instanceof GbifLogMessage) {
			GbifLogMessage message = (GbifLogMessage) o;
			
			if (message.getEvent().equals(LogEvent.LOGGROUP_CLOSE)) {
				closeLogGroup(message.getLogGroup());
			} else {
				if (gbifLogMessageDAO == null) {
					gbifLogMessageDAO = initialiseDAO();
				}

				message.setPortalInstanceId(PORTAL_INSTANCE_ID);
				message.setLevel(level);
				
				LogGroup logGroup = message.getLogGroup();
				
				if (logGroup != null && logGroup.getId() == LogGroup.UNINITIALISED) {
					logGroup.setId(getNextLogGroupId(message.getPortalInstanceId()));
				}
				
				// If handleMessage returns true, there is nothing else to do now - otherwise log
				if (!message.isCountOnly()
					|| (logGroup == null)
					|| !logGroup.handleMessage(message)) {
					gbifLogMessageDAO.create(message);
				}
			}
		}
	}

	@Override
	public void close() {
	}

	@Override
	public boolean requiresLayout() {
		return false;
	}
	
	public synchronized long getNextLogGroupId(long portalInstanceId) {
		if (nextLogGroupId == null) {
			nextLogGroupId = gbifLogMessageDAO.getMaxLogGroupId(portalInstanceId) + 1;
 		}
		return nextLogGroupId++;
	}

	private void closeLogGroup(LogGroup logGroup) {
		if (logGroup != null && !logGroup.isEnded()) {
			Map<String, GbifLogMessage> map = logGroup.getStoredMessages();
			if (map != null) {
				for(GbifLogMessage message : map.values()) {
					gbifLogMessageDAO.create(message);
				}
			}
		}
	}

	private GbifLogMessageDAO initialiseDAO() {
	    MysqlConnectionPoolDataSource dataSource = new MysqlConnectionPoolDataSource();

	    dataSource.setUser(getUserName());
	    dataSource.setPassword(getPassword());
	    dataSource.setServerName(getServerName());
	    dataSource.setPort(Integer.parseInt(getPort()));
	    dataSource.setDatabaseName(getDatabaseName());

		GbifLogMessageDAO dao = new GbifLogMessageDAOImpl();
		dao.setDataSource(dataSource);
		
		return dao;
	}

	/**
	 * @return the databaseName
	 */
	public String getDatabaseName() {
		return databaseName;
	}

	/**
	 * @param databaseName the databaseName to set
	 */
	public void setDatabaseName(String databaseName) {
		this.databaseName = databaseName;
	}

	/**
	 * @return the driverClassName
	 */
	public String getDriverClassName() {
		return driverClassName;
	}

	/**
	 * @param driverClassName the driverClassName to set
	 */
	public void setDriverClassName(String driverClassName) {
		this.driverClassName = driverClassName;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return the port
	 */
	public String getPort() {
		return port;
	}

	/**
	 * @param port the port to set
	 */
	public void setPort(String port) {
		this.port = port;
	}

	/**
	 * @return the serverName
	 */
	public String getServerName() {
		return serverName;
	}

	/**
	 * @param serverName the serverName to set
	 */
	public void setServerName(String serverName) {
		this.serverName = serverName;
	}

	/**
	 * @return the userName
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * @param userName the userName to set
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}
}