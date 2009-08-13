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
package org.gbif.portal.web.content.taxonomy;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.gbif.portal.dto.resources.DataProviderDTO;
import org.gbif.portal.service.DataResourceManager;
import org.gbif.portal.service.ServiceException;
import org.gbif.portal.service.TaxonomyManager;
import org.gbif.portal.web.content.ContentProvider;
import org.gbif.portal.web.content.ContentView;
/**
 * Provides content for the taxonomy pane on front page.
 *
 * @author Dave Martin
 */
public class TaxonomyIntroProvider implements ContentProvider {

	protected static Log log = LogFactory.getLog(TaxonomyIntroProvider.class);	
	/**the Taxonomy Manager providing service methods for the content requried**/
	protected TaxonomyManager taxonomyManager;
	/**the Data Resource Manager providing service methods for the content requried**/
	protected DataResourceManager dataResourceManager;	
	/**The species count attribute name**/
	protected String speciesCountKey="speciesCount";	
	/**The higher taxa count attribute name**/	
	protected String higherTaxaCountKey="higherTaxaCount";
	
	/**
	 * @see org.gbif.portal.web.content.ContentProvider#addContent(org.gbif.portal.web.content.ContentView, java.lang.Object)
	 */
	public void addContent(ContentView cc, HttpServletRequest request, HttpServletResponse response) {
		try {
			DataProviderDTO dataProvider = dataResourceManager.getNubDataProvider();
			//{speciesCount}
			if(dataProvider!=null){
				cc.addObject(speciesCountKey, dataProvider.getSpeciesCount());
				//higherTaxaCount}
				cc.addObject(higherTaxaCountKey, dataProvider.getHigherConceptCount());
			}
		} catch (ServiceException e){
			log.error(e.getMessage(),e);
		}
	}

	/**
	 * @param taxonomyManager the taxonomyManager to set
	 */
	public void setTaxonomyManager(TaxonomyManager taxonomyManager) {
		this.taxonomyManager = taxonomyManager;
	}

	/**
	 * @param higherTaxaCountKey the higherTaxaCountKey to set
	 */
	public void setHigherTaxaCountKey(String higherTaxaCountKey) {
		this.higherTaxaCountKey = higherTaxaCountKey;
	}

	/**
	 * @param speciesCountKey the speciesCountKey to set
	 */
	public void setSpeciesCountKey(String speciesCountKey) {
		this.speciesCountKey = speciesCountKey;
	}

	/**
	 * @param dataResourceManager the dataResourceManager to set
	 */
	public void setDataResourceManager(DataResourceManager dataResourceManager) {
		this.dataResourceManager = dataResourceManager;
	}
}