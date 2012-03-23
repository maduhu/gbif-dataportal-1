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
package org.gbif.portal.util.log;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Appender;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;

import java.util.Enumeration;


/**
 * The GBIF Database Log appender is a Log4J appender.
 * Since Log4J is initialised before the Spring Factory loads up the application, it is not possible
 * to use the datastore in the spring config.
 * Furthermore, the configuration for the DB appender resides in a Log4J properties file
 * and thus is duplicated.
 * This class is a workaround to this situation.  It will look for any GBIFDBLog appenders in the Log4J
 * root tree, and then overwrite the DAO that the appender uses, with the one configured in the spring wiring.
 *
 * @author Tim Robertson
 */
public class GbifAppenderDaoOverride implements InitializingBean {
  /**
   * DAO
   */
  protected GbifLogMessageDAO gbifLogMessageDAO;

  /**
   * Logger
   */
  protected Log logger = LogFactory.getLog(this.getClass());

  /**
   * Sets up the logging
   */
  @SuppressWarnings({"unchecked", "static-access"})
  public void afterPropertiesSet() throws Exception {
    Logger log4jLogger = Logger.getLogger(GbifAppenderDaoOverride.class);
    Enumeration<Appender> appenders = Logger.getRootLogger().getAllAppenders();
    while (appenders.hasMoreElements()) {
      Appender appender = appenders.nextElement();
      if (appender instanceof GbifDatabaseLogAppender) {
        if (gbifLogMessageDAO == null) {
          logger.warn("Incorrect use of GbifAppenderDaoOverride - no DAO supplied!");
        } else {
          logger.info("GbifDatabaseLogAppender found and will have a new datasource set");
          ((GbifDatabaseLogAppender) appender).setGbifLogMessageDAO(gbifLogMessageDAO);
        }
      }
    }
  }

  /**
   * @return Returns the gbifLogMessageDAO.
   */
  public GbifLogMessageDAO getGbifLogMessageDAO() {
    return gbifLogMessageDAO;
  }

  /**
   * @param gbifLogMessageDAO The gbifLogMessageDAO to set.
   */
  public void setGbifLogMessageDAO(GbifLogMessageDAO gbifLogMessageDAO) {
    this.gbifLogMessageDAO = gbifLogMessageDAO;
  }
}
