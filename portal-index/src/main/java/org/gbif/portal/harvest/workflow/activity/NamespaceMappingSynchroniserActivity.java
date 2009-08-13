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

import java.util.LinkedList;
import java.util.List;

import org.gbif.portal.dao.PropertyStoreNamespaceDAO;
import org.gbif.portal.util.mhf.message.MessageAccessException;
import org.gbif.portal.util.workflow.Activity;
import org.gbif.portal.util.workflow.BaseActivity;
import org.gbif.portal.util.workflow.ProcessContext;

/**
 * An activity that will append the namespace extracted from the 
 * namespaces found in the conctext with those found for the 
 * resourceAccessPoint in the context.
 * @author trobertson
 */
public class NamespaceMappingSynchroniserActivity extends BaseActivity implements
		Activity {
	
	/**
	 * The ordered namespaces that will be checked against
	 * the list of the namespaces in the context.  The first found is used
	 */
	protected List<String> orderedListNamespaces = new LinkedList<String>();
	
	/**
	 * Context Keys
	 */
	protected String contextKeyResourceAccessPointId;
	protected String contextKeyNamespaces;
	
	/**
	 * DAOs
	 */
	PropertyStoreNamespaceDAO propertyStoreNamespaceDAO;
	
	/**
	 * @see org.gbif.portal.util.workflow.Activity#execute(org.gbif.portal.util.workflow.ProcessContext)
	 */
	@SuppressWarnings("unchecked")
	public ProcessContext execute(ProcessContext context) throws Exception {
		List<String> namespaces = (List<String>) context.get(getContextKeyNamespaces(), List.class, true);
		long resourceAccessPointId = ((Long) context.get(getContextKeyResourceAccessPointId(), Long.class, true)).longValue();
		boolean found = false;
		for (String testNamespace : orderedListNamespaces) {
			logger.debug("Looking for [" + testNamespace +"]");			
			if (namespaces.contains(testNamespace)) {
				logger.info("Found [" + testNamespace + "] and ensuring attached to ResourceAccessPoint id: " + resourceAccessPointId);
				propertyStoreNamespaceDAO.appendNamespaceIfNotAttached(resourceAccessPointId, testNamespace);
				found=true;
				break;
			}
		}
		if (!found) {
			throw new MessageAccessException("The supported namespaces in the message are not known: " + namespaces);
		}
		return context;
	}

	/**
	 * @return Returns the contextKeyResourceAccessPointId.
	 */
	public String getContextKeyResourceAccessPointId() {
		return contextKeyResourceAccessPointId;
	}

	/**
	 * @param contextKeyResourceAccessPointId The contextKeyResourceAccessPointId to set.
	 */
	public void setContextKeyResourceAccessPointId(
			String contextKeyResourceAccessPointId) {
		this.contextKeyResourceAccessPointId = contextKeyResourceAccessPointId;
	}

	/**
	 * @return Returns the orderedListNamespaces.
	 */
	public List<String> getOrderedListNamespaces() {
		return orderedListNamespaces;
	}

	/**
	 * @param orderedListNamespaces The orderedListNamespaces to set.
	 */
	public void setOrderedListNamespaces(List<String> orderedListNamespaces) {
		this.orderedListNamespaces = orderedListNamespaces;
	}

	/**
	 * @return Returns the propertyStoreNamespaceDAO.
	 */
	public PropertyStoreNamespaceDAO getPropertyStoreNamespaceDAO() {
		return propertyStoreNamespaceDAO;
	}

	/**
	 * @param propertyStoreNamespaceDAO The propertyStoreNamespaceDAO to set.
	 */
	public void setPropertyStoreNamespaceDAO(
			PropertyStoreNamespaceDAO propertyStoreNamespaceDAO) {
		this.propertyStoreNamespaceDAO = propertyStoreNamespaceDAO;
	}

	/**
	 * @return Returns the contextKeyNamespaces.
	 */
	public String getContextKeyNamespaces() {
		return contextKeyNamespaces;
	}

	/**
	 * @param contextKeyNamespaces The contextKeyNamespaces to set.
	 */
	public void setContextKeyNamespaces(String contextKeyNamespaces) {
		this.contextKeyNamespaces = contextKeyNamespaces;
	}

}
