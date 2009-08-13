<%@ page contentType="text/xml" %><%@ include file="/common/taglibs.jsp"%><?xml version="1.0" encoding="UTF-8"?>
<string:trim><tiles:insert page="mapSize.jsp" flush="false"/>
<taxa>
<c:forEach items="${searchResults}" var="taxonConcept"><taxon>
<id>${taxonConcept.key}</id>
<scientificName>${taxonConcept.taxonName}</scientificName><c:if test="${not empty taxonConcept.commonName}">
<commonName><gbif:capitalizeFirstChar>${taxonConcept.commonName}</gbif:capitalizeFirstChar></commonName></c:if>
<mapHTML><![CDATA[
<iframe src="http://${header.host}${pageContext.request.contextPath}/species/${taxonConcept.key}/mapWidget${mapUrlParams}" 
	frameborder="0" 
	hspace="1" 
	vspace="1" 
	scrolling="no"
	${iframeDimensions}
></iframe>
]]></mapHTML>
</taxon>
</c:forEach></taxa>
</string:trim>