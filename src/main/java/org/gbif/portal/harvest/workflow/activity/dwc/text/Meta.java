/**
 * 
 */
package org.gbif.portal.harvest.workflow.activity.dwc.text;

import java.io.File;
import java.net.MalformedURLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.XPath;
import org.dom4j.io.SAXReader;
import org.dom4j.xpath.DefaultXPath;

/**
 * Represents a meta file as per the DwC Text Guidelines
 * @author tim
 */
public class Meta {
	protected Collection<DwCFile> files = new LinkedList<DwCFile>();
	protected static Log log = LogFactory.getLog(Meta.class);
	
	protected static Map<String, String> namespaceMapping = new HashMap<String, String>();
	static {
		// IPT-RC1 
		namespaceMapping.put("dwc-ta", "http://rs.tdwg.org/dwc/terms/xsd/archive/");
		
		// DwC in public review:
		//namespaceMapping.put("dwc-ta", "http://rs.tdwg.org/dwc/text/");
	}
	
	/**
	 * Factory to build from an XML file
	 * @param source Must be an XML file
	 * @return the meta
	 * @throws DocumentException 
	 * @throws MalformedURLException 
	 */
	@SuppressWarnings("unchecked")
	public static Meta build(File source) throws MalformedURLException, DocumentException {
		log.debug("Building metadata");
		Meta meta = new Meta();
		
		SAXReader xmlReader = new SAXReader();
		Document doc = xmlReader.read(source);
		
	    XPath xpath = new DefaultXPath("/dwc-ta:archive");
	    xpath.setNamespaceURIs(namespaceMapping);
	    Element archive = (Element)xpath.selectSingleNode(doc);
	    String fileRoot = "";
	    if (archive.attributeValue("fileRoot") != null)
	    	fileRoot = archive.attributeValue("fileRoot");
	    log.debug("File root: " + fileRoot);
	    
	    xpath = new DefaultXPath("/dwc-ta:archive/dwc-ta:file");
	    xpath.setNamespaceURIs(namespaceMapping);	    
	    List<Element> files = xpath.selectNodes(doc);
	    log.debug("Metafile contains " + files.size() + " described files");
	    for (Element file : files) {
	    	DwCFile dwcFile = meta.new DwCFile();
	    	
	    	// extract the File attributes
	    	if (file.attributeValue("encoding") != null) 
	    		dwcFile.setEncoding(file.attributeValue("encoding"));
	    	if (file.attributeValue("fieldsTerminatedBy") != null)
	    		dwcFile.setFieldTerminator(file.attributeValue("fieldsTerminatedBy"));
	    	String ignoreHeaderLines = file.attributeValue("ignoreHeaderLines");
	    	try {
				dwcFile.setHeaderLinesToIgnore(Integer.parseInt(ignoreHeaderLines));
			} catch (NumberFormatException e) {  // swallow null or bad value
			}
			if (file.attributeValue("linesTerminatedBy") != null)
				dwcFile.setLineTerminator(file.attributeValue("linesTerminatedBy"));
			if (file.attributeValue("location") != null)
				dwcFile.setLocation(fileRoot + file.attributeValue("location"));
			if (file.attributeValue("rowType") != null)
				dwcFile.setRowType(file.attributeValue("rowType"));
			
			// get the field mapping described
			List<Element> fields = file.elements();
			for (Element field : fields) {
				String term = field.attributeValue("term");
				String indexAsString = field.attributeValue("index");
				
				if (term != null && indexAsString != null) {
					int indexAsInt = Integer.parseInt(indexAsString);
					// let bad errors be thrown up
					// note it is an inverted index
					dwcFile.getFields().put(term, indexAsInt);
				}
			}
			
	    	meta.getFiles().add(dwcFile);
	    }
		
		return meta;
	}
	
	/**
	 * As described in the metafile
	 */
	public class DwCFile {
		String encoding;
		String fieldTerminator;
		String lineTerminator;
		int headerLinesToIgnore=0;
		String rowType;
		String location;
		Map<String, Integer> fields = new HashMap<String, Integer>();
		public String getEncoding() {
			return encoding;
		}
		public void setEncoding(String encoding) {
			this.encoding = encoding;
		}
		public String getFieldTerminator() {
			return fieldTerminator;
		}
		public void setFieldTerminator(String fieldTerminator) {
			this.fieldTerminator = fieldTerminator;
		}
		public String getLineTerminator() {
			return lineTerminator;
		}
		public void setLineTerminator(String lineTerminator) {
			this.lineTerminator = lineTerminator;
		}
		public int getHeaderLinesToIgnore() {
			return headerLinesToIgnore;
		}
		public void setHeaderLinesToIgnore(int headerLinesToIgnore) {
			this.headerLinesToIgnore = headerLinesToIgnore;
		}
		public String getRowType() {
			return rowType;
		}
		public void setRowType(String rowType) {
			this.rowType = rowType;
		}
		public String getLocation() {
			return location;
		}
		public void setLocation(String location) {
			this.location = location;
		}
		public Map<String, Integer> getFields() {
			return fields;
		}
		public void setFields(Map<String, Integer> fields) {
			this.fields = fields;
		}
	}





	public Collection<DwCFile> getFiles() {
		return files;
	}





	public void setFiles(Collection<DwCFile> files) {
		this.files = files;
	}
}
