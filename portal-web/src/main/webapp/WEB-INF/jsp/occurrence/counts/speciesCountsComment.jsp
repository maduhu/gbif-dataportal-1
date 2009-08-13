<%@ include file="/common/taglibs.jsp"%>
<h4>
	<spring:message code="occurrence.search.filter.results.countsheader.species"/>			
</h4>
<p>
<c:set var="a0">
	<spring:message code="occurrence.search.filter.results.countsmessage.species.justthis"/>
</c:set>
<spring:message code="occurrence.search.filter.results.countsmessage.species" arguments="${a0}"/>
</p>
