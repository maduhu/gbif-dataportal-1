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
package utils;

import java.util.LinkedList;
import java.util.List;

import org.gbif.portal.harvest.taxonomy.ScientificNameParser;
import org.gbif.portal.model.TaxonName;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author trobertson
 *
 */
public class NameParser {
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		if (args.length != 1 &&  args.length != 2) {
			System.out.println("Please supply a name in quotes... e.g. \n  utils.NameParser \"Puma concolor\" \"John Smith 1776\"\n  utils.NameParser \"Puma concolor\"");
		} else {
			String[] locations = {
					"classpath*:/**/applicationContext-*.xml",
					"classpath*:**/applicationContext-*.xml",
					"classpath*:org/gbif/portal/**/applicationContext-*.xml"
			};
			ApplicationContext context = new ClassPathXmlApplicationContext(locations);
			
			ScientificNameParser parser = (ScientificNameParser) context.getBean("scientificNameParser");
			String name = args[0];
			List<TaxonName> classification = new LinkedList<TaxonName>();
			
			if (args.length == 2) { 
				String author= args[1];
				System.out.println("Parsing name[" + name + "] author[" + author + "]");
				parser.parse(null, name, classification, author);			
			} else {
				System.out.println("Parsing name[" + name + "]");
				parser.parse(null, name, classification);							
			}
			
			for (TaxonName tn : classification) {
				System.out.println(" - " + tn.toFullString());
			}			
		}
		System.exit(1);
	}

}
