<%@ include file="/common/taglibs.jsp"%>
<h4><spring:message code="occurrence.record.download.spreadsheet.title"/></h4>

<tiles:insert page="downloadHelp.jsp"/>
<p><spring:message code="occurrence.record.download.maximum.message"/></p>

<div id="downloadFields">

<form method="get" action="${pageContext.request.contextPath}/species/startDownload.htm">
<h5 id="formatOptions"><spring:message code="download.format.options"/></h5>
<fieldset>
	<input type="radio" name="format" value="tsv" checked="true"/> <spring:message code="download.format.tab"/><br/>
	<input type="radio" name="format" value="csv"/> <spring:message code="download.format.csv"/><br/>
	<input type="radio" name="format" value="excel"/> <spring:message code="download.format.excel"/><br/>	
</fieldset>

<h5 id="requiredFieldsTitle"><spring:message code="occurrence.record.required.fields"/></h5>

<c:forEach items="${mandatoryFields}" var="field">
	<input type="checkbox" name="${field.fieldName}" checked="true"<c:if test="${!param['ignoreMandatory']}"> disabled="true"</c:if>/> <spring:message code="${field.fieldI18nNameKey}" text="${field.fieldI18nNameKey}"/>
</c:forEach>
<c:if test="${!param['ignoreMandatory']}">
<c:forEach items="${mandatoryFields}" var="field">
	<input type="checkbox" name="${field.fieldName}" checked="true" style="visibility:hidden;"/>
</c:forEach>
</c:if>

<h5 id="taxonomyFieldsTitle"><spring:message code="occurrence.record.taxonomy.legend"/></h5>
<gbiftag:selectAll fieldsetId="classificationFields"/>
<table id="classificationFields">
	<tr>
<c:forEach items="${classificationFields}" var="field" varStatus="fieldStatus">
	<td><input type="checkbox" name="${field.fieldName}" checked="true"/> <spring:message code="${field.fieldI18nNameKey}" text="${field.fieldI18nNameKey}"/></td>
</c:forEach>
	</tr>
</table>

<h5 id="otherFieldsTitle"><spring:message code="occurrence.record.other.fields"/></h5>
<gbiftag:selectAll fieldsetId="otherFields"/>
<table id="otherFields">
	<tr>
<c:forEach items="${otherFields}" var="field" varStatus="fieldStatus">
	<c:if test="${fieldStatus.index mod 6==0 && fieldStatus.index!=0}">
		</tr><tr>
	</c:if>
	<td><input type="checkbox" name="${field.fieldName}" checked="true"/> <spring:message code="${field.fieldI18nNameKey}" text="${field.fieldI18nNameKey}"/></td>
</c:forEach>
	</tr>
</table>

<input type="hidden" name="criteria" value="<gbif:criteria criteria="${criteria}"/>"/>
<input type="hidden" name="searchId" value="${searchId}"/>
<input id="downloadNow" type="submit" value="<spring:message code="download.now"/>"/>
</form>

</div><!-- end downloadfields -->