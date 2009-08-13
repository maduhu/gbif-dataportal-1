<%@ include file="/common/taglibs.jsp"%>
<%@ attribute name="issuesBit" required="true" rtexprvalue="true" type="java.lang.Integer" %>
<%@ attribute name="messageSource" required="true" rtexprvalue="true" type="org.springframework.context.MessageSource" %>
<%@ attribute name="locale" required="true" rtexprvalue="true" type="java.util.Locale" %>
<%
	if (issuesBit==0) {
		request.setAttribute("taxonomicIssueText",null);
	} else {
		//no support in JSP EL for bit-wise operators has resulted in this....
		request.setAttribute("taxonomicIssueText", 
								(((issuesBit & 0x01)==0) ? "" : (messageSource.getMessage("taxonomic.not.valid", null, locale) + (issuesBit > 0x01 ? "; " : "")))
								+ (((issuesBit & 0x02)==0) ? "" : (messageSource.getMessage("taxonomic.unknown.kingdom", null, locale) + (issuesBit > 0x03 ? "; " : "")))
								+ (((issuesBit & 0x04)==0) ? "" : messageSource.getMessage("taxonomic.ambiguous", null, locale)));
	}
%>
