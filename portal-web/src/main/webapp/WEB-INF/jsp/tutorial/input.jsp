<%@ include file="/common/taglibs.jsp"%>
<div id="twopartheader">	
	<h2><spring:message code="tutorial.title"/></h2>
	<h3 style="font-size: 1.1em"><spring:message code="tutorial.${tutorialPageName}" text="??tutorial.${tutorialPageName}??"/></h3>
</div>

<tiles:insert page="/help/${localeName}/${tutorialPageName}.html"/>
