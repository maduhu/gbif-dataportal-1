<%@ include file="/common/taglibs.jsp"%>
<div class="adminConsoleContainer">
	<div id="providerContainer">
		<h1><spring:message code="admin.provider.resource.list"/></h1>
		<p>
			<spring:message code="admin.provider.resource.current.resources" arguments="<b>${providerDetail.businessName}</b>"/>
		</p>
		<ul>
			<c:forEach items="${providerDetail.businessResources}" var="resource">
				<li>
					<a href="${pageContext.request.contextPath}/admin/provider/metadata.htm">${resource.name}</a>
				</li>
			</c:forEach>
		</ul>
	</div>
</div>