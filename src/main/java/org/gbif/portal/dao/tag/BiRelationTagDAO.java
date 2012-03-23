package org.gbif.portal.dao.tag;

import java.util.List;

import org.gbif.portal.dto.tag.BiRelationTagDTO;
/**
 * DAO for tag access
 * 
 * @author dmartin
 */
public interface BiRelationTagDAO {

	public List<BiRelationTagDTO> retrieveBiRelationTagsFor(int tagId) throws Exception;
	
	public List<BiRelationTagDTO> retrieveFromBiRelationTagsFor(final int tagId, final Long fromEntityId);
	
	public List<BiRelationTagDTO> retrieveToBiRelationTagsFor(final int tagId, final Long toEntityId);
	
	public List<BiRelationTagDTO> retrieveBiRelationTagFor(final int tagId, final Long fromEntityId, final Long toEntityId);
}