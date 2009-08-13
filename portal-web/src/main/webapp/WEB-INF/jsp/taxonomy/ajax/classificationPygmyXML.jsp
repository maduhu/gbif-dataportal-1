<%@ page contentType="text/xml" %><%@ include file="/common/taglibs.jsp"%><?xml version="1.0" encoding="UTF-8"?>
<node>
<c:choose>
<c:when test="${not empty selectedConcept}">
<id>${selectedConcept.key}</id>
<name>${selectedConcept.taxonName}</name>
<parentId><c:if test="${empty selectedConcept.parentConceptKey}">0</c:if>${selectedConcept.parentConceptKey}</parentId>
<c:if test="${fn:length(concepts)>1}">
<c:forEach items="${concepts}" var="concept" begin="1">
<child>
<id>${concept.key}</id>
<name>${concept.taxonName}</name>
<numChildren>${concept.childCount}</numChildren>
</child>
</c:forEach>
</c:if>
</c:when>
<c:otherwise>
<c:forEach items="${concepts}" var="concept">
<id>${concept.key}</id>
<name>${concept.taxonName}</name>
<parentId>${concept.parentConceptKey}</parentId>
</c:forEach>
</c:otherwise>
</c:choose>
</node>