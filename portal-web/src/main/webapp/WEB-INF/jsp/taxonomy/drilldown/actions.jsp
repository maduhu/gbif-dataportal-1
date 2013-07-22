<%@ include file="/common/taglibs.jsp"%>
<script type="text/javascript">
	<% //retrieve the count %>
	var countCallback = {
		success:function(o){document.getElementById("searchResultCount").innerHTML=o.responseText;},	
		failure: function(o){}
	}	
	YAHOO.util.Connect.asyncRequest('GET',
		"<string:trim>${pageContext.request.contextPath}/occurrences/actionsOccurrenceCount?<gbif:criteria criteria="${occurrenceCriteria}"/><gbiftag:occurrenceFilterOptions/></string:trim>", 
		countCallback, 
		null); 	
</script>
<div id="uat">
  <h4><a href="http://uat.gbif.org/species/${taxonConcept.key}" target="_blank">View this page on the new portal</a></h4>
  <p>
    GBIF is developing a new portal, and an early access version showcasing certain sections is now available.
  </p>
  <p>
    Please note that only Firefox, Chrome and Safari browsers are known to work. Styling issues are known with Internet Explorer for this early release.
  </p>
  <p>
    Feedback is welcome, using the provided buttons ("Report a bug" and "Provide feedback") on the new portal pages.
  </p>
</div>
<div id="furtherActions">
	<h4><spring:message code='actions.for'/> <gbif:taxonPrint concept="${taxonConcept}"/></h4>
	<table cellspacing="1" class="actionsList">
		<tbody>
			<tr valign="top">
				<td><b><spring:message code="actions.explore"/></b></td>
				<td>	
					<ul class="actionsListInline">
						<gbif:isMajorRank concept="${taxonConcept}">					
						<li>
							<a href="${pageContext.request.contextPath}/occurrences/search.htm?<gbif:criteria criteria="${occurrenceCriteria}"/>"><spring:message code="explore.occurrences"/> <span id="searchResultCount"></span></a>
						</li>	
						</gbif:isMajorRank>				
						<li>
							<a href="${pageContext.request.contextPath}/species/browse/taxon/${taxonConcept.key}"><spring:message code="taxonconcept.drilldown.explore.names"/></a>
						</li>
					</ul>
				</td>
			</tr>
			<gbif:isMajorRank concept="${taxonConcept}">			
			<c:if test="${taxonConcept.rank!='kingdom' && taxonConcept.rank!='phylum' && taxonConcept.rank!='class' && taxonConcept.rank!='order'}">
				<tr valign="top">
					<td><b><spring:message code="actions.list"/></b></td>
					<td>
						<ul class="actionsListInline">
							<li>
								<a href="${pageContext.request.contextPath}/occurrences/searchCountries.htm?<gbif:criteria criteria='${occurrenceCriteria}'/>"><spring:message code="occurrence.record.taxonomy.list.countries"/></a>
							</li>
							<li>
								<a href="${pageContext.request.contextPath}/occurrences/searchResources.htm?<gbif:criteria criteria='${occurrenceCriteria}'/>"><spring:message code="occurrence.record.taxonomy.list.dataproviders"/></a>
							</li>	
						</ul>
					</td>
				</tr>
			</c:if>
			<tr valign="top">
				<td><b><spring:message code="actions.download"/></b></td>
				<td>	
					<ul class="actionsListInline">
						<li>
							<a href="${pageContext.request.contextPath}/ws/rest/occurrence/list/?taxonConceptKey=${taxonConcept.key}&format=darwin"><spring:message code="download.darwin.core"/></a>
						</li>	
						<li>
							<a href="${pageContext.request.contextPath}/occurrences/taxon/celldensity/taxon-celldensity-${taxonConcept.key}.kml"><spring:message code="download.google.earth.celldensity"/></a>
						</li>	
						<li>
							<a href="${pageContext.request.contextPath}/occurrences/taxon/placemarks/taxon-placemarks-${taxonConcept.key}.kml"><spring:message code="download.google.earth.placemarks"/></a>
						</li>	
					</ul>
				</td>
			</tr>
			</gbif:isMajorRank>			
		</tbody>
	</table>
</div>