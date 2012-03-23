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
package org.gbif.portal.registration;

import junit.framework.TestCase;

import org.gbif.portal.registration.model.UserLogin;

/**
 * @author dave
 */
public class LDAPUtilsTest extends TestCase {

	public void testCreateUserTest() throws Exception{
		try{
			LDAPUtils l = new LDAPUtils();
			l.setUserLdapUrl("ldap://listserv.gbif.org:389/ou=People,dc=gbif,dc=org");
			l.setGroupLdapUrl("ldap://listserv.gbif.org:389/ou=Groups,dc=gbif,dc=org");
			UserLogin userLogin = new UserLogin();
			userLogin.setUsername("DMartin2");
			userLogin.setFirstName("Dave");
			userLogin.setSurname("Martin");
			userLogin.setEmail("dmartin@gbif.org");
			userLogin.setPassword("password");
			l.createNewUser(userLogin);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void testUpdateUser() throws Exception {
		try{
			LDAPUtils l = new LDAPUtils();
			l.setUserLdapUrl("ldap://listserv.gbif.org:389/ou=People,dc=gbif,dc=org");
			l.setGroupLdapUrl("ldap://listserv.gbif.org:389/ou=Groups,dc=gbif,dc=org");		
			UserLogin userLogin = new UserLogin();
			userLogin.setUsername("DMartin2");
			userLogin.setFirstName("Dave");
			userLogin.setSurname("Martin");
			userLogin.setEmail("dmartin@gbif.org");
			userLogin.setPassword("password2");
			l.updateUser(userLogin);		
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}