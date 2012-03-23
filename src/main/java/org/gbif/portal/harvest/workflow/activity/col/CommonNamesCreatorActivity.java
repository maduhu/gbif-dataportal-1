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
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.gbif.portal.dao.CommonNameDAO;
import org.gbif.portal.dao.TaxonConceptDAO;
import org.gbif.portal.model.CommonName;
import org.gbif.portal.model.TaxonConceptLite;
import org.gbif.portal.util.file.DelimitedFileReader;
import org.gbif.portal.util.workflow.BaseActivity;
import org.gbif.portal.util.workflow.ProcessContext;


/**
 * Creates the Common names 
 * 
 * @author trobertson
 */
public class CommonNamesCreatorActivity extends BaseActivity {
	/**
	 * Names are self explainatory
	 */
	protected String fileUrl;
	protected String contextKeyNameCodes;
	protected String contextKeyDataProviderId;
	
	private static final int COLUMN_NAME_CODE = 0;
	private static final int COLUMN_NAME = 1;
	private static final int COLUMN_LANGUAGE = 2;
	//private static final int COLUMN_COUNTRY = 3;
	
	protected Map<String, String> languageMap;
	
	protected CommonNameDAO commonNameDAO;
	protected TaxonConceptDAO taxonConceptDAO;
	
	/**
     * @see org.gbif.portal.util.workflow.BaseMapContextActivity#doExecute(org.gbif.portal.util.workflow.MapContext)
     */
    @SuppressWarnings("unchecked")
	public ProcessContext execute(ProcessContext context) throws Exception {
    	Map<String,Long> nameCodes = (Map<String,Long>) context.get(contextKeyNameCodes, Map.class, false);
    	Long dataProviderId = (Long) context.get(contextKeyDataProviderId, Long.class, true);
    	logger.info("Starting: " + fileUrl);
    	long time = System.currentTimeMillis();
		InputStream is = new FileInputStream(fileUrl);
		DelimitedFileReader reader = new DelimitedFileReader(is, "\t", "\"", false);
	
		if (reader!=null) {
			while (reader.next()) {
				String nameCode = reader.get(COLUMN_NAME_CODE);
				String name = reader.get(COLUMN_NAME);
				String language = StringUtils.trimToNull(reader.get(COLUMN_LANGUAGE));
				//String country = reader.get(COLUMN_COUNTRY);
				Long taxonConceptId = null;

				try {
					if (nameCodes != null) {
						taxonConceptId = nameCodes.get(nameCode);
					} else {
						TaxonConceptLite taxonConcept = taxonConceptDAO.getTaxonConceptByDataProviderIdAndRemoteId(dataProviderId, nameCode);
						if (taxonConcept != null) {
							taxonConceptId = taxonConcept.getId();
						}
					}
					
					if (taxonConceptId != null) {
						CommonName cn = commonNameDAO.getUnique(taxonConceptId, name, language);
						if (cn == null) {
							String languageCode = "";
							if (language!=null) {
								String mappedCode = languageMap.get(language.toUpperCase());
								if (mappedCode != null) {
									languageCode = mappedCode;
								}
							}

							cn = new CommonName();
							cn.setName(name);
							cn.setLanguage(language);
							cn.setIsoLanguageCode(languageCode);
							cn.setTaxonConceptId(taxonConceptId);
							
							commonNameDAO.createOrUpdate(cn);
						}
					}
				} catch (RuntimeException e) {
					logger.error("Error creating common name.  Name[" + name + "] nameCode[" + nameCode + "] mappedTcId[" + taxonConceptId + "]");
				}
								
			}
		} else {
			logger.error("File reader not created for " + fileUrl);
		}
		
		logger.info("Finished [" + fileUrl + "] in " + ((1 + System.currentTimeMillis() - time)/1000) + " secs");
    	
		return context;		
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
	 * @return Returns the languageMap.
	 */
	public Map<String, String> getLanguageMap() {
		return languageMap;
	}

	/**
	 * @param languageMap The languageMap to set.
	 */
	public void setLanguageMap(Map<String, String> languageMap) {
		this.languageMap = languageMap;
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
	 * @return the cOLUMN_LANGUAGE
	 */
	public static int getCOLUMN_LANGUAGE() {
		return COLUMN_LANGUAGE;
	}

	/**
	 * @return the cOLUMN_NAME
	 */
	public static int getCOLUMN_NAME() {
		return COLUMN_NAME;
	}

	/**
	 * @return the cOLUMN_NAME_CODE
	 */
	public static int getCOLUMN_NAME_CODE() {
		return COLUMN_NAME_CODE;
	}

	/**
	 * @return the commonNameDAO
	 */
	public CommonNameDAO getCommonNameDAO() {
		return commonNameDAO;
	}

	/**
	 * @param commonNameDAO the commonNameDAO to set
	 */
	public void setCommonNameDAO(CommonNameDAO commonNameDAO) {
		this.commonNameDAO = commonNameDAO;
	}

	/**
	 * @return the contextKeyDataProviderId
	 */
	public String getContextKeyDataProviderId() {
		return contextKeyDataProviderId;
	}

	/**
	 * @param contextKeyDataProviderId the contextKeyDataProviderId to set
	 */
	public void setContextKeyDataProviderId(String contextKeyDataProviderId) {
		this.contextKeyDataProviderId = contextKeyDataProviderId;
	}

	/**
	 * @return the taxonConceptDAO
	 */
	public TaxonConceptDAO getTaxonConceptDAO() {
		return taxonConceptDAO;
	}

	/**
	 * @param taxonConceptDAO the taxonConceptDAO to set
	 */
	public void setTaxonConceptDAO(TaxonConceptDAO taxonConceptDAO) {
		this.taxonConceptDAO = taxonConceptDAO;
	}
}