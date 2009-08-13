<%@ include file="/common/taglibs.jsp"%>
<c:set var="entityLink" scope="request">datasets/network/${resourceNetwork.key}</c:set>
<c:set var="mapDescription" scope="request">
	<c:if test="${param['size']!='small'}">
		Specimen and observational data	provided through 
	</c:if>
	<span class="subject"><a href="http://${header.host}${pageContext.request.contextPath}/datasets/network/${resourceNetwork.key}" target="_top">${resourceNetwork.name}</a></span><br/>
	<c:if test="${param['size']!='small'}">from the</c:if>
	Global Biodiversity Information Facility Network
</c:set>
<tiles:insert page="/WEB-INF/jsp/geography/mapWidget.jsp"/>