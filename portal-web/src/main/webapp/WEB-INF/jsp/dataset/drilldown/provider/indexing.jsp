<%@ include file="/common/taglibs.jsp"%>
<h4><spring:message code="indexed.data"/></h4>
<div class="subcontainer">
<c:choose>
	<c:when test="${dataProvider.occurrenceCount>0}">
		<table class="smallStatistics">
		<tr><td class="property"><spring:message code="occurrences.resources" text="Data resources"/>:</td><td><fmt:formatNumber value="${dataProvider.dataResourceCount}" pattern="###,###"/></td></tr>
		<tr><td class="property"><spring:message code="occurrences.indexed"/>:</td><td><fmt:formatNumber value="${dataProvider.occurrenceCount}" pattern="###,###"/></td></tr>
		<tr><td class="property"><spring:message code="occurrences.georeferenced"/>:</td><td><fmt:formatNumber value="${dataProvider.occurrenceCoordinateCount}" pattern="###,###"/></td></tr>
		</table>
		<ul class="genericList">
			<li>
				<a href="${pageContext.request.contextPath}/datasets/provider/${dataProvider.key}/logs/"><spring:message code="view.event.logs.for" text="View event logs for"/> ${dataProvider.name}</a>
			</li>
			<li>
				<span class="new">New!</span> <a href="${pageContext.request.contextPath}/datasets/provider/${dataProvider.key}/indexing/"><spring:message code="view.event.logs.for" text="View indexing history for"/> ${dataProvider.name}</a>
			</li>
		</ul>
	</c:when>
	<c:otherwise>
		<p>
		<spring:message code="dataset.provider.no.records.indexed.yet"/>
		</p>
	</c:otherwise>
</c:choose>
</div>