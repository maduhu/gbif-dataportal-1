<%@ page contentType="text/javascript" %><%@ include file="/common/taglibs.jsp"%><c:if test="${not empty callback}">${callback}(</c:if>{ "Resultset":{
	"totalResultsReturned":"${fn:length(searchResults)}",
	"Result":[
<c:forEach items="${searchResults}" var="country" varStatus="countryStatus">
<c:if test="${countryStatus.index>0}">,</c:if>
	{	
		"id":"${country.key}",
		"name":"${country.name}",
		"count":"${country.count}",
	}
</c:forEach>
	]
}
}<c:if test="${not empty callback}">)</c:if>
