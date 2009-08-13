package org.gbif.portal.web.content.filter;

import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

/**
 * Interface implemented by classes providing a dynamically generated picklist for filters.
 * 
 * @author dmartin
 */
public interface PicklistHelper {

	/**
	 * Generate a picklist of key value pairs.
	 * 
	 * @return Map of key value pairs for a picklist.
	 */
	public Map<String, String> getPicklist(HttpServletRequest request, Locale locale);
}
