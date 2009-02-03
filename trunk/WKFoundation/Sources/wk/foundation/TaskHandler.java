//
//  TaskHandler.java
//  cheetah
//
//  Created by Kieran Kelleher on 3/7/06.
//  Copyright 2006 Kieran Kelleher. All rights reserved.
//

package wk.foundation;

import java.util.concurrent.Callable;

import com.webobjects.foundation.NSDictionary;

/** An interface for handling automated task execution
    of CTScheduledTaskEvent entities.*/
public interface TaskHandler extends Task, Callable {

    /** Set the taskParams dictionary. The handler implementing
    this interface will expect certain keys for the
    task parameters. */
    public void setParams( NSDictionary taskParams );
    
    /** @return the taskParams dictionary */
    public NSDictionary params();

    
    
}
