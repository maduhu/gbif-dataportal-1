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
public class CellCountry extends ModelObject {

	private static final long serialVersionUID = 7371414090533676914L;
	protected int cellId;
	protected String isoCountryCode;
	
	
	/**
	 * Convenience
	 */
	public CellCountry(int cellId, String isoCountryCode) {
		this.cellId = cellId;
		this.isoCountryCode = isoCountryCode;
	}
	
	/**
	 * Default
	 */
	public CellCountry() {
	}

	/**
	 * @return the cellId
	 */
	public int getCellId() {
		return cellId;
	}

	/**
	 * @param cellId the cellId to set
	 */
	public void setCellId(int cellId) {
		this.cellId = cellId;
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
}