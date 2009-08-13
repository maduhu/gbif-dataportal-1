/**
 * 
 */
package org.gbif.portal.web.util;

import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.gbif.portal.util.log.GbifLogMessage;
import org.gbif.portal.util.log.LogGroup;

/**
 * Utilities for Logging user events
 * 
 * @author dmartin
 */
public class UserUtils {
	
	/** set of agents allowed to get in cookie free **/
	protected Set<String> robotAgents;	

	/**
	 * Log this message, using the session log group id.
	 * 
	 * @param request
	 * @return
	 */
	public void logUsage (org.apache.commons.logging.Log logger, GbifLogMessage message,  HttpServletRequest request ){

		//if the user agent is google or the like, dont log
		String userAgent = request.getHeader("User-Agent");
		for (String agentToIgnore : robotAgents) {
			if (userAgent.toUpperCase().contains(agentToIgnore.toUpperCase())) {
				if(logger.isDebugEnabled())
					logger.debug("avoiding logging for user agent: " + userAgent);
				return;
			}
		}		
		
		LogGroup logGroup = new LogGroup();
		Long logGroupId = (Long) request.getSession().getAttribute("logGroupId");
		if(logGroupId!=null){
			logGroup.setId(logGroupId);
		}		
		// TODO - get from a context perhaps?
		message.setPortalInstanceId(1L);
		
		//log the message
		logger.info(message);
		
		//store the log id in the session if new
		if(logGroupId==null && logGroup!=null && logGroup.getId()!= LogGroup.UNINITIALISED){
			request.getSession().setAttribute("logGroupId", logGroup.getId());
		}		
	}

	/**
	 * @param robotAgents the robotAgents to set
	 */
	public void setRobotAgents(Set<String> robotAgents) {
		this.robotAgents = robotAgents;
	}
}