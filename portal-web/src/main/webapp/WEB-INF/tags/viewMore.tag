<%@ include file="/common/taglibs.jsp"%>
<%@ attribute name="text" required="true" rtexprvalue="true" %>
<%@ attribute name="upper" required="true" rtexprvalue="true" type="java.lang.Integer" %>
<%@ attribute name="lower" required="true" rtexprvalue="true" type="java.lang.Integer" %>
<%@ attribute name="containerId" required="true" rtexprvalue="true" %>
<span id="${containerId}">
	<string:truncateNicely upper="${upper}" lower="${lower}">${text}</string:truncateNicely>
	<a href="javascript:swapContent('${containerId}','${containerId}Full');"><spring:message code="view.more" text="view more"/></a>
</span>
<span id="${containerId}Full" style="visibility:hidden; position:absolute; left:-1000px;">${text} 	<a href="javascript:swapContent('${containerId}','${containerId}Full');"><spring:message code="view.less" text="view less"/></a></span>