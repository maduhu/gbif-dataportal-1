<%@ include file="/common/taglibs.jsp"%>
<c:forEach items="${searchResults}" var="taxonConcept" varStatus="status">
        <tr valign="top">
            <td class="tdColumn1" >
                <p class="column1"><string:capitalize>${taxonConcept.rank}</string:capitalize></p>
            </td>
            <td class="tdColumn2" >
                <p class="column2">
                	<span class="speciesName">
                		<a href="${pageContext.request.contextPath}/species/${taxonConcept.key}/commonName/${taxonConcept.commonName}"><string:trim>
							<gbif:taxonPrint concept="${taxonConcept}"/>
							(<c:if test="${not empty taxonConcept.commonNameLanguage}">${taxonConcept.commonNameLanguage}: </c:if><gbif:highlight keyword="${searchString}" cssClass="match" matchAnyPart="true"><string:capitalize>${taxonConcept.commonName}</string:capitalize></gbif:highlight>)
						</string:trim></a>
					</span>
				</p>
            </td>
            <td class="tdColumn3" >
				<c:set var="taxonConcept" scope="request" value="${taxonConcept}"/>
                <p class="column3"><gbiftag:taxonHierarchy concept="${taxonConcept}"/></p>
            </td>
        </tr>
</c:forEach>