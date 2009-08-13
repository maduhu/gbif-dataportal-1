package org.gbif.portal.xml.xpath;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.io.SAXReader;
import org.dom4j.xpath.DefaultXPath;

import junit.framework.TestCase;

/**
 * A test for XPAth or expressions to ensure they do what we expect
 * @author trobertson
 */
public class XPathOrTest extends TestCase {
	protected Document doc;
	
	protected void setUp() throws Exception {
		SAXReader xmlReader = new SAXReader();
		InputStream is = getClass().getResourceAsStream ("/org/gbif/portal/xml/xpath/DigirResponse.xml");
		doc = xmlReader.read(is);
	}

	public void testNoneThere() {
		DefaultXPath xpath = new DefaultXPath("//dwc:XXX | //dwc:YYY");
		Map<String, String> ns = new HashMap<String, String>();
		ns.put("dwc","http://digir.net/schema/conceptual/darwin/2003/1.0");
		xpath.setNamespaceURIs(ns);
		assertEquals("", xpath.valueOf(doc));
	}
	
	public void testBothThere() {
		DefaultXPath xpath = new DefaultXPath("//dwc:InstitutionCode | //dwc:CatalogNumber");
		Map<String, String> ns = new HashMap<String, String>();
		ns.put("dwc","http://digir.net/schema/conceptual/darwin/2003/1.0");
		xpath.setNamespaceURIs(ns);
		// Museo Nacional de Costa Rica (MNCR) is the Inst code
		assertEquals("Museo Nacional de Costa Rica (MNCR)", xpath.valueOf(doc));
	}
	
	public void testOneThere() {
		DefaultXPath xpath = new DefaultXPath("//dwc:InstitutionCode | //dwc:XXX");
		Map<String, String> ns = new HashMap<String, String>();
		ns.put("dwc","http://digir.net/schema/conceptual/darwin/2003/1.0");
		xpath.setNamespaceURIs(ns);
		// Museo Nacional de Costa Rica (MNCR) is the Inst code
		assertEquals("Museo Nacional de Costa Rica (MNCR)", xpath.valueOf(doc));
	}
}
