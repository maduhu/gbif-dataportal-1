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
package org.gbif.portal.dto.geospatial;

import java.util.StringTokenizer;

import org.gbif.portal.dto.BaseDTOFactory;
import org.gbif.portal.util.geospatial.CellIdUtils;
import org.gbif.portal.util.geospatial.LatLongBoundingBox;

public class PointDTOFactory extends BaseDTOFactory {

	/**
	 * Creates a DTO from an Object[2] with <br>
	 * Object[0] being a key<br>
	 * Object[1] being a latitude<br>
	 * Object[2] being a longitude<br>
	 * <br>
	 * or from a comma separated string e.g. "123.2, 123.44"
	 * 
	 * @see org.gbif.portal.dto.DTOFactory#createDTO(java.lang.Object)
	 */	
	public Object createDTO(Object modelObject) {
		if(modelObject instanceof Integer){
			//assume its a cell
			Integer cellId = (Integer) modelObject;
			LatLongBoundingBox llbb = CellIdUtils.toBoundingBox(cellId);
			PointDTO pointDTO = new PointDTO();
			pointDTO.setKey(cellId.toString());
			pointDTO.setLatitude(llbb.getMinLat() +0.5f);
			pointDTO.setLongitude(llbb.getMinLong() +0.5f);					
			return pointDTO;
		} else if(modelObject instanceof String){
			String point = (String) modelObject;
			//looking for comma separated point
			StringTokenizer tokenizer = new StringTokenizer(point,",");
			if(tokenizer.countTokens() == 2){
				PointDTO pointDTO = new PointDTO();
				pointDTO.setKey(point);
				pointDTO.setLatitude(new Float(tokenizer.nextToken().trim()));
				pointDTO.setLongitude(new Float(tokenizer.nextToken().trim()));				
				return pointDTO;
			} else {
				return null;
			}
		} else if (modelObject instanceof Object[]){
			Object[] point = (Object[]) modelObject;
			PointDTO pointDTO = new PointDTO();
			pointDTO.setKey(point[0].toString());
			pointDTO.setLatitude((Float) point[1]);
			pointDTO.setLongitude((Float) point[2]);
			return pointDTO;
		} else {
			return null;
		}
	}
}