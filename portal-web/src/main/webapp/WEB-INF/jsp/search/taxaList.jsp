<%@ include file="/common/taglibs.jsp"%>
<c:forEach items="${searchResults}" var="taxonConcept" varStatus="status">
        <tr valign="top">
            <td class="tdColumn1" >
                <p class="column1"><string:capitalize>${taxonConcept.rank}</string:capitalize></p>
            </td>
            <td class="tdColumn2" >
                <p class="column2">
                	<span class="speciesName">
						<a href="${pageContext.request.contextPath}/species/<string:encodeUrl>${taxonConcept.key}</string:encodeUrl>/"><string:trim>
							<gbif:taxonPrint concept="${taxonConcept}" printName="false">
								<gbif:highlight keyword="${searchString}" cssClass="match" matchAnyPart="true">
									${taxonConcept.taxonName}	<c:if test="${taxonConcept.commonName!=null}">(<c:if test="${not empty taxonConcept.commonNameLanguage}">${taxonConcept.commonNameLanguage}:</c:if><string:capitalize>${taxonConcept.commonName}</string:capitalize>)</c:if>
								</gbif:highlight>
							</gbif:taxonPrint>	
						</string:trim></a>
					</span>
				</p>
            </td>
            <td class="tdColumn3" >
                <p class="column3">
					<c:set var="taxonConcept" scope="request" value="${taxonConcept}"/>
					<gbiftag:taxonHierarchy concept="${taxonConcept}"/>
				</p>
				<c:if test="${taxonConcept.acceptedConceptKey!=null}">
				<p style="margin-left:30px;">
					<c:if test="${taxonConcept.conceptStatus==1 || taxonConcept.conceptStatus==2 || taxonConcept.conceptStatus==4}">
						<c:choose>
							<c:when test="${taxonConcept.conceptStatus==1}">
								<spring:message code="ambiguous.synonym.for"/>
							</c:when>
							<c:when test="${taxonConcept.conceptStatus==2}">
								<spring:message code="misapplied.name.for"/>
							</c:when>
							<c:when test="${taxonConcept.conceptStatus==4}">
								<spring:message code="synonym.for"/>
							</c:when>
						</c:choose>
						<span class="subject">
							<a href="${pageContext.request.contextPath}/species/${taxonConcept.acceptedConceptKey}"><string:trim>
								<gbif:taxonPrint concept="${taxonConcept}" printName="false">${taxonConcept.acceptedTaxonName}</gbif:taxonPrint>
							</string:trim></a>
						</span>
					</c:if>	
					<c:if test="${taxonConcept.conceptStatus==3}">
						<spring:message code="provisionally.applied.name"/>		
					</c:if>
				</p>
				</c:if>
            </td>
        </tr>
</c:forEach>