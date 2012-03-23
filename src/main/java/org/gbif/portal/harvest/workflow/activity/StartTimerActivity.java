/**
 * 
 */
package org.gbif.portal.harvest.workflow.activity;

import org.gbif.portal.util.workflow.BaseActivity;
import org.gbif.portal.util.workflow.ProcessContext;


/**
 * Puts the System.currentTimeMillis in the context
 * @author tim
 */
public class StartTimerActivity extends BaseActivity {
	/**
	 * The context key for the timer name
	 */
	protected String contextKeyTimerName;
	
	/**
	 * @see org.gbif.portal.util.workflow.Activity#execute(org.gbif.portal.util.workflow.ProcessContext)
	 */
	@SuppressWarnings("unchecked")
	public ProcessContext execute(final ProcessContext context) throws Exception {
		context.put(getContextKeyTimerName(), new Long(System.currentTimeMillis()));
		return context;
	}

	/**
	 * @return Returns the contextKeyTimerName.
	 */
	public String getContextKeyTimerName() {
		return contextKeyTimerName;
	}

	/**
	 * @param contextKeyTimerName The contextKeyTimerName to set.
	 */
	public void setContextKeyTimerName(String contextKeyTimerName) {
		this.contextKeyTimerName = contextKeyTimerName;
	}
}
