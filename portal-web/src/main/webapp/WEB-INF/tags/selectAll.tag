<%@ include file="/common/taglibs.jsp"%>
<%@ attribute name="fieldsetId" required="true" rtexprvalue="true" %>
<p class="selectAll">
<a href="javascript:selectAll('${fieldsetId}');"><spring:message code="select.all"/></a> 
| 
<a href="javascript:deselectAll('${fieldsetId}');"><spring:message code="deselect.all"/></a>
</p>