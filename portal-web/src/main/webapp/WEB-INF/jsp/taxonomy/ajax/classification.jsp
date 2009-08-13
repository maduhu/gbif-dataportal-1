<%@ page contentType="text/plain" %><%@ include file="/common/taglibs.jsp"%><c:forEach items="${concepts}" var="concept">${concept.taxonName}
</c:forEach>