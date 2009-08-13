/**
 * 
 */
package launcher;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.gbif.portal.util.workflow.quartz.WorkflowLauncherJob;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleTrigger;
import org.quartz.Trigger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Launcher for indexing tests.
 * 
 * Use this to extract harvested data. To reextract harvested data do the following:
 * 
 * 1) truncate qrtz_triggers or update qrtz_triggers.NEXT_FIRE_TIME to a future time (i.e. a week away).
 * 2) truncate occurrence_record, taxon_concept, taxon_name (this is probably unnecessary but for completeness)
 * 3) Run portal-index/test/launcher/CreateExtractJobsLauncher - this creates the necessary quartz jobs for extraction.
 * 4) Run HarvestingServer  - leave running till data is extracted
 * 
 * @author tim
 * @author dmartin
 */
public class CreateExtractJobsLauncher {
	private ApplicationContext context;
	
	private void init() {
		String[] locations = {"classpath*:/**/applicationContext-*.xml",
				"classpath*:**/applicationContext-*.xml",
				"classpath*:org/gbif/portal/**/applicationContext-*.xml"
		};
		
		context = new ClassPathXmlApplicationContext(locations);
	}
	
	private void index(int minId, int maxId) {
		
		Scheduler scheduler = (Scheduler)context.getBean("scheduleFactoryBean");
		SimpleDateFormat sdfSec = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		String time = sdfSec.format(new Date());
    	
		for (int id=minId; id<=maxId; id++) {
			try {
				String jobName = Integer.toString(id);
				String jobGroup = "extract";
				String triggerName = id + " - " + time;
				String triggerGroup = "extract";
				JobDetail jobDetail = scheduler.getJobDetail(jobName, jobGroup);
				boolean jobExisted = true;
				if (jobDetail == null) {
					jobDetail = new JobDetail(jobName,jobGroup,WorkflowLauncherJob.class);
					jobExisted = false;
				}
				
				Trigger trigger = null;    	
				Date toStart = new Date(System.currentTimeMillis());
				trigger = new SimpleTrigger(triggerName, triggerGroup, toStart);
				trigger.setJobName(jobName);
				trigger.setJobGroup(jobGroup);				
				trigger.setDescription("Extract, created on " + time);
				
				Map<String, Object> seed = new HashMap<String, Object>();
				seed.put("resourceAccessPointId", new Long(id));    		
				jobDetail.getJobDataMap().put(WorkflowLauncherJob.SEED_DATA_KEY, seed);
				jobDetail.getJobDataMap().put(WorkflowLauncherJob.PROPERTY_STORE_KEY_WORKFLOW_KEY, "GBIF:INDEX:1.0:extractOccurrence");
				
				List<String> namespaces = new LinkedList<String>();
				namespaces.add("http://www.gbif.org/portal/index/1.0");
				jobDetail.getJobDataMap().put(WorkflowLauncherJob.PROPERTY_STORE_NAMESPACES_KEY, namespaces);
				if (jobExisted) {
					scheduler.scheduleJob(trigger);
				} else {
					scheduler.scheduleJob(jobDetail, trigger);
				}
			} catch (SchedulerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		System.out.println("Done");
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		CreateExtractJobsLauncher launcher = new CreateExtractJobsLauncher();
		if(args.length>0){
			Integer minId =null;
			Integer maxId=null;
			try {
				minId = Integer.parseInt(args[0]);
				if(args.length>1)
					maxId = Integer.parseInt(args[1]);
			} catch (NumberFormatException e) {
				printUsage();
				return;
			}
			if(maxId==null)
				maxId = minId;
			launcher.init();
			launcher.index(minId, maxId);
		} else {
			printUsage();
		}
	}
	
	public static void printUsage(){
		System.out.println(CreateExtractJobsLauncher.class.getName()+" <resource-access-point-min-id> <resource-access-point-max-id>");
		System.out.println(CreateExtractJobsLauncher.class.getName()+" <resource-access-point-id>");
		System.out.println(" e.g. "+HarvestAccessPointLauncher.class.getName()+" 99");		
		System.out.println(" e.g. "+HarvestAccessPointLauncher.class.getName()+" 1 1223");		
	}
}
