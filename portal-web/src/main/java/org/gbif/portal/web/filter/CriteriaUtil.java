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
package org.gbif.portal.web.filter;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.gbif.portal.web.util.QueryHelper;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.PropertyAccessorUtils;
import org.springframework.beans.PropertyValue;
import org.springframework.web.bind.ServletRequestParameterPropertyValues;
import org.springframework.web.servlet.support.RequestContextUtils;

/**
 * Utility class for creating and manipulating Criteria.
 *
 * @author dmartin
 * 
 * @see CriterionDTO
 * @see CriteriaDTO
 */
public class CriteriaUtil {
	
	private static Log logger = LogFactory.getLog(CriteriaUtil.class);	
	//this is the configured command object of the FilterController - need to remove from here 
	public static final String CRITERIA = "c";
	public static final String SUBJECT = "s";
	public static final String PREDICATE = "p";
	public static final String OBJECT = "o";	
	/** The match all request parameter */
	public static final String MATCH_ALL_PARAM = "matchAll";

	/**
	 * Constructs a url of the form "criteria[0].subject=&lt;subject&gt;...."
	 * @param criteria
	 * @return
	 */
	public static String getUrl(CriteriaDTO criteria){	
		return getUrlFromList(criteria.getCriteria());
	}
	
	/**
	 * Constructs a url of the form "criteria[0].subject=&lt;subject&gt;...."
	 * @param criteria
	 * @return
	 */
	public static String getUrlFromList(List<CriterionDTO> criteria){
		
		StringBuffer sb = new StringBuffer();
		int index=0;
		
		for (CriterionDTO criterion: criteria){
			if(sb.length()>0)
				sb.append('&');
			sb.append(getUrl(criterion.getSubject(), criterion.getPredicate(), criterion.getValue(), index));
			index++;
		}
		return sb.toString();
	}

	/**
	 * Create a portion of a URL for filter links
	 * @param subject
	 * @param predicate
	 * @param value
	 * @param index
	 * @return a url of the form "criteria[0].subject=&lt;subject&gt;...."
	 */
	public static String getUrl(String subject, String predicate, String value, int index){
		StringBuffer sb = new StringBuffer();
		appendElement(sb, index, SUBJECT , subject);
		appendElement(sb, index, PREDICATE, predicate);
		appendElement(sb, index, OBJECT, value);			
		return sb.toString();
	}	

	/**
	 * Retrieves a CriteriaDTO from this request.
	 * @param request
	 * @param filters the filters to use to populate the criteria
	 * @param locale the locale to use to retrieve locale specific values for display, nullable
	 * @return CriteriaDTO for the criteria in the supplied request
	 */
	public static CriteriaDTO getCriteria(String criteriaString, List<FilterDTO> filters, Locale locale){
		logger.debug(criteriaString);
		if(StringUtils.isEmpty(criteriaString))
			return null;
		criteriaString = criteriaString.trim();
		StringTokenizer stringTokenizer = new StringTokenizer(criteriaString, "&");
		ArrayList<PropertyValue> propertyValues = new ArrayList<PropertyValue>();
		while(stringTokenizer.hasMoreTokens()){
			String triplet = stringTokenizer.nextToken();
			int equalsIdx = triplet.indexOf('=');
			if(equalsIdx>0){
				String property = triplet.substring(0,equalsIdx);
				String value = triplet.substring(equalsIdx+1);
				propertyValues.add(new PropertyValue(property, value));
			}
		}
		return getCriteria(propertyValues.toArray(new PropertyValue[propertyValues.size()]), filters, locale);
	}
	
	/**
	 * Retrieves a CriteriaDTO from this request.
	 * @param request
	 * @param filters the filters to use to populate the criteria
	 * @return CriteriaDTO for the criteria in the supplied request
	 */
	public static CriteriaDTO getCriteria(HttpServletRequest request, List<FilterDTO> filters){
		if(logger.isDebugEnabled())
			logger.debug("criteria string:"+request.getQueryString());
		//find the number of criteria set in the request
		MutablePropertyValues mpvs = new ServletRequestParameterPropertyValues(request);
		PropertyValue[] pvs = mpvs.getPropertyValues();
		Locale locale = RequestContextUtils.getLocale(request);
		return getCriteria(pvs, filters, locale);
	}
	
	/**
	 * Retrieves a CriteriaDTO from this request.
	 * @param request
	 * @return CriteriaDTO for the criteria in the supplied request
	 */
	@SuppressWarnings("unchecked")
	public static CriteriaDTO getCriteria(PropertyValue[] pvs, List<FilterDTO> filters, Locale locale){	
		CriteriaDTO criteriaDTO = new CriteriaDTO();
    	List<CriterionDTO> criteria = criteriaDTO.getCriteria();
		String subject = null;
		String predicate= null;
		String value = null;

		Integer currentCriterionIndex = null;
		
		for (PropertyValue pv:  pvs){
			String propertyPath = pv.getName();
			Object propertyValue = pv.getValue();
			
			if(logger.isDebugEnabled()){
				logger.debug("Property: name = "+propertyPath+"; value = "+propertyValue);
			}

			//get current criterion index
			Integer criterionIndex = null;
			int beginIndex = propertyPath.indexOf('[');
			int endIndex = propertyPath.indexOf(']');
			
			if(beginIndex>0 && endIndex>0){
				try{
					criterionIndex = Integer.parseInt(propertyPath.substring(beginIndex+1, endIndex));
				} catch (NumberFormatException e){
					logger.debug("badly formed criteria string:" +pvs);
					continue;
				}
			}
			
			//set if not set
			if(currentCriterionIndex==null){
				currentCriterionIndex = criterionIndex;
			} else if(!currentCriterionIndex.equals(criterionIndex)) {
				if(logger.isDebugEnabled())
					logger.debug("Ignoring incomplete criterion - index: "+criterionIndex);
				//ignore the in complete criterion
				subject = predicate = value = null;
				currentCriterionIndex = criterionIndex;
			}
			
			int pos = PropertyAccessorUtils.getFirstNestedPropertySeparatorIndex(propertyPath);
			// handle nested properties recursively
			if (pos!=-1 && propertyPath.length() > pos+1 ){
				if (propertyPath.startsWith(CRITERIA)){
					String propertyName = propertyPath.substring(propertyPath.indexOf('.')+1);
					if(logger.isDebugEnabled())
						logger.debug("propertyName = "+propertyName);		
					
					if(SUBJECT.equals(propertyName))
						subject = (String) propertyValue;
					if(PREDICATE.equals(propertyName))
						predicate = (String) propertyValue;
					if(OBJECT.equals(propertyName)){
						value = (String) propertyValue;
						value = QueryHelper.tidyValue(value);
					}
					//if all 3 properties are non null create a criterion
					if (subject!=null && predicate!=null && StringUtils.isNotEmpty(value)){
			        	criteria.add(new CriterionDTO(subject, predicate, value));						
						subject = predicate = value = null;
						currentCriterionIndex = null;
					}
				}
			}
		}	
		
		//remove duplicates - start from the front	
		List<CriterionDTO> noDuplicates = removeDuplicates(criteria);
		//sort by category
		criteriaDTO.setCriteria(noDuplicates);
		//set group ids
		setCriteriaGroupValues(filters, criteriaDTO);
		//set display values
		setCriteriaDisplayValues(filters, criteriaDTO, locale);
		//order by subject and value
		orderByCategorySubjectAndValue(criteriaDTO);
		return criteriaDTO;			
	}

	/**
	 * Orders the criteria by category, subject and then value.
	 * 
	 * @param noDuplicates
	 */
	public static void orderByCategorySubjectAndValue(List<CriterionDTO> noDuplicates) {
		Collections.sort(noDuplicates, new Comparator<CriterionDTO>(){
			public int compare(CriterionDTO o1, CriterionDTO o2) {
				if(o1.getGroup()!=null && o2.getGroup()!=null && !o1.getGroup().equals(o2.getGroup()))
					return o1.getGroup().compareTo(o2.getGroup());
				if(o1.getSubject().equals(o2.getSubject()) && o1.getValue()!=null && o2.getValue()!=null)
						return o1.getValue().compareTo(o2.getValue());
				return o1.getSubject().compareTo(o2.getSubject());
			}
		});
	}

	/**
	 * Remove duplicate criterions.
	 * @param criteria
	 * @return
	 */
	public static List<CriterionDTO> removeDuplicates(List<CriterionDTO> criteria) {
		List<CriterionDTO> noDuplicates = new LinkedList<CriterionDTO>();
		for (CriterionDTO criterionDTO: criteria){
			if (!noDuplicates.contains(criterionDTO))
				noDuplicates.add(criterionDTO);
		}
		return noDuplicates;
	}
	
	/**
	 * Set the display value for the criteria.
	 * @param filters
	 * @param criteria
	 */
	public static void setCriteriaDisplayValues(List<FilterDTO> filters, CriteriaDTO criteria, Locale locale) {
		for (CriterionDTO criterion:criteria.getCriteria()){
			setCriterionDisplayValue(filters, criterion, locale);
		}
	}

	/**
	 * Set the display value for the criterion supplied.
	 * @param filters
	 * @param criterion
	 */
	public static void setCriterionDisplayValue(List<FilterDTO> filters, CriterionDTO criterion, Locale locale) {
		if(criterion.getDisplayValue()!=null)
			return;
		FilterDTO theFilter = FilterUtils.getFilterById(filters, criterion.getSubject());
		//todo add a pretty print thingy for wizard based filters
		if (theFilter.getFilterType()==FilterDTO.COMBO_FILTER){
			if(theFilter.getDropDownValues()==null && theFilter.getPicklistHelper()!=null){
				theFilter.setDropDownValues(theFilter.getPicklistHelper().getPicklist(null, locale));
			}
			if(theFilter.getDropDownValues()!=null)
				criterion.setDisplayValue(theFilter.getDropDownValues().get(criterion.getValue()));
		} else if(theFilter.getFilterHelper()!=null){
			criterion.setDisplayValue(theFilter.getFilterHelper().getDisplayValue(criterion.getValue(), locale));
		} else {
			criterion.setDisplayValue(criterion.getValue());
		}
	}

	/**
	 * Set the group id values on the criterion.
	 * @param filters
	 * @param criteria
	 */
	public static void setCriteriaGroupValues(List<FilterDTO> filters, CriteriaDTO criteria) {
		List<CriterionDTO> criterions = criteria.getCriteria();
		for(CriterionDTO criterion:criterions){
			FilterDTO filter = FilterUtils.getFilterById(filters, criterion.getSubject());
			criterion.setGroup(filter.getCategoryId());
		}
	}

	/**
	 * Grouping of utilities to populate a set of criteria from a request
	 * @param filters
	 * @param criteria
	 */
	@SuppressWarnings("unchecked")
	public static CriteriaDTO getCriteriaAndPopulate(HttpServletRequest request, List<FilterDTO> filters) {
		CriteriaDTO criteria  = CriteriaUtil.getCriteria(request, filters);
		CriteriaUtil.checkFilterConstraints(filters, criteria);
		Locale locale = RequestContextUtils.getLocale(request);
		CriteriaUtil.setCriteriaDisplayValues(filters, criteria, locale);
		CriteriaUtil.setCriteriaGroupValues(filters, criteria);
		CriteriaUtil.orderByCategorySubjectAndValue(criteria);
		return criteria;
	}

	/**
	 * Grouping of utilities to populate a set of criteria from a request
	 * @param filters
	 * @param criteria
	 */
	@SuppressWarnings("unchecked")
	public static void populateCriteria(List<FilterDTO> filters, CriteriaDTO criteria, Locale locale) {
		CriteriaUtil.checkFilterConstraints(filters, criteria);
		CriteriaUtil.setCriteriaDisplayValues(filters, criteria, locale);
		CriteriaUtil.setCriteriaGroupValues(filters, criteria);
		CriteriaUtil.orderByCategorySubjectAndValue(criteria);
	}	
	
	/**
	 * Check filter constraints.
	 * @param filters
	 * @param criteria
	 */
	public static void checkFilterConstraints(List<FilterDTO> filters, CriteriaDTO criteria) {
		List<CriterionDTO> processedCriteria = new ArrayList<CriterionDTO>();
		List<CriterionDTO> currentCriteria = criteria.getCriteria();
		int size =currentCriteria.size();
		for (int i=0; i<size; i++){
			 CriterionDTO criterion = currentCriteria.get(i);
			FilterDTO filter = FilterUtils.getFilterById(filters,criterion.getSubject());
			if(!filter.isAllowMultiple()){
				//use the last one added remove any others
				boolean anotherExists = false;
				for(int j=i+1; j<size; j++){
					if(currentCriteria.get(j).getSubject().equals(criterion.getSubject())){
						anotherExists=true;
						break;
					}
				}
				if(!anotherExists)
					processedCriteria.add(criterion);
			} else {
				processedCriteria.add(criterion);
			}
		}
		criteria.setCriteria(processedCriteria);
	}
	
	private static void appendElement(StringBuffer sb, int index, String param, String value){
		if (sb.length()>0)
			sb.append("&");
		sb.append(CRITERIA);
		sb.append("[");
		sb.append(index);
		sb.append("].");
		sb.append(param);
		sb.append('=');
		sb.append(value);	
	}
	
	/**
	 * Fix the criteria values to can be understood by the database. Usually when one of them contain
	 * characters with accents.
	 * 
	 * @param request
	 * @param criteria
	 * @throws UnsupportedEncodingException
	 */
	public static void fixEncoding(HttpServletRequest request, CriteriaDTO criteria) throws UnsupportedEncodingException {
		if(request.getCharacterEncoding() != "UTF-8") {
			for(int c = 0; c < criteria.size(); c++) {
				criteria.get(c).setValue(URLEncoder.encode(criteria.get(c).getValue(), "ISO-8859-1"));
				criteria.get(c).setValue(URLDecoder.decode(criteria.get(c).getValue(), "UTF-8"));
				criteria.get(c).setDisplayValue(URLEncoder.encode(criteria.get(c).getDisplayValue(), "ISO-8859-1"));
				criteria.get(c).setDisplayValue(URLDecoder.decode(criteria.get(c).getDisplayValue(), "UTF-8"));
				request.setCharacterEncoding("UTF-8");
			}
		}
	}
}