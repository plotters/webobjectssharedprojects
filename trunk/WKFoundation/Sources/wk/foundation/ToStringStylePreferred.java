package wk.foundation;

import org.apache.commons.lang.builder.ToStringStyle;

/**
 * A ToStringStyle that puts spaces in appropriate places so that the output wordwraps
 * and is easier to read in console and in HTML debug components.
 *
 * @author kieran
 *
 */
public final class ToStringStylePreferred extends ToStringStyle {

	public static ToStringStylePreferred SHARED_INSTANCE = new ToStringStylePreferred();

	public ToStringStylePreferred() {
		super();
		this.setFieldSeparator(", ");
		this.setContentStart(" [");
	}



}
