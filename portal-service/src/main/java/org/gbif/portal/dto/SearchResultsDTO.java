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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * SearchResultsDTO. 
 * 
 * Encapsulates a list of search results for a query.
 * This class contains keys to enable paging through sets of results. This is only of
 * use if the query is ordered on key.
 * 
 * The hasMoreResults property indicates a limited result set.
 * 
 * @author dmartin
 */
public class SearchResultsDTO implements List{
	
	/**the key of the first result**/
	protected String firstKey;
	/**the key of the last result**/	
	protected String lastKey;
	/**the key of the next result. should be null if hasMoreResults is false**/	
	protected String nextKey;
	/**Indicates if the results returned was limited by an imposed limit**/	
	protected boolean hasMoreResults;
	/**The actual results this class encapsulates **/
	protected List results = new ArrayList();
	
	/**
	 * Sets the results on the DTO truncating the number of results
	 * to the maxResults
	 * @param results
	 * @param maxResults
	 */
	public void setResults(List results, Integer maxResults){
		if(results.size() > maxResults){
			hasMoreResults = true;
			//truncate results
			int overflow = results.size() - maxResults;
			for (int i=0; i<overflow; i++)
				results.remove(results.size()-1);
		}
		setResults(results);
	}		

	/**
	 * Returns true if contains no results.
	 * @return true if contains no results.
	 */
	public boolean isEmpty(){
		if(results==null)
			return true;
		return results.isEmpty();
	}
	
	/**
	 * @return the firstKey
	 */
	public String getFirstKey() {
		return firstKey;
	}

	/**
	 * @param firstKey the firstKey to set
	 */
	public void setFirstKey(String firstKey) {
		this.firstKey = firstKey;
	}
	/**
	 * @return the hasMoreResults
	 */
	public boolean getHasMoreResults() {
		return hasMoreResults;
	}
	
	/**
	 * @return the hasMoreResults
	 */
	public boolean hasMoreResults() {
		return hasMoreResults;
	}
	
	/**
	 * @return the hasMoreResults
	 */
	public boolean isHasMoreResults() {
		return hasMoreResults;
	}		

	/**
	 * @param hasMoreResults the hasMoreResults to set
	 */
	public void setHasMoreResults(boolean hasMoreResults) {
		this.hasMoreResults = hasMoreResults;
	}

	/**
	 * @return the lastKey
	 */
	public String getLastKey() {
		return lastKey;
	}

	/**
	 * @param lastKey the lastKey to set
	 */
	public void setLastKey(String lastKey) {
		this.lastKey = lastKey;
	}

	/**
	 * @return the nextKey
	 */
	public String getNextKey() {
		return nextKey;
	}

	/**
	 * @param nextKey the nextKey to set
	 */
	public void setNextKey(String nextKey) {
		this.nextKey = nextKey;
	}

	/**
	 * Returns the results this results dto encapsulates.
	 * @return the list of results.
	 */
	public List getResults(){
		return results;
	}
	
	/**
	 * Sets the results this results dto encapsulates.
	 * @param results
	 */
	public void setResults(List results){
		this.results = results;
	}
	
	/**
	 * Adds this result this results list encapsulates. The supplied object
	 * should be a DTO of the correct type.
	 * @param the result to add
	 */
	@SuppressWarnings("unchecked")
	public void addResult(Object result){
		this.results.add(result);
	}

	/**
	 * @param o
	 * @return
	 * @see java.util.List#add(java.lang.Object)
	 */
	public boolean add(Object o) {
		return results.add(o);
	}

	/**
	 * @param index
	 * @param element
	 * @see java.util.List#add(int, java.lang.Object)
	 */
	public void add(int index, Object element) {
		results.add(index, element);
	}

	/**
	 * @param c
	 * @return
	 * @see java.util.List#addAll(java.util.Collection)
	 */
	public boolean addAll(Collection c) {
		return results.addAll(c);
	}

	/**
	 * @param index
	 * @param c
	 * @return
	 * @see java.util.List#addAll(int, java.util.Collection)
	 */
	public boolean addAll(int index, Collection c) {
		return results.addAll(index, c);
	}

	/**
	 * 
	 * @see java.util.List#clear()
	 */
	public void clear() {
		results.clear();
	}

	/**
	 * @param o
	 * @return
	 * @see java.util.List#contains(java.lang.Object)
	 */
	public boolean contains(Object o) {
		return results.contains(o);
	}

	/**
	 * @param c
	 * @return
	 * @see java.util.List#containsAll(java.util.Collection)
	 */
	public boolean containsAll(Collection c) {
		return results.containsAll(c);
	}

	/**
	 * @param o
	 * @return
	 * @see java.util.List#equals(java.lang.Object)
	 */
	public boolean equals(Object o) {
		return results.equals(o);
	}

	/**
	 * @param index
	 * @return
	 * @see java.util.List#get(int)
	 */
	public Object get(int index) {
		return results.get(index);
	}

	/**
	 * @return
	 * @see java.util.List#hashCode()
	 */
	public int hashCode() {
		return results.hashCode();
	}

	/**
	 * @param o
	 * @return
	 * @see java.util.List#indexOf(java.lang.Object)
	 */
	public int indexOf(Object o) {
		return results.indexOf(o);
	}

	/**
	 * @return
	 * @see java.util.List#iterator()
	 */
	public Iterator iterator() {
		return results.iterator();
	}

	/**
	 * @param o
	 * @return
	 * @see java.util.List#lastIndexOf(java.lang.Object)
	 */
	public int lastIndexOf(Object o) {
		return results.lastIndexOf(o);
	}

	/**
	 * @return
	 * @see java.util.List#listIterator()
	 */
	public ListIterator listIterator() {
		return results.listIterator();
	}

	/**
	 * @param index
	 * @return
	 * @see java.util.List#listIterator(int)
	 */
	public ListIterator listIterator(int index) {
		return results.listIterator(index);
	}

	/**
	 * @param o
	 * @return
	 * @see java.util.List#remove(java.lang.Object)
	 */
	public boolean remove(Object o) {
		return results.remove(o);
	}

	/**
	 * @param index
	 * @return
	 * @see java.util.List#remove(int)
	 */
	public Object remove(int index) {
		return results.remove(index);
	}

	/**
	 * @param c
	 * @return
	 * @see java.util.List#removeAll(java.util.Collection)
	 */
	public boolean removeAll(Collection c) {
		return results.removeAll(c);
	}

	/**
	 * @param c
	 * @return
	 * @see java.util.List#retainAll(java.util.Collection)
	 */
	public boolean retainAll(Collection c) {
		return results.retainAll(c);
	}

	/**
	 * @param index
	 * @param element
	 * @return
	 * @see java.util.List#set(int, java.lang.Object)
	 */
	public Object set(int index, Object element) {
		return results.set(index, element);
	}

	/**
	 * @return
	 * @see java.util.List#size()
	 */
	public int size() {
		return results.size();
	}

	/**
	 * @param fromIndex
	 * @param toIndex
	 * @return
	 * @see java.util.List#subList(int, int)
	 */
	public List subList(int fromIndex, int toIndex) {
		return results.subList(fromIndex, toIndex);
	}

	/**
	 * @return
	 * @see java.util.List#toArray()
	 */
	public Object[] toArray() {
		return results.toArray();
	}

	/**
	 * @param a
	 * @return
	 * @see java.util.List#toArray(T[])
	 */
	public Object[] toArray(Object[] a) {
		return results.toArray(a);
	}
	
	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}