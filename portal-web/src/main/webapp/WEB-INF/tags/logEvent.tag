<%@ attribute name="eventId" required="true" type="java.lang.Integer" %>
<%@ attribute name="outParam" required="true" %>
<%
org.gbif.portal.util.log.LogEvent le = org.gbif.portal.util.log.LogEvent.get(eventId);	
if(le!=null)
	request.setAttribute(outParam, le.getName());
%>