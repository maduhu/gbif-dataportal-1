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
package org.gbif.portal.web.content.geospatial;


/**
 * Enum type that details the mapping zoom levels supported.
 * 
 * @author Dave Martin
 */
public class ZoomLevel {

	/** 1-6, with 1 being global */
	private int level; 
	private float longitudeRange; 
	private float latitudeRange;

	/** Global */
	public static ZoomLevel ZOOM_LEVEL_1 = new ZoomLevel(1, 180, 360);
	public static ZoomLevel ZOOM_LEVEL_2 = new ZoomLevel(2, 90, 180);
	public static ZoomLevel ZOOM_LEVEL_3 = new ZoomLevel(3, 40, 80);
	public static ZoomLevel ZOOM_LEVEL_4 = new ZoomLevel(4, 20, 40);
	public static ZoomLevel ZOOM_LEVEL_5 = new ZoomLevel(5, 10, 20);
	/** highest zoom level */
	public static ZoomLevel ZOOM_LEVEL_6 = new ZoomLevel(6, 1, 2);
	
	public static ZoomLevel[] levelsDescending = new ZoomLevel[]{ZOOM_LEVEL_6, ZOOM_LEVEL_5, ZOOM_LEVEL_4, ZOOM_LEVEL_3, ZOOM_LEVEL_2, ZOOM_LEVEL_1};
	public static ZoomLevel[] levelsAscending = new ZoomLevel[]{ZOOM_LEVEL_1, ZOOM_LEVEL_2, ZOOM_LEVEL_3, ZOOM_LEVEL_4, ZOOM_LEVEL_5, ZOOM_LEVEL_6};
	
	/**
	 * 
	 * @param level the level as an int
	 * @param latitudeRange the latitude range in degrees
	 * @param longitudeRange the longitude range in degrees
	 */
	private ZoomLevel(int level, float latitudeRange, float longitudeRange){
		this.level=level;
		this.latitudeRange = latitudeRange;
		this.longitudeRange = longitudeRange;
	}

	public static ZoomLevel getZoomLevel(int zoom) {
		if(zoom>6 || zoom<1)
			return ZOOM_LEVEL_1;
		else
			return levelsAscending[zoom-1];
	}
	
	/**
	 * Get the correct zoom level for the supplied max/min lat/long values.
	 * @param minLatitude
	 * @param maxLatitude
	 * @param minLongitude
	 * @param maxLongitude
	 * @return ZoomLvel
	 */
	public static ZoomLevel getZoomLevel(float minLongitude, float minLatitude, float maxLongitude, float maxLatitude, ZoomLevel highestLevel){
		ZoomLevel zoomLevel = getZoomLevel( minLongitude, minLatitude, maxLongitude, maxLatitude);
		if(zoomLevel.getLevel()>highestLevel.getLevel())
			return highestLevel;
		return zoomLevel;
	}
	
	/**
	 * Get the correct zoom level for the supplied max/min lat/long values.
	 * @param minLatitude
	 * @param maxLatitude
	 * @param minLongitude
	 * @param maxLongitude
	 * @return ZoomLvel
	 */
	public static ZoomLevel getZoomLevel(float minLongitude, float minLatitude, float maxLongitude, float maxLatitude){

		float latRange=maxLatitude-minLatitude;
		float longRange=maxLongitude-minLongitude;
		ZoomLevel selectedZoomLevel = ZoomLevel.ZOOM_LEVEL_1; 
		for(ZoomLevel zoomLevel: ZoomLevel.levelsDescending){
			if(latRange<=zoomLevel.getLatitudeRange() && longRange<=zoomLevel.getLongitudeRange()){
				selectedZoomLevel = zoomLevel;	
				break;
			}
		}
		if(selectedZoomLevel.level>3)selectedZoomLevel=ZOOM_LEVEL_3;
		return selectedZoomLevel;
	}

	/**
	 * @return the level
	 */
	public int getLevel() {
		return level;
	}		
	
	/**
	 * @return the latitudeRange
	 */
	public float getLatitudeRange() {
		return latitudeRange;
	}

	/**
	 * @return the longitudeRange
	 */
	public float getLongitudeRange() {
		return longitudeRange;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Zoom Level [level="+level+", latitude range="+latitudeRange+", longitude range="+longitudeRange+"]";
	}
}