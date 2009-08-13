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
package org.gbif.portal.model.occurrence;

import java.io.Serializable;

import org.gbif.portal.model.IntegerEnumType;
import org.gbif.portal.util.db.OccurrenceRecordUtils;

/**
 * Enumerated type for images
 *
 * @author dhobern
 */
public class ImageType extends IntegerEnumType implements Serializable{
	/**
	 * Generated
	 */
	private static final long serialVersionUID = -8538609922118499819L;

	public static final ImageType UNKNOWN = new ImageType("unknown", OccurrenceRecordUtils.IMAGETYPE_UNKNOWN);
	public static final ImageType PRODUCT = new ImageType("product", OccurrenceRecordUtils.IMAGETYPE_PRODUCT);
	public static final ImageType UNKNOWNIMAGE = new ImageType("unknown image", OccurrenceRecordUtils.IMAGETYPE_UNKNOWNIMAGE);
	public static final ImageType LIVEORGANISMIMAGE = new ImageType("live organism image", OccurrenceRecordUtils.IMAGETYPE_LIVEORGANISMIMAGE);
	public static final ImageType SPECIMENIMAGE = new ImageType("specimen image", OccurrenceRecordUtils.IMAGETYPE_SPECIMENIMAGE);
	public static final ImageType LABELIMAGE = new ImageType("label image", OccurrenceRecordUtils.IMAGETYPE_LABELIMAGE);
	
	public ImageType() {
		//default constructor, required by hibernate
	}
	
	private ImageType(String name, int value) {
		super(name, Integer.valueOf(value));
	}
	
	/**
	 * Utility method to return the enumerated instance for the specified name
	 * @param name The enumerated name value
	 * @return The enumerated instance if found or null
	 */
	public static final ImageType getImageType(String name) {
		if (name != null) {
			if (name.equalsIgnoreCase(UNKNOWN.getName())) {
				return UNKNOWN;
			} else if (name.equalsIgnoreCase(PRODUCT.getName())) {
				return PRODUCT;
			} else if (name.equalsIgnoreCase(UNKNOWNIMAGE.getName())) {
				return UNKNOWNIMAGE;
			} else if (name.equalsIgnoreCase(LIVEORGANISMIMAGE.getName())) {
				return LIVEORGANISMIMAGE;
			} else if (name.equalsIgnoreCase(SPECIMENIMAGE.getName())) {
				return SPECIMENIMAGE;
			} else if (name.equalsIgnoreCase(LABELIMAGE.getName())) {
				return LABELIMAGE;
			} 
	
		}
		return null;
	}
	
	/**
	 * Utility method to return the enumerated instance for the specified value
	 * @param value The enumerated name integer value
	 * @return The enumerated instance if found or null
	 */
	public static final ImageType getBasisOfRecord(int value) {
		if (value == UNKNOWN.getValue()) {
			return UNKNOWN;
		} else if (value == PRODUCT.getValue()) {
			return PRODUCT;
		} else if (value == UNKNOWNIMAGE.getValue()) {
			return UNKNOWNIMAGE;
		} else if (value == LIVEORGANISMIMAGE.getValue()) {
			return LIVEORGANISMIMAGE;
		} else if (value == SPECIMENIMAGE.getValue()) {
			return SPECIMENIMAGE;
		} else if (value == LABELIMAGE.getValue()) {
			return LABELIMAGE;
		} 
		return null;
	}
}