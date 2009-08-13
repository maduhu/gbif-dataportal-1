<%@ include file="/common/taglibs.jsp"%>

<a class="anchorHeading" name="Names1"><h2 class="scNames"><spring:message code="blanket.search.scientific.names.title" text="Scientific names"/></h2></a>

<table cellspacing="1" width="100%">
	<tbody>

	<% // Display exact canonical matches first %>	
	<c:set var="searchResults" value="${exactTaxonMatches}" scope="request"/>
	<tiles:insert page="taxaList.jsp"/>
	
	<% // Display exact epithet matches %>
	<c:set var="searchResults" value="${exactEpithetMatches}" scope="request"/>
	<tiles:insert page="taxaList.jsp"/>
	
	<% // Display fuzzy canonical name matches %>
	<c:set var="searchResults" value="${fuzzyTaxonMatches}" scope="request"/>
	<tiles:insert page="taxaList.jsp"/>

	<% // Display fuzzy remote concept matches %>
	<c:set var="searchResults" value="${remoteConceptMatches}" scope="request"/>
	<tiles:insert page="taxaList.jsp"/>
	
	</tbody>
</table>

<% // Display the sound ex search if there are any %>
<c:if test="${not empty soundexNameMatches}">
	<tiles:insert page="soundexNamesList.jsp"/>
</c:if>

<c:if test="${ empty exactTaxonMatches && empty exactEpithetMatches &&	empty fuzzyTaxonMatches && empty soundexNameMatches && empty remoteConceptMatches}">
	<span class="moreMatches"><spring:message code="blanket.search.scientific.name.nomatches" text="No scientific name matches for"/> <span class="subject">"${searchString}"</span></span>
</c:if>

<c:if test="${moreTaxaMatches}">
	<a class="moreMatches" href="${pageContext.request.contextPath}/search/taxa/${searchString}"><spring:message code="blanket.search.scientific.names.viewall" text="View all scientific name matches for"/> "${searchString}"</a>
</c:if>