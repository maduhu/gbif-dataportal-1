package org.gbif.portal.dto.tag;

/**
 * @author dmartin
 */
public class Tag {

	protected int tagId;
	
	public Tag() {}
	
	public Tag(int tagId) {
		super();
		this.tagId = tagId;
	}

	/**
	 * @return the tagId
	 */
	public int getTagId() {
		return tagId;
	}

	/**
	 * @param tagId the tagId to set
	 */
	public void setTagId(int tagId) {
		this.tagId = tagId;
	}	
}