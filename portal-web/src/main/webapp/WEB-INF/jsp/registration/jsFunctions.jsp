<%@ include file="/common/taglibs.jsp"%>
<script type="text/javascript">

//open or close depending on state
function openCloseResource(resourceDivId) {
	if(	document.getElementById(resourceDivId).className == "editResource"){
		updateResource(document.getElementById("form-"+resourceDivId), resourceDivId);
	} else {
		openResource(resourceDivId);
	}
}

// opens the resource details for editing in the list of details
function openResource(resourceDivId) {
	if (document.getElementById("saved-" + resourceDivId) != null) {
		document.getElementById("saved-" + resourceDivId).innerHTML = "";
	}
	document.getElementById(resourceDivId).className = "editResource";
}

// opens the resource details for editing in the list of details
function closeResource(resourceDivId) {
	document.getElementById(resourceDivId).className = "hidden";
}

// updates the resource
function updateResource(formElement, resourceDivId) {
	YAHOO.util.Connect.setForm(formElement);
	
	var callback = {
		success:function(o){
			document.getElementById(resourceDivId).className = "hidden";
			document.getElementById("saved-" + resourceDivId).innerHTML = "<img src='${pageContext.request.contextPath}/images/icons/accept.png'/> ";
		},	
		failure: function(o){alert("An unknown error has occurred");}
	}	
	
	YAHOO.util.Connect.asyncRequest("POST", 
		'${pageContext.request.contextPath}/register/updateDataResource?businessKey=${providerDetail.businessKey}', 
		callback);	
}

// adds the resource
function addResource(formElement, resourceName, resourceIndex) {

	YAHOO.util.Connect.setForm(formElement);
	var callback = {
		success:function(o){
//			document.getElementById("resource-state-"+resourceIndex).innerHTML="<span class='registeredResource'> - <spring:message code='registration.resource.already.registered'/></span>";			
//			document.getElementById("resource-"+resourceIndex).className = "hidden";
//			var saveResourceButton = document.getElementById('resourceSave-'+resourceIndex);
//			saveResourceButton.value="<spring:message code="registration.register.save" text="Save"/>";
			document.location = document.location;
			
		},	
		failure: function(o){alert("An error has occurred updating the UDDI registry.");}
	}	
	
	YAHOO.util.Connect.asyncRequest("POST", 
		'${pageContext.request.contextPath}/register/addDataResource?businessKey=${providerDetail.businessKey}&resourceIndex='+resourceIndex, 
		callback);	
}

// issues the metadata request
function issueMetadataRequest(url) {
	document.getElementById("metadataResources").innerHTML = "<img src='${pageContext.request.contextPath}/images/loading.gif'/>"
	var callback = {
		success:function(o){document.getElementById("metadataResources").innerHTML = o.responseText;},	
		failure: function(o){alert("error: " + o.responseText);}
	}	
	YAHOO.util.Connect.asyncRequest("GET", 
		'${pageContext.request.contextPath}/register/issueMetadataRequest?businessKey=${providerDetail.businessKey}&resourceUrl=' + url, 
		callback,
		null);	
}
</script>