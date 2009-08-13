<%@ include file="/common/taglibs.jsp"%>
<div id="sideMenu">
<ul class="generalActions">
	<li><a href="${pageContext.request.contextPath}/register/">Main menu</a></li>
	<li><a href="${pageContext.request.contextPath}/register/registerDataProvider">Register new data provider</a></li>
	<li><a href="${pageContext.request.contextPath}/register/findDataProvider">Find a data provider </a></li>
	<gbif:isUserInRole role="GBIF LDAP admins">	
		<li><a href="${pageContext.request.contextPath}/register/userUpdate">Associate user with provider <span style="color:#FF0000;">(Admin)</span></a></li>
	</gbif:isUserInRole>	
	<li><a href="${pageContext.request.contextPath}/register/resetPassword">Reset password</a></li>		
	<li><a href="${pageContext.request.contextPath}/register/logoutUser">Log out</a></li>	
	<!-- the selected provider -->
	<c:if test="${not empty param['businessKey']}">
		<li class="noBullet">
			<span class="selectedCategory"><a href="${pageContext.request.contextPath}/register/viewDataProvider?businessKey=${param['businessKey']}"><gbif:lineBreaker maxLengthBeforeBreak="20">${providerDetail.businessName}</gbif:lineBreaker></a></span>
			<ul class="providerActions">
				<li><a href="${pageContext.request.contextPath}/register/updateDataProvider?businessKey=${param['businessKey']}">Update details</a></li>
				<li><a href="${pageContext.request.contextPath}/register/showDataResources?businessKey=${param['businessKey']}">View registered resources</a></li>			
				<li><a href="${pageContext.request.contextPath}/register/registerDataResources?businessKey=${param['businessKey']}">Register a new resource</a></li>						
			</ul>
		</li>
	</c:if>
</ul>
</div><!-- side menu -->