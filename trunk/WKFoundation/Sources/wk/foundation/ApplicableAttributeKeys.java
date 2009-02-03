package wk.foundation;
//
//  ApplicableAttributeKeys.java
//  cheetah
//
//  Created by Kieran Kelleher on 1/25/07.
//  Copyright 2007 Kieran Kelleher. All rights reserved.
//

import com.webobjects.foundation.NSArray;

public interface ApplicableAttributeKeys {
    
    /** @return an array of attribute keys applicable to the
        class in context. This is used for Strategy Pattern
        implementation for EnterpriseObjects where many attribute keys
        are present but only a subset of them are applicable to the
        implemented encapsulated strategy. */
    public NSArray applicableAttributeKeys();
    
}
