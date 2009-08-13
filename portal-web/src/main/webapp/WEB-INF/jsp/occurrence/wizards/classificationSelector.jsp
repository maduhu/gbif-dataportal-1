<%@ include file="/common/taglibs.jsp"%>
<div id="classificationSelector" class="taxonomyWizard">

	<span id="progress" style="float:right;visibility:hidden;"><img src="${pageContext.request.contextPath}/images/loading.gif"/></span>
	<p style="margin-bottom:10px;">
		<c:set var="addFilterMsg"><spring:message code="search.filter.add.filter"/></c:set>
		<spring:message code="classification.wizard.help" arguments="${addFilterMsg}" argumentSeparator="$$$"/>
	</p>

	<c:choose>
	<c:when test="${not empty concepts}">
		<div id="smalltree" class="smalltree">
			<gbif:ajaxTaxonomyBrowser 
				concepts="${concepts}" 
				rootUrl="provider/${dataProvider.key}"
				addOverviewLink="false"
				highestRank="kingdom"
				containerDivId="smalltree"
				callback="classificationFilterCallback"
				markConceptBelowThreshold="true"
				messageSource="${messageSource}"
			/>
		</div>
	</c:when>
	<c:otherwise>
		<spring:message code="classification.wizard.tree.unavailable"/>
	</c:otherwise>
	</c:choose>	
</div>