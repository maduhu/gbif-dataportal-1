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

import org.apache.commons.lang.StringUtils;
import org.gbif.portal.dao.occurrence.RawOccurrenceRecordDAO;
import org.gbif.portal.model.occurrence.RawOccurrenceRecord;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * A Hibernate DAO Implementation for accessing OccurrenceRecord model objects.
 * 
 * @author dmartin
 */
public class RawOccurrenceRecordDAOImpl extends HibernateDaoSupport implements RawOccurrenceRecordDAO {

	/**
	 * @see org.gbif.portal.dao.occurrence.OccurrenceRecordDAO#getRawOccurrenceRecordFor(long)
	 */
	public RawOccurrenceRecord getRawOccurrenceRecordFor(final long rawOccurrenceRecordId) {
		HibernateTemplate template = getHibernateTemplate();
		return (RawOccurrenceRecord) template.execute(new HibernateCallback() {
			public Object doInHibernate(Session session) {
				Query query = session.createQuery(
						"from RawOccurrenceRecord ror " +
						"where ror.id = ?");
				query.setParameter(0, rawOccurrenceRecordId);
				query.setCacheable(true);
				return query.uniqueResult();
			}
		});
	}
	
	/**
	 * @see org.gbif.portal.dao.occurrence.OccurrenceRecordDAO#getRawOccurrenceRecordFor(long)
	 */
	@SuppressWarnings("unchecked")
	public List<RawOccurrenceRecord> findRawOccurrenceRecord(final long dataResourceId, final String catalogueNumber, final int startIndex, final int maxResults) {
		HibernateTemplate template = getHibernateTemplate();
		return (List<RawOccurrenceRecord>) template.execute(new HibernateCallback() {
			public Object doInHibernate(Session session) {
				StringBuffer sb = new StringBuffer("from RawOccurrenceRecord ror where ror.dataResourceId = :dataResourceId ");
				if(StringUtils.isNotEmpty(catalogueNumber)){
					sb.append("and ror.catalogueNumber = :catalogueNumber");
				}
				Query query = session.createQuery(sb.toString());
				query.setParameter("dataResourceId", dataResourceId);
				if(StringUtils.isNotEmpty(catalogueNumber)){
					query.setParameter("catalogueNumber", catalogueNumber);
				}
				query.setCacheable(true);
				query.setFirstResult(startIndex);
				query.setMaxResults(maxResults);
				return query.list();
			}
		});		
	}	
}