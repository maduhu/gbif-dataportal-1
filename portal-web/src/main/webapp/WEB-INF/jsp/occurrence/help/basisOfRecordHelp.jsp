<%@ include file="/common/taglibs.jsp"%>
<div class="otherDetailsFilterHelp">
<c:set var="addFilterMsg"><spring:message code="search.filter.add.filter"/></c:set>
<spring:message code="occurrence.basisOfRecord.help1" arguments="${addFilterMsg}" argumentSeparator="$$$"/>
<spring:message code="occurrence.basisOfRecord.help2"/>
<ul class="helpList">
	<li><span class="subject"><spring:message code="occurrence.basisOfRecord.observation"/></span> <spring:message code="occurrence.basisOfRecord.observation.description"/></li>
	<li><span class="subject"><spring:message code="occurrence.basisOfRecord.specimen"/></span> <spring:message code="occurrence.basisOfRecord.specimen.description"/></li>
	<li><span class="subject"><spring:message code="occurrence.basisOfRecord.living"/></span> <spring:message code="occurrence.basisOfRecord.living.description"/></li>
	<li><span class="subject"><spring:message code="occurrence.basisOfRecord.germplasm"/></span> <spring:message code="occurrence.basisOfRecord.germplasm.description"/></li>
	<li><span class="subject"><spring:message code="occurrence.basisOfRecord.fossil"/></span> <spring:message code="occurrence.basisOfRecord.fossil.description"/></li>
</ul>
</div>