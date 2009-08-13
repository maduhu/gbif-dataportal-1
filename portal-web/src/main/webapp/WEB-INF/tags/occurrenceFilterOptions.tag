<%
	boolean georeferenced = new Boolean(request.getParameter("georeferenced"));
	if(georeferenced)
		out.print("&georeferenced=true");

	boolean taxonomyIssues = new Boolean(request.getParameter("taxonomyIssues"));
	if(taxonomyIssues)
		out.print("&taxonomyIssues=true");

	boolean geospatialIssues = new Boolean(request.getParameter("geospatialIssues"));
	if(geospatialIssues)
		out.write("&geospatialIssues=true");
%>	