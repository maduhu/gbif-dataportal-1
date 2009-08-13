<%@ include file="/common/taglibs.jsp"%>
<tiles:insert page="/WEB-INF/jsp/geography/mapPointCount.jsp"/>
<c:set var="entityKey" value="${dataResource.key}"  scope="request"/>
<c:if test="${dataResource.occurrenceCoordinateCount>0 && zoom >1}">
<ul class="overviewMapLinks">
	<li>
		<c:set var="a0"><span class="subject">${dataResource.name}</span></c:set>
		<c:set var="a1"><gbiftag:boundingBox/></c:set>
		<a href="${pageContext.request.contextPath}/occurrences/boundingBoxWithCriteria.htm?<gbif:criterion subject="24" predicate="0" value="${dataResource.key}"/>&minX=${minMapLong}&minY=${minMapLat}&maxX=${maxMapLong}&maxY=${maxMapLat}"><spring:message code="dataset.findall.viewarea" arguments="${a0}%%%${a1}" argumentSeparator="%%%"/></a>
	</li>
</ul>
</c:if>
