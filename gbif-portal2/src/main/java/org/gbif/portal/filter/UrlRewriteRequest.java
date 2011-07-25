package org.gbif.portal.filter;

import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

class UrlRewriteRequest extends HttpServletRequestWrapper {
  private Map<String, String> params = new HashMap<String, String>();
  private final String requestURI;
  private String servletPath;

  public UrlRewriteRequest(final HttpServletRequest request, final String url, final Map<String, String> additionalParameter) {
    super(request);
    this.requestURI = url;
    if (additionalParameter != null) {
      params = additionalParameter;
    } else {
      params = new HashMap<String, String>();
    }
    // only use first value of each parameter:
    final Enumeration<String> iter = super.getParameterNames();
    while (iter!=null && iter.hasMoreElements()) {
      final String p = iter.nextElement();
      params.put(p, super.getParameter(p));
    }
  }

  @Override
  public String getContextPath() {
    final String x = super.getContextPath();
    return x;
  }

  @Override
  public String getParameter(final String name) {
    return params.get(name);
  }

  @Override
  public Map getParameterMap() {
    return params;
  }

  @Override
  public Enumeration<String> getParameterNames() {
    return Collections.enumeration(params.keySet());
  }

  @Override
  public String[] getParameterValues(final String s) {
    if (params.containsKey(s)) {
      return new String[]{params.get(s)};
    } else {
      return new String[]{};
    }
  }

  @Override
  public String getPathInfo() {
    final String x = super.getPathInfo();
    return x;
  }

  @Override
  public String getPathTranslated() {
    final String x = super.getPathTranslated();
    return x;
  }

  @Override
  public String getQueryString() {
    String x = super.getQueryString();
    String newQS = "";
    boolean amp = false;
    if (x == null) {
      x = "";
    } else if (x.trim().length() > 0) {
      amp = true;
    }
    for (final String p : params.keySet()) {
      if (amp) {
        newQS += "&";
      } else {
        amp = true;
      }
      newQS += p + "=" + params.get(p);
    }
    x = x + newQS;
    return x;
  }

  @Override
  public String getRequestURI() {
    return requestURI;
  }

  @Override
  public StringBuffer getRequestURL() {
    final StringBuffer x = new StringBuffer(requestURI);
    return x;
  }

  @Override
  public String getServletPath() {
    return requestURI;
  }

}
