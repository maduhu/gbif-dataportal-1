<%@ include file="/common/taglibs.jsp"%>
<c:choose>
<c:when test="${totalOccurrenceRecords!=null && totalOccurrenceRecords>0}">
<c:if test ="${totalOccurrenceRecords!=null}"><fmt:formatNumber value="${totalOccurrenceRecords}" pattern="###,###"/> <spring:message code="heading.stats1"/></c:if>
<c:if test ="${dataProviderCount!=null}"><fmt:formatNumber value="${dataProviderCount}" pattern="###,###"/> <spring:message code="heading.stats2"/></c:if>
</c:when>
<c:otherwise>
	<spring:message code="portal.subtitle.welcome"/>
</c:otherwise>
</c:choose>