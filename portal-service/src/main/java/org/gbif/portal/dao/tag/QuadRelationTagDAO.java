package org.gbif.portal.dao.tag;

import java.util.List;

import org.gbif.portal.dto.tag.QuadRelationTagDTO;
/**
 * DAO for tag access
 * 
 * @author dmartin
 */
public interface QuadRelationTagDAO {

	public List<QuadRelationTagDTO> retrieveQuadRelationTagsFor(final int tagId, final Long entity1Id, final Long entity2Id);

	public List<QuadRelationTagDTO> retrieveQuadRelationTagsForEntity1(final int tagId, final Long entity1Id);

	public List<QuadRelationTagDTO> retrieveQuadRelationTagsForEntity2(final int tagId, final Long entity2Id);	
}