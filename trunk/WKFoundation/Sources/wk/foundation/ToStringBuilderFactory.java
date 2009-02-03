package wk.foundation;

import org.apache.commons.lang.builder.ToStringBuilder;

public class ToStringBuilderFactory {
	public static ToStringBuilder createPreferredToStringBuilderStyle(Object object) {
		return new ToStringBuilder(object,new ToStringStylePreferred());
	}
}
