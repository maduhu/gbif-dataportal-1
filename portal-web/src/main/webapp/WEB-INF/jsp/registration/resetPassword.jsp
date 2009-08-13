<%@ include file="/common/taglibs.jsp"%>
<div id="twopartheader">	
	<h2><spring:message code="admin.reset.password" text="Reset password"/></h2>
</div>
<div id="registrationContainer">	
	<form method="POST" action="${pageContext.request.contextPath}/register/resetPassword">
		<fieldset>
			<p>
				Please provide your new password.
			</p>
			<c:if test="${invalidPassword}">
				<p>
					This password is too short. Must be 6 characters or more.				
				</p>
			</c:if>
			<p>
				<label for="password"><spring:message code="admin.login.password"/></label>
				<input class="loginInput" type="password" name="password" id="password" value="${password}"/>
			</p>
			<p class="center">			
				<input type="submit" text="submit"/>
			</p>
		</fieldset>
	</form>
</div>