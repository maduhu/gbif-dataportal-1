package org.gbif.portal;

import org.springframework.test.AbstractDependencyInjectionSpringContextTests;

public class GeospatialIssueResolverTest extends AbstractDependencyInjectionSpringContextTests {
	
	/**
	 * @see org.springframework.test.AbstractDependencyInjectionSpringContextTests#getConfigLocations()
	 */
	protected String[] getConfigLocations() {
		return new String [] {
				"classpath:/data-applicationContext.xml",
				"classpath:/data-applicationContext-dao.xml"
		};
	}

	public void testResolve() {
		GeospatialIssueResolver geospatialIssueResolver = (GeospatialIssueResolver) getBean("geospatialIssueResolver");
		geospatialIssueResolver.resolve();
	}
	
	protected Object getBean(String name) {
		return applicationContext.getBean(name);
	}
}
