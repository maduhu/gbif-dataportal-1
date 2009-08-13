<%@ include file="/common/taglibs.jsp"%>

<form class="aggregateCounts" name="countryCounts" method="get" action="${pageContext.request.contextPath}/occurrences/search.htm">

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
	requestURI="/occurrences/searchCountries.htm?" 
	uid="count" 
>
  <display-el:column titleKey="occurrence.search.filter.results.table.counts.include" class="include">
  	<c:if test="${not empty count.name && fn:length(count.name)>0}"> 
	 	 <input type="checkbox" name="cn" value="${count.key}"/>
	 	</c:if> 
  </display-el:column>	
  <display-el:column titleKey="occurrence.search.filter.results.table.counts.country">
  	 <c:choose>
			<c:when test="${not empty count.name && fn:length(count.name)>0}">	
				<a href="${pageContext.request.contextPath}/countries/${count.key}"><gbif:capitalize><spring:message code="country.${count.key}" text="${count.name}"/></gbif:capitalize></a> 
			</c:when>
			<c:otherwise>
				<spring:message code="country.not.specified" text="Country not specified"/>
			</c:otherwise>
		</c:choose>
	</display-el:column>
  <display-el:column title="${occurrenceCountTitle}" sortProperty="count" class="sortableColumn" sortable="false" >
  	<c:if test="${countsAvailable}"><fmt:formatNumber value="${count.count}" pattern="###,###"/></c:if>
  </display-el:column>  	  
  <display-el:setProperty name="basic.msg.empty_list"><spring:message code="occurrence.search.filter.nonefound"/></display-el:setProperty>	  
  <display-el:setProperty name="paging.banner.onepage"> </display-el:setProperty>	  
  <display-el:setProperty name="basic.empty.showtable">true</display-el:setProperty>	 
</display-el:table>

<input id="refineSearch" type="submit" value="<spring:message code="refine.search"/>"/>
</form>