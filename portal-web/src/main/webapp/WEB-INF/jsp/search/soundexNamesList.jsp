<%@ include file="/common/taglibs.jsp"%>
<p class="soundex">
	<span class="moreMatches"><spring:message code="blanket.search.scientific.name.nomatches" text="No scientific name matches for"/> <span class="subject">"${searchString}"</span>.</span>
	<span class="didyoumean"><spring:message code="blanket.search.name.did.you.mean"/></span>
</p>
<ul class="searchResults">
<c:forEach items="${soundexNameMatches}" var="taxonName">
	<li><a href="${pageContext.request.contextPath}/search/${taxonName}">${taxonName}</a></li>
</c:forEach>
</ul>