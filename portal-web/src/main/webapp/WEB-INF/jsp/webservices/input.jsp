<%@ include file="/common/taglibs.jsp"%>
<div id="twopartheader">	
	<h2><spring:message code="webservice.title"/></h2>
	<h3 style="font-size: 1.1em"><spring:message code="webservice.introduction" text="webservice.introduction"/></h3>
</div>

<tiles:insert page="/webservices/${localeName}/${webservicePageName}.jsp"/>
