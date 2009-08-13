<%@ include file="/common/taglibs.jsp"%>
<c:choose>
	<c:when test="${param['mapSize']=='xsmall'}">
		<c:set var="mapUrlParams" scope="request" value="?size=xsmall"/>
	</c:when>
	<c:when test="${param['mapSize']=='small'}">
		<c:set var="mapUrlParams" scope="request" value="?size=small"/>
	</c:when>
	<c:when test="${param['mapSize']=='medium'}">
		<c:set var="mapUrlParams" scope="request" value="?size=medium"/>
	</c:when>		
	<c:otherwise>
		<c:set var="mapUrlParams" scope="request" value=""/>
	</c:otherwise>	
</c:choose>		
<c:choose>
	<c:when test="${param['mapSize']=='xsmall'}">
	<c:set var="iframeDimensions" scope="request"> width="300px" height="226px"</c:set>
	</c:when>
	<c:when test="${param['mapSize']=='small'}">
	<c:set var="iframeDimensions" scope="request"> width="360px" height="226px"</c:set>
	</c:when>
	<c:when test="${param['mapSize']=='medium'}">
	<c:set var="iframeDimensions" scope="request"> width="548px" height="320px"</c:set>
	</c:when>		
	<c:otherwise>
	<c:set var="iframeDimensions" scope="request"> width="730px" height="410px"</c:set>
	</c:otherwise>	
</c:choose>
<c:choose>
	<c:when test="${param['mapSize']=='xsmall'}">
	<c:set var="iframeDimensionsEscaped" scope="request"> width=\"300px\" height=\"226px\"</c:set>
	</c:when>
	<c:when test="${param['mapSize']=='small'}">
	<c:set var="iframeDimensionsEscaped" scope="request"> width=\"360px\" height=\"226px\"</c:set>
	</c:when>
	<c:when test="${param['mapSize']=='medium'}">
	<c:set var="iframeDimensionsEscaped" scope="request"> width=\"548px\" height=\"320px\"</c:set>
	</c:when>		
	<c:otherwise>
	<c:set var="iframeDimensionsEscaped" scope="request"> width=\"730px\" height=\"410px\"</c:set>
	</c:otherwise>	
</c:choose>