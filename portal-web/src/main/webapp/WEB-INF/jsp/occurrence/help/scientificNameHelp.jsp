<%@ include file="/common/taglibs.jsp"%>
<div class="taxonomyFilterHelp">
<c:set var="addFilterMsg"><spring:message code="search.filter.add.filter"/></c:set>
<p>
<spring:message code="occurrence.scientificName.help1" arguments="${addFilterMsg}" argumentSeparator="$$$"/>
</p>
<p>
<spring:message code="occurrence.scientificName.help2"/>
</p>
</div>