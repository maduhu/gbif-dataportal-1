/**
 * 
 */
package org.gbif.portal.dto.tag;

/**
 * @author davejmartin
 */
public class QuadRelationTagDTO extends TriRelationTagDTO {

	protected Long entity4Id;
	protected String entity4Name;
	
	public QuadRelationTagDTO(int tagId, Long entity1Id, String entity1Name, Long entity2Id, String entity2Name,
      Long entity3Id, String entity3Name, Long entity4Id, String entity4Name, int count) {
	  
		super(tagId, entity1Id, entity1Name, entity2Id, entity2Name, entity3Id, entity3Name, count);
	  
	  this.entity4Id = entity4Id;
	  this.entity4Name = entity4Name;
  }
	
	public Long getEntity4Id() {
  	return entity4Id;
  }
	public void setEntity4Id(Long entity4Id) {
  	this.entity4Id = entity4Id;
  }
	public String getEntity4Name() {
  	return entity4Name;
  }
	public void setEntity4Name(String entity4Name) {
  	this.entity4Name = entity4Name;
  }
}