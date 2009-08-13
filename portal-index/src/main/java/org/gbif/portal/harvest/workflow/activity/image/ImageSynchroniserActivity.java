/**
 * 
 */
package org.gbif.portal.harvest.workflow.activity.image;

import org.apache.commons.lang.StringUtils;
import org.gbif.portal.dao.ImageRecordDAO;
import org.gbif.portal.model.ImageRecord;
import org.gbif.portal.util.workflow.BaseActivity;
import org.gbif.portal.util.workflow.ProcessContext;


/**
 * An activity that will preprocess the single record, to account for errors that IPNI provide.
 * 
 * <ul>
 * 	<li>
 * 		If the Name in the context starts with the Family, then it is stripped from the name.  This
 * 		is because of a known error in the IPNI extract
 * 	</li>
 * @author tim
 */
public class ImageSynchroniserActivity extends BaseActivity {
	/**
	 * DAOs
	 */
	protected ImageRecordDAO imageRecordDAO;
	
	/**
	 * Context keys
	 */
	protected String contextKeyDataProviderId;
	protected String contextKeyDataResourceId;
	protected String contextKeyTaxonConceptId;
	protected String contextKeyUrl;
	protected String contextKeyHtmlForDisplay;
	protected String contextKeyDescription;
	protected String contextKeyRights;
	protected String contextKeyArkivePortletUrl;
	protected String contextKeyHeight;
	protected String contextKeyWidth;
	
	/**
	 * @see org.gbif.portal.util.workflow.Activity#execute(org.gbif.portal.util.workflow.ProcessContext)
	 */
	@SuppressWarnings("unchecked")
	public ProcessContext execute(final ProcessContext context) throws Exception {
		long dataProviderId = ((Long)context.get(getContextKeyDataProviderId(), Long.class, true)).longValue();
		long dataResourceId = ((Long)context.get(getContextKeyDataResourceId(), Long.class, true)).longValue();
		long taxonConceptId = ((Long)context.get(getContextKeyTaxonConceptId(), Long.class, true)).longValue();
		String url = (String) context.get(getContextKeyUrl(), String.class, true);
		String htmlForDisplay = StringUtils.trimToNull((String) context.get(getContextKeyHtmlForDisplay(), String.class, false));
		if (htmlForDisplay == null) {
			String arkivePortletUrl = StringUtils.trimToNull((String) context.get(getContextKeyArkivePortletUrl(), String.class, false));
			if (arkivePortletUrl != null) {
				StringBuffer sb = new StringBuffer();
				sb.append("<iframe src=\"");
				sb.append(arkivePortletUrl);
				sb.append("\" frameborder=\"0\" hspace=\"1\" vspace=\"1\" scrolling=\"no\" width=\"");
				sb.append((String) context.get(getContextKeyWidth(), String.class, true));
				sb.append("\" height=\"");
				sb.append((String) context.get(getContextKeyHeight(), String.class, true));
				sb.append("\"><a href=\"http://www.arkive.org\"><img src=\"http://portlet.arkive.org/images/frogsPortletPortrait.gif\" alt=\"ARKive - Images of Life on Earth\" border=\"0\" width=\"171\" height=\"180\"/></a></iframe>");
				htmlForDisplay = sb.toString();
			}
		}
		String description = (String) context.get(getContextKeyDescription(), String.class, false);
		String rights = (String) context.get(getContextKeyRights(), String.class, false);
		
		ImageRecord image = imageRecordDAO.findByDataResourceAndTaxonConceptAndUrl(dataResourceId, taxonConceptId, url);
		
		if (image == null) {
			image = new ImageRecord();
			image.setDataResourceId(dataResourceId);
			image.setTaxonConceptId(taxonConceptId);
			image.setUrl(url);
			image.setOccurrenceId(null);
		}
		
		image.setHtmlForDisplay(htmlForDisplay);
		image.setDescription(description);
		image.setRights(rights);
		
		imageRecordDAO.updateOrCreate(image);
		
		return context;
	}

	/**
	 * @return the contextKeyArkivePortletUrl
	 */
	public String getContextKeyArkivePortletUrl() {
		return contextKeyArkivePortletUrl;
	}

	/**
	 * @param contextKeyArkivePortletUrl the contextKeyArkivePortletUrl to set
	 */
	public void setContextKeyArkivePortletUrl(String contextKeyArkivePortletUrl) {
		this.contextKeyArkivePortletUrl = contextKeyArkivePortletUrl;
	}

	/**
	 * @return the contextKeyDataProviderId
	 */
	public String getContextKeyDataProviderId() {
		return contextKeyDataProviderId;
	}

	/**
	 * @param contextKeyDataProviderId the contextKeyDataProviderId to set
	 */
	public void setContextKeyDataProviderId(String contextKeyDataProviderId) {
		this.contextKeyDataProviderId = contextKeyDataProviderId;
	}

	/**
	 * @return the contextKeyDataResourceId
	 */
	public String getContextKeyDataResourceId() {
		return contextKeyDataResourceId;
	}

	/**
	 * @param contextKeyDataResourceId the contextKeyDataResourceId to set
	 */
	public void setContextKeyDataResourceId(String contextKeyDataResourceId) {
		this.contextKeyDataResourceId = contextKeyDataResourceId;
	}

	/**
	 * @return the contextKeyDescription
	 */
	public String getContextKeyDescription() {
		return contextKeyDescription;
	}

	/**
	 * @param contextKeyDescription the contextKeyDescription to set
	 */
	public void setContextKeyDescription(String contextKeyDescription) {
		this.contextKeyDescription = contextKeyDescription;
	}

	/**
	 * @return the contextKeyHeight
	 */
	public String getContextKeyHeight() {
		return contextKeyHeight;
	}

	/**
	 * @param contextKeyHeight the contextKeyHeight to set
	 */
	public void setContextKeyHeight(String contextKeyHeight) {
		this.contextKeyHeight = contextKeyHeight;
	}

	/**
	 * @return the contextKeyHtmlForDisplay
	 */
	public String getContextKeyHtmlForDisplay() {
		return contextKeyHtmlForDisplay;
	}

	/**
	 * @param contextKeyHtmlForDisplay the contextKeyHtmlForDisplay to set
	 */
	public void setContextKeyHtmlForDisplay(String contextKeyHtmlForDisplay) {
		this.contextKeyHtmlForDisplay = contextKeyHtmlForDisplay;
	}

	/**
	 * @return the contextKeyTaxonConceptId
	 */
	public String getContextKeyTaxonConceptId() {
		return contextKeyTaxonConceptId;
	}

	/**
	 * @param contextKeyTaxonConceptId the contextKeyTaxonConceptId to set
	 */
	public void setContextKeyTaxonConceptId(String contextKeyTaxonConceptId) {
		this.contextKeyTaxonConceptId = contextKeyTaxonConceptId;
	}

	/**
	 * @return the contextKeyUrl
	 */
	public String getContextKeyUrl() {
		return contextKeyUrl;
	}

	/**
	 * @param contextKeyUrl the contextKeyUrl to set
	 */
	public void setContextKeyUrl(String contextKeyUrl) {
		this.contextKeyUrl = contextKeyUrl;
	}

	/**
	 * @return the contextKeyWidth
	 */
	public String getContextKeyWidth() {
		return contextKeyWidth;
	}

	/**
	 * @param contextKeyWidth the contextKeyWidth to set
	 */
	public void setContextKeyWidth(String contextKeyWidth) {
		this.contextKeyWidth = contextKeyWidth;
	}

	/**
	 * @return the contextKeyRights
	 */
	public String getContextKeyRights() {
		return contextKeyRights;
	}

	/**
	 * @param contextKeyRights the contextKeyRights to set
	 */
	public void setContextKeyRights(String contextKeyRights) {
		this.contextKeyRights = contextKeyRights;
	}

	/**
	 * @return the imageRecordDAO
	 */
	public ImageRecordDAO getImageRecordDAO() {
		return imageRecordDAO;
	}

	/**
	 * @param imageRecordDAO the imageRecordDAO to set
	 */
	public void setImageRecordDAO(ImageRecordDAO imageRecordDAO) {
		this.imageRecordDAO = imageRecordDAO;
	}

}
