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
package org.gbif.portal.web.controller.geography;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.gbif.portal.dto.SearchResultsDTO;
import org.gbif.portal.dto.geospatial.CountryDTO;
import org.gbif.portal.dto.resources.DataProviderDTO;
import org.gbif.portal.dto.tag.BiRelationTagDTO;
import org.gbif.portal.dto.tag.QuadRelationTagDTO;
import org.gbif.portal.dto.taxonomy.TaxonConceptDTO;
import org.gbif.portal.model.occurrence.BasisOfRecord;
import org.gbif.portal.service.DataResourceManager;
import org.gbif.portal.service.GeospatialManager;
import org.gbif.portal.service.OccurrenceManager;
import org.gbif.portal.service.ServiceException;
import org.gbif.portal.service.SystemManager;
import org.gbif.portal.service.TaxonomyManager;
import org.gbif.portal.web.content.map.MapContentProvider;
import org.springframework.context.MessageSource;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;
import org.springframework.web.servlet.mvc.multiaction.NoSuchRequestHandlingMethodException;
import org.springframework.web.servlet.support.RequestContextUtils;

/**
 * A controller providing views on how data is distributed and hosted
 * within the system.
 * 
 * @author dmartin
 */
public class RepatriationController extends MultiActionController {

	protected GeospatialManager geospatialManager;
	
	protected OccurrenceManager occurrenceManager;
	
	protected TaxonomyManager taxonomyManager;
	
	protected DataResourceManager dataResourceManager;
	
	protected SystemManager systemManager;
	
	protected MapContentProvider mapContentProvider;
	
	protected MessageSource messageSource;
	
	protected String countryUnknown = "XX";
	
	protected Long unknownKingdom = -1l;

	protected String viewName = "repatTable";
	
	protected String ajaxViewName = "repatAjaxCaption";
	
	protected String viewRequestKey = "view";
	
	protected String selectAllValue = "all";
	
	protected String countryI18nPrefix = "country.";
	
	protected int taxonomicPriorityThreshold = 10;
	
	protected String selectedHostKey = "host";
	
	protected String selectedCountryKey= "country";
	
	/**
	 * Retrieves a caption for the generated map.
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ModelAndView ajaxCaption(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		ModelAndView mav = new ModelAndView(ajaxViewName);
		
		//retrieve both iso codes
		String host = request.getParameter(selectedHostKey);
		String country = request.getParameter(selectedCountryKey);
		Locale locale = RequestContextUtils.getLocale(request);

		if(host!=null & country!=null){
			addHostCountryInfo(mav, host, country, locale);
		} else if(host!=null){
			addHostInfo(mav, host, locale);
		} else if(country!=null){
			addCountryInfo(mav, country, locale);
		}
		
		mav.addObject("networkTotal", occurrenceManager.getTotalOccurrenceRecordCount());
		return mav;
	}
	
	/**
	 * Add Country specific info to model 
	 * @param mav
	 * @param country
	 * @param locale
	 * @throws Exception
	 */
	private void addCountryInfo(ModelAndView mav, String country, Locale locale) throws Exception {
	  CountryDTO countryDTO = null;
	  String countryIso = countryUnknown.equals(country) ? null : country;
	  List<BiRelationTagDTO> rtList = geospatialManager.retrieveHostCountriesWithDataFor(countryIso);				
	  	
	  if(countryIso!=null) {	
	  	countryDTO = geospatialManager.getCountryFor(country, locale);
	  }			
	  
	  //calculate the percentage of data for this country, hosted in this country
	  int totalHosted = 0;
	  int totalHostedForOthers = 0;
	  int noOfOtherCountries = 0;
	  int hostNotTied = 0;
	  
	  for(BiRelationTagDTO rt: rtList){
	  	totalHosted +=rt.getCount();
	  	
	  	if(rt.getFromEntityName()==null){
	  		hostNotTied+=rt.getCount();
	  	} else if(!rt.getFromEntityName().equals(country)){
	  		totalHostedForOthers+=rt.getCount();
	  		noOfOtherCountries++;
	  	}
	  }
	  
	  //host stats
	  List<QuadRelationTagDTO[]> kingdomBasisOfRecord = generateBasisOrRecordTable(geospatialManager.retrieveBreakdownForCountry(countryIso));
	  
	  mav.addObject("kingdomBasisOfRecord", kingdomBasisOfRecord);
	  mav.addObject("hostNotTied", hostNotTied);
	  if(countryDTO!=null){
	    mav.addObject("country", countryDTO);
	  }
	  mav.addObject("totalHosted", totalHosted);
	  mav.addObject("totalHostedByOthers", totalHostedForOthers);
	  mav.addObject("noOfOtherCountries", noOfOtherCountries);
  }

	/**
	 * Add Host specific info to model 
	 * 
	 * @param mav
	 * @param host
	 * @param locale
	 * @throws Exception
	 */
	private void addHostInfo(ModelAndView mav, String host, Locale locale) throws Exception {
	  String hostIso = countryUnknown.equals(host) ? null : host;
	  List<BiRelationTagDTO> rtList = geospatialManager.retrieveCountriesWithDataHostedBy(hostIso);
	  if(hostIso!=null){
	  	CountryDTO hostDTO = geospatialManager.getCountryFor(hostIso, locale);
	  	mav.addObject("host", hostDTO);
	  }
	  
	  //calculate the percentage of hosted in this country, for this country
	  int totalHosted = 0;
	  int totalHostedForOthers = 0;
	  int noOfOtherCountries = 0;
	  int originUnknown = 0;
	  
	  for(BiRelationTagDTO rt: rtList){
	  	totalHosted +=rt.getCount();
	  	if(rt.getToEntityName()==null){
	  		originUnknown+=rt.getCount();
	  	} else if(rt.getToEntityName()!=null && !rt.getToEntityName().equals(host)){
	  		totalHostedForOthers+=rt.getCount();
	  		noOfOtherCountries++;
	  	} else {
	  		logger.debug(rt.getToEntityName());
	  	}
	  }

	  //host stats
	  List<QuadRelationTagDTO[]> kingdomBasisOfRecord = generateBasisOrRecordTable(geospatialManager.retrieveBreakdownForHost(hostIso));
	  
	  mav.addObject("kingdomBasisOfRecord", kingdomBasisOfRecord);
	  mav.addObject("totalHosted", totalHosted);
	  mav.addObject("originUnknown", originUnknown);
	  mav.addObject("totalHostedForOthers", totalHostedForOthers);
	  mav.addObject("noOfOtherCountries", noOfOtherCountries);
  }

	/**
	 * Add Host - Country specific info to model
	 *  
	 * @param mav
	 * @param host
	 * @param country
	 * @param locale
	 * @throws Exception
	 */
	private void addHostCountryInfo(ModelAndView mav, String host, String country, Locale locale) throws Exception {
	  if(!countryUnknown.equals(country)){
	    CountryDTO countryDTO = geospatialManager.getCountryFor(country, locale);
	    mav.addObject("country", countryDTO);
	  }
	  if(!countryUnknown.equals(host)){
	    CountryDTO hostDTO = geospatialManager.getCountryFor(host, locale);
	    mav.addObject("host", hostDTO);
	  }
	    
	  String hostIso = countryUnknown.equals(host) ? null : host;
	  String countryIso = countryUnknown.equals(country) ? null : country;

	  //FIXME - remove this query
	  BiRelationTagDTO rtDTO = geospatialManager.retrieveTotalRecordHostedInFor(hostIso, countryIso);
	  
	  //host country stats
	  List<QuadRelationTagDTO[]> kingdomBasisOfRecord = generateBasisOrRecordTable(geospatialManager.retrieveBreakdownForHostCountry(hostIso, countryIso));
	  
	  //add the unknown country count
	  mav.addObject("hostedCount", rtDTO.getCount());
	  mav.addObject("kingdomBasisOfRecord", kingdomBasisOfRecord);
  }
	
	/**
	 * Generate table
	 *  7 columns observation, specimen, living, germplasm, fossil, other, total
	 * 
	 * @param retrieveBreakdownForHost
	 * @return
	 */
	private List<QuadRelationTagDTO[]> generateBasisOrRecordTable(List<QuadRelationTagDTO> retrieveBreakdownForHost) throws ServiceException {

		List<QuadRelationTagDTO> tags = handleUnknownKingdom(retrieveBreakdownForHost);
		
		List<QuadRelationTagDTO[]> table = new ArrayList<QuadRelationTagDTO[]>(); 
		int total = 0;
		int otherTotal = 0;
		Long currentId = null;
		String currentName = null;
		QuadRelationTagDTO[] row = null;

		for (QuadRelationTagDTO quadRelationTagDTO: tags) {
			
			if(	(currentId==null && quadRelationTagDTO.getEntity3Id()!=null)
					|| (quadRelationTagDTO.getEntity3Id()!=null && !currentId.equals(quadRelationTagDTO.getEntity3Id()))
					|| (quadRelationTagDTO.getEntity3Id()==null)		
					)
			{
				if(row!=null){
					row[5] = new QuadRelationTagDTO(2001,null,null,null,null,currentId,currentName,null,null,otherTotal);
					row[6] = new QuadRelationTagDTO(2001,null,null,null,null,currentId,currentName,null,null,total);
				}
				
				//seven columns
				row = new QuadRelationTagDTO[7];
				table.add(row);
				total=0;
				otherTotal=0;
				currentId = quadRelationTagDTO.getEntity3Id();
				currentName = quadRelationTagDTO.getEntity3Name();
				//initialise
				for(int i=0; i<row.length; i++){
					row[i] = new QuadRelationTagDTO(2001,null,null,null,null,currentId,currentName,null,null,0);
				}
			}
			
			if(quadRelationTagDTO.getEntity4Id()==null){
				otherTotal += quadRelationTagDTO.getCount();
			} else if(quadRelationTagDTO.getEntity4Id().intValue()==BasisOfRecord.OBSERVATION.getValue()){
				row[0] = quadRelationTagDTO;
			} else if(quadRelationTagDTO.getEntity4Id().intValue()==BasisOfRecord.SPECIMEN.getValue()){
				row[1] = quadRelationTagDTO;
			} else if(quadRelationTagDTO.getEntity4Id().intValue()==BasisOfRecord.LIVING.getValue()){
				row[2] = quadRelationTagDTO;
			} else if(quadRelationTagDTO.getEntity4Id().intValue()==BasisOfRecord.GERMPLASM.getValue()){
				row[3] = quadRelationTagDTO;
			} else if(quadRelationTagDTO.getEntity4Id().intValue()==BasisOfRecord.FOSSIL.getValue()){
				row[4] = quadRelationTagDTO;
			} else {
				otherTotal += quadRelationTagDTO.getCount();
			}
			
			total += quadRelationTagDTO.getCount();
		}
		
		//add the last to columns for the last row
		if(tags.size()>0){
			row[5] = new QuadRelationTagDTO(2001,null,null,null,null,currentId,currentName,null,null,otherTotal);
			row[6] = new QuadRelationTagDTO(2001,null,null,null,null,currentId,currentName,null,null,total);
		}
		
	  return table;
  }

	/**
	 * Process tags with a null entity id or kingdom of "Unknown"
	 * @param retrieveBreakdownForHost
	 * @return
	 */
	private List<QuadRelationTagDTO> handleUnknownKingdom(List<QuadRelationTagDTO> retrieveBreakdownForHost) throws ServiceException {
	 
		List<QuadRelationTagDTO> unknownKingdomTags = new ArrayList<QuadRelationTagDTO>();
		
		TaxonConceptDTO currentKingdom = null;
		Long currentKingdomId = null;
		
		for(QuadRelationTagDTO tag:retrieveBreakdownForHost){
			
			if(tag.getEntity3Id()==null){
				unknownKingdomTags.add(tag);
			} else if(currentKingdomId!=null && tag.getEntity3Id().equals(currentKingdomId)) {
				if(currentKingdom==null || currentKingdom.getTaxonomicPriority() > taxonomicPriorityThreshold){
					unknownKingdomTags.add(tag);
				}
			} else {
				currentKingdom = taxonomyManager.getTaxonConceptFor(tag.getEntity3Id().toString());
				currentKingdomId = tag.getEntity3Id();
				if(currentKingdom==null || 
						currentKingdom.getTaxonomicPriority() > taxonomicPriorityThreshold){
					unknownKingdomTags.add(tag);
				}
			}
		}
		retrieveBreakdownForHost.removeAll(unknownKingdomTags);
		
		int observations = 0;
		int specimens = 0;
		int living = 0;
		int germplasm = 0;
		int fossil = 0;
		int other = 0;
		
		for (QuadRelationTagDTO tag:unknownKingdomTags){
			if(tag.getEntity4Id()==null || tag.getEntity4Id().intValue()==BasisOfRecord.UNKNOWN.getValue()){
				other += tag.getCount();
			} else if(tag.getEntity4Id().intValue()==BasisOfRecord.OBSERVATION.getValue()){
				observations+=tag.getCount();
			} else if(tag.getEntity4Id().intValue()==BasisOfRecord.SPECIMEN.getValue()){
				specimens+=tag.getCount();
			} else if(tag.getEntity4Id().intValue()==BasisOfRecord.LIVING.getValue()){
				living+=tag.getCount();
			} else if(tag.getEntity4Id().intValue()==BasisOfRecord.GERMPLASM.getValue()){
				germplasm+=tag.getCount();
			} else if(tag.getEntity4Id().intValue()==BasisOfRecord.FOSSIL.getValue()){
				fossil+=tag.getCount();
			} else {
				other += tag.getCount();
			}			
		}
		
		if(observations>0)
			retrieveBreakdownForHost.add(new QuadRelationTagDTO(2001, null, null,null, null,null, null,BasisOfRecord.OBSERVATION.getValue().longValue(), null, observations));
		if(specimens>0)
			retrieveBreakdownForHost.add(new QuadRelationTagDTO(2001, null, null,null, null,null, null,BasisOfRecord.SPECIMEN.getValue().longValue(), null, specimens));
		if(living>0)
			retrieveBreakdownForHost.add(new QuadRelationTagDTO(2001, null, null,null, null,null, null,BasisOfRecord.LIVING.getValue().longValue(), null, living));
		if(germplasm>0)
			retrieveBreakdownForHost.add(new QuadRelationTagDTO(2001, null, null,null, null,null, null,BasisOfRecord.GERMPLASM.getValue().longValue(), null, germplasm));
		if(fossil>0)
			retrieveBreakdownForHost.add(new QuadRelationTagDTO(2001, null, null,null, null,null, null,BasisOfRecord.FOSSIL.getValue().longValue(), null, fossil));
		if(other>0)
			retrieveBreakdownForHost.add(new QuadRelationTagDTO(2001, null, null,null, null,null, null,BasisOfRecord.UNKNOWN.getValue().longValue(), null, other));
		
	  return retrieveBreakdownForHost;
  }

	/**
	 * @see org.springframework.web.servlet.mvc.multiaction.MultiActionController#handleNoSuchRequestHandlingMethod(org.springframework.web.servlet.mvc.multiaction.NoSuchRequestHandlingMethodException, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected ModelAndView handleNoSuchRequestHandlingMethod(
			NoSuchRequestHandlingMethodException ex,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		return showFullTable(request);
	}

	/**
	 * Add the information to render the full repatriation table.
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	private ModelAndView showFullTable(HttpServletRequest request) throws Exception {
		
		boolean download = ServletRequestUtils.getBooleanParameter(request, "download", false);
		boolean printable = ServletRequestUtils.getBooleanParameter(request, "printable", false);
		ModelAndView mav = null;
		if(printable){
			mav = new ModelAndView("repatTablePrintable");
		} else if(download){
			mav = new ModelAndView("repatExcelTable");
		} else {
			mav = new ModelAndView(viewName);
		}
		
	  List<Date> dates = systemManager.retrieveRolloverDates();
	  if(dates!=null && !dates.isEmpty()){
	  	mav.addObject("lastRollover", dates.get(0));
	  }

	  List<DataProviderDTO> dataProviders = dataResourceManager.getInternationalDataProviders();
  	mav.addObject("internationalProviders", dataProviders);
		
		String view = ServletRequestUtils.getStringParameter(request, viewRequestKey, "concise");
		final boolean sortByIso = "iso".equals(view) || "concise".equals(view) || "percent".equals(view); 

		//retrieve the tags for the repatriation table
		String selectedCountries = ServletRequestUtils.getStringParameter(request, "country", null);
		List<String> countries = new ArrayList<String>();
		if(selectedCountries!=null){
			StringTokenizer st = new StringTokenizer(selectedCountries, ",");
			while(st.hasMoreTokens()){
				countries.add(st.nextToken());
			}
		}		
		
		String selectedRegion = ServletRequestUtils.getStringParameter(request, "region", null);
		List<String> regions = new ArrayList<String>();
		if(selectedRegion!=null){
			StringTokenizer st = new StringTokenizer(selectedRegion, ",");
			while(st.hasMoreTokens()){
				regions.add(st.nextToken());
			}
		}		
		
		String selectedHost = ServletRequestUtils.getStringParameter(request, "host", null);
		
		if(selectAllValue.equals(selectedCountries)){
			selectedCountries = null;
		}
		if(selectAllValue.equals(selectedHost)){
			selectedHost = null;
		}		
		
		List<BiRelationTagDTO> relationshipTags = null;
		
		if(selectedRegion!=null){
			relationshipTags = geospatialManager.retrieveHostCountriesWithDataForRegion(regions);
		} else if(selectedCountries!=null && selectedHost!=null){
			BiRelationTagDTO relationshipTag = geospatialManager.retrieveTotalRecordHostedInFor(selectedHost, selectedCountries);
			relationshipTags = new ArrayList<BiRelationTagDTO>();
			relationshipTags.add(relationshipTag);
		} else if(selectedCountries!=null){
			relationshipTags = geospatialManager.retrieveHostCountriesWithDataFor(countries);		
		} else if(selectedHost!=null){
			relationshipTags = geospatialManager.retrieveCountriesWithDataHostedBy(selectedHost);
		} else {
			relationshipTags = geospatialManager.retrieveRepatTable();
		}
		
		for(BiRelationTagDTO rt : relationshipTags){
			if(rt.getFromEntityName()==null)
				rt.setFromEntityName(countryUnknown);
			if(rt.getToEntityName()==null)
				rt.setToEntityName(countryUnknown);
		}
		
		//retrieve a list of host countries
		List<String> hostCountryIsoCodes = null;
		if(selectedHost==null){		
			hostCountryIsoCodes = geospatialManager.getHostCountryISOCountryCodes();
		} else {
			hostCountryIsoCodes = new ArrayList<String>();
			hostCountryIsoCodes.add(selectedHost);
		}
		 
		final Locale locale = RequestContextUtils.getLocale(request);
		List<String> hostCountryForDisplay = geospatialManager.getHostCountryISOCountryCodes();
		
		//order hosts by locale string 
		Collections.sort(hostCountryIsoCodes, new Comparator<String>(){
			public int compare(String o1, String o2) {
				if(sortByIso){
					return o1.compareTo(o2);
				}
				String c1 = messageSource.getMessage(countryI18nPrefix+o1, null, locale);
				String c2 = messageSource.getMessage(countryI18nPrefix+o2, null, locale);
				return c1.compareTo(c2);
			}
		});

		Collections.sort(hostCountryForDisplay, new Comparator<String>(){
			public int compare(String o1, String o2) {
				String c1 = messageSource.getMessage(countryI18nPrefix+o1, null, locale);
				String c2 = messageSource.getMessage(countryI18nPrefix+o2, null, locale);
				return c1.compareTo(c2);
			}
		});		
		
		//order tags by country (using the locale) and then by host using the locale
		Collections.sort(relationshipTags, new Comparator<BiRelationTagDTO>(){
			public int compare(BiRelationTagDTO r1, BiRelationTagDTO r2) {

				//compare country
				if(r1.getToEntityId()!=null && r2.getToEntityId()!=null	
						&& !r1.getToEntityId().equals(r2.getToEntityId())){
					//compare locale names
					if(countryUnknown.equals(r1.getToEntityName()))
						return 1;	
					if(countryUnknown.equals(r2.getToEntityName()))
						return -1;	
					
					if(sortByIso){
						return r1.getToEntityName().compareTo(r2.getToEntityName());
					}
					String c1 = messageSource.getMessage(countryI18nPrefix+r1.getToEntityName(), null, locale);
					String c2 = messageSource.getMessage(countryI18nPrefix+r2.getToEntityName(), null, locale);
					return c1.compareTo(c2);
				}
				
				//compare host				
				if(r1.getFromEntityId()!=null && r2.getFromEntityId()!=null	
						&& !r1.getFromEntityId().equals(r2.getFromEntityId())){
					if(sortByIso){
						return r1.getToEntityName().compareTo(r2.getToEntityName());
					}
					//compare locale names
					String c1 = messageSource.getMessage(countryI18nPrefix+r1.getFromEntityName(), null, locale);
					String c2 = messageSource.getMessage(countryI18nPrefix+r2.getFromEntityName(), null, locale);
					return c1.compareTo(c2);
				}				
				return -1;
			}
		});	
		
		if(logger.isDebugEnabled()){
			for(BiRelationTagDTO rt : relationshipTags){
				logger.debug(rt.getToEntityName()+" \t\t\t"+rt.getFromEntityName());
			}
		}
		
		//add the unknown host country at the top of list
		if(selectedHost==null){
			hostCountryIsoCodes.add(countryUnknown);
		}
		
		//padding out maps for nulls
		List<CountryDTO> countriesInOrder = new ArrayList<CountryDTO>();
		
		SearchResultsDTO searchResults = geospatialManager.findAllCountries(locale);
		List<CountryDTO> countryList = searchResults.getResults();
		//order countries by locale string 
		Collections.sort(countryList, new Comparator<CountryDTO>(){
			public int compare(CountryDTO o1, CountryDTO o2) {
				String c1 = messageSource.getMessage(countryI18nPrefix+o1.getIsoCountryCode(), null, locale);
				String c2 = messageSource.getMessage(countryI18nPrefix+o2.getIsoCountryCode(), null, locale);
				return c1.compareTo(c2);
			}
		});
		HashMap<String, CountryDTO> countryMap = new HashMap<String, CountryDTO>();
		for(CountryDTO cDTO : countryList){
			countryMap.put(cDTO.getIsoCountryCode(), cDTO);
		}
		
		//linked hashmap to keep the order
		LinkedHashMap<String, BiRelationTagDTO[]> finalList = new LinkedHashMap<String, BiRelationTagDTO[]>();

		String currentCountry = null; 
		BiRelationTagDTO[] currentList = null;
		for(BiRelationTagDTO rt: relationshipTags){
			
			String countryName = rt.getToEntityName();
			if(currentCountry==null || !currentCountry.equals(countryName)){
				if(!countryName.equals(countryUnknown)){
					countriesInOrder.add(countryMap.get(countryName));
				} else {
					CountryDTO countryDTO = new CountryDTO();
					countryDTO.setIsoCountryCode(countryUnknown);
					countryDTO.setName("Unknown");
					countriesInOrder.add(countryDTO);
				}
				currentCountry = countryName;
				currentList = new BiRelationTagDTO[hostCountryIsoCodes.size()];
				finalList.put(countryName, currentList);
			}
			
			int indexOf = hostCountryIsoCodes.indexOf(rt.getFromEntityName());
			logger.debug("from entity name="+ rt.getFromEntityName()+", index value = "+indexOf+",  currentList:"+currentList.length);
			currentList[indexOf] = rt;
		}
 		
		mav.addObject("stats", finalList);
		mav.addObject("countries", countriesInOrder);
		mav.addObject("countryList", countryList);
		mav.addObject("hosts", hostCountryIsoCodes);
		mav.addObject("hostsOrdered", hostCountryForDisplay);
	  return mav;
  }

	/**
	 * Show view breakdown for this host.
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ModelAndView viewHost(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		ModelAndView mav = new ModelAndView("repat.host");
		String host = request.getParameter("host");
		Locale locale = RequestContextUtils.getLocale(request);
		addHostInfo(mav, host, locale);
		mav.addObject("networkTotal", occurrenceManager.getTotalOccurrenceRecordCount());
		mav.addObject("mapServerUrl", mapContentProvider.getMapServerURL());
		return mav;
	}
	
	/**
	 * Show view breakdown for this country.
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ModelAndView viewCountry(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		ModelAndView mav = new ModelAndView("repat.country");
		String country = request.getParameter("country");
		Locale locale = RequestContextUtils.getLocale(request);
		addCountryInfo(mav, country, locale);
		mav.addObject("networkTotal", occurrenceManager.getTotalOccurrenceRecordCount());
		return mav;		
	}

	/**
	 * Show view breakdown for this host country intersection.
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ModelAndView viewHostCountry(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		ModelAndView mav = new ModelAndView("repat.host.country");
		String host = request.getParameter("host");
		String country = request.getParameter("country");
		Locale locale = RequestContextUtils.getLocale(request);
		addHostCountryInfo(mav, host, country, locale);
		mav.addObject("networkTotal", occurrenceManager.getTotalOccurrenceRecordCount());
		return mav;		
	}
	
	/**
	 * @param geospatialManager the geospatialManager to set
	 */
	public void setGeospatialManager(GeospatialManager geospatialManager) {
		this.geospatialManager = geospatialManager;
	}

	/**
	 * @param messageSource the messageSource to set
	 */
	public void setMessageSource(MessageSource messageSource) {
		this.messageSource = messageSource;
	}

	/**
	 * @param occurrenceManager the occurrenceManager to set
	 */
	public void setOccurrenceManager(OccurrenceManager occurrenceManager) {
		this.occurrenceManager = occurrenceManager;
	}

	/**
	 * @param countryUnknown the countryUnknown to set
	 */
	public void setCountryUnknown(String countryUnknown) {
		this.countryUnknown = countryUnknown;
	}

	/**
	 * @param viewName the viewName to set
	 */
	public void setViewName(String viewName) {
		this.viewName = viewName;
	}

	/**
	 * @param viewRequestKey the viewRequestKey to set
	 */
	public void setViewRequestKey(String viewRequestKey) {
		this.viewRequestKey = viewRequestKey;
	}

	/**
   * @param taxonomyManager the taxonomyManager to set
   */
  public void setTaxonomyManager(TaxonomyManager taxonomyManager) {
  	this.taxonomyManager = taxonomyManager;
  }

	/**
   * @param ajaxViewName the ajaxViewName to set
   */
  public void setAjaxViewName(String ajaxViewName) {
  	this.ajaxViewName = ajaxViewName;
  }

	/**
   * @param selectAllValue the selectAllValue to set
   */
  public void setSelectAllValue(String selectAllValue) {
  	this.selectAllValue = selectAllValue;
  }

	/**
   * @param taxonomicPriorityThreshold the taxonomicPriorityThreshold to set
   */
  public void setTaxonomicPriorityThreshold(int taxonomicPriorityThreshold) {
  	this.taxonomicPriorityThreshold = taxonomicPriorityThreshold;
  }

	/**
   * @param mapContentProvider the mapContentProvider to set
   */
  public void setMapContentProvider(MapContentProvider mapContentProvider) {
  	this.mapContentProvider = mapContentProvider;
  }

	/**
   * @param unknownKingdom the unknownKingdom to set
   */
  public void setUnknownKingdom(Long unknownKingdom) {
  	this.unknownKingdom = unknownKingdom;
  }

	/**
   * @param countryI18nPrefix the countryI18nPrefix to set
   */
  public void setCountryI18nPrefix(String countryI18nPrefix) {
  	this.countryI18nPrefix = countryI18nPrefix;
  }

	/**
   * @param systemManager the systemManager to set
   */
  public void setSystemManager(SystemManager systemManager) {
  	this.systemManager = systemManager;
  }

	/**
   * @param dataResourceManager the dataResourceManager to set
   */
  public void setDataResourceManager(DataResourceManager dataResourceManager) {
  	this.dataResourceManager = dataResourceManager;
  }

	/**
   * @param selectedHostKey the selectedHostKey to set
   */
  public void setSelectedHostKey(String selectedHostKey) {
  	this.selectedHostKey = selectedHostKey;
  }

	/**
   * @param selectedCountryKey the selectedCountryKey to set
   */
  public void setSelectedCountryKey(String selectedCountryKey) {
  	this.selectedCountryKey = selectedCountryKey;
  }
}