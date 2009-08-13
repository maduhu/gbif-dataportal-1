<%@ page contentType="text/xml" %><%@ include file="/common/taglibs.jsp"%><?xml version="1.0" encoding="UTF-8"?>
<classification <c:if test="${not empty selectedConcept}">name="${selectedConcept.taxonName}"</c:if>
  providedBy="${taxonomicProvider}">
<c:if test="${not empty concepts}">
<c:forEach items="${concepts}" var="concept">	
<taxon>
	<c:if test="${lastKey == concept.parentConceptKey}">
		<c:set var="childrenAdded" value="true"/>
	</c:if>
		<id>${concept.key}</id>		
		<name>${concept.taxonName}</name>
		<parentId>${concept.parentConceptKey}</parentId>				
		<c:set var="lastKey">${concept.key}</c:set>
</taxon>		
</c:forEach>
</c:if>
</classification>
