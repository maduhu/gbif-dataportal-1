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
package org.gbif.portal.harvest.workflow.activity.tag;

import javax.sql.DataSource;

import org.gbif.portal.util.workflow.BaseActivity;
import org.gbif.portal.util.workflow.MapContext;
import org.gbif.portal.util.workflow.ProcessContext;

/**
 * Generates the collector tags for the resource.
 * 
 * @author dmartin
 */
public class BaseTagActivity extends BaseActivity {

	protected String contextKeyDataResourceId = "dataResourceId";
	
	// allows for overriding the name when multiple datasources are in use
	DataSource dataSource;
	
	/**
	 * Should be overridden
	 * 
	 * @see org.gbif.portal.util.workflow.Activity#execute(org.gbif.portal.util.workflow.ProcessContext)
	 */
	public ProcessContext execute(ProcessContext processContext) throws Exception {
	  return processContext;
  }
	
	/**
   * @return the contextKeyDataResourceId
   */
  public String getContextKeyDataResourceId() {
  	return contextKeyDataResourceId;
  }

	/**
   * @param contextKeyDataResourceId the contextKeyDataResourceId to set
   */
  public void setContextKeyDataResourceId(String contextKeyDataResourceId) {
  	this.contextKeyDataResourceId = contextKeyDataResourceId;
  }  
  
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		BaseTagActivity launcher = new BaseTagActivity();
		try {
			if(args.length==1){
				Long dataResourceId = Long.parseLong(args[0]);
				MapContext ctx = new MapContext();
				ctx.put(launcher.getContextKeyDataResourceId(), dataResourceId);
			} else {
				launcher.printUsage();
			}
		} catch (Exception e){
			launcher.printUsage();
		}
		System.exit(1);
	}
	
	protected void printUsage(){
		System.out.println("Usage:");
		System.out.println(this.getClass().getName() +" <data-resource-id>");
	}

	protected DataSource getDataSource() {
		return dataSource;
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}


}