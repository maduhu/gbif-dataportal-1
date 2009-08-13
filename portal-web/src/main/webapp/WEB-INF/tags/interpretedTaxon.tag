<%@ include file="/common/taglibs.jsp"%>
<%@ attribute name="concept" required="true" rtexprvalue="true" type="org.gbif.portal.dto.taxonomy.BriefTaxonConceptDTO" %>
<c:if test="${not empty concept}">
<c:set var="scLink">
	<c:if test="${concept.partnerConceptKey!=null}"><a href="${pageContext.request.contextPath}/species/${concept.partnerConceptKey}"></c:if>${concept.taxonName}<c:if test="${concept.partnerConceptKey!=null}"></a></c:if>
</c:set>
<spring:message code="occurrence.record.interpreted.as" arguments="${scLink}"/>
</c:if>	