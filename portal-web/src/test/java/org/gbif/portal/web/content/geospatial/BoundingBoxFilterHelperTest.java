/**
 * 
 */
package org.gbif.portal.web.content.geospatial;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.gbif.portal.dto.PropertyStoreTripletDTO;

/**
 * @author dave
 */
public class BoundingBoxFilterHelperTest extends TestCase {

	public void testPreProcess(){
		BoundingBoxFilterHelper bbfh = new BoundingBoxFilterHelper();
		
		List<PropertyStoreTripletDTO> triplets = new ArrayList<PropertyStoreTripletDTO>();
		triplets.add(new PropertyStoreTripletDTO(null, "SERVICE.OCCURRENCE.QUERY.SUBJECT.BOUNDINGBOX", "SERVICE.QUERY.PREDICATE.EQUAL", "1.0,51.0,2.0,52.0"));
		
		bbfh.preProcess(triplets, null, null);
		System.out.println(triplets.size());
		System.out.println(triplets.get(0));
		//System.out.println(triplets.get(1));
	}
	
}
