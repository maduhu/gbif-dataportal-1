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
package org.gbif.portal.util.log;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.gbif.portal.util.enumeration.Enumeration;

/**
 * Enumerated type for log severity
 * @author Donald Hobern
 */
public class LogSeverity extends Enumeration implements Serializable {

	/**
	 * Generated
	 */
	private static final long serialVersionUID = 2751783681532800807L;
	
	/**
	 * Maps to support get methods
	 */
	private static Map<String, Enumeration> nameMap = new HashMap<String, Enumeration>();
	private static Map<Integer, Enumeration> valueMap = new HashMap<Integer, Enumeration>();

	/**
	 * Log severity values
	 */
	public static final LogSeverity TRACE = new LogSeverity("TRACE", 1);
	public static final LogSeverity DEBUG = new LogSeverity("DEBUG", 2);
	public static final LogSeverity INFO = new LogSeverity("INFO ", 3);
	public static final LogSeverity WARN = new LogSeverity("WARN ", 4);
	public static final LogSeverity ERROR = new LogSeverity("ERROR", 5);
	public static final LogSeverity FATAL = new LogSeverity("FATAL", 6);
	
	protected LogSeverity(String name, Integer value) { 
		super(nameMap, valueMap, name, value); 
	}
	
	public static LogSeverity get(String name) {
		return (LogSeverity) nameMap.get(name);
	}

	public static LogSeverity get(Integer value) {
		return (LogSeverity) valueMap.get(value);
	}
}