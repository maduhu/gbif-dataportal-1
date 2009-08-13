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
package org.gbif.portal.web.util;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.encoders.KeypointPNGEncoderAdapter;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.title.LegendTitle;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.ui.RectangleInsets;

/**
 * Utilities for producing charts.
 * 
 * @author dmartin
 */
public class ChartUtils {
	
	public static Log logger = LogFactory.getLog(ChartUtils.class);	

	protected static String tmpDirSystemProperty = "java.io.tmpdir";	
	
	protected static String defaultExtension = ".png";
	
	/**
	 * Writes out the image using the supplied file name.
	 * 
	 * @param legend
	 * @param fileName
	 * @return
	 */
	public static String writePieChartImageToTempFile(Map<String, Double> legend, String fileName){
		
			String filePath = System.getProperty(tmpDirSystemProperty)+File.separator+fileName+defaultExtension;
			File fileToCheck = new File(filePath);
			if(fileToCheck.exists()){
				return fileName+defaultExtension;
			}
		
	       final DefaultPieDataset data = new DefaultPieDataset();
	       Set<String> keys = legend.keySet();
	       for(String key: keys){
	    	   logger.info("Adding key : "+key);
	    	   data.setValue(key, legend.get(key)); 
	       }

	        // create a pie chart...
	        final boolean withLegend = true;
	        final JFreeChart chart = ChartFactory.createPieChart(
	            null, 
	            data, 
	            withLegend,
	            false,
	            false
	        );

	        PiePlot piePlot = (PiePlot) chart.getPlot();
	        piePlot.setLabelFont(new Font("Arial", Font.PLAIN, 10));
	        piePlot.setLabelBackgroundPaint(Color.WHITE);
	        
	        LegendTitle lt = chart.getLegend();
	        lt.setBackgroundPaint(Color.WHITE);
	        lt.setWidth(300);
	        lt.setBorder(0, 0, 0, 0);
	        lt.setItemFont(new Font("Arial", Font.PLAIN, 11));

	        chart.setPadding(new RectangleInsets(0,0,0,0));
	        chart.setBorderVisible(false);
	        chart.setBackgroundImageAlpha(0);
	        chart.setBackgroundPaint(Color.WHITE);
	        chart.setBorderPaint(Color.LIGHT_GRAY);
	        
	        final BufferedImage image = new BufferedImage(300, 250, BufferedImage.TYPE_INT_RGB);
	        KeypointPNGEncoderAdapter adapter = new KeypointPNGEncoderAdapter();
	        adapter.setQuality(1);
	        try {
				adapter.encode(image);
			} catch (IOException e) {
				logger.error(e.getMessage(), e);
			}
	        final Graphics2D g2 = image.createGraphics();
	        g2.setFont(new Font("Arial", Font.PLAIN, 11));
	        final Rectangle2D chartArea = new Rectangle2D.Double(0, 0, 300, 250);

	        // draw
           chart.draw(g2, chartArea, null, null);

			try {
		        FileOutputStream fOut = new FileOutputStream(fileToCheck);
		        ChartUtilities.writeChartAsPNG(fOut, chart, 300, 250);
		        return fileToCheck.getName();
			} catch (IOException e) {
				logger.error(e.getMessage(), e);
				return null;
			}	        
	}
	
	public static void main(String[] args){
//		Map<String, Double> legend = new HashMap<String, Double>();
//		legend.put("Subject A", new Double(1));
//		legend.put("Subject B", new Double(2));
//		legend.put("Subject C", new Double(3));		
//		writePieChartImageToTempFile(legend, "test");
	}
}