<%@ include file="/common/taglibs.jsp"%>
	<div id="twopartheader">	
		<h2><spring:message code="admin.request.for.access.sent" text="Request for access sent"/></h2>
	</div>
<div id="registrationContainer">
	<p>
		A email has been sent to our our portal administrators at <a href="mailto:portal@gbif.org">portal@gbif.org</a>.<br/> 
		You may receive an email asking you for further information about your request.<br/>
		If your request is approved an email will be sent to the address ${user.email} notifying you.
	</p>
	<p>	
		<a href="${pageContext.request.contextPath}/register/">Click here</a> to continue.
	</p>	
</div>