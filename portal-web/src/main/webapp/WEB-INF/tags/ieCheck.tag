<%
	String ua = (String) request.getHeader( "User-Agent" );
	boolean isMSIE = ( ua != null && ua.indexOf( "MSIE" ) != -1 );
	request.setAttribute("isMSIE", isMSIE);
%>