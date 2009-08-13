/***************************************************************************
 * Copyright (C) 2006 Global Biodiversity Information Facility Secretariat.
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
package org.gbif.portal.web.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.gbif.portal.web.content.ContentProvider;
import org.gbif.portal.web.content.ContentView;
import org.springframework.web.servlet.ModelAndView;
/**
 * Spring Specific Content methods. These methods know how to add content to a 
 * Spring ModelAndView
 *
 * @author Dave Martin
 */
public class ContentUtil {
	
	/**
	 * And content to ModelAndView.
	 * @param mav the ModelAndView to populate
	 * @param the request object. Could be a Command object or HttpServletRequest
	 * @param contentProviders
	 */
	public  static void addContent(ModelAndView mav, List<ContentProvider> contentProviders, HttpServletRequest request, HttpServletResponse response) throws Exception{
		ContentViewImpl cv = new ContentViewImpl();
		cv.setModelAndView(mav);
		for (ContentProvider cp: contentProviders){
			cp.addContent(cv, request, response);
		}				
	}
	
	/**
      * Create a content view wrapper for this ModelAndView
	  *	@param the ModelAndView to wrap
	  */
	public  static ContentView getContentView(ModelAndView mav){
		ContentViewImpl cv = new ContentViewImpl();
		cv.setModelAndView(mav);
		return cv;
	}
	
	/**
	 * Inner class used to wrap ModelAndView instances.
	 * 
	 * @author dmartin
	 */
	static class ContentViewImpl implements ContentView {
		
		/**the spring ModelAndView instance this ContentView encapsulates**/
		protected ModelAndView modelAndView;
		
		/**
		 * @see org.gbif.portal.web.content.ContentView#addObject(java.lang.String, java.lang.Object)
		 */
		public void addObject(String name, Object object) {
			if(name!=null && object !=null)
				modelAndView.addObject(name, object);
		}

		/**
		 * @return the modelAndView
		 */
		public ModelAndView getModelAndView() {
			return modelAndView;
		}

		/**
		 * @param modelAndView the modelAndView to set
		 */
		public void setModelAndView(ModelAndView modelAndView) {
			this.modelAndView = modelAndView;
		}
	}
}
