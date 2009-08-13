<%@ include file="/common/taglibs.jsp"%>
<div id="boundingBoxWizard">
<c:if test="${param['currValue']!=null}">
<script type="text/javascript" language="javascript">
	selectedBoundingBox="${param['currValue']}";
</script>
</c:if>
<div id="mapSelector" 
		class="zoom1" 
		style="background-color:#CCCCCC; filter:alpha(opacity=50);opacity:0.5;" 
		onMouseOver="javascript:disableSelection();document.onmousemove=trackMouse;capture=true; document.body.style.cursor='crosshair';" 
		onMouseOut="javascript:disableSelection();document.body.style.cursor='default';"		
		onMouseDown="javascript:disableSelection();setSelectorRoot('boundingBoxMap', '${param['filterIndex']}');"
		onMouseUp="javascript:enableSelection();setSelectorArea();"		
>&nbsp;</div>	
<table id="mapTable">
<tr>
<td colspan="2">
	<table class="markerPosition" style="margin-left:5px; border:0px; width:400px;">		
		<tr>
			<td style="width:200px;"><spring:message code="boundingbox.wizard.latitude"/>: <span id="latitude">-</span></td>
			<td style="width:200px;"><spring:message code="boundingbox.wizard.longitude"/>: <span id="longitude">-</span></td>
		</tr>
	</table>
	<div id="boundingBoxImg" 
		class="overviewMap"
		style="position:absolute; float:left; filter:alpha(opacity=0);opacity:0;background-color:red;" 
		onMouseOver="javascript:disableSelection();checkForMouseUp;document.onmousemove=trackMouse;capture=true; document.body.style.cursor='crosshair';" 
		onMouseOut="javascript:disableSelection();capture=false; document.body.style.cursor='default';"
		onMouseDown="javascript:disableSelection();setSelectorRoot('boundingBoxMap', '${param['filterIndex']}');document.body.style.cursor='crosshair';"
		onMouseUp="javascript:enableSelection();document.onmouseup=mouseUp;setSelectorArea();document.body.style.cursor='crosshair';"		
	>&nbsp;</div>
	
	<img id="boundingBoxMap"  
			<c:choose>
			<c:when test="${fullScreen}">style="width:1000px;height:500px;"</c:when>
			<c:otherwise>style="width:700px;height:350px;"</c:otherwise>
			</c:choose>	
			src="${pageContext.request.contextPath}/images/globalMap.png"	
			onMouseOver="javascript:document.body.style.cursor='crosshair';" 			
			onMouseOut="javascript:document.body.style.cursor='default';"					
			onLoad="javascript:setOverlayDiv('boundingBoxMap', 'boundingBoxImg');<c:if test="${boundingBox!=null}">setBoundingBoxMarkerOnMap('boundingBoxImg', 'mapSelector', ${boundingBox.minLong}, ${boundingBox.minLat}, ${boundingBox.maxLong}, ${boundingBox.maxLat});</c:if>"
	/>
</td>
</tr>
<tr>
<td valign="top">
<c:choose>
<c:when test="${fullScreen}"><a onclick="javascript:document.location='${pageContext.request.contextPath}/occurrences/search.htm?<gbif:criteria criteria="${criteria}"/>&newFilterSubject=19&currValue='+selectedBoundingBox;">Back</a></c:when>
<c:otherwise>
<!--
	<a onclick="javascript:document.location='${pageContext.request.contextPath}/occurrences/boundingBoxFullScreen.htm?<gbif:criteria criteria="${criteria}"/>&newFilterSubject=19&currValue='+selectedBoundingBox;">Use Full Screen Map</a>
-->	
</c:otherwise>
</c:choose>
</td><td align="right">
<!-- map marker -->
	<ul id="markerPosition">
		<li id="northCoordinate">90</li>
		<li id="westCoordinate">-180</li>
		<li id="eastCoordinate">-180</li>
		<li id="southCoordinate">-90</li>
	</ul>	
</td>	
</tr>
</table>
</div>