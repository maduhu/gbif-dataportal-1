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
package org.gbif.portal.util.mhf.message.impl.xml;

import org.dom4j.Document;
import org.dom4j.Node;
import org.dom4j.XPath;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.gbif.portal.util.mhf.message.Message;
import org.gbif.portal.util.mhf.message.MessageAccessException;
import org.gbif.portal.util.mhf.message.MessageParseException;

import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


/**
 * A wrapper around an XML document providing cached read access.
 * <p/>
 * Access is through XPath expressions and the results are cached locally.
 * <p/>
 * The cache, dom4j tree and the rawXml are instanciated at creation and access to them is
 * protected.  Overriding these and making access more visible is highly discouraged as it
 * can corrupt the index.
 * <p/>
 * This implementation is not thread safe - many threads accessing the cache simultaniously might
 * provide some strange results.
 *
 * @author trobertson
 */
public class XMLMessage implements Message {
  /**
   * Define some key prefixes
   * The same xpath can return different results depending on the method
   * called, thus prefix with a key to avoid ClassCastExceptions when successive
   * methods are called for the same xpath but different return types
   */
  protected static final String GET_PART_AS_STRING_KEY_PREFIX = "1:";
  protected static final String GET_PARTS_AS_STRING_KEY_PREFIX = "2:";
  protected static final String GET_PART_KEY_PREFIX = "3:";
  protected static final String GET_PARTS_KEY_PREFIX = "4:";

  /**
   * The raw content
   */
  protected Object rawXml;

  /**
   * The parsed document
   */
  protected Document document;

  /**
   * The results of the cache keyed on the XPath expression used to locate them
   */
  protected Map<String, Object> cache = new HashMap<String, Object>();

  /**
   * Creates a message with the Raw Data and the Document
   *
   * @param document To wrap
   * @param rawXml   That the document represents
   */
  public XMLMessage(Document document, String rawXml) {
    setDocument(document);
    setRawData(rawXml);
  }

  /**
   * It may be that the raw data is expensive so it will only be created if necessary
   *
   * @param document To wrap
   */
  public XMLMessage(Document document) {
    setDocument(document);
  }

  /**
   * Gets a the value as a string for the given XPath
   *
   * @param location Xpath to evaluate
   * @return The value
   */
  @SuppressWarnings("unchecked")
  public String getPartAsString(Object location) throws MessageAccessException {
    if (!(location instanceof XPath)) {
      throw new MessageAccessException("Only XPath location's are supported for accessing XMLMessage parts - " +
        "received: " + location.getClass());
    }
    XPath xpath = (XPath) location;

    String key = GET_PART_AS_STRING_KEY_PREFIX + xpath.getText();
    if (getCache().containsKey(key)) {
      return (String) getCache().get(key);
    } else {
      String result = xpath.valueOf(getDocument());
      // Do not do this - it does not evaluate expressions returning "false"
      // for example
      // String result = xpath.selectSingleNode(getDocument()).getText();
      getCache().put(key, result);
      return result;
    }
  }

  /**
   * Gets a list of Strings for the multiple values that will be
   * returned from the XPath
   *
   * @param location Xpath to evaluate
   * @return The List of Node
   */
  @SuppressWarnings("unchecked")
  public List<String> getPartsAsString(Object location) throws MessageAccessException {
    if (!(location instanceof XPath)) {
      throw new MessageAccessException("Only XPath location's are supported for accessing XMLMessage parts - " +
        "received: " + location.getClass());
    }
    XPath xpath = (XPath) location;
    String key = GET_PARTS_AS_STRING_KEY_PREFIX + xpath.getText();
    if (getCache().containsKey(key)) {
      return (List<String>) getCache().get(key);
    } else {
      List<Node> result = (List<Node>) xpath.selectNodes(getDocument());
      List<String> resultsAsString = new LinkedList<String>();
      for (Node node : result) {
        resultsAsString.add(node.getText());
      }
      getCache().put(key, resultsAsString);
      return resultsAsString;
    }
  }

  /**
   * Gets a list of MessageIndex
   * returned from the XPath
   *
   * @param location Xpath to evaluate
   * @return The List of Node
   */
  @SuppressWarnings("unchecked")
  public List<Message> getParts(Object location) throws MessageAccessException, MessageParseException {
    if (!(location instanceof XPath)) {
      throw new MessageAccessException("Only XPath location's are supported for accessing XMLMessage parts - " +
        "received: " + location.getClass());
    }
    XPath xpath = (XPath) location;

    String key = GET_PARTS_KEY_PREFIX + xpath.getText();
    if (getCache().containsKey(key)) {
      return (List<Message>) getCache().get(key);
    } else {
      List<Node> result = (List<Node>) xpath.selectNodes(getDocument());
      List<Message> resultsAsIndex = new LinkedList<Message>();
      for (Node node : result) {
        Message indexedResult = new XMLMessageFactory().build(node, true);
        resultsAsIndex.add(indexedResult);
      }
      getCache().put(key, resultsAsIndex);
      return resultsAsIndex;
    }
  }

  /**
   * Gets a list of MessageIndex
   * returned from the XPath
   *
   * @param location Xpath to evaluate
   * @return The List of Node
   */
  @SuppressWarnings("unchecked")
  public Message getPart(Object location) throws MessageAccessException, MessageParseException {
    if (!(location instanceof XPath)) {
      throw new MessageAccessException("Only XPath location's are supported for accessing XMLMessage parts - " +
        "received: " + location.getClass());
    }
    XPath xpath = (XPath) location;

    String key = GET_PART_KEY_PREFIX + xpath.getText();
    if (getCache().containsKey(key)) {
      return (Message) getCache().get(key);
    } else {
      Node result = xpath.selectSingleNode(getDocument());
      Message indexedResult = new XMLMessageFactory().build(result, true);
      getCache().put(key, indexedResult);
      return indexedResult;
    }
  }

  /**
   * @return Returns the cache.
   */
  protected Map<String, Object> getCache() {
    return cache;
  }

  /**
   * @param cache The cache to set.
   */
  protected void setCache(Map<String, Object> cache) {
    this.cache = cache;
  }

  /**
   * @return Returns the document.
   */
  protected Document getDocument() {
    return document;
  }

  /**
   * @param document The document to set.
   */
  protected void setDocument(Document document) {
    this.document = document;
  }

  /**
   * @param rawXml The rawXml to set.
   */
  protected void setRawData(Object rawXml) {
    this.rawXml = rawXml;
  }

  /**
   * @see org.gbif.portal.util.mhf.Message#getRawData()
   */
  public String getRawData() throws MessageAccessException {
    if (rawXml != null)
      return rawXml.toString();
    else {
      // build a short string
      StringWriter writer = new StringWriter();
      OutputFormat outformat = OutputFormat.createCompactFormat();
      outformat.setSuppressDeclaration(true);

      XMLWriter xmlWriter = new XMLWriter(writer, outformat);
      try {
        xmlWriter.write(getDocument());
        xmlWriter.flush();
        String rawXml = writer.toString();
        setRawData(rawXml);
        return rawXml;
      } catch (IOException e) {
        throw new MessageAccessException("Error writing the XML Document", e);
      } finally {
        try {
          xmlWriter.close();
        } catch (IOException e) {
        }
        try {
          writer.close();
        } catch (IOException e) {
        }
      }
    }
  }

  /**
   * @see org.gbif.portal.util.mhf.message.impl.xml.BeautifiedData#getBeautifiedData()
   */
  public String getLoggableData() throws MessageAccessException {
    // build a beautiful string
    StringWriter writer = new StringWriter();
    OutputFormat outformat = OutputFormat.createPrettyPrint();
    outformat.setSuppressDeclaration(true);
    XMLWriter xmlWriter = new XMLWriter(writer, outformat);
    try {
      xmlWriter.write(getDocument());
      xmlWriter.flush();
      String rawXml = writer.toString();
      setRawData(rawXml);
      return rawXml.trim();
    } catch (IOException e) {
      throw new MessageAccessException("Error writing the XML Document", e);
    } finally {
      try {
        xmlWriter.close();
      } catch (IOException e) {
      }
      try {
        writer.close();
      } catch (IOException e) {
      }
    }
  }

  /**
   * Returns the loggbale data
   */
  @Override
  public String toString() {
    try {
      return getLoggableData();
    } catch (Exception e) {
      return super.toString();
    }
  }
}
