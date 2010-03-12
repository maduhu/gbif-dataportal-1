<%@ page language="java" pageEncoding="utf-8" contentType="text/html;charset=utf-8" %>
<%@ include file="/common/taglibs.jsp"%>
<c:choose>
<c:when test="${empty kvps}"><option readonly="true"><spring:message code="dataresource.wizard.no.resources"/></option></c:when>
<c:otherwise><option disabled="disabled" selected="true"><spring:message code="dataresource.wizard.select.resource"/></option></c:otherwise>
</c:choose>
<c:forEach items="${kvps}" var="kv"><option value="${kv.key}"><gbif:formatDropDownList content="${kv.value}" end="..." maxLength="103"/></option>
</c:forEach>