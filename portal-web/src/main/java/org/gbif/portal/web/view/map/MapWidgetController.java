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
package org.gbif.portal.web.view.map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.gbif.portal.dto.util.BoundingBoxDTO;
import org.gbif.portal.dto.util.EntityType;
import org.gbif.portal.web.content.map.MapContentProvider;
import org.gbif.portal.web.view.WidgetControllerSupport;
import org.springframework.web.bind.ServletRequestUtils;

/**
 * Provides map content for widgets.
 * 
 * @author dmartin
 */
public class MapWidgetController extends WidgetControllerSupport  {

	/**
	 * Redirects to the map url
	 * 
	 * @param componentContext
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	public void addOverviewMap(HttpServletRequest request, HttpServletResponse response)  throws Exception {
		logger.debug("Adding overview map to request");
		String key = (String) request.getAttribute("key");
		String entityType = (String) request.getAttribute("entityType");
		
		Float minLatitude = ServletRequestUtils.getFloatParameter(request, "minLatitude");
		Float minLongitude = ServletRequestUtils.getFloatParameter(request,"minLongitude");
		Float maxLatitude = ServletRequestUtils.getFloatParameter(request,"maxLatitude");
		Float maxLongitude = ServletRequestUtils.getFloatParameter(request,"maxLongitude");

		MapContentProvider mapContentProvider = (MapContentProvider) getWebAppContext(request).getBean("mapContentProvider");
		if(minLatitude!=null && minLongitude!=null && maxLatitude!=null && maxLongitude!=null){
			BoundingBoxDTO bbDTO = new BoundingBoxDTO(minLongitude, minLatitude, maxLongitude, maxLatitude);
			mapContentProvider.addMapContentForEntity(request, EntityType.entityTypesByName.get(entityType), key, bbDTO);
		} else {
			mapContentProvider.addMapContentForEntity(request, EntityType.entityTypesByName.get(entityType), key);
		}
	}
}