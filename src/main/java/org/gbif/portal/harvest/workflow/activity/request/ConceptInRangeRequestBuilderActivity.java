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
package org.gbif.portal.harvest.workflow.activity.request;

import java.net.InetAddress;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.velocity.VelocityContext;
import org.gbif.portal.util.mhf.criteria.CollectionCriteria;
import org.gbif.portal.util.mhf.criteria.Criteria;
import org.gbif.portal.util.mhf.criteria.TripletCriteria;
import org.gbif.portal.util.propertystore.MisconfiguredPropertyException;
import org.gbif.portal.util.propertystore.PropertyStore;
import org.gbif.portal.util.request.TemplateUtils;
import org.gbif.portal.util.text.DateFormatter;
import org.gbif.portal.util.workflow.BaseActivity;
import org.gbif.portal.util.workflow.ContextCorruptException;
import org.gbif.portal.util.workflow.ProcessContext;


/**
 * An activity to build a concept in range request.
 *
 * @author trobertson
 */
public class ConceptInRangeRequestBuilderActivity extends BaseActivity {
    /**
     * The PS key for the concept to query
     */
    protected String concept;

	/**
	 * LowerLimit for the range
	 */
	protected String lowerLimit;	    

	/**
	 * UpperLimit for the range
	 */
	protected String upperLimit;
	
	/**
	 * LowerLimit for the range - this will override the lower limit if set
	 */
	protected String contextKeyLowerLimit;	    

	/**
	 * UpperLimit for the range - set this to override the upper limit
	 */
	protected String contextKeyUpperLimit;
	
	/**
	 * For the template
	 */
	protected String searchAnd;
	
	/**
	 * For the template
	 */
	protected String searchGreaterThanOrEquals;
	
	/**
	 * For the template
	 */
	protected String searchLessThanOrEquals;
	
	/**
	 * Template utilities
	 */
	protected TemplateUtils templateUtils;
	
	/**
	 * The PropertyStore
	 */
	protected PropertyStore propertyStore;
	
	/**
	 * To place the generated request into the context
	 */
	protected String contextKeyRequest;
	
	/**
	 * The count of records key
	 */
	protected String contextKeyProcessedCount;
	
	/**
	 * To get the PS namespace that we are working in 
	 */
	protected String contextKeyPsNamespaces;
	
	/**
	 * The context key for the endpoint url
	 */
	protected String contextKeyURL;	
	
	/**
	 * The context key for the remote identifier at the url
	 */
	protected String contextKeyRemoteIdentifier;
	
	/**
	 * The context key for the (TAPIR) output model
	 */
	protected String contextKeyOutputModel;
	
	/**
	 * The namespace or location to push into the request
	 * e.g. DIGiR: The schema location 
	 * If not set then the lowest priority psNamespace is used (e.g. don't set for ABCD ;o)
	 */
	protected String requestNamespaceOrLocation;

	/**
	 * For inventory of things that have changed
	 */
	protected String contextKeyDateLastModified;

	/**
	 * Flag to determine if it is to be used
	 */
	protected String contextKeySupportsDateLastModified;
	
	/**
	 * To get the search value from the PS
	 */
	protected String dateLastModifiedPSKey;
	
	/**
	 * The template to use
	 */
	protected String templateLocation;

    /**
     * @see org.gbif.portal.util.workflow.BaseMapContextActivity#doExecute(org.gbif.portal.util.workflow.MapContext)
     */
    @SuppressWarnings("unchecked")
	public ProcessContext execute(ProcessContext context) throws Exception {
    	// handle the override for limits
    	String lowerLimit = this.lowerLimit;
    	String upperLimit = this.upperLimit;
    	if (getContextKeyLowerLimit() != null) {
    		lowerLimit = (String) context.get(getContextKeyLowerLimit(), String.class, true);
    	}
    	if (getContextKeyUpperLimit() != null) {
    		upperLimit = (String) context.get(getContextKeyUpperLimit(), String.class, true);
    	}
    	
    	
    	logger.debug("Creating a limit search [" + lowerLimit + " - " + upperLimit + "] with a startAt[" 
    			+ context.get(getContextKeyProcessedCount(), Object.class, false) + "]");
    	
    	List<String> namespaces = (List<String>) context.get(getContextKeyPsNamespaces(), List.class, true);
    	CollectionCriteria criteria =
			new CollectionCriteria(
					getSearchAnd())
					.add(new TripletCriteria(
							propertyStore.getProperty(namespaces, getConcept(), String.class),
							getSearchGreaterThanOrEquals(),
							lowerLimit.trim()))
					.add(new TripletCriteria(
							propertyStore.getProperty(namespaces, getConcept(), String.class),
							getSearchLessThanOrEquals(),
							upperLimit.trim()));

		Criteria dateLastModifiedCriteria = buildDateLastModifiedCriteria(context, namespaces);
		if (dateLastModifiedCriteria != null) {
			CollectionCriteria criteriaWithDateModified =
				new CollectionCriteria(
						getSearchAnd())
						.add(criteria)
						.add(dateLastModifiedCriteria);
			criteria = criteriaWithDateModified;
		}
		
		VelocityContext velocity = new VelocityContext();
		velocity.put("DateFormatter", new DateFormatter());
		velocity.put("hostAddress", InetAddress.getLocalHost().getHostAddress());
		velocity.put("criteria", criteria);
		velocity.put("destination", context.get(getContextKeyURL(), String.class, true));
		String remoteIdentifier = (String) context.get(getContextKeyRemoteIdentifier(), String.class, false);
		if (StringUtils.isNotEmpty(remoteIdentifier)) {
			velocity.put("resource", remoteIdentifier);
		}
		if (!StringUtils.isEmpty(getRequestNamespaceOrLocation())) {
			velocity.put("contentNamespace", getRequestNamespaceOrLocation());
		} else if (!StringUtils.isEmpty(getContextKeyOutputModel())) {
			velocity.put("contentNamespace", context.get(getContextKeyOutputModel(), String.class, true));
		} else {
			velocity.put("contentNamespace", namespaces.get(namespaces.size()-1));
		}
		velocity.put("startAt", context.get(getContextKeyProcessedCount(), Object.class, false));
		context.put(getContextKeyRequest(), templateUtils.getAndMerge(getTemplateLocation(), velocity));
		return context;
    }

	/**
	 * @param context
	 * @param namespaces
	 * @return
	 * @throws ContextCorruptException
	 * @throws MisconfiguredPropertyException
	 */
	private Criteria buildDateLastModifiedCriteria(ProcessContext context, List<String> namespaces) throws ContextCorruptException, MisconfiguredPropertyException {
    	if ("true".equalsIgnoreCase(System.getProperty("ignore-date-last-modified"))) {
    		logger.warn("Date last modified has been overridden to be ignored");
    		return null;
    	}		
		if (contextKeySupportsDateLastModified != null && 
				(context.get(contextKeySupportsDateLastModified, Boolean.class, false) != null) &&
				((Boolean)context.get(contextKeySupportsDateLastModified, Boolean.class, false))) {
			logger.debug("contextKeyDateLastModified: " + getContextKeyDateLastModified());
			logger.debug("contextKeyDateLastModified value: " + (String)context.get(getContextKeyDateLastModified(), String.class, false));
			
			// build the criteria to handle a date last modified
			Criteria dateLastModifiedCriteria = null;
			if (getContextKeyDateLastModified() != null) {
				String date = (String)context.get(getContextKeyDateLastModified(), String.class, false);
				if (date != null) {
					dateLastModifiedCriteria = new TripletCriteria(
							propertyStore.getProperty(namespaces, getDateLastModifiedPSKey(), String.class),
							getSearchGreaterThanOrEquals(),
							date);				
				}
			}
			return dateLastModifiedCriteria;
		} else {
			logger.info("In the context, the resource does not support the date last modified: " + context.get(contextKeySupportsDateLastModified, Boolean.class, false));
			return null;
		}
		
	}
	
	/**
	 * @return Returns the concept.
	 */
	public String getConcept() {
		return concept;
	}


	/**
	 * @param concept The concept to set.
	 */
	public void setConcept(String concept) {
		this.concept = concept;
	}


	/**
	 * @return the lowerLimit
	 */
	public String getLowerLimit() {
		return lowerLimit;
	}

	/**
	 * @param lowerLimit the lowerLimit to set
	 */
	public void setLowerLimit(String lowerLimit) {
		this.lowerLimit = lowerLimit;
	}

	/**
	 * @return the propertyStore
	 */
	public PropertyStore getPropertyStore() {
		return propertyStore;
	}

	/**
	 * @param propertyStore the propertyStore to set
	 */
	public void setPropertyStore(PropertyStore propertyStore) {
		this.propertyStore = propertyStore;
	}

	/**
	 * @return the searchAnd
	 */
	public String getSearchAnd() {
		return searchAnd;
	}

	/**
	 * @param searchAnd the searchAnd to set
	 */
	public void setSearchAnd(String searchAnd) {
		this.searchAnd = searchAnd;
	}

	/**
	 * @return the searchGreaterThanOrEquals
	 */
	public String getSearchGreaterThanOrEquals() {
		return searchGreaterThanOrEquals;
	}

	/**
	 * @param searchGreaterThanOrEquals the searchGreaterThanOrEquals to set
	 */
	public void setSearchGreaterThanOrEquals(String searchGreaterThanOrEquals) {
		this.searchGreaterThanOrEquals = searchGreaterThanOrEquals;
	}

	/**
	 * @return the searchLessThanOrEquals
	 */
	public String getSearchLessThanOrEquals() {
		return searchLessThanOrEquals;
	}

	/**
	 * @param searchLessThanOrEquals the searchLessThanOrEquals to set
	 */
	public void setSearchLessThanOrEquals(String searchLessThanOrEquals) {
		this.searchLessThanOrEquals = searchLessThanOrEquals;
	}

	/**
	 * @return the templateUtils
	 */
	public TemplateUtils getTemplateUtils() {
		return templateUtils;
	}

	/**
	 * @param templateUtils the templateUtils to set
	 */
	public void setTemplateUtils(TemplateUtils templateUtils) {
		this.templateUtils = templateUtils;
	}

	/**
	 * @return the upperLimit
	 */
	public String getUpperLimit() {
		return upperLimit;
	}

	/**
	 * @param upperLimit the upperLimit to set
	 */
	public void setUpperLimit(String upperLimit) {
		this.upperLimit = upperLimit;
	}

	/**
	 * @return the contextKeyProcessedCount
	 */
	public String getContextKeyProcessedCount() {
		return contextKeyProcessedCount;
	}

	/**
	 * @param contextKeyProcessedCount the contextKeyProcessedCount to set
	 */
	public void setContextKeyProcessedCount(String contextKeyProcessedCount) {
		this.contextKeyProcessedCount = contextKeyProcessedCount;
	}

	/**
	 * @return the contextKeyRequest
	 */
	public String getContextKeyRequest() {
		return contextKeyRequest;
	}

	/**
	 * @param contextKeyRequest the contextKeyRequest to set
	 */
	public void setContextKeyRequest(String contextKeyRequest) {
		this.contextKeyRequest = contextKeyRequest;
	}

	/**
	 * @return the templateLocation
	 */
	public String getTemplateLocation() {
		return templateLocation;
	}

	/**
	 * @param templateLocation the templateLocation to set
	 */
	public void setTemplateLocation(String templateLocation) {
		this.templateLocation = templateLocation;
	}

	/**
	 * @return the contextKeyURL
	 */
	public String getContextKeyURL() {
		return contextKeyURL;
	}

	/**
	 * @param contextKeyURL the contextKeyURL to set
	 */
	public void setContextKeyURL(String contextKeyURL) {
		this.contextKeyURL = contextKeyURL;
	}

	/**
	 * @return Returns the contextKeyPsNamespaces.
	 */
	public String getContextKeyPsNamespaces() {
		return contextKeyPsNamespaces;
	}

	/**
	 * @param contextKeyPsNamespaces The contextKeyPsNamespaces to set.
	 */
	public void setContextKeyPsNamespaces(String contextKeyPsNamespaces) {
		this.contextKeyPsNamespaces = contextKeyPsNamespaces;
	}


	/**
	 * @return Returns the contextKeyRemoteIdentifier.
	 */
	public String getContextKeyRemoteIdentifier() {
		return contextKeyRemoteIdentifier;
	}


	/**
	 * @param contextKeyRemoteIdentifier The contextKeyRemoteIdentifier to set.
	 */
	public void setContextKeyRemoteIdentifier(String contextKeyRemoteIdentifier) {
		this.contextKeyRemoteIdentifier = contextKeyRemoteIdentifier;
	}


	/**
	 * @return Returns the requestNamespaceOrLocation.
	 */
	public String getRequestNamespaceOrLocation() {
		return requestNamespaceOrLocation;
	}


	/**
	 * @param requestNamespaceOrLocation The requestNamespaceOrLocation to set.
	 */
	public void setRequestNamespaceOrLocation(String requestNamespaceOrLocation) {
		this.requestNamespaceOrLocation = requestNamespaceOrLocation;
	}


	/**
	 * @return Returns the contextKeyLowerLimit.
	 */
	public String getContextKeyLowerLimit() {
		return contextKeyLowerLimit;
	}


	/**
	 * @param contextKeyLowerLimit The contextKeyLowerLimit to set.
	 */
	public void setContextKeyLowerLimit(String contextKeyLowerLimit) {
		this.contextKeyLowerLimit = contextKeyLowerLimit;
	}


	/**
	 * @return Returns the contextKeyUpperLimit.
	 */
	public String getContextKeyUpperLimit() {
		return contextKeyUpperLimit;
	}


	/**
	 * @param contextKeyUpperLimit The contextKeyUpperLimit to set.
	 */
	public void setContextKeyUpperLimit(String contextKeyUpperLimit) {
		this.contextKeyUpperLimit = contextKeyUpperLimit;
	}


	/**
	 * @return the contextKeyOutputModel
	 */
	public String getContextKeyOutputModel() {
		return contextKeyOutputModel;
	}


	/**
	 * @param contextKeyOutputModel the contextKeyOutputModel to set
	 */
	public void setContextKeyOutputModel(String contextKeyOutputModel) {
		this.contextKeyOutputModel = contextKeyOutputModel;
	}


	/**
	 * @return Returns the contextKeyDateLastModified.
	 */
	public String getContextKeyDateLastModified() {
		return contextKeyDateLastModified;
	}


	/**
	 * @param contextKeyDateLastModified The contextKeyDateLastModified to set.
	 */
	public void setContextKeyDateLastModified(String contextKeyDateLastModified) {
		this.contextKeyDateLastModified = contextKeyDateLastModified;
	}


	/**
	 * @return Returns the dateLastModifiedPSKey.
	 */
	public String getDateLastModifiedPSKey() {
		return dateLastModifiedPSKey;
	}


	/**
	 * @param dateLastModifiedPSKey The dateLastModifiedPSKey to set.
	 */
	public void setDateLastModifiedPSKey(String dateLastModifiedPSKey) {
		this.dateLastModifiedPSKey = dateLastModifiedPSKey;
	}

	/**
	 * @return Returns the contextKeySupportsDateLastModified.
	 */
	public String getContextKeySupportsDateLastModified() {
		return contextKeySupportsDateLastModified;
	}

	/**
	 * @param contextKeySupportsDateLastModified The contextKeySupportsDateLastModified to set.
	 */
	public void setContextKeySupportsDateLastModified(
			String contextKeySupportsDateLastModified) {
		this.contextKeySupportsDateLastModified = contextKeySupportsDateLastModified;
	}
}