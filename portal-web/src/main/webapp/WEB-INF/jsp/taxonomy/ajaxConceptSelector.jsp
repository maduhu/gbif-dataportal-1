<%@ include file="/common/taglibs.jsp"%>
<ul>
<c:forEach items="${searchResults}" var="concept">
<li>
	<input type="checkbox" name="${concept.key}"/> <gbif:capitalize>${concept.rank}</gbif:capitalize>: ${concept.taxonName}
	<p>
	<gbiftag:taxonHierarchy concept="${concept}"/>
	</p>
</li>	
</c:forEach>
</ul>