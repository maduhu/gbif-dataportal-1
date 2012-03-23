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
package reports.cites;

/**
 * The record report on a name
 * @author trobertson
 */
public class NameReport {
	// params
	String name;
	int occurrenceCount = 0;
	int geospatialoccurrenceCount = 0;
	boolean isSynonym = false;
	String acceptedName;
	int expandedSynonomyOccurrenceCount = 0;
	
	// needs the name
	public NameReport(String name) {
		this.name = name;
	}
	
	// accessors follow
	public String getAcceptedName() {
		return acceptedName;
	}
	public void setAcceptedName(String acceptedName) {
		this.acceptedName = acceptedName;
	}
	public int getExpandedSynonomyOccurrenceCount() {
		return expandedSynonomyOccurrenceCount;
	}
	public void setExpandedSynonomyOccurrenceCount(
			int expandedSynonomyOccurrenceCount) {
		this.expandedSynonomyOccurrenceCount = expandedSynonomyOccurrenceCount;
	}
	public int getGeospatialoccurrenceCount() {
		return geospatialoccurrenceCount;
	}
	public void setGeospatialoccurrenceCount(int geospatialoccurrenceCount) {
		this.geospatialoccurrenceCount = geospatialoccurrenceCount;
	}
	public boolean isSynonym() {
		return isSynonym;
	}
	public void setSynonym(boolean isSynonym) {
		this.isSynonym = isSynonym;
	}
	public int getOccurrenceCount() {
		return occurrenceCount;
	}
	public void setOccurrenceCount(int occurrenceCount) {
		this.occurrenceCount = occurrenceCount;
	}
	public String getName() {
		return name;
	}
	
	
	
}
