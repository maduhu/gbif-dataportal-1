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
package org.gbif.portal.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.gbif.portal.dao.log.LogMessageDAO;
import org.gbif.portal.dao.log.UserDAO;
import org.gbif.portal.dto.DTOFactory;
import org.gbif.portal.dto.SearchResultsDTO;
import org.gbif.portal.dto.log.LogStatsDTO;
import org.gbif.portal.dto.log.LogStatsDTOFactory;
import org.gbif.portal.dto.log.LoggedActivityDTO;
import org.gbif.portal.dto.occurrence.OccurrenceRecordDTO;
import org.gbif.portal.dto.resources.DataProviderAgentDTO;
import org.gbif.portal.dto.resources.DataProviderDTO;
import org.gbif.portal.dto.resources.DataResourceAgentDTO;
import org.gbif.portal.dto.resources.DataResourceDTO;
import org.gbif.portal.dto.taxonomy.TaxonConceptDTO;
import org.gbif.portal.dto.util.SearchConstraints;
import org.gbif.portal.io.ResultsOutputter;
import org.gbif.portal.model.log.LogMessage;
import org.gbif.portal.model.log.User;
import org.gbif.portal.model.resources.AgentType;
import org.gbif.portal.service.DataResourceManager;
import org.gbif.portal.service.LogManager;
import org.gbif.portal.service.OccurrenceManager;
import org.gbif.portal.service.TaxonomyManager;
import org.gbif.portal.service.util.UserUtils;
import org.gbif.portal.util.log.GbifLogMessage;
import org.gbif.portal.util.log.GbifLogUtils;
import org.gbif.portal.util.log.LogEvent;
import org.gbif.portal.util.log.LogGroup;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;

/**
 * Tools to support GBIF logging
 * 
 * @author Donald Hobern
 */
public class LogManagerImpl implements LogManager {

	/** Logger */
	protected static Log logger = LogFactory.getLog(LogManagerImpl.class);
	
	/** Manager objects	 */
	protected OccurrenceManager occurrenceManager;
	protected TaxonomyManager taxonomyManager;
	protected DataResourceManager dataResourceManager;
	
	protected DTOFactory logMessageDTOFactory;
	
	/** Email addresses for portal contacts	 */
	protected List<String> portalEmailAddresses;
	/** Log utilities */
	protected GbifLogUtils gbifLogUtils;
	
	/** MailSender */
	protected MailSender mailSender;
	
	/** Template messages */
	protected SimpleMailMessage providerTemplateMessage;
	protected SimpleMailMessage userTemplateMessage;
	
	/** DAOs */
	protected UserDAO userDAO;
	protected LogMessageDAO logMessageDAO;	
	
	protected String extractionEventName = "extraction";
	protected String harvestingEventName = "harvesting";
	
	/**
	 * TODO: This is shocking - get this in some startup context
	 * defaults to 1
	 */
	protected long portalInstanceId = 1;
	
	/**
	 * @see org.gbif.portal.service.LogManager#createOccurrenceFeedbackMessage(org.gbif.portal.util.log.LogGroup, java.lang.String, java.lang.String, java.lang.String)
	 */
	public GbifLogMessage createOccurrenceFeedbackMessage(LogGroup logGroup, String userKey, String occurrenceKey, String messageText) {
		GbifLogMessage message = gbifLogUtils.createGbifLogMessage(logGroup, LogEvent.USER_FEEDBACK_OCCURRENCE);
		message.setMessage(messageText);
		message.setRestricted(true);
		
		try {
			OccurrenceRecordDTO record = occurrenceManager.getOccurrenceRecordFor(occurrenceKey);

			if (record != null) {
				message.setOccurrenceId(parseKey(record.getKey()));
				message.setDataProviderId(parseKey(record.getDataProviderKey()));
				message.setDataResourceId(parseKey(record.getDataResourceKey()));
				message.setTaxonConceptId(parseKey(record.getTaxonConceptKey()));
				if (userKey != null) {
					message.setUserId(parseKey(userKey));
				}
			}
			
		} catch (Exception e) {
			logger.warn("Ignoring error", e);
		}
		
		return message;
	}
	
	/**
	 * @see org.gbif.portal.service.LogManager#createResourceFeedbackMessage(org.gbif.portal.util.log.LogGroup, java.lang.String, java.lang.String, java.lang.String)
	 */
	public GbifLogMessage createResourceFeedbackMessage(LogGroup logGroup, String userKey, String dataResourceConceptKey, String messageText) {
		GbifLogMessage message = gbifLogUtils.createGbifLogMessage(logGroup, LogEvent.USER_FEEDBACK_TAXON);
		message.setMessage(messageText);
		message.setRestricted(true);
		
		try {
			DataResourceDTO dto = dataResourceManager.getDataResourceFor(dataResourceConceptKey);

			if (dto != null) {
				message.setDataProviderId(parseKey(dto.getDataProviderKey()));
				message.setDataResourceId(parseKey(dto.getKey()));
				if (userKey != null) {
					message.setUserId(parseKey(userKey));
				}
			}
		} catch (Exception e) {
			logger.warn("Ignoring error", e);
		}
		
		return message;
	}
	
	/**
	 * @see org.gbif.portal.service.LogManager#createTaxonFeedbackMessage(org.gbif.portal.util.log.LogGroup, java.lang.String, java.lang.String, java.lang.String)
	 */
	public GbifLogMessage createTaxonFeedbackMessage(LogGroup logGroup, String userKey, String taxonConceptKey, String messageText) {
		GbifLogMessage message = gbifLogUtils.createGbifLogMessage(logGroup, LogEvent.USER_FEEDBACK_TAXON);
		message.setMessage(messageText);
		message.setRestricted(true);
		
		try {
			TaxonConceptDTO taxon = taxonomyManager.getTaxonConceptFor(taxonConceptKey);

			if (taxon != null) {
				message.setTaxonConceptId(parseKey(taxon.getKey()));
				message.setDataProviderId(parseKey(taxon.getDataProviderKey()));
				message.setDataResourceId(parseKey(taxon.getDataResourceKey()));
				if (userKey != null) {
					message.setUserId(parseKey(userKey));
				}
			}
		} catch (Exception e) {
			logger.warn("Ignoring error", e);
		}
		
		return message;
	}
	
	/**
	 * Will determine if the user is verified
	 * If so then the feedback is sent
	 * If not then a verification message is sent
	 * The message is always logged
	 * @return true if verification message was sent, otherwise false
	 */
	protected boolean sendFeedbackOrVerificationMessages(GbifLogMessage message) {
		boolean verificationSent = false;
		
		// we only send feedback for known users
		if (message.getUserId() == null || message.getUserId()< 1) {
			logger.warn("Ignoring feedback since the user id is not set");
		}
		User user = userDAO.getUserFor(message.getUserId());
		if (user != null) {
			if (!user.isVerified()) {
				sendVerificationMessage(user);
				verificationSent = true;
			} else {
				sendFeedbackMessage(message, user);
			}
		}
		return verificationSent;
	}
	
	/* (non-Javadoc)
	 * @see org.gbif.portal.service.LogManager#sendFeedbackOrVerificationMessages(org.gbif.portal.util.log.GbifLogMessage, boolean)
	 */
	public void sendFeedbackOrVerificationMessages(GbifLogMessage message,
			boolean isVerified) {
		// we only send feedback for known users
		if (message.getUserId() == null || message.getUserId()< 1) {
			logger.warn("Ignoring feedback since the user id is not set");
		}
		User user = userDAO.getUserFor(message.getUserId());
		
		if(isVerified)		
			sendFeedbackMessage(message, user);
		else
			sendVerificationMessage(user);
	}
	
	/* (non-Javadoc)x
	 * @see org.gbif.portal.service.LogManager#isVerifiedUser(java.lang.String)
	 */
	public boolean isVerifiedUser(String userKey) {
		User user = userDAO.getUserFor(parseKey(userKey));
		if(user != null)
			return user.isVerified();
		return false;
	}
	

	/**
	 * @see org.gbif.portal.service.LogManager#endLogGroup(org.gbif.portal.util.log.LogGroup)
	 */
	public boolean endLogGroup(LogGroup logGroup) {
		return gbifLogUtils.endLogGroup(logGroup);
	}

	/**
	 * @see org.gbif.portal.service.LogManager#startLogGroup()
	 */
	public LogGroup startLogGroup() {
		return gbifLogUtils.startLogGroup();
	}

	/**
	 * Parses the supplied key. Returns null if supplied string invalid
	 * @param key
	 * @return a concept key. Returns null if supplied string invalid key
	 */
	protected static Long parseKey(String key){
		Long parsedKey = null;
		try {
			parsedKey = Long.parseLong(key);
		} catch (NumberFormatException e){
			logger.debug("Error parsing key [" + key + "]: " + e.getMessage());
			//expected behaviour for invalid keys
		}
		return parsedKey;
	}
	
	/**
	 * Parses the supplied key. Returns null if supplied string invalid
	 * @param key
	 * @return a concept key. Returns null if supplied string invalid key
	 */
	protected static Integer parseEventKey(String key){
		Integer parsedKey = null;
		try {
			parsedKey = Integer.parseInt(key);
		} catch (NumberFormatException e){
			logger.debug("Error parsing key [" + key + "]: " + e.getMessage());
			//expected behaviour for invalid keys
		}
		return parsedKey;
	}	
	
	/**
	 * Sends a log message to the appropriate recipient
	 * 
	 * TODO Should really make use of velocity templating
	 *  
	 * @param message GbifLogMessage 
	 */
	protected void sendFeedbackMessage(GbifLogMessage message, User user) {
		// for safety
		if (user == null) {
			return;
		}
		
		List<String> toEmailAddresses = new ArrayList<String>();
		List<String> ccEmailAddresses = new ArrayList<String>();
		
		if (message.getDataResourceId() != 0) {
			String dataResourceKey = new Long(message.getDataResourceId()).toString();
			List<DataResourceAgentDTO> agents = dataResourceManager.getAgentsForDataResource(dataResourceKey);
			if (agents != null) {
				for (DataResourceAgentDTO agent : agents) {
					if (agent.getAgentType() == AgentType.DATAADMINISTRATOR.getValue()) {
						toEmailAddresses.add(processEmail(agent.getAgentEmail()));
					} else {
						ccEmailAddresses.add(processEmail(agent.getAgentEmail()));
					}
				}
				
				if (toEmailAddresses.size() == 0 && ccEmailAddresses.size() > 0) {
					toEmailAddresses = ccEmailAddresses;
					ccEmailAddresses = new ArrayList<String>();
				}
			}
		}
		
		if (toEmailAddresses.size() == 0 && message.getDataProviderId() != 0) {
			String dataProviderKey = new Long(message.getDataProviderId()).toString();
			List<DataProviderAgentDTO> agents = dataResourceManager.getAgentsForDataProvider(dataProviderKey);
			if (agents != null) {
				for (DataProviderAgentDTO agent : agents) {
					if (agent.getAgentType() == AgentType.DATAADMINISTRATOR.getValue()) {
						toEmailAddresses.add(processEmail(agent.getAgentEmail()));
					} else {
						ccEmailAddresses.add(processEmail(agent.getAgentEmail()));
					}
				}
				
				if (toEmailAddresses.size() == 0 && ccEmailAddresses.size() > 0) {
					toEmailAddresses = ccEmailAddresses;
					ccEmailAddresses = new ArrayList<String>();
				}
			}
		}
		
		if (toEmailAddresses.size() != 0) {
			ccEmailAddresses.addAll(portalEmailAddresses);
			ccEmailAddresses.removeAll(toEmailAddresses);
			
	        SimpleMailMessage providerMessage = new SimpleMailMessage(providerTemplateMessage);
	        providerMessage.setTo(toEmailAddresses.toArray(new String[toEmailAddresses.size()]));
	        //user that submitted feedback should also be CCed
	        if(user.getEmail()!=null) {
	          ccEmailAddresses.add(processEmail(user.getEmail()));
	        }
	        providerMessage.setCc(ccEmailAddresses.toArray(new String[ccEmailAddresses.size()]));
	        
	        // TODO - NLS and portal URL
	        StringBuffer subjectBuffer = new StringBuffer();

	        SimpleMailMessage userMessage = new SimpleMailMessage(userTemplateMessage);
	        userMessage.setTo(user.getEmail());
	        
	        subjectBuffer.append("Feedback from GBIF Data Portal - ");

	        StringBuffer textBuffer = new StringBuffer();
	        textBuffer.append("The following message was submitted through the GBIF Data Portal.\n\n");
	        
	        if (user != null) {
		        textBuffer.append("  User: ");
		        textBuffer.append(user.getName());
		        textBuffer.append(" (");
		        textBuffer.append(user.getEmail());
		        textBuffer.append(")\n\n");
	        }

	        if (message.getDataProviderId()!=null &&  message.getDataProviderId()!=0) {
	        	try {
	        		DataProviderDTO dto = dataResourceManager.getDataProviderFor(new Long(message.getDataProviderId()).toString());
		        	textBuffer.append("  Data publisher: ");
		        	textBuffer.append(dto.getName());
			        textBuffer.append("\n");
		        	
		        	textBuffer.append("  Portal URL: http://data.gbif.org/datasets/provider/");
		        	textBuffer.append(dto.getKey());
			        textBuffer.append("\n\n");
	        	} catch(Exception e) {
	        		// ignore
	        	}
	        }

	        if (message.getDataResourceId() != null && message.getDataResourceId() != 0) {
	        	try {
	        		DataResourceDTO dto = dataResourceManager.getDataResourceFor(new Long(message.getDataResourceId()).toString());
		        	textBuffer.append("  Data resource: ");
		        	textBuffer.append(dto.getName());
			        textBuffer.append("\n");
		        	
		        	textBuffer.append("  Portal URL: http://data.gbif.org/datasets/resource/");
		        	textBuffer.append(dto.getKey());
			        textBuffer.append("\n\n");
	        	} catch(Exception e) {
	        		// ignore
	        	}
	        }

	        if (message.getOccurrenceId() != null && message.getOccurrenceId() != 0) {
	        	try {
	        		OccurrenceRecordDTO dto = occurrenceManager.getOccurrenceRecordFor(new Long(message.getOccurrenceId()).toString());

	    	        subjectBuffer.append("catalogue number: ");
	    	        subjectBuffer.append(dto.getCatalogueNumber());
	    	        subjectBuffer.append(" ");
	        		
	        		textBuffer.append("  Occurrence record: ");
		        	textBuffer.append(dto.getInstitutionCode());
		        	textBuffer.append(" / ");
		        	textBuffer.append(dto.getCollectionCode());
		        	textBuffer.append(" / ");
		        	textBuffer.append(dto.getCatalogueNumber());
			        textBuffer.append("\n");
		        	
		        	textBuffer.append("  Portal URL: http://data.gbif.org/occurrences/");
		        	textBuffer.append(dto.getKey());
			        textBuffer.append("\n\n");
	        	} catch(Exception e) {
	        		// ignore
	        	}
	        }
	        
	        if (message.getTaxonConceptId() != null && message.getTaxonConceptId() != 0) {
	        	try {
	        		TaxonConceptDTO dto = taxonomyManager.getTaxonConceptFor(new Long(message.getTaxonConceptId()).toString());

	    	        subjectBuffer.append("taxon: ");
	    	        subjectBuffer.append(dto.getTaxonName());
	    	        
	    	        textBuffer.append("  Taxon: ");
		        	textBuffer.append(dto.getTaxonName());
			        textBuffer.append("\n");
		        	
		        	textBuffer.append("  Portal URL: http://data.gbif.org/species/");
		        	textBuffer.append(dto.getKey());
			        textBuffer.append("\n\n");
	        	} catch(Exception e) {
	        		// ignore
	        	}
	        }
	        
	        textBuffer.append(message.getMessage());
	        
	        providerMessage.setSubject(subjectBuffer.toString());
	        providerMessage.setText(textBuffer.toString());
	        
	        userMessage.setSubject(subjectBuffer.toString());
	        userMessage.setText("Thank you for your feedback.  The message has " +
	        		            "been sent on your behalf to the appropriate data publisher.\n\n");

	        try{
	            mailSender.send(providerMessage);
	            mailSender.send(userMessage);
	        }
	        catch(MailException e) {
	            // simply log it and go on...
	            logger.error("Couldn't send message", e);            
	        }
		}
	}

	/**
	 * Sends a verification message to the User given
	 * 
	 * TODO Should really make use of velocity templating
	 *  
	 * @param user to send to 
	 */
	protected void sendVerificationMessage(User user) {
		if (user != null) {
			SimpleMailMessage verificationMessage = new SimpleMailMessage(userTemplateMessage);
			verificationMessage.setTo(user.getEmail());
			verificationMessage.setSubject("Confirm e-mail address for GBIF Data Portal");
			// todo
			verificationMessage.setText("Please visit the following link to confirm your e-mail address:\n" +
					"http://data.gbif.org/feedback/verification/" +
					user.getId() + "/" + UserUtils.getCodeFor(user.getName(), user.getEmail()));			
			try{
	            mailSender.send(verificationMessage);
	        }
	        catch(MailException e) {
	            // simply log it and go on...
	            logger.error("Couldn't send message", e);            
	        }
		}
	}
	
	/**
	 * Clean email strings to replace bogus text with @ and .
	 * @param email
	 * @return
	 */
	private String processEmail(String email) {
		if (email != null)
		{
			StringTokenizer st = new StringTokenizer(email, " \t\n\r\f(){}[]");
			
			if (st.countTokens() > 1) {
				StringBuffer sb = new StringBuffer();
				
				while (st.hasMoreTokens())
				{
					String token = st.nextToken();
	
					if (token.toLowerCase().replaceAll("[^a-z]", "").equals("at"))
					{
						token = "@";
					} else if (token.toLowerCase().replaceAll("[^a-z]", "").equals("dot"))
					{
						token = ".";
					}
					
					sb.append(token);
				}
				
				email = sb.toString();
			}
		}
		
		return email;
	}

	/**
	 * @see org.gbif.portal.service.LogManager#findLogMessagesFor(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.util.Date, org.gbif.portal.dto.util.SearchConstraints)
	 */
	public SearchResultsDTO findLogMessagesFor(String dataProviderKey, String dataResourceKey, String userKey, String occurrenceKey, String taxonConceptKey,  String eventKey, String minEventKey, String maxEventKey, Long minLogLevel, String logGroupKey, Date startDate, Date endDate, SearchConstraints searchConstraints) {
		List<LogMessage> messages = logMessageDAO.findMessages(parseKey(dataProviderKey), parseKey(dataResourceKey), parseKey(userKey), parseKey(occurrenceKey), parseKey(taxonConceptKey), parseEventKey(eventKey), parseEventKey(minEventKey), parseEventKey(maxEventKey), minLogLevel, parseKey(logGroupKey), startDate, endDate, searchConstraints.getStartIndex(), searchConstraints.getMaxResults()+1);
		return logMessageDTOFactory.createResultsDTO(messages, searchConstraints.getMaxResults());
	}	

	/**
	 * @see org.gbif.portal.service.LogManager#findLogMessagesFor(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.util.Date, org.gbif.portal.dto.util.SearchConstraints)
	 */
	public int countLogMessagesFor(String dataProviderKey, String dataResourceKey, String userKey, String occurrenceKey, String taxonConceptKey,  String eventKey, String minEventKey, String maxEventKey, Long minLogLevel, String logGroupKey, Date startDate, Date endDate, SearchConstraints searchConstraints) {
		return logMessageDAO.countMessages(parseKey(dataProviderKey), parseKey(dataResourceKey), parseKey(userKey), parseKey(occurrenceKey), parseKey(taxonConceptKey), parseEventKey(eventKey), parseEventKey(minEventKey), parseEventKey(maxEventKey), minLogLevel, parseKey(logGroupKey), startDate, endDate, searchConstraints.getStartIndex(), searchConstraints.getMaxResults()+1);
	}	

	/**
	 * @see org.gbif.portal.service.LogManager#createOccurrenceFeedbackMessage(org.gbif.portal.util.log.LogGroup, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	public GbifLogMessage createOccurrenceFeedbackMessage(LogGroup logGroup, String userName, String userEmail, String occurrenceKey, String messageText) {
		String userKey = getUserKeyFor(userName, userEmail);
		return createOccurrenceFeedbackMessage(logGroup, userKey, occurrenceKey, messageText);
	}

	/**
	 * @see org.gbif.portal.service.LogManager#createTaxonFeedbackMessage(org.gbif.portal.util.log.LogGroup, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	public GbifLogMessage createTaxonFeedbackMessage(LogGroup logGroup, String userName, String userEmail, String taxonConceptKey, String messageText) {
		String userKey = getUserKeyFor(userName, userEmail);
		return createTaxonFeedbackMessage(logGroup, userKey, taxonConceptKey, messageText);
	}

	/**
	 * @see org.gbif.portal.service.LogManager#createResourceFeedbackMessage(org.gbif.portal.util.log.LogGroup, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	public GbifLogMessage createResourceFeedbackMessage(LogGroup logGroup, String userName, String userEmail, String dataResourceKey, String messageText) {
		String userKey = getUserKeyFor(userName, userEmail);
		return createResourceFeedbackMessage(logGroup, userKey, dataResourceKey, messageText);
	}
	
	/** 
	 * @see org.gbif.portal.service.LogManager#authoriseUser(java.lang.String, java.lang.String)
	 */
	public String authoriseUser(String key, String code) {
		User user = userDAO.getUserFor(parseKey(key));
		if (user != null) {
			boolean verifiedCode = UserUtils.isValidCodeCodeFor(user.getName(), user.getEmail(), code);
			if (verifiedCode) {
				user.setVerified(true);
				userDAO.updateUser(user);
				
				List<LogMessage> messagesToSend = logMessageDAO.findMessages(null, null, user.getId(), null, null, null, null, null, null, null, null, null, 0, 1000);
				logger.info("User has " + messagesToSend.size() + " messages pending...");
				for (LogMessage message : messagesToSend) {
					GbifLogMessage gbifMessage = new GbifLogMessage(message.getId(), message.getPortalInstanceId(), message.getLogGroupId(),
							message.getEventId().intValue(), message.getLevel().intValue(), message.getDataProviderId(),
							message.getDataResourceId(), message.getOccurrenceId(),
							message.getTaxonConceptId(), message.getUserId(), message.getMessage(),
							true, message.getCount().intValue(), message.getTimestamp());
					logger.debug("Sending pending message");
					sendFeedbackMessage(gbifMessage, user);
				}				
			}
			return user.getName();
		}
		
		return null;
	}

	/**
	 * @see org.gbif.portal.service.LogManager#formatLogMessagesFor(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.Long, java.lang.String, java.util.Date, java.util.Date, org.gbif.portal.dto.util.SearchConstraints, org.gbif.portal.io.ResultsOutputter)
	 */
	public void formatLogMessagesFor(String dataProviderKey, 
			String dataResourceKey, String userKey, String occurrenceKey, String taxonConceptKey, 
			String eventKey, String minEventKey, String maxEventKey, Long minLogLevel, String logGroupKey, 
			Date startDate, Date endDate, SearchConstraints searchConstraints, ResultsOutputter resultsOutputter) throws IOException{
	
		logMessageDAO.outputMessages(resultsOutputter, 
				parseKey(dataProviderKey), 
				parseKey(dataResourceKey), 
				parseKey(userKey), 
				parseKey(occurrenceKey), 
				parseKey(taxonConceptKey), 
				parseEventKey(eventKey), 
				parseEventKey(minEventKey), 
				parseEventKey(maxEventKey), 
				minLogLevel, 
				parseKey(logGroupKey), 
				startDate, 
				endDate, 
				searchConstraints.getStartIndex(), 
				searchConstraints.getMaxResults());
	}
	
	/**
	 * @see org.gbif.portal.service.LogManager#getDataResourceLogStatsFor(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.Long, java.lang.String, java.util.Date, java.util.Date)
	 */
	@SuppressWarnings("unchecked")
	public List<LogStatsDTO> getDataResourceLogStatsFor(String dataProviderKey, String dataResourceKey, String userKey, String occurrenceKey, String taxonConceptKey, String eventKey, String minEventKey, String maxEventKey, Long minLogLevel, String logGroupKey, Date startDate, Date endDate) throws IOException {
		List<Object[]> results = logMessageDAO.countMessagesByDataResource(parseKey(dataProviderKey), parseKey(dataResourceKey), parseKey(userKey), parseKey(occurrenceKey), parseKey(taxonConceptKey), parseEventKey(eventKey), parseEventKey(minEventKey), parseEventKey(maxEventKey), minLogLevel, parseKey(logGroupKey), startDate, endDate);
		LogStatsDTOFactory lsdf = new LogStatsDTOFactory();
		return lsdf.createDTOList(results);
	}

	/**
	 * Gets or creates the user for the portal 
	 * @param name
	 * @param email
	 * @return
	 */
	public String getUserKeyFor(String name, String email) {
		User user = userDAO.findUser(portalInstanceId, name, email);
		if (user == null) {
			if(logger.isInfoEnabled())
				logger.info("Creating new user: name[" + name + "] email[" + email + "] portalInstanceId[" + portalInstanceId+ "]");
			user = new User();
			user.setName(name);
			user.setEmail(email);
			user.setPortalInstanceId(portalInstanceId);
			userDAO.createUser(user);
			if(logger.isInfoEnabled())
				logger.info("User created: name[" + name + "] email[" + email + "] portalInstanceId[" + portalInstanceId+ "] id[" + user.getId() + "]");
		} else {
			if(logger.isInfoEnabled())
				logger.info("User already exists: name[" + name + "] email[" + email + "] portalInstanceId[" + portalInstanceId+ "] id[" + user.getId() + "]");
		}
		return "" + user.getId();
	}
	
	/**
	 * @see org.gbif.portal.service.LogManager#getExtractionHistory(java.lang.String, java.lang.String)
	 */
	public List<LoggedActivityDTO> getExtractionHistory(String providerKey,
			String resourceKey) {
		//add list of events ids - start and end harvest events
		List<Integer> eventIds = new ArrayList<Integer>();
		eventIds.add(LogEvent.EXTRACT_BEGIN.getValue());
		eventIds.add(LogEvent.EXTRACT_END.getValue());
		
		List<LogMessage> logMessages = logMessageDAO.getLogMessagesForEventIds(parseKey(providerKey), parseKey(resourceKey), eventIds);
		Map<Long, LoggedActivityDTO> indexingHistory = new LinkedHashMap<Long, LoggedActivityDTO>();		
		
		//use log group 
		for (LogMessage lm: logMessages){
			//is this a harvesting message?
			if(lm.getEventId().intValue()==LogEvent.EXTRACT_BEGIN.getValue() ||
					lm.getEventId().intValue()==LogEvent.EXTRACT_END.getValue()							
				){
				LoggedActivityDTO laDTO = indexingHistory.get(lm.getLogGroupId());
				populateActivity(indexingHistory, lm, LogEvent.EXTRACT_BEGIN, extractionEventName, laDTO);
			}
		}
		//sort the activities chronologically
		return sortActivities(indexingHistory);
	}

	/**
	 * @see org.gbif.portal.service.LogManager#getHarvestingHistory(java.lang.String, java.lang.String)
	 */
	public List<LoggedActivityDTO> getHarvestingHistory(String providerKey, String resourceKey) {
		
		//add list of events ids - start and end harvest events
		List<Integer> eventIds = new ArrayList<Integer>();
		eventIds.add(LogEvent.HARVEST_BEGIN.getValue());
		eventIds.add(LogEvent.HARVEST_END.getValue());
		
		List<LogMessage> logMessages = logMessageDAO.getLogMessagesForEventIds(parseKey(providerKey), parseKey(resourceKey), eventIds);
		Map<Long, LoggedActivityDTO> indexingHistory = new LinkedHashMap<Long, LoggedActivityDTO>();		
		
		//use log group 
		for (LogMessage lm: logMessages){
			//is this a harvesting message?
			if(lm.getEventId().intValue()==LogEvent.HARVEST_BEGIN.getValue() ||
					lm.getEventId().intValue()==LogEvent.HARVEST_END.getValue()							
				){
				LoggedActivityDTO laDTO = indexingHistory.get(lm.getLogGroupId());
				populateActivity(indexingHistory, lm, LogEvent.HARVEST_BEGIN, harvestingEventName, laDTO);
			}
		}
		//sort the activities chronologically
		return sortActivities(indexingHistory);
	}
	
	/**
	 * Populate the activity.
	 * 
	 * @param indexingHistory
	 * @param lm
	 * @param startEvent
	 * @param activityName
	 * @param laDTO
	 */
	private void populateActivity(Map<Long, LoggedActivityDTO> indexingHistory,
			LogMessage lm, LogEvent startEvent, String activityName, LoggedActivityDTO laDTO) {
		if(laDTO==null){
			laDTO = new LoggedActivityDTO();
			laDTO.setLogGroup(lm.getLogGroupId());
			laDTO.setEventName(activityName);
			try {
				if(lm.getDataProviderId()!=null){
					laDTO.setDataProviderKey(lm.getDataProviderId().toString());
					laDTO.setDataProviderName(lm.getDataProvider().getName());
				}
				if(lm.getDataResourceId()!=null){
					laDTO.setDataResourceKey(lm.getDataResourceId().toString());
					laDTO.setDataResourceName(lm.getDataResource().getName());
				}
			} catch (Exception e){
				logger.debug(e.getMessage(), e);
			}
			indexingHistory.put(lm.getLogGroupId(), laDTO);
		}
		
		if(lm.getEventId().intValue()==startEvent.getValue()){
			laDTO.setStartDate(lm.getTimestamp());
		} else {
			laDTO.setEndDate(lm.getTimestamp());
		}
	}

	/**
	 * Sort the indexing history chronologically.
	 * @param indexingHistory
	 * @return
	 */
	private List<LoggedActivityDTO> sortActivities(
			Map<Long, LoggedActivityDTO> indexingHistory) {
		//put into a list
		List<LoggedActivityDTO> activities = new ArrayList<LoggedActivityDTO>();
		Set<Long> logGroupIds = indexingHistory.keySet();
		for(Long logGroupId: logGroupIds){
			//set the duration in seconds
			LoggedActivityDTO laDTO = indexingHistory.get(logGroupId);
			if(laDTO.getStartDate()!=null && laDTO.getEndDate()!=null){
				laDTO.setDurationInMillisecs(laDTO.getEndDate().getTime() - laDTO.getStartDate().getTime());
			}
			activities.add(indexingHistory.get(logGroupId));
		}
		
		//sort by provider, resource, and then chronologically
		Collections.sort(activities, new Comparator<LoggedActivityDTO>(){
			public int compare(LoggedActivityDTO o1, LoggedActivityDTO o2) {
				if(o1!=null && o2!=null){
					if(o1.getDataProviderKey()!=null && o2.getDataProviderKey()!=null && !o1.getDataProviderKey().equals(o2.getDataProviderKey())){
						return o1.getDataProviderKey().compareTo(o2.getDataProviderKey());
					}
					if(o1.getDataResourceKey()!=null && o2.getDataResourceKey()!=null && !o1.getDataResourceKey().equals(o2.getDataResourceKey())){
						return o1.getDataResourceKey().compareTo(o2.getDataResourceKey());
					}
					if(o1.getLogGroup()!=null && o2.getLogGroup()!=null){
						return o1.getLogGroup().compareTo(o2.getLogGroup());
					}
				}
				return -1;
			}
		});
		return activities;
	}	

	/**
	 * @see org.gbif.portal.service.LogManager#getIndexingHistory(java.lang.String, java.lang.String)
	 */
	public List<LoggedActivityDTO> getIndexingHistory(String providerKey, String resourceKey) {

		List<Integer> eventIds = new ArrayList<Integer>();
		eventIds.add(LogEvent.HARVEST_BEGIN.getValue());
		eventIds.add(LogEvent.HARVEST_END.getValue());
		eventIds.add(LogEvent.EXTRACT_BEGIN.getValue());
		eventIds.add(LogEvent.EXTRACT_END.getValue());
		
		//retrieve log messages for these events
		List<LogMessage> logMessages = logMessageDAO.getLogMessagesForEventIds(parseKey(providerKey), parseKey(resourceKey), eventIds);
		Map<Long, LoggedActivityDTO> indexingHistory = new LinkedHashMap<Long, LoggedActivityDTO>();		
		
		//use log group 
		for (LogMessage lm: logMessages){
			//is this a harvesting message?
			if(lm.getEventId().intValue()==LogEvent.HARVEST_BEGIN.getValue() ||
					lm.getEventId().intValue()==LogEvent.HARVEST_END.getValue()							
				){
				LoggedActivityDTO laDTO = indexingHistory.get(lm.getLogGroupId());
				populateActivity(indexingHistory, lm, LogEvent.HARVEST_BEGIN, harvestingEventName, laDTO);
			}
			//is this a extraction message?			
			if(lm.getEventId().intValue()==LogEvent.EXTRACT_BEGIN.getValue() ||
					lm.getEventId().intValue()==LogEvent.EXTRACT_END.getValue()){
				LoggedActivityDTO laDTO = indexingHistory.get(lm.getLogGroupId());
				populateActivity(indexingHistory, lm, LogEvent.EXTRACT_BEGIN, extractionEventName, laDTO);			}
		}
		
		//put into a list and sort
		return sortActivities(indexingHistory);
	}	
	
	/**
	 * @see org.gbif.portal.service.LogManager#getEarliestLogMessageDate()
	 */
	public Date getEarliestLogMessageDate() {
		return logMessageDAO.getEarliestLogMessageDate();
	}	
	
	/**
	 * @see org.gbif.portal.service.LogManager#getLatestLogMessageDate()
	 */
	public Date getLatestLogMessageDate() {
		return logMessageDAO.getLatestLogMessageDate();	
	}	
	
	/**
	 * @param gbifLogUtils the gbifLogUtils to set
	 */
	public void setGbifLogUtils(GbifLogUtils gbifLogUtils) {
		this.gbifLogUtils = gbifLogUtils;
	}

	/**
	 * @param occurrenceManager the occurrenceManager to set
	 */
	public void setOccurrenceManager(OccurrenceManager occurrenceManager) {
		this.occurrenceManager = occurrenceManager;
	}

	/**
	 * @param taxonomyManager the taxonomyManager to set
	 */
	public void setTaxonomyManager(TaxonomyManager taxonomyManager) {
		this.taxonomyManager = taxonomyManager;
	}

	/**
	 * @param dataResourceManager the dataResourceManager to set
	 */
	public void setDataResourceManager(DataResourceManager dataResourceManager) {
		this.dataResourceManager = dataResourceManager;
	}

	/**
	 * @param portalEmailAddresses the portalEmailAddresses to set
	 */
	public void setPortalEmailAddresses(List<String> portalEmailAddresses) {
		this.portalEmailAddresses = portalEmailAddresses;
	}

	/**
	 * @param mailSender the mailSender to set
	 */
	public void setMailSender(MailSender mailSender) {
		this.mailSender = mailSender;
	}

	/**
	 * @param providerTemplateMessage the providerTemplateMessage to set
	 */
	public void setProviderTemplateMessage(SimpleMailMessage providerTemplateMessage) {
		this.providerTemplateMessage = providerTemplateMessage;
	}

	/**
	 * @param userTemplateMessage the userTemplateMessage to set
	 */
	public void setUserTemplateMessage(SimpleMailMessage userTemplateMessage) {
		this.userTemplateMessage = userTemplateMessage;
	}

	/**
	 * @param logMessageDAO the logMessageDAO to set
	 */
	public void setLogMessageDAO(LogMessageDAO logMessageDAO) {
		this.logMessageDAO = logMessageDAO;
	}

	/**
	 * @param logMessageDTOFactory the logMessageDTOFactory to set
	 */
	public void setLogMessageDTOFactory(DTOFactory logMessageDTOFactory) {
		this.logMessageDTOFactory = logMessageDTOFactory;
	}

	/**
	 * @return Returns the portalInstanceId.
	 */
	public long getPortalInstanceId() {
		return portalInstanceId;
	}

	/**
	 * @param portalInstanceId The portalInstanceId to set.
	 */
	public void setPortalInstanceId(long portalInstanceId) {
		this.portalInstanceId = portalInstanceId;
	}

	/**
	 * @return Returns the userDAO.
	 */
	public UserDAO getUserDAO() {
		return userDAO;
	}

	/**
	 * @param userDAO The userDAO to set.
	 */
	public void setUserDAO(UserDAO userDAO) {
		this.userDAO = userDAO;
	}

	/**
	 * @param extractionEventName the extractionEventName to set
	 */
	public void setExtractionEventName(String extractionEventName) {
		this.extractionEventName = extractionEventName;
	}

	/**
	 * @param harvestingEventName the harvestingEventName to set
	 */
	public void setHarvestingEventName(String harvestingEventName) {
		this.harvestingEventName = harvestingEventName;
	}
}