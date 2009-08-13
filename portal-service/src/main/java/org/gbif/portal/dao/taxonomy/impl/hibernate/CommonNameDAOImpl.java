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

import org.gbif.portal.dao.taxonomy.CommonNameDAO;
import org.gbif.portal.model.taxonomy.CommonName;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * DAO Implementation for CommonName.
 * 
 * @author dmartin
 */
public class CommonNameDAOImpl extends HibernateDaoSupport implements CommonNameDAO {

	/**
	 * @see org.gbif.portal.dao.taxonomy.CommonNameDAO#findCommonNames(java.lang.String, boolean, int, int)
	 */
	@SuppressWarnings("unchecked")
	public List<String> findCommonNames(final String name, final boolean fuzzy, final int startIndex, final int maxResults) {
		HibernateTemplate template = getHibernateTemplate();
		return (List<String>) template.execute(new HibernateCallback() {
			public Object doInHibernate(Session session) {
				String nameStub = name;
				if(fuzzy)
					nameStub= nameStub+'%';
				Query query = session.createQuery("select distinct name from CommonName cn where cn.name like :nameStub");
				query.setParameter("nameStub", nameStub);
				query.setCacheable(true);
				query.setMaxResults(maxResults);
				query.setFirstResult(startIndex);
				return query.list();
			}
		});	
	}

	/**
	 * @see org.gbif.portal.dao.taxonomy.CommonNameDAO#findCommonNames(java.lang.String, boolean, int, int)
	 */
	@SuppressWarnings("unchecked")
	public List<CommonName> findCommonNamesFetchingCorrespondingTaxonName(final String name, final boolean fuzzy, final int startIndex, final int maxResults) {
		HibernateTemplate template = getHibernateTemplate();
		return (List<CommonName>) template.execute(new HibernateCallback() {
			public Object doInHibernate(Session session) {
				String nameStub = name;
				if(fuzzy)
					nameStub= nameStub+'%';
				Query query = session.createQuery(
						"from CommonName cn" +
						" inner join fetch cn.taxonConcept" +
						" inner join fetch cn.taxonConcept.taxonName" +
						" where cn.name like :nameStub");
				query.setParameter("nameStub", nameStub);
				query.setCacheable(true);
				query.setMaxResults(maxResults);
				query.setFirstResult(startIndex);
				return query.list();
			}
		});	
	}	
	
	/**
	 * @see org.gbif.portal.dao.taxonomy.CommonNameDAO#getCommonNamesFor(long, boolean, int, int)
	 */
	@SuppressWarnings("unchecked")
	public List<CommonName> getCommonNamesFor(final long taxonConceptId, final boolean fuzzy, final int startIndex, final int maxResults) {
		HibernateTemplate template = getHibernateTemplate();
		return (List<CommonName>) template.execute(new HibernateCallback() {
			public Object doInHibernate(Session session) {
				Query query = session.createQuery("from CommonName cn where  cn.taxonConceptId=:taxonConceptId");
				query.setParameter("taxonConceptId", taxonConceptId);
				query.setCacheable(true);
				query.setMaxResults(maxResults);
				query.setFirstResult(startIndex);
				return query.list();
			}
		});	
	}

	/**
	 * @see org.gbif.portal.dao.taxonomy.CommonNameDAO#getCommonNameFor(long)
	 */
	public CommonName getCommonNameFor(final long commonNameId) {
		HibernateTemplate template = getHibernateTemplate();
		return ( CommonName) template.execute(new HibernateCallback() {
			public Object doInHibernate(Session session) {
				Query query = session.createQuery(
						"from CommonName cn" +
						" inner join fetch cn.taxonConcept" +
						" inner join fetch cn.taxonConcept.taxonName" +
						" where cn.id like :commonNameId");
				query.setParameter("commonNameId", commonNameId);
				return query.uniqueResult();
			}
		});	
	}
}