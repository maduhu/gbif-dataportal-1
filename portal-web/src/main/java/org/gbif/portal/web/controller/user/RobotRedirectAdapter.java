/***************************************************************************
 * Copyright (C) 2008 Global Biodiversity Information Facility Secretariat.  
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
package org.gbif.portal.web.controller.user;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/**
 * Simple adapter that looks for user agents and redirects
 * 
 * @author davejmartin
 */
public class RobotRedirectAdapter extends HandlerInterceptorAdapter {
	
	protected static Log logger = LogFactory.getLog(RobotRedirectAdapter.class);

	protected List<String> userAgents;
	
	protected List<String> patterns;
	
	protected String redirectUrl;

	
	/**
   * @see org.springframework.web.servlet.handler.HandlerInterceptorAdapter#preHandle(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object)
   */
  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
  	
  	if(userAgents!=null && !userAgents.isEmpty() && patterns!=null && !patterns.isEmpty()){
  		
			StringBuffer requestedUrlBuffer = request.getRequestURL();
			String requestURI = requestedUrlBuffer.toString();
  		
			String userAgent = request.getHeader("User-Agent");
			if(logger.isDebugEnabled()){
				logger.debug("User agent in request: " + userAgent);
			}
			
			if (userAgent != null) {
				for (String agent : userAgents) {
					if (userAgent.toUpperCase().contains(agent.toUpperCase())) {
						if(logger.isDebugEnabled()){
							logger.debug("Checking pattern for: " + userAgent);
						}
						
						for(String pattern: patterns){
							if(requestURI.indexOf(pattern)!=-1){
								if(logger.isInfoEnabled()){
									logger.info("Bad robot check. URL: "+requestURI+" matches pattern: "+pattern);
								}
								response.sendRedirect(request.getContextPath()+redirectUrl);
								return false;
							}
						}  							
					}
				}
			}
  	}
	  return true;
  }

	/**
   * @param userAgents the userAgents to set
   */
  public void setUserAgents(List<String> userAgents) {
  	this.userAgents = userAgents;
  }

	/**
   * @return the patterns
   */
  public List<String> getPatterns() {
  	return patterns;
  }

	/**
   * @param patterns the patterns to set
   */
  public void setPatterns(List<String> patterns) {
  	this.patterns = patterns;
  }

	/**
   * @param redirectUrl the redirectUrl to set
   */
  public void setRedirectUrl(String redirectUrl) {
  	this.redirectUrl = redirectUrl;
  }
}