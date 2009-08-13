/**
 * 
 */
package org.gbif.portal.web.tag;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.Tag;
import javax.servlet.jsp.tagext.TagSupport;

import org.gbif.portal.dto.taxonomy.BriefTaxonConceptDTO;
import org.gbif.portal.dto.util.TaxonRankType;

/**
 * Convienence tag for JSP requiring a test for major rank.
 * 
 * @author dmartin
 */
public class MajorRankTest extends TagSupport {

	private static final long serialVersionUID = 5098356989263220831L;

	protected boolean allowSubspecies = true;
	
	protected BriefTaxonConceptDTO concept;
	
	/**
	 * @see javax.servlet.jsp.tagext.TagSupport#doStartTag()
	 */
	@Override
	public int doStartTag() throws JspException {
		if(concept!=null 
				&& (
					TaxonRankType.isRecognisedMajorRank(concept.getRank())
					|| (allowSubspecies && TaxonRankType.SUB_SPECIES_STR.equals(concept.getRank()))
					)
		){
			return Tag.EVAL_BODY_INCLUDE;
		} else {
			return Tag.SKIP_BODY;
		}
	}

	/**
	 * @return the allowSubspecies
	 */
	public boolean isAllowSubspecies() {
		return allowSubspecies;
	}

	/**
	 * @param allowSubspecies the allowSubspecies to set
	 */
	public void setAllowSubspecies(boolean allowSubspecies) {
		this.allowSubspecies = allowSubspecies;
	}

	/**
	 * @return the concept
	 */
	public BriefTaxonConceptDTO getConcept() {
		return concept;
	}

	/**
	 * @param concept the concept to set
	 */
	public void setConcept(BriefTaxonConceptDTO concept) {
		this.concept = concept;
	}
}