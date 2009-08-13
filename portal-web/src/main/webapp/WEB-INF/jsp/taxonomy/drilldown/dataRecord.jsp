<%@ include file="/common/taglibs.jsp"%>
<c:if test="${not empty regionCounts}">
	<div class="subcontainer">
		<h4><spring:message code="occurrence.data.by.region" text="Occurrences by region"/></h4>							
		<table class="results" style="width:33%">
			<thead>
				<tr><th><spring:message code="occurrence.record.region"/></th><th class="lastColumn"><spring:message code="log.console.count"/></th></tr>
			</thead>	
			<tbody>
				<c:forEach items="${regionCounts}" var="regionCount">
				<tr>
					<td><spring:message code="region.${regionCount.key}" text="${countryCount.key}"/></td><td class="lastColumn"><fmt:formatNumber value="${regionCount.count}" pattern="###,###"/></td>
				</tr>
				</c:forEach>
			</tbody>
		</table>		
	</div><!-- charts sub container -->
</c:if>					