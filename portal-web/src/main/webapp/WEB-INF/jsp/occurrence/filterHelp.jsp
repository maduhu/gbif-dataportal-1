<%@ include file="/common/taglibs.jsp"%>
<h4 id="currentSearch"><spring:message code="search.filter.currentsearch.title"/></h4>
<p class="emptySearch">
<spring:message code="search.filter.empty" text="Your current search is empty."/>
</p>
<input id="filterSearchSubmit" type="submit" value="<spring:message code="search"/>" disabled="true"/>
<tiles:insert page="exampleSearches.jsp"/>