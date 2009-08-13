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
package org.gbif.portal.util.spring.config.string;

import java.util.List;

import org.springframework.beans.factory.config.AbstractFactoryBean;

/**
 * A factory that will create a String from a List<StringBean> in config.
 * 
 * Thus "global" StringBeans may be defined and reused in configuration.
 * For example, complex regular expressions may be built from smaller easier components
 * 
 * There exists further configuration options (for example, seperating all tokens with spaces)
 * 
 * @author trobertson
 */
public class ConcatenatedStringFactoryBean extends AbstractFactoryBean {
	/**
	 * The tokens that make the concatinated String
	 */
	protected List<StringBean> tokens;
	
	/**
	 * Convienience for seperating ALL tokens with a value
	 */
	protected String separator = "";

	/**
	 * @see org.springframework.beans.factory.config.AbstractFactoryBean#createInstance()
	 */
	@Override
	protected String createInstance() throws Exception {
		StringBuffer sb = new StringBuffer();
		int length = getTokens().size();
		int i = 0;
		for (StringBean s : getTokens()) {
			sb.append(s.getString());
			i++;
			if (i<length) {
				sb.append(getSeparator());
			}			
		}
		return sb.toString();
	}

	/**
	 * Returns String.class
	 * @see org.springframework.beans.factory.FactoryBean#getObjectType()
	 */
	public Class getObjectType() {
		return String.class;
	}

	/**
	 * @return Returns the tokens.
	 */
	public List<StringBean> getTokens() {
		return tokens;
	}

	/**
	 * @param tokens The tokens to set.
	 */
	public void setTokens(List<StringBean> tokens) {
		this.tokens = tokens;
	}

	/**
	 * @return Returns the separator.
	 */
	public String getSeparator() {
		return separator;
	}

	/**
	 * @param separator The separator to set.
	 */
	public void setSeparator(String separator) {
		this.separator = separator;
	}
}
