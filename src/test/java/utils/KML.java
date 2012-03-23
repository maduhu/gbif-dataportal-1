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
import java.io.FileWriter;
import java.io.InputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.gbif.portal.util.file.DelimitedFileReader;

/**
 * @author trobertson
 *
 */
public class KML {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			InputStream is = new FileInputStream("c:\\temp\\southAfricaData-CapeTown.txt");
			DelimitedFileReader reader = new DelimitedFileReader(is, "\t", "\"", true);
			int i=0;
			System.out.println("Starting");
			
			
			Map<String, Object[]> snMap;
			Map<String,Map<String, Object[]>> cMap;
			Map<String,Map<String,Map<String, Object[]>>> pMap;
			Map<String,Map<String,Map<String,Map<String, Object[]>>>> kMap = new HashMap<String,Map<String,Map<String,Map<String, Object[]>>>>();
			
			
			FileWriter fw = new FileWriter("c:\\temp\\southAfricaData.kml");
			do {
				String id = reader.get(0);
				String k = reader.get(1);
				String p = reader.get(2);
				String c = reader.get(3);
				String sn = reader.get(4);
				String lat = reader.get(5);
				String lon = reader.get(6);
				
				if (kMap.containsKey(k)) {
					pMap = kMap.get(k);
				} else {
					pMap = new HashMap<String, Map<String, Map<String,Object[]>>>();
					kMap.put(k, pMap);
				}
				
				if (pMap.containsKey(p)) {
					cMap = pMap.get(p);
				} else {
					cMap = new HashMap<String,Map<String,Object[]>>();
					pMap.put(p, cMap);
				}
				
				if (cMap.containsKey(c)) {
					snMap = cMap.get(c);
				} else {
					snMap = new HashMap<String,Object[]>();
					cMap.put(c, snMap);
				}
				
				
				Object[] data = {id, sn, lon, lat};
				snMap.put(id, data);
				i++;
				
				if (i%10000==0) {
					System.out.println("Processed: " + i);
				}
				
				if(i==1000) {
					//break;
				}
			} while (reader.next());
			
			System.out.println("k: " + kMap.size());
			
			
			Set<String> nameAndLocation = new HashSet<String>();
			
			fw.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
			
			

			//fw.write("<kml xmlns=\"http://earth.google.com/kml/2.0\">\n");
			fw.write("<Document xmlns=\"http://earth.google.com/kml/2.1\">\n");
			fw.write("<name>GBIF Data Portal Occurrence Search</name>\n");
			fw.write("<open>true</open>\n");
			 
			fw.write("<Style id=\"gbifIcon1\"><IconStyle><Icon><href>http://newportal.gbif.org/images/GE1.png</href></Icon></IconStyle></Style>\n");

			fw.write("<Folder>\n");
			for (String k : kMap.keySet()) {
				fw.write("  <name>" + k + "</name>\n");

				Map<String,Map<String, Map<String, Object[]>>> phyla = kMap.get(k);
				for (String p : phyla.keySet()) {
					
					fw.write("  <Folder>\n");
					Map<String, Map<String, Object[]>> classes = phyla.get(p);
					fw.write("    <name>" + p + "</name>\n");
					for (String c : classes.keySet()) {
						
						
						fw.write("    <Folder>\n");
						fw.write("      <name>" + c + "</name>\n");
						Map<String, Object[]> sns = classes.get(c);
						
						for(String sn : sns.keySet()) {
							Object[] data = sns.get(sn);
							
							if (!nameAndLocation.contains(data[1].toString() + data[2].toString() + data[3].toString())) {
								fw.write("      <Placemark>\n");
								fw.write("        <name>" + data[1] + "</name>\n");
								fw.write("        <styleUrl>#gbifIcon1</styleUrl>\n");								
								fw.write("        <Point><coordinates>"+ data[2] + "," + data[3] + ",0</coordinates></Point>\n");							
								fw.write("      </Placemark>\n");
								nameAndLocation.add(data[1].toString() + data[2].toString() + data[3].toString());
							}
						}
						
						fw.write("    </Folder>\n");
					}
					
					fw.write("  </Folder>\n");
					fw.flush();
				}
			}
			fw.write("</Folder>");
			fw.write("</Document>");
			
			
			/*
			
			fw.write("<K:Folder>\n");
			for (String k : kingdoms.keySet()) {
				fw.write("  <name>");
				fw.write(k);
				fw.write("</name>\n");
				Map<String, Map<String, Object[]>> phyla = kingdoms.get(k);
				
				for (String p : phyla.keySet()) {
					fw.write("  <P:Folder>\n");
					Map<String, Object[]> classes = phyla.get(p);
					fw.write("    <name>");
					fw.write(p);
					fw.write("</name>\n");
					
					for (String c : classes.keySet()) {
						fw.write("    <C:Folder>\n");
						fw.write("      <name>");
						fw.write(c);
						fw.write("</name>\n");
						
						
						
						
						fw.write("    </Folder>\n");
					}
					fw.write("  </Folder>\n");
					fw.flush();
				}
			}
			fw.write("</Folder>");*/
			fw.flush();
			fw.close();
			
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
