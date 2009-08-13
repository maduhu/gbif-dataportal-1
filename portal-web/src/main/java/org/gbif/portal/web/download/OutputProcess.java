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
package org.gbif.portal.web.download;

import org.gbif.portal.io.ResultsOutputter;

/**
 * An output process encaspulates the details of calling a particular 
 * service layer methods for outputting data.
 * 
 * @author dmartin
 */
public interface OutputProcess {
	
	/**
	 * Run the output process passing the results outputter to
	 * handle formatting of the results.
	 * 
	 * @param resultsOutputter
	 * @throws Exception
	 */
	public void process(ResultsOutputter resultsOutputter) throws Exception;
	
	/**
	 * Set the max number of results to process.
	 * 
	 * @param maxNoToProcess
	 */
	public void setProcessLimit(int maxNoToProcess);
}