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
package org.gbif.portal.harvest.workflow.activity.oaipmh;

import java.io.InputStream;
import java.util.List;

import org.gbif.portal.util.mhf.message.Message;
import org.gbif.portal.util.mhf.message.MessageFactory;
import org.gbif.portal.util.request.RequestUtils;
import org.gbif.portal.util.request.ResponseReader;
import org.gbif.portal.util.workflow.BaseActivity;
import org.gbif.portal.util.workflow.ProcessContext;


/**
 * Continually requests the next page of data, until there is no more flag found to continue doing so.
 * 
 * The initial base URL is concatenated with the page from date for the initial query.
 * The resumption token is concatenated to the the repeat url base for subsequent queries, until there is no 
 * resumption token found in the request
 * 
 * @author trobertson
 */
public class OAIPMHRequestIteratorActivity extends BaseActivity {
	/**
	 * Context keys
	 */
	public String contextKeyInitialBaseURL = "initialBaseURL";
	public String contextKeyRepeatBaseURL = "repeatBaseURL";
	public String contextKeyPageFromDate = "pageFromDate";
	public String contextKeyResumptionToken = "resumptionToken";
	protected String contextKeyResponse;
	
	/**
	 * The property store key for retrieving the message factory for the 
	 * namespace in use
	 */
	protected String propertyStoreKeyMessageFactory;	
	
	/**
	 * Utils
	 */
	RequestUtils requestUtils;
	
	/**
     * @see org.gbif.portal.util.workflow.BaseMapContextActivity#doExecute(org.gbif.portal.util.workflow.MapContext)
     */
	public ProcessContext execute(final ProcessContext context) throws Exception {
		String initialBaseURL = (String) context.get(contextKeyInitialBaseURL, String.class, true);
//		String repeatBaseURL = (String) context.get(contextKeyRepeatBaseURL, String.class, true);
		String pageFromDate = (String) context.get(contextKeyPageFromDate, String.class, true);
		
		// do the initial query
		Message response = (Message) requestUtils.executeGetRequest(
				initialBaseURL+pageFromDate, 
				new ResponseReader() {
					@SuppressWarnings("unchecked")
					public Object read(InputStream is) throws Exception {
						List<String> namespaces = (List<String>) context.get(contextKeyPsNamespaces, List.class, true);
						MessageFactory factory = (MessageFactory) propertyStore.getProperty(
								namespaces,
								propertyStoreKeyMessageFactory,
								MessageFactory.class);
						return factory.build(is); 
					}
					
				});
		context.put(contextKeyResponse, response);
		//launchWorkflow(context, null);
		
    	return context;
	}
}