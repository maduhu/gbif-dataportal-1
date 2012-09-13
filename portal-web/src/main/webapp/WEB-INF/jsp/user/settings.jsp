<%@ include file="/common/taglibs.jsp"%><tiles:insert page="/common/scripts.jsp"/>
<%@ page language="java" pageEncoding="utf-8" contentType="text/html;charset=utf-8" %>
<div id="twopartheader">	
	<h2><spring:message code="settings.title"/></h2>
</div>
<form method="post" action="${pageContext.request.contextPath}/settings.htm" style="margin-bottom:20px;">	
<spring:bind path="settings">
<h4><spring:message code="settings.lookandfeel"/></h4>
<p>
<input type="radio" name="theme" value="standard" <c:if test="${settings.theme=='standard'}">checked="true"</c:if>/><spring:message code="settings.theme.standard"/>
</p>
<p> 
<input type="radio" name="theme" value="800x600" <c:if test="${settings.theme=='800x600'}">checked="true"</c:if>/><spring:message code="settings.theme.800x600"/>
</p>

<h4><spring:message code="settings.language"/></h4>
<p>
<input type="radio" name="locale" value="en" <c:if test="${settings.locale=='en'}">checked="true"</c:if>/>English
</p>
<p> 
<input type="radio" name="locale" value="ko" <c:if test="${settings.locale=='ko'}">checked="true"</c:if>/>한국어 [韓國語] (han-guk-eo)
</p>
<p> 
<input type="radio" name="locale" value="es" <c:if test="${settings.locale=='es'}">checked="true"</c:if>/>Español
</p>
<p> 
<input type="radio" name="locale" value="pl" <c:if test="${settings.locale=='pl'}">checked="true"</c:if>/>Polski
</p>
<input type="radio" name="locale" value="jp" <c:if test="${settings.locale=='jp'}">checked="true"</c:if>/>日本語 
</p>

</spring:bind>
<br/>
<input type="submit" value="<spring:message code="settings.apply.changes"/>"/>
</form>