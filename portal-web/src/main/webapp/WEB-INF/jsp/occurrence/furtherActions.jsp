<%@ include file="/common/taglibs.jsp"%>
<c:if test="${fn:length(results) ge 1}">
<h4 id="searchResultCount"><spring:message code="occurrence.search.filter.retrievingcount"/></h4>
<script type="text/javascript">
	<% //retrieve the count %>
	var countCallback = {
		success:function(o){document.getElementById("searchResultCount").innerHTML=o.responseText;},	
		failure: function(o){}
	}	
	YAHOO.util.Connect.asyncRequest('GET',
		"<string:trim>${pageContext.request.contextPath}/occurrences/occurrenceCount?<gbif:criteria criteria="${criteria}"/><gbiftag:occurrenceFilterOptions/></string:trim>", 
		countCallback, 
		null); 	
</script>

<div id="furtherActions">
	<h4><spring:message code="occurrence.search.filter.whattodo.title"/></h4>
	<table cellspacing="1" class="actionsList">
		<tbody>
			<tr valign="top">
				<td><b><spring:message code="actions.view"/></b></td>
				<td>	
					<ul class="actionsListInline">
						<li>
							<a href="${pageContext.request.contextPath}/occurrences/searchWithTable.htm?<gbif:criteria criteria="${criteria}"/>"><spring:message code="occurrence.search.filter.action.viewtable"/></a>
						</li>		
						<li>
							<a href="${pageContext.request.contextPath}/occurrences/searchWithMap.htm?<gbif:criteria criteria="${criteria}"/>"><spring:message code="occurrence.search.filter.action.viewmap"/></a>
						</li>
					</ul>
				</td>
			</tr>
			<tr valign="top">
				<td><b><spring:message code="actions.specify"/></b></td>
				<td>	
					<ul class="actionsListInline">
						<li> 
							<a href="${pageContext.request.contextPath}/occurrences/searchProviders.htm?<gbif:criteria criteria="${criteria}"/>"><spring:message code="occurrence.switchto.provider.countview"/></a>			
						</li>
						<li> 
							<a href="${pageContext.request.contextPath}/occurrences/searchResources.htm?<gbif:criteria criteria="${criteria}"/>"><spring:message code="occurrence.switchto.resources.countview"/></a>			
						</li>
						<li> 
							<a href="${pageContext.request.contextPath}/occurrences/searchCountries.htm?<gbif:criteria criteria="${criteria}"/>"><spring:message code="occurrence.switchto.country.countview"/></a>			
						</li>
					</ul>
				</td>
			</tr>
			<tr valign="top">
				<td><b><spring:message code="actions.download"/></b></td>
				<td>	
					<ul class="actionsListInline">
						<li> 
							<a href="${pageContext.request.contextPath}/occurrences/downloadSpreadsheet.htm?<gbif:criteria criteria="${criteria}"/>"><spring:message code="occurrence.search.filter.action.download.spreadsheet"/></a>
						</li>
						<li> 
							<a href="${pageContext.request.contextPath}/occurrences/downloadResults.htm?format=brief&criteria=<gbif:criteria criteria="${criteria}" urlEncode="true"/>"><spring:message code="occurrence.record.download.format.darwin.brief" text="Darwin core (limited to 100,000)"/></a>
						</li>
						<li> 
							<a href="${pageContext.request.contextPath}/occurrences/downloadResults.htm?format=kml&criteria=<gbif:criteria criteria="${criteria}" urlEncode="true"/>"><spring:message code="occurrence.record.download.format.ge"/></a>
						</li>
						<li> 
							<a href="${pageContext.request.contextPath}/occurrences/downloadResults.htm?format=species&criteria=<gbif:criteria criteria="${criteria}" urlEncode="true"/>"><spring:message code="occurrence.record.download.format.species" text="Species in results"/></a>			
						</li> 
					</ul>
				</td>
			</tr>
      <tr valign="top">
        <td><b><spring:message code="actions.create" text="Create:"/></b></td>
        <td>  
          <ul class="actionsListInline">
          <c:choose>
            <c:when test="${oneClassification}">
              <li> 
                <c:if test="${viewName!='nicheModelling'}"><a href="${pageContext.request.contextPath}/occurrences/setupModel.htm?<gbif:criteria criteria="${criteria}"/>"></c:if><spring:message code="occurrence.search.filter.action.create.model" text="Niche Model"/><c:if test="${viewName!='nicheModelling'}"></a></c:if>
              </li>
            </c:when>
            <c:otherwise>
              <li> 
                <spring:message code="occurrence.search.filter.action.create.model" text="Niche Model"/> <i><spring:message code="occurrence.search.filter.action.create.model.disable" text="(only available for searches including exactly one classification at genus level or below)"/></i>
              </li>
            </c:otherwise>
          </c:choose>
          </ul>
        </td>
      </tr>
		</tbody>
	</table>
</div>
</c:if>