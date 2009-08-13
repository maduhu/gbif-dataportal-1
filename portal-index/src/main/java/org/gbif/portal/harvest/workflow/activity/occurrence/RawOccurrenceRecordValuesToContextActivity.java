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
package org.gbif.portal.harvest.workflow.activity.occurrence;

import org.gbif.portal.model.RawOccurrenceRecord;
import org.gbif.portal.util.workflow.Activity;
import org.gbif.portal.util.workflow.BaseActivity;
import org.gbif.portal.util.workflow.ProcessContext;

/**
 * An activity that will extract the RawOccurrenceRecord values and put them in the context
 * if there is a context key defined
 * 
 * In particular, this would be used to pull out the taxonomy values
 * 
 * @author trobertson
 */
public class RawOccurrenceRecordValuesToContextActivity extends BaseActivity implements
		Activity {
	/**
	 * Context Keys
	 */
	protected String contextKeyOccurrenceId="occurrenceId";
	protected String contextKeyRawOccurrenceRecord;
	protected String contextKeyKingdom;
	protected String contextKeyPhylum;
	protected String contextKeyClass;
	protected String contextKeyOrder;
	protected String contextKeyFamily;
	protected String contextKeyGenus;
	protected String contextKeySpecies;
	protected String contextKeySubspecies;
	protected String contextKeyScientificName;
	protected String contextKeyAuthor;
	protected String contextKeyRank;
	protected String contextKeyDataResourceId;
	protected String contextKeyDataProviderId;
	
	/**
	 * @see org.gbif.portal.util.workflow.Activity#execute(org.gbif.portal.util.workflow.ProcessContext)
	 */
	@SuppressWarnings("unchecked")
	public ProcessContext execute(ProcessContext context) throws Exception {
		RawOccurrenceRecord ror = (RawOccurrenceRecord) context.get(getContextKeyRawOccurrenceRecord(), RawOccurrenceRecord.class, true);
		context.put(getContextKeyOccurrenceId(), ror.getId());
		if (contextKeyKingdom != null) {
			context.put(getContextKeyKingdom(), upperCaseFirst(ror.getKingdom()));
		}
		if (contextKeyPhylum != null) {
			context.put(getContextKeyPhylum(), upperCaseFirst(ror.getPhylum()));
		}
		if (contextKeyClass != null) {
			context.put(getContextKeyClass(), upperCaseFirst(ror.getKlass()));
		}
		if (contextKeyOrder != null) {
			context.put(getContextKeyOrder(), upperCaseFirst(ror.getOrder()));
		}
		if (contextKeyFamily!= null) {
			context.put(getContextKeyFamily(), upperCaseFirst(ror.getFamily()));
		}
		if (contextKeyGenus != null) {
			context.put(getContextKeyGenus(), upperCaseFirst(ror.getGenus()));
		}
		if (contextKeySpecies!= null) {
			context.put(getContextKeySpecies(), ror.getSpecies());
		}
		if (contextKeySubspecies!= null) {
			context.put(getContextKeySubspecies(), ror.getSubspecies());
		}
		if (contextKeyScientificName!= null) {
			context.put(getContextKeyScientificName(), upperCaseFirst(ror.getScientificName()));
		}
		if (contextKeyAuthor != null) {
			context.put(getContextKeyAuthor(), ror.getAuthor());
		}
		if (contextKeyRank != null) {
			context.put(getContextKeyRank(), ror.getRank());
		}
		if (contextKeyDataResourceId != null) {
			context.put(getContextKeyDataResourceId(), ror.getDataResourceId());
		}
		if (contextKeyDataProviderId != null) {
			context.put(getContextKeyDataProviderId(), ror.getDataProviderId());
		}
		
		return context;
	}

	/**
	 * Fix cases in which polynomials and higher taxa are all upper or lower case
	 * @param name
	 * @return
	 */
	private String upperCaseFirst(String name) {
		if (name!=null && (name.equals(name.toUpperCase()) || name.equals(name.toLowerCase()))) {
			name = name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();
		}
		return name;
	}

	/**
	 * @return Returns the contextKeyAuthor.
	 */
	public String getContextKeyAuthor() {
		return contextKeyAuthor;
	}

	/**
	 * @param contextKeyAuthor The contextKeyAuthor to set.
	 */
	public void setContextKeyAuthor(String contextKeyAuthor) {
		this.contextKeyAuthor = contextKeyAuthor;
	}

	/**
	 * @return Returns the contextKeyClass.
	 */
	public String getContextKeyClass() {
		return contextKeyClass;
	}

	/**
	 * @param contextKeyClass The contextKeyClass to set.
	 */
	public void setContextKeyClass(String contextKeyClass) {
		this.contextKeyClass = contextKeyClass;
	}

	/**
	 * @return Returns the contextKeyFamily.
	 */
	public String getContextKeyFamily() {
		return contextKeyFamily;
	}

	/**
	 * @param contextKeyFamily The contextKeyFamily to set.
	 */
	public void setContextKeyFamily(String contextKeyFamily) {
		this.contextKeyFamily = contextKeyFamily;
	}

	/**
	 * @return Returns the contextKeyGenus.
	 */
	public String getContextKeyGenus() {
		return contextKeyGenus;
	}

	/**
	 * @param contextKeyGenus The contextKeyGenus to set.
	 */
	public void setContextKeyGenus(String contextKeyGenus) {
		this.contextKeyGenus = contextKeyGenus;
	}

	/**
	 * @return Returns the contextKeyKingdom.
	 */
	public String getContextKeyKingdom() {
		return contextKeyKingdom;
	}

	/**
	 * @param contextKeyKingdom The contextKeyKingdom to set.
	 */
	public void setContextKeyKingdom(String contextKeyKingdom) {
		this.contextKeyKingdom = contextKeyKingdom;
	}

	/**
	 * @return Returns the contextKeyOrder.
	 */
	public String getContextKeyOrder() {
		return contextKeyOrder;
	}

	/**
	 * @param contextKeyOrder The contextKeyOrder to set.
	 */
	public void setContextKeyOrder(String contextKeyOrder) {
		this.contextKeyOrder = contextKeyOrder;
	}

	/**
	 * @return Returns the contextKeyPhylum.
	 */
	public String getContextKeyPhylum() {
		return contextKeyPhylum;
	}

	/**
	 * @param contextKeyPhylum The contextKeyPhylum to set.
	 */
	public void setContextKeyPhylum(String contextKeyPhylum) {
		this.contextKeyPhylum = contextKeyPhylum;
	}

	/**
	 * @return Returns the contextKeyRank.
	 */
	public String getContextKeyRank() {
		return contextKeyRank;
	}

	/**
	 * @param contextKeyRank The contextKeyRank to set.
	 */
	public void setContextKeyRank(String contextKeyRank) {
		this.contextKeyRank = contextKeyRank;
	}

	/**
	 * @return Returns the contextKeyRawOccurrenceRecord.
	 */
	public String getContextKeyRawOccurrenceRecord() {
		return contextKeyRawOccurrenceRecord;
	}

	/**
	 * @param contextKeyRawOccurrenceRecord The contextKeyRawOccurrenceRecord to set.
	 */
	public void setContextKeyRawOccurrenceRecord(
			String contextKeyRawOccurrenceRecord) {
		this.contextKeyRawOccurrenceRecord = contextKeyRawOccurrenceRecord;
	}

	/**
	 * @return the contextKeyScientificName
	 */
	public String getContextKeyScientificName() {
		return contextKeyScientificName;
	}

	/**
	 * @param contextKeyScientificName the contextKeyScientificName to set
	 */
	public void setContextKeyScientificName(String contextKeyScientificName) {
		this.contextKeyScientificName = contextKeyScientificName;
	}

	/**
	 * @return Returns the contextKeySpecies.
	 */
	public String getContextKeySpecies() {
		return contextKeySpecies;
	}

	/**
	 * @param contextKeySpecies The contextKeySpecies to set.
	 */
	public void setContextKeySpecies(String contextKeySpecies) {
		this.contextKeySpecies = contextKeySpecies;
	}

	/**
	 * @return the contextKeySubspecies
	 */
	public String getContextKeySubspecies() {
		return contextKeySubspecies;
	}

	/**
	 * @param contextKeySubspecies the contextKeySubspecies to set
	 */
	public void setContextKeySubspecies(String contextKeySubspecies) {
		this.contextKeySubspecies = contextKeySubspecies;
	}

	/**
	 * @return Returns the contextKeyDataProviderId.
	 */
	public String getContextKeyDataProviderId() {
		return contextKeyDataProviderId;
	}

	/**
	 * @param contextKeyDataProviderId The contextKeyDataProviderId to set.
	 */
	public void setContextKeyDataProviderId(String contextKeyDataProviderId) {
		this.contextKeyDataProviderId = contextKeyDataProviderId;
	}

	/**
	 * @return Returns the contextKeyDataResourceId.
	 */
	public String getContextKeyDataResourceId() {
		return contextKeyDataResourceId;
	}

	/**
	 * @param contextKeyDataResourceId The contextKeyDataResourceId to set.
	 */
	public void setContextKeyDataResourceId(String contextKeyDataResourceId) {
		this.contextKeyDataResourceId = contextKeyDataResourceId;
	}

	/**
	 * @return Returns the contextKeyOccurrenceId.
	 */
	public String getContextKeyOccurrenceId() {
		return contextKeyOccurrenceId;
	}

	/**
	 * @param contextKeyOccurrenceId The contextKeyOccurrenceId to set.
	 */
	public void setContextKeyOccurrenceId(String contextKeyOccurrenceId) {
		this.contextKeyOccurrenceId = contextKeyOccurrenceId;
	}
}
