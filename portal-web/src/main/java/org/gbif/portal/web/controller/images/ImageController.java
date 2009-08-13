package org.gbif.portal.web.controller.images;

import java.io.File;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.gbif.portal.dto.occurrence.ImageRecordDTO;
import org.gbif.portal.service.DataResourceManager;
import org.gbif.portal.util.image.ImageUtils;
import org.gbif.portal.web.controller.RestController;
import org.springframework.web.servlet.ModelAndView;

/**
 * ImageController serving images.
 * 
 * TODO Scaling is currently not working properly (on Mac OSX)- just returning the unscaled image.
 * 
 * @author dmartin
 */
public class ImageController extends RestController {
	
	protected DataResourceManager dataResourceManager;
	
	protected String idRequestKey = "id";
	protected String widthRequestKey = "width";
	protected String heightRequestKey = "height";
	
	/** Max file size to read - default of 3MB */
	protected int maxFileSizeInBytes = 3145728;
	
	protected String tempFilePath;
	
	protected String fileFormat = "JPEG";
	
	/**
	 * @see org.gbif.portal.web.controller.RestController#handleRequest(java.util.Map, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	public ModelAndView handleRequest(Map<String, String> propertiesMap, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		String imageRecordKey = propertiesMap.get(idRequestKey);
		
		Integer width = null;
		Integer height = null;
		
		try {
			width = NumberUtils.createInteger(propertiesMap.get(widthRequestKey));
			height = NumberUtils.createInteger(propertiesMap.get(heightRequestKey));
		} catch (NumberFormatException e){
			logger.debug(e);
			width = height = null;
		}
		
		ImageRecordDTO imageRecordDTO = dataResourceManager.getDataResourceImageFor(imageRecordKey);
		if(imageRecordDTO==null)
			return null;
		
		//TODO The actual url seems to be stored in the description field at present.
		String fileUrl = imageRecordDTO.getUrl();
		logger.debug(fileUrl);
		
		if(!ImageUtils.isImageLoadable(fileUrl))
			return null;	

		//TODO Is there a way to pass the stream to JAI instead of it reading it directly?
//		if(width!=null && height !=null){
//			String fileName = imageRecordKey+"-"+request.getSession().getId()+".jpg";
//			//use image scaling to write to temporary file
//			ImageUtils.scaleImage(fileUrl, width, height, fileName, "JPEG");
//		} else {
//			
//			
//		}
		
		byte[] imageAsBytes = null;
		
		//is scaling required
		if(width!=null && height!=null){
			
			if(StringUtils.isEmpty(tempFilePath))
				tempFilePath = System.getProperty("java.io.tmpdir");
			
			String fileName = tempFilePath+imageRecordKey+"-"+request.getSession().getId()+"."+fileFormat;
			//use image scaling to write to temporary file
			ImageUtils.scaleImageAndWriteToFile(fileUrl, width, height, fileName, fileFormat);
		
			File tempFile = new File(fileName);	
			imageAsBytes = FileUtils.readFileToByteArray(new File(fileName));
			response.getOutputStream().write(imageAsBytes);
			FileUtils.forceDelete(tempFile);
		} else {
			HttpMethod getMethod = null;
			try {		
				HttpClient httpClient  = new HttpClient();
				getMethod = new GetMethod(fileUrl);
				httpClient.executeMethod(getMethod);				
				imageAsBytes = getMethod.getResponseBody();
				response.getOutputStream().write(imageAsBytes);
			} finally {
				if (getMethod != null) {
					logger.debug("Releasing connection");
					getMethod.releaseConnection();
				}
			}
		}
		return null;
	}

	/**
	 * @param dataResourceManager the dataResourceManager to set
	 */
	public void setDataResourceManager(DataResourceManager dataResourceManager) {
		this.dataResourceManager = dataResourceManager;
	}

	/**
	 * @return the idRequestKey
	 */
	public String getIdRequestKey() {
		return idRequestKey;
	}

	/**
	 * @param idRequestKey the idRequestKey to set
	 */
	public void setIdRequestKey(String idRequestKey) {
		this.idRequestKey = idRequestKey;
	}

	/**
	 * @return the dataResourceManager
	 */
	public DataResourceManager getDataResourceManager() {
		return dataResourceManager;
	}

	/**
	 * @param heightRequestKey the heightRequestKey to set
	 */
	public void setHeightRequestKey(String heightRequestKey) {
		this.heightRequestKey = heightRequestKey;
	}

	/**
	 * @param maxFileSizeInBytes the maxFileSizeInBytes to set
	 */
	public void setMaxFileSizeInBytes(int maxFileSizeInBytes) {
		this.maxFileSizeInBytes = maxFileSizeInBytes;
	}

	/**
	 * @param widthRequestKey the widthRequestKey to set
	 */
	public void setWidthRequestKey(String widthRequestKey) {
		this.widthRequestKey = widthRequestKey;
	}

	/**
	 * @return the fileFormat
	 */
	public String getFileFormat() {
		return fileFormat;
	}

	/**
	 * @return the heightRequestKey
	 */
	public String getHeightRequestKey() {
		return heightRequestKey;
	}

	/**
	 * @return the maxFileSizeInBytes
	 */
	public int getMaxFileSizeInBytes() {
		return maxFileSizeInBytes;
	}

	/**
	 * @return the tempFilePath
	 */
	public String getTempFilePath() {
		return tempFilePath;
	}

	/**
	 * @return the widthRequestKey
	 */
	public String getWidthRequestKey() {
		return widthRequestKey;
	}
}