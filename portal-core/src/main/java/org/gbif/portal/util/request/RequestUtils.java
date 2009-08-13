/**
 * 
 */
package org.gbif.portal.util.request;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Map;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpConnectionManager;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.SimpleHttpConnectionManager;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpClientParams;
import org.apache.commons.httpclient.params.HttpConnectionManagerParams;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Utilities for issuing requests and reading responses over Http
 * 
 * This is a utility built on top of the Apache Commons Httputils 
 * project
 * 
 * @author tim
 */
public class RequestUtils {
	/**
	 * Logger
	 */
	public static Log logger = LogFactory.getLog(RequestUtils.class);
	
	/**
	 * The encoding to use for requests
	 * Defaults to use UTF-8
	 */
	protected String requestEncoding = "UTF-8";
	
	/**
	 * The key to use when constructing requests of the form:
	 *   http://yadda.yadda.com/yadda?request=yadda
	 * Defaults to request which is the DiGIR, BioCASE, TAPIR form
	 */
	protected String requestParameterKey = "request";
	
	/**
	 * Timeout defualts to 1 minute
	 */
	protected int timeoutInMillisec = 60000;
	
	/**
	 * Executes an Http GET request for the request String given.
	 * The delegated handler is called to deal with the response, and the 
	 * connection is cleaned up afterwards
	 * @param request To execute
	 * @param handler To delegate response handling to
	 * @return The result of the handling
	 * @throws HttpException On Http level error
	 * @throws IOException On connection level error
	 * @throws Exception On handling the response
	 */
	public Object executeGetRequest(String request, ResponseReader handler) throws HttpException, IOException, Exception {
		GetMethod method = null;
		try {
			
			
		    HttpConnectionManagerParams cmparams = new HttpConnectionManagerParams();
		    cmparams.setSoTimeout(timeoutInMillisec);
		    cmparams.setConnectionTimeout(timeoutInMillisec);
		    HttpConnectionManager manager = new SimpleHttpConnectionManager();
		    manager.setParams(cmparams);
		    HttpClientParams params = new HttpClientParams();
		    HttpClient client = new HttpClient(params, manager);				
			//client.getParams().setSoTimeout(timeoutInMillisec);
			client.getParams().setContentCharset("UTF-8");
			method = new GetMethod(request);
			if(logger.isTraceEnabled())
				logger.trace(URLDecoder.decode(request, requestEncoding));
			client.executeMethod(method);
//			 We have trouble reading some UTF-8 using this...
//			return handler.read(method.getResponseBodyAsStream());

//			 So let's dump to string and read that
			String response = method.getResponseBodyAsString();
			logger.debug(response);
			InputStream is = new ByteArrayInputStream(response.getBytes("UTF-8"));
			return handler.read(is);		
		} finally {
			if (method != null) {
				logger.debug("Releasing connection");
				method.releaseConnection();
			}
		}
	}
	
	/**
	 * Builds the request using the supplied URL, the configured requestParameterKey
	 * (recognising that the url may contain parameters also) and encoding the parameter
	 * value using the configured encoding type.
	 * 
	 * @see executeGetRequest(String, ResponseHandler)
	 */
	public Object executeGetRequest(String url, String requestContent, ResponseReader handler) throws HttpException, UnsupportedEncodingException, IOException, Exception {
		if (requestContent != null && requestContent.length() > 0) {
			if (url.contains("?")) {
				url = url + "&" + requestParameterKey + "=" + URLEncoder.encode(requestContent, requestEncoding);
			} else {
				url = url + "?" + requestParameterKey + "=" + URLEncoder.encode(requestContent, requestEncoding);
			}
		}
		
		if (logger.isDebugEnabled())
			logger.debug("Encoded request: " + url);

		return executeGetRequest(url, handler);
	}

	
	/**
	 * Executes a get request building up a URL for all the Key Value pairs supplied
	 * @param url To execute to
	 * @param kvContent The map of key value pairs for the request
	 * @param handler The response handler
	 * @return The result of the handling
	 * @throws HttpException 
	 * @throws UnsupportedEncodingException
	 * @throws IOException
	 * @throws Exception
	 */
	public Object executeGetRequest(String url, Map<String,String> kvContent, ResponseReader handler) throws HttpException, UnsupportedEncodingException, IOException, Exception {
		StringBuffer completeURL = new StringBuffer(url);
		boolean first = true;
		if (url.contains("?")) {
			completeURL.append("&");
		} else {
			completeURL.append("?");
		}
		for (String k : kvContent.keySet()) {
			if (!first) {
				completeURL.append("&");
			}	
			first = false;
			completeURL.append(k);
			completeURL.append("=");
			completeURL.append(kvContent.get(k));
		}
		if (logger.isDebugEnabled())
			logger.debug("Encoded request: " + url);

		return executeGetRequest(completeURL.toString(), handler);
	}	
	
	/**
	 * @return the requestEncoding
	 */
	public String getRequestEncoding() {
		return requestEncoding;
	}

	/**
	 * @param requestEncoding the requestEncoding to set
	 */
	public void setRequestEncoding(String requestEncoding) {
		this.requestEncoding = requestEncoding;
	}

	/**
	 * @return the requestParameterKey
	 */
	public String getRequestParameterKey() {
		return requestParameterKey;
	}

	/**
	 * @param requestParameterKey the requestParameterKey to set
	 */
	public void setRequestParameterKey(String requestParameterKey) {
		this.requestParameterKey = requestParameterKey;
	}

	/**
	 * @return Returns the timeoutInMillisec.
	 */
	public int getTimeoutInMillisec() {
		return timeoutInMillisec;
	}

	/**
	 * @param timeoutInMillisec The timeoutInMillisec to set.
	 */
	public void setTimeoutInMillisec(int timeoutInMillisec) {
		this.timeoutInMillisec = timeoutInMillisec;
	}
}
