<%@ include file="/common/taglibs.jsp"%>
<h4><spring:message code="agents.title"/></h4>
<display:table
	name="agents" 
	class="results" 
	uid="agent" 
	sort="external"
	defaultsort="1"
>
  <display:column property="agentName" titleKey="name"/>
  <display:column titleKey="role">
		<c:choose>
			<c:when test="${agent.agentType==1}">
				<spring:message code="agents.role.administrative"/>
			</c:when>	
			<c:when test="${agent.agentType==2}">
				<spring:message code="agents.role.technical"/>
			</c:when>
		</c:choose>  	
  </display:column>
  <display:column property="agentAddress" titleKey="address" />
  <display:column titleKey="email">
		<gbiftag:emailPrint email="${agent.agentEmail}" />  
  </display:column>
  <display:column property="agentTelephone" titleKey="telephone" />
</display:table>

<p class="termsHelp">
<spring:message code="agents.role.administrative.description"/>
<br/>
<spring:message code="agents.role.technical.description"/>
</p>