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
package org.gbif.portal.xml.xpath;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.io.SAXReader;
import org.dom4j.xpath.DefaultXPath;

/**
 * A quick tester
 * @author trobertson
 */
public class XPathTest {

	protected void orTest() {
		SAXReader xmlReader = new SAXReader();
		try {
			InputStream is = getClass().getClassLoader().getResourceAsStream ("org/gbif/portal/xml/xpath/DigirResponse.xml");
			Document doc = xmlReader.read(is);
			DefaultXPath xpath = new DefaultXPath("//dwc:InstitutionCode | //dwc:CatalogNumber");
			Map<String, String> ns = new HashMap<String, String>();
			ns.put("dwc","http://digir.net/schema/conceptual/darwin/2003/1.0");
			xpath.setNamespaceURIs(ns);
			System.out.println("Result: " + xpath.valueOf(doc));
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}
	
	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		XPathTest me = new XPathTest();
		me.orTest();
	}
}
