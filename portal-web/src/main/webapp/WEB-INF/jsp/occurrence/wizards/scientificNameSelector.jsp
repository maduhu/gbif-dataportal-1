<%@ include file="/common/taglibs.jsp"%>
<div id="scientificNameSelector" class="taxonomyWizard">

	Please type a scientific name and click "find"
	<br/>
	<br/>
	
	Scientific name <input id="scientificNameInput" type="text"/>
	<a href="javascript:findConcepts('${pageContext.request.contextPath}/taxonomy/taxonName/ajax/returnType/concept/provider/1/view/conceptSelector/?query=', 'scientificNameInput', 'nameSearchResults');">Find</a>

	<div id="nameSearchResults" style="margin-top:20px;">
		&nbsp;
	</div>

</div>