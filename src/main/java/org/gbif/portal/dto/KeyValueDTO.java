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
package org.gbif.portal.dto;

import java.io.Serializable;
import java.util.Comparator;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * A lite dto for transferring key value pairs between layers.
 * The comparator will use the value to order when in a list.
 * 
 * @author dmartin
 */
public final class KeyValueDTO implements Comparable, Serializable {

	private static final long serialVersionUID = -5463608481683535799L;
	/**The key **/
	protected String key;
	/**The value **/
	protected String value;
	
	/**
	 * Default contructor.
	 */
	public KeyValueDTO(){}
	
	/**
	 * initialise the key and value.
	 * 
	 * @param key
	 * @param value
	 */
	public KeyValueDTO(String key, String value){
		this.key = key;
		this.value = value;
	}
	
	/**
	 * @return the key
	 */
	public String getKey() {
		return key;
	}

	/**
	 * @param key the key to set
	 */
	public void setKey(String key) {
		this.key = key;
	}

	/**
	 * @return the value
	 */
	public String getValue() {
		return value;
	}

	/**
	 * @param value the value to set
	 */
	public void setValue(String value) {
		this.value = value;
	}

	/**
	 * To order on the value
	 */
	public int compareTo(Object object) {
		if (object instanceof KeyValueDTO) {
			return this.getValue().compareTo(((KeyValueDTO)object).getValue());
		}
		return 0;
	}

	public static class ValueComparator implements Comparator<KeyValueDTO> {
		public int compare(KeyValueDTO keyValue1, KeyValueDTO keyValue2) {
			if(keyValue1==null || keyValue2==null || keyValue1.getValue()==null || keyValue2.getValue()==null)
				return -1;
			return keyValue1.getValue().compareTo(keyValue2.getValue());
		}
	}
	
	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if(obj==null)
			return false;
		if(obj instanceof KeyValueDTO){
			KeyValueDTO kvDTO = (KeyValueDTO) obj;
			if(kvDTO.getKey()!=null && this.getKey()!=null && kvDTO.getKey().equals(this.getKey()))
				return true;
		}
		return false;
	}
}