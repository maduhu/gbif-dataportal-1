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

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.gbif.portal.dao.resources.DataResourceDAO;
import org.gbif.portal.dto.util.TaxonRankType;
import org.gbif.portal.model.occurrence.BasisOfRecord;
import org.gbif.portal.model.resources.DataProvider;
import org.gbif.portal.model.resources.DataResource;
import org.gbif.portal.model.resources.ResourceNetwork;
import org.hibernate.Criteria;
import org.hibernate.FetchMode;
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
 * A hibernate based DAO implementation for DataResourceDAO.
 * 
 * @author Dave Martin
 */
public class DataResourceDAOImpl extends HibernateDaoSupport implements DataResourceDAO {

	protected List<String> resourceNamePrefixes;
	
	/**
	 * @see org.gbif.portal.dao.resources.DataResourceDAO#getDataResourceFor(long)
	 */
	public DataResource getDataResourceFor(final long dataResourceId) {
		HibernateTemplate template = getHibernateTemplate();
		return (DataResource) template.execute(new HibernateCallback() {
			public Object doInHibernate(Session session) {
				Query query = session.createQuery("from DataResource dr left join fetch dr.dataProvider where dr.id = ?");
				query.setParameter(0, dataResourceId);
				query.setCacheable(true);
				return query.uniqueResult();
			}
		});		
	}
	
	/**
	 * @see org.gbif.portal.dao.resources.DataResourceDAO#getAllDataResources()
	 */
	public List<DataResource> getAllDataResources() {
		HibernateTemplate template = getHibernateTemplate();	
		List results =  (List) template.execute(new HibernateCallback() {
			public Object doInHibernate(Session session) {
				Query query = session.createQuery("from DataResource dr where dr.deleted is null order by name");			
				return query.list();
			}
		});			
		return results;
	}	
	/**
	 * @see org.gbif.portal.dao.resources.DataResourceDAO#getDataResourceForProvider(java.lang.Long)
	 */
	@SuppressWarnings("unchecked")
	public List<DataResource> getDataResourcesForProvider(final long dataProviderId) {
		HibernateTemplate template = getHibernateTemplate();
		return (List<DataResource>) template.execute(new HibernateCallback() {
			public Object doInHibernate(Session session) {
				Query query = session.createQuery("from DataResource dr inner join fetch dr.dataProvider dp where dp.id = ? and dr.deleted is null order by dr.name");
				query.setParameter(0, dataProviderId);
				query.setCacheable(true);
				return query.list();
			}
		});		
	}
	
	/**
	 * @see org.gbif.portal.dao.resources.DataResourceDAO#getLastDataResourceAdded()
	 */
	public DataResource getLastDataResourceAdded() {
		HibernateTemplate template = getHibernateTemplate();
		return (DataResource) template.execute(new HibernateCallback() {
			public Object doInHibernate(Session session) {
        Query query =
          session
            .createQuery("from DataResource dr left join fetch dr.dataProvider order by dr.created desc");
        query.setMaxResults(1);
        query.setCacheable(true);
				return query.uniqueResult();
			}
		});		
	}

	/**
	 * @see org.gbif.portal.dao.resources.DataResourceDAO#getDataResourcesWithNonSharedTaxonomies()
	 */
	@SuppressWarnings("unchecked")
	public List<DataResource> getDataResourcesWithNonSharedTaxonomies() {
		HibernateTemplate template = getHibernateTemplate();
		return (List<DataResource>) template.execute(new HibernateCallback() {
			public Object doInHibernate(Session session) {
				Query query = session.createQuery(
						"from DataResource dr inner join fetch dr.dataProvider" +
						" where dr.sharedTaxonomy=false and dr.conceptCount>0" +
						" and dr.deleted is null" +
						" order by dr.name");
				query.setCacheable(true);
				return query.list();
			}
		});		
	}	

	/**
	 * @see org.gbif.portal.dao.resources.DataResourceDAO#findDataResources(java.lang.String, boolean, java.lang.Integer, java.lang.Integer)
	 */
	@SuppressWarnings("unchecked")
	public List<DataResource> findDataResources(final String nameStub, final boolean fuzzy, final DataProvider dataProvider, final BasisOfRecord basisOfRecord, final Date modifiedSince, final int startIndex, final int maxResults) {
		HibernateTemplate template = getHibernateTemplate();
		return (List<DataResource>) template.execute(new HibernateCallback() {
			public Object doInHibernate(Session session) {
				Criteria criteria = session.createCriteria(DataResource.class, "dr");
				if (nameStub != null) {
					if (fuzzy) {
						criteria = criteria.add(Restrictions.like("dr.name", nameStub, MatchMode.START));
					}
					else {
						criteria = criteria.add(Restrictions.eq("dr.name", nameStub));
					}
				}
				if (dataProvider != null) {
					criteria = criteria.add(Restrictions.eq("dr.dataProvider", dataProvider));
				}
				if (basisOfRecord != null) {
					criteria = criteria.add(Restrictions.eq("dr.basisOfRecord", basisOfRecord));
				}
				if (modifiedSince != null) {
					criteria = criteria.add(Restrictions.ge("dp.modified", modifiedSince));
				}
				criteria = criteria.add(Restrictions.isNull("dr.deleted"));
				criteria.setFetchMode("dr.dataProvider", FetchMode.JOIN);
				criteria.setCacheable(true);
				criteria.setMaxResults(maxResults);
				criteria.setFirstResult(startIndex);
				return criteria.list();
			}
		});		
	}
	
	/**
	 * @see org.gbif.portal.dao.resources.DataProviderDAO#findDataProviders(java.lang.String, boolean, int, int)
	 */
	@SuppressWarnings("unchecked")
	public Long countDataResources(final String nameStub, final boolean fuzzy, final DataProvider dataProvider, final BasisOfRecord basisOfRecord, final Date modifiedSince) {
		HibernateTemplate template = getHibernateTemplate();
		return ((Integer) template.execute(new HibernateCallback() {
			public Object doInHibernate(Session session) {
				Criteria criteria = session.createCriteria(DataResource.class, "dr");
				if (nameStub != null) {
					if (fuzzy) {
						criteria = criteria.add(Restrictions.like("dr.name", nameStub, MatchMode.START));
					}
					else {
						criteria = criteria.add(Restrictions.eq("dr.name", nameStub));
					}
				}
				if (dataProvider != null) {
					criteria = criteria.add(Restrictions.eq("dr.dataProvider", dataProvider));
				}
				if (basisOfRecord != null) {
					criteria = criteria.add(Restrictions.eq("dr.basisOfRecord", basisOfRecord));
				}
				if (modifiedSince != null) {
					criteria = criteria.add(Restrictions.ge("dp.modified", modifiedSince));
				}
				criteria = criteria.add(Restrictions.isNull("dr.deleted"));				
				criteria.setFetchMode("dr.dataProvider", FetchMode.JOIN);
				criteria.setProjection(Projections.rowCount());
				return criteria.uniqueResult();
		}
		})).longValue();		
	}	
	
	/**
	 * @see org.gbif.portal.dao.resources.DataResourceDAO#findDataResourcesAndProviders(java.lang.String, boolean, java.lang.Integer, java.lang.Integer)
	 */
	@SuppressWarnings("unchecked")
	public List findDataResourcesAndProvidersAndNetworks(final String nameStub, final boolean fuzzy, final boolean anyOccurrence, final boolean includeCountrySearch, final int startIndex, final int maxResults) {
		List modelObjects = new ArrayList();
		try {
			HibernateTemplate template = getHibernateTemplate();
			List results =  (List) template.execute(new HibernateCallback() {
				public Object doInHibernate(Session session) {
					
					String searchString = nameStub;
					if(fuzzy)
						searchString=searchString+'%';
					String anyPartNameString =  "%"+searchString;					
					
					StringBuffer sb = new StringBuffer("Select 'provider', dp.id, dp.name, 0 as shared_taxonomy, dp.occurrence_count, dp.occurrence_coordinate_count, dp.concept_count, dp.species_count, dp.data_resource_count, dp.id as provider_id, dp.name as provider_name, dp.iso_country_code from data_provider dp");
					sb.append(" where (dp.name like :nameStub ");
					if(anyOccurrence){
						sb.append(" or dp.name like '"+anyPartNameString +"'");
					}
					for(String prefix: resourceNamePrefixes)
						sb.append(" or dp.name like '"+prefix+" "+searchString+"'");
					
					sb.append(") and dp.deleted is null");
					sb.append(" UNION ");
					sb.append(" Select 'resource', dr.id, dr.display_name, dr.shared_taxonomy as shared_taxonomy, dr.occurrence_count, dr.occurrence_coordinate_count, dr.concept_count, dr.species_count, dr.species_count, dp.id as provider_id, dp.name as provider_name, 'XX' from data_resource dr inner join data_provider dp on dr.data_provider_id=dp.id");
					sb.append(" where ( dr.display_name like :nameStub ");				
					if(anyOccurrence){
						sb.append(" or dr.display_name like '"+anyPartNameString +"'");
					}
					for(String prefix: resourceNamePrefixes){
						sb.append(" or dr.display_name like '");
						sb.append(prefix);
						sb.append(' ');
						sb.append(searchString);
						sb.append("'");
					}	
					sb.append(") and dr.deleted is null");
					sb.append(" UNION ");
					sb.append(" Select 'network', rn.id, rn.name, 0 as shared_taxonomy, rn.occurrence_count, rn.occurrence_coordinate_count, rn.concept_count, rn.species_count, rn.data_resource_count, rn.id as provider_id, rn.name as provider_name, rn.code from resource_network rn left join country_name cn on rn.code = cn.iso_country_code");
					sb.append(" where ( rn.name like :nameStub ");
					if(includeCountrySearch){
						sb.append("or rn.code like :nameStub or cn.name like :nameStub");
					}
					if(anyOccurrence){
						sb.append(" or rn.name like '"+anyPartNameString +"'");
					}
					for(String prefix: resourceNamePrefixes){
						sb.append(" or rn.name like '");
						sb.append(prefix);
						sb.append(' ');
						sb.append(searchString);
						sb.append("'");
					}	
					sb.append(") and rn.deleted is null");
										
					Query query = session.createSQLQuery(sb.toString());
					query.setParameter("nameStub", searchString);
					query.setMaxResults(maxResults);
					query.setFirstResult(startIndex);			
					query.setCacheable(true);
					return query.list();
				}
			});		
			
			for (Iterator iter = results.iterator(); iter.hasNext();) {
				Object[] result = (Object[]) iter.next();
				Object modelObject=null;
				Long id = null;
				if(result[1] instanceof BigInteger)
					id = ((BigInteger)result[1]).longValue();
				else if (result[1] instanceof Integer)
					id = ((Integer)result[1]).longValue();
				else if (result[1] instanceof Long)
					id = (Long)result[1];
				
				
				if(result[0].equals("resource")){
					modelObject = new DataResource(id, 
							(String) result[2], 
							((BigInteger)result[3]).intValue()==1, 
							(Integer)result[4], 
							(Integer)result[5], 
							(Integer)result[6], 
							(Integer)result[7], 
							(Integer)result[9], 
							(String)result[10]);
				} else if(result[0].equals("provider")){
					modelObject = new DataProvider(
							id, 
							(String) result[2], 
							(Integer)result[4], 
							(Integer)result[5], 
							(Integer)result[6], 
							(Integer)result[7], 
							(Integer)result[8], 
							(String)result[11]);
				} else {
					modelObject = new ResourceNetwork(id, (String)result[2], (String)result[11], (Integer)result[4], (Integer)result[5], (Integer)result[6], (Integer)result[7], (Integer)result[8]);
				} 
				modelObjects.add(modelObject);
			}
		} catch (Exception e){
			logger.error(e.getMessage(), e);
		}
		return modelObjects;
	}	
	
	/**
	 * @see org.gbif.portal.dao.resources.DataResourceDAO#getTotalDataResourceCount()
	 */
	public int getTotalDataResourceCount() {
		HibernateTemplate template = getHibernateTemplate();
		Object result =  template.execute(new HibernateCallback() {
			public Object doInHibernate(Session session) {
				Query query = session.createQuery("select count(dr.id) from DataResource dr where dr.deleted is null and dr.dataProvider.deleted is null" +
						" and dr.occurrenceCount>=10 and dr.dataProvider.gbifApprover is not null");
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
	 * @see org.gbif.portal.dao.resources.DataResourceDAO#getDataResourcesForResourceNetwork(long)
	 */
	@SuppressWarnings("unchecked")
	public List<DataResource> getDataResourcesForResourceNetwork(final long resourceNetworkId) {
		HibernateTemplate template = getHibernateTemplate();
		return (List<DataResource>) template.execute(new HibernateCallback() {
			public Object doInHibernate(Session session) {
				Query query = session.createQuery(
						"select nm.dataResource from NetworkMembership nm" +
						" join nm.resourceNetwork rn" +
						" left join fetch nm.dataResource.dataProvider" +
						" where rn.id = ? and nm.dataResource.deleted is null" +
						" order by nm.dataResource.name"
				);
				query.setParameter(0, resourceNetworkId);
				query.setCacheable(true);
				return query.list();
			}
		});		
	}

	/**
	 * @see org.gbif.portal.dao.resources.DataResourceDAO#getDatasetAlphabet()
	 */
	@SuppressWarnings("unchecked")
	public List<Character> getDatasetAlphabet() {
		HibernateTemplate template = getHibernateTemplate();		
		List<String> dataResourceChars =  (List<String>) template.execute(new HibernateCallback() {
			public Object doInHibernate(Session session) {
				Query query = session.createSQLQuery("select distinct(SUBSTRING(display_name,1,1)) from data_resource where display_name is not null and deleted is null order by display_name");				
				return query.list();
			}
		});
		List<String> dataProviderChars =  (List<String>) template.execute(new HibernateCallback() {
			public Object doInHibernate(Session session) {
				Query query = session.createSQLQuery("select distinct(SUBSTRING(name,1,1)) from data_provider where name is not null order by name");				
				return query.list();
			}
		});		
		List<String> resourceNetworksChars =  (List<String>) template.execute(new HibernateCallback() {
			public Object doInHibernate(Session session) {
				Query query = session.createSQLQuery("select distinct(SUBSTRING(name,1,1)) from resource_network where name is not null order by name");				
				return query.list();
			}
		});		
		
		ArrayList<Character> chars = new ArrayList<Character>();
		for(String result: dataResourceChars){
			if(StringUtils.isNotEmpty(result))
				chars.add(new Character(Character.toUpperCase(result.charAt(0))));
		}
		for(String result: dataProviderChars){
			if(StringUtils.isNotEmpty(result)){
				Character theChar = new Character(Character.toUpperCase(result.charAt(0)));
				if(!chars.contains(theChar))
					chars.add(theChar);
			}
		}
		for(String result: resourceNetworksChars){
			if(StringUtils.isNotEmpty(result)){
				Character theChar = new Character(Character.toUpperCase(result.charAt(0)));
				if(!chars.contains(theChar))
					chars.add(theChar);
			}
		}		
		
		Collections.sort(chars);
		return chars;
	}
	
	/**
	 * @see org.gbif.portal.dao.resources.DataResourceDAO#getDataResourceCountsForCountry(java.lang.Long)
	 */
	@SuppressWarnings("unchecked")
	public List<Object[]> getDataResourceCountsForCountry(final String isoCountryCode, final boolean geoRefOnly) {
		HibernateTemplate template = getHibernateTemplate();
		return (List) template.execute(new HibernateCallback() {
			public Object doInHibernate(Session session) {
				StringBuffer sb = new StringBuffer("select dr.id, dr.display_name as dr_name, dp.name as dp_name,");
				if(geoRefOnly){
					sb.append(" rc.occurrence_coordinate_count ");
				} else {
					sb.append(" rc.count ");
				}
				sb.append("from resource_country rc" +
						" inner join data_resource dr on rc.data_resource_id=dr.id" +
						" inner join data_provider dp on dr.data_provider_id=dp.id" +
						" where ");
				if(isoCountryCode!=null){
					sb.append("rc.iso_country_code=:isoCountryCode and ");
				}
				sb.append("dr.deleted is null");			
				
				if(geoRefOnly){
					sb.append(" and rc.occurrence_coordinate_count>0");
				}
				sb.append(" order by dr_name");
				Query query = session.createSQLQuery(sb.toString());
				if(isoCountryCode!=null)
					query.setParameter("isoCountryCode", isoCountryCode);
				query.setCacheable(true);
				return query.list();
			}
		});	
	}

	/**
	 * 
	 * @param dataResourceId
	 * @param geoRefOnly
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Object[]> getCountryCountsForDataResource(final Long dataResourceId, final boolean geoRefOnly) {
		HibernateTemplate template = getHibernateTemplate();
		return (List) template.execute(new HibernateCallback() {
			public Object doInHibernate(Session session) {
				StringBuffer sb = new StringBuffer("select c.id as country_id, c.iso_country_code as iso_country_code, c.region as region,");
				if(geoRefOnly){
					sb.append(" rc.occurrence_coordinate_count as the_count");
				} else {
					sb.append(" rc.count as the_count");
				}
				sb.append(" from resource_country rc inner join country c on c.iso_country_code=rc.iso_country_code");
				if(dataResourceId!=null){
					sb.append(" where rc.data_resource_id=:dataResourceId ");
				}
				
				if(geoRefOnly){
					sb.append(" and rc.occurrence_coordinate_count>0");
				}
				sb.append(" order by c.iso_country_code");
				SQLQuery query = session.createSQLQuery(sb.toString());
				if(dataResourceId!=null)
					query.setParameter("dataResourceId", dataResourceId);
				query.setCacheable(true);
				query.addScalar("country_id", Hibernate.LONG);
				query.addScalar("iso_country_code", Hibernate.STRING);
				query.addScalar("region", Hibernate.STRING);
				query.addScalar("the_count", Hibernate.INTEGER);
				return query.list();
			}
		});	
	}
	
	/**
	 * @see org.gbif.portal.dao.resources.DataResourceDAO#getDataResourceList(java.lang.Long)
	 */
	@SuppressWarnings("unchecked")
	public List getDataResourceList(final Long dataProviderId) {
		HibernateTemplate template = getHibernateTemplate();
		return (List) template.execute(new HibernateCallback() {
			public Object doInHibernate(Session session) {
				Query query = session.createQuery("select dr.id, dr.name from DataResource dr where dr.dataProvider.id = ? and dr.deleted is null order by dr.name");
				query.setParameter(0, dataProviderId);
				query.setCacheable(true);
				return query.list();
			}
		});	
	}	

	/**
	 * TODO This may need refactoring to use a join table similar to resource_country or taxon_country
	 * 
	 * @see org.gbif.portal.dao.resources.DataResourceDAO#getDataResourceWithOccurrencesFor(java.lang.Long, java.lang.String, boolean)
	 */
	@SuppressWarnings("unchecked")
	public List<Object[]> getDataResourceWithOccurrencesFor(final Long taxonConceptId,
			final String rank, final boolean georeferenced) {
		HibernateTemplate template = getHibernateTemplate();
		return (List) template.execute(new HibernateCallback() {
			public Object doInHibernate(Session session) {
				StringBuffer sb = new StringBuffer("select oc.dataResource.id, oc.dataResource.name, oc.dataProvider.name, count(oc.id) " +
						"from OccurrenceRecord oc where oc.");
				
				if(TaxonRankType.isRecognisedMajorRank(rank)){
					sb.append(rank);
					sb.append("ConceptId=:taxonConceptId");
				} else {
					sb.append("nubTaxonConceptId=:taxonConceptId");
				}
				
				if(georeferenced){
					sb.append(" and oc.cellId is not null and oc.geospatialIssue=0");
				}
				
				sb.append(" group by oc.dataResourceId");
				
				Query query = session.createQuery(sb.toString());
				query.setParameter("taxonConceptId", taxonConceptId);
				query.setCacheable(true);
				return query.list();
			}
		});	
	}	
	
	/**
	 * @param resourceNamePrefixes the resourceNamePrefixes to set
	 */
	public void setResourceNamePrefixes(List<String> resourceNamePrefixes) {
		this.resourceNamePrefixes = resourceNamePrefixes;
	}
}