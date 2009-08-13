<%@ include file="/common/taglibs.jsp"%>
<div id="twopartheader">	
	<h2><spring:message code="registration.header"/></h2>
	<h3>Registered Providers</h3>
</div>
<div id="registrationContainer">
<form method="post" action="${pageContext.request.contextPath}/register/sendRegistrationLoginsRequest">
<c:if test="${not empty providerRegistrationLogins}">
<p>
	<c:choose>
		<c:when test="${not empty username}">
			<b>${username}</b> is already associated with the following data providers:		
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
				<c:when test="${not empty username}">
					Please pick the provider you wish <b>${username}</b> to be associated with
				</c:when>
				<c:otherwise>
						Please pick the provider you wish to be associated with.
				</c:otherwise>	
			</c:choose>	
		</p>
		<c:if test="${not empty username}">
			<input type="hidden" name="user" value="${user}"/>
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
	<input type="submit" value="submit"/>		
</form>		
</div>