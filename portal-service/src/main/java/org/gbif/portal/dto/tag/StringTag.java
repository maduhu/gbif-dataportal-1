/**
 * 
 */
package org.gbif.portal.dto.tag;

/**
 * @author dave
 */
public class StringTag extends Tag {
	protected long entityId;
	protected String entityName;
	protected String value;
	
	public StringTag(){}
	/**
	 * @param tagId
	 * @param entityId
	 * @param entityName
	 * @param value
	 */
	public StringTag(int tagId, long entityId, String entityName, String value) {
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
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
}
