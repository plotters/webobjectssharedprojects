//
//  WKStringUtilities.java
//  cheetah
//
//  Created by Kieran Kelleher on 1/19/06.
//  Copyright 2006 Kieran Kelleher. All rights reserved.
//

package wk.foundation;

import org.apache.commons.lang.StringUtils;

import com.Ostermiller.util.CSVParser;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSMutableArray;

/** Common string utilities from various sources. Objective is to consolidate
    dependencies on other frameworks to this class. */
public class WKStringUtilities {
	public static final String CSV_PATTERN = "\"([^\"]+?)\",?|([^,]+),?|,";

    /** @return null-safe toString of the object. */
    public static String toString( Object object ) {
        return ( object == null ? "null" : object.toString() );
    }

    /** @return a String surrounded by delimiter*/
    public static String delimit( String aValue, String delimiter) {
        return delimiter + aValue + delimiter;
    }

    /** @return a String surrounded by delimiter*/
    public static String delimit( Object aValue, String delimiter) {
        return delimiter + toString( aValue ) + delimiter;
    }

    /** @return a string stripped of a leading and trailing character if it exists. */
    public static String stripDelimiter( String aValue, char delimiter ) {
        return StringUtils.strip( aValue, String.valueOf( delimiter ) );
    }

    /** @return an NSArray of strings stripped of delimiter */
    public static NSArray stripDelimiter( NSArray arrayOfStrings, char delimiter ) {
        String delimiterStr = String.valueOf( delimiter );
        NSMutableArray resultArray = new NSMutableArray();

        for ( java.util.Enumeration e = arrayOfStrings.objectEnumerator(); e.hasMoreElements(); ) {
            resultArray.addObject( StringUtils.strip( (String)e.nextElement(),delimiterStr ) );
        }

        return resultArray.immutableClone();
    }

    /** @return NSArray of strings when passed a line of CSV data */
    public static NSArray arrayFromCsvRecord( String csvRecord ) {
        String[][] fieldValuesParsed = CSVParser.parse( csvRecord );
        return new NSArray( fieldValuesParsed[0] );
    }

    public static String arrayToString(NSArray array){
        StringBuffer buff = new StringBuffer();

        for (java.util.Enumeration enumerator = array.objectEnumerator(); enumerator
                .hasMoreElements();) {
            Object element = enumerator.nextElement();
            buff.append(element.toString());
            buff.append('\n');
        }

        return buff.toString();
    }

    public static String join(String[] array, char separator) {
        return StringUtils.join(array, separator);
    }

    /**
     * Useful for converting an elementID into something that can be used as a dictionary key.
     * @param elementID, for example <code>1.0.2.2.3.1.9.10.12</code>
     * @return the elementID string with the periods replaced by a character that
     * will not interfere with use of the safeElementID in keypaths.
     */
    public static String safeElementID(String elementID) {
        return StringUtils.replaceChars(elementID, '.', '_');
    }

    /**
     * Useful for converting a keypath to something that can be used as a dictionary key.
     * @param keyPath
     * @return a keypath with periods replaced by underscores.
     */
    public static String keyPathToUnderScoreString(String keyPath) {
        return StringUtils.replaceChars(keyPath, '.', '_');
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
	    StringBuffer finalString = null;
	    if (!WKFoundationUtilities.stringIsNullOrEmpty(key) && !key.trim().equals("")) {
	        finalString = new StringBuffer();
	        String lastHop=key.indexOf(".") == -1 ? key : key.endsWith(".") ? "" : key.substring(key.lastIndexOf(".") + 1);
	        StringBuffer tempString = new StringBuffer();
	        char[] originalArray = lastHop.toCharArray();
	        originalArray[0] = Character.toUpperCase(originalArray[0]);
	        Character tempChar = null;
	        Character nextChar = null;
	        for(int i=0;i<(originalArray.length-1);i++){
	            tempChar = new Character(originalArray[i]);
	            nextChar = new Character(originalArray[i+1]);
	            if(Character.isUpperCase(originalArray[i]) &&
	               Character.isLowerCase(originalArray[i+1])) {
	                finalString.append(tempString.toString());
	                if (i>0) finalString.append(' ');
	                tempString = new StringBuffer();
	            }
	            tempString.append(tempChar.toString());
	        }
	        finalString.append(tempString.toString());
	        finalString.append(nextChar);
	    }
	    return finalString == null ? "" : finalString.toString();
	}

	public static boolean isCSVStringPattern(String csvLine) {
		return csvLine.matches(CSV_PATTERN);
	}

	/**
	 * Strips non-digits from a string
	 *
	 * @param s
	 *            a String whose digits are extracted.
	 * @return a String containing digits only
	 */
	public static String getDigitsOnly(String s) {
		StringBuffer digitsOnly = new StringBuffer();
		char c;
		for (int i = 0; i < s.length(); i++) {
			c = s.charAt(i);
			if (Character.isDigit(c)) {
				digitsOnly.append(c);
			}
		}
		return digitsOnly.toString();
	}



}
