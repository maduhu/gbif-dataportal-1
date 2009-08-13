<%@ include file="/common/taglibs.jsp"%>
<c:choose>
<c:when test="${dataProvider!=null}">${dataProvider.name}
	<c:if test="${dataResource!=null}">: ${dataResource.name}</c:if>
</c:when>
<c:otherwise>	
	<spring:message code="portal.logs"/>
</c:otherwise>
</c:choose>