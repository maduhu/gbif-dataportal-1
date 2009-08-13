/***************************************************************************
 * Copyright (C) 2006 Global Biodiversity Information Facility Secretariat.
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

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.gbif.portal.dao.geospatial.CountryDAO;
import org.gbif.portal.model.taxonomy.TaxonConcept;
import org.gbif.portal.util.request.IPUtils;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * The DAO for the Country Model object. 
 * 
 * @author dmartin
 */
public class CountryDAOImpl extends HibernateDaoSupport implements CountryDAO {

	/** 
	 * Default ISO code to use when locale is null
	 * TODO Need to move to somewhere more accessible
	 */
	protected String defaultISOLanguageCode ="en";

	/** The list of currently support locales - dynamically loaded on application start up */
	protected List<String> supportedLocales = null;
	
	/**
	 * A map of Character to lists of ISO country codes. 
	 * Used to bring back countries in alphabetical searches.
	 */
	protected Map<Character, List<String>> characterIsoCodeMap;
	
	/**
	 * @see org.gbif.portal.dao.geospatial.CountryDAO#getCountryFor(long, java.util.Locale)
	 */
	public Object getCountryFor(final long countryId, final Locale locale) {
		return getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session) {
				Query query = session.createQuery("from Country c inner join c.countryNames cn where c.countryId=:countryId and cn.locale=:locale");
				query.setLong("countryId", countryId);
				query.setString("locale", getLocaleForQuery(locale));
				return query.uniqueResult();
			}
		});
	}

	/**
	 * @see org.gbif.portal.dao.geospatial.CountryDAO#getCountryForIsoCountryCode(java.lang.String, java.util.Locale)
	 */
	public Object getCountryForIsoCountryCode(final String isoCountryCode, final Locale locale) {
		return getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session) {
				Query query = session.createQuery("from Country c inner join c.countryNames cn where c.isoCountryCode=:isoCountryCode and cn.locale=:locale");
				query.setString("isoCountryCode", isoCountryCode);
				query.setString("locale", getLocaleForQuery(locale));
				return query.uniqueResult();
			}
		});
	}

	/**
	 * @see org.gbif.portal.dao.geospatial.CountryDAO#findCountriesFor(java.lang.String, boolean, java.util.Locale)
	 */
	@SuppressWarnings("unchecked")
	public List findCountriesFor(final String nameStub, final boolean fuzzy, final boolean anyOccurrence, final boolean searchLocaleOnly,  final Locale locale, final int startIndex, final int maxResults) {
		return (List) getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session) {
				
				StringBuffer sb = null;
				if(searchLocaleOnly){
					sb = new StringBuffer("select c, cn.name from Country c inner join c.countryNames cn where (cn.name like :name ");
					if(anyOccurrence){
						sb.append(" or cn.name like :anyPartName");
					}
					sb.append(") and cn.locale=:locale");
					sb.append(" order by cn.name");					
				} else {
					
					sb = new StringBuffer("select distinct c, cns.name from CountryName cn inner join cn.country c inner join c.countryNames cns where (cns.name like :name");
					if(anyOccurrence){
						sb.append(" or cns.name like :anyPartName");
					}
					sb.append(")");
//					sb.append(" and cn.locale=:locale");
					sb.append(" order by cn.name");
				}
				
				Query query = session.createQuery(sb.toString());
				String searchString = nameStub;
				if(fuzzy)
					searchString = searchString+'%';
				if(anyOccurrence)
					query.setString("anyPartName", "% "+searchString);
				
				query.setString("name", searchString);
				if(searchLocaleOnly){
					query.setString("locale", getLocaleForQuery(locale));
				}
				query.setFirstResult(startIndex);
				query.setMaxResults(maxResults);
				return query.list();
			}
		});
	}

	/**
	 * @see org.gbif.portal.dao.geospatial.CountryDAO#getCountriesFor(java.lang.String, boolean, java.util.Locale)
	 */
	@SuppressWarnings("unchecked")
	public List getCountriesFor(final char theChar, final boolean allowAdditionalSorting, final Locale locale) {
		return (List) getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session) {
				
				List<String> extraIsoCodes = null;
				
				StringBuffer sb = new StringBuffer("from Country c inner join c.countryNames cn where (cn.name like :name ");
				if(allowAdditionalSorting && characterIsoCodeMap!=null && characterIsoCodeMap.containsKey(theChar)){
					extraIsoCodes = characterIsoCodeMap.get(theChar);
					for(String isoCode: extraIsoCodes){
						sb.append(" or cn.isoCountryCode='");
						sb.append(isoCode);
						sb.append("'");
					}
				}
				sb.append(") ");
				sb.append("and cn.locale=:locale order by cn.name");					

				Query query = session.createQuery(sb.toString());
				String searchString = theChar+"%";
				query.setString("name", searchString);
				query.setString("locale", getLocaleForQuery(locale));
				return query.list();
			}
		});
	}	
	
	
	/**
	 * @see org.gbif.portal.dao.geospatial.CountryDAO#findCountriesFor(java.lang.String, boolean, java.util.Locale)
	 */
	@SuppressWarnings("unchecked")
	public List findAllCountries(final Locale locale) {
		return (List) getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session) {
				Query query = session.createQuery("from Country c inner join c.countryNames cn where cn.locale=:locale order by cn.name");
				query.setString("locale", getLocaleForQuery(locale));
				return query.list();
			}
		});
	}
	
	/**
	 * @see org.gbif.portal.dao.geospatial.CountryDAO#getCountryAlphabet(java.util.Locale)
	 */
	@SuppressWarnings("unchecked")
	public List<Character> getCountryAlphabet(final Locale locale) {
		HibernateTemplate template = getHibernateTemplate();		
		List<String> results =  (List<String>) template.execute(new HibernateCallback() {
			public Object doInHibernate(Session session) {
				Query query = session.createSQLQuery("select distinct(SUBSTRING(name,1,1)) from country_name where locale=? and name!=' %' order by name");
				query.setParameter(0, getLocaleForQuery(locale));
				return query.list();
			}
		});		
		ArrayList<Character> chars = new ArrayList<Character>();
		for(String result:results){
			if(StringUtils.isNotEmpty(result))
				chars.add(new Character(result.charAt(0)));
		}
		return chars;
	}	
	
	/**
	 * @see org.gbif.portal.dao.taxonomy.TaxonConceptDAO#getCountryCountsForTaxonConcept(java.lang.Long)
	 */
	@SuppressWarnings("unchecked")
	public List<Object[]> getCountryCountsForTaxonConcept(final long taxonConceptId, final Locale locale) {
		HibernateTemplate template = getHibernateTemplate();
		return (List) template.execute(new HibernateCallback() {
			public Object doInHibernate(Session session) {
				SQLQuery query = session.createSQLQuery("select tc.iso_country_code the_iso_country_code, " +
						" tc.iso_country_code the_iso_country_code2, cn.name as cn_name, tc.count as the_count from taxon_country tc" +
						" inner join country_name cn on tc.iso_country_code=cn.iso_country_code" +
						" where tc.taxon_concept_id=:taxonConceptId and cn.locale=:locale order by cn_name");
				query.setParameter("taxonConceptId", taxonConceptId);
				query.setParameter("locale", getLocaleForQuery(locale));
				query.addScalar("the_iso_country_code", Hibernate.STRING);
				query.addScalar("the_iso_country_code2", Hibernate.STRING);
				query.addScalar("the_count", Hibernate.INTEGER);
				query.setCacheable(true);
				return query.list();
			}
		});	
	}	
	
	/**
	 * @see org.gbif.portal.dao.resources.DataResourceDAO#getCountryCountsForCountry(java.lang.Long)
	 */
	@SuppressWarnings("unchecked")
	public List<Object[]> getCountryCountsForCountry(final String isoCountryCode, final boolean geoRefOnly, final Locale locale) {
		HibernateTemplate template = getHibernateTemplate();
		return (List) template.execute(new HibernateCallback() {
			public Object doInHibernate(Session session) {
				StringBuffer sb = new StringBuffer("select dp.iso_country_code as the_iso_country_code, cn.name as cn_name,");
				if(geoRefOnly){
					sb.append("  sum(rc.occurrence_coordinate_count) as the_count from resource_country rc" +
							" inner join data_resource dr on rc.data_resource_id=dr.id " +
							" inner join data_provider dp on dr.data_provider_id=dp.id " +
							" inner join country_name cn on dp.iso_country_code=cn.iso_country_code" +
							" where ");
				} else {
					sb.append("  sum(rc.count) as the_count from resource_country rc" +
							" inner join data_resource dr on rc.data_resource_id=dr.id " +
							" inner join data_provider dp on dr.data_provider_id=dp.id " +
							" inner join country_name cn on dp.iso_country_code=cn.iso_country_code" +
							" where ");
				}
				if(isoCountryCode!=null){
					sb.append(" rc.iso_country_code=:isoCountryCode and cn.locale=:locale");
				}			
				
				if(geoRefOnly){
					sb.append(" and rc.occurrence_coordinate_count>0");
				}
				
				sb.append(" and dr.deleted is null");
				
				sb.append(" group by cn_name");
				SQLQuery query = session.createSQLQuery(sb.toString());
				if(isoCountryCode!=null) {
					query.setParameter("isoCountryCode", isoCountryCode);
					query.setParameter("locale", getLocaleForQuery(locale));
					query.addScalar("the_iso_country_code", Hibernate.STRING);
					query.addScalar("cn_name", Hibernate.STRING);
					query.addScalar("the_count", Hibernate.INTEGER);					
				}
				query.setCacheable(true);
				logger.debug("query is: " + sb.toString());
				return query.list();
			}
		});	
	}		

	/**
	 * Retrieves the locale using the default if the supplied locale is null or not
	 * supported.
	 * @param locale
	 * @return language code e.g. "en"
	 */
	@SuppressWarnings("unchecked")
	protected String getLocaleForQuery(Locale locale){
		if(supportedLocales==null){
			logger.debug("retrieving supported locales");
			supportedLocales = (List<String>) getHibernateTemplate().execute(new HibernateCallback() {
				public Object doInHibernate(final Session session) {
					//initialise supported locales
					final Query query = session.createQuery("select distinct cn.locale from CountryName cn");
					return query.list();
				}
			});
			if(logger.isDebugEnabled())
				logger.debug("Supported locales: "+ToStringBuilder.reflectionToString(supportedLocales));
		}
		if(locale!=null){
			if(supportedLocales.contains(locale.getLanguage()))
				return locale.getLanguage();
		}
		return defaultISOLanguageCode;
	}

	/**
	 * @see org.gbif.portal.dao.geospatial.CountryDAO#getTotalCountryCount()
	 */
	public int getTotalCountryCount() {
		Long count = (Long) getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session) {
				Query query = session.createQuery("select count(c.id) from Country c");
				return query.uniqueResult();
			}
		});
		return count.intValue();
	}

	/**
	 * @see org.gbif.portal.dao.geospatial.CountryDAO#getHostCountryISOCountryCodes()
	 */
	@SuppressWarnings("unchecked")
	public List getHostCountryISOCountryCodes() {
		return (List) getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session) {
				Query query = session.createQuery("select distinct isoCountryCode from DataProvider");
				return query.list();
			}
		});
	}

	/**
	 * @see org.gbif.portal.dao.geospatial.CountryDAO#getCountryForIP(java.lang.String, java.util.Locale)
	 */
	public Object getCountryForIP(final String ipAddress, final Locale locale) {
		return (Object) getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session) {
				String localeStr = getLocaleForQuery(locale);
				Query query = session.createQuery("select distinct c, cn from IPCountry ic inner join ic.country c inner join c.countryNames cn where ic.startLong<=:convertedIP and ic.endLong>=:convertedIP and cn.locale=:locale");
				query.setParameter("locale", localeStr);
				query.setLong("convertedIP", IPUtils.convertIPtoLong(ipAddress));				
				return query.uniqueResult();
			}
		});
	}

	/**
	 * @see org.gbif.portal.dao.geospatial.CountryDAO#getCountriesForRegion(java.lang.String, java.util.Locale)
	 */
	@SuppressWarnings("unchecked")
	public List<Object[]> getCountriesForRegion(final String regionCode, final Locale locale) {
		return (List<Object[]>) getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session) {
				Query query = session.createQuery("from Country c inner join c.countryNames cn where c.region=:regionCode and cn.locale=:locale");
				query.setString("regionCode", regionCode);
				query.setString("locale", getLocaleForQuery(locale));
				return query.list();
			}
		});	
	}
	
	
	/**
	 * @see org.gbif.portal.dao.geospatial.CountryDAO#getCountryList()
	 */
	public List getCountryList() {
		HibernateTemplate template = getHibernateTemplate();	
		List results =  (List) template.execute(new HibernateCallback() {
			public Object doInHibernate(Session session) {
				Query query = session.createQuery(
						"select c.isoCountryCode, cn.name " +
						" from Country c, CountryName cn" +
						" where c.isoCountryCode = cn.isoCountryCode and cn.locale='en' order by cn.name");			
				return query.list();
			}
		});			
		return results;
	}
	
/*
 * 
 * 
 * ("select c, cn.name from Country c inner join c.countryNames cn where (cn.name like :name ");
 * 
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

*/

	/**
	 * @param defaultISOLanguageCode the defaultISOLanguageCode to set
	 */
	public void setDefaultISOLanguageCode(String defaultISOLanguageCode) {
		this.defaultISOLanguageCode = defaultISOLanguageCode;
	}

	/**
	 * @param supportedLocales the supportedLocales to set
	 */
	public void setSupportedLocales(List<String> supportedLocales) {
		this.supportedLocales = supportedLocales;
	}

	/**
	 * @param characterIsoCodeMap the characterIsoCodeMap to set
	 */
	public void setCharacterIsoCodeMap(
			Map<Character, List<String>> characterIsoCodeMap) {
		this.characterIsoCodeMap = characterIsoCodeMap;
	}
}