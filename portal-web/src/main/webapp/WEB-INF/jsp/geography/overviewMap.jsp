<%@ include file="/common/taglibs.jsp"%>
<div id="overviewMapSmallDiv">
	<img id="overviewMapSmall" 
			class="overViewMapSmall" 
			alt="global overview map" 
			width="240px"
			height="120px"
			src="${pageContext.request.contextPath}/images/globalMap.png"
	/>
	<div id="mapPositionalMarker" class="boundingBoxMarker" style="position:relative; left:0px; display:none;">&nbsp;</div>	
</div>
<script type="text/javascript" language="javascript">
	setTimeout("setBoundingBoxMarkerOnMap('overviewMapSmall', 'overviewMapSmallDiv', 'mapPositionalMarker', ${minMapLong}, ${minMapLat}, ${maxMapLong}, ${maxMapLat});", 2000);	
</script>