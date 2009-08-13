<%@ include file="/common/taglibs.jsp"%>
<c:set var="pointsTotal" value="${dataResource.occurrenceCoordinateCount}" scope="request"/>
<c:set var="occurrenceCount" value="${dataResource.occurrenceCount}" scope="request"/>
<c:set var="entityName" scope="request">${dataResource.name}</c:set>
<c:set var="extraParams" scope="request"><gbif:criterion subject="24" predicate="0" value="${dataResource.key}" index="0"/></c:set>
<tiles:insert name="overviewMap"/>
<tiles:insert name="resourceOverviewMap"/>