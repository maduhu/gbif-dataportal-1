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
package org.gbif.portal.dao;

import java.util.Map;

import org.hibernate.ScrollableResults;
import org.hibernate.Session;

/**
 * Implementations are used to lazy load associations in batches for performance.
 * 
 * @author dmartin
 */
public interface AssociationTraverser {
	/**
	 * Load any required associations for the batch.
	 * @param batchSize
	 * @param scrollableResults
	 * @param session
	 */
	public void batchPreprocess(int batchSize, ScrollableResults scrollableResults, Session session);

	/**
	 * Load associations and any other objects for this record and add with name
	 * to a Map for lookups.
	 * @param object
	 * @param session
	 * @return
	 */
	public Map<String, Object> traverse(Object object, Session session);
	
	/**
	 * Clear up associations for this batch.
	 * @param batchSize
	 * @param scrollableResults
	 * @param session
	 */
	public void batchPostprocess(int batchSize, ScrollableResults scrollableResults, Session session);
	
	/**
	 * Called at the end of the entire process allowing tidy up.
	 */
	public void reset();
}
