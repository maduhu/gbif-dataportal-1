<%@ include file="/common/taglibs.jsp"%>
<div class="otherDetailsFilterHelp">
<c:set var="addFilterMsg"><spring:message code="search.filter.add.filter"/></c:set>
<spring:message code="occurrence.month.help" arguments="${addFilterMsg}" argumentSeparator="$$$"/>
</div>