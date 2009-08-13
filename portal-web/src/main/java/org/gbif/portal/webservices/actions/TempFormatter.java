/**
 * 
 */
package org.gbif.portal.webservices.actions;

import org.gbif.portal.io.PropertyFormatter;

/**
 * @author
 *
 */
public class TempFormatter implements PropertyFormatter {

	/* (non-Javadoc)
	 * @see org.gbif.portal.io.PropertyFormatter#format(java.lang.String, java.lang.String)
	 */
	public String format(String arg0, String arg1) {
		
		return arg1;
	}

}
