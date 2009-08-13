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
package org.gbif.portal.dao.occurrence.impl.hibernate;

import java.util.List;

import org.gbif.portal.dao.occurrence.LinkRecordDAO;
import org.gbif.portal.model.occurrence.LinkRecord;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * A Hibernate DAO Implementation for accessing LinkRecord model objects.
 * 
 * @author Donald Hobern
 */
public class LinkRecordDAOImpl extends HibernateDaoSupport implements LinkRecordDAO {

	/* (non-Javadoc)
	 * @see org.gbif.portal.dao.occurrence.LinkRecordDAO#getLinkRecordsForOccurrenceRecord(long)
	 */
	@SuppressWarnings("unchecked")
	public List<LinkRecord> getLinkRecordsForOccurrenceRecord(final long occurrenceRecordId) {
		HibernateTemplate template = getHibernateTemplate();
		return (List<LinkRecord>) template.execute(new HibernateCallback() {
			public Object doInHibernate(Session session) {
				Query query = session.createQuery(
						"from LinkRecord lr" +
						" where lr.occurrenceRecordId = ?");
				query.setParameter(0, occurrenceRecordId);
				query.setCacheable(true);
				return query.list();
			}
		});		
	}

	/* (non-Javadoc)
	 * @see org.gbif.portal.dao.occurrence.LinkRecordDAO#getLinkRecordsForTaxonConcept(long)
	 */
	@SuppressWarnings("unchecked")
	public List<LinkRecord> getLinkRecordsForTaxonConcept(final long taxonConceptId) {
		HibernateTemplate template = getHibernateTemplate();
		return (List<LinkRecord>) template.execute(new HibernateCallback() {
			public Object doInHibernate(Session session) {
				Query query = session.createQuery(
						"from LinkRecord lr" +
						" where lr.taxonConceptId = ?");
				query.setParameter(0, taxonConceptId);
				query.setCacheable(true);
				return query.list();
			}
		});		
	}
}