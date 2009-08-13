<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
	<title>GBIF Data Portal : ScientificNameSearchAPI</title>
	<base href="http://wiki.gbif.org/dadiwiki/" />
		<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
	<meta name="keywords" content="" />
	<meta name="description" content="" />
	<link rel="stylesheet" type="text/css" href="css/gbif.css" />
	<link rel="stylesheet" type="text/css" href="css/print.css" media="print" />
	<link rel="icon" href="images/favicon.ico" type="image/x-icon" />
	<link rel="shortcut icon" href="images/favicon.ico" type="image/x-icon" />
	<link rel="alternate" type="application/rss+xml" title="GBIF Data Portal Design: revisions for ScientificNameSearchAPI (RSS)" href="http://wiki.gbif.org/dadiwiki/wikka.php?wakka=ScientificNameSearchAPI/revisions.xml" />
	<link rel="alternate" type="application/rss+xml" title="GBIF Data Portal Design: recently edited pages (RSS)" href="http://wiki.gbif.org/dadiwiki/wikka.php?wakka=ScientificNameSearchAPI/recentchanges.xml" />
</head>
<body  >
<!--starting page content-->
<div class="page">
<h2>Scientific name and Common name searching</h2>

<br />
The basic url is <a class="ext" href="http://data.gbif.org/species/nameSearch?query=Puma">http://data.gbif.org/species/nameSearch?query=Puma</a><span class='exttail'>&#8734;</span> to which any of the parameters listed below are added. <br />
<br />
<h5>Example URLs</h5>

<br />
1) Retrieve a list species scientific & common names starting "Pu":<br />
<div class="indent"><a class="ext" href="http://data.gbif.org/species/nameSearch?rank=species&amp;view=text&amp;query=Pu">http://data.gbif.org/species/nameSearch?rank=species&amp;view=text&amp;query=Pu</a><span class='exttail'>&#8734;</span>*<br />
<div class="indent"></div></div>
2) Retrieve a list taxa with GBIF ids with a scientific or common name starting with "A": <br />
<div class="indent"><a class="ext" href="http://data.gbif.org/species/nameSearch?query=A&amp;maxResults=100&amp;returnType=nameId">http://data.gbif.org/species/nameSearch?query=A&amp;maxResults=100&amp;returnType=nameId</a><span class='exttail'>&#8734;</span></div>
<br />
3) Retrieve HTML for map for Lepidoptera in JSON format: <br />
<div class="indent"><a class="ext" href="http://data.gbif.org/species/nameSearch?maxResults=1&amp;view=json&amp;returnType=nameIdMap&amp;query=Lepidoptera&amp;exactOnly=true">http://data.gbif.org/species/nameSearch?maxResults=1&amp;view=json&amp;returnType=nameIdMap&amp;query=Lepidoptera&amp;exactOnly=true</a><span class='exttail'>&#8734;</span></div>
<br />
4) Retrieve a list of common names for Puma concolor in JSON format:<br />
<div class="indent"><a class="ext" href="http://data.gbif.org/species/nameSearch?query=Puma%20concolor&amp;view=json&amp;returnType=commonName">http://data.gbif.org/species/nameSearch?query=Puma%20concolor&amp;view=json&amp;returnType=commonName</a><span class='exttail'>&#8734;</span><br />
</div>
<h5>Required Parameters</h5>

<br />
<ul><li> <strong>query</strong> - the (partial) name to search for.</li></ul>
<br />
<h5>Optional Parameters</h5>

<br />
<ul><li> <strong>allowUnconfirmed</strong> - whether to allow unconfirmed names to be returned. These are names derived from occurrence data which we have not associated with a taxon listed in established checklists such as Catalogue of Life).  Recognised values include "true" and "false". Defaults to false.</li></ul>
<br />
<ul><li> <strong>callback</strong> - the javascript method to use as a callback method. Only used when the json view is selected like so: <strong>view=json</strong>. The callback mechanism used is similar to the callback mechanism used by the Yahoo Web services JSON API. See <a class="ext" href="http://developer.yahoo.com/common/json.html#using">http://developer.yahoo.com/common/json.html#using</a><span class='exttail'>&#8734;</span>
<br />
</li><li>  <strong>exactOnly</strong> - only return exact matches. Recognised values include "true" and "false". Defaults to false.</li></ul>
<br />
<ul><li>  <strong>mapSize</strong> - only relevant when a <strong>returnType=nameIdMap</strong> has been specified (see optional parameter <strong>returnType</strong> below. recognised values include:
<ol type="1"><li> small - image of size 360px by 226px
</li><li> medium - image of size 548px by 320px
</li><li> full (default) - image of size 730px by 410px
</li></ol><br />
</li><li>  <strong>maxResults</strong> - the max results to return. Upper limit is 1000, anything over will be ignored.</li></ul>
<br />
<ul><li> <strong>nameType</strong> - the type of names to match on. Recognised values include:
<ol type="1"><li> scientific - only match scientific names
</li><li> common - only match common names
</li><li> both (the default)
</li></ol><br />
</li><li> <strong>prioritise</strong> - only use this if a value supplied of "both" is supplied for the parameter "names" and you wish to give a priority to scientific or common names (i.e. more scientific name please)
<ol type="1"><li> common - find common names first and include scientific names if space in resultset
</li><li> scientific - find scientific names first and include common names if space in resultset</li></ol></li></ul>
<br />
<ul><li> <strong>rank</strong>  - recognised values include the 7 major Linnaean ranks : 
<ol type="1"><li> kingdom - e.g. only find kingdoms such as Animalia
</li><li> phylum
</li><li> class
</li><li> order
</li><li> family
</li><li> genus
</li><li> species
</li><li> scientificName (covers species & subspecies)
Note: ignored for common name searches at present</li></ol></li></ul>
<br />
<ul><li> <strong>returnType</strong> - recognised values include:
<ol type="1"><li> name  - hence recognised homonym values will be returned only once.
</li><li> nameId  - name and system id pairs
</li><li> nameIdMap  - name and system id pairs plus html for embedding map image
</li><li> nameIdUrl - name and system id pairs plus constructed portal url
</li><li> commonName - returns common names for the supplied sci name </li></ol></li></ul>
<br />
<ul><li> <strong>soundex</strong> - set to true to perform a soundex style matching algorithm. Recognised values include "true" and "false". Defaults to false. Currently only supported for scientific names.</li></ul>
<br />
<ul><li> <strong>startIndex</strong> - enables paging through results. Default value is 0. To start the results from the 10th result, supply a value of 10. </li></ul>
<br />
<ul><li> <strong>view</strong> - specify the view type you wish to be returned
<ol type="1"><li> json - JSON format
</li><li> xml - proprietary simple XML format
</li><li> text - tab delimited text (default)</li></ol></li></ul>

</div>


</body>
</html>