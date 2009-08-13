<%@ include file="/common/taglibs.jsp"%>
<c:if test="${not empty networks}">
<h4><spring:message code="dataset.networks.list.title"/></h4>
<ul class="genericList">
<c:forEach items="${networks}" var="network">
	<li><a href="${pageContext.request.contextPath}/datasets/network/${network.key}">${network.name}</a></li>
</c:forEach>
</ul>
</c:if>