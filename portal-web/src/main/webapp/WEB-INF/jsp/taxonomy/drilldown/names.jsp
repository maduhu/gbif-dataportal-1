<%@ include file="/common/taglibs.jsp"%>
<c:if test="${not empty partners}">
<h4><spring:message code="taxonconcept.drilldown.explore.names" text="Names and classification"/></h4>
<table width="80%" border="0" cellpadding="2" cellspacing="0">

<c:forEach items="${partners}" var="partner" varStatus="partnerStatus">
	<tr valign="top">
		<td colspan="2"> 
			<b><spring:message code="taxonomy.according.to"/> <a href="${pageContext.request.contextPath}/datasets/resource/${partner.dataResource.key}">${partner.taxonConcept.dataProviderName}: ${partner.dataResource.name}</a><b>
		</td>
	</tr>
	
	<tr valign="top">
		<td class="label"><spring:message code="name"/></td>
		<td>
			<a href="${pageContext.request.contextPath}/species/browse/taxon/${partner.taxonConcept.key}"><gbif:taxonPrint concept="${partner.taxonConcept}"/><c:if test="${not empty partner.taxonConcept.author}"> ${partner.taxonConcept.author}</c:if></a>
		</td>
	</tr>

	<c:if test="${not empty partner.higherConcepts}">
		<tr valign="top">
			<td class="label"><spring:message code="taxonomy.classification"/></td>
			<td>
				<gbif:flattree classname="classificationCondensed" concepts="${partner.higherConcepts}" selectedConcept="${partner.taxonConcept}" messageSource="${messageSource}"/>
			</td>
		</tr>
	</c:if>

	<c:if test="${partner.dataResource.basisOfRecord!='nomenclator'}">
		<tr valign="top">
			<td class="label"><spring:message code="taxonomy.status"/></td>
			<td>
				<c:choose>
					<c:when test="${not empty partner.relationshipAssertions}">
						<c:forEach items="${partner.relationshipAssertions}" var="relationshipAssertion">
							<c:choose>
								<c:when test="${relationshipAssertion.relationshipType==1}">
									<spring:message code="ambiguous.synonym.for"/> <a href="${pageContext.request.contextPath}/species/${relationshipAssertion.toTaxonConceptKey}"><gbif:taxonPrint concept="${taxonConcept}" printName="false">${relationshipAssertion.toTaxonName}</gbif:taxonPrint></a>	<br/>
								</c:when>
								<c:when test="${relationshipAssertion.relationshipType==2}">
									<spring:message code="misapplied.name.for"/> <a href="${pageContext.request.contextPath}/species/${relationshipAssertion.toTaxonConceptKey}"><gbif:taxonPrint concept="${taxonConcept}" printName="false">${relationshipAssertion.toTaxonName}</gbif:taxonPrint></a>	<br/>
								</c:when>
								<c:when test="${relationshipAssertion.relationshipType==3}">
									<spring:message code="provisionally.applied.name"/><br/>
								</c:when>
								<c:when test="${relationshipAssertion.relationshipType==4}">
									<spring:message code="synonym.for"/> <a href="${pageContext.request.contextPath}/species/${relationshipAssertion.toTaxonConceptKey}"><gbif:taxonPrint concept="${taxonConcept}" printName="false">${relationshipAssertion.toTaxonName}</gbif:taxonPrint></a>	<br/>
								</c:when>
							</c:choose>
						</c:forEach>
					</c:when>
					<c:otherwise>
						<spring:message code="accepted.name"/><br/>
					</c:otherwise>
				</c:choose>
			</td>
		</tr>
	</c:if>		

	<c:if test="${not empty partner.synonyms}">
		<tr valign="top">
			<td class="label"><spring:message code="taxonomy.synonyms"/></td>
			<td>
				<c:forEach items="${partner.synonyms}" var="synonym" varStatus="snStatus">
					<a href="${pageContext.request.contextPath}/species/${synonym.fromTaxonConceptKey}"><gbif:taxonPrint concept="${taxonConcept}" printName="false">${synonym.fromTaxonName}</gbif:taxonPrint></a><c:if test="${snStatus.count < fn:length(partner.synonyms)}">, </c:if>
				</c:forEach>
			</td>
		</tr>	
	</c:if>

	<c:if test="${not empty partner.commonNames}">
		<tr valign="top">
			<td class="label"><spring:message code="taxonomy.common.names"/></td>
			<td>
				<table>
					<c:forEach var='commonNameWithLanguageOrdered' items='${partner.commonNames}' varStatus="cnStatus">
						<tr valign='top'>
							<c:forEach var='commonNameWithLanguage' items='${commonNameWithLanguageOrdered}' varStatus="cnStatus2">
								<td><c:if test="${not empty commonNameWithLanguage.key}">${commonNameWithLanguage.key} :</c:if></td>
								<td>
									<c:forEach items="${commonNameWithLanguage.value}" var="commonName" varStatus="cnStatus3">
										<string:capitalize>${commonName.name}</string:capitalize><c:if test="${cnStatus3.count < fn:length(commonNameWithLanguage.value)}">, </c:if>
									</c:forEach>
								</td>
							</c:forEach>
						</tr>
					</c:forEach>
				</table>
			</td>
		</tr>	
	</c:if>
	
	<c:if test="${not empty partner.remoteConcepts}">
		<c:forEach items="${partner.remoteConcepts}" var="remoteConcept">
			<c:choose>
				<c:when test="${remoteConcept.idType.value==1}">
					<tr valign="top">
						<td class="label"><spring:message code="remote.concept.localid"/></td>
						<td>${remoteConcept.remoteId}</td>
					</tr>
				</c:when>
			</c:choose>
			<c:choose>
				<c:when test="${remoteConcept.idType.value==2}">
					<tr valign="top">
						<td class="label"><spring:message code="remote.concept.url"/></td>
						<td><a href="${remoteConcept.remoteId}">${remoteConcept.remoteId}</a></td>
					</tr>
				</c:when>
			</c:choose>
			<c:choose>
				<c:when test="${remoteConcept.idType.value==4}">
					<tr valign="top">
						<td class="label"><spring:message code="remote.concept.specialist"/></td>
						<td>${remoteConcept.remoteId}</td>
					</tr>
				</c:when>
			</c:choose>
			<c:choose>
				<c:when test="${remoteConcept.idType.value==5}">
					<tr valign="top">
						<td class="label"><spring:message code="remote.concept.scrutinydate"/></td>
						<td>${remoteConcept.remoteId}</td>
					</tr>
				</c:when>
			</c:choose>
			<c:choose>
				<c:when test="${remoteConcept.idType.value==6}">
					<tr valign="top">
						<td class="label"><spring:message code="remote.concept.guid"/></td>
						<td>
							<c:choose>
								<c:when test="${fn:startsWith(remoteConcept.remoteId, 'urn:lsid:')}">
									<a href="http://www.ipni.org/${remoteConcept.remoteId}">${remoteConcept.remoteId}</a>
								</c:when>
								<c:when test="${fn:startsWith(remoteConcept.remoteId, 'doi:')}">
									<a href="http://dx.doi.org/${remoteConcept.remoteId}">${remoteConcept.remoteId}</a>
								</c:when>
								<c:when test="${fn:startsWith(remoteConcept.remoteId, 'http://')}">
									<a href="${remoteConcept.remoteId}">${remoteConcept.remoteId}</a>
								</c:when>
								<c:otherwise>
									${remoteConcept.remoteId}
								</c:otherwise>
							</c:choose>
						</td>
					</tr>
				</c:when>
			</c:choose>
		</c:forEach>	
	</c:if>

	<tr valign="top">
		<td class="label"><spring:message code="feedback"/></td>
		<td><a class="feedback" href='javascript:feedback("${pageContext.request.contextPath}/feedback/taxon/${partner.taxonConcept.key}")'><spring:message code="feedback.to.provider.on.classification.link"  arguments="${partner.taxonConcept.dataProviderName}" argumentSeparator="|"/> <gbif:taxonPrint concept="${partner.taxonConcept}"/><c:if test="${not empty partner.taxonConcept.author}"> ${partner.taxonConcept.author}</c:if></a></td>
	</tr>
	<tr valign="top">
		<td class="label"></td>
		<td><spring:message code="feedback.to.provider.on.classification.explanation" /></td>
	</tr>	
	
	<c:if test="${partnerStatus.count < fn:length(partners)}">
		<tr valign="top"><td colspan="2"><br/></td></tr>
	</c:if>
	
</c:forEach>
</table>
<br/>

</c:if>
