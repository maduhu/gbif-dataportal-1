<%@ include file="/common/taglibs.jsp"%>
<spring:message code="dataset.logs" text="Portal Logs"/>
<c:choose>
	<c:when test="${not empty dataProvider}">
		- ${dataProvider.name}
	</c:when>
	<c:when test="${not empty dataResource}">
		- ${dataResource.name}
	</c:when>
</c:choose>