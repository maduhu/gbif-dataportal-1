<%@ include file="/common/taglibs.jsp"%>
<c:choose>
<c:when test="${not empty param['host'] && not empty param['country']}">
  <c:if test="${param['country']!='XX' && param['host']!='XX'}">
	<ul class="genericList">
	  <li><a href="${pageContext.request.contextPath}/occurrences/searchWithTable.htm?<gbif:criterion subject="5" predicate="0" value="${country.isoCountryCode}"/>&<gbif:criterion subject="32" predicate="0" value="${host.isoCountryCode}" index="1"/>">View these records</a></li>
	  <li><a href="${pageContext.request.contextPath}/occurrences/downloadSpreadsheet.htm?<gbif:criterion subject="5" predicate="0" value="${country.isoCountryCode}"/>&<gbif:criterion subject="32" predicate="0" value="${host.isoCountryCode}" index="1"/>">Download a spreadsheet of these records</a></li>
	  <li><a href="${pageContext.request.contextPath}/countries/repatriation/viewHostCountry.htm?country=${param['country']}&host=${param['host']}" target="_blank">Printable view</a></li>  
	</ul>
	</c:if>
</c:when>
<c:when test="${not empty param['host'] && empty param['country']}">
  <c:if test="${param['host']!='XX' && param['country']!='XX'}">  
	  <ul class="genericList">
	    <li><a href="${pageContext.request.contextPath}/occurrences/searchWithTable.htm?<gbif:criterion subject="32" predicate="0" value="${host.isoCountryCode}"/>">View these records</a></li>
	    <li><a href="${pageContext.request.contextPath}/occurrences/downloadSpreadsheet.htm?<gbif:criterion subject="32" predicate="0" value="${host.isoCountryCode}"/>">Download a spreadsheet of these records</a></li>
		<li><a href="${pageContext.request.contextPath}/countries/repatriation/viewHost.htm?host=${host.isoCountryCode}" target="_blank">Printable view</a></li>      
	  </ul>
  </c:if>
</c:when>
<c:when test="${empty param['host'] && not empty param['country'] && not empty country}">
  <ul class="genericList">
    <li><a href="${pageContext.request.contextPath}/countries/${country.isoCountryCode}">An overview of data for <gbif:capitalize>${country.name}</gbif:capitalize></a></li>
    <li><a href="${pageContext.request.contextPath}/occurrences/searchWithTable.htm?<gbif:criterion subject="5" predicate="0" value="${country.isoCountryCode}"/>">View these records</a></li>
    <li><a href="${pageContext.request.contextPath}/occurrences/downloadSpreadsheet.htm?<gbif:criterion subject="5" predicate="0" value="${country.isoCountryCode}"/>">Download a spreadsheet of these records</a></li>  
    <li><a href="${pageContext.request.contextPath}/countries/repatriation/viewCountry.htm?country=${country.isoCountryCode}" target="_blank">Printable view</a></li>
  </ul>
</c:when>
</c:choose>