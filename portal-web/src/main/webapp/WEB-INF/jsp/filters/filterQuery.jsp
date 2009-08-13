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
<string:trim>
	<%	CriterionDTO lastCriterion = null;  %>
	<c:forEach items="${criteria}" var="criterion" varStatus="criterionStatus"><string:trim>
		<%
			CriterionDTO criterionDTO = (CriterionDTO) pageContext.getAttribute("criterion");
			FilterDTO filterDTO = FilterUtils.getFilterById(filters, criterionDTO.getSubject());					
			pageContext.setAttribute("filter", filterDTO);
			pageContext.setAttribute("predicate", filterDTO.getPredicates().get(new Integer(criterionDTO.getPredicate())));		
		%></string:trim>	
		<c:if test="${lastValue!=null}"> ${lastValue}</c:if>
		<c:choose>
		<c:when test="${lastCriterion!=null && lastCriterion.subject!= criterion.subject}">, </c:when>
		<c:otherwise> </c:otherwise>
		</c:choose>		
		<string:trim>			
		<c:choose>
		<c:when test="${lastCriterion==null || ( lastCriterion!=null && lastCriterion.subject!= criterion.subject) }">
			<spring:message code="${filter.displayName}"/> <spring:message code="${predicate.value}"/>
		</c:when>
		<c:otherwise> 
      <spring:message code="filter.or"/> <spring:message code="${predicate.value}"/>
		</c:otherwise>
		</c:choose>
		<c:set var="lastValue"><spring:message code="${criterion.displayValue}" text="${criterion.displayValue}"/></c:set>
		<c:set var="lastCriterion" value="${criterion}"/>
		</string:trim>
	</c:forEach>
	<c:if test="${lastValue!=null}"> ${lastValue}</c:if>
</string:trim>