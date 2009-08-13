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

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.gbif.portal.util.mhf.message.Message;
import org.gbif.portal.util.mhf.message.MessageFactory;
import org.gbif.portal.util.mhf.message.MessageUtils;
import org.gbif.portal.util.propertystore.PropertyStore;
import org.gbif.portal.util.request.RequestUtils;
import org.gbif.portal.util.request.ResponseReader;
import org.gbif.portal.util.workflow.BaseActivity;
import org.gbif.portal.util.workflow.ProcessContext;

/**
 * Simple Paging Request Activity for harvesting by using simple paging request parameters.
 * 
 * @author dmartin
 * 
 * @see http://wiki.tdwg.org/twiki/bin/view/TAPIR/GetInvokedOperations for request param definitions.
 */
public class SimplePageRequestActivity extends BaseActivity {
	
	/**
	 * The request utilities
	 */
	protected RequestUtils requestUtils;

	/**
	 * Message utilities
	 */
	protected MessageUtils messageUtils;

	/**
	 * The property store in use
	 */
	protected PropertyStore propertyStore;
	
	/**
	 * To place the generated request into the context
	 */
	protected String contextKeyRequest;
	
	/**
	 * The count of records key
	 */
	protected String contextKeyUrl;
	
	/**
	 * To get the PS namespace that we are working in 
	 */
	protected String contextKeyPsNamespaces;
	
	/**
	 * The key for the retrieved message
	 */
	protected String contextKeyMessage;

	/**
	 * Paging start index
	 */
	protected String contextKeyStartIndex = "startIndex";
	
	/**
	 * End of records flag.
	 */
	protected String contextKeyRecordsFinished;
	
	/**
	 * Request parameters to be appended to the resource access point url.
	 * e.g. ?template=xxxx&op=s
	 */
	protected Map<String, String> requestParameters;
	
	/**
	 * Whether to url encode the request parameters
	 */
	protected boolean urlEncodeParameters = true; 
	
	/**
	 * Request param for indicating the first record to retrieve.
	 */
	protected String startRequestParamKey = "start";

	/**
	 * Request param for indicating the maximum no. of records to retrieve
	 */
	protected String maxResultsRequestParamKey = "limit";
	
	/**
	 * The no. of records to retrieve in a single request.
	 */
	protected int pageSize = 100;
	
	/**
	 * The no. of records to retrieve in a single request.
	 */
	protected int initialStartIndex = 0;	
	
	/**
	 * Property store key for the message factory e.g. "MESSAGE.FACTORY"
	 */
	protected String propertyStoreKeyMessageFactory;	

	/**
	 * Property store key for the start field
	 */	
	protected String propertyStoreKeyStart = "RESPONSE.START.RECORD";
	
	/**
	 * Property store key for the next field
	 */	
	protected String propertyStoreKeyNext = "RESPONSE.NEXT.RECORD";
	
	/**
	 * Property store key for the total returned field
	 */	
	protected String propertyStoreKeyTotalReturned = "RESPONSE.TOTALRETURNED.RECORD";
	
	/**
	 * Flag value to indicate end of records.
	 */
	protected String endOfRecordsFlag = "TRUE";
	
	/**
	 * @see org.gbif.portal.util.workflow.Activity#execute(org.gbif.portal.util.workflow.ProcessContext)
	 */
	@SuppressWarnings("unchecked")
	public ProcessContext execute(final ProcessContext context) throws Exception {
		
		//retrieve the start index from the context
		Integer startIndex = (Integer) context.get(contextKeyStartIndex, Integer.class, false);
		if(startIndex==null){
			startIndex = initialStartIndex;
		}

		//construct the url		
		StringBuffer constructedUrl = createRequestUrl(context, startIndex);
		final List<String> psNamespaces = (List<String>) context.get(contextKeyPsNamespaces, List.class, true);
		
		//make the request
		Message response = (Message) requestUtils.executeGetRequest(
				constructedUrl.toString(), 
				new ResponseReader() {
					@SuppressWarnings("unchecked")
					public Object read(InputStream is) throws Exception {
						final MessageFactory factory = (MessageFactory) propertyStore.getProperty(
								psNamespaces,
								propertyStoreKeyMessageFactory,
								MessageFactory.class);						
						return factory.build(is); 
					}
				});
		
		//retrieve the starting point for the next page of records 
    Integer next = messageUtils.extractConceptAsInteger(response, psNamespaces, propertyStoreKeyNext, false);
    Integer totalReturned = messageUtils.extractConceptAsInteger(response, psNamespaces, propertyStoreKeyTotalReturned, false);
    if(logger.isDebugEnabled()){
      Integer start  = messageUtils.extractConceptAsInteger(response, psNamespaces, propertyStoreKeyStart, false);		
    	logger.debug("Start: "+start+", Next: "+next+", Total returned: "+totalReturned);
    }
    
    //check if the end of records has been reached
    if(next==null || totalReturned<pageSize){
    	logger.debug("Signalling end of records has been reached.");
    	context.put(contextKeyRecordsFinished, endOfRecordsFlag);
    }
    context.put(contextKeyStartIndex, next);
		context.put(contextKeyMessage, response);		
		return context;		
	}

	/**
	 * Construct the url for the request.
	 * 
	 * @param context
	 * @param startIndex
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	private StringBuffer createRequestUrl(final ProcessContext context,
			Integer startIndex) throws UnsupportedEncodingException {
		String url = (String) context.get(contextKeyUrl);
		StringBuffer constructedUrl = new StringBuffer(url);
		if(requestParameters!=null && !requestParameters.isEmpty() ){
			constructedUrl.append("?");
			Iterator<String> iter = requestParameters.keySet().iterator();
			while(iter.hasNext()){
				String key = iter.next();
				constructedUrl.append(key);
				constructedUrl.append("=");
				String paramValue = requestParameters.get(key);
				if(urlEncodeParameters){
					paramValue = URLEncoder.encode(paramValue, "UTF-8");
				}
				constructedUrl.append(paramValue);
				if(iter.hasNext()){
					constructedUrl.append("&");
				}
			}
		}
		if(requestParameters==null || requestParameters.isEmpty() ){
			constructedUrl.append("?");
		} else {
			constructedUrl.append("&");
		}
		constructedUrl.append(startRequestParamKey);
		constructedUrl.append("=");
		constructedUrl.append(startIndex);
		constructedUrl.append("&");
		constructedUrl.append(maxResultsRequestParamKey);
		constructedUrl.append("=");
		constructedUrl.append(pageSize);
		return constructedUrl;
	}

	/**
	 * @param requestUtils the requestUtils to set
	 */
	public void setRequestUtils(RequestUtils requestUtils) {
		this.requestUtils = requestUtils;
	}

	/**
	 * @param propertyStore the propertyStore to set
	 */
	public void setPropertyStore(PropertyStore propertyStore) {
		this.propertyStore = propertyStore;
	}

	/**
	 * @param messageUtils the messageUtils to set
	 */
	public void setMessageUtils(MessageUtils messageUtils) {
		this.messageUtils = messageUtils;
	}

	/**
	 * @param contextKeyRequest the contextKeyRequest to set
	 */
	public void setContextKeyRequest(String contextKeyRequest) {
		this.contextKeyRequest = contextKeyRequest;
	}

	/**
	 * @param contextKeyURL the contextKeyURL to set
	 */
	public void setContextKeyURL(String contextKeyURL) {
		this.contextKeyUrl = contextKeyURL;
	}

	/**
	 * @param contextKeyPsNamespaces the contextKeyPsNamespaces to set
	 */
	public void setContextKeyPsNamespaces(String contextKeyPsNamespaces) {
		this.contextKeyPsNamespaces = contextKeyPsNamespaces;
	}

	/**
	 * @param propertyStoreKeyMessageFactory the propertyStoreKeyMessageFactory to set
	 */
	public void setPropertyStoreKeyMessageFactory(
			String propertyStoreKeyMessageFactory) {
		this.propertyStoreKeyMessageFactory = propertyStoreKeyMessageFactory;
	}

	/**
	 * @param requestParameters the requestParameters to set
	 */
	public void setRequestParameters(Map<String, String> requestParameters) {
		this.requestParameters = requestParameters;
	}

	/**
	 * @param startRequestParamKey the startRequestParamKey to set
	 */
	public void setStartRequestParamKey(String startRequestParamKey) {
		this.startRequestParamKey = startRequestParamKey;
	}

	/**
	 * @param maxResultsRequestParamKey the maxResultsRequestParamKey to set
	 */
	public void setMaxResultsRequestParamKey(String maxResultsRequestParamKey) {
		this.maxResultsRequestParamKey = maxResultsRequestParamKey;
	}

	/**
	 * @param contextKeyUrl the contextKeyUrl to set
	 */
	public void setContextKeyUrl(String contextKeyUrl) {
		this.contextKeyUrl = contextKeyUrl;
	}

	/**
	 * @param contextKeyMessage the contextKeyMessage to set
	 */
	public void setContextKeyMessage(String contextKeyMessage) {
		this.contextKeyMessage = contextKeyMessage;
	}

	/**
	 * @param contextKeyStartIndex the contextKeyStartIndex to set
	 */
	public void setContextKeyStartIndex(String contextKeyStartIndex) {
		this.contextKeyStartIndex = contextKeyStartIndex;
	}

	/**
	 * @param pageSize the pageSize to set
	 */
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	/**
	 * @param initialStartIndex the initialStartIndex to set
	 */
	public void setInitialStartIndex(int initialStartIndex) {
		this.initialStartIndex = initialStartIndex;
	}

	/**
	 * @param propertyStoreKeyStart the propertyStoreKeyStart to set
	 */
	public void setPropertyStoreKeyStart(String propertyStoreKeyStart) {
		this.propertyStoreKeyStart = propertyStoreKeyStart;
	}

	/**
	 * @param propertyStoreKeyNext the propertyStoreKeyNext to set
	 */
	public void setPropertyStoreKeyNext(String propertyStoreKeyNext) {
		this.propertyStoreKeyNext = propertyStoreKeyNext;
	}

	/**
	 * @param propertyStoreKeyTotalReturned the propertyStoreKeyTotalReturned to set
	 */
	public void setPropertyStoreKeyTotalReturned(
			String propertyStoreKeyTotalReturned) {
		this.propertyStoreKeyTotalReturned = propertyStoreKeyTotalReturned;
	}

	/**
	 * @param contextKeyRecordsFinished the contextKeyRecordsFinished to set
	 */
	public void setContextKeyRecordsFinished(String contextKeyRecordsFinished) {
		this.contextKeyRecordsFinished = contextKeyRecordsFinished;
	}

	/**
	 * @param urlEncodeParameters the urlEncodeParameters to set
	 */
	public void setUrlEncodeParameters(boolean urlEncodeParameters) {
		this.urlEncodeParameters = urlEncodeParameters;
	}
}