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

import org.gbif.portal.dao.geospatial.CentiCellDensityDAO;
import org.gbif.portal.model.ModelEntityType;
import org.gbif.portal.model.geospatial.CentiCellDensity;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
/**
 * Implementation of CentiCellDensityDAO.
 * 
 * @author dhobern
 */
public class CentiCellDensityDAOImpl extends HibernateDaoSupport implements CentiCellDensityDAO {
	
	/**
	 * @see org.gbif.portal.dao.geospatial.CellDensityDAO#getCellDensities(org.gbif.portal.model.ModelEntityType, long)
	 */
	@SuppressWarnings("unchecked")
	public List<CentiCellDensity> getCentiCellDensities(final ModelEntityType entityType, final List<Long> entityIds, final long cellId) {
		return (List<CentiCellDensity>) getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session) {
				Query query = session.createQuery("from CentiCellDensity where identifier.type=:type and identifier.entityId in (:entityIds) and identifier.cellId=:cellId order by identifier.centiCellId");
				query.setInteger("type", entityType.getValue());
				query.setParameterList("entityIds", entityIds);
				query.setLong("cellId", cellId);
				return query.list();
			}
		});
	}

}