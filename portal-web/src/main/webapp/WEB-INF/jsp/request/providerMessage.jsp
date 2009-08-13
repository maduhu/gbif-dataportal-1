<%@ include file="/common/taglibs.jsp"%>
<div id="twopartheader">					
	<h2><spring:message code="provider.requesting"/></h2>
	<h3><spring:message code="provider.requesting.subheader"/></h3>
</div><!-- End twopartheader -->
<table class="providerRequest">
<tr>
<td>
<img src="${pageContext.request.contextPath}/skins/shared/images/icons/loading.gif"/>
</td>
<td class="providerInfo">
<p>
<b><spring:message code="provider.requesting.wait"/></b> 
</p>
</td>
</tr>
</table>

<script>
requestDataFromProvider('${pageContext.request.contextPath}/occurrences/${occurrenceRecordKey}/providerUrl/');
</script>