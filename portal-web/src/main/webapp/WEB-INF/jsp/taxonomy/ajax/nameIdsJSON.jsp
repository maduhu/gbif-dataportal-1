<%@ page contentType="text/json" %><%@ include file="/common/taglibs.jsp"%><c:if test="${not empty callback}">${callback}(</c:if>{ "Resultset":{
	"totalResultsReturned":"${fn:length(searchResults)}",
	"Result":[
<c:forEach items="${searchResults}" var="taxonConcept" varStatus="tcStatus">
<c:if test="${tcStatus.index>0}">,</c:if>
	{	
		"id":"${taxonConcept.key}",
		"scientificName":"${taxonConcept.taxonName}",
		"commonName":"<gbif:capitalizeFirstChar>${taxonConcept.commonName}</gbif:capitalizeFirstChar>"
	}
</c:forEach>
	]
}
}<c:if test="${not empty callback}">)</c:if>