<%@ include file="/common/taglibs.jsp"%>
<%@ attribute name="issuesBit" required="true" rtexprvalue="true" type="java.lang.Integer" %>
<%@ attribute name="messageSource" required="true" rtexprvalue="true" type="org.springframework.context.MessageSource" %>
<%@ attribute name="locale" required="true" rtexprvalue="true" type="java.util.Locale" %>
<%

try{
	if (issuesBit==0) {
		request.setAttribute("geospatialIssueText",null);
	} else {
		//no support in JSP EL for bit-wise operators has resulted in this....
		request.setAttribute("geospatialIssueText", 
								(((issuesBit & 0x01)==0) ? "" : (messageSource.getMessage("geospatial.negated.latitude", null, locale) + (issuesBit > 0x01 ? "; " : "")))
								+ (((issuesBit & 0x02)==0) ? "" : (messageSource.getMessage("geospatial.negated.longitude", null, locale) + (issuesBit > 0x03 ? "; " : "")))
								+ (((issuesBit & 0x04)==0) ? "" : (messageSource.getMessage("geospatial.inverted", null, locale) + (issuesBit > 0x07 ? "; " : "")))
								+ (((issuesBit & 0x08)==0) ? "" : (messageSource.getMessage("geospatial.zero.coordinates", null, "coordinates supplied as (0.0, 0.0)", locale) + (issuesBit > 0x0F ? "; " : "")))
								+ (((issuesBit & 0x10)==0) ? "" : (messageSource.getMessage("geospatial.out.of.range", null, locale) + (issuesBit > 0x1F ? "; " : "")))
								+ (((issuesBit & 0x20)==0) ? "" : messageSource.getMessage("geospatial.mismatch", null, locale)	)
					);
	}
} catch(Exception e){
	e.printStackTrace();
	
}
%>