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
package org.gbif.portal.dao.taxonomy.impl.hibernate;

import java.util.Date;
import java.util.List;

import org.gbif.portal.dao.taxonomy.TaxonConceptDAO;
import org.gbif.portal.model.taxonomy.CommonName;
import org.gbif.portal.model.taxonomy.TaxonConcept;
import org.gbif.portal.model.taxonomy.TaxonConceptLite;
import org.gbif.portal.model.taxonomy.TaxonRank;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * A hibernate based DAO implementation for TaxonConceptDAO.
 * 
 * @author dmartin
 */
public class TaxonConceptDAOImpl extends HibernateDaoSupport implements TaxonConceptDAO {

	/** The maximum number of child concepts to return */
	protected int maxChildConcepts = 10000; 
	/** Any concepts with a taxonomic priority below this threshold are considered "unconfirmed" */
	protected int taxonomicPriorityThreshold = 20;

	/**
	 * @see org.gbif.portal.dao.taxonomy.TaxonConceptDAO#getTaxonConcept(long)
	 */
	public TaxonConcept getTaxonConceptFor(final long taxonConceptId) {
		HibernateTemplate template = getHibernateTemplate();
		return (TaxonConcept) template.execute(new HibernateCallback() {
			public Object doInHibernate(Session session) {
				Query query = session.createQuery(
						"from TaxonConcept tc" +
						" inner join fetch tc.taxonName" +
						" left join fetch tc.dataResource" +
						" left join fetch tc.dataProvider" +
						" left join fetch tc.parentConcept" +
						" where tc.id = ?");
				query.setParameter(0, taxonConceptId);
				query.setCacheable(true);
				return query.uniqueResult();
			}
		});		
	}

	/**
	 * @see org.gbif.portal.dao.taxonomy.TaxonConceptDAO#getTaxonConceptLiteFor(java.lang.Long)
	 */
	public TaxonConceptLite getTaxonConceptLiteFor(final long taxonConceptId) {
		HibernateTemplate template = getHibernateTemplate();
		return (TaxonConceptLite) template.execute(new HibernateCallback() {
			public Object doInHibernate(Session session) {
				Query query = session.createQuery(
						"from TaxonConceptLite tc" +
						" inner join fetch tc.taxonNameLite" +
						" where tc.id = ?");
				query.setParameter(0, taxonConceptId);
				query.setCacheable(true);
				return query.uniqueResult();
			}
		});	
	}

	/**
	 * @see org.gbif.portal.dao.taxonomy.TaxonConceptDAO#getTaxonConceptForRemoteId(long)
	 */
	@SuppressWarnings("unchecked")
	public List<TaxonConcept> getTaxonConceptForRemoteId(final String remoteId) {
		HibernateTemplate template = getHibernateTemplate();
		return (List<TaxonConcept>) template.execute(new HibernateCallback() {
			public Object doInHibernate(Session session) {
				Query query = session.createQuery(
						"from TaxonConcept tc" +
						" inner join fetch tc.taxonName" +
						" inner join fetch tc.remoteConcepts as remoteConcept" +
						" left join fetch tc.dataResource" +
						" left join fetch tc.dataProvider" +
						" left join fetch tc.parentConcept" +
						" left join fetch tc.kingdomConcept left join fetch tc.kingdomConcept.taxonNameLite" +
						" left join fetch tc.phylumConcept left join fetch tc.phylumConcept.taxonNameLite" +
						" left join fetch tc.orderConcept left join fetch tc.orderConcept.taxonNameLite" +
						" left join fetch tc.classConcept left join fetch tc.classConcept.taxonNameLite" +
						" left join fetch tc.familyConcept left join fetch tc.familyConcept.taxonNameLite" +	
						" left join fetch tc.genusConcept left join fetch tc.genusConcept.taxonNameLite" +	
						" left join fetch tc.speciesConcept left join fetch tc.speciesConcept.taxonNameLite" +						
						" where remoteConcept.remoteId = ?");
				query.setParameter(0, remoteId);
				query.setCacheable(true);
				return query.list();
			}
		});
	}

	/**
	 * @see org.gbif.portal.dao.taxonomy.TaxonConceptDAO#getDetailedTaxonConceptFor(long)
	 */
	public TaxonConcept getDetailedTaxonConceptFor(final long taxonConceptId) {
		HibernateTemplate template = getHibernateTemplate();
		return (TaxonConcept) template.execute(new HibernateCallback() {
			public Object doInHibernate(Session session) {
				Query query = session.createQuery("from TaxonConcept tc" +
						" inner join fetch tc.taxonName" +
						" left join fetch tc.parentConcept left join fetch tc.parentConcept.taxonName" +
						" left join fetch tc.kingdomConcept left join fetch tc.kingdomConcept.taxonNameLite" +
						" left join fetch tc.phylumConcept left join fetch tc.phylumConcept.taxonNameLite" +
						" left join fetch tc.orderConcept left join fetch tc.orderConcept.taxonNameLite" +
						" left join fetch tc.classConcept left join fetch tc.classConcept.taxonNameLite" +
						" left join fetch tc.familyConcept left join fetch tc.familyConcept.taxonNameLite" +
						" left join fetch tc.genusConcept left join fetch tc.genusConcept.taxonNameLite" +
						" left join fetch tc.speciesConcept left join fetch tc.speciesConcept.taxonNameLite" +
						" inner join fetch tc.dataResource" +
						" inner join fetch tc.dataProvider" +
						" where tc.id=?"
						);
				query.setParameter(0, taxonConceptId);
				query.setCacheable(true);
				return query.uniqueResult();
			}
		});
	}

	/**
	 * @see org.gbif.portal.dao.taxonomy.TaxonConceptDAO#getTaxonConcept(long)
	 */
	public TaxonConcept getParentConceptFor(final long taxonConceptId) {
		HibernateTemplate template = getHibernateTemplate();
		return (TaxonConcept) template.execute(new HibernateCallback() {
			public Object doInHibernate(Session session) {
				Query query = session.createQuery(
						"select tc.parentConcept from TaxonConcept tc" +
						" inner join fetch tc.parentConcept.taxonName" +
						" left join fetch tc.parentConcept.dataResource" +
						" left join fetch tc.parentConcept.dataProvider" +
						" left join fetch tc.parentConcept.parentConcept" +
						" left join fetch tc.parentConcept.kingdomConcept left join fetch tc.parentConcept.kingdomConcept.taxonNameLite" +
						" left join fetch tc.parentConcept.phylumConcept left join fetch tc.parentConcept.phylumConcept.taxonNameLite" +
						" left join fetch tc.parentConcept.orderConcept left join fetch tc.parentConcept.orderConcept.taxonNameLite" +
						" left join fetch tc.parentConcept.classConcept left join fetch tc.parentConcept.classConcept.taxonNameLite" +
						" left join fetch tc.parentConcept.familyConcept left join fetch tc.parentConcept.familyConcept.taxonNameLite" +	
						" left join fetch tc.parentConcept.genusConcept left join fetch tc.parentConcept.genusConcept.taxonNameLite" +	
						" left join fetch tc.parentConcept.speciesConcept left join fetch tc.parentConcept.speciesConcept.taxonNameLite" +						
						" where tc.id = ?");
				query.setParameter(0, taxonConceptId);
				query.setCacheable(true);
				return query.uniqueResult();
			}
		});		
	}	

	/**
	 * @see org.gbif.portal.dao.taxonomy.TaxonConceptDAO#getTaxonConcept(long)
	 */
	@SuppressWarnings("unchecked")
	public List<TaxonConcept> getChildConceptsFor(final long taxonConceptId, final boolean allowUnconfirmed) {
		HibernateTemplate template = getHibernateTemplate();
		return (List<TaxonConcept>) template.execute(new HibernateCallback() {
			public Object doInHibernate(Session session) {
				StringBuffer sb = new StringBuffer(
						"from TaxonConcept tc" +
						" inner join fetch tc.taxonName" +
						" left join fetch tc.kingdomConcept left join fetch tc.kingdomConcept.taxonNameLite" +
						" left join fetch tc.phylumConcept left join fetch tc.phylumConcept.taxonNameLite" +
						" left join fetch tc.orderConcept left join fetch tc.orderConcept.taxonNameLite" +
						" left join fetch tc.classConcept left join fetch tc.classConcept.taxonNameLite" +
						" left join fetch tc.familyConcept left join fetch tc.familyConcept.taxonNameLite" +	
						" left join fetch tc.genusConcept left join fetch tc.genusConcept.taxonNameLite" +	
						" left join fetch tc.speciesConcept left join fetch tc.speciesConcept.taxonNameLite" +						
						" where tc.parentConcept.id = ? and tc.isAccepted=true ");
				if(!allowUnconfirmed){
					sb.append(" and tc.taxonomicPriority<=");
					sb.append(taxonomicPriorityThreshold);
				}
				sb.append(" order by tc.rank, tc.taxonName.canonical");
				Query query = session.createQuery(sb.toString());
				query.setParameter(0, taxonConceptId);
				query.setCacheable(true);
				query.setMaxResults(maxChildConcepts);
				return query.list();
			}
		});		
	}

	/**
	 * @see org.gbif.portal.dao.taxonomy.TaxonConceptDAO#getLiteChildConceptsFor(long)
	 */
	@SuppressWarnings("unchecked")
	public List<TaxonConceptLite> getLiteChildConceptsFor(final long taxonConceptId, final boolean allowUnconfirmed) {
		HibernateTemplate template = getHibernateTemplate();
		return (List<TaxonConceptLite>) template.execute(new HibernateCallback() {
			public Object doInHibernate(Session session) {
				StringBuffer sb = new StringBuffer(
					"from TaxonConceptLite tcl inner join fetch tcl.taxonNameLite " +
					"where tcl.parentConceptId = :taxonConceptId and tcl.isAccepted=true ");
					
				if(!allowUnconfirmed){
					sb.append(" and tcl.taxonomicPriority<=");
					sb.append(taxonomicPriorityThreshold);
				}	
				
				sb.append(" order by tcl.taxonRank, tcl.taxonNameLite.canonical");
				Query query = session.createQuery(sb.toString());
				query.setParameter("taxonConceptId", taxonConceptId);
				query.setMaxResults(maxChildConcepts);
				return query.list();
			}
		});	
	}	
	

	/**
	 * @see org.gbif.portal.dao.taxonomy.TaxonConceptDAO#getLiteChildConceptsFor(long)
	 */
	@SuppressWarnings("unchecked")
	public List<TaxonConceptLite> getLiteChildConceptsFor(final long taxonConceptId, final String isoCountryCode, final boolean allowUnconfirmed) {
		if(isoCountryCode==null)
			return getLiteChildConceptsFor(taxonConceptId, allowUnconfirmed);
		
		HibernateTemplate template = getHibernateTemplate();
		return (List<TaxonConceptLite>) template.execute(new HibernateCallback() {
			public Object doInHibernate(Session session) {
				StringBuffer sb = new StringBuffer("select tct.taxonConceptLite from TaxonCountry tct " +
						"inner join fetch tct.taxonConceptLite.taxonNameLite");
				sb.append(" where tct.taxonConceptLite.parentConceptId = :taxonConceptId and tct.taxonConceptLite.isAccepted=true " +
						"and  tct.key.isoCountryCode =:isoCountryCode ");
				
				if(!allowUnconfirmed){
					sb.append(" and tct.taxonConceptLite.taxonomicPriority<=");
					sb.append(taxonomicPriorityThreshold);
				}
				sb.append("order by tct.taxonConceptLite.taxonRank, tct.taxonConceptLite.taxonNameLite.canonical");
				Query query = session.createQuery(sb.toString());
				query.setParameter("taxonConceptId", taxonConceptId);
				query.setParameter("isoCountryCode", isoCountryCode);				
				query.setMaxResults(maxChildConcepts);
				return query.list();
			}
		});
	}

	/**
	 * @see org.gbif.portal.dao.taxonomy.TaxonConceptDAO#getNubConceptFor(long)
	 */
	public TaxonConcept getNubConceptFor(final long taxonConceptId) {
		HibernateTemplate template = getHibernateTemplate();
		return (TaxonConcept) template.execute(new HibernateCallback() {
			public Object doInHibernate(Session session) {
				Query query = session.createQuery(
						"select tc.partnerConcept from TaxonConcept tc" +
						" inner join fetch tc.partnerConcept.taxonName" +
						" left join fetch tc.partnerConcept.kingdomConcept left join fetch tc.partnerConcept.kingdomConcept.taxonNameLite" +
						" left join fetch tc.partnerConcept.phylumConcept left join fetch tc.partnerConcept.phylumConcept.taxonNameLite" +
						" left join fetch tc.partnerConcept.orderConcept left join fetch tc.partnerConcept.orderConcept.taxonNameLite" +
						" left join fetch tc.partnerConcept.classConcept left join fetch tc.partnerConcept.classConcept.taxonNameLite" +
						" left join fetch tc.partnerConcept.familyConcept left join fetch tc.partnerConcept.familyConcept.taxonNameLite" +	
						" left join fetch tc.partnerConcept.genusConcept left join fetch tc.partnerConcept.genusConcept.taxonNameLite" +	
						" left join fetch tc.partnerConcept.speciesConcept left join fetch tc.partnerConcept.speciesConcept.taxonNameLite" +						
						" where tc.id = ?");
				query.setParameter(0, taxonConceptId);
				query.setCacheable(true);
				return query.uniqueResult();
			}
		});	
	}

	/**
	 * @see org.gbif.portal.dao.taxonomy.TaxonConceptDAO#getTaxonConceptsForNubTaxonConcept(long)
	 */
	@SuppressWarnings("unchecked")
	public List<TaxonConcept> getTaxonConceptsForNubTaxonConcept(final long nubConceptId) {
		if(logger.isDebugEnabled())
			logger.debug("Retrieving taxon concepts for nub concept:"+nubConceptId);
		HibernateTemplate template = getHibernateTemplate();
		return (List<TaxonConcept>) template.execute(new HibernateCallback() {
			public Object doInHibernate(Session session) {
				Query query = session.createQuery(
						"from TaxonConcept tc" +
						" inner join fetch tc.taxonName" +
						" where tc.partnerConceptId = :partnerConceptId" +
						" and tc.isNubConcept = false" +
						" order by tc.taxonRank asc, tc.taxonName.canonical");
				query.setParameter("partnerConceptId", nubConceptId);
				query.setCacheable(true);
				return query.list();
			}
		});	
	}

	/**
	 * @see org.gbif.portal.dao.taxonomy.TaxonConceptDAO#getTaxonConceptIdsForNubTaxonConcept(long)
	 */
	@SuppressWarnings("unchecked")
	public List<Long> getTaxonConceptIdsForNubTaxonConcept(final long nubConceptId) {
		if(logger.isDebugEnabled())
			logger.debug("Retrieving taxon concept ids for nub concept:"+nubConceptId);
		HibernateTemplate template = getHibernateTemplate();
		return (List<Long>) template.execute(new HibernateCallback() {
			public Object doInHibernate(Session session) {
				Query query = session.createQuery(
						"select tc.id from TaxonConcept tc" +
						" where tc.partnerConceptId = :partnerConceptId" +
						" and tc.isNubConcept = false");
				query.setParameter("partnerConceptId", nubConceptId);
				query.setCacheable(true);
				return query.list();
			}
		});	
	}
	
	/**
	 * @see org.gbif.portal.dao.taxonomy.TaxonConceptDAO#findTaxonConceptsWithSameCanonicalAndRankAs(long, int, int)
	 */
	@SuppressWarnings("unchecked")
	public List<TaxonConcept> findTaxonConceptsWithSameCanonicalAndRankAs(final long taxonConceptId, final Long dataProviderId, 
			final Long dataResourceId, final int startIndex, final int maxResults) {
		HibernateTemplate template = getHibernateTemplate();
		return (List<TaxonConcept>) template.execute(new HibernateCallback() {
			public Object doInHibernate(Session session) {
				StringBuffer sb = new StringBuffer(
						"select target from TaxonConcept target, TaxonConcept source" +
						" inner join fetch target.dataResource" +
						" inner join fetch target.dataProvider" +
						" inner join fetch target.taxonName" +
						" where source.taxonName.canonical = target.taxonName.canonical"+
						" and source.taxonName.taxonRank = target.taxonName.taxonRank" +
						" and target.id <> :id" +
						" and source.id = :id");
				if(dataProviderId!=null)
					sb.append(" and target.dataProviderId = :dataProviderId");
				if(dataResourceId!=null)
					sb.append(" and target.dataResourceId = :dataResourceId");
				Query query = session.createQuery(sb.toString());
				query.setParameter("id", taxonConceptId);
				if(dataProviderId!=null)
					query.setParameter("dataProviderId", dataProviderId);
				if(dataResourceId!=null)
					query.setParameter("dataResourceId", dataResourceId);				
				query.setCacheable(true);
				query.setMaxResults(maxResults);
				query.setFirstResult(startIndex);
				return query.list();
			}
		});	
	}

	/**
	 * @see org.gbif.portal.dao.taxonomy.TaxonConceptDAO#getDataProviderRootConceptsFor(long)
	 */
	@SuppressWarnings("unchecked")
	public List<TaxonConcept> getDataProviderRootConceptsFor(final long dataProviderId) {
		if(logger.isDebugEnabled())
			logger.debug("Retrieving root concepts for data provider id:"+dataProviderId);
		HibernateTemplate template = getHibernateTemplate();
		return (List<TaxonConcept>) template.execute(new HibernateCallback() {
			public Object doInHibernate(Session session) {
				Query query = session.createQuery(
						"from TaxonConcept tc" +
						" inner join fetch tc.taxonName" +
						" where tc.dataProvider.id = :dataProviderId" +
						" and tc.parentConcept.id is null" +
						" and tc.isAccepted=true" +
						" order by tc.taxonRank asc, tc.taxonName.canonical");
				query.setParameter("dataProviderId", dataProviderId);
				query.setCacheable(true);
				query.setMaxResults(maxChildConcepts);
				return query.list();
			}
		});	
	}	
	
	/**
	 * @see org.gbif.portal.dao.taxonomy.TaxonConceptDAO#getCountryRootConceptsFor(java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	public List<TaxonConceptLite> getCountryRootConceptsFor(final String isoCountryCode) {
		HibernateTemplate template = getHibernateTemplate();
		return (List<TaxonConceptLite>) template.execute(new HibernateCallback() {
			public Object doInHibernate(Session session) {
				//FIXME This query hardcodes the root rank to be
				//kingdom - not very elegant but the alternative query
				//select..where parent_concept_id is null was soo slooow
				Query query = session.createQuery(
						"select tct.taxonConceptLite from TaxonCountry tct" +
						" inner join fetch tct.taxonConceptLite.taxonNameLite" +
						" where tct.key.isoCountryCode = :isoCountryCode" +
						" and tct.taxonConceptLite.taxonRank=:taxonRank" +
						" and tct.taxonConceptLite.isAccepted=true" +
						" order by tct.taxonConceptLite.taxonRank asc, tct.taxonConceptLite.taxonNameLite.canonical");
				query.setParameter("isoCountryCode", isoCountryCode);
				query.setParameter("taxonRank", TaxonRank.KINGDOM);
				query.setCacheable(true);
				return query.list();
			}
		});	
	}

	/**
	 * @see org.gbif.portal.dao.taxonomy.TaxonConceptDAO#getDataResourceRootConceptsFor(long)
	 */
	@SuppressWarnings("unchecked")
	public List<TaxonConcept> getDataResourceRootConceptsFor(final long dataResourceId) {
		if(logger.isDebugEnabled())
			logger.debug("Retrieving root concepts for data resource id:"+dataResourceId);
		HibernateTemplate template = getHibernateTemplate();
		return (List<TaxonConcept>) template.execute(new HibernateCallback() {
			public Object doInHibernate(Session session) {
				Query query = session.createQuery(
						"from TaxonConcept tc" +
						" inner join fetch tc.taxonName" +
						" where tc.dataResource.id = :dataResourceId" +
						" and tc.parentConcept.id is null" +
						" and tc.isAccepted=true" +
						" order by tc.taxonRank asc, tc.taxonName.canonical"
				);
				query.setParameter("dataResourceId", dataResourceId);
				query.setCacheable(true);
				query.setMaxResults(maxChildConcepts);
				return query.list();
			}
		});	
	}

	/**
	 * @see org.gbif.portal.dao.taxonomy.TaxonConceptDAO#getParentChildConcepts(java.lang.String)
	 */
	public TaxonConcept getParentChildConcepts(final long taxonConceptId, final boolean retrieveChildren, 
			final boolean allowUnconfirmed) {
		HibernateTemplate template = getHibernateTemplate();
		return (TaxonConcept) template.execute(new HibernateCallback() {
			public Object doInHibernate(Session session) {
				StringBuffer sb = new StringBuffer("from TaxonConcept tc");
				sb.append(" inner join fetch tc.taxonName");
				sb.append(" left outer join fetch tc.parentConcept");
				sb.append(" left outer join fetch tc.parentConcept.taxonName");
				if(retrieveChildren){
					sb.append(" left outer join fetch tc.childConcepts as child");
					sb.append(" left outer join fetch child.taxonNameLite");
				}
				sb.append(" where tc.id=?");
				if(retrieveChildren && !allowUnconfirmed){
					sb.append(" and child.taxonomicPriority<=");
					sb.append(taxonomicPriorityThreshold);
				}
				if(retrieveChildren)		
					sb.append(" order by child.taxonNameLite.canonical");
				Query query = session.createQuery(sb.toString());
				query.setParameter(0, taxonConceptId);
				query.setCacheable(true);
				query.setMaxResults(maxChildConcepts);
				return query.uniqueResult();
			}
		});
	}

	/**
	 * @see org.gbif.portal.dao.taxonomy.TaxonConceptDAO#findTaxonConcepts(java.lang.String, java.lang.String, boolean, org.gbif.portal.model.taxonomy.TaxonRank, java.lang.Long, java.lang.Long, java.lang.Long, java.lang.String, java.util.Date, int, int)
	 */
	@SuppressWarnings("unchecked")
	public List<TaxonConcept> findTaxonConcepts(final String canonical, final String specificEpithet, final boolean fuzzy, 
			final TaxonRank taxonRank, final Long dataProviderId, final Long dataResourceId, final Long resourceNetworkId, 
			final String hostIsoCountryCode, final Date modifiedSince, final boolean allowUnconfirmed, final boolean sortAlphabetically, 
			final int startIndex, final int maxResults) {
		HibernateTemplate template = getHibernateTemplate();
		return (List<TaxonConcept>) template.execute(new HibernateCallback() {
			public Object doInHibernate(Session session) {
				if(logger.isDebugEnabled())
					logger.debug("searching with: '"+canonical+"' and specificEpithet: '"+specificEpithet+"', max results:"+maxResults+", start index: "+startIndex);
				
				StringBuffer sb = new StringBuffer("select tc from TaxonConcept tc");
				sb.append(" inner join fetch tc.taxonName");
				sb.append(" left join fetch tc.parentConcept pc left join fetch pc.taxonName");
				sb.append(" left join fetch tc.kingdomConcept kc left join fetch kc.taxonNameLite");
				sb.append(" left join fetch tc.phylumConcept phc left join fetch phc.taxonNameLite");
				sb.append(" left join fetch tc.orderConcept oc left join fetch oc.taxonNameLite");
			    sb.append(" left join fetch tc.classConcept cc left join fetch cc.taxonNameLite");
				sb.append(" left join fetch tc.familyConcept fc left join fetch fc.taxonNameLite");	
				sb.append(" left join fetch tc.genusConcept gc left join fetch gc.taxonNameLite");	
				sb.append(" left join fetch tc.speciesConcept sc left join fetch sc.taxonNameLite");
				if(resourceNetworkId!=null) {
					sb.append(" left join tc.dataResource dr left join dr.networkMemberships nm");
					sb.append(" left join nm.resourceNetwork rn");
				}

				//FIXME including this left join fetch causes hibernate to exclude the limit!!!
				//sb.append(" left join fetch tc.relationshipAssertions");
				//sb.append(" left join fetch tc.commonNames");
				String connector = " where";
				if(canonical!=null) {
					sb.append(connector);
					sb.append(" tc.taxonName.canonical like :canonical");
					connector = " and";
				}
				if(specificEpithet!=null) {
					sb.append(connector);
					sb.append(" tc.taxonName.specificEpithet like :specificEpithet");
					connector = " and";
				}				
				if(taxonRank!=null && taxonRank!= TaxonRank.UNKNOWN) {
					sb.append(connector);
					sb.append(" tc.taxonRank = :taxonRank");		
					connector = " and";
				}
				if(dataProviderId!=null) {
					sb.append(connector);
					sb.append(" tc.dataProvider.id = :dataProviderId");
					connector = " and";
				}
				if(dataResourceId!=null) {
					sb.append(connector);
//					if (dataResourceId == 1) {
//						sb.append(" tc.isNubConcept = true");
//					}
//					else {
						sb.append(" tc.dataResource.id = :dataResourceId");
//					}
					connector = " and";
				}
				if(resourceNetworkId!=null) {
					sb.append(connector);
					//sb.append(" tc.dataResource.networkMemberships.resourceNetwork.id = :resourceNetworkId");
					sb.append(" rn.id = :resourceNetworkId");
					connector = " and";
				}
				if(hostIsoCountryCode!=null) {
					sb.append(connector);
					sb.append(" tc.dataProvider.isoCountryCode = :hostIsoCountryCode");
					connector = " and";
				}
				if(modifiedSince!=null) {
					sb.append(connector);
					sb.append(" tc.modified >= :modifiedSince");
				}
				if(!allowUnconfirmed){
					sb.append(connector);
					sb.append(" tc.taxonomicPriority <= "+taxonomicPriorityThreshold);					
				}
				
				if(sortAlphabetically){
					sb.append(" order by tc.taxonName.canonical");
				}
				
				//set the query object
				Query query = session.createQuery(sb.toString());
				
				if(canonical!=null){
					String canonicalString = canonical;
					if(fuzzy)
						canonicalString = canonicalString+'%';
					query.setParameter("canonical", canonicalString);
				}
				if(specificEpithet!=null){
					String specificEpithetString = specificEpithet;
					if(fuzzy)
						specificEpithetString = specificEpithetString+'%';
					query.setParameter("specificEpithet", specificEpithetString);
				}
				if(taxonRank!=null && taxonRank!= TaxonRank.UNKNOWN)
					query.setParameter("taxonRank", taxonRank);
				if(dataProviderId!=null)
					query.setParameter("dataProviderId", dataProviderId);
				if(dataResourceId!=null && dataResourceId > 1)
					query.setParameter("dataResourceId", dataResourceId);
				if(resourceNetworkId!=null)
					query.setParameter("resourceNetworkId", resourceNetworkId);
				if(hostIsoCountryCode!=null)
					query.setParameter("hostIsoCountryCode", hostIsoCountryCode);
				if(modifiedSince!=null)
					query.setParameter("modifiedSince", modifiedSince);
				
				query.setCacheable(true);
				query.setMaxResults(maxResults);
				query.setFirstResult(startIndex);
				return query.list();
			}
		});	
	}

	/**
	 * @see org.gbif.portal.dao.taxonomy.TaxonConceptDAO#countTaxonConcepts(java.lang.String, boolean, org.gbif.portal.model.taxonomy.TaxonRank, java.lang.Long, java.lang.Long, java.lang.Long, java.lang.String, java.util.Date)
	 */
	@SuppressWarnings("unchecked")
	public Long countTaxonConcepts(final String nameStub, final boolean fuzzy, final TaxonRank taxonRank, final Long dataProviderId, 
			final Long dataResourceId, final Long resourceNetworkId, final String hostIsoCountryCode, final Date modifiedSince) {
		HibernateTemplate template = getHibernateTemplate();
		Object result = template.execute(new HibernateCallback() {
			public Object doInHibernate(Session session) {
				if(logger.isDebugEnabled())
					logger.debug("searching with: "+nameStub);
				String searchString = nameStub;
				if(searchString!=null && fuzzy)
					searchString = searchString+'%';
				StringBuffer sb = new StringBuffer("select count(tc.id) from TaxonConcept tc");
				if(resourceNetworkId!=null) {
					sb.append(" left join tc.dataResource dr left join dr.networkMemberships nm");
					sb.append(" left join nm.resourceNetwork rn");
				}
				String connector = " where";
				if (searchString!=null) {
					sb.append(connector);
					sb.append(" tc.taxonName.canonical like :searchString");
					connector = " and";
				}
				if(taxonRank!=null && taxonRank!= TaxonRank.UNKNOWN) {
					sb.append(connector);
					sb.append(" tc.taxonRank = :taxonRank");		
					connector = " and";
				}
				if(dataProviderId!=null) {
					sb.append(connector);
					sb.append(" tc.dataProvider.id = :dataProviderId");
					connector = " and";
				}
				if(dataResourceId!=null) {
					sb.append(connector);
					if (dataResourceId == 1) {
						sb.append(" tc.isNubConcept = true");
					}
					else {
						sb.append(" tc.dataResource.id = :dataResourceId");
					}
					connector = " and";
				}
				if(resourceNetworkId!=null) {
					sb.append(connector);
					sb.append(" rn.id = :resourceNetworkId");
					connector = " and";
				}
				if(hostIsoCountryCode!=null) {
					sb.append(connector);
					sb.append(" tc.dataProvider.isoCountryCode = :hostIsoCountryCode");
					connector = " and";
				}
				if(modifiedSince!=null) {
					sb.append(connector);
					sb.append(" tc.modified >= :modifiedSince");
				}
				
				//set the query object
				Query query = session.createQuery(sb.toString());
				if (searchString != null)
					query.setParameter("searchString", searchString);
				if(taxonRank!=null && taxonRank!= TaxonRank.UNKNOWN)
					query.setParameter("taxonRank", taxonRank);
				if(dataProviderId!=null)
					query.setParameter("dataProviderId", dataProviderId);
				if(dataResourceId!=null && dataResourceId > 1)
					query.setParameter("dataResourceId", dataResourceId);
				if(resourceNetworkId!=null)
					query.setParameter("resourceNetworkId", resourceNetworkId);
				if(hostIsoCountryCode!=null)
					query.setParameter("hostIsoCountryCode", hostIsoCountryCode);
				if(modifiedSince!=null)
					query.setParameter("modifiedSince", modifiedSince);
				
				query.setCacheable(true);
				return query.uniqueResult();
			}
		});	
		if (result instanceof Integer)
			return ((Integer)result).longValue();
		return (Long) result;
	}

	/**
	 * @see org.gbif.portal.dao.taxonomy.TaxonConceptDAO#getTaxonConceptCount(java.lang.Long, java.lang.Long)
	 */
	public int getTaxonConceptCount(final Long dataProviderId, final Long dataResourceId) {
		Object result =  getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session) {
				StringBuffer sb = new StringBuffer("Select count(tc.id) from TaxonConcept tc");
				if(dataProviderId!=null || dataResourceId!=null)
					sb.append(" where");
				if(dataProviderId!=null)
					sb.append(" and tc.dataProvider.id = :dataProviderId");
				if(dataResourceId!=null) 
					sb.append(" and tc.dataResource.id = :dataResourceId");
				//set the query object
				Query query = session.createQuery(sb.toString());
				if(dataProviderId!=null)
					query.setParameter("dataProviderId", dataProviderId);
				if(dataResourceId!=null)
					query.setParameter("dataResourceId", dataResourceId);
				
				query.setCacheable(true);
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
	 * Uses a Hibernate Criteria object to assemble and execute query.
	 * 
	 * @see org.gbif.portal.dao.taxonomy.TaxonConceptDAO#getTaxonConceptCount(java.lang.String, boolean, java.lang.Long)
	 */
	public int getTaxonConceptCount(final String nameStub, final boolean fuzzy,  final Long dataProviderId, final Long dataResourceId) {
		Object result =  getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session) {
				String searchString = null;
				if(fuzzy)
					searchString = nameStub+'%';
				else
					searchString = nameStub;
				
				StringBuffer sb = new StringBuffer("Select count(tc.id) from TaxonConcept tc");
				sb.append(" inner join tc.taxonName");
				sb.append(" where tc.taxonName.canonical like :searchString");
				if(dataProviderId!=null)
					sb.append(" and tc.dataProvider.id = :dataProviderId");
				if(dataResourceId!=null)
					sb.append(" and tc.dataResource.id = :dataResourceId");
				//set the query object
				Query query = session.createQuery(sb.toString());
				query.setParameter("searchString", searchString);
				if(dataProviderId!=null)
					query.setParameter("dataProviderId", dataProviderId);
				if(dataResourceId!=null)
					query.setParameter("dataResourceId", dataResourceId);
				
				query.setCacheable(true);
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
	 * @see org.gbif.portal.dao.taxonomy.TaxonConceptDAO#getTaxonConceptCountForRank(org.gbif.portal.model.taxonomy.TaxonRank, java.lang.Boolean, java.lang.Long)
	 */
	public int getTaxonConceptCountForRank(final TaxonRank taxonRank, final Boolean higherThanSuppliedRank, final Long dataProviderId, 
			final Long dataResourceId) {
		Object result =  getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session) {
				StringBuffer sb = new StringBuffer("Select count(tc.id) from TaxonConcept tc");
				sb.append(" where tc.isAccepted=true and tc.taxonRank ");
				if (taxonRank != TaxonRank.UNKNOWN) {
					if (higherThanSuppliedRank == null) {
						sb.append("=");
					} else if (higherThanSuppliedRank.booleanValue()) {
						sb.append(">");
					} else {
						sb.append("<");
					}
					sb.append(" :taxonRank");
				}				
				if(dataProviderId!=null)
					sb.append(" and tc.dataProvider.id = :dataProviderId");
				if(dataResourceId!=null)
					sb.append(" and tc.dataResource.id = :dataResourceId");
				//set the query object
				Query query = session.createQuery(sb.toString());
				query.setParameter("taxonRank", taxonRank);
				if(dataProviderId!=null)
					query.setParameter("dataProviderId", dataProviderId);
				if(dataResourceId!=null)
					query.setParameter("dataResourceId", dataResourceId);
				query.setCacheable(true);
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
	 * Return an list of object arrays of length 2, a TaxonConcept and a Common Name
	 * 
	 * @see org.gbif.portal.dao.taxonomy.TaxonConceptDAO#findTaxonConceptsForCommonName(java.lang.String, boolean, int, int)
	 */
	@SuppressWarnings("unchecked")
	public List<CommonName> findTaxonConceptsForCommonName(final String commonNameStub, final boolean fuzzy,  
			final int startIndex, final int maxResults) {
		HibernateTemplate template = getHibernateTemplate();
		return (List<CommonName>) template.execute(new HibernateCallback() {
			public Object doInHibernate(Session session) {
				StringBuffer sb = new StringBuffer(
						"from CommonName cn" +
						" inner join fetch cn.taxonConcept tc" +
						" inner join fetch tc.taxonName" +
						" left join fetch tc.kingdomConcept left join fetch tc.kingdomConcept.taxonNameLite" +
						" left join fetch tc.phylumConcept left join fetch tc.phylumConcept.taxonNameLite" +
						" left join fetch tc.orderConcept left join fetch tc.orderConcept.taxonNameLite" +
						" left join fetch tc.classConcept left join fetch tc.classConcept.taxonNameLite" +
						" left join fetch tc.familyConcept left join fetch tc.familyConcept.taxonNameLite" +	
						" left join fetch tc.genusConcept left join fetch tc.genusConcept.taxonNameLite" +	
						" left join fetch tc.speciesConcept left join fetch tc.speciesConcept.taxonNameLite" +						
						" where cn.name like :commonNameStub and tc.isNubConcept=true");
				Query query = session.createQuery(sb.toString());
				String searchString = commonNameStub;
				if(fuzzy)
					searchString+='%';
				query.setParameter("commonNameStub", searchString);
				query.setCacheable(true);
				query.setMaxResults(maxResults);
				query.setFirstResult(startIndex);
				return query.list();
			}
		});		
	}

	/**
	 * @see org.gbif.portal.dao.taxonomy.TaxonConceptDAO#countChildConcepts(java.lang.Long, org.gbif.portal.model.taxonomy.TaxonRank, org.gbif.portal.model.taxonomy.TaxonRank)
	 */
	public int countChildConcepts(final long parentId, final TaxonRank parentRank, final TaxonRank childRank, final boolean countSynonyms, 
			final boolean onlyCountAccepted, final boolean allowUnconfirmed) {
		HibernateTemplate template = getHibernateTemplate();
		Long count = (Long) template.execute(new HibernateCallback() {
			public Object doInHibernate(Session session) {
				StringBuffer sb = new StringBuffer("select count(tc.id) from TaxonConcept tc where");
				if(countSynonyms){		
					sb.append(" tc.");
					sb.append(parentRank.getName());
					sb.append("ConceptId = :parentId ");
				} else {
					sb.append(" tc.parentConceptId = :parentId");
				}
				sb.append(" and tc.taxonRank = :childRank");
				if(onlyCountAccepted)
					sb.append(" and tc.isAccepted=true");
				if(!allowUnconfirmed){
					sb.append(" and tc.taxonomicPriority <= "+taxonomicPriorityThreshold);					
				}
				Query query = session.createQuery(sb.toString());
				query.setParameter("parentId", parentId);
				query.setParameter("childRank", childRank);
				query.setCacheable(true);
				return query.uniqueResult();
			}
		});	
		return count.intValue();
	}

	/**
	 * @param maxChildConcepts the maxChildConcepts to set
	 */
	public void setMaxChildConcepts(int maxChildConcepts) {
		this.maxChildConcepts = maxChildConcepts;
	}

	@SuppressWarnings("unchecked")
	public List<TaxonConcept> getAuthoritativeTaxonConceptsForNubTaxonConcept(final Long nubConceptId) {
		if(logger.isDebugEnabled())
			logger.debug("Retrieving authoritative taxon concepts for nub concept:"+nubConceptId);
		HibernateTemplate template = getHibernateTemplate();
		return (List<TaxonConcept>) template.execute(new HibernateCallback() {
			public Object doInHibernate(Session session) {
				// First get the main partner
				Query query = session.createQuery(
						"select partner from TaxonConcept partner, TaxonConcept nub" +
						" inner join fetch partner.taxonName" +
						" where partner.partnerConceptId = :nubConceptId" +
						" and nub.id = partner.partnerConceptId" +
						" and nub.partnerConceptId = partner.id" +
						" and partner.isNubConcept = false" +
						" and partner.taxonomicPriority <= 10");
				query.setParameter("nubConceptId", nubConceptId);
				query.setCacheable(true);
				List<TaxonConcept> list = query.list();
				
				if (logger.isDebugEnabled()) 
					logger.debug("Nub partner " + list.size());
				
				// Then get any other authoritative partners
				query = session.createQuery(
						"select partner from TaxonConcept partner, TaxonConcept nub" +
						" inner join fetch partner.taxonName" +
						" where partner.partnerConceptId = :nubConceptId" +
						" and nub.id = partner.partnerConceptId" +
						" and nub.partnerConceptId != partner.id" +
						" and partner.isNubConcept = false" +
						" and partner.taxonomicPriority <= 10");
				query.setParameter("nubConceptId", nubConceptId);
				query.setCacheable(true);
				list.addAll(query.list());
				
				if (logger.isDebugEnabled()) 
					logger.debug("All partners " + list.size());
				return list;
			}
		});	
	}
	
	/**
	 * @see org.gbif.portal.dao.taxonomy.TaxonConceptDAO#getTaxonConceptForRemoteId(java.lang.Long, java.lang.Long, java.lang.String)
	 */
	public TaxonConcept getTaxonConceptForRemoteId(final Long dataProviderId, final Long dataResourceId, final String remoteId) {
		HibernateTemplate template = getHibernateTemplate();
		return (TaxonConcept) template.execute(new HibernateCallback() {
			public Object doInHibernate(Session session) {
				StringBuffer sb = new StringBuffer(
						"select rc.taxonConcept from RemoteConcept rc" +
						" inner join rc.taxonConcept tc " +
						" left join fetch tc.dataResource" +
						" left join fetch tc.dataProvider" +
						" left join fetch tc.parentConcept" +
						" where rc.remoteId = :remoteId");
				if(dataProviderId!=null)
					sb.append(" and tc.dataProviderId = :dataProviderId");
				if(dataResourceId!=null)
					sb.append(" and tc.dataResourceId = :dataResourceId");
		
				Query query = session.createQuery(sb.toString());
				query.setParameter("remoteId", remoteId);
				if(dataProviderId!=null)
					query.setParameter("dataProviderId", dataProviderId);
				if(dataResourceId!=null)
					query.setParameter("dataResourceId", dataResourceId);
				query.setCacheable(true);
				return query.uniqueResult();
			}
		});		
	}
}