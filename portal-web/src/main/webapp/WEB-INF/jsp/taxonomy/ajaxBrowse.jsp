<%@ include file="/common/taglibs.jsp"%>
<c:choose>
	<c:when test="${not empty concepts}">
			<gbif:ajaxTaxonomyBrowser 
				concepts="${concepts}" 
				rootUrl="provider/${dataProvider.key}" 
				highestRank="kingdom"
				addOverviewLink="false"
				selectedConcept="${taxonConcept}"
				containerDivId="${param['containerDivId']}"
				callback="${param['callback']}"
				markConceptBelowThreshold="${param['markConceptBelowThreshold']!=null ? param['markConceptBelowThreshold'] : true}"
				messageSource="${messageSource}"
			/>								
	</c:when>
	<c:otherwise>		
		<spring:message code="taxonomy.browser.notree"/>
	</c:otherwise>	
</c:choose>