<%@ page contentType="text/javascript" %><%@ include file="/common/taglibs.jsp"%><string:trim><tiles:insert page="mapSize.jsp" flush="false"/><c:if test="${not empty callback}">${callback}(</c:if>{ "Resultset":{
	"totalResultsReturned":"${fn:length(searchResults)}",
	"Result":[<c:forEach items="${searchResults}" var="taxonConcept" varStatus="tcStatus"><c:if test="${tcStatus.index>0}">,</c:if>{	
		"id":"${taxonConcept.key}",
		"scientificName":"${taxonConcept.taxonName}",
		"commonName":"<gbif:capitalizeFirstChar>${taxonConcept.commonName}</gbif:capitalizeFirstChar>",
		"mapHTML":"<iframe src=\"http://${header.host}${pageContext.request.contextPath}/species/${taxonConcept.key}/mapWidget${mapUrlParams}\" frameborder=\"0\" hspace=\"1\" vspace=\"1\" scrolling=\"no\"	${iframeDimensionsEscaped}></iframe>"
	}</c:forEach>]
}
}
</string:trim><c:if test="${not empty callback}">)</c:if>