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
package org.gbif.portal.model.resources;

import org.gbif.portal.model.BaseObject;

/**
 * @author dmartin
 */
public class RegistrationLogin extends BaseObject {

	private String loginId;
	private String businessKey;
	
	public RegistrationLogin() {}
	
	public RegistrationLogin(String loginId, String businessKey) {
		this.loginId = loginId;
		this.businessKey = businessKey;
	}
	/**
	 * @return the businessKey
	 */
	public String getBusinessKey() {
		return businessKey;
	}
	/**
	 * @param businessKey the businessKey to set
	 */
	public void setBusinessKey(String businessKey) {
		this.businessKey = businessKey;
	}
	/**
	 * @return the loginId
	 */
	public String getLoginId() {
		return loginId;
	}
	/**
	 * @param loginId the loginId to set
	 */
	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}
}