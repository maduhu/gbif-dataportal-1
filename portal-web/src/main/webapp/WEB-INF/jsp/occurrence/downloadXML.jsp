<%@ include file="/common/taglibs.jsp"%>
<h4><spring:message code="occurrence.record.download.xml.title" text="Download matching records in XML"/></h4>

<tiles:insert page="downloadHelp.jsp"/>

<form method="get" action="${pageContext.request.contextPath}/occurrences/downloadResults.htm">
<div id="downloadFields">
<h5 id="formatOptions"><spring:message code="download.format.options" text="Format options"/></h5>
<fieldset>
	<input type="radio" name="format" value="brief" checked="true"/> <spring:message code="occurrence.record.download.format.darwin.brief" text="Brief Darwin Core records (styled for easy viewing in a browser)"/><br/>
<!--
	<input type="radio" name="format" value="darwin" disabled="true"/> <spring:message code="occurrence.record.download.format.darwin.full" text="Full Darwin Core records (styled for easy viewing in a browser)"/><br/>	
-->	
	<input type="radio" name="format" value="kml"/> <spring:message code="occurrence.record.download.format.ge"/><br/>
</fieldset>

<input type="hidden" name="criteria" value="<gbif:criteria criteria="${criteria}"/>"/>
<input type="hidden" name="searchId" value="${searchId}"/>
</div>
<input type="submit" value="<spring:message code="download.now"/>"/>
</form>

