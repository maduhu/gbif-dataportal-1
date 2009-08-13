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

/**
 * A Simple bean for representing a user
 * 
 * @author dmartin
 */
public class UserLogin {

	private String firstName;
	private String surname;
	private String email;
	private String telephone;
	private String username;
	private String password;
	
	/**
	 * @param firstName
	 * @param surname
	 * @param email
	 * @param username
	 * @param password
	 */
	public UserLogin(String firstName, String surname, String email,
			String username, String password) {
		this.firstName = firstName;
		this.surname = surname;
		this.email = email;
		this.username = username;
		this.password = password;
	}
	
	public UserLogin(){}
	
	public String getFullName(){
		StringBuffer sb = new StringBuffer();
		if(firstName!=null)
			sb.append(firstName);
		if(firstName!=null && surname!=null)
			sb.append(" ");
		if(surname!=null)
			sb.append(surname);
		return sb.toString();
	}
	
	/**
	 * @return the firstName
	 */
	public String getFirstName() {
		return firstName;
	}
	/**
	 * @param firstName the firstName to set
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	/**
	 * @return the surname
	 */
	public String getSurname() {
		return surname;
	}
	/**
	 * @param surname the surname to set
	 */
	public void setSurname(String surname) {
		this.surname = surname;
	}
	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}
	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}
	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}
	/**
	 * @param username the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}
	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}
	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}
	/**
	 * @return the telephone
	 */
	public String getTelephone() {
		return telephone;
	}
	/**
	 * @param telephone the telephone to set
	 */
	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}
}