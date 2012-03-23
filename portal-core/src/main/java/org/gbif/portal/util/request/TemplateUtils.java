/**
 *
 */
package org.gbif.portal.util.request;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;

import java.io.InputStream;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Properties;

/**
 * Provides Utilities for dealing with templating.
 * <p/>
 * This was not extracted into an interface as there seems no common
 * way to define a utility that works with available templating packages
 * that runs efficiently.  To be truly implementation independant would require
 * significant copying from one context into the tmplate "merge" context, and thus
 * thus is a Velocity specific class.  Velocity is both tried and tested in th open
 * source community, and has been tested internally, performing well.
 *
 * @author tim
 */
public class TemplateUtils {
  /**
   * Logger
   */
  public static Log logger = LogFactory.getLog(TemplateUtils.class);

  /**
   * Initialise Velocity once - log the error, which is fatal
   */
  static {
    try {
      InputStream velPropsIS = TemplateUtils.class.getClassLoader().getResourceAsStream("velocity.properties");
      logger.info("Velocity Engine initializing");
      Properties velProps = new Properties();
      velProps.load(velPropsIS);
      Velocity.init(velProps);
    } catch (Exception e) {
      logger.error("Error initialising velocity: " + e.getMessage(), e);
      logger.error("Velocity initialisation failed, and thus no indexing activities will run correctly " +
        " - please check that velocity.properties is available on the CP root, and is correct!");
    }
  }

  /**
   * The template for the location which must be relative to the CP root
   *
   * @return The template
   * @throws Exception                 Some internal Velocity error reading the template
   * @throws ParseErrorException       If the template is not parsable by velocity
   * @throws ResourceNotFoundException If the location itself is invalid
   */
  public Template getTemplate(String location) throws ResourceNotFoundException, ParseErrorException, Exception {
    logger.debug("Returning template: " + location);
    return Velocity.getTemplate(location);
  }

  /**
   * Merges the template with the supplied context to the writer
   *
   * @param template To merge
   * @param context  To merge with
   * @param writer   To merge to
   * @throws ResourceNotFoundException When the template is loading something not
   *                                   found in the context
   * @throws ParseErrorException       If the template cannont be parsed correctly
   * @throws MethodInvocationException If the temlate tries to call a method on
   *                                   a context object that does not exist
   * @throws Exception                 On some other internal error - e.g the context object calling failed
   */
  public void merge(Template template, VelocityContext context, Writer writer) throws ResourceNotFoundException,
    ParseErrorException, MethodInvocationException, Exception {
    template.merge(context, writer);
  }

  /**
   * Merges the temlate and returns it as a string.
   *
   * @see TempalteUtils.merge(Template, VelocityContext, Writer)
   */
  public String merge(Template template, VelocityContext context) throws ResourceNotFoundException,
    ParseErrorException, MethodInvocationException, Exception {
    StringWriter writer = new StringWriter();
    merge(template, context, writer);
    return writer.toString();
  }

  /**
   * Gets the template for the location and returns it merged with the context
   * as a String
   *
   * @see TempalteUtils.merge(Template, VelocityContext, Writer)
   * @see TempalteUtils.get(String)
   */
  public String getAndMerge(String location, VelocityContext context) throws ResourceNotFoundException,
    ParseErrorException, MethodInvocationException, Exception {
    return merge(getTemplate(location), context);
  }
}
