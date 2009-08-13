<%@ include file="/common/taglibs.jsp"%>
<div id="twopartheader">					
	<h2><spring:message code="download.failed"/></h2>
	<h3><spring:message code="download.failed.subheader"/></h3>
</div><!-- End twopartheader -->
<table class="downloadLayout"><tr>
<td>
<c:if test="${not empty fileDescription}">
	<p class="subject">
		<spring:message code="${fileType}" text="${fileType}"/>
	</p>
	<p>
		<spring:message code="description"/>:	
		${fileDescription} 
	</p>
	<p style="max-width: 80px; width: 80px;" >
    <spring:message code="download.original.search" text="Original search"/>: <a href="${pageContext.request.contextPath}${originalUrl}">http://${header.host}${pageContext.request.contextPath}${originalUrl}</a>	
	</p>
	<p>
	   <c:set var="emailLink"><a href="mailto:portal@gbif.org">portal@gbif.org</a></c:set>
  	<spring:message code="download.failed.follow.on" arguments="${emailLink}" argumentSeparator="%%%%%%"/>
  </p>	
  <p>
    <a href="${originalUrl}">Click here</a> to return to the original search.  
  </p>  
</c:if>
</td>
</tr>
</table>