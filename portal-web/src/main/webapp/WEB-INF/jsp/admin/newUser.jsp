<%@ include file="/common/taglibs.jsp"%>
<div class="registrationContainer">
	<div id="twopartheader">	
		<h2><spring:message code="admin.new.user" text="New user"/></h2>
	</div>
	<form method="POST" action="${pageContext.request.contextPath}/user/newUser">
		<fieldset>
			<p>
				<label for="firstName"><spring:message code="firstName" text="First name"/></label>
				<spring:bind path="user.firstName">
					<input class="loginInput" type="text" name="${status.expression}" id="firstName" value="${status.value}"/>
					<span class="error">${status.errorMessage}</span>
				</spring:bind>	
			</p>
			<script type="text/javascript">
				function setUsername(){
					var value = document.getElementById('firstName').value + document.getElementById('surname').value;
					if(value!=null && value.length>0){
						value=value.replace(/\s+/g,'');
						document.getElementById('username').value = value.toLowerCase();
					} 
				}
			</script>
			<p>
				<label for="surname"><spring:message code="surname" text="Surname"/></label>
				<spring:bind path="user.surname">
					<input class="loginInput" type="text" name="${status.expression}" id="surname" value="${status.value}" onchange="javascript:setUsername();"/>
					<span class="error">${status.errorMessage}</span>
				</spring:bind>					
			</p>			
			<p>
				<p class="registrationHelp">
					Please supply your email address. An email will be sent to this address for verification.
				</p>
				<label for="email"><spring:message code="email"/></label>
				<spring:bind path="user.email">
					<input class="loginInput" type="text" name="${status.expression}" id="email" value="${status.value}"/>
					<span class="error">${status.errorMessage}</span>
				</spring:bind>					
			</p>
			<p>
				<label for="telephone"><spring:message code="telephone"/></label>
				<spring:bind path="user.telephone">
					<input class="loginInput" type="text" name="${status.expression}" id="telephone" value="${status.value}"/>(Optional)
					<span class="error">${status.errorMessage}</span>
				</spring:bind>				
			</p>
				<spring:bind path="user.username">
					<p class="registrationHelp">Please supply a <b>username and password</b>. We will check if the supplied username is available.<br/><br/>
						Your username and password must be at least <b>6 characters</b> in length. The username can not contain any special characters.
					<c:if test="${not empty suggestedUsername}">
						The username <b>${status.expression} is taken</b>. The name <b>${suggestedUsername}</b> is currently available.
					</c:if>
					</p>
					<p>
					<label for="preferredLoginName"><spring:message code="username" text="Username"/></label>
					<input class="loginInput" type="text" name="${status.expression}" id="username" value="${status.value}"/>
					<span class="error">${status.errorMessage}</span>
					</p>
				</spring:bind>					
			<p>
				<label for="password"><spring:message code="admin.login.password"/></label>
				<spring:bind path="user.password">
					<input class="loginInput" type="password" name="${status.expression}" id="password" value="${status.value}"/>
					<span class="error">${status.errorMessage}</span>					
				</spring:bind>					
			</p>
			<p>
				<input type="submit" value="<spring:message code="register" text="Register"/>"/>
				<input type="button" name="cancel" value="<spring:message code="cancel" text="Cancel"/>" onclick="javascript:history.go(-1);"/>
			</p>
		</fieldset>
	</form>
</div>