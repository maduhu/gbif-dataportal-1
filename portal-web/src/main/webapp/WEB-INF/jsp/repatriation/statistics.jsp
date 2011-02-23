<%@ include file="/common/taglibs.jsp"%>
<c:choose>
  <c:when test="${not empty param['host'] && param['host']=='XX'}">
    <c:set var="hostName" scope="request"><spring:message code="repat.abbr.intl.networks"/></c:set>
  </c:when>
  <c:when test="${not empty param['host'] && param['host']!='XX'}">
    <c:set var="hostName" scope="request"><gbif:capitalize>${host.name}</gbif:capitalize></c:set>
  </c:when>
</c:choose> 

<c:choose>
<c:when test="${not empty param['host'] && not empty param['country']}">
  <h4>Data hosted by ${hostName} for <c:if test="${not empty country}"><gbif:capitalize>${country.name}</gbif:capitalize></c:if></h4>	
  <c:if test="${empty country}">
    <p>
      <fmt:formatNumber pattern="###,###">${hostedCount}</fmt:formatNumber>
      occurrence records hosted by ${hostName}
      did not include enough information to ascertain<br/>
      their country of origin.
    </p>
  </c:if>
  <c:if test="${not empty country}">
	<p>
		<fmt:formatNumber pattern="###,###">${hostedCount}</fmt:formatNumber> occurrence records for 
		<gbif:capitalize>${country.name}</gbif:capitalize> are hosted by ${hostName}.
	</p>
	<p>
		This accounts for
		<gbiftag:lessThanPercent lowerLimit="0.01" value="${hostedCount/country.occurrenceCount}" text="less than 0.01%"/> 
		of the occurrence records 
		available for <gbif:capitalize>${country.name}</gbif:capitalize> 
		within the GBIF Network.
	</p>
  </c:if>
</c:when>

<c:when test="${not empty param['host'] && empty param['country']}">
  <h4>Data hosted by ${hostName}</h4>
  <p> 
    <span class="subject"><fmt:formatNumber pattern="###,###">${totalHosted}</fmt:formatNumber></span> 
    occurrence records are hosted by ${hostName}.
  </p>
  <p>
    <c:choose>
      <c:when test="${param['host']!='XX' && totalHostedForOthers == 0 && totalHosted > 0}">
        All of these records originate from ${hostName}
      </c:when>
      <c:when test="${param['host']!='XX' && totalHostedForOthers > 0 && totalHosted > 0}">
        ${hostName} hosts <fmt:formatNumber pattern="###,###">${totalHostedForOthers}</fmt:formatNumber> records for
        ${noOfOtherCountries} other countries & territories.
        
        This accounts for
        <gbiftag:lessThanPercent lowerLimit="0.01" value="${totalHostedForOthers/totalHosted}" text="less than 0.01%"/>
        of the total data hosted by ${hostName}.
      </c:when> 
    </c:choose> 
  </p>
  <p>
    <c:if test="${originUnknown>0}">
        <fmt:formatNumber pattern="###,###">${originUnknown}</fmt:formatNumber>
        records 
        (<gbiftag:lessThanPercent lowerLimit="0.01" value="${originUnknown/totalHosted}" text="less than 0.01%"/>) 
        did not include enough information to ascertain their origin.  
    </c:if>
  </p>
  <c:if test="${totalHosted > 0}">
  <p>
    The data hosted by ${hostName} accounts for 
    <gbiftag:lessThanPercent lowerLimit="0.01" value="${totalHosted/networkTotal}" text="less than 0.01%"/> 
    of the data within the GBIF network.  
  </p>
  </c:if>
</c:when>

<c:when test="${empty param['host'] && not empty param['country'] && not empty country}">
  <h4>Data for <gbif:capitalize>${country.name}</gbif:capitalize></h4>
  <p> 
    The GBIF Network contains <fmt:formatNumber pattern="###,###">${country.occurrenceCount}</fmt:formatNumber> 
    occurrence records for <gbif:capitalize>${country.name}</gbif:capitalize> of which 
    <fmt:formatNumber pattern="###,###">${country.occurrenceCoordinateCount}</fmt:formatNumber> are georeferenced.
  </p>
  <p>
    <c:choose>
      <c:when test="${totalHostedByOthers==country.occurrenceCount}">
        All of these records are served from other ${noOfOtherCountries} countries.
       </c:when>
       <c:when test="${totalHostedByOthers==0}">
        All of these records are served from <gbif:capitalize>${country.name}</gbif:capitalize>.
       </c:when>
       <c:otherwise>
        Of these records, <fmt:formatNumber pattern="###,###">${totalHostedByOthers}</fmt:formatNumber> records 
        (<gbiftag:lessThanPercent lowerLimit="0.01" value="${totalHostedByOthers/country.occurrenceCount}" text="less than 0.01%"/>)
        are hosted by another country.     
        <br/>
        ${noOfOtherCountries} countries, other than <gbif:capitalize>${country.name}</gbif:capitalize> provide data originating from <gbif:capitalize>${country.name}</gbif:capitalize>.
       </c:otherwise> 
    </c:choose>    
  </p>
  <c:if test="${hostNotTied>0}">  
    <p>
       <fmt:formatNumber pattern="###,###">${hostNotTied}</fmt:formatNumber>
       records are provided through networks or publishers associated with multiple countries & territories.<br/>
       This accounts for 
       <gbiftag:lessThanPercent lowerLimit="0.01" value="${hostNotTied/country.occurrenceCount}" text="less than 0.01%"/>
       of the data for <gbif:capitalize>${country.name}</gbif:capitalize>. 
    </p>
  </c:if>
  <p>
    The data hosted for <gbif:capitalize>${country.name}</gbif:capitalize> 
    accounts for
    <gbiftag:lessThanPercent lowerLimit="0.01" value="${country.occurrenceCount/networkTotal}" text="less than 0.01%"/> 
    of the data within the GBIF network.  
  </p>
</c:when>
<c:when test="${empty param['host'] && not empty param['country'] && empty country}">
  <p> 
    The GBIF Network contains <fmt:formatNumber pattern="###,###">${totalHosted}</fmt:formatNumber> 
    occurrence records which do not provide enough information to determine their country of origin.
  </p>
  <p>
    These records are provided by ${noOfOtherCountries} countries and account for 
    <gbiftag:lessThanPercent lowerLimit="0.01" value="${totalHosted/networkTotal}" text="less than 0.01%"/>
    of the records within the GBIF Network.
  </p>
</c:when>
</c:choose>