<%@ page contentType="text/plain" %><%@ include file="/common/taglibs.jsp"%><c:forEach items="${searchResults}" var="dataset">${dataset.name}
</c:forEach>