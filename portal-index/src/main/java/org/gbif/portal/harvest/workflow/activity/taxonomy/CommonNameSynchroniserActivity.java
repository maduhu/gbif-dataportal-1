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
package org.gbif.portal.harvest.workflow.activity.taxonomy;

import org.gbif.portal.dao.CommonNameDAO;
import org.gbif.portal.model.CommonName;
import org.gbif.portal.util.workflow.BaseActivity;
import org.gbif.portal.util.workflow.ProcessContext;


/**
 * Creates the Common names 
 * 
 * @author trobertson
 */
public class CommonNameSynchroniserActivity extends BaseActivity {
	/**
	 * Names are self explainatory
	 */
	protected String contextKeyLanguage;
	protected String contextKeyTaxonConceptId;
	protected String contextKeyISOLanguageCode;
	protected String contextKeyCommonName;
	
	protected CommonNameDAO commonNameDAO;
	
	/**
     * @see org.gbif.portal.util.workflow.BaseMapContextActivity#doExecute(org.gbif.portal.util.workflow.MapContext)
     */
    @SuppressWarnings("unchecked")
	public ProcessContext execute(ProcessContext context) throws Exception {
    	String language = (String) context.get(getContextKeyLanguage(), String.class, false);
    	String isoLanguageCode = (String)context.get(getContextKeyISOLanguageCode(), String.class, false);
    	String commonName = (String)context.get(getContextKeyCommonName(), String.class, false);
    	Long taxonConceptId = (Long) context.get(getContextKeyTaxonConceptId(), Long.class, false);
    	
    	if (language!=null && commonName!=null && taxonConceptId!=null) {	    	
			CommonName cn = commonNameDAO.getUnique(taxonConceptId, commonName, language);
			if (cn == null) {
				cn = new CommonName();
				cn.setName(commonName);
				cn.setLanguage(language);
				if (isoLanguageCode == null) {
					isoLanguageCode="";					
				}
				cn.setIsoLanguageCode(isoLanguageCode);
				cn.setTaxonConceptId(taxonConceptId);
				logger.debug("Creating new common name:" + cn);
				commonNameDAO.create(cn);
			} else {
				logger.debug("Common name exists:" + cn);
			}
		} else {
			logger.debug("Unable to create common name for commonName[" + commonName + "] language[" + language + "] taxonConceptId[" + taxonConceptId + "]");
		}
		return context;		
	}

	/**
	 * @return Returns the commonNameDAO.
	 */
	public CommonNameDAO getCommonNameDAO() {
		return commonNameDAO;
	}

	/**
	 * @param commonNameDAO The commonNameDAO to set.
	 */
	public void setCommonNameDAO(CommonNameDAO commonNameDAO) {
		this.commonNameDAO = commonNameDAO;
	}

	/**
	 * @return Returns the contextKeyCommonName.
	 */
	public String getContextKeyCommonName() {
		return contextKeyCommonName;
	}

	/**
	 * @param contextKeyCommonName The contextKeyCommonName to set.
	 */
	public void setContextKeyCommonName(String contextKeyCommonName) {
		this.contextKeyCommonName = contextKeyCommonName;
	}

	/**
	 * @return Returns the contextKeyISOLanguageCode.
	 */
	public String getContextKeyISOLanguageCode() {
		return contextKeyISOLanguageCode;
	}

	/**
	 * @param contextKeyISOLanguageCode The contextKeyISOLanguageCode to set.
	 */
	public void setContextKeyISOLanguageCode(String contextKeyISOLanguageCode) {
		this.contextKeyISOLanguageCode = contextKeyISOLanguageCode;
	}

	/**
	 * @return Returns the contextKeyLanguage.
	 */
	public String getContextKeyLanguage() {
		return contextKeyLanguage;
	}

	/**
	 * @param contextKeyLanguage The contextKeyLanguage to set.
	 */
	public void setContextKeyLanguage(String contextKeyLanguage) {
		this.contextKeyLanguage = contextKeyLanguage;
	}

	/**
	 * @return Returns the contextKeyTaxonConceptId.
	 */
	public String getContextKeyTaxonConceptId() {
		return contextKeyTaxonConceptId;
	}

	/**
	 * @param contextKeyTaxonConceptId The contextKeyTaxonConceptId to set.
	 */
	public void setContextKeyTaxonConceptId(String contextKeyTaxonConceptId) {
		this.contextKeyTaxonConceptId = contextKeyTaxonConceptId;
	}
}