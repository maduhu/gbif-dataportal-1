<%@ include file="/common/taglibs.jsp"%>
<div id="twopartheader">
	<h2>
		<c:choose>
			<c:when test="${not empty dataResource}">
				<spring:message code="dataset.resource" text="Dataset"/>: <a href="${pageContext.request.contextPath}/datasets/resource/${dataResource.key}"><span class="subject">${dataResource.name}</span></a>
				<c:if test="${dataResource.logoUrl!=null}">
					<gbiftag:scaleImage imageUrl="${dataResource.logoUrl}" maxWidth="200" maxHeight="80" imgClass="logo" addLink="false"/>
				</c:if>
			</c:when>		
			<c:when test="${not empty dataProvider}">
				<spring:message code="dataset.provider"/>: <a href="${pageContext.request.contextPath}/datasets/provider/${dataProvider.key}"><span class="subject">${dataProvider.name}</span></a>
				<c:if test="${dataProvider.logoUrl!=null}">	
					<gbiftag:scaleImage imageUrl="${dataProvider.logoUrl}" maxWidth="200" maxHeight="80" imgClass="logo" addLink="false"/>
				</c:if>
			</c:when>
			<c:otherwise>
				<spring:message code="indexing.history.title"/>
			</c:otherwise>
		</c:choose>
	</h2>
	<h3>
		<c:choose>
		<c:when test="${not empty dataResource || not empty dataProvider}">
				<spring:message code="indexing.history.title"/>
		</c:when>
		<c:otherwise>
				<spring:message code="portal.title"/>
		</c:otherwise>		
		</c:choose>		
	</h3>	
</div>
<c:choose>
<c:when test="${not empty indexingHistory}">

<h4><spring:message code="indexing.history.summary"/></h4>
<fieldset>
<p><label for="totalIndexing"><spring:message code="indexing.history.summary.total.time"/>:</label><gbiftag:duration durationInMillisecs="${totalProcessing}"/></p>
<p><label for="totalHarvesting"><spring:message code="indexing.history.summary.harvesting.time"/>:</label><gbiftag:duration durationInMillisecs="${totalHarvesting}"/></p>
<p><label for="totalProcessing"><spring:message code="indexing.history.summary.processing.time"/>:</label><gbiftag:duration durationInMillisecs="${totalExtraction}"/></p>
</fieldset>

<p>
<b><spring:message code="harvesting"/></b> 
	- <spring:message code="harvesting.description"/> <br/>
<b><spring:message code="extraction"/></b> 
	- <spring:message code="extraction.description"/> 
</p>

<c:if test="${firstAvailableLog!=null && lastAvailableLog!=null}">
<p>
	<c:set var="a0"><b><fmt:formatDate value="${firstAvailableLog}"/></b></c:set>
	<c:set var="a1"><b><fmt:formatDate value="${lastAvailableLog}"/></b></c:set>
	<spring:message code="indexing.history.log.messages.available" arguments="${a0}____${a1}" argumentSeparator="____"/>
</p>
<p>
	<spring:message code="indexing.history.log.messages.earliest" arguments="${a0}" argumentSeparator="&&&&"/>
</p>
</c:if>

<c:if test="${dataResource!=null}">
<ul class="genericList">
	<li><a href="${pageContext.request.contextPath}/datasets/resource/${dataResource.key}/datatracking/"><spring:message code="indexing.history.preview.indexed"/></a></li>
<table><tr><td style="background-color:#D4ECF5; width:600px; padding:10px;"><spring:message code="indexing.history.preview.indexed.message"/></td></tr></table>	
</ul>	
</c:if>

<c:if test="${not empty timeSeriesChart}">
<h4><spring:message code="indexing.history.processing.timeline"/></h4>
<p>
<img src="${pageContext.request.contextPath}/download/${timeSeriesChart}"/>
<table>
<tr><td style="background-color:blue; width:10px; height:6px; border:1px solid #CCCCCC;"></td><td style="padding-left:10px;"><spring:message code="harvesting" text="Harvesting"/></td></tr>
<tr><td style="background-color:red; width:10px; height:6px;border:1px solid #CCCCCC;"></td><td style="padding-left:10px;"><spring:message code="extraction" text="Processing"/></td></tr>
</table>
</p>
</c:if>

<table class="indexingHistory">
<thead>
	<th><spring:message code="indexing.history.processid"/></th>
	<c:if test="${empty dataProvider}">		
		<th><spring:message code="data.provider"/></th>
	</c:if>
	<c:if test="${empty dataResource}">			
		<th><spring:message code="dataset"/></th>	
	</c:if>	
	<th><spring:message code="indexing.history.event"/></th>		
	<th><spring:message code="indexing.history.start"/></th>			
	<th><spring:message code="indexing.history.end"/></th>				
	<th><spring:message code="indexing.history.duration"/></th>
</thead>
<tbody>
<c:forEach items="${indexingHistory}" var="indexingActivity">
<tr <c:if test="${indexingActivity.eventName=='extraction'}">class="extractionEvent"</c:if>>
	<td>
		<a href="${indexingActivity.logGroup}">${indexingActivity.logGroup}</a>
	</td>
	<c:if test="${empty dataProvider}">	
	<td>
		<a href="${pageContext.request.contextPath}/datasets/provider/${indexingActivity.dataProviderKey}/indexing/">${indexingActivity.dataProviderName}</a>
	</td>
	</c:if>
	<c:if test="${empty dataResource}">		
	<td>
		<a href="${pageContext.request.contextPath}/datasets/resource/${indexingActivity.dataResourceKey}/indexing/">${indexingActivity.dataResourceName}</a>	
	</td>
	</c:if>
	<td>
		<spring:message code="${indexingActivity.eventName}" text="${indexingActivity.eventName}"/>		
	</td>
	<td>
		<fmt:formatDate value="${indexingActivity.startDate}" pattern="MMM dd, yyyy - HH:mm"/>			
	</td>
	<td>
		<fmt:formatDate value="${indexingActivity.endDate}" pattern="MMM dd, yyyy - HH:mm"/>
	</td>
	<td>
		<c:choose>
			<c:when test="${indexingActivity.durationInMillisecs!=null}">
				<gbiftag:duration durationInMillisecs="${indexingActivity.durationInMillisecs}"/>
			</c:when>	
			<c:otherwise>
				<spring:message code="indexing.history.manually.terminated"/>
			</c:otherwise>			
		</c:choose>
	</td>
</tr>
</c:forEach>
</tbody>
</table>
</c:when>
<c:otherwise>
<p>
	<spring:message code="indexing.history.no.available"/>
</p>		
</c:otherwise>
</c:choose>