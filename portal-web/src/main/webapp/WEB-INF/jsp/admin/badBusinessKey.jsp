<%@ include file="/common/taglibs.jsp"%>
<div id="twopartheader">
	<h2>Malformed business key</h2>
	<h3>Registration</h3>	
</div>
<div id="registrationContainer">
	<p>The supplied business key ${param['businessKey']} is incorrect or malformed. </p>
	<p>If this problem persists please email <a href="mailto:portal@gbif.org">portal@gbif.org</a></p>
	<p><a href="${pageContext.request.contextPath}/register/">Click here</a> to continue</p>
</div>