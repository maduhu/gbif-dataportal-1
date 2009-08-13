<%@ include file="/common/taglibs.jsp"%>
<div class="adminConsoleContainer">
	<div id="providerContainer">
		<h1><spring:message code="admin.provider.list"/></h1>
		<p>
			<spring:message code="admin.provider.selectprovider"/>
		</p>
		<ul>
			<c:forEach items="${providerList}" var="provider">
				<li>
					<a href="${pageContext.request.contextPath}/admin/provider/metadata.htm?businessKey=${provider.key}">${provider.value}</a>
				</li>
			</c:forEach>
		</ul>
	</div>
</div>