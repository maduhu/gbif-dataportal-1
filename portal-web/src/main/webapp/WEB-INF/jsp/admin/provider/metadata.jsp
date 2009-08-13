<%@ include file="/common/taglibs.jsp"%>
<div class="adminConsoleContainer">
	<div id="providerContainer">
		<h1><spring:message code="admin.provider.details"/></h1>
		
		<form onSubmit='javascript:indexSecondaryContacts();' id="providerContainerForm" method="post" action="<c:url value='/admin/provider/create.htm?_target1'/>">
			<spring:bind path="providerDetail.businessKey">
				<input type="hidden" name="${status.expression}" value="${status.value}" />
			</spring:bind>	

			<fieldset>
				<h3><spring:message code="admin.provider"/></h3>
				<p>
					<spring:bind path="providerDetail.businessName">
						<label for="${status.expression}"><spring:message code="admin.provider.name"/></label>
						<input type="text" name="${status.expression}" value="${status.value}" id="${status.expression}"/> <span class="error">${status.errorMessage}</span>
					</spring:bind>	
				</p>
				<p>
					<spring:bind path="providerDetail.businessDescription">
						<label for="${status.expression}"><spring:message code="admin.provider.description"/></label>
						<textarea rows="3" name="${status.expression}" id="${status.expression}"><c:out value="${status.value}"/></textarea> <span class="error"><c:out value="${status.errorMessage}"/></span>
					</spring:bind>	
				</p>
				<p>
					<spring:bind path="providerDetail.businessCountry">
						<label for="${status.expression}"><spring:message code="admin.provider.country"/></label>
						<input type="text" name="${status.expression}" value="${status.value}" id="${status.expression}"/> <span class="error"><c:out value="${status.errorMessage}"/></span>
					</spring:bind>	
				</p>
				<p>
					<spring:bind path="providerDetail.businessLogoURL">
						<label for="${status.expression}"><spring:message code="admin.provider.logoUrl"/></label>
						<input type="text" name="${status.expression}" value="${status.value}" id="${status.expression}"/> <span class="error"><c:out value="${status.errorMessage}"/></span>
					</spring:bind>	
				</p>
			</fieldset>

			<fieldset>
				<h3><spring:message code="admin.provider.primarycontact"/></h3>
				<p>				
					<spring:bind path="providerDetail.businessPrimaryContact.name">
						<label for="${status.expression}"><spring:message code="admin.provider.contact.name"/></label>
						<input disabled="true" type="text" name="${status.expression}" value="${status.value}" id="${status.expression}"/> <span class="error"><c:out value="${status.errorMessage}"/></span>
					</spring:bind>	
				</p>
				<p>
					<spring:bind path="providerDetail.businessPrimaryContact.useType">
						<label for="${status.expression}"><spring:message code="admin.provider.contact.useType"/></label>
						<select name="${status.expression}" id="${status.expression}">
							<c:forEach items="${contactTypes}" var="type">
								## Transform the Enum to it's String
								<spring:transform value="${type}" var="typeString"/>
								<option value="<c:out value="${typeString}"/>"
				                    <c:if test="${status.value == typeString}"> selected="selected"</c:if>>
				                    	<spring:message code="admin.provider.contact.${typeString}"/>
				                </option>								
							</c:forEach>					                 
						</select> <span class="error"><c:out value="${status.errorMessage}"/></span>
					</spring:bind>	
				</p>
				<p>
					<spring:bind path="providerDetail.businessPrimaryContact.email">
						<label for="${status.expression}"><spring:message code="admin.provider.contact.email"/></label>
						<input disabled="true"  type="text" name="${status.expression}" value="${status.value}" id="${status.expression}"/> <span class="error"><c:out value="${status.errorMessage}"/></span>
					</spring:bind>	
				</p>				
				<p>
					<spring:bind path="providerDetail.businessPrimaryContact.phone">
						<label for="${status.expression}"><spring:message code="admin.provider.contact.phone"/></label>
						<input disabled="true"  type="text" name="${status.expression}" value="${status.value}" id="${status.expression}"/> <span class="error"><c:out value="${status.errorMessage}"/></span>
					</spring:bind>	
				</p>				
			</fieldset>

			<div id="secondaryContactsContainer">
				<c:forEach items="${providerDetail.businessSecondaryContacts}" var="contact" varStatus="status">
					<div>
						<fieldset>
							<h3><spring:message code="admin.provider.secondarycontact"/> - <a onclick='javascript:deleteSecondaryContact(this.parentNode.parentNode.parentNode)'><spring:message code="delete"/></a></h3>
							<p>
								<spring:bind path="providerDetail.businessSecondaryContacts[${status.index}].name">
									<label for="${status.expression}"><spring:message code="admin.provider.contact.name"/></label>
									<input type="text" name="${status.expression}" value="${status.value}" id="${status.expression}"/> <span class="error"><c:out value="${status.errorMessage}"/></span>
								</spring:bind>	
							</p>
							<p>
								<spring:bind path="providerDetail.businessSecondaryContacts[${status.index}].useType">
									<label for="${status.expression}"><spring:message code="admin.provider.contact.useType"/></label>
									<select name="${status.expression}" id="${status.expression}">
										<c:forEach items="${contactTypes}" var="type">
											## Transform the Enum to it's String
											<spring:transform value="${type}" var="typeString"/>
											<option value="<c:out value="${typeString}"/>"
							                    <c:if test="${status.value == typeString}"> selected="selected"</c:if>>
							                    	<spring:message code="admin.provider.contact.${typeString}"/>
							                </option>								
										</c:forEach>					                 
									</select> <span class="error"><c:out value="${status.errorMessage}"/></span>
								</spring:bind>	
							</p>
							<p>
								<spring:bind path="providerDetail.businessSecondaryContacts[${status.index}].email">
									<label for="${status.expression}"><spring:message code="admin.provider.contact.email"/></label>
									<input type="text" name="${status.expression}" value="${status.value}" id="${status.expression}"/> <span class="error"><c:out value="${status.errorMessage}"/></span>
								</spring:bind>	
							</p>				
							<p>
								<spring:bind path="providerDetail.businessSecondaryContacts[${status.index}].phone">
									<label for="${status.expression}"><spring:message code="admin.provider.contact.phone"/></label>
									<input type="text" name="${status.expression}" value="${status.value}" id="${status.expression}"/> <span class="error"><c:out value="${status.errorMessage}"/></span>
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
						for (var i=0; i<document.getElementById("secondaryContactsContainer").childNodes.length; i++) {
							// ignore the text nodes that signal whitespace
							if (document.getElementById("secondaryContactsContainer").childNodes[i] instanceof HTMLDivElement) {
								var target = document.getElementById("secondaryContactsContainer").childNodes[i];
								
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
				<a href='javascript:createSecondaryContact()'><spring:message code="admin.provider.link.create.secondary"/></a>
			</p>
			<p>
				TODO - GBIF Participant - How do we get this?
			</p>
			
			<p class="center">			
				<input type="submit" value="<spring:message code="next"/>"/>
			</p>
		</form>
		<div id="secondaryContactTemplate" class="hidden">
			<fieldset>
				<h3><spring:message code="admin.provider.secondarycontact"/> - <a onclick='javascript:deleteSecondaryContact(this.parentNode.parentNode.parentNode)'><spring:message code="delete"/></a></h3>
				<p>
					<label for="businessSecondaryContacts[X_X].name"><spring:message code="admin.provider.contact.name"/></label>
					<input type="text" name="businessSecondaryContacts[X_X].name" id="businessSecondaryContacts[X_X].name"/>
				</p>
				<p>
					<label for="businessSecondaryContacts[X_X].useType"><spring:message code="admin.provider.contact.useType"/></label>
					<select name="businessSecondaryContacts[X_X].useType" id="businessSecondaryContacts[X_X].useType">
						<c:forEach items="${contactTypes}" var="type">
							<option value="<c:out value="${type}"/>">
		                    	<spring:message code="admin.provider.contact.${type}"/>
			                </option>								
						</c:forEach>					                 
					</select>
				</p>
				<p>
					<label for="businessSecondaryContacts[X_X].email"><spring:message code="admin.provider.contact.email"/></label>
					<input type="text" name="businessSecondaryContacts[X_X].email" id="businessSecondaryContacts[X_X].email"/>
				</p>				
				<p>
					<label for="businessSecondaryContacts[X_X].phone"><spring:message code="admin.provider.contact.phone"/></label>
					<input type="text" name="businessSecondaryContacts[X_X].phone" id="businessSecondaryContacts[X_X].phone"/>
				</p>				
			</fieldset>		
		</div>
	</div>
</div>