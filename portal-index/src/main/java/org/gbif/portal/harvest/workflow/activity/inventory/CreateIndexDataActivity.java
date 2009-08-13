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
package org.gbif.portal.harvest.workflow.activity.inventory;

import java.util.List;

import org.gbif.portal.dao.IndexDataDAO;
import org.gbif.portal.model.IndexData;
import org.gbif.portal.util.mhf.message.Message;
import org.gbif.portal.util.mhf.message.MessageUtils;
import org.gbif.portal.util.propertystore.PropertyNotFoundException;
import org.gbif.portal.util.propertystore.PropertyStore;
import org.gbif.portal.util.workflow.Activity;
import org.gbif.portal.util.workflow.BaseActivity;
import org.gbif.portal.util.workflow.ProcessContext;

/**
 * An activity that will create IndexData definitions for the inventory message
 * in the context 
 * 
 * @author trobertson
 */
public class CreateIndexDataActivity extends BaseActivity implements
		Activity {	
	/**
	 * Context Keys
	 */
	protected String contextKeyResourceAccessPointId;
	protected String contextKeyInventoryMessage;
	protected String contextKeyLastConcept;
	protected String contextKeyProcessedCount;
	
	/**
	 * DAOs
	 */
	protected IndexDataDAO indexDataDAO;
	
	/**
	 * Property store keys to pull out the concept and the count (optional)
	 */
	protected String psKeyConceptMessage;
	protected String psKeyConcept;
	protected String psKeyCount;
	
	/**
	 * Used if there is no count, then this is the default count (defaults to 100)
	 */
	protected int countIfNotProvided = 100;
	
	/**
	 * The target count we are looking get per request
	 */
	protected int countPerRange = 1000;
	
	/**
	 * The concept type that is used
	 */
	protected int conceptType = IndexData.TYPE_SCIENTIFIC_NAME;
	
	/**
	 * Utils
	 */
	protected MessageUtils messageUtils;
	
	/**
	 * @see org.gbif.portal.util.workflow.Activity#execute(org.gbif.portal.util.workflow.ProcessContext)
	 */
	@SuppressWarnings("unchecked")
	public ProcessContext execute(ProcessContext context) throws Exception {
		Message inventoryMessage = (Message) context.get(getContextKeyInventoryMessage(), Message.class, true);
		List<String> namespaces = (List<String>) context.get(getContextKeyPsNamespaces(), List.class, true);		
		List<Message> conceptMessages = messageUtils.extractSubMessageList(inventoryMessage, namespaces, getPsKeyConceptMessage(), true);
		
		int totalCount = conceptMessages.size();
		
		int runningCount = 0;
		String lowerConcept = null;
		String upperConcept = null;
		
		// loop over building up the ranges as indexData's 
		for (Message conceptMessage : conceptMessages) {
			String concept = messageUtils.extractConceptAsString(conceptMessage, namespaces, getPsKeyConcept(), true);
			if (lowerConcept == null) {
				lowerConcept = concept;				
			} 			
			
			// get the count or use the default for the current loop 
			int count = countIfNotProvided;
			try {
				count = Integer.parseInt(messageUtils.extractConceptAsString(conceptMessage, namespaces, getPsKeyCount(), true));
			} catch (PropertyNotFoundException e) {
				// swallow - not mapped
			} catch (Exception e) {
				logger.warn("Attempted to get a count as it is mapped but failed [using default count]", e);
			} 
			
			// the running count + the count is greater than that of the limit, then create the range			
			if ((count + runningCount) > countPerRange) {
				
				// it may be that the first record has more than that required, but if so we need to use it
				if (upperConcept == null) {
					upperConcept = lowerConcept;
				}
				
				// skip any that are both null - it would be a range that was effectively AAA-zzz (everything) 
				if (!((lowerConcept == null || lowerConcept.length()==0)
						&& (upperConcept == null || upperConcept.length()==0))) {
					indexDataDAO.create(
							new IndexData(
									((Long) context.get(getContextKeyResourceAccessPointId(), Long.class, true)).longValue(),
									conceptType,
									validateRange(lowerConcept, "A", false),
									validateRange(upperConcept, "z", true)));
				}
				
				// reset for the next loop
				lowerConcept = concept;
				upperConcept = null;
				runningCount = 0;
				
			} else {
				// set it for later
				upperConcept = concept;
				runningCount += count;
			}			
		}
		if (conceptMessages.size() >= 1 ) {
			if (lowerConcept != null) {
				if (upperConcept == null) {
					upperConcept = lowerConcept;
				}
				indexDataDAO.create(
						new IndexData(
								((Long) context.get(getContextKeyResourceAccessPointId(), Long.class, true)).longValue(),
								conceptType,
								validateRange(lowerConcept, "A", false),
								validateRange(upperConcept, "z", true)));
			}
		}
		
		context.put(getContextKeyLastConcept(), upperConcept);
		if (getContextKeyProcessedCount() != null) {
			Integer currentValue = (Integer) context.get(getContextKeyProcessedCount(), Integer.class, false);
			if (currentValue == null) {
				context.put(getContextKeyProcessedCount(), totalCount);
			} else {
				int runningTotal = currentValue + totalCount;
				context.put(getContextKeyProcessedCount(), runningTotal);
			}
		}
		return context;
	}
	
	/**
	 * Validates the range for usage
	 * Checks for &, " " and ranges less than 
	 * @param range
	 * @param replaceValue
	 * @return
	 */
	public String validateRange(String range, String replaceValue, boolean atEnd) {
		// if it looks ok, return
		if ( range != null
				&& range.length()>=3
				&& !range.contains("&")
				&& !range.contains("'")
				&& !range.matches(" +")) {
			return range;
			
		} else if (range == null) {
			return replaceValue + replaceValue + replaceValue;
			
		} else {
			if (range.matches(" +")) {
				return replaceValue + replaceValue + replaceValue;
			}
			
			// start by removing everything after &
			if (range.contains("&")) {
				if (!atEnd)
					range = range.substring(0,range.indexOf("&"));
				else 
					range = range.substring(0,range.indexOf("&")) + replaceValue;
			}
			// the by removing everything after &
			if (range.contains("'")) {
				if (!atEnd)
					range = range.substring(0,range.indexOf("'"));
				else 
					range = range.substring(0,range.indexOf("'")) + replaceValue;
			}
			// pad if necessary
			if (range==null || range.length()==0) {
				return replaceValue + replaceValue + replaceValue;
			}
			if (range.length()==1) {
				return range + replaceValue + replaceValue;
			}
			if (range.length()==2) {
				return range + replaceValue;
			}
			return range;
		}
	}

	/**
	 * @return Returns the contextKeyInventoryMessage.
	 */
	public String getContextKeyInventoryMessage() {
		return contextKeyInventoryMessage;
	}

	/**
	 * @param contextKeyInventoryMessage The contextKeyInventoryMessage to set.
	 */
	public void setContextKeyInventoryMessage(String contextKeyInventoryMessage) {
		this.contextKeyInventoryMessage = contextKeyInventoryMessage;
	}

	/**
	 * @return Returns the contextKeyResourceAccessPointId.
	 */
	public String getContextKeyResourceAccessPointId() {
		return contextKeyResourceAccessPointId;
	}

	/**
	 * @param contextKeyResourceAccessPointId The contextKeyResourceAccessPointId to set.
	 */
	public void setContextKeyResourceAccessPointId(
			String contextKeyResourceAccessPointId) {
		this.contextKeyResourceAccessPointId = contextKeyResourceAccessPointId;
	}

	/**
	 * @return Returns the indexDataDAO.
	 */
	public IndexDataDAO getIndexDataDAO() {
		return indexDataDAO;
	}

	/**
	 * @param indexDataDAO The indexDataDAO to set.
	 */
	public void setIndexDataDAO(IndexDataDAO indexDataDAO) {
		this.indexDataDAO = indexDataDAO;
	}

	/**
	 * @return Returns the messageUtils.
	 */
	public MessageUtils getMessageUtils() {
		return messageUtils;
	}

	/**
	 * @param messageUtils The messageUtils to set.
	 */
	public void setMessageUtils(MessageUtils messageUtils) {
		this.messageUtils = messageUtils;
	}

	/**
	 * @return Returns the propertyStore.
	 */
	public PropertyStore getPropertyStore() {
		return propertyStore;
	}

	/**
	 * @param propertyStore The propertyStore to set.
	 */
	public void setPropertyStore(PropertyStore propertyStore) {
		this.propertyStore = propertyStore;
	}

	/**
	 * @return Returns the psKeyConcept.
	 */
	public String getPsKeyConcept() {
		return psKeyConcept;
	}

	/**
	 * @param psKeyConcept The psKeyConcept to set.
	 */
	public void setPsKeyConcept(String psKeyConcept) {
		this.psKeyConcept = psKeyConcept;
	}

	/**
	 * @return Returns the psKeyCount.
	 */
	public String getPsKeyCount() {
		return psKeyCount;
	}

	/**
	 * @param psKeyCount The psKeyCount to set.
	 */
	public void setPsKeyCount(String psKeyCount) {
		this.psKeyCount = psKeyCount;
	}

	/**
	 * @return Returns the countIfNotProvided.
	 */
	public int getCountIfNotProvided() {
		return countIfNotProvided;
	}

	/**
	 * @param countIfNotProvided The countIfNotProvided to set.
	 */
	public void setCountIfNotProvided(int countIfNotProvided) {
		this.countIfNotProvided = countIfNotProvided;
	}

	/**
	 * @return Returns the countPerRange.
	 */
	public int getCountPerRange() {
		return countPerRange;
	}

	/**
	 * @param countPerRange The countPerRange to set.
	 */
	public void setCountPerRange(int countPerRange) {
		this.countPerRange = countPerRange;
	}

	/**
	 * @return Returns the psKeyConceptMessage.
	 */
	public String getPsKeyConceptMessage() {
		return psKeyConceptMessage;
	}

	/**
	 * @param psKeyConceptMessage The psKeyConceptMessage to set.
	 */
	public void setPsKeyConceptMessage(String psKeyConceptMessage) {
		this.psKeyConceptMessage = psKeyConceptMessage;
	}

	/**
	 * @return Returns the conceptType.
	 */
	public int getConceptType() {
		return conceptType;
	}

	/**
	 * @param conceptType The conceptType to set.
	 */
	public void setConceptType(int conceptType) {
		this.conceptType = conceptType;
	}

	/**
	 * @return Returns the contextKeyLastConcept.
	 */
	public String getContextKeyLastConcept() {
		return contextKeyLastConcept;
	}

	/**
	 * @param contextKeyLastConcept The contextKeyLastConcept to set.
	 */
	public void setContextKeyLastConcept(String contextKeyLastConcept) {
		this.contextKeyLastConcept = contextKeyLastConcept;
	}

	/**
	 * @return Returns the contextKeyProcessedCount.
	 */
	public String getContextKeyProcessedCount() {
		return contextKeyProcessedCount;
	}

	/**
	 * @param contextKeyProcessedCount The contextKeyProcessedCount to set.
	 */
	public void setContextKeyProcessedCount(String contextKeyProcessedCount) {
		this.contextKeyProcessedCount = contextKeyProcessedCount;
	}
}
