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
package org.gbif.portal.web.download;

import java.util.TimerTask;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.gbif.portal.service.SystemManager;

/**
 * Simple timer task that checks for long running queries and kills them.
 * 
 * @author dmartin
 */
public class LongRunningQueryTask extends TimerTask {

	protected static Log logger = LogFactory.getLog(LongRunningQueryTask.class);	
	
	protected int maxProcessLengthInSecs = 1200;
	
	protected SystemManager systemManager;
	
	/**
	 * @see java.util.TimerTask#run()
	 */
	@Override
	public void run() {
		try {
			logger.debug("Checking for long running queries.");
			//get the directory to search
			systemManager.killLongRunningQueries(maxProcessLengthInSecs);
		} catch (Exception e){
			logger.error(e.getMessage(), e);
		}
	}
	
	/**
	 * @param systemManager the systemManager to set
	 */
	public void setSystemManager(SystemManager systemManager) {
		this.systemManager = systemManager;
	}

	/**
	 * @param maxProcessLengthInSecs the maxProcessLengthInSecs to set
	 */
	public void setMaxProcessLengthInSecs(int maxProcessLengthInSecs) {
		this.maxProcessLengthInSecs = maxProcessLengthInSecs;
	}
}