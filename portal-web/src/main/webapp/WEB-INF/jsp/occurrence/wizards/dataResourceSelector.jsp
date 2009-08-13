<%@ include file="/common/taglibs.jsp"%>
<div id="dataResourceWizard" class="datasetWizard">

<span id="progress" style="float:right; margin-right:20px; visibility:hidden;">Loading resources...</span> 

<spring:message code="dataresource.wizard.select.network.provider"/>
<select id="dataProviderId" 
	name="dataProviderId" 
	onchange="javascript:populateWithAjax('<string:trim>${pageContext.request.contextPath}/datasets/ajax/</string:trim>'+this.value, 'dataResourceId');"
	onKeyUp="javascript:populateWithAjax('<string:trim>${pageContext.request.contextPath}/datasets/ajax/</string:trim>'+this.value, 'dataResourceId');">
<option selected="selected" disabled><spring:message code="actions.select"/></option>
<c:if test="${not empty networks}">
<optgroup label="<spring:message code="dataset.networks.list.title"/>">
<c:forEach items="${networks}" var="kv"><option value="network/${kv.key}">${kv.value}</option>
</c:forEach>
</optgroup>
</c:if>
<c:if test="${not empty dataProviders}">
<optgroup label="<spring:message code="dataset.providers.list.title"/>">
<c:forEach items="${dataProviders}" var="kv"><option value="provider/${kv.key}">${kv.value}</option>
</c:forEach>
</optgroup>
</c:if>
</select>					
<br/>
<spring:message code="dataresource.wizard.select.dataset"/>
<select id="dataResourceId" name="dataResourceId" 
onchange="javascript:setWizardValues(0, this.value, this.options[this.selectedIndex].innerHTML);"></select>

<!--
<input type="submit" style="margin-bottom:20px;" name="setResource" value="<spring:message code="dataresource.wizard.submit.search"/>" onclick="javascript:setWizardValuesFromDropdown('dataResourceId');addConstructedFilter();"/>
-->			
</div>