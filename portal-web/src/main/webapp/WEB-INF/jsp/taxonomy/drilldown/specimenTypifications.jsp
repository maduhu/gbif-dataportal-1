<%@ include file="/common/taglibs.jsp"%>
<c:if test="${not empty typifications}">
<h4><spring:message code="specimen.types"/></h4>
<display:table
	name="typifications" 
	class="results" 
	uid="typification" 
	sort="external"
	defaultsort="1"
>
  <display:column titleKey="catalogue.number">
  	<a href="${pageContext.request.contextPath}/occurrences/${typification.occurrenceRecordKey}">${typification.occurrenceRecordCatalogueNumber}</a>
  </display:column>
  <display:column property="typeStatus" titleKey="type.status"/>
  <display:column property="scientificName" titleKey="type.scientific.name"/>
  <display:column titleKey="data.provider">
  	<a href="${pageContext.request.contextPath}/datasets/provider/${typification.dataProviderKey}">${typification.dataProviderName}</a>
  </display:column>
  <display:column titleKey="dataset">
  	<a href="${pageContext.request.contextPath}/datasets/resource/${typification.dataResourceKey}">${typification.dataResourceName}</a>
  </display:column>
  
  
</display:table>
</c:if>