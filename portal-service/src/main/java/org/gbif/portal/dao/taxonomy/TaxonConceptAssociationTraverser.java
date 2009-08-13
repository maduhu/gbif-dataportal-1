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
package org.gbif.portal.dao.taxonomy;

import java.util.HashMap;
import java.util.Map;

import org.gbif.portal.dao.AssociationTraverser;
import org.gbif.portal.model.taxonomy.TaxonConcept;
import org.gbif.portal.model.taxonomy.TaxonConceptLite;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;

/**
 * Association traverser for taxon concepts.
 * 
 * @author dmartin
 */
public class TaxonConceptAssociationTraverser implements AssociationTraverser {

	/**
	 * @see org.gbif.portal.dao.AssociationTraverser#batchPostprocess(int, org.hibernate.ScrollableResults, org.hibernate.Session)
	 */
	public void batchPostprocess(int batchSize,
			ScrollableResults scrollableResults, Session session) {}

	/**
	 * @see org.gbif.portal.dao.AssociationTraverser#batchPreprocess(int, org.hibernate.ScrollableResults, org.hibernate.Session)
	 */
	public void batchPreprocess(int batchSize,
			ScrollableResults scrollableResults, Session session) {}

	/**
	 * @see org.gbif.portal.dao.AssociationTraverser#traverse(java.lang.Object, org.hibernate.Session)
	 */
	public Map<String, Object> traverse(Object object, Session session) {

		Map<String, Object> beanMap = new HashMap<String, Object>();
		
		TaxonConcept tc = null;
		
		if(object instanceof Object[])
			tc = (TaxonConcept) ((Object[]) object)[0];
		else
			tc = (TaxonConcept) object;
		
		beanMap.put("taxonConcept", tc);
		beanMap.put("taxonName", tc.getTaxonName());
		beanMap.put("taxonRank", tc.getTaxonRank().getName());
		addHigherConceptName(beanMap, "kingdom", tc.getKingdomConcept());
		addHigherConceptName(beanMap, "phylum", tc.getPhylumConcept());
		addHigherConceptName(beanMap, "class", tc.getClassConcept());
		addHigherConceptName(beanMap, "order", tc.getOrderConcept());
		addHigherConceptName(beanMap, "family", tc.getFamilyConcept());
		addHigherConceptName(beanMap, "genus", tc.getGenusConcept());
		addHigherConceptName(beanMap, "species", tc.getSpeciesConcept());
		beanMap.put("dataProvider", tc.getDataProvider());
		beanMap.put("dataResource", tc.getDataResource());
		beanMap.put("dataResourceId", tc.getDataResource().getId());
		beanMap.put("dataResourceName", tc.getDataResource().getName());

		return beanMap;
	}
	
	private static void addHigherConceptName(Map<String, Object> beanMap, String rank, TaxonConceptLite tcl){
		if(tcl!= null){
			beanMap.put(rank, tcl.getTaxonNameLite().getCanonical());
		}
	}

	public void reset() {}
}