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
package org.gbif.portal.harvest.workflow.activity.col;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.gbif.portal.dao.RelationshipAssertionDAO;
import org.gbif.portal.dao.RemoteConceptDAO;
import org.gbif.portal.dao.TaxonConceptDAO;
import org.gbif.portal.model.RemoteConcept;
import org.gbif.portal.model.TaxonConcept;
import org.gbif.portal.model.TaxonName;
import org.gbif.portal.util.file.DelimitedFileReader;
import org.gbif.portal.util.workflow.BaseActivity;
import org.gbif.portal.util.workflow.ContextCorruptException;
import org.gbif.portal.util.workflow.ProcessContext;


/**
 * This loads (NOT SYNCs) the data from the Catalogue of Life data files (written for the 2007 dump).
 * In short, every record from the taxa table will receive a GBIF concept.   
 * 
 * Basically it loads in a file, and creates (not syncs - it's a loader!!!)
 * taxa as they come in, storing the saved ones in the big old hashmap in the context for
 * children
 * It loads top down so parent_id is always persisted before child, and the COL data is ordered so accepted
 * concept is there first
 * 
 * TODO Code is commented throughout the process, but would be good to put the process in the class header 
 * 
 * @author trobertson
 */
public class TaxaCreator extends BaseActivity {
	/**
	 * Names are self explainatory
	 */
	protected String fileUrl;
	protected String contextKeyMappedIds;
	protected String contextKeyNameCodes;
	protected String contextKeyResourceIds;
	protected String contextKeyDataProviderId;	
	protected String contextKeyRAMap;
	protected TaxonConceptDAO taxonConceptDAO;
	protected RemoteConceptDAO remoteConceptDAO;
	protected RelationshipAssertionDAO relationshipAssertionDAO;
	
	// the file columns
	private static final int COLUMN_TAXA_ID = 0;
	private static final int COLUMN_SCIENTIFIC_NAME = 1;
	private static final int COLUMN_GENUS = 2;
	private static final int COLUMN_SPECIES = 3;
	private static final int COLUMN_INFRASPECIES = 4;
	private static final int COLUMN_INFRASPECIES_MARKER = 5;
	private static final int COLUMN_AUTHOR = 6;
	private static final int COLUMN_RANK = 7;
	private static final int COLUMN_NAME_CODE = 8;
	private static final int COLUMN_PARENT_ID = 9;
	private static final int COLUMN_STATUS_ID = 10;
	private static final int COLUMN_IS_ACCEPTED = 11;
	private static final int COLUMN_DATABASE_ID = 12;
	private static final int COLUMN_PROVIDER_LINK_URL = 13;
	private static final int COLUMN_ACCEPTED_NAME_CODE = 14;
	private static final int COLUMN_SPECIALIST = 15;
	private static final int COLUMN_SCRUTINY_DATE = 16;
	
	
	/**
     * @see org.gbif.portal.util.workflow.BaseMapContextActivity#doExecute(org.gbif.portal.util.workflow.MapContext)
     */
    @SuppressWarnings("unchecked")
	public ProcessContext execute(ProcessContext context) throws Exception {
    	// Initialise the maps
    	Map<String, Long> mappedIds = initialiseMappedIds(context);
    	Map<String, Long> nameCodes = initialiseNameCodeMp(context);
    	Map<String,Map<String,String>> raMap = initialiseRAMap(context);
    	Map<String, Long> resourceIds = (Map<String, Long>) context.get(contextKeyResourceIds, Map.class, true);
    	
    	logger.info("Starting: " + fileUrl);
    	long time = System.currentTimeMillis();
		InputStream is = new FileInputStream(fileUrl);
		DelimitedFileReader reader = new DelimitedFileReader(is, "\t", "\"", true);
	
		if (reader!=null) {
			do {
				// skip not assigned and copy any parent GBIF id to this concept id so it is skipped
				if ("NOT ASSIGNED".equalsIgnoreCase(reader.get(COLUMN_SCIENTIFIC_NAME))) {
					Long parent = mappedIds.get(reader.get(COLUMN_PARENT_ID));
					if (parent!=null) {
						mappedIds.put(reader.get(COLUMN_TAXA_ID), parent);
					}
					
				} else {
					// create the concept
					TaxonConcept tc = createConcept(mappedIds, reader, resourceIds, (Long) context.get(contextKeyDataProviderId, Long.class, true));
					long id = taxonConceptDAO.create(tc);
					mappedIds.put(reader.get(COLUMN_TAXA_ID), id);
					nameCodes.put(reader.get(COLUMN_NAME_CODE), id);
							
					// create the remote_concept to COL should it exist
					String nameCode = reader.get(COLUMN_NAME_CODE);
					if (StringUtils.isNotEmpty(nameCode)) {
						RemoteConcept rc = new RemoteConcept();
						rc.setIdType(1);
						rc.setRemoteId(nameCode);
						rc.setTaxonConceptId(id);
						remoteConceptDAO.create(rc);							
					}
					
					// create the remote_concept to the provider should it exist
					String providerLink = reader.get(COLUMN_PROVIDER_LINK_URL);
					if (StringUtils.isNotEmpty(providerLink)) {
						RemoteConcept rc = new RemoteConcept();
						rc.setIdType(2);
						rc.setRemoteId(providerLink);
						rc.setTaxonConceptId(id);
						remoteConceptDAO.create(rc);
					}
					
					// create the remote_concept to Taxa - it should exist
					String taxaId = reader.get(COLUMN_TAXA_ID);
					if (StringUtils.isNotEmpty(taxaId)) {
						RemoteConcept rc = new RemoteConcept();
						rc.setIdType(3);
						rc.setRemoteId(taxaId);
						rc.setTaxonConceptId(id);
						remoteConceptDAO.create(rc);
					}
				
					// create the remote_concept to Taxa - it should exist
					// nice little misuse of the model, but need to stuff this somewhere...
					String specialistId = reader.get(COLUMN_SPECIALIST);
					if (StringUtils.isNotEmpty(specialistId)) {
						RemoteConcept rc = new RemoteConcept();
						rc.setIdType(4);
						rc.setRemoteId(specialistId);
						rc.setTaxonConceptId(id);
						remoteConceptDAO.create(rc);
					}
				
					// create the remote_concept to Taxa - it should exist
					// nice little misuse of the model, but need to stuff this somewhere...
					String scrutinyDate = reader.get(COLUMN_SCRUTINY_DATE);
					if (StringUtils.isNotEmpty(scrutinyDate)) {
						RemoteConcept rc = new RemoteConcept();
						rc.setIdType(5);
						rc.setRemoteId(scrutinyDate);
						rc.setTaxonConceptId(id);
						remoteConceptDAO.create(rc);
					}
				
					/* 
					 * get the status id to determine whether there is enough to create the concept.
					 * A concept is not created unless it's relationships can also be formed
					 * The non created ones can be investigated and possibly checked with the COL folks
					 * ColStatusId translates as:
					 * 0) higher taxa
					 * 1) accepted name
					 * 2) ambiguous synonym
					 * 3) misapplied name
					 * 4) provisionally applied name
					 * 5) synonym 
					 */
					String colStatusId = reader.get(COLUMN_STATUS_ID);
					// now store the RA to create if any
					if (!"1".equals(colStatusId)
							&& !"0".equals(colStatusId)) {
						
						Map<String,String> fromTo = raMap.get(colStatusId);
						if (fromTo == null) {
							fromTo = new HashMap<String, String>();
							raMap.put(colStatusId, fromTo);	
						}
						String target = reader.get(COLUMN_ACCEPTED_NAME_CODE);
						if ("4".equals(colStatusId)) {
							target = "" + id;
						}
						fromTo.put("" + id, target);
						
						
						
						
						raMap.put(colStatusId, fromTo);
					}
						
						
				}				
			} while (reader.next());
		} else {
			logger.error("File reader not created for " + fileUrl);
		}
		
		logger.info("Finished [" + fileUrl + "] in " + ((1 + System.currentTimeMillis() - time)/1000) + " secs");
		return context;		
	}

	/**
	 * @param context
	 * @return
	 * @throws ContextCorruptException
	 */
	@SuppressWarnings("unchecked")
	private Map<String, Long> initialiseNameCodeMp(ProcessContext context) throws ContextCorruptException {
		Map<String,Long> nameCodes = (Map<String,Long>) context.get(contextKeyNameCodes, Map.class, false);
    	if (nameCodes == null) {
    		nameCodes = new HashMap<String,Long>();
    		logger.info("Creating the name codes map");
    		context.put(contextKeyNameCodes, nameCodes);
    	}
		return nameCodes;
	}

	/**
	 * @param context
	 * @return
	 * @throws ContextCorruptException
	 */
	@SuppressWarnings("unchecked")
	private Map<String,Map<String,String>> initialiseRAMap(ProcessContext context) throws ContextCorruptException {
		Map<String,Map<String,String>> map = (Map<String,Map<String,String>>) context.get(contextKeyRAMap, Map.class, false);
    	if (map == null) {
    		map = new HashMap<String,Map<String,String>>();
    		logger.info("Creating the RA map");
    		context.put(contextKeyRAMap, map);
    	}
		return map;
	}

	/**
	 * @param context
	 * @return
	 * @throws ContextCorruptException
	 */
	@SuppressWarnings("unchecked")
	private Map<String, Long> initialiseMappedIds(ProcessContext context) throws ContextCorruptException {
		Map<String,Long> mappedIds = (Map<String,Long>) context.get(contextKeyMappedIds, Map.class, false);
    	if (mappedIds == null) {
    		mappedIds = new HashMap<String,Long>();
    		logger.info("Creating the mapped IDs");
    		context.put(contextKeyMappedIds, mappedIds);
    	}
		return mappedIds;
	}

	/**
	 * Creates a concept from the line in the file
	 * @param mappedIds For the previously created Ids
	 * @param reader To the file
	 * @return The concept that is unpersisted
	 */
	protected TaxonConcept createConcept(Map<String, Long> mappedIds, DelimitedFileReader reader, Map<String, Long> resourceIds, long dataProviderId) {
		TaxonName tn = new TaxonName();
		tn.setCanonical(reader.get(COLUMN_SCIENTIFIC_NAME));
		tn.setGeneric(reader.get(COLUMN_GENUS));
		tn.setSpecific(reader.get(COLUMN_SPECIES));
		tn.setInfraSpecific(reader.get(COLUMN_INFRASPECIES));
		tn.setInfraSpecificMarker(reader.get(COLUMN_INFRASPECIES_MARKER));
		tn.setAuthor(reader.get(COLUMN_AUTHOR));
		tn.setRank(Integer.parseInt(reader.get(COLUMN_RANK)));
		
		TaxonConcept tc = new TaxonConcept();
		tc.setDataProviderId(dataProviderId);
		tc.setDataResourceId(resourceIds.get(reader.get(COLUMN_DATABASE_ID)));
		tc.setRank(Integer.parseInt(reader.get(COLUMN_RANK)));
		if ("1".equals(reader.get(COLUMN_IS_ACCEPTED))) {
			tc.setAccepted(true);
			tc.setPriority(1);
		} else {
			tc.setAccepted(false);
			if (Integer.parseInt(reader.get(COLUMN_RANK))<7000) {
				tc.setPriority(100);
			} else {
				tc.setPriority(1);
			}
		}
		Long parent = mappedIds.get(reader.get(COLUMN_PARENT_ID));
		if (parent!=null) {
			tc.setParentId(parent);
		} else if (Integer.parseInt(reader.get(COLUMN_RANK))>1000){
			logger.warn("Parent not found for name[" + reader.get(COLUMN_SCIENTIFIC_NAME) + "] taxaId[" + reader.get(COLUMN_TAXA_ID) + "] parentTaxaId[" + reader.get(COLUMN_PARENT_ID) + "]");
		}
		tc.setTaxonName(tn);
		
		return tc;
	}

	/**
	 * @return Returns the contextKeyMappedIds.
	 */
	public String getContextKeyMappedIds() {
		return contextKeyMappedIds;
	}

	/**
	 * @param contextKeyMappedIds The contextKeyMappedIds to set.
	 */
	public void setContextKeyMappedIds(String contextKeyMappedIds) {
		this.contextKeyMappedIds = contextKeyMappedIds;
	}

	/**
	 * @return Returns the fileUrl.
	 */
	public String getFileUrl() {
		return fileUrl;
	}

	/**
	 * @param fileUrl The fileUrl to set.
	 */
	public void setFileUrl(String fileUrl) {
		this.fileUrl = fileUrl;
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
	 * @return Returns the contextKeyNameCodes.
	 */
	public String getContextKeyNameCodes() {
		return contextKeyNameCodes;
	}

	/**
	 * @param contextKeyNameCodes The contextKeyNameCodes to set.
	 */
	public void setContextKeyNameCodes(String contextKeyNameCodes) {
		this.contextKeyNameCodes = contextKeyNameCodes;
	}

	/**
	 * @return Returns the remoteConceptDAO.
	 */
	public RemoteConceptDAO getRemoteConceptDAO() {
		return remoteConceptDAO;
	}

	/**
	 * @param remoteConceptDAO The remoteConceptDAO to set.
	 */
	public void setRemoteConceptDAO(RemoteConceptDAO remoteConceptDAO) {
		this.remoteConceptDAO = remoteConceptDAO;
	}

	/**
	 * @return Returns the relationshipAssertionDAO.
	 */
	public RelationshipAssertionDAO getRelationshipAssertionDAO() {
		return relationshipAssertionDAO;
	}

	/**
	 * @param relationshipAssertionDAO The relationshipAssertionDAO to set.
	 */
	public void setRelationshipAssertionDAO(
			RelationshipAssertionDAO relationshipAssertionDAO) {
		this.relationshipAssertionDAO = relationshipAssertionDAO;
	}

	/**
	 * @return Returns the contextKeyResourceIds.
	 */
	public String getContextKeyResourceIds() {
		return contextKeyResourceIds;
	}

	/**
	 * @param contextKeyResourceIds The contextKeyResourceIds to set.
	 */
	public void setContextKeyResourceIds(String contextKeyResourceIds) {
		this.contextKeyResourceIds = contextKeyResourceIds;
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
	 * @return Returns the contextKeyRAMap.
	 */
	public String getContextKeyRAMap() {
		return contextKeyRAMap;
	}

	/**
	 * @param contextKeyRAMap The contextKeyRAMap to set.
	 */
	public void setContextKeyRAMap(String contextKeyRAMap) {
		this.contextKeyRAMap = contextKeyRAMap;
	}
}