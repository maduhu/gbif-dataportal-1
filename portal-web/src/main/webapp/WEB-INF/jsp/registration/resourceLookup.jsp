<%@ include file="/common/taglibs.jsp"%>
<tiles:insert page="jsFunctions.jsp"/>
<div id="twopartheader">	
	<h2><spring:message code="registration.header"/></h2>
	<h3><spring:message code="registration.header.resources.register" text="Register resources for "/> ${providerDetail.businessName}</h3>
</div>
<div id="registrationContainer">
	<p>
		<spring:message code="registration.provider.issue.metadata.1" text=""/><br/>
		<spring:message code="registration.provider.issue.metadata.2" text=""/>
	</p>
	<p>
		<spring:message code="registration.provider.issue.metadata.3" text=""/>
	</p>
	<p>	
		<spring:message code="registration.provider.issue.metadata.4" text="Example urls"/>
		<ul>
			<li>http://digir.acnatsci.org:80/digir/DiGIR.php</li>
			<li>http://145.18.162.60/tapirlink/tapir.php/rmnh_Martin</li>				
		</ul>
	</p>						
	
	<script type="text/javascript">
		function checkResourceType(selectElement){
			if(selectElement.value=='DiGIR' || selectElement.value=='TAPIR'){
				document.getElementById('resourceUrl').disabled=false;				
				document.getElementById('resourceSearchSubmit').disabled=false;		
				document.getElementById('manuallyRegisterHelp').className="hidden";
				document.getElementById('metadataResources').innerHTML="";				
			} else {
				document.getElementById('resourceUrl').disabled=true;				
				document.getElementById('resourceSearchSubmit').disabled=true;		
				document.getElementById('manuallyRegisterHelp').className="registrationHelp";
				document.getElementById('metadataResources').innerHTML="";
			}
		}
		
		function redirectToManual(){
			var url="${pageContext.request.contextPath}/register/manuallyRegisterResource?businessKey=${param['businessKey']}";
			document.location = url+'&resourceType='+document.getElementById('resourceType').value;
		}
	</script>
	<form id="providerContainerForm" method="get" action="${pageContext.request.contextPath}/register/registerDataResources">
		<fieldset class="resourceLookup">	
			<p>
				<label for="resourceType">Resource type: </label>
				<select id="resourceType" name="resourceType" onchange="javascript:checkResourceType(this);">
					<c:forEach items="${resourceTypes}" var="resourceType">
				    <option value="${resourceType}" <c:if test="${foundResourceType==resourceType}">selected="true"</c:if>><spring:message code="registration.resource.${resourceType}" text="${resourceType}"/></option>
			    </c:forEach>     	            	            
				</select>
			</p>		
			<p>
				<label for="resourceUrl">Access point: </label>
				<input id="resourceUrl" type="text" class="longUrl" name="resourceUrl" value="${param['resourceUrl']}"/>
				<input type="hidden" name="businessKey" value="${param['businessKey']}"/>			
			</p>
			<p>
			<input id="resourceSearchSubmit" type="submit" value="<spring:message code="registration.contact.provider.for.metadata"/>"/>
			</p>
		</fieldset>		
		<div id="manuallyRegisterHelp" class="hidden">
			<p>The resource type selected <b>is not supported</b> for a resource lookup. </p>
			<p>To manually add a resource <a href="javascript:redirectToManual();">click here</a>.</p>					
		</div><!--manuallyRegisterHelp-->		
	</form>
	<div id="metadataResources">
		<c:if test="${not empty param['resourceUrl']}">
			<tiles:insert page="metadataResourceList.jsp"/>
		</c:if>	
	</div><!--metdataResources-->
</div>