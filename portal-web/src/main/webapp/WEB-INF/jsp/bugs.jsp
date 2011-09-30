<%@ include file="/common/taglibs.jsp"%>
<div id="twopartheader">	
	<h2><spring:message code="version"/> <gbif:propertyLoader bundle="portal" property="version"/></h2>
	<h3><spring:message code="bugs.fixes"/></h3>
</div>
<ul class="genericList">
  <li><a href="${pageContext.request.contextPath}/datasets/">Data Publishers</a> - Change all references of "Data Providers" to "Data Publishers"</li>
  <li><a href="${pageContext.request.contextPath}/species/">Species Browse Page</a> - Improved species classification browsing </li>		
</ul>

<h4>Bug fixes in version 1.3.1</h4>
  <ul class="genericList">
    <li>Species classification page - indicate the status of a taxon having occurrence data available -- <a href="http://code.google.com/p/gbif-dataportal/issues/detail?id=52">issue tracking</a> - <a href="${pageContext.request.contextPath}/occurrences/search.htm">SAMPLE</a></li>
    <li>Internationalisation - more internationalised texts inside the data portal -- <a href="http://code.google.com/p/gbif-dataportal/issues/detail?id=85">issue tracking</a> - <a href="${pageContext.request.contextPath}/occurrences/">SAMPLE</a></li>
    <li>Country pages - additional column to show data of a country/resource that is not displayed on the map -- <a href="http://code.google.com/p/gbif-dataportal/issues/detail?id=67">issue tracking</a> - <a href="${pageContext.request.contextPath}/countries/ZA/">SAMPLE</a></li>
    <li>Occurrence search - Searches containing accents are now possible -- <a href="http://code.google.com/p/gbif-dataportal/issues/detail?id=29">issue tracking</a> - <a href="${pageContext.request.contextPath}/occurrences/">SAMPLE</a></li>
    <li>Occurrence page - Identifier links (or multiple ones) are now being displayed when present -- <a href="http://code.google.com/p/gbif-dataportal/issues/detail?id=87">issue tracking</a> - <a href="${pageContext.request.contextPath}/occurrences/208050127/">SAMPLE</a></li>   
  </ul>

<h4>Bug fixes in version 1.3</h4>
<ul class="genericList">
		<li>Occurrence search - initialise variables of bounding box to avoid NaN values -- <a href="http://code.google.com/p/gbif-dataportal/issues/detail?id=56">issue tracking</a> - <a href="${pageContext.request.contextPath}/occurrences/search.htm">SAMPLE</a></li>
		<li>Occurrence search - When selecting datasets, second drop down is now showing -- <a href="http://code.google.com/p/gbif-dataportal/issues/detail?id=50">issue tracking</a> - <a href="${pageContext.request.contextPath}/occurrences/">SAMPLE</a></li>
		<li>Occurrence search - Dataset names are shortened when selecting from the dataset select box -- <a href="http://code.google.com/p/gbif-dataportal/issues/detail?id=19">issue tracking</a> - <a href="${pageContext.request.contextPath}/occurrences/">SAMPLE</a></li>
		<li>Occurrence search results - Suppress message on recognised synonyms when strings identical -- <a href="http://code.google.com/p/gbif-dataportal/issues/detail?id=75">issue tracking</a> - <a href="${pageContext.request.contextPath}/occurrences/search.htm?c[0].s=0&c[0].p=0&c[0].o=Bacillariophyta">SAMPLE</a></li>
		<li>Occurrence download - Collector number is now returned (in all formats) -- <a href="http://code.google.com/p/gbif-dataportal/issues/detail?id=71">issue tracking</a> - <a href="${pageContext.request.contextPath}/occurrences/downloadSpreadsheet.htm?c[0].s=0&c[0].p=0&c[0].o=Amphilophus%20macracanthus&c[1].s=24&c[1].p=0&c[1].o=1023">SAMPLE</a></li>
		<li>Occurrence web service - Date last modified filter working properly -- <a href="http://code.google.com/p/gbif-dataportal/issues/detail?id=61">issue tracking</a> - <a href="${pageContext.request.contextPath}/ws/rest/occurrence/list?dataresourcekey=222&originisocountrycode=ES&modifiedsince=2009-01-01">SAMPLE</a></li>
		<li>Occurrence web service - Response brings back exact number of records -- <a href="http://code.google.com/p/gbif-dataportal/issues/detail?id=72">issue tracking</a> - <a href="${pageContext.request.contextPath}/ws/rest/occurrence/list?scientificname=solanum">SAMPLE</a></li>		
		<li>Data tracking page - Change wording on "View unprocessed data" -- <a href="http://code.google.com/p/gbif-dataportal/issues/detail?id=81">issue tracking</a> - <a href="${pageContext.request.contextPath}/datasets/resource/654/indexing/">SAMPLE</a></li>
		<li>General - Improved treatment of the portal's urls -- <a href="http://code.google.com/p/gbif-dataportal/issues/detail?id=11">issue tracking</a> - <a href="${pageContext.request.contextPath}/occurrences">SAMPLE</a></li>
		<li>Main page - Does not include counts from deleted data publishers -- <a href="http://code.google.com/p/gbif-dataportal/issues/detail?id=74">issue tracking</a> - <a href="${pageContext.request.contextPath}/">SAMPLE</a></li>
		<li>Maps - Geoserver shows all points when using the tab-density plugin -- <a href="http://code.google.com/p/gbif-dataportal/issues/detail?id=82">issue tracking</a> - <a href="${pageContext.request.contextPath}/countries/AG">SAMPLE</a></li>
		<li>Maps - Max default zoom level is now set to 3 (to give users at least a recognisable part of a continent) -- <a href="http://code.google.com/p/gbif-dataportal/issues/detail?id=39">issue tracking</a> - <a href="${pageContext.request.contextPath}/countries/AG">SAMPLE</a></li>
</ul>


<h4>Bug fixes in version 1.2.6</h4>
<ul class="genericList">
		<li>Feedback sent through mirror sites - Mirrors now have the ability to configure their own SMTP server to be used by the mirror web application
		<li>Occurrence Page - In the individual record display, "Dataset Rights" field was included -- <a href="${pageContext.request.contextPath}/occurrences/42352199/">SAMPLE</a></li>
		<li>Occurrences Download - "Dataset Rights" column is now included in the Excel spreadsheet, CSV file and tab delimited file, when downloading occurrence records -- <a href="${pageContext.request.contextPath}/occurrences/downloadSpreadsheet.htm?c[0].s=0&c[0].p=0&c[0].o=Inga%20vera&c[1].s=37&c[1].p=0&c[1].o=0">SAMPLE</a></li>		
		<li>Feedback messages - Users are informed of the "verify your e-mail" process once they submit feedback through the data portal (in case they are not registered) -- <a href="${pageContext.request.contextPath}/feedback/occurrence/45891569">SAMPLE</a></li>
		<li>Species Page - Fixed inconsistency between map display and list of contributing resources -- <a href="${pageContext.request.contextPath}/species/13803552/">SAMPLE</a></li>
		<li>Occurrence Page - In the individual record display, slight change in order of appearance for "Collector name" -- <a href="${pageContext.request.contextPath}/occurrences/110056/">SAMPLE</a></li>
		<li>Species/Occurrences pages - Give more information to the user about the kind of feedback being sent -- <a href="${pageContext.request.contextPath}/occurrences/15904399/">SAMPLE</a></li>
		<li>Species Pages - When a species is shown and it doesn't have any georeferenced records, map won't be displayed  -- <a href="${pageContext.request.contextPath}/species/14443938/">SAMPLE</a></li>
</ul>


<h4>Bug fixes in version 1.2.5</h4>
<ul class="genericList">
		<li>Maps - Switch from Mapserver to Geoserver as map content provider -- <a href="${pageContext.request.contextPath}/countries/AR">SAMPLE</a></li>
		<li>Occurrence search results - Fixed centi-cell accuracy when returning occurrence records within a 1 degree area -- <a href="${pageContext.request.contextPath}/occurrences/searchWithTable.htm?c[1].p=0&c[0].s=24&c[1].o=16.1W%2C28.6N%2C16.0W%2C28.7N&c[0].o=1896&c[0].p=0&c[1].s=19">SAMPLE</a></li>
		<li>Retrieve original record from data provider - Added a 'graceful' error message when the provider is inaccesible  -- <a href="${pageContext.request.contextPath}/occurrences/45340996/rawProviderMessage/">SAMPLE</a></li>
		<li>Dataset Portal logs - Fixed display of the "Date" field in the log events when using Spanish as the preferred language in browser   -- <a href="${pageContext.request.contextPath}/datasets/resource/240/logs/?event=&logGroup=&logLevel=40000&sd_day=20&sd_month=06&sd_year=2007&ed_day=20&ed_month=08&ed_year=2008">SAMPLE</a></li>
		<li>All pages - All pages now provide link to communications portal (www.gbif.org)  -- <a href="${pageContext.request.contextPath}/">SAMPLE</a></li>
</ul>

<h4>Bug fixes in version 1.2.4</h4>
<ul class="genericList">
		<li>Species pages - In "Explore All Occurrences," on each species page, the expected number of records is given -- <a href="${pageContext.request.contextPath}/species/13140809">SAMPLE</a></li>
        <li>Occurrence search - Countries sorted by their name instead of their ISO Code -- <a href="${pageContext.request.contextPath}/occurrences/searchCountries.htm?c[0].s=0&c[0].p=0&c[0].o=Inga vera">SAMPLE</a></li>
        <li>Data Publisher/Dataset pages - Formatted links into real links (a href...) in metadata fields (rights, description) -- <a href="${pageContext.request.contextPath}/datasets/provider/41">SAMPLE</a></li>
		<li>Country pages - Table included - "Countries providing data for Map" -- <a href="${pageContext.request.contextPath}/countries/AR">SAMPLE</a></li>        
        <li>Dataset individual page -  Consolidate content and display of citation texts -- <a href="${pageContext.request.contextPath}/datasets/resource/461">SAMPLE</a></li>
		<li>Occurrence search - Sort datasets by their name when specifying two filters in occurrence search -- <a href="${pageContext.request.contextPath}/occurrences/searchResources.htm?c[0].s=0&c[0].p=0&c[0].o=Inga vera&c[1].s=32&c[1].p=0&c[1].o=US">SAMPLE</a></li>
		<li>Occurrence search - Sort publishers by their name when specifying two filters in occurrence search -- <a href="${pageContext.request.contextPath}/occurrences/searchProviders.htm?c[0].s=0&c[0].p=0&c[0].o=Inga vera&c[1].s=32&c[1].p=0&c[1].o=US">SAMPLE</a></li>
		<li>Occurrence Search - Fixed inclusion of recognised synonyms -- <a href="${pageContext.request.contextPath}/occurrences/search.htm?c[0].s=0&c[0].p=0&c[0].o=Felis+concolor">SAMPLE</a></li>		
		<li>Occurrence Search - Added image filter (filters were re-arranged into categories) -- <a href="${pageContext.request.contextPath}/occurrences/">SAMPLE</a></li>		        
        <li>Occurrence search - Fixed "Occurrence Date" filter. Date string was not updating automatically -- <a href="${pageContext.request.contextPath}/occurrences/">SAMPLE</a></li>
        <li>Country page - Fixed spelling of "Cote D'ivoire" -- <a href="${pageContext.request.contextPath}/countries/CI">SAMPLE</a></li>
		<li>Occurrences page - Removed author name when it is redundant -- <a href="${pageContext.request.contextPath}/occurrences/163107166">SAMPLE</a></li>                
        <li>Occurrence niche model generation page - Fixed spelling of "Precipitation of wettest quarter" -- <a href="${pageContext.request.contextPath}/occurrences/setupModel.htm?c[0].s=0&c[0].p=0&c[0].o=Inga vera">SAMPLE</a></li>
		<li>Occurrences page - Removed word "Year" on the "Date collected" field -- <a href="{pageContext.request.contextPath}/occurrences/163107166">SAMPLE</a></li>
        <li>Occurrences page - Removed "interpreted as" when it is redundant for field "Continent" -- <a href="{pageContext.request.contextPath}/occurrences/163107166">SAMPLE</a></li>
        <li>Dataset page - Non-active access points were removed -- <a href="{pageContext.request.contextPath}/datasets/resource/2023">SAMPLE</a></li>
        <li>General fixes to memory related problems</li>
</ul>