/**
 * 
 */
package org.gbif.portal.model.log;

import org.gbif.portal.model.BaseObject;

/**
 * Model object for user.
 * Just a temp model for now as it will move when the user must log in
 * 
 * @author trobertson
 */
public class User extends BaseObject {
	protected Long portalInstanceId;
	protected String name;
	protected String email;
	protected boolean verified = false;
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
	 * @return Returns the portalInstanceId.
	 */
	public Long getPortalInstanceId() {
		return portalInstanceId;
	}
	/**
	 * @param portalInstanceId The portalInstanceId to set.
	 */
	public void setPortalInstanceId(Long portalInstanceId) {
		this.portalInstanceId = portalInstanceId;
	}
	/**
	 * @return Returns the verified.
	 */
	public boolean isVerified() {
		return verified;
	}
	/**
	 * @param verified The verified to set.
	 */
	public void setVerified(boolean verified) {
		this.verified = verified;
	}
}