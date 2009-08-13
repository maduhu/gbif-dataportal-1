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
package org.gbif.portal.web.download;

/**
 * @author dave
 */
public class FileType {

	protected String fileNamePrefix;
	protected String directoryPath;
	protected int expiryTimeInSecs;
	protected boolean rootIsTempDir = true;

	/**
	 * @return the rootIsTempDir
	 */
	public boolean isRootIsTempDir() {
		return rootIsTempDir;
	}
	/**
	 * @param rootIsTempDir the rootIsTempDir to set
	 */
	public void setRootIsTempDir(boolean rootIsTempDir) {
		this.rootIsTempDir = rootIsTempDir;
	}
	/**
	 * @return the directoryPath
	 */
	public String getDirectoryPath() {
		return directoryPath;
	}
	/**
	 * @param directoryPath the directoryPath to set
	 */
	public void setDirectoryPath(String directoryPath) {
		this.directoryPath = directoryPath;
	}
	/**
	 * @return the expiryTimeInSecs
	 */
	public int getExpiryTimeInSecs() {
		return expiryTimeInSecs;
	}
	/**
	 * @param expiryTimeInSecs the expiryTimeInSecs to set
	 */
	public void setExpiryTimeInSecs(int expiryTimeInSecs) {
		this.expiryTimeInSecs = expiryTimeInSecs;
	}
	/**
	 * @return the fileNamePrefix
	 */
	public String getFileNamePrefix() {
		return fileNamePrefix;
	}
	/**
	 * @param fileNamePrefix the fileNamePrefix to set
	 */
	public void setFileNamePrefix(String fileNamePrefix) {
		this.fileNamePrefix = fileNamePrefix;
	}
}