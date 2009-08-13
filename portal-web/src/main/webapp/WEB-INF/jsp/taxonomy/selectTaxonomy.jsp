<%@ include file="/common/taglibs.jsp"%>
<div id="selectTaxonomy" class="taxonomytreeinfopane">
	<form method="get" action="${pageContext.request.contextPath}/species/taxonomyFinder.htm">	
		<spring:message code="taxonomy.browser.select"/>    
		<select id="taxonomy" name="taxonomy" onchange="javascript:form.action='${pageContext.request.contextPath}/species/taxonomyFinder.htm';form.submit()">
		
			<c:if test="${not empty dataProviders && not empty dataResources}"><optgroup label="<spring:message code="taxonomy.browser.select.sharedtaxonomies"/>"></c:if>
		
            <c:forEach items="${requestScope['dataProviders']}" var="theDataProvider">
                <option value="provider/${theDataProvider.key}"  <c:if test="${dataProvider!=null && dataProvider.key == theDataProvider.key}">selected=true</c:if>>
					<string:truncateNicely lower="80" upper="100">
						<string:trim>${theDataProvider.name}</string:trim>
					</string:truncateNicely>
                </option>
            </c:forEach>
		
			<c:if test="${not empty dataProviders && not empty dataResources}"></optgroup><optgroup label="<spring:message code="taxonomy.browser.select.inferredtaxonomies"/>"></c:if>
		
            <c:forEach items="${requestScope['dataResources']}" var="theDataResource">
                <option value="resource/${theDataResource.key}"  title="${theDataResource.name} - ${theDataResource.dataProviderName}" <c:if test="${dataResource!=null && dataResource.key == theDataResource.key}">selected=true</c:if>>
					<string:truncateNicely lower="80" upper="100">
						<string:trim>${theDataResource.name} - ${theDataResource.dataProviderName}</string:trim>
					</string:truncateNicely>
                </option>
            </c:forEach>

			<c:if test="${not empty dataProviders && not empty dataResources}"></optgroup></c:if>
        </select>
    </form>
</div>