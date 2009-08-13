<%@ include file="/common/taglibs.jsp"%>
<ul id="resources" class="genericList">
	<c:if test="${fn:length(dataResources) > 0}">
			<c:forEach items="${dataResources}" var="dataResource" varStatus="status">
				<li>
					<a href="${pageContext.request.contextPath}/datasets/resource/${dataResource.key}/">${dataResource.name}</a>
					<p class="resultsDetails">${dataResource.dataProviderName}</p>
				</li>
			</c:forEach>
	</c:if>
</ul>