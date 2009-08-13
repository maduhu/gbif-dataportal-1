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

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.gbif.portal.dto.resources.DataProviderDTO;
import org.gbif.portal.dto.resources.DataResourceDTO;
import org.gbif.portal.dto.taxonomy.BriefTaxonConceptDTO;

/**
 * Simple tag that constructs a link to a taxonomy tree with the correct concept selected.
 *
 * @author Dave Martin
 */
public class TaxonomyTreeLinkTag extends TagSupport {

	private static final long serialVersionUID = -1761546172607242170L;
	protected static Log logger = LogFactory.getLog(TaxonomyTreeLinkTag.class);		

	/**the data resource, nullable**/
	protected DataResourceDTO dataResource;
	/**the data provider **/
	protected DataProviderDTO dataProvider;
	/**the concept **/
	protected BriefTaxonConceptDTO selectedConcept;
	/**the css class for the link**/
	protected String cssClass; 
	
	/**
	 * Writes out a link to a taxonomy tree, working out if the provider is providing
	 * a shared taxonomy.
	 * 
	 * @see javax.servlet.jsp.tagext.TagSupport#doStartTag()
	 */
	@Override
	public int doStartTag() throws JspException {

		//construct link
		ServletRequest req = pageContext.getRequest();
		if (!(req instanceof HttpServletRequest)){
			throw new JspException("PageContext contains a request object not of type HttpServletRequest "+req.getClass().getName());
		}
		HttpServletRequest hReq = (HttpServletRequest) req;	

		//construct the url
		StringBuffer sb = new StringBuffer();
		sb.append(" <a");
		if(StringUtils.isNotEmpty(cssClass)){
			sb.append(" class='");
			sb.append(cssClass);			
			sb.append("'");
		}

		sb.append(" href=\"");
		sb.append(hReq.getContextPath());
		sb.append("/species/browse/");
		if(dataResource!=null && !dataResource.isSharedTaxonomy()){
			sb.append("resource/");
			sb.append(dataResource.getKey());
		} else {
			sb.append("provider/");
			sb.append(dataProvider.getKey());			
		}
		
		if(selectedConcept!=null){
			sb.append("/taxon/");
			sb.append(selectedConcept.getKey());			
		}
		sb.append("\">"); 
		
		try{
			pageContext.getOut().write(sb.toString());
		} catch (IOException e){
			throw new JspException(e);
		}
		return EVAL_BODY_INCLUDE;
	}
	
	/**
	 * @see javax.servlet.jsp.tagext.TagSupport#doEndTag()
	 */
	@Override
	public int doEndTag() throws JspException {
		try{
			pageContext.getOut().write("</a>");
		} catch (IOException e){
			logger.error(e.getMessage(), e);		
		}
		return EVAL_PAGE;
	}

	/**
	 * @param cssClass the cssClass to set
	 */
	public void setCssClass(String cssClass) {
		this.cssClass = cssClass;
	}

	/**
	 * @param concept the concept to set
	 */
	public void setSelectedConcept(BriefTaxonConceptDTO concept) {
		this.selectedConcept = concept;
	}

	/**
	 * @param dataProvider the dataProvider to set
	 */
	public void setDataProvider(DataProviderDTO dataProvider) {
		this.dataProvider = dataProvider;
	}

	/**
	 * @param dataResource the dataResource to set
	 */
	public void setDataResource(DataResourceDTO dataResource) {
		this.dataResource = dataResource;
	}
}