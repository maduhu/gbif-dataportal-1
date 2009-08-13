<%@ include file="/common/taglibs.jsp"%>
<div id="twopartheader">	
	<h2><spring:message code="admin.password.reset" text="Password reset"/></h2>
</div>
<div id="registrationContainer">
	<p>
		Thank you. Your password has been set to the new value supplied. 
	</p>
	<p>
		<a href="${pageContext.request.contextPath}/register/">Click here</a> to continue.
	</p>
</div>