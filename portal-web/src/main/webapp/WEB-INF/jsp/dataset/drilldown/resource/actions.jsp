<%@ include file="/common/taglibs.jsp"%>
<div id="furtherActions">
	<h4><spring:message code='actions.for'/> ${dataResource.name}</h4>
	<table cellspacing="1" class="actionsList">
		<tbody>
			<tr valign="top">
				<td><b><spring:message code="actions.explore"/></b></td>
				<td>	
					<ul class="actionsListInline">
						<c:if test="${dataResource.occurrenceCount>0}">
							<li>
								<a href="${pageContext.request.contextPath}/occurrences/search.htm?<gbif:criterion subject="24" predicate="0" value="${dataResource.key}" index="0"/>"><spring:message code="explore.occurrences"/></a>
							</li>
						</c:if>
						<li>
							<gbif:taxatreeLink dataResource="${dataResource}" dataProvider="${dataProvider}">
								<spring:message code="dataset.taxonomytreelink"/>
							</gbif:taxatreeLink>
						</li>
					</ul>		
				</td>		
			</tr>
			<c:if test="${dataResource.occurrenceCount>0}">
				<tr>
					<td><b><spring:message code="actions.list"/></b></td>
					<td>	
						<ul class="actionsListInline">
							<li>
								<c:set var="a0">${dataResource.name}</c:set>
								<a href="${pageContext.request.contextPath}/occurrences/searchCountries.htm?<gbif:criterion subject="24" predicate="0" value="${dataResource.key}" index="0"/>"><spring:message code="dataset.list.countries" arguments="${a0}"/></a>
							</li>
						</ul>
					</td>
				</tr>	
			</c:if>					
			<tr>
				<td><b><spring:message code="actions.download"/></b></td>
				<td>	
					<c:if test="${dataResource.occurrenceCount>0}">					
					<ul class="actionsListInline">
						<li>
							<a href="${pageContext.request.contextPath}/ws/rest/occurrence/list/?dataResourceKey=${dataResource.key}&format=darwin"><spring:message code="download.darwin.core"/></a>
						</li>	
						<li>
							<a href="${pageContext.request.contextPath}/occurrences/resource/celldensity/resource-celldensity-${dataResource.key}.kml"><spring:message code="download.google.earth.celldensity"/></a>
						</li>	
						<li>
							<a href="${pageContext.request.contextPath}/occurrences/resource/placemarks/resource-placemarks-${dataResource.key}.kml"><spring:message code="download.google.earth.placemarks"/></a>
						</li>	
					</ul>
					</c:if>	
					<ul class="actionsListInline">
						<c:if test="${dataResource.occurrenceCount>0}">
						<li>
							<a href="${pageContext.request.contextPath}/occurrences/downloadResults.htm?format=species&criteria=<gbif:criterion subject="24" predicate="0" value="${dataResource.key}" index="0" urlEncode="true"/>"><spring:message code="dataset.list.species" arguments="${a0}"/></a>
						</li>		
						</c:if>
						<li>
							<a href="${pageContext.request.contextPath}/species/downloadSpreadsheet.htm?<gbif:criterion subject="1" predicate="0" value="${dataResource.key}" index="0"/>"><spring:message code="dataset.download.taxonomy"/></a>
						</li>					
					</ul>
				</td>
			</tr>	
			<tr>
				<td><b><spring:message code="actions.send"/></b></td>
				<td>	
					<ul class="actionsListInline">
						<li>
							<a class="feedback" href='javascript:feedback("${pageContext.request.contextPath}/feedback/resource/${dataResource.key}")'><spring:message code="feedback.to.provider.link"  arguments="${dataProvider.name}" argumentSeparator="|"/></a>
						</li>	
					</ul>
				</td>
			</tr>	
		</tbody>
	</table>
</div>
