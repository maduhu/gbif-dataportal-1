<%@ include file="/common/taglibs.jsp"%>
<%@ page import="java.util.*" %>
<%@ page import="javax.servlet.jsp.*" %>
<%@ page import="javax.servlet.jsp.jstl.core.*" %>
<%@ page import="org.apache.commons.lang.*" %>
<%@ page import="org.gbif.portal.web.filter.*" %>
<%@ page import="org.apache.taglibs.string.util.*" %>
<h4 id="addSearchFilter"><spring:message code="search.filter.editor.title" text="Add search filter"/></h4>
<div id="filters">
	<div class="filter">
	
		<script type="text/javascript">
			loadingWizardText="<spring:message code="filter.wizard.loading"/>"; 
			wizardSetupUrl = "${pageContext.request.contextPath}${rootWizardUrl}";
			defaultWizardValue = "<spring:message code="filter.wizard.nullvalue"/>";			
		</script>	
	
		<span id="subject" class="type">
			<tiles:insert page="/WEB-INF/jsp/filters/filterDropDown.jsp"/>
		</span>
		
		<span id="predicate" class="predicate">						
			<select id="newFilterPredicate" name="c[0].p" tabindex="6">
				<%
					List<FilterDTO> filters = (List<FilterDTO>) request.getAttribute("filters");				
					FilterDTO filterDTO = FilterUtils.getFilterById(filters, "0");											
					pageContext.setAttribute("filter", filterDTO);
					pageContext.setAttribute("predicates", filterDTO.getPredicates());
				%>
        <c:forEach items="${predicates}" var="predicate">
            <option value="${predicate.id}"><spring:message code="${predicate.value}"/></option>
        </c:forEach>
			</select>
		</span>
		
		<span id="newFilterValue" class="value">
			<input id="statesinput" class="statesinput" name="c[0].o"  value="" tabindex="7"/>
			<c:if test="${not empty filter.autoCompleteUrl}">
				<div id="statescontainer" class="statescontainer"></div>
				<gbiftag:autoComplete url="${pageContext.request.contextPath}/${filter.autoCompleteUrl}" inputId="statesinput" containerId="statescontainer"/>
			</c:if>	
		</span>	
		<span class="submitFilter">
			<input type="submit" tabindex="8" class="addFilterSubmit" onClick="javascript:addConstructedFilter();" value="<spring:message code="search.filter.add.filter"/>"/>
		</span>	
		
		<script>
			<string:trim>
				<gbiftag:ieCheck/>
				<c:choose>	
				<c:when test="${isMSIE}">
					document.getElementById('statesinput').attachEvent("onkeypress", addFilter);		
				</c:when>
				<c:otherwise>
					document.getElementById("statesinput").addEventListener('keypress', addFilter, false);
				</c:otherwise>
				</c:choose>					
					document.getElementById("statesinput").focus();
			</string:trim>	
		</script>
		<br/>
	</div>	
</div>		
<div id="selectedFilterHelp"></div>
<div id="filterWizard">&nbsp;</div>
<script>
	var currValue = "${param['currValue']}";
	var theSubjectSelect = document.getElementById('newFilterSubject');
	changeDropdownValues(theSubjectSelect,'p', predicates);
	changeObjectInput(theSubjectSelect);
</script>