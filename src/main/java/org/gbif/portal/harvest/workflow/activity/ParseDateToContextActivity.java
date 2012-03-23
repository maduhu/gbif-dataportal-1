/**
 * 
 */
package org.gbif.portal.harvest.workflow.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.gbif.portal.util.workflow.BaseActivity;
import org.gbif.portal.util.workflow.MapContext;
import org.gbif.portal.util.workflow.ProcessContext;


/**
 * Extracts year month and day concepts from a date string and puts them into the context
 * as singular items
 * 
 * @author donald hobern
 */
public class ParseDateToContextActivity extends BaseActivity {
	/**
	 * The key for the date to extract from
	 */
	protected String contextKeyDate = "date";

	/**
	 * The key for the year to insert into the context
	 */
	protected String contextKeyYear = "year";

	/**
	 * The key for the month to insert into the context
	 */
	protected String contextKeyMonth = "month";

	/**
	 * The key for the day to insert into the context
	 */
	protected String contextKeyDay = "day";

	/**
	 * @see org.gbif.portal.util.workflow.Activity#execute(org.gbif.portal.util.workflow.ProcessContext)
	 */
	@SuppressWarnings("unchecked")
	public ProcessContext execute(final ProcessContext context) throws Exception {
		String date = (String) context.get(getContextKeyDate(), String.class, false);
		
		if (date != null) {
			String year = null;
			String month = null;
			String day = null;

			// Including "-" as a separator also stops us having to worry about
			// negative values below...
			StringTokenizer st = new StringTokenizer(date, "-/. ");
			List<Integer> values = new ArrayList();

			while (st.hasMoreTokens()) {
				String token = st.nextToken();
				try {
					values.add(new Integer(token));
				} catch (NumberFormatException nfe) {
					// Not a number - could try parsing month names...
				}
			}

			if (values.size() == 3) {
				String pattern = "";
				
				for (Integer value : values) {
					if (value > 1000 && value < 10000) {
						// Assume this is a year...
						year = value.toString();
						pattern += "Y";
					} else if (value <= 12) {
						// Could be a day or a month
						pattern += "?";
					} else if (value <= 31) {
						// Assume this is a day
						day = value.toString();
						pattern += "D";
					} else {
						logger.error("Could not parse date " + date);
					}
				}
				
				if (year != null && day != null) {
					month = values.get(pattern.indexOf("?")).toString();
				}
				else if (pattern.equals("Y??")) {
					// Should be YMD
					month = values.get(1).toString();
					day = values.get(2).toString();
				}
				else if (pattern.equals("??Y")) {
					// Most likely DMY
					day = values.get(0).toString();
					month = values.get(1).toString();
				}
			}
			
			
			if (year != null)
				context.put(getContextKeyYear(), year);
			if (month != null)
				context.put(getContextKeyMonth(), month);
			if (day != null)
				context.put(getContextKeyDay(), day);

			logger.debug("Parsed date " + date + " to " + year + "-" + month + "-" + day);
		}

		return context;
	}

	@SuppressWarnings("unchecked")
	public static void main(String[] args) {
		ProcessContext context = new MapContext();
		context.put("year", "1980-03-07");
		ParseDateToContextActivity me = new ParseDateToContextActivity();
		try {
			me.execute(context);
			System.out.println(context.get("date") + " parsed to (YMD): " + context.get("year") + " / " + context.get("month") + " / " +context.get("day"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * @return the contextKeyDate
	 */
	public String getContextKeyDate() {
		return contextKeyDate;
	}

	/**
	 * @param contextKeyDate the contextKeyDate to set
	 */
	public void setContextKeyDate(String contextKeyDate) {
		this.contextKeyDate = contextKeyDate;
	}

	/**
	 * @return the contextKeyDay
	 */
	public String getContextKeyDay() {
		return contextKeyDay;
	}

	/**
	 * @param contextKeyDay the contextKeyDay to set
	 */
	public void setContextKeyDay(String contextKeyDay) {
		this.contextKeyDay = contextKeyDay;
	}

	/**
	 * @return the contextKeyMonth
	 */
	public String getContextKeyMonth() {
		return contextKeyMonth;
	}

	/**
	 * @param contextKeyMonth the contextKeyMonth to set
	 */
	public void setContextKeyMonth(String contextKeyMonth) {
		this.contextKeyMonth = contextKeyMonth;
	}

	/**
	 * @return the contextKeyYear
	 */
	public String getContextKeyYear() {
		return contextKeyYear;
	}

	/**
	 * @param contextKeyYear the contextKeyYear to set
	 */
	public void setContextKeyYear(String contextKeyYear) {
		this.contextKeyYear = contextKeyYear;
	}
}
