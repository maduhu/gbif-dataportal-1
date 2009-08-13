<%@ include file="/common/taglibs.jsp"%>
<c:if test="${not empty imageResults}">
<h4><spring:message code="image.overview"/></h4>
<display:table
	name="imageResults" 
	class="results" 
	uid="imageRecord" 
	sort="external"
	defaultsort="1"
>
  <display:column titleKey="data.provider">
  	<a href="${pageContext.request.contextPath}/datasets/provider/${imageRecord.dataProviderKey}">${imageRecord.dataProviderName}</a>
  </display:column>
  <display:column titleKey="dataset">
  	<a href="${pageContext.request.contextPath}/datasets/resource/${imageRecord.dataResourceKey}">${imageRecord.dataResourceName}</a>
  </display:column>
  <display:column titleKey="catalogue.number">
    <c:if test="${imageRecord.occurrenceRecordKey!=null}">
  		<a href="${pageContext.request.contextPath}/occurrences/${imageRecord.occurrenceRecordKey}">${imageRecord.occurrenceRecordCatalogueNumber}</a>
  	</c:if>
  </display:column>
  <display:column property="description" titleKey="description"/>
  <display:column titleKey="image">
	<div class="taxonImage">
		<% // is there a html to display, else need to scale image %>
		<c:choose>
			<c:when test="${not empty imageRecord.htmlForDisplay}">
					${imageRecord.htmlForDisplay}
			</c:when>
			<c:when test="${not empty imageRecord.url}">
				<c:choose>
					<c:when test="${imageRecord.imageType>1}">
						<c:choose>
							<c:when test="${fn:endsWith(imageRecord.url, 'mpg') || fn:endsWith(imageRecord.url, 'mpeg') }">
								<embed src="${imageRecord.url}" autostart="true" controller="true" controls="console" />
							</c:when>
							<c:when test="${imageRecord.url!=null && imageRecord.url!= 'NULL'}">			
								<a href="${imageRecord.url}"><gbiftag:scaleImage imageUrl="${imageRecord.url}" maxWidth="300" maxHeight="200" addLink="false"/></a>
							</c:when>					
						</c:choose>
					</c:when>
					<c:otherwise>
						<a href="${imageRecord.url}">${imageRecord.url}</a>
					</c:otherwise>
				</c:choose>	
			</c:when>		
		</c:choose>	
	</div>
  </display:column>
</display:table>
	
</c:if>