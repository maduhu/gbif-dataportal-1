<%@ include file="/common/taglibs.jsp"%>
<div id="taxonomy" class="taxonomyContainer">
	<div id="twopartheader">
		<h2>
		<c:choose>
			<c:when test="${selectedConcept!=null}">
				<spring:message code="taxonomy.browser.classification.of"/> 
				<span class="subject"><string:capitalize>${selectedConcept.rank}</string:capitalize>: 
				<gbif:taxonPrint concept="${selectedConcept}"/></span> 
				${selectedConcept.author}
			</c:when>
			<c:otherwise>
				<spring:message code="taxonomy.browser.classification"/>
			</c:otherwise>
		</c:choose>
		</h2>
		<h3>
			<spring:message code="taxonomy.browser.species.recorded.in" text="Species recorded in"/>:
			<a href="${pageContext.request.contextPath}/countries/${country.isoCountryCode}"><gbif:capitalize>${country.name}</gbif:capitalize></a>
		</h3>
	</div>
	<c:choose>
		<c:when test="${not empty concepts}">
			<div id="furtherActions">
				<h4><spring:message code='actions.for'/> <gbif:capitalize>${country.name}</gbif:capitalize></h4>
				<table cellspacing="1" class="actionsList">
					<tbody>
						<tr valign="top">
							<td><b><spring:message code="actions.explore"/></b></td>
							<td>	
								<ul class="actionsListInline">
									<li>
										<a href="${pageContext.request.contextPath}/species/search.htm?<gbif:criterion subject="12" predicate="0" value="${country.isoCountryCode}"/>&<gbif:criterion subject="9" predicate="0" value="7000" index="1"/>"><spring:message code="taxonomy.browser.species.recorded.in" text="Species recorded in"/> <gbif:capitalize>${country.name}</gbif:capitalize></a>						
									</li>						
									<li>
										<a href="${pageContext.request.contextPath}/species/search.htm?<gbif:criterion subject="12" predicate="0" value="${country.isoCountryCode}"/>"><spring:message code="taxonomy.browser.taxa.recorded.in" text="Taxa recorded in"/> <gbif:capitalize>${country.name}</gbif:capitalize></a>						
									</li>						
								</ul>
							</td>
						</tr>					
						<tr valign="top">
							<td><b><spring:message code="actions.download"/></b></td>
							<td>	
								<ul class="actionsListInline">
									<li>
										<a href="${pageContext.request.contextPath}/species/downloadSpreadsheet.htm?<gbif:criterion subject="12" predicate="0" value="${country.isoCountryCode}"/>&<gbif:criterion subject="9" predicate="0" value="7000" index="1"/>"><spring:message code="taxonomy.browser.species.recorded.in" text="Species recorded in"/> <gbif:capitalize>${country.name}</gbif:capitalize></a>						
									</li>						
									<li>
										<a href="${pageContext.request.contextPath}/species/downloadSpreadsheet.htm?<gbif:criterion subject="12" predicate="0" value="${country.isoCountryCode}"/>"><spring:message code="taxonomy.browser.taxa.recorded.in" text="Taxa recorded in"/> <gbif:capitalize>${country.name}</gbif:capitalize></a>						
									</li>						
								</ul>
							</td>
						</tr>
					</tbody>
				</table>
			</div><!--end further actions-->		
			<div class="smalltree">
				<gbif:smallbrowser concepts="${concepts}" selectedConcept="${selectedConcept}" rootUrl="/species/browse/country/${country.isoCountryCode}" markConceptBelowThreshold="${dataProvider.key==nubProvider.key}" highestRank="kingdom" messageSource="${messageSource}"/>
			</div><!--end smalltree-->		
		</c:when>
		<c:otherwise>		
			<spring:message code="taxonomy.browser.notree"/> <gbif:capitalize>${country.name}</gbif:capitalize>
		</c:otherwise>	
	</c:choose>	
</div>	