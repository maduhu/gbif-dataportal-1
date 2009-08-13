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
package org.gbif.portal.dao.log;

import org.gbif.portal.model.log.User;
/**
 * DAO interface for user
 * 
 * @author trobertson
 */
public interface UserDAO {

	/**
	 * Find user
	 * @param portalInstanceId
	 * @param name
	 * @param email
	 * @return
	 */ 
	public User findUser(long portalInstanceId, String name, String email);
	
	/**
	 * Find user
	 * @param id
	 * @return
	 */ 
	public User getUserFor(long id);
	
	/**
	 * @param user Creates it
	 */
	public void createUser(User user);
	
	/**
	 * @param user Updates the user details
	 */
	public void updateUser(User user);
}