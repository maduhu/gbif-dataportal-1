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

package org.gbif.portal.util.geospatial;

/**
 * A bounding box for lat long
 * 
 * @author tim
 */
public class LatLongBoundingBox {

	public static final LatLongBoundingBox GLOBAL_BOUNDING_BOX = new LatLongBoundingBox(-180, -90, 180, 90);
	
	float minLong;
	float minLat;
	float maxLong;
	float maxLat;
	
	/**
	 * Force the box to be well formed
	 * @param minLat
	 * @param maxLat
	 * @param minLong
	 * @param maxLong
	 */
	public LatLongBoundingBox(float minLong, float minLat, float maxLong, float maxLat) {
		this.minLong = minLong;
		this.minLat = minLat;
		this.maxLong = maxLong;
		this.maxLat = maxLat;
	}
	
	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object t) {
		if (t instanceof LatLongBoundingBox) {
			LatLongBoundingBox target = (LatLongBoundingBox) t;
			if (minLat == target.getMinLat()
					&& minLong == target.getMinLong()
					&& maxLat == target.getMaxLat()
					&& maxLong == target.getMaxLong()) {
				return true;
			}
		}
		return super.equals(t);
	}
	
	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "minLong[" + minLong +"] minLat["+ minLat+"] maxLong["+ maxLong +"] maxLat["+ maxLat +"] ";
	}

	/**
	 * @return the maxLat
	 */
	public float getMaxLat() {
		return maxLat;
	}
	/**
	 * @param maxLat the maxLat to set
	 */
	public void setMaxLat(float maxLat) {
		this.maxLat = maxLat;
	}
	/**
	 * @return the maxLong
	 */
	public float getMaxLong() {
		return maxLong;
	}
	/**
	 * @param maxLong the maxLong to set
	 */
	public void setMaxLong(float maxLong) {
		this.maxLong = maxLong;
	}
	/**
	 * @return the minLat
	 */
	public float getMinLat() {
		return minLat;
	}
	/**
	 * @param minLat the minLat to set
	 */
	public void setMinLat(float minLat) {
		this.minLat = minLat;
	}
	/**
	 * @return the minLong
	 */
	public float getMinLong() {
		return minLong;
	}
	/**
	 * @param minLong the minLong to set
	 */
	public void setMinLong(float minLong) {
		this.minLong = minLong;
	}
}