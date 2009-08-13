<%@ attribute name="durationInMillisecs" required="true" %>
<%@ include file="/common/taglibs.jsp"%>
<c:choose>
	<c:when test="${durationInMillisecs<60000}">
		<gbif:decimal noDecimalPlaces="0">${durationInMillisecs/1000}</gbif:decimal> <spring:message code="seconds"/>
	</c:when>
	<c:when test="${durationInMillisecs>=60000 && durationInMillisecs<3600000}">
		<gbif:decimal>${durationInMillisecs/60000}</gbif:decimal> <spring:message code="minutes"/>				
	</c:when>
	<c:when test="${durationInMillisecs>=3600000 &&durationInMillisecs<86400000}">
		<gbif:decimal>${durationInMillisecs/1440000}</gbif:decimal> <spring:message code="hours"/>						
	</c:when>
	<c:otherwise>
		<gbif:decimal>${durationInMillisecs/86400000}</gbif:decimal> <spring:message code="days"/>
	</c:otherwise>				
</c:choose>