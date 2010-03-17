<%@ include file="/common/taglibs.jsp"%>
<%@ page language="java" pageEncoding="utf-8" contentType="text/html;charset=utf-8" %>
<%@ page import="java.util.*" %>
<%@ page import="javax.servlet.jsp.*" %>
<%@ page import="javax.servlet.jsp.jstl.core.*" %>
<%@ page import="org.apache.commons.lang.*" %>
<%@ page import="org.gbif.portal.web.filter.*" %>
<%@ page import="org.apache.taglibs.string.util.*" %>
<%@ page import="java.net.*" %>
<form id="filterSearchForm" name="filterSearch" method="get" action="${pageContext.request.contextPath}${filterAction}">
<%
		List<FilterDTO> filters = (List<FilterDTO>) request.getAttribute("filters");
		CriteriaDTO criteriaDTO = (CriteriaDTO) request.getAttribute("criteria");		
		List<CriterionDTO> criteria = criteriaDTO.getCriteria();
		// Added to encode and decode criteria values
		if(request.getCharacterEncoding() == null) {
			for(CriterionDTO criterionDTO : criteria) {
				criterionDTO.setValue(URLEncoder.encode(criterionDTO.getValue(), "ISO-8859-1"));
				criterionDTO.setValue(URLDecoder.decode(criterionDTO.getValue(), "UTF-8"));
				criterionDTO.setDisplayValue(URLEncoder.encode(criterionDTO.getDisplayValue(), "ISO-8859-1"));
				criterionDTO.setDisplayValue(URLDecoder.decode(criterionDTO.getDisplayValue(), "UTF-8"));
			}
		}
		// End
		pageContext.setAttribute("criteria", criteria);
%>
<h4 id="currentSearch"><spring:message code="search.filter.currentsearch.title"/></h4>
<p class="emptySearch">
<spring:message code="search.filter.minus.signs" text="Filters can be removed by clicking on the minus signs."/>
</p>

<c:forEach items="${criteria}" var="criterion" varStatus="criterionStatus">
	<%
		CriterionDTO criterionDTO = (CriterionDTO) pageContext.getAttribute("criterion");
		FilterDTO filterDTO = FilterUtils.getFilterById(filters, criterionDTO.getSubject());					
		pageContext.setAttribute("filter", filterDTO);
		pageContext.setAttribute("predicate", filterDTO.getPredicates().get(new Integer(criterionDTO.getPredicate())));		
	%>	
	<c:choose>
		<c:when test="${filter.categoryId == 0}"><c:set var="filterClass">taxonomy</c:set></c:when>
		<c:when test="${filter.categoryId == 1}"><c:set var="filterClass">geospatial</c:set></c:when>
		<c:when test="${filter.categoryId == 2}"><c:set var="filterClass">datasets</c:set></c:when>			
	</c:choose>	
	<c:if test="${lastCriterion==null}"><ul class="${filterClass}"></c:if>
	<c:if test="${lastCriterion!=null && lastCriterion.subject!= criterion.subject}"></ul><ul class="${filterClass}"></c:if>
	<li<c:if test="${criterionStatus.index==filterIndex}"> class="alteredFilter"</c:if>>
		<c:choose>
		<c:when test="${lastCriterion==null || (lastCriterion!=null && lastCriterion.subject!=criterion.subject) }">
			<spring:message code="${filter.displayName}"/>
		</c:when>
		<c:otherwise>
			<span class="secondCondition"><spring:message code="${filter.i18nMultipleConditionKey}"/></span>
		</c:otherwise>
		</c:choose>
		<spring:message code="${predicate.value}"/>
		<spring:message code="${criterion.displayValue}" text="${criterion.displayValue}"/> 
		<a href="javascript:removeConstructedFilter(${criterionStatus.index});"
			 title="<spring:message code="search.filter.removefilter"/>"><img alt="<spring:message code="search.filter.removefilter"/>" src="${pageContext.request.contextPath}/images/filter/grey_minus.png"/></a>
		<input name="c[${criterionStatus.index}].s" type="hidden" value="${criterion.subject}"/>
		<input name="c[${criterionStatus.index}].p" type="hidden" value="${criterion.predicate}"/>
		<input name="c[${criterionStatus.index}].o" type="hidden" value="${criterion.value}"/>
	</li>
	<c:set var="lastCriterion" value="${criterion}" />
</c:forEach></ul>
	<input id="filterSearchSubmit" type="submit" value="<spring:message code="search"/>"/>
</form>
<div id="hiddenCriteria" style="visibility:hidden;position:absolute; float:left;">
<c:forEach items="${criteria}" var="criterion" varStatus="criterionStatus">
	<input name="c[${criterionStatus.index}]" type="hidden" value="c[${criterionStatus.index}].s=${criterion.subject}&c[${criterionStatus.index}].p=${criterion.predicate}&c[${criterionStatus.index}].o=${criterion.value}"/>
</c:forEach>
</div>