<%@ include file="/common/taglibs.jsp"%>
<c:choose>
<c:when test="${not empty occurrenceRecord}">
${occurrenceRecord.institutionCode} ${occurrenceRecord.collectionCode} ${occurrenceRecord.catalogueNumber}
</c:when>
<c:otherwise>
<spring:message code="occurrence.record" text="Occurrence record"/> ${rawOccurrenceRecord.key}
</c:otherwise>
</c:choose>