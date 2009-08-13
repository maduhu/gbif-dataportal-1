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

import org.gbif.portal.dao.resources.PropertyStoreNamespaceDAO;
import org.gbif.portal.model.resources.PropertyStoreNamespace;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * A hibernate based DAO implementation for PropertyStoreNamespaceDAO.
 * 
 * @author Donald Hobern
 */
public class PropertyStoreNamespaceDAOImpl extends HibernateDaoSupport implements PropertyStoreNamespaceDAO {

	/* (non-Javadoc)
	 * @see org.gbif.portal.dao.resources.PropertyStoreNamespaceDAO#getPropertyStoreNamespacesForResourceAccessPoint(long)
	 */
	@SuppressWarnings("unchecked")
	public List<PropertyStoreNamespace> getPropertyStoreNamespacesForResourceAccessPoint(final long resourceAccessPointId) {
		HibernateTemplate template = getHibernateTemplate();
		return (List<PropertyStoreNamespace>) template.execute(new HibernateCallback() {
			public Object doInHibernate(Session session) {
				Query query = session.createQuery(
						"select rap.propertyStoreNamespaces from ResourceAccessPoint rap" +
						" where rap.id = ?"
				);
				query.setParameter(0, resourceAccessPointId);
				query.setCacheable(true);
				return query.list();
			}
		});		
	}
}