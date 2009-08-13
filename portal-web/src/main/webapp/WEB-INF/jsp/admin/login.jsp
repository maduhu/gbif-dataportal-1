<%@ include file="/common/taglibs.jsp"%>
<c:set var="registrationRedirect"><gbif:propertyLoader bundle="portal" property="registrationRedirect"/></c:set>
<c:if test="${not empty registrationRedirect}">
<script type="text/javascript">
	document.location = "${registrationRedirect}";
</script>
</c:if>
<div id="twopartheader">
	<h2><spring:message code="admin.login"/></h2>
</div>
<div class="registrationContainer">
	<form method="POST" action="j_security_check">
  	<fieldset>
     <c:if test="${not empty param['message']}">
     <p class="warningMessage">
       <spring:message code="${param['message']}" text=""/>
     </p>
     </c:if>
     <p>
       <label for="username"><spring:message code="name"/></label>
       <input class="loginInput" type="text" name="username" id="username" value="${param['u']}"/>
       <input type="hidden" name="j_username" id="j_username"/>
     </p>
     <p>
       <label for="password"><spring:message code="admin.login.password"/></label>
       <input class="loginInput" type="password" name="j_password" id="password" value="<gbif:decrypt encryptedPassword="${param['p']}"/>"/>
     </p>
     <p class="center">
       <c:set var="usernamePostFix" value=""/>
       <input type="submit" 
          value="<spring:message code="admin.login"/>" 
          onclick="javascript:document.getElementById('j_username').value = document.getElementById('username').value + '${usernamePostFix}'"                                     
       />
     </p>
		</fieldset>
  </form>
  <p>
    Forgotten your username/password? <a href="${pageContext.request.contextPath}/user/forgottenPassword">Click here</a> for an email reminder.<br/>
  </p>
  <p>
    New users, please <a href="${pageContext.request.contextPath}/user/newUser">click here</a> for a small form to register.
  </p>
</div>