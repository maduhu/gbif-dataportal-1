<%@ include file="/common/taglibs.jsp"%>
<div id="quickConceptSearch">
	<c:choose>
		<c:when test="${not empty dataResource}">
	    	<spring:message code="quick.taxon.search.explanation" arguments="${dataResource.name}" argumentSeparator="|"/>
		</c:when>
		<c:otherwise>
	    	<spring:message code="quick.taxon.search.explanation.this.classification"/>
		</c:otherwise>
	</c:choose>
	<div id="statesmod">
	    <div id="statesautocomplete">
	    	<table style="width:800px; margin-bottom:0px;">
	    		<tr>
    				<c:set var="placeholder"><spring:message code="quick.taxon.search"/></c:set>
	   				<td style="width:290px;" valign="top">
  	 					<input id="statesinput" style="width:280px; padding:0; padding-left:3px; color:#AAAAAA;" value="${not empty param['qs'] ? param['qs'] : placeholder }" type="search" placeholder="<spring:message code="quick.taxon.search"/>"/> 
			      		<div id="statescontainer" style="margin-left:3px;width:200px;"></div>
   					</td>	
		       	<td style="width:60px; valign:top;" valign="top"><input type="submit" onclick="javascript:findBrowseUrl(document.getElementById('statesinput').value, null, null);" value="<spring:message code="search"/>"></td>
	    			<td><span id="noMatchesReport"></span></td>		       	
   				</tr>
		    </table>
   	 </div><!--statesautocomplete-->
	</div><!--statesmod-->
	
	<gbiftag:placeholder inputId="statesinput" selectedVar="quickSearchSelected" receivedKeyPressVar="receivedKeyPress"/>
	
	<!-- AutoComplete ends -->
	
	<c:set var="autoCompleteUrl">${pageContext.request.contextPath}/species/taxonName/ajax/rank/any/view/ajaxTaxonConceptName</c:set>
	<c:choose>
		<c:when test="${dataResource!=null && !dataResource.sharedTaxonomy}">
				<c:set var="autoCompleteUrl">${autoCompleteUrl}/resource/${dataResource.key}/</c:set>
		</c:when>
		<c:otherwise>
				<c:set var="autoCompleteUrl">${autoCompleteUrl}/provider/${dataProvider.key}/</c:set>			
		</c:otherwise>
	</c:choose>		
	
	<gbiftag:autoComplete 
			url="${autoCompleteUrl}" 
			inputId="statesinput" 
			containerId="statescontainer"
	/>					
	<script>
	<gbiftag:ieCheck/>
	<c:choose>	
	<c:when test="${isMSIE}">
		document.getElementById('statesinput').attachEvent("onkeypress", startFindBrowseUrl);		
	</c:when>
	<c:otherwise>
			document.getElementById('statesinput').addEventListener('keypress', startFindBrowseUrl, false);
	</c:otherwise>
	</c:choose>		
		
	function startFindBrowseUrl(ev){
		if(ev.keyCode==13){
			findBrowseUrl(document.getElementById('statesinput').value,
		<c:choose>
			<c:when test="${dataResource!=null && !dataResource.sharedTaxonomy}">null,"${dataResource.key}"</c:when>
			<c:otherwise>"${dataProvider.key}", null</c:otherwise>
		</c:choose>);
		}
	}

	function findBrowseUrl(taxonName, providerKey, resourceKey){
		if(!receivedKeyPress)
			return;
	
		var url="${pageContext.request.contextPath}/species/taxonName/ajax/returnType/concept/view/ajaxBrowseUrls/";
		<c:choose>
			<c:when test="${dataResource!=null && !dataResource.sharedTaxonomy}">
					url = url+"resource/${dataResource.key}/";
			</c:when>
			<c:otherwise>
					url= url+"provider/${dataProvider.key}/";
			</c:otherwise>
		</c:choose>	
		url=url+"?query="+taxonName;
		
		var callback = {
			success:function(o){
				var taxonElements = o.responseXML.getElementsByTagName("taxon");
				if(taxonElements.length>0){
					document.location = taxonElements[0].getElementsByTagName("url").item(0).childNodes[0].nodeValue+"?qs="+taxonName;
				} else {
					document.getElementById('noMatchesReport').innerHTML="<spring:message code="no.matches.with.taxonomy"/> \""+taxonName+"\"";
				}
			},	
			failure: function(o){}
		}	
		//make the ajax request for the wizard
		YAHOO.util.Connect.asyncRequest('GET',
			url, 
			callback, 
			null); 
	}
</script>
</div><!--quickConceptSearch-->