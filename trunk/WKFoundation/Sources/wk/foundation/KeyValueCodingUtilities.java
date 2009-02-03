package wk.foundation;

import org.apache.commons.lang.StringUtils;

public class KeyValueCodingUtilities {
    /**
     * @param keys - valid NSKeyValueCoding keys
     * @return a keypath of the form key1.key2.key3.key4
     */
    public static String keyPathFromKeysArray(String[] keys) {
        return StringUtils.join(keys, '.');
    }




}
