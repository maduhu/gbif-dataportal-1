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

import org.gbif.portal.dao.taxonomy.RemoteConceptDAO;
import org.gbif.portal.model.taxonomy.IdType;
import org.gbif.portal.model.taxonomy.RemoteConcept;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * Hibernate implementation of the remote concept DAO interface.
 * 
 * @author dmartin
 */
public class RemoteConceptDAOImpl extends HibernateDaoSupport implements RemoteConceptDAO {

	/**
	 * @see org.gbif.portal.dao.taxonomy.RemoteConceptDAO#findRemoteUrlFor(long)
	 */
	@SuppressWarnings("unchecked")
	public List<String> findRemoteUrlFor(final long taxonConceptId) {
		HibernateTemplate template = getHibernateTemplate();
		return (List<String>) template.execute(new HibernateCallback() {
			public Object doInHibernate(Session session) {
				Query query = session.createQuery(
						"select remoteId from RemoteConcept rc" +
						" where rc.taxonConceptId = :taxonConceptId and rc.idType= :idType");
				query.setParameter("taxonConceptId", taxonConceptId);
				query.setParameter("idType", IdType.URL);
				query.setCacheable(true);
				return query.list();
			}
		});	
	}

	/**
	 * @see org.gbif.portal.dao.taxonomy.RemoteConceptDAO#findRemoteUrlFor(long)
	 */
	@SuppressWarnings("unchecked")
	public List<RemoteConcept> findRemoteConceptsFor(final long taxonConceptId) {
		HibernateTemplate template = getHibernateTemplate();
		return (List<RemoteConcept>) template.execute(new HibernateCallback() {
			public Object doInHibernate(Session session) {
				Query query = session.createQuery(
						"from RemoteConcept rc" +
						" where rc.taxonConceptId = :taxonConceptId order by rc.idType");
				query.setParameter("taxonConceptId", taxonConceptId);
				query.setCacheable(true);
				return query.list();
			}
		});	
	}
}