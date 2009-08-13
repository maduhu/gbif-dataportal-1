<%@ page contentType="text/xml" %><%@ include file="/common/taglibs.jsp"%><?xml version="1.0" encoding="UTF-8"?>
<countries>
<c:forEach items="${searchResults}" var="country"><country>
<id>${country.key}</id>
<name><gbif:capitalize>${country.name}</gbif:capitalize></name>
</country>
</c:forEach></countries>