<%@ include file="/common/taglibs.jsp"%>
<div class="taxonomyFilterHelp">
<c:set var="addFilterMsg"><spring:message code="search.filter.add.filter"/></c:set>
<spring:message code="occurrence.commonName.help1" arguments="${addFilterMsg}" argumentSeparator="$$$"/>
<br/>
<br/>
<spring:message code="occurrence.commonName.help2"/>
</div>