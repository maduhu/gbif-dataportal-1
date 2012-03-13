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

package org.gbif.portal.util.geospatial;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import junit.framework.TestCase;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author tim
 * @author dave
 */
public class CellIdUtilsTest extends TestCase {
	
	protected static Log logger = LogFactory.getLog(CellIdUtilsTest.class);	

	
	public void testMyTest(){
		
		try {
			//30.0E,5.0S,50.0E,5.0N
			//logger.info(CellIdUtils.toCellId(-5f, 30f));
			//logger.info(CellIdUtils.toCellId(5f, 50f));
			
			logger.info(CellIdUtils.toCellId(48.843299865723f, 38.752498626709f));
			
			logger.info(CellIdUtils.toCellId(48.8f, 38.7f));
			
		} catch (UnableToGenerateCellIdException e) {
			e.printStackTrace();
		}		
	}
	
	
	
	/**
	 * Test method for {@link org.gbif.portal.util.geospatial.CellIdUtils#toCellId(java.lang.Float, java.lang.Float)}.
	 */
	public void testCreateCellId() {
		try {
			assertEquals(0, CellIdUtils.toCellId(new Float(-90), new Float(-180)));
			assertEquals(0, CellIdUtils.toCellId(new Float(-90), new Float(-179.9)));
			assertEquals(359, CellIdUtils.toCellId(new Float(-89.1), new Float(179.2)));
			assertEquals(360, CellIdUtils.toCellId(new Float(-88.5), new Float(-180)));
			assertEquals(6647, CellIdUtils.toCellId(new Float(-71.31), new Float(-12.4233)));
		} catch (UnableToGenerateCellIdException e) {
			fail(e.toString());
		}
		try {
			CellIdUtils.toCellId(new Float(-100), null);
			fail("-100, null should throw exception");
		} catch (UnableToGenerateCellIdException e) {
		}
	}

	/**
	 * Test method for {@link org.gbif.portal.util.geospatial.CellIdUtils#toCentiCellId(java.lang.Float, java.lang.Float)}.
	 */
	public void testCreateCentiCellId() {
		try {
			assertEquals(0, CellIdUtils.toCentiCellId(new Float(-90.0), new Float(170)));
			assertEquals(0, CellIdUtils.toCentiCellId(new Float(89.05), new Float(179.05)));
			assertEquals(99, CellIdUtils.toCentiCellId(new Float(-89.05), new Float(-179.05)));
			assertEquals(10, CellIdUtils.toCentiCellId(new Float(10.15), new Float(10.03)));
			assertEquals(0, CellIdUtils.toCentiCellId(new Float(-88.99), new Float(-179.99)));
			assertEquals(67, CellIdUtils.toCentiCellId(new Float(41.6128), new Float(-87.2192)));
			assertEquals(0, CellIdUtils.toCentiCellId(new Float(-41), new Float(-87)));
		} catch (UnableToGenerateCellIdException e) {
			fail(e.toString());
		}
		try {
			CellIdUtils.toCellId(new Float(-100), null);
			fail("-100, null should throw exception");
		} catch (UnableToGenerateCellIdException e) {
		}
	}
	
	/**
	 * Test method for {@link org.gbif.portal.util.geospatial.CellIdUtils#toBoundingBox(int)}.
	 */
	public void testToBoundingBox() {
		assertEquals(new LatLongBoundingBox(-180,-90, -179, -89), CellIdUtils.toBoundingBox(0));
		assertEquals(new LatLongBoundingBox(-179, -90,-178, -89), CellIdUtils.toBoundingBox(1));
		assertEquals(new LatLongBoundingBox(-180, -89, -179, -88), CellIdUtils.toBoundingBox(360));
		assertEquals(new LatLongBoundingBox(-13,-72,-12, -71), CellIdUtils.toBoundingBox(6647));
	}	
	
	/**
	 * Test method for {@link org.gbif.portal.util.geospatial.CellIdUtils#toBoundingBox(int,int)}.
	 */
	public void testToBoundingBox2() {
		assertEquals(new LatLongBoundingBox((float)-180.0,(float)-90.0,(float)-179.9, (float)-89.9), CellIdUtils.toBoundingBox(0,0));
		assertEquals(new LatLongBoundingBox((float)-180.0, (float)-89.0,(float)-179.9,(float)-88.9), CellIdUtils.toBoundingBox(360,0));
		assertEquals(new LatLongBoundingBox((float)-179.9,(float)-88.9,(float)-179.8, (float)-88.8), CellIdUtils.toBoundingBox(360,11));
	}
	
	/**
	 * The cells enclosed by should return cells that are partially enclosed also 
	 * Test method for {@link org.gbif.portal.util.geospatial.CellIdUtils#getCellsEnclosedBy(float,float,float,float)}.
	 */
	public void testGetCellsEnclosedBy() {
		try {
			Set<Integer> results = CellIdUtils.getCellsEnclosedBy(-90, -89, -180, -179);
			assertTrue(results.contains(0));
			assertTrue(results.size()==1);
			
			results = CellIdUtils.getCellsEnclosedBy(-90, (float)-88, -180, (float)-178);
			assertTrue(results.contains(0));
			assertTrue(results.contains(1));
			assertTrue(results.contains(360));
			assertTrue(results.contains(361));
			assertTrue(results.size()==4);
			
		} catch (UnableToGenerateCellIdException e) {
			fail(e.getMessage());
		}
	}
	
	// test the bottom left corner of the world 
	public void testGetCellsEnclosedByBottomLeft() {
		try {
			Set<Integer> results = CellIdUtils.getCellsEnclosedBy(-90, -89, -180, -179);
			assertTrue(results.contains(0));
			assertTrue(results.size()==1);
			
			results = CellIdUtils.getCellsEnclosedBy(-89, -88, -179, -178);
			assertTrue(results.contains(361));
			assertTrue(results.size()==1);			
			
			results = CellIdUtils.getCellsEnclosedBy((float)-89.9, (float)-87.5, (float)-179.9, (float)-177.5);
			assertTrue(results.contains(0));
			assertTrue(results.contains(1));
			assertTrue(results.contains(2));
			assertTrue(results.contains(360));
			assertTrue(results.contains(361));
			assertTrue(results.contains(362));
			assertTrue(results.contains(720));
			assertTrue(results.contains(721));
			assertTrue(results.contains(722));
			assertTrue(results.size()==9);
			
			results = CellIdUtils.getCellsEnclosedBy((float)-89.9, (float)-87.5, (float)-179.9, (float)-178);
			assertTrue(results.contains(0));
			assertTrue(results.contains(1));
			assertTrue(results.contains(360));
			assertTrue(results.contains(361));
			assertTrue(results.contains(720));
			assertTrue(results.contains(721));
			assertTrue(results.size()==6);
			
			results = CellIdUtils.getCellsEnclosedBy((float)-89.9, (float)-88, (float)-179.9, (float)-177.1);
			assertTrue(results.contains(0));
			assertTrue(results.contains(1));
			assertTrue(results.contains(2));
			assertTrue(results.contains(360));
			assertTrue(results.contains(361));
			assertTrue(results.contains(362));
			assertTrue(results.size()==6);
			
			
		} catch (UnableToGenerateCellIdException e) {
			fail(e.getMessage());
		}
	}
	
	// test the bottom right corner of the world 
	public void testGetCellsEnclosedByBottomRight() {
		try {
			Set<Integer> results = CellIdUtils.getCellsEnclosedBy(-90, -89, 179, 180);
			assertTrue(results.contains(359));
			assertTrue(results.size()==1);
			
			results = CellIdUtils.getCellsEnclosedBy(-89, -88, 178, 179);
			assertTrue(results.contains(718));
			assertTrue(results.size()==1);			
			
			results = CellIdUtils.getCellsEnclosedBy((float)-89.9, (float)-87.5, (float)177.2, (float)179.5);
			assertTrue(results.contains(357));
			assertTrue(results.contains(358));
			assertTrue(results.contains(359));
			assertTrue(results.contains(717));
			assertTrue(results.contains(718));
			assertTrue(results.contains(719));
			assertTrue(results.contains(1077));
			assertTrue(results.contains(1078));
			assertTrue(results.contains(1079));
			assertTrue(results.size()==9);
			
		} catch (UnableToGenerateCellIdException e) {
			fail(e.getMessage());
		}
	}
	
	// test the top left corner of the world 
	public void testGetCellsEnclosedByTopLeft() {
		try {
			Set<Integer> results = CellIdUtils.getCellsEnclosedBy(89, 90, -180, -179);
			assertTrue(results.contains(64440));
			assertTrue(results.size()==1);
			
			results = CellIdUtils.getCellsEnclosedBy(88, 89, -179, -178);
			assertTrue(results.contains(64081));
			assertTrue(results.size()==1);			
			
			results = CellIdUtils.getCellsEnclosedBy((float)87.2, (float)89.5, (float)-179.1, (float)-177.9);
			assertTrue(results.contains(63720));
			assertTrue(results.contains(63721));
			assertTrue(results.contains(63721));
			assertTrue(results.contains(64080));
			assertTrue(results.contains(64081));
			assertTrue(results.contains(64082));
			assertTrue(results.contains(64440));
			assertTrue(results.contains(64441));
			assertTrue(results.contains(64442));
			assertTrue(results.size()==9);
			
		} catch (UnableToGenerateCellIdException e) {
			fail(e.getMessage());
		}
	}
	
	// test the top right corner of the world 
	public void testGetCellsEnclosedByTopRight() {
		try {
			Set<Integer> results = CellIdUtils.getCellsEnclosedBy(89, 90, 179, 180);
			assertTrue(results.contains(64799));
			assertTrue(results.size()==1);
			
			results = CellIdUtils.getCellsEnclosedBy(88, 89, 178, 179);
			assertTrue(results.contains(64438));
			assertTrue(results.size()==1);			
			
			results = CellIdUtils.getCellsEnclosedBy((float)87.2, (float)89.5, (float)177.2, (float)179.9);
			assertTrue(results.contains(64077));
			assertTrue(results.contains(64078));
			assertTrue(results.contains(64079));
			assertTrue(results.contains(64437));
			assertTrue(results.contains(64438));
			assertTrue(results.contains(64439));
			assertTrue(results.contains(64797));
			assertTrue(results.contains(64798));
			assertTrue(results.contains(64799));
			assertTrue(results.size()==9);
			
		} catch (UnableToGenerateCellIdException e) {
			fail(e.getMessage());
		}
	}

	// test the 0,0 area of the world 
	public void testGetCellsEnclosedByCentre() {
		try {
			Set<Integer> results = CellIdUtils.getCellsEnclosedBy(0, 1, 0, 1);
			assertTrue(results.contains(32580));
			assertTrue(results.size()==1);
			
			results = CellIdUtils.getCellsEnclosedBy(-1, 0, 0, 1);
			assertTrue(results.contains(32220));
			assertTrue(results.size()==1);
			
			results = CellIdUtils.getCellsEnclosedBy(-1, 1, -1, 1);
			assertTrue(results.contains(32219));
			assertTrue(results.contains(32220));
			assertTrue(results.contains(32579));
			assertTrue(results.contains(32580));
			assertTrue(results.size()==4);			
			
			results = CellIdUtils.getCellsEnclosedBy((float)-0.9, (float)1.5, (float)-0.8, (float)1.4);
			assertTrue(results.contains(32219));
			assertTrue(results.contains(32220));
			assertTrue(results.contains(32221));
			assertTrue(results.contains(32579));
			assertTrue(results.contains(32580));
			assertTrue(results.contains(32581));
			assertTrue(results.contains(32939));
			assertTrue(results.contains(32940));
			assertTrue(results.contains(32941));
			assertTrue(results.size()==9);
			
		} catch (UnableToGenerateCellIdException e) {
			fail(e.getMessage());
		}
	}	
	public void testToCellId() {
		try {
			assertEquals(2,CellIdUtils.toCellId(-89.5f, -177.5f));
			assertEquals(0,CellIdUtils.toCellId(-89.9f, -179.9f));
		} catch (UnableToGenerateCellIdException e) {
			fail(e.getMessage());
		}
	}
	
	
	/**
	 * Test a conversion from and to ids
	 */
	public void testLL2Id2LL() {
		try {
			List<Integer> cells = new LinkedList<Integer>(CellIdUtils.getCellsEnclosedBy(-30,-20,110,130));
			LatLongBoundingBox bb = CellIdUtils.getBoundingBoxForCells(cells);
			assertEquals(110, (int)bb.getMinLong());
			assertEquals(-30, (int)bb.getMinLat());
			assertEquals(130, (int)bb.getMaxLong());
			assertEquals(-20, (int)bb.getMaxLat());
		} catch (UnableToGenerateCellIdException e) {
			fail("Incorrectly thrown - the lat longs are valid: " + e.getMessage());
		}
	}

	public void testGetCentiCellIdforBoundingBox(){
		try{
			Integer[] centiCell = CellIdUtils.getCentiCellIdForBoundingBox(17.1f, 19.2f, 17.2f, 19.3f);
			assertTrue(centiCell.length==2);
			assertTrue(centiCell[1]==21);
			centiCell = CellIdUtils.getCentiCellIdForBoundingBox(17.0f, 19.8f, 17.1f, 19.9f);
			assertTrue(centiCell.length==2);
			assertTrue(centiCell[1]==80);
			centiCell = CellIdUtils.getCentiCellIdForBoundingBox(-17.0f, 19.8f, -16.9f, 19.9f);
			assertTrue(centiCell.length==2);
			//assertTrue(centiCell[2]==21);
			centiCell = CellIdUtils.getCentiCellIdForBoundingBox(-17.0f, -19.9f, -16.9f, -19.8f);
			assertTrue(centiCell.length==2);
			//assertTrue(centiCell[2]==21);
			centiCell = CellIdUtils.getCentiCellIdForBoundingBox(17.0f, -19.9f, 17.1f, -19.8f);
			assertTrue(centiCell.length==2);
			//assertTrue(centiCell[2]==21);
		} catch (Exception e){
			logger.error(e.getMessage(), e);
			fail("Incorrectly thrown - the lat longs are valid: " + e.getMessage());
		}
	}
}