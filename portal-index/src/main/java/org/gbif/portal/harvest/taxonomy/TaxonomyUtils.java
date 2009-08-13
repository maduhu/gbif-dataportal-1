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
package org.gbif.portal.harvest.taxonomy;

import java.sql.Timestamp;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.gbif.portal.dao.RelationshipAssertionDAO;
import org.gbif.portal.dao.RemoteConceptDAO;
import org.gbif.portal.dao.TaxonConceptDAO;
import org.gbif.portal.dao.TaxonomyDenormaliserDAO;
import org.gbif.portal.model.LinnaeanRankClassification;
import org.gbif.portal.model.RelationshipAssertion;
import org.gbif.portal.model.RemoteConcept;
import org.gbif.portal.model.TaxonConcept;
import org.gbif.portal.model.TaxonConceptLite;
import org.gbif.portal.model.TaxonName;

/**
 * Utilities for dealing with taxonomies 
 * 
 * @author trobertson
 */
public class TaxonomyUtils {
	
	/**
	 * Threshold for comparisons to decide whether concepts match.  This has been selected so as 
	 * to be lenient in cases in which only the kingdom matches (provided there is only one 
	 * candidate classification in the kingdom).  For example, comparing:
	 * 
	 * Plantae	Magnoliophyta	Magnoliopsida	Apiales			Apiaceae		Oenanthe
	 * Plantae	Tracheophyta	Dicotyledoneae	Umbelliferales	Umbelliferae	Oenanthe
	 * 30/30 	0/10			0/10			0/10			0/30			-
	 * 
	 * This gives a total of 30/90 or 33. 
	 */
	public static final int COMPARISON_THRESHOLD = 33;

	/**
	 * DAOs
	 */
	protected TaxonConceptDAO taxonConceptDAO;
	protected RemoteConceptDAO remoteConceptDAO;
	protected TaxonomyDenormaliserDAO taxonomyDenormaliserDAO;
	protected RelationshipAssertionDAO relationshipAssertionDAO;
	
	/**
	 * Names that should be ignored from any classification
	 */
	protected Set<String> namesToIgnoreUppercase = new HashSet<String>();
	
	/**
	 * The name to use when the kingdom is not known.
	 * This will be created and all concepts stored in it
	 * Defaults to "Unknown"
	 */
	public String nameOfUnknownKingdom = "Unknown";
	
	/**
	 * Logger
	 */
	protected Log logger = LogFactory.getLog(getClass());

	/**
	 * The theashold at which we decide that a match is acceptable
	 * Defaults to 33
	 */
	protected int confidenceThreshold = 33;
	
	/**
	 * When paging over the concepts, use this size
	 * Default is 10000
	 */
	protected int pageSize = 10000;
	
	/**
	 * Lets the user modifiy any ranks after the regular expression parsing.
	 * This is because the marker list may get pretty long, so handling it all in RegEx is both a massive amount
	 * of configuration, and also slow to process due to the amount of reg ex matches
	 */
	protected Map<String, Integer> infraspecifiMarkerMappingsUppercase = new HashMap<String, Integer>();
	
	/**
	 * Denormalise the taxonomy for the provider id given
	 * This will clear the denormalised version of the taxonomy and then rebuild it
	 *   - Kingdom and it's children are set to self point 
	 *   - Phylum and it's children are set to self point
	 *   .
	 *   .
	 *   .
	 *   - Species and it's children are set to self point
	 *  
	 *  Then all children of minor ranks are set, in order of rank.  This can be done since the highest taxa minor ranks themselves
	 *  are either roots (e.g. no higher parents) or have the parent set from above.  If there are several minor ranks between major ranks,
	 *  since this is an ordered set, they will be handled.  The next major rank, should it be a child of a minor rank will also be handled. 
	 * 
	 * Users should not that this is capable of taking a long time to complete for large taxonomies (hours?) 
	 * @param id to denormalise
	 */
	public void denormalisedTaxonomyForProvider(long id) {
		logger.info("Clearing the taxonomy for provider[" + id +"]");
		long time = System.currentTimeMillis();
		taxonomyDenormaliserDAO.clearDenormalisedDataForProvider(id);
		logger.info("Taxonomy for provider[" + id +"] cleared in " + (((1 + System.currentTimeMillis() - time))/1000) + " secs");
		
		logger.info("Getting the providers ranks");
		time = System.currentTimeMillis();
		List<Integer> ranks = taxonomyDenormaliserDAO.getDistinctRanksForProvider(id);
		logger.info("Provider has " + ranks.size() + " ranks.  Determined in " + (((1 + System.currentTimeMillis() - time))/1000) + " secs");
		for (int rank : ranks) {
			logger.info("Denormalising rank: " + rank);
			time = System.currentTimeMillis();
			taxonomyDenormaliserDAO.copyParentDenormalisationForRankAndForProvider(id, rank);
			logger.info("Rank [" + rank + "] denormalised in " + (((1 + System.currentTimeMillis() - time))/1000) + " secs");
		}
	}
	
	/**
	 * Denormalise the taxonomy for the resource id given
	 * This will clear the denormalised version of the taxonomy and then rebuild it
	 *   - Kingdom and it's children are set to self point 
	 *   - Phylum and it's children are set to self point
	 *   .
	 *   .
	 *   .
	 *   - Species and it's children are set to self point
	 *  
	 *  Then all chilren of minor ranks are set, in order of rank.  This can be done since the highest taxa minor ranks themselves
	 *  are either roots (e.g. no higher parents) or have the parent set from above.  If there are several minor ranks between major ranks,
	 *  since this is an ordered set, they will be handled.  The next major rank, should it be a child of a minor rank will also be handled. 
	 * Users should not that this is capable of taking a long time to complete for large taxonomies (hours?) 
	 * @param id to denormalise
	 */
	public void denormalisedTaxonomyForResource(long id) {
		logger.info("Clearing the taxonomy for resource[" + id +"]");
		long time = System.currentTimeMillis();
		taxonomyDenormaliserDAO.clearDenormalisedDataForResource(id);
		logger.info("Taxonomy for resource[" + id +"] cleared in " + (((1 + System.currentTimeMillis() - time))/1000) + " secs");
		
		logger.info("Getting the resources ranks");
		time = System.currentTimeMillis();
		List<Integer> ranks = taxonomyDenormaliserDAO.getDistinctRanksForResource(id);
		logger.info("Resource has " + ranks.size() + " ranks.  Determined in " + (((1 + System.currentTimeMillis() - time))/1000) + " secs");
		for (int rank : ranks) {
			if (rank >= 1000) { 
				logger.info("Denormalising rank: " + rank);
				time = System.currentTimeMillis();
				taxonomyDenormaliserDAO.copyParentDenormalisationForRankAndForResource(id, rank);
				logger.info("Rank [" + rank + "] denormalised in " + (((1 + System.currentTimeMillis() - time))/1000) + " secs");
			}
		}
	}
	
	/**
	 * Checks that there are no duplicate ranks in the classification, removing an arbitrary one and order the list
	 * @param classification That needs to be ordered and checked
	 */
	public void ensureNoDuplicateRanksAndOrder(List<TaxonConceptLite> classification) {
		logClassification(toListOfTaxonName(classification), "Classification before sorting and removal of duplicates:");
		
		// sort the classification
		Collections.sort(classification, new java.util.Comparator<TaxonConceptLite>() {
			public int compare(TaxonConceptLite o1, TaxonConceptLite o2) {
				if (o1.getRank() < o2.getRank()) {
					return -1;
				} else if (o1.getRank() == o2.getRank()){
					return 0;
				} else {
					return 1;
				}
			}
		});		
				
		// Iterate through the classification, removing all duplicate ranks
		int lastRank = -1;
		for (Iterator<TaxonConceptLite> it = classification.iterator(); it.hasNext (); ) {
			TaxonConceptLite concept = it.next ();
			if (concept.getRank().intValue() == lastRank) {
				// remove from the iterator to avoid java.util.ConcurrentModificationException
				it.remove ();
			}
			lastRank = concept.getRank().intValue(); 
		}
		logClassification(toListOfTaxonName(classification), "Classification after sorting and removal of duplicates:");		
	}

	/**
	 * Logs the classification at debug level
	 * @param classification To log
	 */
	public void logClassification(List<TaxonName> classification, String prefix) {
		if (logger.isDebugEnabled()) {
			StringBuffer sb = new StringBuffer();
			if (prefix != null) {
				sb.append(prefix + "\n");
			}
			for (TaxonName concept : classification) {
				sb.append(" - " + concept.toString() + "\n");
			}
			logger.debug(sb.toString().trim());
		}
	}

	/**
	 * This will create a List of TaxonConceptLite from the name, and then call 
	 * @see TaxonomyUtils.synchronise(List<TaxonConceptLite, long long);
	 * @param classification To synchronise
	 * @param dataProviderId That the classification is to be synchronised with
	 * @param dataResourceId That the classification is to be synchronised with
	 * @param taxonomicPriority The priority ranking of the classification in generating portal taxonomy and views
	 * @return The most significant concept in the classification (regardless of whether it was newly created or not)
	 */
	public TaxonConceptLite synchroniseNames(List<TaxonName> classification, long dataProviderId, long dataResourceId, int taxonomicPriority) {
		return synchronise(toTaxonConceptLiteList(classification, dataProviderId, dataResourceId, taxonomicPriority), dataProviderId, dataResourceId);
	}
	
	/**
	 * Creates a list of unsaved concepts for the names provided
	 * @param nameList To create concepts for
	 * @param dataProviderId The provider
	 * @param dataResourceId The resource
	 * @param taxonomicPriority The priority ranking of the classification in generating portal taxonomy and views
	 * @return The list of concepts
	 */
	public List<TaxonConceptLite> toTaxonConceptLiteList(List<TaxonName> nameList, long dataProviderId, long dataResourceId, int taxonomicPriority) {
		List<TaxonConceptLite> concepts = new LinkedList<TaxonConceptLite>();
		for (TaxonName name : nameList) {
			concepts.add(toTaxonConceptLite(name, dataProviderId, dataResourceId, taxonomicPriority));
		}
		return concepts;		
	}
	
	/**
	 * Creates an accepted concept for the name and provider and resource given
	 * @param name To use
	 * @param dataProviderId The provider
	 * @param dataResourceId The resource
	 * @param taxonomicPriority The priority ranking of the classification in generating portal taxonomy and views
	 * @return An unpersisted concept for the name provided
	 */
	public TaxonConceptLite toTaxonConceptLite(TaxonName name, long dataProviderId, long dataResourceId, int taxonomicPriority) {
		TaxonConceptLite concept = new TaxonConceptLite();
		concept.setAccepted(true);
		concept.setDataProviderId(dataProviderId);
		concept.setDataResourceId(dataResourceId);
		concept.setRank(name.getRank());
		concept.setTaxonName(name);		
		concept.setPriority(taxonomicPriority);		
		return concept;		
	}
	
	
	/**
	 * This will synchronise the classification with that of the dataResource given (This will not sync merged taxonomies -
	 * the data resource must be known).
	 * 
	 * The algorthim starts at the top of the classification and works down.
	 * It finds any concepts that already exist in the classification which have a parent id in the list of previously handled 
	 * concepts.  Thus when handling "Puma" from the classification Animalia, Chordata, Puma, Puma concolor it will see if there 
	 * is a "Puma" with a parent of Animalia OR Chordata.  The taxonomy may not be as full as the classification provided...  
	 * 
	 * @param classification To synchronise
	 * @param dataProviderId That the classification is to be synchronised with
	 * @param dataResourceId That the classification is to be synchronised with
	 * @return The most significant concept in the classification (regardless of whether it was newly created or not)
	 */
	public TaxonConceptLite synchronise(List<TaxonConceptLite> classification, long dataProviderId, long dataResourceId) {
		logger.debug("Synchronising classification to dataResourceId[" + dataResourceId + "] dataProviderId[" + dataProviderId + "]");
		if (classification == null || classification.size()==0) {
			logger.warn("Received an empty classification");
			return null;
		}
		
		// Update infraspecific marker ranks - e.g. detect f. var. etc
		updateInfraspecificMarkerRanks(classification);
		
		// This will ensure for example that there are no K,P,G,G,S
		ensureNoDuplicateRanksAndOrder(classification);
		
		// the persisted classification effectively
		List<TaxonConceptLite> persistedConcepts = new LinkedList<TaxonConceptLite>();
		
		// maintain a list of the persisted taxon concept ids
		List<Long> persistedConceptIds = new LinkedList<Long>();
		
		// loop through the names in the classification, making sure they are persisted,
		// cleaning up (e.g. filling in) any previously persisted taxonomic tree
		List<TaxonName> classificationNames = new LinkedList<TaxonName>();
		for (TaxonConceptLite taxonConcept : classification) {
			TaxonName taxonName = taxonConcept.getTaxonName();
			logger.debug("Synchronising TaxonName: " + taxonName);
			
			classificationNames.add(taxonName);
			
			TaxonConceptLite persisted = taxonConcept;
			
			// the one supplied may be in the taxonomy already so ignore it in that case
			// or it may be from another resource
			if (persisted.getId() == null || persisted.getId() < 1 
					|| (taxonConcept.getDataResourceId() != null && taxonConcept.getDataResourceId().longValue() != dataResourceId)) {
				// get any persisted concepts for the name and rank that have a parent concept equal to that already handled
				persisted = getTaxonConceptForClassification(dataProviderId,
														     dataResourceId,
														     classificationNames,
														     COMPARISON_THRESHOLD);
			}
			
			if (persisted != null) {
				logger.debug("There is a persisted concept for " + taxonName);
				logger.debug("persistedConceptIds: " + persistedConceptIds);
				logger.debug("Persisted parent id: " + persisted.getParentId());
				// the previously persisted concept may not have as much info as this one
				// E.g. Maybe persisted was Animalia Mammalia, but now we know Chordata is in there...
				if (persistedConceptIds.size()>0 &&
						!persistedConceptIds.get(persistedConceptIds.size()-1).equals(persisted.getParentId())) {
					
					TaxonConceptLite parent = persistedConcepts.get(persistedConcepts.size()-1);
					
					// make sure that the new parent is actually a more significant rank
					Long oldParentId = persisted.getParentId();
					if (oldParentId != null && oldParentId > 0) {
						TaxonConceptLite oldParent = taxonConceptDAO.getTaxonConcept(oldParentId);
						if (oldParent.getRank().intValue() < parent.getRank().intValue()) {
							logger.debug("Updating the parent of a previously persisted concept");
							taxonConceptDAO.updateParent(persisted.getId(),parent.getId());
						} else {
							logger.debug("Not updating the old parent as the rank is same or lower");
						}
					} else {
						logger.debug("Updating the parent of a previously persisted concept");
						taxonConceptDAO.updateParent(persisted.getId(),parent.getId());
					}
					
					// it is soemtimes (and not very often so this extra DB hit should not be a performance problem)
					// the case that you import a concept that is not accepted, and then find one within the same 
					// taxonomy that is - this means it for sure needs to be accepted
					if ((dataResourceId == 1 && taxonConcept.isSecondary() 
							|| taxonConcept.isAccepted())
						&& (!persisted.isAccepted())) {
						logger.debug("Updating a previously stored concept [" + persisted.getId() + "] that was not accepted to be accepted");
						taxonConceptDAO.updateAccepted(persisted.getId(),true);
					}
					
				}
				persistedConcepts.add(persisted);
				persistedConceptIds.add(persisted.getId());
			} else {
				logger.debug("Creating a new concept for DR[" + dataResourceId + "]: " + taxonName);
				TaxonConcept tc = new TaxonConcept();
				tc.setTaxonName(taxonName);
				tc.setRank(taxonName.getRank());
				tc.setDataProviderId(dataProviderId);
				tc.setDataResourceId(dataResourceId);
				if (dataResourceId == 1 && taxonConcept.isSecondary()) {
					tc.setAccepted(false);
				} else {
					tc.setAccepted(taxonConcept.isAccepted());
				}
				tc.setNubConcept(taxonConcept.isNubConcept());
				tc.setPartnerConceptId(taxonConcept.getPartnerConceptId());
				tc.setSecondary(taxonConcept.isSecondary());
				tc.setPriority(taxonConcept.getPriority());
				if (persistedConceptIds.size()>0) {
					TaxonConceptLite parent = persistedConcepts.get(persistedConcepts.size()-1);
					tc.setParentId(parent.getId());	
				}
				long id = taxonConceptDAO.create(tc);
				persistedConcepts.add(tc);
				persistedConceptIds.add(id);
			}
		}
		
		return (persistedConcepts.size() > 0) ? persistedConcepts.get(persistedConcepts.size()-1) : null;
	}
	
	/**
	 * Inspects any infraspecific markers and updates the rank of the taxon name and the taxon concept
	 * @param classification
	 */
	public void updateInfraspecificMarkerRanks(List<TaxonConceptLite> classification) {
		for (TaxonConceptLite tcl : classification) {
			String marker = tcl.getTaxonName().getInfraSpecificMarker(); 
			if (marker != null) {
				logger.debug("Inspecting the infraspecific marker to see if the rank needs modified: " + marker);
				if (infraspecifiMarkerMappingsUppercase.containsKey(marker.toUpperCase())) {
					if (tcl.getTaxonName().getRank() != infraspecifiMarkerMappingsUppercase.get(marker.toUpperCase())) {
						logger.debug("Marker [" + marker + "] found.  Setting rank from [" + tcl.getTaxonName().getRank() + "] to [" + infraspecifiMarkerMappingsUppercase.get(marker.toUpperCase()) + "]");
						tcl.setRank(infraspecifiMarkerMappingsUppercase.get(marker.toUpperCase()));
						tcl.getTaxonName().setRank(infraspecifiMarkerMappingsUppercase.get(marker.toUpperCase()));
					} else {
						logger.debug("Marker [" + marker + "] found and is already set correctly");
					}
					
				} else {
					logger.debug("Marker [" + marker + "] not found in list of markers to modify - rank will remain unchanged");
				}
			}
		}
	}
	
	/**
	 * A utility that will effectively ensure that the taxonomy from one data resource is represented fully in another.
	 * For all concepts that exists in the source, the target is checked to see if there exists a concept representing the same
	 * classification (note that the target may be a more complete classification that the source).  If the concept does not exist,
	 * then the concept is created. 
	 * 
	 * Typically this method would be used to build a NUB taxonomy.  Taxonomic data resources would be imported with allowCreateKingdoms first,
	 * and then inferred taxonomies would be imported with unknownKingdoms collated.
	 * 
	 * This will import accepted concepts and then non accepted concepts in order of rank
	 * 
	 * @param sourceDataResourceId The resource holding the concepts that are to be imported into the target  
	 * @param targetDataResourceId The target resource to ensure encapsualtes all concepts in the source
	 * @param targetDataProviderId The data provider for the resource owning the taxonomy being built - this MUST own the targetDataResourceId
	 * @param allowCreateUnknownKingdoms If this is set to false then the TaxonomyUtils.nameOfUnknownKingdom is used for any kingdom that 
	 * @param majorRanksOnly If this is set to true, then only major ranks will be imported
	 * @param unpartneredOnly If this is set to true, then only concepts with no partner concept id will be imported
	 * is not represented in the target taxonomy.  If set to true, then the kingdoms are imported from the source.  
	 */
	public void importTaxonomyFromDataResource(long sourceDataResourceId, long targetDataResourceId, long targetDataProviderId, boolean allowCreateUnknownKingdoms, boolean majorRanksOnly, boolean unpartneredOnly) {
		List<Integer> ranksToImport = null;
		if (unpartneredOnly) {
			ranksToImport = taxonConceptDAO.getUnpartneredRanksWithinResource(sourceDataResourceId);
		} else {
			ranksToImport = taxonConceptDAO.getRanksWithinResource(sourceDataResourceId);
		}
		
		logger.debug("There are " + ranksToImport.size() + " ranks to import from data resource[" + sourceDataResourceId + "]: " + ranksToImport);
		
		for (int rank : ranksToImport) {
			logger.info("Importing accepted concepts of rank " + rank + " from source data resource[" + sourceDataResourceId + "] to target data resource[" + targetDataResourceId + "]");
			importTaxonomyFromDataResource(sourceDataResourceId, targetDataResourceId, targetDataProviderId, allowCreateUnknownKingdoms, majorRanksOnly, rank, true, unpartneredOnly);
			logger.info("Importing non-accepted concepts of rank " + rank + " from source data resource[" + sourceDataResourceId + "] to target data resource[" + targetDataResourceId + "]");
			importTaxonomyFromDataResource(sourceDataResourceId, targetDataResourceId, targetDataProviderId, allowCreateUnknownKingdoms, majorRanksOnly, rank, false, unpartneredOnly);
		}
	}
	
	/**
	 * Imports the ranks taxonomy from one resource to another for the given parameters
	 * @param sourceDataResourceId The source resource
	 * @param targetDataResourceId The target resource
	 * @param targetDataProviderId The target provider
	 * @param allowCreateUnknownKingdoms Control flag
	 * @param majorRanksOnly Control flag
	 * @param rank The rank to import
	 * @param accepted Control flag - will import only accepted / non accepted concepts
	 * @param unpartneredOnly Control flag - to set whether we want to import only unpartnered concepts
	 */
	protected void importTaxonomyFromDataResource(long sourceDataResourceId, long targetDataResourceId, long targetDataProviderId, boolean allowCreateUnknownKingdoms, boolean majorRanksOnly, int rank, boolean accepted, boolean unpartnered) {
		boolean hasMore = true;
		long minId = 0;
		while (hasMore) {
			logger.info("Getting concepts of rank[" + rank + "] with minimum id["+minId+"] and accepted[" + accepted + "] unpartneredOnly[" + unpartnered + "]");
			List<List<TaxonConceptLite>> classifications = taxonConceptDAO.getClassificationsOf(rank, sourceDataResourceId, false, accepted, minId, pageSize, unpartnered);
			if (accepted) {
				logger.info("Received " + classifications.size() + " accepted concepts of rank[" + rank + "] unpartneredOnly[" + unpartnered + "]");
			} else {
				logger.info("Received " + classifications.size() + " non accepted concepts of rank[" + rank + "] unpartneredOnly[" + unpartnered + "]");
			}
			
			for (List<TaxonConceptLite> classification : classifications) {
				if (classification.size()>0) {
					minId = classification.get(classification.size()-1).getId();
				}
				classification = removeUnwantedConcepts(classification);
				if (majorRanksOnly) {
					classification = removeMinorRanks(classification);
				}	
				
				if (classification.size()>0) {
					// store the importing one
					long importingConceptId = classification.get(classification.size()-1).getId();
					logger.debug("Finding target id");
					TaxonConceptLite nub = synchroniseAtLowestJoinPoint(classification, targetDataProviderId, targetDataResourceId, allowCreateUnknownKingdoms);
					logger.debug("Target id: " + nub.getId());
					logger.debug("Setting " + importingConceptId + " to partner " + nub.getId());
					taxonConceptDAO.updatePartnerConcept(importingConceptId, nub.getId());
				}				
			}
			// see if there needs to be another page received
			hasMore = (classifications.size()>=pageSize);
		}
	}	
	
	
	/**
	 * Using the configured names to ignore, removes any concepts from the classification that are not wanted
	 * @return The trimmed classification
	 */
	public List<TaxonConceptLite> removeUnwantedConcepts(List<TaxonConceptLite> classification) {
		List<TaxonConceptLite> newClassification = new LinkedList<TaxonConceptLite>();
		for (TaxonConceptLite concept : classification) {
			boolean add = true;
			for (String nameToIgnore : getNamesToIgnoreUppercase()) {
				if (StringUtils.equalsIgnoreCase(nameToIgnore, concept.getTaxonName().getCanonical())) {
					add = false;
				}
			}
			if (add) {
				newClassification.add(concept);
			}			
		}
		return newClassification;
	}
	
	/**
	 * Using the configured names to ignore, removes any concepts from Linnaean ranks
	 * @return The trimmed classification
	 */
	public void removeUnwantedNames(LinnaeanRankClassification classification) {
		if (shouldIgnore(classification.getKingdom())) {
			classification.setKingdom(null);
		}
		if (shouldIgnore(classification.getPhylum())) {
			classification.setPhylum(null);
		}
		if (shouldIgnore(classification.getKlass())) {
			classification.setKlass(null);
		}
		if (shouldIgnore(classification.getOrder())) {
			classification.setOrder(null);
		}
		if (shouldIgnore(classification.getFamily())) {
			classification.setFamily(null);
		}
		if (shouldIgnore(classification.getGenus())) {
			classification.setGenus(null);
		}
		if (shouldIgnore(classification.getScientificName())) {
			classification.setScientificName(null);
		}
	}

	protected boolean shouldIgnore(String name) {
		for (String nameToIgnore : getNamesToIgnoreUppercase()) {
			if (StringUtils.equalsIgnoreCase(nameToIgnore, name)) {
				return true;
			}
		}
		return false;
	}
	
	
	/**
	 * Using the configured names to ignore, removes any concepts from the classification that are not wanted
	 * @return The trimmed classification
	 */
	public List<TaxonName> removeUnwantedNames(List<TaxonName> classification) {
		List<TaxonName> newClassification = new LinkedList<TaxonName>();
		for (TaxonName name : classification) {
			boolean add = true;
			for (String nameToIgnore : getNamesToIgnoreUppercase()) {
				if (StringUtils.equalsIgnoreCase(nameToIgnore, name.getCanonical())) {
					logger.debug("Ignoring name: " + name);
					add = false;
				} 
			}
			if (add) {
				newClassification.add(name);
			}			
		}
		return newClassification;
	}	
	
	/**
	 * Removes any minor ranks
	 * @return The trimmed classification
	 */
	public List<TaxonConceptLite> removeMinorRanks(List<TaxonConceptLite> classification) {
		List<TaxonConceptLite> newClassification = new LinkedList<TaxonConceptLite>();
		for (TaxonConceptLite concept : classification) {
			if (concept.getRank() % 1000 == 0) {
				newClassification.add(concept);
			}
		}
		return newClassification;
	}
	
	/**
	 * This will merge the given classification into the target resource taxonomy.
	 * Only the required concepts are created from the lowest join point.  That is to say a classification of
	 * 
	 * - Animalia Chordata Felidae Puma Puma concolor
	 * 
	 * Merged into a taxonomy containing
	 * 
	 * - Animalia Felidae Puma
	 * 
	 * Would only create "Puma concolor", since the join point is Felidae
	 * 
	 * @param classification To synchronise
	 * @param targetProviderId The target provider
	 * @param targetResourceId The target taxonomy resource
	 * @param createUnknownKingdoms Flag to determine if kingdoms can be created during the syncronising process
	 * @return The most significant concept in the classification (regardless of
	 * whether it was newly created or not)
	 */
	public TaxonConceptLite synchroniseAtLowestJoinPoint(List<TaxonConceptLite> classification, long targetProviderId, long targetResourceId, boolean createUnknownKingdoms) {
		TaxonConceptLite targetConcept = getTaxonConceptForClassification(targetProviderId, targetResourceId, toListOfTaxonName(classification), COMPARISON_THRESHOLD);
		if (targetConcept != null) {
			logger.debug("The concept already exists in target resource [id: " + targetResourceId + "]: " + toListOfTaxonName(classification));
			
			// still synchronise to ensure that any higher ranks are filled in 
			if (!createUnknownKingdoms) {
				for (TaxonConceptLite sourceTaxonConcept : classification) {
					if (sourceTaxonConcept.getRank() == 1000) {
						if (taxonConceptDAO.getTaxonConcept(sourceTaxonConcept.getTaxonName().getCanonical(), 
														    1000, 
														    targetResourceId, 
														    null) == null) {
							// The classification includes an unknown kingdom - replace it
							if (logger.isDebugEnabled())
								logger.debug("Replacing the unknown kingdom " + sourceTaxonConcept.getTaxonName().getCanonical());
							sourceTaxonConcept.setTaxonName(new TaxonName(getNameOfUnknownKingdom(), null, 1000));
							sourceTaxonConcept.setAccepted(false);
							sourceTaxonConcept.setPriority(100);
							break;
						}
					}
				}
			}
			
			targetConcept = synchronise(classification, targetProviderId, targetResourceId);
			
			// Handle the situation in which this concept was added to the nub as a non-accepted secondary
			// taxon but the taxon in the supplied classification is not secondary.
			if (targetResourceId == 1) {
				TaxonConceptLite mostSignificantConcept = classification.get(classification.size() - 1);
				if (    !mostSignificantConcept.isSecondary() 
				 	 && targetConcept.isSecondary() 
				 	 && !targetConcept.isAccepted()) {
					processSecondaryConcepts(mostSignificantConcept, targetConcept);
				}
			}
			
			return targetConcept;
			
		} else {
			logger.debug("Not found in target resource [id: " + targetResourceId + "] - determining join point to resource for classification: " + toListOfTaxonName(classification));
			TaxonConceptLite targetJoinPoint = getJoinPoint(toListOfTaxonName(classification), null, targetProviderId, targetResourceId);
			
			if (targetJoinPoint != null) {
				logger.debug("Join point: " + targetJoinPoint.getTaxonName());
				if (targetJoinPoint.getRank() == 1000) {
					logger.debug("This classification is joining onto kingdom in the target taxonomy: " +  toListOfTaxonName(classification));
				}
				
				// build a new list of the concepts to synchronise
				List<TaxonConceptLite> toSync = new LinkedList<TaxonConceptLite>();
				for (TaxonConceptLite concept : classification) {
					if (concept.getRank() > targetJoinPoint.getRank()) {
						if (!createUnknownKingdoms && concept.getRank() == 1000) {
							concept.setTaxonName(new TaxonName(getNameOfUnknownKingdom(), null, 1000));
							concept.setAccepted(false);
							concept.setPriority(100);
						}
						concept.setPartnerConceptId(concept.getId());
						concept.setNubConcept(true);
						concept.setId(null);
						toSync.add(concept);
					}
				}
				
				TaxonConceptLite conceptToAdd = targetJoinPoint;
				while (conceptToAdd!=null && conceptToAdd.getParentId()!=null) {
					toSync.add(0, conceptToAdd);
					conceptToAdd = taxonConceptDAO.getTaxonConceptLite(conceptToAdd.getParentId());
				}
				logger.debug("Synchronising: " + toListOfTaxonName(toSync));
				
				TaxonConceptLite insertedConcept = synchronise(toSync, targetProviderId, targetResourceId);
				
				// For the nub taxonomy, handle secondary concepts 
				if (targetResourceId == 1) {
					processSecondaryConcepts(insertedConcept, targetJoinPoint);
				}
				
				return insertedConcept;
			} else {
				logger.debug("No join point found for: " + toListOfTaxonName(classification));
				for (TaxonConceptLite concept : classification) {
					concept.setPartnerConceptId(concept.getId());
					concept.setNubConcept(true);
					concept.setId(null);
				}
				
				// if it's trying to make a kingdom, check it is allowed
				if (classification.get(0).getRank() == 1000
						&& createUnknownKingdoms) {
						logger.warn("Classification cannot be joined onto the target in any way - (creating a new kingdom): " + toListOfTaxonName(classification));
						return synchronise(classification, targetProviderId, targetResourceId);
				} else {
					TaxonConceptLite oldKingdom = null;
					// removed the kingdom that can't be created if any
					if (classification.get(0).getRank() == 1000) {
						oldKingdom = classification.remove(0);
					}
					// add an unknown kingdom
					TaxonConceptLite tcl = new TaxonConceptLite();
					tcl.setTaxonName(new TaxonName(getNameOfUnknownKingdom(), null, 1000));
					tcl.setRank(1000);
					tcl.setNubConcept(true);
					tcl.setAccepted(false);
					tcl.setPriority(100);
					tcl.setPartnerConceptId(oldKingdom == null ? null : oldKingdom.getPartnerConceptId());
					classification.add(0, tcl);
					logger.debug("Classification cannot be joined onto the target in any way - (using the \"unknown\" kingdom): " + toListOfTaxonName(classification));
					return synchronise(classification, targetProviderId, targetResourceId);
				}					
			}	
		}
	}

	/**
	 * The nub taxonomy may include concepts which are added because they appear as part of an
	 * occurrence data resource, but which should not normally be displayed because the associated
	 * occurrences are attached to concepts elsewhere in the taxonomy.  These are marked as 
	 * secondary, non-accepted concepts.  If however they subsequently receive children with data
	 * or data of their own, they need to be accepted.
	 * 
	 * This method handles setting the isAccepted flag for nub concepts based on the isSecondary flag.
	 * 
	 * Presuppositions:
	 * 1. Concepts in occurrence resource taxonomies are marked as secondary if they have no associated
	 *    records, and primary otherwise.
	 * 2. When these concepts are added to the nub taxonomy, the secondary flag is preserved
     * 3. All concepts inserted into the nub with the secondary flag are initially marked as not accepted.
     * 4. When a primary, accepted concept (from any source) is joined to a concept in the nub which
     *    is marked as secondary and non-accepted, the entire classification for the new concept should
     *    be marked a accepted.  (Note that concepts marked as primary and non-accepted are ones for
     *    which a resource like CoL has given us a different accepted concept - in these cases we do not
     *    accept the non-accepted concept. 
	 * @param insertedConcept
	 * @param parentConcept
	 */
	private void processSecondaryConcepts(TaxonConceptLite testConcept, TaxonConceptLite conceptToProcess) {
		// There is nothing to do if the new concept is secondary
		if (!testConcept.isSecondary() && testConcept.isAccepted()) {
			// This concept is not secondary - ensure the entire hierarchy is accepted
			while (conceptToProcess != null && conceptToProcess.isSecondary() && !conceptToProcess.isAccepted()) {
				conceptToProcess.setAccepted(true);
				taxonConceptDAO.updateAccepted(conceptToProcess.getId(), true);
				conceptToProcess = taxonConceptDAO.getTaxonConceptLite(conceptToProcess.getParentId());
			}
		}
	}

	/**
	 * Gets the join join point for the classification provided in the target providers taxonomy.
	 * This should be called AFTER it has been deduced that there is no concept in the target taxonomy representing the lowest 
	 * taxa in the classification.  This is required because this method will ignore the lowest taxa and go up the
	 * tree to find the first non clashing classification in the target taxonomy.
	 * Should the provided classification not have a kingdom, then the one provided is added.  This is for safety checking,
	 * as without the kingdom, the results could be undesirable (Note, that it will except a NULL kingdom which will not be added - this should
	 * not be supplied unless you REALLY know what you are doing...)
	 * @param classification To find where it can be merged to
	 * @param kingdom To use should the classification not have one already.  The kingdom is added to the classification
	 * should it be missing
	 * @return The taxon concept within the target that can be merged to, at the appropriate rank
	 */
	public TaxonConceptLite getJoinPoint(List<TaxonName> classification, String kingdom, long targetProviderId, long targetResourceId) {
		TaxonConceptLite targetConcept = null;
		// get the kingdom in there if needbe
		if (StringUtils.isNotEmpty(kingdom)) {
			if (classification.get(0).getRank() != 1000) {
				classification.add(0,new TaxonName(kingdom, null, 1000));
			}
		} else {
			if (classification.get(0).getRank() != 1000) {
				classification.add(0,new TaxonName(getNameOfUnknownKingdom(), null, 1000));
			}
		}
			
		// It must be at least 2 big - Kingdom + something, or else it is just a kingdom
		// Since this is called AFTER it has been deduced that the concept does not exist then 
		// what we actually have is a kingdom that is not in the target - HIGHLY SUSPICIOUS
		// Thus we only deal with classifications larger that 2
		if (classification.size() >= 2){
			
			// build a copy of the classification to shrink down - don't modify the original
			// ignoring the last one as we know that it is not in the target
			List<TaxonName> workingClassification = new LinkedList<TaxonName>(classification);
			workingClassification.remove(workingClassification.size()-1);
			
			// go from lowest to highest taxa finding a point at which this can be merged into the target
			for (int i=workingClassification.size()-1; i>=0; i--) {
				targetConcept = getTaxonConceptForClassification(targetProviderId, targetResourceId, workingClassification, COMPARISON_THRESHOLD);
				if (targetConcept != null) {
					break;
				} else {
					workingClassification.remove(workingClassification.size()-1);
				}
			}
		}
		
		if (targetConcept != null) {
			logger.debug("Target taxonomy join point found: " + targetConcept.getTaxonName() + " for classification: " + classification);
		} else {
			logger.debug("No concept join point found for classification in the target taxonomy: " + classification);
		}
		return targetConcept;
	}
	
	/**
	 * Gets the taxon concept for the classification provided if it exists.
	 * 
	 * This method is intended to be used in the following subtly different situations:
	 * 
	 * 1. When importing the taxonomy for a well-managed taxonomic database.  In this case
	 *    it is expected that each taxon will consistently appear with the same classification
	 *    in all records in which it appears.  If the same taxon name appears in multiple 
	 *    slightly different records, it is to be expected that the instances are cases of 
	 *    homonymy and should be preserved as separate entities.
	 *    
	 * 2. When importing the taxonomy for a database not intended to be taxonomically
	 *    authoritative (e.g. a collection database).  In this case the assumption is that
	 *    the data may include varying classifications for the same taxon and that the
	 *    system should be more cautious about presenting them as different.
	 *    
	 * 3. When merging the taxonomy from a well-managed taxonomic database into the portal
	 *    nub taxonomy.  In this case the classifications may not match those from other
	 *    (even authoritative) sources but the portal should be able to maintain any
	 *    distinctions made in the taxonomic database itself.
	 *    
	 * 4. When merging the taxonomy from a database not intended to be taxonomically
	 *    into the portal nub taxonomy.  In this case the classifications will often not
	 *    match those from other sources and the portal should not assume that they 
	 *    represent different taxa unless there are very strong reasons to do so.
	 *    
	 * The key requirements to handle these cases are as follows:
	 * 
	 * A. In situations 1 and 2, the requirement is to import the resource 
	 *    taxonomy with as much fidelity as possible.  The method should therefore
	 *    preserve every apparent classification and only merge those which provide
	 *    different compatible subsets of the same classification.  If the resource
	 *    taxonomy is not at all well-managed, this may mean that there are many different
	 *    representations for the same taxon in different locations in the same 
	 *    taxonomy.  This will be handled when the taxonomy is tied to the portal
	 *    nub taxonomy.  More generally, when importing taxonomies, this method should
	 *    only return completely compatible matches from the same resource's taxonomy.
	 *    Little harm will befall the portal from over-distinguishing taxa at
	 *    this point, since the important stage will be in situation 3, when the 
	 *    dataset is merged into the portal taxonomy.  
	 *    
	 *    REQUIREMENT - INCLUDE A MODE THAT FINDS ONLY FULLY COMPATIBLE CLASSIFICATIONS
	 *    
	 *    NOTE: special rules may be required for some databases that require unique 
	 *    handling.  The hardest cases will relate to records representing different 
	 *    concepts for the same taxon name with the same classification.  Special 
	 *    processing outside this method will be required to handle such cases.
	 *  
	 * B. In situation 4 on the other hand, the requirement is to minimise the number
	 *    of cases in which the same taxon is split into multiple locations in the
	 *    taxonomy.  The method should therefore be able to determine whether a 
	 *    suitable join point already exists and to use it, even if a significant
	 *    proportion of the classification is different.
	 *    
	 *    REQUIREMENT - INCLUDE A MODE THAT SELECTS THE MOST SUITABLE CLASSIFICATION IF
	 *                  ONE EXISTS
	 *    
	 * C. In situation 3, the requirement is again to reuse an existing taxon if one
	 *    is suitable, but the method should not conflate taxa that have been explicitly
	 *    separated by the resource.
	 *    
	 *    REQUIREMENT - INCLUDE A MODE THAT SELECTS THE MOST SUITABLE CLASSIFICATION 
	 *                  BUT RESPECTS DIFFERENT TAXA SHARING THE SAME NAME WITHIN THE
	 *                  SOURCE CLASSIFICATION
	 *                  
	 * D. In situations 3 and 4, the portal should detect cases in which it is not
	 *    possible safely to merge a candidate taxon with any of the existing taxa under
	 *    the given name, and should create a special taxon concept to store the
	 *    ambiguous information.  It should avoid multiplying these disambiguation 
	 *    concepts for the same name, since otherwise the taxonomy will become
	 *    impossibly complex.
	 *    
	 *    REQUIREMENT - INCLUDE A MODE THAT CAN CREATE DISAMBIGUATION TAXA AS NEEDED
	 *    
	 * These requirements are handled as follows:
	 * 
	 * i. If the request is to find a concept in taxonomies other than the portal
	 *    taxonomy, full compatibility is required (i.e. rejecting any classification 
	 *    with a different name in the same position.  Otherwise no match is returned.  
	 *    This addresses requirement A
	 *    
	 * ii. If the request is to find a concept in the portal taxonomy (resource 1), this
	 *    method will find the most suitable classification using a reasonably lenient
	 *    matching algorithm (threshold set to 33).  This addresses requirement B.
	 *    
	 * iii. If the matching algorithm in ii. cannot distinguish between multiple concepts,
	 *    and the request is for the portal taxonomy, a disambiguation taxon is returned.
	 *    This addresses requirement D.
	 *    
	 * iv. In cases in which the resource taxonomy may include homonyms, it is the 
	 *    responsibility of code using this method to determine which resource concepts
	 *    to associate with existing concepts, and which resource concepts require new
	 *    concepts.  This cannot be handled at this level.  This addresses requirement
	 *    C.
	 *    
	 * NOTE: Import of authoritative taxonomies requires additional logic around this
	 *       method to ensure correct import and merging of homonyms. 
	 * 
	 * @param targetProviderId To define what the target taxonomy is
	 * @param targetResourceId To define what the target taxonomy is
	 * @param classification That needs to be allocated a target Concept Id
	 * @param threshold Minimum acceptable measure for classificationsComparator()
	 * @return The target concept or null if non found
	 */
	public TaxonConceptLite getTaxonConceptForClassification(Long targetProviderId, 
															 Long targetResourceId, 
															 List<TaxonName> classification,
															 int threshold) {
		// This method is simply a public wrapper around the potentially recursive 
		// implementation.  It should always be called externally in such a way that
		// it can recurse, but it needs to be able to invoke itself with or without
		// further recursion.
		
		boolean disambiguate = (targetResourceId == 1);
		boolean requireFullCompatibility = (targetResourceId != 1);
		
		return getTaxonConceptForClassification(targetProviderId, 
												targetResourceId, 
												classification, 
												threshold, 
												disambiguate,
												requireFullCompatibility,
												true);
	}
	
	/**
	 * Private implementation allowing for recursion
	 * 
	 * @param targetProviderId To define what the target taxonomy is
	 * @param targetResourceId To define what the target taxonomy is
	 * @param classification That needs to be allocated a target Concept Id
	 * @param threshold Minimum acceptable measure for classificationsComparator()
	 *        (use COMPARISON_THRESHOLD for normal behaviour)
	 * @param disambiguate True if the method should create disambiguation concepts
	 * @param requireFullCompatibility True if only fully compatible matches are allowed 
	 *        (N.B. leave threshold at its normal level when using this option)
	 * @param recurse True if recursion is allowed
	 * @return The target concept or null if non found
	 */
	private TaxonConceptLite getTaxonConceptForClassification(Long targetProviderId, 
															  Long targetResourceId, 
															  List<TaxonName> classification,
															  int threshold,
															  boolean disambiguate,
															  boolean requireFullCompatibility,
															  boolean recurse) {
		TaxonConceptLite targetConcept = null;

		if (classification != null && classification.size() > 0) {
			TaxonName mostSignificantName = classification.get(classification.size() - 1);
			
			List<List<TaxonConceptLite>> targetOptions = null;
			if (targetResourceId != null) {
				targetOptions = taxonConceptDAO.getClassificationsOf(mostSignificantName.getCanonical(),
						// be lenient - get the one that does not care authorship...
						//mostSignificantName.getAuthor(),  
						mostSignificantName.getRank(), 
						targetResourceId);
			} else {
				targetOptions = taxonConceptDAO.getClassificationsWithinProviderOf(mostSignificantName.getCanonical(),
						// be lenient - get the one that does not care authorship...
						//mostSignificantName.getAuthor(),  
						mostSignificantName.getRank(), 
						targetProviderId);
			}
			
			// We measure the classifications as two sets, those that are accepted as part of the taxonomy and those
			// that are not.  If there is a suitable accepted taxon, we always take it over a better matching
			// unaccepted taxon.  This is because many of these unaccepted taxa will have been added precisely
			// because they appear in the hierarchy of the present record - using these would mean that the 
			// merged taxonomy would just include all classifications from all resources without any filter.
			int bestMeasure = -1;
			int bestUnacceptedMeasure = -1;
			List<List<TaxonConceptLite>> bestClassifications = null;
			List<List<TaxonConceptLite>> bestUnacceptedClassifications = null;
			for (List<TaxonConceptLite> targetClassificationTC : targetOptions) {
				List<TaxonName> target = toListOfTaxonName(targetClassificationTC);
				int measure = classificationsComparator(target, classification, mostSignificantName.getRank() - 1, requireFullCompatibility);
				if (targetClassificationTC.get(targetClassificationTC.size() - 1).isAccepted()) {
					if (measure > bestMeasure) {
						bestMeasure = measure;
						bestClassifications = new LinkedList<List<TaxonConceptLite>>();
					}
					if (measure == bestMeasure) {
						bestClassifications.add(targetClassificationTC);
					}
				} else {
					if (measure > bestUnacceptedMeasure) {
						bestUnacceptedMeasure = measure;
						bestUnacceptedClassifications = new LinkedList<List<TaxonConceptLite>>();
					}
					if (measure == bestUnacceptedMeasure) {
						bestUnacceptedClassifications.add(targetClassificationTC);
					}
				}
			}
			
			// No accepted taxa - try unaccepted
			if (bestMeasure < threshold) {
				bestMeasure = bestUnacceptedMeasure;
				bestClassifications = bestUnacceptedClassifications;
			}
			
			if (bestMeasure >= threshold) {
				if (bestClassifications.size() == 1) {
					List<TaxonConceptLite> bestClassification = bestClassifications.get(0);
					targetConcept = bestClassification.get(bestClassification.size() - 1);
				} else {
					if (recurse) {
						// We have an embarassment of riches here - let's see if we can choose
						// one of these with a little more work...
						// If the supplied classification includes another real taxon name, let's
						// see if we can find a taxon concept for it.
						int ancestorIndex;
						for (ancestorIndex = classification.size() - 2; ancestorIndex >= 0; ancestorIndex--) {
							TaxonName ancestorName = classification.get(ancestorIndex);
							if (    ancestorName != null 
								 && ancestorName.getCanonical() != null
								 && !namesToIgnoreUppercase.contains(ancestorName.getCanonical().toUpperCase())) {
								break;
							}
						}
						
						if (ancestorIndex >= 0) {
							// Here is the one to try - note that this is recursive
							// Create a shorter classification to test
							List<TaxonName> ancestorClassification = new LinkedList<TaxonName>();
							int i = 0;
							for (TaxonName name : classification) {
								ancestorClassification.add(name);
								if (i == ancestorIndex) {
									break;
								} else {
									i++;
								}
							}
							// Call ourselves recursively to find the ancestor, but do not trigger 
							// disambiguation at the ancestor level
							TaxonConceptLite ancestorConcept = getTaxonConceptForClassification(targetProviderId,
																								targetResourceId,
																								ancestorClassification,
																								threshold,
																								false,
																								requireFullCompatibility,
																								true);
							if (ancestorConcept != null) {
								// We managed to find the immediate ancestor of this name in the classification
								// Let's build a classification based on that concept's classification with the
								// original name from the request and then try using that (with both recursion
								// and disambiguation disabled).
								List<TaxonConceptLite> ancestorConcepts = getClassificationConcepts(ancestorConcept.getId());
								List<TaxonName> newClassification = toListOfTaxonName(ancestorConcepts);
								newClassification.add(mostSignificantName);
								targetConcept = getTaxonConceptForClassification(targetProviderId,
																				 targetResourceId,
																				 newClassification,
																				 threshold,
																				 disambiguate,
																				 requireFullCompatibility,
																				 false);
							}
						}
					}
					
					// If we still cannot distinguish the classifications should we set up a 
					// disambiguation concept for them?
					if (targetConcept == null && disambiguate) {
						if (targetConcept == null) {
							targetConcept = createDisambiguationConcept(targetProviderId, targetResourceId, bestClassifications);
						}
					}
				}
			}
		} 

		return targetConcept;
	}
	
	/**
	 * A disambigution concept is a taxon concept with very low priority (so it is never
	 * shown in search and browse operations) which is created to attach information to
	 * the nub when the classification for the information is ambiguous between one or 
	 * more real taxon concepts.  The disambiguation concept will include isAmbiguousSynonym
	 * relationships with all the potentially matching concepts.
	 * 
	 * This method checks for the any existing disambiguation concept for the
	 * name in the given resource and otherwise creates a new one.  It them makes sure that
	 * this concept has appropriate relationships with all the potentially matching concepts.
	 * 
	 * @param dataProviderId id of data provider for which disambiguation concept is required
	 * @param dataResourceId id of data resource for which disambiguation concept is required
	 * @param classifications classifications which need to be disambiguated
	 * @return disambiguation concept
	 */
	private TaxonConceptLite createDisambiguationConcept(Long dataProviderId, Long dataResourceId, List<List<TaxonConceptLite>> classifications) {
		TaxonConcept disambiguationConcept = null;
		
		TaxonName name = classifications.get(0).get(classifications.get(0).size() - 1).getTaxonName();
		
		disambiguationConcept = taxonConceptDAO.getDisambiguationConcept(name.getCanonical(), dataResourceId);
		List<RelationshipAssertion> existingAssertions = null;
		
		if (disambiguationConcept == null) {
			disambiguationConcept = new TaxonConcept();
			
			disambiguationConcept.setDataProviderId(dataProviderId);
			disambiguationConcept.setDataResourceId(dataResourceId);
			disambiguationConcept.setTaxonName(name);
			disambiguationConcept.setPriority(TaxonConceptDAO.DISAMBIGUATION_PRIORITY);
			disambiguationConcept.setAccepted(false);
			disambiguationConcept.setNubConcept(dataResourceId == 1);
			disambiguationConcept.setRank(name.getRank());
	
			taxonConceptDAO.create(disambiguationConcept);
			existingAssertions = new LinkedList<RelationshipAssertion>();
		} else {
			existingAssertions = relationshipAssertionDAO.getRelationshipAssertionsForFromConcept(disambiguationConcept.getId());
		}

		for (List<TaxonConceptLite> classification : classifications) {
			long toConceptId = classification.get(classification.size() - 1).getId();
			boolean exists = false;
			for (RelationshipAssertion assertion : existingAssertions) {
				if (assertion.getRelationshipType() == 1 && assertion.getToConceptId() == toConceptId) {
					exists = true;
				}
			}
			if (!exists) {
				relationshipAssertionDAO.create(disambiguationConcept.getId(), toConceptId, 1);
			}
		}
		
		return disambiguationConcept;		
	}

	/**
	 * Checks that the root of the sourceList classification is in the target classification, provided that the
	 * root concept is greater than or equal to the lowestRankToCheck.  Should the root in the sourceList be a lower taxa
	 * than that provided, the check is ignored.  Thus this can be used to "Check if the kingdoms are the same, if there is
	 * one in the sourceList".
	 * @param sourceList That is being checked
	 * @param targetList That the source is to be compared to (E.g. A Nub classification)
	 * @param lowestRankToCheck The lowest rank of taxa that should be checked in the source classification
	 * @param rankMustExist Set to true if there must be a match made in the root node
	 * @return true if there is a clash, false if there is no clash
	 */
	public boolean rootSourceConceptClashesWithTarget(List<TaxonName> sourceList, List<TaxonConceptLite> targetList, int lowestRankToCheck, boolean rankMustExist) {
		TaxonName root = sourceList.get(0);
		boolean ranksCompared = false; // indicator to see if there has been a match made - they may not clash but no comparisons are made...
		for (TaxonConceptLite targetConcept : targetList) {
			if (targetConcept.getRank()>lowestRankToCheck) { // only check down to this rank
				break;
			} else if (targetConcept.getTaxonName().getRank() == root.getRank()) {
					ranksCompared = true;
					if (!StringUtils.equals(targetConcept.getTaxonName().getCanonical(), root.getCanonical())) {
						logger.debug("The root names do do match, so the classifications clash.  Rank[" +
								root.getRank() + "], Source[" + root.getCanonical() + "], Target["+ targetConcept.getTaxonName().getCanonical() + "]");
						return true;
					}
			}
		}
		// if they were not compared but should have been
		if (rankMustExist && !ranksCompared) {
			return true;
		}
		return false;
	}
	
	/**
	 * A utility to get a confidence rating of how equal 2 classifications appear.
	 * The procedure is as follows:
	 * 
	 * - Find the major taxa in each classification (those with ranks that are exact multiples of 1000)
	 * - Align the taxa in case of the same name being assigned to different ranks 
	 * - Assign top ratings (up to 100) for fully matching classifications or classifications with 
	 *   differ only because of a null in one or other classification (-1 per null) 
	 * - Assign high ratings (up to 95) if the genus and family match and neither is null (on the grounds
	 *   that homonymous genera in homonymous families seem +/- impossibly unlikely) - give extra marks
	 *   if the root of the family name differs from the root of the genus name
	 * - Otherwise assign a rating based on the individual rank matches, weighted to give extra for 
	 *   matching kingdom and family and subtracting small amounts for null elements. 
	 * 
	 * @param sourceList The source classification to compare to the target
	 * @param targetList The target classification would typically be the backbone/nub classification that you are matching against
	 * It is normal that this would be from the most complete taxonomy - e.g. Nub 
	 * @param lowestRankInclusive The lowest rank (inclusive) to compare down to within the source classification.
	 * It should be noted that if you specify 7000 (Species) but only supply a source classification with a Kingdom and Phylum,
	 * then only 2 ranks will be used in the percentage calculation.
	 * @param requireFullCompatibility if true, reject any classification with any incompatible names
	 * @return A percentage that represents the confidence of match of the classifications
	 */
	public int classificationsComparator(List<TaxonName> sourceList, 
									     List<TaxonName> targetList, 
									     int lowestRankInclusive, 
									     boolean requireFullCompatibility) {
		// Keep rank in range
		if (lowestRankInclusive < 1000 || lowestRankInclusive > 8000) {
			lowestRankInclusive = 8000;
		}
		
		// Get the names tidily into arrays to simplify later steps - ignore ignorable names
		String source[] = new String[lowestRankInclusive/1000];
		String target[] = new String[lowestRankInclusive/1000];
		
		for (TaxonName name : sourceList) {
			if (name.getRank() % 1000 == 0) {
				int index = (name.getRank() / 1000) - 1;
				if (index >= 0 && index < source.length && !namesToIgnoreUppercase.contains(name.getCanonical().toUpperCase())) {
					source[index] = name.getCanonical();
				}
			}
		}

		for (TaxonName name : targetList) {
			if (name.getRank() % 1000 == 0) {
				int index = (name.getRank() / 1000) - 1;
				if (index >= 0 && index < target.length && !namesToIgnoreUppercase.contains(name.getCanonical().toUpperCase())) {
					target[index] = name.getCanonical();
				}
			}
		}
		
		// Align intermediate taxa (in case of taxa shifting rank in the Kingdom to Family range, but not handling suffix changes) - we only compare down to family - even this may be overkill
		// This just runs through both taxonomies in turn moving any names found lower in the second taxonomy to the level from the first taxonomy
		// This just checks for ranks higher than genus
		int lastToCompare = (source.length < 5) ? source.length - 1 : 4;
		
		for (int s = 0; s < lastToCompare; s++) {
			if (source[s] != null) {
				if (target[s] == null || !source[s].equals(target[s])) {
					for (int i = s + 1; i <= lastToCompare; i++) {
						if (target[i] != null && source[s].equals(target[i])) {
							// Move array elements
							target[s] = target[i];
							while (i > s) {
								target[i--] = null;
							}
							break;
						}
					}
				}
			}
		}
		
		for (int s = 0; s < lastToCompare; s++) {
			if (target[s] != null) {
				if (source[s] == null || !target[s].equals(source[s])) {
					for (int i = s + 1; i <= lastToCompare; i++) {
						if (source[i] != null && target[s].equals(source[i])) {
							// Move array elements
							source[s] = source[i];
							while (i > s) {
								source[i--] = null;
							}
							break;
						}
					}
				}
			}
		}
		
		// Now assign a value in the range 0 (no match) to 100 (total match) for the relationship between the classifications
		int measure = 0;
		
		if (compareRange(source, target, 0, source.length - 1, true)) {
			// Full match allowing for nulls
			measure = 100;
			
			// Deduct a little for each null
			for (int i = 0; i < source.length; i++) {
				if (source[i] == null || target[i] == null) {
					measure--;
				}
			}
		} else if (requireFullCompatibility) {
			measure = 0;
		} else if (compareRange(source, target, 4, 5, false)){
			// genus and family match - very good sign
			// Get start of genus
			int genusLength = source[5].length();
			String genusRoot = source[5].substring(0, genusLength > 3 ? source[5].length() - 2 : 1);
			if (!source[4].startsWith(genusRoot)) {
				// Genus and family have different roots - even more significant
				measure = 95;
			} else {
				measure = 90;
			}
		} else {
			// Track null matches separately because they count for nothing if there is no other match
			// Note that totally null matches will already have been handled.
			int nullMeasure = 0;
			int maxPossible = 30;
			// See if kingdoms match
			boolean nullKingdom = source[0] == null || target[0] == null;
			boolean kingdomMatch = nullKingdom || StringUtils.equals(source[0], target[0]);
			if (kingdomMatch) {
				if (nullKingdom) {
					nullMeasure += 15;
				} else {
					measure += 30;
				}
			}
			
			for (int i = 1; i < source.length; i++) {
				int value = (i == 4) ? 30 : 10;
				if (source[i] == null || target[i] == null) {
					nullMeasure += value - 1;
				} else if (source[i].equals(target[i])) {
					measure += value;
				}
				maxPossible += value;
			}
			
			if (measure > 0) {
				// We have some reason for thinking a match may exist - add in the null matches
				measure += nullMeasure;
			}
			
			measure = (measure * 100) / maxPossible;
		}
		
		/*
		StringBuffer sb = new StringBuffer();
		for (TaxonName s : sourceList) {
			sb.append(s.getCanonical());
			sb.append(" ");
		}
		sb.append(" <--> ");
		for (TaxonName s : targetList) {
			sb.append(s.getCanonical());
			sb.append(" ");
		}
		sb.append("MEASURE: ");
		sb.append(measure);
		logger.debug(sb.toString());
		*/

		return measure;
	}
	
	/**
	 * Compare a subrange of two arrays of strings
	 * @param source Array
	 * @param target Array
	 * @param startIndex (inclusive)
	 * @param endIndex (inclusive)
	 * @param treatNullsAsMatch
	 * @return true if all strings match
	 */
	private boolean compareRange(String[] source, String[] target, int startIndex, int endIndex, boolean treatNullsAsMatch) {
		boolean match = true;
		
		if(startIndex >= 0 && endIndex < source.length) {
			for (int i = startIndex; i <= endIndex; i++) {
				if (source[i] == null || target[i] == null) {
					if (!treatNullsAsMatch) {
						match = false;
						break;
					}
				} else {
					if (!source[i].equals(target[i])) {
						match = false;
						break;
					}
				}
			}
		} else {
			match = false;
		}
		
		return match;
	}

	/**
	 * Utility to convert a list of concepts to a list of the contained names
	 * This is particularly useful as a name list may be logged directly
	 * @param conceptList To convert
	 * @return The List of TaxonName that the concept list represents
	 */
	public List<TaxonName> toListOfTaxonName(List<TaxonConceptLite> conceptList) {
		List<TaxonName> nameList = new LinkedList<TaxonName>();
		for (TaxonConceptLite concept : conceptList) {
			nameList.add(concept.getTaxonName());
		}
		return nameList;
	}
	
	/**
	 * @return Returns the taxonConceptDAO.
	 */
	public TaxonConceptDAO getTaxonConceptDAO() {
		return taxonConceptDAO;
	}

	/**
	 * @param taxonConceptDAO The taxonConceptDAO to set.
	 */
	public void setTaxonConceptDAO(TaxonConceptDAO taxonConceptDAO) {
		this.taxonConceptDAO = taxonConceptDAO;
	}

	/**
	 * @return Returns the taxonomyDenormaliserDAO.
	 */
	public TaxonomyDenormaliserDAO getTaxonomyDenormaliserDAO() {
		return taxonomyDenormaliserDAO;
	}

	/**
	 * @param taxonomyDenormaliserDAO The taxonomyDenormaliserDAO to set.
	 */
	public void setTaxonomyDenormaliserDAO(
			TaxonomyDenormaliserDAO taxonomyDenormaliserDAO) {
		this.taxonomyDenormaliserDAO = taxonomyDenormaliserDAO;
	}

	/**
	 * @return Returns the namesToIgnoreUppercase.
	 */
	public Set<String> getNamesToIgnoreUppercase() {
		return namesToIgnoreUppercase;
	}

	/**
	 * @param namesToIgnoreUppercase The namesToIgnoreUppercase to set.
	 */
	public void setNamesToIgnoreUppercase(Set<String> namesToIgnoreUppercase) {
		this.namesToIgnoreUppercase = namesToIgnoreUppercase;
	}

	/**
	 * @return Returns the confidenceThreshold.
	 */
	public int getConfidenceThreshold() {
		return confidenceThreshold;
	}

	/**
	 * @param confidenceThreshold The confidenceThreshold to set.
	 */
	public void setConfidenceThreshold(int confidenceThreshold) {
		this.confidenceThreshold = confidenceThreshold;
	}

	/**
	 * @return Returns the nameOfUnknownKingdom.
	 */
	public String getNameOfUnknownKingdom() {
		return nameOfUnknownKingdom;
	}

	/**
	 * @param nameOfUnknownKingdom The nameOfUnknownKingdom to set.
	 */
	public void setNameOfUnknownKingdom(String nameOfUnknownKingdom) {
		this.nameOfUnknownKingdom = nameOfUnknownKingdom;
	}
	
	/**
	 * @param remoteId
	 * @param dataResourceId
	 * @return
	 */
	public List<RemoteConcept> findRemoteConceptsByRemoteIdAndIdTypeAndDataResourceId(String remoteId, long idType, long dataResourceId) {
		return remoteConceptDAO.findByRemoteIdAndIdTypeAndDataResourceId(remoteId, idType, dataResourceId);
	}

	/**
	 * This method deals only with remote concepts of type 1 ("local ids")
	 * @param tc
	 * @param remoteConceptId
	 */
	public Long synchroniseRemoteConcepts(TaxonConceptLite tc, String remoteConceptId) {
		Long id = null;

		if (remoteConceptId != null) {
			List<RemoteConcept> conceptsForTaxon = remoteConceptDAO.findByTaxonConceptId(tc.getId());

			RemoteConcept remoteConcept = null;
			
			if (conceptsForTaxon != null) {
				for (RemoteConcept concept : conceptsForTaxon) {
					if (concept.getIdType() == 1) {
						remoteConcept = concept;
						id = remoteConcept.getId();
						remoteConcept.setRemoteId(remoteConceptId);
						remoteConcept.setModified(new Timestamp(System.currentTimeMillis()));
						remoteConceptDAO.updateOrCreate(remoteConcept);
					}
				}
			}
			
			if (remoteConcept == null) {
				remoteConcept = new RemoteConcept(tc.getId(), 1, remoteConceptId);
				id = remoteConceptDAO.create(remoteConcept);
			}
		}
		
		return id;
	}

	/**
	 * Link taxon concepts (identified via the ids for associated remote concept records)
	 * with a parent taxon concept (identified via the remoteId string for the parent)
	 * 
	 * @param parentConceptId
	 * @param childConceptIds
	 */
	public void linkTaxonConceptsToParent(Long parentConceptId, List<Long> childConceptIds) {
		taxonConceptDAO.linkTaxonConceptsToParent(parentConceptId, childConceptIds);
	}

	/**
	 * @return the remoteConceptDAO
	 */
	public RemoteConceptDAO getRemoteConceptDAO() {
		return remoteConceptDAO;
	}

	/**
	 * @param remoteConceptDAO the remoteConceptDAO to set
	 */
	public void setRemoteConceptDAO(RemoteConceptDAO remoteConceptDAO) {
		this.remoteConceptDAO = remoteConceptDAO;
	}

	/**
	 * Clean up out-of-date remote concepts
	 * @param dataResourceId
	 * @param timer
	 */
	public void deleteRemoteConceptsOlderThan(Long dataResourceId, Long timer) {
		remoteConceptDAO.deleteRemoteConceptsOlderThan(dataResourceId, timer);
	}

	/**
	 * If taxon_name and taxon_concept records have rank 0 but their parents have
	 * assigned ranks, set the rank to a value lower than the parent rank.
	 * 
	 * This method needs to loop to ensure that the ranks are cleared even if they
	 * are grandchildren or even more remote from the nearest ranked ancestor
	 * 
	 * @param dataResourceId
	 */
	public void updateUnknownRanks(Long dataResourceId) {
		// Don't continue forever...
		for (int i = 0; i < 10; i++) {
			if (!taxonConceptDAO.updateUnknownRanks(dataResourceId)) {
				// No ranks to update...
				break;
			}
		}
		
	}

	/**
	 * @param taxonConceptId
	 * @return
	 */
	public List<TaxonConceptLite> getClassificationConcepts(long taxonConceptId) {
		return taxonConceptDAO.getClassificationConcepts(taxonConceptId);
	}

	/**
	 * @param parentId
	 * @return
	 */
	public TaxonConceptLite getTaxonConceptLite(Long id) {
		return taxonConceptDAO.getTaxonConceptLite(id);
	}

	/**
	 * @param taxonConceptId
	 * @param rank
	 * @return
	 */
	public void updateRank(TaxonConceptLite concept, Integer rank) {
		concept.setRank(rank);
		taxonConceptDAO.updateRank(concept.getId(), rank);
	}

	/**
	 * @return Returns the pageSize.
	 */
	public int getPageSize() {
		return pageSize;
	}

	/**
	 * @param pageSize The pageSize to set.
	 */
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	/**
	 * @return the relationshipAssertionDAO
	 */
	public RelationshipAssertionDAO getRelationshipAssertionDAO() {
		return relationshipAssertionDAO;
	}

	/**
	 * @param relationshipAssertionDAO the relationshipAssertionDAO to set
	 */
	public void setRelationshipAssertionDAO(
			RelationshipAssertionDAO relationshipAssertionDAO) {
		this.relationshipAssertionDAO = relationshipAssertionDAO;
	}

	/**
	 * @return Returns the infraspecifiMarkerMappingsUppercase.
	 */
	public Map<String, Integer> getInfraspecifiMarkerMappingsUppercase() {
		return infraspecifiMarkerMappingsUppercase;
	}

	/**
	 * @param infraspecifiMarkerMappingsUppercase The infraspecifiMarkerMappingsUppercase to set.
	 */
	public void setInfraspecifiMarkerMappingsUppercase(
			Map<String, Integer> infraspecifiMarkerMappingsUppercase) {
		this.infraspecifiMarkerMappingsUppercase = infraspecifiMarkerMappingsUppercase;
	}
}