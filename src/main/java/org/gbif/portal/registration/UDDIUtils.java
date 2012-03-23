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

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.gbif.portal.dao.registry.RegistrationLoginDAO;
import org.gbif.portal.dto.KeyValueDTO;
import org.gbif.portal.model.resources.RegistrationLogin;
import org.gbif.portal.registration.model.ProviderDetail;
import org.gbif.portal.registration.model.ResourceDetail;
import org.uddi4j.UDDIException;
import org.uddi4j.client.UDDIProxy;
import org.uddi4j.datatype.Name;
import org.uddi4j.datatype.binding.AccessPoint;
import org.uddi4j.datatype.binding.BindingTemplate;
import org.uddi4j.datatype.binding.BindingTemplates;
import org.uddi4j.datatype.binding.TModelInstanceDetails;
import org.uddi4j.datatype.binding.TModelInstanceInfo;
import org.uddi4j.datatype.business.BusinessEntity;
import org.uddi4j.datatype.business.Contact;
import org.uddi4j.datatype.business.Contacts;
import org.uddi4j.datatype.business.Email;
import org.uddi4j.datatype.business.Phone;
import org.uddi4j.datatype.service.BusinessService;
import org.uddi4j.datatype.tmodel.TModel;
import org.uddi4j.response.AuthToken;
import org.uddi4j.response.BindingDetail;
import org.uddi4j.response.BusinessDetail;
import org.uddi4j.response.BusinessInfo;
import org.uddi4j.response.BusinessInfos;
import org.uddi4j.response.BusinessList;
import org.uddi4j.response.ServiceDetail;
import org.uddi4j.response.ServiceInfo;
import org.uddi4j.response.ServiceInfos;
import org.uddi4j.response.ServiceList;
import org.uddi4j.response.TModelDetail;
import org.uddi4j.response.TModelInfo;
import org.uddi4j.response.TModelInfos;
import org.uddi4j.response.TModelList;
import org.uddi4j.transport.TransportException;
import org.uddi4j.util.CategoryBag;
import org.uddi4j.util.KeyedReference;

/**
 * A set of helper methods used for synchronising the DataProvider
 * model object with the UDDI database using the Uddi4j client API.
 * 
 * The uddi proxy is passed in in a way that allows external configuration 
 * 
 * @author trobertson
 */
public class UDDIUtils {
	/**
	 * Logger
	 */
	protected static final Log logger = LogFactory.getLog(UDDIUtils.class);
	
	/**
	 * The proxy to uddi
	 */
	protected UDDIProxy uddiProxy;
	
	/**
	 * The username to get an Auth token
	 */
	protected String uddiAuthUser;
	
	/**
	 * The password to get an Auth token
	 */
	protected String uddiAuthPassword;
	
	public static final String CATEGORY_BAG_KEY_COUNTRY = "country";
	public static final String CATEGORY_BAG_KEY_HIGHER_TAXA = "higherTaxa";
	public static final String CATEGORY_BAG_KEY_HIGHER_TAXA_CONCEPT_ID = "higherTaxaConceptId";	
	public static final String CATEGORY_BAG_KEY_LOGO_URL = "logoUrl";

	public static final String CATEGORY_BAG_KEY_OWNER_COUNTRY = "ownerCountry";
	public static final String CATEGORY_BAG_KEY_OWNER_COUNTRY_NAME = "ownerCountryName";	
	public static final String CATEGORY_BAG_KEY_OWNER_NAME = "ownerName";
	public static final String CATEGORY_BAG_KEY_OWNER_ADDRESS = "ownerAddress";
	
	public static final String CATEGORY_BAG_KEY_RECORD_BASIS = "recordBasis";	
	public static final String CATEGORY_BAG_KEY_RESOURCE_NETWORKS = "resourceNetworks";	
	public static final String CATEGORY_BAG_KEY_USAGE_IPR = "usageIPR";	
	
	public static final String CATEGORY_BAG_KEY_RELATES_TO_COUNTRIES = "relatesToCountries";
	
	
	
	/** Registration login DAO for retrieving business keys */
	public RegistrationLoginDAO registrationLoginDAO;

	/**
	 * Retrieves the UDDI business keys 
	 * @param userId
	 * @return
	 */
	public List<String> getAssociatedBusinessKeys(String loginId){
		return registrationLoginDAO.getBusinessKeysFor(loginId);
	}
	
	/**
	 * Creates a login for this businesskey 
	 * @param userId
	 * @return
	 */
	public void createRegistrationLogin(String loginId, String businessKey){
		registrationLoginDAO.createRegistrationLogin(new RegistrationLogin(loginId, businessKey));
	}	
	
	/**
	 * Retrieves the UDDI business keys 
	 * @param userId
	 * @return
	 */
	public void deleteRegistrationLogin(String loginId, String businessKey){
		registrationLoginDAO.deleteRegistrationLogin(new RegistrationLogin(loginId, businessKey));
	}	
	
	/**
	 * Synchronises the provider with the UDDI business
	 * Should a UUID be found for the business, then it is
	 * updated, otherwise new details are created.
	 * 
	 * This will delete contacts from uddi should they be removed from 
	 * the business.
	 * 
	 * @param provider To synchronise
	 * @throws TransportException On uddi communication error - e.g. server down
	 * @throws UDDIException  On uddi communication error - e.g. bad data
	 */
	@SuppressWarnings("unchecked")
	public void synchroniseProvider(ProviderDetail provider) throws UDDIException, TransportException {
		String uuid = provider.getBusinessKey();
		BusinessEntity businessEntity = null;
		boolean creatingNewProvider = StringUtils.isEmpty(uuid);
		if (creatingNewProvider) {
			logger.info("Creating new provider...");
			businessEntity = new BusinessEntity();
		} else {
			logger.info("Synchronising business: " + uuid);
			BusinessDetail detail = getUddiProxy().get_businessDetail(uuid);
			Vector businessEntityVector = detail.getBusinessEntityVector();
			businessEntity = (BusinessEntity) businessEntityVector.get(0);
		}
		
		businessEntity.setDefaultNameString(provider.getBusinessName(), "en");
		businessEntity.setDefaultDescriptionString(provider.getBusinessDescription());
		CategoryBag categoryBag = new CategoryBag();
		categoryBag.add(new KeyedReference(CATEGORY_BAG_KEY_COUNTRY, provider.getBusinessCountry()));
		categoryBag.add(new KeyedReference(CATEGORY_BAG_KEY_LOGO_URL, provider.getBusinessLogoURL()));
		businessEntity.setCategoryBag(categoryBag);
		synchroniseContacts(provider, businessEntity, provider.getBusinessPrimaryContact(), provider.getBusinessSecondaryContacts());
		
		logger.info("About to save...");
		Vector businesses = new Vector();
		businesses.add(businessEntity);
		BusinessDetail businessDetails = uddiProxy.save_business(uddiProxy.get_authToken(getUddiAuthUser(), getUddiAuthPassword()).getAuthInfoString(), businesses);

		if(creatingNewProvider){
			Vector businessEntities = businessDetails.getBusinessEntityVector();
			BusinessEntity be = (BusinessEntity) businessEntities.get(0);
			provider.setBusinessKey(be.getBusinessKey());
		}
		logger.info("Save completed for " + provider.getBusinessName());
	}	
	
	/**
	 * Updates the resource given, which must have the UUID set or it'll be a create
	 * TODO Fill in the rest of the details
	 * @param resource To update
	 * @param businessKey To update
	 * @throws TransportException On uddi communication error - e.g. server down
	 * @throws UDDIException  On uddi communication error - e.g. bad data
	 */
	@SuppressWarnings("unchecked")
	public void updateResource(ResourceDetail resource, String businessKey) throws UDDIException, TransportException {
		BusinessService bs = new BusinessService();
		bs.setBusinessKey(businessKey);
		bs.setServiceKey(resource.getServiceKey());
		bs.setDefaultDescriptionString(resource.getDescription());
		bs.setDefaultNameString(resource.getName(), "en");
		bs.setDefaultName(new Name(resource.getName()));
		
		AuthToken authToken = uddiProxy.get_authToken(getUddiAuthUser(), getUddiAuthPassword());		
		
		//create business service vector
		Vector businessServiceVector = new Vector();
		businessServiceVector.add(bs);
		
		//add other properties into a category bag
		CategoryBag cb = new CategoryBag();
		try {
			Map<String, Object> properties = BeanUtils.describe(resource);
			
			for(String key : properties.keySet()){
				Object property = properties.get(key);
				PropertyDescriptor pd = org.springframework.beans.BeanUtils.getPropertyDescriptor(ResourceDetail.class, key);
				Class propertyType = pd.getPropertyType();
				if(property!=null){
					if(property instanceof List){
						List propertyList = (List) property;	
						for(Object listItem: propertyList){
							if(listItem!=null)
								addToCategoryBagIfNotNull(cb, key, listItem.toString());
						}						
					} else if(propertyType.equals(Date.class)){
						SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyy");
						Method readMethod = pd.getReadMethod();
						Date theDate = (Date) readMethod.invoke(resource, (Object[]) null);
						if(theDate!=null){
							addToCategoryBagIfNotNull(cb,key, sdf.format(theDate));
						}
					} else {
						addToCategoryBagIfNotNull(cb,key, property.toString());
					}
				}
			}
			
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}

		bs.setCategoryBag(cb);
		
		logger.info("About to save...");
		ServiceDetail sd = getUddiProxy().save_service(authToken.getAuthInfoString(), businessServiceVector);
		
		// set in the resource serviceKey if required
		Vector<BusinessService> resultVector = sd.getBusinessServiceVector();
		AccessPoint ap = null;
		BindingTemplate bt = null;
		BindingTemplates bts = null;
		for (BusinessService rbs : resultVector) {
			logger.info("Setting the service key (should be once): " + rbs.getServiceKey());
			resource.setServiceKey(rbs.getServiceKey());
		
			bts =  rbs.getBindingTemplates();
			if(bts!=null && bts.size()>0){
				bt = rbs.getBindingTemplates().get(0);
				ap = bt.getAccessPoint();
			}
			break;
		}
		
		//if there are no binding templates, then add them
		if(bts==null || bts.size()==0){
			
			// Saving TModel
			TModelDetail tModelDetail = null;
			TModelList tModelList = getUddiProxy().find_tModel(resource.getResourceType(), null, null, null, 1);
			if(tModelList.getTModelInfos().size()==0){
				//save this new tmodel
				Vector tModels = new Vector();
				TModel newTModel = new TModel("", resource.getResourceType());
				tModels.add(newTModel);
				tModelDetail = getUddiProxy().save_tModel(authToken.getAuthInfoString(), tModels);
			} else {
				TModelInfo tModelInfo = tModelList.getTModelInfos().get(0);
				tModelDetail = getUddiProxy().get_tModelDetail(tModelInfo.getTModelKey());
			}
			
			// Creating TModelInstanceDetails
			Vector tModelVector = tModelDetail.getTModelVector();
			String tModelKey = ((TModel)(tModelVector.elementAt(0))).getTModelKey();
			Vector tModelInstanceInfoVector = new Vector();
			TModelInstanceInfo tModelInstanceInfo = new TModelInstanceInfo(tModelKey);
			tModelInstanceInfoVector.add(tModelInstanceInfo);
			
			TModelInstanceDetails tModelInstanceDetails = new TModelInstanceDetails();
			tModelInstanceDetails.setTModelInstanceInfoVector(tModelInstanceInfoVector);
			Vector bindingTemplatesVector = new Vector();
	
			// Create a new binding templates using required elements constructor
			// BindingKey must be "" to save a new binding
			BindingTemplate bindingTemplate = new BindingTemplate("", tModelInstanceDetails, new AccessPoint(resource.getAccessPoint(),"http"));
			bindingTemplate.setServiceKey(resource.getServiceKey());
			bindingTemplatesVector.addElement(bindingTemplate);
	
			// **** Save the Binding Template
			BindingDetail bindingDetail = getUddiProxy().save_binding(authToken.getAuthInfoString(), bindingTemplatesVector);			
		}
		
		logger.info("Save completed for " + resource.getName());
	}
	
	/**
	 * Adds the value to the CB under the given key if it is not null
	 * @param cb To add to 
	 * @param cbKey The key to use in the CB
	 * @param value The value
	 */
	protected void addToCategoryBagIfNotNull(CategoryBag cb, String cbKey, String value) {
		if (StringUtils.isNotBlank(value)) {
			cb.add(new KeyedReference(cbKey, value));
		}
	}
	
	/**
	 * Synchronises the contacts in the businessEntity with those supplied.
	 * Quite simply, this deletes any contacts already in the entity and recreates them!
	 *  
	 * @param provider to get the details from
	 * @param businessEntity To sync the contacts with
	 * @param businessPrimaryContact To add
	 * @param businessSecondaryContacts To add
	 */
	public void synchroniseContacts(ProviderDetail provider, BusinessEntity businessEntity, ProviderDetail.Contact businessPrimaryContact, List<ProviderDetail.Contact> businessSecondaryContacts) {
		Contacts contacts = new Contacts();
		contacts.add(createNewUddiContact(provider.getBusinessPrimaryContact()));
		for (ProviderDetail.Contact secondary : provider.getBusinessSecondaryContacts()) {
			contacts.add(createNewUddiContact(secondary));
		}
		businessEntity.setContacts(contacts);
	}

	/**
	 * Creates a Provider detail from the business defined by the given key
	 * 
	 * A username is provided so that the primary user is not added to the 
	 * secondary contacts list.
	 * 
	 * @param uuid To get the business for
	 * @param primaryContactName To NOT add to the secondary contacts 	  
	 * @return The provider details for the given business or null
	 * @throws TransportException On comms error
	 * @throws UDDIException On corrupt data, or invalid key
	 */
	@SuppressWarnings("unchecked")
	public ProviderDetail createProviderFromUDDI(String uuid, String primaryContactName) throws UDDIException, TransportException {
		BusinessDetail detail = getUddiProxy().get_businessDetail(uuid);
		Vector businessEntityVector = detail.getBusinessEntityVector();
		BusinessEntity business = (BusinessEntity) businessEntityVector.get(0);
		ProviderDetail provider = new ProviderDetail();
		provider.setBusinessKey(business.getBusinessKey());
		provider.setBusinessName(business.getDefaultNameString());
		provider.setBusinessDescription(business.getDefaultDescriptionString());
		CategoryBag metadata = business.getCategoryBag();
		Vector keyedMetadata = metadata.getKeyedReferenceVector();
		for (int i=0; i<keyedMetadata.size(); i++) {
			KeyedReference kr = (KeyedReference)keyedMetadata.get(i);
			if (CATEGORY_BAG_KEY_COUNTRY.equals(kr.getKeyName())) {
				provider.setBusinessCountry(kr.getKeyValue());
			} else if (CATEGORY_BAG_KEY_LOGO_URL.equals(kr.getKeyName())) {
				provider.setBusinessLogoURL(kr.getKeyValue());
			}
		}
		//initialise the primary contact
		Contacts contacts = business.getContacts();
		if(contacts.size()>0){
			Contact uddiContact = contacts.get(0);
			ProviderDetail.Contact primaryContact = provider.getBusinessPrimaryContact();
			setContactDetailsFromUDDI(uddiContact, primaryContact);			
		}
		
		addSecondaryContactsToModel(primaryContactName, business, provider);		
		
		// add the resource data
		Vector names = new Vector();
		names.add(new Name("%"));		
		ServiceList serviceList = getUddiProxy().find_service(uuid, names, null, null, null, 10000);
		if (serviceList != null) {
			ServiceInfos serviceInfos = serviceList.getServiceInfos();
			if (serviceInfos.size()>0) {
				Vector<ServiceInfo> serviceInfosVector = serviceInfos.getServiceInfoVector();
				for (ServiceInfo serviceInfo : serviceInfosVector) {
					
					ServiceDetail serviceDetail= getUddiProxy().get_serviceDetail(serviceInfo.getServiceKey());
					Vector<BusinessService> businessServiceVector = serviceDetail.getBusinessServiceVector();
					// there should only be one but...
					for (BusinessService bs : businessServiceVector) {
						logger.info("Adding a resource");
						ResourceDetail resource = new ResourceDetail();
						resource.setName(bs.getDefaultNameString());
						resource.setServiceKey(bs.getServiceKey());
						resource.setDescription(bs.getDefaultDescriptionString());
						
						BindingTemplates bindingTemplates = bs.getBindingTemplates();
						if(bindingTemplates!=null && bindingTemplates.size()>0){
							BindingTemplate bt = bindingTemplates.get(0);
							AccessPoint ap = bt.getAccessPoint();
							if(ap!=null){
								resource.setAccessPoint(ap.getText());
								resource.setResourceType(bt.getDefaultDescriptionString());
							}
						}
						
						CategoryBag cb = bs.getCategoryBag();
						if (cb != null) {
							Vector<KeyedReference> keyedReferences = cb.getKeyedReferenceVector();
							for (KeyedReference kr : keyedReferences) {
								try {
									PropertyDescriptor pd = org.springframework.beans.BeanUtils.getPropertyDescriptor(ResourceDetail.class, kr.getKeyName());
									if(pd!=null){
										Object theProperty = (Object) pd.getReadMethod().invoke(resource, (Object[])null);
										logger.debug(pd.getClass());
										if(theProperty instanceof List){
											List propertyList = (List) theProperty;
											if(propertyList!=null){
												propertyList.add(kr.getKeyValue());
											}
										} else if (pd.getPropertyType().equals(Date.class)){
											SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyy");
											try {
												Date theDate = sdf.parse(kr.getKeyValue());
												BeanUtils.setProperty(resource, kr.getKeyName(), theDate);
											} catch (Exception e){
												logger.debug(e.getMessage(), e);
											}
										} else {
											BeanUtils.setProperty(resource, kr.getKeyName(), kr.getKeyValue());
										}
									}
								} catch(Exception e){
									logger.error(e.getMessage(), e);
								}
							}
						}
						provider.getBusinessResources().add(resource);
					}					
				}
			}			
		}
		if(logger.isDebugEnabled()){
			logger.debug("Provider detail retrieved: "+provider);
		}
		return provider;
	}

	private void setContactDetailsFromUDDI(Contact contact, ProviderDetail.Contact primaryContact) {
		primaryContact.setName(contact.getPersonNameString());
		Vector emails = contact.getEmailVector();
		if (emails.size() > 0) { // shouldn't happen but check...
			primaryContact.setEmail(((Email)emails.get(0)).getText());	
		}
		Vector phones = contact.getPhoneVector();
		if (phones.size() > 0 ) { // shouldn't happen but check...
			primaryContact.setPhone(((Phone)phones.get(0)).getText());	 
		}
		primaryContact.setUseType(contact.getUseType());
	}

	/**
	 * Adds the contacts from the UDDI to the model object ready for display
	 * 
	 * The supplied username is ignored once, but subsequent occurrences will be added as
	 * secondary contacts (e.g. If a single user is given as the "technical" and "administrative"
	 * contact)
	 * 
	 * @param username If a username is supplied, it will be omitted from the secondary 
	 * @param business To extract from
	 * @param provider To add to
	 */
	protected void addSecondaryContactsToModel(String username, BusinessEntity business, ProviderDetail provider) {
		boolean primaryFound = false;
		for (int i=0; i<business.getContacts().size(); i++) {
			Contact contact = business.getContacts().get(i);
			
			if (StringUtils.equals(username, contact.getPersonNameString())) {
				primaryFound = true;
				continue;
			}			
			
			if (primaryFound || !StringUtils.equals(username, contact.getPersonNameString())) {
				ProviderDetail.Contact targetContact = provider.new Contact();
				setContactDetailsFromUDDI(contact, targetContact);				
				provider.getBusinessSecondaryContacts().add(targetContact);
			}
		}
	}
	
	public static void main(String[] args){
		UDDIUtils uu = new UDDIUtils();
		if(args.length==6){
			uu.replicate(args[0], args[1], args[2], args[3], args[4], args[5]);
		} else {
			System.out.println("Usage: org.gbif.portal.registration.UDDIUtil [sourceUDDIPublishUrl] [targetUDDIInquireUrl]" +
					" [targetUDDIPublishUrl] [username] [password]");
		}
	}
	
	
	public void replicate(String sourceUDDIInquireUrl, 
			String sourceUDDIPublishUrl,
			String targetUDDIInquireUrl,
			String targetUDDIPublishUrl,
			String username,
			String password){
		
		try {
			// Select the desired UDDI server node
			UDDIProxy sourceProxy = new UDDIProxy();
//			sourceProxy.setInquiryURL("http://registry.gbif.net/uddi/inquiry");
//			sourceProxy.setPublishURL("http://registry.gbif.net/uddi/publish");		
//
//			UDDIProxy targetProxy = new UDDIProxy();
//			targetProxy.setInquiryURL("http://localhost:8080/juddi/inquiry");
//			targetProxy.setPublishURL("http://localhost:8080/juddi/publish");		
//			
			sourceProxy.setInquiryURL(sourceUDDIInquireUrl);
			sourceProxy.setPublishURL(sourceUDDIPublishUrl);		

			UDDIProxy targetProxy = new UDDIProxy();
			targetProxy.setInquiryURL(targetUDDIInquireUrl);
			targetProxy.setPublishURL(targetUDDIPublishUrl);	
			
			//AuthToken authToken = targetProxy.get_authToken("admin", "password");
			AuthToken authToken = targetProxy.get_authToken(username, password);
			//creating vector of Name Object
			Vector names = new Vector();
			names.add(new Name("%"));
		
			//save all tModels
			TModelList tModelList = sourceProxy.find_tModel("%", null, null, null, 10000);
			TModelInfos tModelInfos = tModelList.getTModelInfos();
			for(int i=0; i<tModelInfos.size(); i++){
				TModelInfo tmi = tModelInfos.get(i);
				TModelDetail tmd = sourceProxy.get_tModelDetail(tmi.getTModelKey());
				Vector tmdv = tmd.getTModelVector();
				for(int j=0; j<tmdv.size(); j++){
					TModel tModel = (TModel) tmdv.get(j);
					tModel.setTModelKey("");
				}				
				targetProxy.save_tModel(authToken.getAuthInfoString(), tmdv);
			}
			
			//save all businesses
			BusinessList businessList = sourceProxy.find_business(names, null, null, null,null,null,10000);
			BusinessInfos bis = businessList.getBusinessInfos();
			
			for(int i=0; i<bis.size(); i++){
				BusinessInfo bi = bis.get(i);
				BusinessDetail bd = sourceProxy.get_businessDetail(bi.getBusinessKey());
				Vector bes = bd.getBusinessEntityVector();
				for(int j=0; j<bes.size(); j++){
					BusinessEntity be = (BusinessEntity) bes.get(j);
					be.setBusinessKey("");
				}
				//save these businesses to the target uddi registry
				targetProxy.save_business(authToken.getAuthInfoString(), bd.getBusinessEntityVector());
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Creates a NEW contact from the provided contact
	 * @param contact That needs translated to a uddi contact
	 * @return The new contact
	 */
	@SuppressWarnings("unchecked")
	public Contact createNewUddiContact(ProviderDetail.Contact source) {
		Contact target = new Contact();
		target.setPersonName(source.getName());
		Vector emails = new Vector();
		emails.add(new Email(source.getEmail()));
		target.setEmailVector(emails);
		Vector phones = new Vector();
		phones.add(new Phone(source.getPhone()));
		target.setPhoneVector(phones);
		target.setUseType(source.getUseType());
		return target;
	}
	
	/**
	 * Returns all the businesses
	 * This may need a filter added at some point depending on what the 
	 * registry is used for
	 * @return All the businesses as a list with the Key and Name
	 * @throws TransportException On comms error
	 * @throws UDDIException On corrupt data
	 */
	@SuppressWarnings("unchecked")
	public List<KeyValueDTO> getProviderListAsKeyValues() throws UDDIException, TransportException {
		Vector names = new Vector();
		names.add(new Name("%"));
		BusinessList businessList = uddiProxy.find_business(names, null, null, null, null, null, 10000);
		List<KeyValueDTO> results = new LinkedList<KeyValueDTO>();
		Vector<BusinessInfo> infos = businessList.getBusinessInfos().getBusinessInfoVector();
		for (BusinessInfo info : infos) {
			results.add(new KeyValueDTO(info.getBusinessKey(), info.getDefaultNameString()));
		}
		return results;		 
	}

	/**
	 * @return Returns the uddiProxy.
	 */
	public UDDIProxy getUddiProxy() {
		return uddiProxy;
	}

	/**
	 * @param uddiProxy The uddiProxy to set.
	 */
	public void setUddiProxy(UDDIProxy uddiProxy) {
		this.uddiProxy = uddiProxy;
	}

	/**
	 * @return Returns the uddiAuthPassword.
	 */
	public String getUddiAuthPassword() {
		return uddiAuthPassword;
	}

	/**
	 * @param uddiAuthPassword The uddiAuthPassword to set.
	 */
	public void setUddiAuthPassword(String uddiAuthPassword) {
		this.uddiAuthPassword = uddiAuthPassword;
	}

	/**
	 * @return Returns the uddiAuthUser.
	 */
	public String getUddiAuthUser() {
		return uddiAuthUser;
	}

	/**
	 * @param uddiAuthUser The uddiAuthUser to set.
	 */
	public void setUddiAuthUser(String uddiAuthUser) {
		this.uddiAuthUser = uddiAuthUser;
	}
	
	/**
	 * @param registrationLoginDAO the registrationLoginDAO to set
	 */
	public void setRegistrationLoginDAO(RegistrationLoginDAO registrationLoginDAO) {
		this.registrationLoginDAO = registrationLoginDAO;
	}
}