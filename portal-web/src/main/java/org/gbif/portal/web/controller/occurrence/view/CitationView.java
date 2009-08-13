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

import java.util.List;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.gbif.portal.ws.util.GbifMappingFactory;
import org.springframework.web.servlet.view.AbstractView;

/**
 * A view for rendering citations for a set of occurrence records.
 * 
 * @author dmartin
 */
public class CitationView extends AbstractView {

	/** Path mapping for WS URLs */
	protected GbifMappingFactory gbifMappingFactory;
	
	/** Whether to output html */
	protected boolean outputHtml = false;
	
	/** The content type to set on the response */
	protected String contentType = "text/html";
	
	protected String jsptoRender = "/WEB-INF/jsp/simpleContainer.jsp";
	
	@Override
	protected void renderMergedOutputModel(Map map, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		List results = (List) request.getAttribute("results");		
		
		String url = request.getRequestURL().toString();
		String servletPath = request.getServletPath();
		String urlBase = url.substring(0, url.indexOf(servletPath));
		
		//get citation text
		String citationText = gbifMappingFactory.getCitationTextForOccurrences(results, urlBase, outputHtml);
		RequestDispatcher rd = request.getRequestDispatcher(jsptoRender);
		request.setAttribute("content", citationText);
		rd.forward(request, response);
		
		//response.setContentType(contentType);
//		/response.getOutputStream().print(citationText);
	}

	/**
	 * @param contentType the contentType to set
	 */
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	/**
	 * @param gbifMappingFactory the gbifMappingFactory to set
	 */
	public void setGbifMappingFactory(GbifMappingFactory gbifMappingFactory) {
		this.gbifMappingFactory = gbifMappingFactory;
	}

	/**
	 * @param outputHtml the outputHtml to set
	 */
	public void setOutputHtml(boolean outputHtml) {
		this.outputHtml = outputHtml;
	}
}