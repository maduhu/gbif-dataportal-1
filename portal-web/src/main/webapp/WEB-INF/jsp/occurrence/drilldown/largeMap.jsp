<%@ include file="/common/taglibs.jsp"%>
<tiles:insert page="occurrenceHeader.jsp"/>
<ul class="genericList">
<li><a href="${pageContext.request.contextPath}/occurrences/${occurrenceRecord.key}"><spring:message code="occurrence.record.back.to.details"/></a></li>
<li><a href="${pageContext.request.contextPath}/occurrences/occurrence-${occurrenceRecord.key}.kml"><spring:message code="occurrence.record.geospatial.google.earth"/></a></li>
</ul>

<c:set var="mapDivName" value="largeMap" scope="request"/>
<c:set var="pointsClickable" value="false" scope="request"/>
<tiles:insert page="/WEB-INF/jsp/geography/googleMap.jsp"/>