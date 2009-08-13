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
package org.gbif.portal.web.filter;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

/**
 * DTO collection of criterion used for passing details of a search.
 * 
 * @author dmartin
 */
public class CriteriaDTO implements List {
	/**The set of criterion objects this criteriaDTO encapsulates**/
	private List<CriterionDTO> criteria = new LinkedList<CriterionDTO>();
	/**Flag indicating if all criteria should be matched or to accept partial matches**/
	protected boolean matchAll = true;

	
	/**
	 * Returns true if criteria is empty.
	 * @return
	 */
	public boolean isEmpty(){
		if(criteria==null)
			return true;
		return criteria.isEmpty();
	}
	
	
	@Override
	/**
	 * Compares each criterion in the collection.
	 * @return true if objects are equal
	 */
	public boolean equals(Object object) {
		if(object == null)
			return false;
		if(object instanceof CriteriaDTO){
			CriteriaDTO criteriaDTO = (CriteriaDTO) object;
			if(this.matchAll!= criteriaDTO.isMatchAll())
				return false;
			List<CriterionDTO> criteria = criteriaDTO.getCriteria();
			if (criteria==null && this.criteria ==null)
				return true;
			if (criteria==null && this.criteria !=null)
				return false;
			if (criteria!=null && this.criteria ==null)
				return false;
			if(this.criteria.size()==criteria.size()){
				//compare criterion
				Iterator criteriaIter = criteria.iterator();
				for(CriterionDTO myCriterion: this.criteria){
					CriterionDTO criterion = (CriterionDTO) criteriaIter.next();
					if (!(myCriterion.equals(criterion))){
						return false;
					}
				}
				return true;
			}
		}
		return false;
	}
	

	@Override
	public int hashCode() {
		if(criteria==null || criteria.size()==0){
			return super.hashCode();
		} else {
			int hashCode=0;
			for (CriterionDTO criterion: criteria)
				hashCode+=criterion.hashCode();
			return hashCode;
		}
	}

	@Override
	public String toString() {
		if(criteria==null || criteria.size()==0){
			return "Empty Criteria";
		} else {
			StringBuffer sb= new StringBuffer();
			for (CriterionDTO criterion: criteria){
				sb.append('[');
				sb.append(criterion.toString());
				sb.append(']');				
			}
			sb.append(", matchAll:");
			sb.append(matchAll);
			return sb.toString();
		}
	}

	/**
	 * @return the criteria
	 */
	public List<CriterionDTO> getCriteria() {
		return criteria;
	}

	/**
	 * @param criteria the criteria to set
	 */
	public void setCriteria(List<CriterionDTO> criteria) {
		this.criteria = criteria;
	}

	/**
	 * @return the matchAll
	 */
	public boolean isMatchAll() {
		return matchAll;
	}

	/**
	 * @param matchAll the matchAll to set
	 */
	public void setMatchAll(boolean matchAll) {
		this.matchAll = matchAll;
	}


	/**
	 * @param o
	 * @return
	 * @see java.util.List#add(java.lang.Object)
	 */
	public boolean add(CriterionDTO o) {
		return criteria.add(o);
	}


	/**
	 * @param index
	 * @param element
	 * @see java.util.List#add(int, java.lang.Object)
	 */
	public void add(int index, CriterionDTO element) {
		criteria.add(index, element);
	}


	/**
	 * @param c
	 * @return
	 * @see java.util.List#addAll(java.util.Collection)
	 */
	@SuppressWarnings("unchecked")
	public boolean addAll(Collection c) {
		return criteria.addAll(c);
	}


	/**
	 * @param index
	 * @param c
	 * @return
	 * @see java.util.List#addAll(int, java.util.Collection)
	 */
	@SuppressWarnings("unchecked")
	public boolean addAll(int index, Collection c) {
		return criteria.addAll(index, c);
	}


	/**
	 * 
	 * @see java.util.List#clear()
	 */
	public void clear() {
		criteria.clear();
	}


	/**
	 * @param o
	 * @return
	 * @see java.util.List#contains(java.lang.Object)
	 */
	public boolean contains(Object o) {
		return criteria.contains(o);
	}


	/**
	 * @param c
	 * @return
	 * @see java.util.List#containsAll(java.util.Collection)
	 */
	public boolean containsAll(Collection c) {
		return criteria.containsAll(c);
	}


	/**
	 * @param index
	 * @return
	 * @see java.util.List#get(int)
	 */
	public CriterionDTO get(int index) {
		return criteria.get(index);
	}


	/**
	 * @param o
	 * @return
	 * @see java.util.List#indexOf(java.lang.Object)
	 */
	public int indexOf(Object o) {
		return criteria.indexOf(o);
	}


	/**
	 * @return
	 * @see java.util.List#iterator()
	 */
	public Iterator<CriterionDTO> iterator() {
		return criteria.iterator();
	}


	/**
	 * @param o
	 * @return
	 * @see java.util.List#lastIndexOf(java.lang.Object)
	 */
	public int lastIndexOf(Object o) {
		return criteria.lastIndexOf(o);
	}


	/**
	 * @return
	 * @see java.util.List#listIterator()
	 */
	public ListIterator<CriterionDTO> listIterator() {
		return criteria.listIterator();
	}


	/**
	 * @param index
	 * @return
	 * @see java.util.List#listIterator(int)
	 */
	public ListIterator<CriterionDTO> listIterator(int index) {
		return criteria.listIterator(index);
	}


	/**
	 * @param index
	 * @return
	 * @see java.util.List#remove(int)
	 */
	public CriterionDTO remove(int index) {
		return criteria.remove(index);
	}


	/**
	 * @param o
	 * @return
	 * @see java.util.List#remove(java.lang.Object)
	 */
	public boolean remove(Object o) {
		return criteria.remove(o);
	}


	/**
	 * @param c
	 * @return
	 * @see java.util.List#removeAll(java.util.Collection)
	 */
	public boolean removeAll(Collection c) {
		return criteria.removeAll(c);
	}


	/**
	 * @param c
	 * @return
	 * @see java.util.List#retainAll(java.util.Collection)
	 */
	public boolean retainAll(Collection c) {
		return criteria.retainAll(c);
	}


	/**
	 * @param index
	 * @param element
	 * @return
	 * @see java.util.List#set(int, java.lang.Object)
	 */
	public CriterionDTO set(int index, CriterionDTO element) {
		return criteria.set(index, element);
	}


	/**
	 * @return
	 * @see java.util.List#size()
	 */
	public int size() {
		return criteria.size();
	}


	/**
	 * @param fromIndex
	 * @param toIndex
	 * @return
	 * @see java.util.List#subList(int, int)
	 */
	public List<CriterionDTO> subList(int fromIndex, int toIndex) {
		return criteria.subList(fromIndex, toIndex);
	}


	/**
	 * @return
	 * @see java.util.List#toArray()
	 */
	public Object[] toArray() {
		return criteria.toArray();
	}


	/**
	 * @param <T>
	 * @param a
	 * @return
	 * @see java.util.List#toArray(T[])
	 */
	public <T> T[] toArray(CriterionDTO[] a) {
		throw new IllegalArgumentException("Method not supported");
	}


	public boolean add(Object o) {
		return criteria.add((CriterionDTO)o);
	}

	public void add(int index, Object element) {
		criteria.add(index, (CriterionDTO) element);
	}


	public Object set(int index, Object element) {
		return criteria.set(index, (CriterionDTO) element);
	}


	@SuppressWarnings("unchecked")
	public Object[] toArray(Object[] a) {
		return criteria.toArray(a);
	}
}