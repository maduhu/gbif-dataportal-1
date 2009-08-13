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
package org.gbif.portal.web.controller.filter;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.gbif.portal.dto.PropertyStoreTripletDTO;
import org.gbif.portal.dto.util.SearchConstraints;
import org.gbif.portal.io.OutputProperty;
import org.gbif.portal.service.triplet.TripletQueryManager;
import org.gbif.portal.util.propertystore.PropertyStore;
import org.gbif.portal.web.content.filter.TripletQuery;
import org.gbif.portal.web.controller.RestController;
import org.gbif.portal.web.download.DownloadUtils;
import org.gbif.portal.web.download.Field;
import org.gbif.portal.web.download.FileWriterFactory;
import org.gbif.portal.web.filter.CriteriaDTO;
import org.gbif.portal.web.filter.CriteriaUtil;
import org.gbif.portal.web.filter.FilterMapWrapper;
import org.gbif.portal.web.filter.FilterUtils;
import org.gbif.portal.web.util.QueryHelper;
import org.springframework.context.MessageSource;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.support.RequestContextUtils;
import org.springframework.web.servlet.view.RedirectView;

/**
 * Controller that supports the download of filter query results in multiple formats.
 * 
 * @author dmartin
 */
public class FilterDownloadController extends RestController {

	protected static Log logger = LogFactory.getLog(FilterDownloadController.class);
	/** Filters to use */
	protected FilterMapWrapper filters;
	/** Fields available for download */
	protected List<Field> downloadFields;
	/** Views of different formats */
	protected Map<String, View> downloadViews;
	/** QueryHelper to produce triplets */
	protected QueryHelper queryHelper;
	/** Message source for i18n messages */
	protected MessageSource messageSource;
	/** Configurable download limit */
	protected int maxResultsDownload = 50000;
	/** results model key */
	protected String criteriaRequestKey = "criteria";
	/** request fields model key */
	protected String requestedFieldsModelKey = "requestedFields";
	/** The record count to retrieve */
	protected String recordCountRequestKey = "recordCount";
	/** Max download overide */
	protected String maxDownloadOverrideRequestKey = "maxDownloadOverride";
	/** The download format - used to chose the FileWriterFactory instance */
	protected String formatRequestKey = "format";
	/** The search id request key */
	protected String searchIdRequestKey = "searchId";
	/** allFields request key - allowing all the fields to be downloaded without specifying */
	protected String allFieldsRequestKey = "allFields";
	/** The file name prefix for the prepared file */
	protected String downloadFilenamePrefix = "occurrence-search-";
	/** Whether downloads should be zipped */
	protected boolean zipped = true;
	/** The property store to use */
	protected PropertyStore propertyStore;
	/** The property store namespace to use for the downloads - defaults to the namespace for occurrences */
	protected String namespace = "http://gbif.org/portal-service/occurrenceOutput/2006/1.0"; 
	/** The service layer property store key for the download fields */
	protected String downloadFieldPSKey = "downloadFields";
	/** Maps formats to a FileWriterFactory */
	protected Map<String, FileWriterFactory> format2FileWriterFactories;
	/** Maps formats to a query manager */
	protected Map<String, TripletQueryManager> format2TripletQueryManager;
	/** The view to direct to once the thread has been started */
	protected String prepareView = "occurrenceDownloadPreparing";
	/** Whether or conditions should be met for the query or any */
	protected boolean matchAll = true;
	/** The file extension for zipped files */
	protected String zippedFileExtension = ".zip";
	/** The base url that this search can be traced to e.g. occurrences/search.htm? */
	protected String searchUrl;
	/** Whether or not time taken should be displayed for this download */
	protected boolean displayTimeTaken = true;
	/** i18n property used in the download descriptor file */
	protected String downloadFileType;
	
	/**
	 * @see org.gbif.portal.web.controller.RestController#handleRequest(java.util.Map, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	public ModelAndView handleRequest(Map<String, String> propertiesMap, HttpServletRequest request, HttpServletResponse response) throws Exception {
	
		//retrieve the request record total
		int recordCount = ServletRequestUtils.getIntParameter(request, recordCountRequestKey, maxResultsDownload);
		int maxDownloadOverrideRequestKey = ServletRequestUtils.getIntParameter(request, this.maxDownloadOverrideRequestKey, -1);
		if(recordCount>maxResultsDownload){
			recordCount=maxResultsDownload;
		}
		if(maxDownloadOverrideRequestKey!=-1){
			recordCount = maxDownloadOverrideRequestKey;
		}
		
		//retrieve the supplied format
		String format = request.getParameter(formatRequestKey);

		//unravel the criteria
		Locale locale = RequestContextUtils.getLocale(request);
		String criteriaString = request.getParameter(criteriaRequestKey);
		CriteriaDTO criteria = CriteriaUtil.getCriteria(criteriaString, filters.getFilters(), locale);
		List<PropertyStoreTripletDTO> triplets = queryHelper.getTriplets(filters.getFilters(), criteria, request, response);
		
		//retrieve the download field mappings
		Map<String, OutputProperty> downloadFieldMappings = (Map<String, OutputProperty>) propertyStore.getProperty(namespace, downloadFieldPSKey);

		//create the output process
		TripletQuery outputProcess = new TripletQuery();
		outputProcess.setTriplets(triplets);
		outputProcess.setMatchAll(matchAll);
		outputProcess.setSearchConstraints(new SearchConstraints(0, recordCount));
		TripletQueryManager tqm = format2TripletQueryManager.get(format);
		outputProcess.setTripletQueryManager(tqm);		

		//retrieve the query description
		String queryDescription = FilterUtils.getQueryDescription(filters.getFilters(), criteria, messageSource, locale);
		
		//create a list of the requested fields
		List<Field> requestedFields = new ArrayList<Field>();
		
		if(request.getParameter(allFieldsRequestKey)!=null){
			logger.debug("All fields requested..");
			requestedFields.addAll(downloadFields);
			logger.debug("Requested fields: "+requestedFields.toString());
		} else {
			for (Field field: downloadFields){
				if(request.getParameter(field.getFieldName())!=null)
					requestedFields.add(field);	
			}
		}
		
		//get the file writer for the chosen format
		FileWriterFactory fwFactory = format2FileWriterFactories.get(format);
		if(fwFactory==null)
			return redirectToDefaultView();
		
		//search id
		String searchId = request.getParameter(searchIdRequestKey);
		String downloadFile = DownloadUtils.startFileWriter(request, response, requestedFields, downloadFieldMappings, outputProcess, null, queryDescription, fwFactory, downloadFilenamePrefix, searchId, zipped);
		
		String filenameWithExt = FilenameUtils.removeExtension(downloadFile);
		
		String criteriaUrl = CriteriaUtil.getUrl(criteria);
		String originalUrl = searchUrl+criteriaUrl;
		
		DownloadUtils.writeDownloadToDescriptor(request, filenameWithExt, originalUrl, downloadFileType, queryDescription, displayTimeTaken, null);
		//redirect to download preparing page
		ModelAndView mav = new ModelAndView(new RedirectView("/download/preparingDownload.htm?downloadFile="+downloadFile, true));
		return mav;
	}

	/**
	 * @param downloadFields the downloadFields to set
	 */
	public void setDownloadFields(List<Field> downloadFields) {
		this.downloadFields = downloadFields;
	}

	/**
	 * @param downloadViews the downloadViews to set
	 */
	public void setDownloadViews(Map<String, View> downloadViews) {
		this.downloadViews = downloadViews;
	}

	/**
	 * @param formatRequestKey the formatRequestKey to set
	 */
	public void setFormatRequestKey(String formatRequestKey) {
		this.formatRequestKey = formatRequestKey;
	}

	/**
	 * @param maxResultsDownload the maxResultsDownload to set
	 */
	public void setMaxResultsDownload(int maxResultsDownload) {
		this.maxResultsDownload = maxResultsDownload;
	}

	/**
	 * @param occurrenceFilters the occurrenceFilters to set
	 */
	public void setFilters(FilterMapWrapper occurrenceFilters) {
		this.filters = occurrenceFilters;
	}

	/**
	 * @param queryHelper the queryHelper to set
	 */
	public void setQueryHelper(QueryHelper queryHelper) {
		this.queryHelper = queryHelper;
	}

	/**
	 * @param recordCountRequestKey the recordCountRequestKey to set
	 */
	public void setRecordCountRequestKey(String recordCountRequestKey) {
		this.recordCountRequestKey = recordCountRequestKey;
	}

	/**
	 * @param requestedFieldsModelKey the requestedFieldsModelKey to set
	 */
	public void setRequestedFieldsModelKey(String requestedFieldsModelKey) {
		this.requestedFieldsModelKey = requestedFieldsModelKey;
	}

	/**
	 * @param messageSource the messageSource to set
	 */
	public void setMessageSource(MessageSource messageSource) {
		this.messageSource = messageSource;
	}

	/**
	 * @param criteriaRequestKey the criteriaRequestKey to set
	 */
	public void setCriteriaRequestKey(String criteriaRequestKey) {
		this.criteriaRequestKey = criteriaRequestKey;
	}

	/**
	 * @param downloadFilenamePrefix the downloadFilenamePrefix to set
	 */
	public void setDownloadFilenamePrefix(String downloadFilenamePrefix) {
		this.downloadFilenamePrefix = downloadFilenamePrefix;
	}

	/**
	 * @param propertyStore the propertyStore to set
	 */
	public void setPropertyStore(PropertyStore propertyStore) {
		this.propertyStore = propertyStore;
	}

	/**
	 * @param searchIdRequestKey the searchIdRequestKey to set
	 */
	public void setSearchIdRequestKey(String searchIdRequestKey) {
		this.searchIdRequestKey = searchIdRequestKey;
	}

	/**
	 * @param zipped the zipped to set
	 */
	public void setZipped(boolean zipped) {
		this.zipped = zipped;
	}

	/**
	 * @param downloadFieldPSKey the downloadFieldPSKey to set
	 */
	public void setDownloadFieldPSKey(String downloadFieldPSKey) {
		this.downloadFieldPSKey = downloadFieldPSKey;
	}

	/**
	 * @param format2FileWriterFactories the format2FileWriterFactories to set
	 */
	public void setFormat2FileWriterFactories(
			Map<String, FileWriterFactory> format2FileWriterFactories) {
		this.format2FileWriterFactories = format2FileWriterFactories;
	}

	/**
	 * @param namespace the namespace to set
	 */
	public void setNamespace(String namespace) {
		this.namespace = namespace;
	}

	/**
	 * @param format2TripletQueryManager the format2TripletQueryManager to set
	 */
	public void setFormat2TripletQueryManager(
			Map<String, TripletQueryManager> format2TripletQueryManager) {
		this.format2TripletQueryManager = format2TripletQueryManager;
	}

	/**
	 * @param matchAll the matchAll to set
	 */
	public void setMatchAll(boolean matchAll) {
		this.matchAll = matchAll;
	}

	/**
	 * @param prepareView the prepareView to set
	 */
	public void setPrepareView(String prepareView) {
		this.prepareView = prepareView;
	}

	/**
	 * @param zippedFileExtension the zippedFileExtension to set
	 */
	public void setZippedFileExtension(String zippedFileExtension) {
		this.zippedFileExtension = zippedFileExtension;
	}

	/**
	 * @param searchUrl the searchUrl to set
	 */
	public void setSearchUrl(String searchUrl) {
		this.searchUrl = searchUrl;
	}

	/**
	 * @param displayTimeTaken the displayTimeTaken to set
	 */
	public void setDisplayTimeTaken(boolean displayTimeTaken) {
		this.displayTimeTaken = displayTimeTaken;
	}

	/**
	 * @param downloadFileType the downloadFileType to set
	 */
	public void setDownloadFileType(String downloadFileType) {
		this.downloadFileType = downloadFileType;
	}
}