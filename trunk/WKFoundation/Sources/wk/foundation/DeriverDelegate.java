package wk.foundation;

import com.webobjects.foundation.NSKeyValueCoding;

/**
 * @author kieran
 * This interface can be used to customize the retrieval of custom data from objects that don't already
 * provide the data we beed. For example if I have a dataObject that has a 5-digit zip field and a 4-digit
 * plus4 field, but no complete zipCode field, I could use a class that implements this interface to derive
 * a zipCode in a DataMappingHelper usage context
 * There is a risk of infinite recursion when passing the DataMapper itself to this delegate. This would
 * happen if the key being used for the derived delegate was itself called on the data mapper from
 * inside the delegate.
 */
public interface DeriverDelegate {
    /** param dataObject a dataObject from which the result will be derived */
    public void setDataMappingHelper(NSKeyValueCoding dataMappingHelper);

    /**
     * @return the value derived from dataObject
     */
    public Object derivedValue();
}
