<%@ include file="/common/taglibs.jsp"%>
<c:choose>
<c:when test="${not empty nameMatches}">
<spring:message code="taxonnameresolve.clashes.for" text="Multiple name matches for"/> ${name}
</c:when>
<c:otherwise>
<spring:message code="taxonnameresolve.heading.nomatches" text="No taxon matches for "/> ${name}
</c:otherwise>
</c:choose>