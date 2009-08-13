<%@ page contentType="text/javascript" %><%@ include file="/common/taglibs.jsp"%><c:if test="${not empty callback}">${callback}(</c:if>{"Resultset":<json:object>
  <json:property name="totalResultsReturned" value="${fn:length(searchResults)}"/>
  <json:array name="dataset" var="dataset" items="${searchResults}">
    <json:object>
      <json:property name="id" value="${dataset.key}"/>
      <json:property name="name" value="${dataset.name}"/>
	<c:if test="${param['datasetType']=='resource'}">		
			<json:property name="description" value="${dataset.description}"/>
			<json:property name="websiteUrl" value="${dataset.websiteUrl}"/>
			<json:property name="logoUrl" value="${dataset.logoUrl}"/>
			<json:property name="rights" value="${dataset.rights}"/>
			<json:property name="citation" value="${dataset.citation}"/>
			<json:property name="dataProviderId" value="${dataset.dataProviderKey}"/>
			<json:property name="dataProviderName" value="${dataset.dataProviderName}"/>
			<json:property name="occurrenceCount" value="${dataset.occurrenceCount}"/>
			<json:property name="occurrenceCoordinateCount" value="${dataset.occurrenceCoordinateCount}"/>			
			<json:property name="occurrenceCleanGeospatialCount" value="${dataset.occurrenceCleanGeospatialCount}"/>			
			<json:property name="providerUDDIRecordCount" value="${dataset.providerRecordCount}"/>			
			<json:property name="conceptCount" value="${dataset.conceptCount}"/>			
			<json:property name="basisOfRecord" value="${dataset.basisOfRecord}"/>
			<json:property name="rootTaxonRank" value="${dataset.rootTaxonRank}"/>
			<json:property name="rootTaxonName" value="${dataset.rootTaxonName}"/>
			<json:property name="scopeCountryCode" value="${dataset.scopeCountryCode}"/>
	</c:if>	      
    </json:object>
  </json:array>
</json:object>}<c:if test="${not empty callback}">)</c:if>