<%@ include file="/common/taglibs.jsp"%>
<c:set var="entityKey" value="${taxonConcept.key}" scope="request"/>
<c:if test="${pointsTotal>0}">
<p>
	<c:choose>
		<c:when test="${zoom>1 && viewablePoints>0}">
			<tiles:insert page="/WEB-INF/jsp/geography/viewableArea.jsp"/>
		</c:when>	
		<c:when test="${occurrenceCount!=null && occurrenceCount>0}">	
			<c:set var="a0"><span class="subject"><fmt:formatNumber value="${pointsTotal}" pattern="###,###"/></span></c:set>
			<c:set var="a1"><span class="subject"><fmt:formatNumber value="${occurrenceCount}" pattern="###,###"/></span></c:set>
			<spring:message code="maps.records.with.coordinates.fullcount" arguments="${a0}%%%${a1}" argumentSeparator="%%%"/>
			<br/><spring:message code="maps.records.disclaimer"/>
		</c:when>
		<c:otherwise>
			<c:set var="a0"><span class="subject"><fmt:formatNumber value="${pointsTotal}" pattern="###,###"/></span></c:set>
			<spring:message code="maps.records.with.coordinates" arguments="${a0}" argumentSeparator="%%%"/>
			<br/><spring:message code="maps.records.disclaimer"/>
		</c:otherwise>
	</c:choose>	
	
	<c:set var="a2"><gbif:taxonPrint concept="${taxonConcept}"/></c:set>
	<spring:message code="maps.records.not.full.distribution" arguments="${a2}" argumentSeparator="%%%"/> 
	
	<br/>
	
	<c:choose>
		<c:when test="${taxonConcept.rank=='species' && subspeciesCount>0}">
				<spring:message code="taxonconcept.map.allsubspecies.species"/> <gbif:taxonPrint concept="${taxonConcept}"/>
				(<a href="${pageContext.request.contextPath}/species/browse/taxon/${taxonConcept.key}"
						title="<spring:message code="taxonconcept.map.allsubspecies.species.title" arguments="${subspeciesCount}"/> ${taxonConcept.taxonName}"
					>${subspeciesCount} <string:lowerCase><spring:message code="taxonrank.subspecies"/></string:lowerCase></a>).
		</c:when>
		<c:when test="${taxonConcept.rank=='genus' && speciesCount>0}">
			<spring:message code="taxonconcept.map.allspecies.genus"/> <gbif:taxonPrint concept="${taxonConcept}"/>
			(<a href="${pageContext.request.contextPath}/species/browse/taxon/${taxonConcept.key}" 
					title="<spring:message code="taxonconcept.map.allspecies.genus.title" arguments="${speciesCount}" text="View the species in the family"/> ${taxonConcept.taxonName}">${speciesCount} <string:lowerCase><spring:message code="taxonrank.species"/></string:lowerCase></a>).
		</c:when>
		<c:when test="${taxonConcept.rank=='family' && generaCount>0}">
			<spring:message code="taxonconcept.map.allgenera"/> ${taxonConcept.rank} ${taxonConcept.taxonName} 
			(<a href="${pageContext.request.contextPath}/species/browse/taxon/${taxonConcept.key}"
				title="<spring:message code="taxonconcept.map.allgenera.family.title" arguments="${speciesCount}" text="View the genera in the family"/> ${taxonConcept.taxonName}"
			>${generaCount} <string:trim> 
			<c:choose>
				<c:when test="${generaCount > 1}"><spring:message code="taxonconcept.genera"/></c:when>
				<c:otherwise><spring:message code="taxonconcept.genus"/></c:otherwise>
			</c:choose></string:trim></a>).
		</c:when>
	</c:choose>
	
	<c:if test="${zoom>1}">
	<ul class="overviewMapLinks">
		<li>
			<c:set var="a0"><gbif:taxonPrint concept="${taxonConcept}"/></c:set>
			<c:set var="a1"><gbiftag:boundingBox/></c:set>
			<a href="${pageContext.request.contextPath}/occurrences/boundingBoxWithCriteria.htm?<gbif:criteria criteria='${occurrenceCriteria}'/>&minX=${minMapLong}&minY=${minMapLat}&maxX=${maxMapLong}&maxY=${maxMapLat}"><spring:message code="occurrence.record.geospatial.findall.viewarea" arguments="${a0}%%%${a1}" argumentSeparator="%%%"/></a>
		</li>
	</ul>	
	</c:if>
</p>	
</c:if>