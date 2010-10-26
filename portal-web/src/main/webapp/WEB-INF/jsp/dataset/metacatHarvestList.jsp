<%@ page language="java" pageEncoding="utf-8" contentType="text/xml;charset=utf-8" %><%@ include file="/common/taglibs.jsp"%><?xml version="1.0" encoding="UTF-8"?>
<harvestList xmlns="eml://ecoinformatics.org/harvestList"
 xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
 xsi:schemaLocation="eml://ecoinformatics.org/harvestList ${pageContext.request.contextPath}/schema/harvestList.xsd">
 <c:forEach items="${dataResources}" var="dataResource">
 <document xmlns="">
   <docid>
    <scope>eml.gbif.package</scope>
    <identifier>${dataResource.key}</identifier>
    <!-- revision is not tracked - all data is derived from the current portal index -->
    <revision>1</revision>
   </docid>
   <documentType><![CDATA[eml://ecoinformatics.org/eml-2.1.0]]></documentType>
   <documentURL><![CDATA[http://${header.host}${pageContext.request.contextPath}/datasets/resource/${dataResource.key}?schema=eml]]></documentURL>
 </document>
</c:forEach>
</harvestList>