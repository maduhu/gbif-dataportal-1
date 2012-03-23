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
package org.gbif.portal.dto.log;

import org.gbif.portal.dto.BaseDTOFactory;
import org.gbif.portal.util.log.LogEvent;

/**
 * Log Stats DTO Factory.
 * 
 * @author dmartin
 */
public class LogStatsDTOFactory extends BaseDTOFactory {

	/**
	 * @see org.gbif.portal.dto.DTOFactory#createDTO(java.lang.Object)
	 */
	public Object createDTO(Object modelObject) {
		
		if(modelObject!=null && modelObject instanceof Object[]){
			
			Object[] stats = (Object[]) modelObject;
			
			if(stats.length>=5){
				LogStatsDTO ls = new LogStatsDTO();
				ls.setEntityKey(stats[0].toString());
				ls.setEntityName(stats[1].toString());
				if(stats[2] instanceof Integer)
					ls.setEventId((Integer) stats[2]);
				try {
					if(stats[2]!=null){
						Integer eventId = (Integer) stats[2];
						LogEvent le = LogEvent.get(eventId.intValue());
						if (le!=null)
							ls.setEventName(le.getName());
						else 
							ls.setEventName("Unknown");
					}
					if(stats[3]!=null){
						Long eventCount = (Long) stats[3];
						ls.setEventCount(eventCount.intValue());
					}
					if(stats[4]!=null){
						Long count = (Long) stats[4];
						if(count!=null &&count.intValue()>0){
							ls.setCount(count.intValue());
						}
					}					
				} catch(NumberFormatException e){
					logger.debug(e.getMessage(), e);
				} catch(ClassCastException e){
					logger.debug(e.getMessage(), e);
				}
				return ls;
			}
		}
		return null;
	}
}