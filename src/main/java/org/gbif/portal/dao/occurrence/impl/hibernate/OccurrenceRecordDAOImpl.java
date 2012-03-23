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
package org.gbif.portal.dao.occurrence.impl.hibernate;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.gbif.portal.dao.occurrence.OccurrenceRecordDAO;
import org.gbif.portal.dto.util.SearchConstraints;
import org.gbif.portal.model.occurrence.BasisOfRecord;
import org.gbif.portal.model.occurrence.OccurrenceRecord;
import org.gbif.portal.model.resources.DataProvider;
import org.gbif.portal.model.resources.DataResource;
import org.gbif.portal.model.resources.ResourceNetwork;
import org.gbif.portal.model.taxonomy.TaxonConcept;
import org.gbif.portal.model.taxonomy.TaxonRank;
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
 * A Hibernate DAO Implementation for accessing OccurrenceRecord model objects.
 * 
 * @author dmartin
 */
public class OccurrenceRecordDAOImpl extends HibernateDaoSupport implements OccurrenceRecordDAO {

	/** Whether or not to use count instead of max id */
	protected boolean useCount = true;
	
	/**
	 * @see org.gbif.portal.dao.occurrence.OccurrenceRecordDAO#getOccurrenceRecordFor(long)
	 */
	public OccurrenceRecord getOccurrenceRecordFor(final long occurrenceRecordId) {
		HibernateTemplate template = getHibernateTemplate();
		return (OccurrenceRecord) template.execute(new HibernateCallback() {
			public Object doInHibernate(Session session) {
				Query query = session.createQuery(
						"from OccurrenceRecord oc" +
						" inner join fetch oc.dataProvider" +
						" inner join fetch oc.dataResource" +
						" inner join fetch oc.taxonConcept" +
						" inner join fetch oc.taxonName" +						
						" where oc.id = ?");
				query.setParameter(0, occurrenceRecordId);
				query.setCacheable(true);
				return query.uniqueResult();
			}
		});		
	}

	/**
	 * @see org.gbif.portal.dao.occurrence.OccurrenceRecordDAO#getOccurrenceRecordFor(java.lang.String, java.lang.String, java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	public List<OccurrenceRecord> getOccurrenceRecordFor(final String institutionCode,
			final String collectionCode, final String catalogueNumber) {
		HibernateTemplate template = getHibernateTemplate();
		return (List<OccurrenceRecord>) template.execute(new HibernateCallback() {
			public Object doInHibernate(Session session) {
				Query query = session.createQuery(
						"from OccurrenceRecord oc" +
						" inner join fetch oc.dataProvider" +
						" inner join fetch oc.dataResource" +
						" inner join fetch oc.taxonConcept" +
						" inner join fetch oc.taxonName" +						
						" where oc.catalogueNumber.code = :catalogueNumber" +
						" and oc.collectionCode.code = :collectionCode" +
						" and oc.institutionCode.code = :institutionCode");
				query.setParameter("catalogueNumber", catalogueNumber);
				query.setParameter("collectionCode", collectionCode);
				query.setParameter("institutionCode", institutionCode);						
				query.setCacheable(true);
				return query.list();
			}
		});	
	}	
	
	/**
	 * @see org.gbif.portal.dao.occurrence.OccurrenceRecordDAO#findOccurrenceRecord(org.gbif.portal.model.taxonomy.TaxonConcept, org.gbif.portal.model.resources.DataResource, java.lang.String, java.lang.String, java.lang.Float, java.lang.Float, java.lang.Float, java.lang.Float, java.util.Date, java.util.Date, BasisOfRecord basisOfRecord, Date modifiedSince, boolean georeferencedOnly, SearchConstraints searchConstraints)
	 */
	@SuppressWarnings("unchecked")
	public List<OccurrenceRecord> findOccurrenceRecords(final TaxonConcept taxonConcept, final DataProvider dataProvider, final DataResource dataResource, final ResourceNetwork resourceNetwork, final String scientificName, final String hostIsoCountryCode, final String originIsoCountryCode, final Float minLongitude, final Float maxLongitude, final Float minLatitude, final Float maxLatitude, final Integer cellId, final Date startDate, final Date endDate, final BasisOfRecord basisOfRecord, final Date modifiedSince, final boolean georeferencedOnly, final SearchConstraints searchConstraints) {
		HibernateTemplate template = getHibernateTemplate();
		return (List<OccurrenceRecord>) template.execute(new HibernateCallback() {
			public Object doInHibernate(Session session) {
				Criteria criteria = buildCriteria(session, 
				  taxonConcept, 
				  dataProvider,
				  dataResource, 
				  resourceNetwork, 
				  scientificName, 
				  hostIsoCountryCode, 
				  originIsoCountryCode, 
				  minLongitude, 
				  maxLongitude, 
				  minLatitude, 
				  maxLatitude, 
				  cellId, 
				  startDate, 
				  endDate, 
				  basisOfRecord, 
				  modifiedSince, 
				  georeferencedOnly, 
				  searchConstraints);
				return criteria.list();
			}
		});
	}
	
	/**
	 * @see org.gbif.portal.dao.occurrence.OccurrenceRecordDAO#countOccurrenceRecord(org.gbif.portal.model.taxonomy.TaxonConcept, org.gbif.portal.model.resources.DataResource, java.lang.String, java.lang.String, java.lang.Float, java.lang.Float, java.lang.Float, java.lang.Float, java.util.Date, java.util.Date, BasisOfRecord, java.util.Date, boolean)
	 */
	@SuppressWarnings("unchecked")
	public Long countOccurrenceRecords(final TaxonConcept taxonConcept, final DataProvider dataProvider, final DataResource dataResource, final ResourceNetwork resourceNetwork, final String scientificName, final String hostIsoCountryCode, final String originIsoCountryCode, final Float minLongitude, final Float maxLongitude, final Float minLatitude, final Float maxLatitude, final Integer cellId, final Date startDate, final Date endDate, final BasisOfRecord basisOfRecord, final Date modifiedSince, final boolean georeferencedOnly) {
		HibernateTemplate template = getHibernateTemplate();
		return ((Integer) template.execute(new HibernateCallback() {
			public Object doInHibernate(Session session) {
				Criteria criteria = buildCriteria(session, 
						  taxonConcept, 
						  dataProvider,
						  dataResource, 
						  resourceNetwork, 
						  scientificName, 
						  hostIsoCountryCode, 
						  originIsoCountryCode, 
						  minLongitude, 
						  maxLongitude, 
						  minLatitude, 
						  maxLatitude, 
						  cellId, 
						  startDate, 
						  endDate, 
						  basisOfRecord, 
						  modifiedSince, 
						  georeferencedOnly, 
						  null);
				criteria.setProjection(Projections.rowCount());
				return criteria.uniqueResult();
			}
		})).longValue();
	}
	
	/**
	 * Builds a criteria object for using the specified criteria, ignoring null arguments.
	 * 
	 * @param session
	 * @param taxonConcept
	 * @param dataProvider
	 * @param dataResource
	 * @param resourceNetwork
	 * @param scientificName
	 * @param hostIsoCountryCode
	 * @param originIsoCountryCode
	 * @param minLongitude
	 * @param maxLongitude
	 * @param minLatitude
	 * @param maxLatitude
	 * @param cellId
	 * @param startDate
	 * @param endDate
	 * @param basisOfRecord
	 * @param modifiedSince
	 * @param georeferencedOnly
	 * @param searchConstraints
	 * @return a constructed hibernate criteria 
	 */
	private Criteria buildCriteria(Session session, final TaxonConcept taxonConcept, final DataProvider dataProvider, final DataResource dataResource, final ResourceNetwork resourceNetwork, final String scientificName, final String hostIsoCountryCode, final String originIsoCountryCode, final Float minLongitude, final Float maxLongitude, final Float minLatitude, final Float maxLatitude, final Integer cellId, final Date startDate, final Date endDate, final BasisOfRecord basisOfRecord, final Date modifiedSince, final boolean georeferencedOnly, final SearchConstraints searchConstraints) {
		Criteria criteria = session.createCriteria(OccurrenceRecord.class, "ocr");
		if (taxonConcept != null) {
			String searchRankString = null;

			if (taxonConcept.getIsNubConcept()) {
				criteria = criteria.createAlias("ocr.nubTaxonConcept", "ocrtc");
			} else {
				criteria = criteria.createAlias("ocr.taxonConcept", "ocrtc");
			}

			TaxonRank taxonConceptRank = taxonConcept.getTaxonRank();
			if (taxonConceptRank != null) {
				if (taxonConceptRank.equals(TaxonRank.KINGDOM)) {
					searchRankString = "ocrtc.kingdomConceptId";
				} else if (taxonConceptRank.equals(TaxonRank.PHYLUM)) {
					searchRankString = "ocrtc.phylumConceptId";
				} else if (taxonConceptRank.equals(TaxonRank.CLASS)) {
					searchRankString = "ocrtc.classConceptId";
				} else if (taxonConceptRank.equals(TaxonRank.ORDER)) {
					searchRankString = "ocrtc.orderConceptId";
				} else if (taxonConceptRank.equals(TaxonRank.FAMILY)) {
					searchRankString = "ocrtc.familyConceptId";
				} else if (taxonConceptRank.equals(TaxonRank.GENUS)) {
					searchRankString = "ocrtc.genusConceptId";
				} else if (taxonConceptRank.equals(TaxonRank.SPECIES)) {
					searchRankString = "ocrtc.speciesConceptId";
				}
			}
			
			if (searchRankString != null) {
				criteria = criteria.add(
						Restrictions.or(Restrictions.eq("ocrtc.id", taxonConcept.getId()), 
										Restrictions.eq(searchRankString, taxonConcept.getId())));
			} else {
				criteria = criteria.add(Restrictions.eq("ocrtc.id", taxonConcept.getId()));
			}
		}
		if (dataResource != null) {
			criteria = criteria.add(Restrictions.eq("ocr.dataResource", dataResource));
		}
		if (hostIsoCountryCode != null || dataProvider != null || resourceNetwork != null) {
			criteria = criteria.createAlias("ocr.dataResource", "ocrdr");
			
			if (hostIsoCountryCode != null) {
				criteria = criteria.createAlias("ocrdr.dataProvider", "ocrpd");
				criteria = criteria.add(Restrictions.eq("ocrpd.isoCountryCode", hostIsoCountryCode));
			} 
			if (dataProvider != null) {
				criteria = criteria.add(Restrictions.eq("ocrdr.dataProvider", dataProvider));
			}
			if (resourceNetwork != null) {
				criteria = criteria.createAlias("ocrdr.networkMemberships", "ocrnm");
				criteria = criteria.add(Restrictions.eq("ocrnm.resourceNetwork", resourceNetwork));
			} 
		}
		if (scientificName != null) {
			criteria = criteria.createAlias("ocr.taxonName", "ocrtn");
			int wildcardIndex = scientificName.indexOf("*");
			if (wildcardIndex > 0) {
				String nameStub = scientificName.substring(0, wildcardIndex);
				criteria = criteria.add(Restrictions.like("ocrtn.canonical", nameStub, MatchMode.START));
			}
			else {
				criteria = criteria.add(Restrictions.eq("ocrtn.canonical", scientificName));
			}
		}
		if (originIsoCountryCode != null) {
			criteria = criteria.add(Restrictions.eq("ocr.isoCountryCode", originIsoCountryCode));
		}
		if (cellId != null) {
			criteria = criteria.add(Restrictions.eq("ocr.cellId", cellId));
		}
		if (minLongitude != null) {
			criteria = criteria.add(Restrictions.ge("ocr.longitude", minLongitude));
		}
		if (maxLatitude != null) {
			criteria = criteria.add(Restrictions.le("ocr.latitude", maxLatitude));
		}
		if (maxLongitude != null) {
			criteria = criteria.add(Restrictions.le("ocr.longitude", maxLongitude));
		}
		if (minLatitude != null) {
			criteria = criteria.add(Restrictions.ge("ocr.latitude", minLatitude));
		}
		if (startDate != null) {
			criteria = criteria.add(Restrictions.ge("ocr.occurrenceDate", startDate));
		}
		if (endDate != null) {
			criteria = criteria.add(Restrictions.le("ocr.occurrenceDate", endDate));
		}
		if (basisOfRecord != null) {
			criteria = criteria.add(Restrictions.eq("ocr.basisOfRecord", basisOfRecord));
		}
		if (modifiedSince != null) {
			criteria = criteria.add(Restrictions.ge("ocr.modified", modifiedSince));
		}
		if (georeferencedOnly) {
			criteria = criteria.add(Restrictions.isNotNull("ocr.cellId"));
		}
		if (searchConstraints != null) {
			if (searchConstraints.getStartIndex() != 0) {
				criteria.setFirstResult(searchConstraints.getStartIndex().intValue());
			}
			if (searchConstraints.getMaxResults() != null) {
				criteria = criteria.setMaxResults(searchConstraints.getMaxResults().intValue());
			}
		}
		
		return criteria;
	}
	
    /** 
     * @see org.gbif.portal.dao.occurrence.OccurrenceRecordDAO#getTotalOccurrenceRecordCountForDeletedProviders()
     */
    public int getTotalOccurrenceRecordCountForDeletedProviders() {         
                    HibernateTemplate template = getHibernateTemplate();
                    Object count = template.execute(new HibernateCallback() {
                            public Object doInHibernate(Session session) {
                                            Query query = session
                                                            .createQuery("select count(*) from OccurrenceRecord oc where oc.dataProvider.deleted is not null");
                                    query.setCacheable(true);
                                    return query.uniqueResult();
                            }
                    });             
                    if (count instanceof Integer)
                            return ((Integer)count).intValue();
                    if (count instanceof Long)
                            return ((Long)count).intValue();
                    return 0;       
    }
	
	
	/**
	 * @see org.gbif.portal.dao.occurrence.OccurrenceRecordDAO#getTotalOccurrenceRecordCount()
	 */
	public int getTotalOccurrenceRecordCount() {
		HibernateTemplate template = getHibernateTemplate();
		Object count = template.execute(new HibernateCallback() {
			public Object doInHibernate(Session session) {
				Query query = null;
				if(useCount)
					query = session.createQuery("select count(oc.id) from OccurrenceRecord oc");
				else
					query = session.createQuery("select max(oc.id) from OccurrenceRecord oc");
				query.setCacheable(true);
				return query.uniqueResult();
			}
		});	
		if (count instanceof Integer)
			return ((Integer)count).intValue();
		if (count instanceof Long)
			return ((Long)count).intValue();
		return 0;
	}
	
  /**
   * @see org.gbif.portal.dao.occurrence.OccurrenceRecordDAO#getTotalGeoreferencedOccurrenceRecordCount()
   */
  public int getTotalGeoreferencedOccurrenceRecordCount() {
    HibernateTemplate template = getHibernateTemplate();
    Object count = template.execute(new HibernateCallback() {
      public Object doInHibernate(Session session) {
        Query query = null;
        query = session.createQuery("select sum(dp.occurrenceCoordinateCount) from DataProvider dp where dp.deleted is null");
        query.setCacheable(true);
        return query.uniqueResult();
      }
    }); 
    if (count instanceof Integer)
      return ((Integer)count).intValue();
    if (count instanceof Long)
      return ((Long)count).intValue();
    return 0;
  }	

	/**
	 * TODO this will change when nub taxon concept ids are added to occurrence record table.
	 * 
	 * @see org.gbif.portal.dao.occurrence.OccurrenceRecordDAO#getCentiCellDensitiesForTaxonConcept(long, int)
	 */
	public List getCentiCellDensitiesForTaxonConcept(final long taxonConceptId, final int cellId) {
		try {
			HibernateTemplate template = getHibernateTemplate();
			return  (List) template.execute(new HibernateCallback() {
				public Object doInHibernate(Session session) {
					TaxonConcept taxonConcept = (TaxonConcept) session.get(TaxonConcept.class, new Long(taxonConceptId));
					StringBuffer queryStr = new StringBuffer();
					queryStr.append("select ore.centi_cell_id, count(ore.id) from occurrence_record ore");

					if( taxonConcept.getTaxonRank().getValue()<TaxonRank.SPECIES.getValue()){					
						queryStr.append(" inner join taxon_concept tc on tc.id=ore.");
						//use nub if available, else use taxon_concept_id
						//if(taxonConcept.getNubConceptId()!=null)
						queryStr.append("nub_concept_id"); 
						//else
							//queryStr.append(	"taxon_concept_id"); 
						
						queryStr.append(" where ");
						if( taxonConcept.getTaxonRank().getValue()<TaxonRank.SPECIES.getValue()){
							if(taxonConcept.getTaxonRank().equals(TaxonRank.KINGDOM))
								queryStr.append("tc.kingdom_concept_id");
							else if(taxonConcept.getTaxonRank().equals(TaxonRank.PHYLUM))
								queryStr.append("tc.phylum_concept_id");
							else if(taxonConcept.getTaxonRank().equals(TaxonRank.CLASS))
								queryStr.append("tc.class_concept_id");
							else if(taxonConcept.getTaxonRank().equals(TaxonRank.ORDER))
								queryStr.append("tc.order_concept_id");
							else if(taxonConcept.getTaxonRank().equals(TaxonRank.FAMILY))
								queryStr.append("tc.family_concept_id");
							else if(taxonConcept.getTaxonRank().equals(TaxonRank.GENUS))
								queryStr.append("tc.genus_concept_id");
						}
						
						queryStr.append("=:taxonConceptId");
					} else {
						queryStr.append(" where ore.nub_concept_id=:taxonConceptId");
					}
					queryStr.append(" and ore.cell_id=:cellId group by ore.centi_cell_id order by ore.centi_cell_id");

					Query query = session.createSQLQuery(queryStr.toString());
					query.setParameter("taxonConceptId", taxonConceptId);
					query.setParameter("cellId", cellId);
					return query.list();
				}
			});		
		} catch (Exception e){
			logger.error(e.getMessage(), e);
			return new LinkedList();
		}
	}

	/**
	 * @see org.gbif.portal.dao.occurrence.OccurrenceRecordDAO#getCentiCellDensitiesForDataProvider(long, int)
	 */
	public List getCentiCellDensitiesForDataProvider(final long dataProviderId, final int cellId) {
		try {
			HibernateTemplate template = getHibernateTemplate();
			return  (List) template.execute(new HibernateCallback() {
				public Object doInHibernate(Session session) {
					Query query = session.createSQLQuery(
					"select centi_cell_id, count(id) from occurrence_record " +
					"where data_provider_id=:dataProviderId and cell_id=:cellId " +
					"group by centi_cell_id order by centi_cell_id");
					query.setParameter("dataProviderId", dataProviderId);
					query.setParameter("cellId", cellId);
					return query.list();
				}
			});		
		} catch (Exception e){
			logger.error(e.getMessage(), e);
			return new LinkedList();
		}
	}

	/**
	 * @see org.gbif.portal.dao.occurrence.OccurrenceRecordDAO#getCentiCellDensitiesForDataResource(long, int)
	 */
	public List getCentiCellDensitiesForDataResource(final long dataResourceId, final int cellId) {
		try {
			HibernateTemplate template = getHibernateTemplate();
			return  (List) template.execute(new HibernateCallback() {
				public Object doInHibernate(Session session) {
					Query query = session.createSQLQuery(
					"select centi_cell_id, count(id) from occurrence_record " +
					"where data_resource_id=:dataResourceId and cell_id=:cellId " +
					"group by centi_cell_id order by centi_cell_id");
					query.setParameter("dataResourceId", dataResourceId);
					query.setParameter("cellId", cellId);
					return query.list();
				}
			});		
		} catch (Exception e){
			logger.error(e.getMessage(), e);
			return new LinkedList();
		}
	}

	/**
	 * @see org.gbif.portal.dao.occurrence.OccurrenceRecordDAO#getCentiCellDensitiesForIsoCountryCode(java.lang.String, int)
	 */
	public List getCentiCellDensitiesForIsoCountryCode(final String isoCountryCode, final int cellId) {
		try {
			HibernateTemplate template = getHibernateTemplate();
			return  (List) template.execute(new HibernateCallback() {
				public Object doInHibernate(Session session) {
					Query query = session.createSQLQuery(
					"select centi_cell_id, count(id) from occurrence_record " +
					"where iso_country_code=:isoCountryCode and cell_id=:cellId " +
					"group by centi_cell_id order by centi_cell_id");
					query.setParameter("isoCountryCode", isoCountryCode);
					query.setParameter("cellId", cellId);
					return query.list();
				}
			});		
		} catch (Exception e){
			logger.error(e.getMessage(), e);
			return new LinkedList();
		}
	}

	/**
	 * @see org.gbif.portal.dao.occurrence.OccurrenceRecordDAO#getCentiCellDensitiesForResourceNetwork(long, int)
	 */
	public List getCentiCellDensitiesForResourceNetwork(final long resourceNetworkId, final int cellId) {
		try {
			HibernateTemplate template = getHibernateTemplate();
			return  (List) template.execute(new HibernateCallback() {
				public Object doInHibernate(Session session) {
					Query query = session.createSQLQuery(
						"select oc.centi_cell_id, count(oc.id) from network_membership nm" + 
						" inner join occurrence_record oc on nm.data_resource_id=oc.data_resource_id" + 
						" where oc.cell_id = :cellId and nm.resource_network_id = :resourceNetworkId"+
						" group by oc.centi_cell_id order by oc.centi_cell_id");
					query.setParameter("resourceNetworkId", resourceNetworkId);
					query.setParameter("cellId", cellId);
					return query.list();
				}
			});		
		} catch (Exception e){
			logger.error(e.getMessage(), e);
			return new LinkedList();
		}
	}

	/**
	 * @param useCount the useCount to set
	 */
	public void setUseCount(boolean useCount) {
		this.useCount = useCount;
	}
}