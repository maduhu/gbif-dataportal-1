<%@ page contentType="text/json" %><%@ include file="/common/taglibs.jsp"%><c:if test="${not empty callback}">${callback}(</c:if>{ "Resultset":{
  "totalResultsReturned":"${not empty points ? fn:length(points) : fn:length(cells) }",
  "Result":[
<c:choose>
<c:when test="${not empty cells}">
<c:forEach items="${cells}" var="cell" varStatus="cellStatus"><string:trim>
<c:if test="${cellStatus.index>0}">,</c:if>{
<gbiftag:cellPoints cellId="${cell.cellId}"/>
<c:choose>
<c:when test="${renderCells}">
    "minLongitude":"${minLongitude}",
    "minLatitude":"${minLatitude}",
    "maxLongitude":"${maxLongitude}",
    "maxLatitude":"${maxLatitude}",
    "count":"${cell.count}"
</c:when>
<c:otherwise>
    "latitude":"${(maxLongitude+minLongitude)/2}",
    "longitude":"${(minLatitude+maxLatitude)/2}",
    "count":"${cell.count}"
</c:otherwise>
</c:choose></string:trim>
}</c:forEach>
</c:when>
<c:otherwise>
<c:forEach items="${points}" var="point" varStatus="pointStatus">
<c:if test="${pointStatus.index>0}">,</c:if>{
    "latitude":"${point.latitude}",
    "longitude":"${point.longitude}",
    "portalUrl":"http://${header.host}${pageContext.request.contextPath}/occurrences/${point.key},"
    "wsUrl":"http://${header.host}${pageContext.request.contextPath}/occurrences/${point.key}"
}
</c:forEach>
</c:otherwise>
</c:choose>
  ]
}
}<c:if test="${not empty callback}">)</c:if>