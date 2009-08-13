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

package org.gbif.portal.web.filter;

import java.util.LinkedList;

import junit.framework.TestCase;

/**
 *
 * @author Dave Martin
 */
public class CriterionTest extends TestCase {

	public void testEquals(){
		CriterionDTO criterion1 = new CriterionDTO("1", "0", "1.0");
		CriterionDTO criterion2 = new CriterionDTO("1", "0", "1.0");
		assertTrue(criterion1.equals(criterion2));
	}

	@SuppressWarnings("unchecked")
	public void testContains(){
		CriterionDTO criterion1 = new CriterionDTO("1", "0", "1.0");
		CriterionDTO criterion2 = new CriterionDTO("1", "0", "1.0");
		LinkedList list = new LinkedList();
		list.add(criterion1);
		assertTrue(list.contains(criterion2));
		
	}
}
