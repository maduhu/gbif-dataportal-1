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
package org.gbif.portal.service.provider;

import java.util.List;

/**
 * Used to indicate that the index has not got the data required to issue the request
 * @author trobertson
 */
public class InsufficientDataInIndexException extends RuntimeException {
	/**
	 * Generated 
	 */
	private static final long serialVersionUID = 4478509418003262881L;
	
	/**
	 * The missing parameters
	 */
	List<String> missingParameters;
	
	/**
	 * @param message To use
	 * @param cause The cause of the error
	 */
	public InsufficientDataInIndexException(List<String> missingParams) {
		super();
		this.missingParameters = missingParams;
	}

	/**
	 * @see java.lang.Throwable#getMessage()
	 */
	@Override
	public String getMessage() {
		StringBuffer sb = new StringBuffer("Parameters missing [");
		for (String s : missingParameters) {
			sb.append(s + ", ");
		}
		sb.append("]");
		return sb.toString();
	}

	/**
	 * @return Returns the missingParameters.
	 */
	public List<String> getMissingParameters() {
		return missingParameters;
	}
}
