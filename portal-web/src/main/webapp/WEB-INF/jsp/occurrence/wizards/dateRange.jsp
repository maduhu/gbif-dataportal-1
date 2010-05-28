<%@ include file="/common/taglibs.jsp"%>
<div id="dateRangeSelector" class="otherDetailsFilterHelp">

<c:set var="startDay"><fmt:formatDate value="${today}" pattern="dd"/></c:set>
<c:set var="startMonth"><fmt:formatDate value="${today}" pattern="MM"/></c:set>
<c:set var="startYear"><fmt:formatDate value="${today}" pattern="yyyy"/></c:set>
<c:set var="endDay"><fmt:formatDate value="${oneYearAgo}" pattern="dd"/></c:set>
<c:set var="endMonth"><fmt:formatDate value="${oneYearAgo}" pattern="MM"/></c:set>
<c:set var="endYear"><fmt:formatDate value="${oneYearAgo}" pattern="yyyy"/></c:set>

<table id="dateSelectors" style="margin-bottom:10px;"><tr><td>
<span style="font-weight:bold;"><spring:message code="start.date" text="Start date"/>:</span>
<div id="startDate">
	<gbiftag:dateSelect name="sd" selectedDay="${endDay}" selectedMonth="${endMonth}" selectedYear="${endYear}" callback="dateChanged();"/>	
</div>	
</td><td>
<span style="font-weight:bold;padding-left:50px;"><spring:message code="end.date" text="End date"/>:</span>
<div id="endDate" style="padding-left:50px;">
	<gbiftag:dateSelect name="ed" selectedDay="${startDay}" selectedMonth="${startMonth}" selectedYear="${startYear}" callback="dateChanged();"/>
</div>
</td>
</tr>
<tr><td colspan="2"><span id="dateWarnings" style="color:#FF0000; visibility:hidden;">Invalid year supplied!</span></td></tr>
</table>

<input type="checkbox" id="singleDate" onchange="javascript:dateChanged();toggleDateSelector('endDate', this.checked);"/> <spring:message code="occurrencedate.use.specific.date"/>

<ul class="classificationCondensed" style="margin-top:10px;">
<li><a href="javascript:setDateSelector('sd','<fmt:formatDate value="${oneMonthAgo}" pattern="dd"/>','<fmt:formatDate value="${oneMonthAgo}" pattern="MM"/>','<fmt:formatDate value="${oneMonthAgo}" pattern="yyyy"/>');setDateSelector('ed','<fmt:formatDate value="${today}" pattern="dd"/>','<fmt:formatDate value="${today}" pattern="MM"/>','<fmt:formatDate value="${today}" pattern="yyyy"/>');toggleDateSelector('endDate', false);toggleSingleDate(false);dateChanged();"><spring:message code="occurrencedate.last.month"/></a></li>
<li><a href="javascript:setDateSelector('sd','<fmt:formatDate value="${sixMonthsAgo}" pattern="dd"/>','<fmt:formatDate value="${sixMonthsAgo}" pattern="MM"/>','<fmt:formatDate value="${sixMonthsAgo}" pattern="yyyy"/>');setDateSelector('ed','<fmt:formatDate value="${today}" pattern="dd"/>','<fmt:formatDate value="${today}" pattern="MM"/>','<fmt:formatDate value="${today}" pattern="yyyy"/>');toggleDateSelector('endDate', false);toggleSingleDate(false);dateChanged();"><spring:message code="occurrencedate.last.six.months"/></a></li>
</ul>
<ul class="classificationCondensed" style="margin-top:10px;margin-bottom:10px;">
<li><a href="javascript:setDateSelector('sd','<fmt:formatDate value="${oneYearAgo}" pattern="dd"/>','<fmt:formatDate value="${oneYearAgo}" pattern="MM"/>','<fmt:formatDate value="${oneYearAgo}" pattern="yyyy"/>');setDateSelector('ed','<fmt:formatDate value="${today}" pattern="dd"/>','<fmt:formatDate value="${today}" pattern="MM"/>','<fmt:formatDate value="${today}" pattern="yyyy"/>'); toggleDateSelector('endDate', false); toggleSingleDate(false); dateChanged();"><spring:message code="occurrencedate.within.the.last.year"/></a></li>
<li><a href="javascript:setDateSelector('sd','<fmt:formatDate value="${fiveYearsAgo}" pattern="dd"/>','<fmt:formatDate value="${fiveYearsAgo}" pattern="MM"/>','<fmt:formatDate value="${fiveYearsAgo}" pattern="yyyy"/>');setDateSelector('ed','<fmt:formatDate value="${today}" pattern="dd"/>','<fmt:formatDate value="${today}" pattern="MM"/>','<fmt:formatDate value="${today}" pattern="yyyy"/>'); toggleDateSelector('endDate', false); toggleSingleDate(false); dateChanged();"><spring:message code="occurrencedate.within.the.last.five.years"/></a></li>
<li><a href="javascript:setDateSelector('sd','<fmt:formatDate value="${tenYearsAgo}" pattern="dd"/>','<fmt:formatDate value="${tenYearsAgo}" pattern="MM"/>','<fmt:formatDate value="${tenYearsAgo}" pattern="yyyy"/>');setDateSelector('ed','<fmt:formatDate value="${today}" pattern="dd"/>','<fmt:formatDate value="${today}" pattern="MM"/>','<fmt:formatDate value="${today}" pattern="yyyy"/>'); toggleDateSelector('endDate', false); toggleSingleDate(false); dateChanged();"><spring:message code="occurrencedate.within.the.last.ten.years"/></a></li>
</li>
</ul>

<!--
<input type="submit" id="updateDateRange" value="Use these dates" onclick="javascript:dateChanged();addConstructedFilter();"/>
-->
</div>