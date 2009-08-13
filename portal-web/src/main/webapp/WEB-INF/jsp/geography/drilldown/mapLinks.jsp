<%@ include file="/common/taglibs.jsp"%>
<tiles:insert page="/WEB-INF/jsp/geography/mapPointCount.jsp"/>
<c:set var="entityKey" value="${country.key}" scope="request"/>
<c:if test="${country.occurrenceCoordinateCount>0 && zoom >1}">
<ul class="overviewMapLinks">
	<li>
		<c:set var="a0"><span class="subject"><gbif:capitalize><spring:message code="country.${country.isoCountryCode}"/></gbif:capitalize></span></c:set>
		<c:set var="a1"><gbiftag:boundingBox/></c:set>
		<a href="${pageContext.request.contextPath}/occurrences/boundingBoxWithCriteria.htm?<gbif:criterion subject="5" predicate="0" value="${country.isoCountryCode}"/>&minX=${minMapLong}&minY=${minMapLat}&maxX=${maxMapLong}&maxY=${maxMapLat}"><spring:message code="geography.drilldown.findall.viewarea" arguments="${a0}%%%${a1}" argumentSeparator="%%%"/></a>
	</li>
</ul>	
</c:if>