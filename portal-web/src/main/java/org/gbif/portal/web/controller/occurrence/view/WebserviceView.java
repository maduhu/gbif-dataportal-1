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
package org.gbif.portal.web.controller.occurrence.view;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.xmlbeans.XmlObject;
import org.gbif.portal.dto.PropertyStoreTripletDTO;
import org.gbif.portal.util.path.PathMapping;
import org.gbif.portal.ws.actions.OccurrenceParameters;
import org.gbif.portal.ws.util.GbifMappingFactory;
import org.springframework.web.servlet.view.AbstractView;

/**
 * A view for rendering web services documents.
 * 
 * @author dmartin
 */
public class WebserviceView extends AbstractView {
	
	/** Path mapping for WS URLs */
	protected PathMapping wsRestPathMapping;	
	/** Path mapping for WS URLs */
	protected GbifMappingFactory gbifMappingFactory;
	
	protected String format = "darwin";
	protected String docGeneratorMethod = "getGbifResponseDocument";
	
	protected String webserviceRoot = "/ws";
	protected String contentType = "text/xml";
	protected String contentDisposition = "occurrence-search.xml";

	/**
	 * @see org.springframework.web.servlet.view.AbstractView#renderMergedOutputModel(java.util.Map, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void renderMergedOutputModel(Map map, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		String url = request.getRequestURL().toString();
		String servletPath = request.getServletPath();
		String urlBase = url.substring(0, url.indexOf(servletPath));

		List<PropertyStoreTripletDTO> triplets = (List<PropertyStoreTripletDTO>) request.getAttribute("triplets");
		List results = (List) request.getAttribute("results");
		
		OccurrenceParameters op = new OccurrenceParameters(triplets, format, urlBase, webserviceRoot, wsRestPathMapping, false);
		Method method = gbifMappingFactory.getClass().getMethod(docGeneratorMethod, new Class[]{OccurrenceParameters.class, List.class});
		try{
			XmlObject xmlObject = (XmlObject) method.invoke(gbifMappingFactory, new Object[]{op,results});
			response.setContentType(contentType);
			response.getOutputStream().print(xmlObject.toString());
			response.setHeader("Content-Disposition", "attachment; "+contentDisposition);
		} catch (Exception e){
			logger.error(e.getMessage(), e);
			return;
		}
	}

	/**
	 * @param wsRestPathMapping the wsRestPathMapping to set
	 */
	public void setWsRestPathMapping(PathMapping wsRestPathMapping) {
		this.wsRestPathMapping = wsRestPathMapping;
	}

	/**
	 * @param gbifMappingFactory the gbifMappingFactory to set
	 */
	public void setGbifMappingFactory(GbifMappingFactory gbifMappingFactory) {
		this.gbifMappingFactory = gbifMappingFactory;
	}

	/**
	 * @param contentType the contentType to set
	 */
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	/**
	 * @param format the format to set
	 */
	public void setFormat(String format) {
		this.format = format;
	}

	/**
	 * @param webserviceRoot the webserviceRoot to set
	 */
	public void setWebserviceRoot(String webserviceRoot) {
		this.webserviceRoot = webserviceRoot;
	}

	/**
	 * @param docGeneratorMethod the docGeneratorMethod to set
	 */
	public void setDocGeneratorMethod(String docGeneratorMethod) {
		this.docGeneratorMethod = docGeneratorMethod;
	}

	/**
	 * @param contentDisposition the contentDisposition to set
	 */
	public void setContentDisposition(String contentDisposition) {
		this.contentDisposition = contentDisposition;
	}
}