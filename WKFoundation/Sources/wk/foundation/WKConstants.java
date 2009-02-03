package wk.foundation;
//
//  WKConstants.java
//  cheetah
//
//  Created by Kieran Kelleher on 1/2/06.
//  Copyright 2006 Kieran Kelleher. All rights reserved.
//

import java.math.BigDecimal;

public class WKConstants {
    public static final int MONEY_SCALE = 7;
    public static final BigDecimal ZERO_BIG_DECIMAL = BigDecimal.valueOf( 0L, MONEY_SCALE );
    
    
    private WKConstants() {
        
    }
}
