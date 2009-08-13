<%@ include file="/common/taglibs.jsp"%>
<fieldset>
<c:if test="${not empty resourceNetwork.name}"><p><label><spring:message code="name"/>:</label>${resourceNetwork.name}</p></c:if>
<c:if test="${not empty resourceNetwork.websiteUrl}"><p><label><spring:message code="website"/>:</label><a href="${resourceNetwork.websiteUrl}">${resourceNetwork.websiteUrl}</a></p></c:if>
<c:if test="${not empty resourceNetwork.description}"><p><label><spring:message code="description"/>:</label>${resourceNetwork.description}</p></c:if>
<c:if test="${not empty resourceNetwork.address}"><p><label><spring:message code="address"/>:</label><p>${resourceNetwork.address}</p></c:if>
<c:if test="${not empty resourceNetwork.email}"><p><label><spring:message code="email"/>:</label>${resourceNetwork.email}</p></c:if>
<c:if test="${not empty resourceNetwork.telephone}"><p><label><spring:message code="telephone"/>:</label>${resourceNetwork.telephone}</p></c:if>
<c:if test="${not empty resourceNetwork.created}"><p><label><spring:message code="date.added"/>:</label><fmt:formatDate value="${resourceNetwork.created}"/></p></c:if>	
<c:if test="${not empty resourceNetwork.modified}"><p><label><spring:message code="last.modified"/>:</label><fmt:formatDate value="${resourceNetwork.modified}"/></p></c:if>	
</fieldset>