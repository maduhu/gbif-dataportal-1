<%@ include file="/common/taglibs.jsp"%>
<c:if test="${fn:length(locations)>1}">
	<spring:message code="occurrence.record.geospatial.locations"/>
	<c:forEach items="${locations}" var="location" varStatus="status">
		<c:if test="${status.index>0}">,&nbsp;</c:if><a href="<c:url value='/occurrences/search.htm?'/><gbif:criterion subject='0' predicate='0' value='${occurrenceRecord.taxonConcept.taxonName.canonical}' index="0"/>&<gbif:criterion subject='5' predicate='0' value='${location.name}' index="1"/>">${location.name}</a>
	</c:forEach>
</c:if>