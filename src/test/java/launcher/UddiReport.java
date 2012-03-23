/**
 * 
 */
package launcher;

import java.util.Vector;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.uddi4j.client.UDDIProxy;
import org.uddi4j.datatype.Name;
import org.uddi4j.datatype.binding.BindingTemplate;
import org.uddi4j.datatype.binding.TModelInstanceInfo;
import org.uddi4j.datatype.service.BusinessService;
import org.uddi4j.datatype.tmodel.TModel;
import org.uddi4j.response.BusinessInfo;
import org.uddi4j.response.BusinessList;
import org.uddi4j.response.ServiceDetail;
import org.uddi4j.response.ServiceInfo;


/**
 * Synchronises the DataResource
 * 
 * @author tim
 */
public class UddiReport {
	/**
	 * Logger
	 */
	protected Log logger = LogFactory.getLog(getClass());
	
	/**
	 * @TODO Move out of course
	 */
	protected static final String INQUIRY_URL = "http://registry.gbif.net/uddi/inquiry";

	/**
	 * A launcher
	 * @param args Ignored
	 * @TODO just a quick entry - to be removed of course...
	 */
	public static void main(String[] args) {
		UddiReport me = new UddiReport();
		me.harvest();
	}
	
	/**
	 * The launch of harvesting
	 */
	@SuppressWarnings("unchecked")
	public void harvest() {
		try {
			UDDIProxy proxy = new UDDIProxy();
			proxy.setInquiryURL(INQUIRY_URL);
			
			Vector names = new Vector();
			names.add(new Name("%"));
			
			// BUSINESSES
			BusinessList businessList = proxy.find_business(names, null, null, null, null, null, 10000);
			Vector  businessInfoVector = businessList.getBusinessInfos().getBusinessInfoVector();
			for (int i=0; i<businessInfoVector.size(); i++) {
				BusinessInfo businessInfo = (BusinessInfo)businessInfoVector.elementAt(i);
				logger.info(businessInfo.getDefaultNameString());
				logger.info(" - " + businessInfo.getDefaultDescriptionString());
				
				// SERVICES
				Vector serviceInfoVector = businessInfo.getServiceInfos().getServiceInfoVector();
				for (int j=0; j<serviceInfoVector.size(); j++) {
					ServiceInfo serviceInfo = (ServiceInfo)serviceInfoVector.elementAt(j);
					ServiceDetail serviceDetail = proxy.get_serviceDetail(serviceInfo.getServiceKey());
					Vector businessServiceVector = serviceDetail.getBusinessServiceVector();					
					for (int l=0; l<businessServiceVector.size(); l++) {
						BusinessService businessService = (BusinessService) businessServiceVector.elementAt(l);
						logger.info("   " + (j+1) + ") " + businessService.getDefaultNameString());
						logger.info("      " + businessService.getDefaultDescriptionString());
						
						// BINDING TEMPLATES
						Vector bindingTemplateVector = businessService.getBindingTemplates().getBindingTemplateVector();
						for (int m=0; m<bindingTemplateVector.size(); m++) {
							BindingTemplate bindingTemplate = (BindingTemplate) bindingTemplateVector.elementAt(m);
							logger.info("      " + (m+1) + ") " + bindingTemplate.getDefaultDescriptionString());
							logger.info("         " + bindingTemplate.getAccessPoint().getText());
							
							// TMODELS							
							Vector TModelInstanceInfoVector = bindingTemplate.getTModelInstanceDetails().getTModelInstanceInfoVector();
							for (int n=0; n<TModelInstanceInfoVector.size(); n++) {
								TModelInstanceInfo tModelInstanceInfo = (TModelInstanceInfo) TModelInstanceInfoVector.elementAt(n);
								Vector TModelDetailVector = proxy.get_tModelDetail(tModelInstanceInfo.getTModelKey()).getTModelVector();
								for (int o=0; o<TModelDetailVector.size(); o++) {
									TModel tModel = (TModel) TModelDetailVector.elementAt(o);
									logger.info("         - " + tModel.getNameString());									
								}								
							}
						}
					}
				}
			}
			

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}			
		
	}
}
