<%@ include file="/common/taglibs.jsp"%>
<div class="otherDetailsFilterHelp">
<c:set var="availableOpt"><spring:message code="imageurl.with.url"/></c:set>
<spring:message code="occurrence.imageurl.help" arguments="${availableOpt}" argumentSeparator="$$$"/>
</div>