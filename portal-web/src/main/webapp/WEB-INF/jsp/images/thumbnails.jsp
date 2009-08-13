<%@ include file="/common/taglibs.jsp"%>
<c:if test="${not empty searchResults}">
	<c:forEach items="${searchResults}" var="image">
		<img src="${image.thumbnailUrl}"/>
	</c:forEach>
</c:if>