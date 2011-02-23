<%@ include file="/common/taglibs.jsp"%>
<div id="twopartheader">	
	<h2><spring:message code="registration.header"/></h2>
	<h3>Admin - Update user</h3>
</div>
<div id="registrationContainer">

<p>Use this tool to associate a system user with a data publisher.</p>
<p>This will allow the user to update a publisher's details and update the resources they are providing.</p>

<form method="get" action="${pageContext.request.contextPath}/register/addRegistrationLogins">
	<fieldset>
		<p>
			<label>User login:</label>	
			<input type="text" name="user" value="${user}"/>
			<c:if test="${userNotFound}"><span class="errors">*not recognised</span></c:if>			
		</p>
		<p>
			<label>User email:</label>
			<input type="text" name="email" value="${email}"/>			
		</p>
		
		<table>			
		<c:forEach items="${userLogins}" var="userLogin">
			<tr>
				<td><a href="${pageContext.request.contextPath}/register/addRegistrationLogins?user=${userLogin.username}">${userLogin.fullName}</a></td>
				<td>${userLogin.username}</td>
				<td>${userLogin.email}</td>
			</tr>
		</c:forEach>
		</table>
		<input type="submit" value="Find user"/>
	</fieldset>
</form>

</div>