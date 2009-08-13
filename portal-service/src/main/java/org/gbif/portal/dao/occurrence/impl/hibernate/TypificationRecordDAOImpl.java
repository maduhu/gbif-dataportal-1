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

import java.util.LinkedList;
import java.util.List;

import org.gbif.portal.dao.occurrence.TypificationRecordDAO;
import org.gbif.portal.model.occurrence.TypeStatus;
import org.gbif.portal.model.occurrence.TypificationRecord;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * A Hibernate DAO Implementation for accessing TypificationRecord model objects.
 * 
 * @author Donald Hobern
 */
public class TypificationRecordDAOImpl extends HibernateDaoSupport implements TypificationRecordDAO {

	/**
	 * @see org.gbif.portal.dao.occurrence.TypificationRecordDAO#getTypificationRecordsForOccurrenceRecord(long)
	 */
	@SuppressWarnings("unchecked")
	public List<TypificationRecord> getTypificationRecordsForOccurrenceRecord(final long occurrenceRecordId) {
		HibernateTemplate template = getHibernateTemplate();
		return (List<TypificationRecord>) template.execute(new HibernateCallback() {
			public Object doInHibernate(Session session) {
				Query query = session.createQuery(
						"from TypificationRecord tr" +
						" where tr.occurrenceRecordId = ?");
				query.setParameter(0, occurrenceRecordId);
				query.setCacheable(true);
				return query.list();
			}
		});		
	}

	/**
	 * @see org.gbif.portal.dao.occurrence.TypificationRecordDAO#getTypeStatusForOccurrenceRecords(java.util.List)
	 */
	@SuppressWarnings("unchecked")
	public List<TypeStatus> getTypeStatusForOccurrenceRecords(final List<Long> occurrenceRecordIds) {
		HibernateTemplate template = getHibernateTemplate();
		return (List<TypeStatus>) template.execute(new HibernateCallback() {
			public Object doInHibernate(Session session) {
				Query query = session.createQuery(
						"from TypeStatus ts" +
						" where ts.occurrenceRecordId in (:occurrenceRecordIds)");
				query.setParameterList("occurrenceRecordIds", occurrenceRecordIds);
				query.setCacheable(true);
				return query.list();
			}
		});	
	}
	
	/**
	 * @see org.gbif.portal.dao.occurrence.TypificationRecordDAO#getTypificationRecordsForTaxonName(long)
	 */
	@SuppressWarnings("unchecked")
	public List<TypificationRecord> getTypificationRecordsForTaxonName(final long taxonNameId) {
		HibernateTemplate template = getHibernateTemplate();
		return (List<TypificationRecord>) template.execute(new HibernateCallback() {
			public Object doInHibernate(Session session) {
				Query query = session.createQuery(
						"from TypificationRecord tr" +
						" where tr.taxonNameId = ?");
				query.setParameter(0, taxonNameId);
				query.setCacheable(true);
				return query.list();
			}
		});		
	}
	
	/**
	 * @see org.gbif.portal.dao.occurrence.TypificationRecordDAO#getTypificationRecordsForTaxonName(long)
	 */
	@SuppressWarnings("unchecked")
	public List<TypificationRecord> getTypificationRecordsForNamesOfPartnersOfTaxonConcept(final long taxonConceptId) {
		HibernateTemplate template = getHibernateTemplate();
		final List<Long> taxonNameIds =  (List<Long>) template.execute(new HibernateCallback() {
			public Object doInHibernate(Session session) {
				Query query = session.createQuery("select distinct tc.taxonName.id from TaxonConcept tc where tc.partnerConceptId=:taxonConceptId");
				query.setParameter("taxonConceptId", taxonConceptId);
				return query.list();
			}
		});		
		
		if (taxonNameIds.size()>0) {
			return (List<TypificationRecord>) template.execute(new HibernateCallback() {
				public Object doInHibernate(Session session) {
					Query query = session.createQuery(
							"from TypificationRecord tr inner join fetch tr.occurrenceRecord inner join fetch tr.dataResource where tr.taxonNameId  in(:ids)");
					query.setParameterList("ids", taxonNameIds);
					query.setCacheable(true);
					return query.list();
				}
			});		
		} else {
			return new LinkedList<TypificationRecord>();
		}
	}
}