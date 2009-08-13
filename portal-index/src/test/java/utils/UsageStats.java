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
package utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.gbif.portal.util.file.DelimitedFileReader;
import org.gbif.portal.util.request.IPUtils;

/**
 * @author trobertson
 *
 */
public class UsageStats {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			InputStream is = new FileInputStream("c:\\temp\\logs\\portal-acces_log");
			DelimitedFileReader reader = new DelimitedFileReader(is, "\t", "\"", true);
			System.out.println("Starting");
			
			
			Set<String> IPs = new HashSet<String>();
			Map<String, Integer> countries = new HashMap<String, Integer>();
			Map<Long[], String> ranges = buildRanges();
			Map<String, String> iso2Country = buildISO2String();
			Map<String, Integer> ipCounts = new HashMap<String, Integer>();
			int total = 0;
			int wstotal = 0;
			int tuttotal = 0;
			int maptotal = 0;
			do {
				String message = reader.get(0);
				String IP = message.substring(0, message.indexOf(" -"));
				// don't include us
				if (!IP.startsWith("192.38.28.")) {
					IPs.add(IP);
				}
				
					
				if (message.indexOf("GET /occurrences")>-1
						|| message.indexOf("GET /search")>-1
						|| message.indexOf("GET /species")>-1
						|| message.indexOf("GET / ")>-1
						|| message.indexOf("GET /welcome")>-1
						|| message.indexOf("GET /settings")>-1
						|| message.indexOf("GET /countries")>-1
						|| message.indexOf("GET /dataset")>-1) {
					total++;
				}else if (message.indexOf("GET /ws")>-1) {
					wstotal++;
				} else if (message.indexOf("GET /tutorial")>-1) {
					tuttotal++;
				}
				if (message.indexOf("overviewMap.png")>-1) {
					maptotal++;
				}
				
				if (ipCounts.containsKey(IP)) {
					int count = ipCounts.get(IP);
					ipCounts.put(IP, count+1);
				} else {
					ipCounts.put(IP, 1);
				}				
				
			} while (reader.next());
			
			for (String IP : IPs) {
				long ipAsLong = IPUtils.convertIPtoLong(IP);
				String iso = getCountry(ranges, ipAsLong);
				if (iso2Country.containsKey(iso.toUpperCase())) {
					
					String countryName = iso2Country.get(iso.toUpperCase());
					if (countries.containsKey(countryName)) {
						int count = countries.get(countryName);
						countries.put(countryName, count+1);
					} else {
						countries.put(countryName, 1);
					}
				}				
			}
			
			
			for (String countryName : countries.keySet()) {
				System.out.println(countryName + "," + countries.get(countryName));
			}
			
			System.out.println("Users: " + IPs.size());
			System.out.println("From countries: " + countries.size());
			System.out.println("Website hits: " + total);
			System.out.println("Web Service hits: " + wstotal);
			System.out.println("Tutorial page hits: " + tuttotal);
			System.out.println("Maps served: " + maptotal);
			
			System.out.println("\n\nGuessing these IPs are bots: ");
			for (String ip : ipCounts.keySet()) {
				int count = ipCounts.get(ip);
				if (count>500) {
					System.out.println(ip + ": " + count);
				}
				
			}			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private static String getCountry(Map<Long[], String> ranges, long value) {
		String country = "Unknown";
		for (Long[] range : ranges.keySet()) {
			long lower = range[0];
			long upper = range[1];
			if (lower<=value && value<=upper) {
				country = ranges.get(range);
			}
		}
		return country;
	}

	private static Map<Long[], String> buildRanges() {
		Map<Long[], String> range2ISO= new HashMap<Long[], String>();
		try {
			InputStream is = new FileInputStream("C:\\portal\\portal-index\\data\\countries\\ip_country.txt");
			DelimitedFileReader reader = new DelimitedFileReader(is, "\t", "\"", true);
			
			do {
				long lower = Long.parseLong(reader.get(2));
				long upper = Long.parseLong(reader.get(3));
				String country = reader.get(4);
				
				Long[] range = {lower, upper};
				range2ISO.put(range, country);
				
			} while (reader.next());
			
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		System.out.println("Ranges built");
		return range2ISO;

	}
	
	private static Map<String, String> buildISO2String() {
		Map<String, String> ISO2String= new HashMap<String, String>();
		try {
			InputStream is = new FileInputStream("C:\\portal\\portal-index\\data\\countries\\iso_3116-1_countries_en.txt");
			DelimitedFileReader reader = new DelimitedFileReader(is, "\t", "\"", true);
			
			do {
				String code = reader.get(0);
				String country = reader.get(1);
				ISO2String.put(code, country);
				
			} while (reader.next());
			
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		System.out.println("Ranges built");
		return ISO2String;

	}
	
}
