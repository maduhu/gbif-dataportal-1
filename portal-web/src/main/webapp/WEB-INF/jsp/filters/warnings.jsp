<%@ include file="/common/taglibs.jsp"%>
<c:if test="${not empty filterWarnings || not empty fatalFilterWarnings}">
<div id="warnings">
	<h4><spring:message code="filter.search.warnings" text="Warnings"/></h4>
	<ul>
	<c:forEach items="${fatalFilterWarnings}" var="filterWarning">
		<li>${filterWarning}</li>
	</c:forEach>
	<c:forEach items="${filterWarnings}" var="filterWarning">
		<li>${filterWarning}</li>
	</c:forEach>
	</ul>	
</div>
</c:if>