/*
 * Copyright 2011 Global Biodiversity Information Facility (GBIF)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.gbif.portal.filter;

import javax.servlet.FilterChain;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.UUID;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

public class RestfulFilterTest {

  @Test
  public void testDoFilter() throws Exception {
    HttpServletRequest req = mock(HttpServletRequest.class);

    HttpServletResponse resp = mock(HttpServletResponse.class);;
    when(resp.getWriter()).thenReturn(new PrintWriter(new StringWriter()));
    FilterChain chain = mock(FilterChain.class);

    RestfulFilter filter = new RestfulFilter();
    filter.doFilter(req, resp, chain);
  }

  @Test
  public void testFilter() throws Exception {
    RestfulFilter filter = new RestfulFilter();

    HttpServletRequest req = mock(HttpServletRequest.class);
    given(req.getServletPath()).willReturn("/species/45326");
    HttpServletRequest req2 = (HttpServletRequest) filter.filter(req);
    assertNull(req2.getParameter("action"));
    assertEquals("45326", req2.getParameter("id"));
    assertEquals("/species.do", req2.getServletPath());

    req = mock(HttpServletRequest.class);
    given(req.getServletPath()).willReturn("/species/45326/add");
    req2 = (HttpServletRequest) filter.filter(req);
    assertEquals(RestfulFilter.CRUD.CREATE.name(), req2.getParameter("action"));
    assertNull(req2.getParameter("id"));
    assertEquals("/species/45326.do", req2.getServletPath());

    req = mock(HttpServletRequest.class);
    given(req.getServletPath()).willReturn("/dataset/ded724e7-3fde-49c5-bfa3-03b4045c4c5f/del");
    req2 = (HttpServletRequest) filter.filter(req);
    assertEquals(RestfulFilter.CRUD.DELETE.name(), req2.getParameter("action"));
    assertEquals(UUID.fromString("ded724e7-3fde-49c5-bfa3-03b4045c4c5f").toString(), req2.getParameter("id"));
    assertEquals("/dataset.do", req2.getServletPath());

    req = mock(HttpServletRequest.class);
    given(req.getServletPath()).willReturn("/my/path/stays/TheSame/adds");
    req2 = (HttpServletRequest) filter.filter(req);
    assertNull(req2.getParameter("action"));
    assertNull(req2.getParameter("id"));
    assertEquals("/my/path/stays/TheSame/adds.do", req2.getServletPath());

    req = mock(HttpServletRequest.class);
    given(req.getServletPath()).willReturn("/my/path/AND/filename/stays/the/same.xml");
    req2 = (HttpServletRequest) filter.filter(req);
    assertNull(req2.getParameter("action"));
    assertNull(req2.getParameter("id"));
    assertEquals("/my/path/AND/filename/stays/the/same.xml", req2.getServletPath());

    req = mock(HttpServletRequest.class);
    given(req.getServletPath()).willReturn("/");
    req2 = (HttpServletRequest) filter.filter(req);
    assertNull(req2.getParameter("action"));
    assertNull(req2.getParameter("id"));
    assertEquals("/", req2.getServletPath());

  }

}
