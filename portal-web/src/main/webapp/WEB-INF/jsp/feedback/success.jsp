<%@ include file="/common/taglibs.jsp"%>
<div id="twopartheader">	
	<h1><spring:message code="feedback.title"/></h1>
</div>
<p>
	<spring:message code="feedback.submitted"/>	<br/><br/>
	<a href="${feedbackOnURL}"><spring:message code="feedback.continue"/></a>
</p>