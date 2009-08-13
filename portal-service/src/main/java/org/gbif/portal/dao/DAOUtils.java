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
package org.gbif.portal.dao;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.gbif.portal.io.ChainableResultsOutputter;
import org.gbif.portal.io.ResultsOutputter;
import org.hibernate.Query;
import org.hibernate.ScrollMode;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;

/**
 * Utility code for DAO methods/
 * 
 * @author dmartin
 */
public class DAOUtils {
	
	protected static Log logger = LogFactory.getLog(DAOUtils.class);

	/**
	 * Process results, scrolling through each record in turn only loading that specific record.
	 * 
	 * @param resultsOutputter
	 * @param session
	 * @param query
	 * @param associationTraverser
	 * @param batchSize
	 * @throws IOException
	 */
	public static void scrollResults(final ResultsOutputter resultsOutputter, Session session, Query query, AssociationTraverser associationTraverser, int batchSize) throws IOException {
		query.setReadOnly(true);
		query.setFetchSize(batchSize);
		
		//Using scrollable results to prevent initiation of all model objects
		ScrollableResults sr = query.scroll(ScrollMode.FORWARD_ONLY);
		
		//go to beginning of resultset
		boolean isNotEmpty = sr.first();
		if(!isNotEmpty){
			//this is necessary due to a bug with ScrollableResults
			//allowing continuous scrolling over an empty resultset. genius.
			return;
		}

		//iterate through the results
		processScrollableResults(resultsOutputter, session, sr, associationTraverser, batchSize);

		//check for a chain
		if(resultsOutputter instanceof ChainableResultsOutputter){
			ChainableResultsOutputter cro = (ChainableResultsOutputter) resultsOutputter;
			ResultsOutputter nextResultsOutputter = cro.getNextResultsOutputter();
			while(nextResultsOutputter!=null && !cro.isChainInOnePass()){
				//back to the start
				sr.first();
				processScrollableResults(nextResultsOutputter, session, sr, associationTraverser, batchSize);
				if(resultsOutputter instanceof ChainableResultsOutputter){
					cro = (ChainableResultsOutputter) resultsOutputter;
					if(!cro.isChainInOnePass())
						nextResultsOutputter = cro.getNextResultsOutputter();
					else
						nextResultsOutputter=null;
				} else {
					nextResultsOutputter=null;
				}
			}
		}
		if(associationTraverser!=null)
			associationTraverser.reset();
		//close the results set
		sr.close();
	}
	
	/**
	 * 
	 * @param resultsOutputter
	 * @param session
	 * @param sr
	 * @param associationTraverser
	 * @param batchSize
	 * @throws IOException
	 */
	public static void processScrollableResults(final ResultsOutputter resultsOutputter, Session session, ScrollableResults sr, AssociationTraverser associationTraverser, int batchSize) throws IOException {
		
		//indicate end of resultset
		boolean eor = false;		
		int batchNo = 0;
		do {
			if(logger.isDebugEnabled()){
				logger.debug("Running batch: "+(batchNo++));
			}
			
			if(associationTraverser!=null)
				associationTraverser.batchPreprocess(batchSize, sr, session);
			
			//process in batches
			for(int i=0; i<batchSize && !eor; i++){
			
				Object record = sr.get();
				Map beanMap = null;
				
				//assemble all required model objects for rendering a single row
				if(associationTraverser!=null){
					beanMap = associationTraverser.traverse(record, session);
				} else {
					beanMap = new HashMap<String, Object>();
					if(record!=null && record instanceof Object[] && ((Object[])record).length>0){
						beanMap.put("record", ((Object[])record)[0]);
					} else {
						beanMap.put("record", record);
					}
				}
				
				//write out result
				resultsOutputter.write(beanMap);
				
				if(beanMap!=null){
					//evict from the session to keep memory footprint down
					for(Object recordElement: beanMap.entrySet()){
						session.evict(recordElement);
					}
					beanMap = null;					
				}

				//check to see if this is the last element in resultset
				if(sr.isLast()){
					eor = true;
				} else {
					sr.next();
				}
			}

			//post process
			if(associationTraverser!=null)
				associationTraverser.batchPostprocess(batchSize, sr, session);

			//flush between batches - to remove objects from the session
			session.flush();
			session.clear();
		} while (!eor);
	}	
}