<%@ include file="/common/taglibs.jsp"%>
<points 
	firstKey="${points.firstKey}"
	lastKey="${points.lastKey}"
	nextKey="${points.nextKey}"
	hasMore="${points.hasMoreResults}" 
	>
<c:forEach items="${points}" var="point" varStatus="status">
	<point>
		<key>${point.key}</key>
		<latitude>${point.latitude}</latitude>
		<longitude>${point.longitude}</longitude>
	</point>
</c:forEach>
</points>