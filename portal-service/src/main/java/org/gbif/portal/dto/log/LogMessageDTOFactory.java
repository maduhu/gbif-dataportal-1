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
package org.gbif.portal.dto.log;

import org.gbif.portal.dto.BaseDTOFactory;
import org.gbif.portal.model.log.LogMessage;
import org.gbif.portal.util.log.LogEvent;

/**
 * LogMessage DTO Factory.
 * 
 * @author dmartin
 */
public class LogMessageDTOFactory extends BaseDTOFactory {

	/**
	 * This handles certain exceptions when the log messages data has become
	 * inconsistent within the database due to deletion of resource, provider
	 * or concepts.
	 * 
	 * @see org.gbif.portal.dto.DTOFactory#createDTO(java.lang.Object)
	 */
	public Object createDTO(Object modelObject) {
		if(modelObject==null || !(modelObject instanceof LogMessage))
			return null;
		LogMessage lm = (LogMessage) modelObject;
		LogMessageDTO lmDTO = new LogMessageDTO();
		
		lmDTO.setKey(lm.getId().toString());
		if(lm.getPortalInstanceId()!=null)
			lmDTO.setPortalInstanceKey(lm.getPortalInstanceId().toString());
		lmDTO.setLogGroupId(lm.getLogGroupId());
		lmDTO.setEventId(lm.getEventId());
		if(lm.getEventId()!=null && lm.getEventId()!=0){
			lmDTO.setEventName(((LogEvent)LogEvent.get(lm.getEventId().intValue())).getName());
		}
		lmDTO.setLevel(lm.getLevel());
		if(lm.getDataProviderId()!=null && lm.getDataProviderId()!=0){
			lmDTO.setDataProviderKey(lm.getDataProviderId().toString());
			try {			
				//this is catching errors in the case of deleted data resources				
				if(lm.getDataProvider()!=null){			
					lmDTO.setDataProviderName(lm.getDataProvider().getName());
				}
			} catch(Exception e){
				logger.debug("The Data provider with id "+lm.getDataProviderId()+" has been delete. Please clean up the log messages.");				
				logger.debug(e.getMessage(), e);
			}			
		}
		if(lm.getDataResourceId()!=null && lm.getDataResourceId()!=0){
			lmDTO.setDataResourceKey(lm.getDataResourceId().toString());
			try {
				//this is catching errors in the case of deleted data resources				
				if(lm.getDataResource()!=null){
					lmDTO.setDataResourceName(lm.getDataResource().getName());
				}
			} catch(Exception e){
				if(logger.isDebugEnabled()){
					logger.debug("The Data resource with id "+lm.getDataResourceId()+" has been delete. Please clean up the log messages.");
					logger.debug(e.getMessage(), e);
				}
			}
		}
		
		if(lm.getOccurrenceId()!=null && lm.getOccurrenceId()!=0)
			lmDTO.setOccurrenceKey(lm.getOccurrenceId().toString());
		if(lm.getTaxonConceptId()!=null && lm.getTaxonConceptId()!=0){
			lmDTO.setTaxonConceptKey(lm.getTaxonConceptId().toString());
			try {
				//this is catching errors in the case of deleted concepts
				if(lm.getTaxonConceptLite()!=null){
					lmDTO.setTaxonName(lm.getTaxonConceptLite().getTaxonNameLite().getCanonical());
				}
			} catch(Exception e){
				if(logger.isDebugEnabled()){
					logger.debug("The taxon concept with id "+lm.getTaxonConceptId()+" has been delete. Please clean up the log messages.");
					logger.debug(e.getMessage(), e);
				}
			}			
		}
		lmDTO.setUserId(lm.getUserId());
		lmDTO.setMessage(lm.getMessage());
		lmDTO.setCount(lm.getCount());
		lmDTO.setTimestamp(lm.getTimestamp());
		return lmDTO;
	}
}