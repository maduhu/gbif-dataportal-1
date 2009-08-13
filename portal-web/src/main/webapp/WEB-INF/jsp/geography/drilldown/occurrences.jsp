<%@ include file="/common/taglibs.jsp"%>
<script type="text/javascript">
	var criteria = "<gbif:criterion subject="5" predicate="0" value="${country.isoCountryCode}" index="0"/>";
</script>
<c:set var="pointsTotal" value="${country.occurrenceCoordinateCount}" scope="request"/>
<c:set var="occurrenceCount" value="${country.occurrenceCount}" scope="request"/>
<c:set var="entityName" scope="request"><gbif:capitalize><spring:message code="country.${country.isoCountryCode}"/></gbif:capitalize></c:set>
<c:set var="extraParams" scope="request"><gbif:criterion subject="5" predicate="0" value="${country.isoCountryCode}" index="0"/></c:set>
<tiles:insert name="overviewMap"/>
<tiles:insert name="geographyOverviewMap"/>