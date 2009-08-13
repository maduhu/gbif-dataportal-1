/**
 * 
 */
package org.gbif.portal.harvest.workflow.activity;

import org.gbif.portal.util.workflow.BaseActivity;
import org.gbif.portal.util.workflow.ProcessContext;


/**
 * Extracts the DSA code from a TAPIR URL and assigns it as the resource code
 * 
 * @author donald hobern
 */
public class ResourceCodeFromTapirUrlActivity extends BaseActivity {
	/**
	 * The key from which to get the name (to use as a default if code not found)
	 */
	protected String contextKeyResourceName;

	/**
	 * The key for the code to insert into the context
	 */
	protected String contextKeyResourceCode;

	/**
	 * The key for the URL
	 */
	protected String contextKeyUrl;

	/**
	 * @see org.gbif.portal.util.workflow.Activity#execute(org.gbif.portal.util.workflow.ProcessContext)
	 */
	@SuppressWarnings("unchecked")
	public ProcessContext execute(final ProcessContext context) throws Exception {
		String code = null;
		String url = (String) context.get(getContextKeyUrl(), String.class, false);
		
		if (url != null) {
			int index = url.toLowerCase().indexOf("dsa=");
			if (index >= 0) {
				int index2 = url.indexOf("&", index);
				int index3 = url.indexOf("/", index);
				if (index2 > index) {
					if (index3 > index && index3 < index2) {
						code = url.substring(index, index3);
					}
					else {
						code = url.substring(index, index2);
					}
				}
				else if (index3 > index) {
					code = url.substring(index, index3);
				}
				else {
					code = url.substring(index);
				}
			}
			else {
				int index2 = url.indexOf("?");
				
				if (index2 > 0) {
					code = url.substring(url.lastIndexOf("/" + 1, index2), index2);
				}
				else {
					code = url.substring(url.lastIndexOf("/") + 1);
				}
			}
			
			if (code == null || code.length() == 0) {
				code = (String) context.get(getContextKeyResourceName(), String.class, true);
			}
			context.put(getContextKeyResourceCode(), code);
			logger.debug("Got resource code " + code);
		}

		return context;
	}

	/**
	 * @return the contextKeyResourceCode
	 */
	public String getContextKeyResourceCode() {
		return contextKeyResourceCode;
	}

	/**
	 * @param contextKeyResourceCode the contextKeyResourceCode to set
	 */
	public void setContextKeyResourceCode(String contextKeyResourceCode) {
		this.contextKeyResourceCode = contextKeyResourceCode;
	}

	/**
	 * @return the contextKeyResourceName
	 */
	public String getContextKeyResourceName() {
		return contextKeyResourceName;
	}

	/**
	 * @param contextKeyResourceName the contextKeyResourceName to set
	 */
	public void setContextKeyResourceName(String contextKeyResourceName) {
		this.contextKeyResourceName = contextKeyResourceName;
	}

	/**
	 * @return the contextKeyUrl
	 */
	public String getContextKeyUrl() {
		return contextKeyUrl;
	}

	/**
	 * @param contextKeyUrl the contextKeyUrl to set
	 */
	public void setContextKeyUrl(String contextKeyUrl) {
		this.contextKeyUrl = contextKeyUrl;
	}

}
