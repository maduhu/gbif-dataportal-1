<%@ include file="/common/taglibs.jsp"%>
<%@ attribute name="resourceNetwork" required="true" rtexprvalue="true" type="org.gbif.portal.dto.resources.ResourceNetworkDTO"%>
${resourceNetwork.name} <c:if test="${not empty resourceNetwork.code && resourceNetwork.code!=resourceNetwork.name}">(${resourceNetwork.code})</c:if>