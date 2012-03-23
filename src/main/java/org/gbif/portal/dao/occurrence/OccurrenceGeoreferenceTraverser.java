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

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.gbif.portal.dao.AssociationTraverser;
import org.gbif.portal.model.occurrence.OccurrenceRecord;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;

/**
 * An OccurrenceGeoreferenceTraverser traverses the hibernate association bringing together all
 * the model objects required to represent an occurrence records and groups these into  a
 * map.
 * 
 * @author dmartin
 */
public class OccurrenceGeoreferenceTraverser implements AssociationTraverser {

	protected static Log logger = LogFactory.getLog(OccurrenceGeoreferenceTraverser.class);	

	/** The data resource id for the last record */
	protected Long lastDataResourceId = null;
	/** The data provider id for the last record */	
	protected Long lastDataProviderId = null;
	/** The taxon name for the last record */	
	protected String lastTaxonName = null;

	/**
	 * @see org.gbif.portal.dao.AssociationTraverser#batchPreprocess(int, org.hibernate.ScrollableResults, org.hibernate.Session)
	 */
	public void batchPreprocess(int batchSize, ScrollableResults scrollableResults, Session session) {}
	
	/**
	 * @see org.gbif.portal.dao.AssociationTraverser#batchPostprocess(int, org.hibernate.ScrollableResults, org.hibernate.Session)
	 */
	public void batchPostprocess(int batchSize, ScrollableResults scrollableResults, Session session) {}
	
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
			OccurrenceRecord or = (OccurrenceRecord) record[0];
			beanMap.put("occurrenceRecord", or);
			beanMap.put("occurrenceRecordId", or.getId());
			
			//dataProviderName, dataResourceName & taxonName
			String taxonName = (String) record[1];
			beanMap.put("taxonName", (String) record[1]);
			beanMap.put("dataResourceName", (String) record[2]);
			beanMap.put("dataProviderName", (String) record[3]);
			
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
			lastTaxonName = taxonName;
			return beanMap;
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
}