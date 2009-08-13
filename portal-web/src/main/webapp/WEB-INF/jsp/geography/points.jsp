<%@ include file="/common/taglibs.jsp"%>
<script type="text/javascript">
	var points = new Array(${ fn:length(points) });
	<c:forEach items="${points}" var="point" varStatus="pointStatus" begin="0">
		<c:if test="${point.latitude!=null || point.longitude!=null}">
			points[${pointStatus.index}] = new Array(2);
			points[${pointStatus.index}][0] = ${point.latitude};
			points[${pointStatus.index}][1] = ${point.longitude};
			points[${pointStatus.index}][2] = ${point.key};
		</c:if>
		<c:if test="${point.latitude==null || point.longitude==null}">
			points[${pointStatus.index}] = null;
		</c:if>
	</c:forEach>
</script>