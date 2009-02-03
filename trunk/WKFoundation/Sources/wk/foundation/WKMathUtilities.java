//
//  WKMathUtilities.java
//  cheetah
//
//  Created by Kieran Kelleher on 8/28/06.
//  Copyright 2006 Kieran Kelleher. All rights reserved.
//

package wk.foundation;

/** A collection of math utilities */
public class WKMathUtilities {

    public final Double REASONABLE_TOLERANCE = new Double(0.001d);

    /** @return true if number1 and number2 are with tolerance */
    public static boolean isWithinTolerance( Number number1, Number number2, Number tolerance ) {
        double delta = Math.abs( number1.doubleValue() - number2.doubleValue() );
        return ( delta - Math.abs( tolerance.doubleValue() ) > 0 ? false : true );
    }


}
