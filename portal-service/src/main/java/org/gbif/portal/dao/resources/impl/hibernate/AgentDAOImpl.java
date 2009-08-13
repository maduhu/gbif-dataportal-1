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

import java.util.List;

import org.gbif.portal.dao.resources.AgentDAO;
import org.gbif.portal.model.resources.DataProviderAgent;
import org.gbif.portal.model.resources.DataResourceAgent;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * A Hibernate DAO Implementation for accessing Agent model objects.
 * 
 * @author Donald Hobern
 */
public class AgentDAOImpl extends HibernateDaoSupport implements AgentDAO {

	/**
	 * @see org.gbif.portal.dao.resources.AgentDAO#getAgentsForDataProvider(long)
	 */
	@SuppressWarnings("unchecked")
	public List<DataProviderAgent> getAgentsForDataProvider(final long dataProviderId) {
		HibernateTemplate template = getHibernateTemplate();
		return (List<DataProviderAgent>) template.execute(new HibernateCallback() {
			public Object doInHibernate(Session session) {
				Query query = session.createQuery(
						"from DataProviderAgent pa" +
						" where pa.dataProvider.id = ?");
				query.setParameter(0, dataProviderId);
				query.setCacheable(true);
				return query.list();
			}
		});		
	}

	/**
	 * @see org.gbif.portal.dao.resources.AgentDAO#getAgentsForDataResource(long)
	 */
	@SuppressWarnings("unchecked")
	public List<DataResourceAgent> getAgentsForDataResource(final long dataResourceId) {
		HibernateTemplate template = getHibernateTemplate();
		return (List<DataResourceAgent>) template.execute(new HibernateCallback() {
			public Object doInHibernate(Session session) {
				Query query = session.createQuery(
						"from DataResourceAgent ra" +
						" where ra.dataResource.id = ?");
				query.setParameter(0, dataResourceId);
				query.setCacheable(true);
				return query.list();
			}
		});		
	}

	/**
	 * @see org.gbif.portal.dao.resources.AgentDAO#getAgentsForEmailAddress(java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	public List getAgentsForEmailAddress(final String email) {
		HibernateTemplate template = getHibernateTemplate();
		return (List<DataResourceAgent>) template.execute(new HibernateCallback() {
			public Object doInHibernate(Session session) {
				Query query = session.createQuery(
						"from Agent a" +
						" where a.email like ?");
				query.setParameter(0, email);
				query.setCacheable(true);
				return query.list();
			}
		});	
	}
}