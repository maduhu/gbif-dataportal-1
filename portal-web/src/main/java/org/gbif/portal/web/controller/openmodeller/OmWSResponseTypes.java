package org.gbif.portal.web.controller.openmodeller;

/**
 * Keep track of the format for the openModeller response xml
 * @author Dave Neufeld
 */
public final class OmWSResponseTypes {
	
	public static final int PINGRESPONSE = 0;
	public static final int GETLAYERSRESPONSE = 1;
	public static final int GETALGORITHMSRESPONSE = 2;	
	public static final int OMJOBRESPONSE = 3;	
	public static final int OMMODELPROGRESSRESPONSE = 4;
	public static final int GETLAYERASURLRESPONSE = 5;
	public static final int OMMODELPARAMSRESPONSE = 6;	
    
	/**
     * Constuct and initialize supported OM response types
     */  	
	public OmWSResponseTypes() {

	}



}
