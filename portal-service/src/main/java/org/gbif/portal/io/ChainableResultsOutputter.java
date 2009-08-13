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
package org.gbif.portal.io;

/**
 * A results outputter that passes the bean map on to another results outputter
 * once it has outputted in its chosen format.
 * This is handy to subclass if you want to chain results outputters without making 
 * multiple passes of the underlying resultset.
 * 
 * @author dmartin
 */
public interface ChainableResultsOutputter extends ResultsOutputter {

	/**
	 * Set the ResultsOutputter to call after this one.
	 * 
	 * @param resultsOutputter
	 */
	public ResultsOutputter getNextResultsOutputter();
	
	/**
	 * Whether this results outputter to be chained in
	 * one pass of the results. 
	 * 
	 * @return false if this results outputter requires its own pass of the data. 
	 */
	public boolean isChainInOnePass();
}