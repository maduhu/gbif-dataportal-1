<%@ include file="/common/taglibs.jsp"%>
<h4><spring:message code="occurrence.record.download.spreadsheet.title"/></h4>

<tiles:insert page="downloadHelp.jsp"/>
<p><spring:message code="occurrence.record.download.maximum.message"/></p>

<div id="downloadFields">

<form method="get" action="${pageContext.request.contextPath}/occurrence/startDownload.htm">
<h5 id="formatOptions"><spring:message code="download.format.options"/></h5>
<fieldset>
	<input type="radio" name="format" value="tsv" checked="true"/> <spring:message code="download.format.tab"/><br/>
	<input type="radio" name="format" value="csv"/> <spring:message code="download.format.csv"/><br/>
	<input type="radio" name="format" value="excel"/> <spring:message code="download.format.excel"/><br/>
</fieldset>

<h5 id="requiredFieldsTitle"><spring:message code="occurrence.record.required.fields" text="Required fields"/></h5>
<table id="mandatoryFields">
<tr>
<c:forEach items="${mandatoryFields}" var="field">
	<td>
		<input type="checkbox" name="${field.fieldName}" checked="true" <c:if test="${!param['ignoreMandatory']}">disabled="true"</c:if> /> <spring:message code="${field.fieldI18nNameKey}" text="${field.fieldI18nNameKey}"/>
	</td>
</c:forEach>
</tr>
</table>

<c:if test="${not empty param['maxDownloadOverride']}">
  <input name="maxDownloadOverride" type="hidden" value="${param['maxDownloadOverride']}"/>
</c:if>

<c:if test="${!param['ignoreMandatory']}">
<c:forEach items="${mandatoryFields}" var="field">
	<input type="checkbox" name="${field.fieldName}" checked="true" style="visibility:hidden;"/>
</c:forEach>
</c:if>

<h5 id="datasetFieldsTitle"><spring:message code="occurrence.record.dataset.legend"/></h5>
<gbiftag:selectAll fieldsetId="datasetFields"/>
<table id="datasetFields">
	<tr>
<c:forEach items="${datasetFields}" var="field" varStatus="fieldStatus">
	<c:if test="${fieldStatus.index mod 6==0 && fieldStatus.index!=0}">
		</tr><tr>
	</c:if>
	<td><input type="checkbox" name="${field.fieldName}" checked="true"/> <spring:message code="${field.fieldI18nNameKey}" text="${field.fieldI18nNameKey}"/></td>
</c:forEach>
	</tr>
</table>

<h5 id="taxonomyFieldsTitle"><spring:message code="occurrence.record.taxonomy.legend"/></h5>
<gbiftag:selectAll fieldsetId="taxonomyFields"/>
<table id="taxonomyFields">
<tr>
<c:forEach items="${taxonomyFields}" var="field" varStatus="fieldStatus">
	<td><input type="checkbox" name="${field.fieldName}" checked="true"/> <spring:message code="${field.fieldI18nNameKey}" text="${field.fieldI18nNameKey}"/></td>
</c:forEach>
</tr>
</table>

<h5 id="geospatialFieldsTitle"><spring:message code="occurrence.record.geospatial.legend"/></h5>
<gbiftag:selectAll fieldsetId="geospatialFields"/>
<table id="geospatialFields">
<tr>
<c:forEach items="${geospatialFields}" var="field" varStatus="fieldStatus">
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

</div><!--end download fields -->