//
//  WKKeyValuePair.java
//  cheetah
//
//  Created by Kieran Kelleher on 3/14/06.
//  Copyright 2006 Kieran Kelleher. All rights reserved.
//

package wk.foundation;

import com.webobjects.foundation.NSKeyValueCoding;

/** A simple class to hold key/value pairs. Used as a foundation
    for editing dictionaries. */
public class WKKeyValuePair implements NSKeyValueCoding {
    protected Object _key;
    protected Object _value;
    
    public WKKeyValuePair() {
        
    }
    
    public WKKeyValuePair( Object aKey, Object aValue ) {
        setKey( aKey );
        setValue( aValue );
    }
    
    public Object key() {
        return _key;
    }
    
    public void setKey( Object key ) {
        _key = key;
    }
    
    public Object value() {
        return _value;
    }
    
    public void setValue( Object aValue ) {
        _value = aValue;
    }
    
    public Object valueForKey(String key) {
        return NSKeyValueCoding.DefaultImplementation.valueForKey(this, key);
    }
    
    public void takeValueForKey( Object value, String key ) {
        NSKeyValueCoding.DefaultImplementation.takeValueForKey( this, value, key );
    }
    
    public String toString() {
        return "key = " + ( key() == null ? "null" : key().toString() )
        + "; value = " + ( value() == null ? "null" : value().toString() );
        
    }
    
    
    
}
