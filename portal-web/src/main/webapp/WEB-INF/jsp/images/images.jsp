<%@ include file="/common/taglibs.jsp"%>
<c:if test="${not empty searchResults}">
	<c:forEach items="${searchResults}" var="image">
		<img src="${image.url}" width="200px"/>
	</c:forEach>
</c:if>