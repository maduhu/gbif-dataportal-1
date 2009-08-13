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
package org.gbif.portal.dto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * A DTO factory for packaging up count dtos.
 * 
 * @author dmartin
 */
public class CountDTOFactory extends BaseDTOFactory {

	/**
	 * @see org.gbif.portal.dto.DTOFactory#createDTO(java.lang.Object)
	 */
	public Object createDTO(Object modelObject) {
		if ( modelObject instanceof Object[]){
			Object[] keyNameCount = (Object[]) modelObject;
			if(keyNameCount.length ==1){
				return keyNameCount[0];
			}
			if(keyNameCount.length >=2){
				CountDTO countDTO = new CountDTO();
				if(keyNameCount[0]!=null)
					countDTO.setKey(keyNameCount[0].toString());
				if(keyNameCount[1]!=null)
					countDTO.setName(keyNameCount[1].toString());
				
				if(keyNameCount.length>3){
					List<String> properties = new ArrayList<String>();
					for (int i=2; i<keyNameCount.length-1;i++){
						properties.add(keyNameCount[i].toString());
					}
					countDTO.setProperties(properties);
				}
				
				if(keyNameCount[keyNameCount.length-1]!=null){				
					if(keyNameCount[keyNameCount.length-1] instanceof Integer)
						countDTO.setCount((Integer) keyNameCount[keyNameCount.length-1]);
					else if (keyNameCount[keyNameCount.length-1]!=null && keyNameCount[keyNameCount.length-1] instanceof Long )
						countDTO.setCount( ((Long) keyNameCount[keyNameCount.length-1]).intValue());
					else if (keyNameCount[keyNameCount.length-1]!=null && keyNameCount[keyNameCount.length-1] instanceof BigDecimal)
						countDTO.setCount( ((BigDecimal) keyNameCount[keyNameCount.length-1]).intValue());
				}
				return countDTO;
			}
		}
		return modelObject;
	}
	
	
	
	
}
