<%@ include file="/common/taglibs.jsp"%>

<form class="aggregateCounts" name="resourceCounts" method="get" action="${pageContext.request.contextPath}/occurrences/search.htm">
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
	requestURI="/occurrences/searchResources.htm?"
	defaultsort="2"
>
  <display-el:column titleKey="occurrence.search.filter.results.table.counts.include" class="include">
 	 <input type="checkbox" name="dr" value="${count.key}"/>
  </display-el:column>	
  <display-el:column titleKey="occurrence.search.filter.results.table.counts.resource" sortProperty="name" sortable="true" class="sortableColumn">
		<a href="${pageContext.request.contextPath}/datasets/resource/${count.key}">${count.name}</a><br/>
		<p class="resultsDetails">${count.properties[0]}</p>
  </display-el:column>	
  <display-el:column title="${occurrenceCountTitle}" sortProperty="count" class="sortableColumn" sortable="${countsAvailable}">
  	<c:if test="${countsAvailable}"><fmt:formatNumber value="${count.count!=null ? count.count : count.occurrenceCount}" pattern="###,###"/></c:if>
  </display-el:column>  
  <display-el:setProperty name="basic.msg.empty_list"><spring:message code="occurrence.search.filter.nonefound"/></display-el:setProperty>	  
  <display-el:setProperty name="paging.banner.onepage"> </display-el:setProperty>	  
  <display-el:setProperty name="basic.empty.showtable">true</display-el:setProperty>	 
  <display-el:setProperty name="locale.resolver">localeResolver</display-el:setProperty>	  
  <display-el:setProperty name="locale.provider">org.displaytag.localization.I18nSpringAdapter</display-el:setProperty>
</display-el:table>

<input id="refineSearch" type="submit" value="<spring:message code="refine.search"/>"/>
</form>