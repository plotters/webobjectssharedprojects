package wk.foundation;

import java.math.BigDecimal;

public class BigDecimalConverter implements ValueConverter {

    public Object valueOf(Object value) throws ValueConverterException {
        BigDecimal result = null;
        if (value != null) {
            try {
                if (value instanceof BigDecimal) {
                    return value;
                } else {
                    return new BigDecimal(value.toString());
                } //~ if (value instanceof BigDecimal)
            } catch (Exception e) {
                throw new ValueConverterException("Cannot convert " + value + " to BigDecimal", e);
            }
        } //~ if (value != null)

        return null;
    }

}
