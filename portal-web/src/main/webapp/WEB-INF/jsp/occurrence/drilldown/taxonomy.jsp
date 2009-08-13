<%@ include file="/common/taglibs.jsp"%>
<fieldset>
<p>
	<c:choose>
		<c:when test="${empty occurrenceRecord}">
			<label><spring:message code="occurrence.record.scientificName"/>:</label> ${rawOccurrenceRecord.scientificName} ${rawOccurrenceRecord.author} 
		</c:when>
		<c:when test="${rawOccurrenceRecord.scientificName==taxonConcept.taxonName && (((empty rawOccurrenceRecord.author) && (empty taxonConcept.author)) || (rawOccurrenceRecord.author==taxonConcept.author))}">
			<label><spring:message code="occurrence.record.scientificName"/>:</label> <gbiftag:taxonLink concept="${taxonConcept}"/> ${rawOccurrenceRecord.author} 
		</c:when>
		<c:otherwise>
			<label><spring:message code="occurrence.record.scientificName"/>:</label><gbif:duplicateAuthorNames scientificName="${rawOccurrenceRecord.scientificName}" authorName="${rawOccurrenceRecord.author}" cssClass="genera"/> 
			<c:set var="interpretedSciName"><c:if test="${not empty taxonConcept.partnerConceptKey}"><a href="${pageContext.request.contextPath}/species/${taxonConcept.partnerConceptKey}"></c:if><span class="genera">${taxonConcept.taxonName}</span><c:if test="${not empty taxonConcept.partnerConceptKey}"></a></c:if> ${partnerConcept.author}</c:set>
			<spring:message code="occurrence.record.interpreted.as" arguments="${interpretedSciName}" argumentSeparator="$$$"/>
		</c:otherwise>
	</c:choose>
</p>

<c:if test="${!rawOnly && not empty occurrenceRecord}">
<p id="classification">
	<label for="classification"><spring:message code="occurrence.record.gbif.taxonomy"/>:</label> 
	<gbif:flattree classname="classificationCondensed" concepts="${concepts}" selectedConcept="${taxonConcept}"/> 
</p>
</c:if>	

<c:if test="${not empty rawOccurrenceRecord.kingdom}">
<p>
	<c:choose>
		<c:when test="${rawOccurrenceRecord.kingdom==kingdomConcept.taxonName}">
			<label for="kingdom"><spring:message code="taxonrank.kingdom"/>:</label> <gbiftag:taxonLink concept="${kingdomConcept}"/>
		</c:when>
		<c:otherwise>
			<label for="kingdom"><spring:message code="taxonrank.kingdom"/>:</label> ${rawOccurrenceRecord.kingdom}
			<gbiftag:interpretedTaxon concept="${kingdomConcept}"/>
		</c:otherwise>
	</c:choose>
</p>
</c:if>

<c:if test="${not empty rawOccurrenceRecord.phylum}">
<p>
	<c:choose>
		<c:when test="${rawOccurrenceRecord.phylum==phylumConcept.taxonName}">
			<label for="phylum"><spring:message code="taxonrank.phylum"/>:</label> <gbiftag:taxonLink concept="${phylumConcept}"/>
		</c:when>
		<c:otherwise>
			<label for="phylum"><spring:message code="taxonrank.phylum"/>:</label> ${rawOccurrenceRecord.phylum}
			<gbiftag:interpretedTaxon concept="${phylumConcept}"/>
		</c:otherwise>
	</c:choose>
</p>
</c:if>

<c:if test="${not empty rawOccurrenceRecord.bioClass}">
<p>
	<c:choose>
		<c:when test="${rawOccurrenceRecord.bioClass==classConcept.taxonName}">
			<label for="class"><spring:message code="taxonrank.class"/>:</label> <gbiftag:taxonLink concept="${classConcept}"/>
		</c:when>
		<c:otherwise>
			<label for="class"><spring:message code="taxonrank.class"/>:</label> ${rawOccurrenceRecord.bioClass}
			<gbiftag:interpretedTaxon concept="${classConcept}"/>
		</c:otherwise>
	</c:choose>
</p>
</c:if>

<c:if test="${not empty rawOccurrenceRecord.order}">
<p>
	<c:choose>
		<c:when test="${rawOccurrenceRecord.order==orderConcept.taxonName}">
			<label for="order"><spring:message code="taxonrank.order"/>:</label> <gbiftag:taxonLink concept="${orderConcept}"/>
		</c:when>
		<c:otherwise>
			<label for="order"><spring:message code="taxonrank.order"/>:</label> ${rawOccurrenceRecord.order}
			<gbiftag:interpretedTaxon concept="${orderConcept}"/>
		</c:otherwise>
	</c:choose>
</p>
</c:if>

<c:if test="${not empty rawOccurrenceRecord.family}">
<p>
	<c:choose>
		<c:when test="${rawOccurrenceRecord.family==familyConcept.taxonName}">
			<label for="family"><spring:message code="taxonrank.family"/>:</label> <gbiftag:taxonLink concept="${familyConcept}"/>
		</c:when>
		<c:otherwise>
			<label for="family"><spring:message code="taxonrank.family"/>:</label> ${rawOccurrenceRecord.family}
			<gbiftag:interpretedTaxon concept="${familyConcept}"/>
		</c:otherwise>
	</c:choose>
</p>
</c:if>

<c:if test="${not empty rawOccurrenceRecord.genus}">
<p>
	<c:choose>
		<c:when test="${rawOccurrenceRecord.genus==genusConcept.taxonName}">
			<label for="genus"><spring:message code="taxonrank.genus"/>:</label> <gbiftag:taxonLink concept="${genusConcept}"/>
		</c:when>
		<c:otherwise>
			<label for="genus"><spring:message code="taxonrank.genus"/>:</label> ${rawOccurrenceRecord.genus}
			<gbiftag:interpretedTaxon concept="${genusConcept}"/>
		</c:otherwise>
	</c:choose>
</p>
</c:if>

<c:if test="${not empty rawOccurrenceRecord.species}">
<p>
	<c:choose>
		<c:when test="${rawOccurrenceRecord.species==speciesConcept.taxonName}">
			<label><spring:message code="taxonrank.species"/>:</label> <gbiftag:taxonLink concept="${speciesConcept}"/>
		</c:when>
		<c:otherwise>
			<label><spring:message code="taxonrank.species"/>:</label> ${rawOccurrenceRecord.species}
			<gbiftag:interpretedTaxon concept="${speciesConcept}"/>
		</c:otherwise>
	</c:choose>
</p>
</c:if>

<c:if test="${not empty rawOccurrenceRecord.subspecies}">
<p>
	<c:choose>
		<c:when test="${rawOccurrenceRecord.subspecies==subspeciesConcept.taxonName}">
			<label><spring:message code="taxonrank.subspecies"/>:</label> <gbiftag:taxonLink concept="${subspeciesConcept}"/>
		</c:when>
		<c:otherwise>
			<label><spring:message code="taxonrank.subspecies"/>:</label> ${rawOccurrenceRecord.subspecies}
			<gbiftag:interpretedTaxon concept="${subspeciesConcept}"/>
		</c:otherwise>
	</c:choose>
</p>
</c:if>

<c:if test="${not empty typifications}">
<p>
	<c:forEach items="${typifications}" var="typification">
		<label><spring:message code="specimen.type.status"/>:</label> ${typification.typeStatus} <c:if test="${not empty typification.scientificName}"><spring:message code="specimen.type.for" arguments="${typification.scientificName}"/></c:if>
		<br/>
	</c:forEach>
</p>	
</c:if>
	
<c:if test="${rawOccurrenceRecord.identifierName!=null || rawOccurrenceRecord.identificationDate!=null}">
	<c:if test="${rawOccurrenceRecord.identifierName!=null}">
		<p>
		<label for="indentifierName"><spring:message code="occurrence.record.identifierName"/>:</label> 
		${rawOccurrenceRecord.identifierName}
		</p>
	</c:if>	
	<c:if test="${rawOccurrenceRecord.identificationDate!=null}">	
		<p>
		<label for="indentificationDate"><spring:message code="occurrence.record.dateIdentified"/>:</label> 
		<fmt:formatDate value="${rawOccurrenceRecord.identificationDate}"/>
		</p>
	</c:if>
</c:if>
</fieldset>	