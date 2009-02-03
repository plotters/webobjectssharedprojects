package wk.foundation;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;

import com.webobjects.foundation.NSValidation;

public class ValidationUtilities {


    /**
     * Same as {@link #validateZip(Object, String)} except exception has object and key data encapsulated
     * @param aValue
     * @param fieldName
     * @param object
     * @param key
     * @return valid zip
     * @throws NSValidation.ValidationException
     */
    public static String validateZip(Object aValue, String fieldName, Object object, String key)  throws
    NSValidation.ValidationException {
        try {
            return validateZip(aValue, fieldName);
        } catch (NSValidation.ValidationException e) {
            throw e.exceptionWithObjectAndKey(object, key);
        }
    }

    /**
     * Throws error if blank or not in the right format
     * @param aValue
     * @param fieldName
     * @return valid zip
     * @throws NSValidation.ValidationException
     */
    public static String validateZip(Object aValue, String fieldName) throws
    NSValidation.ValidationException {

        // Regex string: ^\d{5}$|^\d{5}\-\d{4}$
        String zipRegex = "^\\d{5}$|^\\d{5}\\-\\d{4}$";

        String zip = ObjectUtils.toString(aValue, null);
        zip = StringUtils.stripToNull(zip);

        if (StringUtils.isBlank(zip)) {
            String requiredMessage = (fieldName == null ? "Zip code is required." : fieldName + " is required.");
            throw new NSValidation.ValidationException(requiredMessage);
        } //~ if (StringUtils.isBlank(zip))

        if (zip.matches(zipRegex)) {
            return zip;
        } else {
            String errorMessage;
            if (fieldName == null) {
                errorMessage = "Must be entered as 99999 or 99999-9999.";
            } else {
                errorMessage = fieldName + " must be entered as 99999 or 99999-9999.";
            }
            throw new NSValidation.ValidationException(errorMessage);
        } //~ if (zip.matches(zipRegex))
    }

    public static boolean isBlankString(Object aValue) {
        return StringUtils.isBlank(ObjectUtils.toString(aValue, null));
    }

    /** Checks that an object is a non-null String
        @param aValue an object to be checked.
        @return boolean, true if it is a non-null String, false otherwise */
    public static boolean isNonBlankString(Object aValue) {
        return !StringUtils.isBlank(ObjectUtils.toString(aValue, null));
    }

    /**
     * Checks that a String contains at least one non-white space character
     * @param aValue
     * @param fieldName
     * @return the valid String with leading and trailing whitespace trimmed
     * @throws NSValidation.ValidationException
     */
    public static String validateStringNotBlank(Object aValue, String fieldName) throws NSValidation.ValidationException {
        String testValue = ObjectUtils.toString(aValue, null);
        if (StringUtils.isBlank(testValue)) {
            throw new NSValidation.ValidationException( fieldName + " is required." );
        }
        return StringUtils.strip(testValue);
    }

}
