<%@ include file="/common/taglibs.jsp"%>
<c:set var="configuredGoogleKey"><gbif:propertyLoader bundle="portal" property="googleKey"/></c:set>
<c:choose>
<c:when test="${not empty configuredGoogleKey}">
<c:set var="googleKey" value="${configuredGoogleKey}"/>
</c:when>
<c:when test="${header.host=='data.gbif.org'}">
<c:set var="googleKey" value="ABQIAAAA_2zKI52BmWetar1csiyF-RQvRCjc3TQlw05MpVnVZIVuO5vVARQ_lGtzSKXT-B9U2_I5PczEkG-U6w"/>
</c:when>
<c:when test="${header.host=='portal.gbif.org'}">
<c:set var="googleKey" value="ABQIAAAA_2zKI52BmWetar1csiyF-RSAGBtgZF6NRYentOF7m3jZoHCTQxQFhvzAhYjygzjYi8Wwt1DJSwXNEQ"/>
</c:when>
<c:when test="${header.host=='portaldev.gbif.org'}">
<c:set var="googleKey" value="ABQIAAAA_2zKI52BmWetar1csiyF-RTQRDnkMJG5ThQFjI-3j0EShrbt5BSOo0oab7FTRVjwLzG9snRvyQCyPQ"/>
</c:when>
<c:when test="${header.host=='newportal.gbif.org'}">
<c:set var="googleKey" value="ABQIAAAA_2zKI52BmWetar1csiyF-RQwT8HkPYNwJ8IaFYbqmAdiTPJyqRTscmB4M-tNOuHBlwig31yJt6xpGg"/>
</c:when>
<c:when test="${header.host=='secretariat.mirror.gbif.org'}">
<c:set var="googleKey" value="ABQIAAAA-3PPe-HBV33KJbGlAmg4xhQf3zKISy14BczCDnZ6J69HjkRlCBRIRh9sBISvS82pFmI4M6eZ9U9xpg"/>
</c:when>
<c:when test="${header.host=='localhost:8080'}">
<c:set var="googleKey" value="ABQIAAAA_2zKI52BmWetar1csiyF-RTwM0brOpm-All5BF6PoaKBxRWWERShBfh3xqAbcFJpRx0piutFGEpSSw"/>
</c:when>
<c:when test="${header.host=='aoraia.gbif.org'}">
<c:set var="googleKey" value="ABQIAAAAUdCLksixDAgnY8JZ7a6YJBTIYSWLB1TX9a3yNQW31Ac6nJE2CxQ5wxLn3h6RglqdLDgwWqmSMTwA_g"/>
</c:when>
</c:choose>
<script src="http://maps.google.com/maps?file=api&amp;v=2&amp;key=${googleKey}" type="text/javascript"></script>
<script src="<c:url value='/javascript/LatLonGraticule.js'/>" type="text/javascript" language="javascript"></script>
<script src="<c:url value='/javascript/googlemaps.js'/>" type="text/javascript" language="javascript"></script>