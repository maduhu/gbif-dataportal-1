<%@ include file="/common/taglibs.jsp"%>
<%
	java.util.List<org.gbif.portal.dto.KeyValueDTO> sessionHistory = (java.util.List<org.gbif.portal.dto.KeyValueDTO>) request.getSession().getAttribute("sessionHistory");
	pageContext.setAttribute("sessionHistory", sessionHistory);
%>

<c:if test="${not empty sessionHistory && !(fn:length(sessionHistory)==1 && sessionHistory[0].value==title)}">
	<c:forEach items="${sessionHistory}" var="historyItem">
		<c:if test="${title ne historyItem.value}">
			<c:set var="showRecentlyViewed" value="true"/>
		</c:if>	
	</c:forEach>
	
<c:if test="${showRecentlyViewed}">

<c:forEach items="${sessionHistory}" var="historyItem" begin="0" end="9">
	<c:if test="${title ne historyItem.value && !fn:endsWith(historyItem.key, '/welcome.htm') }">

		<c:if test="${headerAdded==null || !headerAdded}">
			<c:set var="headerAdded" value="true"/>
			<div id="breadcrumbs">
				<h6 id="recentlyViewed"><spring:message code="breadcrumbs.title" text="Recently viewed"/></h6>
				<ul class="breadcrumblist">			
		</c:if>
		<li><a href="${historyItem.key}" title="${historyItem.value}"><string:truncateNicely lower="120" upper="140">${historyItem.value}</string:truncateNicely></a></li>
	</c:if>	
</c:forEach>

<c:if test="${headerAdded}">
		</ul>
	</div><!-- end of breadcrumbs -->
</c:if>	

</c:if>
</c:if>