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
package org.gbif.portal.harvest.workflow.activity.util;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.gbif.portal.dao.DataProviderDAO;
import org.gbif.portal.model.DataProvider;
import org.gbif.portal.util.workflow.BaseActivity;
import org.gbif.portal.util.workflow.ProcessContext;

/**
 * Takes a collection of Strings from the context, and concatinates them, putting them 
 * back into the context.
 * 
 * Control parameters can be passed in to (for example) ensure that there are no duplicate phrases.
 * 
 * @author trobertson
 */
public class StringConcatinationActivity extends BaseActivity {
	/**
	 * The keys to concatinate
	 */
	protected List<String> contextKeyTokensToConcatinate = new LinkedList<String>();
	
	/**
	 * The target key
	 */
	protected String contextKeyTarget;
	
	/**
	 * Should there be a full stop and space between tokens if there is not one?
	 * Default is true
	 */
	protected boolean addFullStopAndSpace = true;
	
	/**
	 * If there are duplicates, should they be added
	 * Default is true
	 */
	protected boolean addOnceOnly = true;
	
	/**
	 * Should the first letter be sentence cased?
	 * Default is true
	 */	
	protected boolean sentenceCaseFirstWord = true;
	
	/**
	 * @see org.gbif.portal.util.workflow.Activity#execute(org.gbif.portal.util.workflow.ProcessContext)
	 */
	@SuppressWarnings("unchecked")
	public ProcessContext execute(ProcessContext context) throws Exception {
		// to keep track of duplicates
		Set<String> valuesAdded = new HashSet<String>();
		StringBuffer result  = new StringBuffer(" ");
		
		for (String key : contextKeyTokensToConcatinate) {
			String token = StringUtils.trimToNull((String) context.get(key, String.class, false));
			if (token != null) {
				if (sentenceCaseFirstWord) {
					token = token.substring(0, 1).toUpperCase() + token.substring(1, token.length());
				}
				
				if ((addOnceOnly && !valuesAdded.contains(token))
						|| !addOnceOnly) {
					valuesAdded.add(token);
					result.append(token);
					if (addFullStopAndSpace) {
						if (!result.toString().trim().endsWith(".")) {
							result.append(". ");
						}
						if (!result.toString().endsWith(" ")) {
							result.append(" ");
						}
					}
				}
			}			
		}		
		context.put(contextKeyTarget, result.toString().trim());
		return context;
	}

	/**
	 * @return Returns the addFullStopAndSpace.
	 */
	public boolean isAddFullStopAndSpace() {
		return addFullStopAndSpace;
	}

	/**
	 * @param addFullStopAndSpace The addFullStopAndSpace to set.
	 */
	public void setAddFullStopAndSpace(boolean addFullStopAndSpace) {
		this.addFullStopAndSpace = addFullStopAndSpace;
	}

	/**
	 * @return Returns the addOnceOnly.
	 */
	public boolean isAddOnceOnly() {
		return addOnceOnly;
	}

	/**
	 * @param addOnceOnly The addOnceOnly to set.
	 */
	public void setAddOnceOnly(boolean addOnceOnly) {
		this.addOnceOnly = addOnceOnly;
	}

	/**
	 * @return Returns the contextKeyTarget.
	 */
	public String getContextKeyTarget() {
		return contextKeyTarget;
	}

	/**
	 * @param contextKeyTarget The contextKeyTarget to set.
	 */
	public void setContextKeyTarget(String contextKeyTarget) {
		this.contextKeyTarget = contextKeyTarget;
	}

	/**
	 * @return Returns the contextKeyTokensToConcatinate.
	 */
	public List<String> getContextKeyTokensToConcatinate() {
		return contextKeyTokensToConcatinate;
	}

	/**
	 * @param contextKeyTokensToConcatinate The contextKeyTokensToConcatinate to set.
	 */
	public void setContextKeyTokensToConcatinate(
			List<String> contextKeyTokensToConcatinate) {
		this.contextKeyTokensToConcatinate = contextKeyTokensToConcatinate;
	}

	/**
	 * @return Returns the sentenceCaseFirstWord.
	 */
	public boolean isSentenceCaseFirstWord() {
		return sentenceCaseFirstWord;
	}

	/**
	 * @param sentenceCaseFirstWord The sentenceCaseFirstWord to set.
	 */
	public void setSentenceCaseFirstWord(boolean sentenceCaseFirstWord) {
		this.sentenceCaseFirstWord = sentenceCaseFirstWord;
	}
	
}
