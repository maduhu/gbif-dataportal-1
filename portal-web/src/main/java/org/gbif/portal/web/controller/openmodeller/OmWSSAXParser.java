package org.gbif.portal.web.controller.openmodeller;

//XML Parser Packages
import java.util.ArrayList;
import java.util.Hashtable;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * XML parsing class to grab need information from the openModeller response xml
 * @author Dave Neufeld
 */
public class OmWSSAXParser extends DefaultHandler {
	
	private int responseType = -1;
	private String pingValue = "0";	
	private String labelValue = "NONE";
	private String layerValue = "NONE";	
	private String algorithmValue = "NONE";
	private String parameterValue = "NONE";
	private ArrayList parametersList = null;	
	private String ticketValue = "NONE";
	private String modelParamsValue = "NONE";
	private String progressValue = "NONE";
	private String urlValue = "NONE";
	private boolean labelEntry = false;
	private boolean algorithmEntry = false;
	private boolean parametersEntry = false;
	private boolean ticketEntry = false;
	private boolean progressEntry = false;
	private boolean modelParamEntry = false;	
	private boolean urlEntry = false;
	private Hashtable resultsHt = new Hashtable();
	
	/**
	 * Constructor for OMwsSAXParser
	 */
	public OmWSSAXParser() {
	  super();
	}

	/**
	 * Start Element handler code
	 *
	 * @param uri	 	 URI String
	 * @param local	 local String
	 * @param qName	 qName String
	 * @param atts 	 Attributes of XML element
	 */
	public void startElement(String uri, String local, String qName, Attributes atts)  {
	  if (qName.equals("status")) 
	  {
		responseType = OmWSResponseTypes.PINGRESPONSE;
	  } 
	  if (qName.equals("Label")) 
	  {
	  	labelEntry = true;
	  }
	        
	  if (qName.equals("Layer")) 	 		  
	  {
		responseType = OmWSResponseTypes.GETLAYERSRESPONSE;
	   	if (atts.getValue("Id")!=null) 
	    {
	      layerValue = (String) atts.getValue("Id");
	    }
	  }	
	  if (qName.equals("Algorithm")) 	 		  
	  {
		responseType = OmWSResponseTypes.GETALGORITHMSRESPONSE;	 
		algorithmEntry = true;
		parametersList = new ArrayList();
		if (atts.getValue("Id")!=null) 
	    {
		  algorithmValue = (String) atts.getValue("Id");
	    }
	  }	
	  if (qName.equals("Parameter")) 	 		  
	  {
		parametersEntry = true;		
	   	if (atts.getValue("Id")!=null) 
	    {
	   	  parameterValue = (String) atts.getValue("Id");
		  parametersList.add(parameterValue);
	    }
	  }	
	  if (qName.equals("ticket")) 
	  {
		responseType = OmWSResponseTypes.OMJOBRESPONSE;
		ticketEntry = true;
	  }
	  if (qName.equals("progress")) 
	  {
		responseType = OmWSResponseTypes.OMMODELPROGRESSRESPONSE;
		progressEntry = true;
	  }	
	  if (modelParamEntry==true) {
		  modelParamsValue = "<" + qName +" ";
		  if (atts.getValue("Mean")!=null) modelParamsValue += "Mean=\"" + atts.getValue("Mean") + "\" ";
		  if (atts.getValue("StdDev")!=null) modelParamsValue += "StdDev=\"" + atts.getValue("StdDev") + "\" ";
		  if (atts.getValue("Minimum")!=null) modelParamsValue += "Minimum=\"" + atts.getValue("Minimum") + "\" ";
		  if (atts.getValue("Maximum")!=null) modelParamsValue += "Maximum=\"" + atts.getValue("Maximum")+ "\" ";
		  modelParamsValue += "/>";
		  //System.out.println("modelParamsValue=" + modelParamsValue);
	  }	  
	  if (qName.equals("Model")) 
	  {
		responseType = OmWSResponseTypes.OMMODELPARAMSRESPONSE;
		modelParamEntry = true;
		//System.out.println("modelParamEntry is true!!!");
	  }	  
	  if (qName.equals("url")) 
	  {
		responseType = OmWSResponseTypes.GETLAYERASURLRESPONSE;
		urlEntry = true;
	  }		  
	}
	
	/**
	 * Character handler code to retrieve values within tag
	 */
	public void characters(char[] ch, int start, int length) throws SAXException {
	  if (responseType==OmWSResponseTypes.PINGRESPONSE) 
	  {			  
	    pingValue = new String(ch, start, length);
	  }		
	  if (responseType==OmWSResponseTypes.GETLAYERSRESPONSE) 
	  {			  
	    labelValue = new String(ch, start, length);
	  }
	  if (responseType==OmWSResponseTypes.OMJOBRESPONSE) 
	  {			  
	    ticketValue = new String(ch, start, length);
	  }	 
	  if (responseType==OmWSResponseTypes.OMMODELPROGRESSRESPONSE) 
	  {			  
	    progressValue = new String(ch, start, length);
	  }	 	  
	  if (responseType==OmWSResponseTypes.GETLAYERASURLRESPONSE) 
	  {			  
	    urlValue = new String(ch, start, length);
	  }		  
	}

	/**
	 * endElement handler code
	 *
	 * @param uri	 	 URI String
	 * @param local	 local String
	 * @param qName	 qName String
	 */
	 public void endElement(String uri, String local, String qName)  {
		 
       if (labelEntry) 
       {
         if (!layerValue.equals("NONE")) resultsHt.put(labelValue,layerValue);
         labelEntry = false;
       }
	   
       if (algorithmEntry && parametersEntry) 
       {
         if (!algorithmValue.equals("NONE")) resultsHt.put(algorithmValue,parametersList);
         //System.out.println(algorithmValue +";" + parametersList);
         
         algorithmEntry = false;	
         parametersEntry = false;
       }
       
       if (ticketEntry) ticketEntry = false;      
       if (progressEntry) progressEntry = false;  
       if (modelParamEntry) {
    	   modelParamEntry = false;    
       }
       if (urlEntry) urlEntry = false; 
	 }
	   
	 /**
	 * Return response type
	 * @return type of response as int
	 *
     */
	 public int getResponseType()  {			
	   return responseType;
	 }

	 /**
     * Return ping response 
     * @return ping response as String
	 *
	 */
	 public String getPingResponse()  {			
       return pingValue;
	 }

	 /**
	 * Return job ticket
	 * @return job ticket as String
     *
     */
	 public String getJobResponse()  {			
	   return ticketValue;
	 }	

	 /**
	 * Return model progress value
	 * @return progress as String
	 *
	 */
	 public String getModelProgressResponse()  {			
	   return progressValue;
	 }	

	 /**
	  * Return model progress value
	  * @return progress as String
	  *
	  */
	 public String getModelParams()  {			
	   return modelParamsValue;
	 }	
		 
	 /**
	 * Return generated model layer url value
	 * @return url as String
	 *
	 */
	 public String getLayerAsUrlResponse()  {			
	   return urlValue;
	 }	
	 
	 /**
	 * Returns OMWS layers as a Hashtable
	 * @return Hashtable of layers
	 *
	 */
	 public Hashtable getResultsAsHashtable()  {			
	   return resultsHt;
	 }
	 
	 /**
	 * Warning handler method
	 * @param spe	 spe SAXParseException
	 */

	 public void warning(SAXParseException spe) throws SAXException {
	     // throw
	     String details = new String("Parsing Warning on Line: " +
	        spe.getLineNumber() + " (" + spe.getMessage() + ")");
	     System.err.println(details);
	 }

	/**
	 * Error handler method
	 * @param spe	 spe SAXParseException
	 */
	 public void error(SAXParseException spe) throws SAXException {
	   // throw
	   String details = new String("Parsing Error on Line: " +
	        spe.getLineNumber() + " (" + spe.getMessage() + ")");
	   System.err.println(details);
	   throw new SAXException("Parsing Error (" + details + ")", spe);
	 }

	/**
	 * FatalError handler method
	 * @param spe	 spe SAXParseException
	 */
	 public void fatalError(SAXParseException spe) throws SAXException {
	   // throw
	   String details = new String("Fatal Parsing Error on Line: " +
	   spe.getLineNumber() + " (" + spe.getMessage() + ")");
	   System.err.println(details);
	   throw new SAXException("Fatal Parsing Error (" + details + ")", spe);
	 }
}