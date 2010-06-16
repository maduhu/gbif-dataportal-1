<%@ include file="/common/taglibs.jsp"%>
<div id="twopartheader">
	<h2><spring:message code="occurrence.record.details.title"/>
		<span class="subject">
			<c:choose>
			<c:when test="${not empty occurrenceRecord}">
				${occurrenceRecord.institutionCode} 
				${occurrenceRecord.collectionCode} 
				${occurrenceRecord.catalogueNumber}
			</c:when>
			<c:otherwise>
				${rawOccurrenceRecord.key}
			</c:otherwise>
			</c:choose>
		</span>
	</h2>
	<h3>
		<span class="genera">${occurrenceRecord.taxonName}</span>
	</h3>
</div>