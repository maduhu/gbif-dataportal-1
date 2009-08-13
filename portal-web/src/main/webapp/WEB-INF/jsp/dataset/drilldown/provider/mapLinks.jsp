<%@ include file="/common/taglibs.jsp"%>
<tiles:insert page="/WEB-INF/jsp/geography/mapPointCount.jsp"/>
<c:set var="entityKey" value="${dataProvider.key}" scope="request"/>
<c:if test="${dataProvider.occurrenceCoordinateCount>0}">
	<spring:message code="maps.records.provider.composite"/><br/>
	<c:if test="${zoom >1}">
		<ul class="overviewMapLinks">
			<li>
				<c:set var="a0"><span class="subject">${dataProvider.name}</span></c:set>
				<c:set var="a1"><gbiftag:boundingBox/></c:set>
				<a href="${pageContext.request.contextPath}/occurrences/boundingBoxWithCriteria.htm?<gbif:criterion subject="25" predicate="0" value="${dataProvider.key}"/>&minX=${minMapLong}&minY=${minMapLat}&maxX=${maxMapLong}&maxY=${maxMapLat}"><spring:message code="dataset.findall.viewarea" arguments="${a0}%%%${a1}" argumentSeparator="%%%"/></a>
			</li>
		</ul>
	</c:if>		
</c:if>	