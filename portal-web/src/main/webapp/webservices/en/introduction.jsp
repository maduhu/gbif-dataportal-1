<p></p>The portal includes a range of web services that can be used by other portals and applications to directly access XML formatted GBIF data.   

The services currently available include: 
<p></p><br/>
<ul>
<li><a href="${pageContext.request.contextPath}/ws/rest/occurrence"><b>Occurrence record data service</b></a> - This service provides a range of filters for selecting occurrence records. The currently supported response formats include <a href="http://www.tdwg.org" target="_blank">TDWG</a> <a href="http://wiki.tdwg.org/DarwinCore" target="_blank">Darwin Core</a> records and <a href="http://code.google.com/apis/kml/documentation/" target="_blank">KML</a> (for use with <a href="http://earth.google.com" target="_blank">Google Earth</a>).</li><br/>

<li><a href="${pageContext.request.contextPath}/ws/rest/taxon"><b>Taxon data service</b></a> - This service provides a range of options for viewing information on the names and classifications used by the different datasets that are accessible through the GBIF portal. Data is returned using the <a href="http://www.tdwg.org" target="_blank">TDWG</a> <a href="http://www.tdwg.org/activities/tnc/tcs-schema-repository/" target="_blank">Taxon Concept Schema</a>.</li><br/>

<li><a href="${pageContext.request.contextPath}/ws/rest/provider"><b>Data publisher metadata service</b></a> - This service returns metadata on the data publishers sharing data through the GBIF portal. The currently supported response format is a simple XML structure.</li><br/>

<li><a href="${pageContext.request.contextPath}/ws/rest/resource"><b>Dataset metadata service</b></a> - This service returns metadata relating to the datasets accessible through the GBIF portal. The currently supported response format is a simple XML structure.</li><br/>

<li><a href="${pageContext.request.contextPath}/ws/rest/network"><b>Data network metadata service</b></a> - This service returns metadata on the data networks connected to the GBIF portal. The currently supported response format is a simple XML structure.</li><br/>

<li><a href="${pageContext.request.contextPath}/ws/rest/density"><b>Occurrence density data service</b></a> - This service returns summary counts of occurrence records by one-degree cell for a single taxon, country, dataset, data publisher or data network. The currently supported response formats include a simple XML format and KML (for use with <a href="http://earth.google.com" target="_blank">Google Earth</a>).</li><br/>
</ul>
<p/><p/><br/><br/>

Note that responses from these web services include styling instructions to make them easier to read and use in a web browser. The web service documentation includes instructions for disabling this feature if it is not desired.