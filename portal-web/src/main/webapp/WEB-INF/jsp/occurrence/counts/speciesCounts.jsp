<%@ include file="/common/taglibs.jsp"%>
<display:table 
	name="results" 
	export="false" 
	class="results" 
	uid="count" 
	requestURI="/occurrences/searchSpecies.htm?"
	sort="external"
	defaultsort="1"
	pagesize="20"
	size="resultSize"
>
  <display:column titleKey="occurrence.search.filter.results.table.counts.species">
		<a href="${pageContext.request.contextPath}/species/${count.key}/">${count.name}</a>
  </display:column>	
  <display:column>
 	 <a href="${pageContext.request.contextPath}/occurrences/search.htm?<gbif:criteria criteria="${criteria}"/>&<gbif:criterion subject="0" predicate="0" value="${count.name}" index="${fn:length(criteria)+1}"/>">			
 	 	<spring:message code="occurrence.search.filter.results.countsmessage.species.justthis"/>
 	 	${count.name}
 	 </a>
  </display:column>	
  <display:setProperty name="basic.msg.empty_list"><spring:message code="occurrence.search.filter.nonefound"/></display:setProperty>	  
  <display:setProperty name="paging.banner.onepage"> </display:setProperty>	  
  <display:setProperty name="basic.empty.showtable">true</display:setProperty>	 
</display:table>