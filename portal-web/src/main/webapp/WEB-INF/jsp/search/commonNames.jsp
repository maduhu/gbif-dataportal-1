<%@ include file="/common/taglibs.jsp"%>
<a name="Names2"><h2 class="scNames"><spring:message code="blanket.search.common.names.title" text="Common names"/></h2></a>
<table cellspacing="1" width="100%">
	<tbody>

	<% // Display exact Common Name matches first %>
	<c:set var="searchResults" value="${exactCommonNameMatches}" scope="request"/>
	<tiles:insert page="commonNamesList.jsp"/>
	
	<% // Display fuzzy Common Name matches  %>
	<c:set var="searchResults" value="${fuzzyCommonNameMatches}" scope="request"/>
	<tiles:insert page="commonNamesList.jsp"/>
	
	<% // Display ending with Common Name matches  %>
	<c:set var="searchResults" value="${endingWithCommonNameMatches}" scope="request"/>
	<tiles:insert page="commonNamesList.jsp"/>	

	</tbody>	
</table>

<c:if test="${ empty exactCommonNameMatches && empty fuzzyCommonNameMatches &&	empty endingWithCommonNameMatches }">
	<span class="moreMatches"><spring:message code="blanket.search.common.name.nomatches" text="No common name matches for"/> <span class="subject">"${searchString}"</span></span>
</c:if>
<c:if test="${moreCommonNameMatches}">
	<a class="moreMatches" href="${pageContext.request.contextPath}/search/commonNames/${searchString}"><spring:message code="blanket.search.common.names.viewall" text="View all common name matches for"/> "${searchString}"</a>
</c:if>