package wk.foundation;

/**
 * A ValueConverter that does nothing!
 * It simply returns the value it is passed
 *
 * @author kieran
 *
 */
public class DumbValueConverter implements ValueConverter {


	public Object valueOf(Object value) throws ValueConverterException {
		return value;
	}

}
