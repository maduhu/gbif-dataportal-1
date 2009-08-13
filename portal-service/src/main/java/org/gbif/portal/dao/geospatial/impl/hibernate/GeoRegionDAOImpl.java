/***************************************************************************
 * Copyright (C) 2006 Global Biodiversity Information Facility Secretariat.
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
package org.gbif.portal.dao.geospatial.impl.hibernate;

import java.util.List;

import org.gbif.portal.dao.geospatial.GeoRegionDAO;
import org.gbif.portal.model.geospatial.GeoRegion;
import org.gbif.portal.model.occurrence.OccurrenceRecord;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * A hibernate based DAO implementation of the {@link GeoRegionDAO} interface.
 * 
 * @author dmartin
 */
public class GeoRegionDAOImpl extends HibernateDaoSupport implements GeoRegionDAO {

	/**
	 * @see org.gbif.portal.dao.geospatial.GeoRegionDAO#getOccurrencesForGeoRegion(java.lang.Long, int, int)
	 */
	@SuppressWarnings("unchecked")
	public List<OccurrenceRecord> getOccurrencesForGeoRegion(final Long geoRegionId, final int startIndex, final int maxResults){
		return (List<OccurrenceRecord>) getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session) {
//				StringBuffer sb = new StringBuffer("select gm.occurrenceRecord from GeoMapping gm where gm.identifier.geoRegionId = :geoRegionId");
				StringBuffer sb = new StringBuffer("from org.gbif.portal.model.occurrence.OccurrenceRecord as oc " +
						"inner join fetch oc.catalogueNumber " +
						"inner join fetch oc.collectionCode " +
						"inner join fetch oc.institutionCode  " +
						"where oc.geoMappings.geoRegionId = :geoRegionId");
				Query query = session.createQuery(sb.toString());
				query.setLong("geoRegionId", geoRegionId);
				query.setFirstResult(startIndex);
				query.setMaxResults(maxResults);
				return query.list();
			}
		});
	}

	/**
	 * @see org.gbif.portal.dao.geospatial.GeoRegionDAO#getGeoRegionsForOccurrenceRecord(java.lang.Long)
	 */
	@SuppressWarnings("unchecked")
	public List<GeoRegion> getGeoRegionsForOccurrenceRecord(final Long occurrenceRecordId){
		return (List<GeoRegion>) getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session) {
				Query query = session.createQuery("select gr from OccurrenceRecord ore " +
						" inner join ore.geoMappings as gm" +
						" inner join gm.geoRegion as gr "+
						" where ore.id=:id");
				query.setLong("id", occurrenceRecordId);
				return query.list();
			}
		});
	}
	
	/**
	 * @see org.gbif.portal.dao.geospatial.GeoRegionDAO#getGeoRegionFor(java.lang.Long)
	 */
	public GeoRegion getGeoRegionFor(final Long geoRegionId) {
		return (GeoRegion) getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session) {
				return (GeoRegion) session.get(GeoRegion.class, geoRegionId);
			}
		});
	}
	
	/**
	 * @see org.gbif.portal.dao.geospatial.GeoRegionDAO#getGeoRegionFor(java.lang.Long)
	 */
	@SuppressWarnings("unchecked")
	public List<GeoRegion> getGeoRegions() {
		return (List<GeoRegion>) getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session) {
				Query query =  session.createQuery("from GeoRegion gr order by gr.regionType, gr.name");
				return (List<GeoRegion>)query.list();
			}
		});
	}

	public List getGeoRegionsForCountry(final String isoCountryCode) {
		HibernateTemplate template = getHibernateTemplate();
		return (List) template.execute(new HibernateCallback() {
			public Object doInHibernate(Session session) {
				Query query = session.createQuery("select id, name from GeoRegion where isoCountryCode=? order by name");
				query.setParameter(0, isoCountryCode);
				query.setCacheable(true);
				return query.list();
			}
		});	
	}	
}