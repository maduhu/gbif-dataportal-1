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
package org.gbif.portal.web.controller.dataset;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.gbif.portal.dto.log.LogStatsDTO;
import org.gbif.portal.dto.log.LoggedActivityDTO;
import org.gbif.portal.dto.resources.DataProviderDTO;
import org.gbif.portal.dto.resources.DataResourceDTO;
import org.gbif.portal.service.DataResourceManager;
import org.gbif.portal.service.LogManager;
import org.gbif.portal.web.controller.RestController;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.encoders.KeypointPNGEncoderAdapter;
import org.jfree.chart.labels.XYItemLabelGenerator;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.time.Hour;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;
import org.jfree.ui.RectangleInsets;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;

/**
 * A controller used for displaying indexing history.
 * 
 * @author dmartin
 */
public class IndexingHistoryController extends RestController {

	protected LogManager harvestingLogManager;
	protected LogManager logManager;
	protected DataResourceManager dataResourceManager;
	protected long defaultProcessShowThreshold = 60000;
	protected int defaultMinProcessingTimeToRender = 3600000;
	
	/**
	 * @see org.gbif.portal.web.controller.RestController#handleRequest(java.util.Map, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	public ModelAndView handleRequest(Map<String, String> propertiesMap, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		String providerKey = null;
		String resourceKey = null;
		DataProviderDTO dataProvider = null;
		DataResourceDTO dataResource = null;
		
		String type = propertiesMap.get("type");
		String key = propertiesMap.get("key");
		String logGroup = propertiesMap.get("logGroup");
		
		if("provider".equals(type)){
			providerKey = key;
		} 
		if("resource".equals(type)){
			resourceKey = key;
		}
		
		if(logGroup!=null){

			ModelAndView mav = new ModelAndView("logGroupStats");
			if(providerKey!=null){
				dataProvider = dataResourceManager.getDataProviderFor(providerKey);
				mav.addObject("dataProvider", dataProvider);
			}
			if(resourceKey!=null){
				dataResource = dataResourceManager.getDataResourceFor(resourceKey);
				dataProvider = dataResourceManager.getDataProviderFor(dataResource.getDataProviderKey());
				mav.addObject("dataResource", dataResource);
				if(providerKey==null){
					mav.addObject("dataProvider", dataProvider);
				}
			}
			List<LogStatsDTO> logStats = logManager.getDataResourceLogStatsFor(providerKey, resourceKey, null, null, null, null, null, null, null, logGroup, null, null);
			mav.addObject("logStats", logStats);
			mav.addObject("logGroup", logGroup);
			return mav;
		}
		
		boolean hideNonFinishers = ServletRequestUtils.getBooleanParameter(request, "hideNonFinishers", true);
		int minProcessingTimeToRender = ServletRequestUtils.getIntParameter(request, "minProcessingTimeToRender", defaultMinProcessingTimeToRender);
		long processShowThreshold = ServletRequestUtils.getLongParameter(request, "processShowThreshold", defaultProcessShowThreshold);
		
		List<LoggedActivityDTO> harvestingHistory = harvestingLogManager.getHarvestingHistory(providerKey, resourceKey);
		List<LoggedActivityDTO> extractionHistory = logManager.getExtractionHistory(providerKey, resourceKey);
		
		//total indexing time
		long totalHarvesting = calculateProcessingTime(hideNonFinishers, processShowThreshold, harvestingHistory);
		long totalExtraction = calculateProcessingTime(hideNonFinishers, processShowThreshold, extractionHistory);
		long totalProcessing = totalHarvesting + totalExtraction;
		
		List<LoggedActivityDTO> indexingHistory  = harvestingHistory;
		indexingHistory.addAll(extractionHistory);
		
		//sort chronologically
		Collections.sort(indexingHistory, new Comparator<LoggedActivityDTO>(){
			public int compare(LoggedActivityDTO o1, LoggedActivityDTO o2) {
				if(o1.getLogGroup()!=null && o2.getLogGroup()!=null)
					return o1.getLogGroup().compareTo(o2.getLogGroup()); 
				return -1;
			}
		});
		
		ModelAndView mav = new ModelAndView("indexingHistory");
		if(providerKey!=null){
			dataProvider = dataResourceManager.getDataProviderFor(providerKey);
			mav.addObject("dataProvider", dataProvider);
		}
		if(resourceKey!=null){
			dataResource = dataResourceManager.getDataResourceFor(resourceKey);
			mav.addObject("dataResource", dataResource);
			if(providerKey==null && dataResource!=null){
				dataProvider = dataResourceManager.getDataProviderFor(dataResource.getDataProviderKey());
				mav.addObject("dataProvider", dataProvider);
			}
		}
		
		//add indexing history
		mav.addObject("indexingHistory", indexingHistory);

		//add processing time durations		
		mav.addObject("totalHarvesting", totalHarvesting);
		mav.addObject("totalExtraction", totalExtraction);
		mav.addObject("totalProcessing", totalProcessing);
		
		//provide the available log range dates
		mav.addObject("firstAvailableLog", harvestingLogManager.getEarliestLogMessageDate());
		mav.addObject("lastAvailableLog", harvestingLogManager.getLatestLogMessageDate());
		
		//time series
		if((dataProvider!=null || dataResource!=null) && !indexingHistory.isEmpty()){
			String fileName = timeSeriesTest(dataProvider, dataResource, indexingHistory, "indexing-history", minProcessingTimeToRender);
			if(fileName!=null){
				mav.addObject("timeSeriesChart", fileName);
			}
		}
		
		//resort
		Collections.sort(indexingHistory, new Comparator<LoggedActivityDTO>(){
			public int compare(LoggedActivityDTO o1, LoggedActivityDTO o2) {
				if(o1.getLogGroup()!=null && o2.getLogGroup()!=null)
					return o1.getLogGroup().compareTo(o2.getLogGroup()); 
				return -1;
			}
		});
		return mav;
	}

	/**
	 * Calculate the processing time for a history of activities.
	 * @param hideNonstarters
	 * @param processShowThreshold
	 * @param activityHistory
	 * @return
	 */
	private long calculateProcessingTime(boolean hideNonstarters,
			long processShowThreshold,
			List<LoggedActivityDTO> activityHistory) {
		List<LoggedActivityDTO> toBeRemoved = new ArrayList<LoggedActivityDTO>();
		long totalHarvesting = 0;
		for(LoggedActivityDTO laDTO: activityHistory){
		
			if(hideNonstarters && (laDTO.getDurationInMillisecs()==null || laDTO.getDurationInMillisecs()<processShowThreshold)){
				toBeRemoved.add(laDTO);
			}
			
			if(laDTO.getDurationInMillisecs()!=null){
				totalHarvesting+=laDTO.getDurationInMillisecs();	
			}
		}
		activityHistory.removeAll(toBeRemoved);
		return totalHarvesting;
	}
	
	/**
	 * Create a time series graphic to display indexing processes.
	 * 
	 * @param dataProvider
	 * @param dataResource
	 * @param activities
	 * @param fileNamePrefix
	 * @return
	 */
	public String timeSeriesTest(DataProviderDTO dataProvider, 
			DataResourceDTO dataResource, 
			List<LoggedActivityDTO> loggedActivities, 
			String fileNamePrefix,
			int minProcessingTimeToRender){
		
		List<LoggedActivityDTO> activities = new ArrayList<LoggedActivityDTO>();
		for(LoggedActivityDTO la:loggedActivities){
			if(la.getDataResourceKey()!=null 
					&& la.getDataResourceName()!=null
					&& la.getEventName()!=null)
			activities.add(la);
		}
		
		//if no activities to render, return
		if(activities.isEmpty())
			return null;
		
		Map<String, Integer> drActualCount = new HashMap<String, Integer>();
		Map<String, Integer> drCount = new HashMap<String, Integer>();
		
		//record the actual counts
		for (LoggedActivityDTO laDTO: activities){
			if(laDTO.getStartDate()!=null && laDTO.getEndDate()!=null && laDTO.getDurationInMillisecs()>minProcessingTimeToRender){			
				if(drActualCount.get(laDTO.getDataResourceName())==null){
					drActualCount.put(laDTO.getDataResourceName(), new Integer(4));
					drCount.put(laDTO.getDataResourceName(), new Integer(0));
				} else {
					Integer theCount = drActualCount.get(laDTO.getDataResourceName());
					theCount = new Integer(theCount.intValue()+4);
					drActualCount.remove(laDTO.getDataResourceName());
					drActualCount.put(laDTO.getDataResourceName(), theCount);
				}
			}
		}
		
		StringBuffer fileNameBuffer = new StringBuffer(fileNamePrefix);
		if(dataResource!=null){
			fileNameBuffer.append("-resource-");
			fileNameBuffer.append(dataResource.getKey());
		} else if (dataProvider!=null){
			fileNameBuffer.append("-provider-");
			fileNameBuffer.append(dataProvider.getKey());
		}
		fileNameBuffer.append(".png");
		
		String fileName = fileNameBuffer.toString();
		String filePath = System.getProperty("java.io.tmpdir")+File.separator+fileName;

		File fileToCheck = new File(filePath);
		if(fileToCheck.exists()){
			return fileName;
		}
		
		TimeSeriesCollection dataset = new TimeSeriesCollection();
		boolean generateChart = false;
		
		int count=1;
		int dataResourceCount = 1;

		Collections.sort(activities, new Comparator<LoggedActivityDTO>(){
			public int compare(LoggedActivityDTO o1, LoggedActivityDTO o2) {
				if(o1==null || o2==null || o1.getDataResourceKey()!=null || o2.getDataResourceKey()!=null)
					return -1;
				return o1.getDataResourceKey().compareTo(o2.getDataResourceKey());
			}
		});
		
		String currentDataResourceKey = activities.get(0).getDataResourceKey();		
		
		for(LoggedActivityDTO laDTO: activities){
			if(laDTO.getStartDate()!=null && laDTO.getEndDate()!=null && laDTO.getDurationInMillisecs()>minProcessingTimeToRender){
				
				if(currentDataResourceKey!=null && !currentDataResourceKey.equals(laDTO.getDataResourceKey())){
					dataResourceCount++;
					count=count+1;
					currentDataResourceKey = laDTO.getDataResourceKey();
				}
				TimeSeries s1 = new TimeSeries(laDTO.getDataResourceName(), "Process time period", laDTO.getEventName(), Hour.class);
				s1.add(new Hour(laDTO.getStartDate()), count); 
				s1.add(new Hour(laDTO.getEndDate()), count);
				dataset.addSeries(s1);
				generateChart = true;
			}
		}
		
		if(!generateChart)
			return null;
		
        // create a pie chart...
        final JFreeChart chart = ChartFactory.createTimeSeriesChart(null, null, null, dataset, false, false, false);
        
        XYPlot plot = chart.getXYPlot();
        plot.setWeight(10);
        plot.getRangeAxis().setAutoRange(false);
        plot.getRangeAxis().setRange(0, drCount.size() +1);
        plot.getRangeAxis().setAxisLineVisible(false);
        plot.getRangeAxis().setAxisLinePaint(Color.WHITE);
        plot.setDomainCrosshairValue(1);
        plot.setRangeGridlinesVisible(false);
        plot.getRangeAxis().setVisible(false);
        plot.getRangeAxis().setLabel("datasets");
        
        XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) plot.getRenderer();
        renderer.setItemLabelsVisible(true);
        MyXYItemLabelGenerator labelGenerator = new MyXYItemLabelGenerator();
        labelGenerator.setDataResourceActualCount(drActualCount);
        labelGenerator.setDataResourceCount(drCount);
        renderer.setItemLabelGenerator(labelGenerator);
        
        List<TimeSeries> seriesList = dataset.getSeries();
        for(TimeSeries series : seriesList){
        	if(((String)series.getRangeDescription()).startsWith("extraction")){
        		renderer.setSeriesPaint(seriesList.indexOf(series), Color.RED);
        	} else {
        		renderer.setSeriesPaint(seriesList.indexOf(series), Color.BLUE);
        	}
        	renderer.setSeriesStroke(seriesList.indexOf(series), new BasicStroke(7f));
        }
        
        int imageHeight = 30*dataResourceCount;
        if(imageHeight<100){
        	imageHeight=100;
        } else {
        	imageHeight=imageHeight+100;
        }
        
        final BufferedImage image = new BufferedImage(900,  imageHeight, BufferedImage.TYPE_INT_RGB);
        KeypointPNGEncoderAdapter adapter = new KeypointPNGEncoderAdapter();
        adapter.setQuality(1);
        try {
			adapter.encode(image);
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}
        final Graphics2D g2 = image.createGraphics();
        g2.setFont(new Font("Arial", Font.PLAIN, 11));
        final Rectangle2D chartArea = new Rectangle2D.Double(0, 0, 900, imageHeight);

        // draw
        chart.draw(g2, chartArea, null, null);

        //styling
        chart.setPadding(new RectangleInsets(0,0,0,0));
        chart.setBorderVisible(false);
        chart.setBackgroundImageAlpha(0);
        chart.setBackgroundPaint(Color.WHITE);
        chart.setBorderPaint(Color.LIGHT_GRAY);

		try {
	        FileOutputStream fOut = new FileOutputStream(filePath);
	        ChartUtilities.writeChartAsPNG(fOut, chart, 900,  imageHeight);
	        return fileName;
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}			
		return null;
	}

//	public static void main(String[] args) throws Exception{
//		
//		List<LoggedActivityDTO> laList = new ArrayList<LoggedActivityDTO>();
//		
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
//		
//		laList.add(new LoggedActivityDTO("177", "Provider 1", "966", "Data Resource 1", "Event 1", 1l, 1 ,2, sdf.parse("20070101"), sdf.parse("20070103"), 36000000l));
//		laList.add(new LoggedActivityDTO("177", "Provider 1", "967", "Data Resource 2", "Event 2", 1l, 1 ,2, sdf.parse("20070501"),sdf.parse("20070503"), 36000000l));
//		laList.add(new LoggedActivityDTO("177", "Provider 1", "968", "Data Resource 3", "Event 2", 1l, 1 ,2, sdf.parse("20070301"),sdf.parse("20070303"), 36000000l));
//		
//		DataProviderDTO dataProvider = new DataProviderDTO();
//		dataProvider.setKey("177");
//		dataProvider.setName("Provider 1");
//		
//		IndexingHistoryController ihc = new IndexingHistoryController();
//		ihc.timeSeriesTest(dataProvider, null, laList, "MyTest");
//	}
	
	/**
	 * @see XYItemLabelGenerator
	 */
	public class MyXYItemLabelGenerator implements XYItemLabelGenerator {
    	
		protected Map<String, Integer> dataResourceActualCount = new HashMap<String, Integer>();
    	protected Map<String, Integer> dataResourceCount = new HashMap<String, Integer>();
    	
		public String generateLabel(XYDataset dataset, int series, int category) {
			//get next
			String seriesKey = (String) dataset.getSeriesKey(series);
			Integer currentCount = dataResourceCount.get(seriesKey);
			Integer count = dataResourceActualCount.get(seriesKey);
			if(currentCount.intValue()==count.intValue()-1 && category==1){
				currentCount=new Integer(currentCount.intValue()+1);
				dataResourceCount.remove(seriesKey);
				dataResourceCount.put(seriesKey, currentCount);
				return seriesKey;
			}
			currentCount=new Integer(currentCount.intValue()+1);
			dataResourceCount.remove(seriesKey);
			dataResourceCount.put(seriesKey, currentCount);
			return "";
		}

		/**
		 * @return the dataResourceActualCount
		 */
		public Map<String, Integer> getDataResourceActualCount() {
			return dataResourceActualCount;
		}

		/**
		 * @param dataResourceActualCount the dataResourceActualCount to set
		 */
		public void setDataResourceActualCount(
				Map<String, Integer> dataResourceActualCount) {
			this.dataResourceActualCount = dataResourceActualCount;
		}

		/**
		 * @return the dataResourceCount
		 */
		public Map<String, Integer> getDataResourceCount() {
			return dataResourceCount;
		}

		/**
		 * @param dataResourceCount the dataResourceCount to set
		 */
		public void setDataResourceCount(Map<String, Integer> dataResourceCount) {
			this.dataResourceCount = dataResourceCount;
		}
    };	
	
	/**
	 * @return the logManager
	 */
	public LogManager getLogManager() {
		return logManager;
	}

	/**
	 * @param logManager the logManager to set
	 */
	public void setLogManager(LogManager logManager) {
		this.logManager = logManager;
	}

	/**
	 * @return the dataResourceManager
	 */
	public DataResourceManager getDataResourceManager() {
		return dataResourceManager;
	}

	/**
	 * @param dataResourceManager the dataResourceManager to set
	 */
	public void setDataResourceManager(DataResourceManager dataResourceManager) {
		this.dataResourceManager = dataResourceManager;
	}

	/**
	 * @param defaultProcessShowThreshold the defaultProcessShowThreshold to set
	 */
	public void setDefaultProcessShowThreshold(long defaultProcessShowThreshold) {
		this.defaultProcessShowThreshold = defaultProcessShowThreshold;
	}

	/**
	 * @param harvestingLogManager the harvestingLogManager to set
	 */
	public void setHarvestingLogManager(LogManager harvestingLogManager) {
		this.harvestingLogManager = harvestingLogManager;
	}

	/**
	 * @param defaultMinProcessingTimeToRender the defaultMinProcessingTimeToRender to set
	 */
	public void setDefaultMinProcessingTimeToRender(
			int defaultMinProcessingTimeToRender) {
		this.defaultMinProcessingTimeToRender = defaultMinProcessingTimeToRender;
	}
}