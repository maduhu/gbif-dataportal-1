<%@ page contentType="text/plain" %><%@ include file="/common/taglibs.jsp"%><c:forEach items="${searchResults}" var="commonName">${commonName.name}	${commonName.language}
</c:forEach>