package wk.foundation;

import com.webobjects.foundation.NSValidation;

public class WKNSKeyValueCodingAndValidationImplementation extends WKNSKeyValueCodingAdditionsImplementation implements
        NSValidation {

    public Object validateTakeValueForKeyPath(Object value, String key) throws ValidationException {
        return NSValidation.DefaultImplementation.validateTakeValueForKeyPath(this, value, key);
    }

    public Object validateValueForKey(Object value, String key) throws ValidationException {
        return NSValidation.DefaultImplementation.validateValueForKey(this, value, key);
    }

}
