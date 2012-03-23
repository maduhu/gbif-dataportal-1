package org.gbif.portal.util.request;

import java.util.StringTokenizer;

/**
 * Utilities for working with IP addresses.
 *
 * @author dmartin
 */
public class IPUtils {

  /**
   * Checks the format of this ip.
   *
   * @param ip
   * @return
   */
  public static boolean isValidRemoteAddress(String ip) {
    if (ip == null)
      return false;
    StringTokenizer st = new StringTokenizer(ip, ".");
    try {
      //4 bits of IP
      if (st.hasMoreTokens())
        Long.parseLong(st.nextToken());
      if (st.hasMoreTokens())
        Long.parseLong(st.nextToken());
      if (st.hasMoreTokens())
        Long.parseLong(st.nextToken());
      if (st.hasMoreTokens())
        Long.parseLong(st.nextToken());
      return true;
    } catch (Exception e) {
      //expected behaviour
      return false;
    }
  }

  /**
   * Converts this ip address into a long value that can use to see
   * if an ip lies in a specific range.
   *
   * @param ip of the form 123.13.123.123
   * @return long value for this ip
   */
  public static long convertIPtoLong(String ip) {
    StringTokenizer st = new StringTokenizer(ip, ".");
    //4 bits of IP
    long a = Long.parseLong(st.nextToken());
    long b = Long.parseLong(st.nextToken());
    long c = Long.parseLong(st.nextToken());
    long d = Long.parseLong(st.nextToken());
    return a * (256l * 256l * 256l) + b * (256l * 256l) + c * 256l + d;
  }
}
