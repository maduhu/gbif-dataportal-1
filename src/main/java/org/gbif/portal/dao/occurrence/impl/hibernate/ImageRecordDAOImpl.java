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

import org.gbif.portal.dao.occurrence.ImageRecordDAO;
import org.gbif.portal.model.occurrence.ImageRecord;
import org.gbif.portal.model.occurrence.ORImage;
import org.gbif.portal.model.resources.ResourceType;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * A Hibernate DAO Implementation for accessing ImageRecord model objects.
 * 
 * @author Donald Hobern
 * @author dmartin
 */
public class ImageRecordDAOImpl extends HibernateDaoSupport implements ImageRecordDAO {

//	protected int imageTypeThreshold = 2;
	
	/**
	 * @see org.gbif.portal.dao.occurrence.ImageRecordDAO#getImageRecordsForOccurrenceRecord(long)
	 */
	@SuppressWarnings("unchecked")
	public List<ImageRecord> getImageRecordsForOccurrenceRecord(final long occurrenceRecordId) {
		HibernateTemplate template = getHibernateTemplate();
		return (List<ImageRecord>) template.execute(new HibernateCallback() {
			public Object doInHibernate(Session session) {
				Query query = session.createQuery("from ImageRecord ir where ir.occurrenceRecordId = ?");
				query.setParameter(0, occurrenceRecordId);
				query.setCacheable(true);
				return query.list();
			}
		});		
	}

	/**
	 * @see org.gbif.portal.dao.occurrence.ImageRecordDAO#getImageRecordsForOccurrenceRecords(java.util.List)
	 */
	@SuppressWarnings("unchecked")
	public List<ORImage> getImageRecordsForOccurrenceRecords(final List<Long> occurrenceRecordIds) {
		HibernateTemplate template = getHibernateTemplate();
		return (List<ORImage>) template.execute(new HibernateCallback() {
			public Object doInHibernate(Session session) {
				Query query = session.createQuery("from ORImage oir where oir.occurrenceRecordId in (:occurrenceRecordIds)");
				query.setParameterList("occurrenceRecordIds", occurrenceRecordIds);
				query.setCacheable(true);
				return query.list();
			}
		});		
	}

	/**
	 * @see org.gbif.portal.dao.occurrence.ImageRecordDAO#getImageRecordsForTaxonConcept(long)
	 */
	@SuppressWarnings("unchecked")
	public List<ImageRecord> getImageRecordsForTaxonConcept(final long taxonConceptId) {
		HibernateTemplate template = getHibernateTemplate();
		return (List<ImageRecord>) template.execute(new HibernateCallback() {
			public Object doInHibernate(Session session) {
				Query query = session.createQuery("from ImageRecord ir" +
						" left join fetch ir.dataResource dr " +
						" left outer join dr.resourceRanks rr " +
						" where ir.taxonConceptId = :taxonConceptId" +
//						" and ir.imageTypeValue >= :imageTypeThreshold" +
						" or ir.taxonConceptLite.partnerConceptId = :taxonConceptId" +
						" and (rr.resourceType = :resourceType or rr.resourceType is null)" +
						" order by rr.rank desc");
				query.setParameter("taxonConceptId", taxonConceptId);
				query.setParameter("resourceType", ResourceType.IMAGE_DATA_RESOURCE);
//				query.setParameter("imageTypeThreshold", imageTypeThreshold);
				query.setCacheable(true);
				return query.list();
			}
		});		
	}

	/**
	 * @see org.gbif.portal.dao.occurrence.ImageRecordDAO#getImageRecordFor(long)
	 */
	public ImageRecord getImageRecordFor(final long imageRecordId) {
		HibernateTemplate template = getHibernateTemplate();
		return (ImageRecord) template.execute(new HibernateCallback() {
			public Object doInHibernate(Session session) {
				return session.get(ImageRecord.class, imageRecordId);
			}
		});	
	}


//	/**
//	 * @param imageTypeThreshold the imageTypeThreshold to set
//	 */
//	public void setImageTypeThreshold(int imageTypeThreshold) {
//		this.imageTypeThreshold = imageTypeThreshold;
//	}
}