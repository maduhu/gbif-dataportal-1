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
package org.gbif.portal.harvest.workflow.activity.uddi;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import org.gbif.portal.dao.CountryDAO;
import org.gbif.portal.dao.DataProviderDAO;
import org.gbif.portal.harvest.util.AgentUtils;
import org.gbif.portal.model.Agent;
import org.gbif.portal.model.Country;
import org.gbif.portal.model.DataProvider;
import org.gbif.portal.util.workflow.BaseActivity;
import org.gbif.portal.util.workflow.ProcessContext;
import org.uddi4j.UDDIException;
import org.uddi4j.client.UDDIProxy;
import org.uddi4j.datatype.Name;
import org.uddi4j.datatype.business.Address;
import org.uddi4j.datatype.business.AddressLine;
import org.uddi4j.datatype.business.BusinessEntity;
import org.uddi4j.datatype.business.Contact;
import org.uddi4j.datatype.business.Contacts;
import org.uddi4j.datatype.business.Email;
import org.uddi4j.datatype.business.Phone;
import org.uddi4j.datatype.service.BusinessServices;
import org.uddi4j.datatype.tmodel.TModel;
import org.uddi4j.response.BusinessDetail;
import org.uddi4j.response.BusinessInfo;
import org.uddi4j.response.BusinessList;
import org.uddi4j.response.TModelDetail;
import org.uddi4j.response.TModelInfo;
import org.uddi4j.response.TModelList;
import org.uddi4j.transport.TransportException;
import org.uddi4j.util.CategoryBag;
import org.uddi4j.util.FindQualifier;
import org.uddi4j.util.FindQualifiers;
import org.uddi4j.util.KeyedReference;
import org.uddi4j.util.TModelBag;

/**
 * Will synchronise all the businesses from UDDI with a data provider.
 * 
 * @TODO Handle deletes...
 * 
 * @author trobertson
 */
public class DataProviderSynchroniserActivity extends BaseActivity {
	/**
	 * The enquiry url
	 * Defaults to http://registry.gbif.net/uddi/inquiry
	 */
	protected String inquiryUrl = "http://registry.gbif.net/uddi/inquiry";
	
	/**
	 * DAO
	 */
	protected DataProviderDAO dataProviderDAO;
	
	/**
	 * DAO
	 */
	protected CountryDAO countryDAO;
	
	/**
	 * Context key for the data provider created or updated
	 */
	protected String contextKeyDataProviderId;
	
	/**
	 * Context key for the BusinessInfo
	 */
	protected String contextKeyBusinessInfo;
	
	/**
	 * Map of hardcoded settings for provider country
	 */
	protected Map providerCountryMap;
	
	/**
	 * The TModel Names of the businesses to synchronise
	 */
	protected List<String> tmodelNames = new LinkedList<String>();
	
	/**
	 * Utils
	 */
	protected AgentUtils agentUtils;
	
	/**
	 * TModel name
	 */
	protected String tModelName = "gbif:nodes";
	
	/**
	 * @see org.gbif.portal.util.workflow.Activity#execute(org.gbif.portal.util.workflow.ProcessContext)
	 */
	@SuppressWarnings("unchecked")
	public ProcessContext execute(ProcessContext context) throws Exception {
		UDDIProxy proxy = new UDDIProxy();
		proxy.setInquiryURL(getInquiryUrl());		
		Vector names = new Vector();
		names.add(new Name("%"));		
		Vector tModels = getTModelKeyStrings(proxy);
		TModelBag tmodelBag = null;
		FindQualifiers findQualifiers = null;
		if (tModels.size() > 0 ) {
			tmodelBag = new TModelBag(tModels);
			findQualifiers = new FindQualifiers();
			findQualifiers.add(new FindQualifier(FindQualifier.orAllKeys));			
		}
		
		BusinessList businessList = proxy.find_business(names, null, null, null, tmodelBag, findQualifiers, 10000);
		Vector  businessInfoVector = businessList.getBusinessInfos().getBusinessInfoVector();
		// TODO handle deletes
		for (int i=0; i<businessInfoVector.size(); i++) {
			BusinessInfo businessInfo = (BusinessInfo)businessInfoVector.elementAt(i);
			DataProvider dataProvider = dataProviderDAO.getByUuid(businessInfo.getBusinessKey());
			if (dataProvider != null) {
				logger.info("Updating DataProvider with id: " + dataProvider.getId()); 
			} else {
				logger.info("Creating new provider: " + businessInfo.getDefaultNameString());
				dataProvider = new DataProvider();
			}
	        BusinessDetail businessDetail = proxy.get_businessDetail(businessInfo.getBusinessKey());
	        Vector businessEntities = businessDetail.getBusinessEntityVector();
	        BusinessEntity businessEntity = (BusinessEntity) businessEntities.firstElement();
	        CategoryBag categoryBag = businessEntity.getCategoryBag();
	        Vector keyedReferences;
			if (categoryBag != null
				 && (keyedReferences = categoryBag.getKeyedReferenceVector()) != null) {
				boolean processed = false;
		        for (int key = 0;
		        	 !processed && key < keyedReferences.size();
		        	 key++) {
		        	KeyedReference keyedReference = (KeyedReference) keyedReferences.elementAt(key);
		        	TModelDetail tModelDetail = proxy.get_tModelDetail(keyedReference.getTModelKey());
		        	Vector tModelVector = tModelDetail.getTModelVector();
		        	TModel tModel = (TModel) tModelVector.firstElement();
		        	if (tModel.getName().getText().equals(tModelName)) {
		        		// data provider is approved - we should include it
		        		
		        		dataProvider.setGbifApprover(keyedReference.getKeyName());
		        		// interpret provider country
		        		String countryCode = keyedReference.getKeyValue().toUpperCase();
		        		Country country = countryDAO.getByIsoCountryCode(countryCode);
		        		if (countryCode!=null && country==null) {
		        			country = countryDAO.getByCountryName(countryCode);
		        			if(country!=null)
		        				countryCode = country.getIsoCountryCode();
		        		}
		        		
		        		if (country==null) {
		            		if (providerCountryMap != null){
		            			countryCode = (String) providerCountryMap.get(businessInfo.getDefaultNameString());
		            		} else {
		            			countryCode = null;
		            		}
		        		}

		    			dataProvider.setName(businessInfo.getDefaultNameString());
		    			//retrieve the description
		    			BusinessServices businessServices = businessEntity.getBusinessServices();
		    			if(businessServices.size()>0){
		    				String description = businessServices.get(0).getDefaultDescriptionString();
		    				dataProvider.setDescription(description);
		    			}
		    			dataProvider.setUuid(businessInfo.getBusinessKey());
		    			dataProvider.setIsoCountryCode(countryCode);
		    			//The description supplied by providers seems to include the website url at present
		    			String websiteUrl = businessInfo.getDefaultDescriptionString();
		    			if(websiteUrl!=null && (websiteUrl.startsWith("http://") || websiteUrl.startsWith("https://")) )
		    				dataProvider.setWebsiteUrl(websiteUrl);
		    			long id = dataProviderDAO.updateOrCreate(dataProvider);
		    			dataProvider.setId(id);
		    			synchroniseAgents(dataProvider, businessEntity);
		    			processed = true;
		    			
		    			context.put(getContextKeyBusinessInfo(), businessInfo);
		    			context.put(getContextKeyDataProviderId(), new Long(dataProvider.getId()));
		    			launchWorkflow(context, null);
		        	}
		        }
		    }
		}	
		// TODO handle deletes...
		return context;
	}
	
	/**
	 * Syncs the agents for the provider
	 * @param dataProvider
	 * @param businessEntity
	 */
	@SuppressWarnings("unchecked")
	private void synchroniseAgents(DataProvider dataProvider, BusinessEntity businessEntity) {
		Contacts contacts = businessEntity.getContacts();
		if (contacts != null) {
			Map<String, Agent> agents = new HashMap<String, Agent>(); 
			for (int c=0; c<contacts.size(); c++) {
				Contact contact = contacts.get(c);
				Agent agent = new Agent();
				agent.setName(contact.getPersonNameString());
				
				Vector pv = contact.getPhoneVector();
				if (!pv.isEmpty()) {
					agent.setTelephone(((Phone) pv.get(0)).getText());
				}
				
				Vector av = contact.getAddressVector();
				if (!av.isEmpty()) {
					Vector<AddressLine> lines = ((Address)av.get(0)).getAddressLineVector();
					StringBuffer sb = new StringBuffer();
					for (int i=0; i<lines.size(); i++) {
						sb.append(lines.get(i).getText());
						if (i<lines.size()) {
							sb.append(",");
						}
					}
					agent.setAddress(sb.toString());
				}
	
				Vector ev = contact.getEmailVector();
				if (!ev.isEmpty()) {
					agent.setEmail(((Email) ev.get(0)).getText());
				}
				
				agents.put(contact.getUseType(), agent);
			}
			Set<String> keysHandled = new HashSet<String>();
			for (String key : agents.keySet()) {
				Agent agent = agents.get(key);
				agentUtils.ensureAgentIsAttachedToProvider(
						agent.getName(), 
						agent.getAddress(), 
						agent.getEmail(), 
						agent.getTelephone(), 
						key, 
						dataProvider.getId());
				keysHandled.add(key);
			}
			for (String key : agents.keySet()) {
				if (!keysHandled.contains(key)) {
					logger.warn("TODO - Need to remove association for agent to provider here...");
				}
			}
		}
	}

	/**
	 * Using the configured TModelNames, gets a Vector of the IDs 
	 * @param proxy To use
	 * @return The Vector of TModel keys, that match the names configured
	 * @throws TransportException No network 
	 * @throws UDDIException UDDI problem
	 */
	protected Vector<String> getTModelKeyStrings(UDDIProxy proxy) throws UDDIException, TransportException  {
		Set<String> keys = new HashSet<String>();
		for (String tmodel : getTmodelNames()) {
			TModelList list = proxy.find_tModel(tmodel, null, null, null, 10000);
			if (list.getTModelInfos() != null) {
				for (int i=0; i<list.getTModelInfos().size(); i++) {
					TModelInfo info = list.getTModelInfos().get(0);
					keys.add(info.getTModelKey());
					logger.info("TModel in use: " + info.getNameString() + " [" + info.getTModelKey() + "]");
				}
			}
		}
		return new Vector<String>(keys);
	}

	/**
	 * @return Returns the inquiryUrl.
	 */
	public String getInquiryUrl() {
		return inquiryUrl;
	}

	/**
	 * @param inquiryUrl The inquiryUrl to set.
	 */
	public void setInquiryUrl(String inquiryUrl) {
		this.inquiryUrl = inquiryUrl;
	}

	/**
	 * @return Returns the dataProviderDAO.
	 */
	public DataProviderDAO getDataProviderDAO() {
		return dataProviderDAO;
	}

	/**
	 * @param dataProviderDAO The dataProviderDAO to set.
	 */
	public void setDataProviderDAO(DataProviderDAO dataProviderDAO) {
		this.dataProviderDAO = dataProviderDAO;
	}

	/**
	 * @return the countryDAO
	 */
	public CountryDAO getCountryDAO() {
		return countryDAO;
	}

	/**
	 * @param countryDAO the countryDAO to set
	 */
	public void setCountryDAO(CountryDAO countryDAO) {
		this.countryDAO = countryDAO;
	}

	/**
	 * @return Returns the contextKeyBusinessInfo.
	 */
	public String getContextKeyBusinessInfo() {
		return contextKeyBusinessInfo;
	}

	/**
	 * @param contextKeyBusinessInfo The contextKeyBusinessInfo to set.
	 */
	public void setContextKeyBusinessInfo(String contextKeyBusinessInfo) {
		this.contextKeyBusinessInfo = contextKeyBusinessInfo;
	}

	/**
	 * @return Returns the contextKeyDataProviderId.
	 */
	public String getContextKeyDataProviderId() {
		return contextKeyDataProviderId;
	}

	/**
	 * @param contextKeyDataProviderId The contextKeyDataProviderId to set.
	 */
	public void setContextKeyDataProviderId(String contextKeyDataProviderId) {
		this.contextKeyDataProviderId = contextKeyDataProviderId;
	}

	/**
	 * @return Returns the tmodelNames.
	 */
	public List<String> getTmodelNames() {
		return tmodelNames;
	}

	/**
	 * @param tmodelNames The tmodelNames to set.
	 */
	public void setTmodelNames(List<String> tmodelNames) {
		this.tmodelNames = tmodelNames;
	}

	/**
	 * @return the providerCountryMap
	 */
	public Map getProviderCountryMap() {
		return providerCountryMap;
	}

	/**
	 * @param providerCountryMap the providerCountryMap to set
	 */
	public void setProviderCountryMap(Map providerCountryMap) {
		this.providerCountryMap = providerCountryMap;
	}

	/**
	 * @return Returns the agentUtils.
	 */
	public AgentUtils getAgentUtils() {
		return agentUtils;
	}

	/**
	 * @param agentUtils The agentUtils to set.
	 */
	public void setAgentUtils(AgentUtils agentUtils) {
		this.agentUtils = agentUtils;
	}

	/**
	 * @param modelName the tModelName to set
	 */
	public void setTModelName(String modelName) {
		tModelName = modelName;
	}

}
