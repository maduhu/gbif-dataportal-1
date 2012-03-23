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

package org.gbif.portal.harvest.workflow.activity.link;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.dom4j.XPath;
import org.gbif.portal.model.LinkRecord;
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
public class ListLinkRecordFromLinkXPathsAccessor implements MessageAccessor {
	/**
	 * Used to access the message
	 */
	protected XPath rootXPath;
	protected XPath urlXPath;
	protected XPath typeXPath;
	protected XPath descriptionXPath;
	
	/**
	 * @throws MessageAccessException 
	 * @see org.gbif.portal.util.mhf.message.MessageAccessor#invoke(org.gbif.portal.util.mhf.message.Message)
	 */
	public List<LinkRecord> invoke(Message message) throws MessageAccessException {
		List<LinkRecord> links = null;

		try {
			List<Message> messages = message.getParts(getRootXPath());
			
			if (messages != null) {
				for (Message m : messages) {
					String url = StringUtils.trimToNull(m.getPartAsString(urlXPath));
					if (url != null) {
						if (links == null) {
							links = new ArrayList<LinkRecord>();
						}
						LinkRecord rir = new LinkRecord();
						rir.setUrl(url);
						if (typeXPath != null) {
							rir.setRawLinkType(StringUtils.trimToNull(m.getPartAsString(typeXPath)));
						}
						if (descriptionXPath != null) {
							rir.setDescription(StringUtils.trimToNull(m.getPartAsString(descriptionXPath)));
						}
						links.add(rir);
					}
				}
			}
		} catch (MessageParseException e) {
			throw new MessageAccessException("Error handling link messages", e);
		}
		
		return links;
	}

	/**
	 * @return the descriptionXPath
	 */
	public XPath getDescriptionXPath() {
		return descriptionXPath;
	}

	/**
	 * @param descriptionXPath the descriptionXPath to set
	 */
	public void setDescriptionXPath(XPath descriptionXPath) {
		this.descriptionXPath = descriptionXPath;
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
	 * @return the typeXPath
	 */
	public XPath getTypeXPath() {
		return typeXPath;
	}

	/**
	 * @param typeXPath the typeXPath to set
	 */
	public void setTypeXPath(XPath typeXPath) {
		this.typeXPath = typeXPath;
	}

	/**
	 * @return the urlXPath
	 */
	public XPath getUrlXPath() {
		return urlXPath;
	}

	/**
	 * @param urlXPath the urlXPath to set
	 */
	public void setUrlXPath(XPath urlXPath) {
		this.urlXPath = urlXPath;
	}
}