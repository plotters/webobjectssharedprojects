package wk.foundation;

/**
 * @author kieran
 * An exception for Value Conversion failures.
 */
public class ValueConverterException extends Exception {

    public ValueConverterException() { }

    public ValueConverterException(String message) {
        super(message);
    }

    public ValueConverterException(Throwable cause) {
        super(cause);
    }

    public ValueConverterException(String message, Throwable cause) {
        super(message, cause);
    }

}
