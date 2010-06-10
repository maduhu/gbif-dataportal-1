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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.gbif.portal.dto.taxonomy.BriefTaxonConceptDTO;
import org.gbif.portal.dto.util.TaxonRankType;
import org.gbif.portal.service.OccurrenceManager;
import org.gbif.portal.service.ServiceException;
import org.gbif.portal.web.controller.taxonomy.bean.SpecialTreeNode;
import org.springframework.context.MessageSource;
import org.springframework.web.servlet.support.RequestContextUtils;

/**
 * Renders a list of Taxon Concepts into a HTML tree structure
 * using an a list of BriefTaxonConceptDTOs preordered in descending rank.
 *
 * @author Dave Martin
 */
public class SmallTaxonomyBrowserTag extends TagSupport {

	/** logger */
	protected static Log logger = LogFactory.getLog(SmallTaxonomyBrowserTag.class);		
	/** serial version uid */
	private static final long serialVersionUID = -1391459181969798828L;	
	/** The root concepts link message */
	protected String rootConceptsLinkMessageKey = "taxonomy.browser.all.highestranks";
	/** The Concepts to render */
	protected List<BriefTaxonConceptDTO> concepts;
	/** The Selected Concept - maybe null */
	protected BriefTaxonConceptDTO selectedConcept;
	/** The highest rank in this taxonomy */
	protected String highestRank;
	/** The root Url */
	protected String rootUrl="/species/browse/";
	/** The overview root Url */
	protected boolean addOverviewLink=true;
	/** The overview root Url */
	protected String overviewLinkText="taxonomy.browser.overviewlink";
	/** The overview root Url */
	protected String overviewLinkTitle="taxonomy.browser.overviewlink.title";
	/** The number of occurrences link message */
	protected String numberOfOccurrences="taxonomy.browser.numberofoccurrences";
	/** The no occurrences link message */
	protected String noOccurrences="taxonomy.browser.nooccurrences";
	/** The  occurrenceManager to calculate the number of taxon occurrences*/
	protected OccurrenceManager occurrenceManager;
	/** The overview root Url */
	protected String overviewRootUrl="/species/";
	/** The overview root Url */
	protected String conceptKeyPath="/taxon/";
	/** Mark concepts which are below the supplied taxonomic threshold */
	protected boolean markConceptBelowThreshold = false;
	/** The taxonomic priority threshold to use - determines the class on spans */
	protected int taxonPriorityThreshold=20;	
	/** Spring message source for i18n */
	protected MessageSource messageSource;
	/**
	 * @see javax.servlet.jsp.tagext.TagSupport#doEndTag()
	 */
	@Override
	public int doEndTag() throws JspException {
		Locale locale = RequestContextUtils.getLocale((HttpServletRequest) pageContext.getRequest());
		
		if(concepts==null || concepts.size()==0)
			return super.doEndTag();
		StringBuffer sb = new StringBuffer();
		HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
		String contextPath = request.getContextPath();
		sb.append("<div><p id=\"smtFirstLevel\"");
		if(selectedConcept==null){
			sb.append(" class=\"selected\">");
		} else {
			sb.append("><a href=\"");
			String rootLink = getRootLink(contextPath);
			sb.append(rootLink);
			sb.append("\">");
		}
		if(StringUtils.isEmpty(highestRank)){
			sb.append(messageSource.getMessage(rootConceptsLinkMessageKey, null, locale));
		} else {
			sb.append(messageSource.getMessage("taxonomy.browser.all."+highestRank, null, locale));
		}
		
		if(selectedConcept!=null)
			sb.append("</a>");
		
		sb.append("</p>");
		addConcepts(sb, pageContext, contextPath);
		sb.append("</div>");
		try{
			pageContext.getOut().write(sb.toString());
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}
		return super.doEndTag();
	}

	/**
	 * Recursive method for appending nodes to the tree.
	 * @param childNodes the child nodes to add to the tree
	 * @param sb the buffer to append HTML to
	 * @param pageContext
	 * @throws JspException
	 */
	void addConcepts(StringBuffer sb, PageContext pageContext, String contextPath)  throws JspException{
		
		//create a copy of the list that we can remove elements from
		List<BriefTaxonConceptDTO> conceptList = new ArrayList<BriefTaxonConceptDTO>();
		conceptList.addAll(concepts);
		addConceptsRecursively(conceptList, sb, contextPath);
	}	

	/**
	 * Recursive method for appending nodes to the tree.
	 * @param conceptList the child nodes to add to the tree
	 * @param sb the buffer to append HTML to
	 * @param pageContext
	 * @throws JspException
	 */
	void addConceptsRecursively(List<BriefTaxonConceptDTO> conceptList, StringBuffer sb, String contextPath)  throws JspException{

		//get the concept to add
		BriefTaxonConceptDTO currentConcept = conceptList.remove(0);
		//get next one to compare ranks
		BriefTaxonConceptDTO nextConcept =null;
		String currentParentKey = currentConcept.getParentConceptKey();
		if(!conceptList.isEmpty())
			nextConcept=conceptList.get(0);	

		if(logger.isDebugEnabled())
			logger.debug("Current concept:"+currentConcept+", next:"+nextConcept);

		//if they are the same rank then its a list class
		if( nextConcept!=null && (	
						//if both null, then they are both root concepts
						(nextConcept.getParentConceptKey()==null && currentParentKey==null) || 
						(nextConcept.getParentConceptKey().equals(currentParentKey))
					)
				){
			//add all elements to list div
			sb.append("<div class=\"list\">");
			while (currentConcept!=null && ( 
						//if both null, then they are both root concepts
						(currentConcept.getParentConceptKey()==null && currentParentKey==null) ||
						(currentConcept.getParentConceptKey().equals(currentParentKey))
					)
				){
				//render the concept
				addElement(currentConcept, sb,  contextPath);
				//add the concept
				if(conceptList.size()>0)
					currentConcept = conceptList.remove(0);
				else
					currentConcept =null;
			}
			if(conceptList.size()>0)
				addConceptsRecursively(conceptList, sb, contextPath);
			sb.append("</div>");
		} else {
			//if they arent the same rank then its a recursive call
			sb.append("<div class=\"ancestor\">");
			addElement(currentConcept, sb,  contextPath);
			if(conceptList.size()>0)
				addConceptsRecursively(conceptList, sb, contextPath);
			sb.append("</div>");
		}
	}	

	/**
	 * Render a special tree node.
	 * @param specialTreeNode
	 * @param sb
	 * @param contextPath
	 */
	protected void addElement(SpecialTreeNode specialTreeNode, StringBuffer sb, String contextPath) {
		
		Locale locale = RequestContextUtils.getLocale((HttpServletRequest)pageContext.getRequest());
		
		sb.append("<p class=\"specialConcept\">");
		String link = getLink(contextPath, specialTreeNode);
		if(link!=null){
			sb.append("<a href=\"");
			sb.append(link);
			sb.append("\" class=\"treeNodeLink\">");
		}
		sb.append("<span class=\"conceptLink\">&nbsp;</span><span id=\"");
		sb.append(specialTreeNode.getExpandRequestParameter());
		sb.append("\">");
		sb.append(messageSource.getMessage(specialTreeNode.getDisplayName(), null, locale));
		sb.append("</span>");
		if(link!=null)
			sb.append("</a>");
		if(specialTreeNode.isShowChildren()){
			sb.append("<div class=\"list\">");
			for(BriefTaxonConceptDTO child: specialTreeNode.getChildren())
				addElement(child, sb, contextPath);
			sb.append("</div>");
		}
		sb.append("</p>");
	}
	
	/**
	 * Render an element in the tree.
	 * @param conceptList
	 * @param sb
	 * @param pageContext
	 * @param contextPath
	 */
	protected void addElement(BriefTaxonConceptDTO concept, StringBuffer sb, String contextPath) {
		
		Locale locale = RequestContextUtils.getLocale((HttpServletRequest)pageContext.getRequest());
		
		if(concept instanceof SpecialTreeNode){
			addElement((SpecialTreeNode) concept,  sb,  contextPath);
			return;
		}
		
		sb.append("<p");
		if(selectedConcept!=null && selectedConcept.getKey().equals(concept.getKey())){
			if(markConceptBelowThreshold && concept.getTaxonomicPriority()>taxonPriorityThreshold)
				sb.append(" class=\"selectedTentative\"");
			else
				sb.append(" class=\"selected\"");
		}
		sb.append(">");
		
		//add link with image
		if(selectedConcept==null || !selectedConcept.getKey().equals(concept.getKey())){
			sb.append("<a href=\"");
			sb.append(getLink(contextPath, concept));
			sb.append("\" class=\"");
			if(markConceptBelowThreshold && concept.getTaxonomicPriority()>taxonPriorityThreshold)
				sb.append("treeNodeLinkTentative");
			else
				sb.append("treeNodeLink");
			sb.append("\" title=\"");
			sb.append(messageSource.getMessage("taxonomy.browser.all.expand.node", null, locale));
			sb.append(" '");
			sb.append(concept.getTaxonName());
			sb.append("'\">");
			sb.append("<span class=\"conceptLink\">&nbsp;&nbsp;</span>");
		} else {
			sb.append("<span class=\"selectedConceptLink\">&nbsp;</span>");			
		}
		sb.append("<span class=\"rank\">");
		//internationalization
//		sb.append(StringUtils.capitalize(concept.getRank()));
		sb.append(messageSource.getMessage("taxonrank."+concept.getRank(), null, StringUtils.capitalize(concept.getRank()), locale));
		sb.append(": ");
		sb.append("</span><span class=\"");
		TaxonRankType taxonRankType = TaxonRankType.getRank(concept.getRank())	;
		
		if(taxonRankType!=null && taxonRankType.getValue()>=TaxonRankType.GENUS.getValue()){
			sb.append("generaTaxon");
		} else {
			sb.append("taxon");
		}
		sb.append("\">");
		sb.append(concept.getTaxonName());
		sb.append("</span>");
		if(selectedConcept==null || !selectedConcept.getKey().equals(concept.getKey()))
			sb.append("</a>");
		
		if(addOverviewLink){
			//if it isnt a nub concept direct towards partnerConceptKey - which will be the nub concept for this concept
			//only link if tied to nub
			String conceptKey = null;
			if(concept.getIsNubConcept())
				conceptKey = concept.getKey();
			else
				conceptKey = concept.getPartnerConceptKey();
			//only supporting overview links for major ranks
			if(conceptKey!=null && (TaxonRankType.isRecognisedMajorRank(concept.getRank()) || TaxonRankType.SUB_SPECIES_STR.equals(concept.getRank())) ){
				sb.append("<span class=\"");
				sb.append("overview");			
				sb.append("\"><a href=\"");
				sb.append(contextPath);
				sb.append(overviewRootUrl);
				//if it isnt a nub concept direct towards partnerConceptKey - which will be the nub concept for this concept
				sb.append(conceptKey);
				sb.append("\" class=\"");
				if(markConceptBelowThreshold && concept.getTaxonomicPriority()>taxonPriorityThreshold)
					sb.append("taxonOverviewLinkTentative");
				else
					sb.append("taxonOverviewLink");
				sb.append("\" title=\"");
				sb.append(messageSource.getMessage(overviewLinkTitle, null, locale));
				sb.append(concept.getTaxonName());
				sb.append("\"><span class=\"overviewLink\">&nbsp;&nbsp;</span>");
				//sb.append(TagUtils.getMessage(overviewLinkText, "Overview", pageContext));
				sb.append(messageSource.getMessage(overviewLinkText, null, locale));
				sb.append("</a></span>");
				//Calculating number of occurrences for species and subspecies
				if(concept.getRank().equals(TaxonRankType.SPECIES_STR)||concept.getRank().equals(TaxonRankType.SUB_SPECIES_STR)){ 
					int occurrencesCount=0;
					try {
						occurrencesCount = occurrenceManager.countOccurrenceRecords(null, null, null, concept.getKey(), null, null, null, null, null, null, null, null, false);
					} catch (ServiceException e) {
						logger.error(e.getMessage(), e);
					}
					Object[] messageArgs={occurrencesCount};
					sb.append("<span class=\"occurrenceCount\">");
					sb.append((occurrencesCount == 0 ? messageSource.getMessage(noOccurrences, null, locale) : messageSource.getMessage(numberOfOccurrences, messageArgs, locale)));
					sb.append("</span>");
				}
			}
		}
		sb.append("</p>");
	}

	/**
	 * Constucts the URL for the root concepts.
	 * 
	 * @param contextPath
	 * @return
	 */
	protected String getRootLink( String contextPath) {
		StringBuffer sb = new StringBuffer();
		sb.append(contextPath);
		sb.append(rootUrl);
		sb.append("/");
		return sb.toString();
	}	

	
	/**
	 * Constructs the URL for this concept.
	 * 
	 * @param contextPath
	 * @param concept
	 * @return
	 */
	protected String getLink(String contextPath, SpecialTreeNode specialTreeNode){
		if(specialTreeNode==null)
			return null;
		if(specialTreeNode.isHigherTaxa()){
			if(specialTreeNode.getParentConceptKey()!=null)
				return getLink(contextPath, specialTreeNode.getParentConceptKey())+"?"+specialTreeNode.getExpandRequestParameter()+"=true#"+specialTreeNode.getExpandRequestParameter();
		} else if(specialTreeNode.isShowChildren()){
			return "#"+specialTreeNode.getExpandRequestParameter();
		} else if(selectedConcept!=null){
			return getLink(contextPath, selectedConcept)+"?"+specialTreeNode.getExpandRequestParameter()+"=true#"+specialTreeNode.getExpandRequestParameter();
		} else {
			return getLink(contextPath, specialTreeNode.getParentConceptKey())+"?"+specialTreeNode.getExpandRequestParameter()+"=true#"+specialTreeNode.getExpandRequestParameter();
		}
		return null;
	}	
	
	/**
	 * Constructs the URL for this concept.
	 * 
	 * @param contextPath
	 * @param concept
	 * @return
	 */
	protected String getLink(String contextPath, BriefTaxonConceptDTO concept){
		if(concept==null)
			return "";
		return getLink(contextPath, concept.getKey());
	}
	
	/**
	 * Constructs the URL for this concept.
	 * 
	 * @param contextPath
	 * @param concept
	 * @return
	 */
	protected String getLink(String contextPath, String conceptKey){
		StringBuffer sb = new StringBuffer();
		sb.append(contextPath);
		sb.append(rootUrl);
		if(conceptKey!=null){
			sb.append(conceptKeyPath);
			sb.append(conceptKey);
			sb.append('/');
		}
		return sb.toString();
	}	
	
	
	/**
	 * @param concepts the concepts to set
	 */
	public void setConcepts(List<BriefTaxonConceptDTO> concepts) {
		this.concepts = concepts;
	}

	/**
	 * @param selectedConcept the selectedConcept to set
	 */
	public void setSelectedConcept(BriefTaxonConceptDTO selectedConcept) {
		this.selectedConcept = selectedConcept;
	}

	/**
	 * @param rootUrl the rootUrl to set
	 */
	public void setRootUrl(String rootUrl) {
		this.rootUrl = rootUrl;
	}

	/**
	 * @param highestRank the highestRank to set
	 */
	public void setHighestRank(String highestRank) {
		this.highestRank = highestRank;
	}

	/**
	 * @param rootConceptsLinkMessageKey the rootConceptsLinkMessageKey to set
	 */
	public void setRootConceptsLinkMessageKey(String rootConceptsLinkMessageKey) {
		this.rootConceptsLinkMessageKey = rootConceptsLinkMessageKey;
	}

	/**
	 * @param overviewRootUrl the overviewRootUrl to set
	 */
	public void setOverviewRootUrl(String overviewRootUrl) {
		this.overviewRootUrl = overviewRootUrl;
	}

	/**
	 * @param addOverviewLink the addOverviewLink to set
	 */
	public void setAddOverviewLink(boolean addOverviewLink) {
		this.addOverviewLink = addOverviewLink;
	}

	/**
	 * @param overviewLinkText the overviewLinkText to set
	 */
	public void setOverviewLinkText(String overviewLinkText) {
		this.overviewLinkText = overviewLinkText;
	}

	/**
	 * @param overviewLinkTitle the overviewLinkTitle to set
	 */
	public void setOverviewLinkTitle(String overviewLinkTitle) {
		this.overviewLinkTitle = overviewLinkTitle;
	}

	/**
	 * @param taxonomicPriorityThreshold the taxonomicPriorityThreshold to set
	 */
	public void setTaxonPriorityThreshold(int taxonPriorityThreshold) {
		this.taxonPriorityThreshold = taxonPriorityThreshold;
	}

	/**
	 * @return the markConceptBelowThreshold
	 */
	public boolean isMarkConceptBelowThreshold() {
		return markConceptBelowThreshold;
	}

	/**
	 * @param markConceptBelowThreshold the markConceptBelowThreshold to set
	 */
	public void setMarkConceptBelowThreshold(boolean markConceptBelowThreshold) {
		this.markConceptBelowThreshold = markConceptBelowThreshold;
	}

	/**
	 * @param messageSource the messageSource to set
	 */
	public void setMessageSource(MessageSource messageSource) {
		this.messageSource = messageSource;
	}
	
	/**
	 * @param occurrenceManager the occurrenceManager to set
	 */
	public void setOccurrenceManager(OccurrenceManager occurrenceManager) {
		this.occurrenceManager = occurrenceManager;
	}
}