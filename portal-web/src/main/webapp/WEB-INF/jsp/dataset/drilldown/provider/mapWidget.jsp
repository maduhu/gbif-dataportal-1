<%@ include file="/common/taglibs.jsp"%>
<c:set var="entityLink" scope="request">datasets/provider/${dataProvider.key}</c:set>
<c:set var="mapDescription" scope="request">
<c:if test="${param['size']!='small'}">
Specimen and observational data	published by 
</c:if>
<span class="subject"><a href="http://${header.host}${pageContext.request.contextPath}/datasets/provider/${dataProvider.key}" target="_top">${dataProvider.name}</a></span><br/>
<c:if test="${param['size']!='small'}">from the </c:if>Global Biodiversity Information Facility Network
</c:set>
<tiles:insert page="/WEB-INF/jsp/geography/mapWidget.jsp"/>