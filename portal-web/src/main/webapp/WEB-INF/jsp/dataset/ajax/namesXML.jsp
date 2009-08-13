<%@ page contentType="text/xml" %><%@ include file="/common/taglibs.jsp"%><?xml version="1.0" encoding="UTF-8"?>
<datasets>
<c:forEach items="${searchResults}" var="name">	<dataset><gbif:capitalizeFirstChar>${name}</gbif:capitalizeFirstChar></dataset>
</c:forEach></datasets>