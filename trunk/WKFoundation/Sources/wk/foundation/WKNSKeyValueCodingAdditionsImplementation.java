package wk.foundation;

import com.webobjects.foundation.NSKeyValueCoding;
import com.webobjects.foundation.NSKeyValueCodingAdditions;

/**
 * A useful class that you can extend to implement the NSKeyValueCodingAdditions interface
 * or you can just copy and paste this class contents into your own class to implement the interface.
 * The purpose here is to save implementing the default methods every time I want this useful interface.
 * @author kieran
 *
 */
public class WKNSKeyValueCodingAdditionsImplementation implements NSKeyValueCodingAdditions {

    public void takeValueForKey(Object value, String key) {
        NSKeyValueCoding.DefaultImplementation.takeValueForKey(this, value, key);
    }

    public Object valueForKey(String key) {
        return NSKeyValueCoding.DefaultImplementation.valueForKey(this, key);
    }

    public void takeValueForKeyPath(Object value, String keyPath) {
        NSKeyValueCodingAdditions.DefaultImplementation.takeValueForKeyPath(this, value, keyPath);

    }

    public Object valueForKeyPath(String keyPath) {
        return NSKeyValueCodingAdditions.DefaultImplementation.valueForKeyPath(this, keyPath);
    }


}
