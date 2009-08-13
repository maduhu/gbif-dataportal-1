/***************************************************************************
 * Copyright (C) 2005 Global Biodiversity Information Facility Secretariat.
 * All Rights Reserved.
 *
 * The contents of this file are subject to the Mozilla Public
 * License Version 1.1 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of
 * the License at http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS
 * IS" basis, WITHOUT WARRANTY OF ANY KIND, either express or
 * implied. See the License for the specific language governing
 * rights and limitations under the License.
 ***************************************************************************/
package org.gbif.portal.web.controller.download;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.BooleanUtils;
import org.gbif.portal.web.download.DownloadUtils;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;
import org.springframework.web.servlet.mvc.multiaction.NoSuchRequestHandlingMethodException;
import org.springframework.web.servlet.view.RedirectView;

/**
 * This class simply looks for the existence of the supplied file in the temp directory
 * and returns it if it exists, otherwise it returns user to its original page.
 * 
 * @author dmartin
 */
public class DownloadController extends MultiActionController {

	/** The download directory to check */
	protected String downloadDirectory;
	/** The request parameter containing the file name */
	protected String downloadFileRequestKey = "downloadFile";
	/** The content type to set on responses */
	protected String contentType = "application/octet-stream";
	
	protected String tmpDirSystemProperty = "java.io.tmpdir";
	
	protected String prepareView = "downloadPreparing";
	protected String readyView = "downloadReady";
	protected String failedView = "downloadFailed";
	protected String expiredView = "downloadExpired";
	protected String monitorView = "downloadProgress";
	protected String ajaxView = "downloadReadyCheck";
	
	protected boolean setContentDisposition = false;
	
	protected String[] tmpFileExtensions = new String[]{"tmp"};
	
	protected String[] completedFileExtensions = new String[]{"zip", "png"};
	
	/**
	 * Directs view to a preparing page.
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ModelAndView preparingDownload(HttpServletRequest request, HttpServletResponse response) throws Exception {
		//retrieve file name
		String fileName = request.getParameter(downloadFileRequestKey);
		String viewName = ServletRequestUtils.getStringParameter(request, "view", prepareView);
		ModelAndView mav = new ModelAndView(viewName);
		mav.addObject("fileName", fileName);
		addFilePropertiesToRequest(mav, fileName, null);
		return mav;
	}

	/**
	 * Directs view to a download ready page which contains a link.
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ModelAndView downloadReady(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		//retrieve file name
		String fileName = request.getParameter(downloadFileRequestKey);
		String filePath = getFilePath(request);
		String failedFilePath = filePath+DownloadUtils.failedExtensionIndicator;
		
		File file = new File(filePath);
		File failedFile = new File(failedFilePath);
		ModelAndView mav;
		
		if(file.exists()){
			String redirectUrl = getCompletionRedirectUrl(fileName, file);
			if(redirectUrl!=null){
				return new ModelAndView(new RedirectView(request.getContextPath()+redirectUrl));
			}
			mav = new ModelAndView(readyView);
			addFilePropertiesToRequest(mav, fileName, file);
			mav.addObject("fileName", fileName);
			mav.addObject("fileSize", file.length());
		} else if (failedFile.exists()){
			mav = new ModelAndView(failedView);
			addFilePropertiesToRequest(mav, fileName, file);
			mav.addObject("fileName", fileName);
			mav.addObject("fileSize", file.length());			
		} else {
			return downloadExpired(request, response);
		}
		return mav;
	}
	
	/**
	 * Create the redirect url for this file.
	 * 
	 * @param fileName
	 * @param file
	 * @return
	 * @throws Exception
	 */
	public String getCompletionRedirectUrl(String fileName, File file) throws Exception {
		String fdn = FilenameUtils.removeExtension(fileName)+DownloadUtils.downloadDescriptorExtension;
		File fd = new File(constructFilePath(fdn));
		if(fd.exists()){
			FileInputStream fInput = new FileInputStream(fd);
			Properties properties = new Properties();
			properties.load(fInput);
			return (String) properties.get(DownloadUtils.completionRedirectUrlKey);
		}
		return null;	
	}
	
	/**
	 * Add file properties to Model
	 * 
	 * @param mav
	 * @param fileName
	 * @param file nullable
	 */
	public void addFilePropertiesToRequest(ModelAndView mav, String fileName, File file) throws Exception {
		Map<String, Object> fileProperties = getFileProperties(fileName, file);
		mav.addAllObjects(fileProperties);
	}

	/**
	 * Retrieve file properties from the download descriptor.
	 * 
	 * @param fileName
	 * @param file
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> getFileProperties (String fileName, File file) throws Exception {
		String fdn = FilenameUtils.removeExtension(fileName) + DownloadUtils.downloadDescriptorExtension;
		File fd = new File(constructFilePath(fdn));
		return getFileProperties(file, fd);
	}

	/**
	 * Retrieve a set of file properties from the file descriptor and the file itself.
	 * 
	 * @param file
	 * @param fileDescriptor
	 * @return
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws ParseException
	 */
	private Map<String, Object> getFileProperties(File file, File fileDescriptor) throws FileNotFoundException, IOException,
      ParseException {
	  Map<String, Object> loadedProperties = new HashMap<String, Object>();
		if(fileDescriptor.exists()){
			FileInputStream fInput = new FileInputStream(fileDescriptor);
			Properties properties = new Properties();
			properties.load(fInput);
			String createdDate = properties.getProperty("createdDate");
			Date created = null;
			if(createdDate!=null){
				SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyy HHmm.ss");
				created = sdf.parse(createdDate);
				loadedProperties.put("createdDate", created);
			}
			
			boolean displayTimeTaken = BooleanUtils.toBoolean(properties.getProperty("displayTimeTaken"));
			if(created!=null && displayTimeTaken){
				if(file!=null){
					Date lastModified = new Date(file.lastModified());
						long timeTakenInMillis = lastModified.getTime() - created.getTime();
						loadedProperties.put("timeTakenInMillis", timeTakenInMillis);
				}
			}
			
			String fileType = properties.getProperty("fileType");
			String fileDescription = properties.getProperty("fileDescription");
			String originalUrl = properties.getProperty("originalUrl");
			if(fileType!=null)
				loadedProperties.put("fileType", fileType);
			if(fileDescription!=null)
				loadedProperties.put("fileDescription", fileDescription);
			if(originalUrl!=null)
				loadedProperties.put("originalUrl", originalUrl);
		}		
		return loadedProperties;
  }

	/**
	 * Directs view to "Your download link has expired".
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ModelAndView downloadExpired(HttpServletRequest request, HttpServletResponse response) throws Exception {
		//retrieve file name
		String fileName = request.getParameter(downloadFileRequestKey);		
		ModelAndView mav = new ModelAndView(expiredView);
		mav.addObject("fileName", fileName);
		return mav;
	}	
	
	/**
	 * AJAX call from the preparation page to see if download is available.
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ModelAndView isDownloadReady(HttpServletRequest request, HttpServletResponse response) throws Exception {
		//retrieve file name
		String fileName = request.getParameter(downloadFileRequestKey);
		String filePath = getFilePath(request);
		String failedFilePath = filePath+DownloadUtils.failedExtensionIndicator;
		
		File file = new File(filePath);
		File failedFile = new File(failedFilePath);		
		
		if(file.exists() || failedFile.exists()){		
			ModelAndView mav = new ModelAndView(ajaxView);
			mav.addObject("fileName", fileName);	
			return mav;
		}	
		return null;
	}	
	
	/**
	 * Get the file path for the request file.
	 * 
	 * @param request
	 * @return
	 */
	public String getFilePath(HttpServletRequest request){
		//retrieve file name
		String fileName = request.getParameter(downloadFileRequestKey);		
		return constructFilePath(fileName);		
	}

	/**
	 * Construct file path
	 * @param fileName
	 * @return
	 */
	private String constructFilePath(String fileName) {
		//if downloadDirectory is not set use the default system temp dir
		return getDownloadDirectory()+File.separator+fileName;
	}
	
	/**
	 * Retrieve the file path for the download directory.
	 * The default is the java.io.tmpdir
	 * @return
	 */
	private String getDownloadDirectory(){
		//if downloadDirectory is not set use the default system temp dir
		if(downloadDirectory==null){
			downloadDirectory = System.getProperty(tmpDirSystemProperty);
		}		
		return downloadDirectory;
	}
	
	/**
	 * @see org.springframework.web.servlet.mvc.multiaction.MultiActionController#handleNoSuchRequestHandlingMethod(org.springframework.web.servlet.mvc.multiaction.NoSuchRequestHandlingMethodException, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected ModelAndView handleNoSuchRequestHandlingMethod(NoSuchRequestHandlingMethodException exception, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String fileName = getDownloadDirectory() + request.getPathInfo();
		File file = new File(fileName);
		return downloadIfFileExists(request, response, file);
	}

	/**
	 * @see org.springframework.web.servlet.mvc.Controller#handleRequest(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public ModelAndView download(HttpServletRequest request, HttpServletResponse response) throws Exception {
		File file = new File(getFilePath(request));
		return downloadIfFileExists(request, response, file);
	}

	/**
	 * View downloads in progress and recently completed downloads.
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ModelAndView monitor(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		ModelAndView mav = new ModelAndView(monitorView);
		
		String downloadDir = getDownloadDirectory();
		File directory = new File(downloadDir);
		
		List<FileDownloadStatus> inProgress = new ArrayList<FileDownloadStatus>();
		Iterator<File> iterator = FileUtils.iterateFiles(directory, tmpFileExtensions, false);
		while(iterator.hasNext()){
			FileDownloadStatus fds = new FileDownloadStatus();
			File downloadFile = iterator.next();
			String fileName = downloadFile.getName();
			String nameWithoutExt = fileName.substring(0, fileName.indexOf("."));
			String fileDescriptor = nameWithoutExt + DownloadUtils.downloadDescriptorExtension;
			File fd = new File(constructFilePath(fileDescriptor));
			if(fd.exists()){
				Map<String, Object> fileDownload = getFileProperties(downloadFile, fd);
				fds.setProperties(fileDownload);
				fds.setCompleted(false);
				fds.setFileName(DownloadUtils.getFileNameWithoutTempMarker(fileName));
				inProgress.add(fds);
			}
		}
		
		List<FileDownloadStatus> completed = new ArrayList<FileDownloadStatus>();
		iterator = FileUtils.iterateFiles(directory, completedFileExtensions, false);
		while(iterator.hasNext()){
			FileDownloadStatus fds = new FileDownloadStatus();
			File downloadFile = iterator.next();
			String fileName = downloadFile.getName();
			String nameWithoutExt = fileName.substring(0, fileName.indexOf("."));
			String fileDescriptor = nameWithoutExt + DownloadUtils.downloadDescriptorExtension;
			File fd = new File(constructFilePath(fileDescriptor));
			if(fd.exists()){
				Map<String, Object> fileDownload = getFileProperties(downloadFile, fd);
				fds.setProperties(fileDownload);
				fds.setCompleted(true);
				fds.setFileName(fileName);
				completed.add(fds);
			}
		}		
		
		mav.addObject("inProgress", inProgress);
		mav.addObject("completed", completed);
		return mav;
	}
	
	/**
	 * Download if file exists, otherwise redirect to link expired view.
	 * 
	 * @param response
	 * @param file
	 * @return
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	private ModelAndView downloadIfFileExists(HttpServletRequest request, HttpServletResponse response, File file) throws FileNotFoundException, IOException {
		
		try{
			if(file.exists()){
				FileInputStream fInput = new FileInputStream(file);
				String contentTypeToUse = ServletRequestUtils.getStringParameter(request, "contentType", contentType);
				response.setContentType(contentTypeToUse);
				if(setContentDisposition){
					response.setHeader("Content-Disposition", "attachment; "+file.getName());
				}
				ServletOutputStream sOut = response.getOutputStream();
				response.setContentLength((int)file.length());
				byte[] buf = new byte[1024];
				int len;
				while ((len = fInput.read(buf)) > 0) {
					sOut.write(buf, 0, len);
		        }		
				sOut.flush();
			} else {
				return new ModelAndView(expiredView);
			}
		} catch (FileNotFoundException e){
			logger.error(e.getMessage(), e);
			return new ModelAndView(expiredView);
		}
		return null;
	}

	/**
	 * FileDownloadStatus
	 *  
	 * @author davejmartin
	 */
	public class FileDownloadStatus {
		
		protected Map<String, Object> properties;
		protected boolean completed = false;
		protected String fileName = null;
		
		public String getFileName() {
    	return fileName;
    }

		public void setFileName(String fileName) {
    	this.fileName = fileName;
    }

		public boolean isCompleted() {
    	return completed;
    }

		public void setCompleted(boolean completed) {
    	this.completed = completed;
    }

		public Map<String, Object> getProperties() {
    	return properties;
    }

		public void setProperties(Map<String, Object> properties) {
    	this.properties = properties;
    }
	}
	
	/**
	 * @param downloadDirectory the downloadDirectory to set
	 */
	public void setDownloadDirectory(String downloadDirectory) {
		this.downloadDirectory = downloadDirectory;
	}

	/**
	 * @param downloadFileRequestKey the downloadFileRequestKey to set
	 */
	public void setDownloadFileRequestKey(String downloadFileRequestKey) {
		this.downloadFileRequestKey = downloadFileRequestKey;
	}

	/**
	 * @param contentType the contentType to set
	 */
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	/**
	 * @param tmpDirSystemProperty the tmpDirSystemProperty to set
	 */
	public void setTmpDirSystemProperty(String tmpDirSystemProperty) {
		this.tmpDirSystemProperty = tmpDirSystemProperty;
	}

	/**
	 * @param prepareView the prepareView to set
	 */
	public void setPrepareView(String prepareView) {
		this.prepareView = prepareView;
	}

	/**
	 * @param readyView the readyView to set
	 */
	public void setReadyView(String readyView) {
		this.readyView = readyView;
	}

	/**
	 * @param failedView the failedView to set
	 */
	public void setFailedView(String failedView) {
		this.failedView = failedView;
	}

	/**
	 * @param expiredView the expiredView to set
	 */
	public void setExpiredView(String expiredView) {
		this.expiredView = expiredView;
	}

	/**
	 * @param ajaxView the ajaxView to set
	 */
	public void setAjaxView(String ajaxView) {
		this.ajaxView = ajaxView;
	}

	/**
   * @param monitorView the monitorView to set
   */
  public void setMonitorView(String monitorView) {
  	this.monitorView = monitorView;
  }

	/**
   * @param setContentDisposition the setContentDisposition to set
   */
  public void setSetContentDisposition(boolean setContentDisposition) {
  	this.setContentDisposition = setContentDisposition;
  }

	/**
   * @param tmpFileExtensions the tmpFileExtensions to set
   */
  public void setTmpFileExtensions(String[] tmpFileExtensions) {
  	this.tmpFileExtensions = tmpFileExtensions;
  }

	/**
   * @param completedFileExtensions the completedFileExtensions to set
   */
  public void setCompletedFileExtensions(String[] completedFileExtensions) {
  	this.completedFileExtensions = completedFileExtensions;
  }
}