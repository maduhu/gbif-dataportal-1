<%@ include file="/common/taglibs.jsp"%>
<% //Used by taxon quick search %>
<taxons>
<c:forEach items="${searchResults}" var="taxonConcept">
	<taxon>
		<name>${taxonConcept.taxonName}</name>
		<kingdom>${taxonConcept.kingdom}</kingdom>
		<url>${pageContext.request.contextPath}/species/browse/taxon/${taxonConcept.key}</url>
	</taxon>
</c:forEach>	
</taxons>