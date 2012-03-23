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
package georegions;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.gbif.portal.util.file.DelimitedFileReader;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * A program thrown together to generate the georegions from the data in the files, created
 * from cutting and pasting from http://millenniumindicators.un.org/unsd/methods/m49/m49regin.htm
 * @author trobertson
 */
public class GeoRegionsFromFiles {
	private ApplicationContext context;
	private Log logger = LogFactory.getLog(GeoRegionsFromFiles.class);
	
	private void init() {
		String[] locations = {
				"classpath*:/**/applicationContext-*.xml",
				"classpath*:**/applicationContext-*.xml",
				"classpath*:org/gbif/portal/**/applicationContext-*.xml"
		};
		context = new ClassPathXmlApplicationContext(locations);
	}
	

	@SuppressWarnings("unchecked")
	private void go() {
		logger.info("Starting...");
		try {
			// the countries
			Map<String, String> countryISO = (Map<String, String>)context.getBean("isoCountryCodeMap_EN");
			
			// mapping of region name to code
			Map<String, String> regions = new HashMap<String, String>();
			regions.put("Eastern Africa", "EAF");
			regions.put("Middle Africa", "MAF");
			regions.put("Northern Africa", "NAF");
			regions.put("Southern Africa", "SAF");
			regions.put("Western Africa", "WAF");
			regions.put("Caribbean", "CAR");
			regions.put("Central America", "CAM");
			regions.put("South America", "SAM");
			regions.put("Northern America", "NAM");
			regions.put("Central Asia", "CAS");
			regions.put("Eastern Asia", "EAS");
			regions.put("Southern Asia", "SAS");
			regions.put("South-Eastern Asia", "SEA");
			regions.put("Western Asia", "WAS");
			regions.put("Eastern Europe", "EEU");
			regions.put("Northern Europe", "NEU");
			regions.put("Southern Europe", "SEU");
			regions.put("Western Europe", "WEU");
			regions.put("Australia and New Zealand", "ANZ");
			regions.put("Melanesia", "MEL");
			regions.put("Micronesia", "MIC");
			regions.put("Polynesia", "POL");
			
			InputStream is = this.getClass().getResourceAsStream("regions.txt");
			DelimitedFileReader reader = new DelimitedFileReader(is, "\t", "\"", true);
			String unGeoRegion = null;
			
			Map<String, String> iso2toRegion = new HashMap<String,String>();
 			
			do {
				if (reader.get(0).length()<1) {
					unGeoRegion = regions.get(reader.get(1));
					continue;
				}
				
				String country =  reader.get(1);
				if (countryISO.containsKey(country.toUpperCase().trim())) {
					String iso2Code = countryISO.get(country.trim().toUpperCase());
					logger.info(iso2Code + "\t" + unGeoRegion);
					iso2toRegion.put(iso2Code, unGeoRegion);
				} else {
					logger.info("Country not found [" + country.trim() + "]");
				}
			} while(reader.next());
			
			
			// so, now lets modify the iso_3116-1_countries_en.txt
			is = this.getClass().getResourceAsStream("iso_3116-1_country_codes.txt");
			reader = new DelimitedFileReader(is, "\t", "\"", true);
			do {
				if (iso2toRegion.get(reader.get(0))!= null &&  iso2toRegion.get(reader.get(0)).length()>0) {
					logger.info(reader.get(0) + "\t" + reader.get(1) + "\t" + iso2toRegion.get(reader.get(0)));
				} else {
					logger.info(reader.get(0) + "\t" + reader.get(1) + "\t");
				}
			} while(reader.next());
			
			
		} catch (RuntimeException e) {
			logger.error(e);
		}
		logger.info("Finished!");
	}

	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		GeoRegionsFromFiles me = new GeoRegionsFromFiles();
		me.init();
		me.go();
		System.exit(0);
	}

}
