package wk.foundation;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSDictionary;
import com.webobjects.foundation.NSKeyValueCoding;
import com.webobjects.foundation.NSKeyValueCodingAdditions;
import com.webobjects.foundation.NSMutableDictionary;

/**
 * A generic class for mapping a data object's keypath values to
 *         simple keys. This class is ideal for generating flat data from an
 *         object graph (an object and its related objects). The data object
 *         must support keyvaluecoding (for now) and this covers NSDictionaries,
 *         EOEnterpriseObjects, etc. This class is useful for mapping imported
 *         flat text file records to the attributes of an internal object or for
 *         pulling values from keys and keypaths on an internal object to create
 *         flat file output. Initialize fieldMap with appropriate constructor or
 *         using setFieldMap after instance creation to use a different field to
 *         attribute map.
 *
 *         By simply setting a fieldmap dictionary where its
 *         keys are say INTERNAL keys (like EO attribute keys) and its
 *         values are EXTERNAL keys or keypaths (like field headings in an import file for
 *         example) and then setting a data record dictionary having keys that
 *         match the EXTERNAL keys and data values, we can simple call
 *         valueForKey( INTERNAL_KEY ) to retrieve the value from the data
 *         record dictionary using the map. Also if certain INTERNAL keys have
 *         no direct EXTERNAL key mapping, then a method can be implemented
 *         which will of course respond to valueForKey to return the derived
 *         value. For example a method named zip() could be used to combine two
 *         external fields named Zip Code and Zip+4.
 * @author kieran
 */
public class DataMappingHelper implements NSKeyValueCoding, NSKeyValueCoding.ErrorHandling {
    public DataMappingHelper() {
    }

    public DataMappingHelper(NSDictionary fieldMap) {
        _fieldMap = fieldMap;
    }

    public DataMappingHelper(NSArray sourceKeys, NSArray targetKeys) {
    	_fieldMap = new NSDictionary(sourceKeys, targetKeys);
    }

    protected NSKeyValueCodingAdditions _dataObject;

    /** @return the current data import record */
    public NSKeyValueCodingAdditions dataObject() {
        return _dataObject;
    }

    /** param dataImportRecord the current data import record */
    public void setDataObject(NSKeyValueCodingAdditions dataImportRecord) {
        // ENHANCEME : have a flag for a cache results feature whereby computed values are cached in a dictionary
        //  and the cache is reset whenever a new dataRecord is pushed in. This would speed up output operations
        // where output rows are inside an inner loop.
        _dataObject = dataImportRecord;
    }

    protected NSDictionary _valueConverters;

    /**
     * @return a dictionary of optional ValueConvertor objects. The keys in this
     *         dictionary are the same keys as used in the fieldmap. If an entry
     *         exists it is invoked <em>after</em> the value has been
     *         retrieved from the dataObject and before it is returned. This is
     *         useful for converting text values to the correct destination
     *         class types for the values.
     */
    @SuppressWarnings("unchecked")
    public NSDictionary valueConverters() {
        if (_valueConverters == null) {
            _valueConverters = NSDictionary.EmptyDictionary;
        } // ~ if (_valueConverters == null)
        return _valueConverters;
    }

    /**
     * @param aDictionary
     *            of optional ValueConvertor objects. The keys in this
     *            dictionary are the same keys as used in the fieldmap. If an
     *            entry exists it is invoked <em>after</em> the value has been
     *            retrieved from the dataObject and before it is returned. This
     *            is useful for converting text values to the correct
     *            destination class types for the values.
     */
    public void setValueConverters(NSDictionary aDictionary) {
        _valueConverters = aDictionary;
    }

    protected NSDictionary _derivedValueDelegates;

    /**
     * @return a dictionary of custom delegate objects that return a value that
     *         is derived from the dataObject.
     */
    public NSDictionary derivedValueDelegates() {
        if (_derivedValueDelegates == null) {
            _derivedValueDelegates = NSDictionary.EmptyDictionary;
        } //~ if (_derivedValueDelegates == null)
        return _derivedValueDelegates;
    }

    /**
     * param derivedValueDelegates a dictionary of custom delegate objects that
     * return a value that is derived from the dataObject.
     */
    public void setDerivedValueDelegates(NSDictionary derivedValueDelegates) {
        _derivedValueDelegates = derivedValueDelegates;
    }

    private NSDictionary _fieldMap;

    /**
     * @return the fieldmap that is used for data value mapping. The keys of the
     *         fieldmap are the keys used on the DataMappingHelper object and
     *         the values of the fieldmap are corresponding keys or keypaths to
     *         get the values from the dataObject that is wrapped by the
     *         DataMappingHelper.
     */
    public NSDictionary fieldMap() {
        if (_fieldMap == null) {
            // Lazily create the default map (CTDataRecord -> Experian)
            // This default is for legacy code support
            // In general you should always provide your map when initializing
            // the object
            NSMutableDictionary mutableMap = new NSMutableDictionary();

            // For now just put in default map values for the fields we want
            mutableMap.takeValueForKey("FULL NAME", "fullName");
            mutableMap.takeValueForKey("NAME PREFIX", "namePrefix");
            mutableMap.takeValueForKey("FIRST NAME", "firstName");
            mutableMap.takeValueForKey("MIDDLE INITIAL", "middleName");
            mutableMap.takeValueForKey("LAST NAME", "lastName");
            mutableMap.takeValueForKey("NAME SUFFIX", "nameSuffix");
            mutableMap.takeValueForKey("SECONDARY ADDRESS", "street2");
            mutableMap.takeValueForKey("PRIMARY ADDRESS", "street1");
            mutableMap.takeValueForKey("CITY", "city");
            mutableMap.takeValueForKey("STATE", "state");
            mutableMap.takeValueForKey("ZIP CODE", "zip5");
            mutableMap.takeValueForKey("ZIP+4", "zipPlus4");
            mutableMap.takeValueForKey("KEY CODE", "keycode");
            mutableMap.takeValueForKey("COUNTY NAME", "countyName");
            mutableMap.takeValueForKey("STATE/COUNTY CODE", "countyCode");
            mutableMap.takeValueForKey("ORIGINAL PRIORITY CODE", "priority");

            _fieldMap = mutableMap.immutableClone();
        }

        return _fieldMap;
    }

//    /**
//     * @return the zip code which is the zip5 and zipPlus4 combined
//     */
//    public String zip() {
//        String zip5 = (String) fieldValueForKey("zip5");
//        String zipPlus4 = (String) fieldValueForKey("zipPlus4");
//        if (zip5 == null) {
//            return null;
//        } // ~ if (zip5 == null)
//        if (zipPlus4 == null) {
//            return zip5;
//        } // ~ if (zipPlus4 == null)
//        return zip5 + "-" + zipPlus4;
//    }

    /**
     * @param key
     * @return the value using the field map to translate
     */
    public Object fieldValueForKey(String key) {
        Object value;
        // First check if this is a derived key
        DeriverDelegate deriver = (DeriverDelegate) derivedValueDelegates().valueForKey(key);
        if (deriver == null) {
            String fieldNameForKey = (String) fieldMap().valueForKey(key);
            value = dataObject().valueForKey(fieldNameForKey);
        } else {
            // Deriver exists
            deriver.setDataMappingHelper(this);
            value = deriver.derivedValue();
        } //~ if (deriver == null)

        // If empty string, make it a null
        if (StringUtils.isBlank((String) value))
            value = null;

        // Now check for an optional ValueConverter
        ValueConverter valueConverter = (ValueConverter) valueConverters().valueForKey(key);
        if (valueConverter != null) {
            try {
                value = valueConverter.valueOf(value);
            } catch (ValueConverterException exception) {
                throw new RuntimeException(exception.getMessage());
            }
        } // ~ if (valueConverter != null)
        return value;
    }

    // NSKeyValueCoding
    public Object valueForKey(String key) {
        return NSKeyValueCoding.DefaultImplementation.valueForKey(this, key);
    }

    // NSKeyValueCoding
    public void takeValueForKey(Object value, String key) {
        NSKeyValueCoding.DefaultImplementation.takeValueForKey(this, value, key);
    }

    /**
     * @see com.webobjects.foundation.NSKeyValueCoding$ErrorHandling#handleQueryWithUnboundKey(java.lang.String)
     * @override
     * @return the result of the 1 to 1 value from the field map We use this
     *         builtin interface method in a clever way. Anytime a valueForKey
     *         is called on this class a custom method will get the priority of
     *         derived, transformation or otherwise custom logic is required
     */
    public Object handleQueryWithUnboundKey(String key) {
        return fieldValueForKey(key);
    }

    // NSKeyValueCoding.ErrorHandling
    public void handleTakeValueForUnboundKey(Object value, String key) {
        NSKeyValueCoding.DefaultImplementation.handleTakeValueForUnboundKey(this, value, key);
    }

    // NSKeyValueCoding.ErrorHandling
    public void unableToSetNullForKey(String key) {
        NSKeyValueCoding.DefaultImplementation.unableToSetNullForKey(this, key);

    }

    @Override
	public String toString() {
    	ToStringBuilder b = new ToStringBuilder(this);
    	b.append("fieldMap", _fieldMap);
    	return b.toString();
    }

}
