/**
 * 
 */
package org.gbif.portal.harvest.workflow.activity.dwc.text;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.XPath;
import org.dom4j.io.SAXReader;
import org.dom4j.xpath.DefaultXPath;
import org.gbif.portal.dao.DataResourceDAO;
import org.gbif.portal.dao.RawOccurrenceRecordDAO;
import org.gbif.portal.dao.ResourceAccessPointDAO;
import org.gbif.portal.harvest.workflow.activity.dwc.text.Meta.DwCFile;
import org.gbif.portal.model.DataResource;
import org.gbif.portal.model.RawOccurrenceRecord;
import org.gbif.portal.model.ResourceAccessPoint;

import au.com.bytecode.opencsv.CSVReader;


/**
 * A TextArchiveHarvester which is written to support the IPT-RC-1.0 version, but should eventually
 * be maintained to harvest all DwcText formats
 * 
 * @author tim
 */
public class TextArchiveHarvester {
	protected static MultiThreadedHttpConnectionManager connectionManager = new MultiThreadedHttpConnectionManager();
	protected static Log log = LogFactory.getLog(TextArchiveHarvester.class);

	// mappings
	protected Set<String> occurrenceTypes = new HashSet<String>();
	protected Map<String, String> dwc2occurrenceMapping = new HashMap<String,String>();
	
	// DAOs
	protected RawOccurrenceRecordDAO rawOccurrenceRecordDAO;
	protected DataResourceDAO dataResourceDAO;
	protected ResourceAccessPointDAO resourceAccessPointDAO;
	
	/**
	 * The harvest entry point
	 * @param dataURL which must exist
	 * @param workingDirectory to extract the archive to
	 * @param dataProviderId being used
	 * @param resourceAccessPointId being used
	 * @param dataResourceId if null, then an EML metadata document is expected 
	 * @param isZippedArchive is for when the whole thing is zipped up (IPT style) as opposed to a meta.xml on a URL
	 */
	public void harvest(String dataURL, String workingDirectory, long dataProviderId, long resourceAccessPointId, Long dataResourceId, boolean isZippedArchive) {
		try {
			if (isZippedArchive) {
				unzip(dataURL, workingDirectory);
			} else {
				// dataUrl points to a meta.xml on the internet
				getFile(dataURL, workingDirectory, "meta.xml");				
			}
				
			if ((dataResourceId == null || dataResourceId<1) && isZippedArchive) {
				log.info("Creating new DataResource");
				dataResourceId = createDataResource(dataProviderId, workingDirectory + "/eml.xml");
				log.info("DataResource created, setting ResourceAccessPoint[" + resourceAccessPointId + "] to use new identifier for data resource[" + dataResourceId + "]");
				ResourceAccessPoint rap = resourceAccessPointDAO.getById(resourceAccessPointId);
				rap.setDataResourceId(dataResourceId);
				resourceAccessPointDAO.updateOrCreate(rap);
			} else if (dataResourceId == null || dataResourceId<1) {
				log.error("Cannot create a data resource without an EML source.  Please set the data resource id on the RAP[" + resourceAccessPointId + "]");
				return;
			}
			
			Meta meta = Meta.build(new File(workingDirectory + "/meta.xml"));
			
			// handle the content of the core files
			// an index of the GUID->ROR_ID is maintained in memory
			// THIS MIGHT BE A CAUSE OF ANY MEMORY ISSUES BUT IS CONSIDERED ACCEPTABLE AT THE MOMENT
			Map<String, Long> guid2gbifId = new HashMap<String, Long>();
			int count=0;
			for (Meta.DwCFile dwcFile : meta.getFiles()) {
				log.info(dwcFile.getLocation() + " has " + dwcFile.getFields().size() + " fields described");
				
				if (dwcFile.getLocation() != null) {
				
					if (dwcFile.getRowType() != null && occurrenceTypes.contains(dwcFile.getRowType())) {
						log.info("Found a file that contains occurrence records");
						
						// the location might reference an external file which is not handled here
						if (!dwcFile.getLocation().startsWith("http://")) {
							synchroniseOccurrence(dwcFile, workingDirectory, guid2gbifId, dataProviderId, resourceAccessPointId, dataResourceId);
							
						} else {
							log.warn("A remote TEXT file has been described and will be retrieved");
							// we use a count to name the files because locations do not make good file names...
							getFile(dwcFile.getLocation(), workingDirectory, "dataFile" + count + ".txt");
							dwcFile.setLocation("dataFile" + count + ".txt");
							synchroniseOccurrence(dwcFile, workingDirectory, guid2gbifId, dataProviderId, resourceAccessPointId, dataResourceId);
							count++;
						}
					}					
				}
			}	
			
			
			
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}
	
	/**
	 * This will build the metadata for the resource
	 * @param dataProviderId being used
	 * @param emlFile To get the metadata from
	 * @return the ID of the created dataResource
	 * @throws DocumentException 
	 * @throws MalformedURLException 
	 */
	protected Long createDataResource(long dataProviderId, String emlFile) throws MalformedURLException, DocumentException {
		SAXReader xmlReader = new SAXReader();
		Document doc = xmlReader.read(new File(emlFile));
		Map<String, String> namespaceMapping = new HashMap<String, String>();
	    namespaceMapping.put("eml", "eml://ecoinformatics.org/eml-2.0.1");
	    
	    DataResource dataResource = new DataResource();
	    dataResource.setDataProviderId(dataProviderId);
	    
	    XPath xpath = new DefaultXPath("/eml:eml/dataset/title");
	    xpath.setNamespaceURIs(namespaceMapping);
	    dataResource.setName(xpath.selectSingleNode(doc).getText());
	    
	    // TODO - the rest of the data resource details
	    
	    long id = dataResourceDAO.create(dataResource);
	    
	    return id;
	}
	
	/**
	 * This will read the actual source file and using the spring configured mapping will create the 
	 * RawOccurrenceRecord and synchronise it with the database
	 * @param meta
	 * @param workingDirectory
	 * @throws IOException
	 */
	protected void synchroniseOccurrence(DwCFile meta, String workingDirectory, Map<String, Long> guid2gbifId, long dataProviderId, long resourceAccessPointId, long dataResourceId) throws IOException {
		log.info("Synchronising " + meta.getLocation());
		File f = new File(workingDirectory + "/" + meta.getLocation());
		
		Map<Integer, String> occurrenceMapping = getIndexesToExtract(meta.getFields());
				
		
		String encoding = "ISO-8859-1";
		if ("UTF-8".equals(meta.getEncoding())
				|| "UTF-16".equals(meta.getEncoding())) {
			encoding = meta.getEncoding();
		}
		// windows-1252 WinLatin in Java?
		
		FileInputStream fis = new FileInputStream(f); 
		InputStreamReader in = new InputStreamReader(fis, encoding);
		
		// THIS IS A QUICK HACK... need to handle enclosing characters properly!!!
		CSVReader csvReader = null;
		log.info("Terminator: " + meta.getFieldTerminator());
		if ("\\t".equals(meta.getFieldTerminator())) {
			log.info("Working with a TAB file");
			csvReader = new CSVReader(in, '\t', '\u0000', meta.getHeaderLinesToIgnore());
		} else {
			log.info("Working with a CSV file");
			csvReader = new CSVReader(in, ',', '"', meta.getHeaderLinesToIgnore());
		}
		
		try {
			String [] nextLine;
			int lineCount=0;
			while ((nextLine = csvReader.readNext()) != null) {
				lineCount++;
				try {
					RawOccurrenceRecord ror = new RawOccurrenceRecord();
					for (Integer index : occurrenceMapping.keySet()) {
						if (lineCount%1000 == 0)
							log.info("line[" + lineCount + "] - " + occurrenceMapping.get(index) + ": " + nextLine[index]);
						
						BeanUtils.setProperty(ror, occurrenceMapping.get(index), nextLine[index]);
					}
					ror.setDataResourceId(dataResourceId);
					ror.setResourceAccessPointId(resourceAccessPointId);
					ror.setDataProviderId(dataProviderId);
					RawOccurrenceRecord existing = rawOccurrenceRecordDAO.getUniqueRecord(ror.getDataResourceId(), ror.getInstitutionCode(), ror.getCollectionCode(), ror.getCatalogueNumber(), null);
					if (existing != null) {
						ror.setId(existing.getId());
						// This is IPT specific, but the first column is the GUID
						guid2gbifId.put(nextLine[0], rawOccurrenceRecordDAO.updateOrCreate(ror));
						
					} else {
						rawOccurrenceRecordDAO.create(ror);
						// This is IPT specific, but the first column is the GUID
						guid2gbifId.put(nextLine[0], rawOccurrenceRecordDAO.updateOrCreate(ror));
					}
					
				} catch (Exception e) {
					log.error("Bad row somewhere around line: " + lineCount, e);
				}
			}
		} finally {
		    csvReader.close();
		}
	}
	
	/**
	 * This will join the dwc2occurrenceMapping and the source file mapping and 
	 * extract the indexes that we are interested in
	 * @param source coming from the metafile
	 * @return The indexes in the metafile that we are interested in
	 */
	protected Map<Integer, String> getIndexesToExtract(Map<String, Integer> source) {
		Map<Integer, String> occurrenceMapping = new HashMap<Integer, String>();
		for (Map.Entry<String, Integer> entry : source.entrySet()) {
			if (dwc2occurrenceMapping.containsKey(entry.getKey())) {
				occurrenceMapping.put(entry.getValue(), dwc2occurrenceMapping.get(entry.getKey()));
			}
		}
		
		if (log.isDebugEnabled()) {
			for (Map.Entry<Integer, String> entry : occurrenceMapping.entrySet())
				log.debug("Extracting field[" + entry.getKey() + "] to apply to RawOccurrenceRecord." + entry.getValue());
		}
		
		return occurrenceMapping;
	}
 	
	// pulls a file from a URL
	public void getFile(String url, String workingDirectory, String fileName) throws HttpException, IOException {
	  	HttpClient client = new HttpClient(connectionManager);	
		GetMethod get = new GetMethod(url);
		FileOutputStream output = null;
		InputStream stream = null;
		byte[] buffer = new byte[2048];
		try {
			log.info("Executing request for [" + url + "]");
			client.executeMethod(get);
			stream = get.getResponseBodyAsStream();  
        	String outpath = workingDirectory + "/" + fileName;
        	File outputDirectory = new File(workingDirectory);
        	if (!outputDirectory.exists()) {
        		outputDirectory.mkdirs();
        	}
            output = new FileOutputStream(outpath);
            int len = 0;
            while ((len = stream.read(buffer)) > 0) {
                output.write(buffer, 0, len);
            }
            
			log.info("Url [" + url+ "] was pulled to [" + workingDirectory + "]");
		} finally {
            try {
				get.releaseConnection();
			} catch (RuntimeException e) {
				log.error(e);
			}
            try {
				if (output != null) output.close();
			} catch (RuntimeException e) {
				log.error(e);
			}
            try {
				stream.close();
			} catch (RuntimeException e) {
				log.error(e);
			}
        }	
	}
	
	/**
	 * This will call the url and unpack the results into the working directory overriding anything 
	 * already there with the same name
	 * @param url To request from
	 * @param workingDirectory To unpack to
	 * @throws HttpException On comms error
	 * @throws IOException On file error
	 */
	public void unzip(String url, String workingDirectory) throws HttpException, IOException {
	  	HttpClient client = new HttpClient(connectionManager);	
		GetMethod get = new GetMethod(url);
		FileOutputStream output = null;
		byte[] buffer = new byte[2048];
		ZipInputStream stream = null;
		try {
			log.info("Executing request for [" + url + "]");
			client.executeMethod(get);
			stream = new ZipInputStream(get.getResponseBodyAsStream());
			ZipEntry entry;
			int count = 0;
            while((entry = stream.getNextEntry())!=null) {
            	log.info(String.format("Entry: %s len %d added %TD",
                        entry.getName(), entry.getSize(),
                        new Date(entry.getTime())));
            	count++;
            	
            	String outpath = workingDirectory + "/" + entry.getName();
                output = new FileOutputStream(outpath);
                int len = 0;
                while ((len = stream.read(buffer)) > 0) {
                    output.write(buffer, 0, len);
                }
            }
			log.info("Url [" + url+ "] contained " + count + " entries which should be unpacked to [" + workingDirectory + "]");
		} finally {
            try {
				get.releaseConnection();
			} catch (RuntimeException e) {
				log.error(e);
			}
            try {
				if (output != null) output.close();
			} catch (RuntimeException e) {
				log.error(e);
			}
            try {
				stream.close();
			} catch (RuntimeException e) {
				log.error(e);
			}
        }		
	}



	public Set<String> getOccurrenceTypes() {
		return occurrenceTypes;
	}



	public void setOccurrenceTypes(Set<String> occurrenceTypes) {
		this.occurrenceTypes = occurrenceTypes;
	}


	public RawOccurrenceRecordDAO getRawOccurrenceRecordDAO() {
		return rawOccurrenceRecordDAO;
	}

	public void setRawOccurrenceRecordDAO(
			RawOccurrenceRecordDAO rawOccurrenceRecordDAO) {
		this.rawOccurrenceRecordDAO = rawOccurrenceRecordDAO;
	}

	public DataResourceDAO getDataResourceDAO() {
		return dataResourceDAO;
	}

	public void setDataResourceDAO(DataResourceDAO dataResourceDAO) {
		this.dataResourceDAO = dataResourceDAO;
	}

	public ResourceAccessPointDAO getResourceAccessPointDAO() {
		return resourceAccessPointDAO;
	}

	public void setResourceAccessPointDAO(
			ResourceAccessPointDAO resourceAccessPointDAO) {
		this.resourceAccessPointDAO = resourceAccessPointDAO;
	}

	public Map<String, String> getDwc2occurrenceMapping() {
		return dwc2occurrenceMapping;
	}

	public void setDwc2occurrenceMapping(Map<String, String> dwc2occurrenceMapping) {
		this.dwc2occurrenceMapping = dwc2occurrenceMapping;
	}
}
