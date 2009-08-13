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

import java.net.URLDecoder;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;
import org.springframework.web.servlet.view.RedirectView;

/**
 * A Simple Form Controller for setting an Accepted Terms and Conditions cookie.
 * 
 * @author dmartin
 */
public class AcceptTermsController extends SimpleFormController {

	protected String disclaimerCookieName = "GbifTermsAndConditions";
	protected String disclaimerCookieValue = "accepted";
	protected String disclaimerNotAcceptedUrl = "/tutorial/";
	protected String forwardUrlRequestKey = "forwardUrl";
	protected String defaultForwardUrl="/";
	protected int cookieMaxAge = Integer.MAX_VALUE;
	protected String domain = ".gbif.org";
	/**
	 * @see org.springframework.web.servlet.mvc.SimpleFormController#onSubmit(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object, org.springframework.validation.BindException)
	 */
	@Override
	protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object commandObject, BindException bindException) throws Exception {
		AgreementCheck disclaimerCheck = (AgreementCheck) commandObject;
		if(disclaimerCheck.accepted){
			Cookie cookie = new Cookie(disclaimerCookieName, disclaimerCookieValue);
			cookie.setMaxAge(cookieMaxAge);
			cookie.setDomain(domain);
			
			//create a cookie
			response.addCookie(cookie);
			//add to session 
			request.getSession().setAttribute(disclaimerCookieName, disclaimerCookieValue);
			if(disclaimerCheck.forwardUrl==null)
				disclaimerCheck.forwardUrl = URLDecoder.decode(disclaimerCheck.forwardUrl, "UTF-8");

			//redirect to forwardURL
			return new ModelAndView(new RedirectView(disclaimerCheck.forwardUrl));
		} else {
			return new ModelAndView(new RedirectView(request.getContextPath()+disclaimerNotAcceptedUrl));	
		}
	}	
	
	/**
	 * Set the current theme.
	 * 
	 * @see org.springframework.web.servlet.mvc.AbstractFormController#formBackingObject(javax.servlet.http.HttpServletRequest)
	 */
	@Override
	protected Object formBackingObject(HttpServletRequest request) throws Exception {
		AgreementCheck disclaimerCheck = new AgreementCheck();
		String forwardUrl = request.getParameter(forwardUrlRequestKey);
		if(forwardUrl==null)
			forwardUrl=defaultForwardUrl;
		disclaimerCheck.setForwardUrl(forwardUrl);
		disclaimerCheck.setAccepted(request.getParameter("acceptedTerms")!=null);
		return disclaimerCheck;
	}

	/**
	 * @param cookieMaxAge the cookieMaxAge to set
	 */
	public void setCookieMaxAge(int cookieMaxAge) {
		this.cookieMaxAge = cookieMaxAge;
	}

	/**
	 * @return the defaultForwardUrl
	 */
	public String getDefaultForwardUrl() {
		return defaultForwardUrl;
	}

	/**
	 * @param defaultForwardUrl the defaultForwardUrl to set
	 */
	public void setDefaultForwardUrl(String defaultForwardUrl) {
		this.defaultForwardUrl = defaultForwardUrl;
	}

	/**
	 * @return the disclaimerCookieName
	 */
	public String getDisclaimerCookieName() {
		return disclaimerCookieName;
	}

	/**
	 * @param disclaimerCookieName the disclaimerCookieName to set
	 */
	public void setDisclaimerCookieName(String disclaimerCookieName) {
		this.disclaimerCookieName = disclaimerCookieName;
	}

	/**
	 * @return the disclaimerCookieValue
	 */
	public String getDisclaimerCookieValue() {
		return disclaimerCookieValue;
	}

	/**
	 * @param disclaimerCookieValue the disclaimerCookieValue to set
	 */
	public void setDisclaimerCookieValue(String disclaimerCookieValue) {
		this.disclaimerCookieValue = disclaimerCookieValue;
	}

	/**
	 * @return the disclaimerNotAcceptedUrl
	 */
	public String getDisclaimerNotAcceptedUrl() {
		return disclaimerNotAcceptedUrl;
	}

	/**
	 * @param disclaimerNotAcceptedUrl the disclaimerNotAcceptedUrl to set
	 */
	public void setDisclaimerNotAcceptedUrl(String disclaimerNotAcceptedUrl) {
		this.disclaimerNotAcceptedUrl = disclaimerNotAcceptedUrl;
	}

	/**
	 * @return the forwardUrlRequestKey
	 */
	public String getForwardUrlRequestKey() {
		return forwardUrlRequestKey;
	}

	/**
	 * @param forwardUrlRequestKey the forwardUrlRequestKey to set
	 */
	public void setForwardUrlRequestKey(String forwardUrlRequestKey) {
		this.forwardUrlRequestKey = forwardUrlRequestKey;
	}

	/**
	 * @return the cookieMaxAge
	 */
	public int getCookieMaxAge() {
		return cookieMaxAge;
	}

	/**
	 * @return the domain
	 */
	public String getDomain() {
		return domain;
	}

	/**
	 * @param domain the domain to set
	 */
	public void setDomain(String domain) {
		this.domain = domain;
	}	
}