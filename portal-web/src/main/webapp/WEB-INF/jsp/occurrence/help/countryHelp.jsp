<%@ include file="/common/taglibs.jsp"%>
<div class="geospatialFilterHelp">
<p>
<c:set var="addFilterMsg"><spring:message code="search.filter.add.filter"/></c:set>
<spring:message code="occurrence.country.help1" arguments="${addFilterMsg}" argumentSeparator="$$$"/>
</p>
<p>
<spring:message code="occurrence.country.help2"/>
</p>
</div>