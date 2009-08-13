package org.gbif.portal.dto.tag;

/**
 * @author dmartin
 */
public class BooleanTag extends Tag {
	
	protected long entityId;
	protected String entityName;
	protected boolean isTrue;
	
	public BooleanTag(){}
	
	public BooleanTag(int tagId, long entityId, String entityName,
			boolean isTrue) {
		super(tagId);
		this.entityId = entityId;
		this.entityName = entityName;
		this.isTrue = isTrue;
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
	 * @return the isTrue
	 */
	public boolean isTrue() {
		return isTrue;
	}
	/**
	 * @param isTrue the isTrue to set
	 */
	public void setTrue(boolean isTrue) {
		this.isTrue = isTrue;
	}
}