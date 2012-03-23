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
package org.gbif.portal.dao.impl.hibernate;

import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.gbif.portal.dao.AssociationTraverser;
import org.gbif.portal.dao.DAOUtils;
import org.gbif.portal.dao.SimpleQueryDAO;
import org.gbif.portal.io.ResultsOutputter;
import org.hibernate.CacheMode;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
/**
 * Implementation of SimpleQueryDAO that allows for untyped queries.
 * 
 * @author dmartin
 */
public class SimpleQueryDAOImpl extends HibernateDaoSupport implements SimpleQueryDAO{
	
	protected static Log logger = LogFactory.getLog(SimpleQueryDAOImpl.class);
	
	/** Object used to process the associations and produce a object array */ 
	protected AssociationTraverser associationTraverser;
	
	/** Batch size used for pre processing of results */
	protected int batchSize = 1000;

	/**
	 * @see org.gbif.portal.dao.SimpleQueryDAO#getByQuery(java.lang.String, java.util.List, java.lang.Integer)
	 */
	@SuppressWarnings("unchecked")
	public List getByQuery(final String queryString, final List<Object> parameters, final Integer startIndex, final Integer maxResults) {
		if(logger.isDebugEnabled()) {
			logger.debug("GetByQuery running with startIndex[" + startIndex+ "], maxResults[" + maxResults+ "]");
		}
		HibernateTemplate template = getHibernateTemplate();
		return (List) template.execute(new HibernateCallback() {
			public Object doInHibernate(Session session) {
				if(logger.isDebugEnabled())
					logger.debug("getByQuery queryString "+queryString);
				Query query = createQuery(queryString, parameters, startIndex, maxResults, session);
				return query.list();
			}
		});
	}
	
	/**
	 * @see org.gbif.portal.dao.SimpleQueryDAO#outputResultsForQuery(java.lang.String, java.util.List, java.lang.Integer, java.lang.Integer)
	 */
	public void outputResultsForQuery(final String queryString, final List<Object> parameters, final Integer startIndex, final Integer maxResults, final ResultsOutputter resultsOutputter) throws IOException {
		
		Session session = getSession();
		session.setCacheMode(CacheMode.IGNORE);
		
		if(logger.isDebugEnabled())
			logger.debug("getByQuery queryString "+queryString);
		Query query = createQuery(queryString, parameters, startIndex, maxResults, session);
		DAOUtils.scrollResults(resultsOutputter, session, query, associationTraverser, batchSize);
	}

	/**
	 * Create the query setting the positional parameters and limits.
	 * 
	 * @param queryString
	 * @param parameters
	 * @param startIndex
	 * @param maxResults
	 * @param session
	 * @return query ready for execution
	 */
	private Query createQuery(final String queryString, final List<Object> parameters, final Integer startIndex, final Integer maxResults, Session session) {
		Query query = session.createQuery(queryString);
		if (parameters != null) {
			int positionalParamIdx = 0;
			for (int i=0; i<parameters.size(); i++) {
				// note that the parameter type is inferred here...
				if(logger.isDebugEnabled())
					logger.debug("getByQuery setting parameter "+i+" : "+parameters.get(i));
				if(parameters.get(i)==null){
					//nulls are handled by not adding
					//any position parameters - setting a position parameter
					//to null or 'null' results in hibernate compiling it down to
					//latitude !=null which behaves differently to is not null
				} else if(parameters.get(i) instanceof Collection){
					Collection collection = (Collection) parameters.get(i);
					for (Iterator iter = collection.iterator(); iter.hasNext();) {
						Object parameter = (Object) iter.next();
						query.setParameter(positionalParamIdx, parameter);
						positionalParamIdx++;
					}
				} else if(parameters.get(i) instanceof Date){
					query.setTimestamp(positionalParamIdx, (Date) parameters.get(i));
					positionalParamIdx++;
				} else {
					query.setParameter(positionalParamIdx, parameters.get(i));
					positionalParamIdx++;
				}
			}
		}
		if(startIndex !=null)
			query.setFirstResult(startIndex);
		if (maxResults != null) 
			query.setMaxResults(maxResults);
		return query;
	}

	/**
	 * @param associationTraverser the associationTraverser to set
	 */
	public void setAssociationTraverser(AssociationTraverser associationTraverser) {
		this.associationTraverser = associationTraverser;
	}
}