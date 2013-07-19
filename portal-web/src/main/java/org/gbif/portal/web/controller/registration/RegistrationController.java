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
package org.gbif.portal.web.controller.registration;

import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;
import org.gbif.portal.dto.KeyValueDTO;
import org.gbif.portal.dto.resources.DataProviderDTO;
import org.gbif.portal.dto.resources.DataResourceDTO;
import org.gbif.portal.registration.ErrorMessageKeys;
import org.gbif.portal.registration.LDAPUtils;
import org.gbif.portal.registration.ResourceExtractionUtils;
import org.gbif.portal.registration.UDDIUtils;
import org.gbif.portal.registration.model.ProviderDetail;
import org.gbif.portal.registration.model.ResourceDetail;
import org.gbif.portal.registration.model.UserLogin;
import org.gbif.portal.service.DataResourceManager;
import org.gbif.portal.service.GeospatialManager;
import org.gbif.portal.service.TaxonomyManager;
import org.gbif.portal.util.mhf.message.Message;
import org.gbif.portal.util.propertystore.PropertyStore;
import org.gbif.portal.util.workflow.ProcessContext;
import org.gbif.portal.util.workflow.Processor;
import org.gbif.portal.web.util.DateUtil;
import org.gbif.portal.web.util.PasswordUtils;
import org.springframework.context.MessageSource;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;
import org.springframework.web.servlet.mvc.multiaction.NoSuchRequestHandlingMethodException;
import org.springframework.web.servlet.support.RequestContextUtils;
import org.springframework.web.servlet.view.RedirectView;
import org.uddi4j.UDDIException;
import org.uddi4j.transport.TransportException;

/**
 * The dispatcher controller for all provider updating activities for the
 * wizard to create or update a provider.
 * 
 * The user is capable of creating and editing data providers in uddi
 * Forms are pre-populated from the LDAP configuration, 
 * 
 * @author trobertson
 * @author dmartin
 */
public class RegistrationController extends MultiActionController {
	
	/** The request constants */
	public static final String REQUEST_BUSINESS_UDDI_KEY = "businessKey";
	public static final String REQUEST_CONTACT_TYPES = "contactTypes";
	public static final String REQUEST_PROVIDER_DETAIL = "providerDetail";
	public static final String REQUEST_COUNTRIES = "countries";
	public static final String REQUEST_RESOURCE_NETWORKS = "resourceNetworks";	
	public static final String REQUEST_BASES_OF_RECORD = "basesOfRecord";
	public static final String REQUEST_RESOURCE_URL = "resourceUrl";
	public static final String REQUEST_RESOURCES = "resources";
	public static final String REQUEST_RESOURCE = "resource";
	public static final String REQUEST_SELECTED_CONCEPT = "selectedConcept";
	public static final String REQUEST_DATA_PROVIDER = "dataProvider";
	public static final String REQUEST_DATA_RESOURCE = "dataResource";
	public static final String REQUEST_CONCEPTS = "concepts";
	public static final String REQUEST_RESOURCE_TYPES = "resourceTypes";
	
	/** The name of the admin role */
	protected String adminRole ="Portal Admins";
	
	/** MailSender */
	protected MailSender mailSender;
	
	protected SimpleMailMessage userTemplateMessage;	
	
	protected String usernamePostfix = "";
	
	/** The LDAP URL */
	protected String ldapUrl;
	
	/** Utils */
	protected UDDIUtils uddiUtils;
	protected LDAPUtils ldapUtils;
	protected ResourceExtractionUtils resourceExtractionUtils;
	
	/** The property store */
	protected PropertyStore propertyStore;
	
	/** Managers */
	protected GeospatialManager geospatialManager;
	protected DataResourceManager dataResourceManager;
	protected TaxonomyManager taxonomyManager;
	
	/** the basis of records */
	protected List<String> basisOfRecordTypes;
	
	/** the resource types supported */
	protected List<String> resourceTypes;

	/** resource 2 namespace mappings */
	protected Map<String, String> resourceType2namespaceList;
	
	/** Minimum username length */
	protected int minimumUsernameLength = 6;
	
	/** Minimum password length */
	protected int minimumPasswordLength = 6;
	
	public PasswordUtils passwordUtils = new PasswordUtils();
	
	protected String adminEmail = "portal@gbif.org";
	
	/** Message source for i18n */
	protected MessageSource messageSource;
	
	/**
	 * Generates a password for a reset password.
	 * @param userName
	 * @return
	 */
	public String generatePassword(String userName){
		String password = userName+passwordUtils.getEncryptKey()+System.currentTimeMillis();
		int hashCode = password.hashCode();
		return Integer.toHexString(hashCode);
	}
	
	/**
	 * @see org.springframework.web.servlet.mvc.multiaction.MultiActionController#handleNoSuchRequestHandlingMethod(org.springframework.web.servlet.mvc.multiaction.NoSuchRequestHandlingMethodException, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected ModelAndView handleNoSuchRequestHandlingMethod(NoSuchRequestHandlingMethodException exception, HttpServletRequest request, HttpServletResponse response) throws Exception {
		if(StringUtils.isEmpty(exception.getMethodName())){
			return viewAdminMenu(request, response);
		}
		return super.handleNoSuchRequestHandlingMethod(exception, request, response);
	}
	
	/**
	 * User verification.
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ModelAndView verification(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String firstName = request.getParameter("fn");
		String surname = request.getParameter("sn");
		String email = request.getParameter("e");
		String username = request.getParameter("u");
		String password = request.getParameter("p");
		String decryptedPassword = passwordUtils.encryptPassword(password, false);
		UserLogin userLogin = new UserLogin(firstName, surname, email, username, decryptedPassword);
		//check the user hasn't already been created
		if(!ldapUtils.userNameInUse(username)){
			ldapUtils.createNewUser(userLogin);
			List<DataProviderDTO> dataProviders = dataResourceManager.findDataProvidersForUser(userLogin.getEmail());
			for(DataProviderDTO dataProvider: dataProviders){
				uddiUtils.createRegistrationLogin(username, dataProvider.getUuid());
			}
		}
		return new ModelAndView(new RedirectView(request.getContextPath()+"/register/?u="+username+"&p="+password));
	}
	
	/**
	 * Forgotten password.
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ModelAndView forgottenPassword(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		if(!isFormSubmission(request)){
			ModelAndView mav = new ModelAndView("forgottenPassword");
			return mav;
		}
		
		String email = request.getParameter("email");
		List<UserLogin> uls = ldapUtils.getUsernamePasswordForEmail(email);

		if(uls.isEmpty()){
			ModelAndView mav = new ModelAndView("forgottenPassword");
			mav.addObject("email", email);
			mav.addObject("unrecognised", true);
			return mav;
		}
		
		for (UserLogin ul : uls){
		
			//reset password
			String newPassword = generatePassword(ul.getUsername());
			ul.setPassword(newPassword);
			ldapUtils.updateUser(ul);
			
			//send verification email
			SimpleMailMessage verificationMessage = new SimpleMailMessage(userTemplateMessage);
			verificationMessage.setTo(email);
			String encryptedPassword = passwordUtils.encryptPassword(ul.getPassword(), true);
			verificationMessage.setSubject("Details for GBIF Data Portal");
			verificationMessage.setText("Please click here to login:\n\n" 
					+"http://"
					+request.getHeader("host")
					+request.getContextPath()
					+"/register/"
					+"?u="+ul.getUsername()
					+"&p="+encryptedPassword
					+"\n\nUsername: "+ul.getUsername()
					+"\nPassword: "+ul.getPassword()
				);
			try {
	            mailSender.send(verificationMessage);
	        } catch(MailException e) {
	            // simply log it and go on...
	            logger.error("Couldn't send message", e);
	            ModelAndView mav = new ModelAndView("emailDetailsFailureView");
	            mav.addObject("email", email);
	            return mav;
	        }
		}
		
        ModelAndView mav = new ModelAndView("emailDetailsSuccessView");
        mav.addObject("email", email);
        return mav;
	}

	/**
	 * Create a new user in LDAP.
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ModelAndView resetPassword(HttpServletRequest request, HttpServletResponse response) throws Exception {

		if(!isFormSubmission(request)){
			return new ModelAndView("resetPassword");
		}
		
		String password = request.getParameter("password");
		if(!validatePassword(password)){
			ModelAndView mav =  new ModelAndView("resetPassword");
			mav.addObject("password", password);
			mav.addObject("invalidPassword", true);
			return mav;
		}

		String remoteUser = request.getRemoteUser();
		ldapUtils.updatePassword(remoteUser, password);
		return new ModelAndView("passwordReset");
	}
	
	/**
	 * Check for invalid characters.
	 * @param username
	 * @return
	 */
	private boolean validateUsername(String username) {
		if(username !=null && username.length()>minimumUsernameLength){
			Pattern p = Pattern.compile("^[A-Za-z]\\w{5,30}$");
			Matcher m = p.matcher(username);
			boolean valid = m.matches();		
			return valid;
		}
		return false;
	}	
	
//	public  static void main(String[] args){
//		RegistrationController rc = new RegistrationController(); 
//		System.out.println(rc.validateUsername("dmartin"));
//		System.out.println(rc.validateUsername("dmartin::"));
//		System.out.println(rc.validateUsername("dmartin123"));
//	}
	
	/**
	 * 
	 * @param password
	 * @return
	 */
	private boolean validatePassword(String password) {
		if(password !=null && password.length()>minimumPasswordLength)
			return true;
		return false;
	}

	/**
	 * Create a new user in LDAP.
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ModelAndView newUser(HttpServletRequest request, HttpServletResponse response) throws Exception {

		if(!isFormSubmission(request)){
			ModelAndView mav = new ModelAndView("newUser");
			UserLogin ul = new UserLogin();
			mav.addObject("user", ul);
			return mav;		
		}

		 UserLogin ul = new UserLogin();
		 ServletRequestDataBinder binder = createBinder(request, ul);
		 binder.bind(request);
		 BindingResult result = binder.getBindingResult();
		 String systemUserName = getSystemUserName(ul.getUsername());
		 String suggestedUsername = null;
		 //validate
		if(StringUtils.isEmpty(ul.getUsername())){
			result.rejectValue("username", ErrorMessageKeys.MUST_BE_SPECIFIED);
		} else if (ul.getUsername().length()<minimumUsernameLength){
			result.rejectValue("username", ErrorMessageKeys.MUST_BE_MINIMIUM_LENGTH);
		} else if (!validateUsername(ul.getUsername())){
			result.rejectValue("username", ErrorMessageKeys.CONTAINS_INVALID_CHARS);			
		} else if(ldapUtils.userNameInUse(systemUserName)){
			//iterate until a username available
			for(int i=0; i<Integer.MAX_VALUE; i++){
				suggestedUsername = getSystemUserName(ul.getUsername()+i);
				if(!ldapUtils.userNameInUse(suggestedUsername)){
					break;
				}
			}
			result.rejectValue("username", ErrorMessageKeys.USERNAME_IN_USE);
		}
		
		if(StringUtils.isEmpty(ul.getFirstName())){
			result.rejectValue("firstName", ErrorMessageKeys.MUST_BE_SPECIFIED);
		}
		if(StringUtils.isEmpty(ul.getSurname())){
			result.rejectValue("surname", ErrorMessageKeys.MUST_BE_SPECIFIED);
		}
		
		if(!StringUtils.isEmpty(ul.getEmail())){
			Pattern p = Pattern.compile(".+@.+\\.[a-z]+");
			Matcher m = p.matcher(ul.getEmail());
			boolean validEmail = m.matches();
			if(!validEmail)
				result.rejectValue("email", ErrorMessageKeys.INVALID_VALUE);
		} else {
			result.rejectValue("email", ErrorMessageKeys.MUST_BE_SPECIFIED);
		}
		if(StringUtils.isEmpty(ul.getPassword())){
			result.rejectValue("password", ErrorMessageKeys.MUST_BE_SPECIFIED);
		} else if (!validatePassword(ul.getPassword())){
			result.rejectValue("password", ErrorMessageKeys.MUST_BE_MINIMIUM_LENGTH);
		}
		
		if(result.hasErrors()){
			ModelAndView mav = new ModelAndView("newUser");
			if(suggestedUsername!=null){
				mav.addObject("suggestedUsername", suggestedUsername);
			}
			mav.addObject(BindingResult.MODEL_KEY_PREFIX + "user", result);
			return mav;
		}
		
		//send verification email
		SimpleMailMessage verificationMessage = new SimpleMailMessage(userTemplateMessage);
		verificationMessage.setTo(ul.getEmail());
		String encryptedPassword = passwordUtils.encryptPassword(ul.getPassword(), true);
		verificationMessage.setSubject("Confirm e-mail address for GBIF Data Portal");
		verificationMessage.setText("Please visit the following link to confirm your e-mail address:\n\n" +
				"http://"+request.getHeader("host")
				+request.getContextPath()
				+"/user/verification"
				+"?fn="+ul.getFirstName()
				+"&sn="+ ul.getSurname()
				+"&e="+ul.getEmail()
				+"&u="+ul.getUsername()
				+"&p="+encryptedPassword);
		try{
            mailSender.send(verificationMessage);
        } catch(MailException e) {
            // simply log it and go on...
            logger.error("Couldn't send message", e);
            ModelAndView mav = new ModelAndView("registrationVerificationFailureView");
            mav.addObject("user", ul);
            return mav;
        }
		
        //successful
        ModelAndView mav = new ModelAndView("registrationVerificationSuccessView");
        mav.addObject("user", ul);
		return mav;
	}
	
	/**
	 * 
	 * @param username
	 * @return
	 */
	private String getSystemUserName(String username) {
		if(StringUtils.isNotEmpty(usernamePostfix)){
			return username+usernamePostfix;
		}
		return username;
	}

	/**
	 * Log the current user out.
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ModelAndView logoutUser(HttpServletRequest request, HttpServletResponse response) throws Exception {	
		request.getSession().invalidate();
		return new ModelAndView(new RedirectView(request.getContextPath()+"/register/"));		
	}
	
	/**
	 * The entry point once a user has logged in.
	 */
	@SuppressWarnings("unchecked")
	public ModelAndView viewAdminMenu(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		UserLogin userLogin = ldapUtils.getUserLogin(request.getRemoteUser());
		//retrieve login/business key mapping
		
		List<String> businessKeys = uddiUtils.getAssociatedBusinessKeys(request.getRemoteUser());
		List<ProviderDetail> pds = new ArrayList<ProviderDetail>();
		
		for(int i=0; i<businessKeys.size(); i++){
			ProviderDetail pd = uddiUtils.createProviderFromUDDI(businessKeys.get(i), userLogin.getFullName());
			pds.add(pd);
		}
		ModelAndView mav = new ModelAndView("registrationMain");
		mav.addObject("providerDetails", pds);
		
		if(StringUtils.isNotEmpty(userLogin.getFullName())){
			mav.addObject("username", userLogin.getFullName());
		} else {
			mav.addObject("username", userLogin.getUsername());
		}
		return mav;
	}
	
	/**
	 * Refreshes the details for the supplied dataset using the latest available for its metadata
	 * request.
	 * 
	 * @return
	 */
	public ModelAndView refreshMetadataDetails(HttpServletRequest request, HttpServletResponse response) throws Exception {

		// get the provider from UDDI if necessary
		ProviderDetail provider = retrieveProviderFromUDDI(request); 
		if(provider==null){
			return new ModelAndView("registrationBadBusinessKey");
		}		
		
		//retrieve resource details
		ResourceDetail resource = retrieveDataResource(request, provider);		
		
		//retrieve resources from access point
		List<ResourceDetail> resources = null;
		try {
			resources = contactProviderForMetadata(resource.getAccessPoint());
		} catch(Exception e){
			logger.error(e.getMessage(), e);
			ModelAndView mav = new ModelAndView("refreshMetadataFailure");
			mav.addObject("resource", resource);
			return mav;
		}

		ResourceDetail metadataResource = null; 
		if(resources.size()==1){
			logger.debug("Only one resource found at end point. Assuming same resource.");
			metadataResource = resources.get(0);
		} else {
			//use name to retrieve the properties
			for(ResourceDetail retrievedResource : resources){
				if(resource.getName().equals(retrievedResource.getName())){
					metadataResource = retrievedResource;
					break;
				}
			}
		}

		//store the updated properties
		List<String> updatedProperties = new ArrayList<String>();

		//find the list of metadata supplied properties
		List<String> readonlyProperties = null;

		boolean refreshFailure = false;
		
		//if not null, update properties available from metadata
		if(metadataResource!=null){
			readonlyProperties = retrieveReadonlyPropertiesForResource(metadataResource);
			for(String readonlyProperty: readonlyProperties){
				Object newValue = PropertyUtils.getProperty(metadataResource, readonlyProperty);
				Object oldValue = PropertyUtils.getProperty(resource, readonlyProperty);
				if(newValue!=null && !newValue.equals(oldValue)){
					PropertyUtils.setProperty(resource, readonlyProperty, newValue);
					updatedProperties.add(readonlyProperty);
				}
			}
		} else {
			refreshFailure = true;
		}
		
		//uddiUtils.updateResource(resource, provider.getBusinessKey());
		
		ModelAndView mav = new ModelAndView("registrationResourceDetail");
		mav.addAllObjects(referenceDataForResource(request, resource));
		mav.addObject(RegistrationController.REQUEST_PROVIDER_DETAIL, provider);
		mav.addObject(RegistrationController.REQUEST_RESOURCE, resource);
		mav.addObject("readonlyProperties", readonlyProperties);
		mav.addObject("updatedProperties", updatedProperties);
		mav.addObject("refreshFailure", refreshFailure);
		mav.addObject("metadataRefresh", true);
		return mav;	
	}

	/**
	 * Enables a user to find a provider and request access to provider details.
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ModelAndView findDataProvider(HttpServletRequest request, HttpServletResponse response) throws Exception {	
		List<KeyValueDTO> providerList = uddiUtils.getProviderListAsKeyValues();
		List<String> businessKeys = uddiUtils.getAssociatedBusinessKeys(request.getRemoteUser());
		List<KeyValueDTO> providerRegistrationLogins = new ArrayList<KeyValueDTO>();
		
		List<KeyValueDTO> toRemove = new ArrayList<KeyValueDTO>();
		for(KeyValueDTO providerKV:providerList){
			if(businessKeys.contains(providerKV.getKey())){
				providerRegistrationLogins.add(providerKV);
				toRemove.add(providerKV);
			} 
		}

		//remove the ones already accessible
		providerList.removeAll(toRemove);
		
		//view this of providers
		ModelAndView mav = new ModelAndView("registrationProviderList");
		mav.addObject("providerList", providerList);
		mav.addObject("providerRegistrationLogins", providerRegistrationLogins);
		
		//if user is admin, not need to send requests
		if(request.isUserInRole(adminRole)){
			mav.addObject("updateAction","updateRegistrationLogins");
		} else {
			mav.addObject("updateAction","sendRegistrationLoginsRequest");
		}
		return mav;
	}

	/**
	 * Show the update user screen.
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ModelAndView userUpdate(HttpServletRequest request, HttpServletResponse response) throws Exception {	
		ModelAndView mav = new ModelAndView("registrationUserUpdate");
		return mav;
	}	
	
	/**
	 * Setup page for a new data provider. When registering a new contact, these some details are pre-populated from LDAP
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ModelAndView addRegistrationLogins(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		String user = request.getParameter("user");
		String email = request.getParameter("email");

		//retrieve the user
		UserLogin userLogin = null;
		if(StringUtils.isNotEmpty(user)){
			userLogin = ldapUtils.getUserLogin(user);
		}
		
		//if not found, try with email
		if(userLogin==null && StringUtils.isNotEmpty(email)){
			List<UserLogin> userLogins = ldapUtils.getUsernamePasswordForEmail(email);
			if(userLogins.size()>1 || userLogins.isEmpty()){
				ModelAndView mav = new ModelAndView("registrationUserUpdate");
				if(userLogins.isEmpty()){
					mav.addObject("userNotFound", true);
					mav.addObject("user", user);
					mav.addObject("email", email);
				} else {
					mav.addObject("userLogins", userLogins);					
				}
				return mav;
			} else {
				userLogin = userLogins.get(0);
			}
		}

		//if not found at all
		if(userLogin==null){
			ModelAndView mav = new ModelAndView("registrationUserUpdate");
			mav.addObject("userNotFound", true);
			mav.addObject("user", user);
			mav.addObject("email", email);
			return mav;
		}			
		
		String username = getSystemUserName(userLogin.getUsername());
		
		List<KeyValueDTO> providerList = uddiUtils.getProviderListAsKeyValues();
		List<String> businessKeys = uddiUtils.getAssociatedBusinessKeys(username);
		List<KeyValueDTO> providerRegistrationLogins = new ArrayList<KeyValueDTO>();
		for(KeyValueDTO providerKV:providerList){
			if(businessKeys.contains(providerKV.getKey())){
				providerRegistrationLogins.add(providerKV);
			}
		}
//		providerList.removeAll(providerRegistrationLogins);
		ModelAndView mav = new ModelAndView("registrationProviderList");
		mav.addObject("providerList", providerList);
		mav.addObject("providerRegistrationLogins", providerRegistrationLogins);
		mav.addObject("userLogin", userLogin);
		mav.addObject("updateAction","updateRegistrationLogins");		
		return mav;
	}	
	
	/**
	 * Admin task to review a users request to see details.
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ModelAndView reviewUserRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {	
		
		ModelAndView mav = new ModelAndView("approveProviderListView");
		String user =  request.getParameter("u");
		UserLogin ul = ldapUtils.getUserLogin(user);
		
		String[] requestedKeys = request.getParameterValues("r");
		List<String> requestedKeysList = new ArrayList<String>();
		
		for(String value: requestedKeys){
			requestedKeysList.add(value);
		}

		//retrieve the providers this user currently has access to
		List<String> existingKeys = uddiUtils.getAssociatedBusinessKeys(user);		
		
		//construct the list of providers to approve
		List<KeyValueDTO> pvs = uddiUtils.getProviderListAsKeyValues();
		List<KeyValueDTO> providersToApprove = new ArrayList<KeyValueDTO>();
		List<KeyValueDTO> existingLogins = new ArrayList<KeyValueDTO>(); 
		for(KeyValueDTO pv: pvs){
			if(existingKeys.contains(pv.getKey()))
				existingLogins.add(pv);
			else if(requestedKeysList.contains(pv.getKey()))
				providersToApprove.add(pv);			
		}
		
		mav.addObject("providerRegistrationLogins", existingLogins);
		mav.addObject("providersToApprove", providersToApprove);
		mav.addObject("user", ul);
		return mav;	
	}
	
	/**
	 * This will send an email for approval to portal@gbif.org to ask if someone can see a providers details
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ModelAndView sendRegistrationLoginsRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {	
	
		String user = request.getParameter("user");
		if(StringUtils.isEmpty(user)){
			user = request.getRemoteUser();	
		} 
		UserLogin ul = ldapUtils.getUserLogin(user);
				
		String[] businessKeysToRemove = request.getParameterValues("businessKeyToRemove");
		List<String> existingKeys = uddiUtils.getAssociatedBusinessKeys(user);
		
		//remove the selected registration logins
		if(businessKeysToRemove!=null){
			for(int i=0;i<businessKeysToRemove.length; i++){
				if(existingKeys.contains(businessKeysToRemove[i]))
					uddiUtils.deleteRegistrationLogin(user, businessKeysToRemove[i]);
			}
		}		
		
		//send approval email
		String[] businessKeys = request.getParameterValues(REQUEST_BUSINESS_UDDI_KEY);
		
		//if not businesskeys selected, non requested return to menu
		if(businessKeys==null){
			return new ModelAndView(new RedirectView(request.getContextPath()+"/register/"));
		}
		
		//send verification email
		SimpleMailMessage verificationMessage = new SimpleMailMessage(userTemplateMessage);
		verificationMessage.setTo(adminEmail);
		verificationMessage.setSubject("User has requested access to Provider Details");
		StringBuffer sb = new StringBuffer("Please click here to review request:\n\n"); 
		sb.append("http://");
		sb.append(request.getHeader("host"));
		sb.append(request.getContextPath());
		sb.append("/register/reviewUserRequest");
		sb.append("?u="+user);
		for(String businessKey: businessKeys){
			sb.append("&r=");
			sb.append(businessKey);			
		}
		verificationMessage.setText(sb.toString());
		try {
            mailSender.send(verificationMessage);
        } catch(MailException e) {
            // simply log it and go on...
            logger.error("Couldn't send message", e);
            ModelAndView mav = new ModelAndView("emailDetailsFailureView");
            return mav;
        }		
		
        //request sent view
        ModelAndView mav = new ModelAndView("requestSentView");	
        mav.addObject("userLogin", ul);
		return mav;		
	}
	
	/**
	 * Update registration logins, creating those for the business keys passed in the request.
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ModelAndView updateRegistrationLogins(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		String user =  request.getParameter("user");
		boolean sendEmail = ServletRequestUtils.getBooleanParameter(request, "sendEmail", false);
		
		//if user is not supplied in a parameter, update is for the current user
		if(StringUtils.isEmpty(user)){
			user = request.getRemoteUser();	
		} 
		
		String[] businessKeys = request.getParameterValues(REQUEST_BUSINESS_UDDI_KEY);
		String[] businessKeysToRemove = request.getParameterValues("businessKeyToRemove");
		List<String> existingKeys = uddiUtils.getAssociatedBusinessKeys(user);
		List<String> createdRegistrations = new ArrayList<String>();
		
		
		//add the selected registration logins		
		if(businessKeys!=null){
			for(int i=0;i<businessKeys.length; i++){
				if(!existingKeys.contains(businessKeys[i])){
					uddiUtils.createRegistrationLogin(user, businessKeys[i]);
					createdRegistrations.add(businessKeys[i]);
				}
			}
		}

		//if required sent a notification email
		if(sendEmail && !createdRegistrations.isEmpty()){
			UserLogin userLogin = ldapUtils.getUserLogin(user);
			//send verification email
			SimpleMailMessage verificationMessage = new SimpleMailMessage(userTemplateMessage);
			verificationMessage.setTo(userLogin.getEmail());
			verificationMessage.setSubject("User has been granted access to Provider Details");
			StringBuffer sb = new StringBuffer("Your request to access the details of the following providers has been granted:\n\n");
			for(String createdRegistration: createdRegistrations){
				ProviderDetail pd = uddiUtils.createProviderFromUDDI(createdRegistration, userLogin.getUsername());
				sb.append(pd.getBusinessName());
				sb.append("\n");
			}
			verificationMessage.setText(sb.toString());
			try {
	            mailSender.send(verificationMessage);
	        } catch(MailException e) {
	            // simply log it and go on...
	            logger.error("Couldn't send message", e);
	        }			
		}
		
		//remove the selected registration logins
		if(businessKeysToRemove!=null){
			for(int i=0;i<businessKeysToRemove.length; i++){
				if(existingKeys.contains(businessKeysToRemove[i]))
					uddiUtils.deleteRegistrationLogin(user, businessKeysToRemove[i]);
			}
		}
		return new ModelAndView(new RedirectView(request.getContextPath()+"/register/"));
	}
	
	/**
	 * Setup page for a new data provider. When registering a new contact, some details are prepopulated from LDAP.
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ModelAndView registerDataProvider(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		// get the authenticated user from LDAP
		UserLogin userLogin = ldapUtils.getUserLogin(request.getRemoteUser());
		
		// get the provider from UDDI if necessary 
		ProviderDetail detail= new ProviderDetail();	

		// these are prepopulated from LDAP
		detail.getBusinessPrimaryContact().setName(userLogin.getUsername());
		detail.getBusinessPrimaryContact().setPhone(userLogin.getTelephone());
		detail.getBusinessPrimaryContact().setEmail(userLogin.getEmail());
		
		Map<String, Object> data = new HashMap<String, Object>();
		data.putAll(referenceDataForProvider(request));
		data.put(RegistrationController.REQUEST_PROVIDER_DETAIL, detail);
		
		ModelAndView mav = new ModelAndView("registrationUpdateProviderDetail", data);
		return mav;	
	}	
	
	/**
	 * Populates the form for displaying the data provider if there is a key in the request.  If there isn't then an 
	 * empty form is used, with a prepolution of the primary contact from LDAP
	 * @TODO Exception handling for services down, invalid keys...   
	 */
	@SuppressWarnings("unchecked")
	public ModelAndView updateDataProvider(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// get the authenticated user from LDAP
		UserLogin userLogin = ldapUtils.getUserLogin(request.getRemoteUser());
		
		// get the provider from UDDI if necessary 
		ProviderDetail detail = null;		
		String key = request.getParameter(REQUEST_BUSINESS_UDDI_KEY);
		if (StringUtils.isNotEmpty(key)) {
			detail = uddiUtils.createProviderFromUDDI(key, userLogin.getUsername());
		}
		if(detail==null){
			return new ModelAndView("registrationBadBusinessKey");
		}
		
		//LDAP details override the UDDI version - is this right?
//		detail.getBusinessPrimaryContact().setName(namePhoneEmail[0]);
//		detail.getBusinessPrimaryContact().setPhone(namePhoneEmail[1]);
//		detail.getBusinessPrimaryContact().setEmail(namePhoneEmail[2]);
		
		Map<String, Object> data = new HashMap<String, Object>();
		data.putAll(referenceDataForProvider(request));
		data.put(RegistrationController.REQUEST_PROVIDER_DETAIL, detail);
		return new ModelAndView("registrationUpdateProviderDetail", data);	
	}
	
	/**
	 * Populates the form for displaying the data provider if there is a key in the request.  If there isn't then an 
	 * empty form is used, with a prepolution of the primary contact from LDAP
	 * 
	 * @TODO Exception handling for services down, invalid keys...   
	 */
	@SuppressWarnings("unchecked")
	public ModelAndView viewDataProvider(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		// get the provider from UDDI if necessary
		ProviderDetail provider = retrieveProviderFromUDDI(request); 
		if(provider==null){
			return new ModelAndView("registrationBadBusinessKey");
		}		
		
		Map<String, Object> data = new HashMap<String, Object>();
		data.putAll(referenceDataForProvider(request));
		data.put(RegistrationController.REQUEST_PROVIDER_DETAIL, provider);
		return new ModelAndView("registrationProviderDetail", data);	
	}	
	
	
	/**
	 * Populates the form for displaying the data provider if there is a key in the request.  If there isn't then an 
	 * empty form is used, with a prepolution of the primary contact from LDAP
	 * 
	 * @TODO Exception handling for services down, invalid keys...   
	 */
	@SuppressWarnings("unchecked")
	public ModelAndView viewDataResource(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		// get the provider from UDDI if necessary
		ProviderDetail provider = retrieveProviderFromUDDI(request); 
		if(provider==null){
			return new ModelAndView("registrationBadBusinessKey");
		}		
		
		//retrieve resource details
		ResourceDetail resource = retrieveDataResource(request, provider);

		if(resource==null){
			return new ModelAndView("registrationBadBusinessKey");
		}
		
		Map<String, Object> data = new HashMap<String, Object>();
		data.putAll(referenceDataForResource(request, resource));
		data.put(RegistrationController.REQUEST_PROVIDER_DETAIL, provider);
		data.put(RegistrationController.REQUEST_RESOURCE, resource);
		
		List<String> beanProperties = retrieveReadonlyPropertiesForResource(resource);
		data.put("readonlyProperties", beanProperties);
		return new ModelAndView("registrationResourceDetail", data);	
	}

	/**
	 * Retrieves a list of properties that are readonly (i.e. available from metadata) for this resource.
	 * 
	 * @param resource
	 * @return
	 */
	private List<String> retrieveReadonlyPropertiesForResource(ResourceDetail resource) {
		if(resource.getResourceType()!=null){
			String namespace = resourceType2namespaceList.get(resource.getResourceType());
			List<String> namespaces = new ArrayList<String>();
			namespaces.add(namespace);
			return resourceExtractionUtils.getMappedBeanPropertiesForNamespace(namespaces);
		}
		return new ArrayList<String>();
	}

	/**
	 * Retrieve the data resource for the provided serviceKey
	 * @param request
	 * @param provider
	 * @return
	 */
	private ResourceDetail retrieveDataResource(HttpServletRequest request, ProviderDetail provider) {
		String serviceKey = request.getParameter("serviceKey");
		List<ResourceDetail> resources = provider.getBusinessResources();
		ResourceDetail resource = null;
		for(ResourceDetail res: resources){
			if(res.getServiceKey().equals(serviceKey)){
				resource = res;
				break;
			}
		}
		return resource;
	}	
	
	/**
	 * Saves the values for the data provider
	 */
	@SuppressWarnings("unchecked")
	public ModelAndView synchroniseProvider(HttpServletRequest request, HttpServletResponse response) throws Exception {
		 ProviderDetail provider = new ProviderDetail();
		 ServletRequestDataBinder binder = createBinder(request, provider);
		 initBinder(request, binder);
		 binder.bind(request);
		 BindingResult result = binder.getBindingResult();		 
		 validateDataProvider(provider, result);
		 
		 if (result.hasErrors()) {
			logger.info("Errors have occurred: " + result);
			Map<String, Object> data = new HashMap<String, Object>();
			// put the errors in the request
			data.put(BindingResult.MODEL_KEY_PREFIX + RegistrationController.REQUEST_PROVIDER_DETAIL, result);
			data.put(RegistrationController.REQUEST_CONTACT_TYPES, ProviderDetail.ContactTypes.values());
			data.put(RegistrationController.REQUEST_PROVIDER_DETAIL, result.getTarget());
			data.putAll(referenceDataForProvider(request));
			return new ModelAndView("registrationUpdateProviderDetail", data);
		} else {
			boolean success = synchroniseProvider(request, provider, result);
			if(!success){
				return new ModelAndView("registrationUDDIFailure");
			}
			//return showDataResources(request, response, provider);
			return new ModelAndView(new RedirectView(request.getContextPath()+"/register/viewDataProvider?"+REQUEST_BUSINESS_UDDI_KEY+"="+provider.getBusinessKey()));
		}		
	}

	/**
	 * This is the entry point when the user types in the URL directly
	 * It must have a key 
	 */
	public ModelAndView showDataResources(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// get the authenticated user from LDAP
		UserLogin userLogin = ldapUtils.getUserLogin(request.getRemoteUser());
		ProviderDetail detail = null;		
		String key = request.getParameter(REQUEST_BUSINESS_UDDI_KEY);
		if (StringUtils.isNotEmpty(key)) {
			detail = uddiUtils.createProviderFromUDDI(key, userLogin.getUsername());
		}
		if (detail != null) {
			return retrieveRegisteredDataResources(request, response, detail);
		} else {
			logger.warn("Direct use of showDataResources with no key or invalid key: " + key);
			return new ModelAndView("registrationBadBusinessKey");
		}		
	}

	/**
	 * Updates details in UDDI for this dataset.
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ModelAndView completeManualRegistration(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		//bind to object
		ResourceDetail resource = new ResourceDetail();
		ServletRequestDataBinder binder = createBinder(request, resource);
		initBinder(request, binder);
		binder.bind(request);
		BindingResult result = binder.getBindingResult();	
		validateDataResource(resource, result);				
		logger.info("Adding the resource: " + resource.getName());
		
		 if (result.hasErrors()) {
			logger.info("Errors have occurred: " + result);
			Map<String, Object> data = new HashMap<String, Object>();
			data.put(REQUEST_RESOURCE, resource);
			data.putAll(referenceDataForResourceList(request));		
			data.put(BindingResult.MODEL_KEY_PREFIX + RegistrationController.REQUEST_RESOURCE, result);
			data.put("editableEndpoint", true);
			// put the errors in the request
			return new ModelAndView("registrationResourceManual", data);
		} else {
			String businessKey = request.getParameter("businessKey");
			uddiUtils.updateResource(resource, businessKey);
			return new ModelAndView(new RedirectView(request.getContextPath()+"/register/showDataResources?businessKey="+businessKey));
		}
	}
	
	/**
	 * This is the entry point for the AJAX call to update the resource
	 */
	@SuppressWarnings("unchecked")
	public ModelAndView updateDataResource(HttpServletRequest request, HttpServletResponse response) throws Exception {
		logger.info("Updating a resource...");
		debugRequestParams(request);
		ResourceDetail resource = new ResourceDetail();
		ServletRequestDataBinder binder = createBinder(request, resource);
		binder.bind(request);
		String businessKey = request.getParameter(REQUEST_BUSINESS_UDDI_KEY);
		BindingResult result = binder.getBindingResult();
		validateDataResource(resource, result);
		
		if (result.getErrorCount() == 0) {
			// it is always the last one, due to the fact that only one is ever submitted
			//although it's id may be something other than 0
			if(logger.isDebugEnabled()){
				logger.debug("Resource name: " + resource.getName());
				logger.debug("Resource description: " + resource.getDescription());
			}
			uddiUtils.updateResource(resource, request.getParameter(REQUEST_BUSINESS_UDDI_KEY));
			return new ModelAndView(new RedirectView(request.getContextPath()+"/register/showDataResources?businessKey="+businessKey));			
		} else {
			logger.error(result.getAllErrors());
			// put the errors in the request
			Map<String, Object> data = new HashMap<String, Object>();
			data.putAll(referenceDataForResource(request, resource));
			data.put(RegistrationController.REQUEST_RESOURCE, resource);
			data.put(BindingResult.MODEL_KEY_PREFIX + RegistrationController.REQUEST_RESOURCE, result);			
			List<String> beanProperties = retrieveReadonlyPropertiesForResource(resource);
			data.putAll(referenceDataForResourceList(request));
			data.put("readonlyProperties", beanProperties);
			return new ModelAndView("registrationResourceDetail", data);				
		}		
	}
	
	/**
	 * Debug request parameters.
	 * 
	 * @param request
	 */
	@SuppressWarnings("unchecked")
	private void debugRequestParams(HttpServletRequest request) {
		Map<String, Object> params = request.getParameterMap();
		for (String key : params.keySet()) {
			Object param = params.get(key);
			if (param instanceof String[]) {
				for (int i=0; i<((String[])param).length; i++) {
					logger.info(key + "[" + i + "]: " + ((String[])param)[i]);
				}
			} else {
				logger.info(key + ": " + 1);
			}
		}
	}
	
	/**
	 * Ajax entry point that adds the resource to provider
	 */
	@SuppressWarnings("unchecked")
	public ModelAndView addDataResource(HttpServletRequest request, HttpServletResponse response) throws Exception {
		logger.info("Adding a resource...");
		debugRequestParams(request);		

		ResourceDetail resourceDetail = new ResourceDetail();
		ServletRequestDataBinder binder = createBinder(request, resourceDetail);
		binder.bind(request);
		BindingResult result = binder.getBindingResult();		 
		validateDataResource(resourceDetail, result);				
		
		logger.info("Adding the resource: " + resourceDetail.getName());
		uddiUtils.updateResource(resourceDetail, request.getParameter("businessKey"));
		Map<String, Object> data = new HashMap<String, Object>();
		data.put(REQUEST_RESOURCE, resourceDetail);
		data.putAll(referenceDataForResourceList(request));		
		return null;
	}
	
	/**
	 * Validate this details of this data resource
	 * @param resourceDetail
	 * @param result
	 */
	private void validateDataResource(ResourceDetail resourceDetail, BindingResult result) {
		if (StringUtils.isEmpty(resourceDetail.getName())) {
			result.rejectValue("name", ErrorMessageKeys.MUST_BE_SPECIFIED);
		}
		if (StringUtils.isEmpty(resourceDetail.getCode())) {
			result.rejectValue("code", ErrorMessageKeys.MUST_BE_SPECIFIED);
		}		
		if (StringUtils.isEmpty(resourceDetail.getAccessPoint())) {
			result.rejectValue("accessPoint", ErrorMessageKeys.MUST_BE_SPECIFIED);
		}
		
		if (resourceDetail.getNorthCoordinate()!=null) {
			if(resourceDetail.getNorthCoordinate()>90 || resourceDetail.getNorthCoordinate()<-90)
				result.rejectValue("northCoordinate", ErrorMessageKeys.INVALID_LATITUDE);
		}
		if (resourceDetail.getSouthCoordinate()!=null) {
			if(resourceDetail.getSouthCoordinate()>90 || resourceDetail.getSouthCoordinate()<-90)
				result.rejectValue("southCoordinate", ErrorMessageKeys.INVALID_LATITUDE);
		}
		if (resourceDetail.getEastCoordinate()!=null) {
			if(resourceDetail.getEastCoordinate()>180 || resourceDetail.getEastCoordinate()<-180)
				result.rejectValue("eastCoordinate", ErrorMessageKeys.INVALID_LONGITUDE);
		}
		if (resourceDetail.getWestCoordinate()!=null) {
			if(resourceDetail.getWestCoordinate()>180 || resourceDetail.getWestCoordinate()<-180)
				result.rejectValue("westCoordinate", ErrorMessageKeys.INVALID_LONGITUDE);
		}
		
		if (resourceDetail.getWestCoordinate()!=null 
				&& resourceDetail.getEastCoordinate()!=null 
				&& resourceDetail.getWestCoordinate()>resourceDetail.getEastCoordinate()) {
				result.rejectValue("westCoordinate", ErrorMessageKeys.WEST_GREATER_THAN_EAST);
		}
		if (resourceDetail.getNorthCoordinate()!=null 
				&& resourceDetail.getSouthCoordinate()!=null 
				&& resourceDetail.getSouthCoordinate()>resourceDetail.getNorthCoordinate()) {
				result.rejectValue("southCoordinate", ErrorMessageKeys.SOUTH_GREATER_THAN_NORTH);
		}
	}	
	
	/**
	 * Populates the page model for the resources within the provider given   
	 */
	public ModelAndView retrieveRegisteredDataResources(HttpServletRequest request, HttpServletResponse response, ProviderDetail provider) throws Exception {
		logger.debug("Entering the resources page");
		Map<String, Object> data = new HashMap<String, Object>();
		if(logger.isDebugEnabled()){
			logger.debug("Provider: " + provider);			
			logger.debug("Provider has resource count: " + provider.getBusinessResources().size());
		}
		
		data.put(REQUEST_PROVIDER_DETAIL, provider);
		if (StringUtils.isNotEmpty(request.getParameter(REQUEST_RESOURCE_URL))) {
			//retrieve resource list from provider
			List<ResourceDetail> resources = contactProviderForMetadata(request.getParameter(REQUEST_RESOURCE_URL));
			replaceKnownResourcesWithUDDICopy(provider, resources);
			data.put(REQUEST_RESOURCES, resources);
		}
		data.putAll(referenceDataForResourceList(request, provider));		
		return new ModelAndView("registrationResourceList", data);
	}

	/**
	 * Populates the page model for the resources within the provider given   
	 */
	public ModelAndView registerDataResources(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		logger.debug("Entering the register data resources page");
		Map<String, Object> data = new HashMap<String, Object>();
		ProviderDetail provider = retrieveProviderFromUDDI(request); 
		if(provider==null){
			return new ModelAndView("registrationBadBusinessKey");
		}		
		
		List<ResourceDetail> resources = null;
		
		logger.debug("Provider has resource count: " + provider.getBusinessResources().size());
		data.put(RegistrationController.REQUEST_PROVIDER_DETAIL, provider);
		if (StringUtils.isNotEmpty(request.getParameter(REQUEST_RESOURCE_URL))) {
			//retrieve resource list from provider
			resources = contactProviderForMetadata(request.getParameter(REQUEST_RESOURCE_URL));
			replaceKnownResourcesWithUDDICopy(provider, resources);
			data.put(REQUEST_RESOURCES, resources);
		}
		
		ModelAndView mav = new ModelAndView("registrationResourceLookup", data);

		//add readonly settings
		if(resources!=null && resources.size()>0){
			ResourceDetail rd = resources.get(0);
			String namespace = resourceType2namespaceList.get(rd.getResourceType());
			List<String> namespaces = new ArrayList<String>();
			namespaces.add(namespace);
			mav.addObject("readonlyProperties", resourceExtractionUtils.getMappedBeanPropertiesForNamespace(namespaces));
		}
		
		data.putAll(referenceDataForResourceList(request));	
		mav.addObject(REQUEST_RESOURCE_TYPES, resourceTypes);
		mav.addAllObjects(data);
		return mav;
	}

	/**
	 * Display a form for fully manual resource registration.   
	 */
	public ModelAndView manuallyRegisterResource(HttpServletRequest request, HttpServletResponse response) throws Exception {
		logger.debug("Entering the register data resources page");
		Map<String, Object> data = new HashMap<String, Object>();
		ProviderDetail provider = retrieveProviderFromUDDI(request); 
		if(provider==null){
			return new ModelAndView("registrationBadBusinessKey");
		}		
		logger.debug("Provider has resource count: " + provider.getBusinessResources().size());
		ResourceDetail resourceDetail = new ResourceDetail();
		
		//set the resource type if passed as param
		String resourceType = request.getParameter("ResourceType");
		resourceDetail.setResourceType(resourceType);

		data.put(REQUEST_RESOURCE, resourceDetail);
		data.put("editableEndpoint", true);
		data.put(RegistrationController.REQUEST_PROVIDER_DETAIL, provider);
		data.putAll(referenceDataForResourceList(request));		
		return new ModelAndView("registrationResourceManual", data);
	}		
		
	/**
	 * Retrieve the provider for this request from UDDI. Returns null in case of bad key.
	 * @param request
	 * @return
	 * @throws NamingException
	 * @throws UDDIException
	 * @throws TransportException
	 */
	private ProviderDetail retrieveProviderFromUDDI(HttpServletRequest request) {
		ProviderDetail provider = null;		
		String key = request.getParameter(REQUEST_BUSINESS_UDDI_KEY);
		// get the authenticated user from LDAP
		try {
			//need to cache these LDAP details for speed
			UserLogin userLogin = ldapUtils.getUserLogin(request.getRemoteUser());
			if (StringUtils.isNotEmpty(key)) {
				provider = uddiUtils.createProviderFromUDDI(key, userLogin.getUsername());
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return provider;
	}	
	
	/**
	 * AJAX entry point to dynamically call the provider with a metadata request   
	 */
	public ModelAndView issueMetadataRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
		ProviderDetail provider = retrieveProviderFromUDDI(request);
		Map<String, Object> data = new HashMap<String, Object>();
		if (StringUtils.isNotEmpty(request.getParameter(REQUEST_RESOURCE_URL))) {
			List<ResourceDetail> resources = contactProviderForMetadata(request.getParameter(REQUEST_RESOURCE_URL));
			replaceKnownResourcesWithUDDICopy(provider, resources);
			data.put(REQUEST_RESOURCES, resources);
		}
		data.putAll(referenceDataForResourceList(request));		
		return new ModelAndView("registrationMetadataResourceList", data);
	}

	/**
	 * Sets the guid on any resource that appears to be already in the provider
	 * this uses the name only... should be enough... hmmm
	 */
	protected void replaceKnownResourcesWithUDDICopy(ProviderDetail provider, List<ResourceDetail> resources) {
		
		//put all known resources into a map
		Map<String, ResourceDetail> resourcesByNames = new HashMap<String, ResourceDetail>();
		for (ResourceDetail resource : resources) {
			resourcesByNames.put(resource.getName(), resource);
		}
		
		//replace any retrieved from request with the UDDI copy
		for (ResourceDetail resource : provider.getBusinessResources()) {
			if (resourcesByNames.containsKey(resource.getName())) {
				
				ResourceDetail rd = resourcesByNames.get(resource.getName());
				int position = resources.indexOf(rd);
				if(position>=0){
					resources.remove(rd);
					//replace with UUDI version
					resources.add(position, resource);
				}
			}
		}
	}
	
	/**
	 * Because the page can dynamically add more Secondary contacts, ensure that the contacts list in the command object is of sufficient size to handle it.
	 * @see org.springframework.web.servlet.mvc.BaseCommandController#initBinder(javax.servlet.http.HttpServletRequest, org.springframework.web.bind.ServletRequestDataBinder)
	 */
	protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws Exception {
		if (binder.getTarget() instanceof ProviderDetail) {
			initProviderBinder(request, binder);
		} else if (binder.getTarget() instanceof ResourceDetail) {
			initResourceDetailBinder(request, binder);
		}	
	}

	/**
	 * Initialises the binder for a provider
	 * This handles the resizing of the secondary contacts and resources
	 */
	protected void initResourceDetailBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws Exception {
		ResourceDetail resourceDetail = (ResourceDetail) binder.getTarget();
		String relatesToCountries = request.getParameter("relatesToCountries");
		if(StringUtils.isNotEmpty(relatesToCountries)){
			ArrayList<String> relatesToCountriesList = new ArrayList<String>();
			StringTokenizer st = new StringTokenizer(relatesToCountries, ",");
			while(st.hasMoreTokens()){
				String iso = st.nextToken();
				if(iso!=null && iso.length()>0)
					relatesToCountriesList.add(iso);
			}
			resourceDetail.setRelatesToCountries(relatesToCountriesList);			
		}
		
		//check for N/A values
		Date startDate = DateUtil.getDateFromRequest(request, "startDate");
		Date endDate = DateUtil.getDateFromRequest(request, "endDate");
		resourceDetail.setStartDate(startDate);
		resourceDetail.setEndDate(endDate);
		
		//set N/A values to null
		if("N/A".equals(resourceDetail.getRecordBasis()))
			resourceDetail.setRecordBasis(null);
		if("N/A".equals(resourceDetail.getOwnerCountry()))
			resourceDetail.setOwnerCountry(null);
		if("N/A".equals(resourceDetail.getIndexingStartTime()))
			resourceDetail.setIndexingStartTime(null);
		if("N/A".equals(resourceDetail.getIndexingFrequency()))
			resourceDetail.setIndexingFrequency(null);
		if("N/A".equals(resourceDetail.getIndexingMaxDuration()))
			resourceDetail.setIndexingMaxDuration(null);
	}
	
	/**
	 * Initialises the binder for a provider
	 * This handles the resizing of the secondary contacts and resources
	 */
	protected void initProviderBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws Exception {
		ProviderDetail providerDetail = (ProviderDetail) binder.getTarget();
		logger.info("Checking to see if the contacts list needs padded");
		int maxSecondaryIndex = getMaxIndex(request, "businessSecondaryContacts");
		logger.info("maxSecondaryIndex: " + maxSecondaryIndex);
		int currentSize = providerDetail.getBusinessSecondaryContacts().size();
		logger.info("currentSize: " + currentSize);
		if (maxSecondaryIndex+1 > currentSize) {
			for (int i=0; i<(maxSecondaryIndex+1-currentSize); i++) {
				logger.info("creating a padded contact");
				providerDetail.getBusinessSecondaryContacts().add(providerDetail.new Contact());
			}
		} else if (maxSecondaryIndex+1 < currentSize) {  
			// shrink it if it's a submit - e.g contacts deleted
			if (isFormSubmission(request)) {
				logger.info("shrinking");
				providerDetail.setBusinessSecondaryContacts(
						providerDetail.getBusinessSecondaryContacts().subList(0, maxSecondaryIndex+1));
			}
		}
		
		logger.info("Checking to see if the resource list needs padded");
		int maxResourceIndex = getMaxIndex(request, "businessResources");
		logger.info("maxResourceIndex: " + maxResourceIndex);
		currentSize = providerDetail.getBusinessResources().size();
		logger.info("currentSize: " + currentSize);
		if (maxResourceIndex+1 > currentSize) {
			for (int i=0; i<(maxResourceIndex+1-currentSize); i++) {
				logger.info("creating a padded resource");
				providerDetail.getBusinessResources().add(new ResourceDetail());
			}
		} else if (maxResourceIndex+1 < currentSize) {  
			// shrink it if it's a submit - e.g contacts deleted
			if (isFormSubmission(request)) {
				logger.info("shrinking");
				providerDetail.setBusinessResources(
						providerDetail.getBusinessResources().subList(0, maxResourceIndex+1));
			}
		}
	}
	
	/**
	 * Determines if the request is a submission or not
	 * @param request TO test
	 * @return true if it is a POST, otherwise false
	 */
	protected boolean isFormSubmission(HttpServletRequest request) {
		return "POST".equals(request.getMethod());
	}	
	
    /**
     * Searches through the parameters in the request to find the highest index of the given
     * property.
     * 
     * @see http://forum.springframework.org/showthread.php?t=19976
     * @param request The request
     * @param property The given property to search for
     * @return The maximum index for the given property, or -1 if the property is not indexed
     */
    protected final int getMaxIndex(HttpServletRequest request, String property) {
        int maxIndex = -1;        
        for (Enumeration parameters = request.getParameterNames(); parameters.hasMoreElements();) {
            String parameterName = (String)parameters.nextElement();
            if (parameterName.startsWith(property)) {
                int startIndex = parameterName.indexOf("[");
                int endIndex = parameterName.indexOf("]");
                if ((startIndex == -1) || (endIndex == -1)) {
                    logger.error("Non-indexed property - " + property);
                    return (-1);
                }
                int index = Integer.parseInt(parameterName.substring((startIndex + 1), endIndex));
                if (index > maxIndex) {
                    maxIndex = index;
                }
            }
        }        
        return (maxIndex);
    }

	/**
	 * Synchronises the provider with the LDAP help entry for it if it exists (UUID being set)
	 * or creates a new one if it does not exist.
	 * @param request For any extra parameters
	 * @param provider To synchronise
	 * @param errors To add to when the uddi cannot be sync'ed with
	 */
	@SuppressWarnings("unchecked")
	protected boolean synchroniseProvider(HttpServletRequest request, ProviderDetail provider, Errors errors) {
		try {
			logger.debug("Synchronizing provider");
			boolean creatingNewProvider = StringUtils.isEmpty(provider.getBusinessKey());
			uddiUtils.synchroniseProvider(provider);
			if(creatingNewProvider){
				uddiUtils.createRegistrationLogin(request.getRemoteUser(), provider.getBusinessKey());
			}
			return true;
		} catch (Exception e) {
			logger.error("Unexpected error communicating with UDDI: " + e.getMessage(), e);
			errors.reject(ErrorMessageKeys.UDDI_COMMUNICATION_ERROR);			
		}
		return false;
	}
	
	/**
	 * Issues a metadata request to the provider
	 */
	@SuppressWarnings("unchecked")
	protected List<ResourceDetail> contactProviderForMetadata(String url) {
		logger.debug("Contacting provider for metadata");
		
		if(resourceType2namespaceList!=null){
			for(String resourceType: resourceType2namespaceList.keySet()){
				
				//retrieve the namespace
				String namespace = resourceType2namespaceList.get(resourceType);
				try {
					//this is geared to DiGIR only
					Processor workflow = (Processor) propertyStore.getProperty(namespace, "REGISTRATION.METADATA.WORKFLOW", Processor.class);
					Map<String, Object> seedData = new HashMap<String, Object>();
					seedData.put("url", url);
					List<String> namespaces = new LinkedList<String>();
					namespaces.add(namespace);
					seedData.put("psNamespaces", namespaces);
					ProcessContext context = workflow.doActivities(seedData);
					
					Message message = (Message) context.get("responseMessage", Message.class, true);
					List<ResourceDetail> resources = resourceExtractionUtils.getResourcesFromMetadata(namespaces, message);
					if(resources!=null && !resources.isEmpty()){
						for(ResourceDetail resourceDetail : resources)
							resourceDetail.setResourceType(resourceType);
						return resources;
					}
				} catch (Exception e){
					logger.error(e.getMessage(), e);					
				}
			}
		}
		return new LinkedList<ResourceDetail>();
	}

	/**
	 * Checks that Business details are all in there
	 * @param command The form object
	 * @param errors The errors to add to
	 */
	protected void validateDataProvider(Object command, Errors errors) {
		ProviderDetail provider = (ProviderDetail)command;
		if (StringUtils.isEmpty(provider.getBusinessName())) {
			errors.rejectValue("businessName", ErrorMessageKeys.MUST_BE_SPECIFIED);
		}
		
//		if (StringUtils.isEmpty(provider.getBusinessCountry())) {
//			errors.rejectValue("businessCountry", ErrorMessageKeys.MUST_BE_SPECIFIED);
//		}
		if (StringUtils.isEmpty(provider.getBusinessDescription())) {
			errors.rejectValue("businessDescription", ErrorMessageKeys.MUST_BE_SPECIFIED);
		}
		int secondaryContactCount=0;
		for (ProviderDetail.Contact contact : provider.getBusinessSecondaryContacts()) {
			logger.info("Validating secondary contact");
			if (StringUtils.isEmpty(contact.getName())) {
				errors.rejectValue("businessSecondaryContacts[" + secondaryContactCount + "].name", ErrorMessageKeys.MUST_BE_SPECIFIED);
			}
			if (StringUtils.isEmpty(contact.getEmail())) {
				errors.rejectValue("businessSecondaryContacts[" + secondaryContactCount + "].email", ErrorMessageKeys.MUST_BE_SPECIFIED);
			}
			if (StringUtils.isEmpty(contact.getPhone())) {
				errors.rejectValue("businessSecondaryContacts[" + secondaryContactCount + "].phone", ErrorMessageKeys.MUST_BE_SPECIFIED);
			}
			secondaryContactCount++;
		}
		logger.info("Metadata validation error count: " + errors.getErrorCount());
	}

	
	/**
	 * Populates the ReferenceData for the provider page 
	 */
	protected Map<String, Object> referenceDataForProvider(HttpServletRequest request) throws Exception {
		Map<String, Object> data = new HashMap<String, Object>();
		data.put(RegistrationController.REQUEST_CONTACT_TYPES, ProviderDetail.ContactTypes.values());
		data.put(RegistrationController.REQUEST_COUNTRIES, geospatialManager.findAllCountries(request.getLocale()).getResults());
		return data;
	}

	/**
	 * Populates the ReferenceData for the resource list page 
	 */
	protected Map<String, Object> referenceDataForResourceList(HttpServletRequest request) throws Exception {
		return referenceDataForResourceList(request, null);
	}
	
	/**
	 * Populates the ReferenceData for the resource list page 
	 */
	protected Map<String, Object> referenceDataForResourceList(HttpServletRequest request, ProviderDetail providerDetail) throws Exception {
		Map<String, Object> data = new HashMap<String, Object>();
		Locale locale = RequestContextUtils.getLocale(request);
		DataProviderDTO nubProvider = dataResourceManager.getNubDataProvider();
		DataResourceDTO dataResourceDTO = null;
		List<DataResourceDTO> resourceDTOs = dataResourceManager.getDataResourcesForProvider(nubProvider.getKey());
		if(resourceDTOs!=null && !resourceDTOs.isEmpty())
			dataResourceDTO = resourceDTOs.get(0); 
		
		data.put(RegistrationController.REQUEST_BASES_OF_RECORD, basisOfRecordTypes);
		data.put(RegistrationController.REQUEST_RESOURCE_NETWORKS, dataResourceManager.getResourceNetworkList());
		data.put(RegistrationController.REQUEST_COUNTRIES, geospatialManager.findAllCountries(locale).getResults());
		data.put(RegistrationController.REQUEST_DATA_PROVIDER, nubProvider);
		data.put(RegistrationController.REQUEST_DATA_RESOURCE, dataResourceDTO);
		data.put(RegistrationController.REQUEST_RESOURCE_TYPES, resourceTypes);
		data.put(RegistrationController.REQUEST_CONCEPTS, taxonomyManager.getRootTaxonConceptsForTaxonomy(nubProvider.getKey(), dataResourceDTO.getKey()));
		data.put("messageSource", messageSource);
		return data;
	}

	/**
	 * Populates the ReferenceData for the resource list page 
	 */
	protected Map<String, Object> referenceDataForResource(HttpServletRequest request, ResourceDetail resourceDetail) throws Exception {
		Map<String, Object> data = new HashMap<String, Object>();
		Locale locale = RequestContextUtils.getLocale(request);
		DataProviderDTO nubProvider = dataResourceManager.getNubDataProvider();
		DataResourceDTO dataResourceDTO = null;
		List<DataResourceDTO> resourceDTOs = dataResourceManager.getDataResourcesForProvider(nubProvider.getKey());
		if(resourceDTOs!=null && !resourceDTOs.isEmpty())
			dataResourceDTO = resourceDTOs.get(0); 
		
		data.put(RegistrationController.REQUEST_BASES_OF_RECORD, basisOfRecordTypes);
		data.put(RegistrationController.REQUEST_RESOURCE_NETWORKS, dataResourceManager.getResourceNetworkList());
		data.put(RegistrationController.REQUEST_COUNTRIES, geospatialManager.findAllCountries(locale).getResults());
		data.put(RegistrationController.REQUEST_DATA_PROVIDER, nubProvider);
		data.put(RegistrationController.REQUEST_DATA_RESOURCE, dataResourceDTO);
		data.put(RegistrationController.REQUEST_RESOURCE_TYPES, resourceTypes);
		data.put("messageSource", messageSource);

		if(StringUtils.isNotEmpty(resourceDetail.getHighestTaxaConceptId())){
			data.put(RegistrationController.REQUEST_CONCEPTS, taxonomyManager.getClassificationFor(resourceDetail.getHighestTaxaConceptId(), true, null, true));
			data.put(RegistrationController.REQUEST_SELECTED_CONCEPT, taxonomyManager.getTaxonConceptFor(resourceDetail.getHighestTaxaConceptId()));
		} else {
			data.put(RegistrationController.REQUEST_CONCEPTS, taxonomyManager.getRootTaxonConceptsForTaxonomy(nubProvider.getKey(), dataResourceDTO.getKey()));			
		}
		return data;
	}	
	
	/**
	 * @param ldapUrl The ldapUrl to set.
	 */
	public void setLdapUrl(String ldapUrl) {
		this.ldapUrl = ldapUrl;
	}

	/**
	 * @param uddiUtils The uddiUtils to set.
	 */
	public void setUddiUtils(UDDIUtils uddiUtils) {
		this.uddiUtils = uddiUtils;
	}

	/**
	 * @param propertyStore the propertyStore to set
	 */
	public void setPropertyStore(PropertyStore propertyStore) {
		this.propertyStore = propertyStore;
	}

	/**
	 * @param ldapUtils The ldapUtils to set.
	 */
	public void setLdapUtils(LDAPUtils ldapUtils) {
		this.ldapUtils = ldapUtils;
	}

	/**
	 * @param geospatialManager The geospatialManager to set.
	 */
	public void setGeospatialManager(GeospatialManager geospatialManager) {
		this.geospatialManager = geospatialManager;
	}

	/**
	 * @return Returns the resourceExtractionUtils.
	 */
	public ResourceExtractionUtils getResourceExtractionUtils() {
		return resourceExtractionUtils;
	}

	/**
	 * @param resourceExtractionUtils The resourceExtractionUtils to set.
	 */
	public void setResourceExtractionUtils(
			ResourceExtractionUtils resourceExtractionUtils) {
		this.resourceExtractionUtils = resourceExtractionUtils;
	}

	/**
	 * @param dataResourceManager The dataResourceManager to set.
	 */
	public void setDataResourceManager(DataResourceManager dataResourceManager) {
		this.dataResourceManager = dataResourceManager;
	}

	/**
	 * @param taxonomyManager The taxonomyManager to set.
	 */
	public void setTaxonomyManager(TaxonomyManager taxonomyManager) {
		this.taxonomyManager = taxonomyManager;
	}

	/**
	 * @param basisOfRecordTypes the basisOfRecordTypes to set
	 */
	public void setBasisOfRecordTypes(List<String> basisOfRecordTypes) {
		this.basisOfRecordTypes = basisOfRecordTypes;
	}

	/**
	 * @param resourceType2namespaceList the resourceType2namespaceList to set
	 */
	public void setResourceType2namespaceList(
			Map<String, String> resourceType2namespaceList) {
		this.resourceType2namespaceList = resourceType2namespaceList;
	}
	
	/**
	 * @param resourceTypes the resourceTypes to set
	 */
	public void setResourceTypes(List<String> resourceTypes) {
		this.resourceTypes = resourceTypes;
	}

	/**
	 * @param mailSender the mailSender to set
	 */
	public void setMailSender(MailSender mailSender) {
		this.mailSender = mailSender;
	}

	/**
	 * @param userTemplateMessage the userTemplateMessage to set
	 */
	public void setUserTemplateMessage(SimpleMailMessage userTemplateMessage) {
		this.userTemplateMessage = userTemplateMessage;
	}

	/**
	 * @param usernamePostfix the usernamePostfix to set
	 */
	public void setUsernamePostfix(String usernamePostfix) {
		this.usernamePostfix = usernamePostfix;
	}

	/**
	 * @param adminRole the adminRole to set
	 */
	public void setAdminRole(String adminRole) {
		this.adminRole = adminRole;
	}

	/**
	 * @param minimumUsernameLength the minimumUsernameLength to set
	 */
	public void setMinimumUsernameLength(int minimumUsernameLength) {
		this.minimumUsernameLength = minimumUsernameLength;
	}

	/**
	 * @param minimumPasswordLength the minimumPasswordLength to set
	 */
	public void setMinimumPasswordLength(int minimumPasswordLength) {
		this.minimumPasswordLength = minimumPasswordLength;
	}

	/**
	 * @param passwordUtils the passwordUtils to set
	 */
	public void setPasswordUtils(PasswordUtils passwordUtils) {
		this.passwordUtils = passwordUtils;
	}

	/**
	 * @param adminEmail the adminEmail to set
	 */
	public void setAdminEmail(String adminEmail) {
		this.adminEmail = adminEmail;
	}

	/**
	 * @param messageSource the messageSource to set
	 */
	public void setMessageSource(MessageSource messageSource) {
		this.messageSource = messageSource;
	}	
}