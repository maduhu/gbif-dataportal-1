<%@ include file="/common/taglibs.jsp"%>
<div id="twopartheader">
	<h2>
		<c:choose>
			<c:when test="${not empty dataProvider}">
				<spring:message code="dataset.provider"/>: <a href="${pageContext.request.contextPath}/datasets/provider/${dataProvider.key}"><span class="subject">${dataProvider.name}</span></a>
				<c:if test="${dataProvider.logoUrl!=null}">
					<gbiftag:scaleImage imageUrl="${dataProvider.logoUrl}" maxWidth="200" maxHeight="80" imgClass="logo" addLink="false"/>
				</c:if>
			</c:when>
			<c:when test="${not empty dataResource}">
				<spring:message code="dataset.resource" text="Dataset"/>: <a href="${pageContext.request.contextPath}/datasets/provider/${dataProvider.key}"><span class="subject">${dataResource.name}</span></a>
				<c:if test="${dataResource.logoUrl!=null}">
					<gbiftag:scaleImage imageUrl="${dataResource.logoUrl}" maxWidth="200" maxHeight="80" imgClass="logo" addLink="false"/>
				</c:if>
			</c:when>
			<c:otherwise>
				Indexing process
			</c:otherwise>
		</c:choose>
	</h2>
	<h3>
		<c:if test="${not empty dataResource || not empty dataProvider}">
				<spring:message code="indexing.history.title"/> - 
		</c:if>
			Process ${logGroup}
	</h3>	
</div>
<table class="indexingHistory">
<thead>
	<c:if test="${empty dataResource}">
	<th><spring:message code="dataset"/></th>
	</c:if>
	<th><spring:message code="indexing.history.event"/></th>
	<th><spring:message code="indexing.history.event.count" text="Event count"/></th>
	<th></th>			
</thead>
<tbody>
<c:forEach items="${logStats}" var="logStat">
<tr>
	<c:if test="${empty dataResource}">
	<td>${logStat.entityName}</td>
	</c:if>
	<td>
		<gbiftag:logEvent eventId="${logStat.eventId}" outParam="eventname"/>
		<spring:message code="${eventname}" text="${eventname}"/>
	</td>
	<td>${logStat.eventCount}</td>
	<td><a href="${pageContext.request.contextPath}/datasets/datatracking/<c:if test="${not empty dataProvider}">provider/${dataProvider.key}/</c:if><c:if test="${not empty dataResource}">resource/${dataResource.key}/</c:if>logs/?logGroup=${logGroup}&event=${logStat.eventId}">View</a></td>
</tr>
</c:forEach>
</tbody>
</table>
<p>
<ul class="genericList">
	<c:if test="${not empty dataResource}">
		<li>
			<a href="${pageContext.request.contextPath}/datasets/<c:if test="${not empty dataResource}">resource/${dataResource.key}/</c:if>indexing/">View all indexing history for ${dataResource.name}</a>
		</li>
	</c:if>
	<c:if test="${not empty dataProvider}">
	<li>
		<a href="${pageContext.request.contextPath}/datasets/<c:if test="${not empty dataProvider}">provider/${dataProvider.key}/</c:if>indexing/">View all indexing history for ${dataProvider.name}</a>
		</li>
	</c:if>
</ul>
</p>