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

package org.gbif.portal.dto.taxonomy;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.gbif.portal.model.taxonomy.IdType;

/**
 * DTO for a remote concept.
 * 
 * @author Donald Hobern
 */
public class RemoteConceptDTO implements Serializable {

	/**
	 * Generated 
	 */
	private static final long serialVersionUID = -6077873747384154612L;
	
	/** The key for the record */
	private String key;
	/** The remote id for the concept */
	private String remoteId;
	/** The concept this remote concept identifies */
	private String taxonConceptKey;
	/** The type of identifier */
	private IdType idType;

	/**
	 * @return the idType
	 */
	public IdType getIdType() {
		return idType;
	}

	/**
	 * @param idType the idType to set
	 */
	public void setIdType(IdType idType) {
		this.idType = idType;
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
	 * @return the remoteId
	 */
	public String getRemoteId() {
		return remoteId;
	}

	/**
	 * @param remoteKey the remoteKey to set
	 */
	public void setRemoteId(String remoteId) {
		this.remoteId = remoteId;
	}

	/**
	 * @return the taxonConceptKey
	 */
	public String getTaxonConceptKey() {
		return taxonConceptKey;
	}

	/**
	 * @param taxonConceptKey the taxonConceptKey to set
	 */
	public void setTaxonConceptKey(String taxonConceptKey) {
		this.taxonConceptKey = taxonConceptKey;
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if(obj==null)
			return false;
		if(obj instanceof RemoteConceptDTO){
			RemoteConceptDTO remoteConceptDTO = (RemoteConceptDTO) obj;
			if(remoteConceptDTO.getKey().equals(this.getKey()))
			  return true;
			if(remoteConceptDTO.getTaxonConceptKey().equals(this.getTaxonConceptKey())
				&& remoteConceptDTO.getIdType().equals(this.getIdType())
				&& remoteConceptDTO.getRemoteId().equals(this.getRemoteId()))
				return true;
		}
		return false;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}