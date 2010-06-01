<%@ include file="/common/taglibs.jsp"%>
<%@ page import="java.util.*" %>
<%@ page import="javax.servlet.jsp.*" %>
<%@ page import="javax.servlet.jsp.jstl.core.*" %>
<%@ page import="org.apache.commons.lang.*" %>
<%@ page import="org.gbif.portal.util.log.*" %>
<%@ page import="org.gbif.portal.dto.log.*" %>
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
				<spring:message code="dataset.logs" text="Portal Logs"/>
			</c:otherwise>
		</c:choose>
	</h2>
	<h3>
		<c:if test="${not empty dataResource || not empty dataProvider}"><spring:message code="dataset.logs" text="Portal Logs"/></c:if>
	</h3>	
</div>

<h4><spring:message code="log.console" text="Log console"/></h4>
<div id="logConsole">
<form id="logConsoleForm" method="get" action="">
<fieldset>

<c:if test="${not empty dataResources}">
<label><spring:message code="log.console.dataset" text="Dataset"/>:</label>
<select id="resource" name="resource">
	<option value=""><spring:message code="all" text="All"/></option>
<c:forEach items="${dataResources}" var="dr">
	<option value="${dr.key}" <c:if test="${param['resource']==dr.key}">selected="true"</c:if>>${dr.name}</option>
</c:forEach>
</select><br/>
</c:if>

<label><spring:message code="log.console.event" text="Event"/>:</label>
<select name="event" id="event">
		<option value=""><spring:message code="all" text="All"/></option>
<c:if test="${not empty harvestEvents}"><optgroup label="<spring:message code="log.console.harvesting" text="Harvesting"/>"></c:if>
<c:forEach items="${harvestEvents}" var="event">
	<c:if test="${event.key!=null}">
		<option value="${event.key}"<c:if test="${param['event']==event.key}"> selected="true"</c:if>>${event.value}</option>
	</c:if>	
</c:forEach>
<c:if test="${not empty harvestEvents}"></optgroup></c:if>
<c:if test="${not empty extractEvents}"><optgroup label="<spring:message code="log.console.extraction" text="Extraction"/>"></c:if>
<c:forEach items="${extractEvents}" var="event">
	<c:if test="${event.key!=null}">
		<option value="${event.key}"<c:if test="${param['event']==event.key}"> selected="true"</c:if>>${event.value}</option>
	</c:if>	
</c:forEach>
<c:if test="${not empty extractEvents}"></optgroup></c:if>
<c:if test="${not empty userEvents}"><optgroup label="<spring:message code="log.console.user" text="User"/>"></c:if>
<c:forEach items="${userEvents}" var="event">
	<c:if test="${event.key!=null}">
		<option value="${event.key}"<c:if test="${param['event']==event.key}"> selected="true"</c:if>>${event.value}</option>
	</c:if>	
</c:forEach>
<c:if test="${not empty userEvents}"></optgroup></c:if>
<c:if test="${not empty usageEvents}"><optgroup label="Usage"></c:if>
<c:forEach items="${usageEvents}" var="event">
	<c:if test="${event.key!=null}">
		<option value="${event.key}"<c:if test="${param['event']==event.key}"> selected="true"</c:if>>${event.value}</option>
	</c:if>	
</c:forEach>
<c:if test="${not empty usageEvents}"></optgroup></c:if>
<c:if test="${not empty otherEvents}"><optgroup label="<spring:message code="log.console.other" text="Other"/>"></c:if>
<c:forEach items="${otherEvents}" var="event">
	<c:if test="${event.key!=null}">
		<option value="${event.key}"<c:if test="${param['event']==event.key}"> selected="true"</c:if>>${event.value}</option>
	</c:if>	
</c:forEach>
<c:if test="${not empty otherEvents}"></optgroup></c:if>
</select><br/>

<label><spring:message code="log.console.loggroup" text="Log group"/>:</label>
<input name="logGroup" value="${param['logGroup']}"/><br/>

<label><spring:message code="log.console.level" text="Level"/>:</label>
<select name="logLevel">
	<option value=""<c:if test="${param['logLevel']==null}"> selected="true"</c:if>>ALL</option>
<c:forEach items="${loggingLevels}" var="logLevel">
	<option value="${logLevel.key}"<c:if test="${param['logLevel']==logLevel.key}"> selected="true"</c:if>>${logLevel.value}</option>
</c:forEach>
</select><br/>

<c:if test="${empty sd_day}"><c:set var="sd_day"><fmt:formatDate value="${oneYearAgo}" pattern="dd"/></c:set></c:if>
<c:if test="${empty sd_month}"><c:set var="sd_month"><fmt:formatDate value="${oneYearAgo}" pattern="MM"/></c:set></c:if>
<c:if test="${empty sd_year}"><c:set var="sd_year"><fmt:formatDate value="${oneYearAgo}" pattern="yyyy"/></c:set></c:if>
<c:if test="${empty ed_day}"><c:set var="ed_day"><fmt:formatDate value="${today}" pattern="dd"/></c:set></c:if>
<c:if test="${empty ed_month}"><c:set var="ed_month"><fmt:formatDate value="${today}" pattern="MM"/></c:set></c:if>
<c:if test="${empty ed_year}"><c:set var="ed_year"><fmt:formatDate value="${today}" pattern="yyyy"/></c:set></c:if>

<br/>

<c:set var="startYear"><fmt:formatDate value="${today}" pattern="yyyy"/></c:set>

<label><spring:message code="log.console.start.date" text="Start date"/>:</label>
<span id="startDate">
	<gbiftag:dateSelect name="sd" selectedDay="${sd_day}" selectedMonth="${sd_month}" selectedYear="${sd_year}" callback="dateChanged();"/>	
</span><br/>

<label><spring:message code="log.console.end.date" text="End date"/>:</label>
<span id="endDate">
	<gbiftag:dateSelect name="ed" selectedDay="${ed_day}" selectedMonth="${ed_month}" selectedYear="${ed_year}" callback="dateChanged();"/>	
</span><br/>

<ul class="classificationCondensed" style="margin-top:10px;margin-bottom:10px; margin-left:170px;">
<li><a href="javascript:setDateSelector('sd','<fmt:formatDate value="${lastWeek}" pattern="dd"/>','<fmt:formatDate value="${lastWeek}" pattern="MM"/>','<fmt:formatDate value="${lastWeek}" pattern="yyyy"/>');setDateSelector('ed','<fmt:formatDate value="${today}" pattern="dd"/>','<fmt:formatDate value="${today}" pattern="MM"/>','<fmt:formatDate value="${today}" pattern="yyyy"/>');"><spring:message code="log.console.last.week"/></a></li>
<li><a href="javascript:setDateSelector('sd','<fmt:formatDate value="${oneMonthAgo}" pattern="dd"/>','<fmt:formatDate value="${oneMonthAgo}" pattern="MM"/>','<fmt:formatDate value="${oneMonthAgo}" pattern="yyyy"/>');setDateSelector('ed','<fmt:formatDate value="${today}" pattern="dd"/>','<fmt:formatDate value="${today}" pattern="MM"/>','<fmt:formatDate value="${today}" pattern="yyyy"/>');"><spring:message code="log.console.last.month"/></a></li>
<li><a href="javascript:setDateSelector('sd','<fmt:formatDate value="${oneYearAgo}" pattern="dd"/>','<fmt:formatDate value="${oneYearAgo}" pattern="MM"/>','<fmt:formatDate value="${oneYearAgo}" pattern="yyyy"/>');setDateSelector('ed','<fmt:formatDate value="${today}" pattern="dd"/>','<fmt:formatDate value="${today}" pattern="MM"/>','<fmt:formatDate value="${today}" pattern="yyyy"/>');"><spring:message code="log.console.last.year"/></a></li>
</ul>

<input type="submit" value="<spring:message code="log.console.submit" text="refresh"/>"/>

<c:if test="${not empty searchResults}">
<p style="padding-top:10px; padding-bottom:0px; margin-bottom:3px;">
<input id="download" name="download" type="checkbox" style="display:none;"/>
<img src="${pageContext.request.contextPath}/images/icons/disk.gif"/> <a href="javascript:downloadLogs('logConsoleForm');"><spring:message code="log.console.download"/></a>
<script type="text/javascript">
	function downloadLogs(formId){
		var form = document.getElementById(formId);
		document.getElementById("download").checked=true;
		form.action=""
		form.method="get";
		form.submit();
	}
	document.getElementById("download").checked=false;
</script>
</p>
</c:if>


</form>
</div>

<display:table 
	name="results" 
	export="false" 	
	class="results" 
	uid="log" 
	requestURI="logs?"
	sort="external"
	defaultsort="1"
	pagesize="100"
	>
	<c:if test="${empty dataResource}">	
  <display:column titleKey="log.console.dataset">
  		<a href="${pageContext.request.contextPath}/datasets/resource/${log.dataResourceKey}" title="${log.dataResourceName}"><string:truncateNicely lower="30" upper="40">${log.dataResourceName}</string:truncateNicely>	</a>
  </display:column>	
  </c:if>
  <display:column titleKey="log.console.loggroup">
  		${log.logGroupId}	
  </display:column>	
  <display:column titleKey="log.console.event">
  	<spring:message code="${log.eventName}" text="${log.eventName}"/>
  </display:column>	
  <display:column titleKey="log.console.level">
		<gbiftag:printLogLevel level="${log.level}"/>
  </display:column>	
  <display:column titleKey="log.console.taxon">
  		<a href="${pageContext.request.contextPath}/species/${log.taxonConceptKey}">${log.taxonName}</a>
  </display:column>	
  <display:column titleKey="log.console.record">
  	<c:if test="${log.occurrenceKey!=null && log.occurrenceKey!='0'}">
  		<a href="${pageContext.request.contextPath}/occurrences/${log.occurrenceKey}">${log.occurrenceKey}</a>	
  	</c:if>	
  </display:column>	  
  <display:column titleKey="log.console.message">
		${log.message}
  </display:column>	
  <display:column titleKey="log.console.count">
	  <c:if test="${log.count!=null && log.count>0}">${log.count}</c:if>
  </display:column>	
  <display:column property="timestamp" decorator="org.gbif.portal.web.ui.I18nDateTimeDecorator" titleKey="occurrence.search.results.date"/>
  <display:setProperty name="pagination.pagenumber.param">pageno</display:setProperty>
  <display:setProperty name="paging.banner.placement">both</display:setProperty>	
</display:table>