<%@ include file="/common/taglibs.jsp"%>
<c:if test="${not empty viewName || not empty results.searchResults}">
<c:if test="${viewName!='resultsDownloadSpreadsheet'}">
<div id="furtherActions">
	<h4><spring:message code="occurrence.search.filter.whattodo.title"/></h4>
	<table cellspacing="1" class="actionsList">
		<tbody>
			<tr valign="top">
				<td><b><spring:message code="actions.download"/></b></td>
				<td>	
					<ul class="actionsListInline">
						<li> 
							<a href="${pageContext.request.contextPath}/species/downloadSpreadsheet.htm?<gbif:criteria criteria="${criteria}"/>"><spring:message code="occurrence.search.filter.action.download.spreadsheet"/>
						</li>
					</ul>
				</td>
			</tr>
		</tbody>
	</table>
</div>
</c:if>
</c:if>