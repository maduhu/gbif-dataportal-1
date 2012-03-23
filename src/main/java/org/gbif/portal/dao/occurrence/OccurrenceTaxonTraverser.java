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

import org.gbif.portal.dao.AssociationTraverser;
import org.gbif.portal.model.taxonomy.TaxonConcept;
import org.gbif.portal.model.taxonomy.TaxonName;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;

/**
 * An association traverser that populates the bean map with the full taxonomic
 * details
 * 
 * @author dmartin
 */
public class OccurrenceTaxonTraverser implements AssociationTraverser {

	/**
	 * @see org.gbif.portal.dao.AssociationTraverser#traverse(java.lang.Object, org.hibernate.Session)
	 */
	public Map<String, Object> traverse(Object object, Session session) {
		//expecting an object array
		if(object instanceof Object[]){
			
			Map<String, Object> beanMap = new HashMap<String, Object>();
			
			//expecting an object array
			Object[] record = (Object[]) object;
			
			//first element is taxon concept (nub)
			TaxonConcept tc = (TaxonConcept) record[0];
			TaxonName tn = (TaxonName) record[1];
			beanMap.put("taxonConcept", tc);
			beanMap.put("taxonName", tn);
			
			//major ranks
			if(tc.getKingdomConcept()!=null)
				beanMap.put("kingdom", tc.getKingdomConcept().getTaxonNameLite().getCanonical());
			if(tc.getPhylumConcept()!=null)			
				beanMap.put("phylum", tc.getPhylumConcept().getTaxonNameLite().getCanonical());
			if(tc.getClassConcept()!=null)			
				beanMap.put("class", tc.getClassConcept().getTaxonNameLite().getCanonical());
			if(tc.getOrderConcept()!=null)	
				beanMap.put("order", tc.getOrderConcept().getTaxonNameLite().getCanonical());
			if(tc.getFamilyConcept()!=null)			
				beanMap.put("family", tc.getFamilyConcept().getTaxonNameLite().getCanonical());
			if(tc.getGenusConcept()!=null)
				beanMap.put("genus", tc.getGenusConcept().getTaxonNameLite().getCanonical());
			if(tc.getSpeciesConcept()!=null)
				beanMap.put("species", tc.getSpeciesConcept().getTaxonNameLite().getCanonical());
			return beanMap;
		}
		return null;
	}
	
	/**
	 * @see org.gbif.portal.dao.AssociationTraverser#batchPostprocess(int, org.hibernate.ScrollableResults, org.hibernate.Session)
	 */
	public void batchPostprocess(int batchSize, ScrollableResults scrollableResults, Session session) {}

	/**
	 * @see org.gbif.portal.dao.AssociationTraverser#batchPreprocess(int, org.hibernate.ScrollableResults, org.hibernate.Session)
	 */
	public void batchPreprocess(int batchSize, ScrollableResults scrollableResults, Session session) {}

	/**
	 * @see org.gbif.portal.dao.AssociationTraverser#reset()
	 */
	public void reset() {}
}
