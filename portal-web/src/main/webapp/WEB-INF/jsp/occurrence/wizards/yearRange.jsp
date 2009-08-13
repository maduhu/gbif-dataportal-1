<%@ include file="/common/taglibs.jsp"%>
<div id="dateRangeSelector" class="otherDetailsFilterHelp">
<table>
<tr>
<td><spring:message code="year.filter.starting.from"/><br/><span style="font-size: 0.7em;"><spring:message code="year.filter.and.including"/></span></td>
<td style="vertical-align:top;"><input id="startYear" type="text" class="yearInput" value="${thisYear - (thisYear mod 10)}" onchange="javascript:yearRangeChanged();" onkeyup="javascript:yearRangeChanged();"/></td>
<td style="padding-left:20px;"><spring:message code="year.filter.up.to"/><br/><span style="font-size: 0.7em;"><spring:message code="year.filter.and.including"/></span></td>
<td style="vertical-align:top;"><input id="endYear" type="text" class="yearInput" value="${thisYear}" onchange="javascript:yearRangeChanged();" onkeyup="javascript:yearRangeChanged();"/></td>
</tr>
</table>
</div>