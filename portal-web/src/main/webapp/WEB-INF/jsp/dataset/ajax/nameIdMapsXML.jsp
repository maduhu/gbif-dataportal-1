<%@ page contentType="text/xml" %><%@ include file="/common/taglibs.jsp"%><?xml version="1.0" encoding="UTF-8"?>
<string:trim><tiles:insert page="mapSize.jsp" flush="false"/>
<datasets>
<c:forEach items="${searchResults}" var="dataset"><dataset>
<id>${dataset.key}</id>
<name>${dataset.name}</name>
<mapHTML><![CDATA[
<iframe src="http://${header.host}${pageContext.request.contextPath}/datasets/<gbiftag:printResourceType dataset="${dataset}"/>/${dataset.key}/mapWidget${mapUrlParams}" 
	frameborder="0" 
	hspace="1" 
	vspace="1" 
	scrolling="no"
	${iframeDimensions}
></iframe>
]]></mapHTML>
</dataset>
</c:forEach></datasets>
</string:trim>