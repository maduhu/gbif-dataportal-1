<%@ include file="/common/taglibs.jsp"%><html>
<head>
<c:choose>
	<c:when test="${param['size']=='xsmall'}">
		<c:set var="size" value="Xsmall"/>
	</c:when>
	<c:when test="${param['size']=='small'}">
		<c:set var="size" value="Small"/>
	</c:when>
	<c:when test="${param['size']=='medium'}">
		<c:set var="size" value="Medium"/>
	</c:when>		
	<c:otherwise>
		<c:set var="size" value=""/>
	</c:otherwise>	
</c:choose>		
<link rel="stylesheet" href="http://${header.host}${pageContext.request.contextPath}/<spring:theme code='mapWidget.css'/>"/>
<%//special case for opera %>
<%
	String ua = (String) request.getHeader( "User-Agent" );
	boolean isMSIE = ( ua != null && ua.indexOf( "MSIE" ) != -1 );
	request.setAttribute("isMSIE", isMSIE);
%>
<c:if test="${isOpera}">
<link rel="stylesheet" href="http://${header.host}${pageContext.request.contextPath}/<spring:theme code='mapWidget-opera.css'/>"/>
</c:if>
<c:if test="${isMSIE}">
<link rel="stylesheet" href="http://${header.host}${pageContext.request.contextPath}/<spring:theme code='mapWidget-ie.css'/>"/>
</c:if>
</head>
<body>
<script type="text/javascript">
	function changeRootLocation(location){
		parent.location.href = location;
	}
</script>
<div id="mapContainer${size}">
<img id="map" class="map${size}" 
	<c:choose>				
		<c:when test="${zoom>1}">
				src="http://${header.host}${pageContext.request.contextPath}/${entityLink}/overviewMap.png?minLongitude=${minMapLong}&minLatitude=${minMapLat}&maxLongitude=${maxMapLong}&maxLatitude=${maxMapLat}"
		</c:when>
		<c:otherwise>
				src="http://${header.host}${pageContext.request.contextPath}/${entityLink}/overviewMap.png"
		</c:otherwise>			
	</c:choose>				
	onclick="javascript:changeRootLocation('http://${header.host}${pageContext.request.contextPath}/${entityLink}');";
	onMouseOver="javascript:document.body.style.cursor='pointer';" 
	onMouseOut="javascript:document.body.style.cursor='default';"		
/>
<div id="mapDescription${size}">
	<table style="width: 100%;">
		<tr>
			<td id="logoLeft">
        <img id="logo" src="http://${header.host}${pageContext.request.contextPath}/images/mapwidget/logo-small.gif"/>
			</td>
			<td id="mapDescriptionText">
				<c:choose>
				<c:when test="${param['size']=='small'}">
					${mapDescription}
				</c:when>
				<c:otherwise>				
					${mapDescription}				
				</c:otherwise>								
				</c:choose>	
			</td>	
			<td id="informationRight"><img id="information" src="http://${header.host}${pageContext.request.contextPath}/images/mapwidget/information.gif" 
				onclick="javascript:changeRootLocation('http://${header.host}${pageContext.request.contextPath}/tutorial/deeplinking');"
				onMouseOver="javascript:document.body.style.cursor='pointer';" 
				onMouseOut="javascript:document.body.style.cursor='default';"	/></td>
		</tr>
	</table>
</div>

<img id="showLegendImg${size}" 
		src="http://${header.host}${pageContext.request.contextPath}/images/mapwidget/legend-maximize.gif"
		onclick="javascript:showLegend();"			
		onMouseOver="javascript:document.body.style.cursor='pointer';" 
		onMouseOut="javascript:document.body.style.cursor='default';"
/>
</div>

<div id="legend${size}">
<img id="legendMinize${size}" src="http://${header.host}${pageContext.request.contextPath}/images/mapwidget/legend-minimize.gif"
		onclick="javascript:hideLegend();"	
		onMouseOver="javascript:document.body.style.cursor='pointer';" 
		onMouseOut="javascript:document.body.style.cursor='default';"		
/>
	<h2 id="legendTitle${size}">Count per <br/>one degree cell</h2>
	<table class="mapLegend">
		<tr><td class="key" bgcolor="#ffff00"/><td class="detail">1 - 9</td></tr>
		<tr><td class="key" bgcolor="#ffcc00"/><td class="detail">10 - 99</td></tr>
		<tr><td class="key" bgcolor="#ff9900"/><td class="detail">100 - 999</td></tr>
		<tr><td class="key" bgcolor="#ff6600"/><td class="detail">1000 - 9999</td></tr>
		<tr><td class="key" bgcolor="#ff3300"/><td class="detail">10000 - 99999</td></tr>
		<tr><td class="key" bgcolor="#cc0000"/><td class="detail">100000+</td></tr>
	</table>
</div>

<script type="text/javascript">
	function showLegend(){
		document.getElementById('showLegendImg${size}').style.visibility = 'hidden';	
		document.getElementById('legend${size}').style.visibility = 'visible';
	}
	
	function hideLegend(){
		document.getElementById('legend${size}').style.visibility = 'hidden';
		document.getElementById('showLegendImg${size}').style.visibility = 'visible';			
	}	
</script>
</body>
</html>