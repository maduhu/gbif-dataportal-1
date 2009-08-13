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
package org.gbif.portal.web.tag;

import org.gbif.portal.dto.taxonomy.BriefTaxonConceptDTO;
import org.gbif.portal.dto.util.TaxonRankType;
import org.gbif.portal.web.controller.taxonomy.bean.SpecialTreeNode;
import org.gbif.portal.web.util.TaxonConceptUtils;
/**
 * Taxonomy browse tag that supports ajax traversing. This class overrides the link 
 * construction of <code>SmallTaxonomyBrowserTag</code> to give javascript function calls.
 * 
 * @author dmartin
 */
public class AjaxTaxonomyBrowseTag extends SmallTaxonomyBrowserTag {

	private static final long serialVersionUID = -8359561270635768501L;
	
	/** The callback function to make before/after opening the tree	 */
	protected String callback;
	
	/** The div that this resides in */
	protected String containerDivId;
	
	/** The javascript function used to expand the tree */
	protected String openTaxonomyTreeJSFunction = "openTaxonomyTreeAtConcept";

	/** Only pass the concept values to the callback for major ranks */
	protected boolean majorRankCallbackOnly = true;
	
	/**
	 * @see org.gbif.portal.web.tag.SmallTaxonomyBrowserTag#getRootLink(java.lang.String)
	 */
	@Override
	protected String getRootLink(String contextPath) {
		super.setRootConceptsLinkMessageKey(rootConceptsLinkMessageKey);
		return getLink(contextPath, (BriefTaxonConceptDTO)null);
	}

	/**
	 * @see org.gbif.portal.web.tag.SmallTaxonomyBrowserTag#getLink(java.lang.String, org.gbif.portal.web.controller.taxonomy.bean.SpecialTreeNode)
	 */
	@Override
	protected String getLink(String contextPath, SpecialTreeNode specialTreeNode) {
		return getLink(contextPath, (BriefTaxonConceptDTO) specialTreeNode);
	}

	/**
	 * @see org.gbif.portal.web.tag.SmallTaxonomyBrowserTag#getLink(java.lang.String, org.gbif.portal.dto.taxonomy.BriefTaxonConceptDTO)
	 */
	@Override
	public String getLink(String contextPath, BriefTaxonConceptDTO concept) {
		StringBuffer sb = new StringBuffer();
		sb.append("javascript:");
		appendCallback("pre", concept, sb);
		appendOpenTreeAtNode(contextPath, concept, sb);
//		appendCallback("post", concept, sb);
		return sb.toString();
	}

	/**
	 * Appends a open tree node function call using the <code>openTaxonomyTreeJSFunction</code>
	 * field variable.
	 * 
	 * @param contextPath
	 * @param concept
	 * @param sb
	 */
	protected void appendOpenTreeAtNode(String contextPath, BriefTaxonConceptDTO concept, StringBuffer sb) {
		
		sb.append(openTaxonomyTreeJSFunction);
		sb.append("('");
		sb.append(contextPath);
		sb.append("/taxonomy/browse/");
		if(concept!=null){
			sb.append("taxon/");
			if(concept instanceof SpecialTreeNode){
				sb.append(concept.getParentConceptKey());
			} else {
				sb.append(concept.getKey());
			}
			sb.append('/');
		}
		sb.append("view/ajax");
		sb.append("?containerDivId=");
		sb.append(containerDivId);
		if(callback!=null){
			sb.append("&callback=");
			sb.append(callback);
		}
		if(concept instanceof SpecialTreeNode){
			SpecialTreeNode stn = (SpecialTreeNode) concept;
			sb.append('&');
			sb.append(stn.getExpandRequestParameter());
			sb.append("=true");
		}
		sb.append("','");
		sb.append(containerDivId);			
		sb.append("'");
		if (callback != null) {
			sb.append(",");
			sb.append(callback);
		}			
		sb.append(");");
	}

	/**
	 * Appends a callback call to url.
	 * 
	 * @param concept
	 * @param sb
	 */
	private void appendCallback(String methodName, BriefTaxonConceptDTO concept, StringBuffer sb) {
		if (callback != null) {
			if (concept == null || majorRankCallbackOnly && !TaxonRankType.isRecognisedMajorRank(concept.getRank())) {
				sb.append(callback);
				sb.append(".");
				sb.append(methodName);
				sb.append("(null,null,null,null,false);");
			} else {
				sb.append(callback);
				sb.append(".");
				sb.append(methodName);
				sb.append("(");
				sb.append(concept.getKey());
				sb.append(",'");
				sb.append(concept.getTaxonName());
				sb.append("','");
				sb.append(TaxonConceptUtils.getFormattedRankAndName(concept));
				sb.append("','");
				sb.append(concept.getRank());
				sb.append("',");			
				sb.append(TaxonRankType.isRecognisedMajorRank(concept.getRank()));
				sb.append(");");
			}
		}
	}

	/**
	 * @return Returns the rootConceptsLinkMessageKey.
	 */
	public String getRootConceptsLinkMessageKey() {
		return rootConceptsLinkMessageKey;
	}

	/**
	 * @return Returns the containerDivId.
	 */
	public String getContainerDivId() {
		return containerDivId;
	}

	/**
	 * @param containerDivId The containerDivId to set.
	 */
	public void setContainerDivId(String containerDivId) {
		this.containerDivId = containerDivId;
	}

	/**
	 * @return Returns the callback.
	 */
	public String getCallback() {
		return callback;
	}

	/**
	 * @param callback The callback to set.
	 */
	public void setCallback(String callback) {
		this.callback = callback;
	}

	/**
	 * @param majorRankCallbackOnly the majorRankCallbackOnly to set
	 */
	public void setMajorRankCallbackOnly(boolean majorRankCallbackOnly) {
		this.majorRankCallbackOnly = majorRankCallbackOnly;
	}

	/**
	 * @param openTaxonomyTreeJSFunction the openTaxonomyTreeJSFunction to set
	 */
	public void setOpenTaxonomyTreeJSFunction(String openTaxonomyTreeJSFunction) {
		this.openTaxonomyTreeJSFunction = openTaxonomyTreeJSFunction;
	}
}