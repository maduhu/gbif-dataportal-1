/***************************************************************************
 * Copyright (C) 2006 Global Biodiversity Information Facility Secretariat.  
 * All Rights Reserved.
 *
 * The contents of this file are subject to the Mozilla Public
 * License Version 1.1 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of
 * the License at http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS
 * IS" basis, WITHOUT WARRANTY OF ANY KIND, either express or
 * implied. See the License for the specific language governing
 * rights and limitations under the License.
 ***************************************************************************/
package org.gbif.portal.webservices.actions;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.xmlbeans.XmlObject;
import org.gbif.portal.dto.util.SearchConstraints;
import org.gbif.portal.util.path.PathMapping;
import org.gbif.portal.webservices.util.GbifWebServiceException;


/**
 * @author Donald Hobern
 *
 */
public abstract class Parameters {
	
	public static Log log = LogFactory.getLog(Parameters.class);

	public static final String GBIF_NAMESPACE = "http://portal.gbif.org/ws/response/gbif";
	public static final String DC_NAMESPACE = "http://purl.org/dc/elements/1.1/";
	public static final String DC_LOCATION = "dc.xsd";
	public static final String DCTERMS_NAMESPACE = "http://purl.org/dc/terms/";
	public static final String DCTERMS_LOCATION = "dcterms.xsd";
	public static final String RDF_NAMESPACE = "http://www.w3.org/1999/02/22-rdf-syntax-ns#";
	public static final String RDF_LOCATION = "rdf.xsd";
	public static final String OWL_NAMESPACE = "http://www.w3.org/2002/07/owl#";
	public static final String OWL_LOCATION = "owl.xsd";
	public static final String COMMON_NAMESPACE = "http://rs.tdwg.org/ontology/voc/Common#";
	public static final String COMMON_LOCATION = "tcom.xsd";
	public static final String TAXONOCCURRENCE_NAMESPACE = "http://rs.tdwg.org/ontology/voc/TaxonOccurrence#";
	public static final String TAXONOCCURRENCE_LOCATION = "TaxonOccurrence.xsd";
	public static final String TAXONCONCEPT_NAMESPACE = "http://rs.tdwg.org/ontology/voc/TaxonConcept#";
	public static final String TAXONCONCEPT_LOCATION = "TaxonConcept.xsd";
	public static final String TAXONNAME_NAMESPACE = "http://rs.tdwg.org/ontology/voc/TaxonName#";
	public static final String TAXONNAME_LOCATION = "TaxonName.xsd";

	/** Placeholder to be replaced with the root URL for the portal web application */
	private static final String PORTAL_ROOT_PLACEHOLDER = "__PORTALROOT__";

	/** Placeholder to be replaced with the root URL for the web service servlet */
	private static final String WS_ROOT_PLACEHOLDER = "__WSROOT__";
	
	/** Placeholder to be replaced with any citation text */
	private static final String CITATION_TEXT_PLACEHOLDER = "__CITATIONTEXT__";
	
	private static final String SCHEMA_PREFIX = "/org/gbif/portal/ws/xsd/";
	private static final String SCHEMA_SUFFIX = ".xsd";
	private static final String STYLESHEET_PREFIX = "/org/gbif/portal/ws/xslt/";
	private static final String STYLESHEET_SUFFIX = ".xslt";
	private static final String HELP_PREFIX = "/org/gbif/portal/ws/help/";
	private static final String SHORTHELP_SUFFIX = ".helpshort.txt";
	private static final String LONGHELP_SUFFIX = ".helplong.txt";
	private static final String STATEMENTS = "/org/gbif/portal/ws/help/statements.txt";

	protected static final Map<String, Object> resources = new HashMap<String,Object>();
	
	public static final String KEY_FORMAT = "format";
	public static final String KEY_SERVICE = "service";
	public static final String KEY_REQUEST = "request";
	public static final String KEY_PORTALROOT = "portalroot";
	public static final String KEY_WSROOT = "wsroot";
	public static final String KEY_MODIFIEDSINCE = "modifiedsince";
	public static final String KEY_STARTINDEX = "startindex";
	public static final String KEY_MAXRESULTS = "maxresults";
	public static final String KEY_STYLESHEET = "stylesheet";

	public static final int FORMAT_BRIEF = 0;
	public static final int FORMAT_DARWIN = 1;
	public static final int FORMAT_KML = 2;
	
	public static final String FORMATNAME_BRIEF = "brief";
	public static final String FORMATNAME_DARWIN = "darwin";
	public static final String FORMATNAME_KML = "kml";

	public static final int DEFAULT_MAXRESULTS = 1000;

	public static final int MINIMUM_WILDCARD_INDEX = 3;
	
	protected int format = FORMAT_BRIEF;
	protected Date modifiedSince = null;
	protected String serviceName = null;
	protected int requestType = Action.HELP;
	protected String portalRoot = null;
	protected String wsRoot = null;
	protected int startIndex = 0;
	protected int maxResults = DEFAULT_MAXRESULTS;
	protected String stylesheet = null;
	protected SearchConstraints searchConstraints = new SearchConstraints();
	protected PathMapping pathMapping;

	protected Parameters() {
		// Prevent use
	}
	
	public Parameters(Map<String, Object> params, PathMapping pathMapping)
			throws GbifWebServiceException	{
		try {
			this.pathMapping = pathMapping;
			
			Set<String> keys = params.keySet();
			for (String k : keys) {
				Object value = params.get(k);
				
				if (k.equals(KEY_FORMAT)) {
					format = getFormat((String) value); 
				}
				else if (k.equals(KEY_SERVICE)) {
					serviceName = (String) value;
				}
				else if (k.equals(KEY_REQUEST)) {
					requestType = Action.getRequestType((String) value);
				}
				else if (k.equals(KEY_PORTALROOT)) {
					portalRoot = (String) value; 
				}
				else if (k.equals(KEY_WSROOT)) {
					wsRoot = (String) value; 
				}
				else if (k.equals(KEY_MODIFIEDSINCE)) {
					modifiedSince = parseDate((String) value, 0, 0, 0);
				}
				else if (k.equals(KEY_STARTINDEX)) {
					int start = Integer.parseInt((String) value);
					if (start >= 0) {
						startIndex = start;
					}
				}
				else if (k.equals(KEY_MAXRESULTS)) {
					int max = Integer.parseInt((String) value);
					if (max > 0 && max <= DEFAULT_MAXRESULTS) {
						maxResults = max;
					}
					else {
						maxResults = DEFAULT_MAXRESULTS;
					}
				}
				else if (k.equals(KEY_STYLESHEET)){
					stylesheet = (String) value;
				}
			}

			searchConstraints.setStartIndex(startIndex);
			searchConstraints.setMaxResults(maxResults + 1);
		}
		catch (GbifWebServiceException gwse) {
			throw(gwse);
		}
		catch (Exception e) {
			throw new GbifWebServiceException("Parameters exception: " + e);
		}
	}
	
	public Map<String, Object> getParameterMap(Integer overrideRequestType) {
		Map<String,Object> map = new HashMap<String,Object>();
		int rt = (overrideRequestType == null) ? requestType : overrideRequestType.intValue();
		map.put("service", serviceName);
		map.put(KEY_REQUEST, Action.getRequestTypeName(rt));
		if (rt != Action.SCHEMA && rt != Action.STYLESHEET) {
			if (stylesheet != null) map.put(KEY_STYLESHEET, stylesheet);
			if (requestType == Action.COUNT || requestType == Action.LIST) {
				if (modifiedSince != null) map.put(KEY_MODIFIEDSINCE, formatDate(modifiedSince));
			}
			if (requestType == Action.LIST) {
				if (startIndex != 0) map.put(KEY_STARTINDEX, new Integer(startIndex).toString());
				map.put(KEY_MAXRESULTS, new Integer(maxResults).toString());
			}
		}
		return map;
	}

	protected int getFormat(String formatString) {
		int format = FORMAT_BRIEF;
		
		if (formatString != null) {
			if (formatString.equals(FORMATNAME_DARWIN)) {
				format = FORMAT_DARWIN;
			} else if (formatString.equals(FORMATNAME_KML)) {
				format = FORMAT_KML;
			}
		}
		
		return format;
	}
	
	public String getFormatName() {
		String name = FORMATNAME_BRIEF;
		
		switch(format) {
		case FORMAT_DARWIN: name = FORMATNAME_DARWIN; break;
		case FORMAT_KML: name = FORMATNAME_KML; break;
		}
		
		return name;
	}
	
	/**
	 * Returns the request type name for a given KVP set.
	 */
	public String getRequestTypeName() {
		return Action.getRequestTypeName(requestType);
	}

	public String getShortHelpText() {
		String name = HELP_PREFIX + getServiceName() + SHORTHELP_SUFFIX;
		return (String) getResource(name, false);
	}
	
	public String getLongHelpText()  {
		String name = HELP_PREFIX + getServiceName() + LONGHELP_SUFFIX;
		String text = (String) getResource(name, false);
		return text;
	}
	
	public XmlObject getStylesheetResource()  {
		String name = STYLESHEET_PREFIX + "gbifResponse" + STYLESHEET_SUFFIX;
		return (XmlObject) getResource(name, true);
	}
	
	public String getStylesheetResource(boolean criteria)  {
		String name = STYLESHEET_PREFIX + "gbifResponse" + STYLESHEET_SUFFIX;
		return (String)getResource(name, criteria);
	}
	
	public String getSchemaResource(boolean criteria)  {
		String name = SCHEMA_PREFIX + "gbifResponse" + SCHEMA_SUFFIX;
		return (String)getResource(name, criteria);
	}
	
	public XmlObject getSchemaResource()  {
		String name = SCHEMA_PREFIX + "gbifResponse" + SCHEMA_SUFFIX;
		return (XmlObject) getResource(name, true);
	}
	
	public String getStatements()  {
		return getStatements("");
	}
	
	public String getStatements(String citationText)  {
		return ((String) getResource(STATEMENTS, false)).replace(CITATION_TEXT_PLACEHOLDER, citationText);
	}
	
	protected Object getResource(String name, boolean asXmlObject) {
		Object resource = null;
		
		if (resources.containsKey(name)) {
			resource = resources.get(name);
		}
		else {
			try {
				URL fileURL = getClass().getResource(name);
				
				BufferedReader br = new BufferedReader(new InputStreamReader(fileURL.openStream()));
				StringBuffer sb = new StringBuffer();
				String line;
				
				while ((line = br.readLine()) != null) {
					line = line.replaceAll(PORTAL_ROOT_PLACEHOLDER, getPortalRoot());
					line = line.replaceAll(WS_ROOT_PLACEHOLDER, getWsRoot());
					sb.append(line);
					sb.append("\n");
				}
				
				if (asXmlObject) {
					resource = XmlObject.Factory.parse(sb.toString());
				}
				else {
					resource = sb.toString();
				}
				
				resources.put(name, resource);
			}
			catch (Exception e) {
				log.error("Could not access resource " + name, e);
			}
		}
		
		return resource;
	}

	protected Date parseDate(String dateString, int h, int m, int s) throws GbifWebServiceException {
		Date date = null;
		try {
			int year = Integer.parseInt(((String) dateString).substring(0, 4));
			int month = Integer.parseInt(((String) dateString).substring(5, 7)) - 1;
			int day = Integer.parseInt(((String) dateString).substring(8, 10));
			Calendar calendar = Calendar.getInstance();
			calendar.set(year, month, day, h, m, s);
			date = calendar.getTime();
		} catch (Exception e) {
			throw new GbifWebServiceException("Invalid date: " + dateString);
		}
		return date;
	}

	protected String formatDate(Date date) {
		StringBuffer sb = new StringBuffer();
		
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		
		sb.append(calendar.get(Calendar.YEAR));
		sb.append("-");
		int month = calendar.get(Calendar.MONTH) + 1;
		if (month < 10) {
			sb.append("0");
		}
		sb.append(month);
		sb.append("-");
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		if (day < 10) {
			sb.append("0");
		}
		sb.append(day);
		
		return sb.toString();
	}
	
	public String getUrl(Integer overrideRequestType, Integer startIndex) {
		StringBuffer sb = new StringBuffer();
		sb.append(wsRoot);

		String mappingRoot = pathMapping.getUrlRoot();
		if (mappingRoot != null) {
			sb.append("/");
			sb.append(mappingRoot);
		}
		
		try
		{
			Map <String, Object> map = getParameterMap(overrideRequestType);
			if (startIndex != null) {
				map.put(KEY_STARTINDEX, startIndex.toString());
			}
			
			List<String> pattern = getPattern(map);
			if (pattern != null) {
				for (int i = 0; i < pattern.size(); i++) {
					sb.append("/");
					sb.append(map.remove(pattern.get(i)));
				}
			}

			String separator = "?";
			for (String key : map.keySet()) {
				Object o = map.get(key);
				if (o instanceof String[]) {
					String[] values = (String[]) o;
					for (int i = 0; i < values.length; i++) {
						sb.append(separator);
						separator = "&";
						sb.append(key + "=" + URLEncoder.encode(values[i], "UTF-8"));
					}
				} else if (o instanceof String){
					String value = (String) o;
					sb.append(separator);
					separator = "&";
					sb.append(key + "=" + URLEncoder.encode(value, "UTF-8"));
				}
			}
		}
		catch (Exception e) {
		}
		return sb.toString();
	}
	
	public String getGetUrl(String service, String keyString) {
		StringBuffer sb = new StringBuffer();
		sb.append(wsRoot);

		String mappingRoot = pathMapping.getUrlRoot();
		if (mappingRoot != null) {
			sb.append("/");
			sb.append(mappingRoot);
		}
		
		try
		{
			Map <String, Object> map = new HashMap<String, Object>();
			
			map.put("service", service);
			map.put("request", "get");
			map.put("key", keyString);
			
		
			List<String> pattern = getPattern(map);
			if (pattern != null) {
				for (int i = 0; i < pattern.size(); i++) {
					sb.append("/");
					sb.append(map.remove(pattern.get(i)));
				}
			}

			String separator = "?";
			for (String key : map.keySet()) {
				sb.append(separator);
				separator = "&";
				sb.append(key + "=" + URLEncoder.encode(map.get(key).toString(), "UTF-8"));
			}
		}
		catch (Exception e) {
		}
		return sb.toString();
	}
	
	protected List<String> getPattern(Map<String,Object> map) {
		List<String> pattern = null;
		
		if (pathMapping != null) {
			for (int i = 0; pattern == null && i < pathMapping.getSupportedPatterns().size(); i++) {
				List<String> testPattern = pathMapping.getSupportedPatterns().get(i);
				for (int j = 0; testPattern != null && j < testPattern.size(); j++) {
					if ((map.get(testPattern.get(j))) == null) {
						testPattern = null;
					}
				}
				pattern = testPattern;
			}
		}

		return pattern;
	}

	/**
	 * @return the maxResults
	 */
	public int getMaxResults() {
		return maxResults;
	}

	/**
	 * @return the modifiedSince
	 */
	public Date getModifiedSince() {
		return modifiedSince;
	}
	
	/**
	 * @return the requestType
	 */
	public int getRequestType() {
		return requestType;
	}
	
	/**
	 * @return the searchConstraints
	 */
	public SearchConstraints getSearchConstraints() {
		return searchConstraints;
	}
	
	/**
	 * @return the startIndex
	 */
	public int getStartIndex() {
		return startIndex;
	}
	
	/**
	 * @return the stylesheet
	 */
	public String getStylesheet() {
		return stylesheet;
	}

	/**
	 * @return the portalRoot
	 */
	public String getPortalRoot() {
		return portalRoot;
	}

	/**
	 * @return the wsRoot
	 */
	public String getWsRoot() {
		return wsRoot;
	}

	/**
	 * @param serviceName the serviceName to set
	 */
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	/**
	 * @return the serviceName
	 */
	public String getServiceName() {
		return serviceName;
	}
	
	public String getSchemaLocation() {
		StringBuffer sb = new StringBuffer();
		
		sb.append(GBIF_NAMESPACE);
		sb.append(" ");
		sb.append(getUrl(Action.SCHEMA, null));
		sb.append(" ");
		sb.append(DC_NAMESPACE);
		sb.append(" ");
		sb.append(portalRoot);
		sb.append("/schema/");
		sb.append(DC_LOCATION);
		sb.append(" ");
		sb.append(DCTERMS_NAMESPACE);
		sb.append(" ");
		sb.append(portalRoot);
		sb.append("/schema/");
		sb.append(DCTERMS_LOCATION);
		sb.append(" ");
		sb.append(RDF_NAMESPACE);
		sb.append(" ");
		sb.append(portalRoot);
		sb.append("/schema/");
		sb.append(RDF_LOCATION);
		sb.append(" ");
		sb.append(OWL_NAMESPACE);
		sb.append(" ");
		sb.append(portalRoot);
		sb.append("/schema/");
		sb.append(OWL_LOCATION);
		sb.append(" ");
		sb.append(COMMON_NAMESPACE);
		sb.append(" ");
		sb.append(portalRoot);
		sb.append("/schema/");
		sb.append(COMMON_LOCATION);
		sb.append(" ");
		sb.append(TAXONOCCURRENCE_NAMESPACE);
		sb.append(" ");
		sb.append(portalRoot);
		sb.append("/schema/");
		sb.append(TAXONOCCURRENCE_LOCATION);
		sb.append(" ");
		sb.append(TAXONCONCEPT_NAMESPACE);
		sb.append(" ");
		sb.append(portalRoot);
		sb.append("/schema/");
		sb.append(TAXONCONCEPT_LOCATION);
		sb.append(" ");
		sb.append(TAXONNAME_NAMESPACE);
		sb.append(" ");
		sb.append(portalRoot);
		sb.append("/schema/");
		sb.append(TAXONNAME_LOCATION);

		return sb.toString();
	}

	/**
	 * @return the format
	 */
	public int getFormat() {
		return format;
	}
}