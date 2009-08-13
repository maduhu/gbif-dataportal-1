package org.gbif.portal.web.controller.openmodeller;

/**
 * Holds lat long values from GBIF occurence response
 * @author David Neufeld
 */
public class Point {
	
	public String id = null;
	public String x = null;
	public String y = null;

	/**
	* Constuct the point
	* @param the id
	* @param longitude
	* @param latitude
	*/  	
	public Point(String id, String x, String y) {
		this.id = id;
		this.x = x;
		this.y = y;
	}

}
