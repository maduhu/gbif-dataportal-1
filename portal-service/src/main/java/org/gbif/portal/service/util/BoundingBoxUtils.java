package org.gbif.portal.service.util;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.gbif.portal.dto.PropertyStoreTripletDTO;
import org.gbif.portal.util.geospatial.CellIdUtils;
import org.gbif.portal.util.geospatial.LatLongBoundingBox;
import org.gbif.portal.util.geospatial.UnableToGenerateCellIdException;

public class BoundingBoxUtils {

	protected static Log logger = LogFactory.getLog(BoundingBoxUtils.class);	
	
	protected static String subject="SERVICE.OCCURRENCE.QUERY.SUBJECT.BOUNDINGBOX";
	
	protected static String latitudeSubject="SERVICE.OCCURRENCE.QUERY.SUBJECT.LATITUDE";
	protected static String longitudeSubject="SERVICE.OCCURRENCE.QUERY.SUBJECT.LONGITUDE";

	protected static String centiCellIdSubject="SERVICE.OCCURRENCE.QUERY.SUBJECT.CENTICELLID";
	protected static String cellIdSubject="SERVICE.OCCURRENCE.QUERY.SUBJECT.CELLID";
	protected static String cellIdMod360Subject="SERVICE.OCCURRENCE.QUERY.SUBJECT.CELLID.MOD360";
	
	protected static String equalsPredicate = "SERVICE.QUERY.PREDICATE.EQUAL";
	protected static String lessThanPredicate = "SERVICE.QUERY.PREDICATE.L";
	protected static String lessThanOrEqualPredicate = "SERVICE.QUERY.PREDICATE.LE";	
	protected static String greaterThanOrEqualPredicate = "SERVICE.QUERY.PREDICATE.GE";
	

	public static List<PropertyStoreTripletDTO> getTripletsFromLatLongBoundingBox(String namespace, LatLongBoundingBox llbb) {
		List<PropertyStoreTripletDTO> triplets = new ArrayList<PropertyStoreTripletDTO>();
		
		try {
			//check if llbb is a centi cell - i.e. 0.1 x 0.1 - this should be the case for zoom level 6 cells 
			try {
				Integer[] centiCell = CellIdUtils.getCentiCellIdForBoundingBox(llbb.getMinLong(), llbb.getMinLat(), llbb.getMaxLong(), llbb.getMaxLat());
				if(centiCell!=null && centiCell.length>0){
					//add a triplet for cell id
					triplets.add(new PropertyStoreTripletDTO(namespace, cellIdSubject, equalsPredicate, centiCell[0]));
					if(centiCell.length==2){
						//add a triplet for centi cell id
						triplets.add(new PropertyStoreTripletDTO(namespace, centiCellIdSubject, equalsPredicate, centiCell[1]));
					}
					// All done
					return triplets;
				}
			} catch (UnableToGenerateCellIdException e) {
				logger.error(e.getMessage(), e);
			}
			
			int[] minMaxCellIds = CellIdUtils.getMinMaxCellIdsForBoundingBox(llbb.getMinLong(), llbb.getMinLat(), llbb.getMaxLong(), llbb.getMaxLat());
			if(logger.isDebugEnabled()){
				logger.debug("Min cell id: "+minMaxCellIds[0]+", max cell id: "+minMaxCellIds[1]);
			}
			//check for adjacent cells - used in zoom level 6 - "view all occurrences in the viewed area"
			if(minMaxCellIds[1]-minMaxCellIds[0]<2){
				triplets.add(new PropertyStoreTripletDTO(namespace, cellIdSubject,greaterThanOrEqualPredicate, minMaxCellIds[0]));			
				triplets.add(new PropertyStoreTripletDTO(namespace, cellIdSubject,lessThanOrEqualPredicate, minMaxCellIds[1]));	
				triplets.add(new PropertyStoreTripletDTO(namespace, latitudeSubject, greaterThanOrEqualPredicate, llbb.getMinLat()));
				triplets.add(new PropertyStoreTripletDTO(namespace, latitudeSubject, lessThanOrEqualPredicate, llbb.getMaxLat()));
				triplets.add(new PropertyStoreTripletDTO(namespace, longitudeSubject, greaterThanOrEqualPredicate, llbb.getMinLong()));
				triplets.add(new PropertyStoreTripletDTO(namespace, longitudeSubject, lessThanOrEqualPredicate, llbb.getMaxLong()));
			} else {
				triplets.add(new PropertyStoreTripletDTO(namespace, cellIdSubject, greaterThanOrEqualPredicate,minMaxCellIds[0]));			
				triplets.add(new PropertyStoreTripletDTO(namespace, cellIdSubject, lessThanOrEqualPredicate, minMaxCellIds[1]));			
				triplets.add(new PropertyStoreTripletDTO(namespace, cellIdMod360Subject, greaterThanOrEqualPredicate, minMaxCellIds[0] % 360));
				
				int maxCellIdmod360 = minMaxCellIds[1] % 360;
				if(maxCellIdmod360==0){
					maxCellIdmod360=360;
				}
				
				triplets.add(new PropertyStoreTripletDTO(namespace, cellIdMod360Subject, lessThanOrEqualPredicate, maxCellIdmod360));
			}
		} catch (UnableToGenerateCellIdException e) {
			logger.error(e.getMessage(), e);
		}

		return triplets;
	}
}
