<%@ include file="/common/taglibs.jsp"%>
<div id="twopartheader">					
	<h2><spring:message code="download.ready"/></h2>
	<h3><spring:message code="download.ready.subheader"/></h3>
</div><!-- End twopartheader -->

<table class="downloadLayout"><tr>
<td>
<p>
<spring:message code="download.start.download.link"/>
</p>
<fieldset class="downloadLink">
<c:if test="${not empty fileDescription}">
	<p class="subject">
		<spring:message code="${fileType}" text="${fileType}"/>
	</p>
	<p>
		<spring:message code="description"/>:	
		${fileDescription} 
		(<a href="${pageContext.request.contextPath}${originalUrl}"><spring:message code="download.original.search" text="Original search"/></a>)
	</p>
</c:if>
<ul class="genericList">
<li><a href="${pageContext.request.contextPath}/download/${fileName}">${fileName} (<spring:message code="download.approx.file.size"/>
<string:trim>
<c:choose>
	<c:when test="${fileSize>1048576}">
		<% out.print(((Long) request.getAttribute("fileSize"))/1048576 ); %> MB
	</c:when>
	<c:when test="${fileSize>1024 && fileSize<1048576}">
		<% out.print(((Long) request.getAttribute("fileSize"))/1024 ); %> KB
	</c:when>
	<c:otherwise>
		${fileSize} B
	</c:otherwise>
</c:choose></string:trim>)</a></li>
</ul>
</fieldset>
<c:if test="${not empty timeTakenInMillis}"><p class="minorInformation"><spring:message code="download.generation.time" text="Time taken:"/><gbiftag:duration durationInMillisecs="${timeTakenInMillis}"/></p></c:if>
</td>
</tr>
</table>