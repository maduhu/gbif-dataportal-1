/***************************************************************************
 * Copyright (C) 2006 Global Biodiversity Information Facility Secretariat.  
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

package org.gbif.portal.dto.util;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.gbif.portal.service.ServiceException;

/**
 * A DTO to represent a bounding box (e.g. for use with mapping). 
 * The box is bounded with java.lang.Float in order to allow "open" sides
 * of type - in the Northern Hemisphere for example
 * 
 * @author trobertson
 */
public class BoundingBoxDTO {
	
	public static final BoundingBoxDTO GLOBAL_BOUNDING_BOX = new BoundingBoxDTO(new Float(-180), new Float(-90), new Float(180), new Float(90));
	
	/** The left bounding parameter */
	protected Float left;
	/** The right bounding parameter */
	protected Float right;
	/** The lower bounding parameter */
	protected Float lower;
	/** The upper bounding parameter */
	protected Float upper;
	
	/**
	 * Takes the parameters to bound a rectangle.
	 * 
	 * @param left The left bounding parameter or null if unspecified
	 * @param lower The lower bounding parameter or null if unspecified
	 * @param right The right bounding parameter or null if unspecified
	 * @param upper The upper bounding parameter or null if unspecified
	 */
	public BoundingBoxDTO(Float left, Float lower, Float right, Float upper) {
		this.left = left;
		this.upper = upper;
		this.right = right;
		this.lower = lower;
	}

	/**
	 * Verifies that bounding box is valid
	 */
	public void checkValidity() throws ServiceException {
		if (upper != null && lower != null && upper < lower)
			throw new ServiceException("maxLatitude smaller than minLatitude");
		if (left != null && right != null && right < left)
			throw new ServiceException("maxLongitude smaller than minLongitude - not handling bounding boxes across dateline");
	}

	/**
	 * Default Constructor
	 */
	public BoundingBoxDTO() {}

	/**
	 * @return the left
	 */
	public Float getLeft() {
		return left;
	}

	/**
	 * @param left the left to set
	 */
	public void setLeft(Float left) {
		this.left = left;
	}

	/**
	 * @return the lower
	 */
	public Float getLower() {
		return lower;
	}

	/**
	 * @param lower the lower to set
	 */
	public void setLower(Float lower) {
		this.lower = lower;
	}

	/**
	 * @return the right
	 */
	public Float getRight() {
		return right;
	}

	/**
	 * @param right the right to set
	 */
	public void setRight(Float right) {
		this.right = right;
	}

	/**
	 * @return the upper
	 */
	public Float getUpper() {
		return upper;
	}

	/**
	 * @param upper the upper to set
	 */
	public void setUpper(Float upper) {
		this.upper = upper;
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if(obj==null)
			return false;
		
		if(obj instanceof BoundingBoxDTO){
			
			BoundingBoxDTO bbDTO = (BoundingBoxDTO) obj;
			if(nullSafeEquals(bbDTO.getLeft(), this.getLeft())
				&& nullSafeEquals(bbDTO.getLower(), this.getLower())
				&& nullSafeEquals(bbDTO.getRight(), this.getRight())
				&& nullSafeEquals(bbDTO.getUpper(), this.getUpper())
			)
				return true;
		}
		return false;
	}

	public boolean nullSafeEquals(Float original, Float comparedTo){
		if (original==null && comparedTo==null)
			return true;
		if (original!=null && comparedTo==null)
			return false;		
		if (original==null && comparedTo!=null)
			return false;			
		return original.equals(comparedTo);
	}	
	
	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}	
}