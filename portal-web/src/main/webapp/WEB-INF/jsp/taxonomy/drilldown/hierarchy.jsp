<%@ include file="/common/taglibs.jsp"%>
<% //only display the flat tree if there is a parent concept, otherwise it is providing no real information %>
<c:if test="${taxonConcept.parentConceptKey!=null}">
<div id="classificationTop">
	<gbif:flattree classname="classificationCondensed" concepts="${concepts}" selectedConcept="${taxonConcept}" messageSource="${messageSource}"/>
</div><br/>
</c:if>
