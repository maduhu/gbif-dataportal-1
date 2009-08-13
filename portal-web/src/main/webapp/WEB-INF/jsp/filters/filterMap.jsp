<%@ include file="/common/taglibs.jsp"%>
<h4><spring:message code="occurrence.search.filter.resultsmap.title"/></h4>
<c:set var="entityName" scope="request"><spring:message code="occurrence.search.filter.resultsmap.this.search"/></c:set>
<c:set var="extraParams" scope="request"><gbif:criteria criteria="${criteria}"/></c:set>
<tiles:insert name="overviewMap"/>
<ul class="overviewMapLinks">
	<c:if test="${noGeospatialIssues}">
	<li>
		<spring:message code="maps.records.with.no.geospatial.issues"/>
	</li>
	</c:if>
	<c:if test="${pointsTotal>0}">
	<li>
		<c:set var="a0"><span class="subject"><fmt:formatNumber value="${pointsTotal}" pattern="###,###"/></span></c:set>	
		<c:choose>
			<c:when test="${pointsTotal==1}">
				<spring:message code="maps.points.total.one"/>
			</c:when>
			<c:otherwise>
				<spring:message code="maps.points.total" arguments="${a0}" argumentSeparator="%%%"/>
			</c:otherwise>
		</c:choose>
		<c:if test="${not empty viewablePoints}">
		<c:set var="a1"><span class="subject"><fmt:formatNumber value="${viewablePoints}" pattern="###,###"/></span></c:set>	
		<c:choose>
			<c:when test="${viewablePoints==0 && pointsTotal==1}"><spring:message code="maps.points.total.non.viewable.one"/></c:when>
			<c:when test="${viewablePoints==0}"><spring:message code="maps.points.total.non.viewable"/></c:when>			
			<c:when test="${viewablePoints==1}"><spring:message code="maps.points.viewed.area.one"/></c:when>
			<c:when test="${viewablePoints==pointsTotal}"><spring:message code="maps.points.total.all.viewable"/></c:when>					
			<c:otherwise><spring:message code="maps.points.viewed.area" arguments="${a1}" argumentSeparator="%%%"/></c:otherwise>
		</c:choose>
		</c:if>
	</li>
	</c:if>
	
	<c:if test="${minMapLat>-90 || maxMapLong <180 || minMapLong >-180 || maxMapLat<90}">
	<c:set var="bb"><gbiftag:boundingBox/></c:set>
  <li>
    <a href="${pageContext.request.contextPath}/occurrences/boundingBoxWithCriteria.htm?<gbif:criteria criteria='${criteria}'/>&minX=${minMapLong}&minY=${minMapLat}&maxX=${maxMapLong}&maxY=${maxMapLat}"><spring:message code="occurrence.record.geospatial.viewall.viewarea" arguments="${bb}" argumentSeparator="%%%"/></a>
  </li>
  </c:if>
</ul>