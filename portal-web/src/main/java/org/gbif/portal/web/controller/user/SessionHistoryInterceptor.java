/***************************************************************************
 * Copyright (C) 2005 Global Biodiversity Information Facility Secretariat.
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

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.gbif.portal.dto.KeyValueDTO;
import org.gbif.portal.web.util.QueryHelper;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/**
 * Stores URL and page titles pairs in a session history.
 * 
 * Session history is stored with the last viewed page first.
 * 
 * @author dmartin
 */
public class SessionHistoryInterceptor extends HandlerInterceptorAdapter {

	protected static Log logger = LogFactory.getLog(SessionHistoryInterceptor.class);
	/** The maximum number of items to store */
	protected int maxSessionHistoryItems = 20;
	/** URL patterns that shouldn't be recorded */	
	protected List<String> ignoreUrlPatterns;
	/** The request attribute holding the page title */		
	protected String pageTitleRequestKey = "title";
	/** The session history key */			
	protected String sessionHistoryKey = "sessionHistory";

	/** The session history key */			
	protected List<String> browsePatterns;	
	
	protected List<String> fullBrowsePatterns;	
	
	private boolean browsePatternsInitialised = false;
	
	/**
	 * @see org.springframework.web.servlet.HandlerInterceptor#afterCompletion(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object, java.lang.Exception)
	 */
	@SuppressWarnings("unchecked")
	public void afterCompletion(HttpServletRequest request,
			HttpServletResponse response, Object object, Exception exception)
			throws Exception {

		//retrieve the page title from the request
		String pageTitle = (String) request.getAttribute(pageTitleRequestKey);
		pageTitle = QueryHelper.tidyValue(pageTitle);
		if(logger.isDebugEnabled() && StringUtils.isNotEmpty(pageTitle)){
			logger.debug(pageTitle);
		}

		//if its not empty then it isn't an AJAX or tile request		
		if(StringUtils.isNotEmpty(pageTitle)){
			StringBuffer requestUrl = new StringBuffer(request.getRequestURL());
			if(StringUtils.isNotEmpty(request.getQueryString())){
				requestUrl.append('?');
				requestUrl.append(request.getQueryString());
			}
			
			//get the request URI for matching ignore patterns
			String requestURI = request.getRequestURI();
			String contextPath = request.getContextPath();
			requestURI = requestURI.replace(contextPath, "");
			
			if(ignoreUrlPatterns==null || !ignoreUrlPatterns.contains(requestURI)){
			
				//ignore if they've hit refresh
				List<KeyValueDTO> sessionHistory = (List<KeyValueDTO>) request.getSession().getAttribute(sessionHistoryKey);
				if(sessionHistory==null){
					sessionHistory = new LinkedList<KeyValueDTO>();
					request.getSession().setAttribute(sessionHistoryKey, sessionHistory);
				}
				
				String fullRequestUrl = requestUrl.toString();
				KeyValueDTO historyItem = new KeyValueDTO(fullRequestUrl, pageTitle);
	
				//ignore if they've hit refresh
				KeyValueDTO lastHistoryItem = null;
				if(!sessionHistory.isEmpty())
					lastHistoryItem = sessionHistory.get(0);
				
				if(lastHistoryItem==null || !lastHistoryItem.equals(historyItem)){
					if(logger.isDebugEnabled()){
						logger.debug("Adding to history: "+historyItem);
					}
					sessionHistory.add(0, historyItem);
				}
				
				//if this item exists anywhere else remove it
				synchronized (sessionHistory) {
					int index = sessionHistory.lastIndexOf(historyItem);
					if(index>0){
						sessionHistory.remove(index);
					}
					
					if(browsePatterns!=null){
						if(!browsePatternsInitialised){
							fullBrowsePatterns = new ArrayList<String>();	
							for(String browsePattern: browsePatterns){
								//construct full url
								browsePattern = "http://"+request.getHeader("host")+request.getContextPath()+"/"+browsePattern;
								fullBrowsePatterns.add(browsePattern);
							}
							browsePatternsInitialised=true;
						}

						//remove any matching patterns
						KeyValueDTO toBeRemoved = null;
						for(String browsePattern: fullBrowsePatterns){
							for(KeyValueDTO kv: sessionHistory){
								if(kv.getKey().startsWith(browsePattern) && fullRequestUrl.startsWith(browsePattern)){
									if(sessionHistory.indexOf(kv)>0){
//										logger.debug("Removing history item : "+kv.getKey());
										toBeRemoved = kv;
										break;
									}
								}
							}
							if(toBeRemoved!=null)
								break;
						}
						
						if(toBeRemoved!=null){
							sessionHistory.remove(toBeRemoved);
						}
					}
				}
				
				//remove the last item if it exceeds maximum
				if(sessionHistory.size()>maxSessionHistoryItems){
					sessionHistory.remove(sessionHistory.size()-1);
				}
			}
		}
	}

	/**
	 * @param maxSessionHistoryItems the maxSessionHistoryItems to set
	 */
	public void setMaxSessionHistoryItems(int maxSessionHistoryItems) {
		this.maxSessionHistoryItems = maxSessionHistoryItems;
	}

	/**
	 * @param ignores the ignores to set
	 */
	public void setIgnoreUrlPatterns(List<String> ignores) {
		this.ignoreUrlPatterns = ignores;
	}

	/**
	 * @param pageTitleRequestKey the pageTitleRequestKey to set
	 */
	public void setPageTitleRequestKey(String pageTitleRequestKey) {
		this.pageTitleRequestKey = pageTitleRequestKey;
	}

	/**
	 * @param sessionHistoryKey the sessionHistoryKey to set
	 */
	public void setSessionHistoryKey(String sessionHistoryKey) {
		this.sessionHistoryKey = sessionHistoryKey;
	}

	/**
	 * @param browsePatterns the browsePatterns to set
	 */
	public void setBrowsePatterns(List<String> browsePatterns) {
		this.browsePatterns = browsePatterns;
	}
}