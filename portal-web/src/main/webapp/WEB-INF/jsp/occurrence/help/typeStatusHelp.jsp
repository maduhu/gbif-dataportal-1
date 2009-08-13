<%@ include file="/common/taglibs.jsp"%>
<div class="taxonomyFilterHelp">
<c:set var="addFilterMsg"><spring:message code="search.filter.add.filter"/></c:set>
<spring:message code="occurrence.typeStatus.help" arguments="${addFilterMsg}" argumentSeparator="$$$"/>
</div>