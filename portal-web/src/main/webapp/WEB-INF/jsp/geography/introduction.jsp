<%@ include file="/common/taglibs.jsp"%>
<h2><a href="${pageContext.request.contextPath}/countries/"><spring:message code="geography.intro.heading"/></a></h2>
	<spring:message code="geography.intro.description"/>
<br/>
<h3><spring:message code="geography.intro.summary"/></h3>
<c:set var="a0"><span class="subject"><fmt:formatNumber value="${noOfCountries}" pattern="###,###"/></span></c:set>
<c:set var="a1"><span class="subject">0</span></c:set>
<c:set var="a2"><span class="subject">0</span></c:set>
<spring:message code="geography.intro.description1" arguments="${a0},${a1},${a2}"/>
<h3><spring:message code="geography.intro.seedatafor"/>: </h3>
<c:choose>
<c:when test="${not empty userCountry}">
<a href="${pageContext.request.contextPath}/countries/${userCountry.isoCountryCode}"><spring:message code="country.${userCountry.isoCountryCode}"/></a>
</c:when>
<c:otherwise>
<a href="${pageContext.request.contextPath}/countries/FR"><spring:message code="country.FR"/></a>
</c:otherwise>
</c:choose>