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
package org.gbif.portal.dao.geospatial.impl.hibernate;

import java.util.List;
import java.util.Set;

import org.gbif.portal.dao.geospatial.CellDensityDAO;
import org.gbif.portal.model.ModelEntityType;
import org.gbif.portal.model.geospatial.CellDensity;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
/**
 * Implementation of CellDensityDAO.
 * 
 * @author trobertson
 * @author dmartin
 */
public class CellDensityDAOImpl extends HibernateDaoSupport implements CellDensityDAO {
	
	/**
	 * @see org.gbif.portal.dao.geospatial.CellDensityDAO#getCellDensities(org.gbif.portal.model.ModelEntityType, long)
	 */
	@SuppressWarnings("unchecked")
	public List<CellDensity> getCellDensities(final ModelEntityType entityType, final List<Long> entityIds) {
		return (List<CellDensity>) getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session) {
				StringBuffer sb = new StringBuffer("from CellDensity where identifier.type=:type ");
				if(entityIds!=null && !entityIds.isEmpty()){		
					sb.append(" and identifier.entityId in (:entityId) ");
				}
				sb.append("order by identifier.cellId");
				
				Query query = session.createQuery(sb.toString());
				query.setInteger("type", entityType.getValue());
				if(entityIds!=null && !entityIds.isEmpty()){
					query.setParameterList("entityId", entityIds);
				}
				return query.list();
			}
		});
	}

	/**
	 * @see org.gbif.portal.dao.geospatial.CellDensityDAO#getCellDensities(org.gbif.portal.model.ModelEntityType, long, int, int)
	 */
	@SuppressWarnings("unchecked")
	public List<CellDensity> getCellDensities(final ModelEntityType entityType, final List<Long> entityIds, final int minCellId, final int maxCellId) {
		return (List<CellDensity>) getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session) {
				Query query = session.createQuery("from CellDensity cd" +
						" where cd.identifier.type= :type" +
						" and cd.identifier.entityId in (:entityId)" +
						" and cd.identifier.cellId >= :minCellId" +
						" and cd.identifier.cellId <= :maxCellId" +
						" and mod(cd.identifier.cellId,360) >= :minCellIdMod360" +
						" and mod(cd.identifier.cellId,360) <= :maxCellIdMod360");
				query.setInteger("type", entityType.getValue());
				query.setParameterList("entityId", entityIds);
				query.setInteger("minCellId", minCellId);
				query.setInteger("maxCellId", maxCellId);
				int maxCellIdmod360 = maxCellId % 360;
				if(maxCellIdmod360==0){
					maxCellIdmod360=360;
				}					
				query.setInteger("minCellIdMod360", minCellId % 360);
				query.setInteger("maxCellIdMod360", maxCellIdmod360);
				return query.list();
			}
		});	
	}		
	
	/**
	 * @see org.gbif.portal.dao.geospatial.CellDensityDAO#getCellDensities(org.gbif.portal.model.ModelEntityType, long, java.util.Set)
	 */
	@SuppressWarnings("unchecked")
	public List<CellDensity> getCellDensities(final ModelEntityType entityType, final List<Long> entityIds, final Set<Integer> cellIds) {
		return (List<CellDensity>) getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session) {
				Query query = session.createQuery("from CellDensity where identifier.type=:type and identifier.entityId in (:entityId) and identifier.cellId in (:cellIds) order by identifier.cellId");
				query.setInteger("type", entityType.getValue());
				query.setParameterList("entityId", entityIds);
				query.setParameterList("cellIds", cellIds);
				return query.list();
			}
		});
	}

	/**
	 * @see org.gbif.portal.dao.geospatial.CellDensityDAO#getCellDensitiesTotal(org.gbif.portal.model.ModelEntityType, long)
	 */
	public int getCellDensitiesTotal(final ModelEntityType entityType, final List<Long> entityIds) {
		Long sum =  (Long) getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session) {
				StringBuffer sb = new StringBuffer("select sum(cd.count) from CellDensity cd where cd.identifier.type=:type ");
				if(entityIds!=null && !entityIds.isEmpty()){		
					sb.append(" and cd.identifier.entityId in (:entityId) ");
				}
				
				Query query = session.createQuery(sb.toString());				
				query.setInteger("type", entityType.getValue());
				if(entityIds!=null && !entityIds.isEmpty()){
					query.setParameterList("entityId", entityIds);
				}
				return query.uniqueResult();
			}
		});
		if(sum==null)
			return 0;
		return sum.intValue();
	}

	/**
	 * @see org.gbif.portal.dao.geospatial.CellDensityDAO#getCellDensitiesTotal(org.gbif.portal.model.ModelEntityType, long, int, int)
	 */
	public int getCellDensitiesTotal(final ModelEntityType entityType, final List<Long> entityIds, final int minCellId, final int maxCellId) {
		Long sum =  (Long) getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session) {
				Query query = session.createQuery("select sum(cd.count) from CellDensity cd" +
						" where cd.identifier.type= :type" +
						" and cd.identifier.entityId in (:entityId)" +
						" and cd.identifier.cellId >= :minCellId" +
						" and cd.identifier.cellId <= :maxCellId" +
						" and mod(cd.identifier.cellId,360) >= :minCellIdMod360" +
						" and mod(cd.identifier.cellId,360) <= :maxCellIdMod360");
				query.setInteger("type", entityType.getValue());
				query.setParameterList("entityId", entityIds);
				query.setInteger("minCellId", minCellId);
				query.setInteger("maxCellId", maxCellId);
				
				int maxCellIdmod360 = maxCellId % 360;
				if(maxCellIdmod360==0){
					maxCellIdmod360=360;
				}				
				query.setInteger("minCellIdMod360", minCellId % 360);
				query.setInteger("maxCellIdMod360", maxCellIdmod360);
				return query.uniqueResult();
			}
		});	
		if(sum==null)
			return 0;
		return sum.intValue();
	}

	/**
	 * TODO refactor into HQL
	 * 
	 * @see org.gbif.portal.dao.geospatial.CellDensityDAO#getTotalsPerCountry(org.gbif.portal.model.ModelEntityType, java.lang.Long)
	 */
	@SuppressWarnings("unchecked")
	public List<Object[]> getTotalsPerCountry(final ModelEntityType met, final Long entityId) {
		return (List<Object[]>) getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session) {
				SQLQuery query = session.createSQLQuery("select cc.iso_country_code the_iso_country_code, " +
						"cc.iso_country_code the_iso_country_code," +
						"sum(count) total_count from cell_density cd "+ 
						"inner join cell_country cc on cd.cell_id=cc.cell_id "+ 
						"where cd.type=:type and cd.entity_id=:entityId " +
						"group by iso_country_code " +
						"order by total_count desc");
				query.setParameter("type", met.getValue());
				query.setParameter("entityId", entityId);
				query.addScalar("the_iso_country_code", Hibernate.STRING);
				query.addScalar("total_count", Hibernate.INTEGER);
				return query.list();
			}
		});
	}
	
	/**
	 * TODO refactor into HQL
	 * 
	 * @see org.gbif.portal.dao.geospatial.CellDensityDAO#getTotalsPerRegion(org.gbif.portal.model.ModelEntityType, java.lang.Long)
	 */
	@SuppressWarnings("unchecked")
	public List<Object[]> getTotalsPerRegion(final ModelEntityType met, final Long entityId) {
		return (List<Object[]>) getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session) {
				SQLQuery query = session.createSQLQuery("select 0, co.region the_region, sum(count) total_count from cell_density cd " +
						"inner join cell_country cc on cd.cell_id=cc.cell_id " +
						"inner join country co on co.iso_country_code = cc.iso_country_code " +
						"where cd.type=:type and cd.entity_id=:entityId group by region order by total_count desc");
				query.setParameter("type", met.getValue());
				query.setParameter("entityId", entityId);
				query.addScalar("the_region", Hibernate.STRING);
				query.addScalar("total_count", Hibernate.INTEGER);
				return query.list();
			}
		});
	}
}