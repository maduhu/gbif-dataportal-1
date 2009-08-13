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
package org.gbif.portal.web.controller.taxonomy.bean;

import java.util.List;

import org.gbif.portal.dto.taxonomy.BriefTaxonConceptDTO;

/**
 * Special tree node for browsing.
 * 
 * @author dmartin
 */
public class SpecialTreeNode extends BriefTaxonConceptDTO {

	private static final long serialVersionUID = 810574453575904346L;
	/** i18n key for the name to display */
	protected String displayName;
	/** the child nodes sitting under this node */
	protected List<BriefTaxonConceptDTO> children;
	/** indicates if child concepts should be exposed */
	protected boolean showChildren = false;
	/** Whether this node is a higher taxa placeholder - alters the behaviour of the link */
	protected boolean isHigherTaxa = false;
	/** the expand request parameter - used to expose children */
	protected String expandRequestParameter;

	/**
	 * @return the displayName
	 */
	public String getDisplayName() {
		return displayName;
	}

	/**
	 * @param displayName the displayName to set
	 */
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	/**
	 * @return the showChildren
	 */
	public boolean isShowChildren() {
		return showChildren;
	}

	/**
	 * @param showChildren the showChildren to set
	 */
	public void setShowChildren(boolean showChildren) {
		this.showChildren = showChildren;
	}

	/**
	 * @return the children
	 */
	public List<BriefTaxonConceptDTO> getChildren() {
		return children;
	}

	/**
	 * @param children the children to set
	 */
	public void setChildren(List<BriefTaxonConceptDTO> children) {
		this.children = children;
	}

	/**
	 * @return the expandRequestParameter
	 */
	public String getExpandRequestParameter() {
		return expandRequestParameter;
	}

	/**
	 * @param expandRequestParameter the expandRequestParameter to set
	 */
	public void setExpandRequestParameter(String expandRequestParameter) {
		this.expandRequestParameter = expandRequestParameter;
	}

	/**
	 * @return the isHigherTaxa
	 */
	public boolean isHigherTaxa() {
		return isHigherTaxa;
	}

	/**
	 * @param isHigherTaxa the isHigherTaxa to set
	 */
	public void setHigherTaxa(boolean isHigherTaxa) {
		this.isHigherTaxa = isHigherTaxa;
	}	
}