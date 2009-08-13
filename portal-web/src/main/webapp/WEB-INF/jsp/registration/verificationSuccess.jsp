<%@ include file="/common/taglibs.jsp"%>
<div id="twopartheader">	
	<h2><spring:message code="registration.header"/></h2>
	<h3>Admin - Verification sent</h3>
</div>
<div>
	<p>
	 We have sent you a verification email. 
	</p>
	<p> 
	 Please click the link within the email to verify your account.
	</p> 
	<p>
	To return to login page, please <a href="${pageContext.request.contextPath}/register/">click here</a>.
	</p>
</div>