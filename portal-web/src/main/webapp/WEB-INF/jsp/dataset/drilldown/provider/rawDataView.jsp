<%@ include file="/common/taglibs.jsp"%>
<div id="twopartheader">
	<h2>
		<spring:message code="raw.data.title"/>
		- <a href="${pageContext.request.contextPath}/datasets/resource/${dataResource.key}"><span class="subject">${dataResource.name}</span></a>
		<c:if test="${dataResource.logoUrl!=null}">
			<gbiftag:scaleImage imageUrl="${dataResource.logoUrl}" maxWidth="200" maxHeight="80" imgClass="logo" addLink="false"/>
		</c:if>
	</h2>
</div>
<div id="warning" class="warning">
	<p>
	 <spring:message code="raw.data.info"/>
	</p>
	<p>
	 <spring:message code="raw.data.record.viewer"/>
	</p>
</div>
<div id="logConsole" style="margin-top:10px;">
<form id="logConsoleForm" method="get" action="">
<fieldset>
<p>
	<label for="catalogueNumber"><spring:message code="occurrence.search.results.catalogueno"/></label>
	<input type="text" name="catalogueNumber" value="${fn:escapeXml(param['catalogueNumber'])}"/>
</p>
	<input type="submit" value="Search"/>
</fieldset>

</form>
</div><!-- recordConsole -->

<display:table 
	name="results" 
	export="false" 	
	class="results" 
	uid="rawOccurrenceRecord" 
	requestURI="?"
	sort="external"
	defaultsort="1"
	pagesize="100"
	>
  <display:column title="Record id">
  		<a href="${pageContext.request.contextPath}/occurrences/datatracking/${rawOccurrenceRecord.key}">${rawOccurrenceRecord.key}</a>
  </display:column>
 	<display:column titleKey="occurrence.search.results.catalogueno">
  		${fn:escapeXml(rawOccurrenceRecord.catalogueNumber)}
  </display:column>	
  <display:column titleKey="occurrence.search.results.name">
  		${rawOccurrenceRecord.scientificName}
  </display:column>	
  <display:column titleKey="taxonrank.kingdom">
  		${rawOccurrenceRecord.kingdom}
  </display:column>	
  <display:column titleKey="taxonrank.phylum">
  		${rawOccurrenceRecord.phylum}
  </display:column>	
  <display:column titleKey="taxonrank.class">
  		${rawOccurrenceRecord.bioClass}
  </display:column>	
  <display:column titleKey="taxonrank.order">
  		${rawOccurrenceRecord.order}
  </display:column>	
  <display:column titleKey="taxonrank.family">
  		${rawOccurrenceRecord.family}
  </display:column>	
  <display:column titleKey="taxonrank.genus">
  		${rawOccurrenceRecord.genus}
  </display:column>	
  <display:column titleKey="taxonrank.species">
  		${rawOccurrenceRecord.species}
  </display:column>	
  <display:column titleKey="occurrence.record.geospatial.latitude">
  		${rawOccurrenceRecord.latitude}
  </display:column>	
  <display:column titleKey="occurrence.record.geospatial.longitude">
  		${rawOccurrenceRecord.longitude}
  </display:column>	
  <display:column titleKey="country">
  		${rawOccurrenceRecord.country}
  </display:column>	
  <display:setProperty name="pagination.pagenumber.param">pageno</display:setProperty>
  <display:setProperty name="paging.banner.placement">both</display:setProperty>	
</display:table>