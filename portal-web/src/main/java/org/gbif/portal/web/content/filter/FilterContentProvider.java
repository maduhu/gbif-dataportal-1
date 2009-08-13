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
package org.gbif.portal.web.content.filter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.gbif.portal.dto.PropertyStoreTripletDTO;
import org.gbif.portal.dto.geospatial.CountryDTO;
import org.gbif.portal.dto.taxonomy.BriefTaxonConceptDTO;
import org.gbif.portal.dto.taxonomy.TaxonConceptDTO;
import org.gbif.portal.dto.util.EntityType;
import org.gbif.portal.dto.util.TaxonRankType;
import org.gbif.portal.service.GeospatialManager;
import org.gbif.portal.service.ServiceException;
import org.gbif.portal.service.TaxonomyManager;
import org.gbif.portal.util.propertystore.PropertyStore;
import org.gbif.portal.web.content.ContentProvider;
import org.gbif.portal.web.content.ContentView;
import org.gbif.portal.web.filter.CriteriaDTO;
import org.gbif.portal.web.filter.CriterionDTO;
import org.gbif.portal.web.filter.FilterDTO;
import org.gbif.portal.web.filter.FilterMapWrapper;
import org.gbif.portal.web.filter.FilterUtils;

/**
 * A Content provider for views requiring filter information. 
 * 
 * @author dmartin
 */
public class FilterContentProvider implements ContentProvider {

	private static Log logger = LogFactory.getLog(FilterContentProvider.class);		
	/**The webapp property store. In particular contains filter details. **/
	protected PropertyStore webappPropertyStore;
	/**The namespace of the webapp property store. Needed to access it**/
	protected String namespace;
	
	/** taxonomy manager for taxonomic queries */
	protected TaxonomyManager taxonomyManager;
	/** geospatial manager for taxonomic queries */
	protected GeospatialManager geospatialManager;
	
	/**The name for the set of occurrence filters for taxon ranks **/
	protected FilterMapWrapper taxonomyFilters;	
	/**The name for the set of occurrence filters **/
	protected FilterMapWrapper occurrenceFilters;	
	/**The name for the set of occurrence filters **/
	protected FilterMapWrapper occurrenceTaxonFilters;
	
	/** The filter name for the publication filter**/
	protected String publicationFilterName;	
	/** The filter used for scientific name searches */
	protected FilterDTO classificationOccurrenceFilter;
	/** The filter used for scientific name searches */
	protected FilterDTO scientificNameOccurrenceFilter;
	/** The filter used for bounding box searches */	
	protected FilterDTO boundingBoxOccurrenceFilter;
	/** The filter used for country searches */
	protected FilterDTO countryOccurrenceFilter;	
	/** The filter used for country searches */
	protected FilterDTO regionOccurrenceFilter;	
	/** The filter used for country searches */
	protected FilterDTO geoConsistencyOccurrenceFilter;
	/** The filter used for country searches */
	protected FilterDTO georeferencedOccurrenceFilter;	
	/** The Taxon Taxonomy Filter */
	protected FilterDTO taxonTaxonomyFilter;
	/** The Rank Taxonomy Filter */	
	protected FilterDTO rankTaxonomyFilter;
	
	protected String cellIdSubject = "SERVICE.OCCURRENCE.QUERY.SUBJECT.CELLID";
	protected String imageSubject = "SERVICE.OCCURRENCE.QUERY.SUBJECT.IMAGEURL";
	protected String occurrenceLatitudeSubject = "SERVICE.OCCURRENCE.QUERY.SUBJECT.LATITUDE";
	protected String occurrenceLongitudeSubject = "SERVICE.OCCURRENCE.QUERY.SUBJECT.LONGITUDE";
	
	protected String collectionCodeSubject = "SERVICE.OCCURRENCE.QUERY.SUBJECT.COLLECTIONCODE";
	protected String institutionCodeSubject = "SERVICE.OCCURRENCE.QUERY.SUBJECT.INSTITUTIONCODE";
	protected String catalogueNumberSubject = "SERVICE.OCCURRENCE.QUERY.SUBJECT.CATALOGUENUMBER";
	
	protected String typeStatusCountSubject = "SERVICE.OCCURRENCE.QUERY.SUBJECT.TYPESTATUSCOUNT";
	protected String geoIssuesSubject = "SERVICE.OCCURRENCE.QUERY.SUBJECT.GEOSPATIALISSUES";
	protected String taxonIssuesSubject = "SERVICE.OCCURRENCE.QUERY.SUBJECT.TAXONOMICISSUES";
	
	protected String isNullPredicate = "SERVICE.QUERY.PREDICATE.ISNULL";
	protected String isNotNullPredicate = "SERVICE.QUERY.PREDICATE.ISNOTNULL";
	protected String notEqualPredicate = "SERVICE.QUERY.PREDICATE.NEQUAL";
	protected String equalPredicate = "SERVICE.QUERY.PREDICATE.EQUAL";	
	protected String gtPredicate = "SERVICE.QUERY.PREDICATE.G";
	
	/** Maps an occurrence triplet to an Entity type name. e.g. OCCURRENCE..TAXON_CONCEPT_ID to TYPE_TAXON */
	protected Map<String, String> triplet2EntityTypeMappings;
	
	/** The taxon entity type */
	protected String taxonEntityType;	
	
	/**
	 * @see org.gbif.portal.web.content.ContentProvider#addContent(org.gbif.portal.web.content.ContentView, java.lang.Object)
	 */
	public void addContent(ContentView cc, HttpServletRequest request, HttpServletResponse response) {
		//not implemented at present
	}

	public void addCollectionCodeTriplet(List<PropertyStoreTripletDTO> triplets, String queryNamespace, String collectionCode) {
		PropertyStoreTripletDTO triplet = new PropertyStoreTripletDTO(queryNamespace, collectionCodeSubject, equalPredicate, collectionCode);
		triplets.add(triplet);
	}	

	public void addInstitutionCodeTriplet(List<PropertyStoreTripletDTO> triplets, String queryNamespace, String instCode) {
		PropertyStoreTripletDTO triplet = new PropertyStoreTripletDTO(queryNamespace, institutionCodeSubject, equalPredicate, instCode);
		triplets.add(triplet);
	}	

	public void addCatalogueNumberTriplet(List<PropertyStoreTripletDTO> triplets, String queryNamespace, String catNo) {
		PropertyStoreTripletDTO triplet = new PropertyStoreTripletDTO(queryNamespace, catalogueNumberSubject, equalPredicate, catNo);
		triplets.add(triplet);
	}	
	
	/**
	 * Add triplets to speciy georeferenced results only.
	 * @param triplets
	 */
	public void addGeoreferencedOnlyOccurrenceTriplets(List<PropertyStoreTripletDTO> triplets, String queryNamespace) {
		PropertyStoreTripletDTO cellIdNotNull = new PropertyStoreTripletDTO(queryNamespace, cellIdSubject, isNotNullPredicate, null);
		triplets.add(cellIdNotNull);
	}	

	/**
	 * Add triplets to speciy georeferenced results only.
	 * @param triplets
	 */
	public void addNotGeoreferencedOccurrenceTriplets(List<PropertyStoreTripletDTO> triplets, String queryNamespace) {
		PropertyStoreTripletDTO cellIdNotNull = new PropertyStoreTripletDTO(queryNamespace, cellIdSubject, isNullPredicate, null);
		triplets.add(cellIdNotNull);
	}		
	
	public void addIgnoreTaxonomicIssuesOccurrenceTriplets(List<PropertyStoreTripletDTO> triplets, String queryNamespace) {
		PropertyStoreTripletDTO noTaxonomicIssues = new PropertyStoreTripletDTO(queryNamespace, taxonIssuesSubject, equalPredicate, 0);
		triplets.add(noTaxonomicIssues);		
	}

	public void addIgnoreGeospatialIssuesOccurrenceTriplets(List<PropertyStoreTripletDTO> triplets, String queryNamespace) {
		PropertyStoreTripletDTO noGeospatialIssues = new PropertyStoreTripletDTO(queryNamespace, geoIssuesSubject, equalPredicate, 0);
		triplets.add(noGeospatialIssues);		
	}
	
	public void addIsTypeStatusOccurrenceTriplets(List<PropertyStoreTripletDTO> triplets, String queryNamespace) {
		PropertyStoreTripletDTO tripleDTO = new PropertyStoreTripletDTO(queryNamespace, typeStatusCountSubject, gtPredicate, 0);
		triplets.add(tripleDTO);		
	}
	
	/**
	 * Specify only records with images associated to them
	 * @param triplets
	 * @param queryNamespace
	 */
	public void addHasImagesOccurrenceTriplets(List<PropertyStoreTripletDTO> triplets, String queryNamespace) {
		PropertyStoreTripletDTO hasImages = new PropertyStoreTripletDTO(queryNamespace, imageSubject, gtPredicate, 0);
		triplets.add(hasImages);
	}

	public void addNoImagesOccurrenceTriplets(List<PropertyStoreTripletDTO> triplets, String queryNamespace) {
		PropertyStoreTripletDTO noImages = new PropertyStoreTripletDTO(queryNamespace, imageSubject, equalPredicate, 0);
		triplets.add(noImages);
	}	
	
	
	/**
	 * Retrieves the correct filter for the rank.
	 * @param rank
	 * @return
	 */
	public FilterDTO getOccurrenceFilterForRank(String rank) {
		if(rank==null)
			return null;
		String filterName = rank;
    	List<FilterDTO> filters =  occurrenceTaxonFilters.getFilters();
    	for (FilterDTO filter: filters){
    		if (filterName.equals(filter.getName()))
    			return filter;
    	}
    	//if no filter supplied, use the scientific name filter
    	return scientificNameOccurrenceFilter;
	}

	/**
	 * Retrieves the correct filter for the rank.
	 * @param rank
	 * @return
	 */
	public FilterDTO getTaxonomyFilterForRank(String rank) {
		if(rank==null)
			return null;		
    	List<FilterDTO> filters = taxonomyFilters.getFilters();
    	for (FilterDTO filter: filters){
    		if (rank.equals(filter.getName()))
    			return filter;
    	}
    	return null;
	}
	
	/**
	 * Creates a set of taxonomy criteria for a taxonomic concept search.
	 * 
	 * @param taxonConcept
	 * @return the criteria for a taxonomic concept search.
	 */
	public CriteriaDTO getTaxonomySearchCriteria(TaxonConceptDTO taxonConcept) {
		CriteriaDTO taxonomyCriteria = new CriteriaDTO();
		List <CriterionDTO> criteria = taxonomyCriteria.getCriteria();
		//add occurrence search criteria for this concept
		criteria.add(new CriterionDTO(taxonTaxonomyFilter.getId(), taxonTaxonomyFilter.getDefaultPredicateId(), taxonConcept.getTaxonName()));
		TaxonRankType taxonRankType = TaxonRankType.getRank(taxonConcept.getRank());
		if(taxonRankType!=null)
			criteria.add(new CriterionDTO(rankTaxonomyFilter.getId(), rankTaxonomyFilter.getDefaultPredicateId(), Integer.toString(taxonRankType.getValue())));
		return taxonomyCriteria;
	}
	
	/**
	 * Create a set of criteria for an occurrence search for this concept.
	 * 
	 * @param taxonConcept
	 * @return
	 */
	public CriteriaDTO getOccurrenceSearchCriteria(TaxonConceptDTO taxonConcept) {
		CriteriaDTO occurrenceCriteria = new CriteriaDTO();
		List <CriterionDTO> criteria = occurrenceCriteria.getCriteria();
		String conceptKey = null;
		if(taxonConcept.getIsNubConcept()){
			conceptKey = taxonConcept.getKey();
		} else {
			conceptKey = taxonConcept.getPartnerConceptKey();
		}
		
		if(conceptKey==null)
			return null;
		
		//add occurrence search critieria for this concept
		CriterionDTO criterionDTO = new CriterionDTO(classificationOccurrenceFilter.getId(), classificationOccurrenceFilter.getDefaultPredicateId(), conceptKey);
		criteria.add(criterionDTO);
		return occurrenceCriteria;
	}
	
	/**
	 * Retrieves a list of entity ids for this query ignoring certain filters and translating others into filters that are cell
	 * density mappable.
	 * 
	 * @param criteria
	 * @return
	 */
	public List<String> getCellDensityEntityIds(CriteriaDTO criteria) {
		
		List<String> entityIds = new ArrayList<String>();
		
		for(CriterionDTO criterion: criteria.getCriteria()){
			if(!criterion.getSubject().equals(boundingBoxOccurrenceFilter.getId())
				&& !geoConsistencyOccurrenceFilter.getId().equals(criterion.getSubject()) 
				&& !georeferencedOccurrenceFilter.getId().equals(criterion.getSubject())
			){
				FilterDTO entityFilter = FilterUtils.getFilterById(occurrenceFilters.getFilters(), criterion.getSubject());
				if(entityFilter.equals(regionOccurrenceFilter)){
					List<CountryDTO> countries = geospatialManager.getCountriesForRegion(criterion.getValue(), null);
					for(CountryDTO country: countries)
						entityIds.add(country.getKey());
				} else {
					entityIds.add(criterion.getValue());
				}
			}
		}
		return entityIds;
	}

	/**
	 * Retrieves the single (non bounding box) entity type for criteria
	 * 
	 * @param criteria
	 * @return
	 */
	public EntityType getCellDensityEntityType(CriteriaDTO criteria) {
		for(CriterionDTO criterion: criteria.getCriteria()){
			if(logger.isDebugEnabled())
				logger.debug("retrieving entity type: "+criterion);
			
			if(!criterion.getSubject().equals(boundingBoxOccurrenceFilter.getId())
					&& !isGeoConsistencyWithNoIssuesFilter(criterion) 
					&& !georeferencedOccurrenceFilter.getId().equals(criterion.getSubject())
				){
				FilterDTO entityFilter = FilterUtils.getFilterById(occurrenceFilters.getFilters(), criterion.getSubject());
				if(logger.isDebugEnabled())
					logger.debug("retrieved entity filter: "+entityFilter);
				
				if(entityFilter.equals(regionOccurrenceFilter)){
					//swap with country filter
					entityFilter = countryOccurrenceFilter;
					logger.debug("swapping region filter to country filter for entity type");
				}
				
				String entityTypeName = triplet2EntityTypeMappings.get(entityFilter.getSubject());
				if(logger.isDebugEnabled())
					logger.debug("entityTypeName:"+entityTypeName);
				EntityType entityType = EntityType.entityTypesByName.get(entityTypeName);
				if(logger.isDebugEnabled())
					logger.debug("entityType:"+entityType);
				
				if(logger.isDebugEnabled())
					logger.debug("returning the entity type: "+entityType);
				return entityType;
			}
		}
		logger.debug("returning null entity type");
		return EntityType.TYPE_ALL;
	}
	
	/**
	 * Returns true if the supplied criterion is for a geo consistency filter.
	 * @param criterion
	 * @return
	 */
	public boolean isGeoConsistencyWithNoIssuesFilter(CriterionDTO criterion){
		if(geoConsistencyOccurrenceFilter.getId().equals(criterion.getSubject())){
			if("0".equals(criterion.getValue()))
				return true;
		}
		return false;
	}

	/**
	 * Retrieves the bounding box criterion if available.
	 * @param criteria
	 * @return
	 */
	public CriterionDTO getBoundingBoxCriterion(CriteriaDTO criteria) {
		for(CriterionDTO criterion: criteria.getCriteria()){
			if(criterion.getSubject().equals(boundingBoxOccurrenceFilter.getId()))
				return criterion;
		}
		return null;
	}

	/**
	 * A query is suitable for cell density if there exists one triplet against of the the supported entity types (taxon, resource, provider, network, country)
	 * and one bounding box query (this could be extended to support lat long).
	 * 
	 * FIXME Need extra step for Classification filter - can only display mutually exclusive classifications
	 * 
	 * @param triplets
	 * @return
	 */
	public boolean isQuerySuitableForCellDensity(CriteriaDTO criteria) throws ServiceException {
		
		String recognisedEntityType = null;
		List<String> ids = new ArrayList<String>();
		
		for(CriterionDTO criterion : criteria.getCriteria()){
			FilterDTO filter = FilterUtils.getFilterById(occurrenceFilters.getFilters(), criterion.getSubject());
			
			//ignore the bounding box filter
			if (boundingBoxOccurrenceFilter.getId().equals(filter.getId()))
				continue;
			//ignore coordinate issues/status filters
			if(isGeoConsistencyWithNoIssuesFilter(criterion) || georeferencedOccurrenceFilter.getId().equals(filter.getId()))
				continue;
			
			//hack to process regions filters as countries
			if(filter.equals(regionOccurrenceFilter))
				filter = countryOccurrenceFilter;
			
			if (!triplet2EntityTypeMappings.containsKey(filter.getSubject()))
				return false;
			
			String entityType = triplet2EntityTypeMappings.get(filter.getSubject());
			if(recognisedEntityType!=null && !recognisedEntityType.equals(entityType))
				return false;
			
			recognisedEntityType = entityType;
			ids.add(criterion.getValue());
		}
		
		//special check for classification filter
		if(recognisedEntityType!=null && taxonEntityType.equals(recognisedEntityType)){
			//TODO this could be made more efficient by full taxonomic comparison
			String recognisedRank = null;
			for(String id: ids){
				BriefTaxonConceptDTO bTc = taxonomyManager.getBriefTaxonConceptFor(id);
				if(recognisedRank==null){
					recognisedRank = bTc.getRank();
				} else {
					if(!bTc.getRank().equals(recognisedRank))
						return false;
				}
			}
		}
		
		return true;
	}
	
	/**
	 * @param namespace the namespace to set
	 */
	public void setNamespace(String namespace) {
		this.namespace = namespace;
	}

	/**
	 * @return the publicationFilterName
	 */
	public String getPublicationFilterName() {
		return publicationFilterName;
	}

	/**
	 * @param publicationFilterName the publicationFilterName to set
	 */
	public void setPublicationFilterName(String publicationFilterName) {
		this.publicationFilterName = publicationFilterName;
	}

	/**
	 * @return the webappPropertyStore
	 */
	public PropertyStore getWebappPropertyStore() {
		return webappPropertyStore;
	}

	/**
	 * @param webappPropertyStore the webappPropertyStore to set
	 */
	public void setWebappPropertyStore(PropertyStore webappPropertyStore) {
		this.webappPropertyStore = webappPropertyStore;
	}

	/**
	 * @param scientificNameOccurrenceFilter the scientificNameOccurrenceFilter to set
	 */
	public void setScientificNameOccurrenceFilter(
			FilterDTO scientificNameOccurrenceFilter) {
		this.scientificNameOccurrenceFilter = scientificNameOccurrenceFilter;
	}

	/**
	 * @return the scientificNameOccurrenceFilter
	 */
	public FilterDTO getScientificNameOccurrenceFilter() {
		return scientificNameOccurrenceFilter;
	}

	/**
	 * @return the countryOccurrenceFilter
	 */
	public FilterDTO getCountryOccurrenceFilter() {
		return countryOccurrenceFilter;
	}

	/**
	 * @param countryOccurrenceFilter the countryOccurrenceFilter to set
	 */
	public void setCountryOccurrenceFilter(FilterDTO countryOccurrenceFilter) {
		this.countryOccurrenceFilter = countryOccurrenceFilter;
	}

	/**
	 * @return the rankTaxonomyFilter
	 */
	public FilterDTO getRankTaxonomyFilter() {
		return rankTaxonomyFilter;
	}

	/**
	 * @param rankTaxonomyFilter the rankTaxonomyFilter to set
	 */
	public void setRankTaxonomyFilter(FilterDTO rankTaxonomyFilter) {
		this.rankTaxonomyFilter = rankTaxonomyFilter;
	}

	/**
	 * @param taxonTaxonomyFilter the taxonTaxonomyFilter to set
	 */
	public void setTaxonTaxonomyFilter(FilterDTO taxonTaxonomyFilter) {
		this.taxonTaxonomyFilter = taxonTaxonomyFilter;
	}

	/**
	 * @param boundingBoxOccurrenceFilter the boundingBoxOccurrenceFilter to set
	 */
	public void setBoundingBoxOccurrenceFilter(FilterDTO boundingBoxOccurrenceFilter) {
		this.boundingBoxOccurrenceFilter = boundingBoxOccurrenceFilter;
	}

	/**
	 * @param classificationOccurrenceFilter the classificationOccurrenceFilter to set
	 */
	public void setClassificationOccurrenceFilter(
			FilterDTO classificationOccurrenceFilter) {
		this.classificationOccurrenceFilter = classificationOccurrenceFilter;
	}

	/**
	 * @param equalPredicate the equalPredicate to set
	 */
	public void setEqualPredicate(String equalPredicate) {
		this.equalPredicate = equalPredicate;
	}

	/**
	 * @param notEqualPredicate the notEqualPredicate to set
	 */
	public void setNotEqualPredicate(String notEqualPredicate) {
		this.notEqualPredicate = notEqualPredicate;
	}

	/**
	 * @param occurrenceLatitudeSubject the occurrenceLatitudeSubject to set
	 */
	public void setOccurrenceLatitudeSubject(String occurrenceLatitudeSubject) {
		this.occurrenceLatitudeSubject = occurrenceLatitudeSubject;
	}

	/**
	 * @param occurrenceLongitudeSubject the occurrenceLongitudeSubject to set
	 */
	public void setOccurrenceLongitudeSubject(String occurrenceLongitudeSubject) {
		this.occurrenceLongitudeSubject = occurrenceLongitudeSubject;
	}

	/**
	 * @param gtPredicate the gtPredicate to set
	 */
	public void setGtPredicate(String gtPredicate) {
		this.gtPredicate = gtPredicate;
	}

	/**
	 * @param isNotNullPredicate the isNotNullPredicate to set
	 */
	public void setIsNotNullPredicate(String isNotNullPredicate) {
		this.isNotNullPredicate = isNotNullPredicate;
	}

	/**
	 * @param isNullPredicate the isNullPredicate to set
	 */
	public void setIsNullPredicate(String isNullPredicate) {
		this.isNullPredicate = isNullPredicate;
	}

	/**
	 * @param occurrenceFilters the occurrenceFilters to set
	 */
	public void setOccurrenceFilters(FilterMapWrapper occurrenceFilters) {
		this.occurrenceFilters = occurrenceFilters;
	}

	/**
	 * @param taxonomyFilters the taxonomyFilters to set
	 */
	public void setTaxonomyFilters(FilterMapWrapper taxonomyFilters) {
		this.taxonomyFilters = taxonomyFilters;
	}

	/**
	 * @param geoConsistencyOccurrenceFilter the geoConsistencyOccurrenceFilter to set
	 */
	public void setGeoConsistencyOccurrenceFilter(
			FilterDTO geoConsistencyOccurrenceFilter) {
		this.geoConsistencyOccurrenceFilter = geoConsistencyOccurrenceFilter;
	}

	/**
	 * @param georeferencedOccurrenceFilter the georeferencedOccurrenceFilter to set
	 */
	public void setGeoreferencedOccurrenceFilter(
			FilterDTO georeferencedOccurrenceFilter) {
		this.georeferencedOccurrenceFilter = georeferencedOccurrenceFilter;
	}

	/**
	 * @param regionOccurrenceFilter the regionOccurrenceFilter to set
	 */
	public void setRegionOccurrenceFilter(FilterDTO regionOccurrenceFilter) {
		this.regionOccurrenceFilter = regionOccurrenceFilter;
	}

	/**
	 * @param geospatialManager the geospatialManager to set
	 */
	public void setGeospatialManager(GeospatialManager geospatialManager) {
		this.geospatialManager = geospatialManager;
	}

	/**
	 * @param taxonomyManager the taxonomyManager to set
	 */
	public void setTaxonomyManager(TaxonomyManager taxonomyManager) {
		this.taxonomyManager = taxonomyManager;
	}

	/**
	 * @param occurrenceTaxonFilters the occurrenceTaxonFilters to set
	 */
	public void setOccurrenceTaxonFilters(FilterMapWrapper occurrenceTaxonFilters) {
		this.occurrenceTaxonFilters = occurrenceTaxonFilters;
	}

	/**
	 * @param taxonEntityType the taxonEntityType to set
	 */
	public void setTaxonEntityType(String taxonEntityType) {
		this.taxonEntityType = taxonEntityType;
	}

	/**
	 * @param triplet2EntityTypeMappings the triplet2EntityTypeMappings to set
	 */
	public void setTriplet2EntityTypeMappings(
			Map<String, String> triplet2EntityTypeMappings) {
		this.triplet2EntityTypeMappings = triplet2EntityTypeMappings;
	}

	public void setCellIdSubject(String cellIdSubject) {
		this.cellIdSubject = cellIdSubject;
	}

	public void setTypeStatusCountSubject(String typeStatusCountSubject) {
		this.typeStatusCountSubject = typeStatusCountSubject;
	}

	public void setGeoIssuesSubject(String geoIssuesSubject) {
		this.geoIssuesSubject = geoIssuesSubject;
	}

	public void setTaxonIssuesSubject(String taxonIssuesSubject) {
		this.taxonIssuesSubject = taxonIssuesSubject;
	}
}