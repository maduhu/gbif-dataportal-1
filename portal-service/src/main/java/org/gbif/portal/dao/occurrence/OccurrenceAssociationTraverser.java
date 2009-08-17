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
package org.gbif.portal.dao.occurrence;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.gbif.portal.dao.AssociationTraverser;
import org.gbif.portal.model.occurrence.IdentifierRecord;
import org.gbif.portal.model.occurrence.IdentifierType;
import org.gbif.portal.model.occurrence.ORImage;
import org.gbif.portal.model.occurrence.OccurrenceRecord;
import org.gbif.portal.model.occurrence.RawOccurrenceRecord;
import org.gbif.portal.model.occurrence.TypeStatus;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;

/**
 * An OccurrenceAssociationTraverser traverses the hibernate association bringing together all
 * the model objects required to represent an occurrence record and groups these into a
 * map.
 * 
 * @author dmartin
 */
public class OccurrenceAssociationTraverser implements AssociationTraverser {

	protected static Log logger = LogFactory.getLog(OccurrenceAssociationTraverser.class);	
	
	/** DAOs for object access */
	protected ImageRecordDAO imageRecordDAO;
	protected TypificationRecordDAO typificationRecordDAO;
	protected IdentifierRecordDAO identifierRecordDAO;

	protected Map<Long, String> orImageUrlMap = null;
	protected Map<Long, String> typeStatusMap = null;
	protected Map<Long, List<IdentifierRecord>> identifiersMap = null;

	/** The data resource id for the last record */
	protected Long lastDataResourceId = null;
	/** The data provider id for the last record */	
	protected Long lastDataProviderId = null;
	/** The taxon name for the last record */	
	protected String lastTaxonName = null;

	/** 
	 * Whether or not to retrieve image urls, typifications and external identifiers.
	 * this extra overhead may not be required for some outputs 
	 */
	protected boolean retrieveIdentifiers = true;
	
	/**
	 * @see org.gbif.portal.dao.AssociationTraverser#batchPreprocess(int, org.hibernate.ScrollableResults, org.hibernate.Session)
	 */
	public void batchPreprocess(int batchSize, ScrollableResults scrollableResults, Session session) {

		if(!retrieveIdentifiers)
			return;
		
		if(logger.isDebugEnabled())
			logger.debug("Current row number:" +scrollableResults.getRowNumber());
		
		List<Long> occurrenceRecordIds = new ArrayList<Long>();
		
		boolean eor = false;
		int numberScrolled = 0;
		
		for(numberScrolled=0; numberScrolled<batchSize-1 && !eor; numberScrolled++){
			//retrieve the id
			Long recordId = (Long) scrollableResults.get(0);
			occurrenceRecordIds.add(recordId);
			
			if(scrollableResults.isLast()){
				eor = true;
				numberScrolled--;
			} else {
				scrollableResults.next();
			}
		}
		scrollableResults.scroll(-numberScrolled);
		
		if(logger.isDebugEnabled()){
			logger.debug("Number scrolled through: "+numberScrolled);
			logger.debug("Scrolled back to: " +scrollableResults.getRowNumber());
		}
			
		//retrieve image records for this batch - and process into Map - 
		List<ORImage> orImageList = imageRecordDAO.getImageRecordsForOccurrenceRecords(occurrenceRecordIds);
		this.orImageUrlMap = new HashMap<Long, String>(); 
		for(ORImage orImage: orImageList){
			//only storing the first image url we find			
			if(this.orImageUrlMap.get(orImage.getOccurrenceRecordId())==null){
				this.orImageUrlMap.put(orImage.getOccurrenceRecordId(), orImage.getUrl());
			}
			session.evict(orImage);
		}
		if(logger.isDebugEnabled())
			logger.debug("Number of images found for batch: "+this.orImageUrlMap.size());		
		
		//retrieve type status for this batch		
		List<TypeStatus> typeStatusList = typificationRecordDAO.getTypeStatusForOccurrenceRecords(occurrenceRecordIds);
		this.typeStatusMap = new HashMap<Long, String>(); 
		for(TypeStatus typeStatus: typeStatusList){
			//only storing the first type status we find
			if(this.typeStatusMap.get(typeStatus.getOccurrenceRecordId())==null){
				this.typeStatusMap.put(typeStatus.getOccurrenceRecordId(), typeStatus.getTypeStatus());
			}
			session.evict(typeStatus);
		}		
		if(logger.isDebugEnabled())
			logger.debug("Number of type status found for batch: "+this.typeStatusMap.size());		
		
		//retrieve identifiers for this batch		
		List<IdentifierRecord> identifierList = identifierRecordDAO.getIdentifierRecordsForOccurrenceRecords(occurrenceRecordIds);
		this.identifiersMap = new HashMap<Long, List<IdentifierRecord>>(); 
		for(IdentifierRecord ir: identifierList){
			List<IdentifierRecord> irs = this.identifiersMap.get(ir.getOccurrenceRecordId());
			if(irs==null){
				irs = new ArrayList<IdentifierRecord>();
				irs.add(ir);
				this.identifiersMap.put(ir.getOccurrenceRecordId(), irs);
			} else {
				irs.add(ir);
			}
			session.evict(ir);
		}
		if(logger.isDebugEnabled())
			logger.debug("Number of identifiers found for batch: "+this.identifiersMap.size());		
	
	}
	
	/**
	 * @see org.gbif.portal.dao.AssociationTraverser#batchPostprocess(int, org.hibernate.ScrollableResults, org.hibernate.Session)
	 */
	public void batchPostprocess(int batchSize, ScrollableResults scrollableResults, Session session) {
		identifiersMap = null;
		orImageUrlMap = null;
		typeStatusMap = null;
	}
	
	/**
	 * @see org.gbif.portal.dao.AssociationTraverser#traverse(java.lang.Object, org.hibernate.Session)
	 */
	public Map<String, Object> traverse(Object object, Session session) {
		
		//expecting an object array
		if(object instanceof Object[]){
			
			Map<String, Object> beanMap = new HashMap<String, Object>();
			
			//expecting an object array
			Object[] record = (Object[]) object;
			
			//first element is occurrence record id
			Long occurrenceRecordId = (Long) record[0];
			beanMap.put("occurrenceRecordId", occurrenceRecordId);
			
			//dataProviderName, dataResourceName & taxonName
			beanMap.put("taxonName", (String) record[1]);
			
			//occurrence record
			OccurrenceRecord or = (OccurrenceRecord) record[2];
			beanMap.put("occurrenceRecord", or);

			beanMap.put("dataResourceName", or.getDataResource().getName());
			beanMap.put("dataProviderName", or.getDataProvider().getName());
			beanMap.put("dataResourceRights", or.getDataResource().getRights());
			beanMap.put("providerIsoCountryCode", or.getDataProvider().getIsoCountryCode());
			
			//add data resource id
			beanMap.put("dataResourceId", or.getDataResourceId());	
			beanMap.put("dataProviderId", or.getDataProviderId());				
			
			Long previousDataResourceId=lastDataResourceId;
			Long previousDataProviderId = lastDataProviderId;
			String previousTaxonName = lastTaxonName;
			
			if(lastDataResourceId!=null)
				beanMap.put("previousDataResourceId",previousDataResourceId);
			if(lastDataProviderId!=null)
				beanMap.put("previousDataProviderId",previousDataProviderId);
			if(lastTaxonName!=null)
				beanMap.put("previousTaxonName",previousTaxonName);

			//store previous data resource id & provider id
			lastDataResourceId = or.getDataResourceId();
			lastDataProviderId = or.getDataProviderId();
			lastTaxonName = (String) record[1];
			
			//raw occurrence record
			RawOccurrenceRecord ror = (RawOccurrenceRecord) record[3];
			beanMap.put("rawOccurrenceRecord", ror);
			
			if(or.getCountry()!=null)
				beanMap.put("region", or.getCountry().getRegion());
			
			//add identifiers
			if(identifiersMap!=null){
				List<IdentifierRecord> identifiers = identifiersMap.remove(occurrenceRecordId);
				if(identifiers!=null){
					for(IdentifierRecord ir: identifiers){
						if(ir.getIdentifierType().equals(IdentifierType.COLLECTORNUMBER)){
							beanMap.put("collectorNumber", ir.getIdentifier());
						} else if(ir.getIdentifierType().equals(IdentifierType.FIELDNUMBER)){
							beanMap.put("fieldNumber", ir.getIdentifier());
						} else if (ir.getIdentifierType().equals(IdentifierType.GUID)){
							beanMap.put("guid", ir.getIdentifier());
						}
					}
				}
			}
			
			//add image url
			if(orImageUrlMap!=null){
				String imageUrl = orImageUrlMap.remove(occurrenceRecordId);
				beanMap.put("imageUrl", imageUrl);
			}

			//add type status
			if(typeStatusMap!=null){
				String typeStatus = typeStatusMap.remove(occurrenceRecordId);
				beanMap.put("typeStatus", typeStatus);
			}
			
			return beanMap;
		} 
		
		if(logger.isDebugEnabled()){
			logger.debug("Traverser received: "+object);
		}
		return null;
	}

	/**
	 * @see org.gbif.portal.dao.AssociationTraverser#reset()
	 */
	public void reset() {
		lastDataProviderId = null;
		lastDataResourceId = null;
		lastTaxonName = null;
	}

	/**
	 * @param identifierRecordDAO the identifierRecordDAO to set
	 */
	public void setIdentifierRecordDAO(IdentifierRecordDAO identifierRecordDAO) {
		this.identifierRecordDAO = identifierRecordDAO;
	}

	/**
	 * @param imageRecordDAO the imageRecordDAO to set
	 */
	public void setImageRecordDAO(ImageRecordDAO imageRecordDAO) {
		this.imageRecordDAO = imageRecordDAO;
	}

	/**
	 * @param typificationRecordDAO the typificationRecordDAO to set
	 */
	public void setTypificationRecordDAO(TypificationRecordDAO typificationRecordDAO) {
		this.typificationRecordDAO = typificationRecordDAO;
	}
	
	/**
	 * @param retrieveIdentifiers the retrieveIdentifiers to set
	 */
	public void setRetrieveIdentifiers(boolean retrieveIdentifiers) {
		this.retrieveIdentifiers = retrieveIdentifiers;
	}
}