<%@ include file="/common/taglibs.jsp"%>
<c:set var="entityName" scope="request"><gbif:taxonPrint concept="${taxonConcept}"/></c:set>
<c:set var="mapTitle" scope="request">${taxonConcept.taxonName}</c:set>
<c:set var="extraParams" scope="request"><gbif:criteria criteria="${occurrenceCriteria}"/></c:set>
<tiles:insert name="overviewMap"/>
<tiles:insert name="taxonOverviewMap"/>
<div style="margin-left:30px;">
   <c:set var="occurrenceSearchSubject" value="20" scope="request"/> 
   <c:set var="occurrenceSearchValue" value="${taxonConcept.key}" scope="request"/>
 <tiles:insert page="/WEB-INF/jsp/geography/drilldown/speciesDataRecord.jsp"/>
</div>