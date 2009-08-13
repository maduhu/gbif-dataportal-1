<%@ page contentType="text/xml" %><%@ include file="/common/taglibs.jsp"%><?xml version="1.0" encoding="UTF-8"?>
<datasets>
<c:forEach items="${searchResults}" var="dataset"><dataset>
<id>${dataset.key}</id>
<name>${dataset.name}</name>
<url>http://${header.host}${pageContext.request.contextPath}/datasets/<gbiftag:printResourceType dataset="${dataset}"/>/${dataset.key}</url>
</dataset>
</c:forEach></datasets>