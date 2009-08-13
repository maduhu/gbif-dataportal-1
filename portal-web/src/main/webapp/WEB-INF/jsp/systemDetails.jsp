<%@ include file="/common/taglibs.jsp"%>
<div id="twopartheader">
	<h2>System Details</h2>
	<h3>System Properties</h3>
</div>	
<fieldset>
  <c:set var="devMode"><gbif:propertyLoader bundle="portal" property="portal.dev.mode"/></c:set>
  <c:if test="${devMode}">
      <label style="width:300px;">Live DB Url</label>&nbsp;<gbif:propertyLoader bundle="portal" property="dataSource.url"/><br/>
      <label style="width:300px;">Harvesting DB Url</label>&nbsp;<gbif:propertyLoader bundle="portal" property="harvestingDataSource.url"/><br/>
      <label style="width:300px;">Logging DB Url</label>&nbsp;<gbif:propertyLoader bundle="portal" property="logDataSource.url"/><br/>
  </c:if>    
	<c:forEach items="${systemProperties}" var="kv">
		<label style="width:300px;">${kv.key}</label>&nbsp;${kv.value}<br/>
	</c:forEach>
</fieldset>

<h4>Actions</h4>
<ul class="genericList">
<li><a href="${pageContext.request.contextPath}/clearCache.htm">Clear Caches Now</a> <c:if test="${cacheCleared}"> Cache Cleared</c:if></li>
<li><a href="${pageContext.request.contextPath}/download/monitor.htm">View download activity</a>
</ul>