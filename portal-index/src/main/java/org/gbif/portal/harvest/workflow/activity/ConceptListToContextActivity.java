/**
 * 
 */
package org.gbif.portal.harvest.workflow.activity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.gbif.portal.util.mhf.message.Message;
import org.gbif.portal.util.mhf.message.MessageUtils;
import org.gbif.portal.util.workflow.BaseActivity;
import org.gbif.portal.util.workflow.ProcessContext;


/**
 * Extracts a list of concepts from a message and puts them into the context
 * as singularly keys items
 * 
 * @author tim
 */
public class ConceptListToContextActivity extends BaseActivity {
	/**
	 * Message utilities
	 */
	protected MessageUtils messageUtils;

	/**
	 * The message to extract from
	 */
	protected String contextKeyMessage;

	/**
	 * The namespace in use
	 */
	protected String contextKeyPsNamespaces;
	
	/**
	 * Used to indicate that all the concepts are required
	 */
	protected boolean allConceptsRequired = false;
	
	/**
	 * The map of keys to extract from the message
	 * with values as keys to put in to the context
	 */
	protected Map<String, String> conceptToContext = new HashMap<String, String>();
	
	/**
	 * @see org.gbif.portal.util.workflow.Activity#execute(org.gbif.portal.util.workflow.ProcessContext)
	 */
	@SuppressWarnings("unchecked")
	public ProcessContext execute(final ProcessContext context) throws Exception {
		Message message = (Message) context.get(getContextKeyMessage(), Message.class, true);
		List<String> namespaces = (List<String>) context.get(contextKeyPsNamespaces, List.class, true);
		int numberExtracted=0;
		for (String key : conceptToContext.keySet()) {
			Object result = messageUtils.extractConcept(message, namespaces, key, allConceptsRequired);
			if (result != null) {
				if (result instanceof String 
						&& !StringUtils.isEmpty((String) result)) {
					numberExtracted++;
					logger.debug(key + ": " + result);
					context.put(conceptToContext.get(key), result);
					
				} else if (result instanceof List
						&& !(((List)result).size()==0)) {
					numberExtracted++;
					if (logger.isDebugEnabled()) {
						StringBuffer sb = new StringBuffer();
						for (Object o : (List)result) {
							sb.append("[" + o.toString() + "] ");
						}
						logger.debug(key + ": " + sb.toString());
					}
					
					context.put(conceptToContext.get(key), result);
				}
			}
		}
		logger.debug("Extracted " + numberExtracted + " concepts that had values");
		return context;
	}

	/**
	 * @return the contextKeyMessage
	 */
	public String getContextKeyMessage() {
		return contextKeyMessage;
	}

	/**
	 * @param contextKeyMessage the contextKeyMessage to set
	 */
	public void setContextKeyMessage(String contextKeyMessage) {
		this.contextKeyMessage = contextKeyMessage;
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
	 * @return the conceptToContext
	 */
	public Map<String, String> getConceptToContext() {
		return conceptToContext;
	}

	/**
	 * @param conceptToContext the conceptToContext to set
	 */
	public void setConceptToContext(Map<String, String> conceptToContext) {
		this.conceptToContext = conceptToContext;
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
	 * @return Returns the allConceptsRequired.
	 */
	public boolean isAllConceptsRequired() {
		return allConceptsRequired;
	}

	/**
	 * @param allConceptsRequired The allConceptsRequired to set.
	 */
	public void setAllConceptsRequired(boolean allConceptsRequired) {
		this.allConceptsRequired = allConceptsRequired;
	}

}
