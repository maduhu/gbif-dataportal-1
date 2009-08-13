package org.gbif.portal.web.controller.openmodeller;
/**
 * Information holder use to prepare OpenModeller request.
 * @author David Neufeld
 */
public class Parameter {

	public String id = null;
	public String val = null;

	/**
	* Constuct the OM parameter
	*/  
	public Parameter(String id, String val) {
		this.id = id;
		this.val = val;
	}

}
