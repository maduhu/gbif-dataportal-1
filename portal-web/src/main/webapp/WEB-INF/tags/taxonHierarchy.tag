<%@ include file="/common/taglibs.jsp"%>
<%@ attribute name="concept" required="true" rtexprvalue="true" type="org.gbif.portal.dto.taxonomy.TaxonConceptDTO" %>
<c:if test="${not empty concept.kingdom || not empty concept.phylumDivision || not empty concept.klass || not empty concept.order || not empty concept.family || not empty concept.genus}">
		${concept.kingdom} 
		<c:if test="${not empty concept.phylumDivision && not empty concept.kingdom}"> - </c:if>${concept.phylumDivision} 
		<c:if test="${not empty concept.klass && (not empty concept.phylumDivision || not empty concept.kingdom)}"> - </c:if>${concept.klass}
		<c:if test="${not empty concept.order && (not empty concept.klass || not empty concept.phylumDivision || not empty concept.kingdom)}"> - </c:if>${concept.order}
		<c:if test="${not empty concept.family && (not empty concept.order || not empty concept.klass || not empty concept.phylumDivision || not empty concept.kingdom)}"> - </c:if>${concept.family}
		<c:if test="${not empty concept.genus && (not empty concept.family || not empty concept.order || not empty concept.klass || not empty concept.phylumDivision || not empty concept.kingdom)}"> - </c:if><span class="genera">${concept.genus}</span>
</c:if>