<%@ include file="/common/taglibs.jsp"%>
<tiles:insert page="jsFunctions.jsp"/>
<div id="twopartheader">	
	<h2><spring:message code="registration.header"/></h2>
	<h3><spring:message code="registration.header.resources.offered.by"/> ${providerDetail.businessName}</h3>
</div>
<div id="registrationContainer">

	<h4><spring:message code="registration.header.resources.offered.by"/> ${providerDetail.businessName}</h4>
	<p>
		<c:set var="a0"><b>${providerDetail.businessName}</b></c:set>
		<c:choose>
			<c:when test="${not empty providerDetail.businessResources}">
				<spring:message code="registration.provider.resource.current.resources" arguments="${a0}" argumentSeparator="$$$$"/>
			</c:when>			
			<c:otherwise>			
				<spring:message code="registration.provider.resource.no.resources" arguments="${a0}" argumentSeparator="$$$$"/>
			</c:otherwise>						
		</c:choose>			
	</p>	
	<ul class="providerActions">
		<c:forEach items="${providerDetail.businessResources}" var="resource" varStatus="resourceStatus">
			<li>
				<a href="${pageContext.request.contextPath}/register/viewDataResource?businessKey=${providerDetail.businessKey}&serviceKey=${resource.serviceKey}">${resource.name}</a>
			</li>
		</c:forEach>
	</ul>
</div>		
