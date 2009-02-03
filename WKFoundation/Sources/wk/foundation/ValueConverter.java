package wk.foundation;


/**
 * @author kieran
 * An interface for utility classes that attempt to convert values to a certain class type.
 * For flexibility by convention, subclasses should be named <TargetClassName>Converter.
 * For example BooleanConverter.
 */
public interface ValueConverter {
    /**
     * @param aValue
     * @return an object of the desired type
     */
    public Object valueOf(Object aValue) throws ValueConverterException;
}
