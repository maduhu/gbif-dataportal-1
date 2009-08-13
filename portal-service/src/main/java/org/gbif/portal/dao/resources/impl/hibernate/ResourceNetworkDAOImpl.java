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

import org.gbif.portal.dao.resources.ResourceNetworkDAO;
import org.gbif.portal.model.resources.ResourceNetwork;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * A hibernate based DAO implementation for ResourceNetworkDAO.
 * 
 * @author Donald Hobern
 */
public class ResourceNetworkDAOImpl extends HibernateDaoSupport implements ResourceNetworkDAO {

	/**
	 * @see org.gbif.portal.dao.resources.ResourceNetworkDAO#getResourceNetworkFor(long)
	 */
	public ResourceNetwork getResourceNetworkFor(final long resourceNetworkId) {
		HibernateTemplate template = getHibernateTemplate();
		return (ResourceNetwork) template.execute(new HibernateCallback() {
			public Object doInHibernate(Session session) {
				Query query = session.createQuery("from ResourceNetwork rn where rn.id = ?");
				query.setParameter(0, resourceNetworkId);
				query.setCacheable(true);
				return query.uniqueResult();
			}
		});		
	}

	/**
	 * @see org.gbif.portal.dao.resources.ResourceNetworkDAO#getResourceNetworksForDataResource(long)
	 */
	@SuppressWarnings("unchecked")
	public List<ResourceNetwork> getResourceNetworksForDataResource(final long dataResourceId) {
		HibernateTemplate template = getHibernateTemplate();
		return (List<ResourceNetwork>) template.execute(new HibernateCallback() {
			public Object doInHibernate(Session session) {
				Query query = session.createQuery(
						"select nm.resourceNetwork from NetworkMembership nm" +
						" join nm.dataResource dr" +
						" where dr.id = ?"
				);
				query.setParameter(0, dataResourceId);
				query.setCacheable(true);
				return query.list();
			}
		});		
	}

	/**
	 * @see org.gbif.portal.dao.resources.ResourceNetworkDAO#findResourceNetworks(java.lang.String, boolean, int, int)
	 */
	@SuppressWarnings("unchecked")
	public List<ResourceNetwork> findResourceNetworks(final String nameStub, final boolean fuzzy, final String code, final Date modifiedSince, final int startIndex, final int maxResults) {
		HibernateTemplate template = getHibernateTemplate();
		return (List<ResourceNetwork>) template.execute(new HibernateCallback() {
			public Object doInHibernate(Session session) {
				Criteria criteria = session.createCriteria(ResourceNetwork.class, "rn");
				if (nameStub != null) {
					if (fuzzy) {
						criteria = criteria.add(Restrictions.like("rn.name", nameStub, MatchMode.START));
					}
					else {
						criteria = criteria.add(Restrictions.eq("rn.name", nameStub));
					}
				}
				if (code != null) {
					criteria = criteria.add(Restrictions.eq("rn.code", code));
				}
				if (modifiedSince != null) {
					criteria = criteria.add(Restrictions.ge("rn.modified", modifiedSince));
				}
				criteria.setCacheable(true);
				return criteria.list();
			}
		});		
	}	
	
	/**
	 * @see org.gbif.portal.dao.resources.ResourceNetworkDAO#findResourceNetworks(java.lang.String, boolean, int, int)
	 */
	@SuppressWarnings("unchecked")
	public Long countResourceNetworks(final String nameStub, final boolean fuzzy, final String code, final Date modifiedSince) {
		HibernateTemplate template = getHibernateTemplate();
		return ((Integer) template.execute(new HibernateCallback() {
			public Object doInHibernate(Session session) {
				Criteria criteria = session.createCriteria(ResourceNetwork.class, "rn");
				if (nameStub != null) {
					if (fuzzy) {
						criteria = criteria.add(Restrictions.like("rn.name", nameStub, MatchMode.START));
					}
					else {
						criteria = criteria.add(Restrictions.eq("rn.name", nameStub));
					}
				}
				if (code != null) {
					criteria = criteria.add(Restrictions.eq("rn.code", code));
				}
				if (modifiedSince != null) {
					criteria = criteria.add(Restrictions.ge("rn.modified", modifiedSince));
				}
				criteria.setProjection(Projections.rowCount());
				return criteria.uniqueResult();
		}
		})).longValue();		
	}
	
	/**
	 * @see org.gbif.portal.dao.resources.ResourceNetworkDAO#getResourceNetworkList()
	 */
	public List getResourceNetworkList() {
		HibernateTemplate template = getHibernateTemplate();
		return (List) template.execute(new HibernateCallback() {
			public Object doInHibernate(Session session) {
				Query query = session.createQuery("select id, name from ResourceNetwork");
				query.setCacheable(true);
				return query.list();
			}
		});
	}	
}