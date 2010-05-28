<%@ include file="/common/taglibs.jsp"%>
<div id="header">
    <h1 id="header" title="Global Biodiversity Information Facility"><a href="http://www.gbif.org">Global Biodiversity Information Facility</a></h1>
	<h3 id="blurb"><spring:message code="portal.header.subtitle"></spring:message></h3>
	<div id="quickSearch">
		<tiles:insert page="blanketSearch.jsp"/>
	</div>
</div> <!-- End header-->