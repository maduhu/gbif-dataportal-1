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

/**
 * Agreement check bean. The forwardUrl is the url to forward the request to if the
 * agreement has been accepted by the user. 
 * 
 * @author dmartin
 */
public class AgreementCheck {

	protected boolean accepted;
	
	protected String forwardUrl;

	/**
	 * @return the accepted
	 */
	public boolean isAccepted() {
		return accepted;
	}

	/**
	 * @param accepted the accepted to set
	 */
	public void setAccepted(boolean accepted) {
		this.accepted = accepted;
	}

	/**
	 * @return the originalRequest
	 */
	public String getForwardUrl() {
		return forwardUrl;
	}

	/**
	 * @param originalRequest the originalRequest to set
	 */
	public void setForwardUrl(String originalRequest) {
		this.forwardUrl = originalRequest;
	}
}