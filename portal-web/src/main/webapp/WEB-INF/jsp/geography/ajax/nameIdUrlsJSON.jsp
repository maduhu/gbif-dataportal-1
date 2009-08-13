<%@ page contentType="text/javascript" %><%@ include file="/common/taglibs.jsp"%><c:if test="${not empty callback}">${callback}(</c:if>{ "Resultset":{
	"totalResultsReturned":"${fn:length(searchResults)}",
	"Result":[
<c:forEach items="${searchResults}" var="country" varStatus="tcStatus">
<c:if test="${tcStatus.index>0}">,</c:if>
	{	
		"id":"${country.key}",
		"name":"<gbif:capitalize>${country.name}</gbif:capitalize>"
		"url":"http://${header.host}${pageContext.request.contextPath}/country/${country.key}"		
	}
</c:forEach>
	]
}
}<c:if test="${not empty callback}">)</c:if>