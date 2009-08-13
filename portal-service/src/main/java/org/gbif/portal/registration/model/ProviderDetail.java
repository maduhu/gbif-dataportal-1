/***************************************************************************
 * Copyright (C) 2005 Global Biodiversity Information Facility Secretariat.
 * All Rights Reserved.
 *
 * The contents of this file are subject to the Mozilla Public
 * License Version 1.1 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of
 * the License at http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS
 * IS" basis, WITHOUT WARRANTY OF ANY KIND, either express or
 * implied. See the License for the specific language governing
 * rights and limitations under the License.
 ***************************************************************************/
package org.gbif.portal.registration.model;

import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * The MVC Model for the a provider.
 * 
 * This encapsulates all the information for the wizard screans to create and manipulate
 * a provider
 * 
 * Note
 * 
 * This is a simple java bean and hence further javadoc is omitted for properties.
 * @link http://wiki.gbif.org/dadiwiki/wikka.php?wakka=RegistrationInterface
 * @author trobertson
 */
public class ProviderDetail {
	// the business level details
	protected String businessKey;
	protected String businessName;
	protected String businessAddress;
	protected String businessCountry;
	protected String businessDescription;
	protected String businessLogoURL;
	protected Contact businessPrimaryContact = new Contact();
	protected String businessGBIFParticipantKey;
	protected List<Contact> businessSecondaryContacts = new LinkedList<Contact>();
	protected List<ResourceDetail> businessResources = new LinkedList<ResourceDetail>();
	
	/**
	 * The contact types
	 */
	public static enum ContactTypes {
		administrative,
		technical;
	};
	
	/**
	 * Encapsulates the details for a simple contact within UDDI
	 * This is a simple java bean and hence further javadoc is omitted for properties.
	 * @author trobertson
	 */	
	public class Contact {
		public static final String USE_TYPE_TECHINCAL = "technical"; 
		public static final String USE_TYPE_ADMINISTRATIVE = "administrative";
		protected String name;
		protected String useType;
		protected String email;
		protected String phone;
		
		/**
		 * @return Returns the email.
		 */
		public String getEmail() {
			return email;
		}
		/**
		 * @param email The email to set.
		 */
		public void setEmail(String email) {
			this.email = email;
		}
		/**
		 * @return Returns the name.
		 */
		public String getName() {
			return name;
		}
		/**
		 * @param name The name to set.
		 */
		public void setName(String name) {
			this.name = name;
		}
		/**
		 * @return Returns the phone.
		 */
		public String getPhone() {
			return phone;
		}
		/**
		 * @param phone The phone to set.
		 */
		public void setPhone(String phone) {
			this.phone = phone;
		}
		/**
		 * @return Returns the useType.
		 */
		public String getUseType() {
			return useType;
		}
		/**
		 * @param useType The useType to set.
		 */
		public void setUseType(String useType) {
			this.useType = useType;
		}
	}

	/**
	 * @return Returns the businessCountry.
	 */
	public String getBusinessCountry() {
		return businessCountry;
	}

	/**
	 * @param businessCountry The businessCountry to set.
	 */
	public void setBusinessCountry(String businessCountry) {
		this.businessCountry = businessCountry;
	}

	/**
	 * @return Returns the businessDescription.
	 */
	public String getBusinessDescription() {
		return businessDescription;
	}

	/**
	 * @param businessDescription The businessDescription to set.
	 */
	public void setBusinessDescription(String businessDescription) {
		this.businessDescription = businessDescription;
	}

	/**
	 * @return Returns the businessGBIFParticipantKey.
	 */
	public String getBusinessGBIFParticipantKey() {
		return businessGBIFParticipantKey;
	}

	/**
	 * @param businessGBIFParticipantKey The businessGBIFParticipantKey to set.
	 */
	public void setBusinessGBIFParticipantKey(String businessGBIFParticipantKey) {
		this.businessGBIFParticipantKey = businessGBIFParticipantKey;
	}

	/**
	 * @return Returns the businessKey.
	 */
	public String getBusinessKey() {
		return businessKey;
	}

	/**
	 * @param businessKey The businessKey to set.
	 */
	public void setBusinessKey(String businessKey) {
		this.businessKey = businessKey;
	}

	/**
	 * @return Returns the businessLogoURL.
	 */
	public String getBusinessLogoURL() {
		return businessLogoURL;
	}

	/**
	 * @param businessLogoURL The businessLogoURL to set.
	 */
	public void setBusinessLogoURL(String businessLogoURL) {
		this.businessLogoURL = businessLogoURL;
	}

	/**
	 * @return Returns the businessName.
	 */
	public String getBusinessName() {
		return businessName;
	}

	/**
	 * @param businessName The businessName to set.
	 */
	public void setBusinessName(String businessName) {
		this.businessName = businessName;
	}

	/**
	 * @return Returns the businessPrimaryContact.
	 */
	public Contact getBusinessPrimaryContact() {
		return businessPrimaryContact;
	}

	/**
	 * @param businessPrimaryContact The businessPrimaryContact to set.
	 */
	public void setBusinessPrimaryContact(Contact businessPrimaryContact) {
		this.businessPrimaryContact = businessPrimaryContact;
	}

	/**
	 * @return Returns the businessSecondaryContacts.
	 */
	public List<Contact> getBusinessSecondaryContacts() {
		return businessSecondaryContacts;
	}

	/**
	 * @param businessSecondaryContacts The businessSecondaryContacts to set.
	 */
	public void setBusinessSecondaryContacts(List<Contact> businessSecondaryContacts) {
		this.businessSecondaryContacts = businessSecondaryContacts;
	}

	/**
	 * @return Returns the businessResources.
	 */
	public List<ResourceDetail> getBusinessResources() {
		return businessResources;
	}

	/**
	 * @param businessResources The businessResources to set.
	 */
	public void setBusinessResources(List<ResourceDetail> businessResources) {
		this.businessResources = businessResources;
	}

	/**
	 * @return the businessAddress
	 */
	public String getBusinessAddress() {
		return businessAddress;
	}

	/**
	 * @param businessAddress the businessAddress to set
	 */
	public void setBusinessAddress(String businessAddress) {
		this.businessAddress = businessAddress;
	}
	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}	
}