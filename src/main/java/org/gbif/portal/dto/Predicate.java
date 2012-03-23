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
package org.gbif.portal.dto;

/**
 * This bean needs a better home. This bean contains details of a predicate for use
 * in the construction of a triplet query.
 *  
 * @author dmartin
 */
public class Predicate {
	
	/** The prefix for this predicate */
	protected String prefix ="";
	/** The postfix for this predicate */
	protected String postfix ="";
	/** Whether this triplet is equivalent to an equals */
	protected boolean isEquals = true;

	/**
	 * @return the isEquals
	 */
	public boolean getIsEquals() {
		return isEquals;
	}	
	
	/**
	 * @return the isEquals
	 */
	public boolean isEquals() {
		return isEquals;
	}
	/**
	 * @param isEquals the isEquals to set
	 */
	public void setIsEquals(boolean isEquals) {
		this.isEquals = isEquals;
	}
	/**
	 * @return the postfix
	 */
	public String getPostfix() {
		return postfix;
	}
	/**
	 * @param postfix the postfix to set
	 */
	public void setPostfix(String postfix) {
		this.postfix = postfix;
	}
	/**
	 * @return the prefix
	 */
	public String getPrefix() {
		return prefix;
	}
	/**
	 * @param prefix the prefix to set
	 */
	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}
}