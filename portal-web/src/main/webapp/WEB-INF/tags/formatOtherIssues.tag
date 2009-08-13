<%@ include file="/common/taglibs.jsp"%>
<%@ attribute name="issuesBit" required="true" rtexprvalue="true" type="java.lang.Integer" %>
<%@ attribute name="messageSource" required="true" rtexprvalue="true" type="org.springframework.context.MessageSource" %>
<%@ attribute name="locale" required="true" rtexprvalue="true" type="java.util.Locale" %>
<%
	if (issuesBit==0) {
		request.setAttribute("otherIssueText",null);
	} else {
		//no support in JSP EL for bit-wise operators has resulted in this....
		request.setAttribute("otherIssueText", 
								(((issuesBit & 0x01)==0) ? "" : (messageSource.getMessage("other.missing.catalogue.number", null, locale) + (issuesBit > 0x01 ? "; " : "")))
								+ (((issuesBit & 0x02)==0) ? "" : (messageSource.getMessage("other.missing.basis.of.record", null, locale) + (issuesBit > 0x03 ? "; " : "")))
								+ (((issuesBit & 0x04)==0) ? "" : (messageSource.getMessage("other.invalid.date", null, locale) + (issuesBit > 0x07 ? "; " : "")))
								+ (((issuesBit & 0x08)==0) ? "" : messageSource.getMessage("other.country.inferred", null, locale)));
	}
%>