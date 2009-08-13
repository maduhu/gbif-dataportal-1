<%@ include file="/common/taglibs.jsp"%>
<div id="twopartheader">	
	<h2><spring:message code="registration.header"/></h2>
	<h3>${providerDetail.businessName}</h3>
</div>	
<div id="registrationContainer">
	<fieldset class="providerDetails">
		<legend><spring:message code="registration.provider"/></legend>
		<p>
				<label for="${status.expression}"><spring:message code="registration.provider.name"/></label>
				${providerDetail.businessName}
		</p>
		<p>
				<label for="${status.expression}"><spring:message code="registration.provider.description"/></label>
				${providerDetail.businessDescription}
		</p>
		<p>
				<label for="${status.expression}"><spring:message code="registration.provider.country"/></label>
				<spring:message code="country.${providerDetail.businessCountry}" text="${providerDetail.businessCountry}"/>
		</p>
		<p>
			<spring:bind path="providerDetail.businessLogoURL">
				<label for="${status.expression}"><spring:message code="registration.provider.logoUrl"/></label>
				<c:if test="${not empty providerDetail.businessLogoURL}"><img src="${providerDetail.businessLogoURL}" alt="${providerDetail.businessLogoURL}"/></c:if>
			</spring:bind>	
		</p>
	</fieldset>
	<input type="hidden" name="${status.expression}" value="${providerDetail.businessKey}"/>
	<fieldset class="providerDetails">
		<legend><spring:message code="registration.provider.primarycontact"/></legend>
		<p>				
				<label for="name"><spring:message code="name"/></label>
				${providerDetail.businessPrimaryContact.name}
		</p>
		<p>
				<label for="use"><spring:message code="registration.provider.contact.useType"/></label>
   	<spring:message code="registration.provider.contact.${providerDetail.businessPrimaryContact.useType}" text="${providerDetail.businessPrimaryContact.useType}"/>
		</p>
		<p>
				<label for="email"><spring:message code="email"/></label>
				${providerDetail.businessPrimaryContact.email}
		</p>				
		<p>
				<label for="phone"><spring:message code="telephone"/></label>
				${providerDetail.businessPrimaryContact.phone}
		</p>				
	</fieldset>

	<div id="secondaryContactsContainer">
		<c:forEach items="${providerDetail.businessSecondaryContacts}" var="contact" varStatus="contactStatus">
			<div>
				<fieldset class="providerDetails">
					<legend><spring:message code="registration.provider.secondarycontact"/></legend>
					<p>
							<label for="${status.expression}"><spring:message code="name"/></label>
							${contact.name}	
					</p>
					<p>
							<label for="${status.expression}"><spring:message code="registration.provider.contact.useType"/></label>
							<spring:message code="registration.provider.contact.${contact.useType}" text="${contact.useType}"/>
					</p>
					<p>
							<label for="${status.expression}"><spring:message code="email"/></label>
							${contact.email}
					</p>				
					<p>
							<label for="${status.expression}"><spring:message code="telephone"/></label>
							${contact.phone}
					</p>				
				</fieldset>
			</div>
		</c:forEach>
	</div>
</div>