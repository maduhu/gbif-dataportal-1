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
package org.gbif.portal.web.tag;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Abstract tree browser tag. This tag renders the supplied list of nodes in a tree structure.
 * It uses the <code>getParentKey</code> method to retrieve the key for the ancestor. When
 * the ancestor changes it is assumed to a new level. The bottom level will have 1 or more nodes which
 * which will be rendered in a div of class <code>list</code>. Ancestors will be rendered in their own 
 * div of class <code>ancestor</code>.
 * 
 * @author dmartin
 */
public abstract class AbstractTreeBrowserTag extends TagSupport {

	private static final long serialVersionUID = 9120167499746959043L;
	protected static Log logger = LogFactory.getLog(AbstractTreeBrowserTag.class);
	
	/** The root node to render - this will be a link when no node is selected */
	protected Object rootNode;

	/** The currently selected node. Null in the case of a tree without a selected node */
	protected Object selectedNode;
	
	/** The nodes to render */
	protected List<Object> nodes;
	
	@Override
	public int doEndTag() throws JspException {
		StringBuffer sb = new StringBuffer();
		HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
		String contextPath = request.getContextPath();
		sb.append("<div><p id=\"smtFirstLevel\"");
		if(selectedNode==null){
			sb.append(" class=\"selected\">");
		} else {
			sb.append("><a href=\"");
			String rootLink = getRootLink(contextPath);
			sb.append(rootLink);
			sb.append("\">");
		}

		sb.append(getRootNodeDisplayValue());
		
		if(selectedNode!=null)
			sb.append("</a>");
		
		sb.append("</p>");
		addNodes(sb, pageContext, contextPath);
		sb.append("</div>");
		try{
			pageContext.getOut().write(sb.toString());
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}		
		return super.doEndTag();
	}

	/**
	 * Recursive method for appending nodes to the tree.
	 * 
	 * @param childNodes the child nodes to add to the tree
	 * @param sb the buffer to append HTML to
	 * @param pageContext
	 * @throws JspException
	 */
	void addNodes(StringBuffer sb, PageContext pageContext, String contextPath)  throws JspException{
		//create a copy of the list that we can remove elements from
		List<Object> nodeList = new ArrayList<Object>();
		nodeList.addAll(nodes);
		addNodesRecursively(nodeList, sb, contextPath);
	}	

	/**
	 * Recursive method for appending nodes to the tree.
	 * 
	 * @param conceptList the child nodes to add to the tree
	 * @param sb the buffer to append HTML to
	 * @param pageContext
	 * @throws JspException
	 */
	void addNodesRecursively(List<Object> nodes, StringBuffer sb, String contextPath)  throws JspException{

		//get the concept to add
		Object currentNode = nodes.remove(0);
		
		//get next one to compare parents
		Object nextNode = null;
		
		String currentParentKey = getParentKey(currentNode);
		if(!nodes.isEmpty())
			nextNode=nodes.get(0);	

		if(logger.isDebugEnabled())
			logger.debug("Current node:"+currentNode+", next:"+nextNode);

		String nextNodeParentKey = getParentKey(nextNode);
		
		//if they are the same rank then its a list class
		if(nextNode!=null && (	
						//if both null, then they are both root concepts
						(nextNodeParentKey==null && currentParentKey==null) || 
						(nextNodeParentKey!=null && nextNodeParentKey.equals(currentParentKey))
					)
				){
			//add all elements to list div
			sb.append("<div class=\"list\">");
			while (currentNode!=null && ( 
						//if both null, then they are both root concepts
						(getParentKey(currentNode)==null && currentParentKey==null) ||
						(getParentKey(currentNode).equals(currentParentKey))
					)
				){
				//render the concept
				addElement(currentNode, sb,  contextPath);
				//add the concept
				if(nodes.size()>0)
					currentNode = nodes.remove(0);
				else
					currentNode =null;
			}
			if(nodes.size()>0)
				addNodesRecursively(nodes, sb, contextPath);
			sb.append("</div>");
		} else {
			//if they dont have the same parent then its a recursive call
			sb.append("<div class=\"ancestor\">");
			addElement(currentNode, sb,  contextPath);
			if(nodes.size()>0)
				addNodesRecursively(nodes, sb, contextPath);
			sb.append("</div>");
		}
	}		

	/**
	 * Renders an node in the tree, displaying a name and linking to a relevant source.
	 * 
	 * @param node
	 * @param sb
	 * @param contextPath
	 */
	protected abstract void addElement(Object node, StringBuffer sb, String contextPath);

	/**
	 * Retrieve the parent key for this object. Dependent on the object type.
	 * 
	 * @param node
	 * @return
	 */
	protected abstract String getKey(Object node);	
	
	
	/**
	 * Retrieve the parent key for this object. Dependent on the object type.
	 * It is expected implementations will do a instanceof test to get the
	 * type and then call the relevant method on the node type to retrieve
	 * a key for its parent.
	 * 
	 * @param node
	 * @return retrieves the parent key for the node
	 */
	protected abstract String getParentKey(Object node);

	/**
	 * Create the a link to the root of this tree.
	 * @param contextPath
	 * @return the link for the root node
	 */
	protected abstract String getRootLink(String contextPath);

	/**
	 * Retrieves the root node display value.
	 * @return the display value for the root node.
	 */
	protected abstract String getRootNodeDisplayValue();

	/**
	 * @param nodes the nodes to set
	 */
	public void setNodes(List<Object> nodes) {
		this.nodes = nodes;
	}

	/**
	 * @param rootNode the rootNode to set
	 */
	public void setRootNode(Object rootNode) {
		this.rootNode = rootNode;
	}

	/**
	 * @param selectedNode the selectedNode to set
	 */
	public void setSelectedNode(Object selectedNode) {
		this.selectedNode = selectedNode;
	}
}