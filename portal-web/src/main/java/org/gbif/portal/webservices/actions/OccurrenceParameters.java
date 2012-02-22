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
package org.gbif.portal.webservices.actions;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.gbif.portal.dto.PropertyStoreTripletDTO;
import org.gbif.portal.dto.taxonomy.BriefTaxonConceptDTO;
import org.gbif.portal.dto.util.BoundingBoxDTO;
import org.gbif.portal.dto.util.TimePeriodDTO;
import org.gbif.portal.model.occurrence.BasisOfRecord;
import org.gbif.portal.service.TaxonomyManager;
import org.gbif.portal.service.util.BoundingBoxUtils;
import org.gbif.portal.util.geospatial.CellIdUtils;
import org.gbif.portal.util.geospatial.LatLongBoundingBox;
import org.gbif.portal.util.path.PathMapping;
import org.gbif.portal.webservices.rest.Dispatcher;
import org.gbif.portal.webservices.util.GbifWebServiceException;


/**
 * Maps request key parameters to triplets subjects for occurrence searching.
 * 
 * @author Donald Hobern
 */
public class OccurrenceParameters extends Parameters {
	
	public static final String OCCURRENCE_SERVICE_NAME ="occurrence";
	
	public static final String KEY_MODE = "mode";
	public static final String KEY_MINLATITUDE = "minlatitude";
	public static final String KEY_MAXLATITUDE = "maxlatitude";
	public static final String KEY_MINLONGITUDE = "minlongitude";
	public static final String KEY_MAXLONGITUDE = "maxlongitude";
	public static final String KEY_MINALTITUDE = "minaltitude";
	public static final String KEY_MAXALTITUDE = "maxaltitude";
	public static final String KEY_MINDEPTH = "mindepth";
	public static final String KEY_MAXDEPTH = "maxdepth";
	public static final String KEY_CELLID = "cellid";
	public static final String KEY_CENTICELLID = "centicellid";
	public static final String KEY_STARTDATE = "startdate";
	public static final String KEY_ENDDATE = "enddate";
	public static final String KEY_STARTYEAR = "startyear";
	public static final String KEY_ENDYEAR = "endyear";
	public static final String KEY_YEAR = "year";
	public static final String KEY_MONTH = "month";
	public static final String KEY_DAY = "day";
	public static final String KEY_SCIENTIFICNAME = "scientificname";
	public static final String KEY_TAXONCONCEPTKEY = "taxonconceptkey";
	public static final String KEY_DATAPROVIDERKEY = "dataproviderkey";
	public static final String KEY_DATARESOURCEKEY = "dataresourcekey";
	public static final String KEY_RESOURCENETWORKKEY = "resourcenetworkkey";
	public static final String KEY_BASISOFRECORDCODE = "basisofrecordcode";
	public static final String KEY_HOSTISOCOUNTRYCODE = "hostisocountrycode";
	public static final String KEY_ORIGINISOCOUNTRYCODE = "originisocountrycode";
	public static final String KEY_ORIGINREGIONCODE = "originregioncode";
	public static final String KEY_TYPESONLY = "typesonly";
	public static final String KEY_COORDINATESTATUS = "coordinatestatus";
	public static final String KEY_COORDINATEISSUES = "coordinateissues";
	public static final String KEY_ICON = "icon";
	public static final String KEY_KEY = "key";
	
  //support institutionCode, collectionCode, catalogueNumber
  private static final String KEY_INSTITUTIONCODE = "institutioncode";
  private static final String KEY_COLLECTIONCODE = "collectioncode";
  private static final String KEY_CATALOGUENUMBER = "cataloguenumber";	

	// Old parameter names, handled for compatibility
	public static final String KEY_GEOREFERENCEDONLY = "georeferencedonly";

	private static final String TRIPLET_NAMESPACE = "http://gbif.org/portal-service/2006/1.0";
	private static final String SUBJECT_BASISOFRECORD = "SERVICE.OCCURRENCE.QUERY.SUBJECT.BASISOFRECORD";
	private static final String SUBJECT_CELLID = "SERVICE.OCCURRENCE.QUERY.SUBJECT.CELLID";
	private static final String SUBJECT_CELLIDMOD360 = "SERVICE.OCCURRENCE.QUERY.SUBJECT.CELLID.MOD360";
	private static final String SUBJECT_CENTICELLID = "SERVICE.OCCURRENCE.QUERY.SUBJECT.CENTICELLID";
	private static final String SUBJECT_KINGDOMID = "SERVICE.OCCURRENCE.QUERY.SUBJECT.TR.KINGDOMID";
	private static final String SUBJECT_PHYLUMID = "SERVICE.OCCURRENCE.QUERY.SUBJECT.TR.PHYLUMID";
	private static final String SUBJECT_CLASSID = "SERVICE.OCCURRENCE.QUERY.SUBJECT.TR.CLASSID";
	private static final String SUBJECT_ORDERID = "SERVICE.OCCURRENCE.QUERY.SUBJECT.TR.ORDERID";
	private static final String SUBJECT_FAMILYID = "SERVICE.OCCURRENCE.QUERY.SUBJECT.TR.FAMILYID";
	private static final String SUBJECT_GENUSID = "SERVICE.OCCURRENCE.QUERY.SUBJECT.TR.GENUSID";
	private static final String SUBJECT_SPECIESID = "SERVICE.OCCURRENCE.QUERY.SUBJECT.TR.SPECIESID";				
	private static final String SUBJECT_TAXONCONCEPTID = "SERVICE.OCCURRENCE.QUERY.SUBJECT.TAXONCONCEPTID";
	private static final String SUBJECT_DATAPROVIDERID = "SERVICE.OCCURRENCE.QUERY.SUBJECT.DATAPROVIDERID";
	private static final String SUBJECT_DATARESOURCEID = "SERVICE.OCCURRENCE.QUERY.SUBJECT.DATARESOURCEID";
	private static final String SUBJECT_DAY = "SERVICE.OCCURRENCE.QUERY.SUBJECT.DAY";
	private static final String SUBJECT_GEOSPATIALISSUES = "SERVICE.OCCURRENCE.QUERY.SUBJECT.GEOSPATIALISSUES";
	private static final String SUBJECT_HOSTCOUNTRYCODE = "SERVICE.OCCURRENCE.QUERY.SUBJECT.HOSTCOUNTRYCODE";
	private static final String SUBJECT_ISOCOUNTRYCODE = "SERVICE.OCCURRENCE.QUERY.SUBJECT.ISOCOUNTRYCODE";
	private static final String SUBJECT_LATITUDE = "SERVICE.OCCURRENCE.QUERY.SUBJECT.LATITUDE";
	private static final String SUBJECT_LONGITUDE = "SERVICE.OCCURRENCE.QUERY.SUBJECT.LONGITUDE";
	private static final String SUBJECT_ALTITUDE = "SERVICE.OCCURRENCE.QUERY.SUBJECT.ALTITUDE";
	private static final String SUBJECT_DEPTH = "SERVICE.OCCURRENCE.QUERY.SUBJECT.DEPTH";
	private static final String SUBJECT_MONTH = "SERVICE.OCCURRENCE.QUERY.SUBJECT.MONTH";
	private static final String SUBJECT_OCCURRENCEDATE = "SERVICE.OCCURRENCE.QUERY.SUBJECT.OCCURRENCEDATE";
	private static final String SUBJECT_OCCURRENCEMODIFICATIONDATE = "SERVICE.OCCURRENCE.QUERY.SUBJECT.OCCURRENCEMODIFICATIONDATE";
	private static final String SUBJECT_REGIONCODE = "SERVICE.OCCURRENCE.QUERY.SUBJECT.REGIONCODE";
	private static final String SUBJECT_TYPESTATUSCOUNT = "SERVICE.OCCURRENCE.QUERY.SUBJECT.TYPESTATUSCOUNT";
	private static final String SUBJECT_RESOURCENETWORKID = "SERVICE.OCCURRENCE.QUERY.SUBJECT.RESOURCENETWORKID";
	private static final String SUBJECT_SCIENTIFICNAME = "SERVICE.OCCURRENCE.QUERY.SUBJECT.SCIENTIFICNAME";
	private static final String SUBJECT_SELECTFIELD = "SERVICE.QUERY.SUBJECT.SELECTFIELD";
	private static final String SUBJECT_YEAR = "SERVICE.OCCURRENCE.QUERY.SUBJECT.YEAR";
	private static final String PREDICATE_EQUAL = "SERVICE.QUERY.PREDICATE.EQUAL";
	private static final String PREDICATE_G = "SERVICE.QUERY.PREDICATE.G";
	private static final String PREDICATE_GE = "SERVICE.QUERY.PREDICATE.GE";
	private static final String PREDICATE_ISNOTNULL = "SERVICE.QUERY.PREDICATE.ISNOTNULL";
	private static final String PREDICATE_LE = "SERVICE.QUERY.PREDICATE.LE";
	private static final String PREDICATE_LIKE = "SERVICE.QUERY.PREDICATE.LIKE";
	private static final String PREDICATE_NEQUAL = "SERVICE.QUERY.PREDICATE.NEQUAL";
	private static final String PREDICATE_RETURN = "SERVICE.QUERY.PREDICATE.RETURN";
	private static final String RETURNFIELDS_COUNT = "SERVICE.OCCURRENCE.QUERY.RETURNFIELDS.COUNT";
	
	//support institutionCode, collectionCode, catalogueNumber
	private static final String SUBJECT_INSTITUTIONCODE = "SERVICE.OCCURRENCE.QUERY.SUBJECT.INSTITUTIONCODE";
	private static final String SUBJECT_COLLECTIONCODE = "SERVICE.OCCURRENCE.QUERY.SUBJECT.COLLECTIONCODE";
	private static final String SUBJECT_CATALOGUENUMBER = "SERVICE.OCCURRENCE.QUERY.SUBJECT.CATALOGUENUMBER";

	public static final int MODE_PROCESSED = 1;
	public static final int MODE_RAW = 0;
	
	public static final String MODENAME_PROCESSED = "processed";
	public static final String MODENAME_RAW = "raw";
	
	public static final int OTHERFORMAT_MAXRESULTS = 1000;

	protected int mode = MODE_RAW;
	protected BoundingBoxDTO boundingBox = new BoundingBoxDTO();
	protected TimePeriodDTO timePeriod = new TimePeriodDTO();
	protected Integer startYear = null;
	protected Integer endYear = null;
	protected Integer[] years = null;
	protected Integer[] months = null;
	protected Integer[] days = null;
	protected String[] cellIds = null;
	protected String[] centiCellIds = null;
	protected String[] scientificNames = null;
	protected String[] taxonConceptKeys = null;
	protected String[] hostIsoCountryCodes = null;
	protected String[] originIsoCountryCodes = null;
	protected String[] dataProviderKeys = null;
	protected String[] dataResourceKeys = null;
	protected String[] resourceNetworkKeys = null;
	protected String[] basisOfRecordCodes = null;
	protected String[] originRegionCodes = null;
	protected Integer minAltitude = null;
	protected Integer maxAltitude = null;
	protected Float minDepth = null;
	protected Float maxDepth = null;
	protected String kmlIconUrl = null;
	protected String key = null;
	protected boolean typesOnly = false;
	protected Boolean coordinateStatus = null;
	protected Boolean coordinateIssues = null;
	
	//support institutionCode, collectionCode, catalogueNumber
	protected String[] institutionCodes = null;
	protected String[] collectionCodes = null;
	protected String[] catalogueNumbers = null;
	

	public static Log log = LogFactory.getLog(OccurrenceParameters.class);
	
	protected OccurrenceParameters() {
		// Null constructor
	}
	
	public String getServiceName() {
		return OCCURRENCE_SERVICE_NAME;
	}
	
	public OccurrenceParameters(Map<String, Object> params, PathMapping pathMapping)
		throws GbifWebServiceException
	{
		super(params, pathMapping);
		
		try {
			this.pathMapping = pathMapping;
			
			Set<String> keys = params.keySet();
			int specifiedMaxResults = -1;
			
			for (String k : keys) {
				Object value = params.get(k);
				
				if (k.equals(KEY_MODE)) {
					mode = getMode((String) value); 
				}
				else if (k.equals(KEY_MINLATITUDE)) {
					boundingBox.setLower(Float.valueOf((String) value)); 
				}
				else if (k.equals(KEY_MAXLATITUDE)) {
					boundingBox.setUpper(Float.valueOf((String) value)); 
				}
				else if (k.equals(KEY_MINLONGITUDE)) {
					boundingBox.setLeft(Float.valueOf((String) value)); 
				}
				else if (k.equals(KEY_MAXLONGITUDE)) {
					boundingBox.setRight(Float.valueOf((String) value)); 
				}
				else if (k.equals(KEY_CELLID)) {
					cellIds = getValue(params, KEY_CELLID, (String) value); 
				}
				else if (k.equals(KEY_CENTICELLID)) {
					centiCellIds = getValue(params, KEY_CENTICELLID, (String) value); 
				}
				else if (k.equals(KEY_STARTDATE)) {
					timePeriod.setStartPeriod(parseDate((String) value, 0, 0, 0));
				}
				else if (k.equals(KEY_ENDDATE)) {
					timePeriod.setEndPeriod(parseDate((String) value, 23, 59, 59));
				}
				else if (k.equals(KEY_STARTYEAR)) {
					try {
						startYear = new Integer((String) value);
					} catch (Exception e) {
						log.error("Invalid integer for startYear: " + value);
					}
				}
				else if (k.equals(KEY_ENDYEAR)) {
					try {
						endYear = new Integer((String) value);
					} catch (Exception e) {
						log.error("Invalid integer for endYear: " + value);
					}
				}
				else if (k.equals(KEY_YEAR)) {
					years = getIntegerValue(params, KEY_YEAR, (String) value, 1000, Calendar.getInstance().get(Calendar.YEAR));
				}
				else if (k.equals(KEY_MONTH)) {
					months = getIntegerValue(params, KEY_MONTH, (String) value, 1, 12);
				}
				else if (k.equals(KEY_DAY)) {
					days = getIntegerValue(params, KEY_DAY, (String) value, 1, 31);
				}
				else if (k.equals(KEY_SCIENTIFICNAME)) {
					String scientificName = (String) value;
					scientificNames = getValue(params, KEY_SCIENTIFICNAME, scientificName);
					for (String name : scientificNames) {
						int wildcardIndex = name.indexOf("*");
						if (wildcardIndex < 0) {
							wildcardIndex = name.indexOf("%");
						}
						if (wildcardIndex > 0 && wildcardIndex < MINIMUM_WILDCARD_INDEX) {
							throw new GbifWebServiceException("Scientific name string must include at least " + MINIMUM_WILDCARD_INDEX + " characters before the first wildcard character");
						}
					}
				}
				else if (k.equals(KEY_TAXONCONCEPTKEY)) {
					taxonConceptKeys = getValue(params, KEY_TAXONCONCEPTKEY, (String) value); 
				}
				else if (k.equals(KEY_DATAPROVIDERKEY)) {
					dataProviderKeys = getValue(params, KEY_DATAPROVIDERKEY, (String) value); 
				}
				else if (k.equals(KEY_DATARESOURCEKEY)) {
					dataResourceKeys = getValue(params, KEY_DATARESOURCEKEY, (String) value); 
				}
				else if (k.equals(KEY_RESOURCENETWORKKEY)) {
					resourceNetworkKeys = getValue(params, KEY_RESOURCENETWORKKEY, (String) value); 
				}
				else if (k.equals(KEY_BASISOFRECORDCODE)) {
					basisOfRecordCodes = getValue(params, KEY_BASISOFRECORDCODE, (String) value); 
				}
				else if (k.equals(KEY_HOSTISOCOUNTRYCODE)) {
					hostIsoCountryCodes = getValue(params, KEY_HOSTISOCOUNTRYCODE, ((String) value).toUpperCase()); 
				}
				else if (k.equals(KEY_ORIGINISOCOUNTRYCODE)) {
					originIsoCountryCodes = getValue(params, KEY_ORIGINISOCOUNTRYCODE, ((String) value).toUpperCase()); 
				}
				else if (k.equals(KEY_ORIGINREGIONCODE)) {
					originRegionCodes = getValue(params, KEY_ORIGINREGIONCODE, ((String) value).toUpperCase()); 
				}
				else if (k.equals(KEY_KEY)) {
					key = (String) value; 
				}
				else if (k.equals(KEY_ICON)) {
					kmlIconUrl = (String) value;
				}
				else if (k.equals(KEY_MAXRESULTS)) {
					specifiedMaxResults = Integer.parseInt((String) value);
				}
				else if (k.equals(KEY_TYPESONLY)) {
					typesOnly = Boolean.parseBoolean((String) value);
				}
				else if (k.equals(KEY_GEOREFERENCEDONLY)) {
					boolean georeferencedOnly = Boolean.parseBoolean((String) value);
					coordinateStatus = georeferencedOnly ? true : null;
				}
				else if (k.equals(KEY_COORDINATESTATUS)) {
					coordinateStatus = Boolean.parseBoolean((String) value);
				}
				else if (k.equals(KEY_COORDINATEISSUES)) {
					coordinateIssues = Boolean.parseBoolean((String) value);
				}
				else if (k.equals(KEY_MINALTITUDE)) {
					minAltitude = Integer.parseInt((String) value);
				}				
				else if (k.equals(KEY_MAXALTITUDE)) {
					maxAltitude = Integer.parseInt((String) value);
				}				
				else if (k.equals(KEY_MINDEPTH)) {
					minDepth = Float.parseFloat((String) value);
				}				
				else if (k.equals(KEY_MAXDEPTH)) {
					maxDepth = Float.parseFloat((String) value);
				}				
			  //support institutionCode, collectionCode, catalogueNumber
				else if(k.equals(KEY_INSTITUTIONCODE)) {
				  institutionCodes = getValue(params, KEY_INSTITUTIONCODE, (String) value);
				}
        else if(k.equals(KEY_COLLECTIONCODE)) {
          collectionCodes = getValue(params, KEY_COLLECTIONCODE, (String) value);
        }
        else if(k.equals(KEY_CATALOGUENUMBER)) {
          catalogueNumbers = getValue(params, KEY_CATALOGUENUMBER, (String) value);
        }				
			}
			
			if (format == FORMAT_KML) {
				coordinateStatus = true;
				if (specifiedMaxResults < 0) {
					specifiedMaxResults = OTHERFORMAT_MAXRESULTS;
				}
				if (specifiedMaxResults > maxResults) {
					if (specifiedMaxResults > OTHERFORMAT_MAXRESULTS) {
						specifiedMaxResults = OTHERFORMAT_MAXRESULTS;
					}
					maxResults = specifiedMaxResults;
					searchConstraints.setMaxResults(maxResults);
				}
			} else if (requestType == Action.GET) {
				if (key == null) {
					throw new GbifWebServiceException("Must provide key for get request");
				} else if (format == FORMAT_BRIEF) {
					format = FORMAT_DARWIN;
				}
			}
			boundingBox.checkValidity();
			timePeriod.checkValidity();
		}
		catch (GbifWebServiceException gwse) {
			throw(gwse);
		}
		catch (Exception e) {
			throw new GbifWebServiceException("OccurrenceParameters exception: " + e);
		}
	}
	
	public OccurrenceParameters(List<PropertyStoreTripletDTO> triplets, String formatName, String urlBase, String servletPath, PathMapping mapping, boolean throwExceptions) throws GbifWebServiceException {
		portalRoot = urlBase;
		wsRoot = urlBase + servletPath;
		pathMapping = mapping;
		format = getFormat(formatName);
		requestType = Action.LIST;
		serviceName = OCCURRENCE_SERVICE_NAME;

		for (PropertyStoreTripletDTO triplet : triplets) {
			boolean processed = false;
			
			if (triplet.getSubject().equals(SUBJECT_BASISOFRECORD) && triplet.getPredicate().equals(PREDICATE_EQUAL)) {
				basisOfRecordCodes = addValue(basisOfRecordCodes, BasisOfRecord.getBasisOfRecord((Integer) triplet.getObject()).getName());
				processed = true;
			} else if (    (    triplet.getSubject().equals(SUBJECT_KINGDOMID)
							 || triplet.getSubject().equals(SUBJECT_PHYLUMID)
							 || triplet.getSubject().equals(SUBJECT_CLASSID)
							 || triplet.getSubject().equals(SUBJECT_ORDERID)
							 || triplet.getSubject().equals(SUBJECT_FAMILYID)
							 || triplet.getSubject().equals(SUBJECT_GENUSID)
							 || triplet.getSubject().equals(SUBJECT_SPECIESID)
							 ||	triplet.getSubject().equals(SUBJECT_TAXONCONCEPTID))
					    && triplet.getPredicate().equals(PREDICATE_EQUAL)) {
				taxonConceptKeys = addValue(taxonConceptKeys, (triplet.getObject()).toString());
				processed = true;
			} else if (triplet.getSubject().equals(SUBJECT_DATAPROVIDERID) && triplet.getPredicate().equals(PREDICATE_EQUAL)) {
				dataProviderKeys = addValue(dataProviderKeys, (triplet.getObject()).toString());
				processed = true;
			} else if (triplet.getSubject().equals(SUBJECT_DATARESOURCEID) && triplet.getPredicate().equals(PREDICATE_EQUAL)) {
				dataResourceKeys = addValue(dataResourceKeys, (triplet.getObject()).toString());
				processed = true;
			} else if (triplet.getSubject().equals(SUBJECT_RESOURCENETWORKID) && triplet.getPredicate().equals(PREDICATE_EQUAL)) {
				resourceNetworkKeys = addValue(resourceNetworkKeys, (triplet.getObject()).toString());
				processed = true;
			} else if (triplet.getSubject().equals(SUBJECT_ISOCOUNTRYCODE) && triplet.getPredicate().equals(PREDICATE_EQUAL)) {
				originIsoCountryCodes = addValue(originIsoCountryCodes, (String) triplet.getObject());
				processed = true;
			} else if (triplet.getSubject().equals(SUBJECT_HOSTCOUNTRYCODE) && triplet.getPredicate().equals(PREDICATE_EQUAL)) {
				hostIsoCountryCodes = addValue(hostIsoCountryCodes, (String) triplet.getObject());
				processed = true;
			} else if (triplet.getSubject().equals(SUBJECT_REGIONCODE) && triplet.getPredicate().equals(PREDICATE_EQUAL)) {
				originRegionCodes = addValue(originRegionCodes, (String) triplet.getObject());
				processed = true;
				//support institutionCode, collectionCode, catalogueNumber  	
      } else if (triplet.getSubject().equals(SUBJECT_INSTITUTIONCODE) && triplet.getPredicate().equals(PREDICATE_EQUAL)) {
        institutionCodes = addValue(institutionCodes, (String) triplet.getObject());
        processed = true;
      } else if (triplet.getSubject().equals(SUBJECT_COLLECTIONCODE) && triplet.getPredicate().equals(PREDICATE_EQUAL)) {
        collectionCodes = addValue(collectionCodes, (String) triplet.getObject());
        processed = true;
      } else if (triplet.getSubject().equals(SUBJECT_CATALOGUENUMBER) && triplet.getPredicate().equals(PREDICATE_EQUAL)) {
        catalogueNumbers = addValue(catalogueNumbers, (String) triplet.getObject());
        processed = true;
			} else if (triplet.getSubject().equals(SUBJECT_CELLID)) {
				if (triplet.getPredicate().equals(PREDICATE_EQUAL)) {
					cellIds = addValue(cellIds, ((Integer) triplet.getObject()).toString());
					processed = true;
				} else if (triplet.getPredicate().equals(PREDICATE_GE)) {
					LatLongBoundingBox box = CellIdUtils.toBoundingBox((Integer) triplet.getObject());
					boundingBox.setLower(box.getMinLat());
					processed = true;
				} else if (triplet.getPredicate().equals(PREDICATE_LE)) {
					LatLongBoundingBox box = CellIdUtils.toBoundingBox((Integer) triplet.getObject());
					boundingBox.setUpper(box.getMaxLat());
					processed = true;
				}
			} else if (triplet.getSubject().equals(SUBJECT_CELLIDMOD360)) {
				if (triplet.getPredicate().equals(PREDICATE_GE)) {
					LatLongBoundingBox box = CellIdUtils.toBoundingBox((Integer) triplet.getObject());
					boundingBox.setLeft(box.getMinLong());
					processed = true;
				} else if (triplet.getPredicate().equals(PREDICATE_LE)) {
					LatLongBoundingBox box = CellIdUtils.toBoundingBox((Integer) triplet.getObject());
					boundingBox.setRight(box.getMaxLong());
					processed = true;
				}
			} else if (triplet.getSubject().equals(SUBJECT_CENTICELLID) && triplet.getPredicate().equals(PREDICATE_EQUAL)) {
				centiCellIds = addValue(centiCellIds, (triplet.getObject()).toString());
				processed = true;
			} else if (    triplet.getSubject().equals(SUBJECT_SCIENTIFICNAME) 
						&& (triplet.getPredicate().equals(PREDICATE_EQUAL) || triplet.getPredicate().equals(PREDICATE_LIKE))) {
				String value = (String) triplet.getObject();
				if (triplet.getPredicate().equals(PREDICATE_LIKE) && value.indexOf("%") < 0) {
					value += "%";
				}
				scientificNames = addValue(scientificNames, value);
				processed = true;
			} else if (triplet.getSubject().equals(SUBJECT_MONTH) && triplet.getPredicate().equals(PREDICATE_EQUAL)) {
				months = addValue(months, (Integer) triplet.getObject());
				processed = true;
			} else if (triplet.getSubject().equals(SUBJECT_DAY) && triplet.getPredicate().equals(PREDICATE_EQUAL)) {
				days = addValue(days, (Integer) triplet.getObject());
				processed = true;
			} else if (triplet.getSubject().equals(SUBJECT_OCCURRENCEDATE)) {
				Date date = (Date) triplet.getObject();
				if (triplet.getPredicate().equals(PREDICATE_EQUAL)) {
					Calendar calendar = Calendar.getInstance();
					calendar.setTime(date);
					calendar.set(Calendar.HOUR, 0);
					calendar.set(Calendar.MINUTE, 0);
					calendar.set(Calendar.SECOND, 0);
					timePeriod.setStartPeriod(calendar.getTime());
					calendar.setTime(date);
					calendar.set(Calendar.HOUR, 23);
					calendar.set(Calendar.MINUTE, 59);
					calendar.set(Calendar.SECOND, 59);
					timePeriod.setEndPeriod(calendar.getTime());
					processed = true;
				} else if (triplet.getPredicate().equals(PREDICATE_GE)) {
					timePeriod.setStartPeriod(date);
					processed = true;
				} else if (triplet.getPredicate().equals(PREDICATE_LE)) {
					timePeriod.setEndPeriod(date);
					processed = true;
				}
			} else if (triplet.getSubject().equals(SUBJECT_YEAR)) {
				if (triplet.getPredicate().equals(PREDICATE_EQUAL)) {
					years = addValue(years, (Integer) triplet.getObject());
					processed = true;
				} else if (triplet.getPredicate().equals(PREDICATE_GE)) {
					startYear = (Integer) triplet.getObject();
					processed = true;
				} else if (triplet.getPredicate().equals(PREDICATE_LE)) {
					endYear = (Integer) triplet.getObject();
					processed = true;
				}
			} else if (triplet.getSubject().equals(SUBJECT_OCCURRENCEMODIFICATIONDATE) && triplet.getPredicate().equals(PREDICATE_GE)) {
				Date date = (Date) triplet.getObject();
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(date);
				calendar.set(Calendar.HOUR, 0);
				calendar.set(Calendar.MINUTE, 0);
				calendar.set(Calendar.SECOND, 0);
				modifiedSince = calendar.getTime();
				processed = true;
			} else if (    (triplet.getSubject().equals(SUBJECT_LATITUDE) || triplet.getSubject().equals(SUBJECT_LONGITUDE))
					    && triplet.getPredicate().equals(PREDICATE_ISNOTNULL)) {
				coordinateStatus = true;
				processed = true;
			} else if (triplet.getSubject().equals(SUBJECT_GEOSPATIALISSUES) && triplet.getPredicate().equals(PREDICATE_EQUAL)) {
				coordinateIssues = (Boolean) triplet.getObject();
				processed = true;
			} else if (triplet.getSubject().equals(SUBJECT_TYPESTATUSCOUNT) && triplet.getPredicate().equals(PREDICATE_G)) {
				typesOnly = true;
				processed = true;
			} 
			
			if (!processed) {
				String errorString = "Could not process triplet: [" + triplet.getSubject() + "],[" + triplet.getPredicate() + "],[" + triplet.getObject() + "]";
				if (throwExceptions) {
					throw new GbifWebServiceException(errorString);
				} else {
					log.error(errorString);
				}
			}
		}
		
		try {
			boundingBox.checkValidity();
			timePeriod.checkValidity();
		} catch (Exception e) {
			throw new GbifWebServiceException("Validity error", e);
		}
	}

	private String[] addValue(String[] existing, String value) {
		String[] values = new String[(existing == null ? 1 : existing.length + 1)];
		
		if (existing != null) {
			for (int i = 0 ; i < existing.length; i++) {
				values[i] = existing[i];
			}
		}
		
		values[values.length - 1] = value;
		
		return values;
	}

	private Integer[] addValue(Integer[] existing, Integer value) {
		Integer[] values = new Integer[(existing == null ? 1 : existing.length + 1)];
		
		if (existing != null) {
			for (int i = 0 ; i < existing.length; i++) {
				values[i] = existing[i];
			}
		}
		
		values[values.length - 1] = value;
		
		return values;
	}

	/**
	 * Return array of values for a parameter - using either the array 
	 * inserted by Dispatcher.processGet under <key>_array or the one
	 * supplied value.
	 * 
	 * @param map
	 * @param key
	 * @param string
	 * @return
	 */
	private String[] getValue(Map<String, Object> map, String key, String string) {
		String[] values = (String[]) map.get(key + Dispatcher.ARRAY_SUFFIX);
		if (values == null) {
			values = new String[1];
			values[0] = string;
		}
		
		return values;
	}

	/**
	 * Return array of Integer values for a parameter - using either the array 
	 * inserted by Dispatcher.processGet under <key>_array or the one
	 * supplied value.
	 * 
	 * @param map
	 * @param key
	 * @param string
	 * @param minValue
	 * @param maxValue
	 * @return
	 */
	private Integer[] getIntegerValue(Map<String, Object> map, String key, String string,
									  Integer minValue, Integer maxValue) {
		String[] originalValues = (String[]) map.get(key + Dispatcher.ARRAY_SUFFIX);
		Integer[] values = null;
		
		if (originalValues == null) {
			values = new Integer[1];

			try {
				values[0] = new Integer(string);
				if (    (minValue != null && values[0] < minValue)
					 || (maxValue != null && values[0] > maxValue)) {
					log.error("Integer value " + string + " out of range (" + minValue + " - " + maxValue + ")");
					values[0] = null;
				}
			} catch (Exception e) {
				log.error("Invalid integer for " + key + ": " + string);
			}
			
			if (values[0] == null) {
				values = null;
			}
		} else {
			values = new Integer[originalValues.length];
			
			int processed = 0;
			for (int i = 0; i < originalValues.length; i++) {
				try {
					values[processed] = new Integer(originalValues[i]);
					if (    (minValue != null && values[processed] < minValue)
							 || (maxValue != null && values[processed] > maxValue)) {
						log.error("Integer value " + originalValues[i] + " out of range (" + minValue + " - " + maxValue + ")");
					} else {
						processed++;
					}
				} catch (Exception e) {
					log.error("Invalid integer for " + key + ": " + originalValues[i]);
				}
			}
			if (processed == 0) {
				values = null;
			} else if (processed < values.length) {
				Integer[] newValues = new Integer[processed];
				for (int i = 0; i < processed; i++) {
					newValues[i] = values[i];
				}
				
				values = newValues;
			}
		}
		
		return values;
	}

	/**
	 * Returns the request type name for a given KVP set.
	 */
	public String getRequestTypeName() {
		return Action.getRequestTypeName(requestType);
	}

	private int getMode(String modeString) {
		int mode = MODE_PROCESSED;
		
		if (modeString != null) {
			if (modeString.equals(MODENAME_RAW)) {
				mode = MODE_RAW;
			}
		}
		
		return mode;
	}
	
	public String getModeName() {
		String name = MODENAME_PROCESSED;
		
		switch(mode) {
		case MODE_RAW: name = MODENAME_RAW;
		}
		
		return name;
	}
	
	public Map<String, Object> getParameterMap(Integer overrideRequestType) {
		Map<String,Object> map = super.getParameterMap(overrideRequestType);
		int rt = (overrideRequestType == null) ? requestType : overrideRequestType.intValue();
		if (rt != Action.SCHEMA && rt != Action.STYLESHEET) {
			if (requestType == Action.COUNT || requestType == Action.LIST) {
				if (taxonConceptKeys != null) map.put(KEY_TAXONCONCEPTKEY, taxonConceptKeys);
				if (scientificNames != null) map.put(KEY_SCIENTIFICNAME, scientificNames);
				if (dataProviderKeys != null) map.put(KEY_DATAPROVIDERKEY, dataProviderKeys);
				if (dataResourceKeys != null) map.put(KEY_DATARESOURCEKEY, dataResourceKeys);
				if (resourceNetworkKeys != null) map.put(KEY_RESOURCENETWORKKEY, resourceNetworkKeys);
				if (basisOfRecordCodes != null) map.put(KEY_BASISOFRECORDCODE, basisOfRecordCodes);
				if (hostIsoCountryCodes != null) map.put(KEY_HOSTISOCOUNTRYCODE, hostIsoCountryCodes);
				if (originIsoCountryCodes != null) map.put(KEY_ORIGINISOCOUNTRYCODE, originIsoCountryCodes);
				if (originRegionCodes != null) map.put(KEY_ORIGINREGIONCODE, originRegionCodes);
				if (cellIds != null) map.put(KEY_CELLID, cellIds);
				if (centiCellIds != null) map.put(KEY_CENTICELLID, centiCellIds);
				if (boundingBox.getLower() != null) map.put(KEY_MINLATITUDE, boundingBox.getLower().toString());
				if (boundingBox.getUpper() != null) map.put(KEY_MAXLATITUDE, boundingBox.getUpper().toString());
				if (boundingBox.getLeft() != null) map.put(KEY_MINLONGITUDE, boundingBox.getLeft().toString());
				if (boundingBox.getRight() != null) map.put(KEY_MAXLONGITUDE, boundingBox.getRight().toString());
				if (timePeriod.getStartPeriod() != null) map.put(KEY_STARTDATE, formatDate(timePeriod.getStartPeriod()));
				if (timePeriod.getEndPeriod() != null) map.put(KEY_ENDDATE, formatDate(timePeriod.getEndPeriod()));
				if (startYear != null) map.put(KEY_STARTYEAR, startYear.toString());
				if (endYear != null) map.put(KEY_ENDYEAR, endYear.toString());
				if (years != null) map.put(KEY_YEAR, getStringArray(years));
				if (months != null) map.put(KEY_MONTH, getStringArray(months));
				if (days != null) map.put(KEY_DAY, getStringArray(days));
				if (coordinateStatus != null) map.put(KEY_COORDINATESTATUS, coordinateStatus.toString());
				if (coordinateIssues != null) map.put(KEY_COORDINATEISSUES, coordinateIssues.toString());
				if (typesOnly) map.put(KEY_TYPESONLY, "true");
				if (minAltitude != null) map.put(KEY_MINALTITUDE, minAltitude);
				if (maxAltitude != null) map.put(KEY_MAXALTITUDE, maxAltitude);
				if (minDepth != null) map.put(KEY_MINDEPTH, minDepth);
				if (maxDepth != null) map.put(KEY_MAXDEPTH, maxDepth);	
        //support institutionCode, collectionCode, catalogueNumber
        if (institutionCodes != null) map.put(KEY_INSTITUTIONCODE, institutionCodes);
        if (collectionCodes != null) map.put(KEY_COLLECTIONCODE, collectionCodes);
        if (catalogueNumbers != null) map.put(KEY_CATALOGUENUMBER, catalogueNumbers);  				
			}
			if (requestType == Action.LIST || requestType == Action.GET) {
				map.put(KEY_MODE, getModeName());
				map.put(KEY_FORMAT, getFormatName());
				if (format == FORMAT_KML) {
					if (kmlIconUrl != null) map.put(KEY_ICON, kmlIconUrl);
				}
			}
			if (requestType == Action.GET) {
				if (key != null) map.put(KEY_KEY, key);
			}
		}
		return map;
	}

	/**
	 * @param integers
	 * @return
	 */
	private String[] getStringArray(Integer[] integers) {
		String[] strings = new String[integers.length];
		
		for (int i = 0; i < integers.length; i++) {
			strings[i] = integers[i].toString();
		}
		
		return strings;
	}
	
  //support institutionCode, collectionCode, catalogueNumber
  /**
   * @return the institutionCodes.
   */
  public String[] getInstitutionCodes() {
    return institutionCodes;
  }

  
  /**
   * @return the collectionCodes.
   */
  public String[] getCollectionCodes() {
    return collectionCodes;
  }

  
  /**
   * @return the catalogueNumbers.
   */
  public String[] getCatalogueNumbers() {
    return catalogueNumbers;
  }

  /**
	 * @return the basisOfRecordCode
	 */
	public String[] getBasisOfRecordCodes() {
		return basisOfRecordCodes;
	}

	/**
	 * @return the boundingBox
	 */
	public BoundingBoxDTO getBoundingBox() {
		return boundingBox;
	}

	/**
	 * @return the dataProviderKey
	 */
	public String[] getDataProviderKeys() {
		return dataProviderKeys;
	}

	/**
	 * @return the dataResourceKey
	 */
	public String[] getDataResourceKeys() {
		return dataResourceKeys;
	}

	/**
	 * @return the resourceNetworkKey
	 */
	public String[] getResourceNetworkKeys() {
		return resourceNetworkKeys;
	}

	/**
	 * @return the hostIsoCountryCode
	 */
	public String[] getHostIsoCountryCodes() {
		return hostIsoCountryCodes;
	}

	/**
	 * @return the originIsoCountryCode
	 */
	public String[] getOriginIsoCountryCodes() {
		return originIsoCountryCodes;
	}

	/**
	 * @return the scientificName
	 */
	public String[] getScientificNames() {
		return scientificNames;
	}

	/**
	 * @return the taxonConceptKey
	 */
	public String[] getTaxonConceptKeys() {
		return taxonConceptKeys;
	}

	/**
	 * @return the timePeriod
	 */
	public TimePeriodDTO getTimePeriod() {
		return timePeriod;
	}

	/**
	 * @return the kmlIconUrl
	 */
	public String getKmlIconUrl() {
		return kmlIconUrl;
	}

	/**
	 * @return the key
	 */
	public String getKey() {
		return key;
	}
	
	/**
	 * @return the cellId
	 */
	public String[] getCellIds() {
		return cellIds;
	}

	/**
	 * @return the coordinateStatus
	 */
	public Boolean getCoordinateStatus() {
		return coordinateStatus;
	}

	/**
	 * @return the coordinateIssues
	 */
	public Boolean getCoordinateIssues() {
		return coordinateIssues;
	}

	/**
	 * @return the endYear
	 */
	public Integer getEndYear() {
		return endYear;
	}

	/**
	 * @return the months
	 */
	public Integer[] getMonths() {
		return months;
	}

	/**
	 * @return the originRegionCodes
	 */
	public String[] getOriginRegionCodes() {
		return originRegionCodes;
	}

	/**
	 * @return the startYear
	 */
	public Integer getStartYear() {
		return startYear;
	}

	/**
	 * @return the years
	 */
	public Integer[] getYears() {
		return years;
	}

	/**
	 * @return the centiCellIds
	 */
	public String[] getCentiCellIds() {
		return centiCellIds;
	}

	/**
	 * @return the days
	 */
	public Integer[] getDays() {
		return days;
	}

	/**
	 * @return the typesOnly
	 */
	public boolean getTypesOnly() {
		return typesOnly;
	}

	/**
	 * @return the mode
	 */
	public int getMode() {
		return mode;
	}
	
	public List<PropertyStoreTripletDTO> getTriplets(TaxonomyManager taxonomyManager) {
		List<PropertyStoreTripletDTO> triplets = new ArrayList<PropertyStoreTripletDTO>();
		if (dataResourceKeys != null) {
			for (int i = 0; i < dataResourceKeys.length; i++) {
				addTriplet(triplets, SUBJECT_DATARESOURCEID, PREDICATE_EQUAL, new Long(dataResourceKeys[i]));
			}
		}
		if (dataProviderKeys != null) {
			for (int i = 0; i < dataProviderKeys.length; i++) {
				addTriplet(triplets, SUBJECT_DATAPROVIDERID, PREDICATE_EQUAL, new Long(dataProviderKeys[i]));
			}
		}
		if (resourceNetworkKeys != null) {
			for (int i = 0; i < resourceNetworkKeys.length; i++) {
				addTriplet(triplets, SUBJECT_RESOURCENETWORKID, PREDICATE_EQUAL, new Long(resourceNetworkKeys[i]));
			}
		}
		if (taxonConceptKeys != null) {
			for (int i = 0; i < taxonConceptKeys.length; i++) {
				try {
					BriefTaxonConceptDTO taxon = taxonomyManager.getTaxonConceptFor(taxonConceptKeys[i]);
					if (taxon != null && !taxon.getIsNubConcept()) {
						taxon = taxonomyManager.getTaxonConceptFor(taxon.getPartnerConceptKey());
					}
					if (taxon != null) {
						String subject = SUBJECT_TAXONCONCEPTID;
						switch(taxon.getRankValue()) {
						case 1000: subject = SUBJECT_KINGDOMID; break;
						case 2000: subject = SUBJECT_PHYLUMID; break;
						case 3000: subject = SUBJECT_CLASSID; break;
						case 4000: subject = SUBJECT_ORDERID; break;
						case 5000: subject = SUBJECT_FAMILYID; break;
						case 6000: subject = SUBJECT_GENUSID; break;
						case 7000: subject = SUBJECT_SPECIESID; break;
						}
						addTriplet(triplets, subject, PREDICATE_EQUAL, new Long(taxon.getKey()));
					}
				} catch (Exception e) {
					log.error("Failed to get taxon " + taxonConceptKeys[i]);
				}
			}
		}
		if (scientificNames != null) {
			for (int i = 0; i < scientificNames.length; i++) {
				String name = scientificNames[i].replace("*", "%");
				String predicate = (name.indexOf("%") >= 0) ? PREDICATE_LIKE : PREDICATE_EQUAL;
				addTriplet(triplets, SUBJECT_SCIENTIFICNAME, predicate, name);
			}
		}
		if (hostIsoCountryCodes != null) {
			for (int i = 0; i < hostIsoCountryCodes.length; i++) {
				addTriplet(triplets, SUBJECT_HOSTCOUNTRYCODE, PREDICATE_EQUAL, hostIsoCountryCodes[i]);
			}
		}
		if (originIsoCountryCodes != null) {
			for (int i = 0; i < originIsoCountryCodes.length; i++) {
				addTriplet(triplets, SUBJECT_ISOCOUNTRYCODE, PREDICATE_EQUAL, originIsoCountryCodes[i]);
			}
		}
		if (originRegionCodes != null) {
			for (int i = 0; i < originRegionCodes.length; i++) {
				addTriplet(triplets, SUBJECT_REGIONCODE, PREDICATE_EQUAL, originRegionCodes[i]);
			}
		}
		if (basisOfRecordCodes != null) {
			for (int i = 0; i < basisOfRecordCodes.length; i++) {
				addTriplet(triplets, SUBJECT_BASISOFRECORD, PREDICATE_EQUAL, BasisOfRecord.getBasisOfRecord(basisOfRecordCodes[i]).getValue());
			}
		}
	  //support institutionCode, collectionCode, catalogueNumber		
    if (institutionCodes != null) {
      for (int i = 0; i < institutionCodes.length; i++) {
        addTriplet(triplets, SUBJECT_INSTITUTIONCODE, PREDICATE_EQUAL, institutionCodes[i]);
      }
    }		
    if (collectionCodes != null) {
      for (int i = 0; i < collectionCodes.length; i++) {
        addTriplet(triplets, SUBJECT_COLLECTIONCODE, PREDICATE_EQUAL, collectionCodes[i]);
      }
    }   
    if (catalogueNumbers != null) {
      for (int i = 0; i < catalogueNumbers.length; i++) {
        addTriplet(triplets, SUBJECT_CATALOGUENUMBER, PREDICATE_EQUAL, catalogueNumbers[i]);
      }
    }       
		
		if (cellIds != null) {
			for (int i = 0; i < cellIds.length; i++) {
				addTriplet(triplets, SUBJECT_CELLID, PREDICATE_EQUAL, new Integer(cellIds[i]));
			}
		}
		if (centiCellIds != null) {
			for (int i = 0; i < centiCellIds.length; i++) {
				addTriplet(triplets, SUBJECT_CENTICELLID, PREDICATE_EQUAL, new Integer(centiCellIds[i]));
			}
		}
		if (boundingBox.getLeft() != null && boundingBox.getLower() != null && boundingBox.getRight() != null && boundingBox.getUpper() != null) {
			List<PropertyStoreTripletDTO> cellIdTriplets = BoundingBoxUtils.getTripletsFromLatLongBoundingBox(TRIPLET_NAMESPACE, new LatLongBoundingBox(boundingBox.getLeft(), boundingBox.getLower(), boundingBox.getRight(), boundingBox.getUpper()));
			triplets.addAll(cellIdTriplets);
		}
		if (timePeriod.getStartPeriod() != null) {
			addTriplet(triplets, SUBJECT_OCCURRENCEDATE, PREDICATE_GE, timePeriod.getStartPeriod());
		}
		if (timePeriod.getEndPeriod() != null) {
			addTriplet(triplets, SUBJECT_OCCURRENCEDATE, PREDICATE_LE, timePeriod.getEndPeriod());
		}
		if (startYear != null) {
			addTriplet(triplets, SUBJECT_YEAR, PREDICATE_GE, startYear);
		}
		if (endYear != null) {
			addTriplet(triplets, SUBJECT_YEAR, PREDICATE_LE, endYear);
			addTriplet(triplets, SUBJECT_YEAR, PREDICATE_NEQUAL, new Integer(0));
		}
		if (startYear != null || endYear != null) {
			addTriplet(triplets, SUBJECT_YEAR, PREDICATE_ISNOTNULL, null);
		}
		if (years != null) {
			for (int i = 0; i < years.length; i++) {
				addTriplet(triplets, SUBJECT_YEAR, PREDICATE_EQUAL, years[i]);
			}
		}
		if (months != null) {
			for (int i = 0; i < months.length; i++) {
				addTriplet(triplets, SUBJECT_MONTH, PREDICATE_EQUAL, months[i]);
			}
		}
		if (days != null) {
			for (int i = 0; i < days.length; i++) {
				addTriplet(triplets, SUBJECT_DAY, PREDICATE_EQUAL, days[i]);
			}
		}
		if (modifiedSince != null) {
			addTriplet(triplets, SUBJECT_OCCURRENCEMODIFICATIONDATE, PREDICATE_GE, modifiedSince);
		}
		if (coordinateStatus != null) {
			addTriplet(triplets, SUBJECT_LATITUDE, PREDICATE_ISNOTNULL, null);
			addTriplet(triplets, SUBJECT_LONGITUDE, PREDICATE_ISNOTNULL, null);
		}
		if (coordinateIssues != null) {
			addTriplet(triplets, SUBJECT_GEOSPATIALISSUES, coordinateIssues ? PREDICATE_NEQUAL : PREDICATE_EQUAL, new Integer(0));
		}
		if (typesOnly) {
			addTriplet(triplets, SUBJECT_TYPESTATUSCOUNT, PREDICATE_G, new Integer(0));
		}
		if (minAltitude != null) {
			addTriplet(triplets, SUBJECT_ALTITUDE, PREDICATE_GE, minAltitude);
		}
		if (maxAltitude != null) {
			addTriplet(triplets, SUBJECT_ALTITUDE, PREDICATE_LE, maxAltitude);
		}		
		if (minDepth != null) {
			addTriplet(triplets, SUBJECT_DEPTH, PREDICATE_GE, minDepth);
		}
		if (maxDepth != null) {
			addTriplet(triplets, SUBJECT_DEPTH, PREDICATE_LE, maxDepth);
		}	
		if (requestType == Action.COUNT) {
			addTriplet(triplets, SUBJECT_SELECTFIELD, PREDICATE_RETURN, RETURNFIELDS_COUNT);
		}
			
		return triplets;
	}
	
	private void addTriplet(List<PropertyStoreTripletDTO> triplets, String subject, String predicate, Object object) {
		PropertyStoreTripletDTO triplet = new PropertyStoreTripletDTO();
		triplet.setSubject(subject);
		triplet.setPredicate(predicate);    		
		triplet.setObject(object);
		triplet.setNamespace(TRIPLET_NAMESPACE);
		triplets.add(triplet);
	}
}