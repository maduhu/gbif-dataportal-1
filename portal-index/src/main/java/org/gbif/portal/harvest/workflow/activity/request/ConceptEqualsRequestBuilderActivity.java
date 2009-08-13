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
import org.gbif.portal.util.mhf.criteria.Criteria;
import org.gbif.portal.util.mhf.criteria.TripletCriteria;
import org.gbif.portal.util.propertystore.PropertyStore;
import org.gbif.portal.util.request.TemplateUtils;
import org.gbif.portal.util.text.DateFormatter;
import org.gbif.portal.util.workflow.BaseActivity;
import org.gbif.portal.util.workflow.ProcessContext;


/**
 * An activity to build a concept in range request.
 *
 * @author trobertson
 */
public class ConceptEqualsRequestBuilderActivity extends BaseActivity {
    /**
     * The PS key for the concept to query
     */
    protected String concept;

	/**
	 * The value in the context that the concept must equal
	 */
	protected String contextKeyConceptValue;
	
	/**
	 * For the template
	 */
	protected String searchEquals;
	
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
	 * The namespace or location to push into the request
	 * e.g. DIGiR: The schema location 
	 * If not set then the lowest priority psNamespace is used (e.g. don't set for ABCD ;o)
	 */
	protected String requestNamespaceOrLocation;
	
	/**
	 * The template to use
	 */
	protected String templateLocation;

    /**
     * @see org.gbif.portal.util.workflow.BaseMapContextActivity#doExecute(org.gbif.portal.util.workflow.MapContext)
     */
    @SuppressWarnings("unchecked")
	public ProcessContext execute(ProcessContext context) throws Exception {
    	logger.debug("Creating a concept search equal to:  " + context.get(getContextKeyConceptValue(), String.class, true));
    	
    	List<String> namespaces = (List<String>) context.get(getContextKeyPsNamespaces(), List.class, true);
		Criteria criteria = new TripletCriteria(
				propertyStore.getProperty(namespaces, getConcept(), String.class),
				getSearchEquals(),
				context.get(getContextKeyConceptValue(), String.class, true));
	
		VelocityContext velocity = new VelocityContext();
		velocity.put("DateFormatter", new DateFormatter());
		velocity.put("hostAddress", InetAddress.getLocalHost().getHostAddress());
		velocity.put("criteria", criteria);
		velocity.put("destination", context.get(getContextKeyURL(), String.class, true));
		String remoteIdentifier = (String) context.get(getContextKeyRemoteIdentifier(), String.class, false);
		if (StringUtils.isNotEmpty(remoteIdentifier)) {
			velocity.put("resource", remoteIdentifier);
		}
		if (StringUtils.isEmpty(getRequestNamespaceOrLocation())) {
			velocity.put("contentNamespace", namespaces.get(namespaces.size()-1));
		} else {
			velocity.put("contentNamespace", getRequestNamespaceOrLocation());
		}
		velocity.put("startAt", context.get(getContextKeyProcessedCount(), Object.class, false));
		context.put(getContextKeyRequest(), templateUtils.getAndMerge(getTemplateLocation(), velocity));
		return context;
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
	 * @return Returns the contextKeyConceptValue.
	 */
	public String getContextKeyConceptValue() {
		return contextKeyConceptValue;
	}

	/**
	 * @param contextKeyConceptValue The contextKeyConceptValue to set.
	 */
	public void setContextKeyConceptValue(String contextKeyConceptValue) {
		this.contextKeyConceptValue = contextKeyConceptValue;
	}

	/**
	 * @return Returns the contextKeyProcessedCount.
	 */
	public String getContextKeyProcessedCount() {
		return contextKeyProcessedCount;
	}

	/**
	 * @param contextKeyProcessedCount The contextKeyProcessedCount to set.
	 */
	public void setContextKeyProcessedCount(String contextKeyProcessedCount) {
		this.contextKeyProcessedCount = contextKeyProcessedCount;
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
	 * @return Returns the contextKeyRequest.
	 */
	public String getContextKeyRequest() {
		return contextKeyRequest;
	}

	/**
	 * @param contextKeyRequest The contextKeyRequest to set.
	 */
	public void setContextKeyRequest(String contextKeyRequest) {
		this.contextKeyRequest = contextKeyRequest;
	}

	/**
	 * @return Returns the contextKeyURL.
	 */
	public String getContextKeyURL() {
		return contextKeyURL;
	}

	/**
	 * @param contextKeyURL The contextKeyURL to set.
	 */
	public void setContextKeyURL(String contextKeyURL) {
		this.contextKeyURL = contextKeyURL;
	}

	/**
	 * @return Returns the propertyStore.
	 */
	public PropertyStore getPropertyStore() {
		return propertyStore;
	}

	/**
	 * @param propertyStore The propertyStore to set.
	 */
	public void setPropertyStore(PropertyStore propertyStore) {
		this.propertyStore = propertyStore;
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
	 * @return Returns the searchEquals.
	 */
	public String getSearchEquals() {
		return searchEquals;
	}

	/**
	 * @param searchEquals The searchEquals to set.
	 */
	public void setSearchEquals(String searchEquals) {
		this.searchEquals = searchEquals;
	}

	/**
	 * @return Returns the templateLocation.
	 */
	public String getTemplateLocation() {
		return templateLocation;
	}

	/**
	 * @param templateLocation The templateLocation to set.
	 */
	public void setTemplateLocation(String templateLocation) {
		this.templateLocation = templateLocation;
	}

	/**
	 * @return Returns the templateUtils.
	 */
	public TemplateUtils getTemplateUtils() {
		return templateUtils;
	}

	/**
	 * @param templateUtils The templateUtils to set.
	 */
	public void setTemplateUtils(TemplateUtils templateUtils) {
		this.templateUtils = templateUtils;
	}
}