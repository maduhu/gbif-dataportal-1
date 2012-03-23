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
import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.gbif.portal.model.resources.DataResource;

/**
 * Results outputter that builds records the processed data resources in a result set.
 * 
 * @author dmartin
 */
public class DataResourceAuditor implements ChainableResultsOutputter {

	protected static Log logger = LogFactory.getLog(DataResourceAuditor.class);	
	
	/** The results outputter to call after this one */
	private ResultsOutputter nextResultsOutputter;	
	/** The data resource keys and names  */
	private Map<String, String> dataResources = new LinkedHashMap<String, String>();
	/** A map of data resource id and counts (for the number of times seen) */
	private Map<String, Integer> dataResourcesCounts = new LinkedHashMap<String, Integer>();
	/** Whether to pass the bean map on */
	private boolean chainInOnePass = true;
	/** If a reflection lookup didnt work the first time, give up */
	private boolean reflectionFailed = false;
	/** The cached DR lookup method */
	private Method getDRMethod = null;
	
	/**
	 * @see org.gbif.portal.io.ResultsOutputter#write(java.util.Map)
	 */
	public void write(Map beans) throws IOException {
		if(beans==null)
			return;
		
		Long dataResourceId = (Long) beans.get("dataResourceId");		
		String dataResourceName = (String) beans.get("dataResourceName");
		
		if(dataResourceId==null && !reflectionFailed){
			Object record = beans.get("record");
			try {
				Map description = BeanUtils.describe(record);
				if(description.containsKey("dataResource")){
					if(getDRMethod==null)
						getDRMethod = record.getClass().getMethod("getDataResource", (Class[])null);
					DataResource dr = (DataResource) getDRMethod.invoke(record, (Object[]) null);
					if(dr!=null){
						dataResourceName = dr.getName();
						dataResourceId = dr.getId();
					}
				}
			} catch (Exception e){
				logger.debug(e.getMessage(), e);
				reflectionFailed = true;
			}
		}
		
		if(dataResourceId!=null && dataResourceName!=null &&  !dataResources.containsKey(dataResourceId.toString())){
			dataResources.put(dataResourceId.toString(), dataResourceName);
			dataResourcesCounts.put(dataResourceId.toString(), new Integer(0));
		}
		
		//increment count
		if(dataResourceId!=null && dataResourceName!=null){
			//if not in there initialise
			if(!dataResourcesCounts.containsKey(dataResourceId.toString())){
				dataResourcesCounts.put(dataResourceId.toString(), new Integer(0));
			}
			//increment
			Integer currentCount = dataResourcesCounts.get(dataResourceId.toString());
			dataResourcesCounts.put(dataResourceId.toString(), currentCount.intValue()+1);
		}
		
		if(nextResultsOutputter!=null && chainInOnePass)
			nextResultsOutputter.write(beans);		
	}

	/**
	 * @return the dataResources
	 */
	public Map<String, String> getDataResources() {
		return dataResources;
	}
	
	/**
	 * @return the dataResourcesCounts
	 */
	public Map<String, Integer> getDataResourcesCounts() {
		return dataResourcesCounts;
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
}