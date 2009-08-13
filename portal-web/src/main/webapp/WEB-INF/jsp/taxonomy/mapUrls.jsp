<%@ include file="/common/taglibs.jsp"%>
<% //Used by taxon quick search %>
<taxons>
<c:forEach items="${searchResults}" var="taxonConcept">
	<taxon>
		<name>${taxonConcept.taxonName}</name>
		<commonName><gbif:capitalizeFirstChar>${taxonConcept.commonName}</gbif:capitalizeFirstChar></commonName>
		<key>${taxonConcept.key}</key>
		<url>species/${taxonConcept.key}/overviewMap.png</url>
	</taxon>
</c:forEach>	
</taxons>