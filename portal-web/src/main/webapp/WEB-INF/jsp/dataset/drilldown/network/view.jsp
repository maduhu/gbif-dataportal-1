<%@ include file="/common/taglibs.jsp"%>
<div id="twopartheader">
	<h2>
		<spring:message code="dataset.network"/>: <span class="subject">${resourceNetwork.name}</span> 
	</h2>
</div>

<c:if test="${resourceNetwork.websiteUrl!=null}"><a href="${resourceNetwork.websiteUrl}"></c:if>
<gbiftag:scaleImage imageUrl="${resourceNetwork.logoUrl}" maxWidth="200" maxHeight="70" addLink="false" imgClass="logo"/>	
<c:if test="${resourceNetwork.websiteUrl!=null}"></a></c:if>

<tiles:insert page="actions.jsp"/>
<div class="subcontainer">
	<h4><spring:message code="occurrence.overview"/></h4>
	<tiles:insert page="occurrences.jsp"/>
</div>	
<div class="subcontainer">
	<tiles:insert page="indexing.jsp"/>	
</div>	
<div class="subcontainer">
	<h4><spring:message code="dataset.information"/></h4>
	<tiles:insert page="information.jsp"/>
</div>
<c:if test="${not empty dataResources}">
	<div class="subcontainer">
		<h4><spring:message code="dataset.provider.resources"/></h4>
		<tiles:insert page="resources.jsp"/>
	</div>
</c:if>	