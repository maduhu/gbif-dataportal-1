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
package org.gbif.portal.server;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Basically sits forever running jobs
 * @author trobertson
 */
public class HarvestingServer {
	/**
	 * Logger
	 */
	protected Log logger = LogFactory.getLog(getClass());
	
	/**
	 * The spring context
	 */
	private ApplicationContext context;
	
	/**
	 * Sets up
	 */
	private void init() {
		String[] locations = {"classpath*:/org/gbif/**/applicationContext-*.xml"};		
		context = new ClassPathXmlApplicationContext(locations);
		Scheduler scheduler = (Scheduler) context.getBean("scheduleFactoryBean", Scheduler.class);
		try {
			scheduler.start();
		} catch (SchedulerException e) {
			logger.error("Launch failed", e);
		}
	}

	/**
	 * @param args Ignored
	 */
	public static void main(String[] args) {
		HarvestingServer me = new HarvestingServer();
		me.init();		
		// will not shut down, due to there being a Quartz scheduler started
	}
}
