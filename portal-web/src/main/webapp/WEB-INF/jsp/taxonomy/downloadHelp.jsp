<%@ include file="/common/taglibs.jsp"%>
<p>
<spring:message code="occurrence.record.download.your.query"/>
<span id="queryDescription"><tiles:insert page="/WEB-INF/jsp/filters/filterQuery.jsp"/></span>
</p>
<!--
<p id="citationDownload">
<spring:message code="occurrence.record.download.citation"/> 
<a href="${pageContext.request.contextPath}/occurrences/occurrence-search-citation-${searchId}.txt?format=citation&criteria=<gbif:criteria criteria="${criteria}" urlEncode="true"/>"><spring:message code="occurrence.record.download.citation.here"/></a>.
</p>
-->