/**
 * 
 */
package launcher;

import org.gbif.portal.dao.OccurrenceRecordDAO;
import org.gbif.portal.model.OccurrenceRecord;
import org.gbif.portal.util.geospatial.CellIdUtils;
import org.gbif.portal.util.geospatial.UnableToGenerateCellIdException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Launcher for indexing tests
 * @author dave
 */
public class CentiCellUpdateLauncher {
	private ApplicationContext context;

	private long startId = 0;
	private long lastId = 1000;
	
	private void init() {
		String[] locations = {"classpath*:/**/applicationContext-*.xml",
				"classpath*:**/applicationContext-*.xml",
				"classpath*:org/gbif/portal/**/applicationContext-*.xml"
		};
		
		context = new ClassPathXmlApplicationContext(locations);
	}
	
	private void index() {
		for(long i=startId; i<=lastId; i++){
			OccurrenceRecordDAO occurrenceRecordDAO = (OccurrenceRecordDAO) context.getBean("occurrenceRecordDAO");
			OccurrenceRecord or = occurrenceRecordDAO.getById(i);
			if(or!=null && or.getLatitude()!=null && or.getLongitude()!=null){
				try {
					int centiCelId = CellIdUtils.toCentiCellId(or.getLatitude(), or.getLongitude());
					int cellId = CellIdUtils.toCellId(or.getLatitude(), or.getLongitude());
					or.setCentiCellId(centiCelId);
					or.setCellId(cellId);
					occurrenceRecordDAO.update(or);
//					if(i%10000 ==0)
//						System.out.println("Updated up to "+i);
				} catch (UnableToGenerateCellIdException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		System.out.println("Finished.");
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		try {
			CentiCellUpdateLauncher launcher = new CentiCellUpdateLauncher();
			launcher.init();
			
			launcher.setStartId(new Long(args[0]));
			launcher.setLastId(new Long(args[1]));
			
			launcher.index();
		} catch (Exception e) {
			System.out.println("Usage:  "+CentiCellUpdateLauncher.class.toString()+" <start-id> <last-id>");
			e.printStackTrace();
		}
	}

	/**
	 * @param lastId the lastId to set
	 */
	public void setLastId(long lastId) {
		this.lastId = lastId;
	}

	/**
	 * @param startId the startId to set
	 */
	public void setStartId(long startId) {
		this.startId = startId;
	}
}