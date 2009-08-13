<%@ include file="/common/taglibs.jsp"%>
<div id="twopartheader">	
	<h2><spring:message code="registration.header"/></h2>
	<h3>Manual Registration</h3>
</div>
<div id="registrationContainer">	
	<p>Please complete the form and click "register" to register your resource.</p>
	<p>You will still have chance to edit the details of your resource after you have initially registered it.</p>
	<div class="editResource">
		<form method="post" action="${pageContext.request.contextPath}/register/completeManualRegistration">
			<c:set var="pathPrefix" value="" scope="request"/>
			<c:set var="resourceIndex" value="0" scope="request"/>										
			<c:set var="pathPostfix" value="" scope="request"/>					
			<c:set var="resource" value="${resource}" scope="request"/>										
			<input type="hidden" name="businessKey" value="${param['businessKey']}"/>
			<tiles:insert page="resourceForm.jsp"/>
			<input id="resourceSave-${resourceStatus.index}" type="submit" class="resourceSave" value="<spring:message code="registration.register.resource"/>" 
		</form>	
	</div>
</div>	