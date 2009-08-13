<%@ include file="/common/taglibs.jsp"%>${taxonConcept.taxonName} <c:if test="${not empty taxonConcept.commonName}">(<gbif:capitalize>${taxonConcept.commonName}</gbif:capitalize>)</c:if>
<c:if test="${partnerConcept!=null && partnerResource!=null && partnerResource.taxonomicPriority <= taxonomicPriorityThreshold}">
- ${partnerConcept.dataProviderName}<c:if test="${not empty partnerResource.name}">: </c:if>${partnerResource.name}
</c:if>
