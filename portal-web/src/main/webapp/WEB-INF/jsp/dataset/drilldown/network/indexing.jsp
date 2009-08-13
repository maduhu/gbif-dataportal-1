<%@ include file="/common/taglibs.jsp"%>
<h4><spring:message code="indexed.data"/></h4>
<table class="smallStatistics">
<tr><td class="property"><spring:message code="occurrences.resources"/></td><td><fmt:formatNumber value="${resourceNetwork.dataResourceCount}" pattern="###,###"/></td></tr>
<tr><td class="property"><spring:message code="occurrences.indexed"/></td><td><fmt:formatNumber value="${resourceNetwork.occurrenceCount}" pattern="###,###"/></td></tr>
<tr><td class="property"><spring:message code="occurrences.georeferenced"/></td><td><fmt:formatNumber value="${resourceNetwork.occurrenceCoordinateCount}" pattern="###,###"/></td></tr>
</table>