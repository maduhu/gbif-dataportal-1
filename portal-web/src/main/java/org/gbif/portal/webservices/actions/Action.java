/**
 * 
 */
package org.gbif.portal.webservices.actions;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.gbif.portal.util.path.PathMapping;
import org.gbif.portal.webservices.util.GbifWebServiceException;


/**
 * @author Ali Kalufya (kalufya@gmail.com)
 * This is parent of all action objects that produce XML 
 * meeting parameters given as parameter map. 
 */
public abstract class Action {
	
	public static final int HELP = 0;
	public static final int LIST = 1;
	public static final int GET = 2;
	public static final int COUNT = 3;
	public static final int SCHEMA = 4;
	public static final int STYLESHEET = 5;
	public static final int GET_CAPABILITIES = 6;
	public static final int DESCRIBE_FEATURE_TYPE = 7;
	public static final int GET_FEATURES = 8;
	
	public static final String ACTION_HELP = "help";
	public static final String ACTION_LIST = "list";
	public static final String ACTION_GET = "get";
	public static final String ACTION_COUNT = "count";
	public static final String ACTION_SCHEMA = "schema";
	public static final String ACTION_STYLESHEET = "stylesheet";
	public static final String ACTION_GET_CAPABILITIES = "get_capabilities";
	public static final String ACTION_DESCRIBE_FEATURE_TYPE = "describe_feature_type";
	public static final String ACTION_GET_FEATURES = "get_features";
	
	protected PathMapping pathMapping;
	
	//public abstract XmlObject getData(Map<String, Object> params)
	//	throws GbifWebServiceException;

	/**
	 * Returns the request type for a request type value.
	 */
	public static String getRequestTypeName(int type) {
		String name;
		
		switch (type) {
		case LIST: name = ACTION_LIST; break;
		case GET: name = ACTION_GET; break;
		case COUNT: name = ACTION_COUNT; break;
		case SCHEMA: name = ACTION_SCHEMA; break;
		case STYLESHEET: name = ACTION_STYLESHEET; break;
		case GET_CAPABILITIES: name = ACTION_GET_CAPABILITIES; break;
		case DESCRIBE_FEATURE_TYPE: name = ACTION_DESCRIBE_FEATURE_TYPE; break;
		case GET_FEATURES: name = ACTION_GET_FEATURES; break;
		default: name = ACTION_HELP; break;
		}
		
		return name;
	}

	/**
	 * Returns the request type for a given KVP set.
	 */
	public static int getRequestType(String request) {
		if (request != null) {
			request = request.toLowerCase();

			if (request.equals(ACTION_LIST))
				return LIST;
			else if (request.equals(ACTION_GET))
				return GET;
			else if (request.equals(ACTION_COUNT))
				return COUNT;
			else if (request.equals(ACTION_STYLESHEET))
				return STYLESHEET;
			else if (request.equals(ACTION_SCHEMA))
				return SCHEMA;
			else if (request.equals(ACTION_GET_CAPABILITIES))
				return GET_CAPABILITIES;
			else if (request.equals(ACTION_DESCRIBE_FEATURE_TYPE))
				return DESCRIBE_FEATURE_TYPE;
			else if (request.equals(ACTION_GET_FEATURES))
				return GET_FEATURES;
			else
				return HELP;
		} else
			return HELP;
	}
	
	/**
	 * Return the help page for this service
	 * 
	 * @param params
	 * @return
	 * @throws GbifWebServiceException
	 */
	public Map<String,Object> returnHelpPage(Parameters params)
	throws GbifWebServiceException
	{
		Map<String,Object> results = new HashMap<String,Object>();
		
		Map<String,String> headerMap;
		Map<String,String> parameterMap;

		headerMap = returnHeader(params,false);
		parameterMap = returnParameters(params.getParameterMap(null));	
		
		results.put("headerMap", headerMap);
		results.put("parameterMap", parameterMap);	
		return results;		
		
	}		
	
	
	protected Map<String,String> returnParameters(Map map) {
		
		Map<String,String> parameterMap = new HashMap<String,String>();
		
		for (Object key : map.keySet()) {
			if (key instanceof String) {
				Object value = map.get(key);
				if (value instanceof String) {
					parameterMap.put( (String)key, (String)value);
				}
				if (value instanceof String[]) {
					StringBuffer sb = new StringBuffer();
					String[] values = (String[]) value;
					for (int i = 0; i < values.length; i++) {
						sb.append(values[i]);
						if (i < values.length - 1) {
							sb.append(", ");
						}
					}
					parameterMap.put( (String)(key), sb.toString());
				}
			}
		}
		return parameterMap;
	}	
	
	protected Map<String,String> returnHeader(Parameters parameters, boolean shortHelp)
	{		
		Map<String,String> headerMap = new HashMap<String,String>();
		
		String stylesheetUrl = (parameters.getStylesheet() == null) ? parameters.getUrl(Action.STYLESHEET, null) : parameters.getStylesheet();
		
		headerMap.put("stylesheet", stylesheetUrl);
		
		headerMap.put("statements", parameters.getStatements(""));
		if(shortHelp)
			headerMap.put("help", parameters.getShortHelpText());
		else
			headerMap.put("help", parameters.getLongHelpText());
		headerMap.put("request", parameters.getRequestTypeName());
		
		headerMap.put("schemaLocation", parameters.getSchemaLocation());
		
		return headerMap;
	}
	
	protected Map<String,String> returnSummary(Parameters parameters, List<?> list, boolean includeNext)
	{
		Map<String,String> summaryMap = new HashMap<String,String>();
		
		int listSize = 0;
		
		if(list!=null)
			listSize=list.size();
			
		summaryMap.put("start", new Integer(parameters.getStartIndex()).toString());
		
		if(listSize>0)	//query returned 1 or more results
		{
			summaryMap.put("totalMatched", (new Integer(parameters.getStartIndex() + listSize).toString()));
		}
		if (parameters.getMaxResults() < listSize+1 && includeNext) {
			list = list.subList(0, parameters.getMaxResults());
			Integer next = new Integer(parameters.getStartIndex() + list.size());
			summaryMap.put("next", next.toString());
			summaryMap.put("nextRequestUrl", parameters.getUrl(null, next));
		}
		summaryMap.put("totalReturned", (new Integer(listSize).toString()));		
		
		return summaryMap;
	}
	
	protected Map<String,String> returnSummary(Long count)
	{
		Map<String,String> summaryMap = new HashMap<String,String>();
		
		summaryMap.put("totalMatched",count.toString());

		return summaryMap;
	}	

	/**
	 * @return the pathMapping
	 */
	public PathMapping getPathMapping() {
		return pathMapping;
	}

	/**
	 * @param pathMapping the pathMapping to set
	 */
	public void setPathMapping(PathMapping pathMapping) {
		this.pathMapping = pathMapping;
	}
}
