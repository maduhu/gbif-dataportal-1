<%@ include file="/common/taglibs.jsp"%>
<c:set var="pointsTotal" value="${dataProvider.occurrenceCoordinateCount}" scope="request"/>
<c:set var="occurrenceCount" value="${dataProvider.occurrenceCount}" scope="request"/>
<c:set var="entityName" scope="request">${dataProvider.name}</c:set>
<c:set var="extraParams" scope="request"><gbif:criterion subject="25" predicate="0" value="${dataProvider.key}" index="0"/></c:set>
<tiles:insert name="overviewMap"/>
<tiles:insert name="providerOverviewMap"/>