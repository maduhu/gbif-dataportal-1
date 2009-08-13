package org.gbif.portal.dto.tag;

/**
 * @author dmartin
 */
public class GeographicalCoverageTag extends Tag {

	protected long entityId;
	protected String entityName;
	protected float minLongitude;
	protected float minLatitude;
	protected float maxLongitude;
	protected float maxLatitude;
	
	public GeographicalCoverageTag(){}
	
	public GeographicalCoverageTag(int tagId, long entityId, String entityName,
			float minLongitude, float minLatitude, float maxLongitude,
			float maxLatitude) {
		super(tagId);
		this.entityId = entityId;
		this.entityName = entityName;
		this.minLongitude = minLongitude;
		this.minLatitude = minLatitude;
		this.maxLongitude = maxLongitude;
		this.maxLatitude = maxLatitude;
	}
	/**
	 * @return the entityId
	 */
	public long getEntityId() {
		return entityId;
	}
	/**
	 * @param entityId the entityId to set
	 */
	public void setEntityId(long entityId) {
		this.entityId = entityId;
	}
	/**
	 * @return the entityName
	 */
	public String getEntityName() {
		return entityName;
	}
	/**
	 * @param entityName the entityName to set
	 */
	public void setEntityName(String entityName) {
		this.entityName = entityName;
	}
	/**
	 * @return the minLongitude
	 */
	public float getMinLongitude() {
		return minLongitude;
	}
	/**
	 * @param minLongitude the minLongitude to set
	 */
	public void setMinLongitude(float minLongitude) {
		this.minLongitude = minLongitude;
	}
	/**
	 * @return the minLatitude
	 */
	public float getMinLatitude() {
		return minLatitude;
	}
	/**
	 * @param minLatitude the minLatitude to set
	 */
	public void setMinLatitude(float minLatitude) {
		this.minLatitude = minLatitude;
	}
	/**
	 * @return the maxLongitude
	 */
	public float getMaxLongitude() {
		return maxLongitude;
	}
	/**
	 * @param maxLongitude the maxLongitude to set
	 */
	public void setMaxLongitude(float maxLongitude) {
		this.maxLongitude = maxLongitude;
	}
	/**
	 * @return the maxLatitude
	 */
	public float getMaxLatitude() {
		return maxLatitude;
	}
	/**
	 * @param maxLatitude the maxLatitude to set
	 */
	public void setMaxLatitude(float maxLatitude) {
		this.maxLatitude = maxLatitude;
	}
}