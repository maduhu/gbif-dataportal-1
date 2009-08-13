<%@ attribute name="dataset" required="true" type="java.lang.Object"%><%
	if(dataset instanceof org.gbif.portal.dto.resources.ResourceNetworkDTO){
		out.print("resource");
	} else if(dataset instanceof org.gbif.portal.dto.resources.DataProviderDTO){
		out.print("provider");	
	} else {
		out.print("resource");	
	}
%>