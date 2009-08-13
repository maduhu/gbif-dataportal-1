<%@ include file="/common/taglibs.jsp"%>
	<div id="twopartheader">
		<h2><spring:message code="blanket.search.header"/> <strong>${searchString}</strong> </h2>
	</div>

<c:set var="searchTitle"><tiles:getAsString name="searchTitle"/></c:set>

<c:choose>
	<c:when test="${searchTitle=='blanket.search.geographical.areas.title'}">
		<c:set var="searchClass">scCountries</c:set>
	</c:when>
	<c:when test="${searchTitle=='blanket.search.data.resources.title'}">
		<c:set var="searchClass">scDatasets</c:set>
	</c:when>
	<c:otherwise>
		<c:set var="searchClass">scNames</c:set>
	</c:otherwise>
</c:choose>

<h2 class="${searchClass}"><spring:message code="${searchTitle}"/></h2>
<table cellspacing="1" width="100%">
	<tbody>
		<c:set var="urlRoot"><tiles:getAsString name="urlRoot"/></c:set>
		<tiles:insert name="list"/>
	</tbody>
</table>

<c:if test="${(pageNo>1) || hasMoreResults}">

<div style="width:100%; text-align:center;">
	[ 
	<c:if test="${pageNo>1}">
		<a href="${pageContext.request.contextPath}/search/${urlRoot}/${searchString}/${pageNo-1}/${highWaterMark}">
	</c:if>
	<spring:message code="previous" text="Previous"/>
	<c:if test="${pageNo>1}">
		</a> 
	</c:if>	
	]
	<c:forEach begin="1" end="${highWaterMark}" varStatus="status">
		<c:if test="${status.index!=pageNo}"><a href="${pageContext.request.contextPath}/search/${urlRoot}/${searchString}/${status.index}/${highWaterMark}"></c:if>${status.index}<c:if test="${status.index!=pageNo}"></a></c:if>
	</c:forEach>
	
	[
	<c:if test="${hasMoreResults}">
		<a href="${pageContext.request.contextPath}/search/${urlRoot}/${searchString}/${pageNo+1}/${highWaterMark}">
	</c:if>
	<spring:message code="next" text="Next"/>
	<c:if test="${hasMoreResults}">	
		</a>
	</c:if>	
	]
</div>

</c:if>