package org.gbif.portal;

import org.gbif.portal.dao.CellCountryDAO;
import org.gbif.portal.dao.GbifLogMessageDAO;
import org.gbif.portal.dao.OccurrenceRecordDAO;
import org.gbif.portal.model.CellCountry;
import org.gbif.portal.model.GbifLogMessage;
import org.gbif.portal.model.OccurrenceRecord;
import org.gbif.portal.util.db.OccurrenceRecordUtils;
import org.gbif.portal.util.geospatial.CellIdUtils;
import org.gbif.portal.util.geospatial.UnableToGenerateCellIdException;
import org.gbif.portal.util.log.LogEvent;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.URIException;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GeospatialIssueResolver {
	/**
	 * Logger
	 */
	protected Log logger = LogFactory.getLog(getClass());   
	
	protected static int geospatial_parse_issue = 32;
	
	protected static HttpClient client =  new HttpClient(new MultiThreadedHttpConnectionManager());

	CellCountryDAO cellCountryDAO;
	OccurrenceRecordDAO occurrenceRecordDAO;
	GbifLogMessageDAO gbifLogMessageDAO;
	
	public void resolve() {
		// cache to hold lat long - country code key - value pairs
		Map<Integer, String> cache = new HashMap<Integer, String>();
		
		// get list of occurence records having geospatial issue = 32
		List<OccurrenceRecord> occurrenceRecordList = occurrenceRecordDAO.getByGeospatialIssueNumber(geospatial_parse_issue);
		logger.info("OccurrenceRecord List size: "+String.valueOf(occurrenceRecordList.size()));
		
		for (OccurrenceRecord or: occurrenceRecordList) {
			
			String orCountryCode = or.getIsoCountryCode();
			Float orLatitude = or.getLatitude();
			Float orLongitude = or.getLongitude();
			
			logger.info("Resolving geospatial issue for occurrence_record with id="
					+String.valueOf(or.getId())
					+", latitude="+String.valueOf(orLatitude)
					+" , longitude="+String.valueOf(orLongitude)
					+", and iso_country_code="+orCountryCode);
			
			// want to find actual country via reverse geocoding
			String countryCode = null;
			
			NameValuePair latlng = new NameValuePair("latlng", String.valueOf(orLatitude)+","+String.valueOf(orLongitude));
			int hashCode = latlng.hashCode();
			//logger.info("HashCode="+String.valueOf(hashCode));
			
			// check cache - do we need to send a request?
			if (cache.containsKey(hashCode)) {
				countryCode = cache.get(hashCode);
				logger.info("We've seen this combination of lat long before! - using cache");
			} else {
				logger.info("This combination of lat long has not been seen before");
			}
			
			// make a request if country was not in cache
			if (countryCode==null) {
				// construct request URI from latitude longitude
				String requestUri = "http://maps.google.com/maps/api/geocode/json";
				
				// construct additional name value pairs
				NameValuePair sensor = new NameValuePair("sensor", "false" );

				// construct get method
				GetMethod method = newHttpGet(requestUri, new NameValuePair[]{latlng, sensor});
			
				String response = null;
				try {
					client.executeMethod(method);
					
					// Dump to String, use a proper buffered reader and expect UTF-8
					response = IOUtils.toString(method.getResponseBodyAsStream(), "UTF-8");
				} catch (Exception e) {
					logger.error("The request could not be executed",e);
				} finally {
					method.releaseConnection();
				}
				
				try {
					JSONObject jsonResponse = null;
					if (response!=null) {
						jsonResponse = new JSONObject(response);
						logger.info("response: "+jsonResponse.toString());
					}
					
					if (jsonResponse!=null) {
						if (jsonResponse.get("status").equals("OK")) {
							JSONArray result = jsonResponse.getJSONArray("results");
							//logger.info("inner response:"+result.toString());
							JSONObject innerResult = result.getJSONObject(0);
							if (innerResult.has("address_components")) {
								JSONArray innerResultArray = innerResult.getJSONArray("address_components");
								for (int k=0; k<innerResultArray.length(); k++) {
									JSONObject innerResultArrayObject = innerResultArray.getJSONObject(k);
									//logger.info("types object: "+innerResultArrayObject.toString());
									if (innerResultArrayObject.has("types")) {
										String types = innerResultArrayObject.get("types").toString();
										if (types.contains("country")) {
											countryCode = innerResultArrayObject.getString("short_name");
											logger.info("Reverse geocoding completed: countryCode="+countryCode);
											if (StringUtils.isNotBlank(countryCode)) {
												// add entry to cache
												cache.put(latlng.hashCode(), countryCode);
												
												// calculate the cell id
												int cellId = -1;
												try {
													cellId = CellIdUtils.toCellId(orLatitude, orLongitude);
												} catch (UnableToGenerateCellIdException e) {
													// TODO Auto-generated catch block
													e.printStackTrace();
												} 
												//logger.info("The cell id is: "+String.valueOf(cellId));
												
												// does a cell_country record exist for that cell id and country code?
												CellCountry cellCountry = null;
												if (cellId>=0){
													cellCountry = cellCountryDAO.getByCellIdAndIsoCountryCode(cellId, countryCode);
												}
												
												// if it doesn't add the cell_country record
												if (cellCountry==null && cellId>=0 && countryCode!=null) {
													logger.info("Creating new cell country record");
													cellCountry = new CellCountry(cellId, countryCode);
													cellCountryDAO.create(cellCountry);	
												} else {
													logger.info("cell_country record already exists for cell_id="+String.valueOf(cellId)+" and iso_country_code="+countryCode);
												}
											} else {
												logger.error("The countryCode was null or empty, as per the response");
											}
										}
									}
								}
							}			
						} else if (jsonResponse.get("status").equals("ZERO_RESULTS")) {
							logger.error("This latlng could not be reverse geocoded! Likely, the latlng corresponds to a remote location");
						} else if (jsonResponse.get("status").equals("OVER_QUERY_LIMIT")) {
							logger.error("The request quota has been surpassed");
						} else if (jsonResponse.get("status").equals("REQUEST_DENIED") || jsonResponse.get("status").equals("INVALID_REQUEST")) {
							logger.error("The request has been denied - please check the request is valid: "+method.getURI().toString());
						}
					} else {
						logger.error("The response was empty from request: "+method.getURI().toString());
					}
				} catch (JSONException e) {
					logger.error("There was a problem parsing the JSON response",e);
				} catch (URIException e) {
					logger.error("There was a problem getting the URI", e);
				} 
			} 
			
			if (countryCode!=null) {				
				// does the OR's country code match the one we found?
				if (orCountryCode.equalsIgnoreCase(countryCode)) {
					logger.info("**The occurrence_record is correct: iso_country_code="+countryCode);
					// remove geospatial issue from Occurrence Record
					logger.info("Removing geospatial issue flag #"+OccurrenceRecordUtils.GEOSPATIAL_COUNTRY_COORDINATE_MISMATCH+" as the iso_country_code is correct");
					or.setGeospatialIssue(OccurrenceRecordUtils.NO_ISSUES);
					occurrenceRecordDAO.update(or);
					
					// remove gbif_log_message
					int affected = gbifLogMessageDAO.deleteGeospatialIssueEventsByOccurrenceIdAndEventId(or.getId());
					logger.info(String.valueOf(affected)+ " gbif_log_message records were deleted having occurrence_id="+String.valueOf(or.getId())+" and event_id=1008");
					
				} else {
					logger.info("The occurrence_record is incorrect: iso_country_code is="+orCountryCode+", but should be="+countryCode);
					// remove flag
					or.setGeospatialIssue(OccurrenceRecordUtils.NO_ISSUES);
					
					// give more informative gbif_log_message
					GbifLogMessage gbifLogMessage = gbifLogMessageDAO.getByOccurrenceIdAndGeospatialIssue(or.getId(), LogEvent.EXTRACT_GEOSPATIALISSUE.getValue());
					
					if (gbifLogMessage!=null) {
						String newMessage = "(Lat: "+String.valueOf(orLatitude)+", Lon: "+String.valueOf(orLongitude)+"): Geospatial issue: coordinates correspond to country ("+countryCode+") not ("+orCountryCode+")";
						gbifLogMessage.setMessage(newMessage);
						gbifLogMessageDAO.update(gbifLogMessage);
						logger.info("The gbif_log_message's message field has been updated to indicate the country to which the lat long values actually belong (determined from Reverse Geocoding)");
					} else {
						logger.warn("No gbif_log_message actually exists with occurrence_id="+String.valueOf(or.getId())+" and event_id="+String.valueOf(LogEvent.EXTRACT_GEOSPATIALISSUE.getValue()));
					}
				}
			} else {
				logger.error("The countryCode could not be reverse geocoded!");
			}	
		} //end for
	}
	
	/**
	 * Generate GetMethod
	 * 
	 * @param url
	 * @param nameValuePairs
	 * @return GetMethod
	 */
	private GetMethod newHttpGet(String url, NameValuePair[] nameValuePairs){
		GetMethod method = new GetMethod(url);
        method.setFollowRedirects(true);
		method.setDoAuthentication(false);
		method.setQueryString(nameValuePairs);
        return method;
	}
	
	public CellCountryDAO getCellCountryDAO() {
		return cellCountryDAO;
	}
	public void setCellCountryDAO(CellCountryDAO cellCountryDAO) {
		this.cellCountryDAO = cellCountryDAO;
	}
	public OccurrenceRecordDAO getOccurrenceRecordDAO() {
		return occurrenceRecordDAO;
	}
	public void setOccurrenceRecordDAO(OccurrenceRecordDAO occurrenceRecordDAO) {
		this.occurrenceRecordDAO = occurrenceRecordDAO;
	}
	public GbifLogMessageDAO getGbifLogMessageDAO() {
		return gbifLogMessageDAO;
	}

	public void setGbifLogMessageDAO(GbifLogMessageDAO gbifLogMessageDAO) {
		this.gbifLogMessageDAO = gbifLogMessageDAO;
	}
	
}
