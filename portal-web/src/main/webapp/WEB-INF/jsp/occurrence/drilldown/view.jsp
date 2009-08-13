<%@ include file="/common/taglibs.jsp"%>
<tiles:insert page="occurrenceHeader.jsp"/>
<div id="occurrenceDrillDown" class="occurrenceContainer">
	<c:if test="${not empty occurrenceRecord && !rawOnly}">
		<tiles:insert page="actions.jsp"/>	
	</c:if>
	<c:set var="noIssues" value="${!rawOnly && occurrenceRecord.taxonomicIssue==0 && occurrenceRecord.geospatialIssue==0 && occurrenceRecord.otherIssue==0}"/>
	<c:if test="${!noIssues}">	
		<div id="warnings" class="topContainer">
			<h4><spring:message code="taxonconcept.drilldown.warnings.title"/></h4>
			<p>
			<c:if test="${not empty occurrenceRecord}">
				<gbiftag:formatGeospatialIssues issuesBit="${occurrenceRecord.geospatialIssue}" messageSource="${messageSource}" locale="${request.getLocale}"/>
				<c:if test="${not empty geospatialIssueText}">
					 <spring:message code="geospatial.issues"/>: ${geospatialIssueText}<br/>
				</c:if>	
				<gbiftag:formatOtherIssues issuesBit="${occurrenceRecord.otherIssue}" messageSource="${messageSource}" locale="${request.getLocale}"/>
				<c:if test="${not empty otherIssueText}">
					 <spring:message code="other.issues"/>: ${otherIssueText}<br/>
				</c:if>	
				<gbiftag:formatTaxonomicIssues issuesBit="${occurrenceRecord.taxonomicIssue}" messageSource="${messageSource}" locale="${request.getLocale}"/>
				<c:if test="${not empty taxonomicIssueText}">
					 <spring:message code="taxonomic.issues"/>: ${taxonomicIssueText}<br/>
				</c:if>
			</c:if>
			<c:if test="${empty occurrenceRecord || rawOnly}">
				 <spring:message code="occurrence.record.parse.unavailable"/><br/>
			</c:if>				
			</p>
		</div>
	</c:if>

	<div id="occurrenceDataset"<c:if test="${noIssues}"> class="topContainer"</c:if>>
		<h4><spring:message code="occurrence.record.dataset.legend"/></h4>
		<tiles:insert page="dataset.jsp"/>
	</div>	
	
	<div id="occurrenceTaxonomy">
		<h4><spring:message code="occurrence.record.taxonomy.legend"/></h4>
		<tiles:insert page="taxonomy.jsp"/>
	</div>
	
	<div id="occurrenceGeospatial">
		<h4><spring:message code="occurrence.record.geospatial.legend"/></h4>
		<tiles:insert page="geospatial.jsp"/>
	</div>
	
  <c:if test="${not empty rawOccurrenceRecord}">
     <p class="lastModified">
        <spring:message code="occurrence.record.created" text="First indexed"/>: <fmt:formatDate value="${rawOccurrenceRecord.created}"/>
     </p>        
     <p class="lastModified">     
        <spring:message code="occurrence.record.modified" text="Last indexed"/>: <fmt:formatDate value="${rawOccurrenceRecord.modified}"/>
     </p>
  </c:if>   
</div>