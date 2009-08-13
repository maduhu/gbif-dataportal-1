package org.gbif.portal.web.controller.openmodeller;

//XML Parser Packages
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * Utility class to help formulate openModeller xml/soap requests
 * @author Dave Neufeld
 */
public class OmWSSOAPRequestUtil {
	
	private OmWSSAXParser omwsSAXParser;

	/**
	* Constuct and openModeller request utility
	*/     
    public OmWSSOAPRequestUtil() {

	}

	/**
	* Ping request
	* @return a ping SOAP request
	*/ 
    public String getPingRequest() {
	    String pingRequest = "<?xml version=\"1.0\" encoding=\"iso-8859-1\"?>" +
			"<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"" +
			"xmlns:soapenc=\"http://schemas.xmlsoap.org/soap/encoding/\" +" +
			"xmlns:omws=\"http://openmodeller.cria.org.br/ws/1.0\"" +
			"xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\"" +
			"soap:encodingStyle=\"http://schemas.xmlsoap.org/soap/encoding/\"" +
			"xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">" +
			"<soap:Body>" +
			"<omws:ping xsi:nil=\"true\" xmlns:omws=\"http://openmodeller.cria.org.br/ws/1.0\" /> </soap:Body> </soap:Envelope>";
	      	
    	return pingRequest;
    }

    /**
	* Get layers request
	* @return a getLayers SOAP request
	*/     
    public String getLayersRequest() {
    	String getLayersRequest = "<?xml version=\"1.0\" encoding=\"iso-8859-1\"?><soap:Envelope " +
    		"xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"" +
    		"xmlns:soapenc=\"http://schemas.xmlsoap.org/soap/encoding/\"" +
    		"xmlns:omws=\"http://openmodeller.cria.org.br/ws/1.0\"" +
    		"xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\"" +
    		"soap:encodingStyle=\"http://schemas.xmlsoap.org/soap/encoding/\"" +
    		"xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">" +
    		"<soap:Body>" +
    		"<omws:getLayers xsi:nil=\"true\" xmlns:omws=\"http://openmodeller.cria.org.br/ws/1.0\" /> </soap:Body> </soap:Envelope>";
    	return getLayersRequest;
    }

    /**
	* Get algorithms request
	* @return a getAlgorithms SOAP request
	*/ 
    public String getAlgorithmsRequest() {
    	String getLayersRequest = "<?xml version=\"1.0\" encoding=\"iso-8859-1\"?><soap:Envelope " +
    		"xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"" +
    		"xmlns:soapenc=\"http://schemas.xmlsoap.org/soap/encoding/\"" +
    		"xmlns:omws=\"http://openmodeller.cria.org.br/ws/1.0\"" +
    		"xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\"" +
    		"soap:encodingStyle=\"http://schemas.xmlsoap.org/soap/encoding/\"" +
    		"xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">" +
    		"<soap:Body>" +
    		"<omws:getAlgorithms xsi:nil=\"true\" xmlns:omws=\"http://openmodeller.cria.org.br/ws/1.0\" /> </soap:Body> </soap:Envelope>";
    	return getLayersRequest;
    }
    
	/**
	* Create model request
	* @param the information holder for the model
	* @return a create model SOAP request
	*/     
    public String getCreateModelRequest(OMModel omModel) {
    	String initialCreateModelRequest = "<?xml version=\"1.0\" encoding=\"iso-8859-1\"?> <soap:Envelope " + 
    		"xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" " +
    		"xmlns:soapenc=\"http://schemas.xmlsoap.org/soap/encoding/\" " + 
    		"xmlns:omws=\"http://openmodeller.cria.org.br/ws/1.0\" " +
    		"xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" " +
    		"soap:encodingStyle=\"http://schemas.xmlsoap.org/soap/encoding/\" " +
    		"xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">" +
    		"<soap:Body>" +
    		"<omws:createModel" +
    		"  xmlns:omws=\"http://openmodeller.cria.org.br/ws/1.0\"" +
    		"  soap:encodingStyle=\"http://xml.apache.org/xml-soap/literalxml\">" +
    		"<ModelParameters" +
    		"  xmlns=\"http://openmodeller.cria.org.br/xml/1.0\">" +
    		"  <Sampler>" +
    	    "<Environment>";
    	StringBuffer modelRequestBuffer = new StringBuffer(initialCreateModelRequest);
    	List<String> layers = omModel.getLayers();
    	for (int i=0; i<layers.size(); i++) 
    	{
    		modelRequestBuffer.append("  <Map Id=\"" + (String) layers.get(i) + "\" IsCategorical=\"0\" />");
    	}
    	modelRequestBuffer.append("  <Mask Id=\"" + omModel.getMaskId() + "\" />");
    	modelRequestBuffer.append("</Environment>");
    	modelRequestBuffer.append("<Presence Label=\"GBIF Occurence Data\">");
    	modelRequestBuffer.append("<CoordinateSystem>GEOGCS[\"WGS 84\",DATUM[\"WGS_1984\",SPHEROID[\"WGS 84\",6378137,298.257223563,AUTHORITY[\"EPSG\",\"7030\"]],TOWGS84[0,0,0,0,0,0,0],AUTHORITY[\"EPSG\",\"6326\"]],PRIMEM[\"Greenwich\",0,AUTHORITY[\"EPSG\",\"8901\"]],UNIT[\"degree\",0.0174532925199433,AUTHORITY[\"EPSG\",\"9108\"]],AXIS[\"Lat\",NORTH],AXIS[\"Long\",EAST],AUTHORITY[\"EPSG\",\"4326\"]]</CoordinateSystem>");
    	List<Point> points = omModel.getPoints();
    	for (int i=0; i<points.size(); i++) 
    	{
    		Point pt = (Point) points.get(i);
    		modelRequestBuffer.append("  <Point Id=\"" + pt.id + "\" X=\"" + pt.x + "\"" + " Y=\"" + pt.y + "\"  />");
    	}
   		modelRequestBuffer.append("</Presence>");
   		modelRequestBuffer.append("</Sampler>");
   		modelRequestBuffer.append("<Algorithm Version=\"" + omModel.getAlgorithmVersion() + "\" Id=\"" + omModel.getAlgorithmId() + "\">");
   		modelRequestBuffer.append("  <Parameters>");
   		List parameters = omModel.getParameters();
    	for (int i=0; i<parameters.size(); i++) 
    	{
    		Parameter param = (Parameter) parameters.get(i);
    		modelRequestBuffer.append("  <Parameter Value=\"" + param.val + "\" Id=\"" + param.id + "\"  />");
    	}   		

   		modelRequestBuffer.append("  </Parameters>");
   		modelRequestBuffer.append("</Algorithm>");
   		modelRequestBuffer.append("<Statistics/>");
   		modelRequestBuffer.append("</ModelParameters>");
   		modelRequestBuffer.append("</omws:createModel>");
   		modelRequestBuffer.append("</soap:Body>");
   		modelRequestBuffer.append("</soap:Envelope>");
    	return modelRequestBuffer.toString();
    }    
    
	/**
	* Generate a request to check the progress of the model
	* @param the id for the model request
	* @return the getModelProgress SOAP request
	*/     
    public String getModelProgressRequest(String ticket) {
	    String modelProgressRequest = "<?xml version=\"1.0\" encoding=\"iso-8859-1\"?>" +
			"<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" " +
			"xmlns:soapenc=\"http://schemas.xmlsoap.org/soap/encoding/\" " +
			"xmlns:omws=\"http://openmodeller.cria.org.br/ws/1.0\" " +
			"xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" " +
			"soap:encodingStyle=\"http://schemas.xmlsoap.org/soap/encoding/\" " +
			"xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">" +
			"<soap:Body>" +
			"<getProgress xmlns=\"http://openmodeller.cria.org.br/ws/1.0\">" +
			"<ticket>" + ticket + "</ticket>" +
			"</getProgress></soap:Body></soap:Envelope>";
	      	
    	return modelProgressRequest;
    }
    
	/**
	* Generate a request to retrieve an XML representation of the model
	* @param the id for the model request
	* @return the getModel SOAP request
	*/         
    public String getModelRequest(String ticket) {
	    String modelRequest = "<?xml version=\"1.0\" encoding=\"iso-8859-1\"?>" +
			"<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" " +
			"xmlns:soapenc=\"http://schemas.xmlsoap.org/soap/encoding/\" " +
			"xmlns:omws=\"http://openmodeller.cria.org.br/ws/1.0\" " +
			"xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" " +
			"soap:encodingStyle=\"http://schemas.xmlsoap.org/soap/encoding/\" " +
			"xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">" +
			"<soap:Body>" +
			"<getModel xmlns=\"http://openmodeller.cria.org.br/ws/1.0\">" +
			"<ticket>" + ticket + "</ticket>" +
			"</getModel></soap:Body></soap:Envelope>";
	      	
    	return modelRequest;
    }
    
	/**
	* Generate a request to project the model
	* @param the information holder for the model
	* @return the getModelProgress SOAP request
	*/     
    public String getProjectModelRequest(OMModel omModel) {
    	String initialProjectModelRequest = "<?xml version=\"1.0\" encoding=\"iso-8859-1\"?> <soap:Envelope " + 
    		"xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" " +
    		"xmlns:soapenc=\"http://schemas.xmlsoap.org/soap/encoding/\" " + 
    		"xmlns:omws=\"http://openmodeller.cria.org.br/ws/1.0\" " +
    		"xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" " +
    		"soap:encodingStyle=\"http://schemas.xmlsoap.org/soap/encoding/\" " +
    		"xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">" +
    		"<soap:Body>" +
    		"<omws:projectModel" +
    		"  xmlns:omws=\"http://openmodeller.cria.org.br/ws/1.0\"" +
    		"  soap:encodingStyle=\"http://xml.apache.org/xml-soap/literalxml\">" +
    		"<ProjectionParameters" +
    		"  xmlns=\"http://openmodeller.cria.org.br/xml/1.0\">"; 

    	StringBuffer modelRequestBuffer = new StringBuffer(initialProjectModelRequest);
    	modelRequestBuffer.append("<Algorithm Id=\"" + omModel.getAlgorithmId() + "\" Version=\"" + omModel.getAlgorithmVersion() + "\">");
    	modelRequestBuffer.append("  <Parameters>");  
   		List parameters = omModel.getParameters();
    	for (int i=0; i<parameters.size(); i++) 
    	{
    		Parameter param = (Parameter) parameters.get(i);
    		modelRequestBuffer.append("  <Parameter Id=\"" + param.id + "\" Value=\"" + param.val + "\"  />");
    	}    	
    	modelRequestBuffer.append("  </Parameters>"); 
    	modelRequestBuffer.append("<Model>");
    	
    	modelRequestBuffer.append(omModel.getModelParams());
    	modelRequestBuffer.append("</Model>");
    	modelRequestBuffer.append("</Algorithm>"); 
    	
    	List<String> layers = omModel.getLayers();
    	modelRequestBuffer.append("<Environment NumLayers=\"" + layers.size() +"\">");
    	for (int i=0; i<layers.size(); i++) 
    	{
    		modelRequestBuffer.append("  <Map Id=\"" + (String) layers.get(i) + "\" IsCategorical=\"0\" />");
    	}
    	modelRequestBuffer.append("  <Mask Id=\"" + omModel.getMaskId() + "\" />");
    	modelRequestBuffer.append("</Environment>");
    	modelRequestBuffer.append("  <OutputParameters FileType=\"ByteHFA\">");
    	modelRequestBuffer.append("    <TemplateLayer Id=\"" + omModel.getTemplateLayerId() + "\"/>");
    	modelRequestBuffer.append("  </OutputParameters>");    	

   		modelRequestBuffer.append("</ProjectionParameters>");
   		modelRequestBuffer.append("</omws:projectModel>");
   		modelRequestBuffer.append("</soap:Body>");
   		modelRequestBuffer.append("</soap:Envelope>");
    	return modelRequestBuffer.toString();
    }  
    
	/**
	* Generate a request to retrieve the model results as a layer URL
	* @param the id for the projected model request
	* @return the getLayerAsURL SOAP request
	*/         
    public String getLayerAsURLRequest(String id) {
	    String modelRequest = "<?xml version=\"1.0\" encoding=\"iso-8859-1\"?>" +
			"<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" " +
			"xmlns:soapenc=\"http://schemas.xmlsoap.org/soap/encoding/\" " +
			"xmlns:omws=\"http://openmodeller.cria.org.br/ws/1.0\" " +
			"xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" " +
			"soap:encodingStyle=\"http://schemas.xmlsoap.org/soap/encoding/\" " +
			"xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">" +
			"<soap:Body>" +
			"<getLayerAsUrl xmlns=\"http://openmodeller.cria.org.br/ws/1.0\" soap:encodingStyle=\"http://xml.apache.org/xml-soap/literalxml\">" +
			"<id xsi:type=\"xsd:string\">" + id + "</id>" +
			"</getLayerAsUrl></soap:Body></soap:Envelope>";
	      	
    	return modelRequest;
    }
    
    public String getLogRequest(String ticket) {
	    String modelRequest = "<?xml version=\"1.0\" encoding=\"iso-8859-1\"?>" +
			"<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" " +
			"xmlns:soapenc=\"http://schemas.xmlsoap.org/soap/encoding/\" " +
			"xmlns:omws=\"http://openmodeller.cria.org.br/ws/1.0\" " +
			"xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" " +
			"soap:encodingStyle=\"http://schemas.xmlsoap.org/soap/encoding/\" " +
			"xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">" +
			"<soap:Body>" +
			"<getLog xmlns=\"http://openmodeller.cria.org.br/ws/1.0\">" +
			"<ticket>" + ticket + "</ticket>" +
			"</getLog></soap:Body></soap:Envelope>";
	      	
    	return modelRequest;
    }    
    
    /**
     * Parse the xmlResponse using SAX
     * @param xmlRequestString	the xmlRequest string
     */
    public void parseXMLResponseSAX(String xmlResponseString) {
          // Create a SAX Parser
          SAXParserFactory factory = SAXParserFactory.newInstance();
          try {
              SAXParser saxParser = factory.newSAXParser();

    	      // Create input source from request string
    	      ByteArrayInputStream bais = new ByteArrayInputStream(xmlResponseString.getBytes());

              // Parse the Document
    	      omwsSAXParser = new OmWSSAXParser();
              saxParser.parse(new InputSource(bais), omwsSAXParser);
    	      //Hashtable resultsHt = omwsSAXParser.getResultsAsHashtable();
    	      //System.out.println("resultsHt.size()=" + resultsHt.size());
          } catch (ParserConfigurationException pce) {
              System.err.println (pce);
          } catch (SAXException se) {
              System.err.println (se);
          } catch (IOException ioe) {
              System.err.println (ioe);
          }

    } 	

    /**
     * Clean-up unsafe XML characters
     * @param xmlString	the xml string
     * @param boolean operator requesting decode of string
     */
    public String cleanup(String str, boolean decode) {
    	String saXMLEquivalent[] = {"&amp;", "&apos;", "&quot;", "&lt;", "&gt;"};
        String saSpecialChars[] = {"&", "\'", "\"", "<", ">"};
        String sFind;
        String sReplace;
        boolean bFound;
        int iPos = -1;
        int i = 0;
     
        while (i < saXMLEquivalent.length) {
            String newStr = "";
            if (decode) {
                //Search for XML encodeded string and convert it back to plain ASCII
                sFind = saXMLEquivalent[i];
                sReplace = saSpecialChars[i];
             } else {
                 //Search for special chars in ASCII and replace with XML safe chars
                 sFind = saSpecialChars[i];
                 sReplace = saXMLEquivalent[i];
             }
             do {
                 iPos = str.indexOf(sFind, ++iPos);
                 if (iPos > -1) {
                     newStr = newStr + str.substring(0, iPos) + sReplace + str.substring(iPos+sFind.length(),str.length());
                     str = newStr;
                     newStr = "";
                     bFound = true;
                 } else {
                     bFound = false;
                 }
             } while ( bFound );
             i++;
         }
         return(str);
    }
    
    	
    public OmWSSAXParser getOMwsSAXParser() {
    	return this.omwsSAXParser;
    }
}