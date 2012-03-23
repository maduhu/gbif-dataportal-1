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
package org.gbif.portal.io;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.gbif.portal.model.BaseEnumType;

/**
 * Abstract class for supporting outputs with selected fields only.
 * 
 * Note: These classes are not intended to be spring wired singletons.
 * 
 * @author dmartin
 */
public abstract class SelectableFieldsOutputter implements ResultsOutputter {

	protected static Log logger = LogFactory.getLog(SelectableFieldsOutputter.class);	
	/** Mapping from field names to bean properties */
	protected final Map<String, OutputProperty> outputFieldMapping;
    /**
 	 * A list of property names to download e.g
	 * 
	 * 1) latitude
	 * 2) imageUrl
	 * 3) typeStatus
	 * 4) rawOccurrenceRecord
	 */	
	protected final List<String> selectedFieldNames;
	/** Property helper - for i18n logic, implementation to be web-side, interface portal-service */
	protected PropertyFormatter propertyFormatter;
	/**
	 * 
	 * @param outputFieldMapping mapping of field names to bean names and properties
	 * @param selectedFieldNames the selected fields to output
	 * @param propertyFormatter
	 */
	public SelectableFieldsOutputter(Map<String, OutputProperty> outputFieldMapping, List<String> selectedFieldNames, PropertyFormatter propertyFormatter) {
		this(outputFieldMapping, selectedFieldNames);		
		this.propertyFormatter = propertyFormatter;
	}	
	
	/**
	 * 
	 * @param outputFieldMapping mapping of field names to bean names and properties
	 * @param selectedFieldNames the selected fields to output
	 * @param propertyFormatter
	 */
	public SelectableFieldsOutputter(Map<String, OutputProperty> outputFieldMapping, List<String> selectedFieldNames) {
		this.outputFieldMapping = outputFieldMapping;
		this.selectedFieldNames = selectedFieldNames;
	}	
	
	/**
	 * @see org.gbif.portal.io.ResultsOutputter#write(java.lang.Object)
	 */
	public void write(Map beanMap) throws IOException {
		
		for(String selectedField: selectedFieldNames){
			
			//write out delimiter
			if(selectedFieldNames.indexOf(selectedField)>0)
				writeDelimiter();	
				
			String propertyValue = getSelectedFieldValue(beanMap, selectedField);
			//write out the retrieved value
			writeValue(propertyValue);
		}
		//write out end of record marker		
		writeEndOfRecord();
	}

	protected String getSelectedFieldValue(Map beanMap, String selectedField) {
		String propertyValue = null;
		if(outputFieldMapping.get(selectedField)!=null){
			OutputProperty outputProperty = outputFieldMapping.get(selectedField);
			Object bean = beanMap.get(outputProperty.getBeanName());
			if(bean!=null){
				try {
					String propertyName = StringUtils.capitalize(outputProperty.getPropertyName());
					Method getter = bean.getClass().getMethod("get"+propertyName, (Class[]) null);
					Object nestedProperty = getter.invoke(bean, (Object[]) null);						
					if(nestedProperty!=null && nestedProperty instanceof BaseEnumType){
						propertyValue = ((BaseEnumType) nestedProperty).getName();
					} else if(nestedProperty!=null){
						propertyValue = nestedProperty.toString();
					}
				} catch (Exception e){
					//log and carry on
					logger.error(e.getMessage(), e);
				}
			} else {
				Object property = beanMap.get(outputProperty.getPropertyName());
				if(property!=null)
					propertyValue = property.toString();
			}
		}
		//write out the property value
		if(propertyValue!=null && propertyFormatter!=null){
			propertyValue = propertyFormatter.format(selectedField, propertyValue);
		}
		return propertyValue;
	}

	/**
	 * Writes out this value to the underlying destination.
	 * 
	 * @param value
	 * @throws IOException
	 */
	public abstract void writeValue(String value) throws IOException;

	/**
	 * Writes out this value to the underlying destination.
	 * 
	 * @param value
	 * @throws IOException
	 */
	public abstract void writeDelimiter() throws IOException;
	
	/**
	 * Writes out an end of record marker to the underlying destination.
	 * 
	 * @throws IOException
	 */
	public abstract void writeEndOfRecord() throws IOException;

	/**
	 * @return the outputFieldMapping
	 */
	public Map<String, OutputProperty> getOutputFieldMapping() {
		return outputFieldMapping;
	}

	/**
	 * @return the propertyFormatter
	 */
	public PropertyFormatter getPropertyFormatter() {
		return propertyFormatter;
	}

	/**
	 * @return the requestedFields
	 */
	public List<String> getSelectedFieldNames() {
		return selectedFieldNames;
	}
}