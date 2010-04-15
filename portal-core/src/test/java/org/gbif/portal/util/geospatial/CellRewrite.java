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
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.regex.Pattern;

import junit.framework.TestCase;

/**
 * Reads a file of cell_id count and produces what is needed for maps
 * 
 */
public class CellRewrite extends TestCase {
	public static void main(String[] args) throws Exception {
		BufferedReader reader = new BufferedReader(new FileReader(args[0]));
		String line = reader.readLine();
		line = reader.readLine();
		Pattern tab = Pattern.compile("\t");
		BufferedWriter writer = new BufferedWriter(new FileWriter(args[1]));
		writer.write("MINX\tMINY\tMAXX\tMAXY\tDENSITY\n");
		while(line!=null) {
			String[] parts = tab.split(line);
			LatLongBoundingBox llbb = CellIdUtils.toBoundingBox(Integer.parseInt(parts[1]));
			writer.write(llbb.minLong + "\t" + llbb.minLat + "\t" + llbb.maxLong + "\t" + llbb.maxLat + "\t" + parts[2] + "\n");
			line = reader.readLine();
		}
		writer.close();
		reader.close();
	}
}