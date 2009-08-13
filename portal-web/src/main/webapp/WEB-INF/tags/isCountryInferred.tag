<%@ include file="/common/taglibs.jsp"%>
<%@ attribute name="issuesBit" required="true" rtexprvalue="true" type="java.lang.Integer" %>
<%
	//no support in JSP EL for bit-wise operators has resulted in this....
	request.setAttribute("countryInferred", (issuesBit!=null && (issuesBit.intValue() & 0x08) !=0));
%>