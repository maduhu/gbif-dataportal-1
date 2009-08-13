<%@ include file="/common/taglibs.jsp"%>
<fieldset class="resourceForm">

<c:if test="${not empty readonlyProperties}">
	<p class="resourceNotes">
	Note: Fields marked with <span class="info"><spring:message code="registration.metdata.supplied"/></span>
	are retrieved from the metadata request.<br/>
	These fields are readonly and can only be updated by the provider updating the details supplied in the metadata
	request for the ${resource.resourceType} endpoint.
	</p>
</c:if>

<!-- hidden values -->								
<spring:bind path="resource.serviceKey">						
	<input type="hidden" name="${status.expression}" value="${resource.serviceKey}"/>
</spring:bind>
<p>
	<spring:bind path="resource.name">												
		<label for="${status.expression}"><spring:message code="registration.resource.name" text="Name"/></label>
		<c:choose>
			<c:when test="${not empty resource.name || !editableEndpoint}">
				${status.value}
				<input type="hidden" name="${status.expression}" value="${status.value}"/>		
			</c:when>		
			<c:otherwise>
				<input type="text" name="${status.expression}" value="${status.value}"/>	
			</c:otherwise>		
		</c:choose>		
		<gbiftag:resourceDetailCheck 
			readonlyProperties="${readonlyProperties}" 
			updatedProperties="${updatedProperties}" 
			propertyName="name" errorMessage="${status.errorMessage}"/>
	</spring:bind>								
</p>
<p>
	<spring:bind path="resource.code">												
		<label for="${status.expression}"><spring:message code="registration.resource.code" text="Code"/></label>
		<c:choose>
			<c:when test="${not empty resource.code || !editableEndpoint}">
				${resource.name}
				<input type="hidden" name="${status.expression}" value="${status.value}"/>		
			</c:when>		
			<c:otherwise>
				<input type="text" name="${status.expression}" value="${status.value}"/>	
			</c:otherwise>		
		</c:choose>			
		<gbiftag:resourceDetailCheck 
			readonlyProperties="${readonlyProperties}" 
			updatedProperties="${updatedProperties}" 
			propertyName="code" errorMessage="${status.errorMessage}"/>
	</spring:bind>								
</p>
<p>
	<spring:bind path="resource.resourceType">												
		<label for="${status.expression}"><spring:message code="registration.resource.name" text="Resource Type"/></label>
		<c:set var="selectedResourceType" value="${not empty status.value ? status.value : param['resourceType']}"/>
		<c:choose>
			<c:when test="${!editableEndpoint}">
				<spring:message code="registration.resource.${selectedResourceType}" text="${selectedResourceType}"/>
				<input id="${status.expression}" name="${status.expression}" type="hidden" value="${selectedResourceType}"/>
			</c:when>
			<c:otherwise>
				<select id="${status.expression}" name="${status.expression}" onchange="javascript:checkResourceType(this);" <c:if test="${!editableEndpoint}">readonly="true"</c:if>>
					<c:forEach items="${resourceTypes}" var="resourceType">
				    <option value="${resourceType}"<c:if test="${selectedResourceType == resourceType}"> selected="true"</c:if>>		
				    	<spring:message code="registration.resource.${resourceType}" text="${resourceType}"/>
				    </option>				
					</c:forEach>
				</select>		
			</c:otherwise>	
		</c:choose>			
		<gbiftag:resourceDetailCheck 
			readonlyProperties="${readonlyProperties}" 
			updatedProperties="${updatedProperties}" 
			propertyName="resourceType" errorMessage="${status.errorMessage}"/>
	</spring:bind>								
</p>
<p>
	<c:set var="url" value="${not empty param['resourceUrl'] ? param['resourceUrl'] : resource.accessPoint}"/>
	<spring:bind path="resource.accessPoint">												
		<label for="${status.expression}"><spring:message code="registration.resource.name" text="Access point"/></label>
		<c:choose>
			<c:when test="${not empty resource.name || !editableEndpoint}">
				<c:if test="${fn:startsWith(url, 'http://')}"><a href="${url}" target="_blank"></c:if>${url}<c:if test="${fn:startsWith(url, 'http://')}"></a></c:if>
				<input type="hidden" name="${status.expression}" value="${url}"/>		
			</c:when>		
			<c:otherwise>
				<input type="text" name="${status.expression}" value="${url}"/>
			</c:otherwise>		
		</c:choose>				
		<gbiftag:resourceDetailCheck 
			readonlyProperties="${readonlyProperties}" 
			updatedProperties="${updatedProperties}" 
			propertyName="accessPoint" errorMessage="${status.errorMessage}"/>
	</spring:bind>								
</p>
<p>
		<spring:bind path="resource.description">
			<label for="${status.expression}"><spring:message code="registration.resource.description"/></label>
			<textarea name="${status.expression}" rows="5" <c:if test="${fn:contains(readonlyProperties, 'description')}">readonly="true"</c:if>>${status.value}</textarea>
		<gbiftag:resourceDetailCheck 
			readonlyProperties="${readonlyProperties}" 
			updatedProperties="${updatedProperties}" 
			propertyName="description" errorMessage="${status.errorMessage}"/>
		</spring:bind>	
</p>
<p>
		<spring:bind path="resource.rights">
			<label for="${status.expression}"><spring:message code="registration.resource.rights" text="Rights"/></label>
			<textarea class="textareaSmall" name="${status.expression}" <c:if test="${fn:contains(readonlyProperties, 'right')}">readonly="true"</c:if>>${status.value}</textarea>
			<gbiftag:resourceDetailCheck 
				readonlyProperties="${readonlyProperties}" 
				updatedProperties="${updatedProperties}" 
				propertyName="rights" errorMessage="${status.errorMessage}"/>
		</spring:bind>	
</p>
<p>
		<spring:bind path="resource.citation">
			<label for="${status.expression}"><spring:message code="registration.resource.citation" text="Citation"/></label>
			<textarea class="textareaSmall" name="${status.expression}" <c:if test="${fn:contains(readonlyProperties, 'citation')}">readonly="true"</c:if>>${status.value}</textarea>
			<gbiftag:resourceDetailCheck 
				readonlyProperties="${readonlyProperties}" 
				updatedProperties="${updatedProperties}" 
				propertyName="citation" errorMessage="${status.errorMessage}"/>
		</spring:bind>	
</p>
<p>
		<spring:bind path="resource.recordCount">
			<label for="${status.expression}"><spring:message code="registration.resource.recordCount" text="Record count"/></label>
			<input type="text" name="${status.expression}" value="${status.value}" <c:if test="${fn:contains(readonlyProperties, 'recordCount')}">readonly="true"</c:if>/>
			<gbiftag:resourceDetailCheck 
				readonlyProperties="${readonlyProperties}" 
				updatedProperties="${updatedProperties}" 
				propertyName="recordCount" errorMessage="${status.errorMessage}"/>
		</spring:bind>	
</p>
<p>		
		<spring:bind path="resource.ownerName">
			<label for="${status.expression}"><spring:message code="registration.resource.owner.name"/></label>
			<input type="text" name="${status.expression}" value="${status.value}" <c:if test="${fn:contains(readonlyProperties, 'ownerName')}">readonly="true"</c:if>/>
			<gbiftag:resourceDetailCheck 
				readonlyProperties="${readonlyProperties}" 
				updatedProperties="${updatedProperties}" 
				propertyName="ownerName" errorMessage="${status.errorMessage}"/>
		</spring:bind>											
</p>	
<p>		
		<spring:bind path="resource.ownerAddress">
			<label for="${status.expression}"><spring:message code="registration.resource.owner.address"/></label>
			<input type="text" name="${status.expression}" value="${status.value}" <c:if test="${fn:contains(readonlyProperties, 'ownerAddress')}">readonly="true"</c:if>/>
			<gbiftag:resourceDetailCheck 
				readonlyProperties="${readonlyProperties}" 
				updatedProperties="${updatedProperties}" 
				propertyName="ownerAddress" errorMessage="${status.errorMessage}"/>
		</spring:bind>											
</p>													
<p>		
		<spring:bind path="resource.ownerCountry">
			<label for="${status.expression}"><spring:message code="registration.resource.owner.country"/></label>
			<select id="${status.expression}" name="${status.expression}" <c:if test="${fn:contains(readonlyProperties, 'ownerCountry')}">readonly="true"</c:if>>
					<option value="N/A"><spring:message code="registration.not.specified"/></option>
				<c:forEach items="${countries}" var="country">
					<option value="${country.isoCountryCode}" <c:if test="${country.isoCountryCode == status.value}">selected="true"</c:if>>
						<string:capitalize><string:lowerCase>${country.name}</string:lowerCase></string:capitalize>
					</option>
				</c:forEach>
			</select>											
			<gbiftag:resourceDetailCheck 
				readonlyProperties="${readonlyProperties}" 
				updatedProperties="${updatedProperties}" 
				propertyName="ownerCountry" errorMessage="${status.errorMessage}"/>
		</spring:bind>											
</p>									
<p>
		<spring:bind path="resource.website">
			<label for="${status.expression}"><spring:message code="registration.resource.website" text="Website"/></label>
			<input type="text" name="${status.expression}" value="${status.value}" <c:if test="${fn:contains(readonlyProperties, 'website')}">readonly="true"</c:if>/>
			<gbiftag:resourceDetailCheck 
				readonlyProperties="${readonlyProperties}" 
				updatedProperties="${updatedProperties}" 
				propertyName="website" errorMessage="${status.errorMessage}"/>
		</spring:bind>	
</p>
<p>		
		<spring:bind path="resource.recordBasis">
			<label for="${status.expression}"><spring:message code="registration.resource.owner.resource.networks" text="Record basis"/></label>
			<c:choose>
				<c:when test="${fn:contains(readonlyProperties, 'recordBasis') || (not empty resource.recordBasis && !fn:contains(basesOfRecord, resource.recordBasis))}">
					<input type="text" readonly="true" value="${resource.recordBasis}"/>
				</c:when>
				<c:otherwise>
					<select id="${status.expression}" name="${status.expression}">
						<option value="N/A"><spring:message code="registration.not.specified"/></option>					
						<c:forEach items="${basesOfRecord}" var="basisOfRecord">
							<option value="${basisOfRecord}" <c:if test="${basisOfRecord == status.value}">selected="true"</c:if>>
								<spring:message code="basis.of.record.${basisOfRecord}" text="basisOfRecord"/>
							</option>
						</c:forEach>
					</select>											
				</c:otherwise>			
			</c:choose>
			<gbiftag:resourceDetailCheck 
				readonlyProperties="${readonlyProperties}" 
				updatedProperties="${updatedProperties}" 
				propertyName="recordBasis" errorMessage="${status.errorMessage}"/>
		</spring:bind>											
</p>	
<p>		
	<fieldset id="resourceNetworkMemberships" class="subFieldset">
	<legend><spring:message code="registration.resource.owner.resource.networks" text="Resource Networks"/></legend>
		<spring:bind path="resource.resourceNetworks">
			<table>
				<tr><td>&nbsp;</td><td>&nbsp;</td><td><span class="help">Selected resource networks</span><td>
				</tr>
				<tr>
					<td>
						<select id="resourceNetworkList${resourceIndex}" class="relatedCountries" size="6">
							<c:forEach items="${resourceNetworks}" var="resourceNetwork" varStatus="resourceNetworkStatus">
								<c:if test="${!fn:contains(status.value, resourceNetwork.key)}">
									<option value="${resourceNetwork.key}">
										<string:capitalize><string:lowerCase>${resourceNetwork.value}</string:lowerCase></string:capitalize>
									</option>
								</c:if>	
							</c:forEach>
						</select>									
					</td>		
					<td>	
						<p>
							<a href="javascript:addSelectedOption('resourceNetworkList${resourceIndex}','selectedResourceNetworks${resourceIndex}');resetHidden('selectedResourceNetworks${resourceIndex}', '${status.expression}');">Add</a>
						</p>
						<p>	
							<a href="javascript:addSelectedOption('selectedResourceNetworks${resourceIndex}','resourceNetworkList${resourceIndex}');resetHidden('selectedResourceNetworks${resourceIndex}', '${status.expression}');">Remove</a>
						</p>		
					</td>
					<td>
						<select id="selectedResourceNetworks${resourceIndex}" class="relatedCountries" size="6">
							<c:forEach items="${resourceNetworks}" var="resourceNetwork" varStatus="resourceNetworkStatus">
								<c:if test="${fn:contains(status.value, resourceNetwork.key)}">
									<option value="${resourceNetwork.key}">
										<string:capitalize><string:lowerCase>${resourceNetwork.value}</string:lowerCase></string:capitalize>
									</option>
								</c:if>	
							</c:forEach>
						</select>			
						<input type="hidden" id="${status.expression}" name="${status.expression}"						
							value="<string:trim><c:forEach items="${resourceNetworks}" var="resourceNetwork" varStatus="resourceNetworkStatus"><c:if test="${fn:contains(status.value, resourceNetwork.key)}">${resourceNetwork.key},</c:if></c:forEach></string:trim>"/>
					</td>			
				</tr>
			</table>				
			<gbiftag:resourceDetailCheck 
				readonlyProperties="${readonlyProperties}" 
				updatedProperties="${updatedProperties}" 
				propertyName="resourceNetworks" errorMessage="${status.errorMessage}"/>
		</spring:bind>
	</fieldset>													
</p>
<p>		
	<fieldset id="geographicCoverage" class="subFieldset">
	<legend><spring:message code="registration.resource.geographic.coverage" text="Geographic coverage"/></legend>
			<spring:bind path="resource.relatesToCountries">
				<table>
					<tr><td>&nbsp;</td><td>&nbsp;</td><td><span class="help">Selected countries</span><td>
					</tr>
					<tr>
						<td>
							<select id="countriesList${resourceIndex}" class="relatedCountries" size="10">
								<c:forEach items="${countries}" var="country" varStatus="countryStatus">
									<c:if test="${!fn:contains(status.value, country.isoCountryCode)}">
										<option value="${country.isoCountryCode}">
											<string:capitalize><string:lowerCase>${country.name}</string:lowerCase></string:capitalize>
										</option>
									</c:if>	
								</c:forEach>
							</select>									
						</td>		
						<td>	
							<p>
								<a href="javascript:addSelectedOption('countriesList${resourceIndex}','selectedCountries${resourceIndex}');resetHidden('selectedCountries${resourceIndex}', '${status.expression}');">Add</a>
							</p>
							<p>	
								<a href="javascript:addSelectedOption('selectedCountries${resourceIndex}','countriesList${resourceIndex}');resetHidden('selectedCountries${resourceIndex}', '${status.expression}');">Remove</a>
							</p>		
						</td>
						<td>
							<select id="selectedCountries${resourceIndex}" class="relatedCountries" size="10">
								<c:forEach items="${countries}" var="country" varStatus="countryStatus">
									<c:if test="${fn:contains(status.value, country.isoCountryCode)}">
										<option value="${country.isoCountryCode}">
											<string:capitalize><string:lowerCase>${country.name}</string:lowerCase></string:capitalize>
										</option>
									</c:if>	
								</c:forEach>
							</select>			
							<input type="hidden" id="${status.expression}" name="${status.expression}"						
								value="<string:trim><c:forEach items="${countries}" var="country" varStatus="countryStatus"><c:if test="${fn:contains(status.value, country.isoCountryCode)}">${country.isoCountryCode},</c:if></c:forEach></string:trim>"/>
						</td>			
					</tr>
				</table>		
				<gbiftag:resourceDetailCheck 
					readonlyProperties="${readonlyProperties}" 
					updatedProperties="${updatedProperties}" 
					propertyName="relatesToCountries" errorMessage="${status.errorMessage}"/>			
			</spring:bind>
	</fieldset>
</p>
<p>
	<fieldset id="boundingBox" class="subFieldsetNoHeader">
		<!-- 
		<p>
			<a href="javascript:openMap();">Use map to set coordinates</a>
		</p>	
		 -->
		<p>
		  You may specify latitudinal and longitudinal limits to the data coverage.
		</p>   
		<p>
			<spring:bind path="resource.northCoordinate">
			<label>North coordinate</label>
			<input id="north" name="${status.expression}" type="text" class="coordinates" value="${status.value}"/>
			<gbiftag:resourceDetailCheck 
				readonlyProperties="${readonlyProperties}" 
				updatedProperties="${updatedProperties}" 
				propertyName="northCoordinate" errorMessage="${status.errorMessage}"/>
			</spring:bind>
		</p>	
		<p>
			<spring:bind path="resource.southCoordinate">
			<label>South coordinate</label>
			<input id="south" name="${status.expression}" type="text" class="coordinates" value="${status.value}"/>
			<gbiftag:resourceDetailCheck 
				readonlyProperties="${readonlyProperties}" 
				updatedProperties="${updatedProperties}" 
				propertyName="southCoordinate" errorMessage="${status.errorMessage}"/>
			</spring:bind>
		</p>
		<p>
			<spring:bind path="resource.eastCoordinate">
			<label>East coordinate</label>
			<input id="east" name="${status.expression}" type="text" class="coordinates" value="${status.value}"/>
			<gbiftag:resourceDetailCheck 
				readonlyProperties="${readonlyProperties}" 
				updatedProperties="${updatedProperties}" 
				propertyName="eastCoordinate" errorMessage="${status.errorMessage}"/>
			</spring:bind>
		</p>	
		<p>
			<spring:bind path="resource.westCoordinate">
			<label>West coordinate</label>
			<input id="west" name="${status.expression}" type="text" class="coordinates" value="${status.value}"/>
			<gbiftag:resourceDetailCheck 
				readonlyProperties="${readonlyProperties}" 
				updatedProperties="${updatedProperties}" 
				propertyName="westCoordinate" errorMessage="${status.errorMessage}"/>
			</spring:bind>
		</p>	
	</fieldset>		
</p>	
<p>
	<fieldset id="indexingPreferences" class="subFieldset">
		<legend>Indexing preferences</legend>
		<p>
			<spring:bind path="resource.indexingStartTime">
				<label for="${status.expression}">Start time</label>		
				<select name="${status.expression}" class="smallValue" <c:if test="${fn:contains(readonlyProperties, 'indexingStartTime')}">disabled="true"</c:if>>
					<option value="N/A"><spring:message code="registration.not.specified"/></option>								
					<c:forEach begin="0" end="23" var="x">
						<c:set var="startTime">${x<=9 ? '0' : ''}${x}:00:00GMT</c:set>
						<option value="${startTime}"<c:if test="${startTime == status.value}"> selected="true"</c:if>>${startTime}</option>
					</c:forEach>
				</select>
				<c:if test="${fn:contains(readonlyProperties, 'indexingStartTime')}">
					<input type="hidden" name="${status.expression}" value="${status.value}"/>
				</c:if>			
				<gbiftag:resourceDetailCheck 
					readonlyProperties="${readonlyProperties}" 
					updatedProperties="${updatedProperties}" 
					propertyName="indexingStartTime" errorMessage="${status.errorMessage}"/>
			</spring:bind>			
		</p>
		<p>
			<spring:bind path="resource.indexingMaxDuration">
				<label for="${status.expression}">Maximum duration</label>					
				<select name="${status.expression}" class="smallValue" <c:if test="${fn:contains(readonlyProperties, 'indexingMaxDuration')}">disabled="true"</c:if>>
					<option value="N/A"><spring:message code="registration.not.specified"/></option>								
					<c:forEach begin="1" end="24" var="x">
						<c:set var="duration">PT${x}H</c:set>
						<option value="${duration}"<c:if test="${duration == status.value}"> selected="true"</c:if>>${x} hour<c:if test="${x>1}">s</c:if></option>
					</c:forEach>
				</select>			
				<c:if test="${fn:contains(readonlyProperties, 'indexingMaxDuration')}">
					<input type="hidden" name="${status.expression}" value="${status.value}"/>
				</c:if>			
				<gbiftag:resourceDetailCheck 
					readonlyProperties="${readonlyProperties}" 
					updatedProperties="${updatedProperties}" 
					propertyName="indexingMaxDuration" errorMessage="${status.errorMessage}"/>
			</spring:bind>			
		</p>
		<p>
			<spring:bind path="resource.indexingFrequency">
				<label for="${status.expression}">Frequency</label>								
				<select name="${status.expression}" class="smallValue" <c:if test="${fn:contains(readonlyProperties, 'indexingFrequency')}">disabled="true"</c:if>>
					<option value="N/A"><spring:message code="registration.not.specified"/></option>								
					<c:forEach begin="1" end="12" var="x">
						<c:set var="duration">P${x}M</c:set>
						<option value="${duration}"<c:if test="${duration == status.value}"> selected="true"</c:if>> every ${x>1? x:''} month<c:if test="${x>1}">s</c:if></option>
					</c:forEach>
				</select>			
				<c:if test="${fn:contains(readonlyProperties, 'indexingFrequency')}">
					<input type="hidden" name="${status.expression}" value="${status.value}"/>
				</c:if>			
				<gbiftag:resourceDetailCheck 
					readonlyProperties="${readonlyProperties}" 
					updatedProperties="${updatedProperties}" 
					propertyName="indexingFrequency" errorMessage="${status.errorMessage}"/>
			</spring:bind>			
		</p>
	</fieldset>		
</p>
<p>
<fieldset id="temporalCoverage" class="subFieldset">
	<legend>Temporal coverage</legend>
	<c:set var="ed_day"><fmt:formatDate value="${today}" pattern="dd"/></c:set>
	<c:set var="ed_month"><fmt:formatDate value="${today}" pattern="MM"/></c:set>
	<c:set var="ed_year"><fmt:formatDate value="${today}" pattern="yyyy"/></c:set>
	<p>
	<div id="startDate"  class="dateRange">
		<label>Start date</label>
		<spring:bind path="resource.startDate">	
			<c:set var="sd_day"><fmt:formatDate value="${status.value}" pattern="dd"/></c:set>
			<c:set var="sd_month"><fmt:formatDate value="${status.value}" pattern="MM"/></c:set>
			<c:set var="sd_year"><fmt:formatDate value="${status.value}" pattern="yyyy"/></c:set>	
			<gbiftag:dateSelect name="${status.expression}" 
				selectedDay="${sd_day}" 
				selectedMonth="${sd_month}" 
				selectedYear="${sd_year}"/>
			<gbiftag:resourceDetailCheck 
				readonlyProperties="${readonlyProperties}" 
				updatedProperties="${updatedProperties}" 
				propertyName="startDate" errorMessage="${status.errorMessage}"/>
		</spring:bind>
	</div>		
	</p>
	<p>
	<div id="endDate${resourceIndex}" class="dateRange">
		<label>End date</label>
		<spring:bind path="resource.endDate">	
			<c:set var="ed_day"><fmt:formatDate value="${status.value}" pattern="dd"/></c:set>
			<c:set var="ed_month"><fmt:formatDate value="${status.value}" pattern="MM"/></c:set>
			<c:set var="ed_year"><fmt:formatDate value="${status.value}" pattern="yyyy"/></c:set>		
			<gbiftag:dateSelect name="${status.expression}" 
				selectedDay="${ed_day}" 
				selectedMonth="${ed_month}" 
				selectedYear="${ed_year}"/>
			<gbiftag:resourceDetailCheck 
				readonlyProperties="${readonlyProperties}" 
				updatedProperties="${updatedProperties}" 
				propertyName="endDate" errorMessage="${status.errorMessage}"/>
		</spring:bind>	
	</div>
	</p>
	<p>	
		<spring:bind path="resource.livingCollection">
			<label>This is a living collection</label>
			<input type="checkbox" id="${status.expression}" name="${status.expression}" 
				<c:if test="${status.value}">checked="true"</c:if>
				 onchange="javascript:toggleDateSelector('endDate${resourceIndex}', this.checked);"
				<c:if test="${fn:contains(readonlyProperties, 'isLivingCollection')}">readonly="true"</c:if>/>	
			
			<c:if test="${status.value}">
				<script>
					toggleDateSelector('endDate${resourceIndex}', true);
				</script>
			</c:if>
			<gbiftag:resourceDetailCheck 
				readonlyProperties="${readonlyProperties}" 
				updatedProperties="${updatedProperties}" 
				propertyName="livingCollection" errorMessage="${status.errorMessage}"/>
		</spring:bind>	
	</p>
</fieldset>
</p>
<p>
<fieldset id="taxonomicCoverage" class="subFieldset">
	<legend>Taxonomic coverage</legend>
	<p>If applicable, please select the <b>root taxon</b> for this resource using the tree below.</p>	
	<p>
		<spring:bind path="resource.highestTaxa">
			<input type="hidden" id="highestTaxonName-${resourceIndex}" name="${status.expression}" value="${status.value}" readonly="true"/> 
			<gbiftag:resourceDetailCheck 
				readonlyProperties="${readonlyProperties}" 
				updatedProperties="${updatedProperties}" 
				propertyName="highestTaxa" errorMessage="${status.errorMessage}"/>
		</spring:bind>	
	</p>	
	<p>
		<spring:bind path="resource.highestTaxaConceptId">
			<input type="hidden" id="highestTaxonId-${resourceIndex}" name="${status.expression}" value="${status.value}"/> 
		</spring:bind>	
		<script>
			var callback${resourceIndex} = {
				pre:function(key, name, fullName, rank, isMajorRank){
					
					if(key==null){
						document.getElementById("highestTaxonName-${resourceIndex}").value = "";												
						document.getElementById("highestTaxonId-${resourceIndex}").value = "";																									
					} else {
						document.getElementById("highestTaxonName-${resourceIndex}").value = fullName;												
						document.getElementById("highestTaxonId-${resourceIndex}").value = key;																									
					}													
				},	
				post: function(){}
			}											
		</script>										
		<div id="taxonomyTree[${resourceIndex}]" class="smalltree" style="margin:0;">
				<gbif:ajaxTaxonomyBrowser 
					concepts="${concepts}" 
					rootUrl="provider/${dataProvider.key}" 
					highestRank="kingdom"
					addOverviewLink="false"
					containerDivId="taxonomyTree[${resourceIndex}]"
					callback="callback${resourceIndex}"
					selectedConcept="${selectedConcept}"
					messageSource="${messageSource}"/>
		</div>
	</p>	
</fieldset><!-- Taxonomic coverage -->		
</p>			
</fieldset><!-- resourceForm -->