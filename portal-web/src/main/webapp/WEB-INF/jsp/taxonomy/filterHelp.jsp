<%@ include file="/common/taglibs.jsp"%>
<h4 id="currentSearch"><spring:message code="search.filter.currentsearch.title"/></h4>
<p class="emptySearch">
	<spring:message code="search.filter.empty" text="Your current search is empty."/>
</p>
<input id="filterSearchSubmit" type="submit" value="<spring:message code="search"/>" disabled="true"/>
<div id="exampleSearches">
<h4><spring:message code="taxonomy.search.filter.example.searches"/></h4>
<ul class="genericList">
<li><a href="${pageContext.request.contextPath}/species/search.htm?c%5B0%5D.s=0&c%5B0%5D.p=0&c%5B0%5D.o=Oenanthe&c%5B1%5D.s=9&c%5B1%5D.p=0&c%5B1%5D.o=6000"><spring:message code="taxonomy.search.filter.example.searches.oenanthe.genus"/></a></li>
<li><a href="${pageContext.request.contextPath}/species/search.htm?c%5B0%5D.s=0&c%5B0%5D.p=0&c%5B0%5D.o=Puma+concolor*"><spring:message code="taxonomy.search.filter.example.searches.pumaconcolor"/></a></li>
</ul>
</div>