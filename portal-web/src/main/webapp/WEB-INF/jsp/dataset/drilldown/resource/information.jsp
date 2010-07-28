<%@ include file="/common/taglibs.jsp"%>
<fieldset>
<c:if test="${not empty dataResource.name}"><p><label><spring:message code="name"/>:</label>${dataResource.name}</p></c:if>
<c:if test="${not empty dataResource.websiteUrl}"><p><label><spring:message code="website"/>:</label><a href="<c:if test="${fn:length(ataResource.websiteUrl)>6 && !fn:startsWith(dataResource.websiteUrl, 'http://')}">http://</c:if>${dataResource.websiteUrl}">${dataResource.websiteUrl}</a></p></c:if>
<c:if test="${not empty dataResource.description}"><p><label><spring:message code="description"/>:</label>
<table border="0" cellpadding="0" cellspacing="0"><tr><td>
<gbif:formatText content="${dataResource.description}"/>
</td></tr></table>
</p></c:if>
<c:if test="${not empty dataResource.rights}"><p><label><spring:message code="rights"/>:</label><gbif:formatText content="${dataResource.rights}"/></p></c:if>

<%-- 
<c:if test="${not empty dataResource.citation}"><p><label for="description"><spring:message code="citation"/>:</label>${dataResource.citation}</p></c:if>
<c:if test="${not empty citationText}"><p><label for="description"><spring:message code="how.to.cite"/>:</label>${citationText}</p></c:if>
--%> 
 
<c:set var="currentDate"><gbif:currentDate/></c:set>
<c:set var="dataResourceLink"><a href="${urlBase}/datasets/resource/${dataResource.key}">${urlBase}/datasets/resource/${dataResource.key}</a></c:set>
<c:choose>
 <c:when test="${dataResource.overrideCitation}">
    <c:if test="${not empty dataResource.citation}">
		<p><label for="citation"><spring:message code="citation.supplied"/>:</label>${dataResource.citation}</p>
	</c:if>
	<p><label for="citation"><spring:message code="how.to.cite"/>:</label>${dataResource.dataProviderName}, ${dataResource.name} <spring:message code="citation.entry" arguments="${dataResourceLink}%%%${currentDate}" argumentSeparator="%%%"/></p> 
 </c:when>
 <c:when test="${fn:length(dataResource.citation)==0}">
	<p><label for="citation"><spring:message code="how.to.cite"/>:</label>${dataResource.dataProviderName}, ${dataResource.name} <spring:message code="citation.entry" arguments="${dataResourceLink}%%%${currentDate}" argumentSeparator="%%%"/></p>
 </c:when>
 <c:otherwise>
	<p><label for="citation"><spring:message code="how.to.cite"/>:</label>${dataResource.citation} <spring:message code="citation.entry" arguments="${dataResourceLink}%%%${currentDate}" argumentSeparator="%%%"/></p>
 </c:otherwise> 
</c:choose>


<c:if test="${not empty dataResource.basisOfRecord}"><p><label for="basisOfRecord"><spring:message code="basis.of.record"/>:</label><spring:message code="basis.of.record.${dataResource.basisOfRecord}" text="${dataResource.basisOfRecord}"/></p></c:if>
<c:if test="${not empty dataResource.scopeCountryCode}"><p><label for="scopeCountry"><spring:message code="scope.country" text=""/>:</label><a href="${pageContext.request.contextPath}/countries/${dataResource.scopeCountryCode}"><spring:message code="country.${dataResource.scopeCountryCode}" text=""/></a></p></c:if>
<c:if test="${not empty dataResource.scopeContinentCode}"><p><label for="scopeContinentCode"><spring:message code="scope.continent" text=""/>:</label><spring:message code="continent.${dataResource.scopeContinentCode}" text=""/></p></c:if>
<c:if test="${not empty dataResource.rootTaxonRank && not empty dataResource.rootTaxonName}"><p><label for="taxonScope"><spring:message code="scope.taxonomic"/>:</label>${dataResource.rootTaxonRank}: ${dataResource.rootTaxonName}</p></c:if>
<c:forEach items="${resourceAccessPoints}" var="resourceAccessPoint">
	<p><label><spring:message code="dataset.accessPointUrl"/>:</label><a href="${resourceAccessPoint.url}">${resourceAccessPoint.url}</a></p>
</c:forEach>
<c:if test="${not empty dataResource.created}"><p><label><spring:message code="date.added"/>:</label><fmt:formatDate value="${dataResource.created}"/></p></c:if>	
<c:if test="${not empty dataResource.modified}"><p><label><spring:message code="last.modified"/>:</label><fmt:formatDate value="${dataResource.modified}"/></p></c:if>	
</fieldset>
  