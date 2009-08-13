<%@ include file="/common/taglibs.jsp"%>
<div class="geospatialFilterHelp">
<c:set var="addFilterMsg"><spring:message code="search.filter.add.filter"/></c:set>
<spring:message code="occurrence.region.help" arguments="${addFilterMsg}" argumentSeparator="$$$"/>
</div>