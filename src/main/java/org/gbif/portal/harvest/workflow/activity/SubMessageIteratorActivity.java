/**
 * 
 */
package org.gbif.portal.harvest.workflow.activity;

import java.util.List;
import java.util.Map;

import org.gbif.portal.util.mhf.message.Message;
import org.gbif.portal.util.mhf.message.MessageUtils;
import org.gbif.portal.util.workflow.BaseActivity;
import org.gbif.portal.util.workflow.ProcessContext;


/**
 * Takes a message from the context, and using the configured parameters, extracts sub
 * messages, launching the processor for each one
 * @author tim
 */
public class SubMessageIteratorActivity extends BaseActivity {
	/**
	 * Message utilities
	 */
	protected MessageUtils messageUtils;
	
	/**
	 * The context key for the message
	 */
	protected String contextKeyMessage;
	
	/**
	 * The context key for the sub message
	 * This is the key that will go into the child workflow
	 */
	protected String contextKeySubMessage;
	
	/**
	 * The PS key for the concept to iterate over
	 */
	protected String conceptToIterate;
	
	/**
	 * The namespace of the message
	 */
	protected String contextKeyPsNamespaces;
	
	/**
	 * Context key for the messages processed
	 */
	protected String contextKeyMessagesProcessed;
	
	/**
	 * Context key for a running count in the context to update
	 */
	protected String contextKeyTotalCount;
	
	/**
	 * @see org.gbif.portal.util.workflow.Activity#execute(org.gbif.portal.util.workflow.ProcessContext)
	 */
	@SuppressWarnings("unchecked")
	public ProcessContext execute(final ProcessContext context) throws Exception {
		Message message = (Message) context.get(contextKeyMessage, Message.class, true);
		List<String> namespaces = (List<String>) context.get(contextKeyPsNamespaces, List.class, true);
		List subMessages = messageUtils.extractSubMessageList(message, namespaces, conceptToIterate, true);
		int i=0;
		for (Object sub : subMessages) {
			Map<String, Object> seed = buildWorkflowSeedData(context);
			if (getContextKeySubMessage() == null) {
				seed.put(contextKeyMessage, sub);
			} else {
				seed.put(contextKeySubMessage, sub);
			}
			ProcessContext child = launchWorkflow(context, seed);
			onFinishChild(context, child);
			i++;
		}
		
		if (getContextKeyTotalCount() != null) {
			Integer totalCount = (Integer) context.get(getContextKeyTotalCount(), Integer.class, false);
			if (totalCount == null) {
				logger.debug("Setting " + getContextKeyTotalCount() + " to: " + i);
				context.put(getContextKeyTotalCount(), new Integer(i));
			} else {
				logger.debug("Setting " + getContextKeyTotalCount() + " to: " + (totalCount.intValue() + i));
				context.put(getContextKeyTotalCount(), new Integer(totalCount.intValue() + i));
			}
		}		
		if (getContextKeyMessagesProcessed() != null) {
			logger.debug("Setting " + getContextKeyMessagesProcessed() + " to: " + i);
			context.put(getContextKeyMessagesProcessed(), new Integer(i));
		}
		
		logger.debug("Total count: " + context.get(getContextKeyTotalCount(), Integer.class, false));
		
		return context;
	}
	
	/**
	 * Called when the child processor is finished, to be overriden by child classes
	 * @param parent Parent context
	 * @param child The resulting child context
	 */
	protected void onFinishChild(ProcessContext parent, ProcessContext child) {		
	}

	/**
	 * @return the conceptToIterate
	 */
	public String getConceptToIterate() {
		return conceptToIterate;
	}

	/**
	 * @param conceptToIterate the conceptToIterate to set
	 */
	public void setConceptToIterate(String conceptToIterate) {
		this.conceptToIterate = conceptToIterate;
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
	 * @return Returns the contextKeyMessagesProcessed.
	 */
	public String getContextKeyMessagesProcessed() {
		return contextKeyMessagesProcessed;
	}

	/**
	 * @param contextKeyMessagesProcessed The contextKeyMessagesProcessed to set.
	 */
	public void setContextKeyMessagesProcessed(String contextKeyMessagesProcessed) {
		this.contextKeyMessagesProcessed = contextKeyMessagesProcessed;
	}

	/**
	 * @return Returns the contextKeyTotalCount.
	 */
	public String getContextKeyTotalCount() {
		return contextKeyTotalCount;
	}

	/**
	 * @param contextKeyTotalCount The contextKeyTotalCount to set.
	 */
	public void setContextKeyTotalCount(String contextKeyTotalCount) {
		this.contextKeyTotalCount = contextKeyTotalCount;
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
	 * @return Returns the contextKeySubMessage.
	 */
	public String getContextKeySubMessage() {
		return contextKeySubMessage;
	}

	/**
	 * @param contextKeySubMessage The contextKeySubMessage to set.
	 */
	public void setContextKeySubMessage(String contextKeySubMessage) {
		this.contextKeySubMessage = contextKeySubMessage;
	}
}
