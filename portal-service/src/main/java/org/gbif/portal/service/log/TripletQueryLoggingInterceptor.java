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
package org.gbif.portal.service.log;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.gbif.portal.dto.PropertyStoreTripletDTO;
import org.gbif.portal.dto.SearchResultsDTO;
import org.gbif.portal.dto.occurrence.BriefOccurrenceRecordDTO;
import org.gbif.portal.dto.occurrence.KmlOccurrenceRecordDTO;
import org.gbif.portal.util.log.GbifLogMessage;
import org.gbif.portal.util.log.GbifLogUtils;
import org.gbif.portal.util.log.LogEvent;
import org.gbif.portal.util.log.LogGroup;

/**
 * Interceptor that builds and creates log messages for triple queries.
 * Each data resource in the response gets a message of the number of records returned, along
 * with a SUMMARY of the criteria used to get the results.
 * 
 * @author trobertson
 */
public class TripletQueryLoggingInterceptor implements MethodInterceptor {
	/**
	 * Logger
	 */
	protected static Log logger = LogFactory.getLog(TripletQueryLoggingInterceptor.class);
	
	/**
	 * Maps service keys to brief values
	 * E.g. SERVICE.OCCURRENCE.QUERY.SUBJECT.NUBCONCEPTID -> scientific name
	 */
	Map<String, String> serviceKeyToBriefLoggableName;
	
	/**
	 * For when the object needs mapping to a readable value
	 * E.g. TC ID 1 -> Animalia 
	 */
	Map<String, LoggableFromPredicateAndObject> serviceObjectToLoggableStringMappers;
	
	/**
	 * Logging
	 */
	GbifLogUtils gbifLogUtils;
	
	/**
	 * This is making the assumption that it is wired in correctly.  If the arguments are not correct then the 
	 * method is invoked and a warning given.
	 * It expects the first argument to be the:
	 *   List<PropertyStoreTripletDTO> criteria 
	 * 
	 * @see org.aopalliance.intercept.MethodInterceptor#invoke(org.aopalliance.intercept.MethodInvocation)
	 */
	@SuppressWarnings("unchecked")
	public Object invoke(MethodInvocation invocation) throws Throwable {
		Object methodResult = null;
		try {
			logger.debug("Invoking the method");
			methodResult = invocation.proceed();
			return methodResult;
			
		} finally {
			try {
				SearchResultsDTO results = (SearchResultsDTO) methodResult;
				if (results!=null && results.getResults()!= null) {
					if (results.getResults().size()>0) {
						Object[] arguments = invocation.getArguments();
						List<PropertyStoreTripletDTO> criteria = (List<PropertyStoreTripletDTO>) arguments[0];
						StringBuffer messagePreamble = new StringBuffer();
						createMessagePreamble(messagePreamble, criteria);
						Map<String, String> resource2ProviderMap = new HashMap<String, String>();
						Map<String, Integer> drCounts = getCountsPerResource(results, resource2ProviderMap);
						if(logger.isInfoEnabled()){
							for (String key : drCounts.keySet()) {
								logger.info("Resource [" + key + "] - " + messagePreamble + ": " + drCounts.get(key));
								
								LogGroup logGroup = gbifLogUtils.startLogGroup();
								try {
									GbifLogMessage gbifMessage = gbifLogUtils.createGbifLogMessage(logGroup, LogEvent.USAGE_OCCURRENCE_SEARCH);
									gbifMessage.setCount(drCounts.get(key));
									gbifMessage.setDataResourceId(parseKey(key));
									gbifMessage.setDataProviderId(parseKey(resource2ProviderMap.get(key)));
									// TODO - get from a context perhaps?
									gbifMessage.setPortalInstanceId(1L);
									gbifMessage.setTimestamp(new Date());
									gbifMessage.setRestricted(false);
									gbifMessage.setMessage(messagePreamble.toString().trim());
									logger.info(gbifMessage);
									
								} finally {
									gbifLogUtils.endLogGroup(logGroup);
								}														
							}
						}
					}
				}
			} catch (Exception e) { // e.g. the arguments are not correct
				logger.warn("Unable to create logging for triplet query (could be misuse of the interceptor, please read the docs)", e);
			}
		}
	}
	
	/**
	 * Parses the supplied key. Returns null if supplied string invalid
	 * @param key
	 * @return a concept key. Returns null if supplied string invalid key
	 */
	protected static Long parseKey(String key){
		Long parsedKey = null;
		try {
			parsedKey = Long.parseLong(key);
		} catch (NumberFormatException e){
			//expected behaviour for invalid keys
		}
		return parsedKey;
	}
	
	/**
	 * Maps the results into grouped counts for the resources
	 * @param results
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected Map<String, Integer> getCountsPerResource(SearchResultsDTO results, Map<String, String> resource2ProviderMap) {
		Map<String, Integer> drCounts = new HashMap<String, Integer>();
		
		for (Object dtoAsObject : results.getResults()) {
			if (dtoAsObject instanceof BriefOccurrenceRecordDTO) {
				BriefOccurrenceRecordDTO orDTO = (BriefOccurrenceRecordDTO) dtoAsObject;
				String key = orDTO.getDataResourceKey();
				if (drCounts.containsKey(key)) {
					int count = drCounts.get(key);
					count++;
					drCounts.put(key, count);
				} else {
					drCounts.put(key, 1);
				}
				resource2ProviderMap.put(key, orDTO.getDataProviderKey());
				
			} else if (dtoAsObject instanceof KmlOccurrenceRecordDTO) {
				KmlOccurrenceRecordDTO orDTO = (KmlOccurrenceRecordDTO) dtoAsObject;
				String key = orDTO.getDataResourceKey();
				if (drCounts.containsKey(key)) {
					int count = drCounts.get(key);
					count++;
					drCounts.put(key, count);
				} else {
					drCounts.put(key, 1);
				}
				resource2ProviderMap.put(key, orDTO.getDataProviderKey());
			}
		}
		return drCounts;
	}
	
	

	/**
	 * Creates a brief message from the criteria
	 * @param messagePreamble
	 * @param criteria
	 * @return
	 */
	protected void createMessagePreamble(StringBuffer messagePreamble, List<PropertyStoreTripletDTO> criteria) {
		Map<String, Set<String>> subjects = new HashMap<String, Set<String>>();
		
		for (PropertyStoreTripletDTO dto : criteria) {
			String subject = dto.getSubject();
			String loggableSubject = getServiceKeyToBriefLoggableName().get(subject);
			
			// only log the ones we care about - e.g. ones we can make sense of
			if (loggableSubject!=null) {
				String value = null;
				// some of them are null due to "coordinates areNot null" for example
				if (dto.getObject()!=null) {
					value = dto.getObject().toString();
				}
				
				
				// map the object to what it really is - e.g. ID -> loggable string
				if (serviceObjectToLoggableStringMappers.containsKey(subject)) {
					LoggableFromPredicateAndObject converter = serviceObjectToLoggableStringMappers.get(subject);
					value = converter.getLoggable(dto.getPredicate(), dto.getObject());
					if (value == null) {
						if(logger.isDebugEnabled()){
							logger.debug("Unable to get a loggable for key[" + dto.getObject() + "] using [" + converter.getClass().getSimpleName() + "] - ignoring from message");
						}
					}
				}
				
				if (value != null) {
					if (subjects.containsKey(loggableSubject)) {
						subjects.get(loggableSubject).add(value);
					} else {
						Set<String> values = new HashSet<String>();
						values.add(value);
						subjects.put(loggableSubject, values);
					}					
				}
			}
		}
		
		for (String subject : subjects.keySet()) {
			messagePreamble.append(subject + "[");
			for (String value : subjects.get(subject)) {
				messagePreamble.append(value + ",");
			}
			messagePreamble.delete(messagePreamble.lastIndexOf(","),messagePreamble.lastIndexOf(",")+1);
			messagePreamble.append("] ");
		}
	}

	/**
	 * @return Returns the serviceKeyToBriefLoggableName.
	 */
	public Map<String, String> getServiceKeyToBriefLoggableName() {
		return serviceKeyToBriefLoggableName;
	}

	/**
	 * @param serviceKeyToBriefLoggableName The serviceKeyToBriefLoggableName to set.
	 */
	public void setServiceKeyToBriefLoggableName(
			Map<String, String> serviceKeyToBriefLoggableName) {
		this.serviceKeyToBriefLoggableName = serviceKeyToBriefLoggableName;
	}

	/**
	 * @return Returns the serviceObjectToLoggableStringMappers.
	 */
	public Map<String, LoggableFromPredicateAndObject> getServiceObjectToLoggableStringMappers() {
		return serviceObjectToLoggableStringMappers;
	}

	/**
	 * @param serviceObjectToLoggableStringMappers The serviceObjectToLoggableStringMappers to set.
	 */
	public void setServiceObjectToLoggableStringMappers(
			Map<String, LoggableFromPredicateAndObject> serviceObjectToLoggableStringMappers) {
		this.serviceObjectToLoggableStringMappers = serviceObjectToLoggableStringMappers;
	}

	/**
	 * @return Returns the gbifLogUtils.
	 */
	public GbifLogUtils getGbifLogUtils() {
		return gbifLogUtils;
	}

	/**
	 * @param gbifLogUtils The gbifLogUtils to set.
	 */
	public void setGbifLogUtils(GbifLogUtils gbifLogUtils) {
		this.gbifLogUtils = gbifLogUtils;
	}
}