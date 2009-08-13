<%@ include file="/common/taglibs.jsp"%>
<div id="twopartheader">
	<h2><spring:message code="dataset.resource"/>: <span class="subject">${dataResource.name}</span></h2>
	<h3 style="font-size: 1.1em"><spring:message code="dataset.providedby"/> <a href="${pageContext.request.contextPath}/datasets/provider/${dataProvider.key}">${dataProvider.name}</a></h3>
</div>

<c:if test="${dataResource.logoUrl!=null && fn:startsWith(dataResource.logoUrl, 'http://')}">
	

	<c:if test="${dataResource.websiteUrl!=null}"><a href="${dataResource.websiteUrl}"></c:if>
	<gbiftag:scaleImage imageUrl="${dataResource.logoUrl}" maxWidth="200" maxHeight="70" addLink="false" imgClass="logo"/>	
	<c:if test="${dataResource.websiteUrl!=null}"></a></c:if>	
</c:if>

<tiles:insert page="actions.jsp"/>

<% //todo need to use the taxonomy provider field %>
<c:if test="${dataResource.basisOfRecord!='taxonomy' && dataResource.basisOfRecord!='nomenclator'}">
<div class="subcontainer">
	<h4><spring:message code="occurrence.overview"/></h4>
	<tiles:insert page="occurrences.jsp"/>
</div>
<div class="subcontainer">
	<tiles:insert page="indexing.jsp"/>	
</div>	
</c:if>
<div class="subcontainer">
	<h4><spring:message code="dataset.information"/></h4>
	<tiles:insert page="information.jsp"/>
</div>
<c:if test="${fn:length(agents) > 0}">
<div class="subcontainer">
	<tiles:insert page="../agents.jsp"/>
</div>	
<div class="subcontainer">
	<tiles:insert page="networks.jsp"/>
</div>	
</c:if>