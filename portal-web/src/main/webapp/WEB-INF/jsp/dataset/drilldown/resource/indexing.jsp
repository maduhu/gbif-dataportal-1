<%@ include file="/common/taglibs.jsp"%>
<h4><spring:message code="indexed.data"/></h4>
<c:choose>
	<c:when test="${dataResource.occurrenceCount>0}">
		<table class="smallStatistics">
		<tr><td class="property"><spring:message code="occurrences.indexed"/>:</td><td><fmt:formatNumber value="${dataResource.occurrenceCount}" pattern="###,###"/></td></tr>
		<tr><td class="property"><spring:message code="provider.record.count"/>:</td><td><fmt:formatNumber value="${dataResource.providerRecordCount}" pattern="###,###"/></td></tr>
		<tr><td class="property"><spring:message code="occurrences.georeferenced"/>:</td><td><fmt:formatNumber value="${dataResource.occurrenceCoordinateCount}" pattern="###,###"/></td></tr>
		<tr><td class="property"><spring:message code="occurrences.no.geo.issues"/>:</td><td><fmt:formatNumber value="${dataResource.occurrenceCleanGeospatialCount}" pattern="###,###"/></td></tr>
		<tr><td class="property"><spring:message code="count.species"/>:</td><td><fmt:formatNumber value="${dataResource.speciesCount}" pattern="###,###"/></td></tr>
		<tr><td class="property"><spring:message code="count.taxa"/>:</td><td><fmt:formatNumber value="${dataResource.conceptCount}" pattern="###,###"/></td></tr>
		</table>
		<ul class="genericList">
			<li>
				<a href="${pageContext.request.contextPath}/datasets/resource/${dataResource.key}/logs/"><spring:message code="view.event.logs.for" text="View event logs for"/> ${dataResource.name}</a>
			</li>
			<li>
				<span class="new">New!</span> <a href="${pageContext.request.contextPath}/datasets/resource/${dataResource.key}/indexing/"><spring:message code="view.indexing.history.for" text="View indexing history for"/> ${dataResource.name}</a>
			</li>
		</ul>
	</c:when>
	<c:otherwise>
	
		<table class="smallStatistics">
		<tr><td colspan="2"><spring:message code="dataset.resource.no.records.indexed.yet"/></td></tr>
		<c:if test="${dataResource.providerRecordCount>0}">
			<tr><td class="property"><spring:message code="provider.record.count"/>:</td><td><fmt:formatNumber value="${dataResource.providerRecordCount}" pattern="###,###"/></td></tr>
		</c:if>
		</table>
	</c:otherwise>
</c:choose>	

