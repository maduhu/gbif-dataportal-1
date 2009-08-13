<%@ include file="/common/taglibs.jsp"%>
<div id="twopartheader">	
	<h2><spring:message code="registration.header"/></h2>
	<h3>
		<c:choose>
			<c:when test="${not empty providerDetail.businessName}">
				<spring:message code="registration.provider.update" text="Update details for"/> ${providerDetail.businessName}</h3>				
			</c:when>
			<c:otherwise>
				<spring:message code="registration.provider.register.new" text="Register a new provider"/>	
			</c:otherwise>	
		</c:choose>
	</h3>
</div>	
<div id="registrationContainer">
	<form onSubmit='javascript:indexSecondaryContacts();' id="providerContainerForm" method="post" action="<c:url value='/register/synchroniseProvider'/>">
		<spring:bind path="providerDetail.businessKey">
			<input type="hidden" name="${status.expression}" value="${status.value}" />
		</spring:bind>	

		<fieldset class="providerDetails">
			<legend><spring:message code="registration.provider"/></legend>
			<p>
				<spring:bind path="providerDetail.businessName">
					<label for="${status.expression}"><spring:message code="registration.provider.name"/></label>
					<input type="text" name="${status.expression}" value="${status.value}" id="${status.expression}"/> 
					<span class="error">${status.errorMessage}</span>
				</spring:bind>	
			</p>
			<p>
				<spring:bind path="providerDetail.businessDescription">
					<label for="${status.expression}"><spring:message code="registration.provider.description"/></label>
					<textarea rows="3" name="${status.expression}" id="${status.expression}"><c:out value="${status.value}"/></textarea> 
					<span class="error">${status.errorMessage}</span>
				</spring:bind>	
			</p>
			<p>
				<spring:bind path="providerDetail.businessCountry">
					<label for="${status.expression}"><spring:message code="registration.provider.country"/></label>
					<select id="${status.expression}" name="${status.expression}">
						<option value="">Please select...</option>
        <c:forEach items="${countries}" var="country">
            <option value="${country.isoCountryCode}" <c:if test="${country.isoCountryCode == status.value}">selected="true"</c:if>>
            	<string:capitalize><string:lowerCase>${country.name}</string:lowerCase></string:capitalize>
            </option>
        </c:forEach>
					</select>
					<span class="error">${status.errorMessage}</span>					
				</spring:bind>	
			</p>
			<p>
				<spring:bind path="providerDetail.businessLogoURL">
					<label for="${status.expression}"><spring:message code="registration.provider.logoUrl"/></label>
					<input type="text" name="${status.expression}" value="${status.value}" id="${status.expression}"/> <span class="error"><c:out value="${status.errorMessage}"/></span>
					<span class="error">${status.errorMessage}</span>					
				</spring:bind>	
			</p>
		</fieldset>

		<fieldset class="providerDetails">
			<legend><spring:message code="registration.provider.primarycontact"/></legend>
			<p>				
				<spring:bind path="providerDetail.businessPrimaryContact.name">
					<label for="${status.expression}"><spring:message code="name"/></label>
					${status.value}
					<input type="hidden" name="${status.expression}" value="${status.value}" id="${status.expression}"/>
				</spring:bind>	
			</p>
			<p>
				<spring:bind path="providerDetail.businessPrimaryContact.useType">
					<label for="${status.expression}"><spring:message code="registration.provider.contact.useType"/></label>
					<select name="${status.expression}" id="${status.expression}">
						<c:forEach items="${contactTypes}" var="type">
							## Transform the Enum to it's String
							<spring:transform value="${type}" var="typeString"/>
							<option value="<c:out value="${typeString}"/>"
			                    <c:if test="${status.value == typeString}"> selected="selected"</c:if>>
			                    	<spring:message code="registration.provider.contact.${typeString}"/>
			                </option>								
						</c:forEach>					                 
					</select> 
					<span class="error">${status.errorMessage}</span>
				</spring:bind>	
			</p>
			<p>
				<spring:bind path="providerDetail.businessPrimaryContact.email">
					<label for="${status.expression}"><spring:message code="email"/></label>
					${status.value}
					<input type="hidden" name="${status.expression}" value="${status.value}" id="${status.expression}"/> 
				</spring:bind>	
			</p>				
			<p>
				<spring:bind path="providerDetail.businessPrimaryContact.phone">
					<label for="${status.expression}"><spring:message code="telephone"/></label>
					${status.value}
					<input type="hidden" name="${status.expression}" value="${status.value}" id="${status.expression}"/>
				</spring:bind>	
			</p>				
		</fieldset>

		<div id="secondaryContactsContainer">
			<c:forEach items="${providerDetail.businessSecondaryContacts}" var="contact" varStatus="contactStatus">
				<div>
					<fieldset class="providerDetails">
						<legend><spring:message code="registration.provider.secondarycontact"/> - <a class="jsLink" onclick='javascript:deleteSecondaryContact(this.parentNode.parentNode.parentNode)'><spring:message code="delete"/></a></legend>
						<p>
							<spring:bind path="providerDetail.businessSecondaryContacts[${contactStatus.index}].name">
								<label for="${status.expression}"><spring:message code="name"/></label>
								<input type="text" name="${status.expression}" value="${status.value}" id="${status.expression}"/> 
								<span class="error">${status.errorMessage}</span>
							</spring:bind>	
						</p>
						<p>
							<spring:bind path="providerDetail.businessSecondaryContacts[${contactStatus.index}].useType">
								<label for="${status.expression}"><spring:message code="registration.provider.contact.useType"/></label>
								<select name="${status.expression}" id="${status.expression}">
									<c:forEach items="${contactTypes}" var="type">
										## Transform the Enum to it's String
										<spring:transform value="${type}" var="typeString"/>
										<option value="<c:out value="${typeString}"/>"
						                    <c:if test="${status.value == typeString}"> selected="selected"</c:if>>
						                    	<spring:message code="registration.provider.contact.${typeString}"/>
						                </option>								
									</c:forEach>					                 
								</select> 
							<span class="error">${status.errorMessage}</span>
							</spring:bind>	
						</p>
						<p>
							<spring:bind path="providerDetail.businessSecondaryContacts[${contactStatus.index}].email">
								<label for="${status.expression}"><spring:message code="email"/></label>
								<input type="text" name="${status.expression}" value="${status.value}" id="${status.expression}"/> 
								<span class="error">${status.errorMessage}</span>
							</spring:bind>	
						</p>				
						<p>
							<spring:bind path="providerDetail.businessSecondaryContacts[${contactStatus.index}].phone">
								<label for="${status.expression}"><spring:message code="telephone"/></label>
								<input type="text" name="${status.expression}" value="${status.value}" id="${status.expression}"/> 
								<span class="error">${status.errorMessage}</span>
							</spring:bind>	
						</p>				
					</fieldset>
				</div>
			</c:forEach>
		</div>
		<p>
			<script>
				// creates and appends a new DIV with a secondary contact
				function createSecondaryContact() {
					var text = document.getElementById("secondaryContactTemplate").innerHTML;
					var target = document.createElement("DIV");
					target.innerHTML = text;
					var parent = document.getElementById("secondaryContactsContainer");
					parent.appendChild(target);
				}
				// removes the target secondary contact
				function deleteSecondaryContact(target) {
					document.getElementById("secondaryContactsContainer").removeChild(target);
				}
				// to be called on submission - adds the correct index to each secondary contact
				function indexSecondaryContacts() {
					var index=0;
					var childDivs = document.getElementById("secondaryContactsContainer").getElementsByTagName("div");
					for (var i=0; i<childDivs.length; i++) {
							var target = childDivs[i];
							var inputs = target.getElementsByTagName("input");								
							for (var j=0; j<inputs.length; j++) {
								replaceAttribute(inputs[j], "name", /\[.+\]/g, "[" + index + "]");
							}
							var selects = target.getElementsByTagName("select");								
							for (var j=0; j<selects.length; j++) {
								replaceAttribute(selects[j], "name", /\[.+\]/g, "[" + index + "]");
							}
							index = index+1;
					}
				}
				
				// replaces attributes of the specified name on the given node,
				// that match the regEx with the new value
				function replaceAttribute(node, attributeName, regEx, newValue) {
					var attribute = node.getAttribute(attributeName);
					var newText = attribute.replace(regEx, newValue);
					// alert("changing: " + attributeName + " from " + attribute + " to " + newText);
					node.setAttribute(attributeName, newText);
				}
				
			</script>
			<a href='javascript:createSecondaryContact()'><spring:message code="registration.provider.link.create.secondary"/></a>
		</p>
		<p>			
			<input type="submit" value="<spring:message code="update" text="Update"/>"/>
		</p>
	</form>
	<div id="secondaryContactTemplate" class="hidden">
		<fieldset class="providerDetails">
			<legend><spring:message code="registration.provider.secondarycontact"/> - <a class="jsLink" class="deleteContact" onclick='javascript:deleteSecondaryContact(this.parentNode.parentNode.parentNode)'><spring:message code="delete"/></a></legend>
			<p>
				<label for="businessSecondaryContacts[X_X].name"><spring:message code="name"/></label>
				<input type="text" name="businessSecondaryContacts[X_X].name" id="businessSecondaryContacts[X_X].name"/>
			</p>
			<p>
				<label for="businessSecondaryContacts[X_X].useType"><spring:message code="registration.provider.contact.useType"/></label>
				<select name="businessSecondaryContacts[X_X].useType" id="businessSecondaryContacts[X_X].useType">
					<c:forEach items="${contactTypes}" var="type">
						<option value="<c:out value="${type}"/>">
	                    	<spring:message code="registration.provider.contact.${type}"/>
		                </option>								
					</c:forEach>					                 
				</select>
			</p>
			<p>
				<label for="businessSecondaryContacts[X_X].email"><spring:message code="email"/></label>
				<input type="text" name="businessSecondaryContacts[X_X].email" id="businessSecondaryContacts[X_X].email"/>
			</p>				
			<p>
				<label for="businessSecondaryContacts[X_X].phone"><spring:message code="telephone"/></label>
				<input type="text" name="businessSecondaryContacts[X_X].phone" id="businessSecondaryContacts[X_X].phone"/>
			</p>				
		</fieldset>		
	</div>
</div>