package org.gbif.portal.dto.tag;

public class NumberTag extends Tag {

	protected long entityId;
	protected String entityName;
	protected int value;
	
	public NumberTag(){}
	/**
	 * @param tagId
	 * @param entityId
	 * @param entityName
	 * @param value
	 */
	public NumberTag(int tagId, long entityId, String entityName, int value) {
		super(tagId);
		this.entityId = entityId;
		this.entityName = entityName;
		this.value = value;
	}
	public long getEntityId() {
		return entityId;
	}
	public void setEntityId(long entityId) {
		this.entityId = entityId;
	}
	public String getEntityName() {
		return entityName;
	}
	public void setEntityName(String entityName) {
		this.entityName = entityName;
	}
	public int getValue() {
		return value;
	}
	public void setValue(int value) {
		this.value = value;
	}	
}
