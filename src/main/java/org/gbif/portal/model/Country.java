/**
 * 
 */
package org.gbif.portal.model;

/**
 * A DataResource represents a Resource on the internet that contains BioDiversity Data.
 * (A DataResource can be termed DataSet)
 * 
 * It should be noted that a DataResource is served from a DataProvider, and accessed through
 * a ResourceAccessPoint.  A DataResource is "Access Agnostic" - it can indeed be accessed through
 * multiple access methods, but the transport is effectively irrelevant.
 * 
 * @author tim
 */
public class Country extends ModelObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7037517382679330748L;
	
	protected String isoCountryCode;
	protected Float minLatitude;
	protected Float maxLatitude;
	protected Float minLongitude;
	protected Float maxLongitude;
	
	/**
	 * Convenience
	 */
	public Country(String isoCountryCode, Float minLatitude, Float maxLatitude, Float minLongitude, Float maxLongitude) {
		this.isoCountryCode = isoCountryCode;
		this.minLatitude = minLatitude;
		this.maxLatitude = maxLatitude;
		this.minLongitude = minLongitude;
		this.maxLongitude = maxLongitude;
	}
	
	/**
	 * Default
	 */
	public Country() {
	}

	/**
	 * @return the isoCountryCode
	 */
	public String getIsoCountryCode() {
		return isoCountryCode;
	}

	/**
	 * @param isoCountryCode the isoCountryCode to set
	 */
	public void setIsoCountryCode(String isoCountryCode) {
		this.isoCountryCode = isoCountryCode;
	}

	/**
	 * @return the maxLatitude
	 */
	public Float getMaxLatitude() {
		return maxLatitude;
	}

	/**
	 * @param maxLatitude the maxLatitude to set
	 */
	public void setMaxLatitude(Float maxLatitude) {
		this.maxLatitude = maxLatitude;
	}

	/**
	 * @return the maxLongitude
	 */
	public Float getMaxLongitude() {
		return maxLongitude;
	}

	/**
	 * @param maxLongitude the maxLongitude to set
	 */
	public void setMaxLongitude(Float maxLongitude) {
		this.maxLongitude = maxLongitude;
	}

	/**
	 * @return the minLatitude
	 */
	public Float getMinLatitude() {
		return minLatitude;
	}

	/**
	 * @param minLatitude the minLatitude to set
	 */
	public void setMinLatitude(Float minLatitude) {
		this.minLatitude = minLatitude;
	}

	/**
	 * @return the minLongitude
	 */
	public Float getMinLongitude() {
		return minLongitude;
	}

	/**
	 * @param minLongitude the minLongitude to set
	 */
	public void setMinLongitude(Float minLongitude) {
		this.minLongitude = minLongitude;
	}
}