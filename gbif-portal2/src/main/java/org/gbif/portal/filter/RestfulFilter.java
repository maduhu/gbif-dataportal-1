package org.gbif.portal.filter;

import com.google.inject.Singleton;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.regex.Pattern;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

/**
 * A servlet filter that takes pretty URLs and converts them into a parameter based request very suitable for struts2 actions derived from CrudAction.
 * The supported URLs allow CRUD style operations based on standard pretty URLs like the following:
 * action/
 * -> action.do
 * action/add
 * -> action.do?add=true
 * action/4372
 * -> action.do?id=4372
 * action/save
 * -> action.do?save=true
 * action/4372/save
 * -> action.do?id=4372&save=true
 * action/4372/del
 * -> action.do?id=4372&del=true
 *
 * Note that the save(=insert or update) and delete paths are not strictly RESTful, but it allows to use simple POST requests from forms instead of generating DELETE or PUT http requests
 *
 * @see org.gbif.portal.action.CrudAction
 * @author markus
 *
 */
@Singleton
public class RestfulFilter implements Filter {
  public class UrlRewriteResponse extends HttpServletResponseWrapper {

    public UrlRewriteResponse(final HttpServletResponse response) {
      super(response);
    }

    @Override
    public String encodeUrl(final String url) {
      final String x = super.encodeUrl(url);
//			System.out.println("resp.encodeUrl():"+x);
      return x;
    }

    @Override
    public String encodeURL(final String url) {
      final String x = super.encodeURL(url);
//			System.out.println("resp.encodeURL():"+x);
      return x;
    }

  }

  protected static final Logger log = LoggerFactory.getLogger(RestfulFilter.class);

  private static final Pattern splitPaths = Pattern.compile("/");

  @Override
  public void destroy() {
  }

  @Override
  public void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain chain) throws IOException, ServletException {
    if (request instanceof HttpServletRequest) {
      final HttpServletRequest req = (HttpServletRequest) request;
      final HttpServletResponse resp = (HttpServletResponse) response;

      if (req.getServletPath().contains(".")) {
        chain.doFilter(request, response);
      } else {
        // wrap the request for a nub name search with given kingdom and checklist ID
        chain.doFilter(filter(req), resp);
      }
    }
  }

  protected ServletRequest filter(final HttpServletRequest req) throws IOException, ServletException {
    final Map<String, String> params = new HashMap<String, String>();
    // see if we have an empty / at the end, ignore
    String url = req.getServletPath();
    if (url.endsWith("/")) {
      url = url.substring(0, url.length() - 1);
    }
    if (StringUtils.trimToNull(url) == null) {
//			System.out.println("empty path. Index.do");
      return new UrlRewriteRequest(req, "/index.do", params);
    }

    final LinkedList<String> parts = new LinkedList<String>(Arrays.asList(splitPaths.split(url)));
//		System.out.println("pretty filter: "+StringUtils.join(parts,"|"));

    // see if we got a final add
    if (!parts.isEmpty() && parts.getLast().equalsIgnoreCase("add")) {
      parts.removeLast();
      params.put("add", "true");
    } else {
      // see if we got a final edit or del
      if (!parts.isEmpty() && parts.getLast().equalsIgnoreCase("save")) {
        parts.removeLast();
        params.put("save", "true");
      } else if (!parts.isEmpty() && parts.getLast().equalsIgnoreCase("del")) {
        parts.removeLast();
        params.put("del", "true");
      }
      // see if we got an integer id parameter in the URL
      Integer id = null;
      if (!parts.isEmpty()) {
        try {
          id = Integer.parseInt(parts.getLast());
          parts.removeLast();
          params.put("id", id.toString());
        } catch (final NumberFormatException e) {
        }
      }
    }
    // add suffix to final path if it doesnt contain a suffix yet
    String action = parts.get(parts.size() - 1);
    if (action != null && !action.contains(".")) {
      action = action + ".do";
      parts.set(parts.size() - 1, action);
    }
    final String newUrl = StringUtils.join(parts, "/");
//		System.out.println("New URL: "+newUrl);
    return new UrlRewriteRequest(req, newUrl, params);
  }

  @Override
  public void init(final FilterConfig filterConfig) throws ServletException {
  }

}
