<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet 
	version="2.0" 
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
	xmlns:tc="http://rs.tdwg.org/ontology/voc/TaxonConcept#"
	xmlns:tn="http://rs.tdwg.org/ontology/voc/TaxonName#"
	xmlns:to="http://rs.tdwg.org/ontology/voc/TaxonOccurrence#"
	xmlns:tcom="http://rs.tdwg.org/ontology/voc/Common#"
	xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#"
	xmlns:owl="http://www.w3.org/2002/07/owl#"
	xmlns:gbif="http://portal.gbif.org/ws/response/gbif" 
	xmlns:xsd="http://www.w3.org/2001/XMLSchema" 
	xmlns:xs="http://www.w3.org/2001/XMLSchema" 
	xmlns:fn="http://www.w3.org/2005/xpath-functions" 
	xmlns:xdt="http://www.w3.org/2005/xpath-datatypes">
	<xsl:output version="1.0" encoding="UTF-8" indent="no" doctype-public="-//W3C//DTD XHTML 1.0 Strict//EN"  omit-xml-declaration="no" media-type="text/xhtml"/>
	
	<xsl:key name="taxa" match="tc:TaxonConcept" use="@gbifKey"/>
	 
	<xsl:template match="gbif:header">
		<table id="reqSummary" summary="Request details">
			<tr><th colspan="2">Request details</th></tr>
			<xsl:for-each select="gbif:parameter"><tr><td><xsl:value-of select="@name"/></td><td><xsl:value-of select="@value"/></td></tr></xsl:for-each>
			<xsl:for-each select="gbif:summary">
				<xsl:for-each select="@start"><tr><td>First record returned</td><td><xsl:value-of select="."/></td></tr></xsl:for-each>
				<xsl:for-each select="@next"><tr><td>Next record available</td><td><xsl:value-of select="."/></td></tr></xsl:for-each>
				<xsl:for-each select="@totalReturned"><tr><td>Number of records returned</td><td><xsl:value-of select="."/></td></tr></xsl:for-each>
				<xsl:for-each select="@totalMatched"><tr><td>Number of records matched</td><td><xsl:value-of select="."/></td></tr></xsl:for-each>
			</xsl:for-each>
			<xsl:for-each select="gbif:nextRequestUrl"><td>Next page of records</td><td><a><xsl:attribute name="href"><xsl:value-of select="."/></xsl:attribute><xsl:apply-templates/></a></td></xsl:for-each>
		</table>

		<xsl:for-each select="gbif:statements"><pre><h3 id="summary"><xsl:apply-templates select="."/></h3></pre></xsl:for-each>
		
		<xsl:for-each select="gbif:help">
			<xsl:choose>
				<xsl:when test="starts-with(.,'http://')">
					<h3 id="summary">For help with this web service, see: <a><xsl:attribute name="href"><xsl:value-of select="."/></xsl:attribute><xsl:value-of select="."/></a></h3>
				</xsl:when>
				<xsl:otherwise>
					<pre><h3 id="summary"><xsl:value-of select="."/></h3></pre>
				</xsl:otherwise>
			</xsl:choose>
		</xsl:for-each>
	</xsl:template>
	
	<xsl:template match="tc:TaxonConcept">
		<a><xsl:attribute name="name"><xsl:text disable-output-escaping="yes">taxon-</xsl:text><xsl:value-of select="@gbifKey"/></xsl:attribute></a>
		<h6 class="dlHead"><xsl:value-of select="./tc:hasName/tn:TaxonName/tn:nameComplete"/> (according to: <xsl:value-of select="./tc:accordingToString"/><xsl:text disable-output-escaping="yes">)</xsl:text></h6>
		<hr size="1"/>
		<dl class="tablestyle">
			<dt>Taxon key in GBIF portal</dt><dd><xsl:value-of select="@gbifKey"/></dd><br/>
			<dt>Taxon page in GBIF portal</dt><dd><a><xsl:attribute name="href"><xsl:text disable-output-escaping="yes">__PORTALROOT__/species/</xsl:text><xsl:value-of select="@gbifKey"/></xsl:attribute><xsl:text disable-output-escaping="yes">__PORTALROOT__/species/</xsl:text><xsl:value-of select="@gbifKey"/></a></dd><br/>
			<dt>Status in GBIF portal</dt><dd><xsl:value-of select="@status"/></dd><br/>
			<dt>Web service request for data for taxon concept</dt><dd><a><xsl:attribute name="href"><xsl:value-of select="./@rdf:about"/></xsl:attribute><xsl:value-of select="./@rdf:about"/></a></dd><br/>
			<dt>Web service request for occurrences for taxon</dt><dd><a><xsl:attribute name="href"><xsl:text disable-output-escaping="yes">__WSROOT__/rest/occurrence/list?taxonconceptkey=</xsl:text><xsl:value-of select="@gbifKey"/></xsl:attribute><xsl:text disable-output-escaping="yes">__WSROOT__/rest/occurrence/list?taxonconceptkey=</xsl:text><xsl:value-of select="@gbifKey"/></a></dd><br/>
			<xsl:for-each select="tc:hasName/tn:TaxonName[tn:scientific='true']">
				<dt>Scientific name</dt><dd><xsl:value-of select="./tn:nameComplete"/></dd><br/>
				<xsl:for-each select="./tn:authorship">
					<dt>Authorship</dt><dd><xsl:value-of select="."/></dd><br/>
				</xsl:for-each>
				<xsl:for-each select="./tn:rank/@rdf:resource">
					<dt>Rank</dt><dd><a><xsl:attribute name="href"><xsl:value-of select="."/></xsl:attribute><xsl:value-of select="substring-after(.,'#')"/></a></dd><br/>
				</xsl:for-each>
			</xsl:for-each>
			<xsl:for-each select="tc:hasName/tn:TaxonName[tn:scientific='false']">
				<dt>Common name</dt><dd><xsl:value-of select="./tn:nameComplete"/></dd><br/>
				<xsl:for-each select="./tn:language">
					<dt>Language</dt><dd><xsl:value-of select="."/></dd><br/>
				</xsl:for-each>
				<xsl:for-each select="./tn:rank/@rdf:resource">
					<dt>Rank</dt><dd><a><xsl:attribute name="href"><xsl:value-of select="."/></xsl:attribute><xsl:value-of select="substring-after(./tn:rank/@rdf:resource,'#')"/></a></dd><br/>
				</xsl:for-each>
			</xsl:for-each>
			<xsl:for-each select="*">
				<xsl:choose>
					<xsl:when test="name(.)='tc:hasName'"/>
					<xsl:when test="name(.)='tc:hasRelationship'"/>
					<xsl:when test="name(.)='tc:accordingTo'"/>
					<xsl:when test="name(.)='tc:accordingToString'"/>
					<xsl:when test="name(.)='tc:primary'"/>
					<xsl:when test="name(.)='owl:sameAs'"/>
					<xsl:otherwise>
						<dt><xsl:value-of select="local-name(.)"/></dt><dd><xsl:value-of select="."/></dd><br/>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:for-each>
			<xsl:for-each select="tc:hasRelationship/tc:Relationship">
				<dt><a><xsl:attribute name="href"><xsl:value-of select="./tc:relationshipCategory/@rdf:resource"/></xsl:attribute><xsl:value-of select="substring-after(./tc:relationshipCategory/@rdf:resource,'#')"/></a></dt><dd><xsl:for-each select="key('taxa',substring-after(./tc:toTaxon/@rdf:resource,'/get/'))"><a><xsl:attribute name="href"><xsl:text disable-output-escaping="yes">__WSROOT__/rest/taxon/get/</xsl:text><xsl:value-of select="./@gbifKey"/></xsl:attribute><xsl:value-of select="./tc:hasName/tn:TaxonName/tn:nameComplete"/></a> (according to: <xsl:value-of select="./tc:accordingToString"/><xsl:text disable-output-escaping="yes">)</xsl:text></xsl:for-each> - taxon <xsl:value-of select="substring-after(./tc:toTaxon/@rdf:resource,'/get/')"/></dd><br/>
			</xsl:for-each>
		</dl>
		<hr size="1"/>
	</xsl:template>

	<xsl:template match="gbif:taxonConcepts">
		<h4>Taxon concepts</h4>
		<div class="occurrence">
			<xsl:for-each select="tc:TaxonConcept[tc:primary='true']">
				<xsl:apply-templates select="."/>
			</xsl:for-each>
			<xsl:for-each select="tc:TaxonConcept[tc:primary='false']">
				<xsl:apply-templates select="."/>
			</xsl:for-each>
		</div>
	</xsl:template>

	<xsl:template match="gbif:occurrenceRecords">
		<h4>Occurrence overview</h4>
		<div class="occurrence">
			<table id="occurrenceOverview" width="100%">
				<tbody>
					<tr>
						<th>Key</th>
						<th>Catalogue number</th>
						<th>Scientific name</th>
						<th>Latitude</th>
						<th>Longitude</th>
						<th>Date</th>
						<th>Portal URL</th>
					</tr>
					<xsl:for-each select="to:TaxonOccurrence">
						<tr>
							<td><a>
								<xsl:attribute name="href"><xsl:text disable-output-escaping="yes">#occurrence-</xsl:text><xsl:value-of select="@gbifKey"/></xsl:attribute>
								<xsl:value-of select="@gbifKey"/>
							</a></td>
							<td><xsl:value-of select="./to:catalogNumber"/></td>
							<td><xsl:value-of select="./to:identifiedTo/to:Identification/to:taxonName"/></td>
							<td><xsl:value-of select="./to:decimalLatitude"/></td>
							<td><xsl:value-of select="./to:decimalLongitude"/></td>
							<td><xsl:value-of select="./to:earliestDateCollected"/></td>
							<td><a><xsl:attribute name="href"><xsl:text disable-output-escaping="yes">__PORTALROOT__/occurrences/</xsl:text><xsl:value-of select="@gbifKey"/></xsl:attribute><xsl:text disable-output-escaping="yes">__PORTALROOT__/occurrences/</xsl:text><xsl:value-of select="@gbifKey"/></a></td>
						</tr>
					</xsl:for-each>
				</tbody>
			</table>
		</div>
		<h4>Occurrence data</h4>
		<div class="occurrence">
			<xsl:for-each select="to:TaxonOccurrence">
				<hr size="1"/>
				<a><xsl:attribute name="name"><xsl:text disable-output-escaping="yes">occurrence-</xsl:text><xsl:value-of select="@gbifKey"/></xsl:attribute></a>
				<h6 class="dlHead"><xsl:value-of select="./to:identifiedTo/to:Identification/to:taxonName"/><xsl:text disable-output-escaping="yes"> (Catalogue number: </xsl:text><xsl:value-of select="./to:catalogNumber"/><xsl:text disable-output-escaping="yes">)</xsl:text></h6>
				<hr size="1"/>
				<dl class="tablestyle">
					<dt>Occurrence key in GBIF portal</dt><dd><xsl:value-of select="@gbifKey"/></dd><br/>
					<dt>Occurrence page in GBIF portal</dt><dd><a><xsl:attribute name="href"><xsl:text disable-output-escaping="yes">__PORTALROOT__/occurrences/</xsl:text><xsl:value-of select="@gbifKey"/></xsl:attribute><xsl:text disable-output-escaping="yes">__PORTALROOT__/occurrences/</xsl:text><xsl:value-of select="@gbifKey"/></a></dd><br/>
					<dt>Web service request for data for occurrence</dt><dd><a><xsl:attribute name="href"><xsl:value-of select="./@rdf:about"/></xsl:attribute><xsl:value-of select="./@rdf:about"/></a></dd><br/>
					<dt>Taxon key in GBIF portal</dt><dd><xsl:value-of select="./to:identifiedTo/to:Identification/to:taxon/tc:TaxonConcept/@gbifKey"/></dd><br/>
					<dt>Taxon page in GBIF portal</dt><dd><a><xsl:attribute name="href"><xsl:text disable-output-escaping="yes">__PORTALROOT__/species/</xsl:text><xsl:value-of select="./to:identifiedTo/to:Identification/to:taxon/tc:TaxonConcept/@gbifKey"/></xsl:attribute><xsl:text disable-output-escaping="yes">__PORTALROOT__/species/</xsl:text><xsl:value-of select="./to:identifiedTo/to:Identification/to:taxon/tc:TaxonConcept/@gbifKey"/></a></dd><br/>
					<dt>Web service request for taxon</dt><dd><a><xsl:attribute name="href"><xsl:text disable-output-escaping="yes">__WSROOT__/rest/taxon/get/</xsl:text><xsl:value-of select="./to:identifiedTo/to:Identification/to:taxon/tc:TaxonConcept/@gbifKey"/></xsl:attribute><xsl:text disable-output-escaping="yes">__WSROOT__/rest/taxon/get/</xsl:text><xsl:value-of select="./to:identifiedTo/to:Identification/to:taxon/tc:TaxonConcept/@gbifKey"/></a></dd><br/>
					<xsl:for-each select="*">
						<xsl:choose>
							<xsl:when test="name(.)='to:identifiedTo'">
								<xsl:for-each select="./to:Identification">
									<xsl:for-each select="./to:taxon/tc:TaxonConcept/tc:hasName/tn:TaxonName/tn:nameComplete">
										<dt>Identified as</dt><dd><xsl:value-of select="."></xsl:value-of></dd><br/>
									</xsl:for-each>
									<xsl:for-each select="./to:taxon/tc:TaxonConcept/tc:hasName/tn:TaxonName/tn:rank">
										<dt>Identification rank</dt><dd><a><xsl:attribute name="href"><xsl:value-of select="./@rdf:resource"/></xsl:attribute><xsl:value-of select="substring-after(./@rdf:resource,'#')"/></a></dd><br/>
									</xsl:for-each>
									<xsl:for-each select="./tcom:taxonomicPlacementFormal">
										<dt>Identification higher taxa</dt><dd><xsl:value-of select="."/></dd><br/>
									</xsl:for-each>
									<xsl:for-each select="./to:expertName">
										<dt>Identified by</dt><dd><xsl:value-of select="."/></dd><br/>
									</xsl:for-each>
									<xsl:for-each select="./to:date">
										<dt>Identification date</dt><dd><xsl:value-of select="."/></dd><br/>
									</xsl:for-each>
								</xsl:for-each>
							</xsl:when>
							<xsl:when test="name(.)='owl:sameAs'"/>
							<xsl:otherwise>
								<dt><xsl:value-of select="local-name(.)"/></dt><dd><xsl:value-of select="."/></dd><br/>
							</xsl:otherwise>
						</xsl:choose>
					</xsl:for-each>
				</dl>
			</xsl:for-each>
			<hr size="1"/>
		</div>
	</xsl:template>
	
	<xsl:template match="gbif:dataResource">
		<h3>Dataset: <xsl:value-of select="gbif:name"/></h3>
		<div class="resource">
			<xsl:for-each select="gbif:logoUrl">
				<img><xsl:attribute name="src"><xsl:value-of select="."/></xsl:attribute></img><br/>
			</xsl:for-each>
			<dl class="tablestyle">
				<xsl:for-each select="*">
					<xsl:choose>
						<xsl:when test="name(.)='gbif:name'"/>
						<xsl:when test="name(.)='gbif:accessPoints'">
							<xsl:for-each select="gbif:accessPoint">
								<dt>Access point URL</dt><dd><a><xsl:attribute name="href"><xsl:value-of select="gbif:url"/></xsl:attribute><xsl:value-of select="gbif:url"/></a></dd><br/>
								<xsl:for-each select="gbif:identifier">
									<dt>Access point resource identifier</dt><dd><xsl:value-of select="."/></dd><br/>
								</xsl:for-each>
								<xsl:for-each select="gbif:schema">
									<dt>Access point schema</dt><dd><xsl:value-of select="."/></dd><br/>
								</xsl:for-each>
							</xsl:for-each>
						</xsl:when>
						<xsl:when test="name(.)='gbif:resourceNetworks'"/>
						<xsl:when test="name(.)='gbif:taxonConcepts'"/>
						<xsl:when test="name(.)='gbif:occurrenceRecords'"/>
						<xsl:when test="name(.)='owl:sameAs'"/>
						<xsl:otherwise>
							<dt><xsl:value-of select="local-name(.)"/></dt><dd><xsl:value-of select="."/></dd><br/>
						</xsl:otherwise>
					</xsl:choose>
				</xsl:for-each>
				<dt>Dataset key in GBIF portal</dt><dd><xsl:value-of select="@gbifKey"/></dd><br/>
				<dt>Dataset page in GBIF portal</dt><dd><a><xsl:attribute name="href"><xsl:text disable-output-escaping="yes">__PORTALROOT__/datasets/resource/</xsl:text><xsl:value-of select="@gbifKey"/></xsl:attribute><xsl:text disable-output-escaping="yes">__PORTALROOT__/datasets/resource/</xsl:text><xsl:value-of select="@gbifKey"/></a></dd><br/>
				<dt>Web service request for metadata for dataset</dt><dd><a><xsl:attribute name="href"><xsl:value-of select="./@rdf:about"/></xsl:attribute><xsl:value-of select="./@rdf:about"/></a></dd><br/>
				<dt>Web service request for occurrences from dataset</dt><dd><a><xsl:attribute name="href"><xsl:text disable-output-escaping="yes">__WSROOT__/rest/occurrence/list?dataresourcekey=</xsl:text><xsl:value-of select="@gbifKey"/></xsl:attribute><xsl:text disable-output-escaping="yes">__WSROOT__/rest/occurrence/list?dataresourcekey=</xsl:text><xsl:value-of select="@gbifKey"/></a></dd><br/>
			</dl><br/>
			<xsl:for-each select="gbif:resourceNetworks">
				<xsl:apply-templates select="gbif:resourceNetwork"/>
			</xsl:for-each>
			<xsl:for-each select="gbif:taxonConcepts">
				<xsl:apply-templates select="."/>
			</xsl:for-each>
			<xsl:for-each select="gbif:occurrenceRecords">
				<xsl:apply-templates select="."/>
			</xsl:for-each>
		</div>
	</xsl:template>
	
	<xsl:template match="gbif:dataProvider">
		<h3>Data provider: <xsl:value-of select="gbif:name"/></h3>
		<div class="provider">
			<xsl:for-each select="gbif:logoUrl">
				<img><xsl:attribute name="src"><xsl:value-of select="."/></xsl:attribute></img><br/>
			</xsl:for-each>
			<dl class="tablestyle">
				<xsl:for-each select="*">
					<xsl:choose>
						<xsl:when test="name(.)='gbif:name'"/>
						<xsl:when test="name(.)='gbif:dataResources'"/>
						<xsl:when test="name(.)='owl:sameAs'"/>
						<xsl:otherwise>
							<dt><xsl:value-of select="local-name(.)"/></dt><dd><xsl:value-of select="."/></dd><br/>
						</xsl:otherwise>
					</xsl:choose>
				</xsl:for-each>
				<dt>Data provider key in GBIF portal</dt><dd><xsl:value-of select="@gbifKey"/></dd><br/>
				<dt>Data provider page in GBIF portal</dt><dd><a><xsl:attribute name="href"><xsl:text disable-output-escaping="yes">__PORTALROOT__/datasets/provider/</xsl:text><xsl:value-of select="@gbifKey"/></xsl:attribute><xsl:text disable-output-escaping="yes">__PORTALROOT__/datasets/provider/</xsl:text><xsl:value-of select="@gbifKey"/></a></dd><br/>
				<dt>Web service request for metadata for data provider</dt><dd><a><xsl:attribute name="href"><xsl:value-of select="./@rdf:about"/></xsl:attribute><xsl:value-of select="./@rdf:about"/></a></dd><br/>
			</dl><br/>
			<xsl:for-each select="gbif:dataResources">
				<xsl:apply-templates select="gbif:dataResource"/>
			</xsl:for-each>
		</div>
	</xsl:template>
	
	<xsl:template match="gbif:resourceNetwork">
		<h3>Data network: <xsl:value-of select="gbif:name"/></h3>
		<div class="network">
			<xsl:for-each select="gbif:logoUrl">
				<img><xsl:attribute name="src"><xsl:value-of select="."/></xsl:attribute></img><br/>
			</xsl:for-each>
			<dl class="tablestyle">
				<xsl:for-each select="*">
					<xsl:choose>
						<xsl:when test="name(.)='gbif:name'"/>
						<xsl:when test="name(.)='gbif:dataResources'"/>
						<xsl:when test="name(.)='owl:sameAs'"/>
						<xsl:otherwise>
							<dt><xsl:value-of select="local-name(.)"/></dt><dd><xsl:value-of select="."/></dd><br/>
						</xsl:otherwise>
					</xsl:choose>
				</xsl:for-each>
				<dt>Data network key in GBIF portal</dt><dd><xsl:value-of select="@gbifKey"/></dd><br/>
				<dt>Data network page in GBIF portal</dt><dd><a><xsl:attribute name="href"><xsl:text disable-output-escaping="yes">__PORTALROOT__/datasets/network/</xsl:text><xsl:value-of select="@gbifKey"/></xsl:attribute><xsl:text disable-output-escaping="yes">__PORTALROOT__/datasets/network/</xsl:text><xsl:value-of select="@gbifKey"/></a></dd><br/>
				<dt>Web service request for metadata for data network</dt><dd><a><xsl:attribute name="href">xsl:value-of select="./@rdf:about"/></xsl:attribute><xsl:value-of select="./@rdf:about"/></a></dd><br/>
			</dl><br/>
			<xsl:for-each select="gbif:dataResources">
				<xsl:apply-templates select="gbif:dataResource"/>
			</xsl:for-each>
		</div>
	</xsl:template>
	
	<xsl:template match="/">
		
		<html>
			
			<xsl:choose>

				<xsl:when test="gbif:gbifResponse/gbif:header/gbif:parameter[@name='service']/@value='taxon'">
					<head>
						<meta content="text/html; charset=utf-8" http-equiv="Content-Type"/>
						<title>GBIF taxon web service response</title>
						<link href="__PORTALROOT__/skins/ws/XML.css" rel="stylesheet" type="text/css"/>
						<link href="__PORTALROOT__/skins/ws/taxon.css" rel="stylesheet" type="text/css"/>
					</head>
					
					<body>
						<h1>GBIF taxon web service response</h1>
						<xsl:for-each select="gbif:gbifResponse">
							<xsl:apply-templates select="gbif:header"/>
							<xsl:for-each select="gbif:dataProviders">
								<h2 class="leftIcon">Taxon concept data</h2>
								<xsl:apply-templates/>
							</xsl:for-each>
						</xsl:for-each>
					</body>
				</xsl:when>
				
				<xsl:when test="gbif:gbifResponse/gbif:header/gbif:parameter[@name='service']/@value='provider'">
					<html>
						<head>
							<meta content="text/html; charset=utf-8" http-equiv="Content-Type"/>
							<title>GBIF provider web service response</title>
							<link href="__PORTALROOT__/skins/ws/XML.css" rel="stylesheet" type="text/css"/>
							<link href="__PORTALROOT__/skins/ws/provider.css" rel="stylesheet" type="text/css"/>
						</head>
						
						<body>
							<h1>GBIF provider web service response</h1>
							<xsl:for-each select="gbif:gbifResponse">
								<xsl:apply-templates select="gbif:header"/>
								<xsl:for-each select="gbif:dataProviders">
									<h2 class="leftIcon">Metadata for GBIF data providers</h2>
									<xsl:apply-templates select="gbif:dataProvider"/>
								</xsl:for-each>
							</xsl:for-each>
						</body>
					</html>
				</xsl:when>
				
				<xsl:when test="gbif:gbifResponse/gbif:header/gbif:parameter[@name='service']/@value='resource'">
					<head>
						<meta content="text/html; charset=utf-8" http-equiv="Content-Type"/>
						<title>GBIF resource web service response</title>
						<link href="__PORTALROOT__/skins/ws/XML.css" rel="stylesheet" type="text/css"/>
						<link href="__PORTALROOT__/skins/ws/resource.css" rel="stylesheet" type="text/css"/>
					</head>
					
					<body>
						<h1>GBIF resource web service response</h1>
						<xsl:for-each select="gbif:gbifResponse">
							<xsl:apply-templates select="gbif:header"/>
							<xsl:for-each select="gbif:dataProviders">
								<h2 class="leftIcon">Metadata for datasets served by GBIF data providers</h2>
								<xsl:apply-templates select="gbif:dataProvider"/>
							</xsl:for-each>
						</xsl:for-each>
					</body>
				</xsl:when>
				
				<xsl:when test="gbif:gbifResponse/gbif:header/gbif:parameter[@name='service']/@value='network'">
					<head>
						<meta content="text/html; charset=utf-8" http-equiv="Content-Type"/>
						<title>GBIF network web service response</title>
						<link href="__PORTALROOT__/skins/ws/XML.css" rel="stylesheet" type="text/css"/>
						<link href="__PORTALROOT__/skins/ws/network.css" rel="stylesheet" type="text/css"/>
					</head>
					
					<body>
						<h1>GBIF network web service response</h1>
						<xsl:for-each select="gbif:gbifResponse">
							<xsl:apply-templates select="gbif:header"/>
							<xsl:for-each select="gbif:resourceNetworks">
								<h2 class="leftIcon">Metadata for networks of datasets included in the GBIF network</h2>
								<xsl:apply-templates/>
							</xsl:for-each>
						</xsl:for-each>
					</body>
				</xsl:when>
				
				<xsl:when test="gbif:gbifResponse/gbif:header/gbif:parameter[@name='service']/@value='density'">
					<head>
						<meta content="text/html; charset=utf-8" http-equiv="Content-Type"/>
						<title>GBIF provider web service response</title>
						<link href="__PORTALROOT__/skins/ws/XML.css" rel="stylesheet" type="text/css"/>
						<link href="__PORTALROOT__/skins/ws/density.css" rel="stylesheet" type="text/css"/>
					</head>
					
					<body>
						<h1>GBIF occurrence density web service response</h1>
						<xsl:for-each select="gbif:gbifResponse">
							<xsl:apply-templates select="gbif:header"/>
							<xsl:for-each select="gbif:densityRecords">
								<h2 class="leftIcon">Occurrence record counts by one-degree cell</h2>
								
								<table width="100%"><tr><th>Cell id</th><th>Latitude</th><th>Longitude</th><th>Count</th><th>Portal URL</th></tr>
									<xsl:for-each select="gbif:densityRecord"><tr>
										<td><xsl:value-of select="./@cellid"/></td>
										<td>(<xsl:value-of select="./gbif:minLatitude"/> - <xsl:value-of select="./gbif:maxLatitude"/>)</td>
										<td>(<xsl:value-of select="./gbif:minLongitude"/> - <xsl:value-of select="./gbif:maxLongitude"/>)</td>
										<td><xsl:value-of select="./gbif:count"/></td>
										<td><a><xsl:attribute name="href"><xsl:value-of select="./gbif:portalUrl"/></xsl:attribute><xsl:value-of select="./gbif:portalUrl"/></a></td>
									</tr>
									</xsl:for-each>
								</table>
								
							</xsl:for-each>
						</xsl:for-each>
					</body>
				</xsl:when>
				
				<xsl:otherwise>
					<head>
						<meta content="text/html; charset=utf-8" http-equiv="Content-Type"/>
						<title>GBIF occurrence web service response</title>
						<link href="__PORTALROOT__/skins/ws/XML.css" rel="stylesheet" type="text/css"/>
						<link href="__PORTALROOT__/skins/ws/occurrence.css" rel="stylesheet" type="text/css"/>
					</head>
					<body>
						<h1>GBIF occurrence web service response</h1>
						<xsl:for-each select="gbif:gbifResponse">
							<xsl:apply-templates select="gbif:header"/>
							<xsl:for-each select="gbif:dataProviders">
								<h2 class="leftIcon">Occurrence data</h2>
								<xsl:apply-templates/>
							</xsl:for-each>
						</xsl:for-each>
					</body>
				</xsl:otherwise>
				
			</xsl:choose>

		</html>
	</xsl:template>

</xsl:stylesheet>
