<%@ include file="/common/taglibs.jsp"%>
<div id="twopartheader">	
	<h2><spring:message code="version"/> <gbif:propertyLoader bundle="portal" property="version"/></h2>
	<h3><spring:message code="bugs.fixes"/></h3>
</div>
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
        <li>Data Provider/Dataset pages - Formatted links into real links (a href...) in metadata fields (rights, description) -- <a href="${pageContext.request.contextPath}/datasets/provider/41">SAMPLE</a></li>
		<li>Country pages - Table included - "Countries providing data for Map" -- <a href="${pageContext.request.contextPath}/countries/AR">SAMPLE</a></li>        
        <li>Dataset individual page -  Consolidate content and display of citation texts -- <a href="${pageContext.request.contextPath}/datasets/resource/461">SAMPLE</a></li>
		<li>Occurrence search - Sort datasets by their name when specifying two filters in occurrence search -- <a href="${pageContext.request.contextPath}/occurrences/searchResources.htm?c[0].s=0&c[0].p=0&c[0].o=Inga vera&c[1].s=32&c[1].p=0&c[1].o=US">SAMPLE</a></li>
		<li>Occurrence search - Sort providers by their name when specifying two filters in occurrence search -- <a href="${pageContext.request.contextPath}/occurrences/searchProviders.htm?c[0].s=0&c[0].p=0&c[0].o=Inga vera&c[1].s=32&c[1].p=0&c[1].o=US">SAMPLE</a></li>
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