<%@ include file="/common/taglibs.jsp"%>
<ul>
	<li>
		<c:set var="a0"><span class="subject"><fmt:formatNumber value="${country.speciesCount}" pattern="###,###"/></span></c:set>
		<c:set var="a1"><span class="subject"><gbif:capitalize><string:lowerCase>${country.name}</string:lowerCase></gbif:capitalize></span></c:set>
		<spring:message code="geography.drilldown.species.count" arguments="${a0}%%%${a1}" argumentSeparator="%%%"/>
	</li>
<c:if test="${country.occurrenceCount>0}">	
	<li>
		<c:set var="a0"><span class="subject"><gbif:capitalize><string:lowerCase>${country.name}</string:lowerCase></gbif:capitalize></span></c:set>
		<a href="${pageContext.request.contextPath}/occurrences/searchSpecies.htm?<gbif:criterion subject="5" predicate="0" value="${country.isoCountryCode}" index="0"/>">
			<spring:message code="geography.drilldown.species.explore" arguments="${a0}"/>
		</a>
	</li>
</c:if>	
</ul>