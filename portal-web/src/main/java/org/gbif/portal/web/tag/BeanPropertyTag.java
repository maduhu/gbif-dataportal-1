/**
 * 
 */
package org.gbif.portal.web.tag;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author dave
 */
public class BeanPropertyTag extends TagSupport {

	private static final long serialVersionUID = 1462152656473311422L;
	protected static Log logger = LogFactory.getLog(BeanPropertyTag.class);
	
	protected Object bean;
	
	protected String property;
	
	protected String requestProperty;
	
	/**
	 * @see javax.servlet.jsp.tagext.TagSupport#doStartTag()
	 */
	@Override
	public int doStartTag() throws JspException {
		String propertyValue = null;
		try {
			propertyValue = BeanUtils.getProperty(bean, property);
		} catch (Exception e){
			logger.warn(e.getMessage(), e);
		}
		pageContext.getRequest().setAttribute(requestProperty, propertyValue);
		
		return super.doStartTag();
	}

	/**
	 * @param bean the bean to set
	 */
	public void setBean(Object bean) {
		this.bean = bean;
	}

	/**
	 * @param property the property to set
	 */
	public void setProperty(String property) {
		this.property = property;
	}

	/**
	 * @param requestProperty the requestProperty to set
	 */
	public void setRequestProperty(String requestProperty) {
		this.requestProperty = requestProperty;
	}
}