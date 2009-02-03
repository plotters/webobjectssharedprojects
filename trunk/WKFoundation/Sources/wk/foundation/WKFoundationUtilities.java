package wk.foundation;
//
//  WKFoundationUtilities.java
//  cheetah
//
//  Created by Kieran Kelleher on 2/15/07.
//  Copyright (c) 2007 Kieran Kelleher. All rights reserved.
//

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSKeyValueCodingAdditions;
import com.webobjects.foundation.NSMutableArray;

/** Class of static foundation utility methods */
public class WKFoundationUtilities {
	private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger( WKFoundationUtilities.class );
	
    private WKFoundationUtilities() {
        
    }

    /** Checks if the object is a valid String
        @param aValue an object
        @return true if non-null String at least 1 character in length */
    public static boolean validateRequiredString(Object aValue) {
		// Check that it is not null
		if (aValue == null) {
			return false;
		} else if (aValue instanceof String) {
			// Check that it is a String with length greater than zero
			if (((String)aValue).length() < 1) {
				return false;
			}
		} else {
			return false;
		}
		
		return true;
    }
    
    /** Increments an alpha type string code by one
        @param alphaCode the string
        @return String incremented by one
Requires: string consists of capital letters from A thru Z only */
    public static String incrementAlphaCode(String alphaCode) {
		
		/** Requires:
		alphaCode is non-null and not empty, letters A thru Z only*/
		
		// Manipulate using a mutable string
		StringBuffer newAlphaCode = new StringBuffer( alphaCode );
		boolean carryOne = false;
		
		int length = newAlphaCode.length();
		int index = length - 1;
		
		do {
			char letter = newAlphaCode.charAt(index);
			
			if (letter == 'Z') {
				letter = 'A';
				carryOne = true;
			} else {
				letter = (char)(letter + 1);
				carryOne = false;
			}
			
			newAlphaCode.setCharAt(index, letter);
			
			index = index - 1;
			
		} while ( (index >= 0) && (carryOne == true) );
		
		// Case of adding 1 to something like "ZZZZ" to get "AAAAA", like
		// adding 1 to 9999 gives 10000
		if (carryOne == true) {
			newAlphaCode = newAlphaCode.insert(0,'A');
		}
		
		return newAlphaCode.toString();
    }
    
	/** @return an array of display names from an array of key values. */
	public static NSArray displayNamesFromKeys( NSArray keys ) {
		NSMutableArray displayNames = new NSMutableArray();
		
		for ( java.util.Enumeration e = keys.objectEnumerator() ; e.hasMoreElements() ; ) {
			String key = (String)e.nextElement();
			displayNames.addObject( WKStringUtilities.displayNameForKey( key ) );
		}
		
		return displayNames.mutableClone();
	}
    
    /**
	    * Calculates a default display name for a given
	 * key path. For instance for the key path:
	 * "foo.bar" the display name would be "Bar".
	    Courtesy of Project Wonder ERXStringUtilities
	 * @param key to calculate the display name
	 * @return display name for the given key
	 */
	public static String displayNameForKey(String key) {
		return WKStringUtilities.displayNameForKey(key);
	}
    
    /**
        * Simple test if the string is either null or
     * equal to "".
     * @param s string to test
     * @return result of the above test
     */
    public static boolean stringIsNullOrEmpty(String s) {
        return ((s == null) || (s.length() == 0));
    }
    
    
    /** @return 1, 0, -1 meaning greater than, equal to or less than result for comparison of two numbers.
        The numbers must be of the same class, otherwise a an exception will occur */
    public static int compareNumbers(Number numberToCheck, Number compareToNumber) {
        // Arbitrary initial value other than 1, 0, or -1 for verifying if a supported Number class was passed in.
        final int INITIAL_VALUE = 9;
        
        if ( log.isDebugEnabled() ) log.debug("numberToCheck class = " + numberToCheck.getClass().getName() + "; value = " + numberToCheck );
        if ( log.isDebugEnabled() ) log.debug("compareToNumber class = " + compareToNumber.getClass().getName() + "; value = " + compareToNumber );
        
        
        // Set to an initial value that we check later
        int comparisonResult = INITIAL_VALUE;
        
        // BigDecimal
        if ( numberToCheck instanceof BigDecimal ) {
            if ( compareToNumber instanceof BigDecimal ) {
                comparisonResult = ((BigDecimal)numberToCheck).compareTo( (BigDecimal)compareToNumber );
            } else {
                comparisonResult = ((BigDecimal)numberToCheck).compareTo( convertNumberToBigDecimal( compareToNumber ) );
            }
        }
        
        // BigInteger
        if ( numberToCheck instanceof BigInteger ) {
            if ( compareToNumber instanceof BigInteger ) {
                comparisonResult = ((BigInteger)numberToCheck).compareTo( (BigInteger)compareToNumber );
            } else {
                comparisonResult = ((BigInteger)numberToCheck).compareTo( convertNumberToBigInteger( compareToNumber ) );
            }
        }
        
        // BigInteger
        if ( numberToCheck instanceof Integer ) {
            if ( compareToNumber instanceof Integer ) {
                comparisonResult = ((Integer)numberToCheck).compareTo( (Integer)compareToNumber );
            } else {
                comparisonResult = ((Integer)numberToCheck).compareTo( convertNumberToInteger( compareToNumber ) );
            }
        }
        
        // Double
        if ( numberToCheck instanceof Double ) {
            if ( compareToNumber instanceof Double ) {
                comparisonResult = ((Double)numberToCheck).compareTo( (Double)compareToNumber );
            } else {
                comparisonResult = ((Double)numberToCheck).compareTo( convertNumberToDouble( compareToNumber ) );
            }
        }
        
        // Long
        if ( numberToCheck instanceof Long ) {
            if ( compareToNumber instanceof Long ) {
                comparisonResult = ((Long)numberToCheck).compareTo( (Long)compareToNumber );
            } else {
                comparisonResult = ((Long)numberToCheck).compareTo( convertNumberToLong( compareToNumber ) );
            }
        }
        
        // If it is still 9, the arbitrary initial value, then
        if ( comparisonResult == INITIAL_VALUE ) throw new IllegalArgumentException( "only BigDecimal, BigInteger, Integer, Long and Double are supported by this method." );
        
        return comparisonResult;
    }
    
    public static BigDecimal convertNumberToBigDecimal( Number aNumber ) {
        return new BigDecimal( aNumber.toString() );
    }
    
    public static BigInteger convertNumberToBigInteger( Number aNumber ) {
        return new BigInteger( aNumber.toString() );
    }
    
    public static Integer convertNumberToInteger( Number aNumber ) {
        return new Integer( aNumber.toString() );
    }
    
    public static Double convertNumberToDouble( Number aNumber ) {
        return new Double( aNumber.toString() );
    }
    
    public static Long convertNumberToLong( Number aNumber ) {
        return new Long( aNumber.toString() );
    }
    
    
    /** @return an NSArray of strings corresponding to the values as determined by the keypaths
        in the passed array argument. valueForKeyPath is called for each keypath. Null or erroneous
        keyPath calls are set with an empty string value.
        This works well since the keypath may be invalid for the object class. */
    public static NSArray toStringsArray( NSKeyValueCodingAdditions object, NSArray keyPaths ) {
        NSMutableArray stringsArray = new NSMutableArray();
        
        for ( java.util.Enumeration e = keyPaths.objectEnumerator(); e.hasMoreElements(); ) {
            String keyPath = (String)e.nextElement();
            if ( log.isDebugEnabled() ) log.debug("keyPath = " + keyPath);
            String value = null;
            try {
                value = object.valueForKeyPath( keyPath ).toString();
                if ( log.isDebugEnabled() ) log.debug("value = " + value);
            } catch (Exception exc) {
                if ( log.isDebugEnabled() ) log.debug("Error resolving '" + value
                                                      + "'", exc );
            }
            if ( value == null ) value = "";
            stringsArray.addObject( value );
        }
        
        return stringsArray;
    }
    
    /** Creates a temporary empty file and a Writer stream ready for writing */
    public static File createTempFile() {
        File tempFile = null;
        
        String tempFileName = UniqueStringGenerator.sharedInstance().nextUnique();
        if ( log.isDebugEnabled() ) log.debug("tempFileName = " + tempFileName);
        try {
            tempFile = File.createTempFile( tempFileName, ".tmp" );
            
            // Sets the file to automatic delete upon exit of VM
            tempFile.deleteOnExit();
        } catch (IOException ioExc) {
            log.error("Failed to create temp file named " + tempFileName, ioExc);
        }
        return tempFile;
    }
    
    /** @return a formatted phone number when passed a 10-digit string. */
    public static String format10DigitPhone(String phone) {
		if (phone == null) {
			return phone;
		}
		
		if ( phone.matches("^\\d{10}$") ) {
			return "(" 
			+ phone.substring(0,3)
			+ ") "
			+ phone.substring(3,6)
			+ "-"
			+ phone.substring(6,10);
		} else {
			return phone;
		}
    }
        
    
}
