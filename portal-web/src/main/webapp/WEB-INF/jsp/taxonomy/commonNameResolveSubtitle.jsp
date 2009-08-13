<%@ include file="/common/taglibs.jsp"%>
<c:choose>
<c:when test="${not empty nameMatches}">
<spring:message code="commonnameresolve.clashes.for"/> ${name}
</c:when>
<c:otherwise>
<spring:message code="commonnameresolve.heading.nomatches"/> ${name}
</c:otherwise>
</c:choose>