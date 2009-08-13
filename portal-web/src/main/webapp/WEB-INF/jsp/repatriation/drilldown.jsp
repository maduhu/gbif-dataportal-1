<%@ include file="/common/taglibs.jsp"%>
<h2 class="printFriendly">
  <c:if test="${not empty param['host'] && param['host']!='all'}">Data hosted by <spring:message code="country.${param['host']}"/></c:if>
  <c:if test="${not empty param['country'] && param['country']!='all'}">Data for <spring:message code="country.${param['country']}"/></c:if>
</h2>
<div id="overviewMap">
<c:choose>
<c:when test="${not empty param['host'] && not empty param['country']}">
<c:set var="mapServerUrl" value="http://maps.gbif.org/mapserver/draw.pl?dtype=box&imgonly=1&mode=browse&refresh=Refresh&layer=countryborders&layer=countrylabel&path="/>
<c:set var="mapLayerUrl">http://${header.host}${pageContext.request.contextPath}/maplayers/homeCountry/</c:set>
<img src="${mapServerUrl}${mapLayerUrl}${param['host']}/${param['country']}.txt"/>
</c:when>
<c:when test="${empty param['host'] && not empty param['country']}">
<img src="${pageContext.request.contextPath}/countries/${param['country']}/overviewMap.png"/>
</c:when>
<c:when test="${not empty param['host'] && empty param['country']}">
<img src="${pageContext.request.contextPath}/countries/hosted/${param['host']}/overviewMap.png"/>
</c:when>
</c:choose>
</div>

<div id="statistics">
	<tiles:insert page="statistics.jsp"/>
</div><!-- statistics -->

<div id="breakdown">
	<tiles:insert page="breakdown.jsp"/>
</div><!-- breakdown -->