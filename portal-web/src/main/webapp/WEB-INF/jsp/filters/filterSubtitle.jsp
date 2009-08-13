<%@ include file="/common/taglibs.jsp"%>
<c:if test="${fn:length(criteria)>0}"> - </c:if> 
<tiles:insert page="filterQuery.jsp"/>
<c:if test="${not empty param['pageno']}"> - <spring:message code="page" text="Page"/> ${param['pageno']}</c:if>