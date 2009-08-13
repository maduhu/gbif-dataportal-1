<%@ include file="/common/taglibs.jsp"%>
<%@ page import="java.util.*" %>
<%@ page import="javax.servlet.jsp.*" %>
<%@ page import="javax.servlet.jsp.jstl.core.*" %>
<%@ page import="org.apache.commons.lang.*" %>
<%@ page import="org.gbif.portal.web.filter.*" %>
<%@ page import="org.apache.taglibs.string.util.*" %>
<%
		List<FilterDTO> filters = (List<FilterDTO>) request.getAttribute("filters");
		pageContext.setAttribute("filters", filters);		 
%>		
<%
	String currFilterCategory =null;
%>			

<c:choose>
	<c:when test="${not empty param['newFilterSubject']}">
		<c:set var="filterSubject" value="${param['newFilterSubject']}"/>
	</c:when>
	<c:otherwise>
		<c:set var="filterSubject" value="0"/>	
	</c:otherwise>	
</c:choose>

<select name="c[0].s" id="newFilterSubject" tabindex="5"
				onchange="javascript:changeDropdownValues(this,'p', predicates);changeObjectInput(this);"  
				onKeyUp="javascript:changeDropdownValues(this,'p', predicates);changeObjectInput(this);">
    <c:forEach items="${filters}" var="filter">
		<% 
			FilterDTO thisFilter = (FilterDTO) pageContext.findAttribute("filter");							
			String filterCategory = thisFilter.getCategory();
			boolean newCategory = false;
			if(StringUtils.isNotEmpty(filterCategory)){
			  newCategory = !filterCategory.equals(currFilterCategory);
				currFilterCategory = filterCategory;
				pageContext.setAttribute("filterCategory", filterCategory);									
			}
			pageContext.setAttribute("newCategory", newCategory);	
		%>
    	<c:if test="${newCategory && filterCategory!=null}"></optgroup></c:if>							
    	<c:if test="${newCategory}"><optgroup label="<spring:message code="${filter.category}"/>"></c:if>
			<% //get category, if its change add optgroup, set changed to true	%>
			<c:if test="${filter.editable}">
        <option value="${filter.id}"<c:if test="${filterSubject==filter.id}"> selected="true"</c:if>>
            <spring:message code="${filter.displayName}"/>
        </option>	  
      </c:if>               
    </c:forEach>
    </optgroup>
</select>

<c:if test="${filterSubject==0}">
<script type="text/javascript">
	var filterDropdown = document.getElementById('newFilterSubject');
	filterDropdown.selectedIndex=0;
</script>
</c:if>