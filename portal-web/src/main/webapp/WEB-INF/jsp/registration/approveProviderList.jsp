<%@ include file="/common/taglibs.jsp"%>
<div id="twopartheader">	
	<h2><spring:message code="registration.header"/></h2>
	<h3>Review user request to see Data Publisher details</h3>
</div>
<div id="registrationContainer">
<p>
${user.fullName} (<a href="mailto:${user.email}">${user.email}</a>) has requested to view the details of one or more publishers.
</p>
<p>
	Please review the list below and approve their request if it is appropriate.
</p>
<c:if test="${not empty providerRegistrationLogins}">
<p>
	<b>${user.fullName}</b> is already associated with the following data publishers:		
	<ul>	
	<c:forEach items="${providerRegistrationLogins}" var="keyValue">
		<li>${keyValue.value}</li>
	</c:forEach>
	</ul>
</p>
</c:if>

<c:choose>
<c:when test="${not empty providersToApprove}">
<form method="post" action="${pageContext.request.contextPath}/register/updateRegistrationLogins">
<input type="hidden" name="user" value="${user.username}"/>
<input type="hidden" name="sendEmail" value="true"/>
<table>
	<thead>
	</thead>			
	<c:forEach items="${providersToApprove}" var="keyValue">
		<tr>
			<td><input type="checkbox" name="businessKey" value="${keyValue.key}" checked="true"/></td>
			<td>${keyValue.value}</td>
		</tr>
	</c:forEach>
</table>
	<input type="submit" value="Approve"/>		
</form>		
</c:when>
<c:otherwise>
	All publishers within this request have already been approved. <a href="${pageContext.request.contextPath}/register/">Click here</a> to continue.
</c:otherwise>
</c:choose>
</div>