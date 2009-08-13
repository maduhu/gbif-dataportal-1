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
package org.gbif.portal.web.controller.openmodeller;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Client code for OMWebService
 *
 * @author Dave Neufeld
 * @author Dave Martin
 */
public class OmWSClientProxy {
	
  protected static Log logger = LogFactory.getLog(OmWSClientProxy.class);	
	
  public static int MODEL_SERVER_READY = 1;
  public static int MODEL_PROGRESS_COMPLETE = 100;
  public static int MODEL_PROGRESS_QUEUED = -1;
  public static int MODEL_PROGRESS_ABORTED = -2;
  
  private String omService;
  
  public OmWSSOAPRequestUtil omwsSOAPRequestUtil = new OmWSSOAPRequestUtil();
 
  /**
   * Execute the remote openModeller request
   * @return the response from the openModeller web service
   */        
  public String executeOMwsRequest(String soapRequest) {
	  PostMethod httppost = null;	
	  String omwsResponse = "Response FAILED";
	  try 
	  {
		logger.debug("Sending post request to: "+this.omService);
		httppost = new PostMethod(this.omService);	
	    HttpClient client = new HttpClient();
	        
	    httppost.setRequestEntity(new StringRequestEntity(soapRequest, null, null));
        client.executeMethod(httppost);
        if (httppost.getStatusCode() == HttpStatus.SC_OK) {
        	omwsResponse = httppost.getResponseBodyAsString();
        } else {
        	omwsResponse = httppost.getStatusLine().toString();
        }
        return omwsResponse;
	  }
	  catch (Exception e)
	  {
		logger.error(e.getMessage(), e);
		return omwsResponse;
	  }
	  finally
	  {
		httppost.releaseConnection();
	  }
	  
  }
  
  /**
   * Ping the openModeller web service to ensure it's up and running
   */        
  public void ping() {

	  String soapRequest = omwsSOAPRequestUtil.getPingRequest();
	  String omwsResponse = this.executeOMwsRequest(soapRequest);
	  omwsSOAPRequestUtil.parseXMLResponseSAX(omwsResponse);
	  OmWSSAXParser omwsSAXParser = omwsSOAPRequestUtil.getOMwsSAXParser();
      if (omwsSAXParser.getPingResponse().equals(Integer.toString(MODEL_SERVER_READY))) 
	  {
    	logger.debug("OMWS Service is ready.");
	  }
	  else 
	  {
		logger.debug("OMWS Service is offline.");
	  }
  }

  /**
   * Query the openModeller web service for available model layers
   */        
  public void getLayers() {
	  String soapRequest = omwsSOAPRequestUtil.getLayersRequest();
	  String omwsResponse = this.executeOMwsRequest(soapRequest);  

	  logger.debug(omwsResponse);
	  omwsSOAPRequestUtil.parseXMLResponseSAX(omwsResponse);
	  OmWSSAXParser omwsSAXParser = omwsSOAPRequestUtil.getOMwsSAXParser();
	  Hashtable layersHt = omwsSAXParser.getResultsAsHashtable();
	  Enumeration keys = layersHt.keys();
	  String label = null;
      // Printout results from Hashtable
	  logger.debug("OMWS Available layers:");
	  try {
//      FileWriter out = new FileWriter("c:/temp/omlayers.txt");


	  while(keys.hasMoreElements())
	  {
		  label = (String) keys.nextElement();
		  String layer = (String) layersHt.get(label);
		  logger.debug("label=" + label + ";layer=" + layer);
	      //out.write("label=" + label + ";layer=" + layer + "\n");

	  }
//      out.flush();
//      out.close();
	  } catch (Exception e) {
		  logger.error(e.getMessage(), e);
	  }
  }

  /**
   * Query the openModeller web service for available model algorithms
   */        
  public void getAlgorithms() {
	  String soapRequest = omwsSOAPRequestUtil.getAlgorithmsRequest();
	  String omwsResponse = this.executeOMwsRequest(soapRequest);  

	  omwsSOAPRequestUtil.parseXMLResponseSAX(omwsResponse);
	  OmWSSAXParser omwsSAXParser = omwsSOAPRequestUtil.getOMwsSAXParser();
	  Hashtable resultsHt = omwsSAXParser.getResultsAsHashtable();
	  Enumeration keys = resultsHt.keys();
	  String id = null;
      // Printout results from Hashtable
	  System.out.println("OMWS Available algorithms:");
	  while(keys.hasMoreElements())
	  {
		  id = (String) keys.nextElement();
		  ArrayList parametersList = (ArrayList) resultsHt.get(id);
		  String parameters="";
		  for (int i=0; i < parametersList.size(); i++) 
		  {
			  if (i<parametersList.size()-1)
			  {
			    parameters += parametersList.get(i) + ",";
			  }
			  else
			  {
				parameters += parametersList.get(i);
			  }
		  
		  }
		  System.out.println("algorithm id=" + id + ";parametersList=" + parameters);
	  }
  }

  /**
   * Create a specific model instance to execute on the openModeller web service
   * @return the id of the model created
   */        
  public String createModel()  {
      // Set up the model parameters
	  OMModel omModel = new OMModel();
	  omModel.addLayer("/system/modelagem/layers/publico/clima/WORLDCLIM/precipitacao/prec_10min/prec_1");
	  omModel.addLayer("/system/modelagem/layers/publico/clima/WORLDCLIM/temperatura/tmax_10min/tmax_1");
	  omModel.setMaskId("/system/modelagem/layers/publico/clima/WORLDCLIM/temperatura/tmax_10min/tmax_1");
	  omModel.addPoint("1","-68.85","-11.15");
	  omModel.addPoint("2","-67.38","-14.32");
	  omModel.addPoint("3","-67.15","-15.52");
	  omModel.addPoint("4","-65.12","-16.73");
	  omModel.addPoint("5","-63.17","-17.80");
	  omModel.setAlgorithm("0.2","Bioclim");
	  omModel.addParameter("StandardDeviationCutoff","0.7");
	  String soapRequest = omwsSOAPRequestUtil.getCreateModelRequest(omModel);
	  logger.debug(soapRequest);
	  String omwsResponse = this.executeOMwsRequest(soapRequest);
	  logger.debug(omwsResponse);
	  omwsSOAPRequestUtil.parseXMLResponseSAX(omwsResponse);
	  OmWSSAXParser omwsSAXParser = omwsSOAPRequestUtil.getOMwsSAXParser();
      //System.out.println("The model params are: " + omwsSAXParser.getModelParams());
	  logger.debug("The model ticket is: " + omwsSAXParser.getJobResponse());
	  return omwsSAXParser.getJobResponse();
  }  

  /**
   * Check progress of the model request
   * a value ranging from 0 to 100 is returned 100 represents 
   * that the model has finished processing 
   */        
  public int getModelProgress(String ticket) {

	  String soapRequest = omwsSOAPRequestUtil.getModelProgressRequest(ticket);
	  logger.debug(soapRequest);
	  String omwsResponse = this.executeOMwsRequest(soapRequest);
	  logger.debug(omwsResponse);
	  omwsSOAPRequestUtil.parseXMLResponseSAX(omwsResponse);
	  OmWSSAXParser omwsSAXParser = omwsSOAPRequestUtil.getOMwsSAXParser();
	  String statusAsString = omwsSAXParser.getModelProgressResponse();
	  try{
		  return Integer.parseInt(statusAsString);
	  } catch(Exception e){
		  logger.debug(e.getMessage(), e);
	  }
	  return MODEL_PROGRESS_QUEUED;
  }

  /**
   * Print out an XML representation of the model
   * @return the XML model representation
   */        
  public String getModel(String ticket) {
	  String soapRequest = omwsSOAPRequestUtil.getModelRequest(ticket);
	  String omwsResponse = this.executeOMwsRequest(soapRequest);
	  logger.debug(omwsResponse);
	  omwsSOAPRequestUtil.parseXMLResponseSAX(omwsResponse);
	  OmWSSAXParser omwsSAXParser = omwsSOAPRequestUtil.getOMwsSAXParser();
	  if(logger.isDebugEnabled()){
		  logger.debug("OMWS model params: " + omwsSAXParser.getModelParams());
	  }
	  return omwsResponse;
  }

  /**
   * Project the model, this is when the model is processed based on all of the 
   * required input parameters
   * @param the model information holder
   * @return the id of the projected model
   */        
  public String projectModel(OMModel omModel) {

	  //omModel.addParameter("StandardDeviationCutoff","0.7");
	  String soapRequest = omwsSOAPRequestUtil.getProjectModelRequest(omModel);
	 logger.debug(soapRequest);
	  String omwsResponse = this.executeOMwsRequest(soapRequest);
	 logger.debug(omwsResponse);
	  
	  omwsSOAPRequestUtil.parseXMLResponseSAX(omwsResponse);
	  OmWSSAXParser omwsSAXParser = omwsSOAPRequestUtil.getOMwsSAXParser();
	 logger.debug("The projected model ticket is: " + omwsSAXParser.getJobResponse());
	  return omwsSAXParser.getJobResponse();
  } 

  /**
   * Gets the projected model results as an erdas imagine image file 
   * @param the id of the projected model (assumes model project is complete)
   * @return the URL for the result image
   */        
  public String getLayerAsUrl(String id) {

	  String soapRequest = omwsSOAPRequestUtil.getLayerAsURLRequest(id);
	 logger.debug(soapRequest);
	  String omwsResponse = this.executeOMwsRequest(soapRequest);
	 logger.debug(omwsResponse);

	  omwsSOAPRequestUtil.parseXMLResponseSAX(omwsResponse);
	  OmWSSAXParser omwsSAXParser = omwsSOAPRequestUtil.getOMwsSAXParser();
      return omwsSAXParser.getLayerAsUrlResponse();
  }

  /**
   * Gets the remote openModeller log associated with the model
   * @param the id of the model 
   * @return the text from the openModeller log file
   */ 
  public void getLog(String ticket) {

	  String soapRequest = omwsSOAPRequestUtil.getLogRequest(ticket);
	 logger.debug(soapRequest);
	  String omwsResponse = this.executeOMwsRequest(soapRequest);
	 logger.debug("log: " + omwsResponse);
  } 

  public static void main(String[] args){
	  OmWSClientProxy p = new OmWSClientProxy();
	  p.ping();
//	  p.getLayers();
	  
	  p.getLayerAsUrl("/system/modelagem/layers/publico/clima/IPCC/futuro/ccc_integer/diurnal_temp_range/mean_temp/info/");
  }

	/**
	 * @param omService the omService to set
	 */
	public void setOmService(String omService) {
		this.omService = omService;
	}
	
	/**
	 * @param omwsSOAPRequestUtil the omwsSOAPRequestUtil to set
	 */
	public void setOmwsSOAPRequestUtil(OmWSSOAPRequestUtil omwsSOAPRequestUtil) {
		this.omwsSOAPRequestUtil = omwsSOAPRequestUtil;
	}
}