<%@ include file="/common/taglibs.jsp"%>Taxonomy Search
<c:if test="${resultSize>0}">
	- 	
	<c:choose>
		<c:when test="${! resultSizeIsExact}">
		<spring:message code="occurrence.search.filter.max.results.exceeded">
			  <fmt:param value="${ resultSize-1} "/>
		</spring:message>
		</c:when>	
		<c:otherwise >
			<spring:message code="occurrence.search.filter.noofrecords"/> ${resultSize}
			<c:choose>
				<c:when test="${fn:length(results.list)==1}">
					<spring:message code="occurrence.search.filter.singularrecord"/>
				</c:when>
				<c:otherwise >
					<spring:message code="occurrence.search.filter.pluralrecord"/>
				</c:otherwise>
			</c:choose>
		</c:otherwise>
	</c:choose>
</c:if>	