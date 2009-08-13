<%@ include file="/common/taglibs.jsp"%>
<c:choose>
<c:when test="${not empty nameMatches}">
<p>
<spring:message code="commonnameresolve.text.linked" />
<spring:message code="commonnameresolve.text.select" />
</p>
<ul>
	<c:forEach items="${nameMatches}" var="taxonConcept" varStatus="status">
	<li>
		<a href="${pageContext.request.contextPath}/species/${taxonConcept.key}/commonName/<string:encodeUrl>${name}</string:encodeUrl>/">
			<gbif:taxonPrint concept="${taxonConcept}"/>
		</a>
		<p class="resultsDetails">
			<c:set var="taxonConcept" scope="request" value="${taxonConcept}"/>
			<gbiftag:taxonHierarchy concept="${taxonConcept}"/>
		</p>
	</li>	
	</c:forEach>
</ul>
</c:when>
<c:otherwise>
<div id="twopartheader">
	<h2><spring:message code="commonnameresolve.heading.nomatches"/> "<span class="subject">${name}</span>"</h2>
</div>	
<p>
<spring:message code="commonnameresolve.text.nomatches" /> "<span class="subject">${name}</span>"
</p>
<ul>
<li><a href="${pageContext.request.contextPath}/search/${name}"><spring:message code="nameresolve.text.nomatches.search"/> "<span class="subject">${name}</span>"</a></li>
</ul>
</c:otherwise>
</c:choose>