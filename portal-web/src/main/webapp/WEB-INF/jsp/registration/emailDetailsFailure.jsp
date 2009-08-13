<%@ include file="/common/taglibs.jsp"%>
<div id="twopartheader">	
	<h2><spring:message code="registration.header"/></h2>
	<h3>Admin - Email details failure</h3>
</div>
<div>
	<p class="warningMessage">
	 We were unable to send your details to ${email}. 
	</p> 
	<p>
	 If this problem persists please email us at <a href="mailto:portal@gbif.org">portal@gbif.org</a> 
	</p> 
</div>