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

import java.io.InputStream;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.gbif.portal.web.controller.openmodeller.OMModel;
import org.gbif.portal.web.controller.openmodeller.OmWSClientProxy;
import org.gbif.portal.web.controller.openmodeller.OmWSSAXParser;

/**
 * Runnable that generates the model and writes the resulting image to file.
 */
public class OpenModellerFileWriter extends FileWriter {

	/** The open modeller endpoint to fire requests to */
	protected String openModellerEndpoint;
	
	/** the model id - used in the file name */
	protected String modelId;
	
	/** The template to use */
	protected String template;
	
	/** Timeout for progress request. */
	protected static long timeout = 3600000;
	
	/** Sleep time between progress requests. */
	protected static long sleepTime = 10000;
	
	/** The OMModel to use */
	protected OMModel omModel = null;
	
	/** The image file extension */
	protected String imgFileExtension = ".png";
	
	/**
	 * 1) Creates the model - polling for progress
	 * 2) Projects the moodel - polling fro progres
	 * 3) Downloads the generated image to a temp file
	 * 
	 * @see org.gbif.portal.web.download.FileWriter#writeFile()
	 */
	@Override
	public void writeFile() throws Exception {

	  OmWSClientProxy omClient = new OmWSClientProxy();
	  omClient.setOmService(openModellerEndpoint);
	  
	  String soapRequest = omClient.omwsSOAPRequestUtil.getCreateModelRequest(omModel);
	  if(logger.isDebugEnabled()){
	    logger.debug("Open Modeller request: "+soapRequest);
	  }
  
	  String displayRequest = omClient.omwsSOAPRequestUtil.cleanup(soapRequest, false);
	  String omwsResponse = omClient.executeOMwsRequest(soapRequest);
	  if(logger.isDebugEnabled()){
			logger.debug("Open Modeller response: "+omwsResponse);
	  }
  
	  omClient.omwsSOAPRequestUtil.parseXMLResponseSAX(omwsResponse);
	  OmWSSAXParser omwsSAXParser = omClient.omwsSOAPRequestUtil.getOMwsSAXParser();
	  String ticket = omwsSAXParser.getJobResponse();		
		
		
	  boolean success = pollforTicket(omClient, ticket);
	  if(!success){
		  logger.error("Model Generation Timeout at Create Model Stage !!!!!");
		  signalFileWriteFailure();
		  return;
	  }
		
	  //retrieve model
	  logger.debug("Show project model request...");
	  omwsResponse = omClient.getModel(ticket);

	  omClient.omwsSOAPRequestUtil.parseXMLResponseSAX(omwsResponse);
	  omwsSAXParser = omClient.omwsSOAPRequestUtil.getOMwsSAXParser();
	  String modelParams = omwsSAXParser.getModelParams();
	  omModel.setModelParams(modelParams);

	  soapRequest = omClient.omwsSOAPRequestUtil.getProjectModelRequest(omModel);
	  omClient.omwsSOAPRequestUtil.cleanup(soapRequest, false);
	  omwsResponse = omClient.executeOMwsRequest(soapRequest);
	  omClient.omwsSOAPRequestUtil.parseXMLResponseSAX(omwsResponse);
	  omwsSAXParser = omClient.omwsSOAPRequestUtil.getOMwsSAXParser();
	  
	  ticket = omwsSAXParser.getJobResponse();
	  if(logger.isDebugEnabled()){
		  logger.debug("Ticket: "+ticket);
	  }
	  
	  //poll for ticket
	  success = pollforTicket(omClient, ticket);

	  if(!success){
		  logger.error("Model Generation Timeout at Project Model Stage !!!!!");
		  signalFileWriteFailure();
		  return;
	  }
	  
	  String urlStr = omClient.getLayerAsUrl(ticket);
	  
	  if(logger.isDebugEnabled()){
		  logger.debug("Generated IMG =" + urlStr);
	  }
	  
	  urlStr = urlStr.substring(0,urlStr.length()-4) + imgFileExtension;
	  
	  if(logger.isDebugEnabled()){
		  logger.debug("PNG url =" + urlStr);
	  }
	  
	  HttpClient client  = new HttpClient();
	  HttpMethod method = new GetMethod(urlStr);
	  int state = client.executeMethod(method);
	  logger.debug(state);
	  
	  InputStream in = method.getResponseBodyAsStream();
	  byte[] buffer = new byte[1000];
	  int bytesRead = in.read(buffer);
	  while(bytesRead>0){
		  outputStream.write(buffer, 0, bytesRead);
		  bytesRead = in.read(buffer);
	  }
	  method.releaseConnection();
	  
	  logger.debug("Image written to file");
	  signalFileWriteComplete();
	}

	/**
	 * Poll the OpenModeller server for progress.
	 * 
	 * @param ticket
	 * @throws InterruptedException
	 */
	private static boolean pollforTicket(OmWSClientProxy omClient, String ticket) throws InterruptedException {
		
	  boolean success = false;	
	  boolean progressComplete = false;
		//poll for response
	  long startTime = System.currentTimeMillis();
	  long currentTime = System.currentTimeMillis();
	  
	  while(!progressComplete && (startTime+timeout)>currentTime){
		  logger.debug("Progress request...");
		  int status = omClient.getModelProgress(ticket);
		  
      if (status == OmWSClientProxy.MODEL_PROGRESS_COMPLETE){
	    	progressComplete = true;
	    	success = true;
		  }
  	  if(status == OmWSClientProxy.MODEL_PROGRESS_QUEUED){
  		  logger.debug("OM model is not complete. Progress: Queued");
  	  } else if(status == OmWSClientProxy.MODEL_PROGRESS_ABORTED){
  		  logger.debug("OM model is not complete. Progress: Aborted");
	      progressComplete = true;
	      success = false;
  	  } else {
  		  logger.debug("OM model is not complete. Progress: "+status);
  	  }
		  if(logger.isDebugEnabled()){
			  logger.debug("Time taken (secs): "+((currentTime-startTime)/1000));
		  }
		  if(!progressComplete){
		  	logger.debug("Sleeping....");
			  Thread.sleep(sleepTime);
		  }
		  currentTime = System.currentTimeMillis();
	  }
	  
	  logger.debug("Returning success.");	  
	  return success;
	}

	/**
	 * @param template the template to set
	 */
	public void setTemplate(String template) {
		this.template = template;
	}

	/**
	 * @param timeout the timeout to set
	 */
	public static void setTimeout(long timeout) {
		OpenModellerFileWriter.timeout = timeout;
	}

	/**
	 * @param sleepTime the sleepTime to set
	 */
	public static void setSleepTime(long sleepTime) {
		OpenModellerFileWriter.sleepTime = sleepTime;
	}

	/**
	 * @param omModel the omModel to set
	 */
	public void setOmModel(OMModel omModel) {
		this.omModel = omModel;
	}

	/**
	 * @param modelId the modelId to set
	 */
	public void setModelId(String modelId) {
		this.modelId = modelId;
	}

	/**
	 * @param imgFileExtension the imgFileExtension to set
	 */
	public void setImgFileExtension(String imgFileExtension) {
		this.imgFileExtension = imgFileExtension;
	}

	/**
	 * @param openModellerEndpoint the openModellerEndpoint to set
	 */
	public void setOpenModellerEndpoint(String openModellerEndpoint) {
		this.openModellerEndpoint = openModellerEndpoint;
	}
}