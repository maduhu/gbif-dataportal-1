package org.gbif.portal.web.controller.openmodeller;

import java.util.ArrayList;
import java.util.List;

/**
 * Contains information needed to run an OpenModeller request
 * 
 * @author David Neufeld
 */
public class OMModel {
	
    private List<String> layers = new ArrayList<String>();
    private List<Point> points = new ArrayList<Point>();
    private List parameters = new ArrayList();
    private String maskId = null;
    private String templateLayerId = null;
    private String algorithmId = null;
    private String algorithmVersion = null;
    private String modelParams = null;
	
    /**
	 * Add a layer to be incorporated into the model 
	 * @param the layerId
	 */   
    public void addLayer(String layerId) {
    	layers.add(layerId);
    }

	/**
	 * Get layers associated with the model 
	 * @return an array list to iterate through the layers
	 */   
    public List<String> getLayers() {
    	return this.layers;
    }

	/**
	 * Set the mask layer for the model 
	 * @param the layer to use as a mask in the model
	 */   
    public void setMaskId(String maskId) {
    	this.maskId = maskId;
    }

	/**
	 * Get the mask layer associated with the model 
	 * @return the mask layer
	 */   
    public String getMaskId() {
    	return maskId;
    }

	/**
	 * Set the template layer for the model 
	 * @param the template layer to use in the model
	 */   
    public void setTemplateLayerId(String templateLayerId) {
    	this.templateLayerId = templateLayerId;
    }

	/**
	 * Get the template layer associated with the model 
	 * @return the template layer
	 */   
    public String getTemplateLayerId() {
    	return templateLayerId;
    }

	/**
	 * Add point occurrences for the model 
	 * @param the point id of the occurrence
	 * @param the x coordinate of the occurrence
	 * @param the y coordinate of the occurrence
	 */   
    public void addPoint(String ptId, String x, String y) {
    	Point pt = new Point(ptId, x, y);
    	points.add(pt);
    }

	/**
	 * Get point occurrences associated with the model 
	 * @return an array list to iterate through the points
	 */   
    public List<Point> getPoints() {
    	return this.points;
    }

	/**
	 * Set algorithm to be used by the model
	 * @param the algorithm version
	 * @param the algorithm id	 
	 */   
    public void setAlgorithm(String algorithmVersion, String algorithmId) {
    	this.algorithmVersion = algorithmVersion;
    	this.algorithmId = algorithmId;
    }

	/**
	 * Get the algorithm id associated with the model 
	 * @return the algorithm id
	 */   
    public String getAlgorithmId() {
    	return algorithmId;
    }
    
	/**
	 * Set model params to be used by the model
	 * @param the model params as xml returned in create model response
	 */   
    public void setModelParams(String modelParams) {
    	this.modelParams = modelParams;
    }

	/**
	 * Get the algorithm id associated with the model 
	 * @return the algorithm id
	 */   
    public String getModelParams() {
    	return modelParams;
    } 
	/**
	 * Get the version of the algorithm 
	 * @return the algorithm version
	 */   
    public String getAlgorithmVersion() {
    	return algorithmVersion;
    }

	/**
	 * Add an algorithm parameter 
	 * @param the parameter id to add to the algorithm
	 * @param the parameter value to add to the algorithm
	 */   
    public void addParameter(String paramId, String paramVal) {
    	Parameter param = new Parameter(paramId, paramVal);
    	parameters.add(param);
    }

	/**
	 * Get parameters used by the algorithm 
	 * @return an array list to iterate through the parameters
	 */   
    public List getParameters() {
    	return this.parameters;
    }    
}