/**
 * 
 */
package org.gbif.portal.harvest.workflow.activity;

import java.io.InputStream;
import java.util.List;

import org.gbif.portal.util.mhf.message.Message;
import org.gbif.portal.util.mhf.message.MessageFactory;
import org.gbif.portal.util.mhf.message.MessageUtils;
import org.gbif.portal.util.propertystore.PropertyStore;
import org.gbif.portal.util.request.RequestUtils;
import org.gbif.portal.util.request.ResponseReader;
import org.gbif.portal.util.workflow.BaseActivity;
import org.gbif.portal.util.workflow.ProcessContext;


/**
 * Using the property store, the MHF, temlate utils and request utils
 * this will issue a request and place the response Message into the context.
 * 
 * @TODO This makes the BIG assumption that the MessageFactory in use will support
 * building from an InputStream.  This either needs enforced or refactored in this class
 * 
 * See javadoc for configurable options.
 * 
 * @author tim
 */
public class RequestIssueActivity extends BaseActivity {
	/**
	 * The request utilities
	 */
	protected RequestUtils requestUtils;
	
	/**
	 * The property store in use
	 */
	protected PropertyStore propertyStore;
	
	/**
	 * Message utilities
	 */
	protected MessageUtils messageUtils;
	
	/**
	 * The context key for the endpoint url
	 */
	protected String contextKeyURL;
	
	/**
	 * The context key for the property store namespace 
	 */
	protected String contextKeyPsNamespaces;
	
	/**
	 * The context key for the request to issue
	 */
	protected String contextKeyRequest;	
	
	/**
	 * The context key for the response Message
	 */
	protected String contextKeyResponse;
	
	/**
	 * The property store key for retrieving the message factory for the 
	 * namespace in use
	 */
	protected String propertyStoreKeyMessageFactory;
		
	/**
	 * @see org.gbif.portal.util.workflow.Activity#execute(org.gbif.portal.util.workflow.ProcessContext)
	 */
	@SuppressWarnings("unchecked")
	public ProcessContext execute(final ProcessContext context) throws Exception {
		logger.info((String) context.get(contextKeyRequest, String.class, true));
		Message response = (Message) requestUtils.executeGetRequest(
				(String) context.get(contextKeyURL, String.class, true), 
				(String) context.get(contextKeyRequest, String.class, true), 
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
		return context;
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
	 * @return the contextKeyResponse
	 */
	public String getContextKeyResponse() {
		return contextKeyResponse;
	}

	/**
	 * @param contextKeyResponse the contextKeyResponse to set
	 */
	public void setContextKeyResponse(String contextKeyResponse) {
		this.contextKeyResponse = contextKeyResponse;
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
	 * @return the messageUtils
	 */
	public MessageUtils getMessageUtils() {
		return messageUtils;
	}

	/**
	 * @param messageUtils the messageUtils to set
	 */
	public void setMessageUtils(MessageUtils messageUtils) {
		this.messageUtils = messageUtils;
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
	 * @return the propertyStoreKeyMessageFactory
	 */
	public String getPropertyStoreKeyMessageFactory() {
		return propertyStoreKeyMessageFactory;
	}

	/**
	 * @param propertyStoreKeyMessageFactory the propertyStoreKeyMessageFactory to set
	 */
	public void setPropertyStoreKeyMessageFactory(
			String propertyStoreKeyMessageFactory) {
		this.propertyStoreKeyMessageFactory = propertyStoreKeyMessageFactory;
	}

	/**
	 * @return the requestUtils
	 */
	public RequestUtils getRequestUtils() {
		return requestUtils;
	}

	/**
	 * @param requestUtils the requestUtils to set
	 */
	public void setRequestUtils(RequestUtils requestUtils) {
		this.requestUtils = requestUtils;
	}
}
