<%@ include file="/common/taglibs.jsp"%>
<%@ attribute name="name" required="false" rtexprvalue="true" %>
<%@ attribute name="selectedDay" required="false" rtexprvalue="true" %>
<%@ attribute name="selectedMonth" required="false" rtexprvalue="true" %>
<%@ attribute name="selectedYear" required="false" rtexprvalue="true" %>
<%@ attribute name="callback" required="false" rtexprvalue="true" %>
<%@ attribute name="hideLabels" required="false" rtexprvalue="true" %>

<c:if test="${hideLabels}"><spring:message code="day"/>:</c:if> 
<select id="<c:if test="${not empty name}">${name}_</c:if>day" name="<c:if test="${not empty name}">${name}_</c:if>day" class="daySelect" <c:if test="${not empty callback}">onchange="javascript:${callback}"</c:if>>

<c:choose>
	<c:when test="${selectedMonth==2 && ( selectedYear % 4 == 0 || ((year % 100 != 0) && (year % 400 == 0)) ) }">
		<c:set var="dayCount" value="29"/>			
	</c:when>
	<c:when test="${selectedMonth==2}">
		<c:set var="dayCount" value="28"/>			
	</c:when>
	<c:when test="${selectedMonth==4 || selectedMonth==6 || selectedMonth==9 || selectedMonth==11}">
		<c:set var="dayCount" value="30"/>			
	</c:when>
	<c:otherwise>
		<c:set var="dayCount" value="31"/>			
	</c:otherwise>
</c:choose>
<c:forEach begin="1" end="${dayCount}" var="day">
	<option value="<c:if test="${day<10}">0</c:if>${day}" <c:if test="${selectedDay==day}"> selected="true"</c:if>>${day}</option>
</c:forEach>
</select>
			
<c:if test="${hideLabels}"><spring:message code="month"/>:</c:if>
<select id="<c:if test="${not empty name}">${name}_</c:if>month" name="<c:if test="${not empty name}">${name}_</c:if>month" class="monthSelect" onchange="javascript:validateDayListFromMonthSelector(this.parentNode);<c:if test="${not empty callback}">${callback}</c:if>">
	<option value="01"<c:if test="${selectedMonth==01}"> selected="true"</c:if>><spring:message code="month.jan"/></option>
	<option value="02"<c:if test="${selectedMonth==02}"> selected="true"</c:if>><spring:message code="month.feb"/></option>
	<option value="03"<c:if test="${selectedMonth==03}"> selected="true"</c:if>><spring:message code="month.mar"/></option>
	<option value="04"<c:if test="${selectedMonth==04}"> selected="true"</c:if>><spring:message code="month.apr"/></option>
	<option value="05"<c:if test="${selectedMonth==05}"> selected="true"</c:if>><spring:message code="month.may"/></option>
	<option value="06"<c:if test="${selectedMonth==06}"> selected="true"</c:if>><spring:message code="month.jun"/></option>
	<option value="07"<c:if test="${selectedMonth==07}"> selected="true"</c:if>><spring:message code="month.jul"/></option>
	<option value="08"<c:if test="${selectedMonth==08}"> selected="true"</c:if>><spring:message code="month.aug"/></option>
	<option value="09"<c:if test="${selectedMonth==09}"> selected="true"</c:if>><spring:message code="month.sep"/></option>
	<option value="10"<c:if test="${selectedMonth==10}"> selected="true"</c:if>><spring:message code="month.oct"/></option>
	<option value="11"<c:if test="${selectedMonth==11}"> selected="true"</c:if>><spring:message code="month.nov"/></option>
	<option value="12"<c:if test="${selectedMonth==12}"> selected="true"</c:if>><spring:message code="month.dec"/></option>
</select>

<c:if test="${hideLabels}"><spring:message code="year"/>:</c:if>

<input id="<c:if test="${not empty name}">${name}_</c:if>year" 
	name="<c:if test="${not empty name}">${name}_</c:if>year" 
	class="yearInput" 
	onKeyUp="javascript:validateDayListFromMonthSelector(this.parentNode);<c:if test="${not empty callback}">${callback}</c:if>"
	value="${selectedYear}"
	maxlength="4"
/>