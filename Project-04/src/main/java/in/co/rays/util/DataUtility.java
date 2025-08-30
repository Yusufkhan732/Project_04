package in.co.rays.util;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Utility class for common data operations such as converting Strings to Date,
 * Timestamp, int, long, and formatting data. Provides helper methods for
 * application-wide data handling.
 * 
 * @author Yusuf Khan
 * @version 1.0
 */
public class DataUtility {

	/** Default date format used in the application. */
	public static final String APP_DATE_FORMAT = "dd-MM-yyyy";

	/** Default date-time format used in the application. */
	public static final String APP_TIME_FORMAT = "dd-MM-yyyy HH:mm:ss";

	/** Formatter for date operations. */
	private static final SimpleDateFormat formatter = new SimpleDateFormat(APP_DATE_FORMAT);

	/** Formatter for timestamp operations. */
	private static final SimpleDateFormat timeFormatter = new SimpleDateFormat(APP_TIME_FORMAT);

	/**
	 * Trims a string if it is not null.
	 *
	 * @param val the input string
	 * @return trimmed string or null if input is null
	 */
	public static String getString(String val) {
		if (DataValidator.isNotNull(val)) {
			return val.trim();
		} else {
			return val;
		}
	}

	/**
	 * Converts an object to string.
	 *
	 * @param val input object
	 * @return string value or empty string if object is null
	 */
	public static String getStringData(Object val) {
		if (val != null) {
			return val.toString();
		} else {
			return "";
		}
	}

	/**
	 * Converts a string to integer.
	 *
	 * @param val input string
	 * @return integer value or 0 if invalid
	 */
	public static int getInt(String val) {
		if (DataValidator.isInteger(val)) {
			return Integer.parseInt(val);
		} else {
			return 0;
		}
	}

	/**
	 * Converts a string to long.
	 *
	 * @param val input string
	 * @return long value or 0 if invalid
	 */
	public static long getLong(String val) {
		if (DataValidator.isLong(val)) {
			return Long.parseLong(val);
		} else {
			return 0;
		}
	}

	/**
	 * Converts a string to Date.
	 *
	 * @param val input date string
	 * @return Date object or null if parsing fails
	 */
	public static Date getDate(String val) {
		Date date = null;
		try {
			date = formatter.parse(val);
		} catch (Exception e) {
		}
		return date;
	}

	/**
	 * Converts a Date to string.
	 *
	 * @param date input Date object
	 * @return formatted date string or empty string if null
	 */
	public static String getDateString(Date date) {
		try {
			return formatter.format(date);
		} catch (Exception e) {
		}
		return "";
	}

	/**
	 * Placeholder method for adding/subtracting days to a date.
	 *
	 * @param date input date
	 * @param day  number of days
	 * @return modified date (currently null)
	 */
	public static Date getDate(Date date, int day) {
		return null;
	}

	/**
	 * Converts a string to Timestamp.
	 *
	 * @param val input timestamp string
	 * @return Timestamp object or null if parsing fails
	 */
	public static Timestamp getTimestamp(String val) {
		Timestamp timeStamp = null;
		try {
			timeStamp = new Timestamp((timeFormatter.parse(val)).getTime());
		} catch (Exception e) {
			return null;
		}
		return timeStamp;
	}

	/**
	 * Converts a long value to Timestamp.
	 *
	 * @param l time in milliseconds
	 * @return Timestamp object or null if error
	 */
	public static Timestamp getTimestamp(long l) {
		Timestamp timeStamp = null;
		try {
			timeStamp = new Timestamp(l);
		} catch (Exception e) {
			return null;
		}
		return timeStamp;
	}

	/**
	 * Returns the current system timestamp.
	 *
	 * @return current Timestamp
	 */
	public static Timestamp getCurrentTimestamp() {
		Timestamp timeStamp = null;
		try {
			timeStamp = new Timestamp(new Date().getTime());
		} catch (Exception e) {
		}
		return timeStamp;
	}

	/**
	 * Returns the long value of a Timestamp.
	 *
	 * @param tm input Timestamp
	 * @return time in milliseconds or 0 if error
	 */
	public static long getTimestamp(Timestamp tm) {
		try {
			return tm.getTime();
		} catch (Exception e) {
			return 0;
		}
	}

	/**
	 * Main method to test the utility methods.
	 *
	 * @param args command line arguments
	 */
	public static void main(String[] args) {
		// Test methods (code unchanged)
		System.out.println("getString Test:");
		System.out.println("Original: '  Hello World  ' -> Trimmed: '" + getString("  Hello World  ") + "'");
		System.out.println("Null input: " + getString(null));

		System.out.println("\ngetStringData Test:");
		System.out.println("Object to String: " + getStringData("1234"));
		System.out.println("Null Object: '" + getStringData(null) + "'");

		System.out.println("\ngetInt Test:");
		System.out.println("Valid Integer String: '124' -> " + getInt("124"));
		System.out.println("Invalid Integer String: 'abc' -> " + getInt("abc"));
		System.out.println("Null String: -> " + getInt(null));

		System.out.println("\ngetLong Test:");
		System.out.println("Valid Long String: '123456789' -> " + getLong("123456789"));
		System.out.println("Invalid Long String: 'abc' -> " + getLong("abc"));

		System.out.println("\ngetDate Test:");
		String dateStr = "10/15/2024";
		Date date = getDate(dateStr);
		System.out.println("String to Date: '" + dateStr + "' -> " + date);

		System.out.println("\ngetDateString Test:");
		System.out.println("Date to String: '" + getDateString(new Date()) + "'");

		System.out.println("\ngetTimestamp(String) Test:");
		String timestampStr = "10/15/2024 10:30:45";
		Timestamp timestamp = getTimestamp(timestampStr);
		System.out.println("String to Timestamp: '" + timestampStr + "' -> " + timestamp);

		System.out.println("\ngetTimestamp(long) Test:");
		long currentTimeMillis = System.currentTimeMillis();
		Timestamp ts = getTimestamp(currentTimeMillis);
		System.out.println("Current Time Millis to Timestamp: '" + currentTimeMillis + "' -> " + ts);

		System.out.println("\ngetCurrentTimestamp Test:");
		Timestamp currentTimestamp = getCurrentTimestamp();
		System.out.println("Current Timestamp: " + currentTimestamp);

		System.out.println("\ngetTimestamp(Timestamp) Test:");
		System.out.println("Timestamp to long: " + getTimestamp(currentTimestamp));
	}
}
