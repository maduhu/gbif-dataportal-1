<%@ include file="/common/taglibs.jsp"%>
<c:set var="pointsTotal" value="${resourceNetwork.occurrenceCoordinateCount}" scope="request"/>
<c:set var="occurrenceCount" value="${resourceNetwork.occurrenceCount}" scope="request"/>
<c:set var="entityName" scope="request">${resourceNetwork.name}</c:set>
<c:set var="extraParams" scope="request"><gbif:criterion subject="26" predicate="0" value="${resourceNetwork.key}" index="0"/></c:set>
<tiles:insert name="overviewMap"/>
<tiles:insert name="resourceNetworkOverviewMap"/>