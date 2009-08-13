<%@ page contentType="text/javascript" %><%@ include file="/common/taglibs.jsp"%><c:if test="${not empty callback}">${callback}(</c:if>{ "Resultset":{
	"totalResultsReturned":"${fn:length(searchResults)}",
	"Result":[
<c:forEach items="${searchResults}" var="dataset" varStatus="datasetStatus">
<c:if test="${datasetStatus.index>0}">,</c:if>
	{	
		"id":"${dataset.key}",
		"name":"${dataset.name}",
		"count":"${dataset.count}",
		"provider":"${dataset.properties[0]}"
	}
</c:forEach>
	]
}
}<c:if test="${not empty callback}">)</c:if>
