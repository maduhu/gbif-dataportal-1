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

import java.io.IOException;
import java.util.Map;

/**
 * An implementation of a ResultsOutputter will wrap an outputstream or buffer
 * and will write to this destination in a certain format.
 * 
 * Note: Implementations are not intended to be spring wired.
 * 
 * @see DelimitedResultsOutputter
 * @see SelectableFieldsOutputter
 * @see VelocityResultsOutputter
 * @see ExcelResultsOutputter
 * 
 * @author dmartin
 */
public interface ResultsOutputter {

	/**
	 * Write out a single result to the output stream/buffer that this 
	 * object wraps.
	 * 
	 * @param beans the beans required to output a single result.
	 */
	public void write(Map beans) throws IOException;
}