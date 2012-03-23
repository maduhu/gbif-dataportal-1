/**
 * 
 */
package launcher.nub;

import java.util.LinkedList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.gbif.portal.dao.TaxonConceptDAO;
import org.gbif.portal.harvest.taxonomy.TaxonomyUtils;
import org.gbif.portal.model.TaxonConceptLite;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;


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
	 * The ids of the resources to merge into the nub taxonomy
	 */
	protected List<Long> resourceIds;
	
	/**
	 * The DAO for dealing with the taxon concepts
	 */
	protected TaxonConceptDAO taxonConceptDAO;
	
	/**
	 * The spring context 
	 */
	protected ApplicationContext context;
	
	/**
	 * The utilities for merging the classifications
	 */
	protected TaxonomyUtils taxonomyUtils;

	/**
	 * Does the merging of the taxonomy by paging over each resources' species or lower concepts 1000 at time
	 */
	protected void launch(long id) {
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
					} else {
						logger.warn("No nub concept found for classification (needs to be imported if required):" + taxonomyUtils.toListOfTaxonName(classification));
					}
					
				} catch (RuntimeException e) {
					logger.warn("Can't set to nub [classification: {" + taxonomyUtils.toListOfTaxonName(classification) + "}]", e);
					
				}
			}				
		}
		logger.info("Finished resource id: " + id);
	}
	
	/**
	 */
	protected JoinResourceTaxonomyToNub() {
		String[] locations = {"classpath*:/**/applicationContext-*.xml",
				"classpath*:**/applicationContext-*.xml",
				"classpath*:org/gbif/portal/**/applicationContext-*.xml"
		};
		context = new ClassPathXmlApplicationContext(locations);
		taxonConceptDAO = (TaxonConceptDAO) context.getBean("taxonConceptDAO");
		taxonomyUtils = (TaxonomyUtils)context.getBean("taxonomyUtils");
	}
	
	/**
	 * @param args Usage: <nub-provider-id> <nub-resource-id> [<resource-id-to-merge-in]+
	 * E.g. NubTaxonomyCreator 1 1 10 12 14 15
	 */
	public static void main(String[] args) {
		if(args.length<1){
			System.out.println("Usage: [<resource-id-to-merge-in]+");
		} else {
			try {
				List<Long> ids = new LinkedList<Long>();
				for (int i=0; i<args.length; i++) {
					ids.add(Long.parseLong(args[i]));
				}
				JoinResourceTaxonomyToNub launcher = new JoinResourceTaxonomyToNub();
				for (Long id : ids) {
					launcher.launch(id);
				}
				
			} catch (NumberFormatException e) {
				System.out.println("Usage: [<resource-id-to-merge-in]+");
				e.printStackTrace();
			}
		}
		System.exit(1);
	}

	/**
	 * @return the taxonConceptDAO
	 */
	public TaxonConceptDAO getTaxonConceptDAO() {
		return taxonConceptDAO;
	}

	/**
	 * @param taxonConceptDAO the taxonConceptDAO to set
	 */
	public void setTaxonConceptDAO(TaxonConceptDAO taxonConceptDAO) {
		this.taxonConceptDAO = taxonConceptDAO;
	}

	/**
	 * @return the context
	 */
	public ApplicationContext getContext() {
		return context;
	}

	/**
	 * @param context the context to set
	 */
	public void setContext(ApplicationContext context) {
		this.context = context;
	}

	/**
	 * @return Returns the taxonomyUtils.
	 */
	public TaxonomyUtils getTaxonomyUtils() {
		return taxonomyUtils;
	}

	/**
	 * @param taxonomyUtils The taxonomyUtils to set.
	 */
	public void setTaxonomyUtils(TaxonomyUtils taxonomyUtils) {
		this.taxonomyUtils = taxonomyUtils;
	}
}
