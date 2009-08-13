<%@ include file="/common/taglibs.jsp"%>
<div id="twopartheader">
	<h2>
		Name Mapping Tool
	</h2>
	<h3>
		Use this tool to map a set of scientific or common names to a GBIF ID
	</h3>
</div>

<form name="export" method="post" action="${pageContext.request.contextPath}/species/name-ids.txt?export=true">
	Click here to download a tab delimited list of name id pairs <input type="submit" "value="Download tab file"/>

<display:table 
	name="names" 
	class="results" 
	uid="name" 
	requestURI="/species/downloadMapping.htm?"
	sort="external"
	defaultsort="1"
	size="resultSize"
	export="false"
>
  <display:column title="Supplied name">
  	<c:choose>
  		<c:when test="${fn:length(name)>1}">  
				<span style="color:#FF6600; font-weight: bold">
			</c:when>	
  		<c:when test="${fn:length(name)==1}">  
				<span style="color:#006600; font-weight: bold">  		
  		</c:when>
  		<c:otherwise>
				<span style="color:#FF0000; font-weight: bold">  					
  		</c:otherwise>
  	</c:choose>				
			${suppliedNames[name_rowNum-1]}
		  </span>	
		<input type="hidden" name="supplied-name-${name_rowNum}" value="${suppliedNames[name_rowNum-1]}"/>  		
  </display:column>	
  <display:column title="ID">
  	<c:choose>
  		<c:when test="${fn:length(name)>1}">
  			<c:forEach items="${name}" var="nameItem">
					<a href="${pageContext.request.contextPath}/species/${nameItem.key}">${nameItem.key}</a>	
					<br/>
					<br/>											  			
  			</c:forEach>
			</c:when>
			<c:otherwise>	
				<a href="${pageContext.request.contextPath}/species/${name[0].key}">${name[0].key}</a>		
			</c:otherwise>
		</c:choose>
  </display:column>	
  <display:column title="Matched taxon">
		<!-- the supplied name -->  
  	<c:choose>
  		<c:when test="${fn:length(name)>1}">
  			<c:forEach items="${name}" var="nameItem" varStatus="nameItemStatus">
					<input type="radio" name="systemId-${name_rowNum}" value="${nameItem.key}" <c:if test="${nameItemStatus.index==0}">checked="true"</c:if>/>
					<span style="color:#FF6600; font-weight: bold">
						<gbif:capitalize>${nameItem.rank}</gbif:capitalize><c:if test="${not empty nameItem}">:	</c:if>
						<gbif:taxonPrint concept="${nameItem}"/>			
					</span>
					<br/>
					<span style="color: #AAA; font-size: 0.9em;"><gbiftag:taxonHierarchy concept="${nameItem}"/></span>					
					<br/>
  			</c:forEach>
			</c:when>  	
  		<c:when test="${not empty name}">  
				<input type="hidden" name="systemId-${name_rowNum}" value="${name[0].key}"/>
				<span style="color:#006600; font-weight: bold">
					<gbif:capitalize>${name[0].rank}</gbif:capitalize><c:if test="${not empty name}">:	</c:if><gbif:taxonPrint concept="${name[0]}"/>			
				</span>
				<br/>
				<span style="color: #AAA; font-size: 0.9em ;"><gbiftag:taxonHierarchy concept="${name[0]}"/></span>
			</c:when>
			<c:otherwise>
				<span style="color:#FF0000; font-weight: bold">Name not recognised</span>
			</c:otherwise>
		</c:choose>	
  </display:column>	  
  <display:setProperty name="basic.msg.empty_list"><spring:message code="occurrence.search.filter.nonefound"/></display:setProperty>	  
  <display:setProperty name="paging.banner.onepage"> </display:setProperty>	  
  <display:setProperty name="basic.empty.showtable">true</display:setProperty>	 
  <display:setProperty name="basic.msg.empty_list_row">
  	<tr class="empty">
		<td colspan="13">No names supplied</td>
	</tr>
	</tr>
  </display:setProperty>	  
  <display:setProperty name="paging.banner.no_items_found"> </display:setProperty>	  
  <display:setProperty name="pagination.pagenumber.param">pageno</display:setProperty>
</display:table>

</form>