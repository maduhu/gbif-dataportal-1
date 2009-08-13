<%@ include file="/common/taglibs.jsp"%><c:forEach items="${searchResults}" var="taxonConcept">${taxonConcept.taxonName}
</c:forEach>