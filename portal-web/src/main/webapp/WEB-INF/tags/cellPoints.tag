<%@ attribute name="cellId" required="true" type="java.lang.Integer" %>
<%
org.gbif.portal.util.geospatial.LatLongBoundingBox llbb = org.gbif.portal.util.geospatial.CellIdUtils.toBoundingBox(cellId);
request.setAttribute("minLatitude", llbb.getMinLat());
request.setAttribute("maxLatitude", llbb.getMaxLat());
request.setAttribute("minLongitude", llbb.getMinLong());
request.setAttribute("maxLongitude", llbb.getMaxLong());
System.out.println(llbb);
%>