<%@ page contentType="text/json" %><%@ include file="/common/taglibs.jsp"%><c:if test="${not empty callback}">${callback}(</c:if>{ "classificationSearch":{
	"provider":"${taxonomicProvider}",
	"classification":[<c:forEach items="${concepts}" var="concept" varStatus="nameStatus"><c:if test="${nameStatus.index>0}">,</c:if>	
	{	
		"id":"${concept.key}",
		"rank":"${concept.rank}",
		"scientificName":"${concept.taxonName}",
		"parentId":"${concept.parentConceptKey}",
		"partnerId":"${concept.partnerConceptKey}"			
<c:if test="${includeCounts}">"children":"${concept.childCount}"</c:if>		
	}
	</c:forEach>
	]
}}<c:if test="${not empty callback}">)</c:if>