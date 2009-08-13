<%@ include file="/common/taglibs.jsp"%>
<c:set var="a0"><span class="subject"><fmt:formatNumber value="${viewablePoints}" pattern="###,###"/></span></c:set>
<c:choose>
	<c:when test="${viewablePoints==1}"><spring:message code="maps.points.viewed.area.one"/></c:when>
	<c:when test="${viewablePoints>1}"><spring:message code="maps.points.viewed.area" arguments="${a0}" argumentSeparator="%%%"/></c:when>
</c:choose>