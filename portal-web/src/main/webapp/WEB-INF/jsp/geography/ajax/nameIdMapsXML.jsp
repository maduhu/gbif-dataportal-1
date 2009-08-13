<%@ page contentType="text/xml" %><%@ include file="/common/taglibs.jsp"%><?xml version="1.0" encoding="UTF-8"?>
<string:trim><tiles:insert page="mapSize.jsp" flush="false"/>
<countries>
<c:forEach items="${searchResults}" var="country"><country>
<id>${country.key}</id>
<name><gbif:capitalize>${country.name}</gbif:capitalize></name>
<mapHTML><![CDATA[
<iframe src="http://${header.host}${pageContext.request.contextPath}/countries/${country.key}/mapWidget${mapUrlParams}" 
	frameborder="0" 
	hspace="1" 
	vspace="1" 
	scrolling="no"
	${iframeDimensions}
></iframe>
]]></mapHTML>
</country>
</c:forEach></countries>
</string:trim>