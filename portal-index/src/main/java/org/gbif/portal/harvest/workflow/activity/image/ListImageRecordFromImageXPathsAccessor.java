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

package org.gbif.portal.harvest.workflow.activity.image;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.dom4j.XPath;
import org.gbif.portal.model.ImageRecord;
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
public class ListImageRecordFromImageXPathsAccessor implements MessageAccessor {
	/**
	 * Used to access the message
	 */
	protected XPath rootXPath;
	protected XPath urlXPath;
	protected XPath alternateUrlXPath;
	protected XPath typeXPath;
	protected XPath descriptionXPath;
	protected XPath rightsXPath;
	
	/**
	 * @throws MessageAccessException 
	 * @see org.gbif.portal.util.mhf.message.MessageAccessor#invoke(org.gbif.portal.util.mhf.message.Message)
	 */
	public List<ImageRecord> invoke(Message message) throws MessageAccessException {
		List<ImageRecord> images = null;

		try {
			List<Message> messages = message.getParts(getRootXPath());
			
			if (messages != null) {
				for (Message m : messages) {
					String url = null;
					String type = null;
					if (urlXPath != null) {
						url = StringUtils.trimToNull(m.getPartAsString(urlXPath));
						if (typeXPath != null) {
							type = StringUtils.trimToNull(m.getPartAsString(typeXPath));
						}
					}
					if (url == null && alternateUrlXPath != null) {
						url = StringUtils.trimToNull(m.getPartAsString(alternateUrlXPath));
						type = "PRODUCT";
					}
					if (url != null) {
						if (images == null) {
							images = new ArrayList<ImageRecord>();
						}
						ImageRecord rir = new ImageRecord();
						rir.setUrl(url);
						if (type != null) {
							rir.setRawImageType(type);
						}
						if (descriptionXPath != null) {
							rir.setDescription(StringUtils.trimToNull(m.getPartAsString(descriptionXPath)));
						}
						if (typeXPath != null) {
							rir.setRights(StringUtils.trimToNull(m.getPartAsString(rightsXPath)));
						}
						images.add(rir);
					}
				}
			}
		} catch (MessageParseException e) {
			throw new MessageAccessException("Error handling image messages", e);
		}
		
		return images;
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
	 * @return the rightsXPath
	 */
	public XPath getRightsXPath() {
		return rightsXPath;
	}

	/**
	 * @param rightsXPath the rightsXPath to set
	 */
	public void setRightsXPath(XPath rightsXPath) {
		this.rightsXPath = rightsXPath;
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

	/**
	 * @return the alternateUrlXPath
	 */
	public XPath getAlternateUrlXPath() {
		return alternateUrlXPath;
	}

	/**
	 * @param alternateUrlXPath the alternateUrlXPath to set
	 */
	public void setAlternateUrlXPath(XPath alternateUrlXPath) {
		this.alternateUrlXPath = alternateUrlXPath;
	}
}