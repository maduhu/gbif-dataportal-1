<%@ include file="/common/taglibs.jsp"%>
<div class="registrationContainer">
	<div id="twopartheader">	
		<h2><spring:message code="admin.forgotten.password" text="Forgotten username/password"/></h2>
	</div>
	<form method="POST" action="${pageContext.request.contextPath}/user/forgottenPassword">
		<fieldset>
			<p>
				Please provide your email address and we will email you your login details.
			</p>
			<c:if test="${unrecognised}">
				<p class="warningMessage">
				Email address not recognised.
				</p>			
			</c:if>
			<p>
				<label for="email"><spring:message code="email"/></label>
				<input class="loginInput" type="text" name="email" id="email" value="${email}"/>
			</p>
			<p class="center">			
				<input type="submit" value="submit"/>
			</p>
		</fieldset>
	</form>
</div>