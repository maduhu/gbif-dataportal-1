<%@ include file="/common/taglibs.jsp" %>
<%@ page language="java" pageEncoding="utf-8" contentType="text/html;charset=utf-8" %>
<%@ page import="java.util.*" %>
<%@ page import="javax.servlet.jsp.*" %>
<%@ page import="javax.servlet.jsp.jstl.core.*" %>
<%@ page import="org.apache.commons.lang.*" %>
<%@ page import="org.gbif.portal.web.filter.*" %>
<%@ page import="org.gbif.portal.web.content.filter.*" %>
<%@ page import="org.gbif.portal.web.filter.*" %>
<%@ page import="org.apache.taglibs.string.util.*" %>

<%// Filter Type Array %>
<script type="text/javascript">
	<%//the filter types for each filter - 0=string, 1=combo, 2=wizard.	%>
	var filterTypes = new Array(${fn:length(requestScope['filters'])});
	<c:forEach items="${requestScope['filters']}" var="filter" varStatus="filterStatus" begin="0">
	filterTypes[${filter.id}]=${filter.filterType};
	</c:forEach>
</script>

<%// Filter Predicates %>
<script type="text/javascript">
	var predicates = new Array(${ fn:length(requestScope['filters'])});
	<c:forEach items="${requestScope['filters']}" var="filter" varStatus="filterStatus" begin="0">
	predicates[${filter.id}] = new Array(<string:trim>
	<c:forEach items="${filter.predicates}" var="predicate" varStatus="predicateStatus" begin="0"><c:if test="${predicateStatus.index>0}">,</c:if>"<spring:message code="${predicate.value}"/>"</c:forEach>
	</string:trim>);
	</c:forEach>
</script>

<%// Filter Auto Completes %>
<script type="text/javascript">
	var autoCompleteUrls = new Array(${ fn:length(requestScope['filters'])});
	<c:forEach items="${requestScope['filters']}" var="filter" varStatus="filterStatus" begin="0">
	<c:if test="${not empty filter.autoCompleteUrl}">
		autoCompleteUrls[${filter.id}] = "${pageContext.request.contextPath}/<string:trim>${filter.autoCompleteUrl}</string:trim>";
	</c:if>	
	</c:forEach>
</script>

<%// Filter Help %>
<script type="text/javascript">
	var helpViews = new Array(${ fn:length(requestScope['filters'])});
	<c:forEach items="${requestScope['filters']}" var="filter" varStatus="filterStatus" begin="0">
	<c:if test="${not empty filter.helpView}">
		helpViews[${filter.id}] = "${pageContext.request.contextPath}<string:trim>${filter.helpView}</string:trim>";
	</c:if>	
	</c:forEach>
</script>

<%// Filter Wizard Default values %>
<script type="text/javascript">
	var defaultWizardValues = new Array(${ fn:length(requestScope['filters'])});
	var defaultWizardDisplayValues = new Array(${ fn:length(requestScope['filters'])});
		
	<c:forEach items="${requestScope['filters']}" var="filter" varStatus="filterStatus" begin="0">
	
	<%			
	FilterDTO filter = (FilterDTO) pageContext.findAttribute("filter");
	if(filter.getFilterHelper()!=null){
		FilterHelper fh = filter.getFilterHelper();
		if(fh.getDefaultValue(request)!=null){
			out.print("defaultWizardValues["+filter.getId()+"] = \""+fh.getDefaultValue(request)+"\";");
			out.print("defaultWizardDisplayValues["+filter.getId()+"] = \""+fh.getDefaultDisplayValue(request)+"\";");			
		}
	}
	%>	
	</c:forEach>
</script>




<%// Filter Dropdowns  %>
<script type="text/javascript">
	var dropDownIds = new Array(${ fn:length(requestScope['filters']) });
	<string:trim>
	<c:forEach items="${requestScope['filters']}" var="filter" varStatus="filterStatus" begin="0">
		<c:if test="${filter.dropDownValues!=null || filter.picklistHelper!=null}">
	dropDownIds[${filter.id}] = new Array(<string:trim>
<%			
	FilterDTO filter = (FilterDTO) pageContext.findAttribute("filter");
	if(filter.getDropDownValues()==null)
		filter.setDropDownValues(filter.getPicklistHelper().getPicklist(request, request.getLocale()));
	Iterator ddIter = filter.getDropDownValues().keySet().iterator();
	while(ddIter.hasNext()){
		String element = (String) ddIter.next();
		pageContext.getOut().print("'"+element+"'");
		if(ddIter.hasNext())
			pageContext.getOut().print(",");
	}
%>					
		</string:trim>);
		</c:if>
	</c:forEach>
	</string:trim>	
	
	<% //drop down values %>
	var dropDownValues = new Array(dropDownIds.length);
	<string:trim>
	<c:forEach items="${requestScope['filters']}" var="filter" varStatus="filterStatus" begin="0">
		<c:if test="${filter.dropDownValues!=null}">
	dropDownValues[${filter.id}] = new Array(<string:trim>
<%

     FilterDTO filter = (FilterDTO) pageContext.findAttribute("filter");
			if(filter.getDropDownValues()==null){
				filter.setDropDownValues(filter.getPicklistHelper().getPicklist(request, request.getLocale()));
			}
			Collection dropDownValues = filter.getDropDownValues().values();
			pageContext.setAttribute("dropDownValues", dropDownValues);		
%>	
			<c:forEach items="${dropDownValues}" var="dropDownValue" varStatus="dropDownValueStatus" begin="0">
<%
   try{
      String dropDownValue = (String) pageContext.getAttribute("dropDownValue");
      dropDownValue = dropDownValue.replaceAll("([\"'])", "\\\\$1");
      pageContext.setAttribute("dropDownValue", dropDownValue);
   } catch(Exception e){
	e.printStackTrace();
   }
%>
				<c:if test="${dropDownValueStatus.index>0}">,</c:if>"<string:truncateNicely lower="35" upper="40"><spring:message code="${dropDownValue}" text="${dropDownValue}"/></string:truncateNicely>"
			</c:forEach>		
		</string:trim>);
		</c:if>
	</c:forEach>
	</string:trim>
</script>	