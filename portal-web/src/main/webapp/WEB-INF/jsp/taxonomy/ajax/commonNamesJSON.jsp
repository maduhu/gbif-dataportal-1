<%@ page contentType="text/javascript" %><%@ include file="/common/taglibs.jsp"%><c:if test="${not empty callback}">${callback}(</c:if>{ "Resultset":{
	"totalResultsReturned":"${fn:length(searchResults)}",
	"Result":[
<c:forEach items="${searchResults}" var="commonName" varStatus="tcStatus">
<c:if test="${tcStatus.index>0}">,</c:if>
	{	
		"id":"${commonName.taxonConceptKey}",			
		"commonName":"<gbif:capitalizeFirstChar>${commonName.name}</gbif:capitalizeFirstChar>",
		"language":"${commonName.language}",
		"scientificName":"${commonName.taxonName}"
	}
</c:forEach>
	]
}
}<c:if test="${not empty callback}">)</c:if>


	