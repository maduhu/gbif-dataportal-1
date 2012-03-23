/**
 * 
 */
package launcher.nub.multithread;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.gbif.portal.dao.TaxonConceptDAO;
import org.gbif.portal.harvest.taxonomy.TaxonomyUtils;
import org.gbif.portal.model.TaxonConceptLite;


/**
 * Joins a resource taxonomy to the nub
 *  
 * @author tim
 */
public class JoinResourceTaxonomyToNub {
	/**
	 * The commons logger
	 */
	protected Log logger = LogFactory.getLog(this.getClass());
	
	/**
	 * The resource within the nub provider that is termed the nub taxonomy
	 */
	protected long nubResourceId = 1;
	
	/**
	 * Does the merging of the taxonomy by paging over each resources' species or lower concepts 1000 at time
	 */
	protected void join(TaxonConceptDAO taxonConceptDAO, TaxonomyUtils taxonomyUtils, long id) {
		logger.info("Starting resource id: " + id);
		
		boolean hasMore = true;
		long minId = 0;
		while (hasMore) {
			List<List<TaxonConceptLite>> classifications = taxonConceptDAO.getClassificationsOf(1000, id, true, minId, 10000);
			logger.info("Received " + classifications.size() + " results to merge");
			
			// see if there needs to be another page received
			if (classifications.size()==10000) {
				List<TaxonConceptLite> classification = classifications.get(9999);
				TaxonConceptLite lastConcept =  classification.get(classification.size()-1);
				minId = lastConcept.getId();
				hasMore=true;
			} else {
				hasMore = false;
			}
			
			for (List<TaxonConceptLite> classification : classifications) {
				try {
					//TaxonConceptLite nubConcept = taxonomyUtils.getJoinPoint(taxonomyUtils.toListOfTaxonName(classification), null, 1);
					TaxonConceptLite nubConcept = taxonomyUtils.getTaxonConceptForClassification(1L, 1L, taxonomyUtils.toListOfTaxonName(classification), TaxonomyUtils.COMPARISON_THRESHOLD);
					if (nubConcept != null) {
						taxonConceptDAO.updatePartnerConcept(classification.get(classification.size()-1).getId(), nubConcept.getId());
					}
				} catch (RuntimeException e) {
					logger.warn("Can't set to nub", e);
				}
			}				
		}
		logger.info("Finished resource id: " + id);
	}
}
