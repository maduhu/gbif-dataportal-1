package org.gbif.portal.filter;

import com.google.inject.Singleton;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Singleton
public class CharsetFilter implements Filter {

  @Override
  public void init(final FilterConfig filterConfig) throws ServletException {
  }

  @Override
  public void destroy() {
  }

  @Override
  public void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain chain) throws IOException, ServletException {
    if (request instanceof HttpServletRequest) {
      final HttpServletRequest req = (HttpServletRequest) request;
      final HttpServletResponse resp = (HttpServletResponse) response;

      req.setCharacterEncoding("UTF-8");

      chain.doFilter(req, resp);
      final String contentType = resp.getContentType();
      if (contentType != null && contentType.startsWith("text/html")) {
        resp.setCharacterEncoding("UTF-8");
      }
    }
  }

}
