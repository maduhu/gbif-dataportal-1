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

package org.gbif.portal.util.db;


/**
 * Utilities for dealing with OccurrenceRecord
 * <p/>
 * TODO i18n for the formatting of issues
 *
 * @author dhobern
 */
public class OccurrenceRecordUtils {

  /**
   * Constant values for identifier types
   */

  public static final int IDENTIFIERTYPE_GUID = 1;
  public static final int IDENTIFIERTYPE_FIELDNUMBER = 2;
  public static final int IDENTIFIERTYPE_COLLECTORNUMBER = 3;
  public static final int IDENTIFIERTYPE_ACCESSIONNUMBER = 4;
  public static final int IDENTIFIERTYPE_SEQUENCENUMBER = 5;
  public static final int IDENTIFIERTYPE_OTHERCATALOGNUMBER = 6;
  public static final int IDENTIFIERTYPE_SOURCEID = 7;

  /**
   * Constant values for image types
   */

  public static final int IMAGETYPE_UNKNOWN = 0;
  public static final int IMAGETYPE_PRODUCT = 1;
  public static final int IMAGETYPE_UNKNOWNIMAGE = 2;
  public static final int IMAGETYPE_LIVEORGANISMIMAGE = 3;
  public static final int IMAGETYPE_SPECIMENIMAGE = 4;
  public static final int IMAGETYPE_LABELIMAGE = 5;

  /**
   * Constant values for link types
   */

  public static final int LINKTYPE_UNKNOWN = 0;
  public static final int LINKTYPE_OCCURRENCEPAGE = 1;

  /**
   * Constant for clearing issue fields
   */
  public static final int NO_ISSUES = 0x00;

  /**
   * Constant bit values for occurrence_record.geospatial_issue
   */

  /**
   * Set if latitude appears to have the wrong sign
   */
  public static final int GEOSPATIAL_PRESUMED_NEGATED_LATITUDE = 0x01;

  /**
   * Set if longitude appears to have the wrong sign
   */
  public static final int GEOSPATIAL_PRESUMED_NEGATED_LONGITUDE = 0x02;

  /**
   * Set if latitude and longitude appears to have been switched
   */
  public static final int GEOSPATIAL_PRESUMED_INVERTED_COORDINATES = 0x04;

  /**
   * Set if coordinates are (0,0)
   */
  public static final int GEOSPATIAL_ZERO_COORDINATES = 0x08;

  /**
   * Set if coordinates not in range -90 to 90 and -180 to 180
   */
  public static final int GEOSPATIAL_COORDINATES_OUT_OF_RANGE = 0x10;

  /**
   * Set if coordinates do not match country name
   */
  public static final int GEOSPATIAL_COUNTRY_COORDINATE_MISMATCH = 0x20;

  /**
   * Set if country name is not understood
   */
  public static final int GEOSPATIAL_UNKNOWN_COUNTRY_NAME = 0x40;

  /**
   * Set if altitude is >10000m or <-1000m
   */
  public static final int GEOSPATIAL_ALTITUDE_OUT_OF_RANGE = 0x80;

  /**
   * Set if altitude is -9999 or some other bogus value
   */
  public static final int GEOSPATIAL_PRESUMED_ERRONOUS_ALTITUDE = 0x100;

  /**
   * Set if supplied min > max altitude
   */
  public static final int GEOSPATIAL_PRESUMED_MIN_MAX_ALTITUDE_REVERSED = 0x200;

  /**
   * Set if supplied depth is in feet instead of metric
   */
  public static final int GEOSPATIAL_PRESUMED_DEPTH_IN_FEET = 0x400;

  /**
   * Set if depth is larger than is feasible
   */
  public static final int GEOSPATIAL_DEPTH_OUT_OF_RANGE = 0x800;

  /**
   * Set if supplied min>max
   */
  public static final int GEOSPATIAL_PRESUMED_MIN_MAX_DEPTH_REVERSED = 0x1000;

  /**
   * Set if supplied altitude is in feet instead of metric
   */
  public static final int GEOSPATIAL_PRESUMED_ALTITUDE_IN_FEET = 0x2000;

  /**
   * Set if altitude is a non numeric value
   */
  public static final int GEOSPATIAL_PRESUMED_ALTITUDE_NON_NUMERIC = 0x4000;

  /**
   * Set if depth is a non numeric value
   */
  public static final int GEOSPATIAL_PRESUMED_DEPTH_NON_NUMERIC = 0x8000;

  /**
   * All geospatial bits
   */
  public static final int GEOSPATIAL_MASK = 0xFFFF;

  /**
   * Constant bit values for occurrence_record.taxonomic_issue
   */

  /**
   * Set if scientific name cannot be parsed as such
   */
  public static final int TAXONOMIC_INVALID_SCIENTIFIC_NAME = 0x01;

  /**
   * Set if kingdom not known for record
   */
  public static final int TAXONOMIC_UNKNOWN_KINGDOM = 0x02;

  /**
   * Set if scientific name as provided is ambiguous (e.g. inter-kingdom homonym)
   */
  public static final int TAXONOMIC_AMBIGUOUS_NAME = 0x04;

  /**
   * All taxonomic bits
   */
  public static final int TAXONOMIC_MASK = 0x07;

  /**
   * Constant bit values for occurrence_record.other_issue
   */

  /**
   * Set if record has no catalogue number/unit id
   */
  public static final int OTHER_MISSING_CATALOGUE_NUMBER = 0x01;

  /**
   * Set if basis of record not known
   */
  public static final int OTHER_MISSING_BASIS_OF_RECORD = 0x02;

  /**
   * Set if occurrence date not valid
   */
  public static final int OTHER_INVALID_DATE = 0x04;

  /**
   * Set if country inferred from coordinates (probably should be handled differently) TODO
   */
  public static final int OTHER_COUNTRY_INFERRED_FROM_COORDINATES = 0x08;

  /**
   * All other bits
   */
  public static final int OTHER_MASK = 0x0F;

  /**
   * Format a report of the geospatial issues with a record
   *
   * @param issue
   * @return String summary
   */
  public static String formatGeospatialIssue(int issue) {
    StringBuilder buffer = new StringBuilder();

    if (issue != 0) {
      buffer.append("Geospatial issues:");
      String prefix = "";
      if ((issue & GEOSPATIAL_PRESUMED_NEGATED_LATITUDE) != 0) {
        buffer.append(prefix).append(" latitude probably negated");
        prefix = ";";
      }
      if ((issue & GEOSPATIAL_PRESUMED_NEGATED_LONGITUDE) != 0) {
        buffer.append(prefix).append(" longitude probably negated");
        prefix = ";";
      }
      if ((issue & GEOSPATIAL_PRESUMED_INVERTED_COORDINATES) != 0) {
        buffer.append(prefix).append(" latitude and longitude probably transposed");
        prefix = ";";
      }
      if ((issue & GEOSPATIAL_ZERO_COORDINATES) != 0) {
        buffer.append(prefix).append(" coordinates supplied as (0.0, 0.0)");
        prefix = ";";
      }
      if ((issue & GEOSPATIAL_COORDINATES_OUT_OF_RANGE) != 0) {
        buffer.append(prefix).append(" supplied coordinates out of range");
        prefix = ";";
      }
      if ((issue & GEOSPATIAL_COUNTRY_COORDINATE_MISMATCH) != 0) {
        buffer.append(prefix).append(" coordinates fall outside specified country");
        prefix = ";";
      }
      if ((issue & GEOSPATIAL_UNKNOWN_COUNTRY_NAME) != 0) {
        buffer.append(prefix).append(" supplied country name not known");
        prefix = ";";
      }
      if ((issue & GEOSPATIAL_ALTITUDE_OUT_OF_RANGE) != 0) {
        buffer.append(prefix).append(" supplied altitude out of range");
      }
      if ((issue & GEOSPATIAL_PRESUMED_MIN_MAX_ALTITUDE_REVERSED) != 0) {
        buffer.append(prefix).append(" minimum and maximum altitude reversed");
      }
      if ((issue & GEOSPATIAL_PRESUMED_ALTITUDE_IN_FEET) != 0) {
        buffer.append(prefix).append(" presumed altitude fields supplied in feet");
      }
      if ((issue & GEOSPATIAL_PRESUMED_DEPTH_IN_FEET) != 0) {
        buffer.append(prefix).append(" presumed depth fields supplied in feet");
      }
      if ((issue & GEOSPATIAL_DEPTH_OUT_OF_RANGE) != 0) {
        buffer.append(prefix).append(" supplied depth out of range");
      }
      if ((issue & GEOSPATIAL_PRESUMED_MIN_MAX_DEPTH_REVERSED) != 0) {
        buffer.append(prefix).append(" minimum and maximum depth reversed");
      }
      buffer.append(".");
    }

    return buffer.toString();
  }

  /**
   * Format a report of the taxonomic issues with a record
   *
   * @param issue
   * @return String summary
   */
  public static String formatTaxonomicIssue(int issue) {
    StringBuilder buffer = new StringBuilder();

    if (issue != 0) {
      buffer.append("Taxonomic issues:");
      String prefix = "";
      if ((issue & TAXONOMIC_INVALID_SCIENTIFIC_NAME) != 0) {
        buffer.append(prefix).append(" scientific name not validly formed");
        prefix = ";";
      }
      if ((issue & TAXONOMIC_UNKNOWN_KINGDOM) != 0) {
        buffer.append(prefix).append(" kingdom not known for record");
        prefix = ";";
      }
      if ((issue & TAXONOMIC_AMBIGUOUS_NAME) != 0) {
        buffer.append(prefix).append(" supplied name ambiguous");
      }
      buffer.append(".");
    }

    return buffer.toString();
  }

  /**
   * Format a report of the other issues with a record
   *
   * @param issue
   * @return String summary
   */
  public static String formatOtherIssue(int issue) {
    StringBuilder buffer = new StringBuilder();

    if (issue != 0) {
      buffer.append("Miscellaneous issues:");
      String prefix = "";
      if ((issue & OTHER_MISSING_CATALOGUE_NUMBER) != 0) {
        buffer.append(prefix).append(" missing catalogue number");
        prefix = ";";
      }
      if ((issue & OTHER_MISSING_BASIS_OF_RECORD) != 0) {
        buffer.append(prefix).append(" basis of record not known");
        prefix = ";";
      }
      if ((issue & OTHER_INVALID_DATE) != 0) {
        buffer.append(prefix).append(" supplied date invalid");
        prefix = ";";
      }
      if ((issue & OTHER_COUNTRY_INFERRED_FROM_COORDINATES) != 0) {
        buffer.append(prefix).append(" country inferred from coordinates");
      }
      buffer.append(".");
    }

    return buffer.toString();
  }
}
