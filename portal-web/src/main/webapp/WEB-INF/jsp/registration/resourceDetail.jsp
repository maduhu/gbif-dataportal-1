<%@ include file="/common/taglibs.jsp"%>
<tiles:insert page="jsFunctions.jsp"/>
<div id="twopartheader">	
	<h2><spring:message code="registration.header"/></h2>
	<h3><spring:message code="registration.header.resources.offered.by"/> ${providerDetail.businessName}</h3>
</div>
<div id="registrationContainer">
	<h4>${resource.name}</h4>
	<p>
	To update the metadata for this resource, please
	<a href="${pageContext.request.contextPath}/register/refreshMetadataDetails?businessKey=${param['businessKey']}&serviceKey=${param['serviceKey']}">click here</a>.
	</p>
	<p>
	This will retrieve the latest metadata from the provider and update the fields marked with <span class="info"><spring:message code="registration.metdata.supplied"/></span>.
	</p>
	<c:if test="${metadataRefresh}">
			<p class="registrationHelp">
			<c:choose>
				<c:when test="${refreshFailure}">
				There was a problem retrieving the latest metadata from the access point <a href="${resource.accessPoint}">${resource.accessPoint}</a>.<br/>
				If this problem persists, please email <a href="mailto:portal@gbif.org">portal@gbif.org</a>
				</c:when>
				<c:otherwise>
					We have retrieved the latest details from the from the access point <a href="${resource.accessPoint}">${resource.accessPoint}</a>. <br/>
					To save this update, please click <b>Update</b> at the bottom of the form.
				</c:otherwise>
			</c:choose>
			</p>
		
	</c:if>	
	<form id="form-${resource.serviceKey}" method="POST" action="${pageContext.request.contextPath}/register/updateDataResource?businessKey=${param['businessKey']}">
		<div id="editResource" class="editResource">
			<c:set var="pathPrefix" value="resource" scope="request"/>
			<c:set var="resourceIndex" value="" scope="request"/>										
			<c:set var="pathPostfix" value="" scope="request"/>					
			<c:set var="resource" value="${resource}" scope="request"/>												
			<tiles:insert page="resourceForm.jsp"/>						
			<input type="button" class="resourceSave" name="cancel" value="<spring:message code="cancel" text="Cancel"/>" onclick="javascript:history.go(-1);"/>						
			<input type="submit" class="resourceSave" name="submit" value="<spring:message code="update" text="Update"/>"/>			
		</div><!-- editResource div -->
	</form><!-- Form -->
</div><!-- registrationContainer-->