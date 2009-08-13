/**
 * 
 */
package org.gbif.portal.dto.taxonomy;

import org.gbif.portal.model.taxonomy.CommonName;
import org.gbif.portal.model.taxonomy.TaxonConcept;

/**
 * @author dave
 */
public class TaxonConceptCommonNameDTOFactory extends TaxonConceptDTOFactory {

	/**
	 * @see org.gbif.portal.dto.taxonomy.TaxonConceptDTOFactory#createDTO(java.lang.Object)
	 */
	@Override
	public Object createDTO(Object modelObject) {
		if(modelObject instanceof CommonName){
			CommonName cn = (CommonName) modelObject;
			TaxonConcept tc = cn.getTaxonConcept();
			TaxonConceptDTO tcDTO = (TaxonConceptDTO) super.createDTO(tc);
			tcDTO.setCommonName(cn.getName());
			tcDTO.setCommonNameLanguage(cn.getLanguage());
			return tcDTO;
		}
		return null;
	}
}
