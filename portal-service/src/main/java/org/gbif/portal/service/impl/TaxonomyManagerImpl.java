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
package org.gbif.portal.service.impl;

import java.io.IOException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import org.gbif.portal.dao.geospatial.CountryDAO;
import org.gbif.portal.dao.occurrence.ImageRecordDAO;
import org.gbif.portal.dao.occurrence.TypificationRecordDAO;
import org.gbif.portal.dao.resources.DataResourceDAO;
import org.gbif.portal.dao.taxonomy.CommonNameDAO;
import org.gbif.portal.dao.taxonomy.RelationshipAssertionDAO;
import org.gbif.portal.dao.taxonomy.RemoteConceptDAO;
import org.gbif.portal.dao.taxonomy.TaxonConceptDAO;
import org.gbif.portal.dao.taxonomy.TaxonNameDAO;
import org.gbif.portal.dto.CountDTO;
import org.gbif.portal.dto.DTOFactory;
import org.gbif.portal.dto.DTOUtils;
import org.gbif.portal.dto.SearchResultsDTO;
import org.gbif.portal.dto.occurrence.ImageRecordDTO;
import org.gbif.portal.dto.occurrence.TypificationRecordDTO;
import org.gbif.portal.dto.taxonomy.BriefTaxonConceptDTO;
import org.gbif.portal.dto.taxonomy.BriefTaxonConceptDTOFactory;
import org.gbif.portal.dto.taxonomy.CommonNameDTO;
import org.gbif.portal.dto.taxonomy.RelationshipAssertionDTO;
import org.gbif.portal.dto.taxonomy.RemoteConceptDTO;
import org.gbif.portal.dto.taxonomy.TaxonConceptDTO;
import org.gbif.portal.dto.taxonomy.TaxonConceptDTOFactory;
import org.gbif.portal.dto.util.SearchConstraints;
import org.gbif.portal.dto.util.TaxonRankType;
import org.gbif.portal.model.occurrence.ImageRecord;
import org.gbif.portal.model.occurrence.TypificationRecord;
import org.gbif.portal.model.resources.DataResource;
import org.gbif.portal.model.taxonomy.CommonName;
import org.gbif.portal.model.taxonomy.RelationshipAssertion;
import org.gbif.portal.model.taxonomy.RemoteConcept;
import org.gbif.portal.model.taxonomy.TaxonConcept;
import org.gbif.portal.model.taxonomy.TaxonConceptLite;
import org.gbif.portal.model.taxonomy.TaxonRank;
import org.gbif.portal.service.ServiceException;
import org.gbif.portal.service.TaxonomyManager;

/**
 * An implementation of the TaxonomyManager interface that makes use of the
 * DAO layer objects for data access.
 * 
 * @see TaxonConceptDAO
 * @see TaxonConceptDTO
 * @see BriefTaxonConceptDTO
 * @see CommonNameDTO
 * @see RelationshipAssertionDTO
 * 
 * @author dmartin
 */
@SuppressWarnings("unchecked")
public class TaxonomyManagerImpl implements TaxonomyManager {
	
	protected static Log logger = LogFactory.getLog(TaxonomyManagerImpl.class);	
	
	/** The DAO interface for accessing Taxon Concepts */
	protected TaxonConceptDAO taxonConceptDAO;
	/** The DAO interface for accessing Taxon Names */
	protected TaxonNameDAO taxonNameDAO;	
	/** The DAO interface for accessing Common Names */
	protected CommonNameDAO commonNameDAO;	
	/** The DAO interface for accessing Relationship Assertions */
	protected RelationshipAssertionDAO relationshipAssertionDAO;	
	/** The DAO interface for accessing Data Resources */
	protected DataResourceDAO dataResourceDAO;
	/** The DAO interface for accessing Countries */
	protected CountryDAO countryDAO;
	/** The DAO interface for accessing Countries */
	protected ImageRecordDAO imageRecordDAO;
	/** The remote concept DAO */
	protected RemoteConceptDAO remoteConceptDAO;
	/** The typification DAO */
	protected TypificationRecordDAO typificationRecordDAO;
	
	/** The DTO factory for producing BriefTaxonConceptDTOs from model objects */
	protected BriefTaxonConceptDTOFactory briefTaxonConceptDTOFactory;	
	/** The DTO factory for producing TaxonConceptDTOs from model objects */
	protected DTOFactory taxonConceptDTOFactory;
	/** The DTO factory for producing DistributionDTOs from model objects */
	protected DTOFactory distributionDTOFactory;
	/** The DTO factory for common names */
	protected DTOFactory commonNameDTOFactory;
	/** The DTO factory for relationship assertions */
	protected DTOFactory relationshipAssertionDTOFactory;
	/** The DTO Factory for taxonConceptCommonNameDTOFactory */
	protected DTOFactory taxonConceptCommonNameDTOFactory;
	/** The DTO Factory for taxonConceptCommonNameDTOFactory */
	protected DTOFactory keyValueDTOFactory;
	/** The DTO Factory for imageRecordDTOFactory */
	protected DTOFactory imageRecordDTOFactory;	
	/** The DTO Factory for RemoteConceptDTOs */
	protected DTOFactory remoteConceptDTOFactory;	
	/** The DTO Factory for TypificationRecordDTOs */
	protected DTOFactory typificationRecordDTOFactory;	
	/** DTO Factory for CountDTOs */
	protected DTOFactory countDTOFactory;	
	
	/** yahoo base url **/
	protected String imageWebServiceBaseURL="http://search.yahooapis.com/ImageSearchService/V1/imageSearch?appid=portaldev.gbif.org&query=";
	
	/**
	 * @see org.gbif.portal.service.TaxonomyManager#getTaxonConceptFor(java.lang.String)
	 */
	public TaxonConceptDTO getTaxonConceptFor(String taxonConceptKey) throws ServiceException {
		Long taxonConceptId = parseKey(taxonConceptKey);
		TaxonConcept taxonConcept = taxonConceptDAO.getDetailedTaxonConceptFor(taxonConceptId);
		return (TaxonConceptDTO) taxonConceptDTOFactory.createDTO(taxonConcept);
	}
	
	/**
	 * @see org.gbif.portal.service.TaxonomyManager#getTaxonConceptFor(java.lang.String, java.lang.String)
	 */
	public TaxonConceptDTO getTaxonConceptFor(String taxonConceptKey, String isoLanguageCode) throws ServiceException {
		Long taxonConceptId = parseKey(taxonConceptKey);
		TaxonConcept taxonConcept = taxonConceptDAO.getDetailedTaxonConceptFor(taxonConceptId);
		return (TaxonConceptDTO) new TaxonConceptDTOFactory(isoLanguageCode).createDTO(taxonConcept);
	}
	
	/**
	 * @see org.gbif.portal.service.TaxonomyManager#getBriefTaxonConceptFor(java.lang.String)
	 */
	public BriefTaxonConceptDTO getBriefTaxonConceptFor(String taxonConceptKey) throws ServiceException {
		Long taxonConceptId = parseKey(taxonConceptKey);
		TaxonConceptLite taxonConcept = taxonConceptDAO.getTaxonConceptLiteFor(taxonConceptId);
		return (BriefTaxonConceptDTO) briefTaxonConceptDTOFactory.createDTO(taxonConcept);
	}
	
	/**
	 * @see org.gbif.portal.service.TaxonomyManager#getTaxonConceptForRemoteId(java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	public List<TaxonConceptDTO> getTaxonConceptForRemoteId(String remoteId) throws ServiceException {
		List<TaxonConcept> taxonConcepts = taxonConceptDAO.getTaxonConceptForRemoteId(remoteId);
		return (List<TaxonConceptDTO>) taxonConceptDTOFactory.createDTOList(taxonConcepts);
	}	
	
	/**
	 * @see org.gbif.portal.service.TaxonomyManager#getParentConceptFor(java.lang.String)
	 */
	public TaxonConceptDTO getParentConceptFor(String taxonConceptKey) throws ServiceException {
		Long taxonConceptId = parseKey(taxonConceptKey);
		TaxonConcept parentConcept = taxonConceptDAO.getParentConceptFor(taxonConceptId);
		return (TaxonConceptDTO) taxonConceptDTOFactory.createDTO(parentConcept);
	}

	/**
	 * @see org.gbif.portal.service.TaxonomyManager#getChildConceptsFor(java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	public List<BriefTaxonConceptDTO> getChildConceptsFor(String taxonConceptKey, boolean allowUnconfirmed) throws ServiceException {
		Long taxonConceptId = parseKey(taxonConceptKey);
		List<TaxonConcept> childConcepts = taxonConceptDAO.getChildConceptsFor(taxonConceptId, allowUnconfirmed);
		return (List<BriefTaxonConceptDTO>) briefTaxonConceptDTOFactory.createDTOList(childConcepts);
	}
	
	/**
	 * @see org.gbif.portal.service.TaxonomyManager#getChildConceptsFor(java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	public List<BriefTaxonConceptDTO> getChildConceptsFor(String taxonConceptKey, String isoCountryCode, boolean allowUnconfirmed) throws ServiceException {
		Long taxonConceptId = parseKey(taxonConceptKey);
		List<TaxonConceptLite> childConcepts = taxonConceptDAO.getLiteChildConceptsFor(taxonConceptId, isoCountryCode, allowUnconfirmed);
		return (List<BriefTaxonConceptDTO>) briefTaxonConceptDTOFactory.createDTOList(childConcepts);
	}

	/**
	 * @see org.gbif.portal.service.TaxonomyManager#getNubTaxonConceptFor(java.lang.String)
	 */
	public TaxonConceptDTO getNubTaxonConceptFor(String taxonConceptKey) throws ServiceException {
		Long taxonConceptId = parseKey(taxonConceptKey);
		TaxonConcept nubConcept = taxonConceptDAO.getNubConceptFor(taxonConceptId);
		return (TaxonConceptDTO) taxonConceptDTOFactory.createDTO(nubConcept);
	}	

	/**
	 * @see org.gbif.portal.service.TaxonomyManager#getTaxonConceptsForNubTaxonConcept(java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	public List<TaxonConceptDTO> getTaxonConceptsForNubTaxonConcept(String nubConceptKey) throws ServiceException {
		Long nubConceptId = parseKey(nubConceptKey);
		List<TaxonConcept> taxonConcepts = taxonConceptDAO.getTaxonConceptsForNubTaxonConcept(nubConceptId);
		if(logger.isDebugEnabled() && taxonConcepts!=null)
			logger.debug("Retrieved taxon concepts for nub concept:"+taxonConcepts.size());
		return (List<TaxonConceptDTO>) taxonConceptDTOFactory.createDTOList(taxonConcepts);
	}

	/**
	 * @see org.gbif.portal.service.TaxonomyManager#getAuthoritativeTaxonConceptsForNubTaxonConcept(java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	public List<TaxonConceptDTO> getAuthoritativeTaxonConceptsForNubTaxonConcept(String nubConceptKey) throws ServiceException {
		Long nubConceptId = parseKey(nubConceptKey);
		List<TaxonConcept> taxonConcepts = taxonConceptDAO.getAuthoritativeTaxonConceptsForNubTaxonConcept(nubConceptId);
		if(logger.isDebugEnabled() && taxonConcepts!=null)
			logger.debug("Retrieved authoritative taxon concepts for nub concept:"+taxonConcepts.size());
		return (List<TaxonConceptDTO>) taxonConceptDTOFactory.createDTOList(taxonConcepts);
	}

	/**
	 * @see org.gbif.portal.service.TaxonomyManager#getRootTaxonConceptsFor(java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	public List<BriefTaxonConceptDTO> getRootTaxonConceptsForTaxonomy(String dataProviderKey, String dataResourceKey) throws ServiceException {
		
		List<TaxonConcept> rootConcepts = retrieveRootConcepts(dataProviderKey, dataResourceKey);
		if(logger.isDebugEnabled() && rootConcepts!=null)
			logger.debug("Retrieved root concepts:"+rootConcepts.size());
		return (List<BriefTaxonConceptDTO>) briefTaxonConceptDTOFactory.createDTOList(rootConcepts);
	}	

	/**
	 * @see org.gbif.portal.service.TaxonomyManager#getRootTaxonConceptsForCountry(java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	public List<BriefTaxonConceptDTO> getRootTaxonConceptsForCountry(String isoCountryCode) throws ServiceException {
		List<TaxonConceptLite> rootConcepts = taxonConceptDAO.getCountryRootConceptsFor(isoCountryCode);		
		return (List<BriefTaxonConceptDTO>) briefTaxonConceptDTOFactory.createDTOList(rootConcepts);
	}
	
	/**
	 * @see org.gbif.portal.service.TaxonomyManager#getRootConceptRankForTaxonomy(java.lang.String, java.lang.String)
	 */
	public TaxonRankType getRootConceptRankForTaxonomy(String dataProviderKey, String dataResourceKey) throws ServiceException {

		List<TaxonConcept> rootConcepts = retrieveRootConcepts(dataProviderKey, dataResourceKey);
		//iterate through checking the rank
		if(rootConcepts==null || rootConcepts.size()==0)
			return null;
		
		TaxonRank rank = rootConcepts.get(0).getTaxonRank();
		for (TaxonConcept taxonConcept: rootConcepts){
			if(!rank.getValue().equals(taxonConcept.getTaxonRank().getValue()))
				return null;
		}
		return TaxonRankType.getRank(rank.getName());
	}

	/**
	 * Retrieves the root concepts, deciding upon whether the data resource is part of a shared taxonomy.
	 * 
	 * @param dataProviderId
	 * @param dataResourceId
	 * @return List of TaxonConcept root concepts
	 */
	private List<TaxonConcept> retrieveRootConcepts(String dataProviderKey, String dataResourceKey){
		Long dataProviderId = parseKey(dataProviderKey);
		Long dataResourceId = parseKey(dataResourceKey);		
		if(dataProviderId==null && dataResourceId==null)
			throw new IllegalArgumentException("The dataProviderKey or dataResourceKey must be provided.");		
		
		List<TaxonConcept> rootConcepts = null;
		if(dataResourceId!=null){
			DataResource dataResource = dataResourceDAO.getDataResourceFor(dataResourceId);			
			if(dataResource !=null && !dataResource.isSharedTaxonomy())
				rootConcepts = taxonConceptDAO.getDataResourceRootConceptsFor(dataResourceId);
			else if (dataProviderId!=null)
				rootConcepts = taxonConceptDAO.getDataProviderRootConceptsFor(dataProviderId);			
		} else {
			rootConcepts = taxonConceptDAO.getDataProviderRootConceptsFor(dataProviderId);			
		}		
		return rootConcepts;
	}

	
	
	/**
	 * @see org.gbif.portal.service.TaxonomyManager#getClassificationFor(java.lang.String, boolean, java.lang.String)
	 */
	public List<BriefTaxonConceptDTO> getClassificationFor(String taxonConceptKey, boolean retrieveChildren, String isoCountryCode, boolean allowUnconfirmed) throws ServiceException {
		return getClassificationFor(taxonConceptKey, true, retrieveChildren, isoCountryCode, false, allowUnconfirmed);
	}

	/**
	 * Uses TaxonConceptDAO.getParentChildConcepts to retrieve the specified concept,
	 * its child concepts and its parent concept. It then goes up the tree calling getParent
	 * until parent it null.
	 * 
	 * @see org.gbif.portal.service.TaxonomyManager#getClassification(java.lang.String, boolean)
	 */
	public List<BriefTaxonConceptDTO> getClassificationFor(String taxonConceptKey, boolean ascend, boolean descend, 
			String isoCountryCode, boolean includeCounts, boolean allowUnconfirmed) throws ServiceException {
		Long taxonConceptId = parseKey(taxonConceptKey);
		List<BriefTaxonConceptDTO> classificationDTOList = new ArrayList<BriefTaxonConceptDTO>();
		//add the concept
		TaxonConcept taxonConcept = taxonConceptDAO.getParentChildConcepts(taxonConceptId, false, allowUnconfirmed);
		if(taxonConcept!=null){
			
			if(ascend){
				//add the parent concepts
				TaxonConcept parentConcept = taxonConceptDAO.getParentConceptFor(taxonConcept.getId());		
				//recurse till parent null
				
				//save the IDs being stored in the Classification list, to check for previous existence when entering a new one (to avoid infinite loops)
				List<Long> previousIds = new ArrayList<Long>();
				
				while (parentConcept!=null && !previousIds.contains(parentConcept.getId())){
					classificationDTOList.add(0, (BriefTaxonConceptDTO) briefTaxonConceptDTOFactory.createDTO(parentConcept, includeCounts));
					previousIds.add(parentConcept.getId());
					Long oldId = parentConcept.getId();
					parentConcept = taxonConceptDAO.getParentConceptFor(parentConcept.getId());
					// avoid infinite loops due to bad data
					if(parentConcept!=null && parentConcept.getId().equals(oldId))
						parentConcept = null;
				}
			}
			
			//add this concept to the list
			classificationDTOList.add((BriefTaxonConceptDTO) briefTaxonConceptDTOFactory.createDTO(taxonConcept, includeCounts));
			
			if(descend){
				//order child concepts
				List<TaxonConceptLite> childConcepts = taxonConceptDAO.getLiteChildConceptsFor(taxonConceptId, isoCountryCode, allowUnconfirmed);
				ArrayList<TaxonConceptLite> childConceptList = new ArrayList<TaxonConceptLite>();
				childConceptList.addAll(childConcepts);
				Collections.sort(childConceptList, new Comparator<TaxonConceptLite>(){
					public int compare(TaxonConceptLite c1, TaxonConceptLite c2) {
						if(c1.getTaxonRank().getValue() != c2.getTaxonRank().getValue())
							return c1.getTaxonRank().getValue().compareTo(c2.getTaxonRank().getValue());
						//else compare canonical
						return c1.getTaxonNameLite().getCanonical().compareTo(c2.getTaxonNameLite().getCanonical());
					}
				});			
				//add child concepts
				for (TaxonConceptLite childConcept: childConceptList) {
					// non accepted ones are not added
					if (childConcept.isAccepted()) {
						classificationDTOList.add((BriefTaxonConceptDTO) briefTaxonConceptDTOFactory.createDTO(childConcept, includeCounts));
					}
				}
			}
			return classificationDTOList;
		}
		return null;
	}	

	/**
	 * @see org.gbif.portal.service.TaxonomyManager#countChildConceptsFor(java.lang.String, org.gbif.portal.dto.util.TaxonRankType)
	 */
	public int countChildConceptsFor(String taxonConceptKey, TaxonRankType childConceptRank, boolean countSynonyms, boolean onlyCountAccepted, boolean allowUnconfirmed) {
		if(!TaxonRankType.isRecognisedMajorRank(childConceptRank.getName()) && !TaxonRankType.SUB_SPECIES_STR.equals(childConceptRank.getName()))
			return 0;
		
		TaxonRank childRank = TaxonRank.getTaxonRank(childConceptRank.getName());
		TaxonConcept tc = taxonConceptDAO.getTaxonConceptFor(parseKey(taxonConceptKey));
		if(tc!=null){
			TaxonRank parentRank = tc.getTaxonRank();
			if(!TaxonRankType.isRecognisedMajorRank(parentRank.getName()))
				return 0;
			
			return taxonConceptDAO.countChildConcepts(tc.getId(), parentRank, childRank, countSynonyms, onlyCountAccepted, allowUnconfirmed);
		}
		return 0;
	}
	
	/**
	 * @see org.gbif.portal.service.TaxonomyManager#findTaxonConceptsWithSameScientificNameAndRankAs(java.lang.String, org.gbif.portal.dto.util.SearchConstraints)
	 */
	public SearchResultsDTO findTaxonConceptsWithSameScientificNameAndRankAs(String taxonConceptKey, String dataProviderKey, String dataResourceKey, SearchConstraints searchConstraints) throws ServiceException {
		Long taxonConceptId = parseKey(taxonConceptKey);
		Long dataProviderId = parseKey(dataProviderKey);
		Long dataResourceId = parseKey(dataResourceKey); 
		
		List<TaxonConcept> matchingConcepts = taxonConceptDAO.findTaxonConceptsWithSameCanonicalAndRankAs(taxonConceptId, dataProviderId, dataResourceId, searchConstraints.getStartIndex(), searchConstraints.getMaxResults());
		return taxonConceptDTOFactory.createResultsDTO(matchingConcepts, searchConstraints.getMaxResults());
	}
	
	/**
	 * @see org.gbif.portal.service.TaxonomyManager#findTaxonConcepts(java.lang.String, boolean, java.lang.String, java.lang.String, org.gbif.portal.dto.util.SearchConstraints)
	 */
	public SearchResultsDTO findTaxonConcepts(String nameStub, boolean fuzzy, String rank, String dataProviderKey, String dataResourceKey, String resourceNetworkKey, String hostIsoCountryCode, String isoLanguageCode, Date modifiedSince, boolean allowUnconfirmed, boolean sortAlphabetically, SearchConstraints searchConstraints) throws ServiceException {
		if(logger.isDebugEnabled())
			logger.debug("Search string: "+nameStub);
		Long dataProviderId = parseKey(dataProviderKey);
		Long dataResourceId = parseKey(dataResourceKey);	
		Long resourceNetworkId = parseKey(resourceNetworkKey);	
		try {
			TaxonRank taxonRank = null;
			if(StringUtils.isNotEmpty(rank)){
				taxonRank = TaxonRank.getTaxonRank(rank);
			}
			if (hostIsoCountryCode != null && countryDAO.getCountryForIsoCountryCode(hostIsoCountryCode, null) == null ) {
				throw new ServiceException("No country found for host ISO code " + hostIsoCountryCode);
			}
			List<TaxonConcept> matchingConcepts = taxonConceptDAO.findTaxonConcepts(nameStub, null, fuzzy, taxonRank, dataProviderId, dataResourceId, resourceNetworkId, hostIsoCountryCode, modifiedSince, allowUnconfirmed, sortAlphabetically, searchConstraints.getStartIndex(), searchConstraints.getMaxResults()+1);
			if(logger.isDebugEnabled())
				logger.debug("matchingConcepts: "+matchingConcepts.size());
			
			if (isoLanguageCode!=null) {
				return new TaxonConceptDTOFactory(isoLanguageCode).createResultsDTO(matchingConcepts, searchConstraints.getMaxResults());
			} else {
				return taxonConceptDTOFactory.createResultsDTO(matchingConcepts, searchConstraints.getMaxResults());
			}			
			
		} catch (Exception ex){
			logger.error(ex.getMessage(), ex);
			return null;
		}
	}

	/**
	 * @see org.gbif.portal.service.TaxonomyManager#findSpeciesConcepts(java.lang.String, java.lang.String, boolean, java.lang.String, java.lang.String, java.util.Date, org.gbif.portal.dto.util.SearchConstraints)
	 */
	public SearchResultsDTO findSpeciesConcepts(String scientificName, String specificEphitet, boolean fuzzy, String dataProviderKey, String dataResourceKey, Date modifiedSince, SearchConstraints searchConstraints) throws ServiceException {
		if(logger.isDebugEnabled())
			logger.debug("Search scientificName: "+scientificName+", specificEphitet: "+specificEphitet);
		Long dataProviderId = parseKey(dataProviderKey);
		Long dataResourceId = parseKey(dataResourceKey);	
		List<TaxonConcept> matchingConcepts = taxonConceptDAO.findTaxonConcepts(scientificName, specificEphitet, fuzzy, (TaxonRank) null, dataProviderId, dataResourceId, null, null, modifiedSince, true, false, searchConstraints.getStartIndex(), searchConstraints.getMaxResults()+1);
		return taxonConceptDTOFactory.createResultsDTO(matchingConcepts, searchConstraints.getMaxResults());
	}
	
	/**
	 * @see org.gbif.portal.service.TaxonomyManager#countTaxonConcepts(java.lang.String, boolean, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.util.Date)
	 */
	public Long countTaxonConcepts(String nameStub, boolean fuzzy, String rank, String dataProviderKey, String dataResourceKey, String resourceNetworkKey, String hostIsoCountryCode, Date modifiedSince) throws ServiceException {
		if(logger.isDebugEnabled())
			logger.debug("Search string: "+nameStub);
		Long dataProviderId = parseKey(dataProviderKey);
		Long dataResourceId = parseKey(dataResourceKey);	
		Long resourceNetworkId = parseKey(resourceNetworkKey);	
		try {
			TaxonRank taxonRank = null;
			if(StringUtils.isNotEmpty(rank)){
				taxonRank = TaxonRank.getTaxonRank(rank);
			}
			if (hostIsoCountryCode != null && countryDAO.getCountryForIsoCountryCode(hostIsoCountryCode, null) == null ) {
				throw new ServiceException("No country found for host ISO code " + hostIsoCountryCode);
			}
			Long count = taxonConceptDAO.countTaxonConcepts(nameStub, fuzzy, taxonRank, dataProviderId, dataResourceId, resourceNetworkId, hostIsoCountryCode, modifiedSince);
			if(logger.isDebugEnabled())
				logger.debug("concept count: "+count);
			return count;
		} catch (Exception ex){
			logger.error(ex.getMessage(), ex);
			return null;
		}
	}

	/**
	 * @see org.gbif.portal.service.TaxonomyManager#findRemoteUrlFor(java.lang.String)
	 */
	public List<String> findRemoteUrlFor(String taxonConceptKey) {
		return remoteConceptDAO.findRemoteUrlFor(parseKey(taxonConceptKey));
	}
	
	/**
	 * @see org.gbif.portal.service.TaxonomyManager#findRemoteUrlFor(java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	public List<RemoteConceptDTO> findRemoteConceptsFor(String taxonConceptKey) {
		List<RemoteConcept> remoteConcepts = remoteConceptDAO.findRemoteConceptsFor(parseKey(taxonConceptKey));
		return (List<RemoteConceptDTO>) remoteConceptDTOFactory.createDTOList(remoteConcepts);
	}
	
	/**
	 * @see org.gbif.portal.service.TaxonomyManager#getTotalTaxonConceptCount()
	 */
	public int getTotalTaxonConceptCount() throws ServiceException {
		return taxonConceptDAO.getTaxonConceptCount(null, null);
	}	
	
	/**
	 * @see org.gbif.portal.service.TaxonomyManager#getTaxonConceptCount(java.lang.String, boolean, java.lang.String)
	 */
	public int getTaxonConceptCount(String nameStub, boolean fuzzy, String dataProviderKey, String dataResourceKey) throws ServiceException {
		Long dataProviderId = parseKey(dataProviderKey);
		Long dataResourceId = parseKey(dataResourceKey);
		return taxonConceptDAO.getTaxonConceptCount(nameStub, fuzzy, dataProviderId, dataResourceId);
	}

	/**
	 * @see org.gbif.portal.service.TaxonomyManager#getTaxonConceptCountForRank(java.lang.String, java.lang.Boolean, java.lang.String)
	 */
	public int getTaxonConceptCountForRank(String taxonRank, Boolean higherThanSuppliedRank, String dataProviderKey, String dataResourceKey) throws ServiceException {
		TaxonRank recognisedRank = TaxonRank.getTaxonRank(taxonRank);
		if(recognisedRank!=null && recognisedRank!= TaxonRank.UNKNOWN){
			Long dataProviderId = parseKey(dataProviderKey);
			Long dataResourceId = parseKey(dataResourceKey);
			return taxonConceptDAO.getTaxonConceptCountForRank(recognisedRank, higherThanSuppliedRank, dataProviderId, dataResourceId);
		}
		throw new IllegalArgumentException("Unrecognised rank value: "+taxonRank);
	}	

	/**
	 * @see org.gbif.portal.service.TaxonomyManager#findMatchingScientificNamesWithSoundex(java.lang.String, boolean, org.gbif.portal.dto.util.SearchConstraints)
	 */
	public SearchResultsDTO findMatchingScientificNamesWithSoundex(String name, SearchConstraints searchConstraints) throws ServiceException {
		List<String> matchingNames = taxonNameDAO.findScientificNames(name, false, null, null, true, searchConstraints.getStartIndex(), searchConstraints.getMaxResults()+1);
		return DTOUtils.createSearchResultsDTO(matchingNames, searchConstraints.getMaxResults());
	}	
	
	
	public SearchResultsDTO findMatchingScientificNames(String name, boolean fuzzy, TaxonRankType taxonRankType, Boolean higherThanRankSupplied, boolean soundex, String dataProviderKey, String dataResourceKey, boolean allowUnconfirmed, SearchConstraints searchConstraints) throws ServiceException {
		TaxonRank taxonRank = null;
		if(taxonRankType!=null)
			taxonRank = TaxonRank.getTaxonRank(taxonRankType.getName());
		
		List<String> matchingNames = null;
		
		if(dataProviderKey!=null || dataResourceKey!=null){
			matchingNames = taxonNameDAO.findScientificNamesInTaxonomy(name, fuzzy, taxonRank, higherThanRankSupplied, soundex, parseKey(dataProviderKey), parseKey(dataResourceKey), allowUnconfirmed, searchConstraints.getStartIndex(), searchConstraints.getMaxResults()+1);
		} else {
			matchingNames = taxonNameDAO.findScientificNames(name, fuzzy, taxonRank, higherThanRankSupplied, soundex, searchConstraints.getStartIndex(), searchConstraints.getMaxResults()+1);			
		}
		return DTOUtils.createSearchResultsDTO(matchingNames, searchConstraints.getMaxResults());
	}
	
	/**
	 * @see org.gbif.portal.service.TaxonomyManager#findMatchingScientificNames(java.lang.String, boolean, boolean, org.gbif.portal.dto.util.SearchConstraints)
	 */
	public SearchResultsDTO findMatchingScientificNames(String name, boolean fuzzy, SearchConstraints searchConstraints) throws ServiceException {
		List<TaxonConcept> matchingNames = taxonConceptDAO.findTaxonConcepts(name, null, fuzzy, null, null, null, null, null, null, true, false, searchConstraints.getStartIndex(), searchConstraints.getMaxResults());
		return briefTaxonConceptDTOFactory.createResultsDTO(matchingNames, searchConstraints.getMaxResults());
	}		

	/**
	 * @see org.gbif.portal.service.TaxonomyManager#findTaxonConceptsForCommonName(java.lang.String, boolean, org.gbif.portal.dto.util.SearchConstraints)
	 */
	public SearchResultsDTO findTaxonConceptsForCommonName(String commonNameStub, boolean fuzzy, SearchConstraints searchConstraints) throws ServiceException {
		List<CommonName> results = taxonConceptDAO.findTaxonConceptsForCommonName(commonNameStub, fuzzy, searchConstraints.getStartIndex(), searchConstraints.getMaxResults()+1);
		return taxonConceptCommonNameDTOFactory.createResultsDTO(results, searchConstraints.getMaxResults());
	}
	
	/**
	 * @see org.gbif.portal.service.TaxonomyManager#findMatchingCommonNames(java.lang.String, boolean, org.gbif.portal.dto.util.SearchConstraints)
	 */
	public SearchResultsDTO findMatchingCommonNames(String name, boolean fuzzy, SearchConstraints searchConstraints) throws ServiceException {
		List<String> results = commonNameDAO.findCommonNames(name, fuzzy, searchConstraints.getStartIndex(), searchConstraints.getMaxResults()+1);
		SearchResultsDTO searchResultsDTO = new SearchResultsDTO();
		DTOUtils.populate(searchResultsDTO, results, searchConstraints.getMaxResults());
		return searchResultsDTO;
	}

	/**
	 * @see org.gbif.portal.service.TaxonomyManager#getCommonNameFor(java.lang.String)
	 */
	public CommonNameDTO getCommonNameFor(String commonNameKey) {
		Long commonNameId = parseKey(commonNameKey);
		CommonName commonName = commonNameDAO.getCommonNameFor(commonNameId);
		return (CommonNameDTO) commonNameDTOFactory.createDTO(commonName);
	}

	/**
	 * @see org.gbif.portal.service.TaxonomyManager#findImagesFor(java.lang.String, org.gbif.portal.dto.util.SearchConstraints)
	 */
	public SearchResultsDTO findImagesFor(String taxonConceptKey, SearchConstraints searchConstraints) throws ServiceException {
		//todo and constraints to DAO methods
		List<ImageRecord> imageRecords = imageRecordDAO.getImageRecordsForTaxonConcept(parseKey(taxonConceptKey));
		return imageRecordDTOFactory.createResultsDTO(imageRecords, searchConstraints.getMaxResults());
	}
	
	/**
	 * This is currently dependent on the Yahoo Image Search Web Service. 
	 * 
	 * TODO review use of DOM4j and XPath
	 * 
	 * @see org.gbif.portal.service.TaxonomyManager#findImagesForScientificName(java.lang.String, org.gbif.portal.dto.util.SearchConstraints)
	 */
	@SuppressWarnings("unchecked")
	protected SearchResultsDTO findImagesForScientificName(String scientificName, SearchConstraints searchConstraints) throws ServiceException {

		try {
			scientificName = URLEncoder.encode(scientificName, "UTF-8");
			String searchUrlAsString=imageWebServiceBaseURL+scientificName+"&results="+searchConstraints.getMaxResults();
			SAXReader xmlReader = new SAXReader();
			URL searchUrl = new URL(searchUrlAsString);
			Document doc = xmlReader.read(searchUrl);
			Element resultSetElement = doc.getRootElement();
			
			/*
			// To use namespace uri's then this is correct
			// look at the property store namespace for BIOCASE and see the XPATH objects being created in config
			// This file would then have 
			// - getResultsXPath()
			// - getTitleXPath() etc etc etc
			Map<String, String> namespaceURIs = new HashMap<String,String>();
			namespaceURIs.put("y", "urn:yahoo:srchmi");			
			XPath xpath = new DefaultXPath("//y:Result");
			xpath.setNamespaceURIs(namespaceURIs);
			List<Node> results =(List<Node>) xpath.selectNodes(doc);
			*/
			 
			
			List<Node> results =(List<Node>) resultSetElement.selectNodes("//*[local-name()='Result']");
			SearchResultsDTO searchResults = new SearchResultsDTO();
			for (Node result: results){
				String title = result.selectSingleNode("//*[local-name()='Title']").getText();				
				String description = result.selectSingleNode("//*[local-name()='Summary']").getText();							
				String url = result.selectSingleNode("//*[local-name()='Url']").getText();
				String height = result.selectSingleNode("//*[local-name()='Height']").getText();
				String width = result.selectSingleNode("//*[local-name()='Width']").getText();
//				String fileFormat = result.selectSingleNode("//*[local-name()='FileFormat']").getText();
				ImageRecordDTO imageDTO = new ImageRecordDTO();
				imageDTO.setUrl(url);
				imageDTO.setTitle(title);
				imageDTO.setDescription(description);
				if(StringUtils.isNotEmpty(height))
					imageDTO.setHeightInPixels(Integer.parseInt(height));
				if(StringUtils.isNotEmpty(width))
					imageDTO.setWidthInPixels(Integer.parseInt(width));
				
				String thumbnailUrl =  ((Node)result.selectNodes("//*[local-name()='Thumbnail']/*[local-name()='Url']").get(0)).getText();
//				String thumbnailHeight =  ((Node)result.selectNodes("//*[local-name()='Thumbnail']/*[local-name()='Height']").get(0)).getText();
//				String thumbnailWidth =  ((Node)result.selectNodes("//*[local-name()='Thumbnail']/*[local-name()='Width']").get(0)).getText();
				
				imageDTO.setUrl(thumbnailUrl);
//				if(StringUtils.isNotEmpty(thumbnailHeight))
//					imageDTO.setThumbnailHeightInPixels(Integer.parseInt(thumbnailHeight));
//				if(StringUtils.isNotEmpty(width))
//					imageDTO.setThumbnailWidthInPixels(Integer.parseInt(thumbnailWidth));
				
				searchResults.addResult(imageDTO);
			}
			return searchResults;
		} catch(IOException e){
			logger.error(e.getMessage(), e);
		} catch(DocumentException e){
			logger.error(e.getMessage(), e);
		}
		return new SearchResultsDTO();
	}		

	/**
	 * @see org.gbif.portal.service.TaxonomyManager#findRelationshipAssertionsForFromTaxonConcept(java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	public List<RelationshipAssertionDTO> findRelationshipAssertionsForFromTaxonConcept(String fromTaxonConceptKey) {
		Long fromTaxonConceptId = parseKey(fromTaxonConceptKey);
		List<RelationshipAssertion> relationshipAssertions = relationshipAssertionDAO.getRelationshipAssertionsForFromTaxonConcept(fromTaxonConceptId);
		if(logger.isDebugEnabled() && relationshipAssertions!=null)
			logger.debug("Retrieved relationship assertions for from concept:"+relationshipAssertions.size());
		return (List<RelationshipAssertionDTO>) relationshipAssertionDTOFactory.createDTOList(relationshipAssertions);
	}

	/**
	 * @see org.gbif.portal.service.TaxonomyManager#findRelationshipAssertionsForToTaxonConcept(java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	public List<RelationshipAssertionDTO> findRelationshipAssertionsForToTaxonConcept(String toTaxonConceptKey) {
		Long toTaxonConceptId = parseKey(toTaxonConceptKey);
		List<RelationshipAssertion> relationshipAssertions = relationshipAssertionDAO.getRelationshipAssertionsForToTaxonConcept(toTaxonConceptId);
		if(logger.isDebugEnabled() && relationshipAssertions!=null)
			logger.debug("Retrieved relationship assertions for to concept:"+relationshipAssertions.size());
		return (List<RelationshipAssertionDTO>) relationshipAssertionDTOFactory.createDTOList(relationshipAssertions);
	}

	/**
	 * @see org.gbif.portal.service.TaxonomyManager#findCommonNamesForTaxonConcept(java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	public List<CommonNameDTO> findCommonNamesForTaxonConcept(String taxonConceptKey, SearchConstraints searchConstraints) {
		Long taxonConceptId = parseKey(taxonConceptKey);
		List<CommonName> commonNames = commonNameDAO.getCommonNamesFor(taxonConceptId, false, searchConstraints.getStartIndex(), searchConstraints.getMaxResults());
		if(logger.isDebugEnabled() && commonNames!=null)
			logger.debug("Retrieved common names for concept:"+commonNames.size());
		return (List<CommonNameDTO>) commonNameDTOFactory.createDTOList(commonNames);
	}	

	/**
	 * @see org.gbif.portal.service.TaxonomyManager#getCountryCountsForTaxonConcept(java.lang.String)
	 */
	public List<CountDTO> getCountryCountsForTaxonConcept(String taxonConceptKey, Locale locale) throws ServiceException {
		Long taxonConceptId = parseKey(taxonConceptKey);		
		List counts = countryDAO.getCountryCountsForTaxonConcept(taxonConceptId, locale);
		return countDTOFactory.createDTOList(counts);
	}
	
  /**
   * @see org.gbif.portal.service.TaxonomyManager#getTypificationRecordsForTaxonName(java.lang.String)
   */
  @SuppressWarnings("unchecked")
  public List<TypificationRecordDTO> getTypificationRecordsForPartnersOfTaxonConcept(String nubConceptKey) throws ServiceException {
    Long nubConceptId = parseKey(nubConceptKey);
    if(nubConceptId==null)
      return new ArrayList<TypificationRecordDTO>();
    List<TypificationRecord> records = typificationRecordDAO.getTypificationRecordsForNamesOfPartnersOfTaxonConcept(nubConceptId);
    return typificationRecordDTOFactory.createDTOList(records);   
  }

  /**
   * @see org.gbif.portal.service.TaxonomyManager#getTypificationRecordsForTaxonName(java.lang.String)
   */
  @SuppressWarnings("unchecked")
  public List<TypificationRecordDTO> getTypificationRecordsForTaxonConcept(String conceptKey) throws ServiceException {
    Long conceptId = parseKey(conceptKey);
    if (conceptId==null)
      return new ArrayList<TypificationRecordDTO>();
    return typificationRecordDTOFactory.createDTOList(typificationRecordDAO.getTypificationRecordsForTaxonConcept(conceptId));
  }

	/**
	 * @see org.gbif.portal.service.TaxonomyManager#getTaxonConceptForRemoteId(java.lang.String, java.lang.String, java.lang.String)
	 */
	public TaxonConceptDTO getTaxonConceptForRemoteId(String dataProviderKey, String dataResourceKey, String remoteId) throws ServiceException {
		TaxonConcept tc = taxonConceptDAO.getTaxonConceptForRemoteId(parseKey(dataProviderKey), parseKey(dataResourceKey), remoteId);
		return (TaxonConceptDTO) taxonConceptDTOFactory.createDTO(tc);
	}	

	/**
	 * @see org.gbif.portal.service.TaxonomyManager#isValidTaxonConceptKey(java.lang.String)
	 */
	public boolean isValidTaxonConceptKey(String taxonConceptKey){
		Long key = parseKey(taxonConceptKey);
		return key!=null;
	}
	
	/**
	 * Parses the supplied key. Returns null if supplied string invalid
	 * @param key
	 * @return a concept key. Returns null if supplied string invalid key
	 */
	protected static Long parseKey(String key){
		Long parsedKey = null;
		try {
			parsedKey = Long.parseLong(key);
		} catch (NumberFormatException e){
			//expected behaviour for invalid keys
		}
		return parsedKey;
	}	
	
	/**
	 * @param taxonConceptDAO the taxonConceptDAO to set
	 */
	public void setTaxonConceptDAO(TaxonConceptDAO taxonConceptDAO) {
		this.taxonConceptDAO = taxonConceptDAO;
	}

	/**
	 * @param briefTaxonConceptDTOFactory the briefTaxonConceptDTOFactory to set
	 */
	public void setBriefTaxonConceptDTOFactory(
			BriefTaxonConceptDTOFactory briefTaxonConceptDTOFactory) {
		this.briefTaxonConceptDTOFactory = briefTaxonConceptDTOFactory;
	}

	/**
	 * @param taxonConceptDTOFactory the taxonConceptDTOFactory to set
	 */
	public void setTaxonConceptDTOFactory(DTOFactory taxonConceptDTOFactory) {
		this.taxonConceptDTOFactory = taxonConceptDTOFactory;
	}
	
	/**
	 * @param dataResourceDAO the dataResourceDAO to set
	 */
	public void setDataResourceDAO(DataResourceDAO dataResourceDAO) {
		this.dataResourceDAO = dataResourceDAO;
	}

	/**
	 * @param taxonNameDAO the taxonNameDAO to set
	 */
	public void setTaxonNameDAO(TaxonNameDAO taxonNameDAO) {
		this.taxonNameDAO = taxonNameDAO;
	}

	/**
	 * @param distributionDTOFactory The distributionDTOFactory to set.
	 */
	public void setDistributionDTOFactory(DTOFactory distributionDTOFactory) {
		this.distributionDTOFactory = distributionDTOFactory;
	}

	/**
	 * @param countryDAO the countryDAO to set
	 */
	public void setCountryDAO(CountryDAO countryDAO) {
		this.countryDAO = countryDAO;
	}

	/**
	 * @param baseUrl the baseUrl to set
	 */
	public void setImageWebServiceBaseURL(String baseUrl) {
		this.imageWebServiceBaseURL = baseUrl;
	}

	/**
	 * @param commonNameDAO the commonNameDAO to set
	 */
	public void setCommonNameDAO(CommonNameDAO commonNameDAO) {
		this.commonNameDAO = commonNameDAO;
	}

	/**
	 * @param commonNameDTOFactory the commonNameDTOFactory to set
	 */
	public void setCommonNameDTOFactory(DTOFactory commonNameDTOFactory) {
		this.commonNameDTOFactory = commonNameDTOFactory;
	}

	/**
	 * @param relationshipAssertionDAO the relationshipAssertionDAO to set
	 */
	public void setRelationshipAssertionDAO(
			RelationshipAssertionDAO relationshipAssertionDAO) {
		this.relationshipAssertionDAO = relationshipAssertionDAO;
	}

	/**
	 * @param relationshipAssertionDTOFactory the relationshipAssertionDTOFactory to set
	 */
	public void setRelationshipAssertionDTOFactory(
			DTOFactory relationshipAssertionDTOFactory) {
		this.relationshipAssertionDTOFactory = relationshipAssertionDTOFactory;
	}

	/**
	 * @param taxonConceptCommonNameDTOFactory the taxonConceptCommonNameDTOFactory to set
	 */
	public void setTaxonConceptCommonNameDTOFactory(
			DTOFactory taxonConceptCommonNameDTOFactory) {
		this.taxonConceptCommonNameDTOFactory = taxonConceptCommonNameDTOFactory;
	}

	/**
	 * @param keyValueDTOFactory the keyValueDTOFactory to set
	 */
	public void setKeyValueDTOFactory(DTOFactory keyValueDTOFactory) {
		this.keyValueDTOFactory = keyValueDTOFactory;
	}

	/**
	 * @param imageRecordDAO the imageRecordDAO to set
	 */
	public void setImageRecordDAO(ImageRecordDAO imageRecordDAO) {
		this.imageRecordDAO = imageRecordDAO;
	}

	/**
	 * @param imageRecordDTOFactory the imageRecordDTOFactory to set
	 */
	public void setImageRecordDTOFactory(DTOFactory imageRecordDTOFactory) {
		this.imageRecordDTOFactory = imageRecordDTOFactory;
	}

	/**
	 * @param remoteConceptDAO the remoteConceptDAO to set
	 */
	public void setRemoteConceptDAO(RemoteConceptDAO remoteConceptDAO) {
		this.remoteConceptDAO = remoteConceptDAO;
	}

	/**
	 * @param remoteConceptDTOFactory the remoteConceptDTOFactory to set
	 */
	public void setRemoteConceptDTOFactory(DTOFactory remoteConceptDTOFactory) {
		this.remoteConceptDTOFactory = remoteConceptDTOFactory;
	}

	/**
	 * @return Returns the typificationRecordDAO.
	 */
	public TypificationRecordDAO getTypificationRecordDAO() {
		return typificationRecordDAO;
	}

	/**
	 * @param typificationRecordDAO The typificationRecordDAO to set.
	 */
	public void setTypificationRecordDAO(TypificationRecordDAO typificationRecordDAO) {
		this.typificationRecordDAO = typificationRecordDAO;
	}

	/**
	 * @return Returns the typificationRecordDTOFactory.
	 */
	public DTOFactory getTypificationRecordDTOFactory() {
		return typificationRecordDTOFactory;
	}

	/**
	 * @param typificationRecordDTOFactory The typificationRecordDTOFactory to set.
	 */
	public void setTypificationRecordDTOFactory(
			DTOFactory typificationRecordDTOFactory) {
		this.typificationRecordDTOFactory = typificationRecordDTOFactory;
	}

	/**
	 * @param countDTOFactory the countDTOFactory to set
	 */
	public void setCountDTOFactory(DTOFactory countDTOFactory) {
		this.countDTOFactory = countDTOFactory;
	}
}