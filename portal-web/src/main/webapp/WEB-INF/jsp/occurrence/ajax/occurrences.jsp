<%@ page contentType="text/plain" %><%@ include file="/common/taglibs.jsp"%><string:trim>
<c:choose>
<c:when test="${not empty cells}">
<c:forEach items="${cells}" var="cell"><string:trim>
<gbiftag:cellPoints cellId="${cell.cellId}"/>
<c:choose>
<c:when test="${renderCells}">
${minLongitude} ${minLatitude} ${maxLongitude} ${maxLatitude} ${cell.count}
</c:when>
<c:otherwise>
${(maxLongitude+minLongitude)/2} ${(minLatitude+maxLatitude)/2} ${cell.count}
</c:otherwise>
</c:choose></string:trim>
</c:forEach>
</c:when>
<c:otherwise>
<c:forEach items="${points}" var="point">${point.latitude} ${point.longitude}
</c:forEach>
</c:otherwise>
</c:choose>
</string:trim>