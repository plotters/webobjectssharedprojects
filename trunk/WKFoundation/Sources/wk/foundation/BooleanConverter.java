package wk.foundation;

import com.webobjects.foundation.NSArray;

public class BooleanConverter implements ValueConverter {
    private NSArray _trueStrings;
    private NSArray _falseStrings;

    public Object valueOf(Object aValue) throws ValueConverterException {
        Boolean result = Boolean.FALSE;

        if (aValue instanceof Boolean) {
            result = (Boolean)aValue;
        } else if (aValue == null) {
            result = Boolean.FALSE;
        } else if (aValue instanceof String) {
            // Empty string is translated to FALSE
            String strValue = aValue.toString().toUpperCase();
            if (trueStrings().containsObject(strValue)) {
                result = Boolean.TRUE;
            } else if (falseStrings().containsObject(strValue) || strValue.length() == 0) {
                result = Boolean.FALSE;
            } //~ if (trueStrings.containsObject(strValue))
        } else {
            throw new ValueConverterException("The value " + aValue + " cannot be converted to a Boolean.");
        } //~ if (aValue instanceof Boolean)

        return result;
    }

    private NSArray trueStrings(){
        if (_trueStrings == null) {
            _trueStrings = new NSArray(new String[]{"T","TRUE","Y","YES"});
        } //~ if (condition)
        return _trueStrings;
    }

    private NSArray falseStrings(){
        if (_falseStrings == null) {
            _falseStrings = new NSArray(new String[]{"F","FALSE","N","NO"});
        } //~ if (_falseStrings == null)
        return _falseStrings;
    }

}
