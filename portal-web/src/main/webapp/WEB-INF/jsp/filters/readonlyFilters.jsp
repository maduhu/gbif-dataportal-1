<%@ include file="/common/taglibs.jsp"%>
<%@ page import="java.util.*" %>
<%@ page import="javax.servlet.jsp.*" %>
<%@ page import="javax.servlet.jsp.jstl.core.*" %>
<%@ page import="org.apache.commons.lang.*" %>
<%@ page import="org.gbif.portal.web.filter.*" %>
<%@ page import="org.apache.taglibs.string.util.*" %>
<%
		List<FilterDTO> filters = (List<FilterDTO>) request.getAttribute("filters");
		CriteriaDTO criteriaDTO = (CriteriaDTO) request.getAttribute("criteria");		
		List<CriterionDTO> criteria = criteriaDTO.getCriteria();
		pageContext.setAttribute("criteria", criteria);		 
%>
<h4 id="currentSearch"><spring:message code="occurrence.search.filter.currentsearch.title"/></h4>
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
	<li>
		<c:choose>
		<c:when test="${lastCriterion==null || (lastCriterion!=null && lastCriterion.subject!=criterion.subject) }">
			<spring:message code="${filter.displayName}"/>
		</c:when>
		<c:otherwise>
			<span class="secondCondition"><spring:message code="filter.or"/></span>
		</c:otherwise>
		</c:choose>
		<spring:message code="${predicate.value}"/>
		<spring:message code="${criterion.displayValue}" text="${criterion.displayValue}"/>
	</li>
	<c:set var="lastCriterion" value="${criterion}"/>
</c:forEach></ul>
<br/>
<div id="changeSearch">
<a href="search.htm?<gbif:criteria criteria="${criteria}"/>"><string:trim>
	<spring:message code="search.filter.action.changesearch" text="Change your search"/>
</string:trim></a>
</div>