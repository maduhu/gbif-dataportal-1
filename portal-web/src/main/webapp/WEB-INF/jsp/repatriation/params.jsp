<%@ include file="/common/taglibs.jsp"%>
<c:set var="countrySelected" scope="request" value="${not empty param['country'] && param['country']!='all'}"/>
<c:set var="hostSelected" scope="request" value="${not empty param['host'] && param['host']!='all'}"/>
<c:set var="showFlags" scope="request" value="${(empty param['hideFlags'] || !param['hideFlags']) && param['view']!='full'}"/>
<c:set var="showIso" scope="request" value="${empty param['view'] || param['view']=='iso' || param['view']=='concise' || param['view']=='percent'}"/>
<c:set var="selectedView" scope="request" value="${not empty param['view'] ? param['view'] : 'concise'}"/>