<%@ include file="/common/taglibs.jsp"%><string:trim>
<c:choose>
	<c:when test="${recordCount>maxCount}">
		<spring:message code="occurrence.search.actions.matched.over" arguments="${maxCount}"/> 
	</c:when>
	<c:when test="${recordCount==1}">
		<spring:message code="occurrence.search.actions.matched.one"/>
	</c:when>	
	<c:otherwise>
		<spring:message code="occurrence.search.actions.matched.many" arguments="${recordCount}"/>
	</c:otherwise>
</c:choose>	
</string:trim>