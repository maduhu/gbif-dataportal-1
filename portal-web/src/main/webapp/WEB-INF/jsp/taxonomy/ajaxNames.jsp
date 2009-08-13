<%@ include file="/common/taglibs.jsp"%><c:forEach items="${searchResults}" var="name"><gbif:capitalizeFirstChar>${name}</gbif:capitalizeFirstChar>
</c:forEach>