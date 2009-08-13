<%@ page contentType="text/javascript" %><%@ include file="/common/taglibs.jsp"%><string:trim><tiles:insert page="mapSize.jsp" flush="false"/><c:if test="${not empty callback}">${callback}(</c:if>{ "Resultset":{
	"totalResultsReturned":"${fn:length(searchResults)}",
	"Result":[<c:forEach items="${searchResults}" var="dataset" varStatus="tcStatus"><c:if test="${tcStatus.index>0}">,</c:if>{	
		"id":"${dataset.key}",
		"name":"${dataset.name}",
		"mapHTML":"<iframe src=\"http://${header.host}${pageContext.request.contextPath}/datasets/<gbiftag:printResourceType dataset="${dataset}"/>/${dataset.key}/mapWidget${mapUrlParams}\" frameborder=\"0\" hspace=\"1\" vspace=\"1\" scrolling=\"no\"	${iframeDimensionsEscaped}></iframe>"
	}</c:forEach>]
}
}
</string:trim><c:if test="${not empty callback}">)</c:if>