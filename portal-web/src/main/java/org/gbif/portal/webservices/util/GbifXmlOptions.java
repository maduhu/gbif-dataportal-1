/**
 * Extension to allow apache xmlbeans XML options class to be wired in Spring
 */
package org.gbif.portal.webservices.util;

import java.util.Map;

import org.apache.xmlbeans.XmlOptions;

/**
 * @author Donald Hobern
 *
 */
public class GbifXmlOptions extends XmlOptions {
	
	protected Map suggestedPrefixes;
	protected int prettyPrintIndent = 2;
	/**
	 * 
	 */
	private static final long serialVersionUID = 193255229328887089L;

	public GbifXmlOptions() {
		super();

		setSavePrettyPrint();
		setSavePrettyPrintIndent(prettyPrintIndent);
		setSaveAggressiveNamespaces();
	}

	/**
	 * @return the suggestedPrefixes
	 */
	public Map getSuggestedPrefixes() {
		return suggestedPrefixes;
	}

	/**
	 * @param suggestedPrefixes the suggestedPrefixes to set
	 */
	public void setSuggestedPrefixes(Map suggestedPrefixes) {
		this.suggestedPrefixes = suggestedPrefixes;
		
		setSaveSuggestedPrefixes(suggestedPrefixes);
	}

	/**
	 * @return the prettyPrintIndent
	 */
	public int getPrettyPrintIndent() {
		return prettyPrintIndent;
	}

	/**
	 * @param prettyPrintIndent the prettyPrintIndent to set
	 */
	public void setPrettyPrintIndent(int prettyPrintIndent) {
		this.prettyPrintIndent = prettyPrintIndent;

		setSavePrettyPrintIndent(prettyPrintIndent);
	}
}
