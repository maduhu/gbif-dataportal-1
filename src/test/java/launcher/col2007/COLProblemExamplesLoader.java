package launcher.col2007;
import org.gbif.portal.dao.TaxonConceptDAO;
import org.gbif.portal.model.TaxonConcept;
import org.gbif.portal.model.TaxonName;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/***************************************************************************
 * Copyright (C) 2005 Global Biodiversity Information Facility Secretariat.
 * All Rights Reserved.
 *
 * The contents of this file are subject to the Mozilla Public
 * License Version 1.1 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of
 * the License at http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS
 * IS" basis, WITHOUT WARRANTY OF ANY KIND, either express or
 * implied. See the License for the specific language governing
 * rights and limitations under the License.
 ***************************************************************************/

/**
 * @author trobertson
 *
 */
public class COLProblemExamplesLoader {
	private ApplicationContext context;
	
	private void init() {
		String[] locations = {"classpath*:/**/applicationContext-*.xml",
				"classpath*:**/applicationContext-*.xml",
				"classpath*:org/gbif/portal/**/applicationContext-*.xml"
		};
		
		context = new ClassPathXmlApplicationContext(locations);
	}

	
	public void load() {
		// The Ocimum problem...
		long id = create(new TaxonName("Plantae", null, 1000), true, null);
		long p_id = id;
		id = create(new TaxonName("Magnoliophyta", null, 2000), true, id);
		id = create(new TaxonName("Magnoliopsida", null, 3000), true, id);
		id = create(new TaxonName("Lamiales", null, 4000), true, id);
		id = create(new TaxonName("Lamiaceae", null, 5000), true, id);
		long id2 = id;
		id = create(new TaxonName("Ocimum", null, 6000), true, id);
		id = create(new TaxonName("Ocimum campechianum", null, 7000), true, id);
		// couple of non accepted ones...
		id = create(new TaxonName("Ocimum", null, 6000), false, id2);
		id = create(new TaxonName("Ocimum basilicum", null, 7000), false, id);
		
		// The Pomatomus problem....
		id = create(new TaxonName("Animalia", null, 1000), true, null);
		id = create(new TaxonName("Chordata", null, 2000), true, id);
		long chor_id = id;
		id = create(new TaxonName("Actinopterygii", null, 3000), true, id);
		id = create(new TaxonName("Perciformes", null, 4000), true, id);
		id2 = id;
		id = create(new TaxonName("Epigonidae", null, 5000), true, id);  // it IS accepted!
		id = create(new TaxonName("Pomatomus", null, 6000), false, id);
		id = create(new TaxonName("Pomatomus telescopus", null, 7000), false, id);
		id = create(new TaxonName("Pomatomidae", null, 5000), true, id2);
		id = create(new TaxonName("Pomatomus", null, 6000), true, id);
		id = create(new TaxonName("Pomatomus saltatrix", null, 7000), true, id);
		
		// the Oenanthe problem
		id = create(new TaxonName("Aves", null, 3000), true, chor_id);
		id = create(new TaxonName("Passeriformes", null, 4000), true, id);
		id = create(new TaxonName("Muscicapidae", null, 5000), true, id);
		id = create(new TaxonName("Oenanthe", null, 6000), true, id);
		id = create(new TaxonName("Oenanthe monacha", null, 7000), true, id);
		id = create(new TaxonName("Oenanthe", null, 6000), false, p_id);
		id = create(new TaxonName("Oenanthe javanica", null, 7000), false, id);
		id = create(new TaxonName("Magnoliophyta", null, 2000), true, p_id);
		id = create(new TaxonName("Magnoliopsida", null, 3000), true, id);
		id = create(new TaxonName("Apiales", null, 4000), true, id);
		id = create(new TaxonName("Apiaceae", null, 5000), true, id);
		id = create(new TaxonName("Oenanthe", null, 6000), true, id);
		id = create(new TaxonName("Oenanthe laciniata", null, 7000), true, id);
		
		
	}
	
	public long create(TaxonName name, boolean accepted, Long parentId) {
		TaxonConceptDAO dao = (TaxonConceptDAO) context.getBean("taxonConceptDAO");
		TaxonConcept tc = new TaxonConcept();
		tc.setDataProviderId((long)2);
		tc.setDataResourceId((long)2);
		tc.setRank(name.getRank());
		tc.setParentId(parentId);
		tc.setAccepted(accepted);
		tc.setTaxonName(name);
		tc.setPriority(1);
		return dao.create(tc);
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		COLProblemExamplesLoader me = new COLProblemExamplesLoader();
		me.init();
		me.load();
		System.out.println("Done");
	}

}
