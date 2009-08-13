<%@ include file="/common/taglibs.jsp"%>
<c:choose>
<c:when test="${empty resources}">
	<div class="registrationHelp">
		<p>Sorry - no resources identified with the supplied endpoint.</p>
		<p>
		To manually add a resource <a href="${pageContext.request.contextPath}/register/manuallyRegisterResource?businessKey=${param['businessKey']}">click here</a>.
		</p>		
	</div>
</c:when>
<c:otherwise>
<p><spring:message code="registration.metadata.resources"/></p>
	<ul>
	<c:forEach items="${resources}" var="resource" varStatus="resourceStatus">
		<li>
			<span class="listHeading">${resource.name}</span> 
			<span id="resource-state-${resourceStatus.index}" class="registeredResource">
				<c:if test="${resource.serviceKey != null}"> - <spring:message code="registration.resource.already.registered"/></c:if>
			</span> 			
				- <a href="javascript:openResource('resource-' + ${resourceStatus.index})"><spring:message code="details"/></a>		
		
			<div id="resource-${resourceStatus.index}" class="hidden">
				<form id="resource-form-${resourceStatus.index}">			
					<c:set var="resourceIndex" value="${resourceStatus.index}" scope="request"/>										
					<c:set var="resource" value="${resource}" scope="request"/>										
					<tiles:insert page="resourceForm.jsp"/>
					<input type="button" class="resourceSave" value="<spring:message code="registration.register.cancel" text="Cancel"/>" onclick="javascript:closeResource('resource-${resourceStatus.index}');"/>
					<input id="resourceSave-${resourceStatus.index}" 
						type="button" class="resourceSave" 
						<c:choose>
							<c:when test="${empty resource.serviceKey}">
								value="<spring:message code="registration.register.resource"/>" 
							</c:when>
							<c:otherwise>	
								value="<spring:message code="registration.register.update" text="Update"/>" 
							</c:otherwise>						
						</c:choose>						
						onclick="javascript:addResource(this.form,'${resource.name}', ${resourceStatus.index})"/>								
				</form>						
			</div>
		</li>
	</c:forEach>
	</ul>
</c:otherwise>
</c:choose>