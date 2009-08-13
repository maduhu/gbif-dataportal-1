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

package org.gbif.portal.dao.resources.impl.hibernate;

import java.util.Date;
import java.util.List;

import org.gbif.portal.dao.resources.DataProviderDAO;
import org.gbif.portal.model.resources.DataProvider;
import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * A hibernate based DAO implementation for DataProviderDAO.
 * 
 * @author Dave Martin
 */
public class DataProviderDAOImpl extends HibernateDaoSupport implements DataProviderDAO {

	/**
	 * @see org.gbif.portal.dao.resources.DataProviderDAO#getDataProviderFor(long)
	 */
	public DataProvider getDataProviderFor(final long dataProviderId) {
		HibernateTemplate template = getHibernateTemplate();
		return (DataProvider) template.execute(new HibernateCallback() {
			public Object doInHibernate(Session session) {
				Query query = session.createQuery("from DataProvider dp where dp.id = ?");
				query.setParameter(0, dataProviderId);
				query.setCacheable(true);
				return query.uniqueResult();
			}
		});		
	}
	
	/**
	 * @see org.gbif.portal.dao.resources.DataProviderDAO#getDataProviderFor(long)
	 */
	@SuppressWarnings("unchecked")
	public List<DataProvider> getDataProviderAssociatedWithUser(final String emailUsername, final String emailDomain) {
		HibernateTemplate template = getHibernateTemplate();
		return (List<DataProvider>) template.execute(new HibernateCallback() {
			public Object doInHibernate(Session session) {
				Query query = session.createQuery("select distinct dp from DataProvider dp " +
						"inner join dp.dataProviderAgents agents " +
						"inner join  agents.agent ag " +
						"where ag.email like :emailUsername and ag.email like :emailDomain");
				query.setParameter("emailUsername", emailUsername);
				query.setParameter("emailDomain", emailDomain);
				query.setCacheable(true);
				return query.list();
			}
		});		
	}	

	/**
	 * @see org.gbif.portal.dao.resources.DataProviderDAO#getDataProvidersOfferingTaxonomies()
	 */
	@SuppressWarnings("unchecked")
	public List<DataProvider> getDataProvidersOfferingTaxonomies() {
		HibernateTemplate template = getHibernateTemplate();
		return (List<DataProvider>) template.execute(new HibernateCallback() {
			public Object doInHibernate(Session session) {
				Query query = session.createQuery(
						"select dr.dataProvider from DataResource dr" +
						" inner join dr.dataProvider as dp" +
						" where dr.sharedTaxonomy=true" +
						" group by dp.id order by dp.name"
				);
				query.setCacheable(true);
				return query.list();
			}
		});		
	}
	
	/**
	 * @see org.gbif.portal.dao.resources.DataProviderDAO#findDataProviders(java.lang.String, boolean, int, int)
	 */
	@SuppressWarnings("unchecked")
	public List<DataProvider> findDataProviders(final String nameStub, final boolean fuzzy, final String isoCountryCode, final Date modifiedSince, final int startIndex, final int maxResults) {
		HibernateTemplate template = getHibernateTemplate();
		return (List<DataProvider>) template.execute(new HibernateCallback() {
			public Object doInHibernate(Session session) {
				Criteria criteria = session.createCriteria(DataProvider.class, "dp");
				if (nameStub != null) {
					if (fuzzy) {
						criteria = criteria.add(Restrictions.like("dp.name", nameStub, MatchMode.START));
					}
					else {
						criteria = criteria.add(Restrictions.eq("dp.name", nameStub));
					}
				}
				if (isoCountryCode != null) {
					criteria = criteria.add(Restrictions.eq("dp.isoCountryCode", isoCountryCode));
				}
				if (modifiedSince != null) {
					criteria = criteria.add(Restrictions.ge("dp.modified", modifiedSince));
				}
				criteria.setCacheable(true);
				return criteria.list();
			}
		});		
	}	
	
	/**
	 * @see org.gbif.portal.dao.resources.DataResourceDAO#getDataResourceCountsForCountry(java.lang.Long)
	 */
	@SuppressWarnings("unchecked")
	public List<Object[]> getDataProviderCountsForCountry(final String isoCountryCode) {
		HibernateTemplate template = getHibernateTemplate();
		return (List) template.execute(new HibernateCallback() {
			public Object doInHibernate(Session session) {
				Query query = session.createSQLQuery("select distinct dp.id, dp.name as dp_name, sum(rc.count) from data_provider dp" +
						" inner join data_resource dr on dr.data_provider_id=dp.id" +
						" inner join resource_country rc on rc.data_resource_id=dr.id" +	
						" where rc.iso_country_code=? group by dp.id order by dp_name");
				query.setParameter(0, isoCountryCode);
				query.setCacheable(true);
				return query.list();
			}
		});	
	}
	
	/**
	 * @see org.gbif.portal.dao.resources.DataProviderDAO#findDataProviders(java.lang.String, boolean, int, int)
	 */
	@SuppressWarnings("unchecked")
	public Long countDataProviders(final String nameStub, final boolean fuzzy, final String isoCountryCode, final Date modifiedSince) {
		HibernateTemplate template = getHibernateTemplate();
		return ((Integer) template.execute(new HibernateCallback() {
			public Object doInHibernate(Session session) {
				Criteria criteria = session.createCriteria(DataProvider.class, "dp");
				if (nameStub != null) {
					if (fuzzy) {
						criteria = criteria.add(Restrictions.like("dp.name", nameStub, MatchMode.START));
					}
					else {
						criteria = criteria.add(Restrictions.eq("dp.name", nameStub));
					}
				}
				if (isoCountryCode != null) {
					criteria = criteria.add(Restrictions.eq("dp.isoCountryCode", isoCountryCode));
				}
				if (modifiedSince != null) {
					criteria = criteria.add(Restrictions.ge("dp.modified", modifiedSince));
				}
				criteria.setProjection(Projections.rowCount());
				return criteria.uniqueResult();
		}
		})).longValue();		
	}	
	
	/**
	 * @see org.gbif.portal.dao.resources.DataProviderDAO#getTotalDataProviderCount()
	 */
	public int getTotalDataProviderCount() {
		HibernateTemplate template = getHibernateTemplate();
		Object result =  template.execute(new HibernateCallback() {
			public Object doInHibernate(Session session) {
				Query query = session.createQuery("select count(dp.id) from DataProvider dp");
				return query.uniqueResult();
			}
		});
		if (result instanceof Integer)
			return ((Integer)result).intValue();
		if (result instanceof Long)
			return ((Long)result).intValue();
		return 0;
	}

	/**
	 * @see org.gbif.portal.dao.resources.DataProviderDAO#getSystemDetails()
	 */
	@SuppressWarnings("unchecked")
	public List<Object[]> getSystemDetails() {
		HibernateTemplate template = getHibernateTemplate();	
		List results =  (List) template.execute(new HibernateCallback() {
			public Object doInHibernate(Session session) {
				Query query = session.createSQLQuery(
				"Select * from view_indexing");				
				return query.list();
			}
		});			
		return results;
	}

	/**
	 * @see org.gbif.portal.dao.resources.DataProviderDAO#getDataProviderList()
	 */
	public List getDataProviderList() {
		HibernateTemplate template = getHibernateTemplate();	
		List results =  (List) template.execute(new HibernateCallback() {
			public Object doInHibernate(Session session) {
				Query query = session.createQuery("select id, name from DataProvider where deleted is null order by name");			
				return query.list();
			}
		});			
		return results;
	}
	
	/**
	 * @see org.gbif.portal.dao.resources.DataProviderDAO#getAllDataProviders()
	 */
	public List<DataProvider> getAllDataProviders() {
		HibernateTemplate template = getHibernateTemplate();	
		List results =  (List) template.execute(new HibernateCallback() {
			public Object doInHibernate(Session session) {
				Query query = session.createQuery("from DataProvider order by name");			
				return query.list();
			}
		});			
		return results;
	}

	/**
	 * @see org.gbif.portal.dao.resources.DataProviderDAO#getDataProviderCountsForHostCountry(java.lang.String, boolean)
	 */
	public int getDataProviderCountsForHostCountry(final String isoCountryCode,
			final boolean georeferenced) {
		HibernateTemplate template = getHibernateTemplate();	
		Integer result =  (Integer) template.execute(new HibernateCallback() {
			public Object doInHibernate(Session session) {
				StringBuffer sb =  new StringBuffer("select sum(dp.");
				if(georeferenced){
					sb.append("occurrenceCount");
				} else {
					sb.append("occurrenceCoordinateCount");
				}
				sb.append(") from DataProvider dp where dp.isoCountryCode=:isoCountryCode");
				Query query = session.createQuery(sb.toString());
				query.setParameter("isoCountryCode", isoCountryCode);
				return query.uniqueResult();
			}
		});			
		return result;
	}

	/**
	 * @see org.gbif.portal.dao.resources.DataProviderDAO#getRolloverDates()
	 */
	@SuppressWarnings("unchecked")
  public List<Date> getRolloverDates() {
		HibernateTemplate template = getHibernateTemplate();	
		List<Date> results =  (List) template.execute(new HibernateCallback() {
			public Object doInHibernate(Session session) {
				SQLQuery query = session.createSQLQuery("select rollover_date from rollover order by rollover_date desc");
				query.addScalar("rollover_date", Hibernate.DATE);
				return query.list();
			}
		});			
		return results;
  }

	/**
	 * @see org.gbif.portal.dao.resources.DataProviderDAO#getInternationalDataProviders(boolean)
	 */
	@SuppressWarnings("unchecked")
  public List<DataProvider> getInternationalDataProviders(final boolean withOccurrencesOnly) {
		HibernateTemplate template = getHibernateTemplate();
		return (List<DataProvider>) template.execute(new HibernateCallback() {
			public Object doInHibernate(Session session) {
				StringBuffer sb = new StringBuffer("from DataProvider dp where dp.isoCountryCode is null");
				if(withOccurrencesOnly){
					sb.append(" and dp.occurrenceCount > 0");
				}
				sb.append(" order by dp.name");
				Query query = session.createQuery(sb.toString());
				return query.list();
			}
		});	
  }
}