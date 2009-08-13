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

package org.gbif.portal.model;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.hibernate.HibernateException;
import org.hibernate.type.NullableType;
import org.hibernate.usertype.UserType;

/**
 * BaseEnumType
 * 
 * Base type (UserType) for persisted enumerations.
 * 
 * This implementation will tie our model to hibernate, to be refactored later.
 * 
 * @author dbarnier
 */
public abstract class BaseEnumType implements UserType, Serializable {

	private static final Map<Class, Map> enumClassMap = new HashMap<Class, Map>();
	/**The code for this enumerated type**/
	private Serializable code;
	/**The name of this enumerated type**/
	private String name;
	
	public BaseEnumType() {
		//default constructor, required by hibernate 
	}
	
	/**
	 * Initialises the name and code and adds to enum map.
	 * @param name
	 * @param code
	 * @throws IllegalArgumentException if code already exists
	 */
	@SuppressWarnings("unchecked")
	public BaseEnumType(String name, Serializable code) {
		this.name = name;
		this.code = code;
		
		Map enumEntryMap = enumClassMap.get(returnedClass());
		if (enumEntryMap == null) {
			enumEntryMap = new HashMap();
			enumClassMap.put(returnedClass(), enumEntryMap);
		}
		if (enumEntryMap.containsKey(code)) {
			throw new IllegalArgumentException("code must be unique: "+code);
		}
		enumEntryMap.put(code, this);
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.hibernate.usertype.UserType#assemble(java.io.Serializable, java.lang.Object)
	 */
	public Object assemble(Serializable cached, Object owner) {
		//todo - verify this is correct
		return cached;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.hibernate.usertype.UserType#deepCopy(java.lang.Object)
	 */
	public Object deepCopy(Object value)
	throws HibernateException {
		//enums are immutable, return value
		return value;
	}
	
	/**
	 * @see org.hibernate.usertype.UserType#disassemble(java.lang.Object)
	 */
	public Serializable disassemble(Object value) {
		//todo - verify this is correct
		return (BaseEnumType)value;
	}
	
	/**
	 * @see org.hibernate.usertype.UserType#equals(java.lang.Object, java.lang.Object)
	 */
	public boolean equals(Object x, Object y)
	throws HibernateException {
		if (x == y) {
			return true;
		}
		else if (x == null || y == null) {
			return false;
		}
		else {
			return getNullableType().isEqual(x, y);
		}
	}
	
	/**
	 * Returns the Hibernate type of the enumerated code
	 * @return
	 */
	public abstract NullableType getNullableType();
	
	/**
	 * @see org.hibernate.usertype.UserType#hashCode(java.lang.Object)
	 */
	public int hashCode(Object x) {
		//todo - verify this is correct
		return x.hashCode();
	}
	
	/**
	 * @see org.hibernate.usertype.UserType#isMutable()
	 */
	public boolean isMutable() {
		//enums are immutable, return false
		return false;
	}
	
	/**
	 * @see org.hibernate.usertype.UserType#nullSafeGet(java.sql.ResultSet, java.lang.String[], java.lang.Object)
	 */
	public Object nullSafeGet(ResultSet rs, String[] names, Object owner)
	throws HibernateException, SQLException {
		Serializable enumCode = (Serializable)getNullableType().nullSafeGet(rs, names[0]);
		Map enumEntryMap = enumClassMap.get(returnedClass());
		if (enumEntryMap != null) {
			return enumEntryMap.get(enumCode);
		}
		return null;
	}
	
	/**
	 * @see org.hibernate.usertype.UserType#nullSafeSet(java.sql.PreparedStatement, java.lang.Object, int)
	 */
	public void nullSafeSet(PreparedStatement st, Object value, int index)
	throws HibernateException, SQLException {
		if (value == null) {
			st.setNull(index, getNullableType().sqlType());
		}
		else if (value instanceof BaseEnumType) {
			st.setObject(index, ((BaseEnumType)value).getCode(), getNullableType().sqlType());
		}
		else {
//			System.out.println("+++++ st: " + st +" +++++");			
//			System.out.println("+++++ value: " + value +" +++++");			
//			System.out.println("+++++ index: " + index +" +++++");			
			throw new IllegalArgumentException("value not of type BaseEnumType");
		}
	}
	
	/**
	 * @see org.hibernate.usertype.UserType#replace(java.lang.Object, java.lang.Object, java.lang.Object)
	 */
	public Object replace(Object original, Object target, Object owner)
	throws HibernateException {
		//enums are immutable, return original
		return original;
	}
	
	/**
	 * @see org.hibernate.usertype.UserType#returnedClass()
	 */
	public Class returnedClass() {
		return getClass();
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.hibernate.usertype.UserType#sqlTypes()
	 */
	public int[] sqlTypes() {
		return new int[] { getNullableType().sqlType() };
	}

	/**
	 * @return the code
	 */
	public Serializable getCode() {
		return code;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}	
	
	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return getName()+":"+getCode();
	}
}