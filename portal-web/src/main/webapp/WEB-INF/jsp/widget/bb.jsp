<%@ include file="/common/taglibs.jsp"%>
<script>
function openMap(){
	document.getElementById('blackBackground').style.display="block";
	document.getElementById('mapContainer').style.display="block";
	setOverlayDiv('boundingBoxMap', 'boundingBoxImg');
}

function hideMap(){
	document.getElementById('blackBackground').style.display="none";
	document.getElementById('mapContainer').style.display="none";
	
	/* set the values in text boxes */
	document.getElementById('north').value = document.getElementById('northCoordinate').innerHTML;
	document.getElementById('south').value = document.getElementById('southCoordinate').innerHTML;
	document.getElementById('east').value = document.getElementById('eastCoordinate').innerHTML;
	document.getElementById('west').value = document.getElementById('westCoordinate').innerHTML;
}
</script>

<style type="text/css">
#blackBackground { display:none; background-color:black; position:absolute; left:0px; top:0px; width:100%; height:2024px; filter:alpha(opacity=50);opacity:0.5; }

#mapContainer { display:none; position: absolute; float:top; left:0px; top:0px; bgProperties:scroll;}
#mapContainerTable { border:none; border-collapse:collapse; }
#mapContainerTable tr, td { border: none; width 100%;}
#mapTable { background-color:white; }

.topSpacer { height: 1100px;}
.mapLeft { width:20%;background-color:none; }
.mapCenter { background-color:white; }
.mapClose { background-color:white; padding:5px;}
.mapRight { width:20%; background-color:none; }
.sidebit { width:40px; background-color:white;}

#globalMap { width:900px; height:450px; }
.zoom1 { position: absolute; left: -50px;	top: 0px;	width: 38px;	height: 18px;	border: 2px solid #990000; }

#boundingBoxImg { filter:alpha(opacity=50); opacity:0.5; background-color:red; }

</style>

<!-- The background div -->
<div id="blackBackground">&nbsp;</div>
<!-- The map container -->
<div id="mapContainer">
<table id="mapContainerTable">
<tbody>
<tr><td colspan="3" class="topSpacer">&nbsp;</td></tr>
<tr>
	<td class="mapLeft">&nbsp;</td>
	<td class="sidebit">&nbsp;</td>
	<td class="mapCenter"><tiles:insert page="/WEB-INF/jsp/occurrence/wizards/boundingbox.jsp"/></td>
	<td class="sidebit">&nbsp;</td>
	<td class="mapRight">&nbsp;</td>
</tr>	
<tr>
	<td class="mapLeft">&nbsp;</td>
	<td class="sidebit">&nbsp;</td>	
	<td class="mapClose"><a href="javascript:hideMap();">Close map</a></td>
	<td class="sidebit">&nbsp;</td>
	<td class="mapRight">&nbsp;</td>
</tr>
</tbody>
</table>
</div>