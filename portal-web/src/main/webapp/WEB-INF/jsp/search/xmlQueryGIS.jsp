<%@ include file="/common/taglibs.jsp"%><response>
	<datasource>
		<dbsource>Global Biodiversity Information Facility (GBIF)</dbsource>
		<dbwebaddress>http://${header.host}${pageContext.request.contextPath}/</dbwebaddress>
		<dbdatadate>2007-09-12</dbdatadate>
		<dbcurrentdate>2007-11-01</dbcurrentdate>
		<dbexpirydate/>
		<dbtermsofuse>Please read the following webpage for information on using this data: http://${header.host}tutorial/datauseagreement</dbtermsofuse>
	</datasource>
	<taxa>
<c:if test="${not empty searchResults}">
	<c:set var="currentTaxonConceptKey" value=""/>
	<c:set var="taxonConceptIndex" value="0"/>
	<c:forEach items="${searchResults}" var="occurrenceRecord">
	
<c:if test="${occurrenceRecord.nubTaxonConceptKey!=currentTaxonConceptKey}">
	<c:if test="${not empty currentTaxonConceptKey}">
	   </records>
	  </taxon>
	</c:if>
		<c:set var="currentTaxonConceptKey" value="${occurrenceRecord.nubTaxonConceptKey}"/>
		<c:set var="taxonConcept" value="${concepts[taxonConceptIndex]}"/>
		<c:set var="taxonConceptIndex" value="${taxonConceptIndex+1}"/>
		<taxon>
			<taxonkey>${occurrenceRecord.nubTaxonConceptKey}</taxonkey>
			<taxonname>${taxonConcept.taxonName}</taxonname>
			<rank><string:capitalize>${taxonConcept.rank}</string:capitalize></rank>
			<kingdom><string:capitalize>${taxonConcept.kingdom}</string:capitalize></kingdom>
			<recordcount>${recordCounts[taxonConceptIndex]}</recordcount>
			<records>
		</c:if><record lat="${occurrenceRecord.latitude}" 
		long="${occurrenceRecord.longitude}" 
		precision="" 
		year="<fmt:formatDate value="${occurrenceRecord.occurrenceDate}" pattern="yyyy"/>">http://${header.host}${pageContext.request.contextPath}/occurrences/${occurrenceRecord.key}/</record>
	 </c:forEach> 
</c:if>	  
			</records>
		</taxon>
	</taxa>
</response>