<%@ include file="/common/taglibs.jsp"%>
<c:if test="${fn:length(distributions)>0}">
<h3><spring:message code="taxonconcept.drilldown.distributions"/></h3>
	<ul>
		<c:forEach items="${distributions}" var="distribution" varStatus="status">	
			<li>${distribution.text}</li>
		</c:forEach>
	</ul>
</c:if>