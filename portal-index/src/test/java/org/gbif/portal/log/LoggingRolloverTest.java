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
package org.gbif.portal.log;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.velocity.VelocityContext;
import org.gbif.portal.util.request.TemplateUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * To try and replicate the logging file rollover problem
 * (Classpath is lost when the log file rolls over - or is it?)
 * @author trobertson
 */
public class LoggingRolloverTest {
	private ApplicationContext context;
	protected static Log logger = LogFactory.getLog("test");
	
	private void init() {
		String[] locations = {
				"classpath*:/**/applicationContext-*.xml",
				"classpath*:**/applicationContext-*.xml",
				"classpath*:org/gbif/portal/**/applicationContext-*.xml"
		};
		context = new ClassPathXmlApplicationContext(locations);
	}
	
	protected void run(int i) throws Exception {
		TemplateUtils templateUtils = (TemplateUtils) context.getBean("templateUtils");
		logger.info("\n\n\n\n\n\n\n\n\n\n\n\n\n\n***********************************************************************************");
		logger.info("Starting: " + i);
		String request = templateUtils.getAndMerge("org/gbif/portal/util/propertystore/biocase_1_3/template/scan.vm", new VelocityContext());
		for (int j=0; j<10; j++) {
			logger.info(request);
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			LoggingRolloverTest me = new LoggingRolloverTest();
			me.init();
			for (int i=0; i<10000000; i++) {
				me.run(i);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e);
			
		} finally {		
			System.exit(1);
		}
	}

}
