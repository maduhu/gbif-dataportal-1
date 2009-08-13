<%@ include file="/common/taglibs.jsp"%>
<div id="protectedAreaWizard" class="countryProtectedAreaWizard">

<span id="progress" style="float:right; margin-right:20px; visibility:hidden;">Loading protected areas...</span> 

<spring:message code="protectedarea.wizard.description"/>
<br/>
<br/>
<spring:message code="protectedarea.wizard.select.country"/>
<select id="countryId" 
	name="countryId" 
	onchange="javascript:populateWithAjax('<string:trim>${pageContext.request.contextPath}/geography/ajax/</string:trim>'+this.value, 'protectedAreaId');"
	onKeyUp="javascript:populateWithAjax('<string:trim>${pageContext.request.contextPath}/geography/ajax/</string:trim>'+this.value, 'protectedAreaId');">
<option selected="selected" disabled><spring:message code="actions.select"/></option>
<c:if test="${not empty countries}">
<optgroup label="<spring:message code="protectedarea.list.title"/>">
<c:forEach items="${countries}" var="kv"><option value="country/${kv.key}">${kv.value}</option>
</c:forEach>
</optgroup>
</c:if>
</select>					
<br/>
<spring:message code="protectedarea.wizard.select.georegion"/>
<select id="protectedAreaId" name="protectedAreaId" 
onchange="javascript:setWizardValues(0, this.value, this.options[this.selectedIndex].innerHTML);"></select>

<!--
<input type="submit" style="margin-bottom:20px;" name="setResource" value="<spring:message code="dataresource.wizard.submit.search"/>" onclick="javascript:setWizardValuesFromDropdown('dataResourceId');addConstructedFilter();"/>
-->			
</div>
