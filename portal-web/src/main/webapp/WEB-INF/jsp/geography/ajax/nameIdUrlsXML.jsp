<%@ page contentType="text/xml" %><%@ include file="/common/taglibs.jsp"%><?xml version="1.0" encoding="UTF-8"?>
<taxa>
<c:forEach items="${searchResults}" var="country"><taxon>
<id>${country.key}</id>
<name><gbif:capitalize>${country.name}</gbif:capitalize></name>
<url>http://${header.host}${pageContext.request.contextPath}/country/${country.key}</url>
</taxon>
</c:forEach></taxa>