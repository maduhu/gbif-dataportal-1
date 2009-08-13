<%@ page contentType="text/xml" %><%@ include file="/common/taglibs.jsp"%><?xml version="1.0" encoding="UTF-8"?>
<commonNames>
<c:forEach items="${searchResults}" var="commonName">	<commonName>
		<id>${commonName.taxonConceptKey}</id>		
		<name><gbif:capitalizeFirstChar>${commonName.name}</gbif:capitalizeFirstChar></name>
		<language>${commonName.language}</language>	
		<scientificName><gbif:capitalizeFirstChar>${commonName.taxonName}</gbif:capitalizeFirstChar></scientificName>	
	</commonName>
</c:forEach></commonNames>