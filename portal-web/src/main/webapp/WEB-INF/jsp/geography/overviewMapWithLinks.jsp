<%@ include file="/common/taglibs.jsp"%>
<c:set var="urlRoot" scope="request"><tiles:getAsString name="urlRoot"/></c:set>
<c:choose>
	<c:when test="${zoom>1}">
		<div id="overviewContainer">
			<div id="globalOverviewHeader">&nbsp;</div>
			<div id="globalOverviewMap">
				<tiles:insert name="smallOverviewMap"/>
			</div>	
			<div id="globalOverviewLinks">
				 <tiles:insert name="mapLinks"/>
			</div>
			<div id="globalOverviewFooter">&nbsp;</div>			
		</div>
	</c:when>
	<c:otherwise>
		<div id="globalOverviewContainer">		
			 <tiles:insert name="mapLinks"/>
		</div> 
	</c:otherwise>
</c:choose>