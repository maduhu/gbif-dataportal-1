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
import javax.servlet.jsp.tagext.BodyTagSupport;

/**
 * @author dave
 */
public class DecimalTag extends BodyTagSupport {

	private static final long serialVersionUID = -8797050913251487535L;
	protected int noDecimalPlaces = 1;	
	
	/**
	 * @see javax.servlet.jsp.tagext.BodyTagSupport#doEndTag()
	 */
	@Override
	public int doEndTag() throws JspException {
		String number = this.bodyContent.getString();
		
		try {
			double theDouble = Double.parseDouble(number);
			
			int asInt = (int)(theDouble * (Math.pow(10,noDecimalPlaces)));
			
			double theNewDouble = (double) asInt;
			
			String toPrint = Double.toString(theNewDouble/(Math.pow(10,noDecimalPlaces)));
			if(noDecimalPlaces==0 && toPrint.endsWith(".0")){
				toPrint = toPrint.substring(0, toPrint.length()-2);
			}
			pageContext.getOut().write(toPrint);
			
		} catch (Exception e){
			
		}
		return super.doEndTag();
	}

	/**
	 * @param noDecimalPlaces the noDecimalPlaces to set
	 */
	public void setNoDecimalPlaces(int noDecimalPlaces) {
		this.noDecimalPlaces = noDecimalPlaces;
	}
}
