<%@ include file="/common/taglibs.jsp"%>
<%// display warnings if we have matching classifications or if this concept is not accepted %>

<c:if test="${not empty matchingNameClassifications || not empty unacceptedClassifications}">
<div id="warnings">

<c:set var="taxonName"><gbif:taxonPrint concept="${taxonConcept}"/></c:set>
<h4><spring:message code="taxonconcept.drilldown.warnings.title"/></h4>
<c:choose>
	<c:when test="${kingdomConcept!=null && kingdomConcept.isAccepted}">
		<c:if test="${not empty matchingNameClassifications}">	
			<p><spring:message code="taxonconcept.drilldown.warnings.ambiguous.name" arguments="${taxonName}"/></p>
			<c:forEach items="${matchingNameClassifications}" var="classification">
				<gbif:flattree classname="classificationCondensed" concepts="${classification}"/>
			</c:forEach>
		</c:if>
		<c:if test="${not empty unacceptedClassifications}">	
			<p>
				<spring:message code="taxonconcept.drilldown.warnings.presentation.accepted"/><br/>
				<spring:message code="taxonconcept.drilldown.warnings.presentation.accepted.viewed.at"/>
			</p>
			<c:forEach items="${unacceptedClassifications}" var="classification">
				<gbif:flattree classname="classificationCondensed" concepts="${classification}"/>
			</c:forEach>
		</c:if>	
	</c:when>
	<c:otherwise>
		<c:if test="${not empty matchingNameClassifications}">	
			<p><spring:message code="taxonconcept.drilldown.warnings.ambiguous.name" arguments="${taxonName}"/></p>
			<c:forEach items="${matchingNameClassifications}" var="classification">
				<gbif:flattree classname="classificationCondensed" concepts="${classification}"/>
			</c:forEach>
		</c:if>
		<p><spring:message code="taxonconcept.drilldown.warnings.presentation.notaccepted"/></p>
	</c:otherwise>
</c:choose>
</div>
</c:if>