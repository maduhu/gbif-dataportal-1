<%@ include file="/common/taglibs.jsp"%>
<c:choose>
	<c:when test="${zoom>1 && viewablePoints>0}">
      <c:set var="a0"><span class="subject"><fmt:formatNumber value="${pointsTotal}" pattern="###,###"/></span></c:set>
      <c:set var="a1"><span class="subject"><fmt:formatNumber value="${occurrenceCount}" pattern="###,###"/></span></c:set>
      <spring:message code="maps.records.with.coordinates.fullcount" arguments="${a0}%%%${a1}" argumentSeparator="%%%"/>
		<tiles:insert page="viewableArea.jsp"/>
		(<gbiftag:boundingBox/>)
		<br /><spring:message code="maps.records.disclaimer"/>
	</c:when>	
	<c:when test="${occurrenceCount!=null && occurrenceCount>0 && pointsTotal>0}">	
		<c:set var="a0"><span class="subject"><fmt:formatNumber value="${pointsTotal}" pattern="###,###"/></span></c:set>
		<c:set var="a1"><span class="subject"><fmt:formatNumber value="${occurrenceCount}" pattern="###,###"/></span></c:set>
		<spring:message code="maps.records.with.coordinates.fullcount" arguments="${a0}%%%${a1}" argumentSeparator="%%%"/>
		<br/><spring:message code="maps.records.disclaimer"/>
	</c:when>
	<c:when test="${pointsTotal>0}">
		<c:set var="a0"><fmt:formatNumber value="${pointsTotal}" pattern="###,###"/></c:set>
		<spring:message code="maps.records.with.coordinates" arguments="${a0}" argumentSeparator="%%%"/>
		<br/><spring:message code="maps.records.disclaimer"/>
	</c:when>
</c:choose>