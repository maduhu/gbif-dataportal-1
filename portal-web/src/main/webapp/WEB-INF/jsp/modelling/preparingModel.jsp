<%@ include file="/common/taglibs.jsp"%>
<div id="twopartheader">					
	<h2><spring:message code="model.preparing"/></h2>
	<h3><spring:message code="model.preparing.subheader"/></h3>
</div><!-- End twopartheader -->

<table class="prepareDownload">
<tr>
<td>
<img src="${pageContext.request.contextPath}/skins/shared/images/icons/loading.gif"/>
</td>
<td class="downloadInfo">
<c:if test="${not empty fileType}">
  <h4><spring:message code="${fileType}" text="${fileType}"/></h4>
 <c:if test="${not empty fileDescription}">
   <p class="downloadLink">
     <spring:message code="description"/>: ${fileDescription} 
   </p>
 </c:if>   
</c:if>
<p>
<b><spring:message code="model.preparing.wait"/></b> 
</p>
<p>
<spring:message code="model.preparing.check.back"/>
</p>
<p>
<spring:message code="model.preparing.redirect"/>
</p>
</td>
</tr>
</table>
<script>
pollforFile('${pageContext.request.contextPath}/download/isDownloadReady.htm?downloadFile=${fileName}');
</script>