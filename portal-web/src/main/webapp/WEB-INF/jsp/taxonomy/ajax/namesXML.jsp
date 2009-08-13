<%@ page contentType="text/xml" %><%@ include file="/common/taglibs.jsp"%><?xml version="1.0" encoding="UTF-8"?>
<names>
<c:forEach items="${searchResults}" var="name">	<name><gbif:capitalizeFirstChar>${name}</gbif:capitalizeFirstChar></name>
</c:forEach></names>