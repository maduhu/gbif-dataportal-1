/***************************************************************************
 * Copyright (C) 2006 Global Biodiversity Information Facility Secretariat.  
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

package org.gbif.portal.webservices.actions;

import org.gbif.portal.dto.SearchResultsDTO;
import org.gbif.portal.dto.occurrence.ExtendedOccurrenceRecordDTO;
import org.gbif.portal.dto.occurrence.IdentifierRecordDTO;
import org.gbif.portal.dto.occurrence.ImageRecordDTO;
import org.gbif.portal.dto.occurrence.KmlOccurrenceRecordDTO;
import org.gbif.portal.dto.occurrence.RawOccurrenceRecordDTO;
import org.gbif.portal.dto.occurrence.TypificationRecordDTO;
import org.gbif.portal.dto.resources.DataProviderDTO;
import org.gbif.portal.dto.resources.DataResourceDTO;
import org.gbif.portal.dto.taxonomy.TaxonConceptDTO;
import org.gbif.portal.model.occurrence.IdentifierType;
import org.gbif.portal.service.DataResourceManager;
import org.gbif.portal.service.OccurrenceManager;
import org.gbif.portal.service.ServiceException;
import org.gbif.portal.service.TaxonomyManager;
import org.gbif.portal.service.triplet.TripletQueryManager;
import org.gbif.portal.webservices.util.GbifWebServiceException;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author
 * 
 */
public class OccurrenceAction extends Action {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.gbif.portal.service.OccurrenceManager
	 */
	protected OccurrenceManager occurrenceManager;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.gbif.portal.service.TaxonomyManager
	 */
	protected TaxonomyManager taxonomyManager;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.gbif.portal.service.triplet.TripletQueryManager
	 */
	protected TripletQueryManager tripletOccurrenceManager;
	
	protected TripletQueryManager tripletOccurrenceKMLManager;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.gbif.portal.service.triplet.TripletQueryManager
	 */
	protected TripletQueryManager tripletOccurrenceCountsManager;

	protected DataResourceManager dataResourceManager;
	

	private static DateFormat ymdFormat = new SimpleDateFormat("yyyy-MM-dd");
	protected int styleCount = 24; // number of KML icons

	public static Log log = LogFactory.getLog(OccurrenceAction.class);

	/**
	 * List of icons for use with KML
	 */
	protected List<String> kmlIcons;	
	
	/**
	 * Gets the template of the Occurrence Action.
	 * 
	 * @param parameterMap
	 * @return String with the template
	 */
	public String getTemplate(Map<String, Object> parameterMap) {
		OccurrenceParameters params = null;

		try {
			params = new OccurrenceParameters(parameterMap, pathMapping);

			switch (params.getRequestType()) {
			case LIST:
				if (params.getFormat() == OccurrenceParameters.FORMAT_KML)
					return "org/gbif/portal/ws/occurrence/occurrence-kml.vm";
				else if(params.getFormat() == OccurrenceParameters.FORMAT_BRIEF)
					return "org/gbif/portal/ws/occurrence/occurrence-brief.vm";
				else if(params.getFormat() == OccurrenceParameters.FORMAT_DARWIN)
					return "org/gbif/portal/ws/occurrence/occurrence-darwin.vm";
			case GET:
				if (params.getFormat() == OccurrenceParameters.FORMAT_KML)
					return "org/gbif/portal/ws/occurrence/occurrence-kml.vm";
				else if(params.getFormat() == OccurrenceParameters.FORMAT_BRIEF)
					return "org/gbif/portal/ws/occurrence/occurrence-brief.vm";
				else if(params.getFormat() == OccurrenceParameters.FORMAT_DARWIN)
					return "org/gbif/portal/ws/occurrence/occurrence-darwin.vm";
			case COUNT:
				return "org/gbif/portal/ws/occurrence/occurrence-count.vm";
			case HELP:
				return "org/gbif/portal/ws/occurrence/occurrence-count.vm";
			default:
				return "org/gbif/portal/ws/occurrence/occurrence-count.vm";
			}
		} catch (Exception se) {
			log
					.error("Unregistered data service error: "
							+ se.getMessage(), se);
			if (params == null)
				return "org/gbif/portal/ws/occurrence/occurrence-count.vm";// gbifMappingFactory.getGbifResponseDocument(parameterMap,
							// se);
			else
				return "org/gbif/portal/ws/occurrence/occurrence-count.vm";// gbifMappingFactory.getGbifResponseDocument(params,
							// se);
		}

	}

	/**
	 * Counts number of occurrence records
	 * 
	 * @param params
	 * @return a map with count element
	 * @throws GbifWebServiceException
	 */
	public Map<String, Object> countOccurrenceRecords(
			OccurrenceParameters params) throws GbifWebServiceException {

		Map<String, String> headerMap;
		Map<String, String> parameterMap;
		Map<String, String> summaryMap=null;

		Map<String, Object> results = new HashMap<String, Object>();
		Long recordCount = 0L;

		headerMap = returnHeader(params, true);
		parameterMap = returnParameters(params.getParameterMap(null));
		

		try {
			SearchResultsDTO searchResultsDTO = tripletOccurrenceCountsManager
					.doTripletQuery(params.getTriplets(taxonomyManager), true,
							params.getSearchConstraints());

			
			
			if (searchResultsDTO.getResults().size() > 0) {
				recordCount = (Long) searchResultsDTO.getResults().get(0);
			}
			
			summaryMap = returnSummary(recordCount);
			
			results.put("headerMap", headerMap);
			results.put("parameterMap", parameterMap);
			results.put("summaryMap", summaryMap);
			return results;

		} catch (ServiceException se) {
			log
					.error("Unregistered data service error: "
							+ se.getMessage(), se);
			throw new GbifWebServiceException("Data service problems - " + se.getMessage());
		}
		catch (Exception se) {
			log.error("Data service error: " + se.getMessage(), se);
			throw new GbifWebServiceException("Data service problems - " + se.toString());
		}		
		
	}

	/**
	 * Gets an occurrence record
	 * 
	 * @param params
	 * @return a map with the occurrence and the count (1)
	 * @throws GbifWebServiceException
	 */
	public Map<String, Object> getOccurrenceRecord(OccurrenceParameters params)
			throws GbifWebServiceException {

		Map<String, String> headerMap=null;
		Map<String, String> parameterMap;
		Map<String, String> summaryMap=null;

		Map<String, Object> results = new HashMap<String, Object>();

		Map<DataProviderDTO, Map<DataResourceDTO, Set<Map<String,Object>>>> mapDTO = new HashMap<DataProviderDTO, Map<DataResourceDTO, Set<Map<String,Object>>>>();

		parameterMap = returnParameters(params.getParameterMap(null));

		try {
			
			// kml request type
			if (params.getFormat() == OccurrenceParameters.FORMAT_KML) {

				KmlOccurrenceRecordDTO dto = occurrenceManager.getKmlOccurrenceRecordFor(params.getKey());
				
				List<KmlOccurrenceRecordDTO> list = new ArrayList<KmlOccurrenceRecordDTO>();
				list.add(dto);
				
				//find the nextUrl
				String nextUrl = null;
				if (params.getMaxResults() < results.size()) {
					list = list.subList(0, params.getMaxResults());
					Integer next = new Integer(params.getStartIndex() + results.size());
					nextUrl = params.getUrl(null, next);
				}				

				String description = params.getStatements(getCitationTextForOccurrences(list, params.getPortalRoot(), true)) + (nextUrl == null ? "" :"<p><a href=\"" + nextUrl +"\">More records</a>");
				results.put("documentDescription", description);
				results.put("documentName", "GBIF Data Portal Occurrence Search");
				results.put("documentOpen", "true");
				results.put("hostUrl", params.getPortalRoot());
				results.put("results", returnKMLMap(params, list));
				results.put("count", 1);
			}
			else // non-kml request type
			{				
				ExtendedOccurrenceRecordDTO dto = occurrenceManager.getExtendedOccurrenceRecordFor(params.getKey());
				//sanitize illegal characters from the dto
				sanitizeDTO(dto);
								
				// switch (params.getFormat()) {
				// case OccurrenceParameters.FORMAT_KML:
				// doc = gbifMappingFactory.getKmlDocument(params, dto);
				// break;
				// default:
				
				if(dto == null)
					throw new GbifWebServiceException("Invalid occurrence key supplied");
				
				else  {
					List<ExtendedOccurrenceRecordDTO> set = new ArrayList<ExtendedOccurrenceRecordDTO>();
					set.add(dto);
					
					Map<String,Object> extraElementsEOR = new TreeMap<String,Object>();
					
					//extract extra elements for each of the EOR
					for(ExtendedOccurrenceRecordDTO eorDTO: set)
					{
						//Maps for Images, Typifications, Identifiers
						Set<Object> imageSet = new HashSet<Object>();
						Set<Object> typificationSet = new HashSet<Object>();
						Set<Object> identifierSet = new HashSet<Object>();
						
						//add the images
						List<ImageRecordDTO> images	= occurrenceManager.getImageRecordsForOccurrenceRecord(eorDTO.getKey());
						for(ImageRecordDTO imageDTO: images)
							imageSet.add(imageDTO.getUrl());
						
						//add the typifications
						List<TypificationRecordDTO> typifications = occurrenceManager.getTypificationRecordsForOccurrenceRecord(eorDTO.getKey());
						for(TypificationRecordDTO typificationRecordDTO: typifications)
							typificationSet.add(typificationRecordDTO.getTypeStatus());
						
						//add the identifiers
						List<IdentifierRecordDTO> identifiers = occurrenceManager.getIdentifierRecordsForOccurrenceRecord(eorDTO.getKey());
						for(IdentifierRecordDTO identifierRecordDTO: identifiers)
						{
							Map<String,String> identifierMap = new HashMap<String,String>();
							if(identifierRecordDTO.getIdentifierType().equals(IdentifierType.FIELDNUMBER))
								identifierMap.put("collectorsFieldNumber", identifierRecordDTO.getIdentifier());
							else if (identifierRecordDTO.getIdentifierType().equals(IdentifierType.COLLECTORNUMBER)) 
								identifierMap.put("collectorsBatchNumber", identifierRecordDTO.getIdentifier());
							
							identifierSet.add(identifierMap);
						}
						
						//add the higher taxa
						Set<Map<String,String>> higherTaxaInfo = new HashSet<Map<String,String>>(); //info for a higher taxon
						
						TaxonConceptDTO tcdto = taxonomyManager.getTaxonConceptFor(eorDTO.getTaxonConceptKey());
						StringBuffer formalTaxonomy = new StringBuffer();
						if (tcdto.getKingdom() != null) {
							higherTaxaInfo.add(addHigherTaxon(tcdto, tcdto.getKingdom()));
							addFormalTaxonomy(tcdto.getKingdom(), formalTaxonomy);
						}
						if (tcdto.getPhylumDivision() != null) {
							higherTaxaInfo.add(addHigherTaxon(tcdto, tcdto.getPhylumDivision()));
							addFormalTaxonomy(tcdto.getKingdom(), formalTaxonomy);
						}
						if (tcdto.getKlass() != null) {
							higherTaxaInfo.add(addHigherTaxon(tcdto, tcdto.getKlass()));
							addFormalTaxonomy(tcdto.getKingdom(), formalTaxonomy);
						}
						if (tcdto.getOrder() != null) {
							higherTaxaInfo.add(addHigherTaxon(tcdto, tcdto.getOrder()));
							addFormalTaxonomy(tcdto.getKingdom(), formalTaxonomy);
						}
						if (tcdto.getFamily() != null) {
							higherTaxaInfo.add(addHigherTaxon(tcdto, tcdto.getFamily()));
							addFormalTaxonomy(tcdto.getKingdom(), formalTaxonomy);
						}
						if (tcdto.getGenus() != null) {
							higherTaxaInfo.add(addHigherTaxon(tcdto, tcdto.getGenus()));
							addFormalTaxonomy(tcdto.getKingdom(), formalTaxonomy);
						}
						if (formalTaxonomy.length() > 0) {
							extraElementsEOR.put("formalTaxonomy", formalTaxonomy);
							extraElementsEOR.put("higherTaxa", higherTaxaInfo);
						}						
						
						//add this Sets as extra elements for the EOR
						extraElementsEOR.put("extendedOccurrenceRecordDTO", eorDTO);
						extraElementsEOR.put("imageSet", imageSet);
						extraElementsEOR.put("typificationSet", typificationSet);
						extraElementsEOR.put("identifierSet", identifierSet);
					}					
					
					
					headerMap = returnHeader(params, true, getCitationTextForExtendedOccurrences(set, params.getPortalRoot(), false));
					summaryMap = returnSummary(params, set, true);
					//a map of the resources
					
					Set<Map<String,Object>> extraElementsEORSet = new HashSet<Map<String,Object>>();
					extraElementsEORSet.add(extraElementsEOR);
					
					Map<DataResourceDTO, Set<Map<String,Object>>> mapResource = new HashMap<DataResourceDTO, Set<Map<String,Object>>>();
					mapResource.put(dataResourceManager.getDataResourceFor(dto.getDataResourceKey()), extraElementsEORSet);
					mapDTO.put(dataResourceManager.getDataProviderFor(dto.getDataProviderKey()), mapResource);
				}
				results.put("count", 1);
				results.put("headerMap", headerMap);
				results.put("parameterMap", parameterMap);
				results.put("summaryMap", summaryMap);
				results.put("results", mapDTO);
			}

			return results;

		} catch (ServiceException se) {
			log.error("Data service error: " + se.getMessage(), se);
			throw new GbifWebServiceException("Data service problems - " + se.getMessage());
		}
		catch (Exception se) {
			log.error("Data service error: " + se.getMessage(), se);
			throw new GbifWebServiceException("Data service problems - " + se.toString());
		}
	}
	
  private void sanitizeDTO(ExtendedOccurrenceRecordDTO dto) {
    if (dto == null) {
      return;
    }
    RawOccurrenceRecordDTO ror = dto.getRawOccurrenceRecordDTO();
    if (ror != null) {
      // include here any field that needs sanitation
      ror.setLocality(sanitizeField(ror.getLocality()));
      ror.setCollectorName(sanitizeField(ror.getCollectorName()));
    }
  }

  private String sanitizeField(String content) {
    if (content == null) {
      return null;
    }
    //replace all vertical tabs
    return content.replace('\u000B', ' ');
  }	
	
	public Map<String,String> addHigherTaxon(TaxonConceptDTO tcdto, String name)
	{
		Map<String,String> taxonInfo = new HashMap<String,String>();
		taxonInfo.put("nameComplete", name);
		taxonInfo.put("rankCode", tcdto.getRankCode());		
		
		return taxonInfo;
	}
	
	public void addFormalTaxonomy(String name, StringBuffer formalTaxonomy)
	{
		if (formalTaxonomy.length() > 0) {
			formalTaxonomy.append(", ");
		}
		formalTaxonomy.append(name);
	}

	/**
	 * Groups the results into resources and then providers
	 * 
	 * @param results
	 *            The occurrences to group
	 * @return The grouped occurrences
	 */
	@SuppressWarnings("unchecked")
	protected Map<String, Map<String, Set<ExtendedOccurrenceRecordDTO>>> groupByProviderResource(
			List<ExtendedOccurrenceRecordDTO> results) {
		// looks nasty but is occurrences grouped by resources grouped by
		// providers....
		
		//order the occurrences before traversing them
		Collections.sort(results,
						new Comparator() {
							public int compare(Object a, Object b) {
								ExtendedOccurrenceRecordDTO eor1 = (ExtendedOccurrenceRecordDTO) a;
								ExtendedOccurrenceRecordDTO eor2 = (ExtendedOccurrenceRecordDTO) b;
								String key1 = eor1.getCollectionCode();
								String key2 = eor2.getCollectionCode();
								return key1.compareTo(key2);
							}
						});
		
		Map<String, Map<String, Set<ExtendedOccurrenceRecordDTO>>> providers = new HashMap<String, Map<String, Set<ExtendedOccurrenceRecordDTO>>>();

		for (ExtendedOccurrenceRecordDTO eor : results) {
			// get the resources map or create new one
			Map<String, Set<ExtendedOccurrenceRecordDTO>> resources = null;

			if (providers.containsKey(eor.getDataProviderKey())) {
				resources = providers.get(eor.getDataProviderKey());
			} else {
				resources = new HashMap<String, Set<ExtendedOccurrenceRecordDTO>>();
				providers.put(eor.getDataProviderKey(), resources);
			}

			// get the occurrence set or create new one
			Set<ExtendedOccurrenceRecordDTO> occurrences = null;
			if (resources.containsKey(eor.getDataResourceKey())) {
				occurrences = resources.get(eor.getDataResourceKey());
			} else {

				// TreeSet for ordering all the EORs that are inserted
				occurrences = new TreeSet<ExtendedOccurrenceRecordDTO>(
						new Comparator() {
							public int compare(Object a, Object b) {
								ExtendedOccurrenceRecordDTO eor1 = (ExtendedOccurrenceRecordDTO) a;
								ExtendedOccurrenceRecordDTO eor2 = (ExtendedOccurrenceRecordDTO) b;
								String key1 = eor1.getKey();
								String key2 = eor2.getKey();
								return key1.compareTo(key2);
							}
						});

				resources.put(eor.getDataResourceKey(), occurrences);
			}

			// add the occurrence
			occurrences.add(eor);
		}

		return providers;
	}

	
	
	/**
	 * Find the occurrence record given a specific criteria
	 * 
	 * @param params
	 * @return map with the results and the count of occurrences
	 * @throws GbifWebServiceException
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> findOccurrenceRecords(OccurrenceParameters params)
			throws GbifWebServiceException {

		Map<String, String> headerMap;
		Map<String, String> parameterMap;
		Map<String, String> summaryMap;

		Map<String, Object> results = new HashMap<String, Object>();

		SearchResultsDTO searchResultsDTO = null;

		try {
			// kml request type
			if (params.getFormat() == OccurrenceParameters.FORMAT_KML) {
				searchResultsDTO = tripletOccurrenceKMLManager.doTripletQuery(
						params.getTriplets(taxonomyManager), true, params
								.getSearchConstraints());

				List<KmlOccurrenceRecordDTO> resultsAsKML = (List<KmlOccurrenceRecordDTO>) searchResultsDTO
				.getResults();
				
				//find the nextUrl
				String nextUrl = null;
				if (params.getMaxResults() < results.size()) {
					resultsAsKML = resultsAsKML.subList(0, params.getMaxResults());
					Integer next = new Integer(params.getStartIndex() + results.size());
					nextUrl = params.getUrl(null, next);
				}				

				results.put("documentName","GBIF Data Portal Occurrence Search");
				results.put("documentOpen", "true");
				String description = params.getStatements(getCitationTextForOccurrences(resultsAsKML, params.getPortalRoot(), true)) + (nextUrl == null ? "" :"<p><a href=\"" + nextUrl +"\">More records</a>");
				results.put("documentDescription", description);
				results.put("hostUrl", params.getPortalRoot());
				results.put("results", returnKMLMap(params, searchResultsDTO.getResults()));
				results.put("count", searchResultsDTO.getResults().size());
			} 
			else // non-kml request type
			{
				searchResultsDTO = tripletOccurrenceManager.doTripletQuery(
						params.getTriplets(taxonomyManager), true, params
								.getSearchConstraints());
				//headerMap = returnHeader(params, true);
				parameterMap = returnParameters(params.getParameterMap(null));

				List<ExtendedOccurrenceRecordDTO> resultsAsEOR = (List<ExtendedOccurrenceRecordDTO>) searchResultsDTO
						.getResults();
				
				headerMap = returnHeader(params, true, getCitationTextForExtendedOccurrences(resultsAsEOR, params.getPortalRoot(), false));
				summaryMap = returnSummary(params, resultsAsEOR, true);
				

				Map<String, Map<String, Set<ExtendedOccurrenceRecordDTO>>> groupedResults = groupByProviderResource(resultsAsEOR);

				
				//for each of the providers and resource's key, extract the corresponding DTO
				Map<DataProviderDTO, Map<DataResourceDTO, Set<Map<String, Object>>>> groupedResultsFull;
				groupedResultsFull = new HashMap<DataProviderDTO, Map<DataResourceDTO, Set<Map<String, Object>>>>();
				Map<DataResourceDTO, Set<Map<String, Object>>> dataResourcesFull = new HashMap<DataResourceDTO, Set<Map<String, Object>>>();
				
				for(String providerKey: groupedResults.keySet())
				{
					DataProviderDTO dataProviderDTO = dataResourceManager.getDataProviderFor(providerKey);
					
					dataResourcesFull = new HashMap<DataResourceDTO, Set<Map<String, Object>>>();
					
					Map<String,Object> extraElementsEOR = new HashMap<String,Object>();
					
					for(String resourceKey: groupedResults.get(providerKey).keySet())
					{
						Set<Map<String,Object>> extraElementsEORSet = new HashSet<Map<String,Object>>();
					
							//extract extra elements for each of the EOR
							for(ExtendedOccurrenceRecordDTO eorDTO: groupedResults.get(providerKey).get(resourceKey))
							{
								extraElementsEOR = new HashMap<String,Object>();
								
								if(params.getFormat() == OccurrenceParameters.FORMAT_DARWIN)
								{								
																
									//Maps for Images, Typifications, Identifiers
									Set<Object> imageSet = new HashSet<Object>();
									Set<Object> typificationSet = new HashSet<Object>();
									Set<Object> identifierSet = new HashSet<Object>();
									
									//add the images
									List<ImageRecordDTO> images	= occurrenceManager.getImageRecordsForOccurrenceRecord(eorDTO.getKey());
									for(ImageRecordDTO imageDTO: images)
										imageSet.add(imageDTO.getUrl());
									
									//add the typifications
									List<TypificationRecordDTO> typifications = occurrenceManager.getTypificationRecordsForOccurrenceRecord(eorDTO.getKey());
									for(TypificationRecordDTO typificationRecordDTO: typifications)
										typificationSet.add(typificationRecordDTO.getTypeStatus());
									
									//add the identifiers
									List<IdentifierRecordDTO> identifiers = occurrenceManager.getIdentifierRecordsForOccurrenceRecord(eorDTO.getKey());
									for(IdentifierRecordDTO identifierRecordDTO: identifiers)
									{
										Map<String,String> identifierMap = new HashMap<String,String>();
										if(identifierRecordDTO.getIdentifierType().equals(IdentifierType.FIELDNUMBER))
											identifierMap.put("collectorsFieldNumber", identifierRecordDTO.getIdentifier());
										else if (identifierRecordDTO.getIdentifierType().equals(IdentifierType.COLLECTORNUMBER)) 
											identifierMap.put("collectorsBatchNumber", identifierRecordDTO.getIdentifier());
										
										identifierSet.add(identifierMap);
									}
									
									//add the higher taxa
									Set<Map<String,String>> higherTaxaInfo = new HashSet<Map<String,String>>(); //info for a higher taxon
									
									TaxonConceptDTO tcdto = taxonomyManager.getTaxonConceptFor(eorDTO.getTaxonConceptKey());
									StringBuffer formalTaxonomy = new StringBuffer();
									if (tcdto.getKingdom() != null) {
										higherTaxaInfo.add(addHigherTaxon(tcdto, tcdto.getKingdom()));
										addFormalTaxonomy(tcdto.getKingdom(), formalTaxonomy);
									}
									if (tcdto.getPhylumDivision() != null) {
										higherTaxaInfo.add(addHigherTaxon(tcdto, tcdto.getPhylumDivision()));
										addFormalTaxonomy(tcdto.getPhylumDivision(), formalTaxonomy);
									}
									if (tcdto.getKlass() != null) {
										higherTaxaInfo.add(addHigherTaxon(tcdto, tcdto.getKlass()));
										addFormalTaxonomy(tcdto.getKlass(), formalTaxonomy);
									}
									if (tcdto.getOrder() != null) {
										higherTaxaInfo.add(addHigherTaxon(tcdto, tcdto.getOrder()));
										addFormalTaxonomy(tcdto.getOrder(), formalTaxonomy);
									}
									if (tcdto.getFamily() != null) {
										higherTaxaInfo.add(addHigherTaxon(tcdto, tcdto.getFamily()));
										addFormalTaxonomy(tcdto.getFamily(), formalTaxonomy);
									}
									if (tcdto.getGenus() != null) {
										higherTaxaInfo.add(addHigherTaxon(tcdto, tcdto.getGenus()));
										addFormalTaxonomy(tcdto.getGenus(), formalTaxonomy);
									}
									if (formalTaxonomy.length() > 0) {
										extraElementsEOR.put("formalTaxonomy", formalTaxonomy);
										extraElementsEOR.put("higherTaxa", higherTaxaInfo);
									}		
									extraElementsEOR.put("taxonConceptDTO", tcdto);
									extraElementsEOR.put("imageSet", imageSet);
									extraElementsEOR.put("typificationSet", typificationSet);
									extraElementsEOR.put("identifierSet", identifierSet);								
								}
								
								//add this Sets as extra elements for the EOR
								extraElementsEOR.put("extendedOccurrenceRecordDTO", eorDTO);

								
								extraElementsEORSet.add(extraElementsEOR);
							}
						
						
						DataResourceDTO dataResourceDTO = dataResourceManager.getDataResourceFor(resourceKey);
						dataResourcesFull.put(dataResourceDTO, extraElementsEORSet);
					}
					groupedResultsFull.put(dataProviderDTO, dataResourcesFull);
				}
				
				
				
				
				results.put("headerMap", headerMap);
				results.put("parameterMap", parameterMap);
				results.put("summaryMap", summaryMap);
				results.put("results", groupedResultsFull);
				results.put("count", searchResultsDTO.getResults().size());
			}

			return results;
		}

		catch (ServiceException se) {
			log
					.error("Unregistered data service error: "
							+ se.getMessage(), se);
			throw new GbifWebServiceException("Data service problems - " + se.getMessage());
		}
		catch (Exception se) {
			log.error("Data service error: " + se.getMessage(), se);
			throw new GbifWebServiceException("Data service problems - " + se.toString());
		}		
		
	}
	
	protected Map<String,String> returnHeader(Parameters parameters, boolean shortHelp, String citationText)
	{		
		Map<String,String> headerMap = new HashMap<String,String>();
		
		String stylesheetUrl = (parameters.getStylesheet() == null) ? parameters.getUrl(Action.STYLESHEET, null) : parameters.getStylesheet();
		
		headerMap.put("stylesheet", stylesheetUrl);
		
		headerMap.put("statements", parameters.getStatements(citationText));
		if(shortHelp)
			headerMap.put("help", parameters.getShortHelpText());
		else
			headerMap.put("help", parameters.getLongHelpText());
		headerMap.put("request", parameters.getRequestTypeName());
		
		headerMap.put("schemaLocation", parameters.getSchemaLocation());
		
		return headerMap;
	}

	/**
	 * Creates a Map which stores attributes used for rendering the Velocity KML
	 * Template
	 * 
	 * @param params
	 * @param srDTO
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Map<TaxonConceptDTO, Collection> returnKMLMap(OccurrenceParameters params,
			List<KmlOccurrenceRecordDTO> results) {
		// create a folder for each of the different taxons (inside each folder,
		// there could be a group of placemarks)
		
		Map<TaxonConceptDTO, Collection> folderMap = new HashMap<TaxonConceptDTO, Collection>();
		

		//order the occurrences before traversing them
		Collections.sort(results,
						new Comparator() {
							public int compare(Object a, Object b) {
								KmlOccurrenceRecordDTO kordto1 = (KmlOccurrenceRecordDTO) a;
								KmlOccurrenceRecordDTO kordto2 = (KmlOccurrenceRecordDTO) b;
								String key1 = kordto1.getNubTaxonConceptKey();
								String key2 = kordto2.getNubTaxonConceptKey();
								return key1.compareTo(key2);
							}
						});	
		
		try {
			// loop over records getting cache ofnub ids first
			Map<String, TaxonConceptDTO> nubId2NameMap = new HashMap<String, TaxonConceptDTO>();

			for (KmlOccurrenceRecordDTO dto : results) {
				
				String key="";
				TaxonConceptDTO taxonConceptDTO = new TaxonConceptDTO();
	
				if(dto.getNubTaxonConceptKey()==null)
				{
					taxonConceptDTO.setKey(dto.getTaxonConceptKey());
					taxonConceptDTO.setTaxonName(dto.getTaxonName());
				}
				else
				{
					taxonConceptDTO.setKey(dto.getNubTaxonConceptKey());
					taxonConceptDTO.setTaxonName(dto.getNubTaxonConceptName());
				}
				
				if(!nubId2NameMap.containsKey(taxonConceptDTO.getKey()))
						nubId2NameMap.put(taxonConceptDTO.getKey(), taxonConceptDTO);
				
			}

			// now group the occurrences by nub concept key
			Map<String, List<KmlOccurrenceRecordDTO>> occurrencesGroupedByNubConceptKey = new HashMap<String, List<KmlOccurrenceRecordDTO>>();
			
			for (KmlOccurrenceRecordDTO dto : results) {
				String name = null;
				
				String nubKey="";
				
				if(dto.getNubTaxonConceptKey()!=null)
					nubKey=dto.getNubTaxonConceptKey();
				else
					nubKey=dto.getTaxonConceptKey();
				
				if(occurrencesGroupedByNubConceptKey.containsKey(dto.getNubTaxonConceptKey()))  //a list appropriate for the EOR already exists
					occurrencesGroupedByNubConceptKey.get(dto.getNubTaxonConceptKey()).add(dto);
				else  //create the entry for the KOR list
				{	
					List<KmlOccurrenceRecordDTO> occurrenceNewList = new LinkedList<KmlOccurrenceRecordDTO>();
					occurrenceNewList.add(dto);
					occurrencesGroupedByNubConceptKey.put(dto.getNubTaxonConceptKey(), occurrenceNewList);
				}
				
			}
			
			for (String nubTaxonConceptKey : occurrencesGroupedByNubConceptKey.keySet()) {
				
				List<KmlOccurrenceRecordDTO> occurrences = occurrencesGroupedByNubConceptKey.get(nubTaxonConceptKey);

				Map<String, Integer> countMap = new HashMap<String, Integer>();

				// group of placemarks identified by the location key
				// (locationKey = latitude+longitude)
				Map<String, Map<String, String>> placemarkGroup = new HashMap<String, Map<String, String>>();

				// build the placemark group for this taxon
				for (KmlOccurrenceRecordDTO dto : occurrences) {
					String url = params.getPortalRoot() + "/occurrences/"
							+ dto.getKey();
					String locationKey = dto.getLatitude().toString() + ":"
							+ dto.getLongitude().toString();
					String description = "<p><img src=\""
							+ params.getPortalRoot()
							+ "/images/gbifSmall.gif\"/></p><p><b>Data retrieved from <a href=\""
							+ params.getPortalRoot()
							+ "\">GBIF Data Portal</a></b><p><table><tr><th>Data Provider</th><th>Institution Code</th><th>Collection Code</th><th>Catalogue Number</th><th>Date</th><th>Portal URL</th></tr>";
					String coordinates = dto.getLongitude().toString() + ","
							+ dto.getLatitude().toString() + ",0";
					
					StringBuffer sb = new StringBuffer();
					sb.append("<tr><td>");
					sb.append(dto.getDataProviderName());
					sb.append("</td><td>");
					sb.append(dto.getInstitutionCode());
					sb.append("</td><td>");
					sb.append(dto.getCollectionCode());
					sb.append("</td><td>");
					sb.append(dto.getCatalogueNumber());
					sb.append("</td><td>");
					if (dto.getOccurrenceDate() != null) {
						sb.append(ymdFormat.format(dto.getOccurrenceDate()));
					}
					sb.append("</td><td><a href=\"");
					sb.append(url);
					sb.append("\">");
					sb.append(url);
					sb.append("</a></td></tr>");

					// check to see if the placemark already exists, according
					// to its location (location = latitude:longitude)
					Map<String, String> placemark = placemarkGroup
							.get(locationKey);

					if (placemark == null) { // placemark doesn't exist, create
												// a new one
						Map<String, String> newPlacemarkElement = new HashMap<String, String>();
						newPlacemarkElement.put("name", dto.getTaxonName());
						newPlacemarkElement.put("description",
								(description + sb.toString()));
						newPlacemarkElement.put("coordinates", coordinates);
						countMap.put(locationKey, new Integer(1));
						
						//kmlIconUrl
						String kmlIconUrl = params.getKmlIconUrl();
						if (kmlIconUrl != null)
							styleCount=1;
						else
							styleCount=kmlIcons.size();
						
						newPlacemarkElement.put("styleUrl", "#gbifIcon" + ((Integer.parseInt(dto.getNubTaxonConceptKey()) % styleCount) + 1));
						// insert the new placemark into the group of placemarks belonging to this taxon
						placemarkGroup.put(locationKey, newPlacemarkElement); 

					} else { // it exists, just add the new record's description
								// into the previous description for that placemark
						countMap.put(locationKey, new Integer(countMap.get(locationKey) + 1));
						placemark.put("description", placemark.get("description") + sb.toString());
						// insert the new placemark into the group of placemarks belonging to this taxon
						placemarkGroup.put(locationKey, placemark);
					}

				}

				// iterate over all the placemarks for this nub concept and just
				// change the name tag if there are multiple occurrences
				// associated to
				// to this placemark.
				for (String locationKey : placemarkGroup.keySet()) {
					Map<String, String> placemark = placemarkGroup
							.get(locationKey);
					Integer count = countMap.get(locationKey);
					if (count > 1) {
						placemark.put("name", placemark.get("name") + " ("
								+ count + " records)");
					}
					placemark.put("description", placemark.get("description")
							+ "</table></p>");
				}

				// build a collection of all the placemarks associated to this
				// nub taxon
				Collection<Map<String, String>> placemarkValues = (Collection<Map<String, String>>) placemarkGroup
						.values();

				// a folder is created per each nub taxon
				folderMap.put(nubId2NameMap.get(nubTaxonConceptKey), placemarkValues);
			}

		} catch (Exception e) {
			log.debug("Action error: " + e.toString());
		}
		
		return folderMap;
	}
	
	// TODO: All the citations methods must be moved if possible to superclass
	// Action
	public String getCitationTextForOccurrences(
			List<KmlOccurrenceRecordDTO> records, String urlBase,
			boolean asHtml) {
		Map<String, DataResourceDTO> resourceMap = new HashMap<String, DataResourceDTO>();

		for (KmlOccurrenceRecordDTO dto : records) {
			if (dto!=null) {
				try {
					if (!resourceMap.containsKey(dto.getDataResourceKey())) {
							DataResourceDTO dataResourceDTO = new DataResourceDTO();
							dataResourceDTO.setCitableAgent(dto.getDataResourceCitableAgent());
							dataResourceDTO.setDataProviderName(dto.getDataProviderName());
							dataResourceDTO.setName(dto.getDataResourceName());
							dataResourceDTO.setKey(dto.getDataResourceKey());
							
							resourceMap.put(dto.getDataResourceKey(),dataResourceDTO);
							
					}
				} catch (Exception e) {
					log.error("Could not retrieve data resource " + dto.getDataResourceKey(), e);
				}
			}
		}

		List<DataResourceDTO> resources = new ArrayList<DataResourceDTO>();
		resources.addAll(resourceMap.values());

		return getCitationText(resources, urlBase, asHtml);
	}
	
	// TODO: All the citations methods must be moved if possible to superclass
	// Action
	public String getCitationTextForExtendedOccurrences(
			List<ExtendedOccurrenceRecordDTO> records, String urlBase,
			boolean asHtml) {
		Map<String, DataResourceDTO> resourceMap = new HashMap<String, DataResourceDTO>();

		for (ExtendedOccurrenceRecordDTO dto : records) {
			if (!resourceMap.containsKey(dto.getDataResourceKey())) {
				try {
					resourceMap.put(dto.getDataResourceKey(),
							dataResourceManager.getDataResourceFor(dto
									.getDataResourceKey()));
				} catch (Exception e) {
					log.error("Could not retrieve data resource "
							+ dto.getDataResourceKey(), e);
				}
			}
		}

		List<DataResourceDTO> resources = new ArrayList<DataResourceDTO>();
		resources.addAll(resourceMap.values());

		return getCitationText(resources, urlBase, asHtml);
	}	

	// TODO
	public String getCitationText(List<DataResourceDTO> resources,
			String urlBase, boolean asHtml) {
		StringBuffer sb = new StringBuffer();

		Collections.sort(resources, new Comparator<DataResourceDTO>() {
			public int compare(DataResourceDTO o1, DataResourceDTO o2) {
				String name1 = o1.getCitableAgent() == null ? o1
						.getDataProviderName() : o1.getCitableAgent();
				String name2 = o2.getCitableAgent() == null ? o2
						.getDataProviderName() : o2.getCitableAgent();

				int cp = name1.compareTo(name2);
				if (cp < 0) {
					return -1;
				} else if (cp > 0) {
					return 1;
				} else {
					cp = o1.getName().compareTo(o2.getName());
					if (cp < 0) {
						return -1;
					} else if (cp > 0) {
						return 1;
					} else {
						return 0;
					}
				}
			}
		});

		if (asHtml) {
			sb
					.append("<br/><br/><b>Please cite these data as follows:</b><br/><br/>");
		} else {
			sb.append("\nPlease cite these data as follows:\n\n");
		}

		for (DataResourceDTO resource : resources) {
			getCitationText(sb, resource, urlBase, getDateString(), asHtml);
			if (asHtml) {
				sb.append("<br/><br/>");
			} else {
				sb.append("\n");
			}
		}

		return sb.toString();
	}

	// TODO
	private String getDateString() {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date(System.currentTimeMillis()));

		StringBuffer dateBuffer = new StringBuffer();
		dateBuffer.append(calendar.get(Calendar.YEAR));
		dateBuffer.append("-");
		// NB add one to zero-based month
		if (calendar.get(Calendar.MONTH) < 9) {
			dateBuffer.append("0");
		}
		dateBuffer.append(calendar.get(Calendar.MONTH) + 1);
		dateBuffer.append("-");
		if (calendar.get(Calendar.DAY_OF_MONTH) < 10) {
			dateBuffer.append("0");
		}
		dateBuffer.append(calendar.get(Calendar.DAY_OF_MONTH));

		return dateBuffer.toString();
	}

	// TODO
	private void getCitationText(StringBuffer sb, DataResourceDTO resource,
			String urlBase, String dateString, boolean asHtml) {
		sb.append(resource.getCitableAgent() == null ? resource
				.getDataProviderName() : resource.getCitableAgent());
		sb.append(", ");
		sb.append(resource.getName());
		sb.append(" (accessed through GBIF data portal, ");
		if (asHtml) {
			sb.append("<a href=\"");
			sb.append(urlBase);
			sb.append("/datasets/resource/");
			sb.append(resource.getKey());
			sb.append("\">");
		}
		sb.append(urlBase);
		sb.append("/datasets/resource/");
		sb.append(resource.getKey());
		if (asHtml) {
			sb.append("</a>");
		}
		sb.append(", ");
		sb.append(dateString);
		sb.append(")");
	}

	public void setDataResourceManager(DataResourceManager dataResourceManager) {
		this.dataResourceManager = dataResourceManager;
	}

	/**
	 * @return the occurrenceManager
	 */
	public OccurrenceManager getOccurrenceManager() {
		return occurrenceManager;
	}

	/**
	 * @param occurrenceManager
	 *            the occurrenceManager to set
	 */
	public void setOccurrenceManager(OccurrenceManager occurrenceManager) {
		this.occurrenceManager = occurrenceManager;
	}

	/**
	 * @return the tripletOccurrenceManager
	 */
	public TripletQueryManager getTripletOccurrenceManager() {
		return tripletOccurrenceManager;
	}

	/**
	 * @param tripletOccurrenceManager
	 *            the tripletOccurrenceManager to set
	 */
	public void setTripletOccurrenceManager(
			TripletQueryManager tripletOccurrenceManager) {
		this.tripletOccurrenceManager = tripletOccurrenceManager;
	}

	/**
	 * @return the taxonomyManager
	 */
	public TaxonomyManager getTaxonomyManager() {
		return taxonomyManager;
	}

	/**
	 * @param taxonomyManager
	 *            the taxonomyManager to set
	 */
	public void setTaxonomyManager(TaxonomyManager taxonomyManager) {
		this.taxonomyManager = taxonomyManager;
	}

	/**
	 * @return the tripletOccurrenceCountsManager
	 */
	public TripletQueryManager getTripletOccurrenceCountsManager() {
		return tripletOccurrenceCountsManager;
	}

	/**
	 * @param tripletOccurrenceCountsManager
	 *            the tripletOccurrenceCountsManager to set
	 */
	public void setTripletOccurrenceCountsManager(
			TripletQueryManager tripletOccurrenceCountsManager) {
		this.tripletOccurrenceCountsManager = tripletOccurrenceCountsManager;
	}

	/**
	 * @return the kmlIcons
	 */
	public List<String> getKmlIcons() {
		return kmlIcons;
	}

	/**
	 * @param kmlIcons the kmlIcons to set
	 */
	public void setKmlIcons(List<String> kmlIcons) {
		this.kmlIcons = kmlIcons;
	}

	/**
	 * @return the tripletOccurrenceKMLManager
	 */
	public TripletQueryManager getTripletOccurrenceKMLManager() {
		return tripletOccurrenceKMLManager;
	}

	/**
	 * @param tripletOccurrenceKMLManager the tripletOccurrenceKMLManager to set
	 */
	public void setTripletOccurrenceKMLManager(
			TripletQueryManager tripletOccurrenceKMLManager) {
		this.tripletOccurrenceKMLManager = tripletOccurrenceKMLManager;
	}
	
	

}