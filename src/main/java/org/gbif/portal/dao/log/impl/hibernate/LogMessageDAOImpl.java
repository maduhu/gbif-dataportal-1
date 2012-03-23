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
package org.gbif.portal.dao.log.impl.hibernate;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.gbif.portal.dao.DAOUtils;
import org.gbif.portal.dao.log.LogMessageDAO;
import org.gbif.portal.io.ResultsOutputter;
import org.gbif.portal.model.log.LogMessage;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * Log Message DAO implementation
 * 
 * @author dmartin
 */
public class LogMessageDAOImpl extends HibernateDaoSupport implements LogMessageDAO {

	/** Batch size for outputting log messages */
	protected int batchSize = 100;

	/**
	 * @see org.gbif.portal.dao.log.LogMessageDAO#findMessages(java.lang.Long,
	 *      java.lang.Long, java.lang.String, java.lang.Long, java.lang.Long,
	 *      java.util.Date, java.lang.Integer, int)
	 */
	@SuppressWarnings("unchecked")
	public List<LogMessage> findMessages(final Long dataProviderId,
			final Long dataResourceId, final Long userId,
			final Long occurrenceRecordId, final Long taxonConceptId,
			final Integer eventId, final Integer minEventId,
			final Integer maxEventId, final Long minLogLevel,
			final Long logGroupId, final Date startDate, final Date endDate,
			final int startIndex, final int maxResults) {
		return (List<LogMessage>) getHibernateTemplate().execute(
				new HibernateCallback() {
					public Object doInHibernate(Session session) {
						StringBuffer sb = new StringBuffer(
								"from LogMessage lm  left join fetch lm.dataProvider left join fetch lm.taxonConceptLite tc  left join fetch tc.taxonNameLite");
						Query query = setupLogMessageQuery(dataProviderId,
								dataResourceId, userId, occurrenceRecordId,
								taxonConceptId, eventId, minEventId,
								maxEventId, minLogLevel, logGroupId, startDate,
								endDate, startIndex, maxResults, session, sb);
						return query.list();
					}
				});
	}

	/**
	 * @see org.gbif.portal.dao.log.LogMessageDAO#findMessages(java.lang.Long,
	 *      java.lang.Long, java.lang.String, java.lang.Long, java.lang.Long,
	 *      java.util.Date, java.lang.Integer, int)
	 */
	@SuppressWarnings("unchecked")
	public void outputMessages(final ResultsOutputter resultsOutputter,
			final Long dataProviderId, final Long dataResourceId,
			final Long userId, final Long occurrenceRecordId,
			final Long taxonConceptId, final Integer eventId,
			final Integer minEventId, final Integer maxEventId,
			final Long minLogLevel, final Long logGroupId,
			final Date startDate, final Date endDate, final int startIndex,
			final int maxResults) throws IOException {
		StringBuffer sb = new StringBuffer("from LogMessage lm ");
		Query query = setupLogMessageQuery(dataProviderId, dataResourceId,
				userId, occurrenceRecordId, taxonConceptId, eventId,
				minEventId, maxEventId, minLogLevel, logGroupId, startDate,
				endDate, startIndex, maxResults, getSession(), sb);
		DAOUtils.scrollResults(resultsOutputter, getSession(), query, null,
				batchSize);
	}

	/**
	 * @see org.gbif.portal.dao.log.LogMessageDAO#findMessages(java.lang.Long,
	 *      java.lang.Long, java.lang.String, java.lang.Long, java.lang.Long,
	 *      java.util.Date, java.lang.Integer, int)
	 */
	@SuppressWarnings("unchecked")
	public int countMessages(final Long dataProviderId,
			final Long dataResourceId, final Long userId,
			final Long occurrenceRecordId, final Long taxonConceptId,
			final Integer eventId, final Integer minEventId,
			final Integer maxEventId, final Long minLogLevel,
			final Long logGroupId, final Date startDate, final Date endDate,
			final int startIndex, final int maxResults) {
		return (Integer) getHibernateTemplate().execute(
				new HibernateCallback() {
					public Object doInHibernate(Session session) {
						StringBuffer sb = new StringBuffer(
								"select count(lm.id) from LogMessage lm  left join fetch lm.dataProvider left join fetch lm.taxonConceptLite tc  left join fetch tc.taxonNameLite");
						Query query = setupLogMessageQuery(dataProviderId,
								dataResourceId, userId, occurrenceRecordId,
								taxonConceptId, eventId, minEventId,
								maxEventId, minLogLevel, logGroupId, startDate,
								endDate, startIndex, maxResults, session, sb);
						return query.uniqueResult();
					}
				});
	}

	/**
	 * @see org.gbif.portal.dao.log.LogMessageDAO#countMessagesByDataResource(java.lang.Long,
	 *      java.lang.Long, java.lang.Long, java.lang.Long, java.lang.Long,
	 *      java.lang.Long, java.lang.Long, java.lang.Long, java.lang.Long,
	 *      java.lang.Long, java.util.Date, java.util.Date)
	 */
	@SuppressWarnings("unchecked")
	public List<Object[]> countMessagesByDataResource(
			final Long dataProviderId, final Long dataResourceId,
			final Long userId, final Long occurrenceRecordId,
			final Long taxonConceptId, final Integer eventId,
			final Integer minEventId, final Integer maxEventId,
			final Long minLogLevel, final Long logGroupId,
			final Date startDate, final Date endDate) {
		return (List<Object[]>) getHibernateTemplate().execute(
				new HibernateCallback() {
					public Object doInHibernate(Session session) {
						StringBuffer sb = new StringBuffer(
								"select lm.dataResourceId, dr.name, lm.eventId, count(lm.eventId), sum(lm.count) from LogMessage lm inner join lm.dataResource dr");
						constructQuery(dataProviderId, dataResourceId, userId,
								occurrenceRecordId, taxonConceptId, eventId,
								minEventId, maxEventId, minLogLevel,
								logGroupId, startDate, endDate, sb);
						//FIXME to be removed
						sb.append(" and lm.portalInstanceId=1 ");
						sb.append(" group by lm.dataResourceId, lm.eventId order by dr.name, lm.dataResourceId, lm.eventId");
						Query query = session.createQuery(sb.toString());
						setParams(query, dataProviderId, dataResourceId,
								userId, occurrenceRecordId, taxonConceptId,
								eventId, minEventId, maxEventId, minLogLevel,
								logGroupId, startDate, endDate);
						return query.list();
					}
				});
	}

	@SuppressWarnings("unchecked")
	public List<LogMessage> getLogMessagesForEventIds(
			final Long dataProviderId, final Long dataResourceId,
			final List<Integer> eventIds) {
		return (List<LogMessage>) getHibernateTemplate().execute(
				new HibernateCallback() {
					public Object doInHibernate(Session session) {
						StringBuffer sb = new StringBuffer(
								"from LogMessage lm where lm.eventId IN (:eventIds)");
						if (dataProviderId != null) {
							sb.append(" and lm.dataProviderId=:dataProviderId");
						}
						if (dataResourceId != null) {
							sb.append(" and lm.dataResourceId=:dataResourceId");
						}
						sb.append(" order by lm.logGroupId, lm.timestamp ");

						Query query = session.createQuery(sb.toString());
						query.setParameterList("eventIds", eventIds);
						if (dataProviderId != null) {
							query.setParameter("dataProviderId",dataProviderId);
						}
						if (dataResourceId != null) {
							query.setParameter("dataResourceId",dataResourceId);
						}
						return query.list();
					}
				});
	}

	/**
	 * @see org.gbif.portal.dao.log.LogMessageDAO#getEarliestLogMessageDate()
	 */
	public Date getEarliestLogMessageDate() {
		return (Date) getHibernateTemplate().execute(
			new HibernateCallback() {
				public Object doInHibernate(Session session) {
					Query query = session.createQuery("select min(lm.timestamp) from LogMessage lm");
					return query.uniqueResult();
				}
			});
	}
	
	/**
	 * @see org.gbif.portal.dao.log.LogMessageDAO#getLatestLogMessageDate()
	 */
	public Date getLatestLogMessageDate() {
		return (Date) getHibernateTemplate().execute(
				new HibernateCallback() {
					public Object doInHibernate(Session session) {
						Query query = session.createQuery("select max(lm.timestamp) from LogMessage lm");
						return query.uniqueResult();
					}
				});
	}	
	
	/**
	 * Set up the log message query.
	 * 
	 * @return a hibernate query object for execution.
	 */
	private Query setupLogMessageQuery(final Long dataProviderId,
			final Long dataResourceId, final Long userId,
			final Long occurrenceRecordId, final Long taxonConceptId,
			final Integer eventId, final Integer minEventId,
			final Integer maxEventId, final Long minLogLevel,
			final Long logGroupId, final Date startDate, final Date endDate,
			final int startIndex, final int maxResults, Session session,
			StringBuffer sb) {
		
		constructQuery(dataProviderId, dataResourceId, userId,
				occurrenceRecordId, taxonConceptId, eventId, minEventId,
				maxEventId, minLogLevel, logGroupId, startDate, endDate, sb);
		
		Query query = session.createQuery(sb.toString());
		
		setParams(query, dataProviderId, dataResourceId, userId,
				occurrenceRecordId, taxonConceptId, eventId, minEventId,
				maxEventId, minLogLevel, logGroupId, startDate, endDate);
		
		query.setMaxResults(maxResults);
		query.setFirstResult(startIndex);
		return query;
	}

	/**
	 * Sets the params for the HQL query.
	 */
	private void setParams(Query query, final Long dataProviderId,
			final Long dataResourceId, final Long userId,
			final Long occurrenceRecordId, final Long taxonConceptId,
			final Integer eventId, final Integer minEventId,
			final Integer maxEventId, final Long minLogLevel,
			final Long logGroupId, final Date startDate, final Date endDate) {
		if (dataProviderId != null)
			query.setParameter("dataProviderId", dataProviderId);
		if (dataResourceId != null)
			query.setParameter("dataResourceId", dataResourceId);
		if (userId != null)
			query.setParameter("userId", userId);
		if (occurrenceRecordId != null)
			query.setParameter("occurrenceRecordId", occurrenceRecordId);
		if (taxonConceptId != null)
			query.setParameter("taxonConceptId", taxonConceptId);
		if (eventId != null)
			query.setParameter("eventId", eventId);
		if (minEventId != null)
			query.setParameter("minEventId", minEventId);
		if (maxEventId != null)
			query.setParameter("maxEventId", maxEventId);
		if (minLogLevel != null)
			query.setParameter("minLogLevel", minLogLevel);
		if (logGroupId != null)
			query.setParameter("logGroupId", logGroupId);
		if (startDate != null) {
			query.setParameter("startDate", startDate);
		}
		if (endDate != null) {
			query.setParameter("endDate", endDate);
		}
	}

	/**
	 * Construct the HQL query.
	 */
	private void constructQuery(final Long dataProviderId,
			final Long dataResourceId, final Long userId,
			final Long occurrenceRecordId, final Long taxonConceptId,
			final Integer eventId, final Integer minEventId,
			final Integer maxEventId, final Long minLogLevel,
			final Long logGroupId, final Date startDate, final Date endDate,
			StringBuffer sb) {

		StringBuffer qsb = new StringBuffer();
		if (eventId != null)
			addCriteria(qsb, " lm.eventId = :eventId");
		if (minEventId != null)
			addCriteria(qsb, " lm.eventId >= :minEventId");
		if (maxEventId != null)
			addCriteria(qsb, " lm.eventId <= :maxEventId");
		if (dataProviderId != null)
			addCriteria(qsb, " lm.dataProviderId = :dataProviderId");
		if (dataResourceId != null)
			addCriteria(qsb, " lm.dataResourceId = :dataResourceId");
		if (startDate != null)
			addCriteria(qsb, " lm.timestamp >= :startDate");
		if (endDate != null)
			addCriteria(qsb, " lm.timestamp <= :endDate");
		if (userId != null)
			addCriteria(qsb, " lm.userId = :userId");
		if (occurrenceRecordId != null)
			addCriteria(qsb, " lm.occurrenceRecordId = :occurrenceRecordId");
		if (taxonConceptId != null)
			addCriteria(qsb, " lm.taxonConceptId = :taxonConceptId");
		if (minLogLevel != null)
			addCriteria(qsb, " lm.level >= :minLogLevel");
		if (logGroupId != null)
			addCriteria(qsb, " lm.logGroupId = :logGroupId");
		sb.append(qsb.toString());
	}

	private void addCriteria(StringBuffer sb, String criteria) {
		if (sb.length() > 0)
			sb.append(" and ");
		else
			sb.append(" where ");
		sb.append(criteria);
	}
}