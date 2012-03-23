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
public class InventoryOfConceptRequestBuilderActivity extends BaseActivity {
	/**
	 * The PS key for the concept to query
	 */
	protected String conceptKey;

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
	 * To get the PS namespace that we are working in
	 */
	protected String contextKeyPsNamespaces;

	/**
	 * For building a filter
	 */
	protected String contextKeyLowerValue;

	/**
	 * For the filter in the template
	 */
	protected String searchGreaterThan;

	/**
	 * For predicate in filter
	 */
	protected String searchAnd;

	/**
	 * For namespaces that support paging, this can be used
	 */
	protected String contextKeyStartAt;

	/**
	 * The context key for the endpoint url
	 */
	protected String contextKeyURL;

	/**
	 * The context key for the remote identifier at the url
	 */
	protected String contextKeyRemoteIdentifier;

	/**
	 * The namespace or location to push into the request e.g. DIGiR: The schema
	 * location If not set then the lowest priority psNamespace is used (e.g.
	 * don't set for ABCD ;o)
	 */
	protected String requestNamespaceOrLocation;

	/**
	 * The template to use
	 */
	protected String templateLocation;

	/**
	 * For inventory of things that have changed
	 */
	protected String contextKeyDateLastModified;

	/**
	 * To get the search value from the PS
	 */
	protected String dateLastModifiedPSKey;

	/**
	 * For filter
	 */
	protected String searchGreaterThanOrEquals;

	/**
	 * @see org.gbif.portal.util.workflow.BaseMapContextActivity#doExecute(org.gbif.portal.util.workflow.MapContext)
	 */
	@SuppressWarnings("unchecked")
	public ProcessContext execute(ProcessContext context) throws Exception {
		logger.debug("Creating an inventory request for concept key in PS: "
				+ getConceptKey());

		List<String> namespaces = (List<String>) context.get(
				getContextKeyPsNamespaces(), List.class, true);

		VelocityContext velocity = new VelocityContext();
		velocity.put("DateFormatter", new DateFormatter());
		velocity
				.put("hostAddress", InetAddress.getLocalHost().getHostAddress());
		velocity.put("concept", propertyStore.getProperty(namespaces,
				getConceptKey(), String.class));
		velocity.put("destination", context.get(getContextKeyURL(),
				String.class, true));

		Criteria pagingCriteria = buildPagingCriteria(context, namespaces);
		Criteria dateLastModifiedCriteria = buildDateLastModifiedCriteria(
				context, namespaces);
		addCriteriaToVelocityContext(velocity, pagingCriteria,
				dateLastModifiedCriteria);

		if (getContextKeyStartAt() != null) {
			Integer startAt = (Integer) context.get(getContextKeyStartAt(),
					Integer.class, false);
			if (startAt != null) {
				velocity.put("startAt", startAt);
			}
		}

		String remoteIdentifier = (String) context.get(
				getContextKeyRemoteIdentifier(), String.class, false);
		if (StringUtils.isNotEmpty(remoteIdentifier)) {
			velocity.put("resource", remoteIdentifier);
		}
		if (StringUtils.isEmpty(getRequestNamespaceOrLocation())) {
			velocity.put("contentNamespace", namespaces
					.get(namespaces.size() - 1));
		} else {
			velocity.put("contentNamespace", getRequestNamespaceOrLocation());
		}
		context.put(getContextKeyRequest(), templateUtils.getAndMerge(
				getTemplateLocation(), velocity));
		return context;
	}

	/**
	 * @param velocity
	 * @param pagingCriteria
	 * @param dateLastModifiedCriteria
	 */
	private void addCriteriaToVelocityContext(VelocityContext velocity,
			Criteria pagingCriteria, Criteria dateLastModifiedCriteria) {
		if (pagingCriteria != null && dateLastModifiedCriteria != null) {
			CollectionCriteria criteria = new CollectionCriteria(searchAnd);
			criteria.add(pagingCriteria);
			criteria.add(dateLastModifiedCriteria);
			velocity.put("criteria", criteria);

		} else if (pagingCriteria != null) {
			velocity.put("criteria", pagingCriteria);

		} else if (dateLastModifiedCriteria != null) {
			velocity.put("criteria", dateLastModifiedCriteria);

		}
	}

	/**
	 * @param context
	 * @param namespaces
	 * @return
	 * @throws ContextCorruptException
	 * @throws MisconfiguredPropertyException
	 */
	private Criteria buildPagingCriteria(ProcessContext context,
			List<String> namespaces) throws ContextCorruptException,
			MisconfiguredPropertyException {
		// build the criteria to handle paging over the concept
		Criteria pagingCriteria = null;
		if (getContextKeyLowerValue() != null) {
			String minimumValue = (String) context.get(
					getContextKeyLowerValue(), String.class, false);
			if (minimumValue != null) {
				pagingCriteria = new TripletCriteria(propertyStore.getProperty(
						namespaces, getConceptKey(), String.class),
						getSearchGreaterThan(), minimumValue);

			}
		}
		return pagingCriteria;
	}

	/**
	 * @param context
	 * @param namespaces
	 * @return
	 * @throws ContextCorruptException
	 * @throws MisconfiguredPropertyException
	 */
	private Criteria buildDateLastModifiedCriteria(ProcessContext context,
			List<String> namespaces) throws ContextCorruptException,
			MisconfiguredPropertyException {
    	if ("true".equalsIgnoreCase(System.getProperty("ignore-date-last-modified"))) {
    		logger.warn("Date last modified has been overridden to be ignored");
    		return null;
    	}		
		logger.debug("contextKeyDateLastModified: "
				+ getContextKeyDateLastModified());
		logger.debug("contextKeyDateLastModified value: "
				+ (String) context.get(getContextKeyDateLastModified(),
						String.class, false));
		// build the criteria to handle a date last modified
		Criteria dateLastModifiedCriteria = null;
		
		if (getContextKeyDateLastModified() != null
				&& getDateLastModifiedPSKey() != null) {
			String date = (String) context.get(getContextKeyDateLastModified(),
					String.class, false);
			if (date != null) {
				dateLastModifiedCriteria = new TripletCriteria(propertyStore
						.getProperty(namespaces, getDateLastModifiedPSKey(),
								String.class), getSearchGreaterThanOrEquals(),
						date);
			}
		}
		return dateLastModifiedCriteria;
	}

	/**
	 * @return Returns the contextKeyPsNamespaces.
	 */
	public String getContextKeyPsNamespaces() {
		return contextKeyPsNamespaces;
	}

	/**
	 * @param contextKeyPsNamespaces
	 *            The contextKeyPsNamespaces to set.
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
	 * @param contextKeyRemoteIdentifier
	 *            The contextKeyRemoteIdentifier to set.
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
	 * @param contextKeyRequest
	 *            The contextKeyRequest to set.
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
	 * @param contextKeyURL
	 *            The contextKeyURL to set.
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
	 * @param propertyStore
	 *            The propertyStore to set.
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
	 * @param requestNamespaceOrLocation
	 *            The requestNamespaceOrLocation to set.
	 */
	public void setRequestNamespaceOrLocation(String requestNamespaceOrLocation) {
		this.requestNamespaceOrLocation = requestNamespaceOrLocation;
	}

	/**
	 * @return Returns the templateLocation.
	 */
	public String getTemplateLocation() {
		return templateLocation;
	}

	/**
	 * @param templateLocation
	 *            The templateLocation to set.
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
	 * @param templateUtils
	 *            The templateUtils to set.
	 */
	public void setTemplateUtils(TemplateUtils templateUtils) {
		this.templateUtils = templateUtils;
	}

	/**
	 * @return Returns the conceptKey.
	 */
	public String getConceptKey() {
		return conceptKey;
	}

	/**
	 * @param conceptKey
	 *            The conceptKey to set.
	 */
	public void setConceptKey(String conceptKey) {
		this.conceptKey = conceptKey;
	}

	/**
	 * @return Returns the contextKeyLowerValue.
	 */
	public String getContextKeyLowerValue() {
		return contextKeyLowerValue;
	}

	/**
	 * @param contextKeyLowerValue
	 *            The contextKeyLowerValue to set.
	 */
	public void setContextKeyLowerValue(String contextKeyLowerValue) {
		this.contextKeyLowerValue = contextKeyLowerValue;
	}

	/**
	 * @return Returns the searchGreaterThan.
	 */
	public String getSearchGreaterThan() {
		return searchGreaterThan;
	}

	/**
	 * @param searchGreaterThan
	 *            The searchGreaterThan to set.
	 */
	public void setSearchGreaterThan(String searchGreaterThan) {
		this.searchGreaterThan = searchGreaterThan;
	}

	/**
	 * @return Returns the contextKeyStartAt.
	 */
	public String getContextKeyStartAt() {
		return contextKeyStartAt;
	}

	/**
	 * @param contextKeyStartAt
	 *            The contextKeyStartAt to set.
	 */
	public void setContextKeyStartAt(String contextKeyStartAt) {
		this.contextKeyStartAt = contextKeyStartAt;
	}

	/**
	 * @return Returns the contextKeyDateLastModified.
	 */
	public String getContextKeyDateLastModified() {
		return contextKeyDateLastModified;
	}

	/**
	 * @param contextKeyDateLastModified
	 *            The contextKeyDateLastModified to set.
	 */
	public void setContextKeyDateLastModified(String contextKeyDateLastModified) {
		this.contextKeyDateLastModified = contextKeyDateLastModified;
	}

	/**
	 * @return Returns the searchGreaterThanOrEquals.
	 */
	public String getSearchGreaterThanOrEquals() {
		return searchGreaterThanOrEquals;
	}

	/**
	 * @param searchGreaterThanOrEquals
	 *            The searchGreaterThanOrEquals to set.
	 */
	public void setSearchGreaterThanOrEquals(String searchGreaterThanOrEquals) {
		this.searchGreaterThanOrEquals = searchGreaterThanOrEquals;
	}

	/**
	 * @return Returns the searchAnd.
	 */
	public String getSearchAnd() {
		return searchAnd;
	}

	/**
	 * @param searchAnd
	 *            The searchAnd to set.
	 */
	public void setSearchAnd(String searchAnd) {
		this.searchAnd = searchAnd;
	}

	/**
	 * @return Returns the dateLastModifiedPSKey.
	 */
	public String getDateLastModifiedPSKey() {
		return dateLastModifiedPSKey;
	}

	/**
	 * @param dateLastModifiedPSKey
	 *            The dateLastModifiedPSKey to set.
	 */
	public void setDateLastModifiedPSKey(String dateLastModifiedPSKey) {
		this.dateLastModifiedPSKey = dateLastModifiedPSKey;
	}
}