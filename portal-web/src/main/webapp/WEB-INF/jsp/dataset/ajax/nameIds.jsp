<%@ page contentType="text/plain" %><%@ include file="/common/taglibs.jsp"%><c:forEach items="${searchResults}" var="dataset">${dataset.key}	${dataset.name}
</c:forEach>