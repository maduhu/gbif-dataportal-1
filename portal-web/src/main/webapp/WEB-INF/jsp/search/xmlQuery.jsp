<%@ include file="/common/taglibs.jsp"%>
<response>
	<datasource>
		<dbsource>Global Biodiversity Information Facility (GBIF)</dbsource>
		<dbwebaddress>http://${header.host}${pageContext.request.contextPath}</dbwebaddress>
		<dbdatadate>2007-09-12</dbdatadate>
		<dbcurrentdate>2007-11-01</dbcurrentdate>
		<dbexpirydate/>
    <dbtermsofuse>Please read the following webpage for information on using this
data: http://data.gbif.org/tutorial/datauseagreement</dbtermsofuse>
	</datasource>
  <taxa>
	<c:forEach items="${searchResults}" var="taxonConcept" varStatus="taxonConceptStatus"><taxon>
	    <taxonkey>${taxonConcept.key}</taxonkey>
	    <taxonname>${taxonConcept.taxonName}</taxonname>
	    <rank><string:capitalize>${taxonConcept.rank}</string:capitalize></rank>	
	    <kingdom><string:capitalize>${taxonConcept.kingdom}</string:capitalize></kingdom>
	    <records>
	     <c:set var="statsItem" value="${stats[taxonConceptStatus.index]}"/>	
	     <totalrecords>${statsItem.occurrenceCount}</totalrecords>
	     <totalgisrecords>${statsItem.occurrenceCoordinateCount}</totalgisrecords>
	     <url>http://${header.host}${pageContext.request.contextPath}/species/${taxonConcept.key}/</url>
	    </records>
	</taxon>
	</c:forEach></taxa>
</response>