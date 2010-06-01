<%@ include file="/common/taglibs.jsp"%>
<tiles:insert page="title.jsp"/>
<p>
<spring:message code="repat.tabledescription"/><br/><br/>
<spring:message code="repat.how.to.search.title"/><br/>
<ul class="genericList">
<li><spring:message code="repat.how.to.search.list.item1"/></li>
<li><spring:message code="repat.how.to.search.list.item2"/></li>
<li><spring:message code="repat.how.to.search.list.item3"/></li>
<li><spring:message code="repat.how.to.search.list.item4"/></li>
</ul>
</p>
<form id="repatChangeViewForm" name="viewOptions" method="get" action="">
<table>
  <tr>
    <td>
	  <spring:message code="repat.select.view"/>:
    </td>
    <td>
		<select name="view" onchange="javascript:document.viewOptions.submit();">
			<option value="concise" <c:if test="${param['view']=='concise'}">selected="true"</c:if>><spring:message code="repat.select.view.option.concise"/></option>
		 	<option value="iso" <c:if test="${param['view']=='iso'}">selected="true"</c:if>><spring:message code="repat.select.view.option.iso"/></option>
	    	<option value="percent" <c:if test="${param['view']=='percent'}">selected="true"</c:if>><spring:message code="repat.select.view.option.percent"/></option>            
			<option value="full" <c:if test="${param['view']=='full'}">selected="true"</c:if>><spring:message code="repat.select.view.option.full"/></option>
		</select>
	</td>
    <td style="padding-left:15px;">
      <spring:message code="repat.select.host"/>:
    </td>
    <td>
      <select name="host" onchange="javascript:document.viewOptions.submit();">
        <option value="all" <c:if test="${empty param['host'] || param['host']=='all'}">selected="true"</c:if>><spring:message code="repat.select.option.all"/></option>
        <c:forEach items="${hostsOrdered}" var="host">
        <c:if test="${host!='TW' && host!='CN' && host!='UK'}">
        <option value="${host}" <c:if test="${param['host']==host}">selected="true"</c:if>><spring:message code="country.${host}"/></option>
        </c:if>
        </c:forEach> 
      </select>
    </td>		
    <td style="padding-left:15px;">
      <spring:message code="repat.select.country"/>:
    </td>
    <td>
      <select name="country" onchange="javascript:document.viewOptions.submit();">
        <option value="all" <c:if test="${empty param['country'] || param['country']=='all'}">selected="true"</c:if>><spring:message code="repat.select.option.all"/></option>
        <c:forEach items="${countryList}" var="country">
        <c:if test="${country.isoCountryCode!='TW' && country.isoCountryCode!='UK' && country.isoCountryCode!='CN'}">
         <option value="${country.isoCountryCode}" <c:if test="${param['country']==country.isoCountryCode}">selected="true"</c:if>>
          <spring:message code="country.${country.isoCountryCode}"/>
         </option>
        </c:if> 
        </c:forEach> 
      </select>
    </td>
	</tr>		
</table>	
</form>

<p>
<img src="${pageContext.request.contextPath}/images/icons/disk.gif"/> 
<a href="${pageContext.request.contextPath}/countries/repatriation/table.txt?download=true&stylesheet=repat.xsl<c:if test="${not empty param['host']}">&host=${param['host']}</c:if><c:if test="${not empty param['host']}">&country=${param['country']}</c:if>"><spring:message code="repat.download"/></a></li>
</p>

<style type="text/css">
.rowEntry td {
<c:choose>
<c:when test="${selectedView=='concise'}">text-align: center;</c:when>
<c:otherwise>text-align: right;</c:otherwise>
</c:choose>
}
</style>

<tiles:insert page="params.jsp"/>
<tiles:insert page="table.jsp"/>

<p style="margin-top:20px;">
	<spring:message code="repat.intl.networks.list.title"/><br/>
	<ul class="genericList">
		<li>AndinoNET</li>
		<li>BioNET-ASEANET</li>
		<li>BioNET-EASIANET</li>
		<li>BioNET-INTERNATIONAL</li>
		<li>BioNET-SAFRINET</li>
		<li>Bioversity International</li>
		<li>Botanic Gardens Conservation International</li>
		<li>CABI Bioscience</li>
		<li>Chinese Taipei</li>
		<li>Ciencia y Tecnología para el Desarollo</li>
		<li>Consortium for the Barcode of Life</li>
		<li>Consortium of European Taxonomic Facilities (CETAF)</li>
		<li>DIVERSITAS</li>
		<li>Discover Life </li>
		<li>ETI Bioinformatics </li>
		<li>Encyclopedia of Life (EOL)</li>
		<li>Finding Species</li>
		<li>Freshwater Biological Association - FreshwaterLife</li>
		<li>Integrated Taxonomic Information System</li>
		<li>Inter-American Biodiversity Information Network</li>
		<li>International Centre for Insect Physiology and Ecology</li>
		<li>International Commission on Zoological Nomenclature</li>
		<li>International Species Information System</li>
		<li>Major Systematic Entomology Facilities</li>
		<li>Natural Science Collections Alliance</li>
		<li>NatureServe</li>
		<li>Nordic Genetic Resource Center (NORDGEN)</li>
		<li>Ocean Biogeographic Information System</li>
		<li>Pacific Biodiversity Information Forum</li>
		<li>Scientific Committee on Antarctic Research (SCAR)</li>
		<li>Society for the Preservation of Natural History Collections</li>
		<li>Species 2000</li>
		<li>Taxonomic Databases Working Group</li>
		<li>United Nations Environment Programme</li>
		<li>Wildscreen</li>
		<li>World Data Center for Biodiversity and Ecology</li>
		<li>World Federation for Culture Collections</li>
	</ul>
</p>

<p style="display:none;">Highlighted Cell = <span id="rowDebug"></span></p>
<p style="display:none;">Selected Cell = <span id="selectedRowDebug"></span></p>

<script src="${pageContext.request.contextPath}/javascript/yahoo/yahoo.js" type="text/javascript" language="javascript"></script>
<script src="${pageContext.request.contextPath}/javascript/yahoo/connection.js" type="text/javascript" language="javascript"></script>
<script src="${pageContext.request.contextPath}/javascript/yahoo/dom.js" type="text/javascript" language="javascript"></script>
<script src="${pageContext.request.contextPath}/javascript/yahoo/event.js" type="text/javascript" language="javascript"></script>
<script src="${pageContext.request.contextPath}/javascript/mootools-release-1.11.js" type="text/javascript" language="javascript"></script>
<script src="${pageContext.request.contextPath}/javascript/slimbox.js" type="text/javascript" language="javascript"></script>
<script src="${pageContext.request.contextPath}/javascript/repat.js" type="text/javascript" language="javascript"></script>

<script type="text/javascript">
//set callback and mapserver urls
var serverUrl="http://${header.host}${pageContext.request.contextPath}";
var captionUrl = "http://${header.host}${pageContext.request.contextPath}/countries/repatriation/ajaxCaption?";
//var mapServerUrl = "http://maps.gbif.org/mapserver/draw.pl?dtype=box&imgonly=1&mode=browse&refresh=Refresh&layer=countryborders&layer=countrylabel&path=";

<c:set var="repatriationMaplayers"><gbif:propertyLoader bundle="portal" property="repatriationMaplayers"/></c:set>
<c:choose>
	<c:when test="${not empty repatriationMaplayers}">
		var mapLayerUrl = "${repatriationMaplayers}";
	</c:when>
	<c:otherwise>
		var mapLayerUrl = "http://${header.host}${pageContext.request.contextPath}/maplayers/homeCountry/";
	</c:otherwise>
</c:choose>

var hostCountryUrl = "http://${header.host}${pageContext.request.contextPath}/countries/hosted/";
var countryUrl = "http://${header.host}${pageContext.request.contextPath}/countries/";
var overviewMapFileName = "overviewMap.png";
addListenersToTable('repatTable', 0, ${fn:length(hosts)}, 0, ${fn:length(countries)});
//alert("test");
</script>