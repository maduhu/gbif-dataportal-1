<%@ include file="/common/taglibs.jsp"%>
<div id="datasetContainer" class="datasetContainer">
	<h1><spring:message code="dataset.drilldown.main.title"/>: ${publicationStatistics.publication.title}</h1>
	<div class="subcontainer">
		<h3><spring:message code="dataset.information"/></h3>
		<tiles:insert page="drilldown/information.jsp"/>
	</div>
	<c:if test="${publicationStatistics.publication.key != defaultTaxonomy}">
		<div class="subcontainer">
			<h3><spring:message code="dataset.occurrences"/></h3>
			<tiles:insert page="drilldown/occurrences.jsp"/>
		</div>
	</c:if>
</div>
