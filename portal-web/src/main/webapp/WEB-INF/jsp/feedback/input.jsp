<%@ include file="/common/taglibs.jsp"%>
<div id="twopartheader">	
	<h1><spring:message code="feedback.title"/></h1>
</div>
<p>
	<spring:message code="feedback.blurb"/>
</p>
<fieldset>
	<form method="post" action="<c:url value="/feedback/submit/${type}/${conceptKey}"/>">
		<p>
			<label><spring:message code="feedback.on.page"/>:</label>
			<a href="${feedbackOnURL}">${feedbackOnURL}</a>
			<input type="hidden" name="feedbackOnURL" value="${feedbackOnURL}"/>
		</p>
		<p>
			<label><spring:message code="feedback.type"/>:</label>
			<c:choose>
				<c:when test="${type == 'taxon'}">
					<spring:message code="feedback.type.taxon"/>
				</c:when>
				<c:when test="${type == 'resource'}">
					<spring:message code="feedback.type.resource"/>
				</c:when>
				<c:when test="${type == 'occurrence'}">
					<spring:message code="feedback.type.occurrence"/>
				</c:when>
				<c:otherwise>
					<spring:message code="feedback.type.unknown"/>
				</c:otherwise>
			</c:choose>
		</p>
		<c:if test="${dataProvider!=null}">
			<p>
				<label><spring:message code="feedback.data.provider.name"/>:</label>
				${dataProvider.name}
			</p>
			<input type="hidden" name="dataProviderKey" value="${dataProvider.key}"/>
		</c:if>
		<c:if test="${dataResource!=null}">
			<p>
				<label><spring:message code="feedback.data.resource.name"/>:</label>		
				${dataResource.name}
				<input type="hidden" name="dataResourceKey" value="${dataResource.key}"/>
			</p>
			<c:forEach items="${dataResourceAgents}" var="agent">
				<p>
					<label> </label> - ${agent.agentName} (${agent.agentEmail})
				</p>
			</c:forEach>
		</c:if>
		<c:if test="${taxonConcept!=null}">
			<p>
				<label><spring:message code="feedback.taxon.scientific.name"/>:</label>
				${taxonConcept.taxonName}
			</p>
			<input type="hidden" name="taxonConceptKey" value="${taxonConcept.key}"/>
		</c:if>
		<c:if test="${occurrenceRecord!=null}">
			<p>
				<label><spring:message code="feedback.occurrence.institution.code"/>:</label>
				${occurrenceRecord.institutionCode}
			</p>
			<p>
				<label><spring:message code="feedback.occurrence.collection.code"/>:</label>
				${occurrenceRecord.collectionCode}
			</p>
			<p>
				<label><spring:message code="feedback.occurrence.catalogue.number"/>:</label>
				${occurrenceRecord.catalogueNumber}
			</p>
			<input type="hidden" name="occurrenceRecordKey" value="${occurrenceRecord.key}"/>
		</c:if>
		<p>
			<label><spring:message code="feedback.submitter.name"/>:</label>
			<input type='text' class="feedback" name='submitterName'/>
		</p>
		<p>
			<label><spring:message code="feedback.submitter.email"/>:</label>
			<input type='text' class="feedback" name='submitterEmail'/>
		</p>
		<p>
			<label><spring:message code="feedback.details"/>:</label>
			<textarea class="feedback" cols="30" rows="6" name='feedbackDetails'></textarea>
		</p>
		<p>
			<input type="submit" value='<spring:message code="feedback.submit"/>'/>
		</p>
	</form>
</fieldset>