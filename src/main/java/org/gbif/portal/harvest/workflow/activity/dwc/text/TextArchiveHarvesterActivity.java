/**
 * 
 */
package org.gbif.portal.harvest.workflow.activity.dwc.text;

import org.gbif.portal.util.workflow.BaseActivity;
import org.gbif.portal.util.workflow.ProcessContext;

/**
 * A simple wrapper making the TextArchiveHarvester a workflow Activity
 * 
 * @author tim
 */
public class TextArchiveHarvesterActivity extends BaseActivity {
	// context keys to get the end points
	public static final String CONTEXT_KEY_URL = "url";
	public static final String CONTEXT_KEY_DATA_PROVIDER_ID = "dataProviderId";
	public static final String CONTEXT_KEY_RESOURCE_ACCESS_POINT_ID = "resourceAccessPointId";
	public static final String CONTEXT_KEY_DATA_RESOURCE_ID = "dataResourceId";
	
	protected boolean isZippedArchive = true;
	
	protected TextArchiveHarvester textArchiveHarvester;
	
	/**
	 * @see org.gbif.portal.util.workflow.Activity#execute(org.gbif.portal.util.workflow.ProcessContext)
	 */
	public ProcessContext execute(ProcessContext context) throws Exception {
		logger.info(context);
		logger.info("Starting DWC-TA harvesting of " + context.get(CONTEXT_KEY_URL) + " which has zipped[" + isZippedArchive + "]");
		
		textArchiveHarvester.harvest((String) context.get(CONTEXT_KEY_URL, String.class, true), 
				"/tmp/rap/" + context.get(CONTEXT_KEY_RESOURCE_ACCESS_POINT_ID, Long.class, true), 
				(Long) context.get(CONTEXT_KEY_DATA_PROVIDER_ID, Long.class, true),
				(Long) context.get(CONTEXT_KEY_RESOURCE_ACCESS_POINT_ID, Long.class, true),
				(Long) context.get(CONTEXT_KEY_DATA_RESOURCE_ID, Long.class, true),
				isZippedArchive);
		return context;
	}

	public TextArchiveHarvester getTextArchiveHarvester() {
		return textArchiveHarvester;
	}

	public void setTextArchiveHarvester(TextArchiveHarvester textArchiveHarvester) {
		this.textArchiveHarvester = textArchiveHarvester;
	}

	public boolean isZippedArchive() {
		return isZippedArchive;
	}

	public boolean getIsZippedArchive() {
		return isZippedArchive;
	}

	public void setIsZippedArchive(boolean isZippedArchive) {
		this.isZippedArchive = isZippedArchive;
	}
}
