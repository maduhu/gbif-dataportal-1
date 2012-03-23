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
package org.gbif.portal.country;

import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.gbif.portal.util.file.DelimitedFileReader;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * A quick class that will endeavour to map a 2 digit code from a 3 digit code
 * This needs to be modified afterwards - but will be a decent start for many of the 
 * codes...
 * @author trobertson
 */
public class ISO2CodeFrom3CodeGenerator {
	static Log logger = LogFactory.getLog(ISO2CodeFrom3CodeGenerator.class);
	private ApplicationContext context;
	private void init() {
		String[] locations = {
				"classpath*:/**/applicationContext-*.xml",
				"classpath*:**/applicationContext-*.xml",
				"classpath*:org/gbif/portal/**/applicationContext-*.xml"
		};
		context = new ClassPathXmlApplicationContext(locations);
	}
	
	
	@SuppressWarnings("unchecked")
	protected void run() {
		Map<String,String> iso2Map = (Map<String,String>) context.getBean("isoCountryCodeMap_EN");
		DelimitedFileReader dfr = new DelimitedFileReader(this.getClass().getResourceAsStream("iso3letterCountries.txt"), "||", null, false);
		logger.info("Mapped: ");
		int i=0;
		while (dfr.next()) {
			String iso3Code = dfr.get(0);
			String name = dfr.get(1);
			
			if (iso2Map.containsKey(name)) {
				i++;
				logger.info("<entry key=\"" + iso3Code + "\" value=\"" + iso2Map.get(name) + "\"/>");
			}
		}
		logger.info("(Mapped count =  " + i +")");
		i=0;
		
		dfr = new DelimitedFileReader(this.getClass().getResourceAsStream("iso3letterCountries.txt"), "||", null, false);
		logger.info("\nNot Mapped: ");
		while (dfr.next()) {
			String iso3Code = dfr.get(0);
			String name = dfr.get(1);
			if (!iso2Map.containsKey(name)) {
				i++;
				logger.info(iso3Code + "[" + name +"]");
			}
		}
		logger.info("(Not-mapped count = " + i +")");
		
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ISO2CodeFrom3CodeGenerator me = new ISO2CodeFrom3CodeGenerator();
		me.init();
		me.run();
	}
}
