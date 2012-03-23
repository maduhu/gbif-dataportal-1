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

package org.gbif.portal.dao.taxonomy.impl.hibernate;

import java.util.List;

import org.gbif.portal.dao.taxonomy.TaxonNameDAO;
import org.gbif.portal.model.taxonomy.TaxonName;
import org.gbif.portal.model.taxonomy.TaxonRank;
import org.gbif.portal.util.taxonomy.TaxonNameSoundEx;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * A DAO implementation for the TaxonName entity.
 * 
 * @author Dave Martin
 */
public class TaxonNameDAOImpl extends HibernateDaoSupport implements TaxonNameDAO {
	/**
	 * The java based sound ex algorithm 
	 */
	protected TaxonNameSoundEx taxonNameSoundEx = new TaxonNameSoundEx();
	/** Any concepts with a taxonomic priority below this threshold are considered "unconfirmed" */
	protected int taxonomicPriorityThreshold = 20;	

	/**
	 * @see org.gbif.portal.dao.taxonomy.TaxonNameDAO#getTaxonNameFor(long)
	 */
	public TaxonName getTaxonNameFor(final long taxonNameId) {
		HibernateTemplate template = getHibernateTemplate();
		return (TaxonName) template.execute(new HibernateCallback() {
			public Object doInHibernate(Session session) {
				Query query = session.createQuery(
						"from TaxonName where id = ?");
				query.setParameter(0, taxonNameId);
				query.setCacheable(true);
				return query.uniqueResult();
			}
		});		
	}
	
	/**
	 * @see org.gbif.portal.dao.taxonomy.TaxonNameDAO#findScientificNames(java.lang.String, org.gbif.portal.model.taxonomy.TaxonRank, int, int)
	 */
	@SuppressWarnings("unchecked")
	public List<String> findScientificNamesInTaxonomy(final String nameStub, final boolean fuzzy, final TaxonRank taxonRank, final Boolean higherThanRankSupplied, final boolean soundex, final Long dataProviderId, final Long dataResourceId, final boolean allowUnconfirmed, final int startIndex, final int maxResults) {
		HibernateTemplate template = getHibernateTemplate();
		return (List<String>) template.execute(new HibernateCallback() {
			public Object doInHibernate(Session session) {
				StringBuffer sb = new StringBuffer();
				sb.append("select distinct tn.canonical from TaxonConcept tc inner join tc.taxonName tn where ");
				if(soundex)
					sb.append("tn.searchableCanonical like :nameStub");
				else
					sb.append(" tn.canonical like :nameStub");
				if(taxonRank!=null){
					sb.append(" and tn.taxonRank ");
					if(higherThanRankSupplied!=null){
						if(higherThanRankSupplied){
							sb.append(" < ");
						}else {
							sb.append(" > ");
						}
					} else {
						sb.append(" = ");
					}
					sb.append(" :taxonRank");
				}
				
				if(dataProviderId!=null){
					sb.append(" and tc.dataProviderId= :dataProviderId");
				} 
				
				if(dataResourceId!=null) {
					sb.append(" and tc.dataResourceId= :dataResourceId");
				}

				if(allowUnconfirmed) {
					sb.append(" and tc.taxonomicPriority <= "+taxonomicPriorityThreshold);
				}				
				
				Query query = session.createQuery(sb.toString());
				
				// use the sound ex algorithm if necessary
				String nameStubForQuery = nameStub;
				if(soundex) {
					nameStubForQuery = taxonNameSoundEx.soundEx(nameStub);
				}			
				if(fuzzy && !soundex)
					query.setParameter("nameStub", nameStubForQuery+'%');
				else
					query.setParameter("nameStub", nameStubForQuery);
				if(taxonRank!=null)				
					query.setParameter("taxonRank", taxonRank);
				if(dataProviderId!=null)
					query.setParameter("dataProviderId", dataProviderId);
				if(dataResourceId!=null)
					query.setParameter("dataResourceId", dataResourceId);
				
				query.setCacheable(true);
				query.setMaxResults(maxResults);
				query.setFirstResult(startIndex);
				return query.list();
			}
		});
	}	
	
	/**
	 * @see org.gbif.portal.dao.taxonomy.TaxonNameDAO#findScientificNamesForRank(java.lang.String, org.gbif.portal.model.taxonomy.TaxonRank, int, int)
	 */
	@SuppressWarnings("unchecked")
	public List<String> findScientificNames(final String nameStub, final boolean fuzzy, final TaxonRank taxonRank, final Boolean higherThanRankSupplied, final boolean soundex, final int startIndex, final int maxResults) {
		HibernateTemplate template = getHibernateTemplate();
		return (List<String>) template.execute(new HibernateCallback() {
			public Object doInHibernate(Session session) {
				
				StringBuffer sb = new StringBuffer();
				sb.append("select distinct canonical from TaxonName where ");
				if(soundex)
					sb.append("searchableCanonical like :nameStub");
				else
					sb.append(" canonical like :nameStub");
				if(taxonRank!=null){
					sb.append(" and taxonRank ");
					if(higherThanRankSupplied!=null){
						if(higherThanRankSupplied){
							sb.append(" < ");
						}else {
							sb.append(" > ");
						}
					} else {
						sb.append(" = ");
					}
					sb.append(" :taxonRank");
				}
				Query query = session.createQuery(sb.toString());
				
				// use the sound ex algorithm if necessary
				String nameStubForQuery = nameStub;
				if (soundex) {
					nameStubForQuery = taxonNameSoundEx.soundEx(nameStub);
				}			
				if(fuzzy && !soundex)
					query.setParameter("nameStub", nameStubForQuery+'%');
				else
					query.setParameter("nameStub", nameStubForQuery);
				if(taxonRank!=null)				
					query.setParameter("taxonRank", taxonRank);
				query.setCacheable(true);
				query.setMaxResults(maxResults);
				query.setFirstResult(startIndex);
				return query.list();
			}
		});
	}
}