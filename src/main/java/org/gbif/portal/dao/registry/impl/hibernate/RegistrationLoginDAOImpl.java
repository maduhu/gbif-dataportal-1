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
package org.gbif.portal.dao.registry.impl.hibernate;

import java.util.List;

import org.gbif.portal.dao.registry.RegistrationLoginDAO;
import org.gbif.portal.model.resources.RegistrationLogin;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * @author dmartin
 */
public class RegistrationLoginDAOImpl extends HibernateDaoSupport implements RegistrationLoginDAO {

	/**
	 * @see org.gbif.portal.dao.resources.AgentDAO#getBusinessKeysFor(long)
	 */
	@SuppressWarnings("unchecked")
	public List<String> getBusinessKeysFor(final String loginId) {
		HibernateTemplate template = getHibernateTemplate();
		return (List<String>) template.execute(new HibernateCallback() {
			public Object doInHibernate(Session session) {
				Query query = session.createQuery(
						"select rl.businessKey from RegistrationLogin rl" +
						" where rl.loginId = ?");
				query.setParameter(0, loginId);
				query.setCacheable(true);
				return query.list();
			}
		});		
	}
	
	public void createRegistrationLogin(final RegistrationLogin rl){
		HibernateTemplate template = getHibernateTemplate();
		template.execute(new HibernateCallback() {
			public Object doInHibernate(Session session) {
				session.save(rl);
				return null;
			}
		});		
	}
	
	public void deleteRegistrationLogin(final RegistrationLogin rl){
		HibernateTemplate template = getHibernateTemplate();
		template.execute(new HibernateCallback() {
			public Object doInHibernate(Session session) {
				Query query = session.createQuery("delete from RegistrationLogin rl where rl.loginId=:login and rl.businessKey=:businessKey");
				query.setParameter("login", rl.getLoginId());
				query.setParameter("businessKey", rl.getBusinessKey());
				query.executeUpdate();
				session.flush();
				return null;
			}
		});		
	}	
}