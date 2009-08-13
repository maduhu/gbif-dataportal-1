<%@ include file="/common/taglibs.jsp"%>
<div id="exampleSearches">
<h4><spring:message code="occurrence.search.filter.example.searches"/></h4>
<ul id="exampleSearches" class="genericList" style="margin:0px;">
<li><a href="${pageContext.request.contextPath}/occurrences/search.htm?c[0].s=0&c[0].p=0&c[0].o=Strix+aluco&c[1].s=5&c[1].p=0&c[1].o=PL&c[2].s=17&c[2].p=0&c[2].o=1&c[3].s=17&c[3].p=0&c[3].o=1&c[4].s=29&c[4].p=0&c[4].o=0"><spring:message code="occurrence.search.filter.example.searches.strixaluco"/></a></li>
<li><a href="${pageContext.request.contextPath}/occurrences/search.htm?c[0].s=0&c[0].p=0&c[0].o=Puma+concolor&c[1].s=18&c[1].p=0&c[1].o=NAM&c[2].s=17&c[2].p=0&c[2].o=1&c[3].s=29&c[3].p=0&c[3].o=0"><spring:message code="occurrence.search.filter.example.searches.pumaconcolor"/></a></li>
</ul>
</div>