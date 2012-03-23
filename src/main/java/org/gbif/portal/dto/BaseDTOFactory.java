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
package org.gbif.portal.dto;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
/**
 * A Base DTO Factory. Full implementations of a DTOFactory should subclass
 * this abstract class  and implement the type specific createDTO(Object) method
 * which is particular to a DTO type.
 * 
 * @author dmartin
 */
public abstract class BaseDTOFactory implements DTOFactory {

	protected static Log logger = LogFactory.getLog(BaseDTOFactory.class);	
	
	/** Allow lazy loading */
	protected boolean allowLazyLoading = true;	
	
	/**
	 * @see org.gbif.portal.dto.DTOFactory#createResultsDTO(java.util.List, java.lang.Integer)
	 */
	public SearchResultsDTO createResultsDTO(List modelObjects, Integer maxResults) {
		SearchResultsDTO searchResultsDTO =  new SearchResultsDTO();
		if(modelObjects!=null)		
			DTOUtils.populate(this, searchResultsDTO, modelObjects, maxResults);
		return searchResultsDTO;
	}

	/**
	 * @see org.gbif.portal.dto.DTOFactory#createDTOList(java.util.List)
	 */
	@SuppressWarnings("unchecked")
	public List createDTOList(List modelObjects) {
		if(modelObjects==null)
			return null;
		List dtoList = new ArrayList();
		for (Object modelObject: modelObjects)
			dtoList.add(createDTO(modelObject));
		return dtoList;
	}

	/**
	 * @return the allowLazyLoading
	 */
	public boolean isAllowLazyLoading() {
		return allowLazyLoading;
	}

	/**
	 * @param allowLazyLoading the allowLazyLoading to set
	 */
	public void setAllowLazyLoading(boolean allowLazyLoading) {
		this.allowLazyLoading = allowLazyLoading;
	}
}