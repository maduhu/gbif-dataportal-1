<%@ include file="/common/taglibs.jsp"%>
<c:if test="${dataProvider!=null}">${dataProvider.name}</c:if>
<c:if test="${dataResource!=null}">: ${dataResource.name}</c:if>
<c:if test="${resourceNetwork!=null}"><gbiftag:resourceNetworkPrint resourceNetwork="${resourceNetwork}"/></c:if>