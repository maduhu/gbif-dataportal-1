<%@ page language="java" pageEncoding="utf-8" contentType="text/html;charset=utf-8" %><%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
	<head>
		<tiles:insert name="headcontent" flush="true"/>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
		<title>
			<tiles:insert name="subtitle"/>
				- 			
			<c:set var="title" scope="page"><tiles:getAsString name="title"/></c:set>
			<spring:message code="${title}"/> 
		</title>
		<style>
			body {
				margin: 0px;
				padding: 0px;
				font: Lucida Grande, Arial, Helvetica, sans-serif;
			}
			#content { margin-top:20px; }
		</style>
	</head>
	<body>
		<img src="${pageContext.request.contextPath}/skins/standard/images/speciesHeaderBG.jpg"/>
		<div id="content">
			<tiles:insert name="content"/>
		</div>
	</body>
</html>
