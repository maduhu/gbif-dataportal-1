<%@ include file="/common/taglibs.jsp"%>
<div id="twopartheader">	
	<h2><spring:message code="geography.list.main.title"/></h2>
		<c:set var="ignores"><spring:message code="country.alphabet.skips"/></c:set>
		<gbif:alphabetLink rootUrl="/countries/browse/" selected="${selectedChar}" listClass="flatlist" ignores="${ignores}" letters="${alphabet}"/>
</div>
<p>
<spring:message code="geography.list.iso.explaination"/>
</p>
<h2 id="selectedChar">${selectedChar}</h2>
<c:choose>
	<c:when test="${fn:length(alphabet)==0}">Currently no countries within the system.</c:when>
	<c:otherwise>
	<display:table name="countries" export="false" class="statistics" id="country">
	  <display:column titleKey="geography.drilldown.main.title" class="name">
		  <img src="${pageContext.request.contextPath}/images/flags/<string:lowerCase>${country.isoCountryCode}</string:lowerCase>.gif"/>&nbsp;<a href="${pageContext.request.contextPath}/countries/${country.isoCountryCode}"><gbif:capitalize><spring:message code="country.${country.isoCountryCode}"/></gbif:capitalize>
		  </a>
	  </display:column>	  
	  <display:column titleKey="dataset.list.occurrence.count" class="countrycount">
	  	<c:if test="${country.occurrenceCount>0}"><a href="${pageContext.request.contextPath}/occurrences/search.htm?<gbif:criterion subject="5" predicate="0" value="${country.isoCountryCode}" index="0"/>"></c:if><fmt:formatNumber value="${country.occurrenceCount}" pattern="###,###"/><c:if test="${country.occurrenceCount>0}"></a></c:if>
	  	(<c:if test="${country.occurrenceCoordinateCount>0}"><a href="${pageContext.request.contextPath}/occurrences/search.htm?<gbif:criterion subject="5" predicate="0" value="${country.isoCountryCode}" index="0"/>&<gbif:criterion subject="28" predicate="0" value="0" index="1"/>"></c:if><fmt:formatNumber value="${country.occurrenceCoordinateCount}" pattern="###,###"/><c:if test="${country.occurrenceCoordinateCount>0}"></a></c:if>)
	  </display:column>	  
	  <display:column titleKey="dataset.speciesCount" class="countrycount">
	  	<c:if test="${country.speciesCount>0}"><a href="${pageContext.request.contextPath}/occurrences/searchSpecies.htm?<gbif:criterion subject="5" predicate="0" value="${country.isoCountryCode}" index="0"/>"></c:if><fmt:formatNumber value="${country.speciesCount}" pattern="###,###"/><c:if test="${country.speciesCount>0}"></a></c:if>
  	  </display:column>
	  <display:setProperty name="basic.msg.empty_list"> </display:setProperty>	  
	  <display:setProperty name="basic.empty.showtable">false</display:setProperty>	  
	</display:table>
	</c:otherwise>
</c:choose>

<div id="countryLinks">
<p>
<ul class="genericList">
<li><a href="${pageContext.request.contextPath}/countries/datasharing"><spring:message code="repat.title"/></a></li>
</ul>
</p>	
</div>