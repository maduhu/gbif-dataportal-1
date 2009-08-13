<%@ include file="/common/taglibs.jsp"%>
<div id="twopartheader">	
	<h2><spring:message code="registration.header"/></h2>
	<h3>Admin - Verification failure</h3>
</div>
<div>
	<p>
	 We were unable to send you a verification email. 
	</p> 
	<p>
	 If this problem persists please email us at <a href="mailto:portal@gbif.org">portal@gbif.org</a> 
	</p> 
	<p>
		<a href="${pageContext.request.contextPath}/register/">Click here</a> to continue.
	</p>	
</div>