<%@ page contentType="text/xml" %><%@ include file="/common/taglibs.jsp"%><?xml version="1.0" encoding="UTF-8"?>
<countries>
<c:forEach items="${searchResults}" var="country">	<country><gbif:capitalize>${country.name}</gbif:capitalize></country>
</c:forEach></countries>