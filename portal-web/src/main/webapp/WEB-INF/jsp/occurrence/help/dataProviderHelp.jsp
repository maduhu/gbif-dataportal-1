<%@ include file="/common/taglibs.jsp"%>
<div class="datasetFilterHelp">
<c:set var="addFilterMsg"><spring:message code="search.filter.add.filter"/></c:set>
<spring:message code="occurrence.dataprovider.help" arguments="${addFilterMsg}" argumentSeparator="$$$"/>
</div>