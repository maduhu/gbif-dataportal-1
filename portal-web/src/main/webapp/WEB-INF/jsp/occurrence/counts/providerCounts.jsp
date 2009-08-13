<%@ include file="/common/taglibs.jsp"%>

<form class="aggregateCounts" name="providerCounts" method="get" action="${pageContext.request.contextPath}/occurrences/search.htm">
	<c:forEach items="${criteria}" var="criterion" varStatus="criterionStatus">
		<input name="c[${criterionStatus.index}].s" type="hidden" value="${criterion.subject}"/>
		<input name="c[${criterionStatus.index}].p" type="hidden" value="${criterion.predicate}"/>
		<input name="c[${criterionStatus.index}].o" type="hidden" value="${criterion.value}"/>
	</c:forEach>		

<c:if test="${countsAvailable}">
	<c:set var="occurrenceCountTitle"><spring:message code="occurrences" text="Occurrences"/></c:set>
</c:if>	

<display-el:table 
	name="results" 
	class="results" 
	uid="count"
	requestURI="/occurrences/searchProviders.htm?"
	defaultsort="2"
>
  <display-el:column titleKey="occurrence.search.filter.results.table.counts.include" class="include">
 	 <input type="checkbox" name="dp" value="${count.key}"/>
  </display-el:column>	
  <display-el:column titleKey="occurrence.search.filter.results.table.counts.provider" sortProperty="name" sortable="true" class="sortableColumn">
		<a href="${pageContext.request.contextPath}/datasets/provider/${count.key}">${count.name}</a><br/>
  </display-el:column>	
  <display-el:column title="${occurrenceCountTitle}" sortProperty="count" class="sortableColumn" sortable="${countsAvailable}">
  	<c:if test="${countsAvailable}"><fmt:formatNumber value="${count.count!=null ? count.count : count.occurrenceCount}" pattern="###,###"/></c:if>
  </display-el:column>  
  <display-el:setProperty name="basic.msg.empty_list"><spring:message code="occurrence.search.filter.nonefound"/></display-el:setProperty>	  
  <display-el:setProperty name="paging.banner.onepage"> </display-el:setProperty>	  
  <display-el:setProperty name="basic.empty.showtable">true</display-el:setProperty>  	 
</display-el:table>


<input id="refineSearch" class="refineSearch" type="submit" value="<spring:message code="refine.search"/>"/>
</form>