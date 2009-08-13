<%@ include file="/common/taglibs.jsp"%><?xml version="1.0" encoding="UTF-8"?>
<?xml-stylesheet type="text/xsl" href="${pageContext.request.contextPath}/stylesheet/test.xsl" ?>
<string:trim>
<c:choose>
<c:when test="${rawMessage==null}">
<spring:message code="occurrence.record.dynamic.failure"/><br/>
<a href="javascript:history.go(-1);">Back</a>
</c:when>
<c:otherwise>${rawMessage}</c:otherwise>
</c:choose>
</string:trim>