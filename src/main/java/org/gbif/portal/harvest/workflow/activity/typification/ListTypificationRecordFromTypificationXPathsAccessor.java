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

package org.gbif.portal.harvest.workflow.activity.typification;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.dom4j.XPath;
import org.gbif.portal.model.TypificationRecord;
import org.gbif.portal.util.mhf.message.Message;
import org.gbif.portal.util.mhf.message.MessageAccessException;
import org.gbif.portal.util.mhf.message.MessageAccessor;
import org.gbif.portal.util.mhf.message.MessageParseException;


/**
 * Takes an XPath Expression and invokes the "getParts()" on the Message to return a 
 * List<Message> as the response.
 * 
 * @author Donald Hobern
 */
public class ListTypificationRecordFromTypificationXPathsAccessor implements MessageAccessor {
	/**
	 * Used to access the message
	 */
	protected XPath rootXPath;
	protected XPath scientificNameXPath;
	protected XPath publicationXPath;
	protected XPath typeStatusXPath;
	protected XPath notesXPath;
	
	/**
	 * @throws MessageAccessException 
	 * @see org.gbif.portal.util.mhf.message.MessageAccessor#invoke(org.gbif.portal.util.mhf.message.Message)
	 */
	public List<TypificationRecord> invoke(Message message) throws MessageAccessException {
		List<TypificationRecord> typifications = null;

		try {
			List<Message> messages = message.getParts(getRootXPath());
			
			if (messages != null) {
				for (Message m : messages) {
					String typeStatus = StringUtils.trimToNull(m.getPartAsString(typeStatusXPath));
					if (typeStatus != null) {
						if (typifications == null) {
							typifications = new ArrayList<TypificationRecord>();
						}
						TypificationRecord tr = new TypificationRecord();
						tr.setTypeStatus(typeStatus);
						if (scientificNameXPath != null) {
							tr.setScientificName(StringUtils.trimToNull(m.getPartAsString(scientificNameXPath)));
						}
						if (publicationXPath != null) {
							tr.setPublication(StringUtils.trimToNull(m.getPartAsString(publicationXPath)));
						}
						if (notesXPath != null) {
							tr.setNotes(StringUtils.trimToNull(m.getPartAsString(notesXPath)));
						}
						typifications.add(tr);
					}
				}
			}
		} catch (MessageParseException e) {
			throw new MessageAccessException("Error handling typification messages", e);
		}
		
		return typifications;
	}

	/**
	 * @return the notesXPath
	 */
	public XPath getNotesXPath() {
		return notesXPath;
	}

	/**
	 * @param notesXPath the notesXPath to set
	 */
	public void setNotesXPath(XPath notesXPath) {
		this.notesXPath = notesXPath;
	}

	/**
	 * @return the rootXPath
	 */
	public XPath getRootXPath() {
		return rootXPath;
	}

	/**
	 * @param rootXPath the rootXPath to set
	 */
	public void setRootXPath(XPath rootXPath) {
		this.rootXPath = rootXPath;
	}

	/**
	 * @return the scientificNameXPath
	 */
	public XPath getScientificNameXPath() {
		return scientificNameXPath;
	}

	/**
	 * @param scientificNameXPath the scientificNameXPath to set
	 */
	public void setScientificNameXPath(XPath scientificNameXPath) {
		this.scientificNameXPath = scientificNameXPath;
	}

	/**
	 * @return the typeStatusXPath
	 */
	public XPath getTypeStatusXPath() {
		return typeStatusXPath;
	}

	/**
	 * @param typeStatusXPath the typeStatusXPath to set
	 */
	public void setTypeStatusXPath(XPath typeStatusXPath) {
		this.typeStatusXPath = typeStatusXPath;
	}

	/**
	 * @return the publicationXPath
	 */
	public XPath getPublicationXPath() {
		return publicationXPath;
	}

	/**
	 * @param publicationXPath the publicationXPath to set
	 */
	public void setPublicationXPath(XPath publicationXPath) {
		this.publicationXPath = publicationXPath;
	}
}