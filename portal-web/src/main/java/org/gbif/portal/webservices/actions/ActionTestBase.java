/**
 * 
 */
package org.gbif.portal.webservices.actions;

import org.springframework.test.AbstractDependencyInjectionSpringContextTests;

/**
 * @author Ali Kalufya
 * This is the base of all action tests, it defines some common functionality and the
 * Spring configurations for loading
 */
public class ActionTestBase extends AbstractDependencyInjectionSpringContextTests {

	//protected GbifXmlOptions xmlOptions;
	
	/* (non-Javadoc)
	 * @see org.springframework.test.AbstractSingleSpringContextTests#getConfigLocations()
	 */
	@Override
	protected String[] getConfigLocations() {
		return new String [] {
				"classpath*:/org/gbif/portal/**/applicationContext-*.xml"};
	}



}
