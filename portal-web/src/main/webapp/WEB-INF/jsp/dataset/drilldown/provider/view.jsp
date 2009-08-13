<%@ include file="/common/taglibs.jsp"%>
<div id="twopartheader">
	<h2>
			<spring:message code="dataset.provider"/>: <span class="subject">${dataProvider.name}</span> 
			<c:if test="${dataProvider.logoUrl!=null}">
				<c:if test="${dataProvider.websiteUrl!=null}"><a href="${dataProvider.websiteUrl}"></c:if>
				<gbiftag:scaleImage imageUrl="${dataProvider.logoUrl}" maxWidth="200" maxHeight="80" imgClass="logo" addLink="false"/>
				<c:if test="${dataProvider.websiteUrl!=null}"></a></c:if>
			</c:if>
	</h2>
</div>

<tiles:insert page="actions.jsp"/>
<c:if test="${!isTaxonomicProvider}">
<div class="subcontainer">
	<h4><spring:message code="occurrence.overview"/></h4>
	<tiles:insert page="occurrences.jsp"/>
</div>	
<div class="subcontainer">
	<tiles:insert page="indexing.jsp"/>	
</div>	
</c:if>
<div class="subcontainer">
	<tiles:insert page="information.jsp"/>
</div>
<% //todo need to use the taxonomy provider field %>
<c:if test="${fn:length(dataResources) > 0}">
<div class="subcontainer">
	<tiles:insert page="resources.jsp"/>
</div>
</c:if>
<c:if test="${fn:length(agents) > 0}">
<div class="subcontainer">
	<tiles:insert page="../agents.jsp"/>
</div>	
</c:if>