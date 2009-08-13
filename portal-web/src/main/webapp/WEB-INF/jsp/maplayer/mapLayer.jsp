<%@ include file="/common/taglibs.jsp"%>MINX	MINY	MAXX	MAXY	DENSITY
<c:forEach items="${densities}" var="density">${density.minLong}	${density.minLat}	${density.maxLong}	${density.maxLat}	${density.count}
</c:forEach>