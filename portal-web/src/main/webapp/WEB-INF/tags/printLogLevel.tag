<%@ attribute name="level" required="true" rtexprvalue="true" type="java.lang.Long" %>
<%
	org.apache.log4j.Level theLevel = org.apache.log4j.Level.toLevel(level.intValue());
	out.print(theLevel.toString());
%>