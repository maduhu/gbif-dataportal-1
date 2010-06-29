<%@ include file="/common/taglibs.jsp"%>
<c:if test="${not empty resourceCounts}">
<h5><spring:message code="geography.drilldown.mapped.resources" text="Resources providing data for Map"/></h5>

<script type="text/javascript">
function toggleTables(firstTable, secondTable, visibleClass){
  var t1 = document.getElementById(firstTable);
  var t2 = document.getElementById(secondTable);
  
  if(t1.className=='hidden'){
    t1.className = visibleClass;
    t2.className = 'hidden'; 
  } else {
    t2.className = visibleClass;
    t1.className = 'hidden'; 
  }
}
</script>

<c:if test="${fn:length(resourceCounts)>4}">
<a href="javascript:toggleTables('sampleTable', 'resourceCount', 'results');" title="Show all resources"><spring:message code="show"/>/<spring:message code="hide"/></a>
</c:if>

<table id="sampleTable" class="results" style="width: 720px;">
  <thead>
    <th><spring:message code="dataset"/></th>
    <th style="text-align: center;"><spring:message code="log.console.count"/></th>
    <c:if test="${showNonGeoreferencedCount}">
    <th style="text-align: center;" class="lastColumn"><spring:message code="log.console.non.georef.count"/></th>
    </c:if>
  </thead>  
  <tbody>
    <c:forEach items="${resourceCounts}" var="resourceCount" begin="0" end="3" varStatus="rowCounter">
    <tr>
      <td style="width:650px;">
        <a href="${pageContext.request.contextPath}/datasets/resource/${resourceCount.key}">${resourceCount.name}</a>
        <p class="resultsDetails">
        ${resourceCount.properties[0]}
        </p>
      </td>
      <td class="lastColumn" style="width:70px;">
        <a href="${pageContext.request.contextPath}/occurrences/search.htm?<gbif:criterion subject="24" predicate="0" value="${resourceCount.key}" index="0"/>&<gbif:criterion subject="${occurrenceSearchSubject}" predicate="0" value="${occurrenceSearchValue}" index="1"/>&<gbif:criterion subject="28" predicate="0" value="0" index="2"/>"><fmt:formatNumber value="${resourceCount.count}" pattern="###,###"/></a>
      </td>
      <c:if test="${showNonGeoreferencedCount}">
      <td class="lastColumn" style="width:200px;">
        <a href="${pageContext.request.contextPath}/occurrences/search.htm?<gbif:criterion subject="28" predicate="0" value="1" index="0"/>&<gbif:criterion subject="${occurrenceSearchSubject}" predicate="0" value="${occurrenceSearchValue}" index="1"/>&<gbif:criterion subject="24" predicate="0" value="${resourceCount.key}" index="2"/>"><fmt:formatNumber value="${nonResourceCounts[rowCounter.index].count-resourceCounts[rowCounter.index].count}" pattern="###,###"/></a>
      </td>
      </c:if>
    </tr>
    </c:forEach>
    <c:if test="${fn:length(resourceCounts)>4}">
    <tr>
      <td colspan="3">
        <p class="showFullTable">      
          <a href="javascript:toggleTables('sampleTable', 'resourceCount', 'results');">View ${fn:length(resourceCounts)-4 } more datasets... </a>
        </p>
      </td>
    </tr>    
    </c:if>
  </tbody>
</table>

<c:if test="${fn:length(resourceCounts)>4}">
<table id="resourceCount" class="hidden" style="width: 720px;">
  <thead>
    <th><spring:message code="dataset"/></th>
    <th style="text-align: center;"><spring:message code="log.console.count"/></th>
    <c:if test="${showNonGeoreferencedCount}">
    <th style="text-align: center;" class="lastColumn"><spring:message code="log.console.non.georef.count"/></th>
    </c:if>
  </thead>  
  <tbody>
    <c:forEach items="${resourceCounts}" var="resourceCount" begin="0" varStatus="rowCounter">
    <tr>
      <td style="width:650px;">
        <a href="${pageContext.request.contextPath}/datasets/resource/${resourceCount.key}">${resourceCount.name}</a>
        <p class="resultsDetails">
        ${resourceCount.properties[0]}
        </p>
      </td>
      <td class="lastColumn" style="width:70px;">
        <a href="${pageContext.request.contextPath}/occurrences/search.htm?<gbif:criterion subject="24" predicate="0" value="${resourceCount.key}" index="0"/>&<gbif:criterion subject="${occurrenceSearchSubject}" predicate="0" value="${occurrenceSearchValue}" index="1"/>&<gbif:criterion subject="28" predicate="0" value="0" index="2"/>"><fmt:formatNumber value="${resourceCount.count}" pattern="###,###"/></a>
      </td>
      <c:if test="${showNonGeoreferencedCount}">
      <td class="lastColumn" style="width:200px;">
        <a href="${pageContext.request.contextPath}/occurrences/search.htm?<gbif:criterion subject="28" predicate="0" value="1" index="0"/>&<gbif:criterion subject="${occurrenceSearchSubject}" predicate="0" value="${occurrenceSearchValue}" index="1"/>&<gbif:criterion subject="24" predicate="0" value="${nonResourceCounts[rowCounter.index].key}" index="2"/>"><fmt:formatNumber value="${nonResourceCounts[rowCounter.index].count-resourceCounts[rowCounter.index].count}" pattern="###,###"/></a>
      </td>
      </c:if>
    </tr>
    </c:forEach>
  </tbody>
</table>
<!-- This has been rendered as a for each.
<display-el:table 
  name="resourceCounts" 
  class="hidden" 
  id="fullTable" 
  uid="resourceCount" 
  style="width: 720px;"
  requestURI="${pageContext.request.contextPath}/countries/${country.isoCountryCode}/?${pageContext.request.queryString}">
  <display-el:column titleKey="dataset" style="width:650px;">
     <a href="${pageContext.request.contextPath}/datasets/resource/${resourceCount.key}">${resourceCount.name}</a>
     <p class="resultsDetails">
     ${resourceCount.properties[0]}  
  </display-el:column>
  <display-el:column class="lastColumn" titleKey="log.console.count" style="width:70px;">
        <a href="${pageContext.request.contextPath}/occurrences/search.htm?<gbif:criterion subject="24" predicate="0" value="${resourceCount.key}" index="0"/>&<gbif:criterion subject="${occurrenceSearchSubject}" predicate="0" value="${occurrenceSearchValue}" index="1"/>&<gbif:criterion subject="28" predicate="0" value="0" index="2"/>"><fmt:formatNumber value="${resourceCount.count}" pattern="###,###"/></a>
  </display-el:column>
</display-el:table>
-->
</c:if>    
</c:if>