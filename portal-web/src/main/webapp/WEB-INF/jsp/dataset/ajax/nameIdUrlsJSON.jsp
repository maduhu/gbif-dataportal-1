<%@ page contentType="text/javascript" %><%@ include file="/common/taglibs.jsp"%><c:if test="${not empty callback}">${callback}(</c:if>{ "Resultset":{
	"totalResultsReturned":"${fn:length(searchResults)}",
	"Result":[
<c:forEach items="${searchResults}" var="dataset" varStatus="tcStatus">
<c:if test="${tcStatus.index>0}">,</c:if>
	{	
		"id":"${dataset.key}",
		"name":"${dataset.name}",
		"url":"http://${header.host}${pageContext.request.contextPath}/datasets/<gbiftag:printResourceType dataset="${dataset}"/>/${dataset.key}"		
	}
</c:forEach>
	]
}
}<c:if test="${not empty callback}">)</c:if>