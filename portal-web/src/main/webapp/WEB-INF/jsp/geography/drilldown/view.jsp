<%@ include file="/common/taglibs.jsp"%>
<div id="twopartheader">
	<h2><spring:message code="geography.drilldown.main.title"/>: <span class="subject"><gbif:capitalize><string:lowerCase><spring:message code="country.${country.isoCountryCode}"/></string:lowerCase></gbif:capitalize></span></h2>
	<c:if test="${not empty country.region}"><h3><spring:message code="region.${country.region}"/></h3></c:if>
</div>	

<tiles:insert page="actions.jsp"/>

<div class="subcontainer">
	<h4><spring:message code="occurrence.overview"/></h4>
	<tiles:insert page="occurrences.jsp"/>
	<div style="margin-left:30px;">
	 <c:set var="occurrenceSearchSubject" value="5" scope="request"/> 
   <c:set var="occurrenceSearchValue" value="${country.isoCountryCode}" scope="request"/>
	 <tiles:insert page="dataRecord.jsp"/>
	 <tiles:insert page="countryCounts.jsp"/>
	</div> 
</div>