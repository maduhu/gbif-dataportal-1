/**
 * 
 */
package org.gbif.portal.dao.log.impl.hibernate;

import org.gbif.portal.dao.log.UserDAO;
import org.gbif.portal.model.log.User;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * @author trobertson
 */
public class UserDAOImpl extends HibernateDaoSupport implements UserDAO {
	/**
	 * @see org.gbif.portal.dao.log.UserDAO#findUser(long, java.lang.String, java.lang.String)
	 */
	public User findUser(final long portalInstanceId, final String name, final String email) {
		return (User) getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session) {
				Query query = session.createQuery("from User where portalInstanceId=:portalInstanceId and " +
						"name=:name and email=:email");
				query.setLong("portalInstanceId", portalInstanceId);
				query.setString("name", name);
				query.setString("email", email);
				return query.uniqueResult();
			}
		});		
	}
	
	/**
	 * @see org.gbif.portal.dao.log.UserDAO#createUser(org.gbif.portal.model.log.User)
	 */
	public void createUser(User user) {
		Long id = (Long) getHibernateTemplate().save(user);
		user.setId(id);
	}
	
	/**
	 * @see org.gbif.portal.dao.log.UserDAO#updateUser(org.gbif.portal.model.log.User)
	 */
	public void updateUser(User user) {
		if (user.getId() != null && user.getId()>0) {
			getHibernateTemplate().update(user);
		} else {
			createUser(user);
		}		
	}

	/**
	 * @see org.gbif.portal.dao.log.UserDAO#findUser(long)
	 */	
	public User getUserFor(long id) {
		return (User) getHibernateTemplate().get(User.class, id);
	}
}
