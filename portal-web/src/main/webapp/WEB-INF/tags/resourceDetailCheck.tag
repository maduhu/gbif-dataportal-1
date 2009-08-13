<%@ include file="/common/taglibs.jsp"%>
<%@ attribute name="readonlyProperties" required="true" rtexprvalue="true" type="java.util.List"%>
<%@ attribute name="updatedProperties" required="true" rtexprvalue="true" type="java.util.List"%>
<%@ attribute name="propertyName" required="true" rtexprvalue="true" type="java.lang.String"%>
<%@ attribute name="errorMessage" required="false" rtexprvalue="true" type="java.lang.String"%>
<c:if test="${fn:contains(readonlyProperties, propertyName)}">
	<span class="info"><spring:message code="registration.metdata.supplied" text="*"/></span>
</c:if>			
<c:if test="${fn:contains(updatedProperties, propertyName)}">
	<span class="info"><spring:message code="registration.metdata.updated" text="updated"/></span>
</c:if>	
<span class="error">${errorMessage}</span>		