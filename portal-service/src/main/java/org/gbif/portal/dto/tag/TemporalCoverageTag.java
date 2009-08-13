/**
 * 
 */
package org.gbif.portal.dto.tag;

import java.sql.Timestamp;

/**
 * @author dmartin
 */
public class TemporalCoverageTag extends Tag {

	protected long entityId;
	protected String entityName;
	protected Timestamp startDate;
	protected Timestamp endDate;
	
	public TemporalCoverageTag(){}	
	
	public TemporalCoverageTag(int tagId, long entityId, String entityName, Timestamp startDate,
			Timestamp endDate) {
		super(tagId);
		this.entityId = entityId;
		this.entityName = entityName;
		this.startDate = startDate;
		this.endDate = endDate;
	}
	/**
	 * @return the startDate
	 */
	public Timestamp getStartDate() {
		return startDate;
	}
	/**
	 * @param startDate the startDate to set
	 */
	public void setStartDate(Timestamp startDate) {
		this.startDate = startDate;
	}
	/**
	 * @return the endDate
	 */
	public Timestamp getEndDate() {
		return endDate;
	}
	/**
	 * @param endDate the endDate to set
	 */
	public void setEndDate(Timestamp endDate) {
		this.endDate = endDate;
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
	 * @param entityId the entityId to set
	 */
	public void setEntityId(long entityId) {
		this.entityId = entityId;
	}
}
