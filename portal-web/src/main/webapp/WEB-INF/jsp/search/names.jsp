<%@ include file="/common/taglibs.jsp"%>
<h2><spring:message code="blanket.search.names.title"/></h2>
<ul class="searchResults">
<% // Display Exact Scientific Name matches first %>
<c:set var="taxonMatches" value="${exactTaxonMatches}" scope="request"/>
<tiles:insert page="taxaList.jsp"/>

<% // Display Exact Common Name matches %>
<c:set var="commonNameMatches" value="${exactCommonNameMatches}" scope="request"/>
<tiles:insert page="commonNamesList.jsp"/>

<% // Display fuzzy Scientific Name matches first %>
<c:set var="taxonMatches" value="${fuzzyTaxonMatches}" scope="request"/>
<tiles:insert page="taxaList.jsp"/>

<% // Display fuzzy Common Name matches first %>
<c:set var="commonNameMatches" value="${fuzzyCommonNameMatches}" scope="request"/>
<tiles:insert page="commonNamesList.jsp"/>
</ul>

<% // Display the sound ex search if there are any %>
<c:if test="${soundexNameMatches != null && !empty soundexNameMatches}">
<tiles:insert page="soundexNamesList.jsp"/>
</c:if>

<c:if test="${ (exactTaxonMatches==null || empty exactTaxonMatches) && (soundexNameMatches==null || empty soundexNameMatches) &&	(exactCommonNameMatches==null || empty exactCommonNameMatches) &&	(fuzzyTaxonMatches==null || empty fuzzyTaxonMatches) && (fuzzyCommonNameMatches==null || empty fuzzyCommonNameMatches) }">
	<spring:message code="blanket.search.name.nomatches"/> ${searchString}
</c:if>

<c:if test="${moreNameMatches}">
	<br/>
	<a href="${pageContext.request.contextPath}/search/names/${searchString}"><spring:message code="blanket.search.names.viewall"/> "${searchString}"</a>
</c:if>