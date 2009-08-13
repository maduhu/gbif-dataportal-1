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
package org.gbif.portal.web.controller.dataset;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.gbif.portal.dto.KeyValueDTO;
import org.gbif.portal.dto.SearchResultsDTO;
import org.gbif.portal.dto.resources.DataProviderDTO;
import org.gbif.portal.dto.resources.DataResourceDTO;
import org.gbif.portal.service.DataResourceManager;
import org.gbif.portal.service.LogManager;
import org.gbif.portal.service.ServiceException;
import org.gbif.portal.util.log.LogEvent;
import org.gbif.portal.web.content.PagingQueryContentProvider;
import org.gbif.portal.web.controller.RestKeyValueController;
import org.gbif.portal.web.download.DownloadUtils;
import org.gbif.portal.web.download.Field;
import org.gbif.portal.web.download.FileWriterFactory;
import org.gbif.portal.web.download.LogEventField;
import org.gbif.portal.web.download.SecondaryOutput;
import org.gbif.portal.web.util.DateUtil;
import org.springframework.context.MessageSource;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.support.RequestContextUtils;
import org.springframework.web.servlet.view.RedirectView;

/**
 * Data Provider log viewing/download controller.
 * 
 * @author dmartin
 */
public class DataProviderLogController extends RestKeyValueController{
	
	/** Service layer managers */
	protected DataResourceManager dataResourceManager;
	protected LogManager logManager;
	
	/** Maps formats to a FileWriterFactory */ 
	protected Map<String, FileWriterFactory> format2FileWriterFactories;
	
	/** Paging content provider */
	protected PagingQueryContentProvider pagingQueryContentProvider;
	
	/** Message source for i18n */
	protected MessageSource messageSource;
	
	/** Request parameters*/
	protected String providerRequestKey = "provider";
	protected String resourceRequestKey = "resource";
	protected String occurrenceRecordRequestKey = "occurrenceRecord";
	protected String eventRequestKey = "event";
	protected String logGroupRequestKey = "logGroup";
	protected String logLevelRequestKey = "logLevel";
	protected String taxonConceptRequestKey = "taxonConcept";
	protected String startDateRequestKey = "startDate";
	protected String endDateRequest3Key = "endDate";
	protected String searchResultsModelKey = "searchResults";

	/** Request parameters*/
	protected String dataProviderModelKey = "dataProvider";
	protected String dataResourcesModelKey = "dataResources";
	protected String eventsModelKey = "events";	
	protected String loggingLevelsModelKey = "loggingLevels";
	
	/** The view to use */
	protected String logView = "logTable";
	
	/** Configured log4j levels to recognise and allow filtering for */
	protected List<KeyValueDTO> loggingLevels;

	/** The events ranges */
	protected static String allHarvestEventsRequestKey="1-999";
	protected static String allExtractEventsRequestKey="1001-1999";
	protected static String allExtractIssuesEventsRequestKey="1003-1999";
	protected static String allUserEventsRequestKey="2001-2999";	
	protected static String allUsageEventsRequestKey="3001-3999";

	/** Request key indicating whether to start download */
	protected String downloadRequestKey = "download";

	/** Default download format */
	protected String defaultDownloadFormat = "tab";
	
	protected int maxLogDownload = 250000;
	
	/** Message ranges */
	protected static Map<String, String> messageRangeKeys;
	static {
		messageRangeKeys = new HashMap<String, String>();
		messageRangeKeys.put(allHarvestEventsRequestKey, "harvestAll");
		messageRangeKeys.put(allExtractEventsRequestKey, "extractAll");
		messageRangeKeys.put(allExtractIssuesEventsRequestKey, "extractAllIssues");
		messageRangeKeys.put(allUserEventsRequestKey, "userAll");
		messageRangeKeys.put(allUsageEventsRequestKey, "usageAll");		
	}
	
	/**
	 * @see org.gbif.portal.web.controller.RestController#handleRequest(java.util.Map, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public ModelAndView handleRequest(Map<String, String> propertiesMap, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		//create a logQuery from the request
		LogQuery logQuery = new LogQuery();
		logQuery.setLogManager(logManager);
		logQuery.setDataResourceManager(dataResourceManager);
		logQuery.setMessageSource(messageSource);
		
		//resource key
		String resourceKey = propertiesMap.get(resourceRequestKey);
		DataResourceDTO dataResource = null;
		if(resourceKey!=null){
			dataResource = dataResourceManager.getDataResourceFor(resourceKey);
		} else {
			resourceKey = request.getParameter(resourceRequestKey);
		}
		logQuery.setResourceKey(resourceKey);
		
		//provider key
		DataProviderDTO dataProvider = null;		
		String providerKey = null;
		if(dataResource!=null){
			providerKey = dataResource.getDataProviderKey();
			dataProvider = dataResourceManager.getDataProviderFor(providerKey);
		} else if(propertiesMap.get(providerRequestKey)!=null){
			providerKey = propertiesMap.get(providerRequestKey);	
		} else {
			providerKey = request.getParameter(providerRequestKey);
		}
		
		if(dataProvider==null && providerKey!=null){
			dataProvider = dataResourceManager.getDataProviderFor(providerKey);
		}		
		
		//set provider key
		logQuery.setProviderKey(providerKey);		
		
		//event
		String eventKey = request.getParameter(eventRequestKey);
		logQuery.setEventKey(eventKey);
		
		//is a range specified?
		if(messageRangeKeys.keySet().contains(eventKey)){
			logQuery.setMinEventKey(eventKey.substring(0,eventKey.indexOf('-')));
			logQuery.setMaxEventKey(eventKey.substring(eventKey.indexOf('-')+1));
			logQuery.setEventName(messageRangeKeys.get(eventKey));
		} else {
			if(StringUtils.isNotEmpty(eventKey) && LogEvent.get(eventKey)!=null)
				logQuery.setEventName(LogEvent.get(eventKey).getName());			
		}
		
		//the specified taxon
		logQuery.setTaxonConceptKey(request.getParameter(taxonConceptRequestKey));

		//the occurrence record
		logQuery.setOccurrenceRecordKey(request.getParameter(occurrenceRecordRequestKey));
		
		//retrieve log group
		String logGroupKey = request.getParameter(logGroupRequestKey);
		logQuery.setLogGroupKey(logGroupKey);
		
		//retrieve logging levels
		String logLevelKey = request.getParameter(logLevelRequestKey);
		Long logLevel = null;
		if(StringUtils.isNotEmpty(logLevelKey)){
			try {
				logLevel = Long.parseLong(logLevelKey);
				if(logLevel==0){
					logger.debug("ALL log level selected.");
					logLevel = null;
				}
			} catch (NumberFormatException e){
				logger.warn("Bad log level supplied:  "+logLevelKey);
			}
		}
		
		//log level
		logQuery.setLogLevel(logLevel);
		
		//set the upper/lower time boundary			
		Date startDate = DateUtil.getDateFromRequest(request, "sd");
		Date endDate = DateUtil.getDateFromRequest(request, "ed");
		logQuery.setStartDate(startDate = DateUtil.setTime(startDate, 0, 0, 0));
		logQuery.setEndDate(DateUtil.setTime(endDate, 23, 59, 59));
		logQuery.setQueryDate(new Date());

		StringBuffer queryUrl = new StringBuffer();
		queryUrl.append('?');
		Map parameterMap = request.getParameterMap();
		Set<String> keySet = parameterMap.keySet();
		for(String key: keySet){
			if(!downloadRequestKey.equals(key)){
				String[] paramvalues = (String[]) parameterMap.get(key);
				for(int i=0; i<paramvalues.length; i++){
					if(paramvalues[i]!=null && paramvalues[i].length()>0){
						queryUrl.append('&');
						queryUrl.append(key);
						queryUrl.append('=');
						queryUrl.append(paramvalues[i]);
					}
				}
			}
		}
		
		StringBuffer host = new StringBuffer();
		host.append("http://");
		host.append(request.getHeader("host"));
		host.append(request.getRequestURI());
		logQuery.setQueryUrl(host.toString() + queryUrl.toString());

		//to download or not?
		boolean download = ServletRequestUtils.getBooleanParameter(request, downloadRequestKey, false);
		if(download){
			
			//get the download format
			String format = ServletRequestUtils.getStringParameter(request, "format", defaultDownloadFormat);
			
			//start download process
			FileWriterFactory fwf = format2FileWriterFactories.get(format);
			
			logQuery.setProcessLimit(250000);
			List<SecondaryOutput> secondaryOutputs = new ArrayList<SecondaryOutput>();
			secondaryOutputs.add(logQuery);

			//set up custom fields
			LogEventField lef = new LogEventField();
			lef.setFieldName("record.eventId");
			List<Field> downloadFields = new ArrayList<Field>();
			downloadFields.add(lef);
			
			String fileId = Long.toString(System.currentTimeMillis());
			String downloadFile = DownloadUtils.startFileWriter(request, response, downloadFields, null, logQuery, secondaryOutputs, "Log messages download", fwf, "log-messages-", fileId, true);
			DownloadUtils.writeDownloadToDescriptor(request, FilenameUtils.removeExtension(downloadFile), queryUrl.toString(), "download.file.type.log.messages", "Log message query", false, null);
			ModelAndView mav = new ModelAndView(new RedirectView("/download/preparingDownload.htm?downloadFile="+downloadFile, true));
			return mav;
		} else {
			return createLogView(request, response, logQuery, dataProvider, dataResource);			
		}
	}

	/**
	 * Set up the view for the log viewer.
	 * 
	 * @param request
	 * @param response
	 * @param logQuery
	 * @param providerKey
	 * @param dataResource
	 * @return
	 * @throws ServiceException
	 * @throws Exception
	 */
	private ModelAndView createLogView(HttpServletRequest request, HttpServletResponse response, LogQuery logQuery,DataProviderDTO dataProvider, DataResourceDTO dataResource) throws ServiceException, Exception {
		//prepare the log view
		ModelAndView mav = new ModelAndView(logView);
		if(dataResource!=null)
			mav.addObject("dataResource", dataResource);
		
		//add the data provider and resource to the request
		if(dataProvider!=null){
			mav.addObject(dataProviderModelKey, dataProvider);
			if(dataResource==null){
				List<DataResourceDTO> dataResources = dataResourceManager.getDataResourcesForProvider(dataProvider.getKey());
				mav.addObject(dataResourcesModelKey, dataResources);
			}
		}
		
		//add dropdown content
		addDropDownContent(request, mav);
		
		//log levels
		if(loggingLevels!=null)
			mav.addObject(loggingLevelsModelKey, loggingLevels);
		
		//do the query
		SearchResultsDTO searchResults = pagingQueryContentProvider.doQuery(logQuery, request, response);
		mav.addObject(searchResultsModelKey, searchResults.getResults());
		return mav;
	}

	/**
	 * Adds the drop down content.
	 * @param request
	 * @param mav
	 */
	@SuppressWarnings("unchecked")
	private void addDropDownContent(HttpServletRequest request, ModelAndView mav) {
		
		//add today and a year from today
		Date today = new Date(System.currentTimeMillis());
		mav.addObject("today", today);
		Date lastWeek = DateUtils.addDays(today, -6);
		mav.addObject("lastWeek", lastWeek);
		Date oneMonthAgo = DateUtils.addMonths(today, -1);
		mav.addObject("oneMonthAgo", oneMonthAgo);
		Date oneYearAgo = DateUtils.addYears(today, -1);
		mav.addObject("oneYearAgo", oneYearAgo);
		
		//add event enumerations
		Collection<LogEvent> logEvents = (Collection)LogEvent.getValueMap().values();
		//split into 4 categories - Harvest, Extract, User and Other
		List<KeyValueDTO> harvestEvents = new ArrayList<KeyValueDTO>();
		List<KeyValueDTO> extractEvents = new ArrayList<KeyValueDTO>();
		List<KeyValueDTO> userEvents = new ArrayList<KeyValueDTO>();
		List<KeyValueDTO> usageEvents = new ArrayList<KeyValueDTO>();
		List<KeyValueDTO> otherEvents = new ArrayList<KeyValueDTO>();
		
		Locale locale = RequestContextUtils.getLocale(request);
		
		for(LogEvent logEvent: logEvents){
			String name = messageSource.getMessage(logEvent.getName(), null, logEvent.getName(), locale);
			KeyValueDTO keyValue = new KeyValueDTO(logEvent.getValue().toString(), name);
			if(logEvent.getValue()>=LogEvent.HARVEST_RANGE_START && logEvent.getValue()<=LogEvent.HARVEST_RANGE_END)
				harvestEvents.add(keyValue);
			else if(logEvent.getValue()>=LogEvent.EXTRACT_RANGE_START && logEvent.getValue()<=LogEvent.EXTRACT_RANGE_END)
				extractEvents.add(keyValue);
			else if(logEvent.getValue()>=LogEvent.USER_RANGE_START && logEvent.getValue()<=LogEvent.USER_RANGE_END)
				userEvents.add(keyValue);
			else if(logEvent.getValue()>=LogEvent.USAGE_RANGE_START && logEvent.getValue()<=LogEvent.USAGE_RANGE_END)
				usageEvents.add(keyValue);
			else
				otherEvents.add(keyValue);
		}
		
		//sort alphabetically
		Collections.sort(harvestEvents, new KeyValueDTO.ValueComparator());
		Collections.sort(extractEvents, new KeyValueDTO.ValueComparator());
		Collections.sort(userEvents, new KeyValueDTO.ValueComparator());
		Collections.sort(otherEvents, new KeyValueDTO.ValueComparator());
		
		if(!harvestEvents.isEmpty() && harvestEvents.size()>1){
			harvestEvents.add(0,new KeyValueDTO(allHarvestEventsRequestKey, messageSource.getMessage("harvestAll", null, "Harvest - all", locale)));
		}
		mav.addObject("harvestEvents", harvestEvents);
		
		if(!extractEvents.isEmpty() && extractEvents.size()>1){
			extractEvents.add(0,new KeyValueDTO(allExtractEventsRequestKey, messageSource.getMessage("extractAll", null, "Extract - all", locale)));
			extractEvents.add(1,new KeyValueDTO(allExtractIssuesEventsRequestKey, messageSource.getMessage("extractAllIssues", null, "Extract - all issues", locale)));
		}
		mav.addObject("extractEvents", extractEvents);
		
		if(!userEvents.isEmpty() && userEvents.size()>1){
			userEvents.add(0,new KeyValueDTO(allUserEventsRequestKey, messageSource.getMessage("userAll", null, "User - all", locale)));
		}
		mav.addObject("userEvents", userEvents);
		
		if(!usageEvents.isEmpty() && usageEvents.size()>1){
			usageEvents.add(0,new KeyValueDTO(allUsageEventsRequestKey, messageSource.getMessage("usageAll", null, "Usage - all", locale)));
		}
		mav.addObject("usageEvents", usageEvents);
		mav.addObject("otherEvents", otherEvents);
	}

	/**
	 * @param logManager the logManager to set
	 */
	public void setLogManager(LogManager logManager) {
		this.logManager = logManager;
	}

	/**
	 * @param providerRequestKey the providerRequestKey to set
	 */
	public void setProviderRequestKey(String providerRequestKey) {
		this.providerRequestKey = providerRequestKey;
	}

	/**
	 * @param resourceRequestKey the resourceRequestKey to set
	 */
	public void setResourceRequestKey(String resourceRequestKey) {
		this.resourceRequestKey = resourceRequestKey;
	}

	/**
	 * @param dataResourceManager the dataResourceManager to set
	 */
	public void setDataResourceManager(DataResourceManager dataResourceManager) {
		this.dataResourceManager = dataResourceManager;
	}

	/**
	 * @param logView the logView to set
	 */
	public void setLogView(String logView) {
		this.logView = logView;
	}

	/**
	 * @param searchResultsModelKey the searchResultsModelKey to set
	 */
	public void setSearchResultsModelKey(String searchResultsModelKey) {
		this.searchResultsModelKey = searchResultsModelKey;
	}

	/**
	 * @param dataProviderModelKey the dataProviderModelKey to set
	 */
	public void setDataProviderModelKey(String dataProviderModelKey) {
		this.dataProviderModelKey = dataProviderModelKey;
	}

	/**
	 * @param dataResourcesModelKey the dataResourcesModelKey to set
	 */
	public void setDataResourcesModelKey(String dataResourcesModelKey) {
		this.dataResourcesModelKey = dataResourcesModelKey;
	}

	/**
	 * @param endDateRequest3Key the endDateRequest3Key to set
	 */
	public void setEndDateRequest3Key(String endDateRequest3Key) {
		this.endDateRequest3Key = endDateRequest3Key;
	}

	/**
	 * @param eventRequestKey the eventRequestKey to set
	 */
	public void setEventRequestKey(String eventRequestKey) {
		this.eventRequestKey = eventRequestKey;
	}

	/**
	 * @param occurrenceRecordRequestKey the occurrenceRecordRequestKey to set
	 */
	public void setOccurrenceRecordRequestKey(String occurrenceRecordRequestKey) {
		this.occurrenceRecordRequestKey = occurrenceRecordRequestKey;
	}

	/**
	 * @param startDateRequestKey the startDateRequestKey to set
	 */
	public void setStartDateRequestKey(String startDateRequestKey) {
		this.startDateRequestKey = startDateRequestKey;
	}

	/**
	 * @param taxonConceptRequestKey the taxonConceptRequestKey to set
	 */
	public void setTaxonConceptRequestKey(String taxonConceptRequestKey) {
		this.taxonConceptRequestKey = taxonConceptRequestKey;
	}

	/**
	 * @param eventsModelKey the eventsModelKey to set
	 */
	public void setEventsModelKey(String eventsModelKey) {
		this.eventsModelKey = eventsModelKey;
	}

	/**
	 * @param loggingLevels the loggingLevels to set
	 */
	public void setLoggingLevels(List<KeyValueDTO> loggingLevels) {
		this.loggingLevels = loggingLevels;
	}

	/**
	 * @param logGroupRequestKey the logGroupRequestKey to set
	 */
	public void setLogGroupRequestKey(String logGroupRequestKey) {
		this.logGroupRequestKey = logGroupRequestKey;
	}

	/**
	 * @param loggingLevelsModelKey the loggingLevelsModelKey to set
	 */
	public void setLoggingLevelsModelKey(String loggingLevelsModelKey) {
		this.loggingLevelsModelKey = loggingLevelsModelKey;
	}

	/**
	 * @param messageSource the messageSource to set
	 */
	public void setMessageSource(MessageSource messageSource) {
		this.messageSource = messageSource;
	}

	/**
	 * @param logLevelRequestKey the logLevelRequestKey to set
	 */
	public void setLogLevelRequestKey(String logLevelRequestKey) {
		this.logLevelRequestKey = logLevelRequestKey;
	}

	/**
	 * @param pagingQueryContentProvider the pagingQueryContentProvider to set
	 */
	public void setPagingQueryContentProvider(
			PagingQueryContentProvider pagingQueryContentProvider) {
		this.pagingQueryContentProvider = pagingQueryContentProvider;
	}

	/**
	 * @param format2FileWriterFactories the format2FileWriterFactories to set
	 */
	public void setFormat2FileWriterFactories(
			Map<String, FileWriterFactory> format2FileWriterFactories) {
		this.format2FileWriterFactories = format2FileWriterFactories;
	}
}