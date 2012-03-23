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

import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.MDC;
import org.apache.log4j.NDC;

/**
 * @author trobertson
 *
 */
public class NDCTest {
	static Log logger = LogFactory.getLog(NDCTest.class);
	
	
	static class LoggerLoop implements Runnable {
		private String name;
		public LoggerLoop(String name) {
			this.name = name;
		}
		@SuppressWarnings("unchecked")
		public void run() {
			MDC.put("name", name);
			for (int i=0; i<10; i++) {
				
				logger.info(name + ": " + i);
				try {
					Thread.sleep(100);
				} catch (Exception e) {
				}
			}
			
			Set<String> keys =MDC.getContext().keySet();
			for (String key : keys) {
				MDC.remove(key);
			}			
		}		
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		for (int i=0; i<10; i++) {
			Thread t = new Thread(new LoggerLoop("T" + i));
			t.start();
		}
	}
}
