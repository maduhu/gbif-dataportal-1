<%@ attribute name="defaultLocale" required="false" %>
<%
	java.util.Locale locale = org.springframework.web.servlet.support.RequestContextUtils.getLocale(request);
	if(locale.getLanguage()!=null)
		out.print(locale.getLanguage());
	else
		out.print(defaultLocale);
%>