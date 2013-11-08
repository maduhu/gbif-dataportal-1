<%@ include file="/common/taglibs.jsp"%>
<link rel="stylesheet" href="${pageContext.request.contextPath}/<spring:theme code='newPortal.css'/>"/>
<div id="topmenu">
<ul>
<li><a href="${pageContext.request.contextPath}/"><spring:message code='topmenu.home'/></a></li>		
<tiles:insert page="mainMenu.jsp"/>
</ul>
</div><!-- End topmenu-->
<!-- Try to redirect some traffic to new portal -->
<tiles:insert page="newPortalAlert.jsp"/>
