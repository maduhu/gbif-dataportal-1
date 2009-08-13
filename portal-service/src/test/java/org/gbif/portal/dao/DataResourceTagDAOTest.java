package org.gbif.portal.dao;

import java.util.List;

import org.gbif.portal.dao.tag.SimpleTagDAO;
import org.gbif.portal.dto.tag.BiRelationTagDTO;
import org.springframework.test.AbstractDependencyInjectionSpringContextTests;

public class DataResourceTagDAOTest extends AbstractDependencyInjectionSpringContextTests {

		/**
		 * @see org.springframework.test.AbstractDependencyInjectionSpringContextTests#getConfigLocations()
		 */
		protected String[] getConfigLocations() {
			return new String [] {
					"classpath*:/**/applicationContext-*.xml",
					"classpath*:**/applicationContext-*.xml",
					"classpath*:org/gbif/portal/**/applicationContext-*.xml",
					"classpath*:/org/gbif/portal/**/impl/applicationContext-*-test.xml",
					"classpath*:org/gbif/portal/dao/applicationContext-dao-ro.xml",
					"classpath*:org/gbif/portal/dao/applicationContext-factories.xml",
					"classpath*:/org/gbif/portal/service/impl/applicationContext-service-test.xml"				
					};
		}
		
		public void testTaxonomicScopeTags(){
			
			
			SimpleTagDAO stDAO = (SimpleTagDAO) this.applicationContext.getBean("dataResourceTagDAO");
			List<BiRelationTagDTO> tags = stDAO.retrieveBiRelationTagsForEntity(4150, 411l);
			logger.debug(tags.size());
			
			
		}
}
