<%@ include file="/common/taglibs.jsp"%>
<div id="twopartheader">	
	<h2>Hello ${username}!</h2>
	<h3>Registration </h3>	
</div>
<div id="registrationContainer">

	<p> <span class="logout">(If you are not ${username} please <a href="${pageContext.request.contextPath}/register/logoutUser">click here</a> to log out)</span></p>
	<c:choose>
		<c:when test="${empty providerDetails}">
			<p>
				You are currently not associated with a Data Publisher.
			</p>
			<ul class="generalActions">
				<li><a href="registerDataProvider">Register a new Data Provider</a></li>				
				<li><a href="findDataProvider">My Data Publisher is already registered, please find it</a></li>									
			</ul>	
		</c:when>				
		<c:otherwise>					
			<p>
				You are currently associated with the following data publishers:
			</p>
				<c:forEach items="${providerDetails}" var="provider">
						<h4><a class="providerLink" href="viewDataProvider?businessKey=${provider.businessKey}">${provider.businessName}</a></h4>
						<ul class="providerActions">
							<li><a href="viewDataProvider?businessKey=${provider.businessKey}">View details</a></li>										
							<li><a href="updateDataProvider?businessKey=${provider.businessKey}">Update details</a></li>		
							<li><a href="showDataResources?businessKey=${provider.businessKey}">View data resources</a></li>										
						</ul>
				</c:forEach>							
			<p style="margin-top:20px;">Other options:</p>
			<ul class="generalActions">
				<li><a href="registerDataProvider">Register a new Data Provider</a></li>				
				<li><a href="findDataProvider">My Data Publisher is already registered, please find it</a></li>									
			</ul>							
		</c:otherwise>								
	</c:choose>			
</div>
