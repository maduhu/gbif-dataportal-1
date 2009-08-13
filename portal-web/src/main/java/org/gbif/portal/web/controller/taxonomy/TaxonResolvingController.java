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
package org.gbif.portal.web.controller.taxonomy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.gbif.portal.dto.CountDTO;
import org.gbif.portal.dto.SearchResultsDTO;
import org.gbif.portal.dto.resources.DataProviderDTO;
import org.gbif.portal.dto.taxonomy.BriefTaxonConceptDTO;
import org.gbif.portal.dto.taxonomy.CommonNameDTO;
import org.gbif.portal.dto.taxonomy.RelationshipAssertionDTO;
import org.gbif.portal.dto.taxonomy.TaxonConceptDTO;
import org.gbif.portal.dto.util.BoundingBoxDTO;
import org.gbif.portal.dto.util.EntityType;
import org.gbif.portal.dto.util.SearchConstraints;
import org.gbif.portal.dto.util.TaxonRankType;
import org.gbif.portal.dto.util.TimePeriodDTO;
import org.gbif.portal.service.DataResourceManager;
import org.gbif.portal.service.GeospatialManager;
import org.gbif.portal.service.OccurrenceManager;
import org.gbif.portal.service.ServiceException;
import org.gbif.portal.service.TaxonomyManager;
import org.gbif.portal.util.log.GbifLogMessage;
import org.gbif.portal.util.log.LogEvent;
import org.gbif.portal.web.content.filter.FilterContentProvider;
import org.gbif.portal.web.content.map.MapContentProvider;
import org.gbif.portal.web.controller.RestController;
import org.gbif.portal.web.controller.taxonomy.bean.PartnerConceptBean;
import org.gbif.portal.web.filter.CriteriaDTO;
import org.gbif.portal.web.util.TaxonConceptUtils;
import org.gbif.portal.web.util.UserUtils;
import org.springframework.context.MessageSource;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.support.RequestContextUtils;
import org.springframework.web.servlet.view.RedirectView;
/**
 * A controller that resolves an id to a Taxon page.
 * It can do this by a number of means. GBIF id, Catalogue of Life Id or canonical and rank of a concept.
 * 
 * @author dmartin
 */
public class TaxonResolvingController extends RestController {
	
	/** Taxonomy Manager for taxonomic service layer methods */
	protected TaxonomyManager taxonomyManager;
	/** Data Resource Manager service layer methods */
	protected DataResourceManager dataResourceManager;
	//service managers
	protected OccurrenceManager occurrenceManager;
	protected GeospatialManager geospatialManager;

	//content providers
	protected FilterContentProvider filterContentProvider;
	protected MapContentProvider mapContentProvider;	
	
	protected TaxonConceptUtils taxonConceptUtils;
	
	/** For name searches, the maximum number to bring back */
	protected int maxNameResolvingResults = 1000;

	protected int maxCommonNamesToDisplay = 1000;
	
	/** Whether to allow unconfirmed names for name resolving */
	protected boolean allowUnconfirmedNames = true;		
	
	/** The threshold governing the displaying of partner concept resource name */
	protected int taxonomicPriorityThreshold = 20;	
	
	/** Request Key properties */
	/** Id request property */
	protected String idRequestKey="id";
	/** rank request property */	
	protected String rankRequestKey="rank";
	/** Common Name request property */
	protected String commonNameRequestKey="commonName";
	/** Common Name request keyword property */
	protected String commonNameRequestKeyword="commonName";
	/** Common Name request keyword property */
	protected String dataResourceRequestKeyword="resource";
	/** Common Name request keyword property */
	protected String dataProviderRequestKeyword="provider";
	
	/** View request property */
	protected String viewRequestKey="view";	

	/** Concept Key Model Key */
	protected String conceptKeyModelKey="conceptKey";
	/** Taxon Concept Model Key */
	protected String taxonConceptModelKey = "taxonConcept";
	/** The Common Name Model Key */
	protected String commonNameModelKey = "commonName";
	
	/** The Name Resolving View name */
	protected String nameResolveViewName="taxonNameResolveView";
	/** The Name to be resolved model key */
	protected String nameModelKey="name";	
	/** The Matches to be resolved model key */	
	protected String nameMatchesModelKey="nameMatches";
	
	/** The maximum number of images to retrieve for a taxon */
	protected int maxImagesToShow = 25;

	/** Utilities for user related actions */
	protected UserUtils userUtils;	

	/** The i18n message source */
	protected MessageSource messageSource;
	
	/** Whether to render charts or not */
	protected boolean showCharts = false;
	
	/** Whether or not to display unconfirmed names */
	protected boolean allowUnconfirmed = true;
	
	/**
	 * This method will resolve the entity by various means.
	 * 
	 * @see org.gbif.portal.web.controller.RestController#handleRequest(java.util.List, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@SuppressWarnings("unchecked")
	public ModelAndView handleRequest(Map<String, String> propertiesMap, HttpServletRequest request, HttpServletResponse response) throws Exception {

		//if no properties, can't resolve
		if(propertiesMap.isEmpty())
			return redirectToDefaultView();
		
		//get the key
		String conceptIdentifier = propertiesMap.get(idRequestKey);
		//adds the target entity to the request
		BriefTaxonConceptDTO taxonConceptDTO = null;
		
		//resource key
		String dataResourceKey = propertiesMap.get(dataResourceRequestKeyword);
		//provider key
		String dataProviderKey = propertiesMap.get(dataProviderRequestKeyword);
		
		if(dataResourceKey!=null || dataProviderKey!=null){
			logger.debug("Resolving remote identifier");			
			//try resolving the id to a data
			taxonConceptDTO = taxonomyManager.getTaxonConceptForRemoteId(dataProviderKey, dataResourceKey, conceptIdentifier);
			if(taxonConceptDTO!=null && !taxonConceptDTO.getIsNubConcept() && taxonConceptDTO.getPartnerConceptKey()!=null){
				taxonConceptDTO = taxonomyManager.getTaxonConceptFor(taxonConceptDTO.getPartnerConceptKey());
			}			
		} else if(taxonomyManager.isValidTaxonConceptKey(conceptIdentifier)){
			logger.debug("Recognised identifier");
			taxonConceptDTO = taxonomyManager.getTaxonConceptFor(conceptIdentifier,  RequestContextUtils.getLocale(request).getLanguage());
			//redirect to nub concept if this is not a nub concept and if available
			if(taxonConceptDTO !=null && !taxonConceptDTO.getIsNubConcept() && taxonConceptDTO.getPartnerConceptKey()!=null){
				logger.debug("Redirecting to nub concept page");
				return new ModelAndView(new RedirectView(request.getContextPath()+"/"+urlRoot+"/"+taxonConceptDTO.getPartnerConceptKey()));
			}
		} else {
			logger.debug("Searching for remote ids");
			List<TaxonConceptDTO> taxonConceptDTOs = taxonomyManager.getTaxonConceptForRemoteId(conceptIdentifier);
			if(taxonConceptDTOs.size()==1) {
				taxonConceptDTO = taxonConceptDTOs.get(0);
				//redirect to the nub concept if available
				if(!taxonConceptDTO.getIsNubConcept() && taxonConceptDTO.getPartnerConceptKey()!=null){
					taxonConceptDTO = taxonomyManager.getTaxonConceptFor(taxonConceptDTO.getPartnerConceptKey());
				}
			} else if (taxonConceptDTOs.size()>1) {
				logger.debug("Multiple matching for remote id - forwarding to resolving view");
				//direct to name resolving view
				return createNameResolvingView(conceptIdentifier, taxonConceptDTOs);
			}
		}
		
		//if we have resolved this taxon, create view for it
		if(taxonConceptDTO!=null){
			request.setAttribute(conceptKeyModelKey, conceptIdentifier);
			request.setAttribute(taxonConceptModelKey, taxonConceptDTO);
			if(propertiesMap.get(commonNameRequestKey)!=null){
				request.setAttribute(commonNameModelKey, propertiesMap.get(commonNameRequestKey));
			}
			logger.debug("Creating full taxon concept view");
			return populateView(taxonConceptDTO.getKey(), propertiesMap, request, response);			
		}
		
		//match rank and concept by name 
		String rank = propertiesMap.get(rankRequestKey);
		//find a concept called this - first using the default taxonomy
		DataProviderDTO  nubDataProvider = dataResourceManager.getNubDataProvider();
		SearchConstraints searchConstraints = new SearchConstraints(0, maxNameResolvingResults);
		SearchResultsDTO searchResultsDTO = null;
		if(nubDataProvider!=null)
			searchResultsDTO = taxonomyManager.findTaxonConcepts(conceptIdentifier, false, rank, nubDataProvider.getKey(), null, null, null, RequestContextUtils.getLocale(request).getLanguage(), null, allowUnconfirmedNames, false, searchConstraints);
		//if no nub provider concept, look for another
		if(searchResultsDTO==null || searchResultsDTO.getResults().size()==0){
			//find a concept called this - first using any taxonomy		
			searchResultsDTO = taxonomyManager.findTaxonConcepts(conceptIdentifier, false, rank, null, null, null, null, RequestContextUtils.getLocale(request).getLanguage(), null, allowUnconfirmedNames, false, searchConstraints);
		}
		List<TaxonConceptDTO> taxonConcepts = (List<TaxonConceptDTO>) searchResultsDTO.getResults();			
		//if one concept returned, use this concept
		if(taxonConcepts.size()==1){
			taxonConceptDTO = (BriefTaxonConceptDTO) taxonConcepts.get(0);
			String conceptKey = taxonConcepts.get(0).getKey();
			request.setAttribute(conceptKeyModelKey, conceptKey);
			request.setAttribute(taxonConceptModelKey, taxonConceptDTO);
			return populateView(taxonConceptDTO.getKey(), propertiesMap, request, response);	
		} else {
			//redirect to name resolving page
			return createNameResolvingView(conceptIdentifier, taxonConcepts);
		}
	}

	/**
	 * Create a name resolving view.
	 * 
	 * @param nameOrId
	 * @param matchingConcepts
	 * @return
	 */
	private ModelAndView createNameResolvingView(String conceptIdentifier, List<TaxonConceptDTO> matchingConcepts){
		ModelAndView mav = new ModelAndView(nameResolveViewName);
		mav.addObject(nameModelKey, conceptIdentifier);
		mav.addObject(nameMatchesModelKey, matchingConcepts);
		return mav;
	}
	
	/**
	 * Create a detailed view of the concept.
	 * 
	 * @param taxonConceptDTO
	 * @param request
	 * @param response
	 * @return
	 */
	protected ModelAndView populateView(String taxonConceptKey, Map<String, String> propertiesMap, HttpServletRequest request, HttpServletResponse response) throws Exception{
		
		String msg = messageSource.getMessage("create", null, Locale.KOREAN);
		logger.debug(msg);
		logger.debug(new String(msg.getBytes("ISO-8859-1"),"UTF-8"));
		
		ModelAndView mav = resolveAndCreateView(propertiesMap, request, true);
		logger.debug("Retrieving full taxon concept dto");
		TaxonConceptDTO taxonConcept = taxonomyManager.getTaxonConceptFor(taxonConceptKey);
		logger.debug("Adding names");
		addNames(taxonConcept, request, response);
		logger.debug("Adding actions");
		addActions(taxonConcept, request, response);
		logger.debug("Adding warnings");
		addWarnings(taxonConcept, request, response);
		logger.debug("Adding occurrences");
		addOccurrences(taxonConcept, request, response);
		logger.debug("Adding images");
		addImages(taxonConcept, request, response);
		logger.debug("Adding partner concepts");
		addPartnerConcepts(taxonConcept, request, response);
		logger.debug("Adding typifications");
		addTypifications(taxonConcept, request, response);
		logger.debug("Adding keywords");
		addKeywords(taxonConcept, request, response);		
		logger.debug("logging usage");
		logUsage(request, taxonConcept);
		return mav;
	}	
	
	/**
	 * Log the viewing of the taxon page.
	 * 
	 * @param request
	 * @param taxonConcept
	 */
	private void logUsage(HttpServletRequest request, TaxonConceptDTO taxonConcept) {
		GbifLogMessage gbifMessage = new GbifLogMessage();
		gbifMessage.setEvent(LogEvent.USAGE_TAXON_VIEW);
		gbifMessage.setTaxonConceptId(parseKey(taxonConcept.getKey()));
		gbifMessage.setDataProviderId(parseKey(taxonConcept.getDataProviderKey()));
		gbifMessage.setDataResourceId(parseKey(taxonConcept.getDataResourceKey()));
		gbifMessage.setTimestamp(new Date());
		gbifMessage.setRestricted(false);
		gbifMessage.setMessage("Taxon page viewed");
		userUtils.logUsage(logger, gbifMessage, request);
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
	 * Adds a comma separated list to the keywords.
	 * 
	 * @param taxonConcept
	 * @param request
	 * @param response
	 */
	@SuppressWarnings("unchecked")
	protected void addKeywords(TaxonConceptDTO taxonConcept, HttpServletRequest request, HttpServletResponse response) {
		StringBuffer sb = new StringBuffer();
		sb.append(taxonConcept.getTaxonName());
		
		List<PartnerConceptBean> partners = (List<PartnerConceptBean>) request.getAttribute("partners");		
		if(partners!=null && !partners.isEmpty()){
			for(PartnerConceptBean partner: partners){
				//add synonyms
				List<RelationshipAssertionDTO> ras = partner.getSynonyms();
				for(RelationshipAssertionDTO ra :ras){
					sb.append(',');
					sb.append(ra.getFromTaxonName());					
				}
				//add common names
				List<Map<String, List<CommonNameDTO>>> commonNamesMapList = (List) partner.getCommonNames();
				for(Map<String, List<CommonNameDTO>> commonNamesMap: commonNamesMapList){
					for(String language: commonNamesMap.keySet()){
						List<CommonNameDTO> commonNameList = commonNamesMap.get(language);
						for(CommonNameDTO commonName: commonNameList){
							sb.append(',');
							sb.append(commonName.getName());
						}
					}
				}
			}
		}
		request.setAttribute("keywordList", sb.toString());
	}

	/**
	 * Adds content for Names pane - this caters for the content in taxonomy/drilldown/names.jsp
	 * 
	 * @param componentContext
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	public void addNames(TaxonConceptDTO taxonConcept, HttpServletRequest request, HttpServletResponse response)  throws Exception{
		//retrieve full classification
		List<BriefTaxonConceptDTO> fullClassification = taxonomyManager.getClassificationFor(taxonConcept.getKey(), false, null, allowUnconfirmed);
		request.setAttribute("concepts", fullClassification);		
	}

	/**
	 * Adds content for Names pane - this caters for the content in taxonomy/drilldown/names.jsp
	 * 
	 * @param componentContext
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	public void addActions(TaxonConceptDTO taxonConcept, HttpServletRequest request, HttpServletResponse response)  throws Exception{
		//add taxonomy search criteria
		logger.debug("Adding taxonomic criteria for taxonomy searching");
		CriteriaDTO taxonomyCriteria = filterContentProvider.getTaxonomySearchCriteria(taxonConcept);
		request.setAttribute("taxonomyCriteria",taxonomyCriteria);
		//add occurrence criteria
		logger.debug("Adding occurrence criteria for taxonomy searching");
		CriteriaDTO occurrenceCriteria = filterContentProvider.getOccurrenceSearchCriteria(taxonConcept);
		request.setAttribute("occurrenceCriteria",occurrenceCriteria);
	}
	
	/**
	 * Adds content for Images pane - this caters for the content in taxonomy/drilldown/images.jsp
	 * 
	 * @param componentContext
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	public void addImages(TaxonConceptDTO taxonConcept, HttpServletRequest request, HttpServletResponse response)  throws Exception{
		SearchResultsDTO imageResults = taxonomyManager.findImagesFor(taxonConcept.getKey(), new SearchConstraints(0, maxImagesToShow));
		request.setAttribute("imageResults",imageResults);
	}
	
	/**
	 * Adds warning for ambiguous name.
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	public void addWarnings(TaxonConceptDTO taxonConcept, HttpServletRequest request, HttpServletResponse response)  throws Exception{
		
		String kingdomConceptKey = taxonConcept.getKingdomConceptKey();
		if(kingdomConceptKey!=null){
			BriefTaxonConceptDTO briefDTO = taxonomyManager.getBriefTaxonConceptFor(kingdomConceptKey);
			request.setAttribute("kingdomConcept",briefDTO);
		}
		
		SearchResultsDTO searchResults = taxonomyManager.findTaxonConceptsWithSameScientificNameAndRankAs(taxonConcept.getKey(), taxonConcept.getDataProviderKey(), null, new SearchConstraints(0,10));
		if(!searchResults.isEmpty()){
			
			//split the classifications into accepted and not accepted.
			List matchingNameClassifications = new ArrayList<List>();
			List unacceptedClassifications = new ArrayList<List>();
			List<BriefTaxonConceptDTO> results = searchResults.getResults();
			
			for (BriefTaxonConceptDTO taxonConceptDTO: results){
				List<BriefTaxonConceptDTO> classification = taxonomyManager.getClassificationFor(taxonConceptDTO.getKey(), false, null, allowUnconfirmed);
				if(!classification.isEmpty() && classification.get(0).isAccepted())
					matchingNameClassifications.add(classification);
				else
					unacceptedClassifications.add(classification);
			}
			request.setAttribute("matchingNameClassifications", matchingNameClassifications);
			request.setAttribute("unacceptedClassifications", unacceptedClassifications);
		}
	}
	
	/**
	 * Adds content for Occurrences pane - this caters for the content in taxonomy/drilldown/occurrences.jsp
	 * 
	 * @param componentContext
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	public void addOccurrences(TaxonConceptDTO taxonConcept, HttpServletRequest request, HttpServletResponse response)  throws Exception{
		
		//add map content - this includes points totals
		mapContentProvider.addMapContentForEntity(request, EntityType.TYPE_TAXON, taxonConcept.getKey());
		int occurrenceCount = -1;
		
		//Total occurrence count - only doing this for species and subspecies - a count against a higher rank will be very expensive
		//TODO Ideally this would hit a statistics table.
		if(TaxonRankType.isGenusOrLowerRank(taxonConcept.getRank())){
			occurrenceCount = occurrenceManager.countOccurrenceRecords(null, null, null, taxonConcept.getKey(), null, null, null, null, null, (BoundingBoxDTO) null, (TimePeriodDTO) null, (Date) null, false);
			request.setAttribute("occurrenceCount", occurrenceCount);
			List<CountDTO> resourceCounts = dataResourceManager.getDataResourceWithOccurrencesFor(taxonConcept.getKey(), taxonConcept.getRank(), true);
			request.setAttribute("resourceCounts", resourceCounts);
		}
		
		//add child concept counts
		if(taxonConcept.getRank().equals(TaxonRankType.SPECIES.getName())){
			//add a subspecies count
			int subspecies = taxonomyManager.countChildConceptsFor(taxonConcept.getKey(), TaxonRankType.SUB_SPECIES, false, true, false);
			request.setAttribute("subspeciesCount",subspecies);
		} else if(taxonConcept.getRank().equals(TaxonRankType.GENUS.getName())){
			//add a species count
			int species = taxonomyManager.countChildConceptsFor(taxonConcept.getKey(), TaxonRankType.SPECIES, false, true, false);
			request.setAttribute("speciesCount",species);
		} else if(taxonConcept.getRank().equals(TaxonRankType.FAMILY.getName())){
			//TODO Is this query necessary for higher concepts - expensive for kingdoms and phyla
			//add a genera count
			int genera = taxonomyManager.countChildConceptsFor(taxonConcept.getKey(), TaxonRankType.GENUS, false, true, false);
			request.setAttribute("generaCount",genera);		
		}

		//point totals
		Integer pointsTotal = (Integer) request.getAttribute("pointsTotal");
		if (showCharts && occurrenceCount!=-1 && pointsTotal!=null && occurrenceCount!=0){
			logger.info("Rendering charts...");
			Locale locale = RequestContextUtils.getLocale(request);
			
			Map<String, Double> legend = new LinkedHashMap<String, Double>();
			legend.put(messageSource.getMessage("charts.georeferenced.with.coordinates", null, "With coordinates", locale), pointsTotal.doubleValue());
			legend.put(messageSource.getMessage("charts.georeferenced.without.coordinates", null, "Without coordinates",  locale), (double)(occurrenceCount-pointsTotal.intValue()));
			
			//create chart
//			String chartFileName = ChartUtils.writePieChartImageToTempFile(legend, "species-"+taxonConcept.getKey());
//			request.setAttribute("georefChart", chartFileName);
			
			//counts per country
//			List<CountDTO> countryCount = geospatialManager.getTotalsPerCountry(EntityType.TYPE_TAXON, taxonConcept.getKey());
//			Map<String, Double> countryLegend = new LinkedHashMap<String, Double>();	
//			Iterator iter = countryCount.iterator();		
//			for (int i=0; i<5 && iter.hasNext(); i++){
//				CountDTO countDTO = (CountDTO) iter.next();
//				logger.info("Country key: "+countDTO.getKey()+", name: "+countDTO.getName());
//				countryLegend.put(messageSource.getMessage("country."+countDTO.getKey(),  null, countDTO.getKey(), locale), countDTO.getCount().doubleValue());
//			}
//			//add the remainder together - Other
//			if(countryCount.size()>5){
//				Integer otherTotal = 0;
//				
//				while (iter.hasNext()){
//					CountDTO countDTO = (CountDTO) iter.next();
//					otherTotal += countDTO.getCount();
//				}
//				countryLegend.put(messageSource.getMessage("charts.country.other", null, "Other", locale), otherTotal.doubleValue());
//			}
//			chartFileName = ChartUtils.writePieChartImageToTempFile(countryLegend, "species-country-"+taxonConcept.getKey());
//			request.setAttribute("countryChart", chartFileName);
//			request.setAttribute("countryCounts", countryCount);
//			
			//counts per region			
			Map<String, Double> regionLegend = new LinkedHashMap<String, Double>();
			List<CountDTO> regionCount = geospatialManager.getTotalsPerRegion(EntityType.TYPE_TAXON, taxonConcept.getKey());
			Iterator<CountDTO> iter = regionCount.iterator();		
			for (int i=0; i<5 && iter.hasNext(); i++){
				CountDTO countDTO = iter.next();
				logger.info("Region key: "+countDTO.getKey()+", name: "+countDTO.getName());
				regionLegend.put(messageSource.getMessage("region."+countDTO.getKey(), null, countDTO.getKey(), locale), countDTO.getCount().doubleValue());
			}
			//add the remainder together - Other
			if(regionCount.size()>5){
				Integer otherTotal = 0;
				
				while (iter.hasNext()){
					CountDTO countDTO = (CountDTO) iter.next();
					otherTotal += countDTO.getCount();
				}
				regionLegend.put(messageSource.getMessage("charts.region.other", null, locale), otherTotal.doubleValue());
			}
//			chartFileName = ChartUtils.writePieChartImageToTempFile(regionLegend, "species-region-"+taxonConcept.getKey());
//			request.setAttribute("regionChart", chartFileName);
			request.setAttribute("regionCounts", regionCount);
			
			//percentages
			request.setAttribute("georeferencedPercentage", (int)( ((float)pointsTotal/(float)occurrenceCount) *100));
			request.setAttribute("nonGeoreferencedPercentage", 100- (int)( ((float)pointsTotal/(float)occurrenceCount) *100));
		}
		
	}	
	
	/**
	 * Add the details of the partner concepts.
	 * 
	 * @param taxonConcept
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@SuppressWarnings({"unchecked"})
	public void addPartnerConcepts(TaxonConceptDTO taxonConcept, HttpServletRequest request, HttpServletResponse response)  throws Exception{
		List<TaxonConceptDTO> partnerConcepts = taxonomyManager.getAuthoritativeTaxonConceptsForNubTaxonConcept(taxonConcept.getKey());
		List<PartnerConceptBean> partners = new ArrayList<PartnerConceptBean>();
		request.setAttribute("partners", partners);
		for(TaxonConceptDTO partnerConcept : partnerConcepts) {
			PartnerConceptBean partner = new PartnerConceptBean();
			partners.add(partner);
			partner.setTaxonConcept(partnerConcept);

			partner.setDataResource(dataResourceManager.getDataResourceFor(partnerConcept.getDataResourceKey()));
			partner.setHigherConcepts(taxonomyManager.getClassificationFor(partnerConcept.getKey(), false, null, allowUnconfirmed));
			partner.setRemoteConcepts(taxonomyManager.findRemoteConceptsFor(partnerConcept.getKey()));
			
			List<Map<String, List<CommonNameDTO>>> orderedCommonNames = getOrderedCommonNames(request, partnerConcept);
			partner.setCommonNames(orderedCommonNames);
			
			List<RelationshipAssertionDTO> synonyms = taxonConceptUtils.retrieveSynonyms(partnerConcept.getKey());
			partner.setSynonyms(synonyms);
			//add from relationship assertions
			List<RelationshipAssertionDTO> relationshipAssertions = taxonomyManager.findRelationshipAssertionsForFromTaxonConcept(partnerConcept.getKey());
			partner.setRelationshipAssertions(relationshipAssertions);
			
			if(logger.isDebugEnabled())
				logger.debug("Added partner: " + partnerConcept.getTaxonName() + " from " + partner.getDataResource().getName() + " (" + partner.getDataResource().getBasisOfRecord() + ")");
		}
	}

	/**
	 * Adds any typification records
	 * @param taxonConcept
	 * @param request
	 * @param response
	 * @throws Exception
	 */ 
	public void addTypifications(TaxonConceptDTO taxonConcept, HttpServletRequest request, HttpServletResponse response)  throws Exception{
		if (taxonConcept.getIsNubConcept()) {
			request.setAttribute("typifications", taxonomyManager.getTypificationRecordsForPartnersOfTaxonConcept(taxonConcept.getKey()));	
		} else {
			request.setAttribute("typifications", taxonomyManager.getTypificationRecordsForPartnersOfTaxonConcept(taxonConcept.getPartnerConceptKey()));			
		}
	}
	
	/**
	 * Gets the ordered common names for the taxon concept.
	 * If the request has a language, then any with that language are first in the list
	 * @param request
	 * @param partnerConcept
	 * @return
	 * @throws ServiceException
	 */
	@SuppressWarnings("unchecked")
	private List<Map<String, List<CommonNameDTO>>> getOrderedCommonNames(HttpServletRequest request, TaxonConceptDTO partnerConcept) throws ServiceException {
		// group the names
		List<CommonNameDTO> commonNames = taxonomyManager.findCommonNamesForTaxonConcept(partnerConcept.getKey(), new SearchConstraints(0, maxCommonNamesToDisplay));
		Map<String, List<CommonNameDTO>> commonNamesByLanguage = new HashMap<String, List<CommonNameDTO>>(); 
		for (CommonNameDTO cn : commonNames) {
			if (commonNamesByLanguage.containsKey(cn.getLanguage())) {
				List<CommonNameDTO> names = commonNamesByLanguage.get(cn.getLanguage());
				names.add(cn);
			} else {
				List<CommonNameDTO> names = new LinkedList<CommonNameDTO>();
				names.add(cn);
				commonNamesByLanguage.put(cn.getLanguage(), names);
			}
		}
		// now order them into a list of preferred language, followed by alternatives
		final String preferredLanguage = RequestContextUtils.getLocale(request).getDisplayLanguage();
		if(logger.isDebugEnabled())
			logger.debug("Preferred language for common name: " + preferredLanguage);
		List<Map<String, List<CommonNameDTO>>> orderedCommonNames = new LinkedList<Map<String, List<CommonNameDTO>>>();
		for (String language : commonNamesByLanguage.keySet()) {
			Map<String, List<CommonNameDTO>> languageWithCommonNames = new HashMap<String, List<CommonNameDTO>>();
			languageWithCommonNames.put(language, commonNamesByLanguage.get(language));
			orderedCommonNames.add(languageWithCommonNames);
		}
		// sort them
		Collections.sort(orderedCommonNames, 
				new Comparator() {
					public int compare(Object o1, Object o2) {
						String language1 = ((Map<String, List<CommonNameDTO>>)o1).keySet().iterator().next();
						String language2 = ((Map<String, List<CommonNameDTO>>)o2).keySet().iterator().next();
						
						if (preferredLanguage != null) {
							if (preferredLanguage.equalsIgnoreCase(language1)) {
								return -1;
							} else if (preferredLanguage.equalsIgnoreCase(language2)) {
								return 1;
							}
						}
						if (language1 == null && language2 == null) {
							return 0;						
						} else if (language1 == null || language2 == null) {
							return -1;
						} else {
							return language1.compareTo(language2);
						}
					}});
		return orderedCommonNames;
	}
	
	/**
	 * Provides custom pattern matching to enable deep linking to concepts by ids or
	 * name values.
	 * 
	 * Matches the patterns:
	 * /123
	 * /123/view-name
	 * /123/commonName/123/
	 * /taxon-name
	 * /taxon-name/view-name
	 * /rank/taxon-name
	 * /rank/taxon-name/view-name
	 * 
	 * <External_id>/provider/<provider-id>
	 * <External_id>/resource/<resource-id>
	 *  
	 * @see org.gbif.portal.web.controller.RestController#mapUrlProperties(java.util.List)
	 */
	@Override
	public Map<String, String> mapUrlProperties(List<String> urlProperties) {

		Map <String, String> mapProperties = new HashMap<String, String>();
		if(urlProperties.size()>0){
			String property1 = urlProperties.get(0);
			//is it an id
			if(TaxonRankType.isRecognisedRank(property1)){
				mapProperties.put(rankRequestKey, property1);
				if(urlProperties.size()>1)
					mapProperties.put(idRequestKey, decodeParameter(urlProperties.get(1)));				
				if(urlProperties.size()>2)
					mapProperties.put(viewRequestKey, urlProperties.get(2));								
			//assume it is a concept name
			} else {
				mapProperties.put(idRequestKey, property1);
				//add view name
				if(urlProperties.size()==2){
					mapProperties.put(viewRequestKey, urlProperties.get(1));
				}
				if(urlProperties.size()>=3){
					if (urlProperties.get(1).equals(dataProviderRequestKeyword)){
						mapProperties.put(dataProviderRequestKeyword, urlProperties.get(2));
					} else if (urlProperties.get(1).equals(dataResourceRequestKeyword)){
						mapProperties.put(dataResourceRequestKeyword, urlProperties.get(2));
					}
				}
				if(urlProperties.size()==4){
					mapProperties.put(viewRequestKey, urlProperties.get(3));
				}
			}
		}
		return mapProperties;
	}

	/**
	 * @param dataResourceManager the dataResourceManager to set
	 */
	public void setDataResourceManager(DataResourceManager dataResourceManager) {
		this.dataResourceManager = dataResourceManager;
	}

	/**
	 * @param taxonomyManager the taxonomyManager to set
	 */
	public void setTaxonomyManager(TaxonomyManager taxonomyManager) {
		this.taxonomyManager = taxonomyManager;
	}

	/**
	 * @param maxNameResolvingResults the maxNameResolvingResults to set
	 */
	public void setMaxNameResolvingResults(int maxNameResolvingResults) {
		this.maxNameResolvingResults = maxNameResolvingResults;
	}

	/**
	 * @param conceptKeyModelKey the conceptKeyModelKey to set
	 */
	public void setConceptKeyModelKey(String conceptKeyModelKey) {
		this.conceptKeyModelKey = conceptKeyModelKey;
	}

	/**
	 * @param idRequestKey the idRequestKey to set
	 */
	public void setIdRequestKey(String idRequestKey) {
		this.idRequestKey = idRequestKey;
	}

	/**
	 * @param nameMatchesModelKey the nameMatchesModelKey to set
	 */
	public void setNameMatchesModelKey(String nameMatchesModelKey) {
		this.nameMatchesModelKey = nameMatchesModelKey;
	}

	/**
	 * @param nameModelKey the nameModelKey to set
	 */
	public void setNameModelKey(String nameModelKey) {
		this.nameModelKey = nameModelKey;
	}

	/**
	 * @param nameResolveViewName the nameResolveViewName to set
	 */
	public void setNameResolveViewName(String nameResolveViewName) {
		this.nameResolveViewName = nameResolveViewName;
	}

	/**
	 * @param rankRequestKey the rankRequestKey to set
	 */
	public void setRankRequestKey(String rankRequestKey) {
		this.rankRequestKey = rankRequestKey;
	}

	/**
	 * @param taxonConceptModelKey the taxonConceptModelKey to set
	 */
	public void setTaxonConceptModelKey(String taxonConceptModelKey) {
		this.taxonConceptModelKey = taxonConceptModelKey;
	}

	/**
	 * @param viewRequestKey the viewRequestKey to set
	 */
	public void setViewRequestKey(String viewRequestKey) {
		this.viewRequestKey = viewRequestKey;
	}

	/**
	 * @param commonNameModelKey the commonNameModelKey to set
	 */
	public void setCommonNameModelKey(String commonNameModelKey) {
		this.commonNameModelKey = commonNameModelKey;
	}

	/**
	 * @param commonNameRequestKey the commonNameRequestKey to set
	 */
	public void setCommonNameRequestKey(String commonNameRequestKey) {
		this.commonNameRequestKey = commonNameRequestKey;
	}

	/**
	 * @param commonNameRequestKeyword the commonNameRequestKeyword to set
	 */
	public void setCommonNameRequestKeyword(String commonNameRequestKeyword) {
		this.commonNameRequestKeyword = commonNameRequestKeyword;
	}

	/**
	 * @param filterContentProvider the filterContentProvider to set
	 */
	public void setFilterContentProvider(FilterContentProvider filterContentProvider) {
		this.filterContentProvider = filterContentProvider;
	}

	/**
	 * @param geospatialManager the geospatialManager to set
	 */
	public void setGeospatialManager(GeospatialManager geospatialManager) {
		this.geospatialManager = geospatialManager;
	}

	/**
	 * @param mapContentProvider the mapContentProvider to set
	 */
	public void setMapContentProvider(MapContentProvider mapContentProvider) {
		this.mapContentProvider = mapContentProvider;
	}

	/**
	 * @param occurrenceManager the occurrenceManager to set
	 */
	public void setOccurrenceManager(OccurrenceManager occurrenceManager) {
		this.occurrenceManager = occurrenceManager;
	}

	/**
	 * @param taxonomicPriorityThreshold the taxonomicPriorityThreshold to set
	 */
	public void setTaxonomicPriorityThreshold(int taxonomicPriorityThreshold) {
		this.taxonomicPriorityThreshold = taxonomicPriorityThreshold;
	}

	/**
	 * @param maxImagesToShow the maxImagesToShow to set
	 */
	public void setMaxImagesToShow(int maxImagesToShow) {
		this.maxImagesToShow = maxImagesToShow;
	}

	/**
	 * @param taxonConceptUtils the taxonConceptUtils to set
	 */
	public void setTaxonConceptUtils(TaxonConceptUtils taxonConceptUtils) {
		this.taxonConceptUtils = taxonConceptUtils;
	}

	/**
	 * @param allowUnconfirmedNames the allowUnconfirmedNames to set
	 */
	public void setAllowUnconfirmedNames(boolean allowUnconfirmedNames) {
		this.allowUnconfirmedNames = allowUnconfirmedNames;
	}

	/**
	 * @param dataProviderRequestKeyword the dataProviderRequestKeyword to set
	 */
	public void setDataProviderRequestKeyword(String dataProviderRequestKeyword) {
		this.dataProviderRequestKeyword = dataProviderRequestKeyword;
	}

	/**
	 * @param dataResourceRequestKeyword the dataResourceRequestKeyword to set
	 */
	public void setDataResourceRequestKeyword(String dataResourceRequestKeyword) {
		this.dataResourceRequestKeyword = dataResourceRequestKeyword;
	}

	/**
	 * @param maxCommonNamesToDisplay the maxCommonNamesToDisplay to set
	 */
	public void setMaxCommonNamesToDisplay(int maxCommonNamesToDisplay) {
		this.maxCommonNamesToDisplay = maxCommonNamesToDisplay;
	}

	/**
	 * @param messageSource the messageSource to set
	 */
	public void setMessageSource(MessageSource messageSource) {
		this.messageSource = messageSource;
	}

	/**
	 * @param showCharts the showCharts to set
	 */
	public void setShowCharts(boolean showCharts) {
		this.showCharts = showCharts;
	}

	/**
	 * @param userUtils the userUtils to set
	 */
	public void setUserUtils(UserUtils userUtils) {
		this.userUtils = userUtils;
	}

	/**
   * @param allowUnconfirmed the allowUnconfirmed to set
   */
  public void setAllowUnconfirmed(boolean allowUnconfirmed) {
  	this.allowUnconfirmed = allowUnconfirmed;
  }
}