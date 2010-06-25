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

import java.io.File;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FilenameUtils;
import org.gbif.portal.dto.PropertyStoreTripletDTO;
import org.gbif.portal.dto.SearchResultsDTO;
import org.gbif.portal.dto.geospatial.PointDTO;
import org.gbif.portal.dto.util.SearchConstraints;
import org.gbif.portal.service.triplet.TripletQueryManager;
import org.gbif.portal.web.content.filter.FilterContentProvider;
import org.gbif.portal.web.download.DownloadUtils;
import org.gbif.portal.web.download.OpenModellerFileWriter;
import org.gbif.portal.web.filter.CriteriaDTO;
import org.gbif.portal.web.filter.CriteriaUtil;
import org.gbif.portal.web.filter.FilterDTO;
import org.gbif.portal.web.filter.FilterMapWrapper;
import org.gbif.portal.web.filter.FilterUtils;
import org.gbif.portal.web.util.QueryHelper;
import org.springframework.context.MessageSource;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;
import org.springframework.web.servlet.support.RequestContextUtils;
import org.springframework.web.servlet.view.RedirectView;

/**
 * A Controller that provides the creation of niche models using integration with OpenModeller
 * web services.
 * 
 * @author dmartin
 */
public class OpenModellerController extends MultiActionController {
    
	protected TripletQueryManager tripletQueryManager; 
    protected QueryHelper queryHelper; 
    protected FilterMapWrapper occurrenceFilters;
    protected FilterContentProvider filterContentProvider;
    protected Map<String, Layer> landLayers = new HashMap<String, Layer>();
    protected Map<String, Layer> marineLayers = new HashMap<String, Layer>();
    protected Map<String, String> templates = new HashMap<String, String>();
    protected Map<String, String> masks = new HashMap<String, String>();
    protected String algorithmName = "EnvelopeScore";
    protected String algorithmVersion = "0.1";
    protected String setupModelView = "occurrenceFilterNicheModelling";
    protected String filtersRequestKey = "filters";
    protected String criteriaRequestKey = "criteria";
    protected String templateRequestKey = "template";
    protected String maskRequestKey = "mask";
    protected String layerRequestKey = "layer";
    protected String selectedLayersModelKey = "selectedLayers";
    protected String selectedLayersRequestKey = "selectedLayers";
    
    protected String landLayersModelKey = "landLayers";
    protected String marineLayersModelKey = "marineLayers";
    protected int maxPointsForModel = 100;
	
    protected String downloadFileNamePrefix = "openModeller-";
    protected String fileExtension = ".png";
    protected String downloadFileType = "download.file.type.nichemodelling";
    
    protected String openModellerEndpoint = "http://modeller.cria.org.br/cgi-bin/om_soap_server.cgi";
    
    protected String prepareModelView = "preparingModel";
    
    protected MessageSource messageSource;
    
    /**
	 * Setup initial view for model selection.
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ModelAndView setupModel(HttpServletRequest request, HttpServletResponse response) throws Exception {
		ModelAndView mav = new ModelAndView(setupModelView);
		List<FilterDTO> filters = occurrenceFilters.getFilters();
		mav.addObject(filtersRequestKey, filters);
		CriteriaDTO criteria = CriteriaUtil.getCriteria(request, filters);
		//fix criteria value
		CriteriaUtil.fixEncoding(request, criteria);
		mav.addObject(criteriaRequestKey, criteria);
		mav.addObject(criteriaRequestKey, CriteriaUtil.getCriteria(request, filters));
		String[] selectedLayers = request.getParameterValues(layerRequestKey);
		if(selectedLayers!=null){
			List<String> layers = Arrays.asList(selectedLayers);
			mav.addObject(selectedLayersModelKey, layers);
		}
		
		mav.addObject("landLayers", landLayers.values());
		mav.addObject("marineLayers", marineLayers.values());		
		return mav;
	}

	/**
	 * Starts the creation of the model, starting a thread and redirecting to a polling view for checking
	 * whether model has been rendered.
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public ModelAndView createModel(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		String criteriaAsString = request.getParameter(criteriaRequestKey);
		CriteriaDTO criteria = CriteriaUtil.getCriteria(java.net.URLDecoder.decode(criteriaAsString, "UTF-8"), occurrenceFilters.getFilters(), (Locale) null);
		List<PropertyStoreTripletDTO> triplets = queryHelper.getTriplets(occurrenceFilters.getFilters(), criteria, request, response);
		if(triplets.size()>0){
			filterContentProvider.addGeoreferencedOnlyOccurrenceTriplets(triplets, triplets.get(0).getNamespace());
		}
		
		if(triplets.isEmpty()){
			//add georeferenced triplets	
			PropertyStoreTripletDTO triplet = triplets.get(0);
			filterContentProvider.addGeoreferencedOnlyOccurrenceTriplets(triplets, triplet.getNamespace());
		}
		
		//do occurrence query
		int maxPoints = ServletRequestUtils.getIntParameter(request, "maxResults", maxPointsForModel);
		SearchResultsDTO results = tripletQueryManager.doTripletQuery(triplets, true, new SearchConstraints(0,maxPoints));
		List<PointDTO> points = results.getResults();
		OMModel omModel = new OMModel();
		for (PointDTO point: points) {
			omModel.addPoint(point.getKey(),point.getLongitude().toString(),point.getLatitude().toString());
		}

		//select the layers
		String[] selectedLayers = request.getParameterValues(layerRequestKey);
		if(selectedLayers!=null){
			for (String selectedLayer: selectedLayers) {
				Layer layer = marineLayers.get(selectedLayer);
				if(layer==null){
					layer = landLayers.get(selectedLayer);
				}
				if(layer!=null){
					if(logger.isDebugEnabled()){
						logger.debug("selected layer: "+selectedLayer+", Adding layer: "+ layer.getName()+ " path:"+ layer.getPath());
					}
					omModel.addLayer(layer.getPath());
				}
			}
		}

		//select the mask		
		String maskId = request.getParameter(maskRequestKey);
		omModel.setMaskId(masks.get(maskId));
		
		//set algorithm
		omModel.setAlgorithm(algorithmVersion,algorithmName);
  
		//set template
		String template = request.getParameter(templateRequestKey);
		omModel.setTemplateLayerId(templates.get(template));

		//id for this generated model
		String modelId = Long.toString(System.currentTimeMillis());
		
		//create a file
		File temporaryFile = DownloadUtils.createTempFileOutput(downloadFileNamePrefix, modelId, fileExtension, false);
		OutputStream outputStream = DownloadUtils.createOutputStream(temporaryFile, downloadFileNamePrefix, modelId, fileExtension, false);
		
		//start the file writing thread
		OpenModellerFileWriter fw = new OpenModellerFileWriter();
		fw.setOmModel(omModel);
		fw.setModelId(modelId);
		fw.setTemporaryFile(temporaryFile);
		fw.setOutputStream(outputStream);
		fw.setImgFileExtension(fileExtension);
		fw.setOpenModellerEndpoint(openModellerEndpoint);
		Thread t = new Thread(fw);
		t.start();
		
		String requestLayerSet = ServletRequestUtils.getStringParameter(request, selectedLayersRequestKey);
		
		
		//construct original url & redirect url
		StringBuffer sb = new StringBuffer("/occurrence/setupModel.htm?");
		sb.append(CriteriaUtil.getUrl(criteria));
		sb.append("&");
		sb.append(maskRequestKey);
		sb.append("=");
		sb.append(maskId);
		
		if(requestLayerSet!=null){
			sb.append("&");
			sb.append(selectedLayersRequestKey);
			sb.append("=");
			sb.append(requestLayerSet);
		}
		
		sb.append("&");
		sb.append(templateRequestKey);
		sb.append("=");
		sb.append(template);
		if(selectedLayers!=null){
			for(String layer : selectedLayers){
				sb.append("&");
				sb.append(layerRequestKey);
				sb.append("=");
				sb.append(layer);
			}
		}
		String originalUrl = sb.toString();
		String downloadFile = DownloadUtils.getFileNameWithoutTempMarker(temporaryFile.getName());
		sb.append("&");
		sb.append("img");
		sb.append("=");
		sb.append(downloadFile);
		String redirectUrl = sb.toString();		
		
		String fileNameWithoutExtension = FilenameUtils.removeExtension(downloadFile);
		//generate description
		Locale locale = RequestContextUtils.getLocale(request);
		StringBuffer downloadDesc = new StringBuffer(messageSource.getMessage("model.preparing.description", null, locale)+"<br>");
		downloadDesc.append(FilterUtils.getQueryDescription(occurrenceFilters.getFilters(), criteria, messageSource, locale));
		DownloadUtils.writeDownloadToDescriptor(request, fileNameWithoutExtension, originalUrl, downloadFileType, downloadDesc.toString(), true, redirectUrl);
		//redirect to download preparing page
		return new ModelAndView(new RedirectView("/download/preparingDownload.htm?downloadFile="+downloadFile+"&view="+prepareModelView, true));
	}
	
	/**
	 * @param tripletQueryManager the tripletQueryManager to set
	 */
	public void setTripletQueryManager(TripletQueryManager tripletQueryManager) {
		this.tripletQueryManager = tripletQueryManager;
	}

	/**
	 * @param queryHelper the queryHelper to set
	 */
	public void setQueryHelper(QueryHelper queryHelper) {
		this.queryHelper = queryHelper;
	}

	/**
	 * @param occurrenceFilters the occurrenceFilters to set
	 */
	public void setOccurrenceFilters(FilterMapWrapper occurrenceFilters) {
		this.occurrenceFilters = occurrenceFilters;
	}

	/**
	 * @param templates the templates to set
	 */
	public void setTemplates(Map<String, String> templates) {
		this.templates = templates;
	}

	/**
	 * @param masks the masks to set
	 */
	public void setMasks(Map<String, String> masks) {
		this.masks = masks;
	}

	/**
	 * @param algorithmName the algorithmName to set
	 */
	public void setAlgorithmName(String algorithmName) {
		this.algorithmName = algorithmName;
	}

	/**
	 * @param algorithmVersion the algorithmVersion to set
	 */
	public void setAlgorithmVersion(String algorithmVersion) {
		this.algorithmVersion = algorithmVersion;
	}

	/**
	 * @param setupModelView the setupModelView to set
	 */
	public void setSetupModelView(String setupModelView) {
		this.setupModelView = setupModelView;
	}

	/**
	 * @param filtersRequestKey the filtersRequestKey to set
	 */
	public void setFiltersRequestKey(String filtersRequestKey) {
		this.filtersRequestKey = filtersRequestKey;
	}

	/**
	 * @param criteriaRequestKey the criteriaRequestKey to set
	 */
	public void setCriteriaRequestKey(String criteriaRequestKey) {
		this.criteriaRequestKey = criteriaRequestKey;
	}

	/**
	 * @param templateRequestKey the templateRequestKey to set
	 */
	public void setTemplateRequestKey(String templateRequestKey) {
		this.templateRequestKey = templateRequestKey;
	}

	/**
	 * @param maskRequestKey the maskRequestKey to set
	 */
	public void setMaskRequestKey(String maskRequestKey) {
		this.maskRequestKey = maskRequestKey;
	}

	/**
	 * @param layerRequestKey the layerRequestKey to set
	 */
	public void setLayerRequestKey(String layerRequestKey) {
		this.layerRequestKey = layerRequestKey;
	}

	/**
	 * @param downloadFileNamePrefix the downloadFileNamePrefix to set
	 */
	public void setDownloadFileNamePrefix(String downloadFileNamePrefix) {
		this.downloadFileNamePrefix = downloadFileNamePrefix;
	}

	/**
	 * @param fileExtension the fileExtension to set
	 */
	public void setFileExtension(String fileExtension) {
		this.fileExtension = fileExtension;
	}

	/**
	 * @param downloadFileType the downloadFileType to set
	 */
	public void setDownloadFileType(String downloadFileType) {
		this.downloadFileType = downloadFileType;
	}

	/**
	 * @param filterContentProvider the filterContentProvider to set
	 */
	public void setFilterContentProvider(FilterContentProvider filterContentProvider) {
		this.filterContentProvider = filterContentProvider;
	}

	/**
	 * @param openModellerEndpoint the openModellerEndpoint to set
	 */
	public void setOpenModellerEndpoint(String openModellerEndpoint) {
		this.openModellerEndpoint = openModellerEndpoint;
	}

	/**
	 * @param prepareModelView the prepareModelView to set
	 */
	public void setPrepareModelView(String prepareModelView) {
		this.prepareModelView = prepareModelView;
	}

	/**
	 * @return the messageSource
	 */
	public MessageSource getMessageSource() {
		return messageSource;
	}

	/**
	 * @param messageSource the messageSource to set
	 */
	public void setMessageSource(MessageSource messageSource) {
		this.messageSource = messageSource;
	}

	/**
	 * @param landLayers the landLayers to set
	 */
	public void setLandLayers(Map<String, Layer> landLayers) {
		this.landLayers = landLayers;
	}

	/**
	 * @param marineLayers the marineLayers to set
	 */
	public void setMarineLayers(Map<String, Layer> marineLayers) {
		this.marineLayers = marineLayers;
	}

	/**
	 * @param selectedLayersModelKey the selectedLayersModelKey to set
	 */
	public void setSelectedLayersModelKey(String selectedLayersModelKey) {
		this.selectedLayersModelKey = selectedLayersModelKey;
	}

	/**
	 * @param landLayersModelKey the landLayersModelKey to set
	 */
	public void setLandLayersModelKey(String landLayersModelKey) {
		this.landLayersModelKey = landLayersModelKey;
	}

	/**
	 * @param marineLayersModelKey the marineLayersModelKey to set
	 */
	public void setMarineLayersModelKey(String marineLayersModelKey) {
		this.marineLayersModelKey = marineLayersModelKey;
	}

	/**
	 * @param maxPointsForModel the maxPointsForModel to set
	 */
	public void setMaxPointsForModel(int maxPointsForModel) {
		this.maxPointsForModel = maxPointsForModel;
	}

	/**
   * @param selectedLayersRequestKey the selectedLayersRequestKey to set
   */
  public void setSelectedLayersRequestKey(String selectedLayersRequestKey) {
  	this.selectedLayersRequestKey = selectedLayersRequestKey;
  }
}