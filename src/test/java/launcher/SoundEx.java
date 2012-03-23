/**
 * 
 */
package launcher;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.gbif.portal.dao.TaxonNameDAO;
import org.gbif.portal.model.TaxonName;
import org.gbif.portal.util.taxonomy.TaxonNameSoundEx;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Sets the sound ex stuff on the taxon name
 * @author tim
 */
public class SoundEx {
	protected static Log logger = LogFactory.getLog(SoundEx.class);
	private ApplicationContext context;
	private TaxonNameDAO taxonNameDAO;
	private void init() {
		String[] locations = {
				"classpath*:/**/applicationContext-*.xml",
				"classpath*:**/applicationContext-*.xml",
				"classpath*:org/gbif/portal/**/applicationContext-*.xml"
		};
		context = new ClassPathXmlApplicationContext(locations);
		taxonNameDAO = (TaxonNameDAO)context.getBean("taxonNameDAO");
	}
	
	private void soundEx() {
		boolean hasMore = true;
		int start = 0;
		TaxonNameSoundEx tnse = new TaxonNameSoundEx();
		while (hasMore) {
			logger.info("Starting at: " + start);
			List<TaxonName> names = taxonNameDAO.getTaxonName(start, 10001);
			if (names.size() < 10001) {
				hasMore = false;
			} else {
				start+=10000;
			}
			for (int i=0; i<(names.size()-1); i++) {
				TaxonName name = names.get(i);
				taxonNameDAO.setSearchableCanonical(name.getId(), tnse.soundEx(name.getCanonical()));
			}
		}		
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		SoundEx launcher = new SoundEx();
		launcher.init();
		launcher.soundEx();
		System.exit(1);
	}
	
	public static void printUsage(){
		System.out.println(SoundEx.class.getName());
	}
}