<%@ attribute name="email" required="true" %>
<%
	if(email!=null && email.indexOf('@')>0){
		email = email.replace("@", "(at)");
	}
	out.print(email);
%>