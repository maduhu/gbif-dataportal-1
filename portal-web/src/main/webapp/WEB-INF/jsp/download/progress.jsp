<%@ include file="/common/taglibs.jsp"%>
<div id="twopartheader">					
	<h2><spring:message code="download.monitor" text="Download monitor"/></h2>
	<h3>Active and completed downloads</h3>
</div><!-- End twopartheader -->

<h4>Download monitor</h4>
<p>
	Below is a list of downloads currently in progress.
</p>
<table class="results">
<thead>
	<th>Download type</th>	
	<th>Description</th>
	<th>Time Started</th>
	<th>Search Url</th>
	<th>Download Page</th>	
</thead>
<tbody>
<c:forEach items="${inProgress}" var="fdStatus">
<tr>
	<td><spring:message code="${fdStatus.properties['fileType']}"/></td>	
	<td>${fdStatus.properties['fileDescription']}</td>
	<td><fmt:formatDate pattern="HH:mm dd/MM/yyyy" value="${fdStatus.properties['createdDate']}"/></td>
	<td><a href="${pageContext.request.contextPath}${fdStatus.properties['originalUrl']}">View</a></td>
	<td><a href="${pageContext.request.contextPath}/download/preparingDownload.htm?downloadFile=${fdStatus.fileName}">Download page</a>
</tr>
</c:forEach>
</tbody>
</table>


<h4>Completed downloads</h4>
<p>
	Below is a list of downloads that have recently completed.
</p>
<table class="results">
<thead>
	<th>Download type</th>	
	<th>Description</th>
	<th>Time Started</th>	
	<th>Time taken</th>
	<th>Search Url</th>
	<th>Download Page</th>
</thead>
<tbody>
<c:forEach items="${completed}" var="fdStatus">
<tr>
	<td><spring:message code="${fdStatus.properties['fileType']}"/></td>	
	<td>${fdStatus.properties['fileDescription']}</td>
	<td><fmt:formatDate pattern="HH:mm dd/MM/yyyy" value="${fdStatus.properties['createdDate']}"/></td>	
	<td><gbiftag:duration durationInMillisecs="${fdStatus.properties['timeTakenInMillis']}"/></td>
	<td><a href="${pageContext.request.contextPath}${fdStatus.properties['originalUrl']}">View</a></td>
	<td><a href="${pageContext.request.contextPath}/download/downloadReady.htm?downloadFile=${fdStatus.fileName}">Download page</a>	
</tr>
</c:forEach>
</tbody>
</table>