//
//  WKArrayUtilities.java
//  cheetah
//
//  Created by Kieran Kelleher on 3/13/05.
//  Copyright (c) 2005 Kieran Kelleher. All rights reserved.
//

package wk.foundation;

import org.apache.log4j.Logger;

import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSKeyValueCodingAdditions;
import com.webobjects.foundation.NSMutableArray;
import com.webobjects.foundation.NSMutableDictionary;

public class WKArrayUtilities {
    private static Logger log = Logger.getLogger( WKArrayUtilities.class );

    /** A simply utility method that removes duplicates from an array based on a key String value.
    The key is used to get a value from the array item and toString() method is used to create
    the value that is used for deduplicating.
    The array item is simply added to a mutable dictionary using this String value as a dictionary
    key. Consequently the last duplicate entry is chosen to be in the dictionary.
    Finally, the array of unique arrayitems is returned from the dictionary.
    @return NSArray of deduplicated items.
    @param theArray the array of items (must implement NSKeyValueCodingAdditions)
    @param key the keypath to the value to be used for deduplicating. */
    public static NSArray arrayWithoutDuplicateKeyStringValue(NSArray theArray, String key){
        NSMutableDictionary dico = new NSMutableDictionary();
        for(java.util.Enumeration e = theArray.objectEnumerator(); e.hasMoreElements(); ){
            NSKeyValueCodingAdditions arrayItem = (NSKeyValueCodingAdditions)e.nextElement();
            String keyValueString = arrayItem.valueForKeyPath(key).toString();
            if(keyValueString != null){
                dico.setObjectForKey(arrayItem, keyValueString);
            }
        }

        return dico.allValues();
    }

    /**
     * @param objects which implememt NSKeyValueCodingAdditions
     * @param keyPath
     * @return an array of unique values retrievable at keyPath
     */
    public static NSArray arrayOfUniqueKeyPathValues(NSArray objects, String keyPath) {
        NSArray values = (NSArray) objects.valueForKeyPath(keyPath);
        NSMutableArray uniqueValues = new NSMutableArray();
        for (java.util.Enumeration enumerator = values.objectEnumerator(); enumerator.hasMoreElements();) {
            Object element = (Object) enumerator.nextElement();
            if (!uniqueValues.containsObject(element)) {
                uniqueValues.addObject(element);
            } //~ if (!uniqueValues.containsObject(element))
        } //~ for (java.util.Enumeration enumerator = ...
        return uniqueValues.immutableClone();
    }

    /** A simple utility method that searches an array for a element keyPath value
        corresponding to a known value.
        @return the index of the first array item having <code>value</code> at the item's <code>keyPath</code>.
        @param array the array of items (elements must implement NSKeyValueCodingAdditions)
        @param value the value we are trying to find
        @param keyPath the keypath to the value in each element to be checked. */
    public static int objectIndexMatchingValueForKeyPath(NSArray array, Object value, String keyPath) {
        // Require not null array,value or keyPath

        if ( log.isDebugEnabled() ) log.debug("Checking for value: " + value + " at keyPath: " + keyPath + " in array: " + array );

        for ( int indexer = 0; indexer < array.count(); indexer++ ) {
            NSKeyValueCodingAdditions element = (NSKeyValueCodingAdditions)array.objectAtIndex( indexer );
            if ( log.isDebugEnabled() ) log.debug("Checking if element value element.valueForKeyPath( '" + keyPath + "' ): " +
                                                  element.valueForKeyPath( keyPath ) + " is " +
                                                  " equal to " + value );
            if ( element.valueForKeyPath( keyPath ).equals( value ) ) {
                if (log.isDebugEnabled()) log.debug("Found match at index: " + indexer);
                return indexer;
            }
        }

        // If we got this far, we did not find the value in the array, so return NotFound
        if (log.isDebugEnabled()) log.debug("We did not find a match");
        return NSArray.NotFound;
    }

    /** @return a array filtered by the class given.
        For example can filter an array of Animals to return any instances or subclasses of Dog */
    public static NSArray arrayFilteredByClass( NSArray array, Class filterClass ) {
        NSMutableArray resultsArray = new NSMutableArray();
        try {
            for ( java.util.Enumeration enumeration = array.objectEnumerator(); enumeration.hasMoreElements(); ) {
                Object item = enumeration.nextElement();
                if ( filterClass.isInstance( item ) ) {
                    resultsArray.addObject( item );
                }
            }
        } catch ( Exception exception ) {
            log.error( "Error filtering for instances of " + filterClass.getName() + " in array = " + array, exception );
            throw new RuntimeException(  "Error filtering for instances of " + filterClass.getName() + " in array = " + array, exception );
        }
        return resultsArray.immutableClone();
    }

    /** @return an array adding the object if not contained in the array already */
    public static NSArray arrayByAddingObjectWithoutDuplicatingObject( NSArray array, Object object ) {
        if ( !array.containsObject( object ) ) {
            array = array.arrayByAddingObject( object );
        }
        return array;
    }

    public static String toLinesOfStrings(NSArray array) {
        StringBuffer buff = new StringBuffer();
        for (java.util.Enumeration enumerator = array.objectEnumerator(); enumerator.hasMoreElements();) {
            Object element = (Object) enumerator.nextElement();
            buff.append(element.toString() + "\n");
        } //~ for (java.util.Enumeration enumerator = ...
        return buff.toString();
    }

}
