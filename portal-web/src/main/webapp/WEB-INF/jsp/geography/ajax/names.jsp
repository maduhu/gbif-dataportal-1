<%@ page contentType="text/plain" %><%@ include file="/common/taglibs.jsp"%><c:forEach items="${searchResults}" var="country"><gbif:capitalize>${country.name}</gbif:capitalize>
</c:forEach>