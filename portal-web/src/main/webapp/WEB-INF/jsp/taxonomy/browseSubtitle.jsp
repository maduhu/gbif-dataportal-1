<%@ include file="/common/taglibs.jsp"%><spring:message code="taxonomy.browser.classification" text="Classification"/> 
<c:if test="${taxonConcept!=null}">
<spring:message code="of" text="of"/> <string:capitalize>${taxonConcept.rank}</string:capitalize>: ${taxonConcept.taxonName} ${taxonConcept.author} 
<c:if test="${!taxonConcept.isNubConcept}">
- ${taxonConcept.dataProviderName}: ${taxonConcept.dataResourceName}
</c:if>
</c:if>