<%@ page language="java" pageEncoding="utf-8" contentType="text/xml" %><%@ include file="/common/taglibs.jsp"%><?xml version="1.0" encoding="UTF-8"?>
<c:if test="${not empty param['stylesheet']}"><?xml-stylesheet type="text/xsl" href="${pageContext.request.contextPath}/stylesheet/${param['stylesheet']}"?></c:if><repatriation>
<c:forEach items="${countries}" var="country" varStatus="countryIndex">
  <country id="${country.isoCountryCode}" name="<spring:message code="country.${country.isoCountryCode}" text="${country.isoCountryCode}"/>" occurrenceCount="${not empty country.occurrenceCount ? country.occurrenceCount : 0}" occurrenceCoordinateCount="${not empty country.occurrenceCoordinateCount ? country.occurrenceCoordinateCount : 0}">
  <c:set var="tagArray" value="${stats[country.isoCountryCode]}"/>
  <c:forEach items="${tagArray}" var="stat" varStatus="statIndex">
    <host id="${hosts[statIndex.index]}" name="<spring:message code="country.${hosts[statIndex.index]}" text="${stat.fromEntityName}"/>" count="${not empty stat ? stat.count : 0}" percent="<fmt:formatNumber type="percent" value="${not empty stat ? stat.count/country.occurrenceCount : 0}"/>"/> </c:forEach>
  </country>
 </c:forEach>      
</repatriation>