/**
 * 
 */
package org.gbif.portal.harvest.workflow.activity.ipni;

import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.gbif.portal.util.workflow.BaseActivity;
import org.gbif.portal.util.workflow.ProcessContext;


/**
 * An activity that will preprocess the single record, to account for errors that IPNI provide.
 * 
 * <ul>
 * 	<li>
 * 		If the Name in the context starts with the Family, then it is stripped from the name.  This
 * 		is because of a known error in the IPNI extract
 * 	</li>
 * @author tim
 */
public class IPNIRecordPreprocessingActivity extends BaseActivity {
	/**
	 * Context keys
	 */
	protected String contextKeyFamily;
	protected String contextKeyName;
	
	/**
	 * The known IPNI error
	 * When they wish to give a Genus, they give "Family Genus"
	 * However, we want the Family for the subfam. and tribe. names
	 */
	protected Pattern familyGenusPattern = Pattern.compile("([A-Z][a-z]+(?:-[a-z]+)*) ([A-Z][a-z]+(?:-[a-z]+)*)");
	
	/**
	 * @see org.gbif.portal.util.workflow.Activity#execute(org.gbif.portal.util.workflow.ProcessContext)
	 */
	@SuppressWarnings("unchecked")
	public ProcessContext execute(final ProcessContext context) throws Exception {
		String family = (String) context.get(getContextKeyFamily(), String.class, false);
		String name = (String) context.get(getContextKeyName(), String.class, false);
		
		// THIS IS A HACK - NEED TO FIND OUT HOW TO GET THE FILE READER TO READ UTF-8
		if (StringUtils.isNotEmpty(name)
				&& name.contains("×")) {
			logger.debug("Replacing × - see IPNIRecordPreprocessingActivity - " + name);
			name = name.replaceAll("×", "�");
			context.put(getContextKeyName(), name);
		}
		
		if (StringUtils.isNotEmpty(family)
				&& StringUtils.isNotEmpty(name)
				&& name.startsWith(family)
				&& familyGenusPattern.matcher(name).matches()) {
			logger.debug("Name ["+ name +"] starts with the Family["+ family+"] so stripping from the name");
			name = name.replaceFirst(family, "");
			name = name.trim();
			context.put(getContextKeyName(), name);
		}
		
		return context;
	}

	/**
	 * @return the contextKeyFamily
	 */
	public String getContextKeyFamily() {
		return contextKeyFamily;
	}

	/**
	 * @param contextKeyFamily the contextKeyFamily to set
	 */
	public void setContextKeyFamily(String contextKeyFamily) {
		this.contextKeyFamily = contextKeyFamily;
	}

	/**
	 * @return the contextKeyName
	 */
	public String getContextKeyName() {
		return contextKeyName;
	}

	/**
	 * @param contextKeyName the contextKeyName to set
	 */
	public void setContextKeyName(String contextKeyName) {
		this.contextKeyName = contextKeyName;
	}
}
