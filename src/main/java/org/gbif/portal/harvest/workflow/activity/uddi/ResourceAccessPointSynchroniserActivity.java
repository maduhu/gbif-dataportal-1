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

import org.gbif.portal.dao.PropertyStoreNamespaceDAO;
import org.gbif.portal.dao.ResourceAccessPointDAO;
import org.gbif.portal.model.ResourceAccessPoint;
import org.gbif.portal.util.propertystore.MisconfiguredPropertyException;
import org.gbif.portal.util.propertystore.PropertyNotFoundException;
import org.gbif.portal.util.workflow.BaseActivity;
import org.gbif.portal.util.workflow.ContextCorruptException;
import org.gbif.portal.util.workflow.ProcessContext;
import org.uddi4j.UDDIException;
import org.uddi4j.client.UDDIProxy;
import org.uddi4j.datatype.binding.BindingTemplate;
import org.uddi4j.datatype.binding.TModelInstanceInfo;
import org.uddi4j.datatype.service.BusinessService;
import org.uddi4j.datatype.tmodel.TModel;
import org.uddi4j.response.BusinessInfo;
import org.uddi4j.response.ServiceDetail;
import org.uddi4j.response.ServiceInfo;
import org.uddi4j.response.ServiceList;
import org.uddi4j.transport.TransportException;

/**
 * This will create the Resource Access Points that can be deduced from the
 * UDDI.  The generic ResourceAccessPoint can be created, but in the case of DiGIR type
 * providers, it must be updated again with the remote access point.
 * 
 * Thus this will ONLY update what is known in UDDI.  Further interrogation of the 
 * endpoint must be carried out.
 * 
 * @TODO Handle deletes...
 * 
 * @author trobertson
 */
public class ResourceAccessPointSynchroniserActivity extends BaseActivity {
	/**
	 * The enquiry url
	 * Defaults to http://registry.gbif.net/uddi/inquiry
	 */
	protected String inquiryUrl = "http://registry.gbif.net/uddi/inquiry";
	
	/**
	 * Context key for the data provider created or updated
	 */
	protected String contextKeyDataProviderId;
	
	/**
	 * Context key for the BusinessInfo
	 */
	protected String contextKeyBusinessInfo;	
	
	/**
	 * Context key for the seed dataResourceAccessPoint 
	 */
	protected String contextKeyResourceAccessPointId;
	
	/**
	 * The TModels will be checked in order and mapped to the namespaces provided
	 * The first value found is used, thus MaNISDiGIR can be checked before DiGIR for example
	 */
	protected List<Map<String, List<String>>> orderedTModelNamespaceMapping = new LinkedList<Map<String, List<String>>>();
	
	/**
	 * DAOs
	 */
	ResourceAccessPointDAO resourceAccessPointDAO;
	PropertyStoreNamespaceDAO propertyStoreNamespaceDAO;
	
	/**
	 * @see org.gbif.portal.util.workflow.Activity#execute(org.gbif.portal.util.workflow.ProcessContext)
	 */
	@SuppressWarnings("unchecked")
	public ProcessContext execute(ProcessContext context) throws Exception {
		long dataProviderId = ((Long)context.get(getContextKeyDataProviderId(), Long.class, true)).longValue();
		BusinessInfo businessInfo = (BusinessInfo)context.get(getContextKeyBusinessInfo(), BusinessInfo.class, true); 
		UDDIProxy proxy = new UDDIProxy();
		proxy.setInquiryURL(getInquiryUrl());
		
		// refresh the services - for some reason it is not complete when 
		// the TModel filter is used (possible uddi4j bug?)
		ServiceList serviceList = proxy.find_service(businessInfo.getBusinessKey(), null, null, null, null, 10000);
		if (serviceList.getServiceInfos()==null || serviceList.getServiceInfos().size() == 0) {
			logger.warn("There are no ServiceInfos for businessInfo: " + businessInfo.getBusinessKey());
		} else {
			for (int j=0; j<serviceList.getServiceInfos().size(); j++) {
				ServiceInfo serviceInfo = (ServiceInfo)serviceList.getServiceInfos().get(j);
				ServiceDetail serviceDetail = proxy.get_serviceDetail(serviceInfo.getServiceKey());
				Vector businessServiceVector = serviceDetail.getBusinessServiceVector();					
				for (int l=0; l<businessServiceVector.size(); l++) {
					BusinessService businessService = (BusinessService) businessServiceVector.elementAt(l);
					logger.debug("   " + (j+1) + ") " + businessService.getDefaultNameString());
					logger.debug("      " + businessService.getDefaultDescriptionString());
					
					// BINDING TEMPLATES
					Vector bindingTemplateVector = businessService.getBindingTemplates().getBindingTemplateVector();
					for (int m=0; m<bindingTemplateVector.size(); m++) {
						BindingTemplate bindingTemplate = (BindingTemplate) bindingTemplateVector.elementAt(m);
						logger.debug("      " + (m+1) + ") " + bindingTemplate.getDefaultDescriptionString());
						logger.debug("         " + bindingTemplate.getAccessPoint().getText());						
						createOrUpdateResourceAccessPoints(dataProviderId, bindingTemplate, proxy, context);
					}
				}
			}
		}		
		return context;
	}
	
	/**
	 * Will create or update all the resource access points associated with the bindingTemplate
	 * If it is creating, it will also create the namespace mappings by calling createNamespaceMappings
	 * @param dataProviderId To use when creating or updating
	 * @param bindingTemplate That is being synchronised
	 * @param proxy To the UDDI server
	 * @throws TransportException Network level error
	 * @throws UDDIException UDDI not working
	 * @throws ContextCorruptException If the context does not have the data
	 * @throws MisconfiguredPropertyException If the property is not mapped correctly
	 * @throws PropertyNotFoundException If the property is not found
	 */
	protected void createOrUpdateResourceAccessPoints(long dataProviderId, BindingTemplate bindingTemplate, UDDIProxy proxy, ProcessContext context) throws Exception {
		List<ResourceAccessPoint> resourceAccessPoints = resourceAccessPointDAO.getByUuid(bindingTemplate.getBindingKey());
		if (resourceAccessPoints == null || resourceAccessPoints.size()==0) {
			logger.info("Creating new ResourceAccessPoint");
			ResourceAccessPoint resourceAccessPoint = new ResourceAccessPoint();
			resourceAccessPoint.setDataProviderId(dataProviderId);
			resourceAccessPoint.setUrl(bindingTemplate.getAccessPoint().getText());
			resourceAccessPoint.setUuid(bindingTemplate.getBindingKey());
			long id = resourceAccessPointDAO.create(resourceAccessPoint);
			createNamespaceMappings(resourceAccessPoint, bindingTemplate, proxy);
			
			Map<String, Object> seed = new HashMap<String, Object>();
			seed.put(getContextKeyResourceAccessPointId(), new Long(id));
			seed.put(getContextKeyPsNamespaces(), resourceAccessPoint.getNamespaces());
			launchWorkflow(context, seed);
			
		} else {
			// only one is launched... default to the emty one (e.g. second sync for BioCASe) or
			// just take the first one (suits DiGIR's subsequent syncs)
			ResourceAccessPoint toLaunch = null;
			for (ResourceAccessPoint resourceAccessPoint : resourceAccessPoints) {
				logger.info("Updating ResourceAccessPoint with id: " + resourceAccessPoint.getId());
				resourceAccessPoint.setDataProviderId(dataProviderId);
				resourceAccessPoint.setUrl(bindingTemplate.getAccessPoint().getText());
				resourceAccessPointDAO.updateOrCreate(resourceAccessPoint);
				
				// override with the null accessor, or if it is not set
				if (resourceAccessPoint.getRemoteIdAtUrl() == null
						|| toLaunch == null) {
					toLaunch = resourceAccessPoint;
				}
			}
				
			Map<String, Object> seed = new HashMap<String, Object>();
			seed.put(getContextKeyResourceAccessPointId(), new Long(toLaunch.getId()));
			seed.put(getContextKeyPsNamespaces(), toLaunch.getNamespaces());
			launchWorkflow(context, seed);				
		}								
	}
	
	/**
	 * Creates the namespace mappings for the ResourceAccessPoint
	 * The orderedTModelNamespaceMapping is used to get the TModels of interest
	 * and the first found in that list is used
	 * @param resourceAccessPoint To associate the namespaces with
	 * @param bindingTemplate That is being synchronised
	 * @param proxy To the UDDI server
	 * @throws TransportException Network level error
	 * @throws UDDIException UDDI not working
	 */
	protected void createNamespaceMappings(ResourceAccessPoint resourceAccessPoint, BindingTemplate bindingTemplate, UDDIProxy proxy) throws UDDIException, TransportException {
		Set<String> tModels = new HashSet<String>();
		// TMODELS							
		Vector TModelInstanceInfoVector = bindingTemplate.getTModelInstanceDetails().getTModelInstanceInfoVector();
		for (int n=0; n<TModelInstanceInfoVector.size(); n++) {
			TModelInstanceInfo tModelInstanceInfo = (TModelInstanceInfo) TModelInstanceInfoVector.elementAt(n);
			Vector TModelDetailVector = proxy.get_tModelDetail(tModelInstanceInfo.getTModelKey()).getTModelVector();			
			for (int o=0; o<TModelDetailVector.size(); o++) {
				TModel tModel = (TModel) TModelDetailVector.elementAt(o);
				logger.debug("         - " + tModel.getNameString());
				tModels.add(tModel.getNameString());
			}					
		}
		
		for (Map<String, List<String>> mapping : orderedTModelNamespaceMapping) {
			boolean found = false;
			Set<String> mappingKeys = mapping.keySet();
			for (String mappingKey : mappingKeys) {
				if (tModels.contains(mappingKey)) {
					propertyStoreNamespaceDAO.createNamespaceMappings(resourceAccessPoint.getId(), mapping.get(mappingKey));
					// add them to the rap
					resourceAccessPoint.getNamespaces().addAll(mapping.get(mappingKey));
					found = true;
					break;
				}			
			}	
			if (found) {
				break;
			}
		}
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
	 * @return Returns the orderedTModelNamespaceMapping.
	 */
	public List<Map<String, List<String>>> getOrderedTModelNamespaceMapping() {
		return orderedTModelNamespaceMapping;
	}

	/**
	 * @param orderedTModelNamespaceMapping The orderedTModelNamespaceMapping to set.
	 */
	public void setOrderedTModelNamespaceMapping(
			List<Map<String, List<String>>> orderedTModelNamespaceMapping) {
		this.orderedTModelNamespaceMapping = orderedTModelNamespaceMapping;
	}

	/**
	 * @return Returns the resourceAccessPointDAO.
	 */
	public ResourceAccessPointDAO getResourceAccessPointDAO() {
		return resourceAccessPointDAO;
	}

	/**
	 * @param resourceAccessPointDAO The resourceAccessPointDAO to set.
	 */
	public void setResourceAccessPointDAO(
			ResourceAccessPointDAO resourceAccessPointDAO) {
		this.resourceAccessPointDAO = resourceAccessPointDAO;
	}

	/**
	 * @return Returns the propertyStoreNamespaceDAO.
	 */
	public PropertyStoreNamespaceDAO getPropertyStoreNamespaceDAO() {
		return propertyStoreNamespaceDAO;
	}

	/**
	 * @param propertyStoreNamespaceDAO The propertyStoreNamespaceDAO to set.
	 */
	public void setPropertyStoreNamespaceDAO(
			PropertyStoreNamespaceDAO propertyStoreNamespaceDAO) {
		this.propertyStoreNamespaceDAO = propertyStoreNamespaceDAO;
	}

	/**
	 * @return Returns the contextKeyResourceAccessPointId.
	 */
	public String getContextKeyResourceAccessPointId() {
		return contextKeyResourceAccessPointId;
	}

	/**
	 * @param contextKeyResourceAccessPointId The contextKeyResourceAccessPointId to set.
	 */
	public void setContextKeyResourceAccessPointId(
			String contextKeyResourceAccessPointId) {
		this.contextKeyResourceAccessPointId = contextKeyResourceAccessPointId;
	}

}
