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

import java.io.InputStream;
import java.util.List;

import org.gbif.portal.dao.ResourceAccessPointDAO;
import org.gbif.portal.util.mhf.message.Message;
import org.gbif.portal.util.mhf.message.MessageFactory;
import org.gbif.portal.util.mhf.message.MessageUtils;
import org.gbif.portal.util.request.RequestUtils;
import org.gbif.portal.util.request.ResponseReader;
import org.gbif.portal.util.workflow.ProcessContext;


/**
 * An activity that will attempt an inventory of date last modified
 * 
 * @author trobertson
 */
public class CheckSupportsDateLastModifiedActivity extends InventoryOfConceptRequestBuilderActivity {
	/**
	 * The request utilities
	 */
	protected RequestUtils requestUtils;
	
	/**
	 * The property store key for retreiving the message factory for the 
	 * namespace in use
	 */
	protected String propertyStoreKeyMessageFactory;	
	
	/**
	 * Message utilities
	 */
	protected MessageUtils messageUtils;	
	
	/**
	 * The concept within the response
	 */
	protected String psKeyConceptMessage;
	
	/**
	 * The RAP ID context key
	 */
	protected String contextKeyResourceAccessPointId="contextKeyResourceAccessPointId";
	
	/**
	 * DAOs
	 */
	protected ResourceAccessPointDAO resourceAccessPointDAO;
	
    /**
     * @see org.gbif.portal.util.workflow.BaseMapContextActivity#doExecute(org.gbif.portal.util.workflow.MapContext)
     */
    @SuppressWarnings("unchecked")
	public ProcessContext execute(ProcessContext context) throws Exception {
    	if ("true".equalsIgnoreCase(System.getProperty("ignore-date-last-modified"))) {
    		logger.warn("Date last modified has been overridden to be ignored");
    		context.remove(getContextKeyDateLastModified());
    		return context;
    	}		
    	
    	
    	// build the request
    	final ProcessContext contextWithRequest = super.execute(context);
    	String request = (String)context.get(getContextKeyRequest(), String.class, true);
    	final List<String> namespaces = (List<String>) contextWithRequest.get(contextKeyPsNamespaces, List.class, true);
		Message response = (Message) requestUtils.executeGetRequest(
				(String) context.get(contextKeyURL, String.class, true), 
				request, 
				new ResponseReader() {
					@SuppressWarnings("unchecked")
					public Object read(InputStream is) throws Exception {
						MessageFactory factory = (MessageFactory) propertyStore.getProperty(
								namespaces,
								propertyStoreKeyMessageFactory,
								MessageFactory.class);
						return factory.build(is); 
					}
					
				});
    	
		List<Message> conceptMessages = messageUtils.extractSubMessageList(response, namespaces, getPsKeyConceptMessage(), true);
		if (conceptMessages != null && conceptMessages.size()>0) {
			logger.info("It would appear that the date last modified inventory works.");
			resourceAccessPointDAO.setSupportsDateLastModified(true, (Long) context.get(getContextKeyResourceAccessPointId(), Long.class, true));
		} else {
			logger.info("When using the date last modified, we got no inventory.  Setting this to not supported and removing from context");
			resourceAccessPointDAO.setSupportsDateLastModified(false, (Long) context.get(getContextKeyResourceAccessPointId(), Long.class, true));
			context.remove(getContextKeyDateLastModified());
		}
		return context;
    }

	/**
	 * @return Returns the requestUtils.
	 */
	public RequestUtils getRequestUtils() {
		return requestUtils;
	}

	/**
	 * @param requestUtils The requestUtils to set.
	 */
	public void setRequestUtils(RequestUtils requestUtils) {
		this.requestUtils = requestUtils;
	}

	/**
	 * @return Returns the messageUtils.
	 */
	public MessageUtils getMessageUtils() {
		return messageUtils;
	}

	/**
	 * @param messageUtils The messageUtils to set.
	 */
	public void setMessageUtils(MessageUtils messageUtils) {
		this.messageUtils = messageUtils;
	}

	/**
	 * @return Returns the propertyStoreKeyMessageFactory.
	 */
	public String getPropertyStoreKeyMessageFactory() {
		return propertyStoreKeyMessageFactory;
	}

	/**
	 * @param propertyStoreKeyMessageFactory The propertyStoreKeyMessageFactory to set.
	 */
	public void setPropertyStoreKeyMessageFactory(
			String propertyStoreKeyMessageFactory) {
		this.propertyStoreKeyMessageFactory = propertyStoreKeyMessageFactory;
	}

	/**
	 * @return Returns the psKeyConceptMessage.
	 */
	public String getPsKeyConceptMessage() {
		return psKeyConceptMessage;
	}

	/**
	 * @param psKeyConceptMessage The psKeyConceptMessage to set.
	 */
	public void setPsKeyConceptMessage(String psKeyConceptMessage) {
		this.psKeyConceptMessage = psKeyConceptMessage;
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
	 * @return Returns the resourceAccessPointDAO.
	 */
	public ResourceAccessPointDAO getResourceAccessPointDAO() {
		return resourceAccessPointDAO;
	}

	/**
	 * @param resourceAccessPointDAO The resourceAccessPointDAO to set.
	 */
	public void setResourceAccessPointDAO(
			ResourceAccessPointDAO resourceAccessPointDAO) {
		this.resourceAccessPointDAO = resourceAccessPointDAO;
	}
}