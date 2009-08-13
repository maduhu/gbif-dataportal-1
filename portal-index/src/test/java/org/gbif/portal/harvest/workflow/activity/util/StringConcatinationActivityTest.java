package org.gbif.portal.harvest.workflow.activity.util;

import java.util.LinkedList;
import java.util.List;

import junit.framework.TestCase;

import org.gbif.portal.util.workflow.MapContext;
import org.gbif.portal.util.workflow.ProcessContext;

/**
 * Test for the String concatination
 * @author trobertson
 */
public class StringConcatinationActivityTest extends TestCase {
	/**
	 * The usual usage test
	 */
	@SuppressWarnings("unchecked")
	public void testOne() {
		StringConcatinationActivity sca = new StringConcatinationActivity();
		ProcessContext context = new MapContext();
		context.put("1", "i am one ");
		context.put("2", "I am two.");
		context.put("3", "Ithree ");
		context.put("4", "four.    ");
		context.put("5", "   five.   ");
		context.put("6", null);
		context.put("7", "");
		context.put("8", " ");
		List<String> keys = new LinkedList<String>();
		keys.add("1");
		keys.add("2");
		keys.add("3");
		keys.add("4");
		keys.add("5");
		keys.add("6");
		keys.add("7");
		keys.add("8");
		keys.add("9");
		sca.setContextKeyTokensToConcatinate(keys);
		sca.setContextKeyTarget("target");
		try {
			sca.execute(context);
			assertEquals("I am one. I am two. Ithree. Four. Five.", context.get("target"));			
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}	
}
