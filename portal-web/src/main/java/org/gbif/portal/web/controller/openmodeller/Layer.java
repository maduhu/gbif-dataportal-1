/**
 * 
 */
package org.gbif.portal.web.controller.openmodeller;

/**
 * Simple POJO representing a niche modelling layer.
 * 
 * @author dmartin
 */
public class Layer {
	
	/** A short unique identifier for this layer */
	protected String id;
	/** i18n key for display name for this layer */
	protected String name;	
	/** The full file path for this layer */
	protected String path;

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the path
	 */
	public String getPath() {
		return path;
	}
	/**
	 * @param path the path to set
	 */
	public void setPath(String path) {
		this.path = path;
	}
}	