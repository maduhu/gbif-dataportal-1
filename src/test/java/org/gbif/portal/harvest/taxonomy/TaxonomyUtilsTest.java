package org.gbif.portal.harvest.taxonomy;

import java.util.LinkedList;
import java.util.List;

import org.gbif.portal.model.TaxonName;
import org.springframework.test.AbstractDependencyInjectionSpringContextTests;

/**
 * 
 * @author trobertson
 */
public class TaxonomyUtilsTest extends
		AbstractDependencyInjectionSpringContextTests {
	protected TaxonomyUtils taxonomyUtils;

	/**
	 * @see org.springframework.test.AbstractDependencyInjectionSpringContextTests#getConfigLocations()
	 */
	protected String[] getConfigLocations() {
		return new String[] { "classpath*:org/gbif/portal/**/applicationContext-*.xml", };
	}

	/**
	 * Test method for
	 * 'org.gbif.portal.harvest.taxonomy.TaxonomyUtils.classificationsComparator(List<TaxonName>,
	 * List<TaxonName>, int)'
	 */
	public void testMatchingFamilyGenusSpecies() {
		int threshold = 50;

		List<TaxonName> sourceA = new LinkedList<TaxonName>();
		sourceA.add(new TaxonName("Animalia", null, 1000));
		sourceA.add(new TaxonName("Chordata", null, 2000));
		sourceA.add(new TaxonName("Mammalia", null, 3000));
		sourceA.add(new TaxonName("Felidae", null, 5000));
		sourceA.add(new TaxonName("Puma", null, 6000));
		sourceA.add(new TaxonName("Puma concolor", null, 7000));

		List<TaxonName> sourceB = new LinkedList<TaxonName>();
		sourceB.add(new TaxonName("Plantae", null, 1000));
		sourceB.add(new TaxonName("Magnoliophyta", null, 2000));
		sourceB.add(new TaxonName("Magnoliopsida", null, 3000));
		sourceB.add(new TaxonName("Umbelliferales", null, 4000));
		sourceB.add(new TaxonName("Asteraceae", null, 5000));
		sourceB.add(new TaxonName("Oenanthe", null, 6000));
		sourceB.add(new TaxonName("Oenanthe fluviatilis", null, 7000));

		List<TaxonName> sourceC = new LinkedList<TaxonName>();
		sourceC.add(new TaxonName("Plantae", null, 1000));
		sourceC.add(new TaxonName("Magnoliophyta", null, 2000));
		sourceC.add(new TaxonName("Magnoliopsida", null, 3000));
		sourceC.add(new TaxonName("Umbelliferales", null, 4000));
		sourceC.add(new TaxonName("Felidae", null, 5000));
		sourceC.add(new TaxonName("Puma", null, 6000));
		sourceC.add(new TaxonName("Puma concolor", null, 7000));

		int measureAA = taxonomyUtils.classificationsComparator(sourceA,
				sourceA, 6000, false);
		int measureAB = taxonomyUtils.classificationsComparator(sourceA,
				sourceB, 6000, false);
		int measureAC = taxonomyUtils.classificationsComparator(sourceA,
				sourceC, 6000, false);
		int measureBC = taxonomyUtils.classificationsComparator(sourceB,
				sourceC, 6000, false);
		int measureCA = taxonomyUtils.classificationsComparator(sourceC,
				sourceA, 6000, false);
		int measureCB = taxonomyUtils.classificationsComparator(sourceC,
				sourceB, 6000, false);
		int measureCC = taxonomyUtils.classificationsComparator(sourceC,
				sourceC, 6000, false);

		assertEquals(measureAC, measureCA);
		assertEquals(measureBC, measureCB);
		assertEquals(measureAB, 0);
		assertEquals(measureAA, 99);
		assertEquals(measureCC, 100);
		assert (measureAC > measureBC);
		assert (measureAC >= threshold);
	}

	/**
	 * Test method for
	 * 'org.gbif.portal.harvest.taxonomy.TaxonomyUtils.classificationsComparator(List<TaxonName>,
	 * List<TaxonName>, int)'
	 */
	public void testOenanthe() {
		int threshold = 33;
		
		List<List<ClassificationHolder>> nub = new LinkedList<List<ClassificationHolder>>();

		List<TaxonName> classification = new LinkedList<TaxonName>();
		classification.add(new TaxonName("Animalia", null, 1000));
		classification.add(new TaxonName("Chordata", null, 2000));
		classification.add(new TaxonName("Aves", null, 3000));
		classification.add(new TaxonName("Passeriformes", null, 4000));
		classification.add(new TaxonName("Muscicapidae", null, 5000));
		classification.add(new TaxonName("Oenanthe", null, 6000));
		
		findOrAddToNub(nub, classification, threshold, 1);

		classification = new LinkedList<TaxonName>();
		classification.add(new TaxonName("Plantae", null, 1000));
		classification.add(new TaxonName("Magnoliophyta", null, 2000));
		classification.add(new TaxonName("Magnoliopsida", null, 3000));
		classification.add(new TaxonName("Apiales", null, 4000));
		classification.add(new TaxonName("Apiaceae", null, 5000));
		classification.add(new TaxonName("Oenanthe", null, 6000));

		findOrAddToNub(nub, classification, threshold, 1);

		classification = new LinkedList<TaxonName>();
		classification.add(new TaxonName("Unknown", null, 1000));
		classification.add(new TaxonName("Umbelliferae", null, 5000));
		classification.add(new TaxonName("Oenanthe", null, 6000));

		findOrAddToNub(nub, classification, threshold, 100);

		classification = new LinkedList<TaxonName>();
		classification.add(new TaxonName("Animalia", null, 1000));
		classification.add(new TaxonName("Chordata", null, 2000));
		classification.add(new TaxonName("Aves", null, 3000));
		classification.add(new TaxonName("Passeriformes", null, 4000));
		classification.add(new TaxonName("Turdidae", null, 5000));
		classification.add(new TaxonName("Oenanthe", null, 6000));

		findOrAddToNub(nub, classification, threshold, 100);

		classification = new LinkedList<TaxonName>();
		classification.add(new TaxonName("Animalia", null, 1000));
		classification.add(new TaxonName("Aves", null, 3000));
		classification.add(new TaxonName("Turdidae", null, 5000));
		classification.add(new TaxonName("Oenanthe", null, 6000));

		findOrAddToNub(nub, classification, threshold, 100);

		classification = new LinkedList<TaxonName>();
		classification.add(new TaxonName("Animalia", null, 1000));
		classification.add(new TaxonName("Turdidae", null, 5000));
		classification.add(new TaxonName("Oenanthe", null, 6000));

		findOrAddToNub(nub, classification, threshold, 100);

		classification = new LinkedList<TaxonName>();
		classification.add(new TaxonName("Plantae", null, 1000));
		classification.add(new TaxonName("Magnoliophyta", null, 2000));
		classification.add(new TaxonName("Magnoliopsida", null, 3000));
		classification.add(new TaxonName("Apiales", null, 4000));
		classification.add(new TaxonName("Apiaceae", null, 5000));
		classification.add(new TaxonName("Oenanthe", null, 6000));

		findOrAddToNub(nub, classification, threshold, 100);

		classification = new LinkedList<TaxonName>();
		classification.add(new TaxonName("Plantae", null, 1000));
		classification.add(new TaxonName("Magnoliophyta", null, 2000));
		classification.add(new TaxonName("Dicotyledonae", null, 3000));
		classification.add(new TaxonName("Apiales", null, 4000));
		classification.add(new TaxonName("Apiaceae", null, 5000));
		classification.add(new TaxonName("Oenanthe", null, 6000));

		findOrAddToNub(nub, classification, threshold, 100);

		classification = new LinkedList<TaxonName>();
		classification.add(new TaxonName("Plantae", null, 1000));
		classification.add(new TaxonName("Spermatophyta", null, 2000));
		classification.add(new TaxonName("Dicotyledoneae", null, 3000));
		classification.add(new TaxonName("Umbelliflorae", null, 4000));
		classification.add(new TaxonName("Umbelliferae", null, 5000));
		classification.add(new TaxonName("Oenanthe", null, 6000));

		findOrAddToNub(nub, classification, threshold, 100);

		classification = new LinkedList<TaxonName>();
		classification.add(new TaxonName("Plantae", null, 1000));
		classification.add(new TaxonName("Angiospermae", null, 2000));
		classification.add(new TaxonName("Dicotyledoneae", null, 3000));
		classification.add(new TaxonName("Umbelliflorae", null, 4000));
		classification.add(new TaxonName("Umbelliferae", null, 5000));
		classification.add(new TaxonName("Oenanthe", null, 6000));

		findOrAddToNub(nub, classification, threshold, 100);

		classification = new LinkedList<TaxonName>();
		classification.add(new TaxonName("Carex", null, 1000));
		classification.add(new TaxonName("Magnoliophyta", null, 2000));
		classification.add(new TaxonName("Dicotyledonae", null, 3000));
		classification.add(new TaxonName("Araliales", null, 4000));
		classification.add(new TaxonName("Apiaceae", null, 5000));
		classification.add(new TaxonName("Oenanthe", null, 6000));

		findOrAddToNub(nub, classification, threshold, 100);

		classification = new LinkedList<TaxonName>();
		classification.add(new TaxonName("Plantae", null, 1000));
		classification.add(new TaxonName("Oenanthe", null, 6000));

		findOrAddToNub(nub, classification, threshold, 100);

		classification = new LinkedList<TaxonName>();
		classification.add(new TaxonName("Plantae", null, 1000));
		classification.add(new TaxonName("Tracheophyta", null, 2000));
		classification.add(new TaxonName("Oenanthe", null, 6000));

		findOrAddToNub(nub, classification, threshold, 100);

		classification = new LinkedList<TaxonName>();
		classification.add(new TaxonName("Plantae", null, 1000));
		classification.add(new TaxonName("Anthophyta", null, 2000));
		classification.add(new TaxonName("Oenanthe", null, 6000));

		findOrAddToNub(nub, classification, threshold, 100);

		classification = new LinkedList<TaxonName>();
		classification.add(new TaxonName("Plantae", null, 1000));
		classification.add(new TaxonName("Apiaceae", null, 5000));
		classification.add(new TaxonName("Oenanthe", null, 6000));

		findOrAddToNub(nub, classification, threshold, 100);

		classification = new LinkedList<TaxonName>();
		classification.add(new TaxonName("Oenanthe", null, 6000));

		findOrAddToNub(nub, classification, threshold, 100);
		
		for (List<ClassificationHolder> classificationSet : nub) {
			System.out.println("\nThe following group together:\n");
			for (ClassificationHolder c : classificationSet) {
				String entry = "    " + c.getConfidence();
				for (TaxonName t : c.getClassification()) {
					entry += " : " + t.getCanonical();
				}
				System.out.println(entry);
			}
		}
		
		assertEquals(nub.size(), 4);
	}

	private class ClassificationHolder {
		private List<TaxonName> classification;
		private int priority;
		private int confidence;
		/**
		 * @return the classification
		 */
		public List<TaxonName> getClassification() {
			return classification;
		}
		/**
		 * @param classification the classification to set
		 */
		public void setClassification(List<TaxonName> classification) {
			this.classification = classification;
		}
		/**
		 * @return the confidence
		 */
		public int getConfidence() {
			return confidence;
		}
		/**
		 * @param confidence the confidence to set
		 */
		public void setConfidence(int confidence) {
			this.confidence = confidence;
		}
		/**
		 * @return the priority
		 */
		public int getPriority() {
			return priority;
		}
		/**
		 * @param priority the priority to set
		 */
		public void setPriority(int priority) {
			this.priority = priority;
		}
		
	}
	
	private void findOrAddToNub(List<List<ClassificationHolder>> nub, List<TaxonName> classification, int threshold, int priority) {
		int bestMeasure = -1;
		List<ClassificationHolder> bestClassificationSet = null;
		for (List<ClassificationHolder> classificationSet : nub) {
			ClassificationHolder existing = classificationSet.get(0);
			int measure = taxonomyUtils.classificationsComparator(existing.getClassification(), classification, 5000, false);
			if (measure > bestMeasure) {
				bestMeasure = measure;
				bestClassificationSet = classificationSet;
			} else if (measure == bestMeasure) {
				bestClassificationSet = null;
			}
		}
		
		ClassificationHolder holder = new ClassificationHolder();
		holder.setClassification(classification);
		holder.setConfidence(bestMeasure);
		holder.setPriority(priority);

		if (bestClassificationSet == null || bestMeasure < threshold) {
			bestClassificationSet = new LinkedList<ClassificationHolder>();
			nub.add(bestClassificationSet);
			bestClassificationSet.add(holder);
		} else {
			if (bestClassificationSet.get(0).getPriority() > priority) {
				bestClassificationSet.add(0, holder);
			} else {
				bestClassificationSet.add(holder);
			}
		}
	}

	/**
	 * Test method for
	 * 'org.gbif.portal.harvest.taxonomy.TaxonomyUtils.classificationsComparator(List<TaxonName>,
	 * List<TaxonName>, int)'
	 */
	public void testMovingIntermediateTaxa() {
		List<TaxonName> sourceA = new LinkedList<TaxonName>();
		sourceA.add(new TaxonName("Animalia", null, 1000));
		sourceA.add(new TaxonName("Hexapoda", null, 2000));
		sourceA.add(new TaxonName("Insecta", null, 3000));
		sourceA.add(new TaxonName("Coleoptera", null, 4000));
		sourceA.add(new TaxonName("Coccinella", null, 6000));
		sourceA.add(new TaxonName("Coccinella septempunctata", null, 7000));

		List<TaxonName> sourceB = new LinkedList<TaxonName>();
		sourceB.add(new TaxonName("Animalia", null, 1000));
		sourceB.add(new TaxonName("Arthropoda", null, 2000));
		sourceB.add(new TaxonName("Hexapoda", null, 3000));
		sourceB.add(new TaxonName("Coleoptera", null, 5000));
		sourceB.add(new TaxonName("Coccinella", null, 6000));
		sourceB.add(new TaxonName("Coccinella septempunctata", null, 7000));

		int measureAB = taxonomyUtils.classificationsComparator(sourceA,
				sourceB, 7000, false);
		int measureBA = taxonomyUtils.classificationsComparator(sourceB,
				sourceA, 7000, false);

		assertEquals(measureBA, measureAB);
		assertEquals(measureAB, 98);
	}

	/**
	 * @return the taxonomyUtils
	 */
	public TaxonomyUtils getTaxonomyUtils() {
		return taxonomyUtils;
	}

	/**
	 * @param taxonomyUtils
	 *            the taxonomyUtils to set
	 */
	public void setTaxonomyUtils(TaxonomyUtils taxonomyUtils) {
		this.taxonomyUtils = taxonomyUtils;
	}
}
