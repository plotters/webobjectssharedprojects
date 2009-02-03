//
//  StopWatch.java
//  cheetah
//
//  Created by Kieran Kelleher on 5/17/05.
//  Copyright 2005 Kieran Kelleher. All rights reserved.
//

package wk.foundation;

import java.util.Date;

public class StopWatch extends Date {
    protected final static int STOPPED = 1;
    protected final static int RUNNING = 2;

    long elapsedTime = 0;
    long startTime = 0;
    int state = STOPPED;
    
    public StopWatch() {
	super();
    }
    
    public void start() {
	if ( state == STOPPED ) {
	    state = RUNNING;
	    startTime = getTime();
	}
    }
    
    public long elapsedTime() {
	if ( state == RUNNING ) {
	    return (new Date()).getTime() - startTime + elapsedTime;
	}
	
	// We are stopped, so just return elapsed time
	return elapsedTime;
    }
    
    public void stop() {
	if ( state == RUNNING ) {
	    // Store the elapsed time
	    elapsedTime = elapsedTime();
	    state = STOPPED;
	}
    }
    
    public String toString() {
	return ( state == RUNNING ? "Running - elapsed time = " + elapsedTime() / 1000 : "Stopped - elapsed time = " + elapsedTime() / 1000 );
    }
    
    public int state() {
	return state;
    }
    
    public void reset() {
	elapsedTime = 0;
	startTime = getTime();
    }
}
