/**
 * 
 */
package org.gbif.portal.harvest.workflow.activity;

import org.gbif.portal.util.workflow.BaseActivity;
import org.gbif.portal.util.workflow.ProcessContext;


/**
 * If the count found at the context key is greater than zero, it will be incremented
 * 
 * @author tim
 */
public class IncrementCountActivity extends BaseActivity {
	/**
	 * The count to increment
	 */
	protected String contextKeyCount;
	
	/**
	 * A context flag to set if the count was incremented
	 */
	protected String contextKeyFlagToSet;
	
	/**
	 * A context flag to use to maintain the number of errors
	 */
	protected String contextKeyErrorCount;
	
	/**
	 * The number of errors allowed
	 * -1 means infinite
	 * The default is 3
	 * If the count is reached, then the count is not incremented  
	 */
	protected int allowedMaxErrorCount = 3;
	
	/**
	 * The value of the flag
	 */
	protected String flagValue;
	
	/**
	 * The increment by value
	 */
	protected int incrementBy;
	
	/**
	 * @see org.gbif.portal.util.workflow.Activity#execute(org.gbif.portal.util.workflow.ProcessContext)
	 */
	@SuppressWarnings("unchecked")
	public ProcessContext execute(final ProcessContext context) throws Exception {
		Integer count = (Integer) context.get(getContextKeyCount(), Integer.class, false);
		
		if (count == null) {
			count = new Integer(0);
			context.put(getContextKeyCount(), count);
		}
		
		Integer errorCount = null;
		
		// handle the limit
		if (getContextKeyErrorCount() != null) {
			errorCount = (Integer) context.get(getContextKeyErrorCount(), Integer.class, false);
			if (errorCount == null) {
				errorCount = new Integer(1);
				context.put(getContextKeyErrorCount(), errorCount);				
			} else {
				errorCount = new Integer(errorCount.intValue()+1);
				context.put(getContextKeyErrorCount(), errorCount);
				logger.info("Running error count: " + errorCount.intValue());
			}
		}
		
		// if the errors are being tracked and the count has hit the limit
		if (errorCount.intValue() > allowedMaxErrorCount && allowedMaxErrorCount>0) {
			logger.warn("Will not increment count as error limit of " + allowedMaxErrorCount + " has been hit");
			throw new ErrorCountException("Limit of " + allowedMaxErrorCount + " has been hit");
		} else {
			context.put(getContextKeyCount(), new Integer(count+getIncrementBy()));
			logger.warn("Incrementing count from " + count + " to " + (count+getIncrementBy()));
			if (contextKeyFlagToSet != null) {
				context.put(getContextKeyFlagToSet(), getFlagValue());
			}
		}
			
		return context;
	}

	/**
	 * @return Returns the contextKeyCount.
	 */
	public String getContextKeyCount() {
		return contextKeyCount;
	}

	/**
	 * @param contextKeyCount The contextKeyCount to set.
	 */
	public void setContextKeyCount(String contextKeyCount) {
		this.contextKeyCount = contextKeyCount;
	}

	/**
	 * @return Returns the incrementBy.
	 */
	public int getIncrementBy() {
		return incrementBy;
	}

	/**
	 * @param incrementBy The incrementBy to set.
	 */
	public void setIncrementBy(int incrementBy) {
		this.incrementBy = incrementBy;
	}

	/**
	 * @return Returns the contextKeyFlagToSet.
	 */
	public String getContextKeyFlagToSet() {
		return contextKeyFlagToSet;
	}

	/**
	 * @param contextKeyFlagToSet The contextKeyFlagToSet to set.
	 */
	public void setContextKeyFlagToSet(String contextKeyFlagToSet) {
		this.contextKeyFlagToSet = contextKeyFlagToSet;
	}

	/**
	 * @return Returns the flagValue.
	 */
	public String getFlagValue() {
		return flagValue;
	}

	/**
	 * @param flagValue The flagValue to set.
	 */
	public void setFlagValue(String flagValue) {
		this.flagValue = flagValue;
	}

	/**
	 * @return Returns the allowedMaxErrorCount.
	 */
	public int getAllowedMaxErrorCount() {
		return allowedMaxErrorCount;
	}

	/**
	 * @param allowedMaxErrorCount The allowedMaxErrorCount to set.
	 */
	public void setAllowedMaxErrorCount(int allowedMaxErrorCount) {
		this.allowedMaxErrorCount = allowedMaxErrorCount;
	}

	/**
	 * @return Returns the contextKeyErrorCount.
	 */
	public String getContextKeyErrorCount() {
		return contextKeyErrorCount;
	}

	/**
	 * @param contextKeyErrorCount The contextKeyErrorCount to set.
	 */
	public void setContextKeyErrorCount(String contextKeyErrorCount) {
		this.contextKeyErrorCount = contextKeyErrorCount;
	}
}
