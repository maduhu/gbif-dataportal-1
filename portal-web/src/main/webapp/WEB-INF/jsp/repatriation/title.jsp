<%@ include file="/common/taglibs.jsp"%>
<div id="twopartheader">
  <h2><spring:message code="repat.title"/>
    <c:if test="${not empty param['host'] && param['host']!='all'}">- Data hosted by <spring:message code="country.${param['host']}"/></c:if>
    <c:if test="${not empty param['country'] && param['country']!='all'}">- Data for <spring:message code="country.${param['country']}"/></c:if>
  </h2>
  <h3><spring:message code="repat.subtitle"/></h3>
</div>