<%@ include file="/common/taglibs.jsp"%>
<div id="twopartheader">					
	<h2><spring:message code="provider.archive"/></h2>
	<h3><spring:message code="provider.archive.subheader"/></h3>
</div><!-- End twopartheader -->
<table class="providerRequest">
<tr>
<td>

</td>
<td class="providerInfo">
<b><spring:message code="provider.archive.description"/></b>
<p/>
<a href="${resourceUrl}" target="_blank">${resourceUrl}</a>
<p/><p/>
Back to the <a href="${pageContext.request.contextPath}/occurrences/${occurrenceRecordKey}/">occurrence page</a> 


</td>
</tr>
</table>