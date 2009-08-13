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

import org.gbif.portal.dao.taxonomy.RelationshipAssertionDAO;
import org.gbif.portal.model.taxonomy.RelationshipAssertion;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * DAO Implementation for RelationshipAssertion.
 * 
 * @author Donald Hobern
 */
public class RelationshipAssertionDAOImpl extends HibernateDaoSupport implements RelationshipAssertionDAO {

	@SuppressWarnings("unchecked")
	public List<RelationshipAssertion> getRelationshipAssertionsForFromTaxonConcept(final long fromTaxonConceptId) {
		HibernateTemplate template = getHibernateTemplate();
		return (List<RelationshipAssertion>) template.execute(new HibernateCallback() {
			public Object doInHibernate(Session session) {
				Query query = session.createQuery(
						"from RelationshipAssertion ra" +
						" where ra.fromConcept.id = :fromTaxonConceptId");
				query.setParameter("fromTaxonConceptId", fromTaxonConceptId);
				query.setCacheable(true);
				return query.list();
			}
		});	
	}

	@SuppressWarnings("unchecked")
	public List<RelationshipAssertion> getRelationshipAssertionsForToTaxonConcept(final long toTaxonConceptId) {
		HibernateTemplate template = getHibernateTemplate();
		return (List<RelationshipAssertion>) template.execute(new HibernateCallback() {
			public Object doInHibernate(Session session) {
				Query query = session.createQuery(
						"from RelationshipAssertion ra" +
						" where ra.toConcept.id = :toTaxonConceptId");
				query.setParameter("toTaxonConceptId", toTaxonConceptId);
				query.setCacheable(true);
				return query.list();
			}
		});	
	}

}