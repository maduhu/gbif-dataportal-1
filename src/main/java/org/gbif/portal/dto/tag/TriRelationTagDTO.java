/**
 * 
 */
package org.gbif.portal.dto.tag;

import java.util.Map;

/**
 * @author davejmartin
 */
public class TriRelationTagDTO {

	protected int tagId;
	protected Long entity1Id;
	protected String entity1Name;
	protected Long entity2Id;
	protected String entity2Name;
	protected Long entity3Id;
	protected String entity3Name;
	protected int count;
	protected Map<String,Object> properties;
	
	public TriRelationTagDTO(){}
	
	public TriRelationTagDTO(int tagId, Long entity1Id, String entity1Name, Long entity2Id, String entity2Name, Long entity3Id,
      String entity3Name, int count) {
		this.tagId = tagId;
	  this.entity1Id = entity1Id;
	  this.entity1Name = entity1Name;
	  this.entity2Id = entity2Id;
	  this.entity2Name = entity2Name;
	  this.entity3Id = entity3Id;
	  this.entity3Name = entity3Name;
	  this.count = count;
  }
	public Long getEntity1Id() {
  	return entity1Id;
  }
	public void setEntity1Id(Long entity1Id) {
  	this.entity1Id = entity1Id;
  }
	public String getEntity1Name() {
  	return entity1Name;
  }
	public void setEntity1Name(String entity1Name) {
  	this.entity1Name = entity1Name;
  }
	public Long getEntity2Id() {
  	return entity2Id;
  }
	public void setEntity2Id(Long entity2Id) {
  	this.entity2Id = entity2Id;
  }
	public String getEntity2Name() {
  	return entity2Name;
  }
	public void setEntity2Name(String entity2Name) {
  	this.entity2Name = entity2Name;
  }
	public Long getEntity3Id() {
  	return entity3Id;
  }
	public void setEntity3Id(Long entity3Id) {
  	this.entity3Id = entity3Id;
  }
	public String getEntity3Name() {
  	return entity3Name;
  }
	public void setEntity3Name(String entity3Name) {
  	this.entity3Name = entity3Name;
  }
	public int getCount() {
  	return count;
  }
	public void setCount(int count) {
  	this.count = count;
  }
	public Map<String, Object> getProperties() {
  	return properties;
  }
	public void setProperties(Map<String, Object> properties) {
  	this.properties = properties;
  }	
}
