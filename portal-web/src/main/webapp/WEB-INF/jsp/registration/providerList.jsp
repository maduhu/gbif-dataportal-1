<%@ include file="/common/taglibs.jsp"%>
<div id="twopartheader">	
	<h2><spring:message code="registration.header"/></h2>
	<h3>Registered Providers</h3>
</div>
<div id="registrationContainer">
<form method="post" action="${pageContext.request.contextPath}/register/${updateAction}">
<c:if test="${not empty providerRegistrationLogins}">
<p>
	<c:choose>
		<c:when test="${not empty userLogin}">
			<b>${userLogin.fullName}</b> (<a href="mailto:${userLogin.email}">${userLogin.email}</a>) is already associated with the following data providers:		
		</c:when>
		<c:otherwise>
			You are already associated with the following data providers:
		</c:otherwise>	
	</c:choose>		
	<table>
			<thead>
				<th colspan="2" style="text-align:left;">Remove</th>
			</thead>
		<c:forEach items="${providerRegistrationLogins}" var="keyValue">
			<tr>
				<td><input type="checkbox" name="businessKeyToRemove" value="${keyValue.key}" selected="true"/></td>
				<td>${keyValue.value}</td>
			</tr>
		</c:forEach>
	</table>
</p>
</c:if>
<c:choose>
	<c:when test="${not empty providerList}">		
		<p>
			<c:choose>
				<c:when test="${not empty userLogin}">
					Please select the provider you wish <b>${userLogin.fullName}</b> (<a href="mailto:${userLogin.email}">${userLogin.email}</a>) to be associated with and click "Submit".
				</c:when>
				<c:otherwise>
						Please select the provider you wish to be associated with and click "Submit".<br/>
						If you select to see a new provider, 
						a notification email will be sent to our portal administrators (<a href="mailto:portal@gbif.org">portal@gbif.org</a>) 
						so that they can enable access.<br/>
						Once you are granted access to a providers details, you will receive an email and then
						you will be able to update this details,<br/>
						and add new resources and update the details of existing resources.
				</c:otherwise>	
			</c:choose>	
		</p>
		
		<c:if test="${not empty userLogin}">		
		<input type="hidden" name="user" value="${userLogin.username}"/>
		</c:if>	
		<table>
			<thead>
				<th colspan="2" style="text-align:left;">Add</th>
			</thead>			
		<c:forEach items="${providerList}" var="keyValue">
			<tr>
				<td><input type="checkbox" name="businessKey" value="${keyValue.key}" /></td>
				<td>${keyValue.value}</td>
			</tr>
		</c:forEach>
		</table>
	</c:when>		
	<c:otherwise>
	<p>
		There are no providers within the system to add.
	</p>		
	</c:otherwise>	
</c:choose>			
	<input type="submit" value="Submit"/>		
</form>		
</div>