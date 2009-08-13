<%@ include file="/common/taglibs.jsp"%>
<c:if test="${country.occurrenceCount>0}">	
<div id="furtherActions">
	<h4><spring:message code='actions.for'/> <gbif:capitalize><spring:message code="country.${country.isoCountryCode}"/>	</gbif:capitalize></h4>
	<table cellspacing="1" class="actionsList">
		<tbody>
			<tr valign="top">
				<td><b><spring:message code="actions.explore"/></b></td>
				<td>	
					<ul class="actionsListInline">
						<li>
							<a href="${pageContext.request.contextPath}/occurrences/search.htm?<gbif:criterion subject="5" predicate="0" value="${country.isoCountryCode}" index="0"/>"><spring:message code="explore.occurrences"/></a>
						</li>						
						<li>
							<c:set var="a0">
								<span class='subject'><gbif:capitalize><spring:message code="country.${country.isoCountryCode}"/></gbif:capitalize></span>
							</c:set>
							<a href="${pageContext.request.contextPath}/species/browse/country/${country.isoCountryCode}"><spring:message code="geography.drilldown.view.taxonomy" text="Explore species recorded in "/> <span class="subject"><spring:message code="country.${country.isoCountryCode}"/></span></a>
						</li>
					</ul>
				</td>
			</tr>
			<tr valign="top">
				<td><b><spring:message code="actions.list"/></b></td>
				<td>
					<ul class="actionsListInline">
						<li>
							<c:set var="a0">
								<span class='subject'><gbif:capitalize><spring:message code="country.${country.isoCountryCode}"/></gbif:capitalize></span>
							</c:set>
							<a href="${pageContext.request.contextPath}/occurrences/searchResources.htm?<gbif:criterion subject="5" predicate="0" value="${country.isoCountryCode}" index="0"/>"><spring:message code="geography.drilldown.list.resources" arguments="${a0}"/></a>
						</li>
					</ul>
				</td>
			</tr>
			<tr valign="top">
				<td><b><spring:message code="actions.download"/></b></td>
				<td>	
					<ul class="actionsListInline">
						<li>
							<a href="${pageContext.request.contextPath}/occurrences/country/celldensity/country-celldensity-${country.isoCountryCode}.kml"><spring:message code="download.google.earth.celldensity"/></a>
						</li>	
						<li>
							<a href="${pageContext.request.contextPath}/occurrences/country/placemarks/country-placemarks-${country.isoCountryCode}.kml"><spring:message code="download.google.earth.placemarks"/></a>
						</li>	
						<li>
							<a href="${pageContext.request.contextPath}/ws/rest/occurrence/list/?originIsoCountryCode=${country.isoCountryCode}&format=darwin"><spring:message code="download.darwin.core"/></a>
						</li>	
						<li>
							<a href="${pageContext.request.contextPath}/ws/rest/occurrence/list/?hostIsoCountryCode=${country.isoCountryCode}&format=darwin"><spring:message code="download.darwin.core.from.served.by.providers"/> <span class='subject'><gbif:capitalize><spring:message code="country.${country.isoCountryCode}"/></gbif:capitalize></span></a>
						</li>	
						<li>
							<a href="${pageContext.request.contextPath}/ws/rest/provider/list?isocountrycode=${country.isoCountryCode}"><spring:message code="download.metadata.for.providers" text="Download metadata for data providers in "/> <span class='subject'><gbif:capitalize><spring:message code="country.${country.isoCountryCode}"/></gbif:capitalize></span></a>
						</li>
					</ul>
				</td>
			</tr>
		</tbody>
	</table>
</div>
</c:if>