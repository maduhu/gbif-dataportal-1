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
package org.gbif.portal.dao;

import org.gbif.portal.model.GbifLogMessage;

import javax.sql.DataSource;


/**
 * Interface for class to persist GbifLogMessages to database.
 * @author Donald Hobern
 */
public interface GbifLogMessageDAO {

	public long create(GbifLogMessage message);

	public long getMaxLogGroupId(Long portalInstanceId);
	
	public void setDataSource(DataSource dataSource);
	
	public int deleteExtractEventsByOccurrenceId(long occurrenceId);
	
	public int deleteGeospatialIssueEventsByOccurrenceIdAndEventId(long occurrenceId);

	public GbifLogMessage getByOccurrenceIdAndGeospatialIssue(long id,
			int extractGeospatialissueValue);
	
	public long update(GbifLogMessage gbifLogMessage);
}