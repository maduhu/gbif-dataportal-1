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
package org.gbif.portal.harvest.workflow.activity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.gbif.portal.dao.IdentifierRecordDAO;
import org.gbif.portal.dao.ImageRecordDAO;
import org.gbif.portal.dao.LinkRecordDAO;
import org.gbif.portal.dao.RawOccurrenceRecordDAO;
import org.gbif.portal.dao.TypificationRecordDAO;
import org.gbif.portal.harvest.workflow.activity.identification.Identification;
import org.gbif.portal.model.IdentifierRecord;
import org.gbif.portal.model.ImageRecord;
import org.gbif.portal.model.LinkRecord;
import org.gbif.portal.model.RawOccurrenceRecord;
import org.gbif.portal.model.TypificationRecord;
import org.gbif.portal.util.db.OccurrenceRecordUtils;
import org.gbif.portal.util.log.GbifLogUtils;
import org.gbif.portal.util.log.LogEvent;
import org.gbif.portal.util.text.DateFormatter;
import org.gbif.portal.util.text.InvalidDateException;
import org.gbif.portal.util.workflow.Activity;
import org.gbif.portal.util.workflow.BaseActivity;
import org.gbif.portal.util.workflow.ProcessContext;

/**
 * An activity that will synchronise a RawOccurrenceRecord with the values found in the 
 * context.
 * @author trobertson
 */
public class RawOccurrenceRecordSynchroniserActivity extends BaseActivity implements
		Activity {
	/**
	 * The daos 
	 */
	protected RawOccurrenceRecordDAO rawOccurrenceRecordDAO;
	protected ImageRecordDAO imageRecordDAO;
	protected LinkRecordDAO linkRecordDAO;
	protected IdentifierRecordDAO identifierRecordDAO;
	protected TypificationRecordDAO typificationRecordDAO;
	
	protected GbifLogUtils gbifLogUtils;
	protected List<String> excludedStatuses;
	
	/**
	 * Context Keys
	 */
	protected String contextKeyIdentifications;
	protected String contextKeyDataProviderId;
	protected String contextKeyDataResourceId;
	protected String contextKeyResourceAccessPointId;
	protected String contextKeyInstitutionCode;
	protected String contextKeyCollectionCode;
	protected String contextKeyCatalogueNumber;
	protected String contextKeyUnitQualifier;
	protected String contextKeyScientificName;
	protected String contextKeyAuthor;
	protected String contextKeyRank;
	protected String contextKeyKingdom;
	protected String contextKeyPhylum;
	protected String contextKeyClass;
	protected String contextKeyOrder;
	protected String contextKeyFamily;
	protected String contextKeyGenus;
	protected String contextKeySpecies;
	protected String contextKeySubspecies;
	protected String contextKeyLatitude;
	protected String contextKeyLongitude;
	protected String contextKeyLatLongPrecision;
	protected String contextKeyMinAltitude;
	protected String contextKeyMaxAltitude;
	protected String contextKeyAltitudePrecision;
	protected String contextKeyMinDepth;
	protected String contextKeyMaxDepth;
	protected String contextKeyDepthPrecision;
	protected String contextKeyContinentOrOcean;
	protected String contextKeyCountry;
	protected String contextKeyStateOrProvince;
	protected String contextKeyCounty;
	protected String contextKeyCollectorName;
	protected String contextKeyLocality;
	protected String contextKeyYear;
	protected String contextKeyMonth;
	protected String contextKeyDay;
	protected String contextKeyBasisOfRecord;
	protected String contextKeyIdentifierName;
	protected String contextKeyDateIdentified;
	protected String contextKeyYearIdentified;
	protected String contextKeyMonthIdentified;
	protected String contextKeyDayIdentified;
	protected String contextKeyGuid;
	protected String contextKeyCollectorNumber;
	protected String contextKeyFieldNumber;
	protected String contextKeyAccessionNumbers;
	protected String contextKeySequenceNumbers;
	protected String contextKeyOtherCatalogNumbers;
	protected String contextKeyImages;
	protected String contextKeyLinks;
	protected String contextKeyTypifications;
	
	protected String contextKeyAddedCount;
	protected String contextKeyUpdatedCount;	
	
	private static DateFormat ymdFormat = new SimpleDateFormat("yyyy-MM-dd");
	
	/**
	 * @see org.gbif.portal.util.workflow.Activity#execute(org.gbif.portal.util.workflow.ProcessContext)
	 */
	@SuppressWarnings("unchecked")
	public ProcessContext execute(ProcessContext context) throws Exception {
		List<Identification> identifications = (List<Identification>) context.get(getContextKeyIdentifications(), List.class, false);
		if (identifications == null) {
			synchronise(context);
		}
		else {
			identifications = pruneIdentifications(context, identifications);
			for (Identification identification : identifications) {
			    context.put(contextKeyScientificName, identification.getScientificName());
			    context.put(contextKeyKingdom, identification.getKingdom());
			    context.put(contextKeyPhylum, identification.getPhylum());
			    context.put(contextKeyClass, identification.getBioClass());
			    context.put(contextKeyOrder, identification.getOrder());
			    context.put(contextKeyFamily, identification.getFamily());
			    context.put(contextKeyGenus, identification.getGenus());
			    context.put(contextKeyRank, identification.getRank());
			    context.put(contextKeyIdentifierName, identification.getIdentifier());
			    context.put(contextKeyDateIdentified, identification.getDate());
			    if (identifications.size() > 1) {
			    	context.put(contextKeyUnitQualifier, identification.getScientificName());
			    }
				synchronise(context);
			}
		}
		return context;
	}

	public ProcessContext synchronise(ProcessContext context) throws Exception {
		long dataProviderId = ((Long)context.get(getContextKeyDataProviderId(), Long.class, true)).longValue();
		long dataResourceId = ((Long)context.get(getContextKeyDataResourceId(), Long.class, true)).longValue();
		String institutionCode = (String)context.get(getContextKeyInstitutionCode(), String.class, false);
		String collectionCode = (String)context.get(getContextKeyCollectionCode(), String.class, false);
		String catalogueNumber = (String)context.get(getContextKeyCatalogueNumber(), String.class, false);
		String unitQualifier = (String)context.get(getContextKeyUnitQualifier(), String.class, false);
		
		// try and find an alternative if the catalogueNumber is missing
		// this is a hacky workaround for the fact that DWC and MaNIS have had versions that have misused
		// the spelling and thus some data arrives as unexpected
		if (catalogueNumber == null) {
			catalogueNumber = (String)context.get("catalogNumber", String.class, false);
		}
		
		Date dateIdentified = null;
		String dateString = StringUtils.trimToNull((String)context.get(getContextKeyDateIdentified(), String.class, false));
		if (dateString != null) {
			try {
				dateIdentified = ymdFormat.parse(dateString);
			}
			catch (Exception e) {
				// do nothing
			}
		}
		if (dateIdentified == null) {
			String yearIdentified = StringUtils.trimToNull((String)context.get(getContextKeyYearIdentified(), String.class, false));
			if (yearIdentified != null) {
				String monthIdentified = StringUtils.trimToNull((String)context.get(getContextKeyMonthIdentified(), String.class, false));
				String dayIdentified = StringUtils.trimToNull((String)context.get(getContextKeyDayIdentified(), String.class, false));
				try {
					dateIdentified = DateFormatter.toDate(dayIdentified, monthIdentified, yearIdentified);
				} catch (InvalidDateException e) {
					logger.warn(e);
				}
			}
		}
		
		if (StringUtils.isEmpty(institutionCode) ||
				StringUtils.isEmpty(collectionCode) ||
				StringUtils.isEmpty(catalogueNumber)) {
			logger.warn("Ignoring RawOccurrenceRecord: InstitutionCode[" + institutionCode + "], CollectionCode["+ collectionCode 
					+ "], CatalogueNumber["+ catalogueNumber + "] for DataResourceID[" + dataResourceId + "]");
						
		} else {
			RawOccurrenceRecord ror = rawOccurrenceRecordDAO.getUniqueRecord(dataResourceId, institutionCode, collectionCode, catalogueNumber, unitQualifier);
			Long rorId = null;
			List<ImageRecord> existingImages = null;
			List<LinkRecord> existingLinks = null;
			List<IdentifierRecord> existingIdentifiers = null;
			List<TypificationRecord> existingTypifications = null;
			logger.debug("Existing ROR: " + ror);
			if (ror !=null) {
				ror.setResourceAccessPointId(((Long)context.get(getContextKeyResourceAccessPointId(), Long.class, true)).longValue());
				ror.setInstitutionCode(institutionCode);
				ror.setCollectionCode(collectionCode);
				ror.setCatalogueNumber(catalogueNumber);
				ror.setScientificName((String)context.get(getContextKeyScientificName(), String.class, false));
				ror.setAuthor((String)context.get(getContextKeyAuthor(), String.class, false));
				ror.setRank((String)context.get(getContextKeyRank(), String.class, false));
				ror.setKingdom((String)context.get(getContextKeyKingdom(), String.class, false));
				ror.setPhylum((String)context.get(getContextKeyPhylum(), String.class, false));
				ror.setKlass((String)context.get(getContextKeyClass(), String.class, false));
				ror.setOrder((String)context.get(getContextKeyOrder(), String.class, false));
				ror.setFamily((String)context.get(getContextKeyFamily(), String.class, false));
				ror.setGenus((String)context.get(getContextKeyGenus(), String.class, false));
				ror.setSpecies((String)context.get(getContextKeySpecies(), String.class, false));
				ror.setSubspecies((String)context.get(getContextKeySubspecies(), String.class, false));
				ror.setLatitude((String)context.get(getContextKeyLatitude(), String.class, false));
				ror.setLongitude((String)context.get(getContextKeyLongitude(), String.class, false));
				ror.setLatLongPrecision((String)context.get(getContextKeyLatLongPrecision(), String.class, false));
				ror.setMinAltitude((String)context.get(getContextKeyMinAltitude(), String.class, false));
				ror.setMaxAltitude((String)context.get(getContextKeyMaxAltitude(), String.class, false));
				ror.setAltitudePrecision((String)context.get(getContextKeyAltitudePrecision(), String.class, false));
				ror.setMinDepth((String)context.get(getContextKeyMinDepth(), String.class, false));
				ror.setMaxDepth((String)context.get(getContextKeyMaxDepth(), String.class, false));
				ror.setDepthPrecision((String)context.get(getContextKeyDepthPrecision(), String.class, false));
				ror.setContinentOrOcean((String)context.get(getContextKeyContinentOrOcean(), String.class, false));
				ror.setCountry((String)context.get(getContextKeyCountry(), String.class, false));
				ror.setStateOrProvince((String)context.get(getContextKeyStateOrProvince(), String.class, false));
				ror.setCounty((String)context.get(getContextKeyCounty(), String.class, false));
				ror.setCollectorName((String)context.get(getContextKeyCollectorName(), String.class, false));
				ror.setLocality((String)context.get(getContextKeyLocality(), String.class, false));
				ror.setYear((String)context.get(getContextKeyYear(), String.class, false));
				ror.setMonth((String)context.get(getContextKeyMonth(), String.class, false));
				ror.setDay((String)context.get(getContextKeyDay(), String.class, false));
				ror.setBasisOfRecord((String)context.get(getContextKeyBasisOfRecord(), String.class, false));
				ror.setIdentifierName((String)context.get(getContextKeyIdentifierName(), String.class, false));
				ror.setDateIdentified(dateIdentified);
				ror.setUnitQualifier(unitQualifier);
				rorId = rawOccurrenceRecordDAO.updateOrCreate(ror);
				existingImages = imageRecordDAO.findByOccurrenceId(rorId);
				existingLinks = linkRecordDAO.findByOccurrenceId(rorId);
				existingIdentifiers = identifierRecordDAO.findByOccurrenceId(rorId);
				existingTypifications = typificationRecordDAO.findByOccurrenceId(rorId);
				logger.debug("Updated RawOccurrenceRecord with id: " + rorId);
				incrementCount(context, contextKeyUpdatedCount);
	
			} else {
				ror = new RawOccurrenceRecord(
						dataProviderId,
						dataResourceId,
						((Long)context.get(getContextKeyResourceAccessPointId(), Long.class, true)).longValue(),
						institutionCode,
						collectionCode,
						catalogueNumber,
						(String)context.get(getContextKeyScientificName(), String.class, false),
						(String)context.get(getContextKeyAuthor(), String.class, false),
						(String)context.get(getContextKeyRank(), String.class, false),
						(String)context.get(getContextKeyKingdom(), String.class, false),
						(String)context.get(getContextKeyPhylum(), String.class, false),
						(String)context.get(getContextKeyClass(), String.class, false),
						(String)context.get(getContextKeyOrder(), String.class, false),
						(String)context.get(getContextKeyFamily(), String.class, false),
						(String)context.get(getContextKeyGenus(), String.class, false),
						(String)context.get(getContextKeySpecies(), String.class, false),
						(String)context.get(getContextKeySubspecies(), String.class, false),
						(String)context.get(getContextKeyLatitude(), String.class, false),
						(String)context.get(getContextKeyLongitude(), String.class, false),
						(String)context.get(getContextKeyLatLongPrecision(), String.class, false),
						(String)context.get(getContextKeyMinAltitude(), String.class, false),
						(String)context.get(getContextKeyMaxAltitude(), String.class, false),
						(String)context.get(getContextKeyAltitudePrecision(), String.class, false),
						(String)context.get(getContextKeyMinDepth(), String.class, false),
						(String)context.get(getContextKeyMaxDepth(), String.class, false),
						(String)context.get(getContextKeyDepthPrecision(), String.class, false),
						(String)context.get(getContextKeyContinentOrOcean(), String.class, false),
						(String)context.get(getContextKeyCountry(), String.class, false),
						(String)context.get(getContextKeyStateOrProvince(), String.class, false),
						(String)context.get(getContextKeyCounty(), String.class, false),
						(String)context.get(getContextKeyCollectorName(), String.class, false),
						(String)context.get(getContextKeyLocality(), String.class, false),
						(String)context.get(getContextKeyYear(), String.class, false),
						(String)context.get(getContextKeyMonth(), String.class, false),
						(String)context.get(getContextKeyDay(), String.class, false),
						(String)context.get(getContextKeyBasisOfRecord(), String.class, false),
						(String)context.get(getContextKeyIdentifierName(), String.class, false),
						dateIdentified,
						unitQualifier,
						new Date(),
						(Date)null,
						(Date)null);
				logger.debug("Creating new RawOccurrenceRecord...");
				rorId = rawOccurrenceRecordDAO.create(ror);
				logger.debug("Created RawOccurrenceRecord with id: " + rorId);
				incrementCount(context, contextKeyAddedCount);
			}
			synchroniseImages(context, dataProviderId, dataResourceId, rorId, existingImages);
			synchroniseLinks(context, dataProviderId, dataResourceId, rorId, existingLinks);
			synchroniseIdentifiers(context, dataProviderId, dataResourceId, rorId, existingIdentifiers);
			synchroniseTypifications(context, dataProviderId, dataResourceId, rorId, existingTypifications);
		}
		return context;
	}
	
	/**
	 * Increment a count within the context.
	 * @param context
	 * @param contextKey
	 */
	private void incrementCount(ProcessContext context, String contextKey) {
		Integer count = (Integer) context.get(contextKey);
		if(count==null){
			count = new Integer(1);
		} else {
			count = count+1; 
		}
		context.put(contextKey, count);
	}

	private List<Identification> pruneIdentifications(ProcessContext context, 
													  List<Identification> identifications) 
						throws Exception {
		if (identifications.size() > 1) {
			List<Identification> preferred = new ArrayList<Identification>();
			for (Identification identification : identifications) {
				if (identification.isPreferred()) {
					preferred.add(identification);
				}
			}
			if (preferred.size() >= 1) {
				if (preferred.size() > 1) {
					// Log for information
					logger.info(gbifLogUtils.createGbifLogMessage(context, LogEvent.HARVEST_MULTIPLEPREFERREDIDENTIFICATIONS, 
													  ((String) context.get(getContextKeyCatalogueNumber(), String.class, false)) + ": unit has multiple preferred identifications"));
				}
				identifications = preferred;
			}
			else {
				// Log for information
				logger.warn(gbifLogUtils.createGbifLogMessage(context, LogEvent.HARVEST_MULTIPLEUNPREFERREDIDENTIFICATIONS, 
												  ((String) context.get(getContextKeyCatalogueNumber(), String.class, false)) + ": unit has multiple non-preferred identifications"));
			}
		}
		return identifications;
	}
	
	/**
	 * Manage raw image records for a raw occurrence record
	 * @param context
	 * @param dataProviderId
	 * @param dataResourceId
	 * @param rorId
	 * @param existingImages
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	private void synchroniseImages(ProcessContext context,
								   Long dataProviderId,
								   Long dataResourceId,
								   Long rorId, 
								   List<ImageRecord> existingImages) throws Exception {
		if (getContextKeyImages() != null) {
			List<ImageRecord> images 
				= (List<ImageRecord>) context.get(getContextKeyImages(), List.class, false);
			if (images != null) {
				for (ImageRecord image : images) {
					if (!image.getUrl().equalsIgnoreCase("NULL")) {
						if (existingImages != null) {
							for (ImageRecord existingImage : existingImages) {
								// If we find the same image (determined by a match of ROR id 
								// and URL, make sure we reuse the same RIR id and remove the
								// image from the list of those to delete.
								if (existingImage.getOccurrenceId() == rorId
									&& StringUtils.equals(existingImage.getUrl(), image.getUrl())) {
									image.setId(existingImage.getId());
									existingImages.remove(existingImage);
									break;
								}
							}
						}
						image.setDataResourceId(dataResourceId);
						image.setOccurrenceId(rorId);
						imageRecordDAO.updateOrCreate(image);
					}
				}					
			}
		}
		if (existingImages != null) {
			for (ImageRecord existingImage : existingImages) {
				imageRecordDAO.delete(existingImage);
			}
		}
	}
	
	/**
	 * Manage raw link records for a raw occurrence record
	 * @param context
	 * @param dataProviderId
	 * @param dataResourceId
	 * @param rorId
	 * @param existingLinks
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	private void synchroniseLinks(ProcessContext context,
								   Long dataProviderId,
								   Long dataResourceId,
								   Long rorId, 
								   List<LinkRecord> existingLinks) throws Exception {
		if (getContextKeyLinks() != null) {
			List<LinkRecord> links 
				= (List<LinkRecord>) context.get(getContextKeyLinks(), List.class, false);
			if (links != null) {
				for (LinkRecord link : links) {
					if (existingLinks != null) {
						for (LinkRecord existingLink : existingLinks) {
							// If we find the same link (determined by a match of ROR id 
							// and URL, make sure we reuse the same RIR id and remove the
							// link from the list of those to delete.
							if (existingLink.getOccurrenceId() == rorId
								&& StringUtils.equals(existingLink.getUrl(), link.getUrl())) {
								link.setId(existingLink.getId());
								existingLinks.remove(existingLink);
								break;
							}
						}
					}
					link.setDataResourceId(dataResourceId);
					link.setOccurrenceId(rorId);
					linkRecordDAO.updateOrCreate(link);
				}					
			}
		}
		if (existingLinks != null) {
			for (LinkRecord existingLink : existingLinks) {
				linkRecordDAO.delete(existingLink);
			}
		}
	}
	
	/**
	 * Manage raw identifier records for a raw occurrence record
	 * @param context
	 * @param dataProviderId
	 * @param dataResourceId
	 * @param rorId
	 * @param existingIdentifiers
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	private void synchroniseIdentifiers(ProcessContext context,
								   Long dataProviderId,
								   Long dataResourceId,
								   Long rorId, 
								   List<IdentifierRecord> existingIdentifiers) throws Exception {
		String id = (String) context.get(getContextKeyGuid(), String.class, false);
		if (id != null && id.length() > 0) {
			synchroniseIdentifier(dataProviderId, dataResourceId, rorId, OccurrenceRecordUtils.IDENTIFIERTYPE_GUID, id, existingIdentifiers);
		}
		id = (String) context.get(getContextKeyFieldNumber(), String.class, false);
		if (id != null && id.length() > 0) {
			synchroniseIdentifier(dataProviderId, dataResourceId, rorId, OccurrenceRecordUtils.IDENTIFIERTYPE_FIELDNUMBER, id, existingIdentifiers);
		}
		id = (String) context.get(getContextKeyCollectorNumber(), String.class, false);
		if (id != null && id.length() > 0) {
			synchroniseIdentifier(dataProviderId, dataResourceId, rorId, OccurrenceRecordUtils.IDENTIFIERTYPE_COLLECTORNUMBER, id, existingIdentifiers);
		}
		List<String> ids = (List<String>) context.get(getContextKeyAccessionNumbers(), List.class, false);
		if (ids != null) {
			for(String accessionNumber : ids) {
				if (accessionNumber.length() > 0) {
					synchroniseIdentifier(dataProviderId, dataResourceId, rorId, OccurrenceRecordUtils.IDENTIFIERTYPE_ACCESSIONNUMBER, accessionNumber, existingIdentifiers);
				}
			}
		}
		ids = (List<String>) context.get(getContextKeySequenceNumbers(), List.class, false);
		if (ids != null) {
			for(String sequenceId : ids) {
				if (sequenceId.length() > 0) {
					synchroniseIdentifier(dataProviderId, dataResourceId, rorId, OccurrenceRecordUtils.IDENTIFIERTYPE_SEQUENCENUMBER, sequenceId, existingIdentifiers);
				}
			}
		}
		ids = (List<String>) context.get(getContextKeyOtherCatalogNumbers(), List.class, false);
		if (ids != null) {
			for(String otherId : ids) {
				if (otherId.length() > 0) {
					synchroniseIdentifier(dataProviderId, dataResourceId, rorId, OccurrenceRecordUtils.IDENTIFIERTYPE_OTHERCATALOGNUMBER, otherId, existingIdentifiers);
				}
			}
		}
		if (existingIdentifiers != null) {
			for (IdentifierRecord existingIdentifier : existingIdentifiers) {
				identifierRecordDAO.delete(existingIdentifier);
			}
		}
	}
	
	/**
	 * Manage raw typification records for a raw occurrence record
	 * @param context
	 * @param dataProviderId
	 * @param dataResourceId
	 * @param rorId
	 * @param existingTypifications
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	private void synchroniseTypifications(ProcessContext context,
								   Long dataProviderId,
								   Long dataResourceId,
								   Long rorId, 
								   List<TypificationRecord> existingTypifications) throws Exception {
		if (getContextKeyTypifications() != null) {
			List<TypificationRecord> typifications 
				= (List<TypificationRecord>) context.get(getContextKeyTypifications(), List.class, false);
			if (typifications != null) {
				for (TypificationRecord typification : typifications) {
					if (!excludedStatuses.contains(typification.getTypeStatus())) {
						if (existingTypifications != null) {
							for (TypificationRecord existingTypification : existingTypifications) {
								// If we find the same typification (determined by a match of ROR id 
								// and URL, make sure we reuse the same RIR id and remove the
								// typification from the list of those to delete.
								if (existingTypification.getOccurrenceId() == rorId
									&& StringUtils.equals(existingTypification.getTypeStatus(), typification.getTypeStatus())
									&& StringUtils.equals(existingTypification.getScientificName(), typification.getScientificName())) {
									typification.setId(existingTypification.getId());
									existingTypifications.remove(existingTypification);
									break;
								}
							}
						}
						typification.setDataResourceId(dataResourceId);
						typification.setOccurrenceId(rorId);
						typificationRecordDAO.updateOrCreate(typification);
					}
				}					
			}
		}
		if (existingTypifications != null) {
			for (TypificationRecord existingTypification : existingTypifications) {
				typificationRecordDAO.delete(existingTypification);
			}
		}
	}
	
	/**
	 * Synchonise a single raw identifier record and remove from list of those to be deleted
	 * @param dataProviderId
	 * @param dataResourceId
	 * @param rorId
	 * @param idType
	 * @param id
	 * @param existingIdentifiers
	 */
	private void synchroniseIdentifier(Long dataProviderId,
			   Long dataResourceId,
			   Long rorId,
			   int idType,
			   String id,
			   List<IdentifierRecord> existingIdentifiers) {
		IdentifierRecord identifier = null;
		
		if (existingIdentifiers != null) {
			for (IdentifierRecord existingIdentifier : existingIdentifiers) {
				// If we find the same identifier (determined by a match of ROR id, 
				// type and value, make sure we reuse the same RIR id and remove the
				// identifier from the list of those to delete.
				if (existingIdentifier.getOccurrenceId() == rorId
					&& existingIdentifier.getIdentifierType() == idType
					&& StringUtils.equals(existingIdentifier.getIdentifier(), id)) {
					identifier = existingIdentifier;
					existingIdentifiers.remove(existingIdentifier);
					break;
				}
			}
		}
		
		// If we found the identifier, it must be a perfect match so we only
		// have to do anything in the case of a new identifier.
		if (identifier == null) {
			identifier = new IdentifierRecord();
			identifier.setDataResourceId(dataResourceId);
			identifier.setOccurrenceId(rorId);
			identifier.setIdentifierType(idType);
			identifier.setIdentifier(id);
			identifierRecordDAO.create(identifier);
		}
	}

	/**
	 * @return the contextKeyMaxAltitude
	 */
	public String getContextKeyMaxAltitude() {
		return contextKeyMaxAltitude;
	}

	/**
	 * @param contextKeyMaxAltitude the contextKeyMaxAltitude to set
	 */
	public void setContextKeyMaxAltitude(String contextKeyMaxAltitude) {
		this.contextKeyMaxAltitude = contextKeyMaxAltitude;
	}

	/**
	 * @return the contextKeyMinAltitude
	 */
	public String getContextKeyMinAltitude() {
		return contextKeyMinAltitude;
	}

	/**
	 * @param contextKeyMinAltitude the contextKeyMinAltitude to set
	 */
	public void setContextKeyMinAltitude(String contextKeyMinAltitude) {
		this.contextKeyMinAltitude = contextKeyMinAltitude;
	}

	/**
	 * @return Returns the contextKeyAltitudePrecision.
	 */
	public String getContextKeyAltitudePrecision() {
		return contextKeyAltitudePrecision;
	}

	/**
	 * @param contextKeyAltitudePrecision The contextKeyAltitudePrecision to set.
	 */
	public void setContextKeyAltitudePrecision(String contextKeyAltitudePrecision) {
		this.contextKeyAltitudePrecision = contextKeyAltitudePrecision;
	}

	/**
	 * @return Returns the contextKeyAuthor.
	 */
	public String getContextKeyAuthor() {
		return contextKeyAuthor;
	}

	/**
	 * @param contextKeyAuthor The contextKeyAuthor to set.
	 */
	public void setContextKeyAuthor(String contextKeyAuthor) {
		this.contextKeyAuthor = contextKeyAuthor;
	}

	/**
	 * @return Returns the contextKeyBasisOfRecord.
	 */
	public String getContextKeyBasisOfRecord() {
		return contextKeyBasisOfRecord;
	}

	/**
	 * @param contextKeyBasisOfRecord The contextKeyBasisOfRecord to set.
	 */
	public void setContextKeyBasisOfRecord(String contextKeyBasisOfRecord) {
		this.contextKeyBasisOfRecord = contextKeyBasisOfRecord;
	}

	/**
	 * @return Returns the contextKeyCatalogueNumber.
	 */
	public String getContextKeyCatalogueNumber() {
		return contextKeyCatalogueNumber;
	}

	/**
	 * @param contextKeyCatalogueNumber The contextKeyCatalogueNumber to set.
	 */
	public void setContextKeyCatalogueNumber(String contextKeyCatalogueNumber) {
		this.contextKeyCatalogueNumber = contextKeyCatalogueNumber;
	}

	/**
	 * @return Returns the contextKeyClass.
	 */
	public String getContextKeyClass() {
		return contextKeyClass;
	}

	/**
	 * @param contextKeyClass The contextKeyClass to set.
	 */
	public void setContextKeyClass(String contextKeyClass) {
		this.contextKeyClass = contextKeyClass;
	}

	/**
	 * @return Returns the contextKeyCollectionCode.
	 */
	public String getContextKeyCollectionCode() {
		return contextKeyCollectionCode;
	}

	/**
	 * @param contextKeyCollectionCode The contextKeyCollectionCode to set.
	 */
	public void setContextKeyCollectionCode(String contextKeyCollectionCode) {
		this.contextKeyCollectionCode = contextKeyCollectionCode;
	}

	/**
	 * @return Returns the contextKeyCollectorName.
	 */
	public String getContextKeyCollectorName() {
		return contextKeyCollectorName;
	}

	/**
	 * @param contextKeyCollectorName The contextKeyCollectorName to set.
	 */
	public void setContextKeyCollectorName(String contextKeyCollectorName) {
		this.contextKeyCollectorName = contextKeyCollectorName;
	}

	/**
	 * @return the contextKeyDay
	 */
	public String getContextKeyDay() {
		return contextKeyDay;
	}

	/**
	 * @param contextKeyDay the contextKeyDay to set
	 */
	public void setContextKeyDay(String contextKeyDay) {
		this.contextKeyDay = contextKeyDay;
	}

	/**
	 * @return the contextKeyMonth
	 */
	public String getContextKeyMonth() {
		return contextKeyMonth;
	}

	/**
	 * @param contextKeyMonth the contextKeyMonth to set
	 */
	public void setContextKeyMonth(String contextKeyMonth) {
		this.contextKeyMonth = contextKeyMonth;
	}

	/**
	 * @return the contextKeyYear
	 */
	public String getContextKeyYear() {
		return contextKeyYear;
	}

	/**
	 * @param contextKeyYear the contextKeyYear to set
	 */
	public void setContextKeyYear(String contextKeyYear) {
		this.contextKeyYear = contextKeyYear;
	}

	/**
	 * @return Returns the contextKeyCountry.
	 */
	public String getContextKeyCountry() {
		return contextKeyCountry;
	}

	/**
	 * @param contextKeyCountry The contextKeyCountry to set.
	 */
	public void setContextKeyCountry(String contextKeyCountry) {
		this.contextKeyCountry = contextKeyCountry;
	}

	/**
	 * @return the contextKeyCounty
	 */
	public String getContextKeyCounty() {
		return contextKeyCounty;
	}

	/**
	 * @param contextKeyCounty the contextKeyCounty to set
	 */
	public void setContextKeyCounty(String contextKeyCounty) {
		this.contextKeyCounty = contextKeyCounty;
	}

	/**
	 * @return the contextKeyStateOrProvince
	 */
	public String getContextKeyStateOrProvince() {
		return contextKeyStateOrProvince;
	}

	/**
	 * @param contextKeyStateOrProvince the contextKeyStateOrProvince to set
	 */
	public void setContextKeyStateOrProvince(String contextKeyStateOrProvince) {
		this.contextKeyStateOrProvince = contextKeyStateOrProvince;
	}

	/**
	 * @return Returns the contextKeyDataProviderId.
	 */
	public String getContextKeyDataProviderId() {
		return contextKeyDataProviderId;
	}

	/**
	 * @param contextKeyDataProviderId The contextKeyDataProviderId to set.
	 */
	public void setContextKeyDataProviderId(String contextKeyDataProviderId) {
		this.contextKeyDataProviderId = contextKeyDataProviderId;
	}


	/**
	 * @return Returns the contextKeyResourceAccessPointId.
	 */
	public String getContextKeyResourceAccessPointId() {
		return contextKeyResourceAccessPointId;
	}

	/**
	 * @param contextKeyResourceAccessPointId The contextKeyResourceAccessPointId to set.
	 */
	public void setContextKeyResourceAccessPointId(
			String contextKeyResourceAccessPointId) {
		this.contextKeyResourceAccessPointId = contextKeyResourceAccessPointId;
	}

	/**
	 * @return Returns the contextKeyDataResourceId.
	 */
	public String getContextKeyDataResourceId() {
		return contextKeyDataResourceId;
	}

	/**
	 * @param contextKeyDataResourceId The contextKeyDataResourceId to set.
	 */
	public void setContextKeyDataResourceId(String contextKeyDataResourceId) {
		this.contextKeyDataResourceId = contextKeyDataResourceId;
	}

	/**
	 * @return the contextKeyMaxDepth
	 */
	public String getContextKeyMaxDepth() {
		return contextKeyMaxDepth;
	}

	/**
	 * @param contextKeyMaxDepth the contextKeyMaxDepth to set
	 */
	public void setContextKeyMaxDepth(String contextKeyMaxDepth) {
		this.contextKeyMaxDepth = contextKeyMaxDepth;
	}

	/**
	 * @return the contextKeyMinDepth
	 */
	public String getContextKeyMinDepth() {
		return contextKeyMinDepth;
	}

	/**
	 * @param contextKeyMinDepth the contextKeyMinDepth to set
	 */
	public void setContextKeyMinDepth(String contextKeyMinDepth) {
		this.contextKeyMinDepth = contextKeyMinDepth;
	}

	/**
	 * @return Returns the contextKeyDepthPrecision.
	 */
	public String getContextKeyDepthPrecision() {
		return contextKeyDepthPrecision;
	}

	/**
	 * @param contextKeyDepthPrecision The contextKeyDepthPrecision to set.
	 */
	public void setContextKeyDepthPrecision(String contextKeyDepthPrecision) {
		this.contextKeyDepthPrecision = contextKeyDepthPrecision;
	}

	/**
	 * @return Returns the contextKeyFamily.
	 */
	public String getContextKeyFamily() {
		return contextKeyFamily;
	}

	/**
	 * @param contextKeyFamily The contextKeyFamily to set.
	 */
	public void setContextKeyFamily(String contextKeyFamily) {
		this.contextKeyFamily = contextKeyFamily;
	}

	/**
	 * @return Returns the contextKeyInstitutionCode.
	 */
	public String getContextKeyInstitutionCode() {
		return contextKeyInstitutionCode;
	}

	/**
	 * @param contextKeyInstitutionCode The contextKeyInstitutionCode to set.
	 */
	public void setContextKeyInstitutionCode(String contextKeyInstitutionCode) {
		this.contextKeyInstitutionCode = contextKeyInstitutionCode;
	}

	/**
	 * @return Returns the contextKeyKingdom.
	 */
	public String getContextKeyKingdom() {
		return contextKeyKingdom;
	}

	/**
	 * @param contextKeyKingdom The contextKeyKingdom to set.
	 */
	public void setContextKeyKingdom(String contextKeyKingdom) {
		this.contextKeyKingdom = contextKeyKingdom;
	}

	/**
	 * @return Returns the contextKeyLatitude.
	 */
	public String getContextKeyLatitude() {
		return contextKeyLatitude;
	}

	/**
	 * @param contextKeyLatitude The contextKeyLatitude to set.
	 */
	public void setContextKeyLatitude(String contextKeyLatitude) {
		this.contextKeyLatitude = contextKeyLatitude;
	}

	/**
	 * @return Returns the contextKeyLatLongPrecision.
	 */
	public String getContextKeyLatLongPrecision() {
		return contextKeyLatLongPrecision;
	}

	/**
	 * @param contextKeyLatLongPrecision The contextKeyLatLongPrecision to set.
	 */
	public void setContextKeyLatLongPrecision(String contextKeyLatLongPrecision) {
		this.contextKeyLatLongPrecision = contextKeyLatLongPrecision;
	}

	/**
	 * @return Returns the contextKeyLocality.
	 */
	public String getContextKeyLocality() {
		return contextKeyLocality;
	}

	/**
	 * @param contextKeyLocality The contextKeyLocality to set.
	 */
	public void setContextKeyLocality(String contextKeyLocality) {
		this.contextKeyLocality = contextKeyLocality;
	}

	/**
	 * @return Returns the contextKeyLongitude.
	 */
	public String getContextKeyLongitude() {
		return contextKeyLongitude;
	}

	/**
	 * @param contextKeyLongitude The contextKeyLongitude to set.
	 */
	public void setContextKeyLongitude(String contextKeyLongitude) {
		this.contextKeyLongitude = contextKeyLongitude;
	}

	/**
	 * @return Returns the contextKeyOrder.
	 */
	public String getContextKeyOrder() {
		return contextKeyOrder;
	}

	/**
	 * @param contextKeyOrder The contextKeyOrder to set.
	 */
	public void setContextKeyOrder(String contextKeyOrder) {
		this.contextKeyOrder = contextKeyOrder;
	}

	/**
	 * @return Returns the contextKeyPhylum.
	 */
	public String getContextKeyPhylum() {
		return contextKeyPhylum;
	}

	/**
	 * @param contextKeyPhylum The contextKeyPhylum to set.
	 */
	public void setContextKeyPhylum(String contextKeyPhylum) {
		this.contextKeyPhylum = contextKeyPhylum;
	}

	/**
	 * @return Returns the contextKeyRank.
	 */
	public String getContextKeyRank() {
		return contextKeyRank;
	}

	/**
	 * @param contextKeyRank The contextKeyRank to set.
	 */
	public void setContextKeyRank(String contextKeyRank) {
		this.contextKeyRank = contextKeyRank;
	}

	/**
	 * @return the contextKeyScientificName
	 */
	public String getContextKeyScientificName() {
		return contextKeyScientificName;
	}

	/**
	 * @param contextKeyScientificName the contextKeyScientificName to set
	 */
	public void setContextKeyScientificName(String contextKeyScientificName) {
		this.contextKeyScientificName = contextKeyScientificName;
	}

	/**
	 * @return Returns the contextKeySpecies.
	 */
	public String getContextKeySpecies() {
		return contextKeySpecies;
	}

	/**
	 * @param contextKeySpecies The contextKeySpecies to set.
	 */
	public void setContextKeySpecies(String contextKeySpecies) {
		this.contextKeySpecies = contextKeySpecies;
	}

	/**
	 * @return the contextKeySubspecies
	 */
	public String getContextKeySubspecies() {
		return contextKeySubspecies;
	}

	/**
	 * @param contextKeySubspecies the contextKeySubspecies to set
	 */
	public void setContextKeySubspecies(String contextKeySubspecies) {
		this.contextKeySubspecies = contextKeySubspecies;
	}

	/**
	 * @return Returns the rawOccurrenceRecordDAO.
	 */
	public RawOccurrenceRecordDAO getRawOccurrenceRecordDAO() {
		return rawOccurrenceRecordDAO;
	}

	/**
	 * @param rawOccurrenceRecordDAO The rawOccurrenceRecordDAO to set.
	 */
	public void setRawOccurrenceRecordDAO(
			RawOccurrenceRecordDAO rawOccurrenceRecordDAO) {
		this.rawOccurrenceRecordDAO = rawOccurrenceRecordDAO;
	}

	/**
	 * @return the identifierRecordDAO
	 */
	public IdentifierRecordDAO getIdentifierRecordDAO() {
		return identifierRecordDAO;
	}

	/**
	 * @param identifierRecordDAO the identifierRecordDAO to set
	 */
	public void setIdentifierRecordDAO(IdentifierRecordDAO identifierRecordDAO) {
		this.identifierRecordDAO = identifierRecordDAO;
	}

	/**
	 * @return the imageRecordDAO
	 */
	public ImageRecordDAO getImageRecordDAO() {
		return imageRecordDAO;
	}

	/**
	 * @param imageRecordDAO the imageRecordDAO to set
	 */
	public void setImageRecordDAO(ImageRecordDAO imageRecordDAO) {
		this.imageRecordDAO = imageRecordDAO;
	}

	/**
	 * @return the linkRecordDAO
	 */
	public LinkRecordDAO getLinkRecordDAO() {
		return linkRecordDAO;
	}

	/**
	 * @param linkRecordDAO the linkRecordDAO to set
	 */
	public void setLinkRecordDAO(LinkRecordDAO linkRecordDAO) {
		this.linkRecordDAO = linkRecordDAO;
	}

	/**
	 * @return the typificationRecordDAO
	 */
	public TypificationRecordDAO getTypificationRecordDAO() {
		return typificationRecordDAO;
	}

	/**
	 * @param typificationRecordDAO the typificationRecordDAO to set
	 */
	public void setTypificationRecordDAO(TypificationRecordDAO typificationRecordDAO) {
		this.typificationRecordDAO = typificationRecordDAO;
	}

	/**
	 * @return Returns the contextKeyGenus.
	 */
	public String getContextKeyGenus() {
		return contextKeyGenus;
	}

	/**
	 * @param contextKeyGenus The contextKeyGenus to set.
	 */
	public void setContextKeyGenus(String contextKeyGenus) {
		this.contextKeyGenus = contextKeyGenus;
	}

	/**
	 * @return the contextKeyImages
	 */
	public String getContextKeyImages() {
		return contextKeyImages;
	}

	/**
	 * @param contextKeyImages the contextKeyImages to set
	 */
	public void setContextKeyImages(String contextKeyImages) {
		this.contextKeyImages = contextKeyImages;
	}

	/**
	 * @return the contextKeyLinks
	 */
	public String getContextKeyLinks() {
		return contextKeyLinks;
	}

	/**
	 * @param contextKeyLinks the contextKeyLinks to set
	 */
	public void setContextKeyLinks(String contextKeyLinks) {
		this.contextKeyLinks = contextKeyLinks;
	}

	/**
	 * @return the contextKeyCollectorNumber
	 */
	public String getContextKeyCollectorNumber() {
		return contextKeyCollectorNumber;
	}

	/**
	 * @param contextKeyCollectorNumber the contextKeyCollectorNumber to set
	 */
	public void setContextKeyCollectorNumber(String contextKeyCollectorNumber) {
		this.contextKeyCollectorNumber = contextKeyCollectorNumber;
	}

	/**
	 * @return the contextKeyFieldNumber
	 */
	public String getContextKeyFieldNumber() {
		return contextKeyFieldNumber;
	}

	/**
	 * @param contextKeyFieldNumber the contextKeyFieldNumber to set
	 */
	public void setContextKeyFieldNumber(String contextKeyFieldNumber) {
		this.contextKeyFieldNumber = contextKeyFieldNumber;
	}

	/**
	 * @return the contextKeyGuid
	 */
	public String getContextKeyGuid() {
		return contextKeyGuid;
	}

	/**
	 * @param contextKeyGuid the contextKeyGuid to set
	 */
	public void setContextKeyGuid(String contextKeyGuid) {
		this.contextKeyGuid = contextKeyGuid;
	}

	/**
	 * @return the contextKeyOtherCatalogNumbers
	 */
	public String getContextKeyOtherCatalogNumbers() {
		return contextKeyOtherCatalogNumbers;
	}

	/**
	 * @param contextKeyOtherCatalogNumbers the contextKeyOtherCatalogNumbers to set
	 */
	public void setContextKeyOtherCatalogNumbers(
			String contextKeyOtherCatalogNumbers) {
		this.contextKeyOtherCatalogNumbers = contextKeyOtherCatalogNumbers;
	}

	/**
	 * @return the contextKeySequenceNumbers
	 */
	public String getContextKeySequenceNumbers() {
		return contextKeySequenceNumbers;
	}

	/**
	 * @param contextKeySequenceNumbers the contextKeySequenceNumbers to set
	 */
	public void setContextKeySequenceNumbers(String contextKeySequenceNumbers) {
		this.contextKeySequenceNumbers = contextKeySequenceNumbers;
	}

	/**
	 * @return the contextKeyAccessionNumbers
	 */
	public String getContextKeyAccessionNumbers() {
		return contextKeyAccessionNumbers;
	}

	/**
	 * @param contextKeyAccessionNumber the contextKeyAccessionNumbers to set
	 */
	public void setContextKeyAccessionNumbers(String contextKeyAccessionNumbers) {
		this.contextKeyAccessionNumbers = contextKeyAccessionNumbers;
	}

	/**
	 * @return the contextKeyTypifications
	 */
	public String getContextKeyTypifications() {
		return contextKeyTypifications;
	}

	/**
	 * @param contextKeyTypifications the contextKeyTypifications to set
	 */
	public void setContextKeyTypifications(String contextKeyTypifications) {
		this.contextKeyTypifications = contextKeyTypifications;
	}

	/**
	 * @return the contextKeyContinentOrOcean
	 */
	public String getContextKeyContinentOrOcean() {
		return contextKeyContinentOrOcean;
	}

	/**
	 * @param contextKeyContinentOrOcean the contextKeyContinentOrOcean to set
	 */
	public void setContextKeyContinentOrOcean(String contextKeyContinentOrOcean) {
		this.contextKeyContinentOrOcean = contextKeyContinentOrOcean;
	}

	/**
	 * @return the contextKeyDateIdentified
	 */
	public String getContextKeyDateIdentified() {
		return contextKeyDateIdentified;
	}

	/**
	 * @param contextKeyDateIdentified the contextKeyDateIdentified to set
	 */
	public void setContextKeyDateIdentified(String contextKeyDateIdentified) {
		this.contextKeyDateIdentified = contextKeyDateIdentified;
	}

	/**
	 * @return the contextKeyIdentifierName
	 */
	public String getContextKeyIdentifierName() {
		return contextKeyIdentifierName;
	}

	/**
	 * @param contextKeyIdentifierName the contextKeyIdentifierName to set
	 */
	public void setContextKeyIdentifierName(String contextKeyIdentifierName) {
		this.contextKeyIdentifierName = contextKeyIdentifierName;
	}

	/**
	 * @return the contextKeyDayIdentified
	 */
	public String getContextKeyDayIdentified() {
		return contextKeyDayIdentified;
	}

	/**
	 * @param contextKeyDayIdentified the contextKeyDayIdentified to set
	 */
	public void setContextKeyDayIdentified(String contextKeyDayIdentified) {
		this.contextKeyDayIdentified = contextKeyDayIdentified;
	}

	/**
	 * @return the contextKeyMonthIdentified
	 */
	public String getContextKeyMonthIdentified() {
		return contextKeyMonthIdentified;
	}

	/**
	 * @param contextKeyMonthIdentified the contextKeyMonthIdentified to set
	 */
	public void setContextKeyMonthIdentified(String contextKeyMonthIdentified) {
		this.contextKeyMonthIdentified = contextKeyMonthIdentified;
	}

	/**
	 * @return the contextKeyYearIdentified
	 */
	public String getContextKeyYearIdentified() {
		return contextKeyYearIdentified;
	}

	/**
	 * @param contextKeyYearIdentified the contextKeyYearIdentified to set
	 */
	public void setContextKeyYearIdentified(String contextKeyYearIdentified) {
		this.contextKeyYearIdentified = contextKeyYearIdentified;
	}

	/**
	 * @return the contextKeyIdentifications
	 */
	public String getContextKeyIdentifications() {
		return contextKeyIdentifications;
	}

	/**
	 * @param contextKeyIdentifications the contextKeyIdentifications to set
	 */
	public void setContextKeyIdentifications(String contextKeyIdentifications) {
		this.contextKeyIdentifications = contextKeyIdentifications;
	}

	/**
	 * @return the contextKeyUnitQualifier
	 */
	public String getContextKeyUnitQualifier() {
		return contextKeyUnitQualifier;
	}

	/**
	 * @param contextKeyUnitQualifier the contextKeyUnitQualifier to set
	 */
	public void setContextKeyUnitQualifier(String contextKeyUnitQualifier) {
		this.contextKeyUnitQualifier = contextKeyUnitQualifier;
	}

	/**
	 * @return the gbifLogUtils
	 */
	public GbifLogUtils getGbifLogUtils() {
		return gbifLogUtils;
	}

	/**
	 * @param gbifLogUtils the gbifLogUtils to set
	 */
	public void setGbifLogUtils(GbifLogUtils gbifLogUtils) {
		this.gbifLogUtils = gbifLogUtils;
	}

	/**
	 * @return the excludedStatuses
	 */
	public List<String> getExcludedStatuses() {
		return excludedStatuses;
	}

	/**
	 * @param excludedStatuses the excludedStatuses to set
	 */
	public void setExcludedStatuses(List<String> excludedStatuses) {
		this.excludedStatuses = excludedStatuses;
	}

	/**
	 * @param contextKeyAddedCount the contextKeyAddedCount to set
	 */
	public void setContextKeyAddedCount(String contextKeyAddedCount) {
		this.contextKeyAddedCount = contextKeyAddedCount;
	}

	/**
	 * @param contextKeyUpdatedCount the contextKeyUpdatedCount to set
	 */
	public void setContextKeyUpdatedCount(String contextKeyUpdatedCount) {
		this.contextKeyUpdatedCount = contextKeyUpdatedCount;
	}
}