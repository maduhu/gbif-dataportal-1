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
package org.gbif.portal.harvest.workflow.activity.taxonomy;

import java.util.List;

import org.gbif.portal.model.TaxonName;
import org.gbif.portal.util.workflow.Activity;
import org.gbif.portal.util.workflow.BaseActivity;
import org.gbif.portal.util.workflow.ProcessContext;

/**
 * An activity that will Log the classification at info level
 * 
 * @author trobertson
 */
public class ClassificationLoggerActivity extends BaseActivity implements Activity {	
		/**
		 * Context Keys
		 */
		protected String contextKeyClassificationList;
		
		/**
		 * @see org.gbif.portal.util.workflow.Activity#execute(org.gbif.portal.util.workflow.ProcessContext)
		 */
		@SuppressWarnings("unchecked")
		public ProcessContext execute(ProcessContext context) throws Exception {
			List<TaxonName> classification = (List<TaxonName>) context.get(getContextKeyClassificationList(), List.class, true);
			StringBuffer sb = new StringBuffer("Classification:");
			for (TaxonName tn : classification) {
				sb.append("\n - " + tn.toString());
			}
			logger.info(sb.toString());
			
			return context;
		}

		/**
		 * @return Returns the contextKeyClassificationList.
		 */
		public String getContextKeyClassificationList() {
			return contextKeyClassificationList;
		}

		/**
		 * @param contextKeyClassificationList The contextKeyClassificationList to set.
		 */
		public void setContextKeyClassificationList(String contextKeyClassificationList) {
			this.contextKeyClassificationList = contextKeyClassificationList;
		}

	}
