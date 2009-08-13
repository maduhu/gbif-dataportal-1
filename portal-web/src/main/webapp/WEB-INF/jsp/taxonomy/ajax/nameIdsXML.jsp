<%@ page contentType="text/xml" %><%@ include file="/common/taglibs.jsp"%><?xml version="1.0" encoding="UTF-8"?>
<taxa>
<c:forEach items="${searchResults}" var="taxonConcept"><taxon>
<id>${taxonConcept.key}</id>
<scientificName>${taxonConcept.taxonName}</scientificName>
<rank>${taxonConcept.rank}</rank><c:if test="${not empty taxonConcept.commonName}">
<commonName><gbif:capitalizeFirstChar>${taxonConcept.commonName}</gbif:capitalizeFirstChar></commonName></c:if>
</taxon>
</c:forEach></taxa>