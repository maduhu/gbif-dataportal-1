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
package org.gbif.portal.web.controller;

import java.net.URLDecoder;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.gbif.portal.web.util.QueryHelper;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;
/**
 * Controller that takes a url with reuqest parameters and redirects to the
 * REST style url.
 * 
 * This should be useful when dealing with the urls created by 
 * html controls such as input and selects.
 * 
 * @todo refactor to support the passing of multiple parameters.
 * @author dmartin
 */
public class RedirectToRestUrlController implements Controller {

	protected static Log logger = LogFactory.getLog(RedirectToRestUrlController.class);	
	/** The search keyword parameter name */
	protected String requestParamName;
	/** Search url portion e.g. /search/ */
	protected String redirectRootUrl;
	/** Whether or not to perform wildcard substitution */
	protected boolean supportWildcardSubstitution = false;
	/** The array of supported wildcards */
	protected char[] supportedWildcards;
	/** The wildcard supported by Service Layer */
	protected char wildcard;
	/** Whether or not to apply url decoding to request params */
	protected boolean decodeArgument = true;
	/** Whether or not to apply url decoding to request params */
	protected boolean encodePath = false;
	/** Whether to replace whitespace with underscores to avoid %20 style urls */
	protected boolean whitespace2Underscores = true;
	
	/**
	 * Performs the initial blanket search.
	 * @param request
	 * @param response
	 * @return
	 */
	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String originalRequestParam = request.getParameter(requestParamName);
		if(logger.isDebugEnabled()){
			logger.debug("Original request parameter: "+originalRequestParam);
		}
		String path = request.getContextPath()+redirectRootUrl;
		
		if(StringUtils.isNotEmpty(originalRequestParam) && originalRequestParam.trim().length()>0){
				originalRequestParam = originalRequestParam.trim();
				if(supportWildcardSubstitution){
					for (int i=0; i<supportedWildcards.length;i++){
						originalRequestParam=originalRequestParam.replace(supportedWildcards[i], wildcard);
					}
				}				
			try {
				if(decodeArgument){
					originalRequestParam = URLDecoder.decode(originalRequestParam, "UTF-8");
					if(logger.isDebugEnabled()){
						logger.debug("URL decoded value: "+originalRequestParam);
					}
				}
				//replace whitespace with underscores for appearances
				if(whitespace2Underscores){
					originalRequestParam = QueryHelper.tidyValue(originalRequestParam);
					originalRequestParam = originalRequestParam.replace(" ",  "_");
					if(logger.isDebugEnabled()){
						logger.debug("pretty print value: "+originalRequestParam);
					}					
				}
				
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			}
			
			String pathSuffix = originalRequestParam;
			if(encodePath){
				pathSuffix = URLEncoder.encode(originalRequestParam, "UTF-8");
			} 
			path = path+pathSuffix;
			if(logger.isDebugEnabled()){
				logger.debug("Redirecting to: "+path);
			}			
		}	
		response.setCharacterEncoding("UTF-8");
		response.sendRedirect(path);
		return null;
	}
	
	/**
	 * @param keywordParam the keywordParam to set
	 */
	public void setRequestParamName(String keywordParam) {
		this.requestParamName = keywordParam;
	}

	/**
	 * @param searchUrl the searchUrl to set
	 */
	public void setRedirectRootUrl(String searchUrl) {
		this.redirectRootUrl = searchUrl;
	}

	/**
	 * @param supportedWildcards the supportedWildcards to set
	 */
	public void setSupportedWildcards(char[] supportedWildcards) {
		this.supportedWildcards = supportedWildcards;
	}

	/**
	 * @param wildcard the wildcard to set
	 */
	public void setWildcard(char wildcard) {
		this.wildcard = wildcard;
	}

	/**
	 * @param supportWildcardSubstitution the supportWildcardSubstitution to set
	 */
	public void setSupportWildcardSubstitution(boolean supportWildcardSubstitution) {
		this.supportWildcardSubstitution = supportWildcardSubstitution;
	}



	/**
	 * @param underscores2Whitespace the underscores2Whitespace to set
	 */
	public void setWhitespace2Underscores(boolean underscores2Whitespace) {
		this.whitespace2Underscores = underscores2Whitespace;
	}

	/**
	 * @param decodeArgument the decodeArgument to set
	 */
	public void setDecodeArgument(boolean decodeArgument) {
		this.decodeArgument = decodeArgument;
	}

	/**
	 * @param encodePath the encodePath to set
	 */
	public void setEncodePath(boolean encodePath) {
		this.encodePath = encodePath;
	}
}