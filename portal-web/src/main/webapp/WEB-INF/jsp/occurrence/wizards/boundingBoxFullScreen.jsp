<%@ include file="/common/taglibs.jsp"%>
<link rel="stylesheet" href="${pageContext.request.contextPath}/<spring:theme code='filters.css'/>"/>
<link rel="stylesheet" href="${pageContext.request.contextPath}/<spring:theme code='tables.css'/>"/>
<link rel="stylesheet" href="${pageContext.request.contextPath}/<spring:theme code='googlemap.css'/>"/>
<link rel="stylesheet" href="${pageContext.request.contextPath}/<spring:theme code='gbifmap.css'/>"/>
<tiles:insert page="/common/scripts.jsp"/>
<c:set var="fullScreen" value="true" scope="request"/>

<script>
	var currValue = "${param['currValue']}";
	<c:if test="${param['currValue']!=null}">	
		selectedBoundingBox = "${param['currValue']}";
	</c:if>
	<c:if test="${param['newFilterSubject']!=null}">	
	var theSubjectSelect = document.getElementById('newFilterSubject');
	changeDropdownValues(theSubjectSelect,'p', predicates);
	changeObjectInput2(theSubjectSelect);
	</c:if>
</script>

<tiles:insert page="boundingbox.jsp"/>