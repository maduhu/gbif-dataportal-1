<%@ include file="/common/taglibs.jsp"%>
<h2><a href="${pageContext.request.contextPath}/datasets/"><spring:message code="dataset.intro.heading"/></a></h2>
<spring:message code="dataset.intro.description"/>
<br/>
<h3><spring:message code="dataset.intro.summary"/></h3>
<c:set var="a0"><span class="subject">${dataResourceCount}</span></c:set>
<c:set var="a1"><span class="subject">${dataProviderCount}</span></c:set>
<spring:message code="dataset.intro.description.counts" arguments="${a0},${a1}"/>
<c:if test="${latestResource!=null}">
<h3><spring:message code="dataset.intro.latestresourceadded"/>: </h3>
<a href="${pageContext.request.contextPath}/datasets/resource/${latestResource.key}/">${latestResource.name}</a>
</c:if>