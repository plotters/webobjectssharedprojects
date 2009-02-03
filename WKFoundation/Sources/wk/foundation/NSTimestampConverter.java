package wk.foundation;

import java.text.ParseException;
import java.util.regex.Pattern;

import com.webobjects.foundation.NSTimestamp;
import com.webobjects.foundation.NSTimestampFormatter;

/**
 * @author kieran
 * This class tries to convert an object into a NSTimestamp
 */
public class NSTimestampConverter implements ValueConverter {

    private static final String mmddyyyyRegex = "^\\d\\d/\\d\\d/\\d\\d\\d\\d$";

    /* (non-Javadoc)
     * @see wk.foundation.ValueConverter#valueOf(java.lang.Object)
     * @return an NSTimestamp object
     */
    public Object valueOf(Object aValue) throws ValueConverterException {
        NSTimestamp result = null;
        
        if (aValue == null) {
            result = null;
        } else if (aValue instanceof String) {
            if (mmddyyyyPattern().matcher((String)aValue).matches()) {
                try {
                    result = (NSTimestamp) mmddyyyyFormatter().parseObject((String)aValue);
                } catch (ParseException exception) {
                    throwGenericException(aValue, exception);
                }
            } //~ if (aValue.)
        } else {
            throwGenericException(aValue, null);
        } //~ if (aValue instanceof String)
        return result;
    }

    private void throwGenericException(Object aValue, Throwable e) throws ValueConverterException{
        throw new ValueConverterException("Could not convert the value " + aValue + " to an NSTimestamp", e);
    }

    Pattern _mmddyyyyPattern = null;

    private Pattern mmddyyyyPattern(){
        if (_mmddyyyyPattern == null) {
            _mmddyyyyPattern = Pattern.compile(mmddyyyyRegex);
        } //~ if (_mmddyyyyPattern == null)
        return _mmddyyyyPattern;
    }

    private NSTimestampFormatter _mmddyyyyFormatter;
    private NSTimestampFormatter mmddyyyyFormatter(){
        if (_mmddyyyyFormatter == null) {
            _mmddyyyyFormatter = new NSTimestampFormatter("%m/%d/%Y");
        }

        return _mmddyyyyFormatter;
    }

}
