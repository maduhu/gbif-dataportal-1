<%@ include file="/common/taglibs.jsp"%>
<div class="datasetWizard" style="margin-top:10px;">
<p>
	Please select the search restrictions you which to add and click "Add Filter"
</p>

<fieldset>
<input id="georeferencedCheckBox" type="checkbox" name="georeferenced" value="true"/> &nbsp;<spring:message code="occurrence.search.filter.option.show.georeferenced"/><br/>
<input id="geospatialIssuesCheckBox" type="checkbox" name="geospatialIssues"  value="true"/> &nbsp;<spring:message code="occurrence.search.filter.option.hide.geospatial.issues"/><br/>
<input id="taxonomyIssuesCheckBox" type="checkbox" name="taxonomyIssues"  value="true"/> &nbsp;<spring:message code="occurrence.search.filter.option.hide.taxonomy.issues"/><br/>
</fieldset>

<input type="submit" name="setRestrictions" value="Set restrictions" onclick="javascript:setRestrictions();addConstructedFilter();"/>
</div>