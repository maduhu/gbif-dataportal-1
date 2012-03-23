/**
 * 
 */
package org.gbif.portal.harvest.workflow.activity;

import org.gbif.portal.util.workflow.BaseActivity;
import org.gbif.portal.util.workflow.ProcessContext;


/**
 * This will launch a workflow from the property store 
 * 
 * @author tim
 */
public class PropertyStoreWorkflowLaunchActivity extends BaseActivity {
	/**
	 * @see org.gbif.portal.util.workflow.Activity#execute(org.gbif.portal.util.workflow.ProcessContext)
	 */
	@SuppressWarnings("unchecked")
	public ProcessContext execute(final ProcessContext context) throws Exception {
		launchWorkflow(context, null);
		logger.info("Finished workflow");
		return context;
	}
}
