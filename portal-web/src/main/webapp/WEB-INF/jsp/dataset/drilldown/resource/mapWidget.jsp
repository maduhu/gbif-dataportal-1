<%@ include file="/common/taglibs.jsp"%>
<c:set var="entityLink" scope="request">datasets/resource/${dataResource.key}</c:set>
<c:set var="mapDescription" scope="request">
<c:if test="${param['size']!='small'}">
Specimen and observational data	provided by 
</c:if>
<span class="subject"><a href="http://${header.host}${pageContext.request.contextPath}/datasets/resource/${dataResource.key}" target="_top">${dataResource.name}</a></span><br/>
<c:if test="${param['size']!='small'}">from the </c:if>Global Biodiversity Information Facility Network
</c:set>
<tiles:insert page="/WEB-INF/jsp/geography/mapWidget.jsp"/>