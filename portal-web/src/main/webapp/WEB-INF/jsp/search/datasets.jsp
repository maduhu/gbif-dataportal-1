<%@ include file="/common/taglibs.jsp"%>
<a name="Datasets"><h2 class="scDatasets"><spring:message code="blanket.search.data.resources.title"/></h2></a>
<table cellspacing="1" width="100%">
	<tbody>
	<tiles:insert page="datasetsList.jsp"/>
	</tbody>
</table>
<c:if test="${fn:length(datasetMatches)==0}">
	<span class="moreMatches"><spring:message code="blanket.search.publication.nomatches"/> 
	<span class="subject">"${searchString}"</span></span>
</c:if>

<c:if test="${moreDatasetMatches}">
	<a class="moreMatches" href="${pageContext.request.contextPath}/search/datasets/${searchString}"><spring:message code="blanket.search.datasets.viewall" text="View all dataset matches for"/> "${searchString}"</a>
</c:if>