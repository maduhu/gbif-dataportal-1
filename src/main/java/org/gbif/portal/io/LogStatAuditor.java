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
package org.gbif.portal.io;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.gbif.portal.dto.log.LogStatsDTO;
import org.gbif.portal.model.log.LogMessage;

/**
 * Log statistics auditor.
 * 
 * @author dmartin
 */
public class LogStatAuditor implements ChainableResultsOutputter {

	protected static Log logger = LogFactory.getLog(LogStatAuditor.class);	
	
	/** The results outputter to call after this one */
	private ResultsOutputter nextResultsOutputter;	
	/** The data resource keys and names  */
	private List<LogStatsDTO> logStats = new ArrayList<LogStatsDTO>();
	/** Whether to pass the bean map on */
	private boolean chainInOnePass = true;
	
	/**
	 * @see org.gbif.portal.io.ResultsOutputter#write(java.util.Map)
	 */
	public void write(Map beans) throws IOException {
		if(beans==null)
			return;
		
		LogMessage lm = null;
		Iterator iter = beans.keySet().iterator();
		while(iter.hasNext()){
			Object key =  iter.next();
			Object entry = beans.get(key);
			if(entry instanceof LogMessage){
				lm = (LogMessage) entry;
				break;
			}
		}
		
		if(lm!=null && lm.getDataResourceId()!=null && lm.getDataResourceId()!=0 && lm.getEventId()!=null){
		
			String entityKey = lm.getDataResourceId().toString();
			String entityName = lm.getDataResource().getName();
			Integer eventId = lm.getEventId();
			LogStatsDTO ls = new LogStatsDTO(entityKey, entityName, eventId);
	
			if(logStats.contains(ls)){
				int index = logStats.indexOf(ls);
				ls = logStats.get(index);
			} else {
				logStats.add(ls);
			}
			
			ls.setEventCount(ls.getEventCount().intValue()+1);
			if(lm.getCount()!=null){
				if(ls.getCount()==null){
					ls.setCount(lm.getCount().intValue());
				} else {
					ls.setCount(ls.getCount().intValue()+lm.getCount().intValue());
				}
			}
		}
		
		if(nextResultsOutputter!=null && chainInOnePass)
			nextResultsOutputter.write(beans);		
	}

	/**
	 * Set the ResultsOutputter to call after this one.
	 * 
	 * @param resultsOutputter
	 */
	public void setNextResultsOutputter(ResultsOutputter resultsOutputter){
		this.nextResultsOutputter = resultsOutputter;
	}

	/**
	 * @return the chainInOnePass
	 */
	public boolean isChainInOnePass() {
		return chainInOnePass;
	}

	/**
	 * @param chainInOnePass the chainInOnePass to set
	 */
	public void setChainInOnePass(boolean chainInOnePass) {
		this.chainInOnePass = chainInOnePass;
	}

	/**
	 * @see org.gbif.portal.io.ChainableResultsOutputter#getNextResultsOutputter()
	 */
	public ResultsOutputter getNextResultsOutputter() {
		return this.nextResultsOutputter;
	}

	/**
	 * @return the logStats
	 */
	public List<LogStatsDTO> getLogStats() {
		return logStats;
	}

	/**
	 * @param logStats the logStats to set
	 */
	public void setLogStats(List<LogStatsDTO> logStats) {
		this.logStats = logStats;
	}
}