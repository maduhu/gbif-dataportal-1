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

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.BasicAttributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.gbif.portal.registration.model.UserLogin;

/**
 * CIRCA is an LDAP that is used for authentication
 * This class provides utilities for communication with LDAP
 * 
 * @author trobertson
 * @author dmartin
 */
public class LDAPUtils {
	
	protected static Log logger = LogFactory.getLog(LDAPUtils.class);	
	
	/** The groups a new user should be added to */
	protected String[] userGroups = {"cn=Data Portal Registration Users"};
	/** The URL for the user context */ 
	protected String userLdapUrl;
	/** The URL for the groups context */ 
	protected String groupLdapUrl;
	/** Initial context factory class for connections */
	protected String initialContextFactory = "com.sun.jndi.ldap.LdapCtxFactory";
	/** Authentication type */
	protected String authenticationType = "simple";
	/** Security prinicipal */
	protected String securityPrincipal = "uid=dmartin,ou=People,dc=gbif,dc=org";
	/** Security credentials */
	protected String securityCredentials = "123";
	
	/**
	 * Gets the common, phone and email for the
	 * @param uid To use for searching in LDAP  
	 * @return An array containing the 3 strings
	 * @throws NamingException On error
	 */
	@SuppressWarnings("unchecked")
	public UserLogin getUserLogin(String uid) throws NamingException {
		DirContext ctx = getUserContext();
		try {
			Attributes attributes = ctx.getAttributes("uid=" + uid);
			
			debugAttributes(attributes);
			
			UserLogin ul = new UserLogin();
			ul.setSurname((String)attributes.get("sn").get());
			ul.setFirstName((String)attributes.get("givenName").get());
			ul.setEmail((String)attributes.get("mail").get());
			ul.setUsername(uid);
			return ul;
		} catch (Exception e){
			//expected behaviour for bad username
			logger.debug(e.getMessage(), e);
			return null;
		}
	}
	
	/**
	 * Log attribute values for debug.
	 * @param attributes
	 * @throws NamingException
	 */
	private void debugAttributes(Attributes attributes) throws NamingException {
		if(logger.isDebugEnabled()){
			// useful for debug
			NamingEnumeration attributesEnum =  attributes.getAll();
			while (attributesEnum!=null && attributesEnum.hasMore()) {
				logger.debug("Attribute:" + attributesEnum.next());
			}
		}
	}
	
	/**
	 * Gets the common, phone and email for the
	 * @param uid To use for searching in LDAP  
	 * @return An array containing the 3 strings
	 * @throws NamingException On error
	 */
	@SuppressWarnings("unchecked")
	public List<UserLogin> getUsernamePasswordForEmail(String email) throws NamingException {
		DirContext ctx = getUserContext();
		NamingEnumeration searchResults = ctx.search("", "mail=" +email, null, new SearchControls());
		List<UserLogin> uls = new ArrayList<UserLogin>();
		while(searchResults.hasMore()){
			SearchResult sr = (SearchResult) searchResults.next(); 
			Attributes attributes = sr.getAttributes();
			debugAttributes(attributes);			
			UserLogin ul = new UserLogin();
			ul.setSurname((String)attributes.get("sn").get());
			ul.setFirstName((String)attributes.get("givenName").get());
			ul.setEmail((String)attributes.get("mail").get());
			ul.setUsername((String)attributes.get("uid").get());
			uls.add(ul);
		}
		return uls;
	}

	/**
	 * Get LDAP context.
	 * @param url
	 * @return
	 * @throws NamingException
	 */
	public DirContext getContext(String url) throws NamingException {
		Hashtable env = new Hashtable();
		env.put(Context.PROVIDER_URL,url);
		env.put(Context.INITIAL_CONTEXT_FACTORY,initialContextFactory);
		env.put(Context.SECURITY_AUTHENTICATION,authenticationType);
		env.put(Context.SECURITY_PRINCIPAL,securityPrincipal); 
		env.put(Context.SECURITY_CREDENTIALS,securityCredentials);		
		DirContext ctx = new InitialDirContext(env);
		return ctx;
	}		
	
	/**
	 * Get user context.
	 * @return
	 * @throws NamingException
	 */
	private DirContext getUserContext() throws NamingException {
		return getContext(getUserLdapUrl());
	}	
	
	/**
	 * Get group context.
	 * @return
	 * @throws NamingException
	 */
	private DirContext getGroupContext() throws NamingException {
		return getContext(getGroupLdapUrl());		
	}	
	
	/**
	 * Creates a user. String array contains:
	 * 1) first name
	 * 2) surname
	 * 3) email
	 * 4) username
	 * 5) password
	 * 
	 * @param userDetails
	 * @return
	 * @throws NamingException
	 */
	public boolean createNewUser(UserLogin userLogin) throws NamingException {
		DirContext ctx = getUserContext();
		Attributes attributes = new BasicAttributes();
		attributes.put(new BasicAttribute("sn", userLogin.getSurname()));
		attributes.put(new BasicAttribute("givenName", userLogin.getFirstName()));
		attributes.put(new BasicAttribute("cn", userLogin.getFirstName()+" "+userLogin.getSurname()));
		attributes.put(new BasicAttribute("mail", userLogin.getEmail()));
		if(userLogin.getTelephone()!=null){
			attributes.put(new BasicAttribute("telephoneNumber", userLogin.getTelephone()));
		}
		attributes.put(new BasicAttribute("userPassword", userLogin.getPassword()));
		attributes.put(new BasicAttribute("objectClass", "top"));		
		attributes.put(new BasicAttribute("objectClass", "person"));
		attributes.put(new BasicAttribute("objectClass", "organizationalPerson"));
		attributes.put(new BasicAttribute("objectClass", "inetorgperson"));
		String contextName = "uid="+userLogin.getUsername();
		String fullContextName = contextName+","+ctx.getNameInNamespace();

		//add the user to ldap
		ctx.createSubcontext(contextName, attributes);
		
		//need to add user to group
		for(int i=0; i<userGroups.length; i++){
			DirContext groupContext = getGroupContext();
			Attributes groupAttributes = groupContext.getAttributes(userGroups[i]);
			groupAttributes.get("uniqueMember").add(fullContextName);
			groupContext.modifyAttributes(userGroups[i],DirContext.REPLACE_ATTRIBUTE, groupAttributes);
		}
		return true;
	 }

	/**
	 * Update the details of the supplied user in LDAP.
	 * @param userLogin
	 * @return
	 * @throws NamingException
	 */
	public boolean updateUser(UserLogin userLogin) throws NamingException {
		DirContext ctx = getUserContext();
		Attributes attributes = new BasicAttributes();
		attributes.put(new BasicAttribute("sn", userLogin.getSurname()));
		attributes.put(new BasicAttribute("givenName", userLogin.getFirstName()));
		attributes.put(new BasicAttribute("cn", userLogin.getFirstName()+" "+userLogin.getSurname()));
		attributes.put(new BasicAttribute("mail", userLogin.getEmail()));
		if(userLogin.getTelephone()!=null){
			attributes.put(new BasicAttribute("telephoneNumber", userLogin.getTelephone()));
		}
		attributes.put(new BasicAttribute("userPassword", userLogin.getPassword()));		
		ctx.modifyAttributes("uid="+userLogin.getUsername(),DirContext.REPLACE_ATTRIBUTE, attributes);
		return true;
	}
	
	/**
	 * Update the password for the supplied user.
	 * @param username
	 * @param newPassword
	 * @throws NamingException
	 */
	public void updatePassword(String username, String newPassword) throws NamingException {
		DirContext ctx = getUserContext();
		Attributes attributes = new BasicAttributes();
		attributes.put(new BasicAttribute("userPassword", newPassword));		
		ctx.modifyAttributes("uid="+username,DirContext.REPLACE_ATTRIBUTE, attributes);
	}
	
	/**
	 * Checks to see if the supplied user name is in use.
	 * 
	 * @param userName
	 * @return
	 * @throws NamingException
	 */
	public boolean userNameInUse(String userName) throws NamingException {
		DirContext ctx = getUserContext();
		Attributes attributes = new BasicAttributes();
		try {
			NamingEnumeration searchContext = ctx.search("uid=" +userName, attributes);
		} catch(Exception e){
			//expected behaviour
			return false;
		}
		return true;
	}

	/**
	 * @return the userGroups
	 */
	public String[] getUserGroups() {
		return userGroups;
	}

	/**
	 * @param userGroups the userGroups to set
	 */
	public void setUserGroups(String[] userGroups) {
		this.userGroups = userGroups;
	}

	/**
	 * @return the userLdapUrl
	 */
	public String getUserLdapUrl() {
		return userLdapUrl;
	}

	/**
	 * @param userLdapUrl the userLdapUrl to set
	 */
	public void setUserLdapUrl(String userLdapUrl) {
		this.userLdapUrl = userLdapUrl;
	}

	/**
	 * @return the groupLdapUrl
	 */
	public String getGroupLdapUrl() {
		return groupLdapUrl;
	}

	/**
	 * @param groupLdapUrl the groupLdapUrl to set
	 */
	public void setGroupLdapUrl(String groupLdapUrl) {
		this.groupLdapUrl = groupLdapUrl;
	}

	/**
	 * @return the initialContextFactory
	 */
	public String getInitialContextFactory() {
		return initialContextFactory;
	}

	/**
	 * @param initialContextFactory the initialContextFactory to set
	 */
	public void setInitialContextFactory(String initialContextFactory) {
		this.initialContextFactory = initialContextFactory;
	}

	/**
	 * @return the authenticationType
	 */
	public String getAuthenticationType() {
		return authenticationType;
	}

	/**
	 * @param authenticationType the authenticationType to set
	 */
	public void setAuthenticationType(String authenticationType) {
		this.authenticationType = authenticationType;
	}

	/**
	 * @return the securityPrincipal
	 */
	public String getSecurityPrincipal() {
		return securityPrincipal;
	}

	/**
	 * @param securityPrincipal the securityPrincipal to set
	 */
	public void setSecurityPrincipal(String securityPrincipal) {
		this.securityPrincipal = securityPrincipal;
	}

	/**
	 * @return the securityCredentials
	 */
	public String getSecurityCredentials() {
		return securityCredentials;
	}

	/**
	 * @param securityCredentials the securityCredentials to set
	 */
	public void setSecurityCredentials(String securityCredentials) {
		this.securityCredentials = securityCredentials;
	}
}