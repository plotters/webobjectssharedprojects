//
//  WKValueUtilities.java
//  cheetah
//
//  Created by Kieran Kelleher on 5/9/06.
//  Copyright (c) 2006 Kieran Kelleher. All rights reserved.
//

package wk.foundation;

import java.math.BigDecimal;
import java.util.Enumeration;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSKeyValueCoding;
import com.webobjects.foundation.NSMutableArray;

/** Static value conversion utilities. Inspired by er.extensions.ERXValueUtilities. */
public class WKValueUtilities {
    private static final Logger log = Logger.getLogger(WKValueUtilities.class);

    public static Integer integerValueWithDefault( Object obj, Number def ) {
        int intValue = intValueWithDefault( obj, def.intValue() );
        return new Integer( intValue );
    }

    public static Number integerValue( Object obj ) {
        Integer result = null;
        if (obj != null) {
            if (obj instanceof Number) {
                result = new Integer( ((Number)obj).intValue() );
            } else if(obj instanceof String) {
                try {
                    String s = ((String)obj).trim(); // Need to trim trailing space
                    if(s.length() > 0)
                        result = new Integer(s);
                } catch(NumberFormatException numberformatexception) {
                    throw new IllegalStateException("Error parsing integer from value : <" + obj + ">");
                }
            } else if (obj instanceof Boolean)
                result = ((Boolean)obj).booleanValue() ? new Integer( 1 ) :  new Integer( 0 );
        }
        return result;

    }

    public static BigDecimal bigDecimalNumber(Object obj){
        try {
            return (BigDecimal) new BigDecimalConverter().valueOf(obj);
        } catch (ValueConverterException e) {
            throw new RuntimeException("Cannot convert " + obj + " to BigDecimal", e);
        }
    }

    /**
     * @param originalArray - array of objects such as strings and booleans for example
     * @return array of integer values. Will throw error if any value cannot be coerced.
     */
    public static NSArray arrayOfCoercedIntegers(NSArray originalArray){
        NSMutableArray coercedArray = new NSMutableArray();
        for (java.util.Enumeration origArrayEnumerator = originalArray.objectEnumerator(); origArrayEnumerator.hasMoreElements();) {
            Object element = origArrayEnumerator.nextElement();
            coercedArray.addObject(integerValue(element));
        } //~ for (java.util.Enumeration origArrayEnumerator = ...
        return coercedArray.immutableClone();
    }

    /**
     * Example: 72 ,45-67   ,
                456789,1234,12
                456789, 45 67 34 123456-123489,
                34, 6789
     * @param aString
     * @return array of words separating by white space (including line feed) and commas.
     */
    public static NSArray wordsFromString(String aString) {
        if (log.isDebugEnabled())
            log.debug("Incoming = " + aString);
        // First replace separator clusters with commas
        String cleanString = aString.replaceAll("(\\s*,\\s*)|(\\s+)", ",");
        if (log.isDebugEnabled())
            log.debug("cleanString = " + cleanString);
        // Remove ending separators if exists since our regex allows separators at end
        String endChompString = StringUtils.chomp(cleanString, ",");
        if (log.isDebugEnabled())
            log.debug("endChompString = " + endChompString);
        NSArray result = NSArray.componentsSeparatedByString(endChompString, ",");
        if (log.isDebugEnabled())
            log.debug("result = " + result);
        return result;
    }

    public static String stringValueWithDefault( Object obj, String def ) {
        String result = null;
        if ( obj == null ) {
            result = def;
        } else {
            if (obj instanceof String ) {
                result = (String)obj;
            } else {
                result = obj.toString();
            }
        }
        return result;
    }

    /**
     * Basic utility method for reading <code>int</code> values. The current
     * implementation tests if the object is an instance of
     * a String, Number and Boolean. Booleans are 1 if they equal
     * <code>true</code>. The default value is used if
     * the object is null or the boolean value is false.
     * @param obj object to be evaluated
     * @param def default value if object is null
     * @return int evaluation of the given object
     */
    public static int intValueWithDefault(Object obj, int def) {
        int value = def;
        if (obj != null) {
            if (obj instanceof Number) {
                value = ((Number)obj).intValue();
            } else if(obj instanceof String) {
                try {
                    String s = ((String)obj).trim(); // Need to trim trailing space
                    if(s.length() > 0)
                        value = Integer.parseInt(s);
                } catch(NumberFormatException numberformatexception) {
                    throw new IllegalStateException("Error parsing integer from value : <" + obj + ">");
                }
            } else if (obj instanceof Boolean)
                value = ((Boolean)obj).booleanValue() ? 1 : def;
        } else {
            value = def;
        }
        return value;
    }


    /**
     * @return a String suitable for console out. Each property is on a separate line
     */
    public static String systemPropertiesAsDumpString() {
        StringBuilder builder = new StringBuilder();
        for (java.util.Enumeration<String> properties = (Enumeration<String>) System.getProperties().propertyNames(); properties
                .hasMoreElements();) {
            String key = properties.nextElement();
            builder.append(key);
            builder.append(" = ");
            builder.append(System.getProperty(key));
            builder.append("\n");

        } //~ for (java.util.Enumeration properties = ...
        return builder.toString();
    }

    /**
     * @param object
     * @return the toString of an object if not null, otherwise "" if null or NSKeyValueCoding.Null
     */
    public static String toString(Object object){
        if (object == null || object instanceof NSKeyValueCoding.Null) {
            return "";
        } else {
            return object.toString();
        } //~ if (object == null)
    }

    /**
     * @param object
     * @return true if null, {@see NSKeyValueCoding.Null} or result of apache lang StringUtils.isBlank returns
     */
    public static boolean isBlank(Object object) {
        if (object == null || object instanceof NSKeyValueCoding.Null) {
            return true;
        } else if (object instanceof String){
            return StringUtils.isBlank((String)object);
        } else {
            return false;
        } //~ if (object == null || object instanceof NSKeyValueCoding.Null)
    }




}
