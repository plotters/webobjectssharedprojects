package wk.foundation;

import java.text.DateFormatSymbols;
import java.text.Format;
import java.util.Hashtable;

import com.webobjects.foundation.NSTimestamp;
import com.webobjects.foundation.NSTimestampFormatter;

/**
 * Provides caching of timestamp formatters.<br />
 * This code was derived from ERXTimestampFormatter
 * and created to remove dependencies so it could be included
 * in this foundation framework
 */

public class WKTimestampFormatter extends NSTimestampFormatter {

	/** holds a reference to the repository */
	private static Hashtable _repository = new Hashtable();
    
	protected static final String DefaultKey = "ERXTimestampFormatter.DefaultKey";
    
    /** The default pattern used in the UI */
    public static String DEFAULT_PATTERN = "%m/%d/%Y";
	
	static {
		_repository.put(DefaultKey, new WKTimestampFormatter());
	}
	

	/**
     * The default pattern used by WOString and friends when no pattern is set. 
     * Looks like this only for compatibility's sake.
	 * @param object
	 * @return
	 */
	public static Format defaultDateFormatterForObject(Object object) {
		Format result = null;
		if(object != null && object instanceof NSTimestamp) {
			result = dateFormatterForPattern("%Y/%m/%d");
		}
		return result;
	}

	/**
	 * Returns a shared instance for the specified pattern.
	 * @return shared instance of formatter
	 */
	public static NSTimestampFormatter dateFormatterForPattern(String pattern) {
		NSTimestampFormatter formatter;
		synchronized(_repository) {
			formatter = (NSTimestampFormatter)_repository.get(pattern);
			if(formatter == null) {
				formatter = new NSTimestampFormatter(pattern);
				_repository.put(pattern, formatter);
			}
		}

		return formatter;
	}
	
	/**
	 * Sets a shared instance for the specified pattern.
	 * @return shared instance of formatter
	 */
	public static void setDateFormatterForPattern(NSTimestampFormatter formatter, String pattern) {
		synchronized(_repository) {
			if(formatter == null) {
				_repository.remove(pattern);
			} else {
				_repository.put(pattern, formatter);
			}
		}

	}
	
	/**
	 * 
	 */
	public WKTimestampFormatter() {
		super();
	}

	/**
	 * @param arg0
	 */
	public WKTimestampFormatter(String arg0) {
		super(arg0);
	}

	/**
	 * @param arg0
	 * @param arg1
	 */
	public WKTimestampFormatter(String arg0, DateFormatSymbols arg1) {
		super(arg0, arg1);
	}

}
