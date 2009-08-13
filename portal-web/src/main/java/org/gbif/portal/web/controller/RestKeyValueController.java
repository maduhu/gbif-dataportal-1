package org.gbif.portal.web.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * A Rest Style Controller than translates a url into key value pairs.
 * 
 * @author dmartin
 */
public class RestKeyValueController extends RestController {

	/** decode the supplied value using url decoding**/
	protected boolean decodeValues = true;
	/** the url encoding **/
	protected String encoding = "UTF-8";

	/**
	 * @see org.gbif.portal.web.controller.RestController#mapUrlProperties(java.util.List)
	 */
	@Override
	public Map<String, String> mapUrlProperties(List<String> urlProperties) {
		Map<String, String> keyValuesMap = new HashMap<String, String>();
		for (Iterator<String> iter = urlProperties.iterator(); iter.hasNext();) {
			String key = iter.next();
			String value = null;
			if(iter.hasNext()){
				value = (String) iter.next();
				if(decodeValues){
					try {
						value = URLDecoder.decode(value,encoding);
					} catch (UnsupportedEncodingException e) {
						logger.error(e.getMessage(), e);
					}
				}
				keyValuesMap.put(key, value);
				if(logger.isDebugEnabled())
					logger.debug("Found key: "+key+", value: "+value);
			}
		}
		return keyValuesMap;
	}

	/**
	 * @param decodeValues the decodeValues to set
	 */
	public void setDecodeValues(boolean decodeValues) {
		this.decodeValues = decodeValues;
	}

	/**
	 * @param encoding the encoding to set
	 */
	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}
}