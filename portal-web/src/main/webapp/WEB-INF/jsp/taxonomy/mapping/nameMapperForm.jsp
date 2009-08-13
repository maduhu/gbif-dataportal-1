<div id="twopartheader">
	<h2>
		GBIF Name Mapper
	</h2>
	<h3>
		Use this tool to map a set of scientific or common names to a GBIF ID
	</h3>	
</div>

<form method="post" action="${pageContext.request.contextPath}/species/downloadMapping.htm">
<p>
	Please paste in your <b>new line separated list of scientific or common names</b> into the text box below. 
	<br/><br/>
</p>	

Are you supplying <b>common names or scientific names</b>?<br/><br/>
<input type="radio" value="true" name="isScientific" id="scientific" checked="true"/> Scientific names<br/>
<input type="radio" value="false" name="isScientific" id="common"/> Common names 
<br/><br/>
<input type="checkbox" value="true" name="mapSynonymsToAccepted" id="mapSynonymsToAccepted" checked="true"/> 
	Map synonyms to accepted name<br/>
<br/><br/>
<input type="checkbox" value="true" name="allowUnconfirmed" id="allowUnconfirmed"/> 
	Allow mapping to unconfirmed names<br/>
 (these are names derived from occurrence data which we have not associated with a taxon listed in established checklists such as Catalogue of Life)<br/>
<br/><br/>

<textarea id="nameList"
	name="nameList" 
	rows="20" 
	columns="800" 
	style="width:800px; height:300px; border:1px solid #CCCCCC;"></textarea>
<script>
	document.getElementById("nameList").setFocus();
</script>

<br/><br/>

<!--
Specify the format you are supplying<br/><br/>
<input type="radio" value="newline" id="newline" checked="true"/> New line separated list<br/>
<input type="radio" value="tab" id="comma"/> Tab separated list<br/>
<input type="radio" value="comma" id="comma"/> Comma separated list<br/>
<br/><br/>
-->

<input type="submit" value="Map Names"/>

</form>