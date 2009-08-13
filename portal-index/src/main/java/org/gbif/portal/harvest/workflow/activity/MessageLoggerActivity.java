/**
 * 
 */
package org.gbif.portal.harvest.workflow.activity;

import org.gbif.portal.util.mhf.message.Message;
import org.gbif.portal.util.workflow.BaseActivity;
import org.gbif.portal.util.workflow.ProcessContext;


/**
 * Logs a message from the context
 * 
 * @author tim
 */
public class MessageLoggerActivity extends BaseActivity {
	/**
	 * The message to log
	 */
	protected String contextKeyMessage;
	
	/**
	 * @see org.gbif.portal.util.workflow.Activity#execute(org.gbif.portal.util.workflow.ProcessContext)
	 */
	public ProcessContext execute(final ProcessContext context) throws Exception {
		Message message = (Message) context.get(getContextKeyMessage(), Message.class, true);
		logger.info(message.getRawData());
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
}
