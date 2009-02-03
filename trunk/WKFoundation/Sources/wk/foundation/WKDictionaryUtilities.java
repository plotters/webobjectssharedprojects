package wk.foundation;

import com.webobjects.foundation.NSDictionary;
import com.webobjects.foundation.NSMutableDictionary;

public class WKDictionaryUtilities {
    /**
     * @param dict
     * @param key
     * @param value
     * @return an immutable dictionary with the key and value params set. Whether the key already
     * existed and was just updated instead of added is not checked.
     */
    public static NSDictionary dictionaryAddingKeyAndValue(NSDictionary dict, Object key, Object value) {
        NSMutableDictionary newDict = dict.mutableClone();
        newDict.setObjectForKey(value, key);
        return newDict.immutableClone();
    }
}
