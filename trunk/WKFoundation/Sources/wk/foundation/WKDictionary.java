//
//  WKDictionary.java
//  cheetah
//
//  Created by Kieran Kelleher on 6/2/05.
//  Copyright (c) 2005 Kieran Kelleher. All rights reserved.
//

package wk.foundation;

import org.apache.log4j.Logger;

import com.webobjects.foundation.NSDictionary;
import com.webobjects.foundation.NSMutableDictionary;
import com.webobjects.foundation.NSPropertyListSerialization;

/** Provides a static method to convert a string into a NSDictionary assuming
the dictionary as string was originally created from a NSDictionary of string values
using the toString method.
This class is intended to be used as an EOF attribute. */
public class WKDictionary extends NSDictionary {
    private static Logger log = Logger.getLogger( WKDictionary.class );

    public WKDictionary( NSDictionary dictionary ) {
        super( dictionary );
    }

    static public WKDictionary fromString( String dictionaryAsString ) {
        if ( log.isDebugEnabled() ) log.debug("argument = " + dictionaryAsString );

        Object object = NSPropertyListSerialization.propertyListFromString( dictionaryAsString );

        if ( log.isDebugEnabled() ) log.debug("class type = " + object.getClass().getName() );

        if ( log.isDebugEnabled() ) log.debug("object = " + object.toString() );

        return new WKDictionary( (NSDictionary)object );
    }

    /** Modifies the immutable dictionary using a temporary mutable copy
        and returns the modified dictionary. */
    public WKDictionary copyWithSetObjectForKey( Object object, Object key ) {
        NSMutableDictionary newDict = this.mutableClone();
        if ( object == null ) {
            newDict.removeObjectForKey( key );
        } else {
            newDict.setObjectForKey( object, key );
        }

        return new WKDictionary( newDict.immutableClone() );
    }

    /** Required to maintain immutability since this is a custom attribute
        and to allow keyvalue coding value pushing in WO. */
    public WKDictionary copyWithTakeValueForKey( Object value,
                                         String key ) {
        return copyWithSetObjectForKey( value, key );
    }
}
