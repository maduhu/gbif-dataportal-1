<%@ include file="/common/taglibs.jsp"%>
<a name="Countries"><h2 class="scCountries"><spring:message code="blanket.search.geographical.areas.title"/></h2></a>
<table cellspacing="1" width="100%">
	<tbody>
	<c:set var="searchResults" value="${countryMatches}" scope="request"/>
	<tiles:insert page="countriesList.jsp"/>
	</tbody>
</table>
<c:if test="${empty countryMatches}">
	<span class="moreMatches"><spring:message code="blanket.search.geospatial.nomatches"/> 
	<span class="subject">"${searchString}"</span></span>
</c:if>

<c:if test="${moreCountryMatches}">
	<a class="moreMatches" href="${pageContext.request.contextPath}/search/countries/${searchString}"><spring:message code="blanket.search.countries.viewall" text="View all country matches for"/> "${searchString}"</a>
</c:if>	
