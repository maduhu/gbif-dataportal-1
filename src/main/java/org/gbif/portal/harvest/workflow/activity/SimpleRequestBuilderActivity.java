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
package org.gbif.portal.harvest.workflow.activity;

import java.net.InetAddress;

import org.apache.velocity.VelocityContext;
import org.gbif.portal.util.propertystore.PropertyStore;
import org.gbif.portal.util.request.TemplateUtils;
import org.gbif.portal.util.text.DateFormatter;
import org.gbif.portal.util.workflow.BaseActivity;
import org.gbif.portal.util.workflow.ProcessContext;


/**
 * An activity to build a simple request for a template.
 *
 * @author trobertson
 */
public class SimpleRequestBuilderActivity extends BaseActivity {
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
	 * The template to use
	 */
	protected String templateLocation;

    /**
     * @see org.gbif.portal.util.workflow.BaseMapContextActivity#doExecute(org.gbif.portal.util.workflow.MapContext)
     */
    @SuppressWarnings("unchecked")
	public ProcessContext execute(ProcessContext context) throws Exception {
		VelocityContext velocity = new VelocityContext();
		velocity.put("DateFormatter", new DateFormatter());
		velocity.put("hostAddress", InetAddress.getLocalHost().getHostAddress());
		velocity.put("destination", context.get(getContextKeyURL(), String.class, true));
		context.put(getContextKeyRequest(), templateUtils.getAndMerge(getTemplateLocation(), velocity));
		return context;
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
}