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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.math.util.MathUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Utilities for dealing with CellId
 *
 * @author tim
 */
public class CellIdUtils {
  /**
   * Logger
   */
  protected static Log logger = LogFactory.getLog(CellIdUtils.class);

  /**
   * Determines the cell id for the Lat / Long provided
   *
   * @param latitude  Which may be null
   * @param longitude Which may be null
   * @return The cell id for the Lat Long pair
   * @throws UnableToGenerateCellIdException
   *          Shoudl the lat long be null or invalid
   */
  public static int toCellId(Float latitude, Float longitude) throws UnableToGenerateCellIdException {
    if (logger.isDebugEnabled())
      logger.debug("Getting cell for [" + latitude + "," + longitude + "]");

    if (latitude == null
      || latitude < -90
      || latitude > 90
      || longitude < -180
      || longitude > 180) {
      throw new UnableToGenerateCellIdException("Latitude[" + latitude + "], Longitude[" + longitude + "] cannot be " +
        "converted to a cell id");
    } else {
      int la = getCellIdFor(latitude);
      int lo = getMod360CellIdFor(longitude);
      int cellId = la + lo;
      return cellId;
    }
  }

  /**
   * Get mod 360 cell id.
   *
   * @param longitude
   * @return
   */
  public static int getMod360CellIdFor(float longitude) {
    return new Double(Math.floor(longitude + 180)).intValue();
  }

  /**
   * Get cell id.
   *
   * @param latitude
   * @return
   */
  public static int getCellIdFor(float latitude) {
    return (new Double(Math.floor(latitude + 90)).intValue()) * 360;
  }

  /**
   * Returns true if the supplied cell id lies in the bounding box demarcated by the min and max cell ids supplied.
   *
   * @param cellId
   * @return
   * @throws Exception
   */
  public static boolean isCellIdInBoundingBox(int cellId, int minCellId, int maxCellId) throws Exception {
    return cellId >= minCellId
      && cellId <= (maxCellId - 361)
      && (cellId % 360) >= (minCellId % 360)
      && (cellId % 360) <= ((maxCellId - 361) % 360);
  }

  /**
   * Determines the centi cell id for the given values
   *
   * @param latitude  Which may be null
   * @param longitude Which may be null
   * @return The centi cell id within the cell for the lat long
   * @throws UnableToGenerateCellIdException
   *          Shoudl the lat long be null or invalid
   */
  public static int toCentiCellId(Float latitude, Float longitude) throws UnableToGenerateCellIdException {
    if (latitude == null
      || latitude < -90
      || latitude > 90
      || longitude < -180
      || longitude > 180) {
      throw new UnableToGenerateCellIdException("Latitude[" + latitude + "], Longitude[" + longitude + "] cannot be " +
        "converted to a centi cell id");
    } else {

      //get decimal value for up to 4 decimal places
      //17.2-> 172000 -> 2000
      int la = Math.abs((int) (latitude * 10000) % 10000);
      if (latitude < 0)
        la = 10000 - la;
      la = (la / 1000) % 10;
      int lo = Math.abs((int) (longitude * 10000) % 10000);
      if (longitude < 0)
        lo = 10000 - lo;
      lo = (lo / 1000) % 10;

      int centiCellId = (la * 10) + lo;
      return Math.abs(centiCellId);
    }
  }

  /**
   * Returns the box of the given cell
   * This may require some more work to avoid divide rounding errors
   *
   * @param cellId To return the lat long box of
   * @return The box
   */
  public static LatLongBoundingBox toBoundingBox(int cellId) {
    int longitude = (cellId % 360) - 180;
    int latitude = -90;
    if (cellId > 0) {
      latitude = new Double(Math.floor(cellId / 360)).intValue() - 90;
    }
    return new LatLongBoundingBox(longitude, latitude, longitude + 1, latitude + 1);
  }

  /**
   * Returns the box of the given cell and centi cell
   * An attempt has been made to avoid rounding errors with floats, but may need revisited
   *
   * @param cellId      To return the lat long box of
   * @param centiCellId within the box
   * @return The box
   */
  public static LatLongBoundingBox toBoundingBox(int cellId, int centiCellId) {
    int longitudeX10 = 10 * ((cellId % 360) - 180);
    int latitudeX10 = -900;
    if (cellId > 0) {
      latitudeX10 = 10 * (new Double(Math.floor(cellId / 360)).intValue() - 90);
    }

    float longOffset = (centiCellId % 10);
    float latOffset = 0;
    if (centiCellId > 0) {
      latOffset = centiCellId / 10;
    }

    float minLatitude = (latitudeX10 + latOffset) / 10;
    float minLongitude = (longitudeX10 + longOffset) / 10;
    float maxLatitude = (latitudeX10 + latOffset + 1) / 10;
    float maxlongitude = (longitudeX10 + longOffset + 1) / 10;
    return new LatLongBoundingBox(minLongitude, minLatitude, maxlongitude, maxLatitude);
  }

  /**
   * Gets the list of cells that are enclosed within the bounding box.
   * Cells that are partially enclosed are returned also
   *
   * @param minLat
   * @param maxLat
   * @param minLong
   * @param maxLong
   * @return The cells that are enclosed by the bounding box
   * @throws UnableToGenerateCellIdException
   *          if the lat longs are invalid
   * @TODO implement this properly, the current version will include cells that are partially
   * included on the bottom and left, but not the top and right
   */
  public static Set<Integer> getCellsEnclosedBy(float minLat, float maxLat, float minLong,
                                                float maxLong) throws UnableToGenerateCellIdException {
    if (minLat < -90) minLat = -90;
    if (maxLat > 90) maxLat = 90;
    if (minLong < -180) minLong = -180;
    if (maxLong > 180) maxLong = 180;

    if (logger.isDebugEnabled())
      logger.debug("Establishing cells enclosed by: " + minLat + ":" + maxLat + "   " + minLong + ":" + maxLong);

    int lower = toCellId(minLat, minLong);
    int upper = toCellId(maxLat, maxLong);

    if (logger.isDebugEnabled())
      logger.debug("Unprocessed cells: " + lower + " -> " + upper);

    // if the BB upper right corner is on a grid, then it needs flagged
    if (Math.ceil(maxLong) == Math.floor(maxLong)) {
      logger.debug("Longitude lies on a boundary");
      upper -= 1;
    }
    if (Math.ceil(maxLat) == Math.floor(maxLat)) {
      logger.debug("Latitude lies on a boundary");
      upper -= 360;
    }

    if (logger.isDebugEnabled())
      logger.debug("Getting cells contained in " + lower + " to " + upper);

    int omitLeft = lower % 360;
    int omitRight = upper % 360;
    if (omitRight == 0)
      omitRight = 360;
    Set<Integer> cells = new HashSet<Integer>();
    for (int i = lower; i <= upper; i++) {
      if ((i % 360 >= omitLeft
        && i % 360 <= omitRight)) {
        cells.add(i);
      }
    }
    return cells;
  }

  /**
   * Return a min cell id and a max cell id for this bounding box.
   *
   * @param minLongitude
   * @param minLatitude
   * @param maxLongitude
   * @param maxLatitude
   * @return the minCellId in int[0] and maxCellId in int[1]
   */
  public static int[] getMinMaxCellIdsForBoundingBox(float minLongitude, float minLatitude, float maxLongitude,
                                                     float maxLatitude) throws UnableToGenerateCellIdException {

    int minCellId = CellIdUtils.toCellId(minLatitude, minLongitude);
    int maxCellId = CellIdUtils.toCellId(maxLatitude, maxLongitude);

    if (Math.ceil((double) maxLatitude) == Math.floor((double) maxLatitude)
      && Math.ceil((double) maxLongitude) == Math.floor((double) maxLongitude)
      && (maxLongitude != 180f && maxLatitude != 90f)
      && maxCellId > 0) {

      //the maxLongitude,maxLatitude point is on a cell intersection, hence the maxCellId should be
      // -361 the maxCellId CellIdUtils will give us i.e. the cell that is
      // 1 below and 1 to the left of the cell id retrieved
      //unless it is the 64799 cell.
      maxCellId = maxCellId - 361;
    }

    return new int[]{minCellId, maxCellId};
  }

  /**
   * Creates a bounding box for the list of unordered cell ids.
   *
   * @param cellDensities
   * @return a LatLongBoundingBox that encapsulates this list of cell ids.
   */
  public static LatLongBoundingBox getBoundingBoxForCells(List<Integer> cellIds) {
    if (cellIds.isEmpty())
      return null;
    //first cell - gives the minLat
    //float minLatitude = toBoundingBox(cellIds.get(0)).minLat;
    //last cell - give the maxLat
    //float maxLatitude = toBoundingBox(cellIds.get(cellIds.size()-1)).maxLat;
    int minLatitudeCellId = cellIds.get(0);
    int maxLatitudeCellId = cellIds.get(cellIds.size() - 1);

    int minLongitudeCellId = cellIds.get(0);
    int maxLongitudeCellId = cellIds.get(cellIds.size() - 1);
    //the min cell (id % 360) - gives min longitude
    //the max cell (id % 360) - gives max longitude
    for (Integer cellId : cellIds) {

      Integer cellIdMod360 = cellId % 360;
      if (cellIdMod360 < (minLongitudeCellId % 360))
        minLongitudeCellId = cellIdMod360;
      if (cellIdMod360 > (maxLongitudeCellId % 360))
        maxLongitudeCellId = cellIdMod360;

      if (cellId < minLatitudeCellId)
        minLatitudeCellId = cellId;
      if (cellId > maxLatitudeCellId)
        maxLatitudeCellId = cellId;
    }
    float minLongitude = toBoundingBox(minLongitudeCellId).minLong;
    float minLatitude = toBoundingBox(minLatitudeCellId).minLat;
    float maxLongitude = toBoundingBox(maxLongitudeCellId).maxLong;
    float maxLatitude = toBoundingBox(maxLatitudeCellId).maxLat;

    return new LatLongBoundingBox(minLongitude, minLatitude, maxLongitude, maxLatitude);
  }

  /**
   * Returns the cell id and centi cell id for the supplied bounding box, Returning null if the supplied bounding box
   * doesnt enclose a single cell.
   * If the bounding box encloses a single cell but not a centi cell, a Integer[] of length 1 is returned with
   * containing the cell id.
   * Otherwise a Integer array of length 2, with Integer[0] being the cell id, Integer[1] being the centi cell.
   *
   * @param minLongitude
   * @param minLatitude
   * @param maxLongitude
   * @param maxLatitude
   * @return
   * @throws UnableToGenerateCellIdException
   *
   */
  public static Integer[] getCentiCellIdForBoundingBox(float minLongitude, float minLatitude, float maxLongitude,
                                                       float maxLatitude) throws UnableToGenerateCellIdException {

    //int[] maxMinCellIds = getMinMaxCellIdsForBoundingBox(minLongitude, minLatitude, maxLongitude, maxLatitude);
    //if(maxMinCellIds==null || (maxMinCellIds[0]!=maxMinCellIds[1]))
    //	return null;

    //Integer cellid = maxMinCellIds[0];

    //int[] maxMinCellIds = getMinMaxCellIdsForBoundingBox(minLongitude, minLatitude, maxLongitude, maxLatitude);

    if (!(isBoundingBoxCentiCell(minLongitude, minLatitude, maxLongitude, maxLatitude))) {
      return null;
    }

    //ascertain whether bounding box is 0.1 by 0.1
    //if(isBoundingBoxCentiCell(minLongitude, minLatitude, maxLongitude, maxLatitude)){

    int[] maxMinCellIds = getMinMaxCellIdsForBoundingBox(minLongitude, minLatitude, maxLongitude, maxLatitude);
    Integer cellid = maxMinCellIds[0];

    int minCentiCell = toCentiCellId(minLatitude, minLongitude);
    int maxCentiCell = toCentiCellId(maxLatitude, maxLongitude);

    float maxLongitude10 = maxLongitude * 10;
    float maxLatitude10 = maxLatitude * 10;

    if (Math.ceil((double) maxLatitude10) == Math.floor((double) maxLatitude10)
      && Math.ceil((double) maxLongitude10) == Math.floor((double) maxLongitude10)
      && maxCentiCell > 0) {

      //the maxLongitude,maxLatitude point is on a centi cell intersection, hence the maxCentiCellId should be
      // maxCentiCellId-11 i.e. the cell that is
      // 1 below and 1 to the left of the centi cell id retrieved
      //unless it is the 100 centi cell.
      if (maxCentiCell > minCentiCell)
        maxCentiCell = maxCentiCell - 11;
      else
        maxCentiCell = maxCentiCell + 9;
    }

    //if(maxCentiCell==minCentiCell){
    return new Integer[]{cellid, minCentiCell};
    //}
    //}
    //return new Integer[]{cellid};
  }

  private static boolean isBoundingBoxCentiCell(float minLongitude, float minLatitude, float maxLongitude,
                                                float maxLatitude) {
    float width = maxLongitude > minLongitude ? maxLongitude - minLongitude : minLongitude - maxLongitude;
    float height = maxLatitude > minLatitude ? maxLatitude - minLatitude : minLatitude - maxLatitude;
    return MathUtils.round(height, 1) == 0.1f && MathUtils.round(width, 1) == 0.1f;
  }

  /**
   * For ease of conversion
   *
   * @param args See usage
   */
  public static void main(String[] args) {
    try {
      if (args.length == 1) {
        LatLongBoundingBox llbb = toBoundingBox(Integer.parseInt(args[0]));
        System.out.println("CellId " + args[0] + ": minX[" + llbb.getMinLong() + "] minY[" + llbb.getMinLat() + "] maxX[" + llbb.getMaxLong() + "] maxY[" + llbb.getMaxLat() + "]");
      } else if (args.length == 2) {
        float lat = Float.parseFloat(args[0]);
        float lon = Float.parseFloat(args[1]);
        System.out.println("lat[" + lat + "] long[" + lon + "] = cellId: " + toCellId(lat, lon));
      } else {
        System.out.println("Provide either a 'cell id' or 'Lat Long' params!");
      }
    } catch (NumberFormatException e) {
      e.printStackTrace();
    } catch (UnableToGenerateCellIdException e) {
      e.printStackTrace();
    }
  }
}
