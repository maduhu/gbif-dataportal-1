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

import java.util.Map;

import org.gbif.portal.dao.RelationshipAssertionDAO;
import org.gbif.portal.util.workflow.BaseActivity;
import org.gbif.portal.util.workflow.ProcessContext;


/**
 * Creates the RAs 
 * 
 * @author trobertson
 */
public class RelationshipCreator extends BaseActivity {
	/**
	 * Names are self explainatory
	 */
	protected String contextKeyRAMap;
	protected String contextKeyNameCodes;
	protected RelationshipAssertionDAO relationshipAssertionDAO;
	
	/**
     * @see org.gbif.portal.util.workflow.BaseMapContextActivity#doExecute(org.gbif.portal.util.workflow.MapContext)
     */
    @SuppressWarnings("unchecked")
	public ProcessContext execute(ProcessContext context) throws Exception {
    	// Initialise the maps
    	Map<String,Map<String,String>> raMap = (Map<String,Map<String,String>>) context.get(contextKeyRAMap, Map.class, true);
    	Map<String,Long> nameCodes = (Map<String,Long>) context.get(contextKeyNameCodes, Map.class, true);
    	
    	for (String colStatusId : raMap.keySet()) {
    		long time = System.currentTimeMillis();
    		logger.info("Starting RAs for colStatusId: " + colStatusId);
    		Map<String,String> relationships = raMap.get(colStatusId);
    		
    		for (String taxonConceptId : relationships.keySet()) {
    			String target = relationships.get(taxonConceptId);
    			try {
					if ("2".equals(colStatusId)) {
						// create an ambiguous synonym
						Long targetId = nameCodes.get(target);
						if (targetId != null) {
							relationshipAssertionDAO.create(Long.parseLong(taxonConceptId), targetId, RelationshipAssertionDAO.TYPE_AMBIGUOUS_SYNONYM);
						} else {
							logger.warn("No GBIF id found for target nameCode[" + target + "] colstatusId[" + colStatusId + "] fromTaxonConceptId[" + taxonConceptId + "]");
						}
						
					} else if ("3".equals(colStatusId)) {
						// create an ambiguous synonym
						Long targetId = nameCodes.get(target);
						if (targetId != null) {
							relationshipAssertionDAO.create(Long.parseLong(taxonConceptId), targetId, RelationshipAssertionDAO.TYPE_MISAPPLIED_NAME);
						} else {
							logger.warn("No GBIF id found for target nameCode[" + target + "] colstatusId[" + colStatusId + "] fromTaxonConceptId[" + taxonConceptId + "]");
						}
						
					} else if ("4".equals(colStatusId)) {
						// create a provisionally applied (self referencing RA)
						relationshipAssertionDAO.create(Long.parseLong(taxonConceptId), Long.parseLong(taxonConceptId), RelationshipAssertionDAO.TYPE_PROVISIONALLY_APPLIED_NAME);								
						
					} else if ("5".equals(colStatusId)) {
						// create an ambiguous synonym
						Long targetId = nameCodes.get(target);
						if (targetId != null) {
							relationshipAssertionDAO.create(Long.parseLong(taxonConceptId), targetId, RelationshipAssertionDAO.TYPE_SYNONYM);
						} else {
							logger.warn("No GBIF id found for target nameCode[" + target + "] colstatusId[" + colStatusId + "] fromTaxonConceptId[" + taxonConceptId + "]");
						}
						
					}
				} catch (Exception e) {
					Long targetId = nameCodes.get(target);
					if (targetId != null) {
						logger.warn("Unable to create the relationship.  Most likely there is a duplicate relationship being created for id[" + taxonConceptId + "] targetId[" + target + "] colStatusId[" + colStatusId + "]", e);
					} else {
						logger.warn("Unable to create the relationship.  Id[" + taxonConceptId + "] target nameCode[" + target + "] colStatusId[" + colStatusId + "]", e);
					}
				}
    		}
    		
    		logger.info("Finished colStatusId " + colStatusId + " in " + ((System.currentTimeMillis()+1 - time)/1000) + " secs");
    	}
    	
		return context;		
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
}