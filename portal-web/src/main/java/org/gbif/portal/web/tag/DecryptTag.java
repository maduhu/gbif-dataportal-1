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

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.gbif.portal.web.util.PasswordUtils;

/**
 * A tag for decrypting a password
 * 
 * @author dmartin
 */
public class DecryptTag extends TagSupport {

	private static final long serialVersionUID = -6227944715951126543L;
	protected static Log logger = LogFactory.getLog(DecryptTag.class);
	protected String encryptedPassword;
	
	/**
	 * @see javax.servlet.jsp.tagext.TagSupport#doStartTag()
	 */
	@Override
	public int doStartTag() throws JspException {
		if(encryptedPassword!=null){
			try {
				String decrypted = PasswordUtils.encryptPassword(encryptedPassword, false);
				if(logger.isDebugEnabled()){
					logger.debug("Decrypted password is: "+decrypted);
				}
				pageContext.getOut().print(decrypted);
			} catch (Exception e){
				logger.error(e.getMessage(), e);
				throw new JspException(e);
			}
		}
		return super.doStartTag();
	}

	/**
	 * @param encryptedPassword the encryptedPassword to set
	 */
	public void setEncryptedPassword(String encryptedPassword) {
		this.encryptedPassword = encryptedPassword;
	}
}
