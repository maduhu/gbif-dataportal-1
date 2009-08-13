<%@ include file="/common/taglibs.jsp"%>
<c:set var="entityLink" scope="request">species/${taxonConcept.key}</c:set>
<c:set var="mapDescription" scope="request">
<c:if test="${param['size']!='small'}">
Specimen and observational data	for
</c:if>
	<span class="subject"><a href="http://${header.host}${pageContext.request.contextPath}/species/${taxonConcept.key}" target="_top">${taxonConcept.taxonName}</a></span><br/>
<c:if test="${param['size']!='small'}">	from the </c:if>Global Biodiversity Information Facility Network
</c:set>
<tiles:insert page="/WEB-INF/jsp/geography/mapWidget.jsp"/>