package wk.foundation;

import org.apache.commons.lang.StringUtils;

import com.webobjects.foundation.NSKeyValueCoding;

public class ZipCodeDeriver implements DeriverDelegate {
    protected NSKeyValueCoding dataObject;

    public ZipCodeDeriver() {
		super();
		_zip5AttributeKey = "zip5";
		_zipPlus4AttributeKey = "zipPlus4";
	}

	public Object derivedValue() {
        return zip();
    }

    public void setDataMappingHelper(NSKeyValueCoding dataMappingHelper) {
        this.dataObject = dataMappingHelper;
    }

    /**
     * WARNING : Be aware of the danger of recursion
     * @return the zip code which is the zip5 and zipPlus4 combined
     */
    private String zip() {
        String zip5 = (String) dataObject.valueForKey(_zip5AttributeKey);
        String zipPlus4 = (String) dataObject.valueForKey(_zipPlus4AttributeKey);
        if (zip5 == null) {
            return null;
        } // ~ if (zip5 == null)

        // Allow for missing leading zeroes
        if (zip5.length() < 5) {
			zip5 = StringUtils.leftPad(zip5, 5, '0');
		} //~ if (zip5.length() < 5)

        if (zipPlus4 == null) {
            return zip5;
        } // ~ if (zipPlus4 == null)

        if (zipPlus4.length() < 4) {
			zipPlus4 = StringUtils.leftPad(zipPlus4, 4, '0');
		} //~ if (zipPlus4.length() < 4)

        return zip5 + "-" + zipPlus4;
    }

    protected String _zip5AttributeKey;

	/** @return the key for the zip5 in the data map */
	public String zip5AttributeKey() {
		return _zip5AttributeKey;
	}

	/** param zip5AttributeKey the key for the zip5 in the data map */
	public void setZip5AttributeKey(String zip5AttributeKey){
		_zip5AttributeKey = zip5AttributeKey;
	}

	protected String _zipPlus4AttributeKey;

	/** @return the key for the zip-plus-4 attribute in the data map */
	public String zipPlus4AttributeKey() {
		return _zipPlus4AttributeKey;
	}

	/** param zipPlus4AttributeKey the key for the zip-plus-4 attribute in the data map */
	public void setZipPlus4AttributeKey(String zipPlus4AttributeKey){
		_zipPlus4AttributeKey = zipPlus4AttributeKey;
	}

}
