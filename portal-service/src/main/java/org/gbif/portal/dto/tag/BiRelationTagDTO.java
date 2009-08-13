package org.gbif.portal.dto.tag;

import java.io.Serializable;
import java.util.Map;

/**
 * @author dmartin
 */
public class BiRelationTagDTO extends Tag implements Serializable{
	
	private static final long serialVersionUID = 3645452696378171998L;
	protected Long fromEntityId;
	protected String fromEntityName;
	protected Long toEntityId;
	protected String toEntityName;
	protected int count;
	protected Map<String,Object> properties;
	
	public BiRelationTagDTO(){}
	
	public BiRelationTagDTO(int tagId, Long fromEntityId, String fromEntityName,
			long toEntityId, String toEntityName, int count) {
		super(tagId);
		this.fromEntityId = fromEntityId;
		this.fromEntityName = fromEntityName;
		this.toEntityId = toEntityId;
		this.toEntityName = toEntityName;
		this.count = count;
	}

	/**
	 * @return the toEntityName
	 */
	public String getToEntityName() {
		return toEntityName;
	}
	/**
	 * @param toEntityName the toEntityName to set
	 */
	public void setToEntityName(String toEntityName) {
		this.toEntityName = toEntityName;
	}

	/**
	 * @return the fromEntityName
	 */
	public String getFromEntityName() {
		return fromEntityName;
	}
	/**
	 * @param fromEntityName the fromEntityName to set
	 */
	public void setFromEntityName(String fromEntityName) {
		this.fromEntityName = fromEntityName;
	}
	/**
	 * @return the count
	 */
	public int getCount() {
		return count;
	}
	/**
	 * @param count the count to set
	 */
	public void setCount(int count) {
		this.count = count;
	}

	/**
	 * @return the toEntityId
	 */
	public Long getToEntityId() {
		return toEntityId;
	}

	/**
	 * @param toEntityId the toEntityId to set
	 */
	public void setToEntityId(Long toEntityId) {
		this.toEntityId = toEntityId;
	}

	/**
	 * @return the fromEntityId
	 */
	public Long getFromEntityId() {
		return fromEntityId;
	}

	/**
	 * @param fromEntityId the fromEntityId to set
	 */
	public void setFromEntityId(Long fromEntityId) {
		this.fromEntityId = fromEntityId;
	}

	/**
	 * @return the properties
	 */
	public Map<String, Object> getProperties() {
		return properties;
	}

	/**
	 * @param properties the properties to set
	 */
	public void setProperties(Map<String, Object> properties) {
		this.properties = properties;
	}
}