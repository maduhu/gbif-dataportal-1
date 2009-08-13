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

package org.gbif.portal.util.geospatial;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.regex.Pattern;

/**
 * Utilities for creating a text file from a cell density table dump 
 * suitable for rendering in Mapserver
 * @author tim
 */
public class CreateMapTextFile {
	
	/**
	 * pass in the input file name and output file name
	 */
	public static void main(String[] args) {
		try {
			if (args.length == 2) {
				
				BufferedReader br = new BufferedReader(new FileReader(args[0]));
				FileWriter fw = new FileWriter(args[1]);
				
				String line = br.readLine();
				Pattern tab = Pattern.compile("\t");
				fw.write("MINX\tMINY\tMAXX\tMAXY\tDENSITY\n");
				while (line!=null){
					String[] parts = tab.split(line);					
					
					// type, id, cell, count
					int cell = Integer.parseInt(parts[2]);
					LatLongBoundingBox llbb = CellIdUtils.toBoundingBox(cell);
					
					fw.write(llbb.getMinLong() + "\t" + llbb.getMinLat() + "\t" + llbb.getMaxLong() + "\t" + llbb.getMaxLat() + "\t" + parts[3] + "\n");
					line = br.readLine();
				}
				
				fw.flush();
				fw.close();
				br.close();
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
}	