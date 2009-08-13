<%@ include file="/common/taglibs.jsp"%>
<h4><spring:message code="taxonomy.search.results"/></h4>
<display:table 
	name="results" 
	export="false" 
	class="results" 
	uid="taxonConcept" 
	requestURI="/species/search.htm?"
	sort="external"
	defaultsort="1"
	pagesize="20"
	size="resultSize"
>
  <display:column titleKey="taxon">
  	<a href="${pageContext.request.contextPath}/species/browse/taxon/${taxonConcept.key}/"
  			title="<spring:message code="taxonomy.search.results.viewlink.title" arguments="${taxonConcept.dataResourceName}"/>"
  		><gbif:taxonPrint concept="${taxonConcept}"/></a>
  </display:column>	
  <display:column titleKey="author">
  	<string:capitalize>${taxonConcept.author}</string:capitalize>
  </display:column>	
  <display:column titleKey="accepted.name">
  	<c:choose>
	  	<c:when test="${taxonConcept.isAccepted}">
	  		<spring:message code="Y"/>
	  	</c:when>
	  	<c:otherwise>
	  		<spring:message code="N"/>
	  	</c:otherwise>  	
  	</c:choose>
  </display:column>	
  <display:column titleKey="rank">
  	<string:capitalize>${taxonConcept.rank}</string:capitalize>
  </display:column>	
  <display:column titleKey="taxonomy.search.filter.dataset.dataresource">
  	${taxonConcept.dataResourceName}
  </display:column>	
  <display:column titleKey="taxonrank.kingdom">
  	${taxonConcept.kingdom}
  </display:column>	
  <display:column titleKey="taxonrank.phylum">
  	${taxonConcept.phylumDivision}
  </display:column>	
  <display:column titleKey="taxonrank.class">
  	${taxonConcept.klass}
  </display:column>	
  <display:column titleKey="taxonrank.order">
  	${taxonConcept.order}
  </display:column>	
  <display:column titleKey="taxonrank.family">
  	${taxonConcept.family}
  </display:column>	
  <display:column titleKey="taxonrank.genus">
  	<span class="genera">${taxonConcept.genus}</span>
  </display:column>	
  <display:column titleKey="taxonrank.species">
  	<span class="genera">${taxonConcept.species}</span>
  </display:column>	
  <display:column class="lastColumn">
		<a href="${pageContext.request.contextPath}/species/browse/taxon/${taxonConcept.key}/"
				title="<spring:message code="taxonomy.search.results.viewlink.title" arguments="${taxonConcept.dataResourceName}"/>"
			><spring:message code="taxonomy.search.results.viewlink.message"/></a>
  </display:column>	
  <display:setProperty name="basic.msg.empty_list"><spring:message code="taxonomy.search.filter.nonefound"/></display:setProperty>	  
  <display:setProperty name="paging.banner.onepage"> </display:setProperty>	  
  <display:setProperty name="basic.empty.showtable">true</display:setProperty>	 
  <display:setProperty name="basic.msg.empty_list_row">
  	<tr class="empty">
		<td colspan="10"><spring:message code="taxonomy.search.filter.nonefound"/></td>
	</tr>
	</tr>
  </display:setProperty>	  
  <display:setProperty name="paging.banner.no_items_found"> </display:setProperty>	  
  <display:setProperty name="pagination.pagenumber.param">pageno</display:setProperty>	
  <display:setProperty name="paging.banner.placement">both</display:setProperty>	
</display:table>