<%@ include file="/common/taglibs.jsp"%>
<%@ attribute name="lowerLimit" required="false" rtexprvalue="true" type="java.lang.Float" %>
<%@ attribute name="value" required="false" rtexprvalue="true" type="java.lang.Float" %>
<%@ attribute name="text" required="false" rtexprvalue="true" type="java.lang.String" %>
<c:set var="formattedNumber"><fmt:formatNumber maxFractionDigits="2">${value}</fmt:formatNumber></c:set>
<c:choose>
<c:when test="${formattedNumber<=lowerLimit}">
${text}
</c:when>
<c:otherwise>
<fmt:formatNumber type="percent" minFractionDigits="2">${value}</fmt:formatNumber>
</c:otherwise>
</c:choose>